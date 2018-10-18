package com.renyu.commonlibrary.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RG on 2015/10/15.
 */
public class Retrofit2Helper {
    private static volatile Retrofit2Helper retrofit2Utils;

    private static Retrofit baseRetrofit;
    private static Retrofit.Builder baseRetrofitBuilder;

    private Retrofit2Helper(String baseUrl) {
        // 基础请求
        baseRetrofitBuilder=new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).baseUrl(baseUrl);
    }

    public void addBaseOKHttpClient(OkHttpClient okHttpClient) {
        baseRetrofitBuilder.client(okHttpClient);
    }

    public void baseBuild() {
        baseRetrofit=baseRetrofitBuilder.build();
    }

    public static Retrofit2Helper getInstance(String baseUrl) {
        if (retrofit2Utils==null) {
            synchronized (Retrofit2Utils.class) {
                if (retrofit2Utils==null) {
                    retrofit2Utils=new Retrofit2Helper(baseUrl);
                }
            }
        }
        return retrofit2Utils;
    }

    public Retrofit getBaseRetrofit() {
        return baseRetrofit;
    }
}
