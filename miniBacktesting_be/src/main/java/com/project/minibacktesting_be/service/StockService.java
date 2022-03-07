package com.project.minibacktesting_be.service;


import com.project.minibacktesting_be.dto.backtesting.BacktestingDataDto;
import com.project.minibacktesting_be.dto.backtesting.BacktestingRequestDto;
import com.project.minibacktesting_be.dto.backtesting.BacktestingResponseDto;

import com.project.minibacktesting_be.model.StockData;
import com.project.minibacktesting_be.model.StockInfo;
import com.project.minibacktesting_be.repository.StockDataRepository;
import com.project.minibacktesting_be.repository.StockInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StockService {

    private final StockInfoRepository stockInfoRepository;
    private final StockDataRepository stockDataRepository;


    public BacktestingResponseDto backTestingCal(BacktestingRequestDto backtestingRequestDto) {

//        //백테스팅 계산하기
//        LocalDate startDate  = backtestingRequestDto.getStartDate();
//        LocalDate endDate = backtestingRequestDto.getEndDate();
//        Long seedMoney = backtestingRequestDto.getSeedMoney();
//        List<String> stockList = backtestingRequestDto.getStockList();
//        List<Integer> ratioList = backtestingRequestDto.getRatioList();
//
//
//        List<BacktestingDataDto> backtestingDataDtos= new ArrayList<>();
//
//        Long totalCash = Long.valueOf(0);
//        Long totalBuyPrice = Long.valueOf(0);
//
//        // 주식 목록에서 주식 하나씩 가져와서 필요한 값만 뽑아내기
//        for (int i = 0; i <stockList.size() ; i++ ){
//
//            // 타겟 주식 이름 가져오기
//            String targetStockName = stockList.get(i);
//
//            // 주식 이름으로 주식 정보 가져오기
//            StockInfo stockInfo = stockInfoRepository.findByStockName(targetStockName);
//
//            // 해당 주식의 데이터 가져오기
//            List<StockData> stockDatas = stockInfo.getStockDatas();
//
//            // 일자에 해당하는 주식 정보를 지닌 리스트
//            List<StockData> targetStockDatas = new ArrayList<>();
//
//            // startDate와 endDate에 해당하는 주식 정보만 골라 내기
//            for (StockData stockData : stockDatas){
//                LocalDate targetDate = stockData.getCloseDate();
//                // 시작일보다 뒤 거나 같은 데이터 이면서 끝날짜 보다 뒤인 데이터라면
//                if (targetDate.isEqual(endDate) || targetDate.isBefore(endDate))
//                    if (targetDate.isEqual(startDate) || targetDate.isAfter(startDate)) {
//                        targetStockDatas.add(stockData);
//                    }
//            }
//
//           targetStockDatas.stream().
//                    sorted(Comparator.comparing(StockData::getCloseDate)).collect(Collectors.toList());
//
//            // 1. 해당 주식을 얼마만큼 사야 되는가? 비율을 곱한 값
//            double stockTargetPrice = ratioList.get(i)*0.01*seedMoney;
//
//            // 2. 해당 주식의 한 주 가격은?
//            Long stockOnePrice =  targetStockDatas.get(1).getClose();
//
//            // 3. 해당 주식을 몇주를 사야 하는가? 1 해당 주식을 사야 하는 총 금액을 2번 해당 주식의 한 주 가격으로 나눈 몫
//            Long stockNum =  Math.floorDiv((long) stockTargetPrice, stockOnePrice);
//
////            // 4. 주식 사고 남은 돈 (현금) : 주당 가격이 정해져 있기 때문에 딱 맞춰 주식을 사는 것은 불가능 하다.
////            Long restCash = Math.floorMod((long) stockTargetPrice, stockOnePrice);
////
//
//            List<Long> stockBuyPrices = new ArrayList<>();
//            for (StockData targetStockData: targetStockDatas){
//                stockBuyPrices.add(targetStockData.getClose() * stockNum);
//            }
//
//
//
////            totalCash += restCash;
//            totalBuyPrice += stockOnePrice * stockNum;
//
//            BacktestingDataDto backtestingDataDto =
//                    new BacktestingDataDto(stockInfo,stockTargetPrice,stockNum,
//                                            targetStockDatas, stockBuyPrices,
//                                stockOnePrice*stockNum);
//
//            backtestingDataDtos.add(backtestingDataDto);
//
//        }



        return null;
    }
}
