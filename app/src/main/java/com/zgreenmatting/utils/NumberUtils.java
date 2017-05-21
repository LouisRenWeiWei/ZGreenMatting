package com.zgreenmatting.utils;

import java.text.DecimalFormat;

/**
 * Created by ASUS on 2017/5/21.
 */

public class NumberUtils {
    public static String format(float value){
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        return decimalFormat.format(value);
    }
}
