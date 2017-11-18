package com.example.tahakothawala.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Process;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager ;
    Sensor sensor ;
    TextView email,password;
    Button login,register;
    String em,pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (TextView)findViewById(R.id.editText);
        password = (TextView)findViewById(R.id.editText2);
        login = (Button)findViewById(R.id.button);
        register = (Button)findViewById(R.id.button2);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                em = email.getText().toString();
                pwd = password.getText().toString();

                if(em.isEmpty() || pwd.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter valid username and password!",Toast.LENGTH_SHORT).show();
                }
                else{
                    new AsyncCaller().execute();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Login.this,RegisterPage.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(myIntent);
            }
        });
        coolestFunctionEver();
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
                String url="https://ide50-padiarushi.cs50.io/login";
                URL object=new URL(url);

                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/text");
                con.setRequestMethod("POST");

                JSONObject cred   = new JSONObject();

                cred.put("phone",em);
                cred.put("password", pwd);

                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(cred.toString());
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

            if(response.contains("True"))
            {
                Toast.makeText(Login.this, "Welcome on board",
                        Toast.LENGTH_LONG).show();
                SharedPreferences sharedPreferences = getSharedPreferences("info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("phone", Long.parseLong(em));
                editor.commit();
                Intent myIntent = new Intent(Login.this,Home2.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(myIntent);
            }
            else if(response.contains("None"))
            {
                Toast.makeText(Login.this, "Username doesn't exist",
                        Toast.LENGTH_LONG).show();
            }

            else if(response.contains("False"))
            {
                Toast.makeText(Login.this, "Incorrect Password",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }


    public void coolestFunctionEver(){
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_PROXIMITY){
            if(sensorEvent.values[0]==0){
                password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            else{
                password.setInputType(129);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        Process.killProcess(Process.myPid());
        System.exit(1);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}