package com.project.minibacktesting_be.dto.portfolio;

import com.project.minibacktesting_be.dto.backtesting.BacktestingResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PortfolioResponseDto {

    private Long portId;
    private boolean myBest;
    private BacktestingResponseDto portBacktestingCal;

}
