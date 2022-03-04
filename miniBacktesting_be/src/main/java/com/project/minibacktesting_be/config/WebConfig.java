package com.project.minibacktesting_be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000") // 서버의 자원에 접근할 출처를 명시
//                .allowedOrigins("*")
                .allowedOriginPatterns("*")
                // 허용되는 메소드 지정
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*") // 서버로 요청 시 사용할 수 있는 헤더 명시
//                .exposedHeaders(HttpHeaders.AUTHORIZATION) // 브라우저에 표시할 헤더명 표시
                .exposedHeaders("Authorization")
                .allowCredentials(true); //클라이언트 쿠키 받는거 허용
    }
}