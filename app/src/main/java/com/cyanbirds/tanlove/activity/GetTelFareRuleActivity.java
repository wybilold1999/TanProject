package com.cyanbirds.tanlove.activity;

import android.arch.lifecycle.Lifecycle;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.cyanbirds.tanlove.R;
import com.cyanbirds.tanlove.activity.base.BaseActivity;
import com.cyanbirds.tanlove.manager.AppManager;
import com.cyanbirds.tanlove.net.IUserBuyApi;
import com.cyanbirds.tanlove.net.base.RetrofitFactory;
import com.cyanbirds.tanlove.utils.JsonUtils;
import com.cyanbirds.tanlove.utils.PreferencesUtils;
import com.cyanbirds.tanlove.utils.ToastUtil;
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
 * Created by wangyb on 2018/2/23.
 */

public class GetTelFareRuleActivity extends BaseActivity {

    @BindView(R.id.btn_get_fare)
    FancyButton mBtnGetFare;
    @BindView(R.id.qualification)
    TextView mQualification;
    @BindView(R.id.rule)
    TextView mRule;
    @BindView(R.id.condition)
    TextView mCondition;
    @BindView(R.id.way)
    TextView mWay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_tel_fare_rule);
        ButterKnife.bind(this);
        Toolbar toolbar = getActionBarToolbar();
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.mipmap.ic_up);
        }
        getFareActvityInfo();
    }

    /**
     * 获取返话费活动条件等
     */
    private void getFareActvityInfo() {
        RetrofitFactory.getRetrofit().create(IUserBuyApi.class)
                .getFareActivityInfo(AppManager.getClientUser().sessionId)
                .subscribeOn(Schedulers.io())
                .map(responseBody -> JsonUtils.parseFareActvityInfo(responseBody.string()))
                .observeOn(AndroidSchedulers.mainThread())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
                .subscribe(fareActivityModel -> {
                    if (null != fareActivityModel) {
                        mQualification.setText(fareActivityModel.qualify);
                        mRule.setText(fareActivityModel.getRule);
                        mCondition.setText(fareActivityModel.getCondition);
                        mWay.setText(fareActivityModel.getWay);
                    }
                }, throwable -> ToastUtil.showMessage(R.string.network_requests_error));
    }


    @OnClick(R.id.btn_get_fare)
    public void onViewClicked() {
        if (AppManager.getClientUser().is_vip) {
            if (PreferencesUtils.getWhichVip(this) == 0) {
                showDialog();
            } else {
                if (PreferencesUtils.getIsCanGetFare(this)) {//当月是否可以领取
                    if (PreferencesUtils.getLoginCount(this) > 20) {//登陆次数需超过20天
                        Intent intent = new Intent(GetTelFareRuleActivity.this, GetTelFareActivity.class);
                        startActivity(intent);
                    } else {
                        showNotSatisfyDialog(getString(R.string.login_count_less));
                    }
                } else {
                    showNotSatisfyDialog(getString(R.string.current_month_geted));
                }
            }
        } else {
            showDialog();
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.no_join_activity);
        builder.setPositiveButton(R.string.right_now_join_activity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(GetTelFareRuleActivity.this, VipCenterActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void showNotSatisfyDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
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
}
