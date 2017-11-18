package com.example.tahakothawala.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Groupie extends AppCompatActivity  {

    JSONObject req ;
    ListView l5;
    Button b5;
    int id ;
    String name ;
    HashMap<Long,String> mem_map = new HashMap<>();
    ArrayList<PersonalTransactions> personalTransactions_list = new ArrayList<>();
    ArrayList<TransactionObjectSplitter> transactions = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupie);
        id = getIntent().getIntExtra("group_id",0);
        name = getIntent().getStringExtra("group_name");
        String group_name = name;
        TextView tv = (TextView) findViewById(R.id.textView3);
        tv.setText(group_name);
        new Groupie.AsyncCaller().execute();

    }

    class AsyncCaller extends AsyncTask<Void, Void, Void> {
        public String response = "error";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            req = new JSONObject();
            try {
                req.put("group_id", id);
            }catch (JSONException j){
                j.printStackTrace();
            }
            //this method will be running on UI thread
        }

        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            try{
                String url="https://ide50-padiarushi.cs50.io/group_trans";
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
            JSONObject jsonObject=null ;
            JSONArray jsonArray = null ;
            JSONArray jsonArray1 = null ;
            try {
                jsonObject = new JSONObject(response);
                jsonArray = jsonObject.getJSONArray("members");
                jsonArray1 = jsonObject.getJSONArray("transactions");

            }catch (JSONException j){
                j.printStackTrace();
                return ;
            }catch (NullPointerException np){
                np.printStackTrace();
                return ;
            }
            if(jsonArray==null || jsonArray1==null){
                Log.d("null json array","members or transactions null");
                return ;
            }
            for(int i=0,l=jsonArray.length();i<l;i++){
                //extract all members
                try {
                    Long phone = jsonArray.getJSONObject(i).getLong("phone");
                    String name = jsonArray.getJSONObject(i).getString("name");
                    mem_map.put(phone,name);
                }catch (JSONException je){
                    je.printStackTrace();
                    return ;
                }
            }
            for(int i=0,l=jsonArray1.length();i<l;i++) {
                //extract all transactions
                try {
                    jsonObject = jsonArray1.getJSONObject(i);
                    Long from_id = jsonObject.getLong("from_id");
                    Long to_id = jsonObject.getLong("to_id");
                    int amount = jsonObject.getInt("amount");
                    String description = jsonObject.getString("description");
                    String date = jsonObject.getString("date");
                    TransactionObjectSplitter o = new TransactionObjectSplitter(from_id,to_id,amount,description,date);
                    transactions.add(o);
                }catch (JSONException je){
                    je.printStackTrace();
                    return ;
                }
            }
            //check hashmap and array list
            for(Long number : mem_map.keySet()){
                Log.e("hashmap",number.toString()+" "+mem_map.get(number));
            }
            for(TransactionObjectSplitter o : transactions){
                Integer i= o.amount ;
                Log.e("transactions",o.from_id.toString()+" "+o.to_id.toString()+"  "+i+"   "+o.date);
            }

            SharedPreferences sp = getSharedPreferences("info",MODE_PRIVATE);
            Long p_num = sp.getLong("phone",0);
            for(Long number : mem_map.keySet()){

                if(!number.equals(p_num)){
                    personalTransactions_list.add(new PersonalTransactions(number,0));
                }

            }
            for(TransactionObjectSplitter o : transactions){
                if(o.from_id.equals(p_num)){
                    for(int i=0,l=personalTransactions_list.size() ; i<l ; i++){
                        PersonalTransactions obj = personalTransactions_list.get(i);
                        if(obj.num.equals(o.to_id)){
                            obj.amount = obj.amount - o.amount ;
                            personalTransactions_list.set(i,obj);
                            break;
                        }
                    }

                }
                else if(o.to_id.equals(p_num)){
                    for(int i=0,l=personalTransactions_list.size() ; i<l ; i++){
                        PersonalTransactions obj = personalTransactions_list.get(i);
                        if(obj.num.equals(o.from_id)){
                            obj.amount = obj.amount + o.amount ;
                            personalTransactions_list.set(i,obj);
                            break;
                        }
                    }

                }
            }

            ArrayAdapter<PersonalTransactions> arrayAdapter = new AdapterForGroupie(Groupie.this, personalTransactions_list,mem_map);
            l5 = (ListView) findViewById(R.id.listView5);
            l5.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();

            b5 = (Button)findViewById(R.id.button3);
            b5.setText("Details  \u279F");
            b5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(Groupie.this,TransGroup.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    myIntent.putExtra("hashmap",mem_map);
                    myIntent.putExtra("list",transactions);
                    myIntent.putExtra("group_id",id);
                    myIntent.putExtra("group_name",name);
                    startActivity(myIntent);
                }
            });

        }
    }
}
