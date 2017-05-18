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

/**
 * MultiTask
 *
 * @author JF Zhang
 * @date 2015/9/23
 */
public class MultiTask extends BaseTask implements IDownloadBaseOption {

    public final static int MAX_THREAD_SIZE = 3;
    /** 该任务的线程 */
    private DownloadThread[] threadPoll;
    /** 附属三条子线程所下载的大小 */
    private long lengthArray[];
    /** 附属三条子线程是否完成 */
    private boolean taskFinish[];
    /** 附属三条子线程是否失败 */
//    private boolean taskFailed[];
    /** 附属三条子线程是否取消 */
    private boolean taskCancel[];
    

    public MultiTask(String fileName, final URL url, final Object entity, List<WeakReference<IDownloadStateListener>> downloadStateListeners) {
        super(fileName, url, entity);
        threadPoll = new DownloadThread[MAX_THREAD_SIZE];
        lengthArray = new long[MAX_THREAD_SIZE];
        taskFinish = new boolean[MAX_THREAD_SIZE];
//        taskFailed = new boolean[MAX_THREAD_SIZE];
        taskCancel = new boolean[MAX_THREAD_SIZE];

        this.downloadStateListeners = downloadStateListeners;

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case _INIT_:
                        for (int i = 1; i <= getMaxThreadSize(); i++) {
                            long startPos = (i - 1) * (block/getMaxThreadSize()) + (i != 1? 1 : 0);
                            long endPos = (i >= getMaxThreadSize())? block : i * (block/getMaxThreadSize());
                            threadPoll[i-1].setDownloadPosistion(startPos, endPos);
                            mExecutor.execute(threadPoll[i-1]);
                        }
                        break;
                }
            }
        };
        //初始化时,计算本地已缓存的文件长度
        for (int i = 1; i <= MAX_THREAD_SIZE; i++) {
            try {
                threadPoll[i-1] = buildDownloadTask(true, i);
                taskFinish[i-1] = false;
                lengthArray[i-1] = threadPoll[i-1].getDownLength();
                downLength += threadPoll[i-1].getDownLength();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        notifyAllToUi(DownloadStatus.WAIT, this.entity, downLength);
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
        for (DownloadThread task : threadPoll) {
            if(task!=null){
                task.cancel();
            }
        }
        notifyAllToUi(DownloadStatus.PAUSE, this.entity, this.downLength);
//        Log.d("WEN", "MultiTask notify onPauseOption" );
    }

    @Override
    public void onStopOption() {
        for (DownloadThread task : threadPoll) {
            if(task!=null){
                task.cancel();
            }
        }
        notifyAllToUi(DownloadStatus.PAUSE, this.entity, this.downLength);
    }

    @Override
    public void onCancelOption() {
        for (DownloadThread task : threadPoll) {
            task.cancel();
        }
    }

    @Override
    protected int getMaxThreadSize() {
        return MAX_THREAD_SIZE;
    }

    @Override
    public void onProcess(int threadId, long size) {
        //根据自定义的线程ID记录该线程目前所下载的文件大小
        lengthArray[threadId - 1] = size;
        long tmp = 0L;
        for (int i = 1; i <= MAX_THREAD_SIZE; i++) {
            tmp += lengthArray[i - 1];
        }
        downLength = tmp;
        notifyAllToUi(DownloadStatus.DLING, this.entity, downLength);
    }

    @Override
    public void onFinish(int threadId) {
        taskFinish[threadId -1] = true;
        boolean tmp = true;
        for (int i = 1; i <= MAX_THREAD_SIZE; i++) {
            tmp = tmp && taskFinish[i - 1];
        }
        if (tmp) {
            String[] subFilePath = new String[MAX_THREAD_SIZE];
            for (int i = 1; i <= MAX_THREAD_SIZE; i++)
                subFilePath[i - 1] = filePath + "_" + i;
            boolean mergeRet = mergeFiles(filePath, subFilePath);
            if (mergeRet) {
                for (String path : subFilePath) {
                    File file = new File(path);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
            notifyAllToUi(DownloadStatus.DONE, this.entity, filePath);
        }
    }

    @Override
    public void onFailed(int threadId, String msg) {
//        taskFailed[threadId -1] = true;
//        boolean tmp = true;
        for (int i = 1; i <= MAX_THREAD_SIZE; i++) {
//            tmp = tmp && taskFinish[i - 1];
            threadPoll[i - 1].cancel();
        }
        notifyAllToUi(DownloadStatus.ERROR, this.entity, msg);
    }

    /**
     *  中断下载时调用（暂停状态，下载任务取消）
     */
    @Override
    public void onCancel(int threadId) {
        notifyAllToUi(DownloadStatus.PAUSE, this.entity, this.downLength);
//        Log.d("WEN", "MultiTask notify PAUSE onCancel +threadid= "+threadId);

//        taskCancel[threadId -1] = true;
//        boolean tmp = true;
//        for (int i = 1; i <= MAX_THREAD_SIZE; i++) {
//            tmp = tmp && taskCancel[i - 1];
//        }
//        if (tmp) {
//            //暂停
//            notifyAllToUi(DownloadStatus.PAUSE, this.entity, this.downLength);
//            Log.d("WEN", "MultiTask notify PAUSE onCancel +threadid= "+threadId);
//        }
    }
}
