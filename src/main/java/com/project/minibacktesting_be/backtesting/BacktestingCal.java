package com.project.minibacktesting_be.backtesting;

import com.project.minibacktesting_be.dto.backtesting.BacktestingEachStockDto;
import com.project.minibacktesting_be.dto.backtesting.BacktestingRequestDto;
import com.project.minibacktesting_be.dto.backtesting.BacktestingResponseDto;
import com.project.minibacktesting_be.dto.backtesting.BacktestingYearDto;
import com.project.minibacktesting_be.model.Stock;
import com.project.minibacktesting_be.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//@Builder
@RequiredArgsConstructor
@Component
public class BacktestingCal {

    private final StockRepository stockRepository;
    private final EachStockCal eachStockCal;
    private final YearYieldCal yearYieldCal;

   public BacktestingResponseDto getResult(BacktestingRequestDto backtestingRequestDto) {


        //백테스팅 계산하기
        LocalDate startDate  = backtestingRequestDto.getStartDate();
        LocalDate endDate = backtestingRequestDto.getEndDate().minusDays(1);
        Long seedMoney = backtestingRequestDto.getSeedMoney();
        List<String> stockList = backtestingRequestDto.getStockList();
        List<Integer> ratioList = backtestingRequestDto.getRatioList();
        List<YearMonth> yearMonthList = new ArrayList<>();

        YearMonth startYearMonth = YearMonth.from(startDate);


        // 투자기간의 날짜 정보 리스트 만들어주기
        while(!startYearMonth.equals(YearMonth.from(endDate.plusDays(1)))){ //다르다면 실행, 동일 하다면 빠져나감
            yearMonthList.add(startYearMonth);
            startYearMonth = startYearMonth.plusMonths(1);

        }

//  스트림 : 주식의 비율을 충족 시키기 위해선 해당 주식을 얼마 사야 할까?
        List<Double> targetPrices = ratioList.
                stream().
                map(s -> s*0.01*seedMoney).
                collect(Collectors.toList());


        // 주식 코드 리스트
        List<String> stockCodes = new ArrayList<>();
       // 주식별 목표 금액
//        List<Double> buyMoney = new ArrayList<>();


        // 종목별로 수익률을 계산한다.
       List<BacktestingEachStockDto> backtestingDataDtos =
               eachStockCal.getStockCalResult(startDate,
                       endDate,
                       stockList,
                       ratioList,
                       yearMonthList,
                       targetPrices,
                       stockCodes);
//                       buyMoney);


       // 전체 수익금을 위한 Array 만들기
        Double[]  monthYieldMoneys = new Double[yearMonthList.size()];
        Arrays.fill(monthYieldMoneys, 0.0);


        // 전체 수익률을 위한 Array 만들기
        Double[]  monthYields = new Double[yearMonthList.size()];
        Arrays.fill(monthYields, 0.0);


        for (int k = 0; k < yearMonthList.size(); k++) {
            for (BacktestingEachStockDto dataDto : backtestingDataDtos) {
                List<Double> targetYieldMoneys = dataDto.getYieldMoneys();
                // 해당 월의 종목 별 수익금 금액에 접근 한다.
                Double targetMoney = targetYieldMoneys.get(k);
                // monthYieldMoney 종목별 수익금에 해당 금액을 추가 한다.
                monthYieldMoneys[k] =
                        Math.round((monthYieldMoneys[k] + targetMoney)*10)/10.0;
            }
            if (k > 0) {
                // 수익률 구하기 ((현재 수익 - 전달 수익) / 전달 수익 ) * 100
                double monthYield =
                        (monthYieldMoneys[k] - monthYieldMoneys[k - 1]) / monthYieldMoneys[k - 1];
                monthYields[k] = monthYield * 100;

            }
        }


        // 최고, 최악의 달을 위한 리스트 (첫달 제외)
        List<Double> withoutFirstYield = Arrays.asList(monthYieldMoneys).subList(1, monthYieldMoneys.length);
        List<YearMonth> withoutFirstMonth = yearMonthList.subList(1, yearMonthList.size());


        // 최고의 달 , 최고의 수익
        double bestMoney = Collections.max(withoutFirstYield);
        int bestIdx = withoutFirstYield.indexOf(bestMoney);
        YearMonth bestMonth =withoutFirstMonth.get(bestIdx);

        // 첫 달 빼고 ! 최악의 달, 최악의 수익
        double worstMoney = Collections.min(withoutFirstYield);
        int worstIdx = withoutFirstYield.indexOf(worstMoney);
        YearMonth worstMonth =withoutFirstMonth.get(worstIdx);

        // 마지막 수익률 ( 현재 수익 - 투자금액 / 투자 금액 *100)
        double finalYield = ((Arrays.asList(monthYieldMoneys).get(monthYieldMoneys.length-1) - seedMoney)/
                seedMoney)*100;

        // 코스피 코스닥 투자시에는 얼마?
        List<Stock> kospiStocks =
                stockRepository.findByStockNameAndCloseDateBetweenOrderByCloseDate("kospiIndex", startDate, endDate);
        List<Stock> kosdaqStocks =
                stockRepository.findByStockNameAndCloseDateBetweenOrderByCloseDate("kosdaqIndex", startDate, endDate);

        Double kospiNum = seedMoney.doubleValue()/kospiStocks.get(0).getClose();
        Double kosdaqNum = seedMoney.doubleValue()/(kosdaqStocks.get(0).getClose());

        // 코스피 수익금 계산
        List<Double> kospiYieldMoney = kospiStocks.
                stream().
                map(Stock:: getClose).
                map(s -> s*kospiNum).
                collect(Collectors.toList());

        // 코스피 수익률 계산
        List<Double> kospiYield = kospiStocks.
                stream().
                map(Stock :: getYieldPct).
                map(s -> s*100).
                collect(Collectors.toList());

        // 코스닥 수익금 계산
        List<Double> kosdaqYieldMoney = kosdaqStocks.
                stream().
                map(Stock :: getClose).
                map(s -> s*kosdaqNum).
                collect(Collectors.toList());

        // 코스닥 수익률계산
        List<Double> kosdaqYield = kosdaqStocks.
                stream().
                map(Stock :: getYieldPct).
                map(s -> s*100).
                collect(Collectors.toList());


        // 첫달의 수익률은 0 : 코스피
        kospiYield.set(0, 0.0);

        // 첫달의 수익률은 0 : 코스닥
        kosdaqYield.set(0, 0.0);

        List<Double> stockYieldMoneys = new ArrayList<>();
//        List<Double> stockYields = new ArrayList<>();

        // 종목별로 최종 수익만 뽑아서 보내주기
        for(BacktestingEachStockDto targetDataDto : backtestingDataDtos){
            Double targetYieldMoney =
                    targetDataDto.getYieldMoneys().
                            get(targetDataDto.getYieldMoneys().size()-1);
//            Double targetYield =
//                    targetDataDto.getYields().
//                            get(targetDataDto.getYields().size()-1);
            stockYieldMoneys.add(targetYieldMoney);
//            stockYields.add(targetYield*100);
        }


       BacktestingYearDto backtestingYearDto =
               yearYieldCal.getYearYield(yearMonthList, Arrays.asList(monthYieldMoneys), kospiYieldMoney, kosdaqYieldMoney);


       BacktestingResponseDto backtestingResponseDto =
                new BacktestingResponseDto(YearMonth.from(startDate).toString(),
                        YearMonth.from(endDate).toString(),
                        bestMonth.toString(),
                        bestMoney,
                        worstMonth.toString(),
                        worstMoney,
                        seedMoney,stockList, stockCodes,targetPrices,
                        monthYieldMoneys[monthYieldMoneys.length-1],
                        monthYieldMoneys[monthYieldMoneys.length-1] - seedMoney,finalYield,
                        yearMonthList.stream().map(YearMonth::toString).collect(Collectors.toList()), Arrays.asList(monthYields), Arrays.asList(monthYieldMoneys),
                        kospiYield, kospiYieldMoney,
                        kosdaqYield, kosdaqYieldMoney,
                        stockYieldMoneys,
                        backtestingYearDto.getYears(),
                        backtestingYearDto.getYearYield(),
                        backtestingYearDto.getKospiYearYield(),
                        backtestingYearDto.getKosdaqYearYield());

        return backtestingResponseDto;
    }





}