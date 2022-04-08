package com.project.minibacktesting_be.backtesting;

import com.project.minibacktesting_be.dto.backtesting.BacktestingYearDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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
        // 12월의 값이 하나도 없다면 혹은 마지막 달이 12월이 아니라면 마지막달의 값이 필요하다.
        if(yearIdxs.size() == 0 || yearMonthList.size()-1 != yearIdxs.get(yearIdxs.size() -1)){
            // 마지막 일자의 인덱스를 가져온다.
            yearIdxs.add(yearMonthList.size()-1);
        }

        List<Integer> years = new ArrayList<>(); // 연단위 수익률
        List<Double> yearYield = new ArrayList<>(); // 연단위 수익률
        List<Double> kospiYearYield = new ArrayList<>(); // 연단위 코스피 수익률
        List<Double> kosdaqYearYield = new ArrayList<>(); // 연단위 코스닥 수익률

        log.info("monthYieldMoneys.size {}", monthYieldMoneys.size());
        log.info("yearMonthList {}", yearMonthList);
        log.info("yearMonthList Size {}", yearMonthList.size());
        log.info("연도 인덱스 {}", yearIdxs);

        for(int y = 0; y < yearIdxs.size(); y++){
            int targetYearIdx = yearIdxs.get(y);
            int previousYearIdx = (y == 0)? 0:yearIdxs.get(y-1);

        // 12월이 없는 경우 or 마지막 월이 12월이 아닌 경우 예외처리

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
