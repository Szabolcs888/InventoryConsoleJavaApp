package com.myinventoryapp.repository;

import com.myinventoryapp.entities.Product;

import java.util.ArrayList;
import java.util.List;

public final class ProductRepository {
    private static final List<Product> PRODUCT_LIST = new ArrayList<>();

    private ProductRepository() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static List<Product> getProductList() {
        return new ArrayList<>(PRODUCT_LIST);
    }

    public static void addProduct(Product product) {
        PRODUCT_LIST.add(product);
    }

    public static Product findProductByName(String inputProductName) {
        Product foundProductByName = null;
        for (Product product : PRODUCT_LIST) {
            if (inputProductName.equalsIgnoreCase(product.getProductName())) {
                foundProductByName = product;
                break;
            }
        }
        return foundProductByName;
    }

    public static void deleteProduct(Product product) {
        PRODUCT_LIST.remove(product);
    }

    public static void clearProductList() {
        PRODUCT_LIST.clear();
    }
}