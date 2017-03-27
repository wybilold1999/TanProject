package com.example.user.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.myapplication.net.UserAndDycUploadRequest;
import com.example.user.myapplication.util.CarParseAndAssembleUtils;
import com.example.user.myapplication.util.CarParseJsonUtil;
import com.example.user.myapplication.util.LingAiParseAndAssembleUtils;
import com.example.user.myapplication.util.LingAiParseJsonUtil;
import com.example.user.myapplication.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadUserDycActivity extends Activity {


	@Bind(R.id.parse)
	Button mParse;
	@Bind(R.id.upload)
	Button mUpload;
	@Bind(R.id.car_parse)
	Button mCarParse;
	@Bind(R.id.car_upload)
	Button mCarUpload;
	@Bind(R.id.state)
	TextView mState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_user_dyc);
		ButterKnife.bind(this);
		EventBus.getDefault().register(this);
	}


	@OnClick({R.id.parse, R.id.upload, R.id.car_parse, R.id.car_upload})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.parse:
				LingAiParseJsonUtil.getInstance(this).parse();
				mState.setText("正在解析");
				break;
			case R.id.upload:
				String data = LingAiParseAndAssembleUtils.getInstance(this).getJsonData();
				new UserAndDycUploadTask().request(data, 0);
				break;
			case R.id.car_parse:
				CarParseJsonUtil.getInstance(this).parse();
				mState.setText("car正在解析");
				break;
			case R.id.car_upload:
				String carData = CarParseAndAssembleUtils.getInstance(this).getJsonData();
				new UserAndDycUploadTask().request(carData, 1);
				break;
		}

	}

	class UserAndDycUploadTask extends UserAndDycUploadRequest {
		@Override
		public void onPostExecute(String s) {
			ToastUtil.showMessage("上传成功");
		}

		@Override
		public void onErrorExecute(String error) {
			ToastUtil.showMessage(error);
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void updateState(UpdateEvent event) {
		mState.setText("解析完成");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
