package com.project.minibacktesting_be.service;


import com.project.minibacktesting_be.backtesting.BacktestingCal;
import com.project.minibacktesting_be.dto.StockSearchResponseDto;
import com.project.minibacktesting_be.dto.backtesting.BacktestingRequestDto;
import com.project.minibacktesting_be.dto.backtesting.BacktestingResponseDto;

import com.project.minibacktesting_be.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class StockService {

    private final StockRepository stockRepository;
    private final BacktestingCal backtestingCal;

    // 백테스팅 계산 하기
    public BacktestingResponseDto backTestingCal(BacktestingRequestDto backtestingRequestDto) {
        return backtestingCal.getResult(backtestingRequestDto);
    }

    //주식 종목 검색
    @Transactional(readOnly = true)
    public List<StockSearchResponseDto> getStockInfo(String keyword, String type) {
        List<StockSearchResponseDto> stockSearchResponseDtoList;
        log.info("로그 확인! ");
        if(type.equals("code")){
            Pageable pageable = PageRequest.of(0, 10);
            stockSearchResponseDtoList = stockRepository.findStockByCode(keyword, pageable);
            return stockSearchResponseDtoList;
        } else {
            Pageable pageable = PageRequest.of(0, 10);
            stockSearchResponseDtoList = stockRepository.findStockByName(keyword, pageable);
            return stockSearchResponseDtoList;
        }
    }
}
