package com.myinventoryapp.util;

import com.myinventoryapp.entities.Customer;
import com.myinventoryapp.entities.Product;
import com.myinventoryapp.entities.SalesTransaction;
import com.myinventoryapp.repository.CustomerRepository;
import com.myinventoryapp.repository.ProductRepository;
import com.myinventoryapp.repository.SalesTransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class IdUtilsTest {

    @Test
    void testGenerateId() {
        List<Product> productList = new ArrayList<>(Arrays.asList(
                new Product("banana", "pr5223508", 720, 102),
                new Product("tea", "pr7236284", 1200, 52),
                new Product("coffee", "pr5204875", 1800, 44)));
        List<Customer> customerList = new ArrayList<>(Arrays.asList(
                new Customer("Egerszegi Krisztina", "cID5794138", 1356),
                new Customer("Darnyi Tamás", "cID9742746", 560),
                new Customer("Thomas Mann", "cID2633111", 5400)));
        List<SalesTransaction> transactionList = new ArrayList<>(Arrays.asList(
                new SalesTransaction("trId5446146", "Darnyi Tamás", "cID9742746",
                        "apple", 1, 560, "2024.11.03. 23:59:04"),
                new SalesTransaction("trId9039796", "Kóbór János", "cID8259712",
                        "coffee", 3, 1800, "2024.11.18. 01:02:13"),
                new SalesTransaction("trId8765603", "Thomas Mann", "cID2633111",
                        "coffee", 3, 1800, "2024.11.07. 21:49:51")));

        try (MockedStatic<ProductRepository> mockProductRepository = Mockito.mockStatic(ProductRepository.class);
             MockedStatic<CustomerRepository> mockCustomerRepository = Mockito.mockStatic(CustomerRepository.class);
             MockedStatic<SalesTransactionRepository> mockTransactionRepository = Mockito.mockStatic(SalesTransactionRepository.class)) {

            mockProductRepository.when(ProductRepository::getProductList).thenAnswer(invocation -> productList);
            mockCustomerRepository.when(CustomerRepository::getCustomerList).thenAnswer(invocation -> customerList);
            mockTransactionRepository.when(SalesTransactionRepository::getSalesTransactionList).thenAnswer(invocation -> transactionList);

            Set<Integer> generatedIds = new HashSet<>();
            for (int i = 0; i < 1000; i++) {
                int id = IdUtils.generateId();

                boolean isTheNumberCorrect = id >= 1_000_000 && id <= 9_999_999;
                assertTrue(isTheNumberCorrect, "ID should be between 1000000 and 9999999, but it is not. The number is " + id);
                assertFalse(generatedIds.contains(id), "Generated ID already existed: " + id);

                productList.add(new Product("mockProduct", "pr" + id, 500, 10));
                mockProductRepository.when(ProductRepository::getProductList).thenAnswer(invocation -> productList);

                generatedIds.add(id);
            }
        }
    }
}