package com.example.tahakothawala.myapplication;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tahakothawala on 26/09/2017 AD.
 */

public class ThirdAdapter extends ArrayAdapter<TransactionObjectSplitter> {

    HashMap<Long,String> hm = null ;
    TextView to,from,amt,date,desc;
    public ThirdAdapter(@NonNull Context context, @NonNull ArrayList<TransactionObjectSplitter> objects, HashMap<Long,String> hm) {
        super(context, R.layout.third_adapter, objects);
        this.hm = hm ;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.third_adapter,parent,false);

        TransactionObjectSplitter groupTransObj = getItem(position);
        to = (TextView)customView.findViewById(R.id.to);
        from = (TextView)customView.findViewById(R.id.from);
        amt = (TextView)customView.findViewById(R.id.amt);
        date = (TextView)customView.findViewById(R.id.date);
        desc = (TextView)customView.findViewById(R.id.desc);

        to.setText(hm.get(groupTransObj.to_id));
        from.setText(hm.get(groupTransObj.from_id));
        Integer x = groupTransObj.amount;
        amt.setText(x.toString());
        date.setText(groupTransObj.date);
        desc.setText(groupTransObj.description);

        return customView;
    }
}