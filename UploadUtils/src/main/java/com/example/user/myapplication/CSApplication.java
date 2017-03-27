package com.example.user.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDexApplication;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.liulishuo.filedownloader.FileDownloader;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * @author zxj
 * @ClassName:CSApplication
 * @Description:全局Application
 * @Date:2015年5月3日上午10:39:37
 */
public class CSApplication extends MultiDexApplication {

    private static CSApplication sApplication;
    private RequestQueue mRequestQueue;
    public final ImageLoader imageLoader = ImageLoader.getInstance();

    public static synchronized CSApplication getInstance() {
        return sApplication;
    }

    /**
     * 获得网络请求队列
     *
     * @return
     */
    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        mRequestQueue = Volley.newRequestQueue(this);
        VolleyLog.DEBUG = false;
        initImageLoader(getApplicationContext());

        FileDownloader.init(sApplication);
    }

    /**
     * 初始化ImageLoader
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
                context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);// 线程池中线程的个数
        config.denyCacheImageMultipleSizesInMemory();// 拒绝缓存多个图片
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());// 将保存的时候的URI名称用MD5
        // 加密
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);// 设置图片下载和显示的工作队列排序
        // config.writeDebugLogs(); // 打开调试日志,删除不显示日志
        config.defaultDisplayImageOptions(defaultOptions);// 显示图片的参数
        // config.diskCache(new UnlimitedDiskCache(cacheDir));//自定义缓存路径

        ImageLoader.getInstance().init(config.build());
    }

    public static DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true).cacheOnDisk(true)
            .imageScaleType(ImageScaleType.EXACTLY).considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565).build();
}

