package com.cyanbirds.tanlove.presenter;

import android.support.v4.util.ArrayMap;

import com.cyanbirds.tanlove.manager.AppManager;
import com.cyanbirds.tanlove.net.IUserApi;
import com.cyanbirds.tanlove.net.base.RetrofitFactory;
import com.cyanbirds.tanlove.view.IUserLoginLogOut;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SmsCodePresenterImpl implements IUserLoginLogOut.CheckSmsCodePresenter {


    private WeakReference<IUserLoginLogOut.CheckSmsCodeView> mViewWeakReference;

    public SmsCodePresenterImpl(IUserLoginLogOut.CheckSmsCodeView view) {
        mViewWeakReference = new WeakReference<>(view);
    }

    @Override
    public void checkSmsCode(String code, String phoneNum, int mPhoneType) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("phone", phoneNum);
        params.put("zone", "86");
        params.put("code", code);
        params.put("type", String.valueOf(mPhoneType)); //0:注册  1:找回密码 2:绑定手机
        params.put("device", "android");
        RetrofitFactory.getRetrofit().create(IUserApi.class)
                .checkSmsCode(AppManager.getClientUser().sessionId, params)
                .subscribeOn(Schedulers.io())
                .map(responseBody -> {
                    JsonObject obj = new JsonParser().parse(responseBody.string()).getAsJsonObject();
                    return obj.get("status").getAsInt();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(status -> {
                    if (mViewWeakReference.get() != null) {
                        mViewWeakReference.get().checkSmsCode(status);
                    }
                }, throwable -> {
                    if (mViewWeakReference.get() != null) {
                        mViewWeakReference.get().onShowNetError();
                    }
                });
    }

    @Override
    public void detachView() {
        if (mViewWeakReference != null) {
            mViewWeakReference.clear();
            mViewWeakReference = null;
        }
    }
}
