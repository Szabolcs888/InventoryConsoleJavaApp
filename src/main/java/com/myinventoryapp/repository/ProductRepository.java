package com.myinventoryapp.repository;

import com.myinventoryapp.entities.Product;

import java.util.ArrayList;
import java.util.List;

public final class ProductRepository {
    private static final List<Product> productList = new ArrayList<>();

    private ProductRepository() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static List<Product> getProductList() {
        return new ArrayList<>(productList);
    }

    public static void addProduct(Product product) {
        productList.add(product);
    }

    public static Product findProductByName(String inputProductName) {
        Product foundProductByName = null;
        for (Product product : productList) {
            if (inputProductName.equalsIgnoreCase(product.getProductName())) {
                foundProductByName = product;
                break;
            }
        }
        return foundProductByName;
    }

    public static void deleteProduct(Product product) {
        productList.remove(product);
    }

    public static void clearProductList() {
        productList.clear();
    }
}