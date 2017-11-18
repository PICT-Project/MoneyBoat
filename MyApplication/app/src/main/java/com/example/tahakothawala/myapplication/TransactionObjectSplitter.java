package com.example.tahakothawala.myapplication;

import java.io.Serializable;

/**
 * Created by padia on 10/9/2017.
 */

public class TransactionObjectSplitter implements Serializable{
    Long from_id,to_id ;
    int amount ;
    String description ;
    String date ;
    public TransactionObjectSplitter(Long from_id, Long to_id, int amount, String description, String date){
        this.from_id = from_id ;
        this.to_id = to_id ;
        this.amount = amount ;
        this.description = description ;
        this.date = date.substring(5,11) ;
    }
}
