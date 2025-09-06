package com.matheusluizroza.ecommerce_api.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matheusluizroza.ecommerce_api.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    public Optional<Product> findByName(String name);

    public List<Product> findByNameContaining(String name);

    public List<Product> findAllByStockGreaterThan(Integer stock);

    public List<Product> findAllByPriceBetween(BigDecimal min, BigDecimal max);

    public List<Product> findAllByOrderByPriceAsc();

    public List<Product> findAllByOrderByPriceDesc();

    public boolean existsByName(String name);
}
