package com.project.minibacktesting_be.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Builder
@AllArgsConstructor
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
    private Double finalYield;

    @Column(nullable = false)
    private boolean myBest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @JsonManagedReference
    @OneToMany(mappedBy = "portfolio", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<PortStock> portStocks = new ArrayList<>();

    public void addPortStocks(List<PortStock> portStocks) {
        portStocks.addAll(portStocks);
    }

    public void updateFinalYield(Double finalYield) {
        this.finalYield = finalYield;
    }

    public static Portfolio createPortfolio(LocalDate startDate, LocalDate endDate, Long seedMoney
            , User user) {
        Portfolio portfolio = Portfolio.builder()
                .startDate(startDate)
                .endDate(endDate)
                .seedMoney(seedMoney)
                .myBest(false)
                .user(user)
                .build();
        return portfolio;
    }

}
