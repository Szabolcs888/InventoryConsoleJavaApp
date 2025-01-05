package com.myinventoryapp.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdUtilsTest {

    @Test
    void testGenerateId() {
        int id = IdUtils.generateId();

        boolean isTheNumberCorrect = id >= 1_000_000 && id <= 9_999_999;
        assertTrue(isTheNumberCorrect, "ID should be between 1000000 and 9999999, but it is not. The number is " + id);
    }
}