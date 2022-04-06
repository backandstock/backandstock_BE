package com.project.minibacktesting_be.controller;


import com.project.minibacktesting_be.dto.community.CommunityPortResponseDto;
import com.project.minibacktesting_be.dto.community.TopFiveResponseDto;
import com.project.minibacktesting_be.dto.likes.LikesRequestDto;
import com.project.minibacktesting_be.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;

    // 주식 top 5 조회 하기
    @GetMapping("/stocks/topfive")
    public List<TopFiveResponseDto> getTop5Info(){

        log.info("top5 주식정보 조회");
        return communityService.getTopFive();
    }


    // 자랑하기 포트폴리오 가져오기
    @GetMapping("/portfolios/boast")
    public List<CommunityPortResponseDto> getCommunityPorts(@RequestParam("page") Integer page,
                                                            @RequestParam("size") Integer size){
        log.info("자랑한 포트폴리오 가져오기.");
        return communityService.getCommnunityPorts(page, size);
    }

    // 시간 옵션별로 포트폴리오 가져오기
    @GetMapping("/portfolios/latest")
    public List<CommunityPortResponseDto> getRecentCommunityPort(
            @RequestParam("option") String option,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size){
        log.info("옵션별로 포트폴리오 가져오기 option:{}", option);
        return communityService.getRecentCommnunityPorts(option ,page, size);
    }

}