package com.warehouse.inventory.repository;

import com.warehouse.inventory.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByLocation_NameAndProduct_Code(String locationName, String productCode);

    Optional<List<Stock>> findByProduct_Code(String productCode);
}
