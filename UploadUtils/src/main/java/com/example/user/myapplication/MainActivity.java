package com.example.user.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.example.user.myapplication.oss.AppManager;
import com.example.user.myapplication.oss.FederationToken;
import com.example.user.myapplication.oss.GetOSSTokenRequest;
import com.example.user.myapplication.util.GiftUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：wangyb
 * 时间：2016/9/2 11:41
 * 描述：
 */
public class MainActivity extends Activity {
	@Bind(R.id.uploadUser)
	Button mUploadUser;
	@Bind(R.id.uploadVideo)
	Button mUploadVideo;
	@Bind(R.id.playVideo)
	Button mPlayVideo;
	@Bind(R.id.video_img)
	ImageView mVideoImg;
	@Bind(R.id.downloaduploadVideo)
	Button mDownloaduploadVideo;
	@Bind(R.id.upload_gift)
	Button mUploadGift;

	private ClientConfiguration mOSSConf;
	/**
	 * oss鉴权获取失败重试次数
	 */
	public int mOSSTokenRetryCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		initOSS();
	}

	@OnClick({R.id.uploadUser, R.id.uploadVideo,
			R.id.playVideo, R.id.downloaduploadVideo,
			R.id.upload_gift})
	public void onClick(View view) {
		Intent intent = new Intent();
		switch (view.getId()) {
			case R.id.uploadUser:
				intent.setClass(this, UploadUserDycActivity.class);
				startActivity(intent);
				break;
			case R.id.uploadVideo:
				intent.setClass(this, UploadVideoActivity.class);
				startActivity(intent);
				break;
			case R.id.playVideo:
				intent.setClass(this, UploadVideoLoveActivity.class);
				startActivity(intent);
				break;
			case R.id.downloaduploadVideo:
				intent.setClass(this, DownloadUploadVideoActivity.class);
				startActivity(intent);
				break;
			case R.id.upload_gift:
				GiftUtil.parseJsonAndUpload();
				break;
		}
	}

	/**
	 * 初始化oss
	 */
	private void initOSS() {
		mOSSConf = new ClientConfiguration();
		mOSSConf.setConnectionTimeout(30 * 1000); // 连接超时，默认15秒
		mOSSConf.setSocketTimeout(30 * 1000); // socket超时，默认15秒
		mOSSConf.setMaxConcurrentRequest(50); // 最大并发请求书，默认5个
		mOSSConf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
		OSSLog.enableLog();

		final Handler handler = new Handler();
		// 每30分钟请求一次鉴权
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				new GetFederationTokenTask().request();
				handler.postDelayed(this, 60 * 30 * 1000);
			}
		};

		handler.postDelayed(runnable, 0);
	}

	class GetFederationTokenTask extends GetOSSTokenRequest {

		@Override
		public void onPostExecute(FederationToken result) {
			try {
				if (result != null) {
					AppManager.setFederationToken(result);
					OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(result.accessKeyId, result.accessKeySecret, result.securityToken);
					OSS oss = new OSSClient(getApplicationContext(), result.endpoint, credentialProvider, mOSSConf);
					AppManager.setOSS(oss);
					mOSSTokenRetryCount = 0;
//                    new GetDataRequest().request();
				} else {
					if (mOSSTokenRetryCount < 5) {
						new GetFederationTokenTask().request();
						mOSSTokenRetryCount++;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onErrorExecute(String error) {
			if (mOSSTokenRetryCount < 5) {
				new GetFederationTokenTask().request();
				mOSSTokenRetryCount++;
			}
		}
	}
}
