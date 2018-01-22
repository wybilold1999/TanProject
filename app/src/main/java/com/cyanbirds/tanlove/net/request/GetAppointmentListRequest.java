package com.cyanbirds.tanlove.net.request;

import android.support.v4.util.ArrayMap;

import com.cyanbirds.tanlove.CSApplication;
import com.cyanbirds.tanlove.R;
import com.cyanbirds.tanlove.entity.AppointmentModel;
import com.cyanbirds.tanlove.entity.FollowModel;
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

/**
 * @author Cloudsoar(wangyb)
 * @datetime 2016-05-03 10:38 GMT+8
 * @email 395044952@qq.com
 */
public class GetAppointmentListRequest extends ResultPostExecute<List<AppointmentModel>> {
    /**
     *
     * @param pageNo
     * @param pageSize
     * @param userId
     * @param flag 0:约会我的 1：我约会的
     */
    public void request(int pageNo, int pageSize, String userId, int flag){
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("userId", userId);
        params.put("flag", String.valueOf(flag));
        Call<ResponseBody> call = AppManager.getLoveService().getAppointmentList(AppManager.getClientUser().sessionId, params);
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
            String result = obj.get("data").getAsString();
            Type listType = new TypeToken<ArrayList<AppointmentModel>>() {
            }.getType();
            Gson gson = new Gson();
            ArrayList<AppointmentModel> appointmentModels = gson.fromJson(result, listType);
            onPostExecute(appointmentModels);
        } catch (Exception e) {
            onErrorExecute(CSApplication.getInstance().getResources()
                    .getString(R.string.data_parser_error));
        }
    }
}
