package com.project.minibacktesting_be.repository;

import com.project.minibacktesting_be.model.PortStock;
import com.project.minibacktesting_be.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByStockNameAndCloseDateBetweenOrderByCloseDate(String targetStockName, LocalDate startDate, LocalDate endDate);
}
