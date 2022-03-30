package com.project.minibacktesting_be.backtesting;

import com.project.minibacktesting_be.dto.backtesting.BacktestingYearDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Component
public class YearYieldCal {

    public BacktestingYearDto getYearYield(List<YearMonth> yearMonthList,
                                            List<Double> monthYieldMoneys,
                                            List<Double> kospiYieldMoneys,
                                            List<Double> kosdaqYieldMoneys) {
        // 연도별 수익률 계산하기
        int startMonth = yearMonthList.get(0).getMonthValue();
        List<Integer> yearIdxs = new ArrayList<>();


        // 12월의 index만 가져오기
        for(YearMonth targetYearMonth : yearMonthList){
            if(targetYearMonth.getMonthValue() == 12){
                yearIdxs.add(yearMonthList.indexOf(targetYearMonth));
            }
        }

        // 마지막 달의 수익률 계산을 위한 부분
        // 만약 2013년 6월까지 실험한다면 2012년 12월이 마지막이 된다.
        // 2013년 6월의 수익도 필요하므로 2013년 6월을 가져오기 위한 코드가 필요하다.

        // 1. 1년 이하의 백테스팅의 경우
        // 2. yearMonthList의 마지막 인덱스와 yearIdx(연도별 데이터를 가져오기위한 idx)의 마지막 값이 같지 않다면
        if(yearIdxs.size() == 0 || yearMonthList.size()-1 != yearIdxs.get(yearIdxs.size() -1)){
            // 마지막 일자의 인덱스를 가져온다.
            yearIdxs.add(yearMonthList.size()-1);
        }

        List<Integer> years = new ArrayList<>(); // 연단위 수익률
        List<Double> yearYield = new ArrayList<>(); // 연단위 수익률
        List<Double> kospiYearYield = new ArrayList<>(); // 연단위 코스피 수익률
        List<Double> kosdaqYearYield = new ArrayList<>(); // 연단위 코스닥 수익률

        for(int y = 0; y < yearIdxs.size(); y++){
            int targetYearIdx = yearIdxs.get(y);
            int previousYearIdx = (y == 0)? 0:yearIdxs.get(y-1);


            Double targetYearYield =
                    ((monthYieldMoneys.get(targetYearIdx)-
                            monthYieldMoneys.get(previousYearIdx))
                            /(monthYieldMoneys.get(previousYearIdx)))*100 ;
            Double targetKospiYearYield =
                    ((kospiYieldMoneys.get(targetYearIdx)-
                            kospiYieldMoneys.get(previousYearIdx))
                            /(kospiYieldMoneys.get(previousYearIdx)))*100 ;

            Double targetKosdaqYearYield =
                    ((kosdaqYieldMoneys.get(targetYearIdx)-
                            kosdaqYieldMoneys.get(previousYearIdx))
                            /(kosdaqYieldMoneys.get(previousYearIdx)))*100 ;

            years.add(yearMonthList.get(targetYearIdx).getYear());
            yearYield.add(targetYearYield);
            kospiYearYield.add(targetKospiYearYield);
            kosdaqYearYield.add(targetKosdaqYearYield);
        }
        return new BacktestingYearDto(years, yearYield, kospiYearYield, kosdaqYearYield);
    }
}
