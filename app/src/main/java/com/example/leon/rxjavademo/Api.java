package com.example.leon.rxjavademo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Leon on 2017/2/20.
 */

public interface Api {


    @GET("top250")
    Observable<MovieBean> listTop250(@Query("start") int start, @Query("count") int count);
}
