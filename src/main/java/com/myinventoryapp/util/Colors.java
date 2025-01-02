package com.myinventoryapp.util;

public enum Colors {
    RESET("\033[0m"),
    RED("\033[0;31m"),
    GREEN("\033[0;32m"),
    GREEN_UNDERLINED("\033[4;32m");

    private final String ansiCode;

    Colors(String ansiCode) {
        this.ansiCode = ansiCode;
    }

    public String getColorCode() {
        return ansiCode;
    }
}