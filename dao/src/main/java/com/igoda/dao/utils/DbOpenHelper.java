package com.igoda.dao.utils;

import android.content.Context;
import com.igoda.dao.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by 任伟伟
 * Datetime: 2016/10/20-16:19
 * Email: renweiwei@ufashion.com
 */

public class DbOpenHelper extends DaoMaster.OpenHelper {
    private Context mContext;
    public DbOpenHelper(Context context, String name) {
        super(context, name);
        mContext = context;
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                ;
            case 2:
                ;
            case 3:
                ;
        }

    }

}
