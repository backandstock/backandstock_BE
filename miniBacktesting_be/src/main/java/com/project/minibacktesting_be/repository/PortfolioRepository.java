package com.project.minibacktesting_be.repository;

import com.project.minibacktesting_be.model.Portfolio;
import com.project.minibacktesting_be.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    List<Portfolio> findAllByUser(User user);
    Page<Portfolio> findAllByMyBest(boolean mybest, Pageable pageable);
}
