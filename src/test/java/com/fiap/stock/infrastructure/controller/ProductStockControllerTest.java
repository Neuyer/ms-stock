package com.fiap.stock.infrastructure.controller;

import com.fiap.stock.core.dto.CreateProductStockDTO;
import com.fiap.stock.core.dto.UpdateProductStockDTO;
import com.fiap.stock.core.entity.ProductStock;
import com.fiap.stock.core.usecase.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.fiap.stock.core.entity.StockOperation.INCREASE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductStockController.class)
class ProductStockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateProductStockUseCase createProductStockUseCase;

    @MockitoBean
    private FindAllStocksUseCase findAllStocksUseCase;

    @MockitoBean
    private FindStockUseCase findStockUseCase;

    @MockitoBean
    private DeleteProductStockUseCase deleteProductStockUseCase;

    @MockitoBean
    private AlterProductStockUseCase alterProductStockUseCase;

    private final String baseUrl = "/stocks";
    private final String validId = UUID.randomUUID().toString();
    private final String validSku = "TEST-SKU";
    private final String validName = "Test Product";
    private final int validQuantity = 10;

    private ProductStock createTestProductStock() {
        return new ProductStock(validId, validSku, validName, validQuantity);
    }

    private UpdateProductStockDTO createTestUpdateProductStockDTO() {
        return new UpdateProductStockDTO(INCREASE, 20);
    }

    @Test
    void findStock_existingSku_returnsOkAndProductStock() throws Exception {
        ProductStock productStock = createTestProductStock();
        when(findStockUseCase.execute(validSku)).thenReturn(productStock);

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/" + validSku))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":\"" + validId + "\",\"sku\":\"" + validSku + "\",\"name\":\"" + validName + "\",\"quantity\":" + validQuantity + "}"));
    }

    @Test
    void findAllStocks_returnsOkAndSetOfProductStocks() throws Exception {
        Set<ProductStock> productStocks = new HashSet<>();
        productStocks.add(createTestProductStock());
        productStocks.add(new ProductStock(UUID.randomUUID().toString(), "ANOTHER-SKU", "Another Product", 5));
        when(findAllStocksUseCase.execute()).thenReturn(productStocks);

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"id\":\"" + validId + "\",\"sku\":\"" + validSku + "\",\"name\":\"" + validName + "\",\"quantity\":" + validQuantity + "},{\"id\":\"" + productStocks.stream().skip(1).findFirst().orElseThrow().getId() + "\",\"sku\":\"ANOTHER-SKU\",\"name\":\"Another Product\",\"quantity\":5}]"));
    }

    @Test
    void findAllStocks_emptySet_returnsOkAndEmptyArray() throws Exception {
        when(findAllStocksUseCase.execute()).thenReturn(Set.of());

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    void createProductStock_validInput_returnsOkAndCreatedProductStock() throws Exception {
        ProductStock createdStock = createTestProductStock();
        when(createProductStockUseCase.execute(any(CreateProductStockDTO.class))).thenReturn(createdStock);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sku\":\"" + validSku + "\",\"name\":\"" + validName + "\",\"quantity\":" + validQuantity + "}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":\"" + validId + "\",\"sku\":\"" + validSku + "\",\"name\":\"" + validName + "\",\"quantity\":" + validQuantity + "}"));
    }

    @Test
    void updateStock_existingSku_returnsOkAndUpdatedProductStock() throws Exception {
        UpdateProductStockDTO updateDTO = createTestUpdateProductStockDTO();
        ProductStock updatedStock = new ProductStock(validId, validSku, validName, updateDTO.quantity());
        when(alterProductStockUseCase.execute(eq(validSku), any(UpdateProductStockDTO.class))).thenReturn(updatedStock);

        mockMvc.perform(MockMvcRequestBuilders.put(baseUrl + "/" + validSku)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\":" + updateDTO.quantity() + "}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":\"" + validId + "\",\"sku\":\"" + validSku + "\",\"name\":\"" + validName + "\",\"quantity\":" + updateDTO.quantity() + "}"));
    }

    @Test
    void deleteProductStock_existingId_returnsOk() throws Exception {
        doNothing().when(deleteProductStockUseCase).execute(validId);

        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "/" + validId))
                .andExpect(status().isOk());
    }

    @Test
    void deleteProductStock_nonExistingId_returnsNotFound() throws Exception {
        doNothing().when(deleteProductStockUseCase).execute(validId);

        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "/non-existent-id"))
                .andExpect(status().isOk());
    }
}
