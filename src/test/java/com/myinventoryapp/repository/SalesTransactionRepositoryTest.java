package com.myinventoryapp.repository;

import com.myinventoryapp.entities.SalesTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SalesTransactionRepositoryTest {

    @BeforeEach
    void setup() {
        SalesTransactionRepository.clearSalesTransactionList();
    }

    @Test
    void testGetSalesTransactionList_EmptyList() {
        List<SalesTransaction> salesTransactions = SalesTransactionRepository.getSalesTransactionList();

        assertTrue(salesTransactions.isEmpty(), "The salesTransaction list should be empty initially.");
        assertNotSame(salesTransactions, SalesTransactionRepository.getSalesTransactionList(),
                "The returned list should be a new instance.");
    }

    @Test
    void testGetSalesTransactionList_ModificationDoesNotAffectOriginal() {
        List<SalesTransaction> salesTransactions = SalesTransactionRepository.getSalesTransactionList();

        salesTransactions.add(new SalesTransaction(
                "trId1430909", "Nagy Anna", "cID5916556",
                "banana", 3, 720, "2024.02.01. 15:03:58"));

        assertTrue(SalesTransactionRepository.getSalesTransactionList().isEmpty(),
                "The original transaction list should not be affected by modifications to the returned list.");
    }

    @Test
    void testGetSalesTransactionList_AfterAddingTransactions() {
        SalesTransaction salesTransaction1 = new SalesTransaction(
                "trId8419791", "Kocsis Zoltán", "cID2088505",
                "apple", 12, 560, "2024.02.01. 15:05:05");
        SalesTransaction salesTransaction2 = new SalesTransaction(
                "trId7545570", "Kovács Ágnes", "cID5513060",
                "pear", 3, 675, "2024.02.01. 15:05:47");
        SalesTransactionRepository.addSalesTransaction(salesTransaction1);
        SalesTransactionRepository.addSalesTransaction(salesTransaction2);

        List<SalesTransaction> salesTransactions = SalesTransactionRepository.getSalesTransactionList();

        assertEquals(2, salesTransactions.size(), "The transactions list should contain two transactions.");
        assertTrue(salesTransactions.contains(salesTransaction1),
                "The transactions list should contain 'trId8419791'.");
        assertTrue(salesTransactions.contains(salesTransaction2),
                "The transactions list should contain 'trId7545570'.");
    }
}