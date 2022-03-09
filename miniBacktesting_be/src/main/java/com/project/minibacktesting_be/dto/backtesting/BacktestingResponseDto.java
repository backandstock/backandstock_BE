package com.project.minibacktesting_be.dto.backtesting;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.YearMonth;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BacktestingResponseDto {

    private YearMonth startDate; // 시작 일자 (주식을 산 날짜)
    private YearMonth endDate; // 종료 일자 (주식을 파는 날짜)
    private YearMonth bestMonth; // 최고의 수익금을 기록한 달
    private Double bestMoney; // 최고의 수익금
    private YearMonth worstMonth; // 최악의 수익금을 기록한달
    private Double worstMoney; // 최악의 수익금
    private Long seedMoney; // 초기 자본

    private List<String> stockNames; // 투자 주식 명
    private List<String> stockCodes; // 투자 주식 코드
    private List<Double> buyMoney; // 주식을 구매한 비용


    private Double finalMoney; // 마지막 수익금
    private Double yieldMoney; // 마지막 수익금 - 수익률
    private Double finalYield; // 마지막 수익률 (초기 자본 대비)

    private List<YearMonth> months; // 투자기간 월 리스트
    private List<Double> monthYield; // 월 수익률
    private List<Double> monthYieldMoney; // 월 수익금


    private List<Double> kospiYield; // kospi 투자시 수익률
    private List<Double> kospiYieldMoney; // kospi 투자시 수익금
    private List<Double> kosdaqYield; // kosdaq 투자시 수익률
    private List<Double> kosdaqYieldMoney; // kosdaq 투자시 수익금


}
