package com.project.minibacktesting_be.repository;

import com.project.minibacktesting_be.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteAllByPortfolioId(Long portfolioId);
}
