package com.project.minibacktesting_be.repository;

import com.project.minibacktesting_be.dto.StockSearchResponseDto;
import com.project.minibacktesting_be.model.Stock;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByStockNameAndCloseDateBetweenOrderByCloseDate(String targetStockName, LocalDate startDate, LocalDate endDate);

    @Query("select new com.project.minibacktesting_be.dto.StockSearchResponseDto(t.stockName, t.stockCode) " +
            "from Stock t " +
            "where t.stockCode like :keyword% " +
            "group by t.stockCode, t.stockName " +
            "order by t.stockCode ASC ")
    List<StockSearchResponseDto> findStockByCode(@Param("keyword") String keyword, Pageable pageable);

    @Query("select new com.project.minibacktesting_be.dto.StockSearchResponseDto(t.stockName, t.stockCode) " +
            "from Stock t " +
            "where t.stockName like :keyword% " +
            "group by t.stockCode, t.stockName " +
            "order by t.stockName ASC ")
    List<StockSearchResponseDto> findStockByName(@Param("keyword") String keyword, Pageable pageable);
}