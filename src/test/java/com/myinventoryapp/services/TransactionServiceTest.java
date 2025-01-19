package com.myinventoryapp.services;

import com.myinventoryapp.entities.Product;
import com.myinventoryapp.entities.SalesTransaction;
import com.myinventoryapp.repository.SalesTransactionRepository;
import com.myinventoryapp.util.DateUtils;
import com.myinventoryapp.util.IdUtils;
import com.myinventoryapp.util.displayhelpers.TransactionDisplayHelper;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class TransactionServiceTest {

    @Test
    void testTransactionRegistration_CallsRequiredMethods() {
        boolean isRegisteredCustomer = true;
        String customerName = "Nagy Anna";
        String customerId = "cID5916556";
        int quantitySold = 5;
        String transactionId = "trId7104850";
        String transactionDate = "2025.01.02. 18:55:21";
        Product product = new Product("mango", "pr4531265", 1350, 46);

        try (MockedStatic<IdUtils> mockedIdUtils = Mockito.mockStatic(IdUtils.class);
             MockedStatic<DateUtils> mockedDateUtils = Mockito.mockStatic(DateUtils.class);
             MockedStatic<TransactionDisplayHelper> mockedTransactionDisplayHelper =
                     Mockito.mockStatic(TransactionDisplayHelper.class);
             MockedStatic<SalesTransactionRepository> mockedSalesTransactionRepository =
                     Mockito.mockStatic(SalesTransactionRepository.class)) {

            mockedIdUtils.when(IdUtils::generateId).thenReturn(7104850);
            mockedDateUtils.when(DateUtils::getCurrentFormattedDate).thenReturn(transactionDate);
            mockedTransactionDisplayHelper.when(() -> TransactionDisplayHelper.displayTransactionInfo(
                            product, quantitySold, customerName, customerId, isRegisteredCustomer, transactionDate)).
                    thenAnswer(invocation -> null);
            mockedSalesTransactionRepository.when(() -> SalesTransactionRepository.addSalesTransaction(any(SalesTransaction.class))).
                    thenAnswer(invocation -> null);

            TransactionService transactionService = new TransactionService();
            transactionService.transactionRegistration(isRegisteredCustomer, customerName, customerId, product, quantitySold);

            SalesTransaction expectedSalesTransaction = new SalesTransaction(
                    transactionId, customerName, customerId, product.getProductName(),
                    quantitySold, product.getUnitPrice(), transactionDate);
            mockedIdUtils.verify(IdUtils::generateId, times(1));
            mockedDateUtils.verify(DateUtils::getCurrentFormattedDate, times(1));
            mockedTransactionDisplayHelper.verify(() -> TransactionDisplayHelper.displayTransactionInfo(
                            product, quantitySold, customerName, customerId, isRegisteredCustomer, transactionDate),
                    times(1));
            mockedSalesTransactionRepository.verify(() -> SalesTransactionRepository.addSalesTransaction(
                    expectedSalesTransaction), times(1));
        }
    }
}