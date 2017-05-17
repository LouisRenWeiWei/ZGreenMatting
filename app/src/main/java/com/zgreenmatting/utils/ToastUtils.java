package com.zgreenmatting.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zgreenmatting.R;


/**
 * Created by 任伟伟
 * Datetime: 2016/11/5-10:33
 * Email: renweiwei@ufashion.com
 */

public class ToastUtils {
    @Deprecated
    public static void showSystemToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
    @Deprecated
    public static void showSystemToast(Context context,int res){
        Toast.makeText(context,context.getString(res),Toast.LENGTH_SHORT).show();
    }
    public static void showCustomerToast(Context context,String message){
        View layout = LayoutInflater.from(context).inflate(R.layout.toast_custome,null);
        TextView tv_message = (TextView) layout.findViewById(R.id.tv_message);
        tv_message.setText(message);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

}
