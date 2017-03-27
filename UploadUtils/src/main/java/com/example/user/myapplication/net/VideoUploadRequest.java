package com.example.user.myapplication.net;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.myapplication.CSApplication;
import com.example.user.myapplication.model.DbVideo;
import com.example.user.myapplication.ResultPostExecute;
import com.example.user.myapplication.oss.Config;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：wangyb
 * 时间：2016/8/31 10:44
 * 描述：
 */
public class VideoUploadRequest extends ResultPostExecute<String> {
	public void request(final String data) {
		try {
			StringRequest request = new StringRequest(Request.Method.POST,
					Config.UPLOAD_VIDEO, new Response.Listener<String>() {

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
					Map<String, String> params = new HashMap<String, String>();
					params.put("data", data);
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
			int code = obj.get("code").getAsInt();
			if (code == 0) {//请求成功
				onPostExecute("");
			} else {
				String error = obj.get("data").getAsString();
				onErrorExecute(error);
			}
		} catch (Exception e) {
			onErrorExecute("");
		}
	}
}
