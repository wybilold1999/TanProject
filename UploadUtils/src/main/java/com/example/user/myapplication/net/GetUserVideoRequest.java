package com.example.user.myapplication.net;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.myapplication.CSApplication;
import com.example.user.myapplication.ResultPostExecute;
import com.example.user.myapplication.model.UserVideo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 作者：wangyb
 * 时间：2016/8/31 10:44
 * 描述：
 */
public class GetUserVideoRequest extends ResultPostExecute<ArrayList<UserVideo>> {
	public void request(String url) {
		try {
			StringRequest request = new StringRequest(Request.Method.GET,
					url, new Response.Listener<String>() {

				@Override
				public void onResponse(String response) {
					parserData(response);
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					onErrorExecute("");
				}
			});
			CSApplication.getInstance().getRequestQueue().add(request);
		} catch (Exception e) {
			e.printStackTrace();
			onErrorExecute("");
		}
	}

	/*
	 * 解析数据
	 *
	 * @param result
	 */
	private void parserData(String result) {
		try {
			JsonObject obj = new JsonParser().parse(result).getAsJsonObject();
			String error = obj.get("error").getAsString();
			if (TextUtils.isEmpty(error)) {//请求成功
				JsonArray jsonArray = obj.get("data").getAsJsonArray();
				Type listType = new TypeToken<ArrayList<UserVideo>>() {
				}.getType();
				Gson gson = new Gson();
				ArrayList<UserVideo> infos = gson.fromJson(jsonArray, listType);
				onPostExecute(infos);
			} else {
				onErrorExecute("");
			}
		} catch (Exception e) {
			onErrorExecute("解析数据错误");
		}
	}
}
