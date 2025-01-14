package com.myinventoryapp.services;

import com.myinventoryapp.entities.Product;
import org.junit.jupiter.api.Test;
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

        String expectedResponse = "cID9168098";
        CustomerService spyCustomerService = Mockito.spy(new CustomerService());
        Mockito.doReturn(expectedResponse).when(spyCustomerService).updateRegisteredCustomerPurchases(
                customerName, product, quantitySold);

        String result = spyCustomerService.handleCustomerTransaction(
                isRegisteredCustomer, customerName, product, quantitySold);

        Mockito.verify(spyCustomerService, times(1)).updateRegisteredCustomerPurchases(
                customerName, product, quantitySold);
        Mockito.verify(spyCustomerService, never()).registerNewCustomer(
                Mockito.anyString(), Mockito.any(Product.class), Mockito.anyInt());
        assertEquals(expectedResponse, result,
                "The method should return the registered costumer id.");
    }

    @Test
    void testHandleCustomerTransaction_ForNewCustomer() {
        boolean isRegisteredCustomer = false;
        String customerName = "Tamasi Tamara";
        Product product = new Product("apple", "pr5197140", 588, 132);
        int quantitySold = 5;

        String expectedResponse = "cID8448077";
        CustomerService spyCustomerService = Mockito.spy(new CustomerService());
        Mockito.doReturn(expectedResponse).when(spyCustomerService).registerNewCustomer(
                customerName, product, quantitySold);

        String result = spyCustomerService.handleCustomerTransaction(
                isRegisteredCustomer, customerName, product, quantitySold);

        Mockito.verify(spyCustomerService, never()).updateRegisteredCustomerPurchases(
                Mockito.anyString(), Mockito.any(Product.class), Mockito.anyInt());
        Mockito.verify(spyCustomerService, times(1)).registerNewCustomer(
                customerName, product, quantitySold);
        assertEquals(expectedResponse, result,
                "The method should return the new costumer ID.");
    }
}