package com.cyanbirds.tanlove.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.LocationManagerProxy;
import com.cyanbirds.tanlove.R;
import com.cyanbirds.tanlove.activity.base.BaseActivity;
import com.cyanbirds.tanlove.config.AppConstants;
import com.cyanbirds.tanlove.config.ValueKey;
import com.cyanbirds.tanlove.db.ConversationSqlManager;
import com.cyanbirds.tanlove.entity.FederationToken;
import com.cyanbirds.tanlove.fragment.FoundFragment;
import com.cyanbirds.tanlove.fragment.HomeLoveFragment;
import com.cyanbirds.tanlove.fragment.MessageFragment;
import com.cyanbirds.tanlove.fragment.PersonalFragment;
import com.cyanbirds.tanlove.fragment.VideoShowFragment;
import com.cyanbirds.tanlove.helper.SDKCoreHelper;
import com.cyanbirds.tanlove.listener.MessageUnReadListener;
import com.cyanbirds.tanlove.manager.AppManager;
import com.cyanbirds.tanlove.manager.NotificationManager;
import com.cyanbirds.tanlove.net.request.GetOSSTokenRequest;
import com.cyanbirds.tanlove.net.request.UploadTokenRequest;
import com.cyanbirds.tanlove.utils.FileAccessorUtils;
import com.cyanbirds.tanlove.utils.PreferencesUtils;
import com.cyanbirds.tanlove.utils.PushMsgUtil;
import com.cyanbirds.tanlove.utils.ToastUtil;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;
import com.igexin.sdk.PushManager;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.analytics.MobclickAgent;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.yuntongxun.ecsdk.ECInitParams;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class MainActivity extends BaseActivity implements MessageUnReadListener.OnMessageUnReadListener, HuaweiApiClient.ConnectionCallbacks, HuaweiApiClient.OnConnectionFailedListener,
		HuaweiApiAvailability.OnUpdateListener {

	private String TAG = this.getClass().getSimpleName();
	private FragmentTabHost mTabHost;
	private int mCurrentTab;
	private ClientConfiguration mOSSConf;

	private static final int REQUEST_PERMISSION = 0;

	private static final int MSG_SET_ALIAS = 1001;//极光推送设置别名
	private static final int MSG_SET_TAGS = 1002;//极光推送设置tag

	private long clickTime = 0; //记录第一次点击的时间

	private static HuaweiApiClient huaweiApiClient;

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case MSG_SET_ALIAS:
					JPushInterface.setAliasAndTags(getApplicationContext(), null, null, mAliasCallback);
					JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
					break;
				case MSG_SET_TAGS:
					JPushInterface.setAliasAndTags(getApplicationContext(), null, null, mAliasCallback);
					JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mAliasCallback);
					break;
			}
		}
	};

	/**
	 * oss鉴权获取失败重试次数
	 */
	public int mOSSTokenRetryCount = 0;

	public final static String CURRENT_TAB = "current_tab";
	private static final TableConfig[] tableConfig = new TableConfig[] {
			new TableConfig(R.string.tab_find_love, HomeLoveFragment.class,
					R.drawable.tab_tao_love_selector),
			new TableConfig(R.string.tab_found, FoundFragment.class,
					R.drawable.tab_found_selector),
			new TableConfig(R.string.video_show, VideoShowFragment.class,
					R.drawable.tab_video_selector),
			new TableConfig(R.string.tab_message, MessageFragment.class,
					R.drawable.tab_my_message_selector),
			new TableConfig(R.string.tab_personal, PersonalFragment.class,
					R.drawable.tab_personal_selector) };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		setupViews();
		setupEvent();
		initOSS();
		SDKCoreHelper.init(this, ECInitParams.LoginMode.FORCE_LOGIN);
		updateConversationUnRead();

		/**
		 * 注册小米推送
		 */
		MiPushClient.registerPush(this, AppConstants.MI_PUSH_APP_ID, AppConstants.MI_PUSH_APP_KEY);
		/**
		 * 注册信鸽推送
		 */
		XGPushManager.registerPush(getApplicationContext(), "userId=" + AppManager.getClientUser().userId);
		//个推
		initGeTuiPush();

		initJPush();

		initHuaWeiPush();

		/**
		 * 启动程序的时候删除apk文件夹下的内容
		 */
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				File file = FileAccessorUtils.getAPKPathName();
				if (file != null && file.exists()) {
					File[] files = file.listFiles();
					if (files.length > 0) {
						for(File f : files) {
							f.delete();
						}
					}
				}
			}
		});

		loadData();
	}

	/**
	 * 点击通知栏的消息，将消息入库
	 */
	private void loadData() {
		String msg = getIntent().getStringExtra(ValueKey.DATA);
		if (!TextUtils.isEmpty(msg)) {
			PushMsgUtil.getInstance().handlePushMsg(false, msg);
			NotificationManager.getInstance().cancelNotification();
			AppManager.isMsgClick = true;
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

	/**
	 * 个推注册
	 */
	private void initGeTuiPush() {
		PackageManager pkgManager = getPackageManager();
		// 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
		boolean sdCardWritePermission =
				pkgManager.checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;
		// read phone state用于获取 imei 设备信息
		boolean phoneSatePermission =
				pkgManager.checkPermission(Manifest.permission.READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;
		if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission) {
			//请求权限
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
					REQUEST_PERMISSION);
		} else {
			// SDK初始化，第三方程序启动时，都要进行SDK初始化工作
			PushManager.getInstance().initialize(this.getApplicationContext());
		}
	}

	private void initJPush() {
		// 初始化 JPush
		JPushInterface.init(this);
//		JPushInterface.setDebugMode(true);

		if (!PreferencesUtils.getJpushSetAliasState(this)) {
			//调用JPush API设置Alias
			mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, AppManager.getClientUser().userId));
			//调用JPush API设置Tag
			Set<String> tag = new LinkedHashSet<>(1);
			if ("男".equals(AppManager.getClientUser().sex)) {
				tag.add("female");
			} else {
				tag.add("male");
			}
			mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tag));
		}
	}

	private void initHuaWeiPush() {
		huaweiApiClient = new HuaweiApiClient.Builder(this).addApi(HuaweiPush.PUSH_API).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).build();
		huaweiApiClient.connect();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);// 必须要调用这句(信鸽推送)
		mCurrentTab = getIntent().getIntExtra(CURRENT_TAB, 0);
		mTabHost.setCurrentTab(mCurrentTab);
	}

	/**
	 * 设置视图
	 */
	private void setupViews() {
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		for (int i = 0; i < tableConfig.length; i++) {
			if (i == 2 && !AppManager.getClientUser().isShowVideo){
				continue;
			}
			mTabHost.addTab(
					mTabHost.newTabSpec(getString(tableConfig[i].titleId))
							.setIndicator(getIndicator(i)),
					tableConfig[i].targetClass, null);
		}
		if (Build.VERSION.SDK_INT >= 11) {
			mTabHost.getTabWidget().setShowDividers(
					LinearLayout.SHOW_DIVIDER_NONE);// 设置不显示分割线
		}
		mTabHost.setCurrentTab(mCurrentTab);

	}


	private void setupEvent(){
		MessageUnReadListener.getInstance().setMessageUnReadListener(this);
	}


	private View getIndicator(int index) {
		View view = View.inflate(this, R.layout.tab_indicator_view, null);
		TextView tv = (TextView) view.findViewById(R.id.tab_item);
		ImageView tab_icon = (ImageView) view.findViewById(R.id.tab_icon);
		tab_icon.setImageResource(tableConfig[index].tabImage);
		tv.setText(tableConfig[index].titleId);
		return view;

	}

	/**
	 * 底部导航配置
	 */
	private static class TableConfig {
		final int titleId;
		final Class<?> targetClass;
		final int tabImage;

		TableConfig(int titleId, Class<?> targetClass, int tabImage) {
			this.titleId = titleId;
			this.targetClass = targetClass;
			this.tabImage = tabImage;
		}
	}

	@Override
	public void notifyUnReadChanged(int type) {
		updateConversationUnRead();
	}

	/**
	 * 更新会话未读消息总数
	 */
	private void updateConversationUnRead() {
		View view;
		if (AppManager.getClientUser().isShowVideo) {
			view = mTabHost.getTabWidget().getChildTabViewAt(3);
		} else {
			view = mTabHost.getTabWidget().getChildTabViewAt(2);
		}
		TextView unread_message_num = (TextView) view
				.findViewById(R.id.unread_message_num);

		int total = ConversationSqlManager.getInstance(this)
				.getAnalyticsUnReadConversation();
		unread_message_num.setVisibility(View.GONE);
		if (total > 0) {
			if (total >= 100) {
				unread_message_num.setText(String.valueOf("99+"));
			} else {
				unread_message_num.setText(String.valueOf(total));
			}
			unread_message_num.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == REQUEST_PERMISSION) {
			if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
				PushManager.getInstance().initialize(this.getApplicationContext());
			} else {
				Log.e("GetuiSdkDemo",
						"we highly recommend that you need to grant the special permissions before initializing the SDK, otherwise some "
								+ "functions will not work");
				PushManager.getInstance().initialize(this.getApplicationContext());
			}
		} else {
			onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	/**
	 * 极光推送设置别名后的回调
	 */
	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			switch (code) {
				case 0:
					//Set tag and alias success
					PreferencesUtils.setJpushSetAliasState(MainActivity.this, true);
					break;

				case 6002:
					//"Failed to set alias and tags due to timeout. Try again after 60s.";
					ConnectivityManager conn = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo info = conn.getActiveNetworkInfo();
					if (info != null && info.isConnected()) {
						mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
						mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
					}
					break;
			}
		}
	};

	/**
	 * ====================================华为推送=================================
	 */
	@Override
	protected void onStart() {
		Log.i(TAG, "onStart, ErrorCode: " + HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(this));
		super.onStart();
		huaweiApiClient.connect();
	}

	@Override
	public void onConnected() {
		Log.i(TAG, "onConnected, IsConnected: " + huaweiApiClient.isConnected());
		if (huaweiApiClient.isConnected()) {
			// 异步调用方式
			PendingResult<TokenResult> tokenResult = HuaweiPush.HuaweiPushApi.getToken(huaweiApiClient);
			tokenResult.setResultCallback(new ResultCallback<TokenResult>() {

				@Override
				public void onResult(TokenResult result) {
					new UploadTokenRequest().request(result.getTokenRes().getToken());
				}

			});
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.i(TAG, "onConnectionFailed, ErrorCode: " + result.getErrorCode());
	}

	@Override
	public void onConnectionSuspended(int cause) {
		Log.i(TAG, "onConnectionSuspended, cause: " + cause + ", IsConnected: " + huaweiApiClient.isConnected());
	}

	@Override
	public void onUpdateFailed(ConnectionResult result) {
		Log.i(TAG, "onUpdateFailed, ErrorCode: " + result.getErrorCode());
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - clickTime) > 2000) {
				ToastUtil.showMessage(R.string.exit_tips);
				clickTime = System.currentTimeMillis();
			} else {
				exitApp();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
