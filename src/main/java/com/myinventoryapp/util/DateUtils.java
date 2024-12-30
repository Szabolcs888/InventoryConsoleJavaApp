package com.myinventoryapp.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static String getCurrentFormattedDate() {
        LocalDateTime nowDateTime = LocalDateTime.now();
        DateTimeFormatter customFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm:ss");
        return nowDateTime.format(customFormat);
    }
}