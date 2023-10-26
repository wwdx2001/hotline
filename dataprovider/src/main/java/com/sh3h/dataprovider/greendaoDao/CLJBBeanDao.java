package com.sh3h.dataprovider.greendaoDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.sh3h.dataprovider.data.entity.response.CLJBBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CLJBBEAN".
*/
public class CLJBBeanDao extends AbstractDao<CLJBBean, Void> {

    public static final String TABLENAME = "CLJBBEAN";

    /**
     * Properties of entity CLJBBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property CLJB_ID = new Property(0, String.class, "CLJB_ID", false, "CLJB__ID");
        public final static Property DESCR = new Property(1, String.class, "DESCR", false, "DESCR");
    }


    public CLJBBeanDao(DaoConfig config) {
        super(config);
    }
    
    public CLJBBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CLJBBEAN\" (" + //
                "\"CLJB__ID\" TEXT," + // 0: CLJB_ID
                "\"DESCR\" TEXT);"); // 1: DESCR
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CLJBBEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CLJBBean entity) {
        stmt.clearBindings();
 
        String CLJB_ID = entity.getCLJB_ID();
        if (CLJB_ID != null) {
            stmt.bindString(1, CLJB_ID);
        }
 
        String DESCR = entity.getDESCR();
        if (DESCR != null) {
            stmt.bindString(2, DESCR);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CLJBBean entity) {
        stmt.clearBindings();
 
        String CLJB_ID = entity.getCLJB_ID();
        if (CLJB_ID != null) {
            stmt.bindString(1, CLJB_ID);
        }
 
        String DESCR = entity.getDESCR();
        if (DESCR != null) {
            stmt.bindString(2, DESCR);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public CLJBBean readEntity(Cursor cursor, int offset) {
        CLJBBean entity = new CLJBBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // CLJB_ID
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1) // DESCR
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CLJBBean entity, int offset) {
        entity.setCLJB_ID(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setDESCR(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(CLJBBean entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(CLJBBean entity) {
        return null;
    }

    @Override
    public boolean hasKey(CLJBBean entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
