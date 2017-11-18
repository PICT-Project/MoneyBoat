package com.example.tahakothawala.myapplication;

/**
 * Created by padia on 10/10/2017.
 */

public class LogObject {
    String from_name=null, to_name=null, date=null, description=null;
    Long from_id, to_id ;
    int amount ;
    public LogObject(String from_name, String to_name, String date, String description, Long from_id, Long to_id, int amount){
        this.from_name = from_name ;
        this.from_id = from_id ;
        this.to_name = to_name ;
        this.to_id = to_id ;
        this.date = date ;
        this.description = description ;
        this.amount = amount ;
    }
}
