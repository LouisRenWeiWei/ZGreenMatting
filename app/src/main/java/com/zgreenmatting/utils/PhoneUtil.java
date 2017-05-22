package com.zgreenmatting.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.Locale;
import java.util.UUID;

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
     * 获取devicesID
     *
     * @param mContext
     * @return
     */
    public static String getDevicesID(Context mContext) {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();//device id
        if(TextUtils.isEmpty(deviceId)||deviceId.contains("0000")||deviceId.contains("****")){
            deviceId = tm.getSimSerialNumber();//Sim Serial Number
            if(TextUtils.isEmpty(deviceId)){
                WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
                //mac地址
                deviceId = wifiManager.getConnectionInfo()!=null?wifiManager.getConnectionInfo().getMacAddress():"";
                if(TextUtils.isEmpty(deviceId)){
                    //2.3以上 Serial Number
                    deviceId = Build.SERIAL;
                    if(TextUtils.isEmpty(deviceId)){
                        //当设备首次启动时，系统会随机生成一个64位的数字，并把这个数字以16进制字符串的形式保存下来
                        deviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                        if(TextUtils.isEmpty(deviceId)){
                            deviceId = AppData.getString(mContext,AppData.DEVICE_ID);
                            if(TextUtils.isEmpty(deviceId)){
                                deviceId = UUID.randomUUID().toString();
                                AppData.saveString(mContext,AppData.DEVICE_ID,deviceId);
                            }
                        }
                    }
                }
            }
        }
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
