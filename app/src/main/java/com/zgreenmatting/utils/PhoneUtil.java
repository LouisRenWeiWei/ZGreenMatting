package com.zgreenmatting.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

public class PhoneUtil {

    /**
     * 获取devicesID
     * @param mContext
     * @return
     */
    public static String getDevicesID(Context mContext) {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        return DEVICE_ID;
    }

    /**
     * 获取手机品牌
     * @return
     */
    public static String getBrand(){
        String brand = android.os.Build.BRAND;//品牌
        String model = android.os.Build.MODEL;//型号
        return brand+"_"+model;
    }
}
