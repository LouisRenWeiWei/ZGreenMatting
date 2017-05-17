package com.igoda.dao.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ren Weiwei on 2016/1/28 14:27.
 * Email: rww_2010@126.com
 */
public class AssetsDatabaseManager {
    private static String tag = "AssetsDatabase";
    private static String databasepath = "/data/data/%s/database"; // %s is packageName
    private Map<String, SQLiteDatabase> databases = new HashMap<String, SQLiteDatabase>();

    private Context context = null;

    private static AssetsDatabaseManager instance = null;

    private AssetsDatabaseManager(Context context) {
        this.context = context;
    }

    public static AssetsDatabaseManager getInstance(Context context) {
        if (instance == null) {
            synchronized (AssetsDatabaseManager.class) {
                if (instance == null) instance = new AssetsDatabaseManager(context);
            }
        }
        return instance;
    }

    public SQLiteDatabase getDatabase(String dbfile) {
        if (databases.get(dbfile) != null) {
            Log.i(tag, String.format("Return a database copy of %s", dbfile));
            return (SQLiteDatabase) databases.get(dbfile);
        }
        if (context == null)
            return null;

        Log.i(tag, String.format("Create database %s", dbfile));
        String spath = getDatabaseFilepath();
        String sfile = getDatabaseFile(dbfile);

        File file = new File(sfile);
        SharedPreferences sp = context.getSharedPreferences(AssetsDatabaseManager.class.toString(), 0);
        boolean flag = sp.getBoolean(dbfile, false); // Get Database file flag, if true means this database file was copied and valid
        if (!flag || !file.exists()) {
            file = new File(spath);
            if (!file.exists() && !file.mkdirs()) {
                Log.i(tag, "Create \"" + spath + "\" fail!");
                return null;
            }
            if (!copyAssetsToFilesystem(dbfile, sfile)) {
                Log.i(tag, String.format("Copy %s to %s fail!", dbfile, sfile));
                return null;
            }
            sp.edit().putBoolean(dbfile, true).commit();
        }

        SQLiteDatabase db = SQLiteDatabase.openDatabase(sfile, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        if (db != null) {
            databases.put(dbfile, db);
        }
        return db;
    }

    private String getDatabaseFilepath() {
        return String.format(databasepath, context.getApplicationInfo().packageName);
    }

    private String getDatabaseFile(String dbfile) {
        return getDatabaseFilepath() + "/" + dbfile;
    }

    private boolean copyAssetsToFilesystem(String assetsSrc, String des) {
        Log.i(tag, "Copy " + assetsSrc + " to " + des);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            AssetManager am = context.getAssets();
            inputStream = am.open(assetsSrc);
            outputStream = new FileOutputStream(des);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            return false;
        }
        return true;
    }

    public boolean closeDatabase(String dbfile) {
        if (databases.get(dbfile) != null) {
            SQLiteDatabase db = (SQLiteDatabase) databases.get(dbfile);
            db.close();
            databases.remove(dbfile);
            return true;
        }
        return false;
    }

    public static void closeAllDatabase() {
        Log.i(tag, "closeAllDatabase");
        if (instance != null) {
            for (int i = 0; i < instance.databases.size(); ++i) {
                if (instance.databases.get(i) != null) {
                    instance.databases.get(i).close();
                }
            }
            instance.databases.clear();
        }
    }
}
