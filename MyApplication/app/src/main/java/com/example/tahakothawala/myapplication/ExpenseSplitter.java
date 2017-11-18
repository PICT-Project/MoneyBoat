package com.example.tahakothawala.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ExpenseSplitter extends Fragment {
    ArrayList<Group_data> group_names_list = new ArrayList<>();
    JSONObject req;
    ListView l;
    View view ;
    GestureDetectorCompat detector;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_groups,container,false);

        l = (ListView)view.findViewById(R.id.listViewSeeAll);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Group_data gd = group_names_list.get(i);
                Intent myIntent = new Intent(getContext(),Groupie.class);
                myIntent.putExtra("group_name",gd.name);
                myIntent.putExtra("group_id",gd.id);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(myIntent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intt = new Intent(getActivity(),AddGroup.class);
                intt.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intt);
            }
        });
        //----------------------------------------------------------new----------------------------------------
        req = new JSONObject();
        try {
            SharedPreferences sp = getContext().getSharedPreferences("info",Context.MODE_PRIVATE);
            Long p_num = sp.getLong("phone",0);
            req.put("phone", p_num);       

        }catch (JSONException j){

        }
        new ExpenseSplitter.AsyncCaller().execute();
        //------------------------------------------------------------------------------------------------------

        return view;
    }

    public void displayList(){
        ListView lv = (ListView)view.findViewById(R.id.listViewSeeAll);

        ArrayAdapter adapter = new GroupCustomAdapter(getContext(),group_names_list);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
            Log.e("background process","person Groups retrieving");
            try{
                String url="https://ide50-padiarushi.cs50.io/person_group";
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
                //
                Log.e("cw","qecqeceweec");
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
                    try {
                        response = "" + sb.toString();
                    }catch (NullPointerException n){
                        response = "error" ;
                    }
                    Log.e("response",response);
                    System.out.println();
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
            JSONArray jsonArray = null;
            //---------------store group info ------------------------------
            //SharedPreferences sp =
            //
            try{
                jsonArray = new JSONArray(response);
            }catch (JSONException j){

            }
            JSONObject jsonObject;
            int l=0;
            if(jsonArray !=null)
                l= jsonArray.length();
            for(int i=0;i<l;i++){
                String name=null;
                int id=0;
                try {
                    jsonObject = jsonArray.getJSONObject(i);
                    name = jsonObject.getString("group_name");
                    id = jsonObject.getInt("group_id");
                }catch (JSONException j){

                }
                Group_data gd = new Group_data(name,id);
                group_names_list.add(gd);
            }
            displayList();
        }
    }
}