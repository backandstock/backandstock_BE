package com.project.minibacktesting_be.repository;

import com.project.minibacktesting_be.model.Comment;
import com.project.minibacktesting_be.model.StockData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockDataRepository extends JpaRepository<StockData, Long> {
}
