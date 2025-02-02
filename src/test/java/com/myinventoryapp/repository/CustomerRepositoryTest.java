package com.myinventoryapp.repository;

import com.myinventoryapp.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerRepositoryTest {

    @BeforeEach
    void setup() {
        CustomerRepository.clearCustomerList();
    }

    @Test
    void testGetCustomerList_EmptyList() {
        List<Customer> customers = CustomerRepository.getCustomerList();

        assertTrue(customers.isEmpty(), "The customer list should be empty initially.");
        assertNotSame(customers, CustomerRepository.getCustomerList(),
                "The returned list should be a new instance.");
    }

    @Test
    void testGetCustomerList_ModificationDoesNotAffectOriginal() {
        List<Customer> customers = CustomerRepository.getCustomerList();

        customers.add(new Customer("Ancsa", "cID7785719", 15750));

        assertTrue(CustomerRepository.getCustomerList().isEmpty(),
                "The original customer list should not be affected by modifications to the returned list.");
    }

    @Test
    void testGetCustomerList_AfterAddingCustomers() {
        Customer customer1 = new Customer("Beata", "cID1548931", 1120);
        Customer customer2 = new Customer("Lakatos Regina", "cID1506683", 3865);
        CustomerRepository.addCustomer(customer1);
        CustomerRepository.addCustomer(customer2);

        List<Customer> customers = CustomerRepository.getCustomerList();

        assertEquals(2, customers.size(), "The customer list should contain two customers.");
        assertTrue(customers.contains(customer1),
                "The customer list should contain 'Beata'.");
        assertTrue(customers.contains(customer2),
                "The customers list should contain 'Lakatos Regina'.");
    }

    @Test
    void testFindCustomerByName_EmptyList() {
        assertNull(CustomerRepository.findCustomerByName("Thomas"),
                "Expected null when searching in an empty list.");
    }

    @Test
    void testFindCustomerByName() {
        Customer customer1 = new Customer("Töröcsik András", "cID2422151", 560);
        Customer customer2 = new Customer("Kovács Ágnes", "cID5513060", 15081);
        CustomerRepository.addCustomer(customer1);
        CustomerRepository.addCustomer(customer2);

        Customer foundCustomerByNameResult = CustomerRepository.findCustomerByName("Kovács Ágnes");

        assertNotNull(foundCustomerByNameResult, "Expected to find the customer 'Kovács Ágnes'.");
        assertEquals(customer2, foundCustomerByNameResult);
    }

    @Test
    void testFindCustomerByName_CaseInsensitiveSearch() {
        Customer customer = new Customer("Zsanett", "cID3551298", 35760);
        CustomerRepository.addCustomer(customer);

        Customer foundCustomerByNameResult = CustomerRepository.findCustomerByName("zSanETt");

        assertNotNull(foundCustomerByNameResult, "Expected to find the customer 'Zsanett' with case-insensitive search.");
        assertEquals(customer, foundCustomerByNameResult, "The returned customer does not match the expected one.");
    }

    @Test
    void testFindCustomerByName_ReturnsNullForNonExistentName() {
        Customer customer = new Customer("Thomas Mann", "cID2633111", 5400);
        CustomerRepository.addCustomer(customer);

        Customer foundCustomerByNameResult = CustomerRepository.findCustomerByName("Daniel Keyes");

        assertNull(foundCustomerByNameResult, "Expected null when the customer name does not exist.");
    }

    @Test
    void testFindCustomerByName_ReturnsFirstMatch() {
        Customer customer1 = new Customer("Egerszegi Krisztina", "cID5794138", 1356);
        Customer customer2 = new Customer("Egerszegi Krisztina", "cID7452340", 3000);
        CustomerRepository.addCustomer(customer1);
        CustomerRepository.addCustomer(customer2);

        Customer foundCustomerByNameResult = CustomerRepository.findCustomerByName("Egerszegi Krisztina");

        assertNotNull(foundCustomerByNameResult, "Expected to find the first customer named 'Egerszegi Krisztina'.");
        assertEquals(customer1, foundCustomerByNameResult, "The returned customer does not match the first match.");
        assertNotEquals(customer2, foundCustomerByNameResult, "The returned customer match the second match.");
    }
}