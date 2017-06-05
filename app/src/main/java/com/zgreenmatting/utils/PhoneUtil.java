package com.zgreenmatting.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.blankj.utilcode.util.DeviceUtils;

import java.util.Locale;

public class PhoneUtil {

    /**
     * 获取APP版本名
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    /**
     * 获取APP版本号
     * @param context
     * @return
     */
    public static String getVersionCode(Context context) {
        return getPackageInfo(context).versionCode + "";
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }

    /**
     * 获取设备ID devicesID
     *
     * @return
     */
    public static String getDevicesID() {
        String deviceId = DeviceUtils.getAndroidID();
        return deviceId;
    }

    /**
     * 获取手机品牌及型号
     *
     * @return
     */
    public static String getBrand() {
        String brand = android.os.Build.BRAND;//品牌
        String model = android.os.Build.MODEL;//型号
        return brand + "_" + model;
    }

    /**
     * 获取手机操作系统语言
     * @return  返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage(){
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取手机上的语言列表
     * @return
     */
    public static Locale[] getSystemLanguageList(){
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     * @return  系统版本号
     */
    public static String getSystemVersion(){
        return android.os.Build.VERSION.RELEASE;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
