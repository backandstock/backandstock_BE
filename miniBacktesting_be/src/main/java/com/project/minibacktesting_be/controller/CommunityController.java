package com.project.minibacktesting_be.controller;


import com.project.minibacktesting_be.dto.community.TopFiveResponseDto;
import com.project.minibacktesting_be.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;


    // 주식 top 5 조회 하기
    @GetMapping("/community/topFive/{option}")
    public TopFiveResponseDto getTop5Info(@PathVariable String option){
        return communityService.getTopFive(option);
    }

}
