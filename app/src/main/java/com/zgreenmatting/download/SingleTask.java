package com.zgreenmatting.download;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;


import com.zgreenmatting.download.impl.BaseTask;
import com.zgreenmatting.download.status.DownloadStatus;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;


public class SingleTask extends BaseTask implements IDownloadBaseOption {

    /** 下载任务 */
    private DownloadThread downloadThread;

    public SingleTask(String fileName, final URL url, final Object entity, List<WeakReference<IDownloadStateListener>> downloadStateListeners) {
        super(fileName, url, entity);

        this.downloadStateListeners = downloadStateListeners;

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case _INIT_:
//                        downloadThread.setDownBlockSize(block);
                        downloadThread.setDownloadPosistion(0, block);
//                        downloadThread.start();
                        mExecutor.execute(downloadThread);
                        break;
                }
            }
        };
        try {
            downloadThread = buildDownloadTask(false, 1);
            downLength = downloadThread.getDownLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        notifyAllToUi(DownloadStatus.WAIT, this.entity, this.downLength);
    }

    @Override
    protected int getMaxThreadSize() {
        return 1;
    }

    @Override
    public void onPrepareOption() {
        notifyAllToUi(DownloadStatus.WAIT, this.entity, downLength);
    }

    @Override
    public void onStartOption() {
        if (!thread.isAlive())
            mExecutor.execute(thread);
    }


    @Override
    public void onPauseOption() {
        if (downloadThread != null)
            downloadThread.cancel();
        notifyAllToUi(DownloadStatus.PAUSE, this.entity, this.downLength);
    }

    @Override
    public void onStopOption() {
        if (downloadThread != null)
            downloadThread.cancel();
        notifyAllToUi(DownloadStatus.PAUSE, this.entity, this.downLength);
    }

    @Override
    public void onCancelOption() {
        if (downloadThread != null)
            downloadThread.cancel();
        notifyAllToUi(DownloadStatus.NONE, this.entity);
    }

    @Override
    public void onProcess(int threadId, long size) {
        this.downLength = size;
        notifyAllToUi(DownloadStatus.DLING, this.entity, downLength);
    }

    @Override
    public void onFinish(int threadId) {
        new File(buildFileName(false, 1)).renameTo(new File(filePath));
        notifyAllToUi(DownloadStatus.DONE, this.entity, filePath);
    }

    @Override
    public void onFailed(int threadId, String msg) {
        notifyAllToUi(DownloadStatus.ERROR, this.entity, msg);
    }

    @Override
    public void onCancel(int threadId) {
        notifyAllToUi(DownloadStatus.NONE, this.entity);
    }
}
