package com.igoda.dao.utils;

import android.content.Context;

import com.igoda.dao.DaoMaster;
import com.igoda.dao.DaoSession;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 任伟伟
 * Datetime: 2016/10/20-16:01
 * Email: renweiwei@ufashion.com
 */

public class AssetsDBDaoUtils {
    private static final String db_area = "area.db";
    private static volatile Map<String,DaoMaster> daoMaster = new HashMap<>();
    private static Context mContext;
    public static void init(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context can't be null");
        }
        mContext = context.getApplicationContext();
    }
    private static DaoMaster getDaoMaster(String db_name){
        DaoMaster dbMaster = daoMaster.get(db_name);
        if(dbMaster==null){
            dbMaster = new DaoMaster(AssetsDatabaseManager.getInstance(mContext).getDatabase(db_area));
            daoMaster.put(db_name,dbMaster);
        }
        return dbMaster;
    }
    public static DaoSession getAreaDaoSession(){
        return getDaoMaster(db_area).newSession();
    }
}
