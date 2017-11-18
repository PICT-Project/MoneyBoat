package com.example.tahakothawala.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AccountSettings extends AppCompatActivity {

    EditText editText7,editText8,editText10;
    Button save,logout;
    ImageButton imageButton2,imageButton3,imageButton5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        imageButton2 = (ImageButton)findViewById(R.id.imageButton2);
        imageButton3 = (ImageButton)findViewById(R.id.imageButton3);
        imageButton5 = (ImageButton)findViewById(R.id.imageButton5);
        save = (Button)findViewById(R.id.button9);
        logout = (Button)findViewById(R.id.button11);
        editText7 = (EditText)findViewById(R.id.editText7);
        editText8 = (EditText)findViewById(R.id.editText8);
        editText10 = (EditText)findViewById(R.id.editText10);
        editText7.setFocusable(false);
        editText8.setFocusable(false);
        editText10.setFocusable(false);

        SharedPreferences sharedPreferences = this.getSharedPreferences("Info", Context.MODE_PRIVATE);
        String x = sharedPreferences.getString("Username","User");
        String y = sharedPreferences.getString("Phone","User");
        String z = sharedPreferences.getString("Password","User");

        editText7.setText(x);
        editText8.setText(y);
        editText10.setText(z);

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText7.setFocusableInTouchMode(true);
            }
        });
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText8.setFocusableInTouchMode(true);
            }
        });
        imageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText10.setFocusableInTouchMode(true);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Settings updated successfully!",Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(AccountSettings.this,Home2.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountSettings.this);
                builder.setMessage("Are you sure you want to logout?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

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
}
