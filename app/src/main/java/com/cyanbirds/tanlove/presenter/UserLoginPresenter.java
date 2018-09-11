package com.cyanbirds.tanlove.presenter;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.cyanbirds.tanlove.CSApplication;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.cyanbirds.tanlove.utils.JsonUtils.parseJson;

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
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("code", code);
        params.put("device_name", AppManager.getDeviceName());
        params.put("channel", channelId);
        params.put("platform", "weichat");
        params.put("version", String.valueOf(AppManager.getVersionCode()));
        params.put("os_version", AppManager.getDeviceSystemVersion());
        params.put("device_id", AppManager.getDeviceId());
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
                .wxLogin(AppManager.getClientUser().sessionId, params)
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
    public void onQQLogin(String token, String openId, String channelId, String city) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("token", token);
        params.put("openid", openId);
        params.put("channel", channelId);
        params.put("platform", "qq");
        params.put("device_name", AppManager.getDeviceName());
        params.put("version", String.valueOf(AppManager.getVersionCode()));
        params.put("os_version", AppManager.getDeviceSystemVersion());
        params.put("device_id", AppManager.getDeviceId());
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
                .qqLogin(AppManager.getClientUser().sessionId, params)
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

}
