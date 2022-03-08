package com.project.minibacktesting_be.controller;

import com.project.minibacktesting_be.dto.backtesting.BacktestingRequestDto;


import com.project.minibacktesting_be.dto.backtesting.BacktestingResponseDto;
import com.project.minibacktesting_be.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class StockController  {

   private final StockService stockService;

    //백테스팅 계산하기
    @PostMapping("/port/result")
    public BacktestingResponseDto backtestingCal(@RequestBody BacktestingRequestDto backtestingRequestDto){
        return stockService.backTestingCal(backtestingRequestDto);
    }

}