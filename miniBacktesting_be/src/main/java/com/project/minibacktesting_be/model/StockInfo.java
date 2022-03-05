package com.project.minibacktesting_be.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class StockInfo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "stockInfo_id")
    private Long id;

    @JsonManagedReference
    @OneToMany(mappedBy = "stockData", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<StockData> stockDatas;


    @Column(nullable = false)
    private String stockName;

    @Column(nullable = false)
    private String stockCode;

    @Column(nullable = false)
    private LocalDate publicDate;

    @Column(nullable = false)
    private String market;

}
