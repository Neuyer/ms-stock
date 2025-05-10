package com.fiap.stock.infrastructure.repository.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("stocks")
public class ProductStockModel {
    @Id
    private String id;
    @Indexed(unique = true)
    String sku;
    String name;
    int quantity;
}
