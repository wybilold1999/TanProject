package com.cyanbirds.tanlove.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cyanbirds.tanlove.R;
import com.cyanbirds.tanlove.activity.base.BaseActivity;
import com.cyanbirds.tanlove.manager.AppManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangyb on 2017/7/21.
 * 描述：客服
 */

public class CustomServiceActivity extends BaseActivity {

    @BindView(R.id.tv_vip)
    TextView mVipTv;

    private int limit = 100;//最多允许输入

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_service);
        ButterKnife.bind(this);
        Toolbar toolbar = getActionBarToolbar();
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.mipmap.ic_up);
        }
        setupView();
    }

    private void setupView() {
        if(AppManager.getClientUser().isShowVip) {
            mVipTv.setVisibility(View.VISIBLE);
        } else {
            mVipTv.setVisibility(View.GONE);
        }
    }
}
