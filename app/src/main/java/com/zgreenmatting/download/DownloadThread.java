package com.zgreenmatting.download;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * DownloadThread
 *
 * @author JF.Chang
 * @date 2015/8/28
 */
public class DownloadThread extends Thread implements Serializable {

    private final static String TAG = DownloadThread.class.getSimpleName();

    /** 本地保存文件 */
    private File saveFile;
    /** 下载路径 */
    private URL downUrl;
    /** 该线程要下载的长度 */
    private long block;
    /** 起始位置 */
    private long startIdx;
    /** 结束位置 */
    private long endIdx;
    /** 该线程已经下载的长度 */
    private long downLength;
    /** 该线程ID */
    private int threadId;
    /** 是否下载完成 */
    private boolean finish = false;
    /** 是否下载中断 */
    private boolean interrupt = false;

    private IDownloadThreadListener downloadTaskListener;
    
//    private MultiTask multiTask;
//
//    public void setMultiTask(MultiTask task){
//        this.multiTask = task;
//    }

    /**
     * @param filePath 文件名,绝对路径
     * @param url 下载地址
     * @param threadId 该线程ID
     * @param downloadTaskListener 回调接口
     */
    public DownloadThread(String filePath, URL url, int threadId, IDownloadThreadListener downloadTaskListener) throws IOException {
        this.saveFile = new File(filePath);
        this.downUrl = url;
        this.threadId = threadId;
        this.downloadTaskListener = downloadTaskListener;
        if (saveFile.exists()) {
            downLength = saveFile.length();
        }
        else {
            this.saveFile.createNewFile();
            downLength = 0;
        }
    }

//    /**
//     * 适应初始化block为0情况
//     * @param block
//     */
//    public void setDownBlockSize(long block) {
//        this.block = block;
//    }

    /**
     * 设置下载文件起始结束位置
     * @param startIdx
     * @param endIdx
     */
    public void setDownloadPosistion(long startIdx, long endIdx) {
        this.startIdx = startIdx;
        this.endIdx = endIdx;
        this.block = endIdx - startIdx;
    }

    public long getDownLength () {
        return downLength;
    }

    public boolean isFinish () {
        return finish;
    }

    /**
     * 暂停/取消
     */
    public void cancel() {
        interrupt = true;
    }
    

    @Override
    public void run() {
        super.run();
        if (downLength < block) {// 未下载完成
            try {
                HttpURLConnection http = (HttpURLConnection) downUrl
                        .openConnection();
                http.setConnectTimeout(5 * 1000);
                http.setRequestMethod("GET");
                http.setRequestProperty(
                        "Accept",
                        "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash,"
                                + " application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, "
                                + "application/x-ms-application, application/vnd.ms-excel,"
                                + " application/vnd.ms-powerpoint, application/msword, */*");
                http.setRequestProperty("Accept-Language", "zh-CN");
                http.setRequestProperty("Referer", downUrl.toString());
                http.setRequestProperty("Charset", "UTF-8");
                // 该线程开始下载位置
                int startPos = (int) (startIdx + downLength);
                // 该线程下载结束位置
                int endPos = (int) (endIdx);
                Log.i("meng", "threadId=" + threadId + ",startPos=" + startPos + ",endPos=" + endPos);
                /*if (threadId != 1 && downLength == 0) {
                    // 设置获取实体数据的范围
                    http.setRequestProperty("Range", "bytes=" + (startPos + 1) + "-" + endPos);
                } else */{
                    // 设置获取实体数据的范围
                    http.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);
                }
                http.setRequestProperty(
                        "User-Agent",
                        "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0;"
                                + " .NET CLR 1.1.4322; .NET CLR 2.0.50727; "
                                + ".NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
                http.setRequestProperty("Connection", "Keep-Alive");
                System.out.println("DownloadThread http.getResponseCode():"
                        + http.getResponseCode());
                int rspCode = http.getResponseCode();
                if (rspCode == 206) {
                    /***
                     * //获取输入流 InputStream inStream = http.getInputStream();
                     * byte[] buffer = new byte[1024]; int offset = 0;
                     * print("Thread " + this.threadId +
                     * " start download from position " + startPos);
                     *
                     * // rwd: 打开以便读取和写入，对于 "rw"，还要求对文件内容的每个更新都同步写入到基础存储设备。
                     * //对于Android移动设备一定要注意同步，否则当移动设备断电的话会丢失数据 RandomAccessFile
                     * threadfile = new RandomAccessFile( this.saveFile, "rwd");
                     * //直接移动到文件开始位置下载的 threadfile.seek(startPos); while
                     * (!downloader.getExit() && (offset = inStream.read(buffer,
                     * 0, 1024)) != -1) { threadfile.write(buffer, 0,
                     * offset);//开始写入数据到文件 downLength += offset; //该线程以及下载的长度增加
                     * downloader.update(this.threadId,
                     * downLength);//修改数据库中该线程已经下载的数据长度
                     * downloader.append(offset);//文件下载器已经下载的总长度增加 }
                     * threadfile.close();
                     *
                     * print("Thread " + this.threadId + " download finish");
                     * this.finish = true;
                     **/
                    // 获取输入流
                    InputStream inStream = http.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(inStream);
                    byte[] buffer = new byte[1024 * 8];
                    int offset = 0;
                    RandomAccessFile threadfile = new RandomAccessFile(
                            this.saveFile, "rwd");
                    // 获取RandomAccessFile的FileChannel
                    FileChannel outFileChannel = threadfile.getChannel();
                    // 直接移动到文件开始位置下载的
                    outFileChannel.position(downLength);
                    // 分配缓冲区的大小
                    while (!interrupt
                            && (offset = bis.read(buffer)) != -1) {
                        outFileChannel.write(ByteBuffer.wrap(buffer, 0, offset));// 开始写入数据到文件
                        // 该线程以及下载的长度增加
                        downLength += offset;
                        onProcess(threadId,downLength);
                    }
                    outFileChannel.close();
                    threadfile.close();
                    inStream.close();
                    if (interrupt) {
                        System.out.println("Thread " + this.threadId + " download cancel");
                        onCancel(threadId);
//                            if(multiTask != null){
//                                multiTask.incrementPauseCount();
//                            }
                    } else {
                        System.out.println("Thread " + this.threadId + " download finish");
                        Log.d("DownloadThread", "Thread " + this.threadId + " download finish");
                        this.finish = true;
                        onFinish(threadId);
                    }
                } else {
                    throw new Exception("{code:"+rspCode +",msg:下载失败!}");
                }
            } catch (Exception e) {
                this.downLength = -1;
                Log.e("DownloadThread", "DownloadFailed--> Thread id " + this.threadId + ":" + e);
                onFailed(threadId, String.valueOf(e));
            }
        } else {
                this.finish = true;
                onFinish(threadId); 
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public boolean equals(Object o) {
        super.equals(o);
        DownloadThread task = (DownloadThread) o;
        if (downUrl == null || task.downUrl == null) return false;
        return downUrl.getPath().equals(task.downUrl.getPath());
    }

    public void onProcess(int threadId, long size) {
        if (downloadTaskListener != null)
            downloadTaskListener.onProcess(threadId, size);
    }

    public void onFinish(int threadId) {
        if (downloadTaskListener != null)
            downloadTaskListener.onFinish(threadId);
    }

    public void onFailed(int threadId, String msg) {
        if (downloadTaskListener != null)
            downloadTaskListener.onFailed(threadId, msg);
    }

    public void onCancel(int threadId) {
        Log.i("meng", "save file size=" + saveFile.length());
        if (downloadTaskListener != null)
            downloadTaskListener.onCancel(threadId);
    }

}
