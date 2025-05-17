package com.fiap.stock.core.usecase;

import com.fiap.stock.core.entity.ProductStock;
import com.fiap.stock.core.gateway.StockGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class FindAllStocksUseCaseTest {

    private StockGateway stockGateway;


    private FindAllStocksUseCase findAllStocksUseCase;

    @BeforeEach
    void setUp() {
        stockGateway = mock(StockGateway.class);
        findAllStocksUseCase = new FindAllStocksUseCase(stockGateway);
    }

    @Test
    void execute_noStocksExist_returnsEmptySet() {
        when(stockGateway.findAll()).thenReturn(Set.of());

        Set<ProductStock> result = findAllStocksUseCase.execute();

        assertTrue(result.isEmpty());
        verify(stockGateway, times(1)).findAll();
    }

    @Test
    void execute_multipleStocksExist_returnsSetOfProductStocks() {
        Set<ProductStock> expectedStocks = new HashSet<>();
        expectedStocks.add(new ProductStock(UUID.randomUUID().toString(), "SKU1", "Product 1", 10));
        expectedStocks.add(new ProductStock(UUID.randomUUID().toString(), "SKU2", "Product 2", 5));
        when(stockGateway.findAll()).thenReturn(expectedStocks);

        Set<ProductStock> result = findAllStocksUseCase.execute();

        assertEquals(expectedStocks.size(), result.size());
        assertTrue(result.containsAll(expectedStocks));
        verify(stockGateway, times(1)).findAll();
    }

    @Test
    void execute_singleStockExists_returnsSetWithOneProductStock() {
        Set<ProductStock> expectedStocks = Set.of(new ProductStock(UUID.randomUUID().toString(), "SKU3", "Product 3", 15));
        when(stockGateway.findAll()).thenReturn(expectedStocks);

        Set<ProductStock> result = findAllStocksUseCase.execute();

        assertEquals(1, result.size());
        assertTrue(result.containsAll(expectedStocks));
        verify(stockGateway, times(1)).findAll();
    }
}
