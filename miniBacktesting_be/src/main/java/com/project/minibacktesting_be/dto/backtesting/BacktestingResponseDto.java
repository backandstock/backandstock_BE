package com.project.minibacktesting_be.dto.backtesting;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;
import java.time.YearMonth;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BacktestingResponseDto {

    private YearMonth startDate;
    private YearMonth endDate;
    private YearMonth bestMonth;
    private Double bestMoney;
    private YearMonth worstMonth;
    private Double worstMoney;
    private Long seedMoney;

    private List<String> stockNames;
    private List<String> stockCodes;
    private List<Double> buyMoney;


    private Double finalMoney;
    private Double yieldMoney;
    private Double finalYield;

    private List<YearMonth> months;
    private List<Double> monthYield;
    private List<Double> monthYieldMoney;


    private List<Double> kospiYield;
    private List<Double> kospiYieldMoney;
    private List<Double> kosdaqYield;
    private List<Double> kosdaqYieldMoney;


}
