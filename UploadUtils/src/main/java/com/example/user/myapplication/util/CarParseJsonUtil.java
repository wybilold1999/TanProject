package com.example.user.myapplication.util;

import android.content.Context;

/**
 * 作者：wangyb
 * 时间：2016/9/1 17:13
 * 描述：
 */
public class CarParseJsonUtil {
	private static CarParseJsonUtil mInstance;
	private static Context mContext;

	private CarParseJsonUtil(){}

	public static CarParseJsonUtil getInstance(Context context) {
		if (mInstance == null) {
			synchronized(CarParseJsonUtil.class) {
				if (mInstance == null) {
					mInstance = new CarParseJsonUtil();
					mContext = context;
				}
			}
		}
		return mInstance;
	}

	public void parse() {
		CarParseAndAssembleUtils.getInstance(mContext).parseJson(json);
	}

	public static String json = "{\n" +
			"\t\"code\": 1,\n" +
			"\t\"ext\": \"\",\n" +
			"\t\"des\": \"\",\n" +
			"\t\"result\": [{\n" +
			"\t\t\"TopicID\": 507785,\n" +
			"\t\t\"PostTime\": \"2016-10-15 22:00:23\",\n" +
			"\t\t\"SupportCount\": 533,\n" +
			"\t\t\"CollectCount\": 2,\n" +
			"\t\t\"ReplyCount\": 0,\n" +
			"\t\t\"GiftCount\": 1,\n" +
			"\t\t\"VID\": 0,\n" +
			"\t\t\"Summary\": \"好无聊，怎么办[害羞][害羞][害羞]\",\n" +
			"\t\t\"UserID\": 1092975,\n" +
			"\t\t\"UserVip\": 0,\n" +
			"\t\t\"Sex\": 2,\n" +
			"\t\t\"Age\": 25,\n" +
			"\t\t\"UserIDNice\": \"\",\n" +
			"\t\t\"NickName\": \"加油_428832\",\n" +
			"\t\t\"AudioFile\": \"\",\n" +
			"\t\t\"AudioSec\": 0,\n" +
			"\t\t\"VedioFile\": \"\",\n" +
			"\t\t\"VedioSize\": 0,\n" +
			"\t\t\"SongName\": \"null\",\n" +
			"\t\t\"ArtistName\": \"null\",\n" +
			"\t\t\"SongUrl\": \"\",\n" +
			"\t\t\"SongTime\": 0,\n" +
			"\t\t\"LevelCity\": \"深圳\",\n" +
			"\t\t\"CarModel\": \"\",\n" +
			"\t\t\"Face\": \"http://img1.chexr.cc/pa/xx_business/201610/11/16101102581200294663.jpg\",\n" +
			"\t\t\"isCollect\": 0,\n" +
			"\t\t\"ClientName\": \"安卓\",\n" +
			"\t\t\"ClientID\": \"2\",\n" +
			"\t\t\"ReplyNick\": [\"叫兽（绝版）\",\n" +
			"\t\t\"大叔DaShu\\uD83D\\uDC95\"],\n" +
			"\t\t\"ReplyContent\": [\" \\u2764\",\n" +
			"\t\t\" 送了1朵玫瑰\"],\n" +
			"\t\t\"Pics\": [\"http://img1.chexr.cc/pa/t_business/201610/15/16101510002349658253.jpg\"],\n" +
			"\t\t\"ActivityID\": 0\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"TopicID\": 503578,\n" +
			"\t\t\"PostTime\": \"2016-10-4 21:41:33\",\n" +
			"\t\t\"SupportCount\": 794,\n" +
			"\t\t\"CollectCount\": 22,\n" +
			"\t\t\"ReplyCount\": 16,\n" +
			"\t\t\"GiftCount\": 5,\n" +
			"\t\t\"VID\": 0,\n" +
			"\t\t\"Summary\": \"上班了，有没有人过来看我[尴尬][尴尬][尴尬]\",\n" +
			"\t\t\"UserID\": 1092975,\n" +
			"\t\t\"UserVip\": 0,\n" +
			"\t\t\"Sex\": 2,\n" +
			"\t\t\"Age\": 25,\n" +
			"\t\t\"UserIDNice\": \"\",\n" +
			"\t\t\"NickName\": \"加油_428832\",\n" +
			"\t\t\"AudioFile\": \"\",\n" +
			"\t\t\"AudioSec\": 0,\n" +
			"\t\t\"VedioFile\": \"\",\n" +
			"\t\t\"VedioSize\": 0,\n" +
			"\t\t\"SongName\": \"null\",\n" +
			"\t\t\"ArtistName\": \"null\",\n" +
			"\t\t\"SongUrl\": \"\",\n" +
			"\t\t\"SongTime\": 0,\n" +
			"\t\t\"LevelCity\": \"深圳\",\n" +
			"\t\t\"CarModel\": \"\",\n" +
			"\t\t\"Face\": \"http://img1.chexr.cc/pa/xx_business/201610/11/16101102581200294663.jpg\",\n" +
			"\t\t\"isCollect\": 0,\n" +
			"\t\t\"ClientName\": \"安卓\",\n" +
			"\t\t\"ClientID\": \"2\",\n" +
			"\t\t\"ReplyNick\": [\"Ls飞\",\n" +
			"\t\t\"加油[_]428832\",\n" +
			"\t\t\"加油[_]428832\"],\n" +
			"\t\t\"ReplyContent\": [\" 18317537100微信\",\n" +
			"\t\t\" 我们加微信聊\",\n" +
			"\t\t\" 福永\"],\n" +
			"\t\t\"Pics\": [\"http://img1.chexr.cc/pa/t_business/201610/04/16100409413387398294.jpg\",\n" +
			"\t\t\"http://img1.chexr.cc/pa/t_business/201610/04/16100409413395216074.jpg\"],\n" +
			"\t\t\"ActivityID\": 0\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"TopicID\": 463086,\n" +
			"\t\t\"PostTime\": \"2016-5-28 13:23:42\",\n" +
			"\t\t\"SupportCount\": 1128,\n" +
			"\t\t\"CollectCount\": 3,\n" +
			"\t\t\"ReplyCount\": 0,\n" +
			"\t\t\"GiftCount\": 0,\n" +
			"\t\t\"VID\": 0,\n" +
			"\t\t\"Summary\": \"好久没玩了，朋友们吧我忘了吧\",\n" +
			"\t\t\"UserID\": 1092975,\n" +
			"\t\t\"UserVip\": 0,\n" +
			"\t\t\"Sex\": 2,\n" +
			"\t\t\"Age\": 25,\n" +
			"\t\t\"UserIDNice\": \"\",\n" +
			"\t\t\"NickName\": \"加油_428832\",\n" +
			"\t\t\"AudioFile\": \"\",\n" +
			"\t\t\"AudioSec\": 0,\n" +
			"\t\t\"VedioFile\": \"\",\n" +
			"\t\t\"VedioSize\": 0,\n" +
			"\t\t\"SongName\": \"null\",\n" +
			"\t\t\"ArtistName\": \"null\",\n" +
			"\t\t\"SongUrl\": \"\",\n" +
			"\t\t\"SongTime\": 0,\n" +
			"\t\t\"LevelCity\": \"深圳\",\n" +
			"\t\t\"CarModel\": \"\",\n" +
			"\t\t\"Face\": \"http://img1.chexr.cc/pa/xx_business/201610/11/16101102581200294663.jpg\",\n" +
			"\t\t\"isCollect\": 0,\n" +
			"\t\t\"ClientName\": \"安卓\",\n" +
			"\t\t\"ClientID\": \"2\",\n" +
			"\t\t\"ReplyNick\": [\"加油[_]428832\",\n" +
			"\t\t\"奇奇8189\",\n" +
			"\t\t\"原来名字还可以打这么长呢我看能打几个字\"],\n" +
			"\t\t\"ReplyContent\": [\" \\u2764\",\n" +
			"\t\t\" \\u2764\",\n" +
			"\t\t\" \\u2764\"],\n" +
			"\t\t\"Pics\": [\"http://img1.chexr.cc/pa/t_business/201605/28/16052801234202522634.jpg\"],\n" +
			"\t\t\"ActivityID\": 0\n" +
			"\t}]\n" +
			"}";
}
