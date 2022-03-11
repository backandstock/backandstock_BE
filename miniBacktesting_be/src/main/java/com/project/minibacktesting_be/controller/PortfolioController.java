package com.project.minibacktesting_be.controller;


import com.project.minibacktesting_be.dto.backtesting.BacktestingRequestDto;
import com.project.minibacktesting_be.dto.portfolio.PortfolioResponseDto;
import com.project.minibacktesting_be.security.provider.UserDetailsImpl;
import com.project.minibacktesting_be.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PortfolioController {
    private final PortfolioService portfolioService;

    //포트폴리오 입력값 저장
    @PostMapping("/port")
    public ResponseEntity<PortfolioResponseDto> savePortfolio(@RequestBody BacktestingRequestDto backtestingRequestDto,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PortfolioResponseDto portfolioResponseDto = portfolioService.savePortfolio(backtestingRequestDto, userDetails);
        return ResponseEntity.ok(portfolioResponseDto);
    }


}
