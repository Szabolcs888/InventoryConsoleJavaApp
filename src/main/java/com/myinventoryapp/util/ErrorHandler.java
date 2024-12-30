package com.myinventoryapp.util;

public class ErrorHandler {

    public static int getValidNumber(String text) {
        boolean isNumberValid;
        int validNumber = 0;
        do {
            try {
                validNumber = Integer.parseInt(UserInputUtils.readFromUser(text));
                isNumberValid = true;
            } catch (NumberFormatException e) {
                System.out.println(
                        Colors.RED.getColorCode() + "Please enter a valid number!" + Colors.RESET.getColorCode());
                isNumberValid = false;
            }
        } while (!isNumberValid);
        return validNumber;
    }

    public static String getYesOrNoAnswer(String question) {
        String yesOrNoAnswer;
        do {
            yesOrNoAnswer = UserInputUtils.readFromUser("\n" + question);
            if (!"Y".equalsIgnoreCase(yesOrNoAnswer) && !"N".equalsIgnoreCase(yesOrNoAnswer)) {
                System.out.println(
                        Colors.RED.getColorCode() + "Please enter \"YES\" or \"NO\"!" + Colors.RESET.getColorCode());
            }
        } while (!"Y".equalsIgnoreCase(yesOrNoAnswer) && !"N".equalsIgnoreCase(yesOrNoAnswer));
        return yesOrNoAnswer;
    }

    public static String getYesOrNoOrDeleteAnswer(String question) {
        String yesOrNoOrDeleteAnswer;
        do {
            yesOrNoOrDeleteAnswer = UserInputUtils.readFromUser("\n" + question);
            if (!"Y".equalsIgnoreCase(yesOrNoOrDeleteAnswer) &&
                    !"N".equalsIgnoreCase(yesOrNoOrDeleteAnswer) &&
                    !"D".equalsIgnoreCase(yesOrNoOrDeleteAnswer)) {
                System.out.println(
                        Colors.RED.getColorCode() + "Please enter \"YES\", \"NO\", or \"DELETE\"!" + Colors.RESET.getColorCode());
            }
        } while (!"Y".equalsIgnoreCase(yesOrNoOrDeleteAnswer) &&
                !"N".equalsIgnoreCase(yesOrNoOrDeleteAnswer) &&
                !"D".equalsIgnoreCase(yesOrNoOrDeleteAnswer));
        return yesOrNoOrDeleteAnswer;
    }

    public static void validateName(String name) {
        if (name.length() < 3) {
            System.out.println(
                    Colors.RED.getColorCode() + "The name must be at least 3 characters long!\n" + Colors.RESET.getColorCode());
        }
        if (name.contains(",")) {
            System.out.println(
                    Colors.RED.getColorCode() + "The name cannot contain a \",\" character!\n" + Colors.RESET.getColorCode());
        }
    }

    public static void validateQuantity(int quantity) {
        if (quantity < 1) {
            System.out.println(
                    Colors.RED.getColorCode() + "\nThe product quantity must be at least 1!" + Colors.RESET.getColorCode());
        }
    }

    public static void validateNonNegativeQuantity(int quantity) {
        if (quantity < 0) {
            System.out.println(
                    Colors.RED.getColorCode() + "\nThe quantity of a product cannot be less than 0!" + Colors.RESET.getColorCode());
        }
    }

    public static void validatePrice(int unitPrice) {
        if (unitPrice < 0) {
            System.out.println(
                    Colors.RED.getColorCode() + "The product value cannot be a negative amount!" + Colors.RESET.getColorCode());
        }
    }
}