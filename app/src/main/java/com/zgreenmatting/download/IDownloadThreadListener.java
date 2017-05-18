package com.zgreenmatting.download;

/**
 * DownloadTaskListener
 *
 * @author JF.Chang
 * @date 2015/8/27
 */
public interface IDownloadThreadListener {

	//正在下载
	void onProcess(int threadId, long size);
	//完成
	void onFinish(int threadId);
	//失败
	void onFailed(int threadId, String msg);
	//取消
	void onCancel(int threadId);
}
