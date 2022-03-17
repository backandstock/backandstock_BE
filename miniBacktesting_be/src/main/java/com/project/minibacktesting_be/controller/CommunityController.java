package com.project.minibacktesting_be.controller;


import com.project.minibacktesting_be.dto.community.CommunityPortResponseDto;
import com.project.minibacktesting_be.dto.community.TopFiveResponseDto;
import com.project.minibacktesting_be.dto.likes.LikesRequestDto;
import com.project.minibacktesting_be.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;

    // 주식 top 5 조회 하기
    @GetMapping("/community/topFive")
    public List<TopFiveResponseDto> getTop5Info(){
        return communityService.getTopFive();
    }


    // 자랑하기 포트폴리오 가져오기
    @GetMapping("/community")
    public List<CommunityPortResponseDto> getCommunityPorts(@RequestParam("page") Integer page,
                                                            @RequestParam("size") Integer size){
        return communityService.getCommnunityPorts(page, size);
    }

    // 시간 옵션별로 포트폴리오 가져오기
    @GetMapping("/community/recent")
    public List<CommunityPortResponseDto> getRecentCommunityPort(
            @RequestParam("option") String option,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size){
        return communityService.getRecentCommnunityPorts(option ,page, size);
    }

}