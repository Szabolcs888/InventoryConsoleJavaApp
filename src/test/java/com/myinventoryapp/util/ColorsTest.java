package com.myinventoryapp.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorsTest {

    @Test
    void testGetColorCode_ValidColorCodes() {
        assertEquals("\033[0m", Colors.RESET.getColorCode(), "RESET color code is incorrect");
        assertEquals("\033[0;31m", Colors.RED.getColorCode(), "RED color code is incorrect");
        assertEquals("\033[0;32m", Colors.GREEN.getColorCode(), "GREEN color code is incorrect");
        assertEquals("\033[4;32m", Colors.GREEN_UNDERLINED.getColorCode(), "GREEN_UNDERLINED color code is incorrect");
    }

    @Test
    void testGetColorCode_CorrectMapping() {
        assertEquals(1, Colors.RESET.ordinal() + 1, "RESET enum position is incorrect");
        assertEquals(2, Colors.RED.ordinal() + 1, "RED enum position is incorrect");
        assertEquals(3, Colors.GREEN.ordinal() + 1, "GREEN enum position is incorrect");
        assertEquals(4, Colors.GREEN_UNDERLINED.ordinal() + 1, "GREEN_UNDERLINED enum position is incorrect");
    }
}