package com.zgreenmatting.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by ASUS on 2017/5/18.
 */

public class SDUtils {
//    public static String getSDPath(){
//        return  Environment.getExternalStorageDirectory().getPath();
//    }
    public static String getAPPSDPath(){
        return  Environment.getExternalStorageDirectory().getPath()+"/matting";
    }
    public static String getGlideSDPath(){
        return  "file://"+Environment.getExternalStorageDirectory().getPath()+"/matting";
    }

    public static String getDBPath() {
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/matting/data/");
        if(!file.exists()){
            file.mkdirs();
        }
        return  Environment.getExternalStorageDirectory().getPath()+"/matting/data/matting.db";
    }

    public static String getImagePath() {
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/matting/image/");
        if(!file.exists()){
            file.mkdirs();
        }
        return  Environment.getExternalStorageDirectory().getPath()+"/matting/image/";
    }
}
