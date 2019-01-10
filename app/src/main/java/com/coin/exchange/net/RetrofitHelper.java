package com.coin.exchange.net;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private Retrofit mRetrofit;

    /**
     * 通过 域名 获取retrofit
     *
     * @param domain
     */
    public RetrofitHelper(String domain, OkHttpClient okHttpClient) {
        this.mRetrofit = new Retrofit.Builder()
                .baseUrl(domain)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public <T> T create(Class<? extends T> clazz) {
        return this.mRetrofit.create(clazz);
    }

}
