package com.example.geo_news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MotionEventCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.geo_news.api.ApiClient;
import com.example.geo_news.api.ApiInterface;
import com.example.geo_news.models.Article;
import com.example.geo_news.models.News;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String API_KEY = "NEWS-API-KEY-HERE";
    private RecyclerView recyclerView;
    private List<Article> articles = new ArrayList<>();
    private NewsAdapter adapter;
    private String TAG = MainActivity.class.getSimpleName();

    private SwipeRefreshLayout swipeRefreshLayout;
    private String selected_country, selected_country_name;
    private NestedScrollView nestedScrollView;
    private FloatingActionButton fab;


    // error layout
    private ConstraintLayout errorConstraintLayout;
    private ImageView errorImage;
    private TextView errorMessage, errorTitle;
    private Button retryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Intent intent = getIntent();
        selected_country = intent.getStringExtra("countrycode");
        selected_country_name = intent.getStringExtra("countryname");

        getSupportActionBar().setTitle(selected_country_name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        nestedScrollView = findViewById(R.id.nested_scroll_view);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(NewsActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        //countryCodePicker = findViewById(R.id.ccp);

        //        error handling
        errorConstraintLayout = findViewById(R.id.error_layout);
        errorImage = findViewById(R.id.errorImageView);
        errorTitle = findViewById(R.id.error_title_tv);
        errorMessage = findViewById(R.id.error_message_tv);
        retryBtn = findViewById(R.id.retry_btn);

        //selected_country = countryCodePicker.getSelectedCountryNameCode().toLowerCase();
        LoadJson(selected_country, "");
//        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
//            @Override
//            public void onCountrySelected() {
//                selected_country = countryCodePicker.getSelectedCountryNameCode().toLowerCase();
//                LoadJson(selected_country, "");
//            }
//        });

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.red), getResources().getColor(R.color.blue), getResources().getColor(R.color.yellow));
        swipeRefreshLayout.setOnRefreshListener(this);


        fab = findViewById(R.id.floatingActionButton);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nestedScrollView.smoothScrollTo(0, 0);
            }
        });
    }

    public void LoadJson(String country, String phrase) {
        errorConstraintLayout.setVisibility(View.GONE);
        nestedScrollView.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
//        String country = Utils.getCountry();
//        String country = "in";
//        Toast.makeText(this, "Country: " + country, Toast.LENGTH_SHORT).show();

        Call<News> call;
        if (phrase.length() > 0) {
            call = apiInterface.getSearchedNews(phrase, "publishedAt", API_KEY);
            call = apiInterface.getSearchedNews(phrase, "publishedAt", API_KEY);
        } else {
            call = apiInterface.getNews(country, API_KEY);
        }

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticle() != null) {
                    if (!articles.isEmpty()) {
                        articles.clear();
                    }

                    articles = response.body().getArticle();
                    adapter = new NewsAdapter(articles, NewsActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    initListener();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
//                    Toast.makeText(MainActivity.this, "No Result!", Toast.LENGTH_SHORT).show();
                    String errorCode;
                    switch (response.code()) {
                        case 404:
                            errorCode = "404 not found";
                            break;
                        case 500:
                            errorCode = "500 server broken";
                            break;
                        default:
                            errorCode = "Unknown error occured";
                            break;
                    }
                    showErrorMessage(R.drawable.no_result, "No Result", "Try Again\n" + errorCode);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                showErrorMessage(R.drawable.no_result, "No Internet", "Please check your connection\n" + t.toString());
            }
        });
    }

    private void initListener() {
        adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(NewsActivity.this, NewsDetailsActivity.class);
                Article article = articles.get(position);

                intent.putExtra("url", article.getUrl());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("img", article.getUrlToImage());
                intent.putExtra("source", article.getSource().getName());
                startActivity(intent);

            }
        });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(NewsActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                LoadJson(selected_country, "");
            }
        }, 3000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_activity, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_btn).getActionView();
        MenuItem searchItem = menu.findItem(R.id.search_btn);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search News: ");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() > 3) {
                    LoadJson(selected_country, s);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                LoadJson(selected_country, s);
                return false;
            }
        });
        searchItem.getIcon().setVisible(false, false);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showErrorMessage(int errorImageView, String title, String message) {
        nestedScrollView.setVisibility(View.GONE);
        if (errorConstraintLayout.getVisibility() == View.GONE) {
            errorConstraintLayout.setVisibility(View.VISIBLE);
        }
        errorImage.setImageResource(errorImageView);
        errorTitle.setText(title);
        errorMessage.setText(message);

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRefresh();
            }
        });
    }
}