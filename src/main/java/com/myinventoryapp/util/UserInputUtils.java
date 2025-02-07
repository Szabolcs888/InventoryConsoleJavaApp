package com.myinventoryapp.util;

import java.util.Scanner;

public final class UserInputUtils {

    private UserInputUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static String readFromUser(String question) {
        System.out.println(question);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().trim();
    }
}