package com.example.user.myapplication.oss;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.myapplication.CSApplication;
import com.example.user.myapplication.ResultPostExecute;
import com.google.gson.Gson;

/**
 * 获取oss鉴权
 * Created by Administrator on 2016/3/14.
 */
public class GetOSSTokenRequest extends ResultPostExecute<FederationToken> {

    public void request(){
        try {
            StringRequest request = new StringRequest(Request.Method.POST,
                    Config.HOST_PORT + "oss/distribute-token", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    parserData(response);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
            CSApplication.getInstance().getRequestQueue().add(request);
        } catch (Exception e) {
        }
    }

    /**
     * 解析数据
     *
     * @param result
     */
    private void parserData(String result) {
        try {
            if (!TextUtils.isEmpty(result)) {
                Gson gson = new Gson();
                String data = AESOperator.getInstance().decrypt(result);
                FederationToken token = gson.fromJson(data, FederationToken.class);
                Log.d("test", token.accessKeyId);
                Log.d("test", token.accessKeySecret);
                Log.d("test", token.securityToken);
                Log.d("test", token.bucketName);
                Log.d("test", token.imagesEndpoint);
                Log.d("test", token.endpoint);
                if (token != null && !TextUtils.isEmpty(token.accessKeySecret) && !TextUtils.isEmpty(token.accessKeyId) && !TextUtils.isEmpty(token.bucketName) && !TextUtils.isEmpty(token.imagesEndpoint)) {
                    onPostExecute(token);
                } else {
                    onErrorExecute("");
                }
            } else {
                onErrorExecute("");
            }
        } catch (Exception e) {
        }
    }

}
