package com.zgreenmatting.download;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


import com.igoda.dao.entity.MattingImage;
import com.zgreenmatting.download.impl.BaseTask;
import com.zgreenmatting.download.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public enum DownloadManager {
    INSTANCE;

    private static final String TAG = DownloadManager.class.getSimpleName();

    //队列最大容量
    public final static int MAX_DOWNLOAD_SIZE = 99;
    //可执行下载的任务数
    public final static int MAX_DOWNLING_PROCESS_SIZE = 2;

    private Context mContext;
    // 等待下载队列
    private BlockingQueue<BaseTask> mTaskQueue;
    //正在执行的任务列表
    private List<BaseTask> mDownloadingTasks;
    //暂停执行的任务列表
    private List<BaseTask> mPausingTasks;
    //错误的任务列表
    private List<BaseTask> mErrorTasks;
    /**
     * 队列互斥变量
     */
//    private Object mutex = new Object();

    private Boolean isInterrupt = false;

    private boolean isAllPause = false; //全部暂停

    private PollThread pollThread;

    private volatile int mDownloadCount;

//    private DownloadTaskType downloadType;//default cdn

    private List<WeakReference<IDownloadStateListener>> downloadStateListeners;

    /**
     * 注册状态监听
     */
    public void registerStateListener(IDownloadStateListener downloadStateListener) {
        downloadStateListeners.add(new WeakReference<IDownloadStateListener>(downloadStateListener));
    }

    /**
     * 取消注册状态监听
     */
    public void unRegisterStateListener(IDownloadStateListener downloadStateListener) {
        Iterator<WeakReference<IDownloadStateListener>> iterator = downloadStateListeners.iterator();
        while (iterator.hasNext()) {
            WeakReference<IDownloadStateListener> listenerWeakReference = iterator.next();
            IDownloadStateListener listener = listenerWeakReference.get();
            if (listener != null && listener == downloadStateListener) {
                iterator.remove();
            }
        }
    }


    public DownloadManager init(Context context) throws IOException {
        mContext = context;
        downloadStateListeners = new LinkedList<WeakReference<IDownloadStateListener>>();
        mTaskQueue = new LinkedBlockingQueue<BaseTask>(MAX_DOWNLOAD_SIZE);
        mDownloadingTasks = Collections.synchronizedList(new ArrayList<BaseTask>());
        mPausingTasks = Collections.synchronizedList(new ArrayList<BaseTask>());
        mErrorTasks = Collections.synchronizedList(new ArrayList<BaseTask>());
        pollThread = new PollThread();
        return this;
    }

    private Handler uiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x01:
                    BaseTask task = (BaseTask) msg.obj;
                    if (task != null)
                        task.onStartOption();
                    break;
            }
        }
    };

    //取任务线程
    private class PollThread extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                takeTask();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                Log.e(TAG, "Poll DownloadThread Exception:" + e1);
            }
        }

        private void takeTask() throws InterruptedException {
            while (!isInterrupt) {
                BaseTask task = null;
                Log.i(TAG, "Poll DownloadThread downloading size:" + mDownloadCount);
                if (mDownloadCount < MAX_DOWNLING_PROCESS_SIZE) {
                    task = mTaskQueue.take();
                }
                if (task != null) {
                    if(mDownloadingTasks.size()< MAX_DOWNLING_PROCESS_SIZE){
                        mDownloadingTasks.add(task);
                        Log.d("WEN", "mDownloadingTasks.add(task);");
                        mDownloadCount = mDownloadingTasks.size();
                        Message msg = uiHandler.obtainMessage();
                        msg.what = 0x01;
                        msg.obj = task;
                        msg.sendToTarget();
                    }
                }
            }
        }
    }

    /**
     * 开启
     */
    public void onStart() {
        Log.i(TAG, "DownloadManage start");
        if (!pollThread.isAlive()) {
            pollThread.start();
        }
    }

    /**
     * 销毁
     */
    public void onDestroy() {
        isInterrupt = true;
        mTaskQueue.clear();
        mDownloadingTasks.clear();
        mErrorTasks.clear();
        mPausingTasks.clear();
    }

    public boolean isRunning() {
        return !isInterrupt;
    }

    //计算所有的任务数
    public int getTotalTaskCount() {
        Log.i(TAG, "mErrorTasks.size():" + mErrorTasks.size());
        return mTaskQueue.size() + mDownloadingTasks.size() + mPausingTasks.size() + mErrorTasks.size();
    }

    /**
     * 是否有该任务
     *
     * @return
     */
    private boolean isExists(BaseTask task) {
        return mTaskQueue.contains(task) ||
                mDownloadingTasks.contains(task) ||
                mPausingTasks.contains(task) ||
                mErrorTasks.contains(task);
    }

    /**
     * 构造一个新的下载任务
     *
     * @param entity
     * @return
     */
    public BaseTask buildNewTask(MattingImage entity, DownloadTaskType type) {
        BaseTask strate = null;
        try {
            String fileName = entity.getName();
            switch (type) {
                case MULTI://cdn
                    URL url4Multi = new URL(entity.getUrl());
                    strate = new MultiTask(fileName, url4Multi, entity, downloadStateListeners);//test
                    break;
                default://https
                    URL url4Single = new URL(entity.getUrl());
                    strate = new SingleTask(fileName, url4Single, entity, downloadStateListeners);
                    break;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return strate;
    }

    /**
     * 添加任务 url
     */
    public void onOffer(MattingImage entity) {
        if (!FileUtils.isSDMounted()) {
            new Handler(mContext.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "Not Found SD Card", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }
        if (getTotalTaskCount() >= MAX_DOWNLOAD_SIZE) {
            new Handler(mContext.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "任务太多啦，等我先下载完一部分哈", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }
        BaseTask task = buildNewTask(entity, DownloadTaskType.SINGLE);
        if (!isExists(task)) {
            onOffer(task);
        }
    }

    /**
     * 添加任务 task
     *
     * @param task
     */
    private boolean onOffer(BaseTask task) {
        boolean ret = false;
        if (isAllPause) {
            ret = mPausingTasks.add(task);
        } else {
            ret = mTaskQueue.offer(task);
            task.onPrepareOption();
            Log.d("WEN", "等待队列中增加1个任务，并通知消费者");

        }
        return ret;
    }

    /**
     * 停止单个任务
     */
    public void onPause(MattingImage entity) {
        BaseTask task = buildNewTask(entity,DownloadTaskType.SINGLE);
        onPause(task);
    }

    private void onPause(BaseTask task) {
        if (mDownloadingTasks.contains(task)) {
            int idx = mDownloadingTasks.indexOf(task);
            BaseTask dlTask = mDownloadingTasks.get(idx);
            mDownloadingTasks.remove(idx);
            dlTask.onPauseOption();
            mDownloadCount--;
        } else if (mTaskQueue.contains(task)) {
            mTaskQueue.remove(task);
        }
        if (!mPausingTasks.contains(task))
            mPausingTasks.add(task);

    }

    /**
     * 删除单个任务
     */
    public void onCancel(MattingImage entity) {
        BaseTask task = buildNewTask(entity, DownloadTaskType.SINGLE);
        if (mTaskQueue.contains(task)) {
            mTaskQueue.remove(task);
        } else if (mDownloadingTasks.contains(task)) {
            int idx = mDownloadingTasks.indexOf(task);
            BaseTask dlTask = mDownloadingTasks.get(idx);
            dlTask.onCancelOption();
            mDownloadingTasks.remove(idx);
            mDownloadCount--;
        } else if (mPausingTasks.contains(task)) {
            mPausingTasks.remove(task);
        } else if (mErrorTasks.contains(task)) {
            mErrorTasks.remove(task);
        } else {
            Log.w(TAG, "can not find task :" + entity.getUrl());
        }
    }

    /**
     * 暂停状态恢复下载任务;把任务放回调度队列
     */
    public void onContinue(MattingImage entity) {
        BaseTask task = buildNewTask(entity, DownloadTaskType.SINGLE);
        if (mPausingTasks.contains(task)) {
            if (onOffer(task)) {
                mPausingTasks.remove(task);
            }
        } else if (mErrorTasks.contains(task)) {
            if (onOffer(task)) {
                mErrorTasks.remove(task);
            }
        }
    }

    public void onFailed(MattingImage entity){
        BaseTask task = buildNewTask(entity, DownloadTaskType.SINGLE);
        onFailed(task);
    }

    public  void onFailed(BaseTask task){
        if(mDownloadingTasks.contains(task)){
            int idx = mDownloadingTasks.indexOf(task);
            mDownloadingTasks.remove(idx);
            mDownloadCount--;
        }else if (mTaskQueue.contains(task)) {
            mTaskQueue.remove(task);
        }
    }

    public void onFinished(MattingImage entity) {
        //调用buildNewTask会产生临时文件,
        // 所以此处调用了,就应该把生成的临时文件删除
        BaseTask task = buildNewTask(entity, DownloadTaskType.SINGLE);
        onFinished(task);
//        //delete temp files
//        switch (entity.downloadType) {
//            case MULTI:
//                for (int i = 1; i <= MultiTask.MAX_THREAD_SIZE; i++) {
//                    String fileName = task.buildFileName(true, i);
//                    File file = new File(fileName);
//                    if (file.exists()) file.delete();
//                }
//                break;
//            default:
//                String fileName = task.buildFileName(false, 1);
//                File file = new File(fileName);
//                if (file.exists()) file.delete();
//                break;
//        }
        String fileName = task.buildFileName(false, 1);
        File file = new File(fileName);
        if (file.exists()) file.delete();
    }

    private void onFinished(BaseTask task){
        if(mDownloadingTasks.contains(task)){
            int idx = mDownloadingTasks.indexOf(task);
            mDownloadingTasks.remove(idx);
            mDownloadCount--;
            Log.d("WEN", "下载完成，从下载队列中移除-" + task.getEntity().toString() + ",队列还有:" + mTaskQueue.size());

        }else if(mTaskQueue.contains(task)){
            mTaskQueue.remove(task);
        }
    }
}
