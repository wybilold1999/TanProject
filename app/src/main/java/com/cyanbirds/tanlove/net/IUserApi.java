package com.cyanbirds.tanlove.net;

import android.support.v4.util.ArrayMap;

import com.cyanbirds.tanlove.entity.AllKeys;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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

    /**
     * 获取微信登录和支付id
     * @return
     */
    @GET("user/getIdKeys")
    Observable<AllKeys> getIdKeys();
}
