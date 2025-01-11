package com.myinventoryapp.dataio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FilePathsTest {

    @Test
    void getProductsFilePath() {
        String expectedPath = "src\\main\\resources\\inventorydata\\productList.txt";
        assertEquals(expectedPath, FilePaths.getProductsFilePath());
    }

    @Test
    void getCustomersFilePath() {
        String expectedPath = "src\\main\\resources\\inventorydata\\customerList.txt";
        assertEquals(expectedPath, FilePaths.getCustomersFilePath());
    }

    @Test
    void getTransactionsFilePath() {
        String expectedPath = "src\\main\\resources\\inventorydata\\transactionList.txt";
        assertEquals(expectedPath, FilePaths.getTransactionsFilePath());
    }
}