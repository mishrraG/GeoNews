package com.example.geo_news;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutInfoActivity extends AppCompatActivity {

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_info);

        getSupportActionBar().setTitle("About App");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView gitlinkPunit = findViewById(R.id.punit_github_tv);
        gitlinkPunit.setMovementMethod(LinkMovementMethod.getInstance());

        TextView gitlinkMonimoy = findViewById(R.id.monimoy_github_tv);
        gitlinkMonimoy.setMovementMethod(LinkMovementMethod.getInstance());

        TextView gitlinkSmish = findViewById(R.id.smish_github_tv);
        gitlinkSmish.setMovementMethod(LinkMovementMethod.getInstance());




    }
}