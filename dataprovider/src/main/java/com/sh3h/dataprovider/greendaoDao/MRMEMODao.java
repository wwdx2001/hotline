package com.sh3h.dataprovider.greendaoDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.sh3h.dataprovider.data.entity.response.MRMEMO;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MRMEMO".
*/
public class MRMEMODao extends AbstractDao<MRMEMO, Void> {

    public static final String TABLENAME = "MRMEMO";

    /**
     * Properties of entity MRMEMO.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Mrmemo_Code = new Property(0, String.class, "Mrmemo_Code", false, "MRMEMO__CODE");
        public final static Property Mrmemo_Desc = new Property(1, String.class, "Mrmemo_Desc", false, "MRMEMO__DESC");
        public final static Property MrParent_Code = new Property(2, String.class, "MrParent_Code", false, "MR_PARENT__CODE");
        public final static Property MrParent_Desc = new Property(3, String.class, "MrParent_Desc", false, "MR_PARENT__DESC");
    }


    public MRMEMODao(DaoConfig config) {
        super(config);
    }
    
    public MRMEMODao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MRMEMO\" (" + //
                "\"MRMEMO__CODE\" TEXT," + // 0: Mrmemo_Code
                "\"MRMEMO__DESC\" TEXT," + // 1: Mrmemo_Desc
                "\"MR_PARENT__CODE\" TEXT," + // 2: MrParent_Code
                "\"MR_PARENT__DESC\" TEXT);"); // 3: MrParent_Desc
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MRMEMO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MRMEMO entity) {
        stmt.clearBindings();
 
        String Mrmemo_Code = entity.getMrmemo_Code();
        if (Mrmemo_Code != null) {
            stmt.bindString(1, Mrmemo_Code);
        }
 
        String Mrmemo_Desc = entity.getMrmemo_Desc();
        if (Mrmemo_Desc != null) {
            stmt.bindString(2, Mrmemo_Desc);
        }
 
        String MrParent_Code = entity.getMrParent_Code();
        if (MrParent_Code != null) {
            stmt.bindString(3, MrParent_Code);
        }
 
        String MrParent_Desc = entity.getMrParent_Desc();
        if (MrParent_Desc != null) {
            stmt.bindString(4, MrParent_Desc);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MRMEMO entity) {
        stmt.clearBindings();
 
        String Mrmemo_Code = entity.getMrmemo_Code();
        if (Mrmemo_Code != null) {
            stmt.bindString(1, Mrmemo_Code);
        }
 
        String Mrmemo_Desc = entity.getMrmemo_Desc();
        if (Mrmemo_Desc != null) {
            stmt.bindString(2, Mrmemo_Desc);
        }
 
        String MrParent_Code = entity.getMrParent_Code();
        if (MrParent_Code != null) {
            stmt.bindString(3, MrParent_Code);
        }
 
        String MrParent_Desc = entity.getMrParent_Desc();
        if (MrParent_Desc != null) {
            stmt.bindString(4, MrParent_Desc);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public MRMEMO readEntity(Cursor cursor, int offset) {
        MRMEMO entity = new MRMEMO( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // Mrmemo_Code
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Mrmemo_Desc
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // MrParent_Code
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // MrParent_Desc
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MRMEMO entity, int offset) {
        entity.setMrmemo_Code(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setMrmemo_Desc(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setMrParent_Code(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMrParent_Desc(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(MRMEMO entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(MRMEMO entity) {
        return null;
    }

    @Override
    public boolean hasKey(MRMEMO entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
