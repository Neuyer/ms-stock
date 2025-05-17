package com.fiap.stock.core.usecase;

import com.fiap.stock.core.entity.ProductStock;
import com.fiap.stock.core.gateway.StockGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteProductStockUseCaseTest {

    private StockGateway stockGateway;

    private DeleteProductStockUseCase deleteProductStockUseCase;

    private final String validId = UUID.randomUUID().toString();
    private final String invalidId = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        stockGateway = mock(StockGateway.class);
        deleteProductStockUseCase = new DeleteProductStockUseCase(stockGateway);
    }

    @Test
    void execute_existingId_deletesStockSuccessfully() {
        when(stockGateway.findById(validId)).thenReturn(Optional.of(new ProductStock(validId, "sku", "name", 1)));
        doNothing().when(stockGateway).deleteById(validId);

        deleteProductStockUseCase.execute(validId);

        verify(stockGateway, times(1)).findById(validId);
        verify(stockGateway, times(1)).deleteById(validId);
    }

    @Test
    void execute_nonExistingId_throwsIllegalStateException() {
        when(stockGateway.findById(invalidId)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> deleteProductStockUseCase.execute(invalidId));

        assertEquals("Stock not found with id: " + invalidId, exception.getMessage());
        verify(stockGateway, times(1)).findById(invalidId);
        verify(stockGateway, never()).deleteById(anyString());
    }

    @Test
    void execute_nullId_throwsIllegalArgumentExceptionFromGateway() {
        when(stockGateway.findById(null)).thenThrow(new IllegalArgumentException("ID cannot be null"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> deleteProductStockUseCase.execute(null));

        assertEquals("ID cannot be null", exception.getMessage());
        verify(stockGateway, times(1)).findById(null);
        verify(stockGateway, never()).deleteById(anyString());
    }

    @Test
    void execute_emptyId_throwsIllegalArgumentExceptionFromGateway() {
        when(stockGateway.findById("")).thenThrow(new IllegalArgumentException("ID cannot be empty"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> deleteProductStockUseCase.execute(""));

        assertEquals("ID cannot be empty", exception.getMessage());
        verify(stockGateway, times(1)).findById("");
        verify(stockGateway, never()).deleteById(anyString());
    }
}
