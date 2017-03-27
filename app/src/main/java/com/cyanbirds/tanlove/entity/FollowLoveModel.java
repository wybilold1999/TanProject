package com.cyanbirds.tanlove.entity;

import java.io.Serializable;
import java.util.List;

public class FollowLoveModel implements Serializable{
	/**
	 * 关注我的人数
	 */
	public int followCount;
	/**
	 * 喜欢我的人数
	 */
	public int loveCount;
	/**
	 * 关注我前三个人的头像
	 */
	public List<FollowModel> followModels;
	/**
	 * 喜欢我前三个人的头像
	 */
	public List<LoveModel> loveModels;

}
