package com.myinventoryapp;

import com.myinventoryapp.ui.menu.*;
import com.myinventoryapp.util.Colors;
import com.myinventoryapp.util.ErrorHandler;
import com.myinventoryapp.dataio.DataLoader;
import com.myinventoryapp.util.MenuOption;

public class InventoryApp {
    private MenuOption1Sell menuOption1Sell;
    private MenuOption2GoodsReceipt menuOption2GoodsReceipt;
    private MenuOption3DisplayProducts menuOption3DisplayProducts;
    private MenuOption4DisplayCustomers menuOption4DisplayCustomers;
    private MenuOption5DisplayTransactions menuOption5DisplayTransactions;
    private MenuOption6SaveData menuOption6SaveData;

    public InventoryApp() {
    }

    public InventoryApp(
            MenuOption1Sell menuOption1Sell,
            MenuOption2GoodsReceipt menuOption2GoodsReceipt,
            MenuOption3DisplayProducts menuOption3DisplayProducts,
            MenuOption4DisplayCustomers menuOption4DisplayCustomers,
            MenuOption5DisplayTransactions menuOption5DisplayTransactions,
            MenuOption6SaveData menuOption6SaveData) {
        this.menuOption1Sell = menuOption1Sell;
        this.menuOption2GoodsReceipt = menuOption2GoodsReceipt;
        this.menuOption3DisplayProducts = menuOption3DisplayProducts;
        this.menuOption4DisplayCustomers = menuOption4DisplayCustomers;
        this.menuOption5DisplayTransactions = menuOption5DisplayTransactions;
        this.menuOption6SaveData = menuOption6SaveData;
    }

    public static void main(String[] args) {
        DataLoader dataLoader = new DataLoader();
        dataLoader.loadAllData();
        InventoryApp app = new InventoryApp(
                new MenuOption1Sell(),
                new MenuOption2GoodsReceipt(),
                new MenuOption3DisplayProducts(),
                new MenuOption4DisplayCustomers(),
                new MenuOption5DisplayTransactions(),
                new MenuOption6SaveData()
        );
        int userChoice = app.menuSelection(app.getWelcomeMessage());
        app.transactionSelector(userChoice);
    }

    int menuSelection(String welcomeMessage) {
        System.out.print(welcomeMessage);
        int userChoice;
        do {
            userChoice = ErrorHandler.getValidNumber(
                    "\n1. Sell Product\n" +
                            "2. Receive Product\n" +
                            "3. Display Available Products\n" +
                            "4. Display Customers\n" +
                            "5. Display Transactions\n" +
                            "6. Save and Exit\n");
            if (userChoice < 1 || userChoice > 6) {
                System.out.println("You can choose from 1 to 6!");
            }
        } while (userChoice < 1 || userChoice > 6);
        return userChoice;
    }

    void transactionSelector(int userChoice) {
        MenuOption menuOption = MenuOption.fromValue(userChoice);
        switch (menuOption) {
            case SELL_PRODUCT:
                menuOption1Sell.sellProduct("\n-SELL PRODUCT MENU-\n");
                break;
            case RECEIVE_PRODUCT:
                menuOption2GoodsReceipt.goodsReceipt("\n-RECEIVE PRODUCT MENU-\n");
                break;
            case DISPLAY_AVAILABLE_PRODUCTS:
                menuOption3DisplayProducts.displayProductList("\n-DISPLAY AVAILABLE PRODUCTS MENU-\n");
                break;
            case DISPLAY_CUSTOMERS:
                menuOption4DisplayCustomers.displayCustomerList("\n-DISPLAY CUSTOMERS MENU-\n");
                break;
            case DISPLAY_TRANSACTIONS:
                menuOption5DisplayTransactions.displayTransactionList("\n-DISPLAY TRANSACTIONS MENU-\n");
                break;
            case SAVE_AND_EXIT:
                menuOption6SaveData.saveData();
                break;
            default:
                throw new IllegalArgumentException("Invalid menu choice: " + userChoice);
        }
        if (menuOption != MenuOption.SAVE_AND_EXIT) {
            int userChoiceAgain = menuSelection("\n-MAIN MENU-\n");
            transactionSelector(userChoiceAgain);
        }
    }

    private String getWelcomeMessage() {
        return "\n\nWelcome to the inventory system!" +
                Colors.GREEN_UNDERLINED.getColorCode() + "\n\nYou can choose from the following menu items:" +
                Colors.RESET.getColorCode();
    }

    void setMenuOption1Sell(MenuOption1Sell menuOption1Sell) {
        this.menuOption1Sell = menuOption1Sell;
    }

    void setMenuOption2GoodsReceipt(MenuOption2GoodsReceipt menuOption2GoodsReceipt) {
        this.menuOption2GoodsReceipt = menuOption2GoodsReceipt;
    }

    void setMenuOption3DisplayProducts(MenuOption3DisplayProducts menuOption3DisplayProducts) {
        this.menuOption3DisplayProducts = menuOption3DisplayProducts;
    }


    void setMenuOption4DisplayCustomers(MenuOption4DisplayCustomers menuOption4DisplayCustomers) {
        this.menuOption4DisplayCustomers = menuOption4DisplayCustomers;
    }

    void setMenuOption5DisplayTransactions(MenuOption5DisplayTransactions menuOption5DisplayTransactions) {
        this.menuOption5DisplayTransactions = menuOption5DisplayTransactions;
    }

    void setMenuOption6SaveData(MenuOption6SaveData menuOption6SaveData) {
        this.menuOption6SaveData = menuOption6SaveData;
    }
}