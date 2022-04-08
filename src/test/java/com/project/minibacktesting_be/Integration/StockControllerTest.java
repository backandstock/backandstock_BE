package com.project.minibacktesting_be.Integration;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.minibacktesting_be.dto.StockSearchResponseDto;
import com.project.minibacktesting_be.dto.backtesting.BacktestingRequestDto;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StockControllerTest {


    @Autowired
    private WebTestClient webTestClient;


    String token = JWT.create()
            .withIssuer("sparta")
            .withClaim("USER_NAME", "testNick@email.com")
            // 토큰 만료 일시 = 현재 시간 + 토큰 유효기간)
            .withClaim("EXPIRED_DATE", new Date(System.currentTimeMillis() +60*60*24*3))
            .sign(Algorithm.HMAC256("jwt_secret_!@#$%"));


    @Test
    @Order(1)
    @DisplayName("백테스팅 계산하기")
    void backtestingCalculation(){

        List<String> stockList = new ArrayList<>(Arrays.asList("LG디스플레이"));
        List<Integer> ratioList = new ArrayList<>(Arrays.asList(100));
        BacktestingRequestDto backtestingRequestDto =
                new BacktestingRequestDto(LocalDate.parse("2011-01-01"),
                        LocalDate.parse("2021-12-01"),
                        1000L,
                        stockList,
                        ratioList,
                        0);

        webTestClient.post().uri("/stocks")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(backtestingRequestDto)
                .exchange()
                .expectStatus().isOk();


    }


    @Test
    @Order(2)
    @DisplayName("주식 조회하기")
    @Transactional
    void searchStock() {

        StockSearchResponseDto stockSearchResponseDto1 =
                new StockSearchResponseDto("윈텍","320000");

        StockSearchResponseDto stockSearchResponseDto2 =
                new StockSearchResponseDto("프로이천","321260");

        List<StockSearchResponseDto> stockSearchResponseDtoList = new ArrayList<>();
        stockSearchResponseDtoList.add(stockSearchResponseDto1);
        stockSearchResponseDtoList.add(stockSearchResponseDto2);



//https://www.baeldung.com/webflux-webclient-parameters
        webTestClient.get().uri(uriBuilder -> uriBuilder.
                        path("/stocks").
                        queryParam("keyword" ,"{keyword}").
                        queryParam("type", "{type}").
                        build("32", "code"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StockSearchResponseDto.class)
                .hasSize(10)
                .consumeWith(list -> {
                    assertEquals(list.getResponseBody().get(0).getStockCode(),
                            stockSearchResponseDtoList.get(0).getStockCode());
                    assertEquals(list.getResponseBody().get(0).getStockName(),
                            stockSearchResponseDtoList.get(0).getStockName());
                });


    }

}
