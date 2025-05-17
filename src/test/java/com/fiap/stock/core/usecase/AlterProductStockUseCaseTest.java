package com.fiap.stock.core.usecase;

import com.fiap.stock.core.dto.UpdateProductStockDTO;
import com.fiap.stock.core.entity.ProductStock;
import com.fiap.stock.core.entity.StockOperation;
import com.fiap.stock.core.gateway.StockGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AlterProductStockUseCaseTest {

    private StockGateway stockGateway;
    private AlterProductStockUseCase alterProductStockUseCase;

    @BeforeEach
    void setUp() {
        stockGateway = mock(StockGateway.class);
        alterProductStockUseCase = new AlterProductStockUseCase(stockGateway);
    }


    private final String validSku = "TEST-SKU";
    private final String validName = "Test Product";
    private final String validId = UUID.randomUUID().toString();
    private final int initialQuantity = 10;

    private ProductStock createTestProductStock() {
        return new ProductStock(validId, validSku, validName, initialQuantity);
    }

    private UpdateProductStockDTO createUpdateDTO(int quantity, StockOperation operation) {
        return new UpdateProductStockDTO(operation, quantity);
    }

    @Test
    void execute_increaseOperation_stockFound_increasesStockAndSaves() {
        ProductStock existingStock = createTestProductStock();
        UpdateProductStockDTO inputDTO = createUpdateDTO(5, StockOperation.INCREASE);

        when(stockGateway.findBySku(validSku)).thenReturn(Optional.of(existingStock));

        ProductStock result = alterProductStockUseCase.execute(validSku, inputDTO);

        assertEquals(initialQuantity + 5, result.getQuantity());
        verify(stockGateway, times(1)).findBySku(validSku);
        verify(stockGateway, times(1)).save(existingStock);
    }

    @Test
    void execute_decreaseOperation_stockFound_decreasesStockAndSaves() {
        ProductStock existingStock = createTestProductStock();
        UpdateProductStockDTO inputDTO = createUpdateDTO(3, StockOperation.DECREASE);

        when(stockGateway.findBySku(validSku)).thenReturn(Optional.of(existingStock));

        ProductStock result = alterProductStockUseCase.execute(validSku, inputDTO);

        assertEquals(initialQuantity - 3, result.getQuantity());
        verify(stockGateway, times(1)).findBySku(validSku);
        verify(stockGateway, times(1)).save(existingStock);
    }

    @Test
    void execute_decreaseOperation_stockFound_insufficientStock_throwsIllegalArgumentException() {
        ProductStock existingStock = createTestProductStock();
        UpdateProductStockDTO inputDTO = createUpdateDTO(15, StockOperation.DECREASE);

        when(stockGateway.findBySku(validSku)).thenReturn(Optional.of(existingStock));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> alterProductStockUseCase.execute(validSku, inputDTO));

        assertEquals("Stock for sku:" + validSku + " is less than required: 15", exception.getMessage());
        verify(stockGateway, times(1)).findBySku(validSku);
        verify(stockGateway, never()).save(any());

    }

    @Test
    void execute_stockNotFound_throwsIllegalStateException() {
        UpdateProductStockDTO inputDTO = createUpdateDTO(5, StockOperation.INCREASE);

        when(stockGateway.findBySku(validSku)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> alterProductStockUseCase.execute(validSku, inputDTO));

        assertEquals("Stock not found for sku: " + validSku, exception.getMessage());
        verify(stockGateway, times(1)).findBySku(validSku);
        verify(stockGateway, never()).save(any());
    }

    @Test
    void execute_nullStockOperation_throwsIllegalArgumentException() {
        ProductStock existingStock = createTestProductStock();
        UpdateProductStockDTO inputDTO = new UpdateProductStockDTO(null, 5);

        when(stockGateway.findBySku(validSku)).thenReturn(Optional.of(existingStock));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> alterProductStockUseCase.execute(validSku, inputDTO));

        assertEquals("No operation informed", exception.getMessage());
        verify(stockGateway, times(1)).findBySku(validSku);
        verify(stockGateway, never()).save(any());
    }

    @Test
    void decrease_sufficientStock_decreasesQuantity() throws Exception {
        java.lang.reflect.Method method = AlterProductStockUseCase.class.getDeclaredMethod("decrease", ProductStock.class, int.class);
        method.setAccessible(true);

        ProductStock productStock = createTestProductStock();
        method.invoke(alterProductStockUseCase, productStock, 3);

        assertEquals(initialQuantity - 3, productStock.getQuantity());
    }
}
