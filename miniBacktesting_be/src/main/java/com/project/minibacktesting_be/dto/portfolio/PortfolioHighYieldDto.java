package com.project.minibacktesting_be.dto.portfolio;

import lombok.*;

import java.time.YearMonth;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioHighYieldDto {
    private Long portId;
    private Double highYield;
    private YearMonth highYieldDate;

}
