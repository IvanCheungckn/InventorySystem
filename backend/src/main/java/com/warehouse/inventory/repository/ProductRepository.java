package com.warehouse.inventory.repository;

import com.warehouse.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByCode(String code);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM product WHERE product.short_name = :shortName")
    long countShortName(@Param("shortName") String shortName);

    void deleteByCode(String code);
}
