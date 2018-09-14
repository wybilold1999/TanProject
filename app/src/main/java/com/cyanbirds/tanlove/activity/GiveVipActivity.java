package com.cyanbirds.tanlove.activity;

import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cyanbirds.tanlove.CSApplication;
import com.cyanbirds.tanlove.R;
import com.cyanbirds.tanlove.activity.base.BaseActivity;
import com.cyanbirds.tanlove.config.AppConstants;
import com.cyanbirds.tanlove.config.ValueKey;
import com.cyanbirds.tanlove.manager.AppManager;
import com.cyanbirds.tanlove.net.IUserPictureApi;
import com.cyanbirds.tanlove.net.base.RetrofitFactory;
import com.cyanbirds.tanlove.net.request.OSSImagUploadRequest;
import com.cyanbirds.tanlove.utils.CheckUtil;
import com.cyanbirds.tanlove.utils.PreferencesUtils;
import com.cyanbirds.tanlove.utils.ProgressDialogUtils;
import com.cyanbirds.tanlove.utils.ToastUtil;
import com.cyanbirds.tanlove.utils.Utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
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
                Utils.goToMarket(this, CheckUtil.getAppMetaData(this, "UMENG_CHANNEL"));
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
            List<String> imgUrls = data.getStringArrayListExtra(ValueKey.IMAGE_URL);
            if (null != imgUrls && imgUrls.size() > 0) {
                ProgressDialogUtils.getInstance(this).show(R.string.dialog_request_uploda);
                new OSSUploadImgTask().request(AppManager.getFederationToken().bucketName,
                        AppManager.getOSSFacePath(), imgUrls.get(0));
            }
        }
    }

    class OSSUploadImgTask extends OSSImagUploadRequest {
        @Override
        public void onPostExecute(String s) {
            String url = AppConstants.OSS_IMG_ENDPOINT + s;
            getDiscoverInfo(url);
        }

        @Override
        public void onErrorExecute(String error) {
            ProgressDialogUtils.getInstance(GiveVipActivity.this).dismiss();
        }
    }

    private void getDiscoverInfo(String imageUrl) {
        RetrofitFactory.getRetrofit().create(IUserPictureApi.class)
                .uploadCommentImg(AppManager.getClientUser().sessionId, imageUrl)
                .subscribeOn(Schedulers.io())
                .map(responseBody -> {
                    JsonObject obj = new JsonParser().parse(responseBody.string()).getAsJsonObject();
                    int code = obj.get("code").getAsInt();
                    if(code == 0){
                        return CSApplication.getInstance().getResources()
                                .getString(R.string.upload_success);
                    }
                    return CSApplication.getInstance().getResources()
                            .getString(R.string.upload_faiure);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
                .subscribe(s -> {
                    ProgressDialogUtils.getInstance(GiveVipActivity.this).dismiss();
                    ToastUtil.showMessage(s);
                    PreferencesUtils.setIsUploadCommentImg(GiveVipActivity.this, true);
                    mUploadImg.setEnabled(false);
                    mUploadImg.setFocusBackgroundColor(getResources().getColor(R.color.gray_text));
                    mUploadImg.setBackgroundColor(getResources().getColor(R.color.gray_text));
                }, throwable -> {
                    ProgressDialogUtils.getInstance(GiveVipActivity.this).dismiss();
                    ToastUtil.showMessage(R.string.network_requests_error);
                });
    }
}
