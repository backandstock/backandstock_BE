package com.project.minibacktesting_be.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockSearchResponseDto {

    private String stockName;
    private String stockCode;

    public StockSearchResponseDto(String stockName, String stockCode) {
        this.stockName = stockName;
        this.stockCode = stockCode;
    }

}
