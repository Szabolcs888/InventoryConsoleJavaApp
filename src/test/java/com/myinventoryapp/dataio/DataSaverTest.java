package com.myinventoryapp.dataio;

import com.myinventoryapp.entities.Customer;
import com.myinventoryapp.entities.Product;
import com.myinventoryapp.entities.SalesTransaction;
import com.myinventoryapp.repository.CustomerRepository;
import com.myinventoryapp.repository.ProductRepository;
import com.myinventoryapp.repository.SalesTransactionRepository;
import com.myinventoryapp.util.FileUtils;
import com.myinventoryapp.util.testutils.TestFilePaths;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

class DataSaverTest {

    @Test
    void testSaveAllData_InvokesFilePathsGetters() {
        try (MockedStatic<FilePaths> mockedFilePaths = Mockito.mockStatic(FilePaths.class)) {
            mockedFilePaths.when(FilePaths::getProductsFilePath)
                    .thenReturn(TestFilePaths.getTestProductsFilePath());
            mockedFilePaths.when(FilePaths::getCustomersFilePath)
                    .thenReturn(TestFilePaths.getTestCustomersFilePath());
            mockedFilePaths.when(FilePaths::getTransactionsFilePath)
                    .thenReturn(TestFilePaths.getTestTransactionsFilePath());

            DataSaver spyDataSaver = Mockito.spy(new DataSaver());
            Mockito.doNothing().when(spyDataSaver).saveProductsToFile(anyString());
            Mockito.doNothing().when(spyDataSaver).saveCustomersToFile(anyString());
            Mockito.doNothing().when(spyDataSaver).saveTransactionsToFile(anyString());

            spyDataSaver.saveAllData();

            mockedFilePaths.verify(FilePaths::getProductsFilePath, times(1));
            mockedFilePaths.verify(FilePaths::getCustomersFilePath, times(1));
            mockedFilePaths.verify(FilePaths::getTransactionsFilePath, times(1));
        }
    }

    @Test
    void testSaveAllData_CallsEachSaverWithExpectedPaths() {
        try (MockedStatic<FilePaths> mockedFilePaths = Mockito.mockStatic(FilePaths.class)) {
            mockedFilePaths.when(FilePaths::getProductsFilePath)
                    .thenReturn(TestFilePaths.getTestProductsFilePath());
            mockedFilePaths.when(FilePaths::getCustomersFilePath)
                    .thenReturn(TestFilePaths.getTestCustomersFilePath());
            mockedFilePaths.when(FilePaths::getTransactionsFilePath)
                    .thenReturn(TestFilePaths.getTestTransactionsFilePath());

            DataSaver spyDataSaver = Mockito.spy(new DataSaver());
            Mockito.doNothing().when(spyDataSaver).saveProductsToFile(anyString());
            Mockito.doNothing().when(spyDataSaver).saveCustomersToFile(anyString());
            Mockito.doNothing().when(spyDataSaver).saveTransactionsToFile(anyString());

            spyDataSaver.saveAllData();

            Mockito.verify(spyDataSaver, times(1))
                    .saveProductsToFile(TestFilePaths.getTestProductsFilePath());
            Mockito.verify(spyDataSaver, times(1))
                    .saveCustomersToFile(TestFilePaths.getTestCustomersFilePath());
            Mockito.verify(spyDataSaver, times(1))
                    .saveTransactionsToFile(TestFilePaths.getTestTransactionsFilePath());
        }
    }

    @Test
    void testSaveProductsToFile_CallsWriteToFileWithCorrectContent() {
        String productsFilePath = TestFilePaths.getTestProductsFilePath();
        List<Product> productList = Arrays.asList(
                new Product("tea", "pr7236284", 1200, 52),
                new Product("coffee", "pr5204875", 1800, 41),
                new Product("orange juice", "pr7987615", 870, 89)
        );

        try (MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class);
             MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class)) {

            mockedProductRepository.when(ProductRepository::getProductList).thenReturn(productList);
            mockedFileUtils.when(() -> FileUtils.writeToFile(anyString(), anyString())).thenAnswer(invocation -> null);

            DataSaver dataSaver = new DataSaver();
            dataSaver.saveProductsToFile(productsFilePath);

            String expectedContent =
                    "tea,pr7236284,1200,52\n" +
                            "coffee,pr5204875,1800,41\n" +
                            "orange juice,pr7987615,870,89\n";
            mockedFileUtils.verify(() -> FileUtils.writeToFile(expectedContent, productsFilePath), times(1));
        }
    }

    @Test
    void testSaveCustomersToFile_CallsWriteToFileWithCorrectContent() {
        String customersFilePath = TestFilePaths.getTestCustomersFilePath();
        List<Customer> customerList = Arrays.asList(
                new Customer("Kovács Ágnes", "cID5513060", 17019),
                new Customer("Puskás Ferenc", "cID5454036", 10035),
                new Customer("Mikhail Bulgakov", "cID3099022", 6360)
        );

        try (MockedStatic<CustomerRepository> mockedCustomerRepository = Mockito.mockStatic(CustomerRepository.class);
             MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class)) {

            mockedCustomerRepository.when(CustomerRepository::getCustomerList).thenReturn(customerList);
            mockedFileUtils.when(() -> FileUtils.writeToFile(anyString(), anyString())).thenAnswer(invocation -> null);

            DataSaver dataSaver = new DataSaver();
            dataSaver.saveCustomersToFile(customersFilePath);

            String expectedContent =
                    "Kovács Ágnes,17019,cID5513060\n" +
                            "Puskás Ferenc,10035,cID5454036\n" +
                            "Mikhail Bulgakov,6360,cID3099022\n";
            mockedFileUtils.verify(() -> FileUtils.writeToFile(expectedContent, customersFilePath), times(1));
        }
    }

    @Test
    void testSaveTransactionsToFile_CallsWriteToFileWithCorrectContent() {
        String transactionsFilePath = TestFilePaths.getTestTransactionsFilePath();
        List<SalesTransaction> transactionList = Arrays.asList(
                new SalesTransaction("trId5446146", "Darnyi Tamás", "cID9742746",
                        "apple", 1, 560, "2024.11.03. 23:59:04"),
                new SalesTransaction("trId9039796", "Kóbór János", "cID8259712",
                        "coffee", 3, 1800, "2024.11.18. 01:02:13"),
                new SalesTransaction("trId8765603", "Thomas Mann", "cID2633111",
                        "coffee", 3, 1800, "2024.11.07. 21:49:51")
        );

        try (MockedStatic<SalesTransactionRepository> mockedTransactionRepository = Mockito.mockStatic(SalesTransactionRepository.class);
             MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class)) {

            mockedTransactionRepository.when(SalesTransactionRepository::getSalesTransactionList).thenReturn(transactionList);
            mockedFileUtils.when(() -> FileUtils.writeToFile(anyString(), anyString())).thenAnswer(invocation -> null);

            DataSaver dataSaver = new DataSaver();
            dataSaver.saveTransactionsToFile(transactionsFilePath);

            String expectedContent =
                    "trId5446146,2024.11.03. 23:59:04,apple,1,560,Darnyi Tamás,cID9742746\n" +
                            "trId9039796,2024.11.18. 01:02:13,coffee,3,1800,Kóbór János,cID8259712\n" +
                            "trId8765603,2024.11.07. 21:49:51,coffee,3,1800,Thomas Mann,cID2633111\n";
            mockedFileUtils.verify(() -> FileUtils.writeToFile(expectedContent, transactionsFilePath), times(1));
        }
    }
}