package com.myinventoryapp.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MenuOptionTest {

    @Test
    void testFromValue_ValidInputs() {
        assertEquals(MenuOption.SELL_PRODUCT, MenuOption.fromValue(1),
                "The fromValue(1) method should return SELL_PRODUCT, but it didn't.");
        assertEquals(MenuOption.RECEIVE_PRODUCT, MenuOption.fromValue(2),
                "The fromValue(2) method should return RECEIVE_PRODUCT, but it didn't.");
        assertEquals(MenuOption.DISPLAY_AVAILABLE_PRODUCTS, MenuOption.fromValue(3),
                "The fromValue(3) method should return DISPLAY_AVAILABLE_PRODUCTS, but it didn't.");
        assertEquals(MenuOption.DISPLAY_CUSTOMERS, MenuOption.fromValue(4),
                "The fromValue(4) method should return DISPLAY_CUSTOMERS, but it didn't.");
        assertEquals(MenuOption.DISPLAY_TRANSACTIONS, MenuOption.fromValue(5),
                "The fromValue(5) method should return DISPLAY_TRANSACTIONS, but it didn't.");
        assertEquals(MenuOption.SAVE_AND_EXIT, MenuOption.fromValue(6),
                "The fromValue(6) method should return SAVE_AND_EXIT, but it didn't.");
    }

    @Test
    void testFromValue_InvalidInput_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> MenuOption.fromValue(-1));
        assertEquals("Invalid menu choice: -1", exception.getMessage());

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> MenuOption.fromValue(0));
        assertEquals("Invalid menu choice: 0", exception2.getMessage());

        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> MenuOption.fromValue(7));
        assertEquals("Invalid menu choice: 7", exception3.getMessage());

        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> MenuOption.fromValue(99));
        assertEquals("Invalid menu choice: 99", exception4.getMessage());
    }

    @Test
    void testEnumValues_CorrectMapping() {
        assertEquals(1, MenuOption.SELL_PRODUCT.ordinal() + 1);
        assertEquals(2, MenuOption.RECEIVE_PRODUCT.ordinal() + 1);
        assertEquals(3, MenuOption.DISPLAY_AVAILABLE_PRODUCTS.ordinal() + 1);
        assertEquals(4, MenuOption.DISPLAY_CUSTOMERS.ordinal() + 1);
        assertEquals(5, MenuOption.DISPLAY_TRANSACTIONS.ordinal() + 1);
        assertEquals(6, MenuOption.SAVE_AND_EXIT.ordinal() + 1);
    }
}