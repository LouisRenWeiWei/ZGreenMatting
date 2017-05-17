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
public class Area {
    @Id
    private String id;
    private String parent_id;
    private String name;
    private String code;
    private int type;
    private int del_flag;

    @Generated(hash = 1312722674)
    public Area(String id, String parent_id, String name, String code, int type,
            int del_flag) {
        this.id = id;
        this.parent_id = parent_id;
        this.name = name;
        this.code = code;
        this.type = type;
        this.del_flag = del_flag;
    }

    @Generated(hash = 179626505)
    public Area() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDel_flag() {
        return del_flag;
    }

    public void setDel_flag(int del_flag) {
        this.del_flag = del_flag;
    }


    @Override
    public String toString() {
        return getName();
    }

}
