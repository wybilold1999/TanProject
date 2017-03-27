package com.example.user.myapplication.model;

import java.util.List;

/**
 * 作者：wangyb
 * 时间：2016/9/1 17:08
 * 描述：
 */
public class UserAndDynamic {

	/**
	 * time : 1.472717337793E9
	 * commnum : 0
	 * contact : 128
	 * brcon : 还是自己试过才知看上好看也不一定适合自己
	 * type : 16
	 * brid : 2264225
	 * user : {"sex":1,"isdefault":0,"prov":"福建","height":153,"age":23,"city":"泉州","photo":"http://photo.025.com/photoserver/photo/2016/8/4/e44f4fea9189efcd133a12402800280p_b.jpg","star":"3","uid":7217411,"marry":"未婚","nick":"暮然回首","level":11,"sphoto":"http://photo.025.com/photoserver/photo/2016/8/4/e44f4fea9189efcd133a12402800280p.jpg","contact":128,"pur":0}
	 * icon : 128
	 * contype : 0
	 * id : 2264225
	 * qiu : 0
	 * deviceinfo : iPad mini
	 * laudnum : 1
	 * devicetype : 2
	 * photo : [{"pid":"971be44ee797437389f72a6407c17914","small":"photo/2016/9/1/a874b4676979aacf133a18501ac0280p_4.jpg","lovenum":0,"host":"http://photo.025.com/photoserver/","ratio":0.669,"big":"photo/2016/9/1/a874b4676979aacf133a18501ac0280p_b.jpg"}]
	 */

	private List<MsgBean> msg;

	public List<MsgBean> getMsg() {
		return msg;
	}

	public void setMsg(List<MsgBean> msg) {
		this.msg = msg;
	}

	public static class MsgBean {
		private String brcon;
		private String brid;
		public String time;
		/**
		 * sex : 1
		 * isdefault : 0
		 * prov : 福建
		 * height : 153
		 * age : 23
		 * city : 泉州
		 * photo : http://photo.025.com/photoserver/photo/2016/8/4/e44f4fea9189efcd133a12402800280p_b.jpg
		 * star : 3
		 * uid : 7217411
		 * marry : 未婚
		 * nick : 暮然回首
		 * level : 11
		 * sphoto : http://photo.025.com/photoserver/photo/2016/8/4/e44f4fea9189efcd133a12402800280p.jpg
		 * contact : 128
		 * pur : 0
		 */

		private UserBean user;
		/**
		 * pid : 971be44ee797437389f72a6407c17914
		 * small : photo/2016/9/1/a874b4676979aacf133a18501ac0280p_4.jpg
		 * lovenum : 0
		 * host : http://photo.025.com/photoserver/
		 * ratio : 0.669
		 * big : photo/2016/9/1/a874b4676979aacf133a18501ac0280p_b.jpg
		 */

		private List<PhotoBean> photo;

		public String getBrcon() {
			return brcon;
		}

		public void setBrcon(String brcon) {
			this.brcon = brcon;
		}

		public String getBrid() {
			return brid;
		}

		public void setBrid(String brid) {
			this.brid = brid;
		}

		public UserBean getUser() {
			return user;
		}

		public void setUser(UserBean user) {
			this.user = user;
		}

		public List<PhotoBean> getPhoto() {
			return photo;
		}

		public void setPhoto(List<PhotoBean> photo) {
			this.photo = photo;
		}

		public static class UserBean {
			private int sex;
			private String prov;
			private int height;
			private int age;
			private String city;
			private String photo;
			private int uid;
			private String marry;
			private String nick;
			private String sphoto;
			public String solioquize;

			public String faceUrl;
			public double distance;
			public Integer UScopeType;
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

			public int getSex() {
				return sex;
			}

			public void setSex(int sex) {
				this.sex = sex;
			}

			public String getProv() {
				return prov;
			}

			public void setProv(String prov) {
				this.prov = prov;
			}

			public int getHeight() {
				return height;
			}

			public void setHeight(int height) {
				this.height = height;
			}

			public int getAge() {
				return age;
			}

			public void setAge(int age) {
				this.age = age;
			}

			public String getCity() {
				return city;
			}

			public void setCity(String city) {
				this.city = city;
			}

			public String getPhoto() {
				return photo;
			}

			public void setPhoto(String photo) {
				this.photo = photo;
			}

			public int getUid() {
				return uid;
			}

			public void setUid(int uid) {
				this.uid = uid;
			}

			public String getMarry() {
				return marry;
			}

			public void setMarry(String marry) {
				this.marry = marry;
			}

			public String getNick() {
				return nick;
			}

			public void setNick(String nick) {
				this.nick = nick;
			}

			public String getSphoto() {
				return sphoto;
			}

			public void setSphoto(String sphoto) {
				this.sphoto = sphoto;
			}
		}

		public static class PhotoBean {
			private String small;
			private String host;
			private String big;

			public String getSmall() {
				return small;
			}

			public void setSmall(String small) {
				this.small = small;
			}

			public String getHost() {
				return host;
			}

			public void setHost(String host) {
				this.host = host;
			}

			public String getBig() {
				return big;
			}

			public void setBig(String big) {
				this.big = big;
			}
		}
	}
}
