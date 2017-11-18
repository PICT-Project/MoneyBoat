package com.example.tahakothawala.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class AddGroup extends AppCompatActivity {
    private static final int RESULT_PICK_CONTACT = 65534;
    View[] v;
    String number=null;
    EditText glb =null;
    //-----------------------
    ArrayList<Long> list = new ArrayList<Long>() ;
    int size_ =1;
    JSONArray mem ;
    JSONObject req ;
    //------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_group);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                setview(position+1);
                size_ = position+1 ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                setview(1);
            }


        });
        mem = new JSONArray();
        req = new JSONObject();
        Button b = (Button)findViewById(R.id.button) ;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText e = (EditText)findViewById(R.id.editText3);
                String group_name = e.getText().toString();
                if(group_name.equals("")){
                    Toast.makeText(AddGroup.this,"Group name can not be left blank",Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences sp = getSharedPreferences("info",MODE_PRIVATE);
                Long p_num = sp.getLong("phone",0);
                for(int i=0;i<size_;i++){
                    EditText editText =(EditText)v[i].findViewById(R.id.editText2);
                    String text = editText.getText().toString();
                    text=text.replaceAll(" ","");
                    if(text.contains("(")){
                        text =text.substring(text.indexOf("("),text.indexOf(")"));
                    }
                    if(text.length()<10){
                        Toast.makeText(AddGroup.this,"Number not properly formatted",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String number = text.substring(text.length()-10,text.length());
                    Long num;
                    try{
                        num = Long.parseLong(number);
                    }catch (Exception ex){
                        Toast.makeText(AddGroup.this,"Number not properly formatted",Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    if(list.contains(num)){
                        Toast.makeText(AddGroup.this,"Duplicate entries!",Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    if(p_num==num){
                        Toast.makeText(AddGroup.this,"Duplicate entries!",Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    list.add(num);
                    Log.d("phone",num.toString());
                    try {
                        JSONObject j = new JSONObject();
                        j.put("phone", num);
                        mem.put(j);
                    }catch (JSONException j){

                    }

                }
                Toast.makeText(AddGroup.this,"Successful !",Toast.LENGTH_SHORT).show();

                try {
                    req.put("phone",p_num);
                    req.put("group_name",group_name);
                    req.put("members",mem);
                }catch (JSONException j){

                }
                Log.e("e",req.toString());
                new AsyncCaller().execute();
            }
        });

    }
    public void setlistener(final View v){
        ImageButton i=(ImageButton) v.findViewById(R.id.imageButton);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText e = (EditText)v.findViewById(R.id.editText2);
                setglb(e);
                pickContact();
            }
        });
    }
    public void setglb(EditText e){
        glb = e;
    }

    public  void setview(int size){
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        rl.removeAllViews();
        LayoutInflater layoutInflater = (LayoutInflater)
                this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        ScrollView sv = new ScrollView(this);
        sv.addView(ll,1000,1000);
        v = new View[size];
        for(int i=0;i<size;i++) {
            v[i] = layoutInflater.inflate(R.layout.add_member_element,ll,false);
            v[i].setId(i);
            setlistener(v[i]);
            ll.addView(v[i]);
        }
        rl.addView(sv);
    }

    public void pickContact()
    {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }
    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     * @param data
     */
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null ;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            number=name+" ("+phoneNo+")";
            glb.setText(number);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class AsyncCaller extends AsyncTask<Void, Void, Void> {
        public String response = "error";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
        }

        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            try{
                String url="https://ide50-padiarushi.cs50.io/group";
                URL object=new URL(url);

                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/text");
                con.setRequestMethod("POST");



                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(req.toString());
                wr.flush();

                //display what returns the POST request

                StringBuilder sb = new StringBuilder();
                int HttpResult = con.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    response = "" + sb.toString();
                } else{
                    System.out.println(con.getResponseMessage());
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //this method will be running on UI thread

            if(response.contains("True"))
            {
                Toast.makeText(AddGroup.this, "Done",
                        Toast.LENGTH_LONG).show();
            }
            Intent intt = new Intent(AddGroup.this,Home2.class);
            intt.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intt);
        }
    }


}
