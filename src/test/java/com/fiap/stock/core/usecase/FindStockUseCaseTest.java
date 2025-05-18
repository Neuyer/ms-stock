package com.fiap.stock.core.usecase;

import com.fiap.stock.core.entity.ProductStock;
import com.fiap.stock.core.gateway.StockGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindStockUseCaseTest {
    private StockGateway stockGateway;
    private FindStockUseCase findStockUseCase;

    private final String validSku = "TEST-SKU";
    private final String invalidSku = "NON-EXISTENT-SKU";
    private final String validName = "Test Product";
    private final String validId = UUID.randomUUID().toString();
    private final int validQuantity = 10;

    private ProductStock createTestProductStock() {
        return new ProductStock(validId, validSku, validName, validQuantity);
    }

    @BeforeEach
    void setUp() {
        stockGateway = mock(StockGateway.class);
        findStockUseCase = new FindStockUseCase(stockGateway);
    }

    @Test
    void execute_existingSku_returnsProductStock() {
        ProductStock expectedStock = createTestProductStock();
        when(stockGateway.findBySku(validSku)).thenReturn(Optional.of(expectedStock));

        ProductStock actualStock = findStockUseCase.execute(validSku);

        assertNotNull(actualStock);
        assertEquals(expectedStock, actualStock);
        verify(stockGateway, times(1)).findBySku(validSku);
    }

    @Test
    void execute_nonExistingSku_throwsIllegalStateException() {
        when(stockGateway.findBySku(invalidSku)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> findStockUseCase.execute(invalidSku));

        assertEquals("Stock not found with sku: " + invalidSku, exception.getMessage());
        verify(stockGateway, times(1)).findBySku(invalidSku);
    }

    @Test
    void execute_nullSku_throwsIllegalArgumentExceptionFromGateway() {
        when(stockGateway.findBySku(null)).thenThrow(new IllegalArgumentException("SKU cannot be null"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> findStockUseCase.execute(null));

        assertEquals("SKU cannot be null", exception.getMessage());
        verify(stockGateway, times(1)).findBySku(null);
    }

    @Test
    void execute_emptySku_throwsIllegalArgumentExceptionFromGateway() {
        when(stockGateway.findBySku("")).thenThrow(new IllegalArgumentException("SKU cannot be empty"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> findStockUseCase.execute(""));

        assertEquals("SKU cannot be empty", exception.getMessage());
        verify(stockGateway, times(1)).findBySku("");
    }
}
