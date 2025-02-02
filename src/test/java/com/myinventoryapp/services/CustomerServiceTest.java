package com.myinventoryapp.services;

import com.myinventoryapp.entities.Customer;
import com.myinventoryapp.entities.Product;
import com.myinventoryapp.repository.CustomerRepository;
import com.myinventoryapp.util.IdUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Test
    void testHandleCustomerTransaction_ForRegisteredCustomer() {
        boolean isRegisteredCustomer = true;
        String customerName = "Temesi Szabolcs";
        Product product = new Product("cherry", "pr7860912", 452, 115);
        int quantitySold = 12;

        String expectedCustomerId = "cID9168098";
        CustomerService spyCustomerService = Mockito.spy(new CustomerService());
        Mockito.doReturn(expectedCustomerId).when(spyCustomerService).updateRegisteredCustomerPurchases(
                customerName, product, quantitySold);

        String customerIdResult = spyCustomerService.handleCustomerTransaction(
                isRegisteredCustomer, customerName, product, quantitySold);

        Mockito.verify(spyCustomerService, times(1)).updateRegisteredCustomerPurchases(
                customerName, product, quantitySold);
        Mockito.verify(spyCustomerService, never()).registerNewCustomer(anyString(), any(Product.class), anyInt());
        assertEquals(expectedCustomerId, customerIdResult, "The method should return the registered costumer id.");
    }

    @Test
    void testHandleCustomerTransaction_ForNewCustomer() {
        boolean isRegisteredCustomer = false;
        String customerName = "Tamasi Tamara";
        Product product = new Product("apple", "pr5197140", 588, 132);
        int quantitySold = 7;

        String expectedCustomerId = "cID8448077";
        CustomerService spyCustomerService = Mockito.spy(new CustomerService());
        Mockito.doReturn(expectedCustomerId).when(spyCustomerService).registerNewCustomer(
                customerName, product, quantitySold);

        String customerIdResult = spyCustomerService.handleCustomerTransaction(
                isRegisteredCustomer, customerName, product, quantitySold);

        Mockito.verify(spyCustomerService, never()).updateRegisteredCustomerPurchases(anyString(), any(Product.class), anyInt());
        Mockito.verify(spyCustomerService, times(1)).registerNewCustomer(
                customerName, product, quantitySold);
        assertEquals(expectedCustomerId, customerIdResult, "The method should return the new costumer ID.");
    }

    @Test
    void testUpdateRegisteredCustomerPurchases_GeneratesCorrectCustomerId() {
        String customerName = "Mikhail Bulgakov";
        String expectedCustomerId = "cID3099022";
        Product product = new Product("pineapple", "pr5711807", 894, 18);
        int quantitySold = 1;
        Customer customer = new Customer(customerName, expectedCustomerId, 3600);

        try (MockedStatic<CustomerRepository> mockedCustomerRepository = Mockito.mockStatic(CustomerRepository.class)) {
            mockedCustomerRepository.when(() -> CustomerRepository.findCustomerByName(customerName)).thenReturn(customer);

            CustomerService customerService = new CustomerService();
            String customerIdResult = customerService.updateRegisteredCustomerPurchases(customerName, product, quantitySold);

            assertEquals(expectedCustomerId, customerIdResult, "The method should return the correct customer ID.");
            mockedCustomerRepository.verify(() ->
                    CustomerRepository.findCustomerByName(customerName), times(1));
        }
    }

    @Test
    void testUpdateRegisteredCustomerPurchases_UpdatesTotalPurchases() {
        String customerName = "Thomas Mann";
        Product product = new Product("coffee", "pr5204875", 1800, 44);
        int quantitySold = 7;
        int initialTotalPurchases = 3600;
        Customer customer = new Customer(customerName, "cID2633111", initialTotalPurchases);

        try (MockedStatic<CustomerRepository> mockedCustomerRepository = Mockito.mockStatic(CustomerRepository.class)) {
            mockedCustomerRepository.when(() -> CustomerRepository.findCustomerByName(customerName)).thenReturn(customer);

            CustomerService customerService = new CustomerService();
            customerService.updateRegisteredCustomerPurchases(customerName, product, quantitySold);

            int expectedCurrentPurchase = product.getUnitPrice() * quantitySold;
            int expectedTotalPurchases = initialTotalPurchases + expectedCurrentPurchase;
            assertEquals(expectedTotalPurchases, customer.getTotalPurchases(),
                    "The customer's total purchases should be updated correctly.");
        }
    }

    @Test
    void registerNewCustomer_GeneratesCorrectCustomerId() {
        String customerName = "Töröcsik András";
        Product product = new Product("lemon", "pr6634365", 880, 45);
        int quantitySold = 12;

        try (MockedStatic<IdUtils> mockedIdUtils = Mockito.mockStatic(IdUtils.class);
             MockedStatic<CustomerRepository> mockedCustomerRepository = Mockito.mockStatic(CustomerRepository.class)) {

            mockedIdUtils.when(IdUtils::generateId).thenReturn(2422151);
            mockedCustomerRepository.when(() -> CustomerRepository.addCustomer(any(Customer.class))).
                    thenAnswer(invocation -> null);

            CustomerService customerService = new CustomerService();
            String customerIdResult = customerService.registerNewCustomer(customerName, product, quantitySold);

            String expectedCustomerId = "cID2422151";
            assertEquals(expectedCustomerId, customerIdResult, "The method should return the correct customer ID.");
            mockedIdUtils.verify(IdUtils::generateId, times(1));
        }
    }

    @Test
    void testRegisterNewCustomer_CreatesCustomerWithCorrectTotalPurchases() {
        String customerName = "Dallos Jenö";
        Product product = new Product("cocoa", "pr7553549", 235, 35);
        int quantitySold = 2;

        try (MockedStatic<IdUtils> mockedIdUtils = Mockito.mockStatic(IdUtils.class);
             MockedStatic<CustomerRepository> mockedCustomerRepository = Mockito.mockStatic(CustomerRepository.class)) {

            mockedIdUtils.when(IdUtils::generateId).thenReturn(6232706);
            mockedCustomerRepository.when(() -> CustomerRepository.addCustomer(any(Customer.class))).
                    thenAnswer(invocation -> null);

            CustomerService customerService = new CustomerService();
            String customerIdResult = customerService.registerNewCustomer(customerName, product, quantitySold);

            assertNotNull(customerIdResult, "The customer ID should not be null.");
            assertEquals(235 * 2, product.getUnitPrice() * quantitySold,
                    "The total purchase calculation should be correct.");
        }
    }

    @Test
    void testRegisterNewCustomer_AddsCustomerToRepository() {
        String customerName = "Szabó Tibor";
        String customerId = "cID6067122";
        Product product = new Product("tea", "pr7236284", 1200, 52);
        int quantitySold = 5;
        int totalPurchases = product.getUnitPrice() * quantitySold;

        try (MockedStatic<IdUtils> mockedIdUtils = Mockito.mockStatic(IdUtils.class);
             MockedStatic<CustomerRepository> mockedCustomerRepository = Mockito.mockStatic(CustomerRepository.class)) {
            mockedIdUtils.when(IdUtils::generateId).thenReturn(6067122);

            CustomerService customerService = new CustomerService();
            customerService.registerNewCustomer(customerName, product, quantitySold);

            mockedCustomerRepository.verify(() -> CustomerRepository.addCustomer(
                    argThat(customer ->
                            customer.getCustomerName().equals(customerName) &&
                                    customer.getCustomerId().equals(customerId) &&
                                    customer.getTotalPurchases() == totalPurchases
                    )
            ), times(1));
        }
    }
}