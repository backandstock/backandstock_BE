package com.project.minibacktesting_be.controller;

import com.project.minibacktesting_be.dto.likes.LikesRequestDto;
import com.project.minibacktesting_be.dto.portfolio.PortfolioResponseDto;
import com.project.minibacktesting_be.security.provider.UserDetailsImpl;
import com.project.minibacktesting_be.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    // 좋아요
    @PostMapping("/community/likes")
    public PortfolioResponseDto postLikes(@RequestBody LikesRequestDto requestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails){
        return likesService.postLikes(requestDto, userDetails);
    }

}
