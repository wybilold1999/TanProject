package com.cyanbirds.tanlove.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.List;

public class Utils {

    /** 包名 */
    public static String pkgName = "com.cyanbirds.tanlove";

    public static void goToMarket(Context context, String channel) {
        /**
         * 根据渠道跳转到不同的应用市场更新APP
         */
        if ("sanxing".equals(channel)) {
            Uri uri = Uri.parse("http://www.samsungapps.com/appquery/appDetail.as?appId=" + pkgName);
            Intent goToMarket = new Intent();
            goToMarket.setClassName("com.sec.android.app.samsungapps", "com.sec.android.app.samsungapps.Main");
            goToMarket.setData(uri);
            try {
                context.startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        } else if ("leshi".equals(channel)) {
            Intent intent = new Intent();
            intent.setClassName("com.letv.app.appstore", "com.letv.app.appstore.appmodule.details.DetailsActivity");
            intent.setAction("com.letv.app.appstore.appdetailactivity");
            intent.putExtra("packageName", pkgName);
            context.startActivity(intent);
        } else {
            Uri uri = Uri.parse("market://details?id=" + pkgName);
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                context.startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void goToSetting(Activity context, int requestCode) {
        //判断是否为小米系统
        if (TextUtils.equals(BrandUtils.getSystemInfo().getOs(), BrandUtils.SYS_MIUI)) {
            Intent miuiIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            miuiIntent.putExtra("extra_pkgname", pkgName);
            //检测是否有能接受该Intent的Activity存在
            List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(miuiIntent, PackageManager.MATCH_DEFAULT_ONLY);
            if (resolveInfos.size() > 0) {
                context.startActivityForResult(miuiIntent, requestCode);
                return;
            }
        }
        //如果不是小米系统 则打开Android系统的应用设置页
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", pkgName, null);
        intent.setData(uri);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * *************************************************网络相关************************************************************
     *
     */

    /**
     * 判断是否有网络连接
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空
            return null != networkInfo && networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空 并且类型是否为WIFI
            if (null != networkInfo && networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            //获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空 并且类型是否为MOBILE
            if (null != networkInfo && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                return networkInfo.isAvailable();
        }
        return false;
    }
}
