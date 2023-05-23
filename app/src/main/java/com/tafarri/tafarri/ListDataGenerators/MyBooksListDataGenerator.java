package com.tafarri.tafarri.ListDataGenerators;

import com.tafarri.tafarri.Models.BookModel;

import java.util.ArrayList;
import java.util.List;

public class MyBooksListDataGenerator {
    // DECLARING THE DATA ARRAY LIST
    static List<BookModel> allData = new ArrayList<>();

    // SETTING/RESETTING ALL SUGGESTED LINKUPS DATA
    public static void setAllDatasAfresh(List<BookModel> newAllData) {
        MyBooksListDataGenerator.allData = newAllData;
    }

    // ADDING ONE DATA TO ARRAY LIST
    public static boolean addOneData(BookModel model) {
        return allData.add(model);
    }

    // GETTING ALL DATA AS ARRAY LIST
    public static List<BookModel> getAllData() {
        return allData;
    }

    // ADDING ONE DATA TO A DESIRED POSITION IN ARRAY LIST
    public static void addOneDataToDesiredPosition(int i, BookModel model){
        allData.add(i, model);
    }
}
