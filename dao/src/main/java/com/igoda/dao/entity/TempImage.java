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
public class TempImage {
    @Id(autoincrement = true)
    private Long id;//id
    private String value;//hash值
    private String sdPath;

    @Generated(hash = 1772981949)
    public TempImage(Long id, String value, String sdPath) {
        this.id = id;
        this.value = value;
        this.sdPath = sdPath;
    }

    @Generated(hash = 1182085286)
    public TempImage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
