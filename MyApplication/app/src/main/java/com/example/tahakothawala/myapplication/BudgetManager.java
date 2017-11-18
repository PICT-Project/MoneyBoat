package com.example.tahakothawala.myapplication;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.zip.Inflater;

public class BudgetManager extends Fragment implements AdapterView.OnItemSelectedListener {

    ListView listView;
    Button seeAll,reset,setBudget;
    FloatingActionButton fab;
    PieChart pieChart;
    View mview;
    TextView spend,amount,updatedAsOn,balance;

    Integer z;

    String desc,cat,amt;
    int amnt;
     DatabaseHandler databaseHandler;

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget_manager, container, false);
        mview = inflater.inflate(R.layout.add_expense_dialog_box,null);
        spend = (TextView) view.findViewById(R.id.Spend);
        reset = (Button) view.findViewById(R.id.reset);
        pieChart = (PieChart) view.findViewById(R.id.pie);
        setBudget = (Button)view.findViewById(R.id.button4);
        balance = (TextView)view.findViewById(R.id.textView34);
        updatedAsOn = (TextView) view.findViewById(R.id.textView9);
        fab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton2);
        seeAll = (Button) view.findViewById(R.id.button8);
        seeAll.setText("    \u25BC   See All");
        listView = (ListView) view.findViewById(R.id.listView);
        updatedAsOn.setText("Updated as on " + DateFormat.getDateInstance().format(new Date()));
        amount = (TextView) view.findViewById(R.id.textView6);
        setSpentAmount();
        SharedPreferences pref = getContext().getSharedPreferences("Bal", Context.MODE_PRIVATE);
        Integer x = pref.getInt("balance",0);
        SharedPreferences pref2 = getContext().getSharedPreferences("Spent", Context.MODE_PRIVATE);
        Integer y = pref2.getInt("AMT",0);
        Integer z = x - y;
        balance.setText(z.toString());
        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            @TargetApi(16)
            public void onClick(View view) {
                Intent m1 = new Intent(getActivity(),SeeAll.class);
                m1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(m1);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("This action will also clear all your transaction details. Are you sure you want to reset?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resetSpentAmount();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        setToStringfromDatabase();
        setDataToPieChart();
        setupDialogBox();
        setupBudget();
        return view;
    }

    public  void setupBudget(){
        setBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final View mView = getActivity().getLayoutInflater().inflate(R.layout.add_budget,null);
                builder.setTitle("").setView(mView);
                builder.setCancelable(true);
                builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText budget = (EditText)mView.findViewById(R.id.editText12);
                        String x = budget.getText().toString();
                        if(x.length()==0){
                            budget.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.more2, 0);
                        }
                        else{
                            Integer a = Integer.parseInt(x);
                            SharedPreferences sharedPreferences = getContext().getSharedPreferences("Spent", Context.MODE_PRIVATE);
                            Integer y = sharedPreferences.getInt("AMT",0);
                            Integer z = a-y;
                            SharedPreferences pref = getContext().getSharedPreferences("Bal", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putInt("balance",z);
                            editor.apply();
                            balance.setText(z.toString());
                            alertDialog.dismiss();
                        }
                    }
                });

            }
        });
    }
    public void setupDialogBox()
    {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
               final View mView = getActivity().getLayoutInflater().inflate(R.layout.add_expense_dialog_box,null);
                builder.setTitle("Add expense details").setView(mView);
                builder.setCancelable(true);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int count = 0;
                        databaseHandler = new DatabaseHandler(getContext());
                        TextView description = (TextView) mView.findViewById(R.id.editText4);
                        EditText cost = (EditText) mView.findViewById(R.id.editText5);
                        Spinner sp = (Spinner) mView.findViewById(R.id.spinner2);
                        DatePicker dp = (DatePicker) mView.findViewById(R.id.datePicker);

                        Integer day = dp.getDayOfMonth();
                        Integer month = dp.getMonth() + 1;
                        Integer year = dp.getYear();

                        HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
                        hashMap.put(1, "Jan");
                        hashMap.put(2, "Feb");
                        hashMap.put(3, "Mar");
                        hashMap.put(4, "Apr");
                        hashMap.put(5, "May");
                        hashMap.put(6, "Jun");
                        hashMap.put(7, "Jul");
                        hashMap.put(8, "Aug");
                        hashMap.put(9, "Sep");
                        hashMap.put(10, "Oct");
                        hashMap.put(11, "Nov");
                        hashMap.put(12, "Dec");

                        desc = description.getText().toString();
                        if (desc.length() == 0) {
                            description.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.more2, 0);
                        }
                        else {
                            count++;
                        }
                        cat = sp.getSelectedItem().toString();
                        amt = cost.getText().toString();
                        if (amt.length() == 0) {
                            cost.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.more2, 0);
                        }
                        else {
                            amnt = Integer.parseInt(amt);
                            count++;
                        }
                        if (count==2) {
                            TransactionDetails transactionDetails = new TransactionDetails();
                            transactionDetails.AMOUNT = amnt;
                            transactionDetails.CATEGORY = cat;
                            transactionDetails.DESCRIPTION = desc;
                            transactionDetails.DATE = hashMap.get(month) + " " + day.toString();

                            databaseHandler.addEntry(transactionDetails);

                            SharedPreferences sharedPreferences = getContext().getSharedPreferences("Spent", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            Integer x = sharedPreferences.getInt("AMT", 0);
                            x = x + amnt;
                            editor.putInt("AMT", x);
                            editor.apply();

                            HashMap<String, String> hashMap1 = new HashMap<String, String>();
                            hashMap1.put("Food", "Hope you had a hearty meal!");
                            hashMap1.put("Education", "Education is the key to success!");
                            hashMap1.put("Entertainment", "The devil's substitute for joy!");
                            hashMap1.put("Others", "Have fun!");
                            hashMap1.put("Bills", "Try paying the bills with love!");
                            hashMap1.put("Shopping", "It's an \"Add-to-cart\" kinda day!");
                            alertDialog.dismiss();
                            Toast.makeText(getContext(), hashMap1.get(cat), Toast.LENGTH_LONG).show();

                            Intent myIntent = new Intent(getContext(), Home2.class);
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(myIntent);
                        }
                    }
                });
             }
        });
    }

    public void setDataToPieChart()
    {
        DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
        ArrayList<TransactionDetails> arrayList = databaseHandler.getAllEntries();
        String[] areas = {"Food","Bills","Education","Shopping","Entertainment","Others"};
        float f=0,b=0,ed=0,s=0,en=0,o=0;
        for(int i=0;i<arrayList.size();i++){
            if(arrayList.get(i).getCATEGORY().equals("Food")){
                f = f + arrayList.get(i).getAMOUNT();
            }
            else if(arrayList.get(i).getCATEGORY().equals("Bills")){
                b = b + arrayList.get(i).getAMOUNT();
            }
            else if(arrayList.get(i).getCATEGORY().equals("Education")){
                ed = ed + arrayList.get(i).getAMOUNT();
            }
            else if(arrayList.get(i).getCATEGORY().equals("Shopping")){
                s = s + arrayList.get(i).getAMOUNT();
            }
            else if(arrayList.get(i).getCATEGORY().equals("Entertainment")){
                en = en + arrayList.get(i).getAMOUNT();
            }
            else if(arrayList.get(i).getCATEGORY().equals("Others")){
                o = o + arrayList.get(i).getAMOUNT();
            }
        }
        float[] amt = new float[] {f,b,ed,s,en,o};
        ArrayList<PieEntry> list = new ArrayList<>();

        for(int i=0;i<6;i++){
            list.add(new PieEntry(amt[i],areas[i]));
        }

        PieDataSet dataSet = new PieDataSet(list,"");
        dataSet.setColors(new int[]{
                Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
                Color.rgb(106, 150, 31), Color.rgb(179, 100, 53), Color.rgb(99,152,237)
        });
        PieData data = new PieData(dataSet);
        data.setValueTextSize(10);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        pieChart.getLegend().setWordWrapEnabled(true);
        pieChart.setData(data);
        pieChart.setDrawEntryLabels(false);
        pieChart.setCenterText("Expenses");
        pieChart.setCenterTextSize(18);
        pieChart.setTouchEnabled(true);
        pieChart.invalidate();
        pieChart.animateY(3000);
    }

    public void resetSpentAmount()
    {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Spent", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("AMT",0);
        editor.apply();

        SharedPreferences pref = getContext().getSharedPreferences("Bal", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = pref.edit();
        editor2.putInt("balance",0);
        editor2.apply();

        amount.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
        DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
        databaseHandler.deleteAllEntries();
        Intent myIntent = new Intent(getContext(),Home2.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
    }

    public void setSpentAmount()
    {
        DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Spent", Context.MODE_PRIVATE);
        Integer y = sharedPreferences.getInt("AMT",0);
        amount.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
        amount.setText("\u20B9 "+y.toString());
    }

    public void setToStringfromDatabase()
    {
        DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
        ArrayList<TransactionDetails> arrayList = databaseHandler.getThreeEntries();
        ArrayAdapter<TransactionDetails> adapter = new CustomAdapter(getContext(),arrayList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TextView selected = (TextView)view;
        String s = selected.getText().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}


