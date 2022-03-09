package com.project.minibacktesting_be.dto.community;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class TopFiveResponseDto {

    List<String> stockNames;
    List<String> stockCodes;
    List<String> results;
    List<Long> closes;
}
