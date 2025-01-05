package com.myinventoryapp.util.testutils;

import java.nio.file.Paths;

public final class TestFilePaths {
    private static final String TEST_PRODUCTS_FILE_PATH = "src/test/resources/inventorytestdata/testProductList.txt";
    private static final String TEST_CUSTOMERS_FILE_PATH = "src/test/resources/inventorytestdata/testCustomerList.txt";
    private static final String TEST_TRANSACTIONS_FILE_PATH = "src/test/resources/inventorytestdata/testTransactionList.txt";

    private TestFilePaths() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static String getTestProductsFilePath() {
        return Paths.get(TEST_PRODUCTS_FILE_PATH).toString();
    }

    public static String getTestCustomersFilePath() {
        return Paths.get(TEST_CUSTOMERS_FILE_PATH).toString();
    }

    public static String getTestTransactionsFilePath() {
        return Paths.get(TEST_TRANSACTIONS_FILE_PATH).toString();
    }
}