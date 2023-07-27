package com.ayad.stockservice.domain.repository.ifc;

import com.ayad.stockservice.domain.model.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
