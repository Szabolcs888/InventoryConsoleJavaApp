package com.myinventoryapp.util;

public enum MenuOption {
    SELL_PRODUCT(1),
    RECEIVE_PRODUCT(2),
    DISPLAY_AVAILABLE_PRODUCTS(3),
    DISPLAY_CUSTOMERS(4),
    DISPLAY_TRANSACTIONS(5),
    SAVE_AND_EXIT(6);

    private final int value;

    MenuOption(int value) {
        this.value = value;
    }

    public static MenuOption fromValue(int value) {
        for (MenuOption option : values()) {
            if (option.value == value) {
                return option;
            }
        }
        throw new IllegalArgumentException("Invalid menu choice: " + value);
    }
}