package com.project.minibacktesting_be.repository;

import com.project.minibacktesting_be.model.PortStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortStockRepository extends JpaRepository<PortStock, Long> {
}
