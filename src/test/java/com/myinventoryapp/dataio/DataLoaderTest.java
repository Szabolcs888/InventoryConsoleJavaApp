package com.myinventoryapp.dataio;

import com.myinventoryapp.repository.CustomerRepository;
import com.myinventoryapp.repository.ProductRepository;
import com.myinventoryapp.repository.SalesTransactionRepository;
import com.myinventoryapp.entities.Customer;
import com.myinventoryapp.entities.Product;
import com.myinventoryapp.entities.SalesTransaction;
import com.myinventoryapp.util.FileUtils;
import com.myinventoryapp.util.testutils.TestFilePaths;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class DataLoaderTest {

    @Test
    void testLoadAllData_CallsGetters() {
        try (MockedStatic<FilePaths> mockedFilePaths = Mockito.mockStatic(FilePaths.class)) {
            mockedFilePaths.when(FilePaths::getProductsFilePath)
                    .thenReturn(TestFilePaths.getTestProductsFilePath());
            mockedFilePaths.when(FilePaths::getCustomersFilePath)
                    .thenReturn(TestFilePaths.getTestCustomersFilePath());
            mockedFilePaths.when(FilePaths::getTransactionsFilePath)
                    .thenReturn(TestFilePaths.getTestTransactionsFilePath());

            DataLoader dataLoader = spy(new DataLoader());
            doNothing().when(dataLoader).loadProductsFromFile(anyString());
            doNothing().when(dataLoader).loadCustomersFromFile(anyString());
            doNothing().when(dataLoader).loadTransactionsFromFile(anyString());

            dataLoader.loadAllData();

            // Verify that the FilePaths getters were called (this is the additional verification)
            mockedFilePaths.verify(FilePaths::getProductsFilePath, times(1));
            mockedFilePaths.verify(FilePaths::getCustomersFilePath, times(1));
            mockedFilePaths.verify(FilePaths::getTransactionsFilePath, times(1));
        }
    }

    @Test
    void testLoadAllData_CallsFileLoaders() {
        DataLoader dataLoader = spy(new DataLoader());
        doNothing().when(dataLoader).loadProductsFromFile(anyString());
        doNothing().when(dataLoader).loadCustomersFromFile(anyString());
        doNothing().when(dataLoader).loadTransactionsFromFile(anyString());

        dataLoader.loadAllData();

        // Verify that the load methods are called with the correct arguments
        verify(dataLoader, times(1)).loadProductsFromFile(
                "src/main/resources/inventorydata/productList.txt");
        verify(dataLoader, times(1)).loadCustomersFromFile(
                "src/main/resources/inventorydata/customerList.txt");
        verify(dataLoader, times(1)).loadTransactionsFromFile(
                "src/main/resources/inventorydata/transactionList.txt");
    }


    @Test
    void testLoadProductsFromFile_PrintsMessage() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        String testProductsFilePath = TestFilePaths.getTestProductsFilePath();
        try (MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class)) {
            mockedFileUtils.when(() -> FileUtils.readFromFile(testProductsFilePath))
                    .thenReturn(Collections.emptyList());

            DataLoader dataLoader = new DataLoader();
            dataLoader.loadProductsFromFile(testProductsFilePath);

            String expectedMessage = "Checking the database of products: ";
            String output = outputStream.toString();
            assertTrue(output.contains(expectedMessage),
                    "Expected message '" + expectedMessage + "' was not found in the output.");
        } finally {
            System.setOut(System.out);
        }
    }

    @Test
    void testLoadProductsFromFile_ReadFromFileCalled() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        String testProductsFilePath = TestFilePaths.getTestProductsFilePath();
        try (MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class)) {
            mockedFileUtils.when(() -> FileUtils.readFromFile(testProductsFilePath))
                    .thenReturn(Arrays.asList(
                            "apple,pr5197140,560,25",
                            "pear,pr4270613,675,19"));

            DataLoader dataLoader = new DataLoader();
            dataLoader.loadProductsFromFile(testProductsFilePath);

            mockedFileUtils.verify(() -> FileUtils.readFromFile(testProductsFilePath), times(1));
        } finally {
            System.setOut(System.out);
        }
    }

    @Test
    void testLoadProductsFromFile_DataAddedToProductRepository() {
        ProductRepository.clearProductList();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        String testProductsFilePath = TestFilePaths.getTestProductsFilePath();
        List<String> testFileData = Arrays.asList(
                "apple,pr5197140,560,25",
                "pear,pr4270613,675,19",
                "lemon,pr6634365,880,43"
        );

        try (MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class);
             MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class)) {
            mockedFileUtils.when(() -> FileUtils.readFromFile(testProductsFilePath))
                    .thenReturn(testFileData);

            DataLoader dataLoader = new DataLoader();
            dataLoader.loadProductsFromFile(testProductsFilePath);

            mockedProductRepository.verify(() -> ProductRepository.addProduct(any(Product.class)), times(testFileData.size()));
            mockedProductRepository.verify(() -> ProductRepository.addProduct(new Product(
                    "apple", "pr5197140", 560, 25)), times(1));
            mockedProductRepository.verify(() -> ProductRepository.addProduct(new Product(
                    "pear", "pr4270613", 675, 19)), times(1));
            mockedProductRepository.verify(() -> ProductRepository.addProduct(new Product(
                    "lemon", "pr6634365", 880, 43)), times(1));
        } finally {
            System.setOut(System.out);
            ProductRepository.clearProductList();
        }
    }


    @Test
    void testLoadCustomersFromFile_PrintsMessage() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        String testCustomersFilePath = TestFilePaths.getTestCustomersFilePath();
        try (MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class)) {
            mockedFileUtils.when(() -> FileUtils.readFromFile(testCustomersFilePath))
                    .thenReturn(Collections.emptyList());

            DataLoader dataLoader = new DataLoader();
            dataLoader.loadCustomersFromFile(testCustomersFilePath);

            String expectedMessage = "Checking the database of customers:";
            String output = outputStream.toString();
            assertTrue(output.contains(expectedMessage),
                    "Expected message '" + expectedMessage + "' was not found in the output.");
        } finally {
            System.setOut(System.out);
        }
    }

    @Test
    void testLoadCustomersFromFile_ReadFromFileCalled() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        String testCustomersFilePath = TestFilePaths.getTestCustomersFilePath();
        try (MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class)) {
            mockedFileUtils.when(() -> FileUtils.readFromFile(testCustomersFilePath))
                    .thenReturn(Arrays.asList(
                            "Mikhail Bulgakov,3600,cID3099022",
                            "Thomas Mann,5400,cID2633111"));

            DataLoader dataLoader = new DataLoader();
            dataLoader.loadCustomersFromFile(testCustomersFilePath);

            mockedFileUtils.verify(() -> FileUtils.readFromFile(testCustomersFilePath), times(1));
        } finally {
            System.setOut(System.out);
        }
    }

    @Test
    void testLoadCustomersFromFile_DataAddedToCustomerRepository() {
        CustomerRepository.clearCustomerList();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        String testCustomersFilePath = TestFilePaths.getTestCustomersFilePath();
        List<String> testFileData = Arrays.asList(
                "Temesi Szabolcs,9000,cID9168098",
                "Egerszegi Krisztina,1356,cID5794138",
                "Nagy Anna,2160,cID5916556"
        );

        try (MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class);
             MockedStatic<CustomerRepository> mockedCustomerRepository = Mockito.mockStatic(CustomerRepository.class)) {
            mockedFileUtils.when(() -> FileUtils.readFromFile(testCustomersFilePath))
                    .thenReturn(testFileData);

            DataLoader dataLoader = new DataLoader();
            dataLoader.loadCustomersFromFile(testCustomersFilePath);

            mockedCustomerRepository.verify(() -> CustomerRepository.addCustomer(any(Customer.class)), times(testFileData.size()));
            mockedCustomerRepository.verify(() -> CustomerRepository.addCustomer(new Customer(
                    "Temesi Szabolcs", "cID9168098", 9000)), times(1));
            mockedCustomerRepository.verify(() -> CustomerRepository.addCustomer(new Customer(
                    "Egerszegi Krisztina", "cID5794138", 1356)), times(1));
            mockedCustomerRepository.verify(() -> CustomerRepository.addCustomer(new Customer(
                    "Nagy Anna", "cID5916556", 2160)), times(1));
        } finally {
            System.setOut(System.out);
            CustomerRepository.clearCustomerList();
        }
    }

    @Test
    void testTransactionsFromFile_PrintsMessage() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        String testTransactionsFilePath = TestFilePaths.getTestTransactionsFilePath();
        try (MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class)) {
            mockedFileUtils.when(() -> FileUtils.readFromFile(testTransactionsFilePath))
                    .thenReturn(Collections.emptyList());

            DataLoader dataLoader = new DataLoader();
            dataLoader.loadTransactionsFromFile(testTransactionsFilePath);

            String expectedMessage = "Checking the database of transactions: ";
            String output = outputStream.toString();
            assertTrue(output.contains(expectedMessage),
                    "Expected message '" + expectedMessage + "' was not found in the output.");
        } finally {
            System.setOut(System.out);
        }
    }

    @Test
    void testLoadTransactionsFromFile_ReadFromFileCalled() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        String testTransactionsFilePath = TestFilePaths.getTestTransactionsFilePath();
        try (MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class)) {
            mockedFileUtils.when(() -> FileUtils.readFromFile(testTransactionsFilePath))
                    .thenReturn(Arrays.asList(
                            "trId1430909,2024.02.01. 15:03:58,banana,3,720,Nagy Anna,cID5916556",
                            "trId6173011,2024.02.11. 19:11:51,apple,8,560,Tamasi Tamara,cID8448077"));

            DataLoader dataLoader = new DataLoader();
            dataLoader.loadTransactionsFromFile(testTransactionsFilePath);

            mockedFileUtils.verify(() -> FileUtils.readFromFile(testTransactionsFilePath), times(1));
        } finally {
            System.setOut(System.out);
        }
    }

    @Test
    void testLoadTransactionsFromFile_DataAddedToTransactionsRepository() {
        SalesTransactionRepository.clearSalesTransactionList();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        String testTransactionsFilePath = TestFilePaths.getTestTransactionsFilePath();
        List<String> testFileData = Arrays.asList(
                "trId1430909,2024.02.01. 15:03:58,banana,3,720,Nagy Anna,cID5916556",
                "trId6173011,2024.02.11. 19:11:51,apple,8,560,Tamasi Tamara,cID8448077",
                "trId4844949,2024.11.03. 23:42:05,cherry,3,452,Egerszegi Krisztina,cID5794138"
        );

        try (MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class);
             MockedStatic<SalesTransactionRepository> mockedTransactionRepository = Mockito.mockStatic(SalesTransactionRepository.class)) {
            mockedFileUtils.when(() -> FileUtils.readFromFile(testTransactionsFilePath))
                    .thenReturn(testFileData);

            DataLoader dataLoader = new DataLoader();
            dataLoader.loadTransactionsFromFile(testTransactionsFilePath);

            mockedTransactionRepository.verify(() -> SalesTransactionRepository.addSalesTransaction(
                    any(SalesTransaction.class)), times(testFileData.size()));
            mockedTransactionRepository.verify(() -> SalesTransactionRepository.addSalesTransaction(new SalesTransaction(
                    "trId1430909", "Nagy Anna", "cID5916556", "banana",
                    3, 720, "2024.02.01. 15:03:58")), times(1));
            mockedTransactionRepository.verify(() -> SalesTransactionRepository.addSalesTransaction(new SalesTransaction(
                    "trId6173011", "Tamasi Tamara", "cID8448077", "apple",
                    8, 560, "2024.02.11. 19:11:51")), times(1));
            mockedTransactionRepository.verify(() -> SalesTransactionRepository.addSalesTransaction(new SalesTransaction(
                    "trId4844949", "Egerszegi Krisztina", "cID5794138", "cherry",
                    3, 452, "2024.11.03. 23:42:05")), times(1));
        } finally {
            System.setOut(System.out);
            SalesTransactionRepository.clearSalesTransactionList();
        }
    }
}