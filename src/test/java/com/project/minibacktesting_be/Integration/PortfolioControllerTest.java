package com.project.minibacktesting_be.Integration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.minibacktesting_be.dto.backtesting.BacktestingRequestDto;
import com.project.minibacktesting_be.dto.portfolio.PortfolioCompareDto;
import com.project.minibacktesting_be.dto.portfolio.PortfolioDetailsResponseDto;
import com.project.minibacktesting_be.dto.portfolio.PortfolioMyBestDto;
import com.project.minibacktesting_be.service.PortfolioService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PortfolioControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PortfolioService portfolioService;


    String token = JWT.create()
            .withIssuer("sparta")
            .withClaim("USER_NAME", "testNick@email.com")
            // 토큰 만료 일시 = 현재 시간 + 토큰 유효기간)
            .withClaim("EXPIRED_DATE", new Date(System.currentTimeMillis() +60*60*24*3))
            .sign(Algorithm.HMAC256("jwt_secret_!@#$%"));

    String portId;


    @Test
    @Order(1)
    @DisplayName("포트폴리오 저장하기")
    @Transactional
    void savePortfolio(){

        List<String> stockList = new ArrayList<>(Arrays.asList("LG디스플레이"));
        List<Integer> ratioList = new ArrayList<>(Arrays.asList(100));
        BacktestingRequestDto backtestingRequestDto =
                new BacktestingRequestDto(LocalDate.parse("2011-01-01"),
                        LocalDate.parse("2021-12-01"),
                        1000L,
                        stockList,
                        ratioList,
                        0);


        byte[] result =  webTestClient.post().uri("/portfolios")
                .header(HttpHeaders.AUTHORIZATION, "BEARER "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(backtestingRequestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.portId").exists()
                .returnResult().getResponseBodyContent();

        String Charsets = new String(result, StandardCharsets.UTF_8);
        String[] results = Charsets.split(":");

        portId = results[1].
                replaceAll("\\}", "");
    }


    @Test
    @Order(2)
    @DisplayName("내 포트폴리오 전체 불러오기")
    @Transactional
    void getAllMyPortfolio(){

        webTestClient.get().uri("/users/portfolios")
                .header(HttpHeaders.AUTHORIZATION, "BEARER "+token)
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    @Order(3)
    @DisplayName("포트폴리오 상세보기")
    @Transactional
    void getPortfolio(){

        PortfolioDetailsResponseDto portfolioDetailsResponseDto =
                portfolioService.getPortfolio(Long.valueOf(portId));

        webTestClient.get().uri("/portfolios/{portId}", portId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @Order(4)
    @DisplayName("포트폴리오 비교하기")
    @Transactional
    void comparePortfolio(){

        List<Long> portIdList =
                new ArrayList<>(Arrays.asList(Long.valueOf(portId)));

        PortfolioCompareDto.Request requestDto =
                new PortfolioCompareDto.Request(portIdList);

        webTestClient.post().uri("/portfolios/comparison")
                .header(HttpHeaders.AUTHORIZATION, "BEARER "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    @Order(5)
    @DisplayName("포트폴리오 자랑하기")
    @Transactional
    void boastPortfolio(){

        PortfolioMyBestDto.Request requestDto =
                new PortfolioMyBestDto.Request(Long.valueOf(portId), true);

        webTestClient.post().uri("/portfolios/boast")
                .header(HttpHeaders.AUTHORIZATION, "BEARER "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.myBest").isEqualTo(requestDto.isMyBest());
    }

    @Test
    @Order(6)
    @DisplayName("포트폴리오 삭제하기")
    @Transactional
    void deleteComment(){

        webTestClient.delete()
                .uri("/portfolios/{portId}", portId)
                .header(HttpHeaders.AUTHORIZATION, "BEARER "+token)
                .exchange()
                .expectStatus().isOk();

    }


}
