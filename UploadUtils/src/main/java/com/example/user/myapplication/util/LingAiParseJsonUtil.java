package com.example.user.myapplication.util;

import android.content.Context;
import android.widget.TextView;

/**
 * 作者：wangyb
 * 时间：2016/9/1 17:13
 * 描述：
 */
public class LingAiParseJsonUtil {
	private static LingAiParseJsonUtil mInstance;
	private static Context mContext;

	private LingAiParseJsonUtil(){}

	public static LingAiParseJsonUtil getInstance(Context context) {
		if (mInstance == null) {
			synchronized(LingAiParseJsonUtil.class) {
				if (mInstance == null) {
					mInstance = new LingAiParseJsonUtil();
					mContext = context;
				}
			}
		}
		return mInstance;
	}

	public void parse() {
		LingAiParseAndAssembleUtils.getInstance(mContext).parseJson(json);
	}

	public static String json = "{\n" +
			"\t\"code\": 1,\n" +
			"\t\"msg\": [{\n" +
			"\t\t\"time\": 1478249487.12,\n" +
			"\t\t\"contact\": 128,\n" +
			"\t\t\"commnum\": \"1\",\n" +
			"\t\t\"type\": 16,\n" +
			"\t\t\"brid\": \"2278178\",\n" +
			"\t\t\"user\": {\n" +
			"\t\t\t\"sex\": 1,\n" +
			"\t\t\t\"isdefault\": 0,\n" +
			"\t\t\t\"prov\": \"天津\",\n" +
			"\t\t\t\"height\": 167,\n" +
			"\t\t\t\"age\": 25,\n" +
			"\t\t\t\"city\": \"和平\",\n" +
			"\t\t\t\"photo\": \"http://photo.025.com/photoserver/photo/2016/10/27/3afe38c0fab25ab4133a20305000500p_b.jpg\",\n" +
			"\t\t\t\"star\": \"3\",\n" +
			"\t\t\t\"uid\": 7268555,\n" +
			"\t\t\t\"marry\": \"未婚\",\n" +
			"\t\t\t\"nick\": \"欧尼酱\",\n" +
			"\t\t\t\"level\": 11,\n" +
			"\t\t\t\"sphoto\": \"http://photo.025.com/photoserver/photo/2016/10/27/3afe38c0fab25ab4133a20305000500p.jpg\",\n" +
			"\t\t\t\"contact\": 128,\n" +
			"\t\t\t\"pur\": 0\n" +
			"\t\t},\n" +
			"\t\t\"icon\": 128,\n" +
			"\t\t\"contype\": 0,\n" +
			"\t\t\"id\": 2278178,\n" +
			"\t\t\"qiu\": 0,\n" +
			"\t\t\"deviceinfo\": \"iPhone6s\",\n" +
			"\t\t\"laudnum\": 15,\n" +
			"\t\t\"devicetype\": 1,\n" +
			"\t\t\"photo\": [{\n" +
			"\t\t\t\"pid\": \"3d55ea35016b4f4084bf11165d0bc666\",\n" +
			"\t\t\t\"small\": \"photo/2016/11/4/4b60e713911d8596133a25002ee02eep_4.jpg\",\n" +
			"\t\t\t\"lovenum\": 5,\n" +
			"\t\t\t\"host\": \"http://photo.025.com/photoserver/\",\n" +
			"\t\t\t\"ratio\": 1,\n" +
			"\t\t\t\"big\": \"photo/2016/11/4/4b60e713911d8596133a25002ee02eep_b.jpg\"\n" +
			"\t\t}]\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"time\": 1478070889.537,\n" +
			"\t\t\"contact\": 128,\n" +
			"\t\t\"commnum\": \"1\",\n" +
			"\t\t\"type\": 16,\n" +
			"\t\t\"brid\": \"2277568\",\n" +
			"\t\t\"user\": {\n" +
			"\t\t\t\"sex\": 1,\n" +
			"\t\t\t\"isdefault\": 0,\n" +
			"\t\t\t\"prov\": \"天津\",\n" +
			"\t\t\t\"height\": 167,\n" +
			"\t\t\t\"age\": 25,\n" +
			"\t\t\t\"city\": \"和平\",\n" +
			"\t\t\t\"photo\": \"http://photo.025.com/photoserver/photo/2016/10/27/3afe38c0fab25ab4133a20305000500p_b.jpg\",\n" +
			"\t\t\t\"star\": \"3\",\n" +
			"\t\t\t\"uid\": 7268555,\n" +
			"\t\t\t\"marry\": \"未婚\",\n" +
			"\t\t\t\"nick\": \"欧尼酱\",\n" +
			"\t\t\t\"level\": 11,\n" +
			"\t\t\t\"sphoto\": \"http://photo.025.com/photoserver/photo/2016/10/27/3afe38c0fab25ab4133a20305000500p.jpg\",\n" +
			"\t\t\t\"contact\": 128,\n" +
			"\t\t\t\"pur\": 0\n" +
			"\t\t},\n" +
			"\t\t\"icon\": 128,\n" +
			"\t\t\"contype\": 0,\n" +
			"\t\t\"id\": 2277568,\n" +
			"\t\t\"qiu\": 0,\n" +
			"\t\t\"deviceinfo\": \"iPhone6s\",\n" +
			"\t\t\"laudnum\": 10,\n" +
			"\t\t\"devicetype\": 1,\n" +
			"\t\t\"photo\": [{\n" +
			"\t\t\t\"pid\": \"b6dceef0b94948acb12b91de456d8e05\",\n" +
			"\t\t\t\"small\": \"photo/2016/11/2/1c61d1db3b27d803133a24e02ee02eep_4.jpg\",\n" +
			"\t\t\t\"lovenum\": 2,\n" +
			"\t\t\t\"host\": \"http://photo.025.com/photoserver/\",\n" +
			"\t\t\t\"ratio\": 1,\n" +
			"\t\t\t\"big\": \"photo/2016/11/2/1c61d1db3b27d803133a24e02ee02eep_b.jpg\"\n" +
			"\t\t}]\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"time\": 1477988470.69,\n" +
			"\t\t\"commnum\": \"4\",\n" +
			"\t\t\"contact\": 128,\n" +
			"\t\t\"brcon\": \"working\",\n" +
			"\t\t\"type\": 16,\n" +
			"\t\t\"brid\": \"2277380\",\n" +
			"\t\t\"user\": {\n" +
			"\t\t\t\"sex\": 1,\n" +
			"\t\t\t\"isdefault\": 0,\n" +
			"\t\t\t\"prov\": \"天津\",\n" +
			"\t\t\t\"height\": 167,\n" +
			"\t\t\t\"age\": 25,\n" +
			"\t\t\t\"city\": \"和平\",\n" +
			"\t\t\t\"photo\": \"http://photo.025.com/photoserver/photo/2016/10/27/3afe38c0fab25ab4133a20305000500p_b.jpg\",\n" +
			"\t\t\t\"star\": \"3\",\n" +
			"\t\t\t\"uid\": 7268555,\n" +
			"\t\t\t\"marry\": \"未婚\",\n" +
			"\t\t\t\"nick\": \"欧尼酱\",\n" +
			"\t\t\t\"level\": 11,\n" +
			"\t\t\t\"sphoto\": \"http://photo.025.com/photoserver/photo/2016/10/27/3afe38c0fab25ab4133a20305000500p.jpg\",\n" +
			"\t\t\t\"contact\": 128,\n" +
			"\t\t\t\"pur\": 0\n" +
			"\t\t},\n" +
			"\t\t\"icon\": 128,\n" +
			"\t\t\"contype\": 0,\n" +
			"\t\t\"id\": 2277380,\n" +
			"\t\t\"qiu\": 0,\n" +
			"\t\t\"deviceinfo\": \"iPhone6s\",\n" +
			"\t\t\"laudnum\": 14,\n" +
			"\t\t\"devicetype\": 1,\n" +
			"\t\t\"photo\": [{\n" +
			"\t\t\t\"pid\": \"4892493e53014f2086d679daf7733c1a\",\n" +
			"\t\t\t\"small\": \"photo/2016/11/1/daab00fc0bcfda12133a24d02ee02eep_m.jpg\",\n" +
			"\t\t\t\"lovenum\": 2,\n" +
			"\t\t\t\"host\": \"http://photo.025.com/photoserver/\",\n" +
			"\t\t\t\"ratio\": 1,\n" +
			"\t\t\t\"big\": \"photo/2016/11/1/daab00fc0bcfda12133a24d02ee02eep_b.jpg\"\n" +
			"\t\t},\n" +
			"\t\t{\n" +
			"\t\t\t\"pid\": \"599f02de4d27489ab02af7aa70353054\",\n" +
			"\t\t\t\"small\": \"photo/2016/11/1/9c6ca7b336a98f74133a24d02ee02eep_m.jpg\",\n" +
			"\t\t\t\"lovenum\": 1,\n" +
			"\t\t\t\"host\": \"http://photo.025.com/photoserver/\",\n" +
			"\t\t\t\"ratio\": 1,\n" +
			"\t\t\t\"big\": \"photo/2016/11/1/9c6ca7b336a98f74133a24d02ee02eep_b.jpg\"\n" +
			"\t\t}]\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"time\": 1477539253.377,\n" +
			"\t\t\"commnum\": \"4\",\n" +
			"\t\t\"contact\": 128,\n" +
			"\t\t\"brcon\": \"每一段感情的开始因为愉悦，每一段感情的结束因为难过。\",\n" +
			"\t\t\"type\": 16,\n" +
			"\t\t\"brid\": \"2276215\",\n" +
			"\t\t\"user\": {\n" +
			"\t\t\t\"sex\": 1,\n" +
			"\t\t\t\"isdefault\": 0,\n" +
			"\t\t\t\"prov\": \"天津\",\n" +
			"\t\t\t\"height\": 167,\n" +
			"\t\t\t\"age\": 25,\n" +
			"\t\t\t\"city\": \"和平\",\n" +
			"\t\t\t\"photo\": \"http://photo.025.com/photoserver/photo/2016/10/27/3afe38c0fab25ab4133a20305000500p_b.jpg\",\n" +
			"\t\t\t\"star\": \"3\",\n" +
			"\t\t\t\"uid\": 7268555,\n" +
			"\t\t\t\"marry\": \"未婚\",\n" +
			"\t\t\t\"nick\": \"欧尼酱\",\n" +
			"\t\t\t\"level\": 11,\n" +
			"\t\t\t\"sphoto\": \"http://photo.025.com/photoserver/photo/2016/10/27/3afe38c0fab25ab4133a20305000500p.jpg\",\n" +
			"\t\t\t\"contact\": 128,\n" +
			"\t\t\t\"pur\": 0\n" +
			"\t\t},\n" +
			"\t\t\"icon\": 128,\n" +
			"\t\t\"contype\": 0,\n" +
			"\t\t\"id\": 2276215,\n" +
			"\t\t\"qiu\": 1,\n" +
			"\t\t\"deviceinfo\": \"iPhone6s\",\n" +
			"\t\t\"laudnum\": 23,\n" +
			"\t\t\"devicetype\": 1,\n" +
			"\t\t\"photo\": [{\n" +
			"\t\t\t\"pid\": \"c8a422af77b6438186865ae09fbc4b6a\",\n" +
			"\t\t\t\"small\": \"photo/2016/10/27/c13a2d507a36ed6a133a20302ee02eep_4.jpg\",\n" +
			"\t\t\t\"lovenum\": 5,\n" +
			"\t\t\t\"host\": \"http://photo.025.com/photoserver/\",\n" +
			"\t\t\t\"ratio\": 1,\n" +
			"\t\t\t\"big\": \"photo/2016/10/27/c13a2d507a36ed6a133a20302ee02eep_b.jpg\"\n" +
			"\t\t}]\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"time\": 1477537245.59,\n" +
			"\t\t\"commnum\": \"4\",\n" +
			"\t\t\"contact\": 128,\n" +
			"\t\t\"brcon\": \"http://photo.025.com/photoserver/photo/2016/10/27/3afe38c0fab25ab4133a20305000500p_b.jpg\",\n" +
			"\t\t\"type\": 18,\n" +
			"\t\t\"brid\": \"2276203\",\n" +
			"\t\t\"user\": {\n" +
			"\t\t\t\"sex\": 1,\n" +
			"\t\t\t\"isdefault\": 0,\n" +
			"\t\t\t\"prov\": \"天津\",\n" +
			"\t\t\t\"height\": 167,\n" +
			"\t\t\t\"age\": 25,\n" +
			"\t\t\t\"city\": \"和平\",\n" +
			"\t\t\t\"photo\": \"http://photo.025.com/photoserver/photo/2016/10/27/3afe38c0fab25ab4133a20305000500p_b.jpg\",\n" +
			"\t\t\t\"star\": \"3\",\n" +
			"\t\t\t\"uid\": 7268555,\n" +
			"\t\t\t\"marry\": \"未婚\",\n" +
			"\t\t\t\"nick\": \"欧尼酱\",\n" +
			"\t\t\t\"level\": 11,\n" +
			"\t\t\t\"sphoto\": \"http://photo.025.com/photoserver/photo/2016/10/27/3afe38c0fab25ab4133a20305000500p.jpg\",\n" +
			"\t\t\t\"contact\": 128,\n" +
			"\t\t\t\"pur\": 0\n" +
			"\t\t},\n" +
			"\t\t\"icon\": 128,\n" +
			"\t\t\"contype\": 0,\n" +
			"\t\t\"id\": 2276203,\n" +
			"\t\t\"qiu\": 0,\n" +
			"\t\t\"laudnum\": 15,\n" +
			"\t\t\"photo\": [{\n" +
			"\t\t\t\"pid\": \"120c240c61484220a5f43b3817cfb1fc\",\n" +
			"\t\t\t\"small\": \"photo/2016/10/27/3afe38c0fab25ab4133a20305000500p_b.jpg\",\n" +
			"\t\t\t\"lovenum\": 4,\n" +
			"\t\t\t\"host\": \"http://photo.025.com/photoserver/\",\n" +
			"\t\t\t\"ratio\": 1,\n" +
			"\t\t\t\"big\": \"photo/2016/10/27/3afe38c0fab25ab4133a20305000500p_b.jpg\"\n" +
			"\t\t}]\n" +
			"\t}],\n" +
			"\t\"extend\": 12\n" +
			"}";
}
