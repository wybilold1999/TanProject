package com.cyanbirds.tanlove.net.request;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.cyanbirds.tanlove.CSApplication;
import com.cyanbirds.tanlove.R;
import com.cyanbirds.tanlove.manager.AppManager;
import com.cyanbirds.tanlove.net.base.ResultPostExecute;
import com.cyanbirds.tanlove.utils.CheckUtil;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * 作者：wangyb
 * 时间：2017/5/13 14:47
 * 描述：
 */
public class UploadCityInfoRequest extends ResultPostExecute<String> {
	public void request(final String city){
		ArrayMap<String, String> params = new ArrayMap<>();
		params.put("channel", CheckUtil.getAppMetaData(CSApplication.getInstance(), "UMENG_CHANNEL"));
		if (!TextUtils.isEmpty(city)) {
			params.put("currentCity", city);
		} else {
			params.put("currentCity", "");
		}
		Call<ResponseBody> call = AppManager.getUserService().uploadCityInfo(params, AppManager.getClientUser().sessionId);
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
			onPostExecute(json);
		} catch (Exception e) {
			onErrorExecute(CSApplication.getInstance().getResources()
					.getString(R.string.data_parser_error));
		}
	}
}
