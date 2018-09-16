package com.cyanbirds.tanlove.net.request;

import android.support.v4.util.ArrayMap;

import com.cyanbirds.tanlove.manager.AppManager;
import com.cyanbirds.tanlove.net.IUserApi;
import com.cyanbirds.tanlove.net.base.ResultPostExecute;
import com.cyanbirds.tanlove.net.base.RetrofitFactory;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * @author Cloudsoar(wangyb)
 * @datetime 2016-05-03 10:38 GMT+8
 * @email 395044952@qq.com
 */
public class UploadTokenRequest extends ResultPostExecute<String> {
    public void request(String gtClientId, String xgToken){
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("gtClientId", gtClientId);
        map.put("xgToken", xgToken);
        Call<ResponseBody> call = RetrofitFactory.getRetrofit().create(IUserApi.class).uploadToken(map, AppManager.getClientUser().sessionId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        response.body().close();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
}
