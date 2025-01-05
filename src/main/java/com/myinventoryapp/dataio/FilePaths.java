package com.myinventoryapp.dataio;

import java.nio.file.Paths;

public final class FilePaths {
    private static final String PRODUCTS_FILE_PATH = "src/main/resources/inventorydata/productList.txt";
    private static final String CUSTOMERS_FILE_PATH = "src/main/resources/inventorydata/customerList.txt";
    private static final String TRANSACTIONS_FILE_PATH = "src/main/resources/inventorydata/transactionList.txt";

    private FilePaths() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static String getProductsFilePath() {
        return Paths.get(PRODUCTS_FILE_PATH).toString();
    }

    public static String getCustomersFilePath() {
        return Paths.get(CUSTOMERS_FILE_PATH).toString();
    }

    public static String getTransactionsFilePath() {
        return Paths.get(TRANSACTIONS_FILE_PATH).toString();
    }
}