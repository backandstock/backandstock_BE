package com.project.minibacktesting_be.repository;

import com.project.minibacktesting_be.model.Portfolio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioReposirory extends JpaRepository<Portfolio, Long> {
    Page<Portfolio> findAllByMyBest(boolean mybest, Pageable pageable);
}
