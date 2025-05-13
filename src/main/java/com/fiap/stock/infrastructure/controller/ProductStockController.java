package com.fiap.stock.infrastructure.controller;

import com.fiap.stock.core.dto.CreateProductStockDTO;
import com.fiap.stock.core.dto.UpdateProductStockDTO;
import com.fiap.stock.core.entity.ProductStock;
import com.fiap.stock.core.usecase.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/v1/stocks")
public class ProductStockController {
    private final CreateProductStockUseCase createProductStockUseCase;
    private final FindAllStocksUseCase findAllStocksUseCase;
    private final FindStockUseCase findStockUseCase;
    private final DeleteProductStockUseCase deleteProductStockUseCase;
    private final AlterProductStockUseCase alterProductStockUseCase;


    public ProductStockController(CreateProductStockUseCase createProductStockUseCase, FindAllStocksUseCase findAllStocksUseCase, FindStockUseCase findStockUseCase, DeleteProductStockUseCase deleteProductStockUseCase, AlterProductStockUseCase alterProductStockUseCase) {
        this.createProductStockUseCase = createProductStockUseCase;
        this.findAllStocksUseCase = findAllStocksUseCase;
        this.findStockUseCase = findStockUseCase;
        this.deleteProductStockUseCase = deleteProductStockUseCase;
        this.alterProductStockUseCase = alterProductStockUseCase;
    }


    @GetMapping("{sku}")
    public ResponseEntity<ProductStock> findStock(@PathVariable String sku) {
        return ResponseEntity.ok(findStockUseCase.execute(sku));
    }

    @GetMapping
    public ResponseEntity<Set<ProductStock>> findAllStocks() {
        return ResponseEntity.ok(findAllStocksUseCase.execute());
    }

    @PostMapping
    public ResponseEntity<ProductStock> createProductStock(@RequestBody CreateProductStockDTO createProductStockDTO) {
        return ResponseEntity.ok(createProductStockUseCase.execute(createProductStockDTO));
    }

    @PutMapping("{sku}")
    public ResponseEntity<ProductStock> updateStock(@PathVariable String sku, @RequestBody UpdateProductStockDTO updateProductStockDTO) {
        return ResponseEntity.ok(alterProductStockUseCase.execute(sku, updateProductStockDTO));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProductStock(@PathVariable String id) {
        deleteProductStockUseCase.execute(id);
        return ResponseEntity.ok()
                .build();
    }
}
