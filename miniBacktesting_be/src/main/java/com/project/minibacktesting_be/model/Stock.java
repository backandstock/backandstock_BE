package com.project.minibacktesting_be.model;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
@Table(indexes = {@Index(name = "searchName", columnList = "stockName"),
        @Index(name = "searchCode", columnList = "stockCode"),
        @Index(name = "top5MarketYield", columnList = "market,closeDate"),
        @Index(name = "top5Volume", columnList = "closeDate"),
        @Index(name = "top5Transaction", columnList = "closeDate"),
        @Index(name = "backtestingCal", columnList = "closeDate,stockName")})

public class Stock {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private LocalDate closeDate;

    @Column(nullable = false)
    private Long high;

    @Column(nullable = false)
    private Long low;

    @Column(nullable = false)
    private Long open;

    @Column(nullable = false)
    private Long close;

    @Column(nullable = false)
    private Long volume;

    @Column
    private Double yieldPct;

    @Column(nullable = false)
    private Long transaction;

    @Column(nullable = false)
    private String stockName;

    @Column(nullable = false)
    private String stockCode;

    @Column(nullable = false)
    private LocalDate publicDate;

    @Column(nullable = false)
    private String market;

}
