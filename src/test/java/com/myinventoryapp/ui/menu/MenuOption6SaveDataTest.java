package com.myinventoryapp.ui.menu;

import com.myinventoryapp.dataio.DataSaver;
import com.myinventoryapp.util.testutils.TestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;

class MenuOption6SaveDataTest {

    @Test
    void testSaveData() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();

        DataSaver mockDataSaver = Mockito.mock(DataSaver.class);
        Mockito.doNothing().when(mockDataSaver).saveAllData();

        MenuOption6SaveData menuOption6SaveData = new MenuOption6SaveData(mockDataSaver);
        menuOption6SaveData.saveData();

        String expectedMessage = "Data has been saved..";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(expectedMessage),
                "Expected message '" + expectedMessage + "' was not found in the output.");
        Mockito.verify(mockDataSaver, times(1)).saveAllData();
    }
}