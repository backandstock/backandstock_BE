package com.project.minibacktesting_be.dto.backtesting;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BacktestingRequestDto {

    private LocalDate startDate;
    private LocalDate endDate;
    private Long seedMoney;
    private List<String> stockList;
    private List<Integer> ratioList;

}