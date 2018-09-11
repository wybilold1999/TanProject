package com.cyanbirds.tanlove.net.request;

import com.cyanbirds.tanlove.CSApplication;
import com.cyanbirds.tanlove.entity.IMessage;
import com.cyanbirds.tanlove.listener.FileProgressListener;
import com.cyanbirds.tanlove.listener.NetFileDownloadListener;
import com.cyanbirds.tanlove.net.base.ResultPostExecute;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

/**
 * 
 * @Description:下载图片请求
 * @author wangyb
 * @Date:2015年7月10日下午5:06:04
 */

public class DownloadImageRequest extends ResultPostExecute<File> {

	/**
	 * 下载请求
	 *
	 * @param url
	 *            请求地址
	 * @param savePath
	 *            保存地址
	 * @param fileName 保存文件名
	 * @param message
	 *            当不是消息的时候可以为空
	 */
	public void request(String url, String savePath,
						String fileName,final IMessage message) {
		final File file = new File(savePath, fileName);
		FileDownloader.setup(CSApplication.getInstance());
		FileDownloader.getImpl().create(url)
				.setPath(file.getAbsolutePath())
				.setListener(new NetFileDownloadListener(){
					@Override
					protected void completed(BaseDownloadTask task) {
						onPostExecute(file);
						if (message != null) {
							message.localPath = file.getPath();
							FileProgressListener.getInstance()
									.notifyFileProgressChanged(message, 100);
						}
					}

					@Override
					protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
						int progress = (int) ((totalBytes > 0) ? (soFarBytes * 1.0 / totalBytes) * 100
								: -1);
						if (progress >= 100) {
							if (message != null)
								message.status = IMessage.MessageStatus.RECEIVED;
						}
						if (message != null)
							FileProgressListener.getInstance()
									.notifyFileProgressChanged(message, progress);
					}

					@Override
					protected void error(BaseDownloadTask task, Throwable e) {
						onErrorExecute("");
					}
				}).start();
	}
}
