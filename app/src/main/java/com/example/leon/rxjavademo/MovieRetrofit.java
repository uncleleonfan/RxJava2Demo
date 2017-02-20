package com.example.leon.rxjavademo;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Leon on 2017/2/20.
 */

public class MovieRetrofit {

    private static MovieRetrofit sMovieRetrofit;
    private final Api mApi;

    public static MovieRetrofit getInstance() {
        if (sMovieRetrofit == null) {
            synchronized (MovieRetrofit.class) {
                if (sMovieRetrofit == null) {
                    sMovieRetrofit = new MovieRetrofit();
                }
            }
        }
        return sMovieRetrofit;
    }

    private MovieRetrofit() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.douban.com/v2/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mApi = retrofit.create(Api.class);
    }

    public Api getApi() {
        return mApi;
    }
}
