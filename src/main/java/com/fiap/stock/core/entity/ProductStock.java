package com.fiap.stock.core.entity;

import java.util.Objects;

public class ProductStock {
    private final String id;
    private final String sku;
    private final String name;
    private int quantity;

    public ProductStock(String id, String sku, String name, int initialQuantity) {

        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id cannot be null or empty.");
        }
        if (sku == null || sku.trim().isEmpty()) {
            throw new IllegalArgumentException("SKU cannot be null or empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        if (initialQuantity < 0) {
            throw new IllegalArgumentException("Initial quantity cannot be negative.");
        }
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.quantity = initialQuantity;
    }

    public String getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseStock(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to increase stock must be positive.");
        }
        this.quantity += amount;
    }

    public void decreaseStock(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to decrease stock must be positive.");
        }
        if (this.quantity < amount) {
            throw new IllegalStateException("Cannot decrease stock below zero. Current quantity: " + this.quantity);
        }
        this.quantity -= amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductStock that = (ProductStock) o;
        return sku.equals(that.sku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku);
    }

    @Override
    public String toString() {
        return "ProductStock{" +
                "sku='" + sku + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}