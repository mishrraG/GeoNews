package com.example.geo_news.api;

import com.example.geo_news.models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("top-headlines")
    Call<News> getNews(
            @Query("country") String country,
            @Query("apiKey") String apiKey
    );

    @GET("everything")
    Call<News> getSearchedNews(
            @Query("qInTitle") String phrase,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey
    );


}
