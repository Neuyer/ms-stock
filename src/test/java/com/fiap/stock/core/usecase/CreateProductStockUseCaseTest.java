package com.fiap.stock.core.usecase;

import com.fiap.stock.core.dto.CreateProductStockDTO;
import com.fiap.stock.core.entity.ProductStock;
import com.fiap.stock.core.gateway.StockGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateProductStockUseCaseTest {

    private StockGateway stockGateway;
    private CreateProductStockUseCase createProductStockUseCase;

    private final String validSku = "TEST-SKU";
    private final String validName = "Test Product";
    private final int validQuantity = 10;

    @BeforeEach
    void setUp() {
        stockGateway = mock(StockGateway.class);
        createProductStockUseCase = new CreateProductStockUseCase(stockGateway);
    }

    private CreateProductStockDTO createValidCreateProductStockDTO() {
        return new CreateProductStockDTO(validSku, validName, validQuantity);
    }


    @Test
    void execute_validInput_stockAlreadyExists_throwsIllegalStateException() {
        CreateProductStockDTO inputDTO = createValidCreateProductStockDTO();
        ProductStock existingStock = new ProductStock(UUID.randomUUID().toString(), validSku, validName, 5);
        when(stockGateway.findBySku(validSku)).thenReturn(Optional.of(existingStock));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> createProductStockUseCase.execute(inputDTO));

        assertEquals("Stock for sku already exists", exception.getMessage());
        verify(stockGateway, times(1)).findBySku(validSku);
        verify(stockGateway, never()).save(any());

    }

    @Test
    void execute_nullInputSku_throwsIllegalArgumentExceptionFromProductStockConstructor() {
        CreateProductStockDTO inputDTO = new CreateProductStockDTO(null, validName, validQuantity);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> createProductStockUseCase.execute(inputDTO));

        assertEquals("SKU cannot be null or empty.", exception.getMessage());
        verify(stockGateway, never()).findBySku(any());
        verify(stockGateway, never()).save(any());

    }

    @Test
    void execute_emptyInputSku_throwsIllegalArgumentExceptionFromProductStockConstructor() {
        CreateProductStockDTO inputDTO = new CreateProductStockDTO("  ", validName, validQuantity);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> createProductStockUseCase.execute(inputDTO));

        assertEquals("SKU cannot be null or empty.", exception.getMessage());
        verify(stockGateway, never()).findBySku(any());
        verify(stockGateway, never()).save(any());

    }

    @Test
    void execute_nullInputName_throwsIllegalArgumentExceptionFromProductStockConstructor() {
        CreateProductStockDTO inputDTO = new CreateProductStockDTO(validSku, null, validQuantity);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> createProductStockUseCase.execute(inputDTO));

        assertEquals("Name cannot be null or empty.", exception.getMessage());
        verify(stockGateway, never()).findBySku(any());
        verify(stockGateway, never()).save(any());

    }

    @Test
    void execute_emptyInputName_throwsIllegalArgumentExceptionFromProductStockConstructor() {
        CreateProductStockDTO inputDTO = new CreateProductStockDTO(validSku, "  ", validQuantity);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> createProductStockUseCase.execute(inputDTO));

        assertEquals("Name cannot be null or empty.", exception.getMessage());
        verify(stockGateway, never()).findBySku(any());
        verify(stockGateway, never()).save(any());

    }

    @Test
    void execute_negativeInputQuantity_throwsIllegalArgumentExceptionFromProductStockConstructor() {
        CreateProductStockDTO inputDTO = new CreateProductStockDTO(validSku, validName, -5);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> createProductStockUseCase.execute(inputDTO));

        assertEquals("Initial quantity cannot be negative.", exception.getMessage());
        verify(stockGateway, never()).findBySku(any());
        verify(stockGateway, never()).save(any());

    }
}
