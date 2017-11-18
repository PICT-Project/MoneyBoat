package com.example.tahakothawala.myapplication;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
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
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;
public class CustomDialogClass extends AppCompatDialogFragment implements TextWatcher {

    ArrayList<MemberDetails> members = new ArrayList<MemberDetails>();
    public HashMap<Long,String> mem_map = null;
     RelativeLayout relativeLayout;

    JSONObject req = new JSONObject();
     TextView tv[];
     TextView tv2[];
     EditText et[],e;
    String desc ="" ;
    int group_id ;
     int total = 0;
     int divided = 0;
    String group_name ;

     @Override
     public Dialog onCreateDialog(Bundle savedInstanceState)  {
         final View v = LayoutInflater.from(getActivity()).inflate(R.layout.custom_dialog_box, null);
         relativeLayout = (RelativeLayout) v.findViewById(R.id.customrelativeLayout);

         RelativeLayout.LayoutParams paramrel = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
         Bundle bundle = this.getArguments();
         group_id = bundle.getInt("group_id");
         group_name = bundle.getString("group_name");
         try {
             req.put("group_id", group_id);
         }catch (JSONException je){
             je.printStackTrace();
         }
         if(bundle.getSerializable("hashmap") != null)
             mem_map = (HashMap<Long,String>)bundle.getSerializable("hashmap");

         SharedPreferences sp = getContext().getSharedPreferences("info", Context.MODE_PRIVATE);
         Long p_num = sp.getLong("phone",9764869960L);
         try {
             req.put("phone",p_num);
         }catch (JSONException je){
             je.printStackTrace();
         }


         for(Long num : mem_map.keySet()){
             if(num.equals(p_num)){
                 continue;
             }
             members.add(new MemberDetails(num,mem_map.get(num)));
         }

         e = v.findViewById(R.id.desc);
         EditText totalamt = (EditText) v.findViewById(R.id.customeditText6);
         tv = new TextView[mem_map.size()-1];
         tv2 = new TextView[mem_map.size()-1];
         et = new EditText[mem_map.size()-1];
         for (int i = 0; i < mem_map.size()-1; i++) {
             LinearLayout linearLayout = new LinearLayout(getContext());
             linearLayout.setOrientation(LinearLayout.HORIZONTAL);
             LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150,
                     LinearLayout.LayoutParams.WRAP_CONTENT);
             LinearLayout.LayoutParams paramname = new LinearLayout.LayoutParams(380,
                     LinearLayout.LayoutParams.WRAP_CONTENT);
             LinearLayout.LayoutParams paramrupee = new LinearLayout.LayoutParams(24,
                     LinearLayout.LayoutParams.WRAP_CONTENT);
             paramname.leftMargin = 10;
             paramname.bottomMargin = 150;
             paramname.topMargin = 20 + i * 175;
             paramrupee.leftMargin = 210;
             paramrupee.bottomMargin = 150;
             paramrupee.topMargin = 20 + i * 175;
             params.leftMargin = 10;
             params.bottomMargin = 150;
             params.topMargin = 20 + i * 175;

             tv[i] = new TextView(getContext());
             tv2[i] = new TextView(getContext());
             et[i] = new EditText(getContext());

             tv[i].setText(members.get(i).name);
             tv2[i].setText("\u20B9");
             tv[i].setTextSize(20);
             et[i].setInputType(InputType.TYPE_CLASS_NUMBER);

             linearLayout.addView(tv[i], paramname);
             linearLayout.addView(tv2[i], paramrupee);
             linearLayout.addView(et[i], params);
             relativeLayout.addView(linearLayout);
         }
         totalamt.addTextChangedListener(this);

         DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {

             }
         };

         return new AlertDialog.Builder(getActivity()).setTitle("Add expense details").setView(v).
                 setPositiveButton("Add", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         EditText totalamt = (EditText) v.findViewById(R.id.customeditText6);
                         String qwerty = totalamt.getText().toString();
                         if(qwerty.length()==0){
                             total=0;
                         }
                         int x[] = new int[members.size()];
                         for (int i1=0;i1<members.size();i1++)
                         {
                             String f = et[i1].getText().toString();
                             int z=0;
                             try{
                              z = Integer.parseInt(f);}
                             catch (NumberFormatException n ){}
                             total -= z;
                             if(total<0){
                                 Toast.makeText(getContext(),"Math Error!",Toast.LENGTH_LONG).show();
                                 return;
                             }
                         }
                         if(total>0){
                             Toast.makeText(getContext(),"Math Error!",Toast.LENGTH_LONG).show();
                             return;
                         }
                         if(total==0){
                             JSONArray transactions = new JSONArray();

                             for (int i1=0;i1<members.size();i1++)
                             {
                                 String f = et[i1].getText().toString();
                                 int z=0;
                                 try{
                                     z = Integer.parseInt(f);}
                                 catch (NumberFormatException n ){return;}
                                 try {
                                     JSONObject t_req = new JSONObject();
                                     t_req.put("id", members.get(i1).phone);
                                     t_req.put("amount",z);
                                     transactions.put(t_req);
                                 }catch (JSONException je){
                                     je.printStackTrace();
                                     return;
                                 }
                             }
                             try {
                                 req.put("transactions", transactions);
                             }catch (JSONException je){
                                 je.printStackTrace();
                                 return;
                             }

                         }
                         try {
                             desc = e.getText().toString();
                         }catch (Exception e){
                             desc = "" ;
                         }
                         try{
                             req.put("desc",desc);
                         }catch (JSONException je){
                             return ;
                         }
                         new AsyncCaller().execute();
                         Intent intt = new Intent(getContext(),Groupie.class);
                         intt.putExtra("group_id",group_id);
                         intt.putExtra("group_name",group_name);
                         intt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                         startActivity(intt);

                     }
                 }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 dialogInterface.dismiss();
             }
         }).create();
     }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        try{
            String ss = editable.toString();
            if(ss.length()==0){
                total = 0;
            }
            else{
            total = Integer.parseInt(ss);}
            divided = total / members.size();
            String d = String.valueOf(divided);
            for (int j = 0; j < members.size(); j++) {
                et[j].setText(d);
            }
        }
         catch (NumberFormatException ne){}
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
                String url="https://ide50-padiarushi.cs50.io/add_trans";
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
            if(response.contains("done")){
            }
        }
    }
}