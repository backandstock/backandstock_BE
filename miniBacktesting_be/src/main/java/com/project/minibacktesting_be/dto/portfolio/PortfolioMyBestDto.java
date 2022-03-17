package com.project.minibacktesting_be.dto.portfolio;

import lombok.*;

public class PortfolioMyBestDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Request {
        private Long portId;
        private boolean myBest;
    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Response {
        private boolean myBest;
    }
}
