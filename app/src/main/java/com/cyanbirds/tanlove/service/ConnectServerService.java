package com.cyanbirds.tanlove.service;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cyanbirds.tanlove.manager.NotificationManagerUtils;

/**
 * 作者：wangyb
 * 时间：2016/9/24 17:42
 * 描述：
 */
public class ConnectServerService extends Service {

	public ConnectServerService() {
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			startForeground(1, NotificationManagerUtils.getInstance().getNotification());
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e("tanlove_log", "service destroy");
		stopForeground(true);
	}
}
