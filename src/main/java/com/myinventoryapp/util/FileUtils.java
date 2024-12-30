package com.myinventoryapp.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.myinventoryapp.util.Colors.*;

public class FileUtils {

    public static List<String> readFromFile(String path) {
        List<String> result = new ArrayList<>();
        try {
            Charset charset = StandardCharsets.ISO_8859_1;
            result = Files.readAllLines(Paths.get(path), charset);
            System.out.println(GREEN.getColorCode() + "OK.." + RESET.getColorCode());
        } catch (
                IOException e) {
            System.out.println(RED.getColorCode() + "COULD NOT BE FOUND!" + RESET.getColorCode());
        }
        return result;
    }

    public static void writeToFile(String content, String path) {
        try {
            new File("src/main/resources").mkdirs();
            Files.write(Paths.get(path), content.getBytes("ISO-8859-1"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to file: " + path, e);
        }
    }
}