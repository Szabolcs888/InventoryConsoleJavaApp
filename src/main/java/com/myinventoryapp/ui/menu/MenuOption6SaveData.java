package com.myinventoryapp.ui.menu;

import com.myinventoryapp.dataio.DataSaver;

public class MenuOption6SaveData {

    public void saveData() {
        DataSaver dataSaver = new DataSaver();
        dataSaver.saveAllData();
        System.out.println("Data has been saved..");
    }
}