package com.iamplus.boxledtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String url = null;
    boolean success = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.process).setOnClickListener(this);
        findViewById(R.id.sleep).setOnClickListener(this);
        findViewById(R.id.listening).setOnClickListener(this);
        findViewById(R.id.speak).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        url = "http://10.100.188.240:9090/set_led?state=";
        switch (view.getId()) {
            case R.id.sleep:
                url = url+"idle";
                break;
            case R.id.process:
                url = url+"processing";
                break;
            case R.id.listening:
                url = url+"listening";
                break;
            case R.id.speak:
                url = url+"speaking";
                break;

        }
        new Thread() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                success = false;
                try {
                    urlConnection =(HttpURLConnection) new URL(url).openConnection();
                    urlConnection.setRequestMethod("PUT");
                    urlConnection.setConnectTimeout(1000);
                    urlConnection.setReadTimeout(1000);
                    urlConnection.connect();
                    if(urlConnection.getResponseCode() == 200) {
                        success = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally{
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), success ? "Success!!": "Failed!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }.start();
    }
}
