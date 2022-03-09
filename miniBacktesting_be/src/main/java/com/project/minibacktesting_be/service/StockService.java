package com.project.minibacktesting_be.service;


import com.project.minibacktesting_be.backtesting.BacktestingCal;
import com.project.minibacktesting_be.dto.StockSearchResponseDto;
import com.project.minibacktesting_be.dto.backtesting.BacktestingDataDto;
import com.project.minibacktesting_be.dto.backtesting.BacktestingRequestDto;
import com.project.minibacktesting_be.dto.backtesting.BacktestingResponseDto;
import com.project.minibacktesting_be.model.Stock;
import com.project.minibacktesting_be.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class StockService {

    private final StockRepository stockRepository;
    private final BacktestingCal backtestingCal;


    public BacktestingResponseDto backTestingCal(BacktestingRequestDto backtestingRequestDto) {
        return backtestingCal.getResult(backtestingRequestDto);
    }

    //주식 종목 검색
    @Transactional(readOnly = true)
    public List<StockSearchResponseDto> getStockInfo(String keyword, String type) {
        List<StockSearchResponseDto> stockSearchResponseDtoList;
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
