package com.igoda.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.igoda.dao.entity.MattingImage;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MATTING_IMAGE".
*/
public class MattingImageDao extends AbstractDao<MattingImage, String> {

    public static final String TABLENAME = "MATTING_IMAGE";

    /**
     * Properties of entity MattingImage.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Path = new Property(2, String.class, "path", false, "PATH");
        public final static Property SdPath = new Property(3, String.class, "sdPath", false, "SD_PATH");
        public final static Property State = new Property(4, int.class, "state", false, "STATE");
        public final static Property Size = new Property(5, long.class, "size", false, "SIZE");
        public final static Property DownloadSize = new Property(6, long.class, "downloadSize", false, "DOWNLOAD_SIZE");
        public final static Property DownloadState = new Property(7, long.class, "downloadState", false, "DOWNLOAD_STATE");
    }


    public MattingImageDao(DaoConfig config) {
        super(config);
    }
    
    public MattingImageDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MATTING_IMAGE\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"PATH\" TEXT," + // 2: path
                "\"SD_PATH\" TEXT," + // 3: sdPath
                "\"STATE\" INTEGER NOT NULL ," + // 4: state
                "\"SIZE\" INTEGER NOT NULL ," + // 5: size
                "\"DOWNLOAD_SIZE\" INTEGER NOT NULL ," + // 6: downloadSize
                "\"DOWNLOAD_STATE\" INTEGER NOT NULL );"); // 7: downloadState
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MATTING_IMAGE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MattingImage entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(3, path);
        }
 
        String sdPath = entity.getSdPath();
        if (sdPath != null) {
            stmt.bindString(4, sdPath);
        }
        stmt.bindLong(5, entity.getState());
        stmt.bindLong(6, entity.getSize());
        stmt.bindLong(7, entity.getDownloadSize());
        stmt.bindLong(8, entity.getDownloadState());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MattingImage entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(3, path);
        }
 
        String sdPath = entity.getSdPath();
        if (sdPath != null) {
            stmt.bindString(4, sdPath);
        }
        stmt.bindLong(5, entity.getState());
        stmt.bindLong(6, entity.getSize());
        stmt.bindLong(7, entity.getDownloadSize());
        stmt.bindLong(8, entity.getDownloadState());
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public MattingImage readEntity(Cursor cursor, int offset) {
        MattingImage entity = new MattingImage( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // path
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // sdPath
            cursor.getInt(offset + 4), // state
            cursor.getLong(offset + 5), // size
            cursor.getLong(offset + 6), // downloadSize
            cursor.getLong(offset + 7) // downloadState
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MattingImage entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPath(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSdPath(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setState(cursor.getInt(offset + 4));
        entity.setSize(cursor.getLong(offset + 5));
        entity.setDownloadSize(cursor.getLong(offset + 6));
        entity.setDownloadState(cursor.getLong(offset + 7));
     }
    
    @Override
    protected final String updateKeyAfterInsert(MattingImage entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(MattingImage entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MattingImage entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
