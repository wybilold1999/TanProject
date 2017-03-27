package com.example.user.myapplication;

import android.app.Activity;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoPlayActivity extends Activity implements OnClickListener, OnPreparedListener {

	private RelativeLayout mRootLayout;
	private ProgressBar mProgressBar;
	private VideoView mVideoView;
	private String mFilePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 防止锁屏
		setContentView(R.layout.activity_video_play);
		mRootLayout = (RelativeLayout) findViewById(R.id.root_layout);
		mVideoView = (VideoView) findViewById(R.id.video_view);
		mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
		mFilePath = "http://real-love-server.oss-cn-shenzhen.aliyuncs.com/tan_love/video/%E5%BE%AE%E5%8D%9A%E6%AD%A3%E6%96%87.mp4";

		MediaController mediaController = new MediaController(this);
		mediaController.setAnchorView(mVideoView);
		mediaController.setMediaPlayer(new MediaController.MediaPlayerControl() {
			@Override
			public void start() {
				mVideoView.start();
			}

			@Override
			public void seekTo(int pos) {
				mVideoView.seekTo(pos);
			}

			@Override
			public void pause() {
				mVideoView.pause();
			}

			@Override
			public boolean isPlaying() {
				return mVideoView.isPlaying();
			}

			@Override
			public int getDuration() {
				return mVideoView.getDuration();
			}

			@Override
			public int getCurrentPosition() {
				return 0;
			}

			@Override
			public int getBufferPercentage() {
				return 0;
			}

			@Override
			public boolean canSeekForward() {
				return true;
			}

			@Override
			public boolean canSeekBackward() {
				return true;
			}

			@Override
			public boolean canPause() {
				return true;
			}

			public int getAudioSessionId() {
				// TODO Auto-generated method stub
				return 0;
			}

		});

		mVideoView.setOnClickListener(this);
		mVideoView.setOnPreparedListener(this);
		mVideoView.setMediaController(mediaController);
		mVideoView.setVideoPath(mFilePath);
		mVideoView.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				Toast.makeText(VideoPlayActivity.this, "播放文件损坏", Toast.LENGTH_SHORT);
				mVideoView.stopPlayback();
				return false;
			}
		});

	}

	/** 是否需要恢复视频播放 */
	private boolean mNeedResume;

	@Override
	public void onResume() {
		super.onResume();
		if (mVideoView != null) {
			if (mNeedResume) {
				mVideoView.start();
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mVideoView != null) {
			if (mVideoView.isPlaying()) {
				mVideoView.pause();
				mNeedResume = true;
			}
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mProgressBar.setVisibility(View.GONE);
		if (!isFinishing()) {
			mVideoView.start();
		} else {
			finish();
		}
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			mRootLayout.setRotation(-90);
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mRootLayout.setRotation(90);
		}
	}
}
