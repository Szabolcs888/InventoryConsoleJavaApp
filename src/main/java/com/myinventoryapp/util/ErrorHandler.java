package com.myinventoryapp.util;

public final class ErrorHandler {
    private static final String YES_OPTION = "Y";
    private static final String NO_OPTION = "N";
    private static final String DELETE_OPTION = "D";

    private ErrorHandler() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

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
            if (!YES_OPTION.equalsIgnoreCase(yesOrNoAnswer) && !NO_OPTION.equalsIgnoreCase(yesOrNoAnswer)) {
                System.out.println(
                        Colors.RED.getColorCode() + "Please enter \"YES\" or \"NO\"!" + Colors.RESET.getColorCode());
            }
        } while (!YES_OPTION.equalsIgnoreCase(yesOrNoAnswer) && !NO_OPTION.equalsIgnoreCase(yesOrNoAnswer));
        return yesOrNoAnswer;
    }

    public static String getYesOrNoOrDeleteAnswer(String question) {
        String yesOrNoOrDeleteAnswer;
        do {
            yesOrNoOrDeleteAnswer = UserInputUtils.readFromUser("\n" + question);
            if (!YES_OPTION.equalsIgnoreCase(yesOrNoOrDeleteAnswer) &&
                    !NO_OPTION.equalsIgnoreCase(yesOrNoOrDeleteAnswer) &&
                    !DELETE_OPTION.equalsIgnoreCase(yesOrNoOrDeleteAnswer)) {
                System.out.println(
                        Colors.RED.getColorCode() + "Please enter \"YES\", \"NO\", or \"DELETE\"!" + Colors.RESET.getColorCode());
            }
        } while (!YES_OPTION.equalsIgnoreCase(yesOrNoOrDeleteAnswer) &&
                !NO_OPTION.equalsIgnoreCase(yesOrNoOrDeleteAnswer) &&
                !DELETE_OPTION.equalsIgnoreCase(yesOrNoOrDeleteAnswer));
        return yesOrNoOrDeleteAnswer;
    }

    public static void validateName(String name) {
        int minNameLength = 3;
        String invalidCharacterComma = ",";
        if (name.length() < minNameLength) {
            System.out.println(
                    Colors.RED.getColorCode() + "The name must be at least 3 characters long!\n" + Colors.RESET.getColorCode());
        }
        if (name.contains(invalidCharacterComma)) {
            System.out.println(
                    Colors.RED.getColorCode() + "The name cannot contain a \",\" character!\n" + Colors.RESET.getColorCode());
        }
    }

    public static void validateQuantity(int quantity) {
        int minQuantityValue = 1;
        if (quantity < minQuantityValue) {
            System.out.println(
                    Colors.RED.getColorCode() + "\nThe product quantity must be at least 1!" + Colors.RESET.getColorCode());
        }
    }

    public static void validateNonNegativeQuantity(int quantity) {
        int zeroValue = 0;
        if (quantity < zeroValue) {
            System.out.println(
                    Colors.RED.getColorCode() + "\nThe quantity of a product cannot be less than 0!" + Colors.RESET.getColorCode());
        }
    }

    public static void validatePrice(int unitPrice) {
        int zeroValue = 0;
        if (unitPrice < zeroValue) {
            System.out.println(
                    Colors.RED.getColorCode() + "The product value cannot be a negative amount!" + Colors.RESET.getColorCode());
        }
    }
}