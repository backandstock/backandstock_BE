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
@Setter
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

    @Column(nullable = false)
    private Long likesCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "portfolio", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

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
                .likesCnt(0L)
                .myBest(false)
                .user(user)
                .build();
        return portfolio;
    }

    public Boolean getMyBest(){
        return this.myBest;
    }

    public void updateMyBest(Boolean myBest){
        this.myBest = myBest;
    }

    public void updateLikesCnt(Long likesCnt){
        this.likesCnt = likesCnt;
    }
}
