package com.project.minibacktesting_be.dto.community;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
public class CommunityPortResponseDto {

    List<String> stockList;
    List<Integer> ratioList;
    Double finalYield;
    Long seedMoney;
    LocalDate startDate;
    LocalDate endDate;
    List<Double> monthYieldMoney;

}
