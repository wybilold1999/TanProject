package com.cyanbirds.tanlove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.cyanbirds.tanlove.R;
import com.cyanbirds.tanlove.activity.base.BaseActivity;
import com.cyanbirds.tanlove.config.AppConstants;
import com.cyanbirds.tanlove.config.ValueKey;
import com.cyanbirds.tanlove.manager.AppManager;
import com.cyanbirds.tanlove.net.request.OSSImagUploadRequest;
import com.cyanbirds.tanlove.utils.PreferencesUtils;
import com.cyanbirds.tanlove.utils.ProgressDialogUtils;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * @author wangyb
 * @Description:赠送vip
 * @Date:2015年7月13日下午2:21:46
 */
public class GiveVipActivity extends BaseActivity {

    public static final int CHOOSE_IMG_RESULT = 0;

    @BindView(R.id.skip_market)
    FancyButton mSkipMarket;
    @BindView(R.id.upload_img)
    FancyButton mUploadImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_vip);
        ButterKnife.bind(this);
        Toolbar toolbar = getActionBarToolbar();
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.mipmap.ic_up);
        }
        setupViews();
        setupEvent();
        setupData();
    }

    /**
     * 设置视图
     */
    private void setupViews() {
    }

    /**
     * 设置事件
     */
    private void setupEvent() {
    }

    /**
     * 设置数据
     */
    private void setupData() {
        if (PreferencesUtils.getIsUploadCommentImg(this)) {
            mUploadImg.setEnabled(false);
            mUploadImg.setFocusBackgroundColor(getResources().getColor(R.color.gray_text));
            mUploadImg.setBackgroundColor(getResources().getColor(R.color.gray_text));
        }
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

    @OnClick({R.id.skip_market, R.id.upload_img})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.skip_market:
                break;
            case R.id.upload_img:
                intent.setClass(this, PhotoChoserActivity.class);
                intent.putExtra(ValueKey.DATA, 1);
                startActivityForResult(intent, CHOOSE_IMG_RESULT);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CHOOSE_IMG_RESULT) {
            String imgUrl = data.getStringExtra(ValueKey.IMAGE_URL);
            if (!TextUtils.isEmpty(imgUrl)) {
                new OSSUploadImgTask().request(AppManager.getFederationToken().bucketName,
                        AppManager.getOSSFacePath(), imgUrl);
            }
        }
    }

    class OSSUploadImgTask extends OSSImagUploadRequest {
        @Override
        public void onPostExecute(String s) {
            PreferencesUtils.setIsUploadCommentImg(GiveVipActivity.this, true);
        }

        @Override
        public void onErrorExecute(String error) {
            ProgressDialogUtils.getInstance(GiveVipActivity.this).dismiss();
        }
    }
}
