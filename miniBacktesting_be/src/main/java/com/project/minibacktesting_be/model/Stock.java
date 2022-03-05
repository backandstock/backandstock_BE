package com.project.minibacktesting_be.model;


import javax.persistence.*;
import java.time.LocalDate;

public class Stock {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "stockData_id")
    private Long id;

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

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate publicDate;

    @Column(nullable = false)
    private String market;

}
