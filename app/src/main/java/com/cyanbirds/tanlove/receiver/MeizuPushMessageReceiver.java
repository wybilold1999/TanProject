package com.cyanbirds.tanlove.receiver;

import android.content.Context;
import android.util.Log;

import com.cyanbirds.tanlove.config.AppConstants;
import com.cyanbirds.tanlove.manager.AppManager;
import com.meizu.cloud.pushsdk.MzPushMessageReceiver;
import com.meizu.cloud.pushsdk.notification.PushNotificationBuilder;
import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;

/**
 * 作者：wangyb
 * 时间：2017/3/31 22:56
 * 描述：
 */
public class MeizuPushMessageReceiver extends MzPushMessageReceiver {
	@Override
	@Deprecated
	public void onRegister(Context context, String pushid) {
		//应用在接受返回的 pushid
		Log.d("test", "test");
	}
	@Override
	public void onMessage(Context context, String s) {
		//接收服务器推送的消息
		Log.d("test", "test");
	}
	@Override
	@Deprecated
	public void onUnRegister(Context context, boolean b) {
		//调用 PushManager.unRegister(context）方法后，会在此回调反注册状态
	}
	//设置通知栏小图标
	@Override
	public void onUpdateNotificationBuilder(PushNotificationBuilder pushNotificationBuilder) {
		Log.d("test", "test");
	}
	@Override
	public void onPushStatus(Context context,PushSwitchStatus pushSwitchStatus) {
		//检查通知栏和透传消息开关状态回调
		Log.d("test", "test");
	}
	@Override
	public void onRegisterStatus(Context context,RegisterStatus registerStatus) {
		com.meizu.cloud.pushsdk.PushManager.subScribeAlias(
				context, AppConstants.MZ_APP_ID,
				AppConstants.MZ_APP_KEY, registerStatus.getPushId(),
				AppManager.getClientUser().userId);
		if ("男".equals(AppManager.getClientUser().sex)) {
			com.meizu.cloud.pushsdk.PushManager.subScribeTags(
					context, AppConstants.MZ_APP_ID,
					AppConstants.MZ_APP_KEY, registerStatus.getPushId(), "female");
		} else {
			com.meizu.cloud.pushsdk.PushManager.subScribeTags(
					context, AppConstants.MZ_APP_ID,
					AppConstants.MZ_APP_KEY, registerStatus.getPushId(), "male");
		}
		//新版订阅回调
		Log.d("test", registerStatus.getPushId());
	}
	@Override
	public void onUnRegisterStatus(Context context,UnRegisterStatus unRegisterStatus) {
		//新版反订阅回调
		Log.d("test", "test");
	}
	@Override
	public void onSubTagsStatus(Context context,SubTagsStatus subTagsStatus) {
		//标签回调
		Log.d("test", "test");
	}
	@Override
	public void onSubAliasStatus(Context context,SubAliasStatus subAliasStatus) {
		//别名回调
		Log.d("test", "test");
	}
	@Override
	public void onNotificationArrived(Context context, String title, String content,
									  String selfDefineContentString) {
		//通知栏消息到达回调
		Log.d("test", "test");
	}
	@Override
	public void onNotificationClicked(Context context, String title, String content,
									  String selfDefineContentString) {
		//通知栏消息点击回调
		Log.d("test", "test");
	}
	@Override
	public void onNotificationDeleted(Context context, String title, String content,
									  String selfDefineContentString) {
		//通知栏消息删除回调；flyme6 以上不再回调
		Log.d("test", "test");
	}
}
