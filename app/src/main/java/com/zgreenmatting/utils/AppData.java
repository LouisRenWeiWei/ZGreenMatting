package com.zgreenmatting.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 任伟伟
 * Datetime: 2016/10/26-16:27
 */
public class AppData {
    public static final String ACCOUNT = "ACCOUNT";
    public static final String PASSWORD = "PASSWORD";
    public static SharedPreferences getSP(Context context){
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
    }

    public static void saveString(Context context,String key,String value){
        SharedPreferences.Editor editor = getSP(context).edit();
        editor.putString(key,value);
        editor.commit();
    }
    public static String getString(Context context,String key){
        return getSP(context).getString(key,"");
    }

}