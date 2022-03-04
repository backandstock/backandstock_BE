package com.project.minibacktesting_be.repository;

import com.project.minibacktesting_be.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioReposirory extends JpaRepository<Portfolio, Long> {
}
