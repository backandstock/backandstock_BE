package com.project.minibacktesting_be.controller;

import com.project.minibacktesting_be.dto.backtesting.BacktestingRequestDto;
import com.project.minibacktesting_be.dto.backtesting.BacktestingResponseDto;
import com.project.minibacktesting_be.dto.StockSearchResponseDto;
import com.project.minibacktesting_be.repository.StockDataRepository;
import com.project.minibacktesting_be.repository.StockInfoRepository;
import com.project.minibacktesting_be.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StockController  {

   private final StockService stockService;
   private final StockDataRepository stockDataRepository;
   private final StockInfoRepository stockInfoRepostiory;


    //백테스팅 계산하기
    @PostMapping("/port/result")
    public BacktestingResponseDto creatComment(@RequestBody BacktestingRequestDto backtestingRequestDto){
        return stockService.backTestingCal(backtestingRequestDto);
    }

    //주식 종목 검색
    @GetMapping("/stock/search")
    public ResponseEntity<List<StockSearchResponseDto>> getStockInfo(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "type", required = false, defaultValue = "") String type){
        if(type.equals("") || type == null){
            throw new RuntimeException("type을 정확히 입력해주세요");
        }
        if(keyword.equals("") || keyword == null){
            throw new RuntimeException("종목이름이나 종목코드를 입력해주세요");
        }
        List<StockSearchResponseDto> stockSearchResponseDtoList = stockService.getStockInfo(keyword, type);
        return ResponseEntity.ok(stockSearchResponseDtoList);
    }

}