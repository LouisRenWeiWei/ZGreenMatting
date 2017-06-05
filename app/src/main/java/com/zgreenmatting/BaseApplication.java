package com.zgreenmatting;

import android.app.Application;
import android.content.Context;

import com.android.volley.toolbox.Volley;
import com.blankj.utilcode.util.Utils;
import com.igoda.dao.utils.DaoUtils;
import com.liulishuo.filedownloader.FileDownloader;

/**
 * Created by czf on 2017/5/13.
 */

public class BaseApplication extends Application {
    public Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Volley.init(mContext,false);//初始化volley
        DaoUtils.init(mContext);
        //DaoUtils.init(mContext, SDUtils.getDBPath());
        FileDownloader.init(this);
        Utils.init(mContext);
    }

}
