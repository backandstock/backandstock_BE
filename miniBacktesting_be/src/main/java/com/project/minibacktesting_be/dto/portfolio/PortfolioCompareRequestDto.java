package com.project.minibacktesting_be.dto.portfolio;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class PortfolioCompareRequestDto {
    private List<Long> portIdList;
}
