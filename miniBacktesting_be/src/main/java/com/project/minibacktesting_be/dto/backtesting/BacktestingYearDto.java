package com.project.minibacktesting_be.dto.backtesting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class BacktestingYearDto {

        private List<Integer> years; // 연단위 리스트
        private List<Double> yearYield; // 연단위 수익률
        private List<Double> kospiYearYield; // 연단위 코스피 수익률
        private List<Double> kosdaqYearYield; // 연단위 코스닥 수익률
}
