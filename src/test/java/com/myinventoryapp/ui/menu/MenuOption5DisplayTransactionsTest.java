package com.myinventoryapp.ui.menu;

import com.myinventoryapp.entities.SalesTransaction;
import com.myinventoryapp.repository.SalesTransactionRepository;
import com.myinventoryapp.util.displayhelpers.TransactionDisplayHelper;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

class MenuOption5DisplayTransactionsTest {

    @Test
    void testDisplayTransactionList_CallsRepositoryAndHelper() {
        String menuMessage = "\n-DISPLAY TRANSACTIONS MENU-\n";
        List<SalesTransaction> transactionList = Arrays.asList(
                new SalesTransaction("trId1818222", "Kocsis Zoltán", "cID2088505",
                        "coffee", 4, 1800, "2024.02.02. 02:18:35"),
                new SalesTransaction("trId5044095", "Váradi Zsolt", "cID7606389",
                        "juice", 15, 870, "2024.02.02. 02:22:09"),
                new SalesTransaction("trId6256471", "Temesi Szabolcs", "cID9168098",
                        "coffee", 5, 1800, "2024.02.02. 20:32:00")
        );

        try (MockedStatic<SalesTransactionRepository> mockedSalesTransactionRepository = Mockito.mockStatic(SalesTransactionRepository.class);
             MockedStatic<TransactionDisplayHelper> mockedTransactionDisplayHelper = Mockito.mockStatic(TransactionDisplayHelper.class)) {

            mockedSalesTransactionRepository.when(SalesTransactionRepository::getSalesTransactionList).thenReturn(transactionList);
            mockedTransactionDisplayHelper.when(() -> TransactionDisplayHelper.displayTransactionList(anyList(), anyString())).
                    thenAnswer(invocation -> null);

            MenuOption5DisplayTransactions menuOption5DisplayTransactions = new MenuOption5DisplayTransactions();
            menuOption5DisplayTransactions.displayTransactionList(menuMessage);

            mockedSalesTransactionRepository.verify(SalesTransactionRepository::getSalesTransactionList, times(1));
            mockedTransactionDisplayHelper.verify(() -> TransactionDisplayHelper.displayTransactionList(
                    transactionList, menuMessage), times(1));
        }
    }

    @Test
    void testDisplayTransactionList_CallsRepositoryAndHelper_EmptyList() {
        String menuMessage = "\n-DISPLAY TRANSACTIONS MENU-\n";

        try (MockedStatic<SalesTransactionRepository> mockedSalesTransactionRepository = Mockito.mockStatic(SalesTransactionRepository.class);
             MockedStatic<TransactionDisplayHelper> mockedTransactionDisplayHelper = Mockito.mockStatic(TransactionDisplayHelper.class)) {

            mockedSalesTransactionRepository.when(SalesTransactionRepository::getSalesTransactionList).thenReturn(Collections.emptyList());
            mockedTransactionDisplayHelper.when(() -> TransactionDisplayHelper.displayTransactionList(anyList(), anyString())).
                    thenAnswer(invocation -> null);

            MenuOption5DisplayTransactions menuOption5DisplayTransactions = new MenuOption5DisplayTransactions();
            menuOption5DisplayTransactions.displayTransactionList(menuMessage);

            mockedSalesTransactionRepository.verify(SalesTransactionRepository::getSalesTransactionList, times(1));
            mockedTransactionDisplayHelper.verify(() -> TransactionDisplayHelper.displayTransactionList(
                    Collections.emptyList(), menuMessage), times(1));
        }
    }
}