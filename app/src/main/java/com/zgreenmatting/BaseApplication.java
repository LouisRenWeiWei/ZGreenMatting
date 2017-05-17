package com.zgreenmatting;

import android.app.Application;
import android.content.Context;

import com.android.volley.toolbox.Volley;

/**
 * Created by czf on 2017/5/13.
 */

public class BaseApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Volley.init(mContext,false);
    }

}
