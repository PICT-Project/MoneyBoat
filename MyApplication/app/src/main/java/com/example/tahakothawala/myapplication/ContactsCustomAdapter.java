package com.example.tahakothawala.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by padia on 9/26/2017.
 */

public class ContactsCustomAdapter extends ArrayAdapter<String> {


    public ContactsCustomAdapter(@NonNull Context context, @NonNull List<String> objects) {
        super(context, R.layout.add_member_element, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.add_member_element, parent, false);

        return customView;
    }
}