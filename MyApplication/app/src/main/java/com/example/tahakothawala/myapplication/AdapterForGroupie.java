package com.example.tahakothawala.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterForGroupie extends ArrayAdapter<PersonalTransactions> {

    HashMap<Long,String> hm = null ;
    public AdapterForGroupie(@NonNull Context context, @NonNull ArrayList<PersonalTransactions> objects, HashMap<Long,String> hm) {
        super(context, R.layout.activity_groupie, objects);
        this.hm = hm ;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.adapter_groupie,parent,false);

        TextView amt = (TextView)customView.findViewById(R.id.amt);
        TextView youowe = (TextView)customView.findViewById(R.id.youowe);
        TextView name = (TextView)customView.findViewById(R.id.name);

        PersonalTransactions groupObjectClass = getItem(position);
        name.setText(hm.get(groupObjectClass.num));
        Integer x = groupObjectClass.amount;
        if(x<0){
            youowe.setText("you owe");
            x = -1*x ;
        }
        else if(x>0){
            youowe.setText("you get");
        }
        else{
            youowe.setText("Settled");
        }

        amt.setText(x.toString());

        return customView;
    }
}
