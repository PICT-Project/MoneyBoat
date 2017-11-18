package com.example.tahakothawala.myapplication;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tahakothawala on 20/09/2017 AD.
 */

public class CustomAdapter2 extends ArrayAdapter<String> {

    public CustomAdapter2(@NonNull Context context, @NonNull ArrayList<String> objects) {
        super(context, R.layout.mylayout, objects);
    }

}
