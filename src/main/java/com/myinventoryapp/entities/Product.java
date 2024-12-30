package com.myinventoryapp.entities;

import java.util.Objects;

public class Product {
    private String productName;
    private String productId;
    private int unitPrice;
    private int quantity;

    public Product(String productName, String productId, int unitPrice, int quantity) {
        this.productName = productName;
        this.productId = productId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductId() {
        return productId;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return unitPrice == product.unitPrice &&
                quantity == product.quantity &&
                Objects.equals(productName, product.productName) &&
                Objects.equals(productId, product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, productId, unitPrice, quantity);
    }
}