package com.example.user.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.myapplication.model.DbVideo;
import com.example.user.myapplication.model.UserVideo;
import com.example.user.myapplication.model.VideoLove;
import com.example.user.myapplication.net.DownloadFileRequest;
import com.example.user.myapplication.net.GetUserVideoRequest;
import com.example.user.myapplication.net.UserVideoIsExists;
import com.example.user.myapplication.net.VideoUploadRequest;
import com.example.user.myapplication.oss.AppManager;
import com.example.user.myapplication.oss.Config;
import com.example.user.myapplication.oss.OSSFileUploadRequest;
import com.example.user.myapplication.util.FileAccessorUtils;
import com.example.user.myapplication.util.Md5Util;
import com.example.user.myapplication.util.ToastUtil;
import com.example.user.myapplication.util.VideoLoveUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadVideoLoveActivity extends Activity {


	@Bind(R.id.btn_single_video_info)
	Button mBtnSingleVideoInfo;
	@Bind(R.id.btn_user_videos)
	Button mBtnUserVideos;
	@Bind(R.id.img_state)
	TextView mImgState;
	@Bind(R.id.video_state)
	TextView mVideoState;
	@Bind(R.id.data_state)
	TextView mDataState;
	@Bind(R.id.upload_data)
	Button mUploadData;

	VideoLove video;

	private DbVideo mDbVideo;
	private Random rand = new Random();

	private List<DbVideo> mDbVideoList = new ArrayList<>();
	private String data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_data);
		ButterKnife.bind(this);
		video = VideoLoveUtil.parseJson();
		new UserVideoIsExistsTask().request(String.valueOf(video.getBody().getVideoPic()));
	}

	@OnClick({R.id.btn_single_video_info, R.id.btn_user_videos, R.id.upload_data})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_single_video_info:
				new OSSVideoImgUploadTask(mDbVideo).request(
						AppManager.getFederationToken().bucketName,
						AppManager.getOSSImgPath(), video.getBody().getVideoPic());
				break;
			case R.id.btn_user_videos:
				new OSSVideoUploadTask(mDbVideo).request(
						AppManager.getFederationToken().bucketName,
						AppManager.getOSSVideoPath(), video.getBody().getVideoUrl());
				break;
			case R.id.upload_data:
				if (!TextUtils.isEmpty(data)) {
//					new VideoUploadTask().request(data);
				}
				break;
		}
	}


	class UserVideoIsExistsTask extends UserVideoIsExists {
		@Override
		public void onPostExecute(Boolean aBoolean) {
			if (!aBoolean) {
				mDbVideo = new DbVideo();
				mDbVideo.curImgPath = video.getBody().getVideoPic();
				mDbVideo.curVideoPath = video.getBody().getVideoUrl();
				mDbVideo.originalImgPath = video.getBody().getVideoPic();
				mDbVideo.originalVideoPath = video.getBody().getVideoUrl();
				mDbVideo.uid = String.valueOf(video.getBody().getUserId());
				mDbVideo.description = video.getBody().getTitle();
				int goldNum = rand.nextInt(40) + 50;
				mDbVideo.rose = String.valueOf(goldNum);
				mDbVideo.username = video.getBody().getNick();
				mDbVideo.view = String.valueOf(video.getBody().getSold());
				mDbVideo.sex = DbVideo.SexType.FEMALE;
				mDbVideo.type = DbVideo.VideoType.DANCING;

				mDbVideoList.add(mDbVideo);
				//上传数据到服务器
				Gson gson = new Gson();
				Log.d("test", gson.toJson(mDbVideoList));
				data = gson.toJson(mDbVideoList);
				new VideoUploadTask().request(data);
//				new DownloadImageTask(video).request(video.getBody().getVideoPic(),
//						FileAccessorUtils.DEFAULT_PATH, Md5Util.md5(video.getBody().getVideoPic()) + ".jpg");
			} else {
				ToastUtil.showMessage("该组数据已经存在");
				mDataState.setText("该组数据已经存在");
			}
		}

		@Override
		public void onErrorExecute(String error) {
			ToastUtil.showMessage(error);
		}
	}

	class DownloadImageTask extends DownloadFileRequest {
		private VideoLove userVideo;

		public DownloadImageTask(VideoLove userVideo) {
			this.userVideo = userVideo;
		}

		@Override
		public void onPostExecute(String s) {
			mDbVideo = new DbVideo();
			mDbVideo.curImgPath = s;//先暂时保存本地图片的路径
			mDbVideo.originalImgPath = userVideo.getBody().getVideoPic();
			mDbVideo.originalVideoPath = userVideo.getBody().getVideoUrl();
			mDbVideo.uid = String.valueOf(userVideo.getBody().getUserId());
			mDbVideo.description = userVideo.getBody().getTitle();
			int goldNum = rand.nextInt(40) + 50;
			mDbVideo.rose = String.valueOf(goldNum);
			mDbVideo.username = userVideo.getBody().getNick();
			mDbVideo.view = String.valueOf(userVideo.getBody().getSold());
			mDbVideo.sex = DbVideo.SexType.FEMALE;
			mDbVideo.type = DbVideo.VideoType.SEXY;

			mDbVideoList.add(mDbVideo);

			//开始下载视频
			new DownloadVideoTask(mDbVideo).request(userVideo.getBody().getVideoUrl(),
					FileAccessorUtils.VIDEO_FILE, Md5Util.md5(userVideo.getBody().getVideoUrl() + ".mp4"));
		}

		@Override
		public void onErrorExecute(String error) {
		}
	}

	class DownloadVideoTask extends DownloadFileRequest {
		private DbVideo mVideo;

		public DownloadVideoTask(DbVideo video) {
			mVideo = video;
		}

		@Override
		public void onPostExecute(String s) {
			mVideo.curVideoPath = s;
			ToastUtil.showMessage("数据下载成功");
			mDataState.setText("数据下载成功");
		}

		@Override
		public void onErrorExecute(String error) {
			ToastUtil.showMessage(error);
		}
	}

	class OSSVideoImgUploadTask extends OSSFileUploadRequest {
		private DbVideo video;

		public OSSVideoImgUploadTask(DbVideo dbVideo) {
			video = dbVideo;
		}

		@Override
		public void onPostExecute(String s) {
			video.curImgPath = Config.imagePoint + s;
			ToastUtil.showMessage("视频图片上传成功");
			Log.d("test", "视频图片上传成功");
			mImgState.setText("视频图片上传成功");
		}

		@Override
		public void onErrorExecute(String error) {
		}
	}

	class OSSVideoUploadTask extends OSSFileUploadRequest {
		private DbVideo video;

		public OSSVideoUploadTask(DbVideo video) {
			this.video = video;
		}

		@Override
		public void onPostExecute(String s) {
			video.curVideoPath = Config.imagePoint + s;
			Log.d("test", "视频上传成功");
			mImgState.setText("视频上传成功");
			//上传数据到服务器
			Gson gson = new Gson();
			Log.d("test", gson.toJson(mDbVideoList));
			data = gson.toJson(mDbVideoList);
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
