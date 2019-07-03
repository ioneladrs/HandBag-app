package com.example.inteligeantav4;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    public static int TIME_OUT=500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
                new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent blue=new Intent(StartActivity.this, BluetoothActivity.class);
                startActivity(blue);
                finish();

            }
        },TIME_OUT);
    }
}
