package com.project.minibacktesting_be.service;

import com.project.minibacktesting_be.backtesting.BacktestingCal;
import com.project.minibacktesting_be.dto.backtesting.BacktestingRequestDto;
import com.project.minibacktesting_be.dto.backtesting.BacktestingResponseDto;
import com.project.minibacktesting_be.dto.community.CommunityPortResponseDto;
import com.project.minibacktesting_be.dto.community.TopFiveResponseDto;
import com.project.minibacktesting_be.dto.likes.LikesRequestDto;
import com.project.minibacktesting_be.model.PortStock;
import com.project.minibacktesting_be.model.Portfolio;
import com.project.minibacktesting_be.model.Stock;
import com.project.minibacktesting_be.repository.PortfolioRepository;
import com.project.minibacktesting_be.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@RequiredArgsConstructor
@Service
public class CommunityService {
    private final StockRepository stockRepository;
    private final PortfolioRepository portfolioRepository;
    private final BacktestingCal backtestingCal;

    public TopFiveResponseDto getTopFive(String option) {
        // 2월의 데이터를 가져오기 위해 startDate, endDate 설정함
        LocalDate startDate = LocalDate.parse("2022-02-01");
        LocalDate endDate = startDate.with(lastDayOfMonth());

        List<Stock> stocks;
        List<String> results;
        if(option.equals("kospi")|| option.equals("kosdaq")){
            stocks = stockRepository.findTop5ByMarketAndCloseDateBetweenOrderByYieldPctDesc(option, startDate, endDate);
            results = stocks.
                    stream().
                    map(stock -> (stock.getYieldPct()*100)).
                    map(stock -> stock.toString()).
                    collect(Collectors.toList());

        }else if(option.equals("volume")){
            stocks = stockRepository.findTop5ByCloseDateBetweenOrderByVolumeDesc(startDate, endDate);
            results = stocks.
                    stream().
                    map(stock -> stock.getVolume().toString()).
                    collect(Collectors.toList());

        }else{
            stocks = stockRepository.findTop5ByCloseDateBetweenOrderByTransactionDesc(startDate, endDate);
            results = stocks.
                    stream().
                    map(stock -> stock.getTransaction().toString()).
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


    public List<CommunityPortResponseDto> getCommnunityPorts(Integer page, Integer size) {


        Pageable pageable = PageRequest.of(page-1, size, Sort.by("likesCnt").descending());
        Page<Portfolio> PagePortfolios =  portfolioRepository.findAllByMyBest(true,pageable);

        // 페이징
//        Pageable pageable = PageRequest.of(page-1, size);
//        Page<Product> foundProductList = productRepository.findAll(pageable);

        // 찾아온 Product들을 PrductResponseDto에 담는 과정
        List<CommunityPortResponseDto> communityPortResponseDtos= new ArrayList<>();

        for(Portfolio portfolio : PagePortfolios) {
            List<PortStock> portfolioStocks = portfolio.getPortStocks();
            BacktestingRequestDto requestDto = new BacktestingRequestDto(portfolio);
            BacktestingResponseDto results = backtestingCal.getResult(requestDto);
            List<Double> monthYieldMoney = results.getMonthYieldMoney();

            CommunityPortResponseDto portResponseDto = CommunityPortResponseDto.
                    builder().
                    stockList(requestDto.getStockList()).
                    ratioList(requestDto.getRatioList()).
                    finalYield(portfolio.getFinalYield()).
                    seedMoney(portfolio.getSeedMoney()).
                    startDate(portfolio.getStartDate()).
                    endDate(portfolio.getEndDate()).
                    monthYieldMoney(monthYieldMoney).
                    build();

            communityPortResponseDtos.add(portResponseDto);
        }

        return communityPortResponseDtos;
    }



}
