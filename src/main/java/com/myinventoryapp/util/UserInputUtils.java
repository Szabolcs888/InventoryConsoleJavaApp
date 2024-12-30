package com.myinventoryapp.util;

import java.util.Scanner;

public class UserInputUtils {

    public static String readFromUser(String question) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(question);
        return scanner.nextLine().trim();
    }
}