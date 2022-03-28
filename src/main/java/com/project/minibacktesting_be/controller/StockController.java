package com.project.minibacktesting_be.controller;

import com.project.minibacktesting_be.dto.StockSearchResponseDto;
import com.project.minibacktesting_be.dto.backtesting.BacktestingRequestDto;
import com.project.minibacktesting_be.dto.backtesting.BacktestingResponseDto;
import com.project.minibacktesting_be.exception.stock.StockSearchException;
import com.project.minibacktesting_be.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class StockController  {

   private final StockService stockService;

    //백테스팅 계산하기
    @PostMapping("/stocks")
    public BacktestingResponseDto backtestingCal(
            @RequestBody BacktestingRequestDto backtestingRequestDto){
        return stockService.backTestingCal(backtestingRequestDto);
    }
    

    //주식 종목 검색
    @GetMapping("/stocks")
    public ResponseEntity<List<StockSearchResponseDto>> getStockInfo(
            @RequestParam(value = "keyword", required = false, defaultValue = "")
                    String keyword,
            @RequestParam(value = "type", required = false, defaultValue = "")
                    String type){
        if(type.equals("") || type == null){
            throw new StockSearchException("Type input is null or does not exist.");
        }

        if(keyword.equals("") || keyword == null){
            throw new StockSearchException("Keyword input is null or does not exist.");
        }
        List<StockSearchResponseDto> stockSearchResponseDtoList =
                stockService.getStockInfo(keyword, type);
        return ResponseEntity.ok(stockSearchResponseDtoList);
    }

}