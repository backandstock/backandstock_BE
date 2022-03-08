package com.project.minibacktesting_be.service;

import com.project.minibacktesting_be.dto.community.TopFiveResponseDto;
import com.project.minibacktesting_be.model.Stock;
import com.project.minibacktesting_be.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@RequiredArgsConstructor
@Service
public class CommunityService {
    private final StockRepository stockRepository;

    public TopFiveResponseDto getTopFive(String option) {
        // 2월의 데이터를 가져오기 위해 startDate, endDate 설정함
        LocalDate startDate = LocalDate.parse("2022-02-01");
        LocalDate endDate = startDate.with(lastDayOfMonth());
        System.out.println(endDate);


        List<Stock> stocks;
        List<Double> results;
        if(option.equals("kospi")|| option.equals("kosdaq")){
            stocks = stockRepository.findTop5ByMarketAndCloseDateBetweenOrderByYieldPctDesc(option, startDate, endDate);

            results = stocks.
                    stream().
                    map(Stock ::getYieldPct).
                    map(i -> i*0.01).
                    collect(Collectors.toList());

        }else if(option.equals("volume")){
            stocks = stockRepository.findTop5ByCloseDateBetweenOrderByVolumeDesc(startDate, endDate);
            System.out.println(stocks.get(0).getVolume().doubleValue());
            results = stocks.
                    stream().
                    map(stock -> stock.getVolume().doubleValue()).
                    collect(Collectors.toList());
        }else{
            stocks = stockRepository.findTop5ByCloseDateBetweenOrderByTransactionDesc(startDate, endDate);

            results = stocks.
                    stream().
                    map(stock -> stock.getTransaction().doubleValue()).
                    collect(Collectors.toList());
        }

        List<String> stockNames = stocks.
                stream().
                map(Stock :: getStockName).
                collect(Collectors.toList());

        List<String> stockCodes = stocks.
                stream().
                map(Stock :: getStockCode).
                collect(Collectors.toList());

        List<Long> closes = stocks.
                stream().
                map(Stock :: getClose).
                collect(Collectors.toList());


        return new TopFiveResponseDto(stockNames, stockCodes, results, closes);

    }
}
