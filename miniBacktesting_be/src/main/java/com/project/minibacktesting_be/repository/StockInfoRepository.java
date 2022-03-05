package com.project.minibacktesting_be.repository;

import com.project.minibacktesting_be.model.StockInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockInfoRepository extends JpaRepository<StockInfo, Long> {
    StockInfo findByStockName(String stockName);
}
