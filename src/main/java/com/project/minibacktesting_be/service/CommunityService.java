package com.project.minibacktesting_be.service;

import com.project.minibacktesting_be.dto.backtesting.BacktestingRequestDto;
import com.project.minibacktesting_be.dto.community.CommunityPortDto;
import com.project.minibacktesting_be.dto.community.CommunityPortResponseDto;
import com.project.minibacktesting_be.dto.community.TopFiveResponseDto;
import com.project.minibacktesting_be.model.*;
import com.project.minibacktesting_be.repository.LikesRepository;
import com.project.minibacktesting_be.repository.PortfolioRepository;
import com.project.minibacktesting_be.repository.StockRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@RequiredArgsConstructor
@Service
@Slf4j
public class CommunityService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    private final StockRepository stockRepository;
    private final PortfolioRepository portfolioRepository;
    private final LikesRepository likesRepository;

    public List<TopFiveResponseDto> getTopFive() {
        // 2월의 데이터를 가져오기 위해 startDate, endDate 설정함

        ArrayList<String> options= new ArrayList<>(Arrays.asList("kospi","kosdaq","volume"));
        LocalDate currentDate = LocalDate.now();
        LocalDate baseDate = currentDate.minusMonths(1);
        LocalDate startDate = baseDate.with(firstDayOfMonth());
        LocalDate endDate = baseDate.with(lastDayOfMonth()).plusDays(1);
        log.info("startDate "+startDate+" endDate "+endDate);
        //        LocalDate startDate =LocalDate.parse("2022-02-01");
//        LocalDate endDate = startDate.with(lastDayOfMonth());

        List<Stock> stocks;
        List<String> results;

        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        List<TopFiveResponseDto> topFiveResponseDtos = new ArrayList<>();
        TopFiveResponseDto topFiveResponseDto;


        for(String option : options){
            if (vop.get("topFive"+option+YearMonth.from(startDate)) != null) {
                topFiveResponseDto = (TopFiveResponseDto) vop.get("topFive"+option+YearMonth.from(startDate));
                log.info("redis topfive : {}", option+ YearMonth.from(startDate));}


            else{
                if(option.equals("kospi")|| option.equals("kosdaq")){
                    stocks = stockRepository.findTop5ByMarketAndCloseDateBetweenOrderByYieldPctDesc(option, startDate, endDate);
                    results = stocks.stream().
                            map(stock -> (stock.getYieldPct()*100)).
                            map(stock -> stock.toString()).
                            collect(Collectors.toList());

                }else
//                    if(option.equals("volume"))
                    {
                    stocks = stockRepository.findTop5ByCloseDateBetweenOrderByVolumeDesc(startDate, endDate);
                    results = stocks.stream().
                            map(stock -> stock.getVolume().toString()).
                            collect(Collectors.toList());

                }
//                else{
//                    stocks = stockRepository.findTop5ByCloseDateBetweenOrderByTransactionDesc(startDate, endDate);
//                    results = stocks.stream().
//                            map(stock -> stock.getTransaction().toString()).
//                            collect(Collectors.toList());
//                }
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

                topFiveResponseDto = new TopFiveResponseDto(option, stockNames, stockCodes, results, closes);
                vop.set("topFive"+option+YearMonth.from(startDate), topFiveResponseDto);
                redisTemplate.expire("topFive"+option+YearMonth.from(startDate), 7, TimeUnit.DAYS);
                log.info("db topfive : {}", option+YearMonth.from(startDate));
            }
            topFiveResponseDtos.add(topFiveResponseDto);
        }

        return topFiveResponseDtos;
    }


    public List<CommunityPortResponseDto> getCommnunityPorts(Integer page, Integer size) {

        // 좋아요, 시간 역순으로 정렬
        Sort sort = Sort.by(Sort.Order.desc("likesCnt"),Sort.Order.desc("createdAt"));
        // 페이징 처리하기
        Pageable pageable = PageRequest.of(page-1, size, sort);
        
        Page<Portfolio> pagePortfolios =
                portfolioRepository.findAllByMyBest(true,pageable);

        // 페이징 처리에서 필터링된 포트폴리오들을 반환함.
        return getCommunityPortResponseDtos(pagePortfolios);
    }



    public List<CommunityPortResponseDto> getRecentCommnunityPorts(String option,Integer page, Integer size) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        Sort sort = Sort.by(Sort.Order.desc("likesCnt"),
                Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page-1, size, sort);

        // 옵션에 따른 시작 일자 지정하기 (오늘, 이번주, 이번달에 자랑하기 된 포트폴리오들)
        LocalDateTime start = null;
        if(option.equals("today")){
            start = currentDateTime.minusHours(24);
        }else if(option.equals("week")){
            start = currentDateTime.minusWeeks(1);
        }else if(option.equals("month")){
            start = currentDateTime.minusMonths(1);
        }

        Page<Portfolio> pagePortfolios = null;
        if(option.equals("all")){
            Pageable pageableLatest = PageRequest.of(page-1, size, Sort.by(Sort.Order.desc("createdAt")));
            pagePortfolios =
                    portfolioRepository.findAllByMyBest(true, pageableLatest);
        }else{
            pagePortfolios =
                portfolioRepository.findAllByMyBestAndCreatedAtBetween(
                        true, start, currentDateTime, pageable);
        }

        return getCommunityPortResponseDtos(pagePortfolios);

    }


    private List<CommunityPortResponseDto> getCommunityPortResponseDtos(Page<Portfolio> pagePortfolios) {
        List<CommunityPortResponseDto> communityPortResponseDtos = new ArrayList<>();

        for (Portfolio portfolio : pagePortfolios) {
            CommunityPortDto portResponseDto;
            CommunityPortResponseDto communityPortResponseDto;

//            ValueOperations<String, Object> vop = redisTemplate.opsForValue();

//            if (vop.get("communityPort"+portfolio.getId()) != null) {
//                portResponseDto = (CommunityPortDto) vop.get("communityPort"+portfolio.getId().toString());
//                log.info("redis communityPort : {}", portfolio.getId());
//            } else {

                BacktestingRequestDto requestDto = new BacktestingRequestDto(portfolio);

//                log.info("db communityPort : {}", portfolio.getId());

                portResponseDto = CommunityPortDto.
                        builder().
                        nickname(portfolio.getUser().getNickname()).
                        portId(portfolio.getId()).
                        stockList(requestDto.getStockList()).
                        ratioList(requestDto.getRatioList()).
                        finalYield(portfolio.getFinalYield()).
                        finalYieldMoney(((portfolio.getFinalYield()+100)*0.01)*portfolio.getSeedMoney()).
                        seedMoney(portfolio.getSeedMoney()).
                        startDate(portfolio.getStartDate().toString()).
                        endDate(portfolio.getEndDate().toString()).
                        createdAt(portfolio.getCreatedAt().toString()).
                        build();

//               vop.set("communityPort"+portfolio.getId().toString(), portResponseDto);
//               redisTemplate.expire("communityPort"+portfolio.getId().toString(), 3, TimeUnit.DAYS);
//            }


            List<Likes> likesList=  likesRepository.findByPortfolio(portfolio);

            List<Long> likesUsers = likesList.stream().
                    map(Likes::getUser).
                    map(User::getId).
                    collect(Collectors.toList());


            communityPortResponseDto = CommunityPortResponseDto.builder().
                    communityPort(portResponseDto).
                    likesCnt(portfolio.getLikesCnt()).
                    commentCnt((long) portfolio.getComments().size()).
                    likesUsers(likesUsers).
                    build();

            communityPortResponseDtos.add(communityPortResponseDto);
        }
        return communityPortResponseDtos;
    }

}
