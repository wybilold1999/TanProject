package com.cyanbirds.tanlove.net.request;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.cyanbirds.tanlove.CSApplication;
import com.cyanbirds.tanlove.R;
import com.cyanbirds.tanlove.entity.ClientUser;
import com.cyanbirds.tanlove.manager.AppManager;
import com.cyanbirds.tanlove.net.base.ResultPostExecute;
import com.cyanbirds.tanlove.utils.AESOperator;
import com.cyanbirds.tanlove.utils.PreferencesUtils;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * 
 * @ClassName:CheckSmsCodeRequest
 * @Description:小米登陆
 * @Author:wangyb
 * @Date:2015年5月11日下午4:35:22
 *
 */
public class XMLoginRequest extends ResultPostExecute<ClientUser> {

	public void request(String xmOAuthResults, final String channelId, final String city) {
		ArrayMap<String, String> params = new ArrayMap<>();
		params.put("xmOAuthResults", xmOAuthResults);
		params.put("device_name", AppManager.getDeviceName());
		params.put("channel", channelId);
		params.put("platform", "xiaomi");
		params.put("version", String.valueOf(AppManager.getVersionCode()));
		params.put("os_version", AppManager.getDeviceSystemVersion());
		params.put("device_id", AppManager.getDeviceId());
		if (!TextUtils.isEmpty(city)) {
			params.put("currentCity", city);
		} else {
			params.put("currentCity", "");
		}
		params.put("loginTime", String.valueOf(PreferencesUtils.getLoginTime(CSApplication.getInstance())));
		Call<ResponseBody> call = AppManager.getUserService().xmLogin(AppManager.getClientUser().sessionId, params);
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
				if(response.isSuccessful()){
					try {
						parseJson(response.body().string());
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						response.body().close();
					}
				} else {
					onErrorExecute(CSApplication.getInstance()
							.getResources()
							.getString(R.string.network_requests_error));
				}
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {
				onErrorExecute(CSApplication.getInstance()
						.getResources()
						.getString(R.string.network_requests_error));
			}
		});
	}

	private void parseJson(String json){
		try {
			String decryptData = AESOperator.getInstance().decrypt(json);
			JsonObject obj = new JsonParser().parse(decryptData).getAsJsonObject();
			int code = obj.get("code").getAsInt();
			if (code != 0) {
				onErrorExecute(CSApplication.getInstance().getResources()
						.getString(R.string.data_load_error));
				return;
			}
			JsonObject data = obj.get("data").getAsJsonObject();
			ClientUser clientUser = new ClientUser();
			clientUser.userId = data.get("uid").getAsString();
			clientUser.userPwd = data.get("upwd").getAsString();
			clientUser.sex = data.get("sex").getAsInt() == 1 ? "男" : (data.get("sex").getAsInt() == 0 ? "女" : "all");
			clientUser.user_name = data.get("nickname").getAsString();
			clientUser.tall = data.get("heigth").getAsString();
			clientUser.weight = data.get("weight").getAsString();
			clientUser.isCheckPhone = data.get("isCheckPhone").getAsBoolean();
			clientUser.is_vip = data.get("isVip").getAsBoolean();
			clientUser.is_download_vip = data.get("isDownloadVip").getAsBoolean();
			clientUser.gold_num = data.get("goldNum").getAsInt();
			JsonObject jsonObject = data.get("showClient").getAsJsonObject();
			clientUser.isShowVip = jsonObject.get("isShowVip").getAsBoolean();
			clientUser.isShowDownloadVip = jsonObject.get("isShowDownloadVip").getAsBoolean();
			clientUser.isShowGold = jsonObject.get("isShowGold").getAsBoolean();
			clientUser.isShowLovers = jsonObject.get("isShowLovers").getAsBoolean();
			clientUser.isShowVideo = jsonObject.get("isShowVideo").getAsBoolean();
			clientUser.isShowMap = jsonObject.get("isShowMap").getAsBoolean();
			clientUser.isShowRpt = jsonObject.get("isShowRpt").getAsBoolean();
			clientUser.isShowTd = jsonObject.get("isShowTd").getAsBoolean();
			clientUser.state_marry = data.get("emotionStatus").getAsString();
			clientUser.face_url = data.get("faceUrl").getAsString();
			clientUser.age = data.get("age").getAsInt();
			clientUser.signature = data.get("signature").getAsString();
			clientUser.constellation = data.get("constellation").getAsString();
			clientUser.mobile = data.get("phone") == null ? "" : data.get("phone").getAsString();
			clientUser.qq_no = data.get("qq").getAsString();
			clientUser.weixin_no = data.get("wechat").getAsString();
			clientUser.distance = data.get("distance").getAsString();
			clientUser.occupation = data.get("occupation").getAsString();
			clientUser.education = data.get("education").getAsString();
			clientUser.city = data.get("city").getAsString();
			clientUser.intrest_tag = data.get("intrestTag").getAsString();
			clientUser.personality_tag = data.get("personalityTag").getAsString();
			clientUser.part_tag = data.get("partTag").getAsString();
			clientUser.purpose = data.get("purpose").getAsString();
			clientUser.love_where = data.get("loveWhere").getAsString();
			clientUser.do_what_first = data.get("doWhatFirst").getAsString();
			clientUser.conception = data.get("conception").getAsString();
			clientUser.sessionId = data.get("sessionId").getAsString();
			clientUser.versionCode = data.get("versionCode").getAsInt();
			clientUser.apkUrl = data.get("apkUrl").getAsString();
			clientUser.versionUpdateInfo = data.get("versionUpdateInfo").getAsString();
			clientUser.imgUrls = data.get("pictures") == null ? "" : data.get("pictures").getAsString();
			clientUser.gifts = data.get("gifts").getAsString();
			onPostExecute(clientUser);
		} catch (Exception e) {
//			onErrorExecute("");
			onErrorExecute(CSApplication.getInstance().getResources()
					.getString(R.string.data_parser_error));
		}
	}
}
