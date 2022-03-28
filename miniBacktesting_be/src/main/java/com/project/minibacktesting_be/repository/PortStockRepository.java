package com.project.minibacktesting_be.repository;

import com.project.minibacktesting_be.model.PortStock;
import com.project.minibacktesting_be.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortStockRepository extends JpaRepository<PortStock, Long> {


    List<PortStock> findByPortfolio(Portfolio portfolio);

    void deleteAllByPortfolioId(Long portfolioId);
}