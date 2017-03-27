package com.cyanbirds.tanlove.net.request;

import android.support.v4.util.ArrayMap;

import com.cyanbirds.tanlove.CSApplication;
import com.cyanbirds.tanlove.R;
import com.cyanbirds.tanlove.entity.YuanFenModel;
import com.cyanbirds.tanlove.manager.AppManager;
import com.cyanbirds.tanlove.net.base.ResultPostExecute;
import com.cyanbirds.tanlove.utils.AESOperator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Cloudsoar(wangyb)
 * @datetime 2016-04-29 16:56 GMT+8
 * @email 395044952@qq.com
 */
public class GetUserPictureRequest extends ResultPostExecute<List<String>> {
    public void request(String userId, int pageNo, int pageSize) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("uid", userId);
        Call<ResponseBody> call = AppManager.getPictureService().getUserPictureList(AppManager.getClientUser().sessionId, params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(response.isSuccessful()){
                        String data = response.body().string();
                        parserJson(data);
                    } else {
                        onErrorExecute(CSApplication.getInstance()
                                .getResources()
                                .getString(R.string.network_requests_error));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    onErrorExecute(CSApplication.getInstance().getResources()
                            .getString(R.string.data_load_error));
                } finally {
                    response.body().close();
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

    private void parserJson(String json){
        try {
            String decryptData = AESOperator.getInstance().decrypt(json);
            JsonObject obj = new JsonParser().parse(decryptData).getAsJsonObject();
            int code = obj.get("code").getAsInt();
            if (code != 0) {
                onErrorExecute(CSApplication.getInstance().getResources()
                        .getString(R.string.data_load_error));
                return;
            }
            String dataString = obj.get("data").getAsString();
            Type listType = new TypeToken<ArrayList<YuanFenModel>>() {
            }.getType();
            Gson gson = new Gson();
            List<String> urls = gson.fromJson(dataString, listType);
            onPostExecute(urls);
        } catch (Exception e) {
            onErrorExecute(CSApplication.getInstance().getResources()
                    .getString(R.string.data_parser_error));
        }
    }
}
