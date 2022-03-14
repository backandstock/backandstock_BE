package com.project.minibacktesting_be.dto.portfolio;


import com.project.minibacktesting_be.dto.backtesting.BacktestingResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PortfolioDetailsResponseDto {

    private Long portId;
    private boolean myBest;
    private int likesCnt;
    private int commentCnt;
    private BacktestingResponseDto portBacktestingCal;

}
