package com.project.minibacktesting_be.repository;

import com.project.minibacktesting_be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
