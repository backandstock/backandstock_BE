package com.project.minibacktesting_be.dto.portfolio;

import lombok.*;

import java.time.YearMonth;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioRankDto {
    private Long portId;
    private Double finalYield;
    private Double finalMoney;
    private List<String> stockName;
    private List<Integer> stockRatio;
    private YearMonth startDate;
    private YearMonth endDate;
}
