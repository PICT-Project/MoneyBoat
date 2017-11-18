package com.example.tahakothawala.myapplication;

import java.util.Date;

/**
 * Created by tahakothawala on 17/09/2017 AD.
 */

public class ListData {
    String expense;
    String price;
    String date;

    public ListData(String expense, String price, String date) {
        this.expense = expense;
        this.price = price;
        this.date = date;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
