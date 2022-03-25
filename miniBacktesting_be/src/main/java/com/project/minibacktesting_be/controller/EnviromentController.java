package com.project.minibacktesting_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class EnviromentController {

    private final Environment environment;

    @GetMapping("/")
    public String checkProfiles(){
        // jar 파일 실행시에 환경변수 설정을 가져옴
        List<String> profile = Arrays.asList(environment.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real", "dev");
        String defaultProfile = profile.isEmpty() ? "default" : profile.get(0);

        return profile.stream()
                .filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfile);
    }
}
