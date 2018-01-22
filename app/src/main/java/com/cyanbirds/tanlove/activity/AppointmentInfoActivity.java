package com.cyanbirds.tanlove.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.cyanbirds.tanlove.R;
import com.cyanbirds.tanlove.activity.base.BaseActivity;
import com.cyanbirds.tanlove.config.ValueKey;
import com.cyanbirds.tanlove.entity.AppointmentModel;
import com.cyanbirds.tanlove.manager.AppManager;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by wangyb on 2018/1/9.
 */

public class AppointmentInfoActivity extends BaseActivity implements GeocodeSearch.OnGeocodeSearchListener,
        AMap.OnMapScreenShotListener{


    @BindView(R.id.toolbar_actionbar)
    Toolbar mToolbarActionbar;
    @BindView(R.id.portrait)
    SimpleDraweeView mPortrait;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.appointment_theme)
    TextView mAppointmentTheme;
    @BindView(R.id.applay_status)
    TextView mApplayStatus;
    @BindView(R.id.time)
    TextView mTime;
    @BindView(R.id.address)
    TextView mAddress;
    @BindView(R.id.remark)
    TextView mRemark;
    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.accept)
    FancyButton mAccept;
    @BindView(R.id.decline)
    FancyButton mDecline;
    @BindView(R.id.chat)
    FancyButton mChat;

    private AppointmentModel mModel;

    private AMap aMap;
    private UiSettings mUiSettings;
    private GeocodeSearch geocoderSearch;
    private LatLonPoint mLatLonPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_info);
        ButterKnife.bind(this);
        mToolbarActionbar.setNavigationIcon(R.mipmap.ic_up);

        mModel = (AppointmentModel) getIntent().getSerializableExtra(ValueKey.DATA);
        initData();
        initMap();
        getLocation();
    }

    private void initData() {
        mPortrait.setImageURI(Uri.parse(mModel.faceUrl));
        mUserName.setText(mModel.userName);
        mAppointmentTheme.setText(mModel.theme);
        mApplayStatus.setText(AppointmentModel.getStatus(mModel.status));
        mTime.setText(mModel.time);
        mRemark.setText(mModel.remark);
    }

    /**
     * 初始化AMap对象
     */
    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();
            mUiSettings.setZoomControlsEnabled(false);// 不显示缩放按钮
            mUiSettings.setLogoPosition(-50);
            mUiSettings.setZoomGesturesEnabled(false);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(16));// 设置缩放比例
        }


        // 地理编码
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);

    }

    @OnClick({R.id.map, R.id.accept, R.id.decline, R.id.chat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.map:
                break;
            case R.id.accept:
                break;
            case R.id.decline:
                break;
            case R.id.chat:
                break;
        }
    }

    /**
     * 展示用户地图
     */
    private void getLocation() {
        try {
            String myLatitude = AppManager.getClientUser().latitude;
            String myLongitude = AppManager.getClientUser().longitude;
            if (!TextUtils.isEmpty(myLatitude) &&
                    !TextUtils.isEmpty(myLongitude)) {
                LatLonPoint latLonPoint = new LatLonPoint(mModel.latitude, mModel.longitude);
                mLatLonPoint = latLonPoint;
                LatLng latLng = new LatLng(mModel.latitude, mModel.longitude);
                aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
                RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 1000,
                        GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onMapScreenShot(Bitmap bitmap) {

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                mapView.setVisibility(View.VISIBLE);
                PoiItem poiItem = new PoiItem("", mLatLonPoint, "", result
                        .getRegeocodeAddress().getFormatAddress());
                mAddress.setText(poiItem.getSnippet());
            } else {
                mapView.setVisibility(View.GONE);
            }
        } else {
            mapView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
