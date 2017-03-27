package com.example.user.myapplication.model;

/**
 * 作者：wangyb
 * 时间：2016/11/26 20:54
 * 描述：视频约爱
 */
public class VideoLove {

	/**
	 * Code : 1
	 * Message : Success
	 * Body : {"TopicId":69847,"UserId":91412518,"Title":"你们觉得这样好看吗","Avatar":"http://view01.img.baomihua.com/y8393816e-eb21-4787-8729-f238b1059b17.jpg","Nick":"若含480","VideoPic":"http://img.oss.shuihulu.com/topic/0b58728656c14344b47ddd51ff87bfc1.jpg","VideoUrl":"http://video.oss.shuihulu.com/video/ea194fd1aa0d4df8aed9dfb9eae4730d.mp4","TimeLen":60,"Videos":44,"Sold":336,"Honor":0,"Likes":3,"Reviews":3,"IsPlayed":0,"Liked":0,"VIPDays":0,"Point":0,"VideoWidth":360,"VideoHeight":480}
	 */

	private int Code;
	private String Message;
	/**
	 * TopicId : 69847
	 * UserId : 91412518
	 * Title : 你们觉得这样好看吗
	 * Avatar : http://view01.img.baomihua.com/y8393816e-eb21-4787-8729-f238b1059b17.jpg
	 * Nick : 若含480
	 * VideoPic : http://img.oss.shuihulu.com/topic/0b58728656c14344b47ddd51ff87bfc1.jpg
	 * VideoUrl : http://video.oss.shuihulu.com/video/ea194fd1aa0d4df8aed9dfb9eae4730d.mp4
	 * TimeLen : 60
	 * Videos : 44
	 * Sold : 336
	 * Honor : 0
	 * Likes : 3
	 * Reviews : 3
	 * IsPlayed : 0
	 * Liked : 0
	 * VIPDays : 0
	 * Point : 0
	 * VideoWidth : 360
	 * VideoHeight : 480
	 */

	private BodyBean Body;

	public int getCode() {
		return Code;
	}

	public void setCode(int Code) {
		this.Code = Code;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String Message) {
		this.Message = Message;
	}

	public BodyBean getBody() {
		return Body;
	}

	public void setBody(BodyBean Body) {
		this.Body = Body;
	}

	public static class BodyBean {
		private int TopicId;
		private int UserId;
		private String Title;
		private String Avatar;
		private String Nick;
		private String VideoPic;
		private String VideoUrl;
		private int TimeLen;
		private int Videos;
		private int Sold;
		private int Honor;
		private int Likes;
		private int Reviews;
		private int IsPlayed;
		private int Liked;
		private int VIPDays;
		private int Point;
		private int VideoWidth;
		private int VideoHeight;

		public int getTopicId() {
			return TopicId;
		}

		public void setTopicId(int TopicId) {
			this.TopicId = TopicId;
		}

		public int getUserId() {
			return UserId;
		}

		public void setUserId(int UserId) {
			this.UserId = UserId;
		}

		public String getTitle() {
			return Title;
		}

		public void setTitle(String Title) {
			this.Title = Title;
		}

		public String getAvatar() {
			return Avatar;
		}

		public void setAvatar(String Avatar) {
			this.Avatar = Avatar;
		}

		public String getNick() {
			return Nick;
		}

		public void setNick(String Nick) {
			this.Nick = Nick;
		}

		public String getVideoPic() {
			return VideoPic;
		}

		public void setVideoPic(String VideoPic) {
			this.VideoPic = VideoPic;
		}

		public String getVideoUrl() {
			return VideoUrl;
		}

		public void setVideoUrl(String VideoUrl) {
			this.VideoUrl = VideoUrl;
		}

		public int getTimeLen() {
			return TimeLen;
		}

		public void setTimeLen(int TimeLen) {
			this.TimeLen = TimeLen;
		}

		public int getVideos() {
			return Videos;
		}

		public void setVideos(int Videos) {
			this.Videos = Videos;
		}

		public int getSold() {
			return Sold;
		}

		public void setSold(int Sold) {
			this.Sold = Sold;
		}

		public int getHonor() {
			return Honor;
		}

		public void setHonor(int Honor) {
			this.Honor = Honor;
		}

		public int getLikes() {
			return Likes;
		}

		public void setLikes(int Likes) {
			this.Likes = Likes;
		}

		public int getReviews() {
			return Reviews;
		}

		public void setReviews(int Reviews) {
			this.Reviews = Reviews;
		}

		public int getIsPlayed() {
			return IsPlayed;
		}

		public void setIsPlayed(int IsPlayed) {
			this.IsPlayed = IsPlayed;
		}

		public int getLiked() {
			return Liked;
		}

		public void setLiked(int Liked) {
			this.Liked = Liked;
		}

		public int getVIPDays() {
			return VIPDays;
		}

		public void setVIPDays(int VIPDays) {
			this.VIPDays = VIPDays;
		}

		public int getPoint() {
			return Point;
		}

		public void setPoint(int Point) {
			this.Point = Point;
		}

		public int getVideoWidth() {
			return VideoWidth;
		}

		public void setVideoWidth(int VideoWidth) {
			this.VideoWidth = VideoWidth;
		}

		public int getVideoHeight() {
			return VideoHeight;
		}

		public void setVideoHeight(int VideoHeight) {
			this.VideoHeight = VideoHeight;
		}
	}
}
