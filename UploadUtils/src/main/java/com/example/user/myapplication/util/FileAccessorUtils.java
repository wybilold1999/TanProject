package com.example.user.myapplication.util;

import android.os.Environment;

import java.io.File;

/**
 * 
 * @ClassName:FileAccessorUtils
 * @Description:文件存取工具类
 * @author Administrator
 * @Date:2015年6月5日上午10:06:20
 */
public class FileAccessorUtils {
	
	/** 默认路径 */
	public static final String DEFAULT_PATH = getExternalStorePath()
			+ "/3A/image";
	/** 文件存储路径 */
	public static final String VIDEO_FILE = getExternalStorePath()
			+ "/3A/video";

	/**
	 * 外置存储卡的路径
	 * 
	 * @return
	 */
	public static String getExternalStorePath() {
		if (isExistExternalStore()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		return null;
	}

	/**
	 * 是否有外存卡
	 * 
	 * @return
	 */
	public static boolean isExistExternalStore() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}


	
	/**
	 * 返回默认存放目录
	 * 
	 * @return
	 */
	public static File getDefaultPathName() {
		if (!isExistExternalStore()) {
			return null;
		}

		File directory = new File(DEFAULT_PATH);
		if (!directory.exists() && !directory.mkdirs()) {
			return null;
		}
		return directory;
	}
}
