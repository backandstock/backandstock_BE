package com.project.minibacktesting_be.dto.backtesting;


import com.project.minibacktesting_be.model.PortStock;
import com.project.minibacktesting_be.model.Portfolio;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
public class BacktestingRequestDto {

    private LocalDate startDate;
    private LocalDate endDate;
    private Long seedMoney;
    private List<String> stockList;
    private List<Integer> ratioList;
//    private Integer rebalancing;


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
    }


}