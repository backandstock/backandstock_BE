package com.project.minibacktesting_be.controller;


import com.project.minibacktesting_be.dto.backtesting.BacktestingRequestDto;
import com.project.minibacktesting_be.dto.portfolio.PortfolioResponseDto;
import com.project.minibacktesting_be.dto.portfolio.PortfolioSaveResponseDto;
import com.project.minibacktesting_be.security.provider.UserDetailsImpl;
import com.project.minibacktesting_be.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PortfolioController {
    private final PortfolioService portfolioService;

    //포트폴리오 저장
    @PostMapping("/port")
    public ResponseEntity<PortfolioSaveResponseDto> savePortfolio(@RequestBody BacktestingRequestDto backtestingRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PortfolioSaveResponseDto portfolioSaveResponseDto = portfolioService.savePortfolio(backtestingRequestDto, userDetails);
        return ResponseEntity.ok(portfolioSaveResponseDto);
    }

//    //내 포트폴리오 전체 불러오기
//    @GetMapping("/port/mypage")
//    public ResponseEntity<List<PortfolioResponseDto>> getAllMyPortfolio(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        List<PortfolioResponseDto> portfolioResponseDtoList = portfolioService.getAllMyPortfolio(userDetails);
//        return ResponseEntity.ok(portfolioResponseDtoList);
//    }

//    //포트폴리오 하나 불러오기
//    @GetMapping("/port/{portId}")
//    public ResponseEntity <PortfolioResponseDto> getPortfolio(@PathVariable Long portId){
//        PortfolioResponseDto portfolioResponseDto = portfolioService.getPortfolio(portId);
//        return ResponseEntity.ok(portfolioResponseDto);
//    }

//    //포트폴리오 삭제
//    @DeleteMapping("/port/{portId}")
//    public




}
