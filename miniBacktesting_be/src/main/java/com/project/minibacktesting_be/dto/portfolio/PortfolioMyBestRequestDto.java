package com.project.minibacktesting_be.dto.portfolio;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PortfolioMyBestRequestDto {
    private Long portId;
    private boolean myBest;
}
