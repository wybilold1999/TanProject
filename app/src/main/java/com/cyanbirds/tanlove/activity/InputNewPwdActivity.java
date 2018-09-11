package com.cyanbirds.tanlove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.cyanbirds.tanlove.R;
import com.cyanbirds.tanlove.activity.base.BaseActivity;
import com.cyanbirds.tanlove.config.AppConstants;
import com.cyanbirds.tanlove.config.ValueKey;
import com.cyanbirds.tanlove.entity.ClientUser;
import com.cyanbirds.tanlove.manager.AppManager;
import com.cyanbirds.tanlove.net.IUserApi;
import com.cyanbirds.tanlove.net.base.RetrofitFactory;
import com.cyanbirds.tanlove.presenter.UserLoginPresenterImpl;
import com.cyanbirds.tanlove.utils.AESEncryptorUtil;
import com.cyanbirds.tanlove.utils.ProgressDialogUtils;
import com.cyanbirds.tanlove.utils.ToastUtil;
import com.cyanbirds.tanlove.view.IUserLoginLogOut;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.umeng.analytics.MobclickAgent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * 
 * @Description:输入新的密码
 * @Author:wangyb
 * @Date:2015年5月12日上午11:43:42
 *
 */
public class InputNewPwdActivity extends BaseActivity<IUserLoginLogOut.Presenter> implements
		OnClickListener,IUserLoginLogOut.View {

	private EditText mPassword;
	private EditText mConfirmPassword;
	private FancyButton mLogin;

	private String mPhone;
	private String mSmsCode;
	private String mCurrrentCity;//定位到的城市
	private JsonObject obj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_newpwd);
		Toolbar toolbar = getActionBarToolbar();
		if (toolbar != null) {
			toolbar.setNavigationIcon(R.mipmap.ic_up);
		}
		setupViews();
		setupData();
		setupEvent();
	}

	/**
	 * 设置视图
	 */
	private void setupViews() {
		mPassword = (EditText) findViewById(R.id.password);
		mConfirmPassword = (EditText) findViewById(R.id.confirm_password);
		mLogin = (FancyButton) findViewById(R.id.login);
	}

	/**
	 * 设置事件
	 */
	private void setupEvent() {
		mLogin.setOnClickListener(this);
	}

	/**
	 * 设置数据
	 */
	private void setupData() {
		mPhone = getIntent().getStringExtra(ValueKey.PHONE_NUMBER);
		mSmsCode = getIntent().getStringExtra(ValueKey.SMS_CODE);
		mCurrrentCity = getIntent().getStringExtra(ValueKey.LOCATION);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.login:
				if (checkInput()) {
					ProgressDialogUtils.getInstance(this).show(R.string.dialog_modifying);
					inputNewPwd();
				}
				break;
		}
	}

	@Override
	public void setPresenter(IUserLoginLogOut.Presenter presenter) {
		if (presenter == null) {
			this.presenter = new UserLoginPresenterImpl(this);
		}
	}

	@Override
	public void loginLogOutSuccess(ClientUser clientUser) {
		ProgressDialogUtils.getInstance(InputNewPwdActivity.this).dismiss();
		hideSoftKeyboard();
		Intent intent = new Intent();
		intent.setClass(InputNewPwdActivity.this, MainActivity.class);
		startActivity(intent);
		finishAll();
	}

	private void inputNewPwd() {
		String cryptPwd = AESEncryptorUtil.crypt(
				mConfirmPassword.getText().toString().trim(), AppConstants.SECURITY_KEY);
		AppManager.getClientUser().userPwd = cryptPwd;
		ArrayMap<String, String> params = new ArrayMap<>();
		params.put("newPassword", cryptPwd);
		params.put("phone", mPhone);
		params.put("smsCode", mSmsCode);
		RetrofitFactory.getRetrofit().create(IUserApi.class)
				.findPwd(AppManager.getClientUser().sessionId, params)
				.subscribeOn(Schedulers.io())
				.map(responseBody -> {
					obj = new JsonParser().parse(responseBody.string()).getAsJsonObject();
					int code = obj.get("code").getAsInt();
					return code;
				})
				.observeOn(AndroidSchedulers.mainThread())
				.as(this.bindAutoDispose())
				.subscribe(integer -> {
					if (integer == 0) {
						ProgressDialogUtils.getInstance(InputNewPwdActivity.this).dismiss();
						ProgressDialogUtils.getInstance(InputNewPwdActivity.this).show(R.string.dialog_request_login);
						presenter.onUserLogin(mPhone, AppManager.getClientUser().userPwd, mCurrrentCity);
					} else {
						ProgressDialogUtils.getInstance(InputNewPwdActivity.this).dismiss();
						ToastUtil.showMessage(obj.get("msg").getAsString());
					}
				}, throwable -> onShowNetError());
	}

	/**
	 * 验证输入
	 */
	private boolean checkInput() {
		String message = "";
		boolean bool = true;
		if (TextUtils.isEmpty(mPassword.getText().toString())) {
			message = getResources().getString(R.string.input_new_password);
			bool = false;
		} else if (!mPassword.getText().toString()
				.equals(mConfirmPassword.getText().toString())) {
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
