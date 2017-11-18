package com.example.tahakothawala.myapplication;

/**
 * Created by padia on 10/10/2017.
 */

public class FinalObject {
    String name ;
    Long id ;
    int amount ;
    String description ;
    String date ;
    public FinalObject(Long id, int amount, String description, String date, String name){
        this.id = id ;
        this.amount = amount ;
        this.description = description ;
        this.date = date.substring(5,11) ;
        this.name = name ;
    }
}
