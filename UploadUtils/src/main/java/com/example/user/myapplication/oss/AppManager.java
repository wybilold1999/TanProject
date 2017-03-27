package com.example.user.myapplication.oss;

import android.content.Context;

import com.alibaba.sdk.android.oss.OSS;
import com.example.user.myapplication.model.ClientUser;

/**
 * 
 * @ClassName:AppManager
 * @Description:APP管理类
 * @Author:wangyb
 * @Date:2015年5月4日下午5:30:07
 *
 */
public class AppManager {

	/** Android 应用上下文 */
	private static Context mContext = null;
	/** 用户登录信息 */
	private static ClientUser mClientUser;

	private static OSS mOSS;

	private static FederationToken mOSSFederationToken;

	public static FederationToken getFederationToken() {
		if(mOSSFederationToken==null){
			mOSSFederationToken=new FederationToken();
		}
		return mOSSFederationToken;
	}

	public static FederationToken setFederationToken(FederationToken token) {
		return mOSSFederationToken = token;
	}

	public static OSS getOSS() {
		return mOSS;
	}

	public static void setOSS(OSS oss) {
		mOSS = oss;
	}


	/**
	 * 设置用户信息
	 * 
	 * @param user
	 */
	public static void setClientUser(ClientUser user) {
		mClientUser = user;
	}

	/**
	 * 获取用户信息
	 * 
	 * @return
	 */
	public static ClientUser getClientUser() {
		return mClientUser;
	}


	/**
	 * 返回上下文对象
	 * 
	 * @return
	 */
	public static Context getContext() {
		return mContext;
	}

	/**
	 * 设置上下文对象
	 * 
	 * @param context
	 */
	public static void setContext(Context context) {
		mContext = context;
	}

	/**
	 * 获取需要上传到oss路径
	 *
	 * @param
	 * @return
	 */
	public static String getOSSImgPath() {
		String path = "tan_love/img/tl_" + getUUID() + ".jpg";
		return path;
	}

	/**
	 * 获取需要上传到oss路径
	 *
	 * @param
	 * @return
	 */
	public static String getOSSVideoPath() {
		String path = "tan_love/video/tl_" + getUUID() + ".mp4";
		return path;
	}

	/**
	 * 获取UUID作为群聊唯一标识
	 * 
	 * @return
	 */
	public static String getUUID() {
		String strUUID = java.util.UUID.randomUUID().toString();
		return strUUID;
	}

}
