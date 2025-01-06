package com.myinventoryapp.dataio;

import com.myinventoryapp.repository.CustomerRepository;
import com.myinventoryapp.repository.ProductRepository;
import com.myinventoryapp.repository.SalesTransactionRepository;
import com.myinventoryapp.entities.Customer;
import com.myinventoryapp.entities.Product;
import com.myinventoryapp.entities.SalesTransaction;
import com.myinventoryapp.util.FileUtils;
import com.myinventoryapp.util.testutils.TestFilePaths;
import com.myinventoryapp.util.testutils.TestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class DataLoaderTest {

    @Test
    void testLoadAllData_InvokesFilePathsGetters() {
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
    void testLoadAllData_CallsEachLoaderWithExpectedPaths() {
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

            // Verify that the load methods are called with the correct arguments
            verify(dataLoader, times(1))
                    .loadProductsFromFile(TestFilePaths.getTestProductsFilePath());
            verify(dataLoader, times(1))
                    .loadCustomersFromFile(TestFilePaths.getTestCustomersFilePath());
            verify(dataLoader, times(1))
                    .loadTransactionsFromFile(TestFilePaths.getTestTransactionsFilePath());
        }
    }

    @Test
    void testLoadProductsFromFile_PrintsMessage() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();

        String testProductsFilePath = TestFilePaths.getTestProductsFilePath();
        try (MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class);
             MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class)) {
            mockedFileUtils.when(() -> FileUtils.readFromFile(testProductsFilePath))
                    .thenReturn(Collections.emptyList());
            mockedProductRepository.when(() -> ProductRepository.addProduct(any(Product.class)))
                    .thenAnswer(invocation -> null);

            DataLoader dataLoader = new DataLoader();
            dataLoader.loadProductsFromFile(testProductsFilePath);

            String expectedMessage = "Checking the database of products: ";
            String output = outputStream.toString();
            assertTrue(output.contains(expectedMessage),
                    "Expected message '" + expectedMessage + "' was not found in the output.");
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testLoadProductsFromFile_ReadFromFileCalled() {
        TestUtils.redirectSystemOut();

        String testProductsFilePath = TestFilePaths.getTestProductsFilePath();
        try (MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class);
             MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class)) {
            mockedFileUtils.when(() -> FileUtils.readFromFile(testProductsFilePath))
                    .thenReturn(Collections.emptyList());
            mockedProductRepository.when(() -> ProductRepository.addProduct(any(Product.class)))
                    .thenAnswer(invocation -> null);

            DataLoader dataLoader = new DataLoader();
            dataLoader.loadProductsFromFile(testProductsFilePath);

            mockedFileUtils.verify(() -> FileUtils.readFromFile(testProductsFilePath), times(1));
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testLoadProductsFromFile_DataAddedToProductRepository() {
        TestUtils.redirectSystemOut();

        String testProductsFilePath = TestFilePaths.getTestProductsFilePath();
        List<String> testProductList = Arrays.asList(
                "apple,pr5197140,560,25",
                "pear,pr4270613,675,19",
                "lemon,pr6634365,880,43"
        );

        try (MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class);
             MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class)) {
            mockedFileUtils.when(() -> FileUtils.readFromFile(testProductsFilePath))
                    .thenReturn(testProductList);
            mockedProductRepository.when(() -> ProductRepository.addProduct(any(Product.class)))
                    .thenAnswer(invocation -> null);

            DataLoader dataLoader = new DataLoader();
            dataLoader.loadProductsFromFile(testProductsFilePath);

            List<Product> expectedProducts = Arrays.asList(
                    new Product("apple", "pr5197140", 560, 25),
                    new Product("pear", "pr4270613", 675, 19),
                    new Product("lemon", "pr6634365", 880, 43)
            );
            mockedProductRepository.verify(() ->
                    ProductRepository.addProduct(any(Product.class)), times(testProductList.size()));
            mockedProductRepository.verify(() ->
                    ProductRepository.addProduct(expectedProducts.get(0)), times(1));
            mockedProductRepository.verify(() ->
                    ProductRepository.addProduct(expectedProducts.get(1)), times(1));
            mockedProductRepository.verify(() ->
                    ProductRepository.addProduct(expectedProducts.get(2)), times(1));
        } finally {
            TestUtils.restoreSystemOut();
            ProductRepository.clearProductList();
        }
    }

    @Test
    void testLoadCustomersFromFile_PrintsMessage() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();

        String testCustomersFilePath = TestFilePaths.getTestCustomersFilePath();
        try (MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class);
             MockedStatic<CustomerRepository> mockedCustomerRepository = Mockito.mockStatic(CustomerRepository.class)) {
            mockedFileUtils.when(() -> FileUtils.readFromFile(testCustomersFilePath))
                    .thenReturn(Collections.emptyList());
            mockedCustomerRepository.when(() -> CustomerRepository.addCustomer(any(Customer.class)))
                    .thenAnswer(invocation -> null);

            DataLoader dataLoader = new DataLoader();
            dataLoader.loadCustomersFromFile(testCustomersFilePath);

            String expectedMessage = "Checking the database of customers:";
            String output = outputStream.toString();
            assertTrue(output.contains(expectedMessage),
                    "Expected message '" + expectedMessage + "' was not found in the output.");
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testLoadCustomersFromFile_ReadFromFileCalled() {
        TestUtils.redirectSystemOut();

        String testCustomersFilePath = TestFilePaths.getTestCustomersFilePath();
        try (MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class);
             MockedStatic<CustomerRepository> mockedCustomerRepository = Mockito.mockStatic(CustomerRepository.class)) {
            mockedFileUtils.when(() -> FileUtils.readFromFile(testCustomersFilePath))
                    .thenReturn(Collections.emptyList());
            mockedCustomerRepository.when(() -> CustomerRepository.addCustomer(any(Customer.class)))
                    .thenAnswer(invocation -> null);

            DataLoader dataLoader = new DataLoader();
            dataLoader.loadCustomersFromFile(testCustomersFilePath);

            mockedFileUtils.verify(() -> FileUtils.readFromFile(testCustomersFilePath), times(1));
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testLoadCustomersFromFile_DataAddedToCustomerRepository() {
        TestUtils.redirectSystemOut();

        String testCustomersFilePath = TestFilePaths.getTestCustomersFilePath();
        List<String> testCustomerList = Arrays.asList(
                "Temesi Szabolcs,9000,cID9168098",
                "Egerszegi Krisztina,1356,cID5794138",
                "Nagy Anna,2160,cID5916556"
        );

        try (MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class);
             MockedStatic<CustomerRepository> mockedCustomerRepository = Mockito.mockStatic(CustomerRepository.class)) {
            mockedFileUtils.when(() -> FileUtils.readFromFile(testCustomersFilePath))
                    .thenReturn(testCustomerList);
            mockedCustomerRepository.when(() -> CustomerRepository.addCustomer(any(Customer.class)))
                    .thenAnswer(invocation -> null);

            DataLoader dataLoader = new DataLoader();
            dataLoader.loadCustomersFromFile(testCustomersFilePath);

            List<Customer> expectedCustomers = Arrays.asList(
                    new Customer("Temesi Szabolcs", "cID9168098", 9000),
                    new Customer("Egerszegi Krisztina", "cID5794138", 1356),
                    new Customer("Nagy Anna", "cID5916556", 2160)
            );
            mockedCustomerRepository.verify(() ->
                    CustomerRepository.addCustomer(any(Customer.class)), times(testCustomerList.size()));
            mockedCustomerRepository.verify(() ->
                    CustomerRepository.addCustomer(expectedCustomers.get(0)), times(1));
            mockedCustomerRepository.verify(() ->
                    CustomerRepository.addCustomer(expectedCustomers.get(1)), times(1));
            mockedCustomerRepository.verify(() ->
                    CustomerRepository.addCustomer(expectedCustomers.get(2)), times(1));
        } finally {
            TestUtils.restoreSystemOut();
            CustomerRepository.clearCustomerList();
        }
    }

    @Test
    void testTransactionsFromFile_PrintsMessage() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();

        String testTransactionsFilePath = TestFilePaths.getTestTransactionsFilePath();
        try (MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class);
             MockedStatic<SalesTransactionRepository> mockedTransactionRepository =
                     Mockito.mockStatic(SalesTransactionRepository.class)) {
            mockedFileUtils.when(() -> FileUtils.readFromFile(testTransactionsFilePath))
                    .thenReturn(Collections.emptyList());
            mockedTransactionRepository.when(() -> SalesTransactionRepository.addSalesTransaction(any(SalesTransaction.class)))
                    .thenAnswer(invocation -> null);

            DataLoader dataLoader = new DataLoader();
            dataLoader.loadTransactionsFromFile(testTransactionsFilePath);

            String expectedMessage = "Checking the database of transactions: ";
            String output = outputStream.toString();
            assertTrue(output.contains(expectedMessage),
                    "Expected message '" + expectedMessage + "' was not found in the output.");
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testLoadTransactionsFromFile_ReadFromFileCalled() {
        TestUtils.redirectSystemOut();

        String testTransactionsFilePath = TestFilePaths.getTestTransactionsFilePath();
        try (MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class);
             MockedStatic<SalesTransactionRepository> mockedTransactionRepository =
                     Mockito.mockStatic(SalesTransactionRepository.class)) {
            mockedFileUtils.when(() -> FileUtils.readFromFile(testTransactionsFilePath)).
                    thenReturn(Collections.emptyList());
            mockedTransactionRepository.when(() -> SalesTransactionRepository.addSalesTransaction(any(SalesTransaction.class)))
                    .thenAnswer(invocation -> null);

            DataLoader dataLoader = new DataLoader();
            dataLoader.loadTransactionsFromFile(testTransactionsFilePath);

            mockedFileUtils.verify(() -> FileUtils.readFromFile(testTransactionsFilePath), times(1));
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testLoadTransactionsFromFile_DataAddedToTransactionsRepository() {
        TestUtils.redirectSystemOut();

        String testTransactionsFilePath = TestFilePaths.getTestTransactionsFilePath();
        List<String> testTransactionsList = Arrays.asList(
                "trId1430909,2024.02.01. 15:03:58,banana,3,720,Nagy Anna,cID5916556",
                "trId6173011,2024.02.11. 19:11:51,apple,8,560,Tamasi Tamara,cID8448077",
                "trId4844949,2024.11.03. 23:42:05,cherry,3,452,Egerszegi Krisztina,cID5794138"
        );

        try (MockedStatic<FileUtils> mockedFileUtils = Mockito.mockStatic(FileUtils.class);
             MockedStatic<SalesTransactionRepository> mockedTransactionRepository =
                     Mockito.mockStatic(SalesTransactionRepository.class)) {
            mockedFileUtils.when(() -> FileUtils.readFromFile(testTransactionsFilePath)).
                    thenReturn(testTransactionsList);
            mockedTransactionRepository.when(() -> SalesTransactionRepository.addSalesTransaction(any(SalesTransaction.class))).
                    thenAnswer(invocation -> null);

            DataLoader dataLoader = new DataLoader();
            dataLoader.loadTransactionsFromFile(testTransactionsFilePath);

            List<SalesTransaction> expectedSalesTransactions = Arrays.asList(
                    new SalesTransaction("trId1430909", "Nagy Anna", "cID5916556",
                            "banana",3, 720,"2024.02.01. 15:03:58"),
                    new SalesTransaction("trId6173011", "Tamasi Tamara", "cID8448077",
                            "apple", 8, 560,"2024.02.11. 19:11:51"),
                    new SalesTransaction("trId4844949", "Egerszegi Krisztina", "cID5794138",
                            "cherry", 3, 452,"2024.11.03. 23:42:05")
            );
            mockedTransactionRepository.verify(() ->
                    SalesTransactionRepository.addSalesTransaction(any(SalesTransaction.class)), times(testTransactionsList.size()));
            mockedTransactionRepository.verify(() ->
                    SalesTransactionRepository.addSalesTransaction(expectedSalesTransactions.get(0)), times(1));
            mockedTransactionRepository.verify(() ->
                    SalesTransactionRepository.addSalesTransaction(expectedSalesTransactions.get(1)), times(1));
            mockedTransactionRepository.verify(() ->
                    SalesTransactionRepository.addSalesTransaction(expectedSalesTransactions.get(2)), times(1));
        } finally {
            TestUtils.restoreSystemOut();
            SalesTransactionRepository.clearSalesTransactionList();
        }
    }
}