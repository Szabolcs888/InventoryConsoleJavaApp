package com.myinventoryapp.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @Test
    void testGetCurrentFormattedDate() {
        String nowDateTimeResult = DateUtils.getCurrentFormattedDate();

        String expectedNowDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm:ss"));
        assertEquals(expectedNowDateTime, nowDateTimeResult);
        String datePattern = "\\d{4}\\.\\d{2}\\.\\d{2}\\. \\d{2}:\\d{2}:\\d{2}";
        assertTrue(Pattern.matches(datePattern, nowDateTimeResult),
                "The formatted date does not match the expected pattern.");
    }
}