package com.project.minibacktesting_be.dto.portfolio;

import lombok.*;

import java.time.YearMonth;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioLowYieldDto {
    private Long portId;
    private Double lowYield;
    private YearMonth lowYieldDate;
}
