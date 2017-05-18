package com.zgreenmatting.download.config;

import android.os.Environment;

public class DownloadConstants {

    public static final String SDCARD_BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    /**
     * project's root folder path
     */
    public static final String APP_BASE_PATH = SDCARD_BASE_PATH + "/matting";
    /**
     * image folder path
     */
    public static final String IMG_PATH = APP_BASE_PATH + "/image";


}
