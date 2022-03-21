package com.project.minibacktesting_be.backtesting;

import com.project.minibacktesting_be.dto.backtesting.BacktestingDataDto;
import com.project.minibacktesting_be.dto.backtesting.BacktestingRequestDto;
import com.project.minibacktesting_be.dto.backtesting.BacktestingResponseDto;
import com.project.minibacktesting_be.model.Portfolio;
import com.project.minibacktesting_be.model.Stock;
import com.project.minibacktesting_be.model.User;
import com.project.minibacktesting_be.repository.StockRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
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

//      스트림 : 주식의 비율을 충족 시키기 위해선 해당 주식을 얼마 사야 할까?
        List<Double> targetPrices = ratioList.
                stream().
                map(s -> s*0.01*seedMoney).
                collect(Collectors.toList());

        List<BacktestingDataDto> backtestingDataDtos= new ArrayList<>();
        List<String> stockCodes = new ArrayList<>();
        List<Double> buyMoney = new ArrayList<>();



        for(String targetStockName : stockList){

            // 타겟 주식이 몇번째 값?
            int Idx = stockList.indexOf(targetStockName);

            // 타겟 주식의 정보 가져오기
            List<Stock> stocks =
                    stockRepository.findByStockNameAndCloseDateBetweenOrderByCloseDate(
                            targetStockName, startDate, endDate);

            // 주식 코드 가져오기
            stockCodes.add(stocks.get(0).getStockCode());

            // 주식 목표 금액
            Double targetPrice = targetPrices.get(Idx);
            buyMoney.add(targetPrice);

            // 주식 목표 금액에 맞추기 위해 필요한 주식 수
            Long firstMonthPrice = stocks.get(0).getClose();
            Double stockNum = targetPrice / firstMonthPrice;


            // 스트림을 활용해서 stock의 종가만 얻어냄
            List<Long> stockPrices =  stocks.
                    stream().
                    map(Stock::getClose).
                    collect(Collectors.toList());


            // 스트림 : 날짜만 뽑아내기 (yearMonth로 변환)
            List<YearMonth> months = stocks.
                    stream().
                    map(Stock::getCloseDate).
                    map(d -> YearMonth.from(d)).
                    collect(Collectors.toList());


            // 리스트 갯수를 맞춰야 한다. (데이터 수가 다 같지 않으므로)
            Double[] yieldMoneys = new Double[yearMonthList.size()];

            for(int i = 0  ; i < yearMonthList.size(); i++){

                YearMonth targetYearMonth = yearMonthList.get(i);
                if(months.contains(targetYearMonth)){
                     yieldMoneys[i] =
                             stockPrices.get(months.indexOf(targetYearMonth))*stockNum;
                }else{
                    yieldMoneys[i] =stockPrices.get(0)*stockNum;
                }
            }

            // 스트림 : 날짜만 뽑아내기 (yearMonth로 변환)
            List<Double> yields = stocks.
                    stream().
                    map(Stock::getYieldPct).
                    collect(Collectors.toList());


            // 해당 종목의 backtest 내용 저장하기
            BacktestingDataDto backtestingDataDto =
                    new BacktestingDataDto(targetStockName, targetPrice,
                            stockNum, months, stockPrices,
                            Arrays.asList(yieldMoneys), yields);

            backtestingDataDtos.add(backtestingDataDto);
        }


        // 전체 수익금을 위한 Array 만들기
        Double[]  monthYieldMoneys = new Double[yearMonthList.size()];
        Arrays.fill(monthYieldMoneys, 0.0);


        // 전체 수익률을 위한 Array 만들기
        Double[]  monthYields = new Double[yearMonthList.size()];
        Arrays.fill(monthYields, 0.0);


        for (int k = 0; k < yearMonthList.size(); k++) {
            for (BacktestingDataDto dataDto : backtestingDataDtos) {
                List<Double> targetYieldMoneys = dataDto.getYieldMoneys();
                // 해당 월의 종목 별 수익금 금액에 접근 한다.
                Double targetMoney = targetYieldMoneys.get(k);
                // monthYieldMoney인 종목별 수익금에 해당 금액을 추가 한다.
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

        // 주식별 월 수익금 리스트
        List<List <Double>> stockYieldMoneys =
                backtestingDataDtos.
                        stream().
                        map(BacktestingDataDto ::getYieldMoneys).
                        collect(Collectors.toList());


        // 주식별 월 수익금 리스트
        List<List <Double>> stockYields =
                backtestingDataDtos.
                        stream().
                        map(BacktestingDataDto ::getYields).
                        collect(Collectors.toList());


        BacktestingResponseDto backtestingResponseDto =
                new BacktestingResponseDto(YearMonth.from(startDate).toString(),
                        YearMonth.from(endDate).toString(),
                        bestMonth.toString(),
                        bestMoney,
                        worstMonth.toString(),
                        worstMoney,
                        seedMoney,stockList, stockCodes,buyMoney,
                        monthYieldMoneys[monthYieldMoneys.length-1],
                        monthYieldMoneys[monthYieldMoneys.length-1] - seedMoney,finalYield,
                        yearMonthList.stream().map(YearMonth::toString).collect(Collectors.toList()), Arrays.asList(monthYields), Arrays.asList(monthYieldMoneys),
                        kospiYield, kospiYieldMoney,
                        kosdaqYield, kosdaqYieldMoney,
                        stockYieldMoneys, stockYields);

        return backtestingResponseDto;
    }
//    public static BacktestingCal getBacktestingCal(LocalDate startDate, LocalDate endDate, Long seedMoney
//            , List<String> stockList, List<Integer> ratioList ) {
//        Portfolio portfolio = Portfolio.builder()
//                .startDate(startDate)
//                .endDate(endDate)
//                .seedMoney(seedMoney)
//                .
//    }

}
