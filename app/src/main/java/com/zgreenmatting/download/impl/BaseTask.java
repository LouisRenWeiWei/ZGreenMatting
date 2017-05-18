package com.zgreenmatting.download.impl;

import android.os.Handler;
import android.util.Log;


import com.zgreenmatting.download.DownloadController;
import com.zgreenmatting.download.DownloadThread;
import com.zgreenmatting.download.IDownloadBaseOption;
import com.zgreenmatting.download.IDownloadStateListener;
import com.zgreenmatting.download.IDownloadThreadListener;
import com.zgreenmatting.download.config.DownloadConstants;
import com.zgreenmatting.download.status.DownloadStatus;
import com.zgreenmatting.download.utils.FileUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public abstract class BaseTask implements IDownloadBaseOption, IDownloadThreadListener {

    protected final static int _INIT_ = 0x01;

    /** 本地保存文件 */
    protected String filePath;
    /** 下载路径 */
    protected final URL url;
    /** 该文件的总长度 */
    protected long block;
    /** 已经下载的长度 */
    protected long downLength;
    /** 下载状态 */
    protected List<WeakReference<IDownloadStateListener>> downloadStateListeners;
    /** 初始线程 */
    protected Thread thread;
    /**  */
    protected Handler handler;
    /** 下载对象 */
    protected Object entity;

    protected ExecutorService mExecutor = Executors.newCachedThreadPool();
    /**
     * 增加下载对象方便上层使用
     * @param fileName
     * @param url
     * @param entity
     */
    public BaseTask(String fileName, final URL url, final Object entity) {
        this.entity = entity;

        FileUtils.createDir(DownloadConstants.IMG_PATH);
        //如:/mnt/sdcard/MMAssistant/image/dd.png
        this.filePath = DownloadConstants.IMG_PATH + "/" + fileName;
        this.url = url;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5 * 1000);
                    conn.setRequestMethod("GET");
                    int rspCode = conn.getResponseCode();
                    if (rspCode != 200) {
                        throw new IOException("response code:" + rspCode);
                    }
                    block = conn.getContentLength();
                    handler.sendEmptyMessage(_INIT_);
                } catch (Exception e) {
                    if (downloadStateListeners != null) {
                        for (WeakReference reference : downloadStateListeners) {
                            IDownloadStateListener stateListener = (IDownloadStateListener) reference.get();
                            if (stateListener != null)
                                stateListener.onFailed(entity, String.valueOf(e));
                        }
                    }
                }
            }
        });
    }

    protected abstract int getMaxThreadSize();

    /**
     * 构造新的下载任务
     * @param isMulite 是否多线程
     * @param threadId 线程ID
     * @return
     * @throws IOException
     */
    protected DownloadThread buildDownloadTask(boolean isMulite, int threadId) throws IOException {
//        if (isMulite) {
//            return new DownloadThread(filePath + "_" + threadId, url, threadId, this);
//        } else {
//            return new DownloadThread(filePath+ "_single", url, threadId, this);
//        }
        return new DownloadThread(buildFileName(isMulite, threadId), url, threadId, this);
    }

    /**
     * 构造临时下载文件名
     * @param isMulite
     * @param threadId
     * @return
     */
    public String buildFileName(boolean isMulite, int threadId) {
        if (isMulite) {
            return filePath + "_" + threadId;
        } else {
            return filePath+ "_single";
        }
    }
    
    @Override
    public boolean equals(Object o) {
        super.equals(o);
        BaseTask strate = (BaseTask) o;
        if (url == null || strate.url == null) return false;
        return url.getPath().equals(strate.url.getPath());
    }

    /**
     * 获取应用
     * @return
     */
    public Object getEntity() {
        return entity;
    }

    /**
     * 合并文件
     * @param outFile
     * @param files
     */
    public boolean mergeFiles(String outFile, String[] files) {
        FileChannel outChannel = null;
        System.out.println("Merge " + Arrays.toString(files) + " into " + outFile);
        boolean isSuccess = false;
        try {
            outChannel = new FileOutputStream(outFile).getChannel();
            for(String f : files){
                FileChannel fc = new FileInputStream(f).getChannel();
                ByteBuffer bb = ByteBuffer.allocate(1024 * 8);
                while(fc.read(bb) != -1){
                    bb.flip();
                    outChannel.write(bb);
                    bb.clear();
                }
                fc.close();
            }
            System.out.println("Merged!! ");
//            return true;
            isSuccess = true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {if (outChannel != null) {outChannel.close();}} catch (IOException ignore) {}
        }
        return isSuccess;
    }

    /**
     * 通知更新
     * @param status
     * @param params
     */
    protected void notifyAllToUi(DownloadStatus status, Object... params) {
        if (downloadStateListeners != null) {
            for (WeakReference<IDownloadStateListener> reference : downloadStateListeners) {
                IDownloadStateListener stateListener = reference.get();
                if (stateListener != null) {
                    switch (status) {
                        case WAIT:
                            stateListener.onPrepare(params[0], (Long) params[1]);
                            break;
                        case DLING:
                            stateListener.onProcess(params[0], (Long) params[1]);
                            break;
                        case ERROR:
                            stateListener.onFailed(params[0], (String) params[1]);
                            DownloadController.INSTANCE.onFailedTask(params[0]);
                            break;
                        case PAUSE:
                            stateListener.onPause(params[0], (Long) params[1]);
                            break;
                        case DONE:
                            stateListener.onFinish(params[0], (String) params[1]);
                            DownloadController.INSTANCE.onFinishTask(params[0]);
                            break;
                        case NONE:
                            stateListener.onCancel(params[0]);
                            break;
                    }
                }
            }
        } else {
            Log.e("MultiTask", "notifyAll failed: state listener is null");
        }
    }
}
