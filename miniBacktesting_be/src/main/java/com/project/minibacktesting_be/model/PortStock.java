package com.project.minibacktesting_be.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;

@NoArgsConstructor
@Getter
@Entity
public class PortStock {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private BigInteger id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PORTFOLIO_ID")
    private Portfolio portfolio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STOCKIN_ID")
    private StockInfo stockInfo;
}
