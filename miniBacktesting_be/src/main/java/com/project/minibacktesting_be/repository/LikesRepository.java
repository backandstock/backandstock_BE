package com.project.minibacktesting_be.repository;

import com.project.minibacktesting_be.model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {
}
