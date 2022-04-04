package com.project.minibacktesting_be.dto.backtesting;


import com.project.minibacktesting_be.model.Portfolio;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class BacktestingRequestDto {

    private LocalDate startDate;
    private LocalDate endDate;
    private Long seedMoney;
    private List<String> stockList;
    private List<Integer> ratioList;
    private Integer rebalancingMonth;


    public BacktestingRequestDto(Portfolio portfolio) {

        this.startDate = portfolio.getStartDate();
        this.endDate = portfolio.getEndDate();
        this.seedMoney = portfolio.getSeedMoney();
        this.stockList = portfolio.getPortStocks().
                stream().
                map(s -> s.getStockName()).
                collect(Collectors.toList());

        this.ratioList = portfolio.getPortStocks().
                stream().
                map(s -> s.getRatio()).
                collect(Collectors.toList());
        this.rebalancingMonth = portfolio.getRebalancingMonth();
    }

//    public BacktestingRequestDto(LocalDate startDate, Local) {
//
//        this.startDate = start
//        this.endDate = portfolio.getEndDate();
//        this.seedMoney = portfolio.getSeedMoney();
//        this.stockList = portfolio.getPortStocks()
//
//        this.ratioList = portfolio.getPortStocks().
//                stream().
//                map(s -> s.getRatio()).
//                collect(Collectors.toList());
//        this.rebalancingMonth = portfolio.getRebalancingMonth();
//    }



}