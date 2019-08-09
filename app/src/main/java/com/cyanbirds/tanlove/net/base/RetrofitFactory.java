package com.cyanbirds.tanlove.net.base;

import android.support.annotation.NonNull;

import com.cyanbirds.tanlove.config.AppConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Meiji on 2017/4/22.
 */

public class RetrofitFactory {

    private volatile static Retrofit retrofit;
    private volatile static OkHttpClient okHttpClient;

    @NonNull
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (RetrofitFactory.class) {
                if (retrofit == null) {

                    retrofit = new Retrofit.Builder()
                            .baseUrl(AppConstants.BASE_URL)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

    public static OkHttpClient initOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (RetrofitFactory.class) {
                if (okHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true);
                    okHttpClient = builder.build();
                }
            }
        }
        return okHttpClient;
    }
}
