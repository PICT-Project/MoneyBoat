package com.example.tahakothawala.myapplication;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static android.content.Context.MODE_PRIVATE;

public class Activity extends Fragment {

    ListView list;
    JSONObject req = null ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_,container,false);
        list = (ListView)view.findViewById(R.id.list);

        new AsyncCaller().execute();

        return view;
    }
    class AsyncCaller extends AsyncTask<Void, Void, Void> {
        public String response = "error";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            req = new JSONObject();
            SharedPreferences sp = getContext().getSharedPreferences("info",MODE_PRIVATE);
            Long p_num = sp.getLong("phone",0);
            try {
                req.put("phone", p_num);
            }catch (JSONException je){
                je.printStackTrace();
                return ;
            }
            //this method will be running on UI thread
        }

        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            Log.e("background process","person Groups retrieving");

            try{
                String url="https://ide50-padiarushi.cs50.io/personal_trans";
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

            ArrayList<LogObject> t = new ArrayList<>();
            ArrayList<FinalObject> finallist = new ArrayList<>();
            JSONArray jsonArray = null;
            Log.e("Log",response);
            try{
                jsonArray = new JSONArray(response);
            }catch (JSONException j){

            }
            JSONObject jsonObject;
            int l=0;
            if(jsonArray !=null)
                l= jsonArray.length();
            for(int i=0;i<l;i++){
                int id=0;
                try {
                    jsonObject = jsonArray.getJSONObject(i);
                    String from_name = jsonObject.getString("from_name");
                    String to_name = jsonObject.getString("to_name");
                    Long from_id = jsonObject.getLong("from_id");
                    Long to_id = jsonObject.getLong("to_id");
                    int amount = jsonObject.getInt("amount");
                    String date  = jsonObject.getString("date");
                    String description = jsonObject.getString("description");
                    t.add(new LogObject(from_name,to_name,date,description,from_id,to_id,amount));
                }catch (JSONException j){
                    j.printStackTrace();
                    return ;
                }
            }
            SharedPreferences sp = getContext().getSharedPreferences("info",MODE_PRIVATE);
            Long p_num = sp.getLong("phone",0);

            for(int i=0,len = t.size(); i<len ;i++){
                //if i want to give

                LogObject o = t.get(i);

                if(o.from_id.equals(p_num)){
                    boolean flag = true ;
                    Long to_id = o.to_id ;
                    for(int j=0; j<finallist.size() ; j++){
                        FinalObject obj = finallist.get(j);
                        if(obj.id.equals(to_id)){
                            obj.amount = obj.amount - o.amount ;
                            finallist.set(j,obj);
                            flag = false ;
                        }
                    }
                    if (flag){
                        finallist.add(new FinalObject(o.to_id,-1*o.amount,o.description,o.date,o.to_name));
                    }
                }

                if(o.to_id.equals(p_num)){
                    boolean flag = true ;
                    Long from_id = o.from_id ;
                    for(int j=0; j<finallist.size() ; j++){

                        FinalObject obj = finallist.get(j);
                        if(obj.id.equals(from_id)){
                            obj.amount = obj.amount + o.amount ;
                            finallist.set(j,obj);
                            flag = false ;
                        }
                    }
                    if (flag) {
                        finallist.add(new FinalObject(o.from_id, o.amount, o.description, o.date, o.from_name));
                    }
                }

            }
            displaylogs(finallist);

        }
    }
    public void  displaylogs(ArrayList<FinalObject> finallist){
        ArrayAdapter<FinalObject> arrayAdapter = new LogAdapter(getContext(), finallist);
        list.setAdapter(arrayAdapter);
    }
}