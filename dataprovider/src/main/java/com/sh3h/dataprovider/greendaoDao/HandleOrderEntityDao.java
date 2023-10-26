package com.sh3h.dataprovider.greendaoDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.sh3h.dataprovider.data.entity.newentity.HandleOrderEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "HANDLE_ORDER_ENTITY".
*/
public class HandleOrderEntityDao extends AbstractDao<HandleOrderEntity, Long> {

    public static final String TABLENAME = "HANDLE_ORDER_ENTITY";

    /**
     * Properties of entity HandleOrderEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ID = new Property(0, Long.class, "ID", true, "_id");
        public final static Property FaId = new Property(1, String.class, "faId", false, "FA_ID");
        public final static Property CaseId = new Property(2, String.class, "caseId", false, "CASE_ID");
        public final static Property OldCaseId = new Property(3, String.class, "oldCaseId", false, "OLD_CASE_ID");
        public final static Property CmSta = new Property(4, String.class, "cmSta", false, "CM_STA");
        public final static Property ArriveDt = new Property(5, String.class, "arriveDt", false, "ARRIVE_DT");
        public final static Property FaTypeCd = new Property(6, String.class, "faTypeCd", false, "FA_TYPE_CD");
        public final static Property Clnr = new Property(7, String.class, "clnr", false, "CLNR");
        public final static Property FaReason = new Property(8, String.class, "faReason", false, "FA_REASON");
        public final static Property FaAct = new Property(9, String.class, "faAct", false, "FA_ACT");
        public final static Property Comment = new Property(10, String.class, "comment", false, "COMMENT");
        public final static Property FinishDt = new Property(11, String.class, "finishDt", false, "FINISH_DT");
        public final static Property RepCd = new Property(12, String.class, "repCd", false, "REP_CD");
        public final static Property Cljg = new Property(13, String.class, "cljg", false, "CLJG");
        public final static Property RegRead = new Property(14, String.class, "regRead", false, "REG_READ");
        public final static Property Cbzt = new Property(15, String.class, "cbzt", false, "CBZT");
    }


    public HandleOrderEntityDao(DaoConfig config) {
        super(config);
    }
    
    public HandleOrderEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"HANDLE_ORDER_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: ID
                "\"FA_ID\" TEXT," + // 1: faId
                "\"CASE_ID\" TEXT," + // 2: caseId
                "\"OLD_CASE_ID\" TEXT," + // 3: oldCaseId
                "\"CM_STA\" TEXT," + // 4: cmSta
                "\"ARRIVE_DT\" TEXT," + // 5: arriveDt
                "\"FA_TYPE_CD\" TEXT," + // 6: faTypeCd
                "\"CLNR\" TEXT," + // 7: clnr
                "\"FA_REASON\" TEXT," + // 8: faReason
                "\"FA_ACT\" TEXT," + // 9: faAct
                "\"COMMENT\" TEXT," + // 10: comment
                "\"FINISH_DT\" TEXT," + // 11: finishDt
                "\"REP_CD\" TEXT," + // 12: repCd
                "\"CLJG\" TEXT," + // 13: cljg
                "\"REG_READ\" TEXT," + // 14: regRead
                "\"CBZT\" TEXT);"); // 15: cbzt
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"HANDLE_ORDER_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, HandleOrderEntity entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
 
        String faId = entity.getFaId();
        if (faId != null) {
            stmt.bindString(2, faId);
        }
 
        String caseId = entity.getCaseId();
        if (caseId != null) {
            stmt.bindString(3, caseId);
        }
 
        String oldCaseId = entity.getOldCaseId();
        if (oldCaseId != null) {
            stmt.bindString(4, oldCaseId);
        }
 
        String cmSta = entity.getCmSta();
        if (cmSta != null) {
            stmt.bindString(5, cmSta);
        }
 
        String arriveDt = entity.getArriveDt();
        if (arriveDt != null) {
            stmt.bindString(6, arriveDt);
        }
 
        String faTypeCd = entity.getFaTypeCd();
        if (faTypeCd != null) {
            stmt.bindString(7, faTypeCd);
        }
 
        String clnr = entity.getClnr();
        if (clnr != null) {
            stmt.bindString(8, clnr);
        }
 
        String faReason = entity.getFaReason();
        if (faReason != null) {
            stmt.bindString(9, faReason);
        }
 
        String faAct = entity.getFaAct();
        if (faAct != null) {
            stmt.bindString(10, faAct);
        }
 
        String comment = entity.getComment();
        if (comment != null) {
            stmt.bindString(11, comment);
        }
 
        String finishDt = entity.getFinishDt();
        if (finishDt != null) {
            stmt.bindString(12, finishDt);
        }
 
        String repCd = entity.getRepCd();
        if (repCd != null) {
            stmt.bindString(13, repCd);
        }
 
        String cljg = entity.getCljg();
        if (cljg != null) {
            stmt.bindString(14, cljg);
        }
 
        String regRead = entity.getRegRead();
        if (regRead != null) {
            stmt.bindString(15, regRead);
        }
 
        String cbzt = entity.getCbzt();
        if (cbzt != null) {
            stmt.bindString(16, cbzt);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, HandleOrderEntity entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
 
        String faId = entity.getFaId();
        if (faId != null) {
            stmt.bindString(2, faId);
        }
 
        String caseId = entity.getCaseId();
        if (caseId != null) {
            stmt.bindString(3, caseId);
        }
 
        String oldCaseId = entity.getOldCaseId();
        if (oldCaseId != null) {
            stmt.bindString(4, oldCaseId);
        }
 
        String cmSta = entity.getCmSta();
        if (cmSta != null) {
            stmt.bindString(5, cmSta);
        }
 
        String arriveDt = entity.getArriveDt();
        if (arriveDt != null) {
            stmt.bindString(6, arriveDt);
        }
 
        String faTypeCd = entity.getFaTypeCd();
        if (faTypeCd != null) {
            stmt.bindString(7, faTypeCd);
        }
 
        String clnr = entity.getClnr();
        if (clnr != null) {
            stmt.bindString(8, clnr);
        }
 
        String faReason = entity.getFaReason();
        if (faReason != null) {
            stmt.bindString(9, faReason);
        }
 
        String faAct = entity.getFaAct();
        if (faAct != null) {
            stmt.bindString(10, faAct);
        }
 
        String comment = entity.getComment();
        if (comment != null) {
            stmt.bindString(11, comment);
        }
 
        String finishDt = entity.getFinishDt();
        if (finishDt != null) {
            stmt.bindString(12, finishDt);
        }
 
        String repCd = entity.getRepCd();
        if (repCd != null) {
            stmt.bindString(13, repCd);
        }
 
        String cljg = entity.getCljg();
        if (cljg != null) {
            stmt.bindString(14, cljg);
        }
 
        String regRead = entity.getRegRead();
        if (regRead != null) {
            stmt.bindString(15, regRead);
        }
 
        String cbzt = entity.getCbzt();
        if (cbzt != null) {
            stmt.bindString(16, cbzt);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public HandleOrderEntity readEntity(Cursor cursor, int offset) {
        HandleOrderEntity entity = new HandleOrderEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // ID
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // faId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // caseId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // oldCaseId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // cmSta
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // arriveDt
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // faTypeCd
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // clnr
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // faReason
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // faAct
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // comment
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // finishDt
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // repCd
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // cljg
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // regRead
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15) // cbzt
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, HandleOrderEntity entity, int offset) {
        entity.setID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFaId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCaseId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setOldCaseId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCmSta(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setArriveDt(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setFaTypeCd(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setClnr(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setFaReason(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setFaAct(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setComment(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setFinishDt(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setRepCd(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setCljg(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setRegRead(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setCbzt(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(HandleOrderEntity entity, long rowId) {
        entity.setID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(HandleOrderEntity entity) {
        if(entity != null) {
            return entity.getID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(HandleOrderEntity entity) {
        return entity.getID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
