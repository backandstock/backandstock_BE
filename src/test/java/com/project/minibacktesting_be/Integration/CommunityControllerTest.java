package com.project.minibacktesting_be.Integration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.minibacktesting_be.dto.comment.GetCommentsResponseDto;
import com.project.minibacktesting_be.dto.community.CommunityPortResponseDto;
import com.project.minibacktesting_be.dto.community.TopFiveResponseDto;
import com.project.minibacktesting_be.model.Comment;
import com.project.minibacktesting_be.model.Portfolio;
import com.project.minibacktesting_be.model.Stock;
import com.project.minibacktesting_be.repository.PortfolioRepository;
import com.project.minibacktesting_be.repository.StockRepository;
import com.project.minibacktesting_be.service.CommunityService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommunityControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private CommunityService communityService;

    String token = JWT.create()
            .withIssuer("sparta")
            .withClaim("USER_NAME", "testNick@email.com")
            // 토큰 만료 일시 = 현재 시간 + 토큰 유효기간)
            .withClaim("EXPIRED_DATE",
                    new Date(System.currentTimeMillis() +60*60*24*3))
            .sign(Algorithm.HMAC256("jwt_secret_!@#$%"));



    @Test
    @Order(1)
    @DisplayName("top5 주식정보 가져오기")
    @Transactional
    void getTopFive(){
        LocalDate currentDate = LocalDate.now();
        LocalDate baseDate = currentDate.minusMonths(1);
        LocalDate startDate = baseDate.with(firstDayOfMonth());
        LocalDate endDate = baseDate.with(lastDayOfMonth()).plusDays(1);
        List<Stock> stocks = stockRepository.
                findTop5ByMarketAndCloseDateBetweenOrderByYieldPctDesc("kospi",
                        startDate, endDate);



        webTestClient.get().uri("/stocks/topfive")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TopFiveResponseDto.class)
                .consumeWith(list ->{
                    assertEquals(list.getResponseBody().get(0).getOption(),
                           "kospi");
                    assertEquals(list.getResponseBody().get(0).getStockNames().get(0),
                            stocks.get(0).getStockName());
                    assertEquals(list.getResponseBody().get(0).getStockCodes().get(0),
                            stocks.get(0).getStockCode());
                    assertEquals(list.getResponseBody().get(0).getCloses().get(0),
                            stocks.get(0).getClose());

                });
    }

    @Test
    @Order(2)
    @DisplayName("자랑하기 포트폴리오 가져오기")
    @Transactional
    void getCommunityPorts(){

        List<CommunityPortResponseDto> responseDtos =
                communityService.getCommnunityPorts(1,5);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.
                        path("/portfolios/boast").
                        queryParam("page" ,"{page}").
                        queryParam("size", "{size}").
                        build("1", "5"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CommunityPortResponseDto.class)
                .consumeWith(list ->{
                    assertEquals(list.getResponseBody().get(0).getCommunityPort().getPortId(),
                            responseDtos.get(0).getCommunityPort().getPortId());


                });
    }


    @Test
    @Order(3)
    @DisplayName("최근 포트폴리오 가져오기")
    @Transactional
    void getRecentPortfolios(){

        List<CommunityPortResponseDto> responseDtos =
                communityService.getRecentCommnunityPorts("all",1,5);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.
                        path("/portfolios/latest").
                        queryParam("option" ,"{option}").
                        queryParam("page" ,"{page}").
                        queryParam("size", "{size}").
                        build("all","1", "5"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CommunityPortResponseDto.class)
                .consumeWith(list ->{
                    assertEquals(list.getResponseBody().get(0).getCommunityPort().getPortId(),
                            responseDtos.get(0).getCommunityPort().getPortId());

                });
    }




}
