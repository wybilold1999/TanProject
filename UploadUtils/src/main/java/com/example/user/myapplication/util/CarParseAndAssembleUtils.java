package com.example.user.myapplication.util;

import android.content.Context;
import android.util.Log;

import com.example.user.myapplication.R;
import com.example.user.myapplication.UpdateEvent;
import com.example.user.myapplication.model.UserAndDycCar;
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

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * 作者：wangyb
 * 时间：2016/9/1 19:54
 * 描述：
 */
public class CarParseAndAssembleUtils {
	private static CarParseAndAssembleUtils mInstance;

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

	private ArrayList<UserAndDycCar.ResultBean> contacts;

	private int totalPhotos = 0;
	private int count = 0;

	private CarParseAndAssembleUtils(){}

	public static CarParseAndAssembleUtils getInstance(Context context) {
		if (mInstance == null) {
			synchronized(LingAiParseJsonUtil.class) {
				if (mInstance == null) {
					mInstance = new CarParseAndAssembleUtils();

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

	public  int getMaleRandomHeight() {
		Random rand = new Random();
		int weight = rand.nextInt(26) + 160;
		return weight;
	}

	public  int getFeMaleRandomHeight() {
		Random rand = new Random();
		int weight = rand.nextInt(26) + 155;
		return weight;
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

	public  String getMarray() {
		String[] marray = new String[]{"单身","热恋","已婚"};
		Random rand = new Random();
		return marray[rand.nextInt(3)];
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
		Type listType = new TypeToken<ArrayList<UserAndDycCar.ResultBean>>() {
		}.getType();
		Gson gson = new Gson();
		JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
		JsonArray array = obj.get("result").getAsJsonArray();
		contacts = gson.fromJson(array, listType);
		if (contacts != null && !contacts.isEmpty()) {
			/**
			 * 设置用户信息
			 */
			if (contacts.get(0) != null) {
				/**
				 * 上传过程中可能需要控制的地方
				 *
				 *
				 *
				 *
				 *
				 *
				 */
				contacts.get(0).isVip = 0;
				contacts.get(0).UScopeType = 0;
				if (contacts.get(0).getSex() == 2) { //女生
					contacts.get(0).setSex(0);
					contacts.get(0).weight = getFeMaleRandomWeight();
					contacts.get(0).sexPart = getFeMaleSexPart();
					contacts.get(0).plable = getFeMaleplable();
					contacts.get(0).height = getFeMaleRandomHeight();
				} else {
					contacts.get(0).setSex(1);
					contacts.get(0).weight = getMaleRandomWeight();
					contacts.get(0).sexPart = getMaleSexPart();
					contacts.get(0).plable = getMaleplable();
					contacts.get(0).height = getMaleRandomHeight();
				}

				contacts.get(0).marry = "单身";
				contacts.get(0).intrest = getintrest();
				contacts.get(0).constellation = getconstellation();
				contacts.get(0).occupation = getoccupation();
				contacts.get(0).education = geteducation();
				contacts.get(0).purpose = getpurpose();
				contacts.get(0).where = getwhere();
				contacts.get(0).doWhatFirst = getdoWhatFirst();
				contacts.get(0).conception = getconception();
				contacts.get(0).distance = getDistance();
				
			}

			String faceUrl = contacts.get(0).getFace().replace("xx_", "");
			new DowanloadFaceUrlTask(contacts.get(0)).request(faceUrl,
					FileAccessorUtils.getDefaultPathName().getAbsolutePath(),
					Md5Util.md5(faceUrl) + ".jpg");
			
			/**
			 * 下载图片
			 */
			for (int i = 0; i < contacts.size(); i++) {
				String str = contacts.get(i).getSummary();
				String str_sec = str.replace("车星人", "***");
				contacts.get(i).setSummary(str_sec);
				totalPhotos += contacts.get(i).getPics().size();
				for (int j = 0; j < contacts.get(i).getPics().size(); j++) {
					String url = contacts.get(i).getPics().get(j).replace("t_","");
					new DownloadDycPhotoTask(contacts.get(i)).request(url,
							FileAccessorUtils.getDefaultPathName().getAbsolutePath(), Md5Util.md5(url) + ".jpg");
				}
			}
		}
	}

	class DowanloadFaceUrlTask extends DownloadFileRequest {
		private UserAndDycCar.ResultBean mResultBean;

		public DowanloadFaceUrlTask(UserAndDycCar.ResultBean resultBean) {
			mResultBean = resultBean;
		}
		@Override
		public void onPostExecute(String s) {
			String imgUrl = ImageUtil.compressImage(s, FileAccessorUtils.DEFAULT_PATH);
			new OSSUploadFaceUrlTask(mResultBean).request(
					AppManager.getFederationToken().bucketName, AppManager.getOSSImgPath(), imgUrl);
		}

		@Override
		public void onErrorExecute(String error) {
		}
	}

	class DownloadDycPhotoTask extends DownloadFileRequest {
		private UserAndDycCar.ResultBean mPhotoBean;

		public DownloadDycPhotoTask(UserAndDycCar.ResultBean photoBean) {
			mPhotoBean = photoBean;
		}

		@Override
		public void onPostExecute(String s) {
			String imgUrl = ImageUtil.compressImage(s, FileAccessorUtils.DEFAULT_PATH);
			new OSSUploadDycPhotoTask(mPhotoBean).request(
					AppManager.getFederationToken().bucketName, AppManager.getOSSImgPath(), imgUrl);
		}

		@Override
		public void onErrorExecute(String error) {
		}
	}

	class OSSUploadFaceUrlTask extends OSSFileUploadRequest {
		private UserAndDycCar.ResultBean mResultBean;

		public OSSUploadFaceUrlTask(UserAndDycCar.ResultBean resultBean) {
			mResultBean = resultBean;
		}
		@Override
		public void onPostExecute(String s) {
			mResultBean.faceUrl = Config.imagePoint + s;
		}

		@Override
		public void onErrorExecute(String error) {
		}
	}

	class OSSUploadDycPhotoTask extends OSSFileUploadRequest {

		private UserAndDycCar.ResultBean mPhotoBean;

		public OSSUploadDycPhotoTask(UserAndDycCar.ResultBean photoBean) {
			mPhotoBean = photoBean;
		}

		@Override
		public void onPostExecute(String s) {
			mPhotoBean.picdatas.add(Config.imagePoint + s);
			count++;
			if (totalPhotos == count) {
				count = 0;
				Log.d("test", "上传完成");
				ToastUtil.showMessage("解析完成");
				EventBus.getDefault().post(new UpdateEvent());
			}
		}

		@Override
		public void onErrorExecute(String error) {
		}
	}

	public String getJsonData() {
		Gson gson = new Gson();
		String data = gson.toJson(contacts);
		Log.d("test",data);
		return data;
	}
}
