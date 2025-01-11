package com.myinventoryapp.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SalesTransactionTest {

    @Test
    void testConstructorAndGetters() {
        SalesTransaction salesTransaction = new SalesTransaction("trId6173011", "Tamasi Tamara",
                "cID8448077", "apple", 8, 560, "2024.02.11. 19:11:51");

        assertEquals("trId6173011", salesTransaction.getTransactionId());
        assertEquals("Tamasi Tamara", salesTransaction.getCustomerName());
        assertEquals("cID8448077", salesTransaction.getCustomerId());
        assertEquals("apple", salesTransaction.getProductName());
        assertEquals(8, salesTransaction.getQuantitySold());
        assertEquals(560, salesTransaction.getUnitPrice());
        assertEquals("2024.02.11. 19:11:51", salesTransaction.getTransactionDate());
    }

    @Test
    void testEqualsAndHashCode() {
        SalesTransaction salesTransaction1 = new SalesTransaction("trId3358221", "Lakatos Regina",
                "cID1506683", "carrot", 2, 245, "2024.11.03. 23:57:13");
        SalesTransaction salesTransaction2 = new SalesTransaction("trId3358221", "Lakatos Regina",
                "cID1506683", "carrot", 2, 245, "2024.11.03. 23:57:13");
        SalesTransaction salesTransaction3 = new SalesTransaction("trId8765603", "Thomas Mann",
                "cID2633111", "coffee", 3, 1800, "2024.11.07. 21:49:51");

        // Equals tests
        assertEquals(salesTransaction1, salesTransaction2); // Same content
        assertNotEquals(salesTransaction1, salesTransaction3); // Different content
        assertNotEquals(salesTransaction1, null); // Compare to null
        assertNotEquals(salesTransaction1, new Object()); // Compare to different class

        // HashCode tests
        assertEquals(salesTransaction1.hashCode(), salesTransaction2.hashCode()); // Same content, same hash
        assertNotEquals(salesTransaction1.hashCode(), salesTransaction3.hashCode()); // Different content, different hash
    }
}