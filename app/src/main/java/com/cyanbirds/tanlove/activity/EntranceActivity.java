package com.cyanbirds.tanlove.activity;

import android.Manifest;
import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cyanbirds.tanlove.CSApplication;
import com.cyanbirds.tanlove.R;
import com.cyanbirds.tanlove.activity.base.BaseActivity;
import com.cyanbirds.tanlove.config.AppConstants;
import com.cyanbirds.tanlove.config.ValueKey;
import com.cyanbirds.tanlove.eventtype.LocationEvent;
import com.cyanbirds.tanlove.manager.AppManager;
import com.cyanbirds.tanlove.net.IUserApi;
import com.cyanbirds.tanlove.net.base.RetrofitFactory;
import com.cyanbirds.tanlove.utils.CheckUtil;
import com.cyanbirds.tanlove.utils.PreferencesUtils;
import com.cyanbirds.tanlove.utils.RxBus;
import com.cyanbirds.tanlove.utils.Utils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * @ClassName:EntranceActivity
 * @Description:登录和注册引导入口
 * @Author:wangyb
 * @Date:2015年5月5日下午5:26:39
 */
public class EntranceActivity extends BaseActivity implements AMapLocationListener {

    @BindView(R.id.login)
    FancyButton mLogin;
    @BindView(R.id.register)
    FancyButton mRegister;

    private final int REQUEST_LOCATION_PERMISSION = 1000;
    private boolean isSecondAccess = false;
    private RxPermissions rxPermissions;

    private AMapLocationClientOption mLocationOption;
    private AMapLocationClient mlocationClient;
    private String mCurrrentCity;//定位到的城市
    private String curLat;
    private String curLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        ButterKnife.bind(this);
        saveFirstLauncher();
        setupViews();
        initLocationClient();
        rxPermissions = new RxPermissions(this);
        requestLocationPermission();
    }

    /**
     * 设置视图
     */
    private void setupViews() {
        mLogin = (FancyButton) findViewById(R.id.login);
        mRegister = (FancyButton) findViewById(R.id.register);
    }

    /**
     * 保存是否第一次启动
     */
    private void saveFirstLauncher() {
        try {
            PreferencesUtils.setIsFirstLauncher(this, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化定位
     */
    private void initLocationClient() {
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取最近3s内精度最高的一次定位结果：
        mLocationOption.setOnceLocationLatest(true);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        //启动定位
        mlocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && !TextUtils.isEmpty(aMapLocation.getCity())) {
            curLat = String.valueOf(aMapLocation.getLatitude());
            curLon = String.valueOf(aMapLocation.getLongitude());
            mCurrrentCity = aMapLocation.getCity();
            PreferencesUtils.setCurrentCity(this, mCurrrentCity);
            PreferencesUtils.setCurrentProvince(EntranceActivity.this, aMapLocation.getProvince());
            RxBus.getInstance().post(AppConstants.CITY_WE_CHAT_RESP_CODE, new LocationEvent(mCurrrentCity));
            uploadCityInfoRequest(aMapLocation.getCity(), String.valueOf(aMapLocation.getLatitude()),
                    String.valueOf(aMapLocation.getLongitude()));
            PreferencesUtils.setLatitude(this, curLat);
            PreferencesUtils.setLongitude(this, curLon);
        }
    }

    private void uploadCityInfoRequest(String city, String lat, String lon) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("channel", CheckUtil.getAppMetaData(CSApplication.getInstance(), "UMENG_CHANNEL"));
        params.put("currentCity", city);
        params.put("latitude", lat);
        params.put("longitude", lon);
        RetrofitFactory.getRetrofit().create(IUserApi.class)
                .uploadCityInfo(params, AppManager.getClientUser().sessionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
                .subscribe(responseBody -> {} , throwable -> {});
    }

    @OnClick({R.id.login, R.id.register})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.login:
                intent.setClass(this, LoginActivity.class);
                if (!TextUtils.isEmpty(AppManager.getClientUser().mobile)) {
                    intent.putExtra(ValueKey.PHONE_NUMBER, AppManager.getClientUser().mobile);
                }
                intent.putExtra(ValueKey.LOCATION, mCurrrentCity);
                intent.putExtra(ValueKey.LATITUDE, curLat);
                intent.putExtra(ValueKey.LONGITUDE, curLon);
                break;
            case R.id.register:
                intent.setClass(this, RegisterActivity.class);
                intent.putExtra(ValueKey.LOCATION, mCurrrentCity);
                intent.putExtra(ValueKey.LATITUDE, curLat);
                intent.putExtra(ValueKey.LONGITUDE, curLon);
                break;
        }
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }

    private void requestLocationPermission() {
        rxPermissions.requestEachCombined(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(permission -> {// will emit 1 Permission object
                    if (permission.granted) {
                        // All permissions are granted !
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // At least one denied permission without ask never again
                    } else {
                        // At least one denied permission with ask never again
                        // Need to go to the settings
                        if (!isSecondAccess) {
                            showAccessLocationDialog();
                        }
                    }
                }, throwable -> {

                });
    }

    private void showAccessLocationDialog() {
        isSecondAccess = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.access_location);
        builder.setPositiveButton(R.string.ok, (dialog, i) -> {
            dialog.dismiss();
            Utils.goToSetting(EntranceActivity.this, REQUEST_LOCATION_PERMISSION);
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            isSecondAccess = false;
            requestLocationPermission();
        }
    }
}