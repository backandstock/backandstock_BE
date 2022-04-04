package com.project.minibacktesting_be.Integration;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import com.project.minibacktesting_be.dto.user.SignupDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    @Autowired
    private WebTestClient webTestClient;


    @Test
    @Order(1)
    @DisplayName("회원가입")
    void registerUser() {
        SignupDto requestDto =
                new SignupDto("userTest@gmail.com",
                        "userTest",
                        "1234");

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isOk();

    }

    String token = JWT.create()
            .withIssuer("sparta")
            .withClaim("USER_NAME", "userTest@gmail.com")
            // 토큰 만료 일시 = 현재 시간 + 토큰 유효기간)
            .withClaim("EXPIRED_DATE", new Date(System.currentTimeMillis() +60*60*24*3))
            .sign(Algorithm.HMAC256("jwt_secret_!@#$%"));

    @Test
    @Order(2)
    @DisplayName("회원정보 수정")
    void updateUser() {
        webTestClient.put().uri(uriBuilder -> uriBuilder.
                path("/users").
                queryParam("nickname" ,"{nickname}").
                build("userUpdateTest"))
                .header(HttpHeaders.AUTHORIZATION, "BEARER " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    @Order(3)
    @DisplayName("회원로그인 정보")
    void userLoginCheck() {
        webTestClient.post().uri("/users/info")
                .header(HttpHeaders.AUTHORIZATION, "BEARER "+token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.nickname")
                .isEqualTo("userUpdateTest");

    }


    @Test
    @Order(4)
    @DisplayName("회원탈퇴")
    void deleteUser() {
        webTestClient.delete().uri("/users")
                .header(HttpHeaders.AUTHORIZATION, "BEARER "+token)
                .exchange()
                .expectStatus().isOk();

    }
}

