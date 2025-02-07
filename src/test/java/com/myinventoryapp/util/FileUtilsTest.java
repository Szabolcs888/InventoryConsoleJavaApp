package com.myinventoryapp.util;

import com.myinventoryapp.util.testutils.TestFilePaths;
import com.myinventoryapp.util.testutils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

class FileUtilsTest {
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
    void testReadFromFile_WithExistingFile() {
        String filePath = TestFilePaths.getTestProductsFilePath();
        List<String> productList = Arrays.asList(
                "carrot,pr2110710,245,31",
                "lemon,pr6634365,880,45",
                "cocoa,pr7553549,235,35"
        );

        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.readAllLines(Mockito.any(Path.class), Mockito.any(Charset.class))).thenReturn(productList);

            List<String> result = FileUtils.readFromFile(filePath);

            assertEquals(productList, result, "The returned list should match the expected content.");
            mockedFiles.verify(() -> Files.readAllLines(Paths.get(filePath), StandardCharsets.ISO_8859_1), times(1));
            String okMessage = "OK..";
            String output = outputStream.toString(StandardCharsets.UTF_8);
            assertTrue(output.contains(okMessage),
                    "Expected message '" + okMessage + "' was not found in the output.");
            String fileNotFound = "COULD NOT BE FOUND!";
            assertFalse(output.contains(fileNotFound),
                    "The '" + fileNotFound + "' message should not appear in the output.");
        }
    }

    @Test
    void testReadFromFile_WithNonExistingFile() {
        String filePath = TestFilePaths.getTestProductsFilePath();

        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.readAllLines(Mockito.any(Path.class), Mockito.any(Charset.class)))
                    .thenThrow(new IOException("File not found"));

            List<String> result = FileUtils.readFromFile(filePath);

            assertEquals(Collections.emptyList(), result, "The returned list should match the expected content.");
            mockedFiles.verify(() -> Files.readAllLines(Paths.get(filePath), StandardCharsets.ISO_8859_1), times(1));
            String okMessage = "OK..";
            String output = outputStream.toString(StandardCharsets.UTF_8);
            assertFalse(output.contains(okMessage),
                    "The '" + okMessage + "' message should not appear in the output.");
            String fileNotFound = "COULD NOT BE FOUND!";
            assertTrue(output.contains(fileNotFound),
                    "Expected message '" + fileNotFound + "' was not found in the output.");
        }
    }

    @Test
    void testWriteToFile_SuccessfulWrite() {
        String content = "Darnyi Tamás,560,cID9742746\n" +
                "Thomas Mann,5400,cID2633111\n" +
                "Mikhail Bulgakov,3600,cID3099022\n";
        String filePath = TestFilePaths.getTestCustomersFilePath();

        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.write(Mockito.any(Path.class), Mockito.any(byte[].class)))
                    .thenAnswer(invocation -> null);

            FileUtils.writeToFile(content, filePath);

            mockedFiles.verify(() -> Files.write(Paths.get(filePath), content.getBytes(StandardCharsets.ISO_8859_1)), times(1));
            String failedWrite = "Failed to write to file: ";
            String output = outputStream.toString(StandardCharsets.UTF_8);
            assertFalse(output.contains(failedWrite),
                    "The '" + failedWrite + "' message should not appear in the output.");
        }
    }

    @Test
    void testWriteToFile_UnsuccessfulWrite() {
        String content = "Puskás Ferenc,4635,cID5454036\n" +
                "Töröcsik András,560,cID2422151\n" +
                "Egerszegi Krisztina,1356,cID5794138\n";
        String filePath = TestFilePaths.getTestCustomersFilePath();

        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.write(Mockito.any(Path.class), Mockito.any(byte[].class)))
                    .thenThrow(new IOException("File system error"));

            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                    FileUtils.writeToFile(content, filePath)
            );

            mockedFiles.verify(() -> Files.write(Paths.get(filePath), content.getBytes(StandardCharsets.ISO_8859_1)),
                    times(1));
            assertTrue(exception.getMessage().contains("Failed to write to file: "));
        }
    }
}