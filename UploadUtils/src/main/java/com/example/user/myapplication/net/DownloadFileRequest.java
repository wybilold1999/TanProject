package com.example.user.myapplication.net;

import android.util.Log;

import com.example.user.myapplication.ResultPostExecute;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

/***
 * @author zxj
 * @ClassName:DownloadFileRequest
 * @Description:下载文件请求
 * @Date:2015年6月9日下午9:00:21
 */
@SuppressWarnings("deprecation")
public class DownloadFileRequest extends ResultPostExecute<String> {

    /**
     * 下载请求
     *
     * @param url          请求地址
     * @param savePath     保存地址
     * @param fileName     保存文件名
     */
    public void request(final String url, final String savePath,
                        String fileName) {
        final File file = new File(savePath, fileName);
        FileDownloader.getImpl().create(url)
                .setPath(file.getAbsolutePath())
                .setListener(new NetFileDownloadListener(){
                    @Override
                    protected void completed(BaseDownloadTask task) {
                        onPostExecute(file.getPath());
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        onErrorExecute("下载失败");
                        Log.d("test", "下载失败的url=" + url);
                    }
                }).start();
    }

}
