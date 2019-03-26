package com.cyanbirds.tanlove.net.base;

import android.support.annotation.NonNull;

import com.cyanbirds.tanlove.CSApplication;
import com.cyanbirds.tanlove.config.AppConstants;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Meiji on 2017/4/22.
 */

public class RetrofitFactory {

    private volatile static Retrofit retrofit;

    @NonNull
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (RetrofitFactory.class) {
                if (retrofit == null) {
                    // 指定缓存路径,缓存大小 50Mb
                    Cache cache = new Cache(new File(CSApplication.getInstance().getCacheDir(), "HttpCache"),
                            1024 * 1024 * 50);

                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                            .cache(cache)
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true);

                    retrofit = new Retrofit.Builder()
                            .baseUrl(AppConstants.BASE_URL)
                            .client(builder.build())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }
}
