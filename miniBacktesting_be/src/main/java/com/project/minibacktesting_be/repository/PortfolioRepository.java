package com.project.minibacktesting_be.repository;

import com.project.minibacktesting_be.model.Portfolio;
import com.project.minibacktesting_be.model.User;
import org.apache.tomcat.jni.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    List<Portfolio> findAllByUser(User user);
    Page<Portfolio> findAllByMyBest(boolean mybest, Pageable pageable);

    Page<Portfolio> findAllByMyBestAndCreatedAtBetween(boolean mybest,
                                                       LocalDateTime start,
                                                       LocalDateTime end,
                                                       Pageable pageable);

    Page<Portfolio> findAllByMyBestOrderByCreatedAtDesc(boolean b, Pageable pageable);

    @Query("select distinct p " +
            "from Portfolio p join fetch p.portStocks " +
            "where p.user = :user")
    List<Portfolio> findPortfolioFetchPortStock(@Param("user") User user);

}
