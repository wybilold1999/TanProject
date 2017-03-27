package com.example.user.myapplication.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.user.myapplication.CSApplication;
import com.example.user.myapplication.R;
import com.example.user.myapplication.UpdateEvent;
import com.example.user.myapplication.model.UserAndDynamic;
import com.example.user.myapplication.net.DownloadFileRequest;
import com.example.user.myapplication.oss.AppManager;
import com.example.user.myapplication.oss.Config;
import com.example.user.myapplication.oss.OSSFileUploadRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnCompressListener;

/**
 * 作者：wangyb
 * 时间：2016/9/1 19:54
 * 描述：
 */
public class LingAiParseAndAssembleUtils {
	private static LingAiParseAndAssembleUtils mInstance;

	public static String[] purposeArray;
	public static String[] whereArray;
	public static String[] doWhatFirstArray;
	public static String[] conceptionArray;
	public static String[] femaleSexPartArray;
	public static String[] maleSexPartArray;
	public static String[] femaleLableArray;
	public static String[] maleLableArray;
	public static String[] intrestArray;
	public static String[] constellationArray;
	public static String[] occupationArray;
	public static String[] educationArray;

	private int allPhotoCount = 0;
	private int allPhotoCountTmp = 0;
	private int alreadyDownloadPicCount = 0;
	private ArrayList<UserAndDynamic.MsgBean> contacts;

	private String data;

	private LingAiParseAndAssembleUtils(){}

	public static LingAiParseAndAssembleUtils getInstance(Context context) {
		if (mInstance == null) {
			synchronized(LingAiParseJsonUtil.class) {
				if (mInstance == null) {
					mInstance = new LingAiParseAndAssembleUtils();

					purposeArray = context.getResources().getStringArray(R.array.purpose);
					whereArray = context.getResources().getStringArray(R.array.where);
					doWhatFirstArray = context.getResources().getStringArray(R.array.doWhatFirst);
					conceptionArray = context.getResources().getStringArray(R.array.conception);
					femaleSexPartArray = context.getResources().getStringArray(R.array.female_sex_part);
					maleSexPartArray = context.getResources().getStringArray(R.array.male_sex_part);
					femaleLableArray = context.getResources().getStringArray(R.array.female_lable);
					maleLableArray = context.getResources().getStringArray(R.array.male_lable);
					intrestArray = context.getResources().getStringArray(R.array.intrest);
					constellationArray = context.getResources().getStringArray(R.array.constellation);
					occupationArray = context.getResources().getStringArray(R.array.occupation);
					educationArray = context.getResources().getStringArray(R.array.education);
				}
			}
		}
		return mInstance;
	}

	public  int getMaleRandomWeight() {
		Random rand = new Random();
		int weight = rand.nextInt(50) + 100;
		return weight;
	}

	public  int getFeMaleRandomWeight() {
		Random rand = new Random();
		int weight = rand.nextInt(30) + 80;
		return weight;
	}

	public  String getMaleSexPart() {
		Random randcount = new Random();
		int count = randcount.nextInt(3) + 1;
		if (count == 1) {
			Random rand = new Random();
			int index = rand.nextInt(maleSexPartArray.length);
			return maleSexPartArray[index];
		} else if (count == 2) {
			StringBuilder tmp2 = new StringBuilder();
			for(int i = 0; i < 2; i++) {
				Random rand = new Random();
				int index = rand.nextInt(maleSexPartArray.length);
				if (!tmp2.toString().contains(maleSexPartArray[index])) {
					tmp2.append(maleSexPartArray[index]);
				}
				if (i == 0) {
					tmp2.append(";");
				}
			}
			return tmp2.toString();
		} else if (count == 3) {
			StringBuilder tmp2 = new StringBuilder();
			for(int i = 0; i < 3; i++) {
				Random rand = new Random();
				int index = rand.nextInt(maleSexPartArray.length);
				if (!tmp2.toString().contains(maleSexPartArray[index])) {
					tmp2.append(maleSexPartArray[index]);
				}
				if (i == 0 || i == 1) {
					tmp2.append(";");
				}
			}
			return tmp2.toString();
		}
		return "";
	}

	public  String getFeMaleSexPart() {
		Random randcount = new Random();
		int count = randcount.nextInt(3) + 1;
		if (count == 1) {
			Random rand = new Random();
			int index = rand.nextInt(femaleSexPartArray.length);
			return femaleSexPartArray[index];
		} else if (count == 2) {
			StringBuilder tmp2 = new StringBuilder();
			for(int i = 0; i < 2; i++) {
				Random rand = new Random();
				int index = rand.nextInt(femaleSexPartArray.length);
				if (!tmp2.toString().contains(femaleSexPartArray[index])) {
					tmp2.append(femaleSexPartArray[index]);
				}
				if (i == 0) {
					tmp2.append(";");
				}
			}
			return tmp2.toString();
		} else if (count == 3) {
			StringBuilder tmp2 = new StringBuilder();
			for(int i = 0; i < 3; i++) {
				Random rand = new Random();
				int index = rand.nextInt(femaleSexPartArray.length);
				if (!tmp2.toString().contains(femaleSexPartArray[index])) {
					tmp2.append(femaleSexPartArray[index]);
				}
				if (i == 0 || i == 1) {
					tmp2.append(";");
				}
			}
			return tmp2.toString();
		}
		return "";
	}

	public  String getMaleplable() {
		Random randcount = new Random();
		int count = randcount.nextInt(3) + 1;
		if (count == 1) {
			Random rand = new Random();
			int index = rand.nextInt(maleLableArray.length);
			return maleLableArray[index];
		} else if (count == 2) {
			StringBuilder tmp2 = new StringBuilder();
			for(int i = 0; i < 2; i++) {
				Random rand = new Random();
				int index = rand.nextInt(maleLableArray.length);
				if (!tmp2.toString().contains(maleLableArray[index])) {
					tmp2.append(maleLableArray[index]);
				}
				if (i == 0) {
					tmp2.append(";");
				}
			}
			return tmp2.toString();
		} else if (count == 3) {
			StringBuilder tmp2 = new StringBuilder();
			for(int i = 0; i < 3; i++) {
				Random rand = new Random();
				int index = rand.nextInt(maleLableArray.length);
				if (!tmp2.toString().contains(maleLableArray[index])) {
					tmp2.append(maleLableArray[index]);
				}
				if (i == 0 || i == 1) {
					tmp2.append(";");
				}
			}
			return tmp2.toString();
		}
		return "";
	}

	public  String getFeMaleplable() {
		Random randcount = new Random();
		int count = randcount.nextInt(3) + 1;
		if (count == 1) {
			Random rand = new Random();
			int index = rand.nextInt(femaleLableArray.length);
			return femaleLableArray[index];
		} else if (count == 2) {
			StringBuilder tmp2 = new StringBuilder();
			for(int i = 0; i < 2; i++) {
				Random rand = new Random();
				int index = rand.nextInt(femaleLableArray.length);
				if (!tmp2.toString().contains(femaleLableArray[index])) {
					tmp2.append(femaleLableArray[index]);
				}
				if (i == 0) {
					tmp2.append(";");
				}
			}
			return tmp2.toString();
		} else if (count == 3) {
			StringBuilder tmp2 = new StringBuilder();
			for(int i = 0; i < 3; i++) {
				Random rand = new Random();
				int index = rand.nextInt(femaleLableArray.length);
				if (!tmp2.toString().contains(femaleLableArray[index])) {
					tmp2.append(femaleLableArray[index]);
				}
				if (i == 0 || i == 1) {
					tmp2.append(";");
				}
			}
			return tmp2.toString();
		}
		return "";
	}

	public  String getintrest() {
		Random randcount = new Random();
		int count = randcount.nextInt(3) + 1;
		if (count == 1) {
			Random rand = new Random();
			int index = rand.nextInt(intrestArray.length);
			return intrestArray[index];
		} else if (count == 2) {
			StringBuilder tmp2 = new StringBuilder();
			for(int i = 0; i < 2; i++) {
				Random rand = new Random();
				int index = rand.nextInt(intrestArray.length);
				if (!tmp2.toString().contains(intrestArray[index])) {
					tmp2.append(intrestArray[index]);
				}
				if (i == 0) {
					tmp2.append(";");
				}
			}
			return tmp2.toString();
		} else if (count == 3) {
			StringBuilder tmp2 = new StringBuilder();
			for(int i = 0; i < 3; i++) {
				Random rand = new Random();
				int index = rand.nextInt(intrestArray.length);
				if (!tmp2.toString().contains(intrestArray[index])) {
					tmp2.append(intrestArray[index]);
				}
				if (i == 0 || i == 1) {
					tmp2.append(";");
				}
			}
			return tmp2.toString();
		}
		return "";
	}

	public  String getconstellation() {
		Random rand = new Random();
		int index = rand.nextInt(constellationArray.length);
		return constellationArray[index];
	}

	public  String getoccupation() {
		Random rand = new Random();
		int index = rand.nextInt(occupationArray.length);
		return occupationArray[index];
	}

	public  String geteducation() {
		Random rand = new Random();
		int index = rand.nextInt(educationArray.length);
		return educationArray[index];
	}

	public  String getpurpose() {
		Random rand = new Random();
		int index = rand.nextInt(purposeArray.length);
		return purposeArray[index];
	}

	public  String getwhere() {
		Random rand = new Random();
		int index = rand.nextInt(whereArray.length);
		return whereArray[index];
	}

	public  String getdoWhatFirst() {
		Random rand = new Random();
		int index = rand.nextInt(doWhatFirstArray.length);
		return doWhatFirstArray[index];
	}

	public  String getconception() {
		Random rand = new Random();
		int index = rand.nextInt(conceptionArray.length);
		return conceptionArray[index];
	}

	public  double getDistance() {
		Random rand = new Random();
		DecimalFormat dcmFmt = new DecimalFormat("0.00");
		double index = rand.nextDouble() * 100;
		return Double.parseDouble(dcmFmt.format(index));
	}
	
	public void parseJson(String json) {
		Type listType = new TypeToken<ArrayList<UserAndDynamic.MsgBean>>() {
		}.getType();
		Gson gson = new Gson();
		JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
		JsonArray array = obj.get("msg").getAsJsonArray();
		contacts = gson.fromJson(array, listType);
		if (contacts != null && !contacts.isEmpty()) {
			/**
			 * 设置用户信息
			 */
			if (contacts.get(0).getUser() != null) {
				/**
				 * 上传过程中可能需要控制的地方
				 *
				 *0:同城 1：缘分 2：颜值
				 *
				 */
				contacts.get(0).getUser().isVip = 1;
				contacts.get(0).getUser().UScopeType = 2;
				if (contacts.get(0).getUser().getSex() == 1) { //女生
					contacts.get(0).getUser().setSex(0);
					contacts.get(0).getUser().weight = getFeMaleRandomWeight();
					contacts.get(0).getUser().sexPart = getFeMaleSexPart();
					contacts.get(0).getUser().plable = getFeMaleplable();
				} else {
					contacts.get(0).getUser().setSex(1);
					contacts.get(0).getUser().weight = getMaleRandomWeight();
					contacts.get(0).getUser().sexPart = getMaleSexPart();
					contacts.get(0).getUser().plable = getMaleplable();
				}
				
				contacts.get(0).getUser().intrest = getintrest();
				contacts.get(0).getUser().constellation = getconstellation();
				contacts.get(0).getUser().occupation = getoccupation();
				contacts.get(0).getUser().education = geteducation();
				contacts.get(0).getUser().purpose = getpurpose();
				contacts.get(0).getUser().where = getwhere();
				contacts.get(0).getUser().doWhatFirst = getdoWhatFirst();
				contacts.get(0).getUser().conception = getconception();
				if (contacts.get(0).getUser().UScopeType == 0) {
					contacts.get(0).getUser().distance = getDistance();
				}

			}

			new DowanloadFaceUrlTask(contacts.get(0).getUser()).request(contacts.get(0).getUser().getPhoto(),
					FileAccessorUtils.getDefaultPathName().getAbsolutePath(),
					Md5Util.md5(contacts.get(0).getUser().getPhoto()) + ".jpg");
			
			/**
			 * 下载图片
			 */
			for (int i = 0; i < contacts.size(); i++) {
				if (null != contacts.get(i).getPhoto()) {
					for (int j = 0; j < contacts.get(i).getPhoto().size(); j++) {
						if (!TextUtils.isEmpty(contacts.get(i).getBrcon()) && contacts.get(i).getBrcon().startsWith("http")) {
							contacts.get(i).setBrcon("分享图片");
						}
						allPhotoCount++;
						String url = contacts.get(i).getPhoto().get(j).getHost() + contacts.get(i).getPhoto().get(j).getBig();
						new DownloadDycPhotoTask(contacts.get(i).getPhoto().get(j)).request(url,
								FileAccessorUtils.getDefaultPathName().getAbsolutePath(), Md5Util.md5(url) + ".jpg");
					}
				}
			}
		}
	}

	class DowanloadFaceUrlTask extends DownloadFileRequest {
		private UserAndDynamic.MsgBean.UserBean mUserBean;

		public DowanloadFaceUrlTask(UserAndDynamic.MsgBean.UserBean userBean) {
			mUserBean = userBean;
		}
		@Override
		public void onPostExecute(String s) {
//			String imgUrl = ImageUtil.compressImage(s, FileAccessorUtils.DEFAULT_PATH);
			new OSSUploadFaceUrlTask(mUserBean).request(
					AppManager.getFederationToken().bucketName, AppManager.getOSSImgPath(), s);
		}

		@Override
		public void onErrorExecute(String error) {
		}
	}

	class DownloadDycPhotoTask extends DownloadFileRequest {
		private UserAndDynamic.MsgBean.PhotoBean mPhotoBean;

		public DownloadDycPhotoTask(UserAndDynamic.MsgBean.PhotoBean photoBean) {
			mPhotoBean = photoBean;
		}

		@Override
		public void onPostExecute(final String s) {
			alreadyDownloadPicCount++;
			Log.d("test", String.valueOf(alreadyDownloadPicCount));
//			String imgUrl = ImageUtil.compressImage(s, FileAccessorUtils.DEFAULT_PATH);
			Luban.get(CSApplication.getInstance())                     // initialization of Luban
					.load(new File(s))                     // set the image file to compress
					.putGear(Luban.THIRD_GEAR)      // set the compress mode, default is : THIRD_GEAR
					.launch(new OnCompressListener() {
						@Override
						public void onStart() {

						}

						@Override
						public void onSuccess(File file) {
							new OSSUploadDycPhotoTask(mPhotoBean).request(
									AppManager.getFederationToken().bucketName, AppManager.getOSSImgPath(), file.getAbsolutePath());
						}

						@Override
						public void onError(Throwable e) {
							Log.d("test", e.getMessage());
						}
					});              // start compression and set the listener
		}

		@Override
		public void onErrorExecute(String error) {
			ToastUtil.showMessage("下载失败");
		}
	}

	class OSSUploadFaceUrlTask extends OSSFileUploadRequest {
		private UserAndDynamic.MsgBean.UserBean mUserBean;

		public OSSUploadFaceUrlTask(UserAndDynamic.MsgBean.UserBean userBean) {
			mUserBean = userBean;
		}
		@Override
		public void onPostExecute(String s) {
			Log.d("test", "头像上传成功");
			mUserBean.faceUrl = Config.imagePoint + s;
		}

		@Override
		public void onErrorExecute(String error) {
		}
	}

	class OSSUploadDycPhotoTask extends OSSFileUploadRequest {

		private UserAndDynamic.MsgBean.PhotoBean mPhotoBean;

		public OSSUploadDycPhotoTask(UserAndDynamic.MsgBean.PhotoBean photoBean) {
			mPhotoBean = photoBean;
		}

		@Override
		public void onPostExecute(String s) {
			allPhotoCountTmp++;
			mPhotoBean.setBig(Config.imagePoint + s);
			if (allPhotoCountTmp == allPhotoCount) {
				Gson gson = new Gson();
				if (TextUtils.isEmpty(contacts.get(0).getUser().faceUrl)) {
					contacts.get(0).getUser().faceUrl = contacts.get(0).getPhoto().get(0).getBig();
				}
				data = gson.toJson(contacts);
				Log.d("test", data);
				ToastUtil.showMessage("解析完成");
				EventBus.getDefault().post(new UpdateEvent());
			}
		}

		@Override
		public void onErrorExecute(String error) {
		}
	}

	public String getJsonData() {
		return data;
	}
}
