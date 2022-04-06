package com.project.minibacktesting_be.Integration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.minibacktesting_be.dto.comment.CommentRequestDto;
import com.project.minibacktesting_be.dto.comment.GetCommentsResponseDto;
import com.project.minibacktesting_be.model.Comment;
import com.project.minibacktesting_be.model.Portfolio;
import com.project.minibacktesting_be.repository.PortfolioRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;


import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PortfolioRepository portfolioRepository;

    String token = JWT.create()
            .withIssuer("sparta")
            .withClaim("USER_NAME", "testNick@email.com")
            // 토큰 만료 일시 = 현재 시간 + 토큰 유효기간)
            .withClaim("EXPIRED_DATE",
                    new Date(System.currentTimeMillis() +60*60*24*3))
            .sign(Algorithm.HMAC256("jwt_secret_!@#$%"));


    String commentId;
    Long portId = 106L;

    @Test
    @Order(1)
    @DisplayName("코멘트 전체 가져오기")
    @Transactional
    void getCommentsByPortId(){


        Optional<Portfolio> portfolio = portfolioRepository.findById(portId);
        List<Comment> commentList = portfolio.get().getComments();

        webTestClient.get().uri("/portfolios/{portId}/comments", portId)
//                .header(HttpHeaders.AUTHORIZATION, "BEARER "+token)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GetCommentsResponseDto.class)
                .consumeWith(list ->{
                    assertEquals(list.getResponseBody().get(0).getCommentId(),
                            commentList.get(0).getId());
                    assertEquals(list.getResponseBody().get(0).getContent(),
                            commentList.get(0).getContent());

                });
    }


    @Test
    @Order(2)
    @DisplayName("코멘트 등록하기")
    @Transactional
    void registerCommentByPortId(){

        CommentRequestDto commentRequestDto =
                new CommentRequestDto("가즈아!");

        byte[] result = webTestClient.post()
                        .uri("/portfolios/{portId}/comments", portId)
                .header(HttpHeaders.AUTHORIZATION, "BEARER "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(commentRequestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.commentId").exists()
                .returnResult().getResponseBodyContent();

        String Charsets = new String(result, StandardCharsets.UTF_8);
        String[] results = Charsets.split(":");

        commentId = results[1].
                replaceAll("\\}", "");
    }


    @Test
    @Order(3)
    @DisplayName("코멘트 수정하기")
    @Transactional
    void updateComment(){


        CommentRequestDto commentRequestDto=
                new CommentRequestDto("수정 가즈아!");

        webTestClient.put()
                .uri("/comments/{commentId}", commentId)
                .header(HttpHeaders.AUTHORIZATION, "BEARER "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(commentRequestDto)
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    @Order(4)
    @DisplayName("대댓글 작성하기")
    @Transactional
    void registerReply(){

        CommentRequestDto commentRequestDto=
                new CommentRequestDto("대댓글 가즈아!");

        webTestClient.post()
                .uri("/comments/{commentId}", commentId)
                .header(HttpHeaders.AUTHORIZATION, "BEARER "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(commentRequestDto)
                .exchange()
                .expectStatus().isOk();

    }




    @Test
    @Order(5)
    @DisplayName("코멘트 삭제하기")
    @Transactional
    void deleteComment(){


        webTestClient.delete()
                .uri("/comments/{commentId}", commentId)
                .header(HttpHeaders.AUTHORIZATION, "BEARER "+token)
                .exchange()
                .expectStatus().isOk();

    }

}
