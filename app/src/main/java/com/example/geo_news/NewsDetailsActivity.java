package com.example.geo_news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import jp.wasabeef.glide.transformations.BlurTransformation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class NewsDetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView appbar_title;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private String dUrl, dSource, dImg;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        appbar_title = findViewById(R.id.title_on_appbar);
        imageView = findViewById(R.id.collapsingToolbarImage);

        Intent intent = getIntent();
        dUrl = intent.getStringExtra("url");
        dImg = intent.getStringExtra("img");
        dSource = intent.getStringExtra("source");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());

        Glide.with(this).load(dImg).apply(requestOptions).transform(new BlurTransformation()).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
        appbar_title.setText(dSource);
        LoadNewsWebview(dUrl);

        bundle = new Bundle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.moreOptions_btn:
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                bundle.putString("dsource", dSource);
                bundle.putString("durl", dUrl);
                bottomSheetFragment.setArguments(bundle);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                return true;

            case R.id.reload_btn:
                LoadNewsWebview(dUrl);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void LoadNewsWebview(String url) {
        WebView webView = findViewById(R.id.detail_web_view);
        webView.getSettings().setJavaScriptEnabled(true);   // special request by Arpit
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }
}
