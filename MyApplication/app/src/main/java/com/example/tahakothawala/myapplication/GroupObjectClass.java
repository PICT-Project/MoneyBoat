package com.example.tahakothawala.myapplication;

/**
 * Created by tahakothawala on 25/09/2017 AD.
 */

public class GroupObjectClass {

   private String name;
    private String owe_pay;
   private int amount;

    public GroupObjectClass(String name, String owe_pay, int amount) {
        this.name = name;
        this.owe_pay = owe_pay;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwe_pay() {
        return owe_pay;
    }

    public void setOwe_pay(String owe_pay) {
        this.owe_pay = owe_pay;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
