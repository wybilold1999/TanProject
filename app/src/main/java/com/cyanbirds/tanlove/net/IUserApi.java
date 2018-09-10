package com.cyanbirds.tanlove.net;

import android.support.v4.util.ArrayMap;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IUserApi {

    @FormUrlEncoded
    @POST("user/login")
    Observable<ResponseBody> userLogin(@Header("token") String token, @FieldMap ArrayMap<String, String> params);

    @FormUrlEncoded
    @POST("user/qq_login")
    Observable<ResponseBody> qqLogin(@Header("token") String token, @FieldMap ArrayMap<String, String> params);

    @FormUrlEncoded
    @POST("user/wechat_login")
    Observable<ResponseBody> wxLogin(@Header("token") String token, @FieldMap ArrayMap<String, String> params);
}
