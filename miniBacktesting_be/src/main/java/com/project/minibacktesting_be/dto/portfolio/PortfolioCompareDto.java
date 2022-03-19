package com.project.minibacktesting_be.dto.portfolio;

import lombok.*;

import java.util.List;

public class PortfolioCompareDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Request {
        private List<Long> portIdList;
    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Response {
        private List<PortfolioRankDto> portfolioRanks;
        private PortfolioHighYieldDto portfolioHighYield;
        private PortfolioLowYieldDto portfolioLowYield;
    }
}
