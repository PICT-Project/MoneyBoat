package com.example.tahakothawala.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInstaller;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class SeeAll extends AppCompatActivity {

    ListView listView2;
    TextView updatedAsOn;
    TextView deleteOption;
    ImageButton deleteForever;
    ArrayList<TransactionDetails> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);

        deleteForever = (ImageButton) findViewById(R.id.imageButton);
        deleteOption = (TextView) findViewById(R.id.textView14);
        updatedAsOn = (TextView) findViewById(R.id.updated);
        listView2 = (ListView) findViewById(R.id.listViewSeeAll);

        final DatabaseHandler databaseHandler = new DatabaseHandler(this);
        arrayList = new ArrayList<>();
        arrayList = databaseHandler.getAllEntries();

        ArrayAdapter<TransactionDetails> adapter = new CustomAdapter(this, arrayList);
        listView2.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        updatedAsOn.setText("Updated as on " + DateFormat.getDateInstance().format(new Date()));
        deleteOption.setText("Clear All");
        listView2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                TransactionDetails transactionDetails = arrayList.get(i);
                final int sel_id = transactionDetails.getId();
                AlertDialog.Builder builder = new AlertDialog.Builder(SeeAll.this);
                builder.setMessage("Are you sure you want to delete?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //delete from Transactions where id=sel_id ;
                        int sub_amount = databaseHandler.getAmountOfSingleTransaction(sel_id);
                        SharedPreferences sharedPreferences = getSharedPreferences("Spent", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Integer x = sharedPreferences.getInt("AMT",0);
                        x = x-sub_amount ;
                        editor.putInt("AMT",x);
                        editor.apply();
                        deleteSingleFromDatabaseAndListView(sel_id);

                        Intent myIntent = new Intent(SeeAll.this,SeeAll.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(myIntent);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });

        deleteForever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SeeAll.this);
                builder.setMessage("Are you sure you want to reset transaction history?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAllFromDatabaseAndListView();

                        Intent myIntent = new Intent(SeeAll.this,SeeAll.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(myIntent);

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

    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(SeeAll.this,Home2.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
    }

    public void deleteSingleFromDatabaseAndListView(int id)
    {
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        databaseHandler.deleteEntry(id);
    }

    public void deleteAllFromDatabaseAndListView()
    {
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        databaseHandler.deleteAllEntries();
    }
}
