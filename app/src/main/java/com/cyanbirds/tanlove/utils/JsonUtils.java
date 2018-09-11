package com.cyanbirds.tanlove.utils;

import android.text.TextUtils;

import com.cyanbirds.tanlove.CSApplication;
import com.cyanbirds.tanlove.R;
import com.cyanbirds.tanlove.entity.AllKeys;
import com.cyanbirds.tanlove.entity.ClientUser;
import com.cyanbirds.tanlove.manager.AppManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonUtils {

    public static ClientUser parseClientUser(String json){
        try {
            String decrptData = AESOperator.getInstance().decrypt(json);
            JsonObject obj = new JsonParser().parse(decrptData).getAsJsonObject();
            int code = obj.get("code").getAsInt();
            if (code == 1) {
                return null;
            }
            JsonObject data = obj.get("data").getAsJsonObject();
            ClientUser clientUser = AppManager.getClientUser();
            clientUser.userId = data.get("uid").getAsString();
            clientUser.userPwd = data.get("upwd").getAsString();
            clientUser.sex = data.get("sex").getAsInt() == 1 ? "男" : (data.get("sex").getAsInt() == 0 ? "女" : "all");
            clientUser.mobile = data.get("phone") == null ? "" : data.get("phone").getAsString();
            clientUser.qq_no = data.get("qq").getAsString();
            clientUser.weixin_no = data.get("wechat").getAsString();
            clientUser.face_url = data.get("faceUrl").getAsString();
            clientUser.user_name = data.get("nickname").getAsString();
            clientUser.occupation = data.get("occupation").getAsString();
            clientUser.education = data.get("education").getAsString();
            clientUser.tall = data.get("heigth").getAsString();
            clientUser.weight = data.get("weight").getAsString();
            clientUser.isCheckPhone = data.get("isCheckPhone").getAsBoolean();
            clientUser.is_vip = data.get("isVip").getAsBoolean();
            clientUser.is_download_vip = data.get("isDownloadVip").getAsBoolean();
            JsonObject jsonObject = data.get("showClient").getAsJsonObject();
            clientUser.isShowVip = jsonObject.get("isShowVip").getAsBoolean();
            clientUser.isShowDownloadVip = jsonObject.get("isShowDownloadVip").getAsBoolean();
            clientUser.isShowGold = jsonObject.get("isShowGold").getAsBoolean();
            clientUser.isShowLovers = jsonObject.get("isShowLovers").getAsBoolean();
            clientUser.isShowVideo = jsonObject.get("isShowVideo").getAsBoolean();
            clientUser.isShowMap = jsonObject.get("isShowMap").getAsBoolean();
            clientUser.isShowRpt = jsonObject.get("isShowRpt").getAsBoolean();
            clientUser.isShowTd = jsonObject.get("isShowTd").getAsBoolean();
            clientUser.isShowAppointment = jsonObject.get("isShowAppointment").getAsBoolean();
            clientUser.isShowGiveVip = jsonObject.get("isShowGiveVip").getAsBoolean();
            clientUser.gold_num = data.get("goldNum").getAsInt();
            clientUser.state_marry = data.get("emotionStatus").getAsString();
            clientUser.city = data.get("city").getAsString();
            clientUser.age = data.get("age").getAsInt();
            clientUser.signature = data.get("signature").getAsString();
            clientUser.constellation = data.get("constellation").getAsString();
            clientUser.distance = data.get("distance").getAsString();
            clientUser.intrest_tag = data.get("intrestTag").getAsString();
            clientUser.personality_tag = data.get("personalityTag").getAsString();
            clientUser.part_tag = data.get("partTag").getAsString();
            clientUser.purpose = data.get("purpose").getAsString();
            clientUser.love_where = data.get("loveWhere").getAsString();
            clientUser.do_what_first = data.get("doWhatFirst").getAsString();
            clientUser.conception = data.get("conception").getAsString();
            clientUser.sessionId = data.get("sessionId").getAsString();
            clientUser.imgUrls = data.get("pictures") == null ? "" : data.get("pictures").getAsString();
            clientUser.gifts = data.get("gifts").getAsString();
            clientUser.versionCode = data.get("versionCode").getAsInt();
            clientUser.apkUrl = data.get("apkUrl").getAsString();
            clientUser.versionUpdateInfo = data.get("versionUpdateInfo").getAsString();
            clientUser.isForceUpdate = data.get("isForceUpdate").getAsBoolean();
            return clientUser;
        } catch (Exception e) {
        }
        return null;
    }

    public static AllKeys parseJsonIdKeys(String json){
        try {
            String decryptData = AESOperator.getInstance().decrypt(json);
            if (!TextUtils.isEmpty(decryptData)) {
                Gson gson = new Gson();
                AllKeys keys = gson.fromJson(decryptData, AllKeys.class);
                return keys;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean parseCheckIsRegister(String json) {
        try {
            JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
            int code = obj.get("code").getAsInt();
            if (code != 0) {
                return false;
            }
            boolean isRegister = obj.get("data").getAsBoolean();
            return isRegister;
        } catch (Exception e) {
        }
        return false;
    }
}
