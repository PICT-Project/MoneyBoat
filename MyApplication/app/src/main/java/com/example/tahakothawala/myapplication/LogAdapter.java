package com.example.tahakothawala.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LogAdapter extends ArrayAdapter<FinalObject> {


    public LogAdapter(@NonNull Context context, @NonNull ArrayList<FinalObject> objects) {
        super(context, R.layout.activity_groupie, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.adapter_groupie,parent,false);

        TextView amt = (TextView)customView.findViewById(R.id.amt);
        TextView youowe = (TextView)customView.findViewById(R.id.youowe);
        TextView name = (TextView)customView.findViewById(R.id.name);

        FinalObject groupObjectClass = getItem(position);
        name.setText(groupObjectClass.name);
        Integer x = groupObjectClass.amount;
        Integer y=x ;
        if(x<0){
            youowe.setText("you owe");
            y = -1*x ;
        }
        else if(x>0){
            youowe.setText("you get");
        }
        else{
            youowe.setText("Settled");
        }

        amt.setText(y.toString());

        return customView;
    }
}
