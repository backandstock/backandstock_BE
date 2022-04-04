package com.project.minibacktesting_be.Integration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.minibacktesting_be.repository.PortfolioRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LikesControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    String token = JWT.create()
            .withIssuer("sparta")
            .withClaim("USER_NAME", "testNick@email.com")
            // 토큰 만료 일시 = 현재 시간 + 토큰 유효기간)
            .withClaim("EXPIRED_DATE",
                    new Date(System.currentTimeMillis() +60*60*24*3))
            .sign(Algorithm.HMAC256("jwt_secret_!@#$%"));

    Long portId = 36L;

    @Test
    @Order(1)
    @DisplayName("좋아요")
    @Transactional
    void postLikes(){

        webTestClient.post().uri("/portfolios/{portId}/likes", portId)
                .header(HttpHeaders.AUTHORIZATION, "BEARER "+token)
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    @Order(2)
    @DisplayName("좋아요 취소")
    @Transactional
    void deleteLikes(){

        webTestClient.delete().uri("/portfolios/{portId}/likes", portId)
                .header(HttpHeaders.AUTHORIZATION, "BEARER "+token)
                .exchange()
                .expectStatus().isOk();
    }


}
