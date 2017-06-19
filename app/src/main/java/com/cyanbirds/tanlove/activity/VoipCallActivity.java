package com.cyanbirds.tanlove.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyanbirds.tanlove.R;
import com.cyanbirds.tanlove.activity.base.BaseActivity;
import com.cyanbirds.tanlove.config.ValueKey;
import com.cyanbirds.tanlove.manager.AppManager;
import com.cyanbirds.tanlove.utils.ToastUtil;
import com.cyanbirds.tanlove.utils.VibratorUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangyb on 2017/6/12.
 * 描述：拨打电话
 */

public class VoipCallActivity extends BaseActivity {

    @BindView(R.id.portrait)
    SimpleDraweeView mPortrait;
    @BindView(R.id.nick_name)
    TextView mNickName;
    @BindView(R.id.answer)
    ImageView mAnswer;
    @BindView(R.id.decline)
    ImageView mDecline;
    @BindView(R.id.receive_call_lay)
    RelativeLayout mCallLay;
    @BindView(R.id.decline_call)
    ImageView mDeclineCall;

    private CountDownTimer timer;
    private String from;
    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voip_call);
        ButterKnife.bind(this);
        setupData();
    }

    private void setupData() {
        String faceUrl = getIntent().getStringExtra(ValueKey.IMAGE_URL);
        String nickName = getIntent().getStringExtra(ValueKey.USER_NAME);
        from = getIntent().getStringExtra(ValueKey.FROM_ACTIVITY);
        if (!TextUtils.isEmpty(faceUrl)) {
            mPortrait.setImageURI(Uri.parse(faceUrl));
        }
        if (!TextUtils.isEmpty(nickName)) {
            mNickName.setVisibility(View.VISIBLE);
            mNickName.setText(nickName);
        }
        if (!TextUtils.isEmpty(from)) {//主动拨打电话
            mCallLay.setVisibility(View.GONE);
            mDeclineCall.setVisibility(View.VISIBLE);
            time = 2000;
        } else {
            mCallLay.setVisibility(View.VISIBLE);
            mDeclineCall.setVisibility(View.GONE);
            time = 50000;
        }
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                timer.cancel();
                if (!TextUtils.isEmpty(from)) {
                    VibratorUtil.cancel();
                    if (!AppManager.getClientUser().is_vip) {
                        showTurnOnVipDialog(getResources().getString(R.string.no_vip_calling));
                    } else if (AppManager.getClientUser().gold_num < 100) {
                        showBuyGoldDialog(getResources().getString(R.string.no_gold_num_calling));
                    }
                } else {
                    finish();
                }
            }

        };
        timer.start();
        VibratorUtil.start();
    }

    @OnClick({R.id.answer, R.id.decline, R.id.decline_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.answer:
                if (!AppManager.getClientUser().is_vip) {
                    showTurnOnVipDialog(getResources().getString(R.string.no_vip_receive_calling));
                } else if (AppManager.getClientUser().gold_num < 100) {
                    showBuyGoldDialog(getResources().getString(R.string.no_gold_num_receive_calling));
                }
                break;
            case R.id.decline:
                ToastUtil.showMessage(R.string.decline_call);
                timer.cancel();
                VibratorUtil.cancel();
                finish();
                break;
            case R.id.decline_call:
                timer.cancel();
                VibratorUtil.cancel();
                finish();
                break;
        }
    }

    private void showTurnOnVipDialog(String tips) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(tips);
        builder.setPositiveButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Intent intent = new Intent(VoipCallActivity.this, VipCenterActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        builder.setNegativeButton(getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (!TextUtils.isEmpty(from)) {
                            finish();
                        }
                    }
                });
        if (!TextUtils.isEmpty(from)) {
            builder.setCancelable(false);
        }
        builder.show();
    }

    private void showBuyGoldDialog(String tips) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(tips);
        builder.setPositiveButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Intent intent = new Intent(VoipCallActivity.this, MyGoldActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        builder.setNegativeButton(getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (!TextUtils.isEmpty(from)) {
                            finish();
                        }
                    }
                });
        if (!TextUtils.isEmpty(from)) {
            builder.setCancelable(false);
        }
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VibratorUtil.cancel();
    }
}
