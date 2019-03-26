package com.cyanbirds.tanlove.net.base;

import com.cyanbirds.tanlove.CSApplication;
import com.cyanbirds.tanlove.config.AppConstants;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * wangyb
 */
public class RetrofitManager {

    private static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;//10MB
    private static Retrofit mRetrofit = null;
    private static OkHttpClient okHttpClient = null;
    private static RetrofitManager mInstance;

    private RetrofitManager() {
        initOkHttp();
        initRetrofit();
    }

    public static RetrofitManager getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitManager.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitManager();
                }
            }
        }
        return mInstance;
    }

    private static void initOkHttp() {
        // https://drakeet.me/retrofit-2-0-okhttp-3-0-config
        final File baseDir = CSApplication.getInstance().getApplicationContext().getCacheDir(); //缓存数据的路径
        okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder()
                .retryOnConnectionFailure(true) //设置出现错误进行重新连接
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .cache(new Cache(baseDir, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE))
                .build();
    }

    private static void initRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    /****************** 对外接口 ************************/
    public Retrofit.Builder getRetrofitBuilder() {
        return null;
    }

    public Retrofit getRetrofitInstance() {
        return mRetrofit;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
