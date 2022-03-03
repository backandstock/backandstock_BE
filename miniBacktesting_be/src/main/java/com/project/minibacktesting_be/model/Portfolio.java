package com.project.minibacktesting_be.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Portfolio extends Timestamped{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "PORTFOLIO_ID")
    private Long id;

    @Column(nullable = false)
    private Long seedMoney;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private int bestMonth;

    @Column(nullable = false)
    private int worstMonth;

    @Column(nullable = false)
    private boolean myBest;

//    @JsonManagedReference
//    @OneToMany(mappedBy = "portfolio", orphanRemoval = true, cascade = CascadeType.ALL)
//    private List<PortStock> portStocks;

    public Portfolio(Long seedMoney,
                     LocalDate startDate,
                     LocalDate endDate,
                     int bestMonth,
                     int worstMonth,
                     boolean myBest){
        this.seedMoney = seedMoney;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bestMonth = bestMonth;
        this.worstMonth = worstMonth;
        this.myBest = myBest;
    }
}
