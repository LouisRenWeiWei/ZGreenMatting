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
    @Id(autoincrement = true)
    private Long id;//id
    private String name;//背景名称
    private String url;//服务器路径
    private String value;//hash值
    private String sdPath;//下载本地路径
    private int state;//状态
    private long size;
    private long downloadSize;//已经下载的大小
    private long downloadState;//下载状态

    @Generated(hash = 104092732)
    public MattingImage(Long id, String name, String url, String value,
            String sdPath, int state, long size, long downloadSize,
            long downloadState) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.value = value;
        this.sdPath = sdPath;
        this.state = state;
        this.size = size;
        this.downloadSize = downloadSize;
        this.downloadState = downloadState;
    }

    @Generated(hash = 1366292585)
    public MattingImage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDownloadSize() {
        return downloadSize;
    }

    public void setDownloadSize(long downloadSize) {
        this.downloadSize = downloadSize;
    }

    public long getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(long downloadState) {
        this.downloadState = downloadState;
    }
}
