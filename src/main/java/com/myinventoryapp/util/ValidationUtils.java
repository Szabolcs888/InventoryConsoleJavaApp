package com.myinventoryapp.util;

public final class ValidationUtils {

    private ValidationUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static boolean isValidName(String customerName) {
        return customerName.length() >= 3 && !customerName.contains(",");
    }
}