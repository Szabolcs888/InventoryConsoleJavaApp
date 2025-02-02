package com.myinventoryapp.ui.menu;

import com.myinventoryapp.dataio.DataSaver;

public class MenuOption6SaveData {
    private final DataSaver dataSaver;

    public MenuOption6SaveData(DataSaver dataSaver) {
        this.dataSaver = dataSaver;
    }

    public void saveData() {
        dataSaver.saveAllData();
        System.out.println("Data has been saved..");
    }
}