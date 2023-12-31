package com.sh3h.dataprovider.greendaoDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.sh3h.dataprovider.data.entity.response.JJCS;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "JJCS".
*/
public class JJCSDao extends AbstractDao<JJCS, Void> {

    public static final String TABLENAME = "JJCS";

    /**
     * Properties of entity JJCS.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property CS_ID = new Property(0, String.class, "CS_ID", false, "CS__ID");
        public final static Property DESCR = new Property(1, String.class, "DESCR", false, "DESCR");
        public final static Property FA_TYPE_CD = new Property(2, String.class, "FA_TYPE_CD", false, "FA__TYPE__CD");
        public final static Property FA_TYPE_DESCR = new Property(3, String.class, "FA_TYPE_DESCR", false, "FA__TYPE__DESCR");
    }


    public JJCSDao(DaoConfig config) {
        super(config);
    }
    
    public JJCSDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"JJCS\" (" + //
                "\"CS__ID\" TEXT," + // 0: CS_ID
                "\"DESCR\" TEXT," + // 1: DESCR
                "\"FA__TYPE__CD\" TEXT," + // 2: FA_TYPE_CD
                "\"FA__TYPE__DESCR\" TEXT);"); // 3: FA_TYPE_DESCR
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"JJCS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, JJCS entity) {
        stmt.clearBindings();
 
        String CS_ID = entity.getCS_ID();
        if (CS_ID != null) {
            stmt.bindString(1, CS_ID);
        }
 
        String DESCR = entity.getDESCR();
        if (DESCR != null) {
            stmt.bindString(2, DESCR);
        }
 
        String FA_TYPE_CD = entity.getFA_TYPE_CD();
        if (FA_TYPE_CD != null) {
            stmt.bindString(3, FA_TYPE_CD);
        }
 
        String FA_TYPE_DESCR = entity.getFA_TYPE_DESCR();
        if (FA_TYPE_DESCR != null) {
            stmt.bindString(4, FA_TYPE_DESCR);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, JJCS entity) {
        stmt.clearBindings();
 
        String CS_ID = entity.getCS_ID();
        if (CS_ID != null) {
            stmt.bindString(1, CS_ID);
        }
 
        String DESCR = entity.getDESCR();
        if (DESCR != null) {
            stmt.bindString(2, DESCR);
        }
 
        String FA_TYPE_CD = entity.getFA_TYPE_CD();
        if (FA_TYPE_CD != null) {
            stmt.bindString(3, FA_TYPE_CD);
        }
 
        String FA_TYPE_DESCR = entity.getFA_TYPE_DESCR();
        if (FA_TYPE_DESCR != null) {
            stmt.bindString(4, FA_TYPE_DESCR);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public JJCS readEntity(Cursor cursor, int offset) {
        JJCS entity = new JJCS( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // CS_ID
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // DESCR
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // FA_TYPE_CD
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // FA_TYPE_DESCR
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, JJCS entity, int offset) {
        entity.setCS_ID(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setDESCR(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setFA_TYPE_CD(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setFA_TYPE_DESCR(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(JJCS entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(JJCS entity) {
        return null;
    }

    @Override
    public boolean hasKey(JJCS entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
