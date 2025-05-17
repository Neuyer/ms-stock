package com.fiap.stock.core.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductStockTest {

    private final String validId = "some-unique-id";
    private final String validSku = "PRODUCT-SKU-123";
    private final String validName = "Awesome Product";
    private final int validInitialQuantity = 10;

    @Test
    void constructor_validInput_createsProductStock() {
        ProductStock productStock = new ProductStock(validId, validSku, validName, validInitialQuantity);
        assertNotNull(productStock);
        assertEquals(validId, productStock.getId());
        assertEquals(validSku, productStock.getSku());
        assertEquals(validName, productStock.getName());
        assertEquals(validInitialQuantity, productStock.getQuantity());
    }

    @Test
    void constructor_nullId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ProductStock(null, validSku, validName, validInitialQuantity));
    }

    @Test
    void constructor_emptyId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ProductStock("  ", validSku, validName, validInitialQuantity));
    }

    @Test
    void constructor_nullSku_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ProductStock(validId, null, validName, validInitialQuantity));
    }

    @Test
    void constructor_emptySku_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ProductStock(validId, "  ", validName, validInitialQuantity));
    }

    @Test
    void constructor_nullName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ProductStock(validId, validSku, null, validInitialQuantity));
    }

    @Test
    void constructor_emptyName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ProductStock(validId, validSku, "  ", validInitialQuantity));
    }

    @Test
    void constructor_negativeInitialQuantity_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ProductStock(validId, validSku, validName, -1));
    }

    @Test
    void increaseStock_positiveAmount_increasesQuantity() {
        ProductStock productStock = new ProductStock(validId, validSku, validName, validInitialQuantity);
        int increaseAmount = 5;
        productStock.increaseStock(increaseAmount);
        assertEquals(validInitialQuantity + increaseAmount, productStock.getQuantity());
    }

    @Test
    void increaseStock_zeroAmount_throwsIllegalArgumentException() {
        ProductStock productStock = new ProductStock(validId, validSku, validName, validInitialQuantity);
        assertThrows(IllegalArgumentException.class, () -> productStock.increaseStock(0));
        assertEquals(validInitialQuantity, productStock.getQuantity()); // Ensure quantity doesn't change
    }

    @Test
    void increaseStock_negativeAmount_throwsIllegalArgumentException() {
        ProductStock productStock = new ProductStock(validId, validSku, validName, validInitialQuantity);
        assertThrows(IllegalArgumentException.class, () -> productStock.increaseStock(-3));
        assertEquals(validInitialQuantity, productStock.getQuantity()); // Ensure quantity doesn't change
    }

    @Test
    void decreaseStock_positiveAmountSufficientStock_decreasesQuantity() {
        ProductStock productStock = new ProductStock(validId, validSku, validName, validInitialQuantity);
        int decreaseAmount = 3;
        productStock.decreaseStock(decreaseAmount);
        assertEquals(validInitialQuantity - decreaseAmount, productStock.getQuantity());
    }

    @Test
    void decreaseStock_positiveAmountInsufficientStock_throwsIllegalStateException() {
        ProductStock productStock = new ProductStock(validId, validSku, validName, 2);
        assertThrows(IllegalStateException.class, () -> productStock.decreaseStock(5));
        assertEquals(2, productStock.getQuantity()); // Ensure quantity doesn't change
    }

    @Test
    void decreaseStock_zeroAmount_throwsIllegalArgumentException() {
        ProductStock productStock = new ProductStock(validId, validSku, validName, validInitialQuantity);
        assertThrows(IllegalArgumentException.class, () -> productStock.decreaseStock(0));
        assertEquals(validInitialQuantity, productStock.getQuantity()); // Ensure quantity doesn't change
    }

    @Test
    void decreaseStock_negativeAmount_throwsIllegalArgumentException() {
        ProductStock productStock = new ProductStock(validId, validSku, validName, validInitialQuantity);
        assertThrows(IllegalArgumentException.class, () -> productStock.decreaseStock(-2));
        assertEquals(validInitialQuantity, productStock.getQuantity()); // Ensure quantity doesn't change
    }

    @Test
    void equals_sameSku_returnsTrue() {
        ProductStock productStock1 = new ProductStock(validId, validSku, "Product A", 5);
        ProductStock productStock2 = new ProductStock("another-id", validSku, "Product B", 10);
        assertEquals(productStock1, productStock2);
    }

    @Test
    void equals_differentSku_returnsFalse() {
        ProductStock productStock1 = new ProductStock(validId, validSku, "Product A", 5);
        ProductStock productStock2 = new ProductStock("another-id", "DIFFERENT-SKU", "Product B", 10);
        assertNotEquals(productStock1, productStock2);
    }

    @Test
    void equals_differentClass_returnsFalse() {
        ProductStock productStock = new ProductStock(validId, validSku, validName, validInitialQuantity);
        assertNotEquals(productStock, new Object());
    }

    @Test
    void hashCode_sameSku_returnsSameHashCode() {
        ProductStock productStock1 = new ProductStock(validId, validSku, "Product A", 5);
        ProductStock productStock2 = new ProductStock("another-id", validSku, "Product B", 10);
        assertEquals(productStock1.hashCode(), productStock2.hashCode());
    }

    @Test
    void hashCode_differentSku_returnsDifferentHashCode() {
        ProductStock productStock1 = new ProductStock(validId, validSku, "Product A", 5);
        ProductStock productStock2 = new ProductStock("another-id", "DIFFERENT-SKU", "Product B", 10);
        assertNotEquals(productStock1.hashCode(), productStock2.hashCode());
    }

    @Test
    void toString_returnsExpectedStringRepresentation() {
        ProductStock productStock = new ProductStock(validId, validSku, validName, validInitialQuantity);
        String expectedString = "ProductStock{sku='" + validSku + "', name='" + validName + "', quantity=" + validInitialQuantity + "}";
        assertEquals(expectedString, productStock.toString());
    }
}
