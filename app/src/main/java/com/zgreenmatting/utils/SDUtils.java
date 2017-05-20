package com.zgreenmatting.utils;

import android.os.Environment;

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
        return  Environment.getExternalStorageDirectory().getPath()+"/matting/data/matting.db";
    }
}
