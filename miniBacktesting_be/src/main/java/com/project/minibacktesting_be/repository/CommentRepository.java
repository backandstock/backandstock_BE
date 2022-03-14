package com.project.minibacktesting_be.repository;

import com.project.minibacktesting_be.model.Comment;
import com.project.minibacktesting_be.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPortfolio(Portfolio portfolio);
}
