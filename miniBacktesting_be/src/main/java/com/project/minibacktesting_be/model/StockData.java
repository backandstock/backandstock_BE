package com.project.minibacktesting_be.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class StockData {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "stockData_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stockInfo_id")
    private StockInfo stockinfo;

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

    @Column(nullable = false)
    private Long transaction;
}
