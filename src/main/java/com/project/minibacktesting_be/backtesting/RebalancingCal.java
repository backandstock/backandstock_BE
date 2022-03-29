package com.project.minibacktesting_be.backtesting;

import com.project.minibacktesting_be.dto.backtesting.BacktestingEachStockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class RebalancingCal {


    public List<BacktestingEachStockDto> getRebalnacingResult(
            List<BacktestingEachStockDto> backtestingEachStockDtos,
            List<Integer> ratioList,
            int option){

        List<List<Long>> stockPriceList = backtestingEachStockDtos.
                stream().
                map(BacktestingEachStockDto::getStockPrices).
                collect(Collectors.toList());


        List<List<Double>> stockYieldMoneyList = backtestingEachStockDtos.
                stream().
                map(BacktestingEachStockDto::getYieldMoneys).
                collect(Collectors.toList());

        List<Double> stockNumList = backtestingEachStockDtos.
                stream().
                map(BacktestingEachStockDto::getStockNum).
                collect(Collectors.toList());


        for (int dayIdx = 0;dayIdx < stockPriceList.get(0).size(); dayIdx++){



            if(dayIdx != 0 & (dayIdx % option) == 0){
                System.out.println("stockNum 계산 전");
                stockNumList.forEach(System.out::println);

               stockNumList = stockNumCal(stockYieldMoneyList,
                       stockPriceList, ratioList, dayIdx);

                System.out.println("stockNum 계산 후");
                stockNumList.forEach(System.out::println);

            }


            // 주식 하나씩 돌아가면서 수익금 재계산
            for(double stockNum : stockNumList){
                int targetStockIdx = stockNumList.indexOf(stockNum);
                Long targetStockPrice =  stockPriceList.get(targetStockIdx).get(dayIdx);
                stockYieldMoneyList.
                        get(targetStockIdx).
                        set(dayIdx,targetStockPrice*stockNum);

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


        System.out.println("totalYield : " + totalYield+" at dayIdx : " + dayIdx);
        // 주식별로 수익금의 해당 날짜의 값을 가져오기
        // 계산식  (해당 달의 총 수익 * ratio*100)/해당 달의 종가
        List<Double> stockNumResult = new ArrayList<>();
        for(int targetStockIdx = 0; targetStockIdx < ratioList.size(); targetStockIdx ++){
            double targetStockNum = (totalYield*ratioList.get(targetStockIdx)*0.01)/
                    stockPriceList.get(targetStockIdx).get(dayIdx);

            System.out.println("targetStockIdx : "+ targetStockIdx +" stockPrice: " +
                    stockPriceList.get(targetStockIdx).get(dayIdx));


            stockNumResult.add(targetStockNum);
        }

        return stockNumResult;
    }





}
