package com.example.tahakothawala.myapplication;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterPage extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager ;
    Sensor sensor ;
    TextView username,password,confirm,phone;
    Button register;
    String usr,phn,pwd,cpwd;
    @Override
    @TargetApi(16)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        password = (TextView)findViewById(R.id.editText2);
        confirm = (TextView)findViewById(R.id.editText5);
        username = (TextView) findViewById(R.id.editText);
        phone = (TextView)findViewById(R.id.editText3);
        register = (Button)findViewById(R.id.button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usr = username.getText().toString().trim();
                phn = phone.getText().toString().trim();
                pwd = password.getText().toString().trim();
                cpwd = confirm.getText().toString().trim();
                String specialChars = "(.*[%,*,;,:,/].*$)";
                String lowerCaseChars = "(.*[a-z].*)";
                String upperCaseChars = "(.*[A-Z].*)";

                if (usr.isEmpty() || pwd.isEmpty() || cpwd.isEmpty() || phn.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "All fields are compulsory!", Toast.LENGTH_SHORT).show();
                }
                else if(phn.length()!=10){
                    Toast.makeText(getApplicationContext(), "Enter a 10-digit number!", Toast.LENGTH_SHORT).show();
                }
                else if(pwd.length()<8 || pwd.length()>15){
                    Toast.makeText(getApplicationContext(), "Password length has to be 8-15 characters!", Toast.LENGTH_SHORT).show();
                }
                else{
                    new AsyncRegister().execute();
                }

            }
        });

        coolestFunctionEver();
    }
    class AsyncRegister extends AsyncTask<Void, Void, Void> {
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
                String url="https://ide50-padiarushi.cs50.io/register";
                URL object=new URL(url);

                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/text");
                con.setRequestMethod("POST");

                JSONObject cred   = new JSONObject();

                cred.put("password", pwd);
                cred.put("name",usr);
                cred.put("phone",phn);

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
                Toast.makeText(RegisterPage.this, "Welcome on board",
                        Toast.LENGTH_LONG).show();
                SharedPreferences sharedPreferences = getSharedPreferences("info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("phone", Long.parseLong(phn));
                editor.commit();
                Intent myIntent = new Intent(RegisterPage.this,Home2.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(myIntent);
            }
            else if(response.contains("False"))
            {
                Toast.makeText(RegisterPage.this, "Email-id or Phone no already REGISTERED",
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
                confirm.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            else{
                password.setInputType(129);
                confirm.setInputType(129);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}