package com.project.minibacktesting_be.backtesting;

import com.project.minibacktesting_be.dto.backtesting.BacktestingEachStockDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class RebalancingCal {


    public List<BacktestingEachStockDto> getRebalnacingResult(
            List<BacktestingEachStockDto> backtestingEachStockDtos,
            List<Integer> ratioList,
            int option){

        // 주식 가격만 모으기
        List<List<Long>> stockPriceList = backtestingEachStockDtos.
                stream().
                map(BacktestingEachStockDto::getStockPrices).
                collect(Collectors.toList());


        // 주식 수익금만 모으기
        List<List<Double>> stockYieldMoneyList = backtestingEachStockDtos.
                stream().
                map(BacktestingEachStockDto::getYieldMoneys).
                collect(Collectors.toList());

        List<Double> stockNumList = backtestingEachStockDtos.
                stream().
                map(BacktestingEachStockDto::getStockNum).
                collect(Collectors.toList());


        for (int dayIdx = 0;dayIdx < stockPriceList.get(0).size(); dayIdx++){

            log.info("---------------------------");
            log.info(String.valueOf(dayIdx+1));
            log.info(String.valueOf(stockPriceList.get(0).size()));
            // 리밸런싱 주기에만 리밸런싱 함, 그리고 마지막 일자는 리밸런싱 제외함 (최종 수익이 산출되기 때문에)
            if(dayIdx != 0 & (dayIdx % option) == 0 && (dayIdx+1) != stockPriceList.get(0).size()){

                log.info("stockNum 계산 전");
                log.info(String.valueOf(stockNumList));

               stockNumList = stockNumCal(stockYieldMoneyList,stockPriceList, ratioList, dayIdx);

                log.info("stockNum 계산 후");
                log.info(String.valueOf(stockNumList));

            }


            // 주식 하나씩 돌아가면서 수익금 재계산
            for(double stockNum : stockNumList){
                // 몇번째 주식을 계산할 것인가?
                int targetStockIdx = stockNumList.indexOf(stockNum);
                // 해당 주식의 target 월 금액
                Long targetStockPrice =  stockPriceList.get(targetStockIdx).get(dayIdx);
                stockYieldMoneyList.
                        get(targetStockIdx).
                        set(dayIdx,targetStockPrice*stockNum);

                log.info(String.valueOf(stockYieldMoneyList.get(targetStockIdx)));

                // 수익금 리스트 변경하기
                backtestingEachStockDtos.get(targetStockIdx).setYieldMoneys(stockYieldMoneyList.get(targetStockIdx));

            }
        }


        return backtestingEachStockDtos;

    }

    public List<Double> stockNumCal(List<List<Double>> stockYieldMoneyList,
                                    List<List<Long>> stockPriceList,
                                       List<Integer> ratioList,
                                       Integer dayIdx){

        // 타겟 월의 수익금만 더해서 totalYield 만들기
        double totalYield = stockYieldMoneyList.
                stream().
                mapToDouble(s->s.get(dayIdx)).
                sum();


        log.info("totalYield : " + totalYield+" at dayIdx : " + dayIdx);
        // 주식별로 수익금의 해당 날짜의 값을 가져오기
        // 계산식  (해당 달의 총 수익 * ratio*100)/해당 달의 종가
        List<Double> stockNumResult = new ArrayList<>();
        for(int targetStockIdx = 0; targetStockIdx < ratioList.size(); targetStockIdx ++){
            double targetStockNum = (totalYield*ratioList.get(targetStockIdx)*0.01)/
                    stockPriceList.get(targetStockIdx).get(dayIdx);

            log.info("targetStockIdx : "+ targetStockIdx +" stockPrice: " +
                    stockPriceList.get(targetStockIdx).get(dayIdx));

            stockNumResult.add(targetStockNum);
        }

        return stockNumResult;
    }

}
