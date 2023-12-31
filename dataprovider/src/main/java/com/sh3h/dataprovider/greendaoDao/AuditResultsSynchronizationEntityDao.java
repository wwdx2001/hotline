package com.sh3h.dataprovider.greendaoDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.sh3h.dataprovider.data.entity.push.AuditResultsSynchronizationEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "AUDIT_RESULTS_SYNCHRONIZATION_ENTITY".
*/
public class AuditResultsSynchronizationEntityDao extends AbstractDao<AuditResultsSynchronizationEntity, Void> {

    public static final String TABLENAME = "AUDIT_RESULTS_SYNCHRONIZATION_ENTITY";

    /**
     * Properties of entity AuditResultsSynchronizationEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property FaId = new Property(0, String.class, "faId", false, "FA_ID");
        public final static Property CaseId = new Property(1, String.class, "caseId", false, "CASE_ID");
        public final static Property ApplyType = new Property(2, String.class, "applyType", false, "APPLY_TYPE");
        public final static Property Result = new Property(3, String.class, "result", false, "RESULT");
        public final static Property Comment = new Property(4, String.class, "comment", false, "COMMENT");
        public final static Property DelayDt = new Property(5, String.class, "delayDt", false, "DELAY_DT");
    }


    public AuditResultsSynchronizationEntityDao(DaoConfig config) {
        super(config);
    }
    
    public AuditResultsSynchronizationEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"AUDIT_RESULTS_SYNCHRONIZATION_ENTITY\" (" + //
                "\"FA_ID\" TEXT," + // 0: faId
                "\"CASE_ID\" TEXT," + // 1: caseId
                "\"APPLY_TYPE\" TEXT," + // 2: applyType
                "\"RESULT\" TEXT," + // 3: result
                "\"COMMENT\" TEXT," + // 4: comment
                "\"DELAY_DT\" TEXT);"); // 5: delayDt
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"AUDIT_RESULTS_SYNCHRONIZATION_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, AuditResultsSynchronizationEntity entity) {
        stmt.clearBindings();
 
        String faId = entity.getFaId();
        if (faId != null) {
            stmt.bindString(1, faId);
        }
 
        String caseId = entity.getCaseId();
        if (caseId != null) {
            stmt.bindString(2, caseId);
        }
 
        String applyType = entity.getApplyType();
        if (applyType != null) {
            stmt.bindString(3, applyType);
        }
 
        String result = entity.getResult();
        if (result != null) {
            stmt.bindString(4, result);
        }
 
        String comment = entity.getComment();
        if (comment != null) {
            stmt.bindString(5, comment);
        }
 
        String delayDt = entity.getDelayDt();
        if (delayDt != null) {
            stmt.bindString(6, delayDt);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, AuditResultsSynchronizationEntity entity) {
        stmt.clearBindings();
 
        String faId = entity.getFaId();
        if (faId != null) {
            stmt.bindString(1, faId);
        }
 
        String caseId = entity.getCaseId();
        if (caseId != null) {
            stmt.bindString(2, caseId);
        }
 
        String applyType = entity.getApplyType();
        if (applyType != null) {
            stmt.bindString(3, applyType);
        }
 
        String result = entity.getResult();
        if (result != null) {
            stmt.bindString(4, result);
        }
 
        String comment = entity.getComment();
        if (comment != null) {
            stmt.bindString(5, comment);
        }
 
        String delayDt = entity.getDelayDt();
        if (delayDt != null) {
            stmt.bindString(6, delayDt);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public AuditResultsSynchronizationEntity readEntity(Cursor cursor, int offset) {
        AuditResultsSynchronizationEntity entity = new AuditResultsSynchronizationEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // faId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // caseId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // applyType
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // result
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // comment
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // delayDt
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, AuditResultsSynchronizationEntity entity, int offset) {
        entity.setFaId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setCaseId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setApplyType(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setResult(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setComment(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDelayDt(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(AuditResultsSynchronizationEntity entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(AuditResultsSynchronizationEntity entity) {
        return null;
    }

    @Override
    public boolean hasKey(AuditResultsSynchronizationEntity entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
