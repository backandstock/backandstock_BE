package com.project.minibacktesting_be.repository;

import com.project.minibacktesting_be.model.Portfolio;
import com.project.minibacktesting_be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findAllByUser(User user);
}
