package com.example.user.myapplication.net;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.myapplication.CSApplication;
import com.example.user.myapplication.ResultPostExecute;
import com.example.user.myapplication.oss.Config;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：wangyb
 * 时间：2016/10/9 16:29
 * 描述：
 */
public class UserVideoIsExists extends ResultPostExecute<Boolean> {
	public void request(final String uid) {
		try {
			StringRequest request = new StringRequest(Request.Method.POST,
					Config.HOST_PORT + Config.USER_VIDEO_IS_EXISTS, new Response.Listener<String>() {

				@Override
				public void onResponse(String response) {
					parserData(response);
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					onErrorExecute("");
				}
			}) {
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					HashMap<String, String> params = new HashMap();
					params.put("uid", uid);
					return params;
				}
			};
			CSApplication.getInstance().getRequestQueue().add(request);
		} catch (Exception e) {
			e.printStackTrace();
			onErrorExecute("");
		}
	}

	/**
	 * 解析数据
	 *
	 * @param result
	 */
	private void parserData(String result) {
		try {
			JsonObject obj = new JsonParser().parse(result).getAsJsonObject();
			int error = obj.get("code").getAsInt();
			if (error == 0) {//请求成功
				onPostExecute(false);//不存在
			} else {
				onPostExecute(true);
			}
		} catch (Exception e) {
			onErrorExecute("解析数据错误");
		}
	}
}
