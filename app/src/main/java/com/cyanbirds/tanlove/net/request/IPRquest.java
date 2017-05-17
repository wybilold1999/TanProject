package com.cyanbirds.tanlove.net.request;

import android.text.TextUtils;

import com.cyanbirds.tanlove.CSApplication;
import com.cyanbirds.tanlove.R;
import com.cyanbirds.tanlove.manager.AppManager;
import com.cyanbirds.tanlove.net.base.ResultPostExecute;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by wangyb on 2017/5/17.
 * 描述：获取ip
 */

public class IPRquest extends ResultPostExecute<String> {

    public void request() {
        Call<ResponseBody> call = AppManager.getUserService().getIp();
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
            if (!TextUtils.isEmpty(json)) {
                int start = json.indexOf("[");
                int end = json.indexOf("]");
                String ip = json.substring(start + 1, end);
                onPostExecute(ip);
            } else {
                onErrorExecute("");
            }
        } catch (Exception e) {
        }
    }

}
