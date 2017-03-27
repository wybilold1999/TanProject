package com.example.user.myapplication.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：wangyb
 * 时间：2016/9/2 15:03
 * 描述：车星人的用户和动态
 */
public class UserAndDycCar {

	/**
	 * TopicID : 493166
	 * PostTime : 2016-8-31 13:19:06
	 * SupportCount : 2423
	 * CollectCount : 16
	 * ReplyCount : 1
	 * GiftCount : 8
	 * VID : 1
	 * Summary : 生活并不完美，我依然快乐。虽然拥有过的东西会失去，得到过的友谊会离开，想追求的感情还那么遥远，但是，我懂，我懂我身边的一切事与物，我会珍惜。[愉快]
	 * UserID : 1045028
	 * UserVip : 1
	 * Sex : 2
	 * Age : 28
	 * UserIDNice :
	 * NickName : miss😋英👄
	 * AudioFile :
	 * AudioSec : 0
	 * VedioFile :
	 * VedioSize : 0
	 * SongName :
	 * ArtistName :
	 * SongUrl :
	 * SongTime : 0
	 * LevelCity : 嘉兴
	 * CarModel : 现代 索纳塔(进口)
	 * Face : http://img1.chexr.cc/pa/xx_business/201511/14/15111410391113973191.jpg
	 * isCollect : 0
	 * ClientName : iPhone 6S
	 * ClientID : 1
	 * ReplyNick : ["小豆豆[_]894958","Train","Train"]
	 * ReplyContent : [" ❤"," ❤"," 送了1朵玫瑰"]
	 * Pics : ["http://img1.chexr.cc/pa/t_business/201608/31/16083101190650326579.jpg"]
	 * ActivityID : 0
	 */

	private List<ResultBean> result;

	public List<ResultBean> getResult() {
		return result;
	}

	public void setResult(List<ResultBean> result) {
		this.result = result;
	}

	public static class ResultBean {
		public String TopicID;
		public String PostTime;
		private String Summary;
		private int UserID;
		private int Sex;
		private int Age;
		private String NickName;
		private String LevelCity;
		private String Face;
		private List<String> Pics;

		public String faceUrl;
		public double distance;
		public Integer UScopeType;
		public int height;
		public String marry;
		public int isVip;
		public int weight;
		public String sexPart;
		public String plable;
		public String intrest;
		public String constellation;
		public String occupation;
		public String education;
		public String purpose; //交友目的
		public String where; //喜欢爱爱地点
		public String doWhatFirst; //首次见面希望
		public String conception; //恋爱观念
		public String userPersonalTag = "愿得一人心，白首不分离";
		public ArrayList<String> picdatas = new ArrayList<>();

		public String getSummary() {
			return Summary;
		}

		public void setSummary(String Summary) {
			this.Summary = Summary;
		}

		public int getUserID() {
			return UserID;
		}

		public void setUserID(int UserID) {
			this.UserID = UserID;
		}

		public int getSex() {
			return Sex;
		}

		public void setSex(int Sex) {
			this.Sex = Sex;
		}

		public int getAge() {
			return Age;
		}

		public void setAge(int Age) {
			this.Age = Age;
		}

		public String getNickName() {
			return NickName;
		}

		public void setNickName(String NickName) {
			this.NickName = NickName;
		}

		public String getLevelCity() {
			return LevelCity;
		}

		public void setLevelCity(String LevelCity) {
			this.LevelCity = LevelCity;
		}

		public String getFace() {
			return Face;
		}

		public void setFace(String Face) {
			this.Face = Face;
		}

		public List<String> getPics() {
			return Pics;
		}

		public void setPics(List<String> Pics) {
			this.Pics = Pics;
		}
	}
}
