package com.fiap.stock.core.gateway;

import com.fiap.stock.core.entity.ProductStock;

import java.util.Optional;
import java.util.Set;

public interface StockGateway {
    Optional<ProductStock> findBySku(String sku);

    Optional<ProductStock> findById(String id);

    Set<ProductStock> findAll();

    void save(ProductStock client);

    void deleteById(String id);
}
