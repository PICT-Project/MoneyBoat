package com.example.tahakothawala.myapplication;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class TransGroup extends AppCompatActivity {

    public HashMap<Long,String> mem_map = new HashMap<>();
    public ArrayList<TransactionObjectSplitter> transactions_list = new ArrayList<>();
    ListView l2;
    int id;
    String group_name ;
    FloatingActionButton floatingActionButton2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_group);

        floatingActionButton2 = (FloatingActionButton)findViewById(R.id.floatingActionButton2);
        l2 = (ListView) findViewById(R.id.l2);

        group_name = getIntent().getStringExtra("group_name");
        id = getIntent().getIntExtra("group_id",0);
        mem_map = (HashMap<Long,String>)getIntent().getSerializableExtra("hashmap");
        transactions_list = (ArrayList<TransactionObjectSplitter>)getIntent().getSerializableExtra("list");

        l2 = (ListView)findViewById(R.id.l2);
        ArrayAdapter<TransactionObjectSplitter> arrayAdapter = new ThirdAdapter(this,transactions_list,mem_map);
        l2.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View view) {
                        FragmentManager manager = getSupportFragmentManager();
                        CustomDialogClass dialog = new CustomDialogClass();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("hashmap",mem_map);
                        bundle.putInt("group_id",id);
                        bundle.putString("group_name",group_name);
                        dialog.setArguments(bundle);
                        dialog.show(manager,"MessageDialog");
                    }
                });
            }
        };
