package com.myinventoryapp.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilsTest {

    @Test
    void testIsValidName_ValidInput() {
        boolean isValidName = ValidationUtils.isValidName("János");

        assertTrue(isValidName);
    }

    @Test
    void testIsValidName_ShortInput() {
        boolean isValidName = ValidationUtils.isValidName("To");

        assertFalse(isValidName);
    }

    @Test
    void testIsValidName_InputContainsComma() {
        boolean isValidName = ValidationUtils.isValidName("Johanna,");

        assertFalse(isValidName);
    }

    @Test
    void testIsValidName_ShortInputAndContainsComma() {
        boolean isValidName = ValidationUtils.isValidName(",o");

        assertFalse(isValidName);
    }
}