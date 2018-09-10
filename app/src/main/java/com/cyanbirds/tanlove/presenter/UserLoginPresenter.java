package com.cyanbirds.tanlove.presenter;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.cyanbirds.tanlove.CSApplication;
import com.cyanbirds.tanlove.R;
import com.cyanbirds.tanlove.activity.LoginActivity_bak;
import com.cyanbirds.tanlove.entity.ClientUser;
import com.cyanbirds.tanlove.helper.IMChattingHelper;
import com.cyanbirds.tanlove.manager.AppManager;
import com.cyanbirds.tanlove.net.IUserApi;
import com.cyanbirds.tanlove.net.base.RetrofitFactory;
import com.cyanbirds.tanlove.utils.AESOperator;
import com.cyanbirds.tanlove.utils.CheckUtil;
import com.cyanbirds.tanlove.utils.PreferencesUtils;
import com.cyanbirds.tanlove.view.IUserLogin;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.umeng.analytics.MobclickAgent;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class UserLoginPresenter implements IUserLogin.Presenter{

    private IUserLogin.View view;

    public UserLoginPresenter(IUserLogin.View view) {
        this.view = view;
    }

    @Override
    public void doShowNetError() {

    }

    @Override
    public void doLoadNoMore() {

    }

    @Override
    public void onUserLogin(String account, String pwd, String city) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("account", account);
        params.put("pwd", pwd);
        params.put("deviceName", AppManager.getDeviceName());
        params.put("appVersion", String.valueOf(AppManager.getVersionCode()));
        params.put("systemVersion", AppManager.getDeviceSystemVersion());
        params.put("deviceId", AppManager.getDeviceId());
        params.put("channel", CheckUtil.getAppMetaData(CSApplication.getInstance(), "UMENG_CHANNEL"));
        if (!TextUtils.isEmpty(city)) {
            params.put("currentCity", city);
        } else {
            params.put("currentCity", "");
        }
        params.put("province", PreferencesUtils.getCurrentProvince(CSApplication.getInstance()));
        params.put("latitude", PreferencesUtils.getLatitude(CSApplication.getInstance()));
        params.put("longitude", PreferencesUtils.getLongitude(CSApplication.getInstance()));
        params.put("loginTime", String.valueOf(PreferencesUtils.getLoginTime(CSApplication.getInstance())));
        RetrofitFactory.getRetrofit().create(IUserApi.class)
                .userLogin(AppManager.getClientUser().sessionId, params)
                .subscribeOn(Schedulers.io())
                .flatMap(responseBody -> {
                    ClientUser clientUser = parseJson(responseBody.string());
                    return Observable.just(clientUser);
                })
                .doOnNext(clientUser -> {
                    MobclickAgent.onProfileSignIn(String.valueOf(clientUser.userId));
                    clientUser.currentCity = city;
                    clientUser.latitude = PreferencesUtils.getLatitude(CSApplication.getInstance());
                    clientUser.longitude = PreferencesUtils.getLongitude(CSApplication.getInstance());
                    AppManager.setClientUser(clientUser);
                    AppManager.saveUserInfo();
                    AppManager.getClientUser().loginTime = System.currentTimeMillis();
                    PreferencesUtils.setLoginTime(CSApplication.getInstance(), System.currentTimeMillis());
                    IMChattingHelper.getInstance().sendInitLoginMsg();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .as(view.bindAutoDispose())
                .subscribe(clientUser -> {
                    if (clientUser == null) {
                        view.onShowNetError();
                    } else {
                        view.loginSuccess(clientUser);
                    }
                }, throwable -> view.onShowNetError());

    }

    @Override
    public void onWXLogin(String code, String channelId, String city) {

    }

    @Override
    public void onQQLogin(String token, String openId, String channelId, String city) {

    }

    private ClientUser parseJson(String json){
        try {
            String decrptData = AESOperator.getInstance().decrypt(json);
            JsonObject obj = new JsonParser().parse(decrptData).getAsJsonObject();
            int code = obj.get("code").getAsInt();
            if (code == 1) {
                return null;
            }
            JsonObject data = obj.get("data").getAsJsonObject();
            ClientUser clientUser = AppManager.getClientUser();
            clientUser.userId = data.get("uid").getAsString();
            clientUser.userPwd = data.get("upwd").getAsString();
            clientUser.sex = data.get("sex").getAsInt() == 1 ? "男" : (data.get("sex").getAsInt() == 0 ? "女" : "all");
            clientUser.mobile = data.get("phone") == null ? "" : data.get("phone").getAsString();
            clientUser.qq_no = data.get("qq").getAsString();
            clientUser.weixin_no = data.get("wechat").getAsString();
            clientUser.face_url = data.get("faceUrl").getAsString();
            clientUser.user_name = data.get("nickname").getAsString();
            clientUser.occupation = data.get("occupation").getAsString();
            clientUser.education = data.get("education").getAsString();
            clientUser.tall = data.get("heigth").getAsString();
            clientUser.weight = data.get("weight").getAsString();
            clientUser.isCheckPhone = data.get("isCheckPhone").getAsBoolean();
            clientUser.is_vip = data.get("isVip").getAsBoolean();
            clientUser.is_download_vip = data.get("isDownloadVip").getAsBoolean();
            JsonObject jsonObject = data.get("showClient").getAsJsonObject();
            clientUser.isShowVip = jsonObject.get("isShowVip").getAsBoolean();
            clientUser.isShowDownloadVip = jsonObject.get("isShowDownloadVip").getAsBoolean();
            clientUser.isShowGold = jsonObject.get("isShowGold").getAsBoolean();
            clientUser.isShowLovers = jsonObject.get("isShowLovers").getAsBoolean();
            clientUser.isShowVideo = jsonObject.get("isShowVideo").getAsBoolean();
            clientUser.isShowMap = jsonObject.get("isShowMap").getAsBoolean();
            clientUser.isShowRpt = jsonObject.get("isShowRpt").getAsBoolean();
            clientUser.isShowTd = jsonObject.get("isShowTd").getAsBoolean();
            clientUser.isShowAppointment = jsonObject.get("isShowAppointment").getAsBoolean();
            clientUser.isShowGiveVip = jsonObject.get("isShowGiveVip").getAsBoolean();
            clientUser.gold_num = data.get("goldNum").getAsInt();
            clientUser.state_marry = data.get("emotionStatus").getAsString();
            clientUser.city = data.get("city").getAsString();
            clientUser.age = data.get("age").getAsInt();
            clientUser.signature = data.get("signature").getAsString();
            clientUser.constellation = data.get("constellation").getAsString();
            clientUser.distance = data.get("distance").getAsString();
            clientUser.intrest_tag = data.get("intrestTag").getAsString();
            clientUser.personality_tag = data.get("personalityTag").getAsString();
            clientUser.part_tag = data.get("partTag").getAsString();
            clientUser.purpose = data.get("purpose").getAsString();
            clientUser.love_where = data.get("loveWhere").getAsString();
            clientUser.do_what_first = data.get("doWhatFirst").getAsString();
            clientUser.conception = data.get("conception").getAsString();
            clientUser.sessionId = data.get("sessionId").getAsString();
            clientUser.imgUrls = data.get("pictures") == null ? "" : data.get("pictures").getAsString();
            clientUser.gifts = data.get("gifts").getAsString();
            clientUser.versionCode = data.get("versionCode").getAsInt();
            clientUser.apkUrl = data.get("apkUrl").getAsString();
            clientUser.versionUpdateInfo = data.get("versionUpdateInfo").getAsString();
            clientUser.isForceUpdate = data.get("isForceUpdate").getAsBoolean();
            return clientUser;
        } catch (Exception e) {
        }
        return null;
    }
}
