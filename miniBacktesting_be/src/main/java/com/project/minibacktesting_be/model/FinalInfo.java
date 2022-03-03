package com.project.minibacktesting_be.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Entity
public class FinalInfo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String stockName;

    @Column(nullable = false)
    private String stockCode;

    @Column(nullable = false)
    private Long publicDate;

    @Column(nullable = false)
    private String market;

}
