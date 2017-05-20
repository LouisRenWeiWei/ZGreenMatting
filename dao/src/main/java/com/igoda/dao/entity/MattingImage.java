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
    private String url;//服务器路径
    private String value;//hash值
    private String ext;
    private String createTime;//创建时间

    private String name;//背景名称   Deprecated

    private String sdPath;//下载本地路径
    private int downloadState;//下载状态

    @Generated(hash = 1486791468)
    public MattingImage(Long id, String url, String value, String ext,
            String createTime, String name, String sdPath, int downloadState) {
        this.id = id;
        this.url = url;
        this.value = value;
        this.ext = ext;
        this.createTime = createTime;
        this.name = name;
        this.sdPath = sdPath;
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

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSdPath() {
        return sdPath;
    }

    public void setSdPath(String sdPath) {
        this.sdPath = sdPath;
    }

    public int getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
    }
}
