package com.example.geo_news;

//splash screen

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent ite= new Intent(Welcome.this, Login.class);
                startActivity(ite);

                finish();//now this activity will not come again
            }
        },3000);
    }
}