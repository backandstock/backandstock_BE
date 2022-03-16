package com.project.minibacktesting_be.dto.community;

import lombok.*;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // 생성자가 있어야 redis에서 자료를 가져 올 수 있다.
public class CommunityPortDto {

    String nickname;
    Long portId;
    List<String> stockList;
    List<Integer> ratioList;
    Double finalYield;
    Integer finalYieldMoney;
    Long seedMoney;


    String startDate;

    String endDate;

//    List<Double> monthYieldMoney;

    String createdAt;



}
