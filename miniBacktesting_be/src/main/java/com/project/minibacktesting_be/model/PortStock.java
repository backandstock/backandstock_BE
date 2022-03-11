package com.project.minibacktesting_be.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;

@NoArgsConstructor
@Getter
@Entity
public class PortStock {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "portStock_id")
    private Long id;

    @Column(nullable = false)
    private Long ratio;

    @Column(nullable = false)
    private String stockName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PORTFOLIO_ID")
    private Portfolio portfolio;

}
