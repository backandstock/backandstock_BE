package com.project.minibacktesting_be.backtesting;

import com.project.minibacktesting_be.dto.backtesting.BacktestingDataDto;
import com.project.minibacktesting_be.dto.backtesting.BacktestingRequestDto;
import com.project.minibacktesting_be.dto.backtesting.BacktestingResponseDto;
import com.project.minibacktesting_be.model.Stock;
import com.project.minibacktesting_be.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Component
public class BacktestingCal {

    private static StockRepository stockRepository;

    public static BacktestingResponseDto getResult(BacktestingRequestDto backtestingRequestDto) {


        //백테스팅 계산하기
        LocalDate startDate  = backtestingRequestDto.getStartDate();
        LocalDate endDate = backtestingRequestDto.getEndDate().minusDays(1);
        Long seedMoney = backtestingRequestDto.getSeedMoney();
        List<String> stockList = backtestingRequestDto.getStockList();
        List<Integer> ratioList = backtestingRequestDto.getRatioList();

        List<YearMonth> yearMonthList = new ArrayList<>();

        YearMonth startYearMonth = YearMonth.from(startDate);


        // 기간의 list 만들어주기
        while(!startYearMonth.equals(YearMonth.from(endDate.plusDays(1)))){ //다르다면 실행, 동일 하다면 빠져나감
            yearMonthList.add(startYearMonth);
            startYearMonth = startYearMonth.plusMonths(1);

        }

//        List<Double> targetPrices = new ArrayList<>();
//         스트림으로 할 수 있는 방법을 찾아보자
//         주식의 비율을 충족 시키기 위해선 해당 주식을 얼마 사야 할까?
//        for (Integer targetRatio : ratioList){
//            double targetPrice = targetRatio* 0.01 *seedMoney;
//            targetPrices.add(targetPrice);
//        }

//         스트림 : 주식의 비율을 충족 시키기 위해선 해당 주식을 얼마 사야 할까?
        List<Double> targetPrices = ratioList.
                stream().
                map(s -> s*0.01*seedMoney).
                collect(Collectors.toList());

        List<BacktestingDataDto> backtestingDataDtos= new ArrayList<>();
        List<String> stockCodes = new ArrayList<>();
        List<Double> buyMoney = new ArrayList<>();



        for (int i = 0; i < stockList.size(); i++) {
            // 타겟 주식 이름 가져오기
            String targetStockName = stockList.get(i);

            // 타겟 주식의 정보 가져오기
            List<Stock> stocks = stockRepository.findByStockNameAndCloseDateBetweenOrderByCloseDate(targetStockName, startDate, endDate);


            // 주식 코드 가져오기
            stockCodes.add(stocks.get(0).getStockCode());

            // 주식 목표 금액
            Double targetPrice = targetPrices.get(i);

            buyMoney.add(targetPrice);

            // 주식 목표 금액에 맞추기 위해 필요한 주식 수
            Long firstMonthPrice = stocks.get(0).getClose();
            Double stockNum = targetPrice / firstMonthPrice;

//            List<YearMonth> months =  new ArrayList<>();
//            List<Long> stockPrices =  new ArrayList<>();
//            List<Double> yieldMoneys =  new ArrayList<>();
//
////             해당 주식의 closeDate List 만들기 (월로 변환함)
//            for (Stock stock : stocks) {
//                LocalDate targetDate = stock.getCloseDate();
//                months.add(YearMonth.from(targetDate));
//                stockPrices.add(stock.getClose());
//                double yieldMoney = stock.getClose() * stockNum;
//                yieldMoneys.add(yieldMoney);
//            }

            // 스트림을 활용해서 stock의 종가만 얻어냄
            List<Long> stockPrices =  stocks.
                    stream().
                    map(Stock::getClose).
                    collect(Collectors.toList());


            // 스트림 : 일자만 뽑아내기
            List<YearMonth> months = stocks.
                    stream().
                    map(Stock::getCloseDate).
                    map(d -> YearMonth.from(d)).
                    collect(Collectors.toList());

            // 스트림 : 수익금 계산하기
            List<Double> yieldMoneys = stocks.
                    stream().map(Stock :: getClose).
                    map(s -> s*stockNum).
                    collect(Collectors.toList());

            BacktestingDataDto backtestingDataDto =
                    new BacktestingDataDto(targetStockName, targetPrice,
                            stockNum, months, stockPrices, yieldMoneys);

            backtestingDataDtos.add(backtestingDataDto);
        }


        // 수익금을 위한 리스트 만들기
        List<Double> monthYieldMoneys = new ArrayList<>();
        for(int y = 0; y < yearMonthList.size(); y++){
            monthYieldMoneys.add(0.0);
        }


        // 수익률을 위한 리스트 만들기
        List<Double> monthYields = new ArrayList<>();
        for(int y = 0; y < yearMonthList.size(); y++){
            monthYields.add(0.0);
        }

        for (int k = 0; k < yearMonthList.size(); k++) {
            for (BacktestingDataDto dataDto : backtestingDataDtos) {
                YearMonth targetYearMonth = yearMonthList.get(k);
                List<YearMonth> targetMonths = dataDto.getMonths();
                List<Double> targetYieldMoneys = dataDto.getYieldMoneys();


                if (targetMonths.contains(targetYearMonth)) {
                    // 해당 월의 인덱스를 통해 종목 별 수익금 금액에 접근 한다.
                    Double targetMoney = targetYieldMoneys.get(targetMonths.indexOf(targetYearMonth));
                    // monthYieldMoney인 종목별 수익금에 해당 금액을 추가 한다.
                    monthYieldMoneys.set(k, monthYieldMoneys.get(k) + targetMoney);

                } else {
                    // 해당 월의 값이 없을 경우 현금으로 대체한다.
                    Double targetMoney = dataDto.getTargetPrice();
                    monthYieldMoneys.set(k, monthYieldMoneys.get(k) + targetMoney);
                }

                if(k > 0){
                    // 수익률 구하기 (현재 수익 - 전달 수익 / 전달 수익  * 100)
                    double monthYield =
                            (monthYieldMoneys.get(k) -monthYieldMoneys.get(k-1)) / monthYieldMoneys.get(k-1);
                    monthYields.set(k, monthYield*100);

                }

            }
        }


        // 최고, 최악의 달을 위한 리스트 (첫달 제외)
        List<Double> withoutFirstYield = monthYieldMoneys.subList(1, monthYieldMoneys.size());
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
        double finalYield = ((monthYieldMoneys.get(monthYieldMoneys.size()-1) - seedMoney)/
                seedMoney)*100;

        // 코스피 코스닥 투자시에는 얼마?
        List<Stock> kospiStocks =
                stockRepository.findByStockNameAndCloseDateBetweenOrderByCloseDate("KODEX 200", startDate, endDate);
        List<Stock> kosdaqStocks =
                stockRepository.findByStockNameAndCloseDateBetweenOrderByCloseDate("KODEX 코스닥 150", startDate, endDate);

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

//        List<Double> kospiYield = new ArrayList<>();
//        List<Double> kospiYieldMoney = new ArrayList<>();
//
//        List<Double> kosdaqYield = new ArrayList<>();
//        List<Double> kosdaqYieldMoney = new ArrayList<>();
//
//
//        for (int l =0 ; l < kospiStocks.size(); l++){
//
//            // kospi
//            // 수익금 추가
//            kospiYieldMoney.add(kospiStocks.get(l).getClose()*kospiNum);
//            // 수익률 추가
//            if(l > 0){
//                kospiYield.add(kospiStocks.get(l).getYieldPct()*100);
//            }else{
//                kospiYield.add(0.0);
//            }
//
//            // kosdaq
//            // 수익금 추가
//            kosdaqYieldMoney.add(kosdaqStocks.get(l).getClose() *kosdaqNum);
//            // 수익률 추가
//            if(l > 0){
//                kosdaqYield.add(kosdaqStocks.get(l).getYieldPct()*100);
//            }else{
//                kosdaqYield.add(0.0);
//            }
//
//        }


        BacktestingResponseDto backtestingResponseDto =
                new BacktestingResponseDto(YearMonth.from(startDate), YearMonth.from(endDate),
                        bestMonth, bestMoney, worstMonth, worstMoney,
                        seedMoney,stockList, stockCodes,buyMoney,
                        monthYieldMoneys.get(monthYieldMoneys.size()-1),
                        monthYieldMoneys.get(monthYieldMoneys.size()-1) - seedMoney,finalYield,
                        yearMonthList, monthYields, monthYieldMoneys,
                        kospiYield, kospiYieldMoney,
                        kosdaqYield, kosdaqYieldMoney);

        return backtestingResponseDto;
    }

}
