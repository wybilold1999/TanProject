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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadVideoActivity extends Activity {


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

	public ArrayList<UserVideo> mUserVideos;

	private DbVideo mDbVideo;

	private List<DbVideo> mDbVideoList = new ArrayList<>();
	private int count = 0;

	private String data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_data);
		ButterKnife.bind(this);
		new UserVideoTask().request(Config.VIDEO_URL);
	}

	@OnClick({R.id.btn_single_video_info, R.id.btn_user_videos, R.id.upload_data})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_single_video_info:
				count = 0;
				for (DbVideo video : mDbVideoList) {
					new OSSVideoImgUploadTask(video).request(
							AppManager.getFederationToken().bucketName,
							AppManager.getOSSImgPath(), video.curImgPath);
				}
				break;
			case R.id.btn_user_videos:
				count = 0;
				for (final DbVideo video : mDbVideoList) {
					new OSSVideoUploadTask(video).request(
							AppManager.getFederationToken().bucketName,
							AppManager.getOSSVideoPath(), video.curVideoPath);
				}
				break;
			case R.id.upload_data:
				if (!TextUtils.isEmpty(data)) {
					new VideoUploadTask().request(data);
				}
				break;
		}
	}

	class UserVideoTask extends GetUserVideoRequest {

		@Override
		public void onPostExecute(ArrayList<UserVideo> userVideos) {
			if (userVideos != null && userVideos.size() > 0) {
				new UserVideoIsExistsTask().request(userVideos.get(0).uid);
				mUserVideos = userVideos;
			}
		}

		@Override
		public void onErrorExecute(String error) {
			ToastUtil.showMessage(error);
		}
	}

	class UserVideoIsExistsTask extends UserVideoIsExists {
		@Override
		public void onPostExecute(Boolean aBoolean) {
			if (!aBoolean) {
				for (UserVideo userVideo : mUserVideos) {
					new DownloadImageTask(userVideo).request(userVideo.cover,
							FileAccessorUtils.DEFAULT_PATH, Md5Util.md5(userVideo.cover) + ".jpg");
				}
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
		private UserVideo userVideo;

		public DownloadImageTask(UserVideo userVideo) {
			this.userVideo = userVideo;
		}

		@Override
		public void onPostExecute(String s) {
			mDbVideo = new DbVideo();
			mDbVideo.curImgPath = s;//先暂时保存本地图片的路径
			mDbVideo.originalImgPath = userVideo.cover;
			String baseUrl = userVideo.cover.substring(0, userVideo.cover.indexOf("./"));
			String url = userVideo.url.substring(2);
			String videoUrl = baseUrl + url;
			mDbVideo.originalVideoPath = videoUrl;
			mDbVideo.uid = userVideo.uid;
			mDbVideo.description = userVideo.description;
			mDbVideo.rose = userVideo.rose;
			mDbVideo.username = userVideo.username;
			mDbVideo.view = userVideo.view;
			mDbVideo.sex = DbVideo.SexType.FEMALE;
			mDbVideo.type = DbVideo.VideoType.NEW;

			mDbVideoList.add(mDbVideo);

			//开始下载视频
			new DownloadVideoTask(mDbVideo).request(videoUrl,
					FileAccessorUtils.VIDEO_FILE, Md5Util.md5(videoUrl) + ".mp4");
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
			count++;
			if (count == mUserVideos.size()) {
				ToastUtil.showMessage("数据下载成功");
				mDataState.setText("数据下载成功");
			}
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
			count++;
			if (count == mUserVideos.size()) {
				ToastUtil.showMessage("视频图片上传成功");
				Log.d("test", "视频图片上传成功");
				mImgState.setText("视频图片上传成功");
			}
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
			count++;
			if (count == mDbVideoList.size()) {
				Log.d("test", "视频上传成功");
				mImgState.setText("视频上传成功");
				//上传数据到服务器
				Gson gson = new Gson();
				Log.d("test", gson.toJson(mDbVideoList));
				data = gson.toJson(mDbVideoList);
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
