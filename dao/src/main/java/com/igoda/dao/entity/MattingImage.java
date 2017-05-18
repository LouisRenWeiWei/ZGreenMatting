package com.igoda.dao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by 任伟伟
 * Datetime: 2016/12/9-11:43
 * Email: renweiwei@ufashion.com
 */
@Entity
public class MattingImage {
    @Id
    private String id;//id
    private String name;//名称
    private String path;//服务器路径
    private String sdPath;//下载本地路径
    private int state;//状态
    private long size;
    private long downloadSize;//已经下载的大小
    private long downloadState;//下载状态

    public long getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(long downloadState) {
        this.downloadState = downloadState;
    }

    public long getDownloadSize() {
        return downloadSize;
    }

    public void setDownloadSize(long downloadSize) {
        this.downloadSize = downloadSize;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

   

    @Generated(hash = 1366292585)
    public MattingImage() {
    }

    @Generated(hash = 1112109635)
    public MattingImage(String id, String name, String path, String sdPath,
            int state, long size, long downloadSize, long downloadState) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.sdPath = sdPath;
        this.state = state;
        this.size = size;
        this.downloadSize = downloadSize;
        this.downloadState = downloadState;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSdPath() {
        return sdPath;
    }

    public void setSdPath(String sdPath) {
        this.sdPath = sdPath;
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
