package com.myinventoryapp.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testConstructorAndGetters() {
        Product product = new Product("pineapple", "pr5711807", 894, 1);

        assertEquals("pineapple", product.getProductName());
        assertEquals("pr5711807", product.getProductId());
        assertEquals(894, product.getUnitPrice());
        assertEquals(1, product.getQuantity());
    }

    @Test
    void testSetQuantity() {
        Product product = new Product("cherry", "pr7860912", 452, 115);
        product.setQuantity(85);

        assertEquals(85, product.getQuantity());
    }

    @Test
    void testEqualsAndHashCode() {
        Product product1 = new Product("banana", "pr5223508", 720, 102);
        Product product2 = new Product("banana", "pr5223508", 720, 102);
        Product product3 = new Product("carrot", "pr2110710", 245, 31);

        // Equals tests
        assertEquals(product1, product2); // Same content
        assertNotEquals(product1, product3); // Different content
        assertNotEquals(product1, null); // Compare to null
        assertNotEquals(product1, new Object()); // Compare to different class

        // HashCode tests
        assertEquals(product1.hashCode(), product2.hashCode()); // Same content, same hash
        assertNotEquals(product1.hashCode(), product3.hashCode()); // Different content, different hash
    }
}