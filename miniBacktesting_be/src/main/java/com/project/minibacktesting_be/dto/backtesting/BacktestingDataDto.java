package com.project.minibacktesting_be.dto.backtesting;

import com.project.minibacktesting_be.model.StockData;
import com.project.minibacktesting_be.model.StockInfo;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BacktestingDataDto {

    private StockInfo stockInfo; // 해당 주식의 정보
    private Double targetPrice;  // 해당 주식을 목표 금액
    private Long stockNum; // 주식 개수
    private List<StockData> stockDatas; // 해당 기간의 주식 데이터 리스트
    private List<Long> stockBuyPrices;
    private Long stockBuyPrice; //해당 주식의 매입 총 가격

}
