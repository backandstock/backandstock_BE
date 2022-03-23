package com.project.minibacktesting_be.controller;


import com.project.minibacktesting_be.dto.backtesting.BacktestingRequestDto;
import com.project.minibacktesting_be.dto.portfolio.*;
import com.project.minibacktesting_be.security.provider.UserDetailsImpl;
import com.project.minibacktesting_be.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PortfolioController {
    private final PortfolioService portfolioService;

    //포트폴리오 저장
    @PostMapping("/portfolios")
    public ResponseEntity<PortfolioSaveResponseDto> savePortfolio(@RequestBody BacktestingRequestDto backtestingRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PortfolioSaveResponseDto portfolioSaveResponseDto = portfolioService.savePortfolio(backtestingRequestDto, userDetails);
        return ResponseEntity.ok(portfolioSaveResponseDto);
    }

    //내 포트폴리오 전체 불러오기
    @GetMapping("/portfolios")
    public ResponseEntity<List<PortfolioResponseDto>> getAllMyPortfolio(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<PortfolioResponseDto> portfolioResponseDtoList = portfolioService.getAllMyPortfolio(userDetails);
        return ResponseEntity.ok(portfolioResponseDtoList);
    }

    //포트폴리오 상세보기
    @GetMapping("/portfolios/{portId}")
    public ResponseEntity <PortfolioDetailsResponseDto> getPortfolio(@PathVariable Long portId){
        PortfolioDetailsResponseDto portfolioDetailsResponseDto = portfolioService.getPortfolio(portId);
        return ResponseEntity.ok(portfolioDetailsResponseDto);
    }

    //포트폴리오 비교하기
    @PostMapping("/portfolios/comparison")
    public ResponseEntity<PortfolioCompareDto.Response> comparePortfolio(@RequestBody PortfolioCompareDto.Request portCompareRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        PortfolioCompareDto.Response portCompareResponseDto = portfolioService.comparePortfolio(portCompareRequestDto, userDetails);
        return ResponseEntity.ok(portCompareResponseDto);
    }

    //포트폴리오 삭제
    @DeleteMapping("/portfolios/{portId}")
    public HashMap<String, Long> deletePortfolio(@PathVariable Long portId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return portfolioService.deletePortfolio(portId, userDetails);
    }

    //포트폴리오 자랑하기
    @PostMapping("/portfolios/boast")
    public ResponseEntity <PortfolioMyBestDto.Response> myBestPortfolio(@RequestBody PortfolioMyBestDto.Request portfolioMyBestRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        PortfolioMyBestDto.Response portfolioMyBestResponseDto = portfolioService.myBestPortfolio(portfolioMyBestRequestDto, userDetails);
        return ResponseEntity.ok(portfolioMyBestResponseDto);
    }




}
