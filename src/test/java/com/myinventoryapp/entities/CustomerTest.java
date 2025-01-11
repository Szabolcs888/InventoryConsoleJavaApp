package com.myinventoryapp.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testConstructorAndGetters() {
        Customer customer = new Customer("Egerszegi Krisztina", "cID5794138", 1356);

        assertEquals("Egerszegi Krisztina", customer.getCustomerName());
        assertEquals("cID5794138", customer.getCustomerId());
        assertEquals(1356, customer.getTotalPurchases());
    }

    @Test
    void testSetTotalPurchases() {
        Customer customer = new Customer("Temesi Szabolcs", "cID9168098", 9000);
        customer.setTotalPurchases(12350);

        assertEquals(12350, customer.getTotalPurchases());
    }

    @Test
    void testEqualsAndHashCode() {
        Customer customer1 = new Customer("Mikhail Bulgakov", "cID3099022", 50);
        Customer customer2 = new Customer("Mikhail Bulgakov", "cID3099022", 50);
        Customer customer3 = new Customer("Lakatos Regina", "cID9168098", 60);

        // Equals tests
        assertEquals(customer1, customer2); // Same content
        assertNotEquals(customer1, customer3); // Different content
        assertNotEquals(customer1, null); // Compare to null
        assertNotEquals(customer1, new Object()); // Compare to different class

        // HashCode tests
        assertEquals(customer1.hashCode(), customer2.hashCode()); // Same content, same hash
        assertNotEquals(customer1.hashCode(), customer3.hashCode()); // Different content, different hash
    }
}