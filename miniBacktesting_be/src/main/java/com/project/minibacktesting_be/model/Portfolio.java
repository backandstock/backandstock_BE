package com.project.minibacktesting_be.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Portfolio {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private BigInteger id;

    @Column(nullable = false)
    private BigInteger seedMoney;

    @Column(nullable = false)
    private BigInteger startDate;

    @Column(nullable = false)
    private BigInteger endDate;

    @Column(nullable = false)
    private BigInteger bestMonth;

    @Column(nullable = false)
    private BigInteger worstMonth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

//    @JsonManagedReference
//    @OneToMany(mappedBy = "portfolio", orphanRemoval = true, cascade = CascadeType.ALL)
//    private List<PortStock> portStocks;

    public Portfolio(BigInteger seedMoney,
                     BigInteger startDate,
                     BigInteger endDate,
                     BigInteger bestMonth,
                     BigInteger worstMonth,
                     User user){
        this.seedMoney = seedMoney;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bestMonth = bestMonth;
        this.worstMonth = worstMonth;
        this.user = user;
    }
}
