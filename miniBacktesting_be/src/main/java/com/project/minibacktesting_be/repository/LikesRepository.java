package com.project.minibacktesting_be.repository;

import com.project.minibacktesting_be.model.Likes;
import com.project.minibacktesting_be.model.Portfolio;
import com.project.minibacktesting_be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    List<Likes> findByPortfolio(Portfolio portfolio);
    List<Likes> findByPortfolioAndUser(Portfolio portfolio, User user);
}
