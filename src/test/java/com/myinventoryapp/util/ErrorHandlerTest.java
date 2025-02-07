package com.myinventoryapp.util;

import com.myinventoryapp.util.testutils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

class ErrorHandlerTest {
    private static final String YES_OR_NO_QUESTION = "Would you like to add a new product or modify the quantity of an existing one? (Y/N)";
    private static final String YES_OR_NO_OR_DELETE_QUESTION = "Would you like to add to or subtract from the product quantity? (Y/N) " +
            "To delete from the inventory, press \"D\"!";

    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        outputStream = TestUtils.redirectSystemOut();
    }

    @AfterEach
    void tearDown() {
        TestUtils.restoreSystemOut();
    }

    @Test
    void testGetValidNumber_ValidInput() {
        String enterANumberMessage = "Please enter the quantity to be sold:";
        String validNumber = "15";

        try (MockedStatic<UserInputUtils> mockedUserInputUtils = Mockito.mockStatic(UserInputUtils.class)) {
            mockedUserInputUtils.when(() -> UserInputUtils.readFromUser(anyString())).thenReturn(validNumber);

            int validNumberResult = ErrorHandler.getValidNumber(enterANumberMessage);

            mockedUserInputUtils.verify(() -> UserInputUtils.readFromUser(enterANumberMessage), times(1));

            assertEquals(Integer.parseInt(validNumber), validNumberResult, "The method should return the valid number.");
            String enterValidNumberMessage = "Please enter a valid number!";
            String output = outputStream.toString(StandardCharsets.UTF_8);
            assertFalse(output.contains(enterValidNumberMessage),
                    "The '" + enterValidNumberMessage + "' message should not appear in the output.");
        }
    }

    @Test
    void testGetValidNumber_InvalidThenValidInput() {
        String enterANumberMessage = "Please enter the product price:";
        String validNumber = "15";

        try (MockedStatic<UserInputUtils> mockedUserInputUtils = Mockito.mockStatic(UserInputUtils.class)) {
            mockedUserInputUtils.when(() -> UserInputUtils.readFromUser(anyString()))
                    .thenReturn("e").thenReturn("hELlo").thenReturn(validNumber);

            int validNumberResult = ErrorHandler.getValidNumber(enterANumberMessage);

            assertEquals(Integer.parseInt(validNumber), validNumberResult, "The method should return the valid number.");
            mockedUserInputUtils.verify(() -> UserInputUtils.readFromUser(enterANumberMessage), times(3));
            String enterValidNumberMessage = "Please enter a valid number!";
            String output = outputStream.toString(StandardCharsets.UTF_8);
            assertTrue(output.contains(enterValidNumberMessage),
                    "Expected message '" + enterValidNumberMessage + "' was not found in the output.");
        }
    }

    @Test
    void testGetYesOrNoAnswer_YesOption() {
        String yesOption = "Y";

        try (MockedStatic<UserInputUtils> mockedUserInputUtils = Mockito.mockStatic(UserInputUtils.class)) {
            mockedUserInputUtils.when(() -> UserInputUtils.readFromUser(anyString())).thenReturn(yesOption);

            String yesOrNoAnswerResult = ErrorHandler.getYesOrNoAnswer(YES_OR_NO_QUESTION);

            assertEquals(yesOption, yesOrNoAnswerResult);
            mockedUserInputUtils.verify(() -> UserInputUtils.readFromUser("\n" + YES_OR_NO_QUESTION), times(1));
            String enterYesOrNoMessage = "Please enter \"YES\" or \"NO\"!";
            String output = outputStream.toString(StandardCharsets.UTF_8);
            assertFalse(output.contains(enterYesOrNoMessage),
                    "The '" + enterYesOrNoMessage + "' message should not appear in the output.");
        }
    }

    @Test
    void testGetYesOrNoAnswer_NoOption() {
        String noOption = "N";

        try (MockedStatic<UserInputUtils> mockedUserInputUtils = Mockito.mockStatic(UserInputUtils.class)) {
            mockedUserInputUtils.when(() -> UserInputUtils.readFromUser(anyString())).thenReturn(noOption);

            String yesOrNoAnswerResult = ErrorHandler.getYesOrNoAnswer(YES_OR_NO_QUESTION);

            assertEquals(noOption, yesOrNoAnswerResult);
            mockedUserInputUtils.verify(() -> UserInputUtils.readFromUser("\n" + YES_OR_NO_QUESTION), times(1));
            String enterYesOrNoMessage = "Please enter \"YES\" or \"NO\"!";
            String output = outputStream.toString(StandardCharsets.UTF_8);
            assertFalse(output.contains(enterYesOrNoMessage),
                    "The '" + enterYesOrNoMessage + "' message should not appear in the output.");
        }
    }

    @Test
    void testGetYesOrNoAnswer_InvalidAnswerThenYesOption() {
        String invalidAnswer = "why";
        String yesOption = "Y";

        try (MockedStatic<UserInputUtils> mockedUserInputUtils = Mockito.mockStatic(UserInputUtils.class)) {
            mockedUserInputUtils.when(() -> UserInputUtils.readFromUser(anyString())).thenReturn(invalidAnswer).thenReturn("Y");

            String yesOrNoAnswerResult = ErrorHandler.getYesOrNoAnswer(YES_OR_NO_QUESTION);

            assertEquals(yesOption, yesOrNoAnswerResult);
            mockedUserInputUtils.verify(() -> UserInputUtils.readFromUser("\n" + YES_OR_NO_QUESTION), times(2));
            String enterYesOrNoMessage = "Please enter \"YES\" or \"NO\"!";
            String output = outputStream.toString(StandardCharsets.UTF_8);
            assertTrue(output.contains(enterYesOrNoMessage),
                    "Expected message '" + enterYesOrNoMessage + "' was not found in the output.");
        }
    }

    @Test
    void testGetYesOrNoOrDeleteAnswer_YesOption() {
        String yesOption = "Y";

        try (MockedStatic<UserInputUtils> mockedUserInputUtils = Mockito.mockStatic(UserInputUtils.class)) {
            mockedUserInputUtils.when(() -> UserInputUtils.readFromUser(anyString())).thenReturn(yesOption);

            String yesOrNoOrDeleteAnswerResult = ErrorHandler.getYesOrNoOrDeleteAnswer(YES_OR_NO_OR_DELETE_QUESTION);

            assertEquals(yesOption, yesOrNoOrDeleteAnswerResult);
            mockedUserInputUtils.verify(() -> UserInputUtils.readFromUser("\n" + YES_OR_NO_OR_DELETE_QUESTION), times(1));
            String enterYesOrNoOrDeleteMessage = "Please enter \"YES\", \"NO\", or \"DELETE\"!";
            String output = outputStream.toString(StandardCharsets.UTF_8);
            assertFalse(output.contains(enterYesOrNoOrDeleteMessage),
                    "The '" + enterYesOrNoOrDeleteMessage + "' message should not appear in the output.");
        }
    }

    @Test
    void testGetYesOrNoOrDeleteAnswer_NoOption() {
        String noOption = "N";

        try (MockedStatic<UserInputUtils> mockedUserInputUtils = Mockito.mockStatic(UserInputUtils.class)) {
            mockedUserInputUtils.when(() -> UserInputUtils.readFromUser(anyString())).thenReturn(noOption);

            String yesOrNoOrDeleteAnswerResult = ErrorHandler.getYesOrNoOrDeleteAnswer(YES_OR_NO_OR_DELETE_QUESTION);

            assertEquals(noOption, yesOrNoOrDeleteAnswerResult);
            mockedUserInputUtils.verify(() -> UserInputUtils.readFromUser("\n" + YES_OR_NO_OR_DELETE_QUESTION), times(1));
            String enterYesOrNoOrDeleteMessage = "Please enter \"YES\", \"NO\", or \"DELETE\"!";
            String output = outputStream.toString(StandardCharsets.UTF_8);
            assertFalse(output.contains(enterYesOrNoOrDeleteMessage),
                    "The '" + enterYesOrNoOrDeleteMessage + "' message should not appear in the output.");
        }
    }

    @Test
    void testGetYesOrNoOrDeleteAnswer_DeleteOption() {
        String deleteOption = "D";

        try (MockedStatic<UserInputUtils> mockedUserInputUtils = Mockito.mockStatic(UserInputUtils.class)) {
            mockedUserInputUtils.when(() -> UserInputUtils.readFromUser(anyString())).thenReturn(deleteOption);

            String yesOrNoOrDeleteAnswerResult = ErrorHandler.getYesOrNoOrDeleteAnswer(YES_OR_NO_OR_DELETE_QUESTION);

            assertEquals(deleteOption, yesOrNoOrDeleteAnswerResult);
            mockedUserInputUtils.verify(() -> UserInputUtils.readFromUser("\n" + YES_OR_NO_OR_DELETE_QUESTION), times(1));
            String enterYesOrNoOrDeleteMessage = "Please enter \"YES\", \"NO\", or \"DELETE\"!";
            String output = outputStream.toString(StandardCharsets.UTF_8);
            assertFalse(output.contains(enterYesOrNoOrDeleteMessage),
                    "The '" + enterYesOrNoOrDeleteMessage + "' message should not appear in the output.");
        }
    }

    @Test
    void testGetYesOrNoOrDeleteAnswer_InvalidAnswerThenYesOption() {
        String invalidAnswer = "anything";
        String yesOption = "Y";

        try (MockedStatic<UserInputUtils> mockedUserInputUtils = Mockito.mockStatic(UserInputUtils.class)) {
            mockedUserInputUtils.when(() -> UserInputUtils.readFromUser(anyString())).thenReturn(invalidAnswer).thenReturn(yesOption);

            String yesOrNoOrDeleteAnswerResult = ErrorHandler.getYesOrNoOrDeleteAnswer(YES_OR_NO_OR_DELETE_QUESTION);

            assertEquals(yesOption, yesOrNoOrDeleteAnswerResult);
            mockedUserInputUtils.verify(() -> UserInputUtils.readFromUser("\n" + YES_OR_NO_OR_DELETE_QUESTION), times(2));
            String enterYesOrNoOrDeleteMessage = "Please enter \"YES\", \"NO\", or \"DELETE\"!";
            String output = outputStream.toString(StandardCharsets.UTF_8);
            assertTrue(output.contains(enterYesOrNoOrDeleteMessage),
                    "Expected message '" + enterYesOrNoOrDeleteMessage + "' was not found in the output.");
        }
    }

    @Test
    void testValidateName_TooShortName() {
        String invalidName = "kw";

        ErrorHandler.validateName(invalidName);

        String nameMinThreeCharMessage = "The name must be at least 3 characters long!";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(nameMinThreeCharMessage),
                "Expected message '" + nameMinThreeCharMessage + "' was not found in the output.");
        String nameContainCommaMessage = "The name cannot contain a \",\" character!\n";
        assertFalse(output.contains(nameContainCommaMessage),
                "The '" + nameContainCommaMessage + "' message should not appear in the output.");
    }

    @Test
    void testValidateName_nameContainsComma() {
        String invalidName = "Johann,a";

        ErrorHandler.validateName(invalidName);

        String nameMinThreeCharMessage = "The name must be at least 3 characters long!";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertFalse(output.contains(nameMinThreeCharMessage),
                "The '" + nameMinThreeCharMessage + "' message should not appear in the output.");
        String nameContainsCommaMessage = "The name cannot contain a \",\" character!\n";
        assertTrue(output.contains(nameContainsCommaMessage),
                "Expected message '" + nameContainsCommaMessage + "' was not found in the output.");
    }

    @Test
    void testValidateName_TooShortName_AndNameContainsComma() {
        String invalidName = ",H";

        ErrorHandler.validateName(invalidName);

        String nameMinThreeCharMessage = "The name must be at least 3 characters long!";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(nameMinThreeCharMessage),
                "Expected message '" + nameMinThreeCharMessage + "' was not found in the output.");
        String nameContainCommaMessage = "The name cannot contain a \",\" character!\n";
        assertTrue(output.contains(nameContainCommaMessage),
                "Expected message '" + nameContainCommaMessage + "' was not found in the output.");
    }

    @Test
    void testValidateQuantity_NegativeNumber() {
        int quantity = -4;

        ErrorHandler.validateQuantity(quantity);

        String quantityMinOneMessage = "The product quantity must be at least 1!";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(quantityMinOneMessage),
                "Expected message '" + quantityMinOneMessage + "' was not found in the output.");
    }

    @Test
    void testValidateQuantity_NumberZero() {
        int quantity = 0;

        ErrorHandler.validateQuantity(quantity);

        String quantityMinOneMessage = "The product quantity must be at least 1!";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(quantityMinOneMessage),
                "Expected message '" + quantityMinOneMessage + "' was not found in the output.");
    }

    @Test
    void testValidateQuantity_PositiveNumber() {
        int quantity = 8;

        ErrorHandler.validateQuantity(quantity);

        String quantityMinOneMessage = "The product quantity must be at least 1!";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertFalse(output.contains(quantityMinOneMessage),
                "The '" + quantityMinOneMessage + "' message should not appear in the output.");
    }

    @Test
    void testValidateNonNegativeQuantity_NegativeNumber() {
        int quantity = -2;

        ErrorHandler.validateNonNegativeQuantity(quantity);

        String quantityMinZeroMessage = "The quantity of a product cannot be less than 0!";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(quantityMinZeroMessage),
                "Expected message '" + quantityMinZeroMessage + "' was not found in the output.");
    }

    @Test
    void testValidateNonNegativeQuantity_NumberZero() {
        int quantity = 0;

        ErrorHandler.validateNonNegativeQuantity(quantity);

        String quantityMinZeroMessage = "The quantity of a product cannot be less than 0!";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertFalse(output.contains(quantityMinZeroMessage),
                "The '" + quantityMinZeroMessage + "' message should not appear in the output.");
    }

    @Test
    void testValidateNonNegativeQuantity_PositiveNumber() {
        int quantity = 28;

        ErrorHandler.validateNonNegativeQuantity(quantity);

        String quantityMinZeroMessage = "The quantity of a product cannot be less than 0!";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertFalse(output.contains(quantityMinZeroMessage),
                "The '" + quantityMinZeroMessage + "' message should not appear in the output.");
    }

    @Test
    void testValidatePrice_NegativeNumber() {
        int price = -420;

        ErrorHandler.validatePrice(price);

        String priceMinZeroMessage = "The product value cannot be a negative amount!";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(priceMinZeroMessage),
                "Expected message '" + priceMinZeroMessage + "' was not found in the output.");
    }

    @Test
    void testValidatePrice_PositiveNumber() {
        int price = 1200;

        ErrorHandler.validatePrice(price);

        String priceMinZeroMessage = "The product value cannot be a negative amount!";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertFalse(output.contains(priceMinZeroMessage),
                "The '" + priceMinZeroMessage + "' message should not appear in the output.");
    }

    @Test
    void testValidatePrice_NumberZero() {
        int price = 0;

        ErrorHandler.validatePrice(price);

        String priceMinZeroMessage = "The product value cannot be a negative amount!";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertFalse(output.contains(priceMinZeroMessage),
                "The '" + priceMinZeroMessage + "' message should not appear in the output.");
    }
}