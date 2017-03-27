package com.example.user.myapplication.util;

import android.util.Log;

import com.example.user.myapplication.UploadVideoLoveActivity;
import com.example.user.myapplication.model.DbVideo;
import com.example.user.myapplication.model.UserVideo;
import com.example.user.myapplication.model.VideoLove;
import com.example.user.myapplication.net.DownloadFileRequest;
import com.example.user.myapplication.net.UserVideoIsExists;
import com.example.user.myapplication.net.VideoUploadRequest;
import com.example.user.myapplication.oss.Config;
import com.example.user.myapplication.oss.OSSFileUploadRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：wangyb
 * 时间：2016/11/26 21:00
 * 描述：
 */
public class VideoLoveUtil {

	private static Gson sGson = new Gson();
	public static VideoLove parseJson() {
		VideoLove videoLove = sGson.fromJson(json, VideoLove.class);
		return videoLove;
	}

	public static String json = "{\n" +
			"\t\"Code\": 1,\n" +
			"\t\"Message\": \"Success\",\n" +
			"\t\"Body\": {\n" +
			"\t\t\"TopicId\": 55438,\n" +
			"\t\t\"UserId\": 91412656,\n" +
			"\t\t\"Title\": \"我是一个大胸兔~\",\n" +
			"\t\t\"Avatar\": \"http://view01.img.baomihua.com/yd7da157d-ef2b-4dd3-aba2-a9da08b17268.jpg\",\n" +
			"\t\t\"Nick\": \"徐美儿\",\n" +
			"\t\t\"VideoPic\": \"http://img.oss.shuihulu.com/topic/58c2b1ad00ed4a0697863ab7ef652eaf.jpg\",\n" +
			"\t\t\"VideoUrl\": \"http://video.oss.shuihulu.com/video/d3a1de82b661423f8519ba8f71bf1901.mp4\",\n" +
			"\t\t\"TimeLen\": 223,\n" +
			"\t\t\"Videos\": 4,\n" +
			"\t\t\"Sold\": 2631,\n" +
			"\t\t\"Honor\": 0,\n" +
			"\t\t\"Likes\": 64,\n" +
			"\t\t\"Reviews\": 84,\n" +
			"\t\t\"IsPlayed\": 0,\n" +
			"\t\t\"Liked\": 0,\n" +
			"\t\t\"VIPDays\": 0,\n" +
			"\t\t\"Point\": 0,\n" +
			"\t\t\"VideoWidth\": 360,\n" +
			"\t\t\"VideoHeight\": 480\n" +
			"\t}\n" +
			"}";
}
