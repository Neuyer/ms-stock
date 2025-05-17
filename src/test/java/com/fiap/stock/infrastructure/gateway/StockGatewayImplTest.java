package com.fiap.stock.infrastructure.gateway;

import com.fiap.stock.core.entity.ProductStock;
import com.fiap.stock.infrastructure.repository.StockRepository;
import com.fiap.stock.infrastructure.repository.model.ProductStockModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class StockGatewayImplTest {


    private StockRepository stockRepository;

    private StockGatewayImpl stockGateway;

    private final String validId = UUID.randomUUID().toString();
    private final String validSku = "TEST-SKU";
    private final String validName = "Test Product";
    private final int validQuantity = 10;

    private ProductStockModel createTestProductStockModel() {
        return ProductStockModel.builder().
                id(validId)
                .sku(validSku)
                .name(validName)
                .quantity(validQuantity)
                .build();

    }

    private ProductStock createTestProductStockEntity() {
        return new ProductStock(validId, validSku, validName, validQuantity);
    }

    @BeforeEach
    void setUp() {
        stockRepository = mock(StockRepository.class);
        stockGateway = new StockGatewayImpl(stockRepository);
    }

    @Test
    void findBySku_existingSku_returnsOptionalOfProductStock() {
        ProductStockModel model = createTestProductStockModel();
        when(stockRepository.findBySku(validSku)).thenReturn(Optional.of(model));

        Optional<ProductStock> result = stockGateway.findBySku(validSku);

        assertTrue(result.isPresent());
        assertEquals(validId, result.get().getId());
        assertEquals(validSku, result.get().getSku());
        verify(stockRepository, times(1)).findBySku(validSku);
    }

    @Test
    void findBySku_nonExistingSku_returnsEmptyOptional() {
        when(stockRepository.findBySku(validSku)).thenReturn(Optional.empty());

        Optional<ProductStock> result = stockGateway.findBySku(validSku);

        assertTrue(result.isEmpty());
        verify(stockRepository, times(1)).findBySku(validSku);
    }

    @Test
    void findById_existingId_returnsOptionalOfProductStock() {
        ProductStockModel model = createTestProductStockModel();
        when(stockRepository.findById(validId)).thenReturn(Optional.of(model));

        Optional<ProductStock> result = stockGateway.findById(validId);

        assertTrue(result.isPresent());
        assertEquals(validId, result.get().getId());
        verify(stockRepository, times(1)).findById(validId);
    }

    @Test
    void findById_nonExistingId_returnsEmptyOptional() {
        when(stockRepository.findById(validId)).thenReturn(Optional.empty());

        Optional<ProductStock> result = stockGateway.findById(validId);

        assertTrue(result.isEmpty());
        verify(stockRepository, times(1)).findById(validId);
    }

    @Test
    void findAll_noStocksExist_returnsEmptySet() {
        when(stockRepository.findAll()).thenReturn(Collections.emptyList());

        Set<ProductStock> result = stockGateway.findAll();

        assertTrue(result.isEmpty());
        verify(stockRepository, times(1)).findAll();

    }

    @Test
    void save_validProductStock_callsStockRepositorySaveWithMappedModel() {
        ProductStock entity = createTestProductStockEntity();
        ProductStockModel model = createTestProductStockModel();

        stockGateway.save(entity);

        verify(stockRepository, times(1)).save(model);
    }

    @Test
    void deleteById_existingId_callsStockRepositoryDeleteById() {
        doNothing().when(stockRepository).deleteById(validId);

        stockGateway.deleteById(validId);

        verify(stockRepository, times(1)).deleteById(validId);
    }
}
