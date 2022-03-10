package com.project.minibacktesting_be.dto.backtesting;
import lombok.*;

import java.time.YearMonth;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BacktestingDataDto {

    private String stockName; // 해당 주식의 정보
    private Double targetPrice;  // 해당 주식을 목표 금액
    private Double stockNum; // 목표 금액 만큼 사려면 몇주를 가지고 있어야 하지?
    private List<YearMonth> months; // 주식의 일자들
    private List<Long> stockPrices; // 해당 기간의 주식 데이터 리스트
    private List<Double> yieldMoneys; // 월마다 해당 주식의 수익금
    private List<Double> yields; // 월 수익률

}