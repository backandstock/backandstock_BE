package com.project.minibacktesting_be.dto.backtesting;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.YearMonthDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.YearMonthSerializer;
import lombok.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.YearMonth;
import java.util.List;

// Java 8 date/time type `java.time.Instant` not supported by default Issue : [duplicate]
// https://m.blog.naver.com/writer0713/221615276956
// https://stackoverflow.com/questions/27952472/serialize-deserialize-java-8-java-time-with-jackson-json-mapper

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BacktestingResponseDto {


//    @JsonSerialize(using =  YearMonthSerializer.class)
//    @JsonDeserialize(using = YearMonthDeserializer.class)
    private String startDate; // 시작 일자 (주식을 산 날짜)

//    @JsonSerialize(using =  YearMonthSerializer.class)
//    @JsonDeserialize(using = YearMonthDeserializer.class)
    private String endDate; // 종료 일자 (주식을 파는 날짜)

//    @JsonSerialize(using =  YearMonthSerializer.class)
//    @JsonDeserialize(using = YearMonthDeserializer.class)
    private String bestMonth; // 최고의 수익금을 기록한 달

    private Double bestMoney; // 최고의 수익금

//    @JsonSerialize(using =  YearMonthSerializer.class)
//    @JsonDeserialize(using = YearMonthDeserializer.class)
    private String worstMonth; // 최악의 수익금을 기록한달
    private Double worstMoney; // 최악의 수익금
    private Long seedMoney; // 초기 자본

    private List<String> stockNames; // 투자 주식 명
    private List<String> stockCodes; // 투자 주식 코드
    private List<Double> buyMoney; // 주식을 구매한 비용

    // 수익률 : (현재 - 이전) /이전
    private Double finalMoney; // 마지막 수익금
    private Double yieldMoney; // 마지막 수익금 - 수익률
    private Double finalYield; // 마지막 수익률 (초기 자본 대비)

    private List<String> months; // 투자기간 월 리스트
    private List<Double> monthYield; // 월 수익률
    private List<Double> monthYieldMoney; // 월 수익금


    private List<Double> kospiYield; // kospi 투자시 수익률
    private List<Double> kospiYieldMoney; // kospi 투자시 수익금
    private List<Double> kosdaqYield; // kosdaq 투자시 수익률
    private List<Double> kosdaqYieldMoney; // kosdaq 투자시 수익금

    private List<Double> stockYieldMoneys; // 주식별 최종 수익

    private List<Integer> years; // 연단위 리스트
    private List<Double> yearYield; // 연단위 수익률
    private List<Double> kospiYearYield; // 연단위 코스피 수익률
    private List<Double> kosdaqYearYield; // 연단위 코스닥 수익률

    
}
