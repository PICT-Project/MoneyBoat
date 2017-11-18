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

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.resource;

/**
 * Created by tahakothawala on 17/09/2017 AD.
 */

public class CustomAdapter extends ArrayAdapter<TransactionDetails> {


    public CustomAdapter(@NonNull Context context, @NonNull ArrayList<TransactionDetails> objects) {
        super(context, R.layout.mylayout, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.mylayout,parent,false);

        TransactionDetails transactionDetails = getItem(position);
        TextView t1 = (TextView)customView.findViewById(R.id.textView13);
        TextView t2 = (TextView)customView.findViewById(R.id.textView12);
        TextView t3 = (TextView)customView.findViewById(R.id.textView16);
        TextView t4 = (TextView)customView.findViewById(R.id.textView5);

        Integer x = transactionDetails.AMOUNT;
        t1.setText(transactionDetails.DESCRIPTION);
        t2.setText(x.toString());
        t3.setText(transactionDetails.DATE);
        t4.setText(transactionDetails.CATEGORY);

        return customView;

    }
}
