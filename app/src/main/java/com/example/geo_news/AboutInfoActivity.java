package com.example.geo_news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AboutInfoActivity extends AppCompatActivity {

    private RecyclerView memberRecyclerView;
    private AboutInfoAdapter aboutInfoAdapter;
    List<AboutInfoModel> memberList;

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

        memberRecyclerView = findViewById(R.id.memberRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        memberRecyclerView.setLayoutManager(layoutManager);

        aboutInfoAdapter = new AboutInfoAdapter(initMember());
        memberRecyclerView.setAdapter(aboutInfoAdapter);
        aboutInfoAdapter.notifyDataSetChanged();

//        TextView gitlinkPunit = findViewById(R.id.punit_github_tv);
//        gitlinkPunit.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private List<AboutInfoModel> initMember() {
        memberList = new ArrayList<>();
        memberList.add(new AboutInfoModel(R.drawable.smish_icon,"Satyajit Mishra", R.string.smishgitlink,"Developer"));
        memberList.add(new AboutInfoModel(R.drawable.monimoy_icon,"Monimoy Paul", R.string.monimoygitlink,"Developer"));
        memberList.add(new AboutInfoModel(R.drawable.punit_icon,"Punit Mishra", R.string.punitgitlink,"Developer"));
        memberList.add(new AboutInfoModel(R.drawable.anshuman_icon,"Anshuman Pati", R.string.anshumangitlink,"Content Writer"));
        return memberList;
    }
}