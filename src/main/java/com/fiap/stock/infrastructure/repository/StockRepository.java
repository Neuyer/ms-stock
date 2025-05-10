package com.fiap.stock.infrastructure.repository;

import com.fiap.stock.infrastructure.repository.model.ProductStockModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends MongoRepository<ProductStockModel, String> {
    Optional<ProductStockModel> findBySku(String sku);
}

