package com.example.taobaounion.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManger {
    private static final RetrofitManger ourInstance = new RetrofitManger();
    private final Retrofit mRetrofit;

    public static RetrofitManger getInstance() {
        return ourInstance;
    }

    private RetrofitManger() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit(){
        return mRetrofit;
    }
}
