package com.example.user.myapplication.model;

/**
 * 作者：wangyb
 * 时间：2016/8/31 18:21
 * 描述：
 */
public class DbVideo {
	public String originalImgPath; //别人服务器的图片地址
	public String originalVideoPath;  //别人服务器的视频地址
	public String curImgPath;  //OSS上的图片地址
	public String curVideoPath; //OSS上的视频地址
	public String uid; //用户id
	public String description; //视频描述
	public String view; //视频观看量
	public String rose; //视频所需要的玫瑰数量
	public String username; //用户名

	public String sex; //视频性别
	public String type; //视频类型  0：最新  1：最热
	public String isTurnOnVideo;  //是否显示video

	public class SexType {
		public static final String FEMALE = "0";
		public static final String MALE = "1";
	}

	public class VideoType {
		public static final String NEW = "0";
		public static final String HOT = "1";
		public static final String DANCING = "2";//热舞
		public static final String CLOTH = "3";//制服
		public static final String SEXY = "4";//性感
		public static final String PURE = "5";//清纯
	}
}
