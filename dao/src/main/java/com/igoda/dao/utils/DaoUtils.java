package com.igoda.dao.utils;

import android.content.Context;

import com.igoda.dao.DaoMaster;
import com.igoda.dao.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * Created by 任伟伟
 * Datetime: 2016/10/20-16:01
 * Email: renweiwei@ufashion.com
 */

public class DaoUtils {
    private static final String DEFAULT_DB_NAME = "greenmatting.db";
    private static volatile DaoMaster daoMaster;

    private static Context mContext;
    private static String DB_NAME;

    /**
     * 在application 的onCreate中调用初始化
     * @param context
     */
    public static void init(Context context) {
        init(context, DEFAULT_DB_NAME);
    }

    public static void init(Context context, String dbName) {
        if (context == null) {
            throw new IllegalArgumentException("context can't be null");
        }
        mContext = context.getApplicationContext();
        DB_NAME = dbName;
        AssetsDBDaoUtils.init(context);
    }

    private static DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            synchronized (DaoUtils.class){
                if(daoMaster==null){
                    DaoMaster.OpenHelper helper = new DbOpenHelper(mContext, DB_NAME);
                    daoMaster = new DaoMaster(helper.getWritableDatabase());
                }
            }
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession() {
        return getDaoMaster().newSession();
    }

    public static void enableQueryBuilderLog(boolean debug) {
        QueryBuilder.LOG_SQL = debug;
        QueryBuilder.LOG_VALUES = debug;
    }
}
