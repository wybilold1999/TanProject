package com.example.user.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.myapplication.model.DbVideo;
import com.example.user.myapplication.net.DownloadFileRequest;
import com.example.user.myapplication.net.VideoUploadRequest;
import com.example.user.myapplication.oss.AppManager;
import com.example.user.myapplication.oss.Config;
import com.example.user.myapplication.oss.OSSFileUploadRequest;
import com.example.user.myapplication.util.FileAccessorUtils;
import com.example.user.myapplication.util.Md5Util;
import com.example.user.myapplication.util.ToastUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownloadUploadVideoActivity extends Activity {

	@Bind(R.id.download_img)
	Button mDownloadImg;
	@Bind(R.id.download_video)
	Button mDownloadVideo;
	@Bind(R.id.upload_img)
	Button mUploadImg;
	@Bind(R.id.upload_video)
	Button mUploadVideo;
	@Bind(R.id.upload_data)
	Button mUploadData;
	@Bind(R.id.img_state)
	TextView mImgState;
	@Bind(R.id.video_state)
	TextView mVideoState;
	@Bind(R.id.data_state)
	TextView mDataState;
	private String data;

	private  String imgUrl = "http://mvimg2.meitudata.com/58060177799c95266.jpg ";
	private  String videoUrl = "http://mvvideo11.meitudata.com/58069893bc36d5140.mp4 ";
	private DbVideo mDbVideo;

	private List<DbVideo> mDbVideoList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download_upload_data);
		ButterKnife.bind(this);
		mDbVideo = new DbVideo();
		mDbVideo.isTurnOnVideo = "0";
		mDbVideo.sex = DbVideo.SexType.MALE;
		mDbVideo.type = DbVideo.VideoType.NEW;

		/*Random random = new Random();
		int uid = random.nextInt(100) + 30;
		mDbVideo.uid = String.valueOf(uid);
		mDbVideo.username = "u" + String.valueOf(uid);
		int rose = random.nextInt(30) + 10;
		mDbVideo.rose = String.valueOf(rose);
		int view = random.nextInt(50) + 10;
		mDbVideo.view = String.valueOf(view);*/

		Random random = new Random();
		int uid = random.nextInt(100) + 30;
		mDbVideo.uid = String.valueOf(uid);
		mDbVideo.username = "u" + String.valueOf(uid);
		int rose = random.nextInt(20) + 10;
		mDbVideo.rose = String.valueOf(rose);
		int view = random.nextInt(50) + 100;
		mDbVideo.view = String.valueOf(view);
		mDbVideo.description = "好久不见哟";

		new DownloadImageTask().request(imgUrl, FileAccessorUtils.DEFAULT_PATH, Md5Util.md5(imgUrl) + ".jpg");
		new DownloadVideoTask().request(videoUrl, FileAccessorUtils.VIDEO_FILE, Md5Util.md5(videoUrl) + ".mp4");
	}

	@OnClick({R.id.download_img, R.id.download_video, R.id.upload_img, R.id.upload_video, R.id.upload_data})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.download_img:
				new DownloadImageTask().request(imgUrl, FileAccessorUtils.DEFAULT_PATH, Md5Util.md5(imgUrl) + ".jpg");
				break;
			case R.id.download_video:
				new DownloadVideoTask().request(videoUrl, FileAccessorUtils.VIDEO_FILE, Md5Util.md5(videoUrl) + ".mp4");
				break;
			case R.id.upload_img:
				break;
			case R.id.upload_video:
				break;
			case R.id.upload_data:
				new VideoUploadTask().request(data);
				break;
		}
	}

	class DownloadImageTask extends DownloadFileRequest {

		@Override
		public void onPostExecute(String s) {
			mDbVideo.originalImgPath = imgUrl;
			new OSSVideoImgUploadTask().request(AppManager.getFederationToken().bucketName,
					AppManager.getOSSImgPath(), s);
		}

		@Override
		public void onErrorExecute(String error) {
			ToastUtil.showMessage(error);
		}
	}

	class DownloadVideoTask extends DownloadFileRequest {

		@Override
		public void onPostExecute(String s) {
			mDbVideo.originalVideoPath = videoUrl;
			new OSSVideoUploadTask().request(AppManager.getFederationToken().bucketName,
					AppManager.getOSSVideoPath(), s);
		}

		@Override
		public void onErrorExecute(String error) {
			ToastUtil.showMessage(error);
		}
	}

	class OSSVideoImgUploadTask extends OSSFileUploadRequest {


		@Override
		public void onPostExecute(String s) {
			mDbVideo.curImgPath = Config.imagePoint + s;
			ToastUtil.showMessage("视频图片上传成功");
			Log.d("test", "视频图片上传成功");
			mImgState.setText("视频图片上传成功");

			if (!TextUtils.isEmpty(mDbVideo.curImgPath) && !TextUtils.isEmpty(mDbVideo.curVideoPath)) {
				mDbVideoList.add(mDbVideo);
				Gson gson = new Gson();
				mImgState.setText("视频上传成功");
				Log.d("test", gson.toJson(mDbVideoList));
				data = gson.toJson(mDbVideoList);
				new VideoUploadTask().request(data);
			}
		}

		@Override
		public void onErrorExecute(String error) {
		}
	}

	class OSSVideoUploadTask extends OSSFileUploadRequest {

		@Override
		public void onPostExecute(String s) {
			mDbVideo.curVideoPath = Config.imagePoint + s;
			if (!TextUtils.isEmpty(mDbVideo.curImgPath) && !TextUtils.isEmpty(mDbVideo.curVideoPath)) {
				mDbVideoList.add(mDbVideo);
				Gson gson = new Gson();
				mImgState.setText("视频上传成功");
				Log.d("test", gson.toJson(mDbVideoList));
				data = gson.toJson(mDbVideoList);
				new VideoUploadTask().request(data);
			}
		}

		@Override
		public void onErrorExecute(String error) {
			ToastUtil.showMessage(error);
			mVideoState.setText(error);
		}
	}

	class VideoUploadTask extends VideoUploadRequest {
		@Override
		public void onPostExecute(String s) {
			ToastUtil.showMessage("数据上传到服务器成功");
			mVideoState.setText("数据上传到服务器成功");
		}

		@Override
		public void onErrorExecute(String error) {
			ToastUtil.showMessage(error);
			mVideoState.setText(error);
		}
	}

}
