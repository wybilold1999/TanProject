package com.example.user.myapplication.oss;

/**
 * @author Cloudsoar(wangyb)
 * @datetime 2016-05-19 20:13 GMT+8
 * @email 395044952@qq.com
 */
public class Config {
//    public static String HOST_PORT = "http://112.74.85.193/MoMoLoveServer/";
    public static String HOST_PORT = "http://192.168.4.107/TanLoveServer/";
//public static String HOST_PORT = "http://192.168.1.102/TanLoveServer/";

    public static String imagePoint = "http://real-love-server.img-cn-shenzhen.aliyuncs.com/";

    public static String VIDEO_URL = "http://app.yueai8.net:2015/adr/video.php?ac=getMemberVideos&hash=fe8f1b326aacb5122105c0f590edd53a&puid=17662624&uid=17772615";

    public static String USER_VIDEO_IS_EXISTS = "video/isExists";

    public static String UPLOAD_VIDEO = HOST_PORT + "video/uploadVideo";
    public static String UPLOAD_USER_DYC = HOST_PORT + "dynamic/insertUserAndDyc";
    public static String UPLOAD_CAR_USER_DYC = HOST_PORT + "dynamic/insertCarUserAndDyc";
    public static String UPLOAD_GIFT = HOST_PORT + "video/uploadGift";
}
