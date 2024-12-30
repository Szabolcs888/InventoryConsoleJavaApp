package com.myinventoryapp.dataio;

import com.myinventoryapp.util.testutils.TestFilePaths;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class DataSaverTest {

    @Test
    void testSaveAllData_CallsGetters() {
        try (MockedStatic<FilePaths> mockedFilePaths = Mockito.mockStatic(FilePaths.class)) {
            mockedFilePaths.when(FilePaths::getProductsFilePath)
                    .thenReturn(TestFilePaths.getTestProductsFilePath());
            mockedFilePaths.when(FilePaths::getCustomersFilePath)
                    .thenReturn(TestFilePaths.getTestCustomersFilePath());
            mockedFilePaths.when(FilePaths::getTransactionsFilePath)
                    .thenReturn(TestFilePaths.getTestTransactionsFilePath());

            DataSaver dataSaver = spy(new DataSaver());
            doNothing().when(dataSaver).saveProductsToFile(anyString());
            doNothing().when(dataSaver).saveCustomersToFile(anyString());
            doNothing().when(dataSaver).saveTransactionsToFile(anyString());

            dataSaver.saveAllData();

            mockedFilePaths.verify(FilePaths::getProductsFilePath, times(1));
            mockedFilePaths.verify(FilePaths::getCustomersFilePath, times(1));
            mockedFilePaths.verify(FilePaths::getTransactionsFilePath, times(1));
        }
    }
}