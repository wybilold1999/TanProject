package com.cyanbirds.tanlove.net;

import android.support.v4.util.ArrayMap;

import com.cyanbirds.tanlove.config.AppConstants;
import com.cyanbirds.tanlove.entity.AllKeys;
import com.cyanbirds.tanlove.entity.CityInfo;

import io.reactivex.Observable;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
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

    @FormUrlEncoded
    @POST("user/register")
    Observable<ResponseBody> userRegister(@FieldMap ArrayMap<String, String> params);

    @FormUrlEncoded
    @POST("user/logoutLogin")
    Observable<ResponseBody> userLogout(@Header("token") String token, @FieldMap ArrayMap<String, String> params);

    /**
     * 获取微信登录和支付id
     * @return
     */
    @GET("user/getIdKeys")
    Observable<ResponseBody> getIdKeys();

    @FormUrlEncoded
    @POST("user/checkIsRegister")
    Observable<ResponseBody> checkIsRegister(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("captch/smsCode")
    Observable<ResponseBody> checkSmsCode(@Header("token") String token, @FieldMap ArrayMap<String, String> params);

    @FormUrlEncoded
    @POST("user/newPasswordSetting")
    Observable<ResponseBody> findPwd(@Header("token") String token, @FieldMap ArrayMap<String, String> params);

    @FormUrlEncoded
    @POST("user/updatePassword")
    Observable<ResponseBody> modifyPwd(@Header("token") String token, @Field("newPassword") String newPassword);

    @FormUrlEncoded
    @POST("user/updatePerson")
    Observable<ResponseBody> updateUserInfo(@Header("token") String token, @FieldMap ArrayMap<String, String> params);

    @GET("oss/distribute-token")
    Observable<ResponseBody> getOSSToken();

    @FormUrlEncoded
    @POST("user/userInfo")
    Observable<ResponseBody> getUserInfo(@Header("token") String token, @Field("uid") String uid);

    /**
     * 缘分卡片
     * @param token
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user/yuanFenUser")
    Observable<ResponseBody> getYuanFenUser(@Header("token") String token, @FieldMap ArrayMap<String, Integer> params);

}
