package com.project.minibacktesting_be.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class PortStock {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "PORTSTOCK_ID")
    private Long id;

    @Column(nullable = false)
    private Integer ratio;

    @Column(nullable = false)
    private String stockName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PORTFOLIO_ID")
    private Portfolio portfolio;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "STOCK_ID")
//    private Stock stock;

}
