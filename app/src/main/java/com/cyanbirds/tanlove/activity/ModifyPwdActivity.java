package com.cyanbirds.tanlove.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.cyanbirds.tanlove.R;
import com.cyanbirds.tanlove.activity.base.BaseActivity;
import com.cyanbirds.tanlove.config.AppConstants;
import com.cyanbirds.tanlove.entity.ClientUser;
import com.cyanbirds.tanlove.manager.AppManager;
import com.cyanbirds.tanlove.net.request.ModifyPwdRequest;
import com.cyanbirds.tanlove.utils.AESEncryptorUtil;
import com.cyanbirds.tanlove.utils.ProgressDialogUtils;
import com.cyanbirds.tanlove.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * @author Cloudsoar(wangyb)
 * @datetime 2015-12-26 14:55 GMT+8
 * @email 395044952@qq.com
 */
public class ModifyPwdActivity extends BaseActivity implements View.OnClickListener{
    private FancyButton mSure;
    private EditText mPassword;
    private EditText mPwdAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        Toolbar toolbar = getActionBarToolbar();
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.mipmap.ic_up);
        }
        getSupportActionBar().setTitle(R.string.modify_pwd);

        setupView();
        setupEvent();
    }

    private void setupView(){
        mSure = (FancyButton) findViewById(R.id.btn_sure);
        mPassword = (EditText) findViewById(R.id.new_pwd);
        mPwdAgain = (EditText) findViewById(R.id.new_pwd_again);
    }

    private void setupEvent(){
        mSure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sure :
                if(inputCheck()){
                    String cryptPwd = AESEncryptorUtil.crypt(
                            mPassword.getText().toString().trim(), AppConstants.SECURITY_KEY);
                    ProgressDialogUtils.getInstance(this).show(R.string.dialog_modifying);
                    new ModifyPwdTask().request(cryptPwd);
                }
                break;
        }
    }

    class ModifyPwdTask extends ModifyPwdRequest {
        @Override
        public void onPostExecute(String s) {
            ProgressDialogUtils.getInstance(ModifyPwdActivity.this).dismiss();
            ToastUtil.showMessage(s);
            ClientUser clientUser = AppManager.getClientUser();
            clientUser.userPwd = AESEncryptorUtil.crypt(mPwdAgain.getText().toString().trim(), AppConstants.SECURITY_KEY);
            AppManager.setClientUser(clientUser);
            AppManager.saveUserInfo();
            finish();

        }

        @Override
        public void onErrorExecute(String error) {
            ProgressDialogUtils.getInstance(ModifyPwdActivity.this).dismiss();
            ToastUtil.showMessage(error);
        }
    }


    /**
     * 输入验证
     */
    private boolean inputCheck() {
        String message = "";
        boolean bool = true;
        if (TextUtils.isEmpty(mPwdAgain.getText().toString())) {
            message = getResources().getString(
                    R.string.again_input_password_tips);
            bool = false;
        } if (TextUtils.isEmpty(mPassword.getText().toString())) {
            message = getResources().getString(
                    R.string.input_password);
            bool = false;
        } else if(!mPwdAgain.getText().toString().trim().equals(
                mPassword.getText().toString().trim())){
                message = getResources().getString(
                    R.string.input_password_different);
            bool = false;
        }
        if (!bool)
            ToastUtil.showMessage(message);
        return bool;
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
