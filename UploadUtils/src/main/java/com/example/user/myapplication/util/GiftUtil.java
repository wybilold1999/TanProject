package com.example.user.myapplication.util;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.myapplication.CSApplication;
import com.example.user.myapplication.ResultPostExecute;
import com.example.user.myapplication.model.Gift;
import com.example.user.myapplication.net.DownloadFileRequest;
import com.example.user.myapplication.oss.AppManager;
import com.example.user.myapplication.oss.Config;
import com.example.user.myapplication.oss.OSSFileUploadRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：wangyb
 * 时间：2016/11/26 10:28
 * 描述：
 */
public class GiftUtil {

	public static int count = 0;
	private static List<Gift> giftList;
	private static Gson gson = new Gson();
	public static void parseJsonAndUpload() {
		JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
		JsonArray array = obj.get("list").getAsJsonArray();
		Type listType = new TypeToken<ArrayList<Gift>>() {
		}.getType();
		giftList = gson.fromJson(array, listType);
		if (giftList != null) {
			for (Gift gift : giftList) {
				new DownloadImg(gift).request(gift.image_url,
						FileAccessorUtils.DEFAULT_PATH,
						Md5Util.md5(gift.dynamic_image_url) + ".jpg");
			}
		}
	}

	static class DownloadImg extends DownloadFileRequest {
		private Gift mGift;

		public DownloadImg(Gift gift) {
			mGift = gift;
		}

		@Override
		public void onPostExecute(String s) {
			new UploadImg(mGift).request(AppManager.getFederationToken().bucketName,
					AppManager.getOSSImgPath(), s);
		}

		@Override
		public void onErrorExecute(String error) {
			ToastUtil.showMessage(error);
		}
	}

	static class UploadImg extends OSSFileUploadRequest {
		private Gift mGift;

		public UploadImg(Gift gift) {
			mGift = gift;
		}

		@Override
		public void onPostExecute(String s) {
			mGift.dynamic_image_url = Config.imagePoint + s;
			count++;
			if (count == giftList.size()) {
				new GiftUploadRequest().request(gson.toJson(giftList));
			}
		}

		@Override
		public void onErrorExecute(String error) {
			ToastUtil.showMessage(error);
		}
	}
	static class GiftUploadRequest extends ResultPostExecute<String> {
		public void request(final String data) {
			try {
				StringRequest request = new StringRequest(Request.Method.POST,
						Config.UPLOAD_GIFT, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						parserData(response);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						onErrorExecute("");
					}
				}) {
					@Override
					protected Map<String, String> getParams() throws AuthFailureError {
						Map<String, String> params = new HashMap<String, String>();
						params.put("data", data);
						return params;
					}
				};
				CSApplication.getInstance().getRequestQueue().add(request);
			} catch (Exception e) {
				e.printStackTrace();
				onErrorExecute("");
			}
		}

		/**
		 * 解析数据
		 *
		 * @param result
		 */
		private void parserData(String result) {
			try {
				JsonObject obj = new JsonParser().parse(result).getAsJsonObject();
				int code = obj.get("code").getAsInt();
				if (code == 0) {//请求成功
					onPostExecute("");
				} else {
					String error = obj.get("data").getAsString();
					onErrorExecute(error);
				}
			} catch (Exception e) {
				onErrorExecute("");
			}
		}
	}

	public static String json = "{\n" +
			"\t\"error\": 0,\n" +
			"\t\"error_reason\": \"\",\n" +
			"\t\"total_pages\": 2,\n" +
			"\t\"current_page\": 1,\n" +
			"\t\"per_page\": 21,\n" +
			"\t\"total_entries\": 23,\n" +
			"\t\"domains\": [\"ucloud.365yf.com\",\n" +
			"\t\"img.365yf.com\"],\n" +
			"\t\"list\": [{\n" +
			"\t\t\"id\": 1,\n" +
			"\t\t\"name\": \"一只玫瑰\",\n" +
			"\t\t\"desc\": \"礼物\",\n" +
			"\t\t\"amount\": 0,\n" +
			"\t\t\"vip_amount\": 0,\n" +
			"\t\t\"product_channel_names\": \"\",\n" +
			"\t\t\"image_url\": \"http://img.365yf.com/gifts/1/7e22ff5234065cca8c07.jpg\",\n" +
			"\t\t\"big_image_url\": \"http://img.365yf.com/gifts/1/02b8e45dc332baaab243.jpg\",\n" +
			"\t\t\"dynamic_image_url\": \"http://img.365yf.com/gifts/1/a8d88029b6e3647da1db.jpg\",\n" +
			"\t\t\"rank\": 1,\n" +
			"\t\t\"status_text\": \"有效\"\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"id\": 18,\n" +
			"\t\t\"name\": \"99朵玫瑰\",\n" +
			"\t\t\"desc\": \"礼物\",\n" +
			"\t\t\"amount\": 20,\n" +
			"\t\t\"vip_amount\": 16,\n" +
			"\t\t\"product_channel_names\": \"\",\n" +
			"\t\t\"image_url\": \"http://img.365yf.com/gifts/18/fab93e340e4c67732e5b.jpg\",\n" +
			"\t\t\"big_image_url\": \"http://img.365yf.com/gifts/18/242cf5adff878373268a.jpg\",\n" +
			"\t\t\"dynamic_image_url\": \"http://img.365yf.com/gifts/18/8a7fb4658e50b9b5afb8.jpg\",\n" +
			"\t\t\"rank\": 2,\n" +
			"\t\t\"status_text\": \"有效\"\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"id\": 15,\n" +
			"\t\t\"name\": \"999朵玫瑰\",\n" +
			"\t\t\"desc\": \"礼物\",\n" +
			"\t\t\"amount\": 30,\n" +
			"\t\t\"vip_amount\": 24,\n" +
			"\t\t\"product_channel_names\": \"\",\n" +
			"\t\t\"image_url\": \"http://img.365yf.com/gifts/15/7a16a6401eb6f9d4aa2a.jpg\",\n" +
			"\t\t\"big_image_url\": \"http://img.365yf.com/gifts/15/7f1f93575a2a1aab0a89.jpg\",\n" +
			"\t\t\"dynamic_image_url\": \"http://img.365yf.com/gifts/15/89f0d6414f4a55288447.jpg\",\n" +
			"\t\t\"rank\": 3,\n" +
			"\t\t\"status_text\": \"有效\"\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"id\": 2,\n" +
			"\t\t\"name\": \"可口可乐\",\n" +
			"\t\t\"desc\": \"礼物\",\n" +
			"\t\t\"amount\": 5,\n" +
			"\t\t\"vip_amount\": 0,\n" +
			"\t\t\"product_channel_names\": \"\",\n" +
			"\t\t\"image_url\": \"http://img.365yf.com/gifts/2/bc906f0dfd340ca98823.jpg\",\n" +
			"\t\t\"big_image_url\": \"http://img.365yf.com/gifts/2/6da0198628e07e180e50.jpg\",\n" +
			"\t\t\"dynamic_image_url\": \"http://img.365yf.com/gifts/2/6e7ff701ed3f6faf8d2d.jpg\",\n" +
			"\t\t\"rank\": 4,\n" +
			"\t\t\"status_text\": \"有效\"\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"id\": 49,\n" +
			"\t\t\"name\": \"樱桃冰淇淋\",\n" +
			"\t\t\"desc\": \"礼物\",\n" +
			"\t\t\"amount\": 10,\n" +
			"\t\t\"vip_amount\": 0,\n" +
			"\t\t\"product_channel_names\": \"\",\n" +
			"\t\t\"image_url\": \"http://img.365yf.com/gifts/49/2d27f8fa20ffd77d97aa.jpg\",\n" +
			"\t\t\"big_image_url\": \"http://img.365yf.com/gifts/49/53fec876d56e96bc869b.jpg\",\n" +
			"\t\t\"dynamic_image_url\": \"http://img.365yf.com/gifts/49/75641b14899d4a874df6.jpg\",\n" +
			"\t\t\"rank\": 4,\n" +
			"\t\t\"status_text\": \"有效\"\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"id\": 50,\n" +
			"\t\t\"name\": \"热狗\",\n" +
			"\t\t\"desc\": \"礼物\",\n" +
			"\t\t\"amount\": 15,\n" +
			"\t\t\"vip_amount\": 0,\n" +
			"\t\t\"product_channel_names\": \"\",\n" +
			"\t\t\"image_url\": \"http://img.365yf.com/gifts/50/9d29f0db2d1f45a22471.jpg\",\n" +
			"\t\t\"big_image_url\": \"http://img.365yf.com/gifts/50/7086b1c73c64eeda6482.jpg\",\n" +
			"\t\t\"dynamic_image_url\": \"http://img.365yf.com/gifts/50/3d623aaf0950fb5bfe88.jpg\",\n" +
			"\t\t\"rank\": 6,\n" +
			"\t\t\"status_text\": \"有效\"\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"id\": 75,\n" +
			"\t\t\"name\": \"日本寿司\",\n" +
			"\t\t\"desc\": \"礼物\",\n" +
			"\t\t\"amount\": 15,\n" +
			"\t\t\"vip_amount\": 0,\n" +
			"\t\t\"product_channel_names\": \"\",\n" +
			"\t\t\"image_url\": \"http://img.365yf.com/gifts/75/c1a16cc0a05d14daa7dc.jpg\",\n" +
			"\t\t\"big_image_url\": \"http://img.365yf.com/gifts/75/2269dd1ba87f9238b067.jpg\",\n" +
			"\t\t\"dynamic_image_url\": \"http://img.365yf.com/gifts/75/ccfd12a0bf315ce71ea7.jpg\",\n" +
			"\t\t\"rank\": 6,\n" +
			"\t\t\"status_text\": \"有效\"\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"id\": 20,\n" +
			"\t\t\"name\": \"化妆盒\",\n" +
			"\t\t\"desc\": \"礼物\",\n" +
			"\t\t\"amount\": 25,\n" +
			"\t\t\"vip_amount\": 20,\n" +
			"\t\t\"product_channel_names\": \"\",\n" +
			"\t\t\"image_url\": \"http://img.365yf.com/gifts/20/1ac849da9846668345f6.jpg\",\n" +
			"\t\t\"big_image_url\": \"http://img.365yf.com/gifts/20/7bf8476470f06bf460c0.jpg\",\n" +
			"\t\t\"dynamic_image_url\": \"http://img.365yf.com/gifts/20/81c2e684d0a9fe9557ce.jpg\",\n" +
			"\t\t\"rank\": 7,\n" +
			"\t\t\"status_text\": \"有效\"\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"id\": 17,\n" +
			"\t\t\"name\": \"女士手表\",\n" +
			"\t\t\"desc\": \"礼物\",\n" +
			"\t\t\"amount\": 35,\n" +
			"\t\t\"vip_amount\": 30,\n" +
			"\t\t\"product_channel_names\": \"\",\n" +
			"\t\t\"image_url\": \"http://img.365yf.com/gifts/17/7d692d9e32a8f1c99198.jpg\",\n" +
			"\t\t\"big_image_url\": \"http://img.365yf.com/gifts/17/def8cc8ae420f9e5a1b7.jpg\",\n" +
			"\t\t\"dynamic_image_url\": \"http://img.365yf.com/gifts/17/1024a442bc03fcdd6622.jpg\",\n" +
			"\t\t\"rank\": 8,\n" +
			"\t\t\"status_text\": \"有效\"\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"id\": 51,\n" +
			"\t\t\"name\": \"时尚零钱包\",\n" +
			"\t\t\"desc\": \"礼物\",\n" +
			"\t\t\"amount\": 35,\n" +
			"\t\t\"vip_amount\": 30,\n" +
			"\t\t\"product_channel_names\": \"\",\n" +
			"\t\t\"image_url\": \"http://img.365yf.com/gifts/51/40f70471f1fe347e44e3.jpg\",\n" +
			"\t\t\"big_image_url\": \"http://img.365yf.com/gifts/51/e2e9289b4a7c117c5807.jpg\",\n" +
			"\t\t\"dynamic_image_url\": \"http://img.365yf.com/gifts/51/b881de5c7eb069549a79.jpg\",\n" +
			"\t\t\"rank\": 8,\n" +
			"\t\t\"status_text\": \"有效\"\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"id\": 19,\n" +
			"\t\t\"name\": \"高跟鞋\",\n" +
			"\t\t\"desc\": \"礼物\",\n" +
			"\t\t\"amount\": 40,\n" +
			"\t\t\"vip_amount\": 32,\n" +
			"\t\t\"product_channel_names\": \"\",\n" +
			"\t\t\"image_url\": \"http://img.365yf.com/gifts/19/ba166ef43c314e61d238.jpg\",\n" +
			"\t\t\"big_image_url\": \"http://img.365yf.com/gifts/19/d9af7d440a6de14de405.jpg\",\n" +
			"\t\t\"dynamic_image_url\": \"http://img.365yf.com/gifts/19/36883287916c0069f9a0.jpg\",\n" +
			"\t\t\"rank\": 9,\n" +
			"\t\t\"status_text\": \"有效\"\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"id\": 16,\n" +
			"\t\t\"name\": \"生日蛋糕\",\n" +
			"\t\t\"desc\": \"礼物\",\n" +
			"\t\t\"amount\": 40,\n" +
			"\t\t\"vip_amount\": 30,\n" +
			"\t\t\"product_channel_names\": \"\",\n" +
			"\t\t\"image_url\": \"http://img.365yf.com/gifts/16/05e2ed66b63cc90fd3e7.jpg\",\n" +
			"\t\t\"big_image_url\": \"http://img.365yf.com/gifts/16/aba0f90ae8c2ad005635.jpg\",\n" +
			"\t\t\"dynamic_image_url\": \"http://img.365yf.com/gifts/16/974aaa80c9bb84884160.jpg\",\n" +
			"\t\t\"rank\": 10,\n" +
			"\t\t\"status_text\": \"有效\"\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"id\": 14,\n" +
			"\t\t\"name\": \"裙子\",\n" +
			"\t\t\"desc\": \"礼物\",\n" +
			"\t\t\"amount\": 50,\n" +
			"\t\t\"vip_amount\": 40,\n" +
			"\t\t\"product_channel_names\": \"\",\n" +
			"\t\t\"image_url\": \"http://img.365yf.com/gifts/14/9246f7f634e7ba172669.jpg\",\n" +
			"\t\t\"big_image_url\": \"http://img.365yf.com/gifts/14/ecc835897abe97292f18.jpg\",\n" +
			"\t\t\"dynamic_image_url\": \"http://img.365yf.com/gifts/14/0582bfa0b796676aff51.jpg\",\n" +
			"\t\t\"rank\": 11,\n" +
			"\t\t\"status_text\": \"有效\"\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"id\": 13,\n" +
			"\t\t\"name\": \"口红\",\n" +
			"\t\t\"desc\": \"礼物\",\n" +
			"\t\t\"amount\": 60,\n" +
			"\t\t\"vip_amount\": 50,\n" +
			"\t\t\"product_channel_names\": \"\",\n" +
			"\t\t\"image_url\": \"http://img.365yf.com/gifts/13/852c0231e3d59c42b37a.jpg\",\n" +
			"\t\t\"big_image_url\": \"http://img.365yf.com/gifts/13/41a3b829d6730b7be828.jpg\",\n" +
			"\t\t\"dynamic_image_url\": \"http://img.365yf.com/gifts/13/f817c30ff2779a023fab.jpg\",\n" +
			"\t\t\"rank\": 12,\n" +
			"\t\t\"status_text\": \"有效\"\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"id\": 65,\n" +
			"\t\t\"name\": \"精美钢琴\",\n" +
			"\t\t\"desc\": \"礼物\",\n" +
			"\t\t\"amount\": 70,\n" +
			"\t\t\"vip_amount\": 45,\n" +
			"\t\t\"product_channel_names\": \"\",\n" +
			"\t\t\"image_url\": \"http://img.365yf.com/gifts/65/abe2ca797a8d8122afd9.jpg\",\n" +
			"\t\t\"big_image_url\": \"http://img.365yf.com/gifts/65/0aafce2e4dc68688e587.jpg\",\n" +
			"\t\t\"dynamic_image_url\": \"http://img.365yf.com/gifts/65/f2e66c7c6959ae83fbca.jpg\",\n" +
			"\t\t\"rank\": 13,\n" +
			"\t\t\"status_text\": \"有效\"\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"id\": 93,\n" +
			"\t\t\"name\": \"最佳女友证\",\n" +
			"\t\t\"desc\": \"礼物\",\n" +
			"\t\t\"amount\": 70,\n" +
			"\t\t\"vip_amount\": 45,\n" +
			"\t\t\"product_channel_names\": \"\",\n" +
			"\t\t\"image_url\": \"http://img.365yf.com/gifts/93/54d563c25b0498ee5da2.jpg\",\n" +
			"\t\t\"big_image_url\": \"http://img.365yf.com/gifts/93/64837e4912b33102b125.jpg\",\n" +
			"\t\t\"dynamic_image_url\": \"http://img.365yf.com/gifts/93/a58eef2b5bcfa2212389.jpg\",\n" +
			"\t\t\"rank\": 13,\n" +
			"\t\t\"status_text\": \"有效\"\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"id\": 10,\n" +
			"\t\t\"name\": \"Chanel香水\",\n" +
			"\t\t\"desc\": \"礼物\",\n" +
			"\t\t\"amount\": 90,\n" +
			"\t\t\"vip_amount\": 70,\n" +
			"\t\t\"product_channel_names\": \"\",\n" +
			"\t\t\"image_url\": \"http://img.365yf.com/gifts/10/73344d6a16985460dd5d.jpg\",\n" +
			"\t\t\"big_image_url\": \"http://img.365yf.com/gifts/10/24cf4f931ee934d36d86.jpg\",\n" +
			"\t\t\"dynamic_image_url\": \"http://img.365yf.com/gifts/10/758ee2df3128c3c345ae.jpg\",\n" +
			"\t\t\"rank\": 15,\n" +
			"\t\t\"status_text\": \"有效\"\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"id\": 9,\n" +
			"\t\t\"name\": \"L.V包\",\n" +
			"\t\t\"desc\": \"礼物\",\n" +
			"\t\t\"amount\": 100,\n" +
			"\t\t\"vip_amount\": 80,\n" +
			"\t\t\"product_channel_names\": \"\",\n" +
			"\t\t\"image_url\": \"http://img.365yf.com/gifts/9/efbebbf9b5336cd0cbe7.jpg\",\n" +
			"\t\t\"big_image_url\": \"http://img.365yf.com/gifts/9/2052766526efa1f8f75d.jpg\",\n" +
			"\t\t\"dynamic_image_url\": \"http://img.365yf.com/gifts/9/1cacf890f7436476c4ff.jpg\",\n" +
			"\t\t\"rank\": 16,\n" +
			"\t\t\"status_text\": \"有效\"\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"id\": 52,\n" +
			"\t\t\"name\": \"铂金对戒\",\n" +
			"\t\t\"desc\": \"礼物\",\n" +
			"\t\t\"amount\": 120,\n" +
			"\t\t\"vip_amount\": 100,\n" +
			"\t\t\"product_channel_names\": \"\",\n" +
			"\t\t\"image_url\": \"http://img.365yf.com/gifts/52/6a7b1410ee35aa6aa0b6.jpg\",\n" +
			"\t\t\"big_image_url\": \"http://img.365yf.com/gifts/52/1e07d72ff35695800db8.jpg\",\n" +
			"\t\t\"dynamic_image_url\": \"http://img.365yf.com/gifts/52/dd6ef1dead2f9eca1786.jpg\",\n" +
			"\t\t\"rank\": 17,\n" +
			"\t\t\"status_text\": \"有效\"\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"id\": 7,\n" +
			"\t\t\"name\": \"求婚钻戒\",\n" +
			"\t\t\"desc\": \"礼物\",\n" +
			"\t\t\"amount\": 160,\n" +
			"\t\t\"vip_amount\": 120,\n" +
			"\t\t\"product_channel_names\": \"\",\n" +
			"\t\t\"image_url\": \"http://img.365yf.com/gifts/7/ad9b0d5f90be6f072e14.jpg\",\n" +
			"\t\t\"big_image_url\": \"http://img.365yf.com/gifts/7/01a79b5191540fb75c03.jpg\",\n" +
			"\t\t\"dynamic_image_url\": \"http://img.365yf.com/gifts/7/6534e77be14555637ebc.jpg\",\n" +
			"\t\t\"rank\": 18,\n" +
			"\t\t\"status_text\": \"有效\"\n" +
			"\t},\n" +
			"\t{\n" +
			"\t\t\"id\": 6,\n" +
			"\t\t\"name\": \"路虎汽车\",\n" +
			"\t\t\"desc\": \"礼物\",\n" +
			"\t\t\"amount\": 200,\n" +
			"\t\t\"vip_amount\": 160,\n" +
			"\t\t\"product_channel_names\": \"\",\n" +
			"\t\t\"image_url\": \"http://img.365yf.com/gifts/6/0f93770368763722cca0.jpg\",\n" +
			"\t\t\"big_image_url\": \"http://img.365yf.com/gifts/6/1c828e1ca89579975904.jpg\",\n" +
			"\t\t\"dynamic_image_url\": \"http://img.365yf.com/gifts/6/9e323f262b67f659e56e.jpg\",\n" +
			"\t\t\"rank\": 19,\n" +
			"\t\t\"status_text\": \"有效\"\n" +
			"\t}],\n" +
			"\t\"money\": 0\n" +
			"}";
}
