package com.sh3h.dataprovider.greendaoDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.sh3h.dataprovider.data.entity.newentity.ArriveDataEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ARRIVE_DATA_ENTITY".
*/
public class ArriveDataEntityDao extends AbstractDao<ArriveDataEntity, Long> {

    public static final String TABLENAME = "ARRIVE_DATA_ENTITY";

    /**
     * Properties of entity ArriveDataEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ID = new Property(0, Long.class, "ID", true, "_id");
        public final static Property FaId = new Property(1, String.class, "faId", false, "FA_ID");
        public final static Property ArriveDt = new Property(2, String.class, "arriveDt", false, "ARRIVE_DT");
    }


    public ArriveDataEntityDao(DaoConfig config) {
        super(config);
    }
    
    public ArriveDataEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ARRIVE_DATA_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: ID
                "\"FA_ID\" TEXT," + // 1: faId
                "\"ARRIVE_DT\" TEXT);"); // 2: arriveDt
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ARRIVE_DATA_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ArriveDataEntity entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
 
        String faId = entity.getFaId();
        if (faId != null) {
            stmt.bindString(2, faId);
        }
 
        String arriveDt = entity.getArriveDt();
        if (arriveDt != null) {
            stmt.bindString(3, arriveDt);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ArriveDataEntity entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
 
        String faId = entity.getFaId();
        if (faId != null) {
            stmt.bindString(2, faId);
        }
 
        String arriveDt = entity.getArriveDt();
        if (arriveDt != null) {
            stmt.bindString(3, arriveDt);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ArriveDataEntity readEntity(Cursor cursor, int offset) {
        ArriveDataEntity entity = new ArriveDataEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // ID
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // faId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // arriveDt
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ArriveDataEntity entity, int offset) {
        entity.setID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFaId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setArriveDt(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ArriveDataEntity entity, long rowId) {
        entity.setID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ArriveDataEntity entity) {
        if(entity != null) {
            return entity.getID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ArriveDataEntity entity) {
        return entity.getID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
