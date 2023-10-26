package com.sh3h.dataprovider.greendaoDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.sh3h.dataprovider.data.entity.newentity.OverrateSelfHandleEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "OVERRATE_SELF_HANDLE_ENTITY".
*/
public class OverrateSelfHandleEntityDao extends AbstractDao<OverrateSelfHandleEntity, Long> {

    public static final String TABLENAME = "OVERRATE_SELF_HANDLE_ENTITY";

    /**
     * Properties of entity OverrateSelfHandleEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ID = new Property(0, Long.class, "ID", true, "_id");
        public final static Property Zhbh = new Property(1, String.class, "zhbh", false, "ZHBH");
        public final static Property Khmc = new Property(2, String.class, "khmc", false, "KHMC");
        public final static Property Gls = new Property(3, String.class, "gls", false, "GLS");
        public final static Property Zd = new Property(4, String.class, "zd", false, "ZD");
        public final static Property Dz = new Property(5, String.class, "dz", false, "DZ");
        public final static Property Cbc = new Property(6, String.class, "cbc", false, "CBC");
        public final static Property Xzq = new Property(7, String.class, "xzq", false, "XZQ");
        public final static Property Ssdm = new Property(8, String.class, "ssdm", false, "SSDM");
        public final static Property Khlx = new Property(9, String.class, "khlx", false, "KHLX");
        public final static Property Tyshxydm = new Property(10, String.class, "tyshxydm", false, "TYSHXYDM");
        public final static Property Lxr = new Property(11, String.class, "lxr", false, "LXR");
        public final static Property Lxfs = new Property(12, String.class, "lxfs", false, "LXFS");
        public final static Property Yjdz = new Property(13, String.class, "yjdz", false, "YJDZ");
        public final static Property Bz = new Property(14, String.class, "bz", false, "BZ");
        public final static Property Qfje = new Property(15, String.class, "qfje", false, "QFJE");
        public final static Property Yhh = new Property(16, String.class, "yhh", false, "YHH");
    }


    public OverrateSelfHandleEntityDao(DaoConfig config) {
        super(config);
    }
    
    public OverrateSelfHandleEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"OVERRATE_SELF_HANDLE_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: ID
                "\"ZHBH\" TEXT," + // 1: zhbh
                "\"KHMC\" TEXT," + // 2: khmc
                "\"GLS\" TEXT," + // 3: gls
                "\"ZD\" TEXT," + // 4: zd
                "\"DZ\" TEXT," + // 5: dz
                "\"CBC\" TEXT," + // 6: cbc
                "\"XZQ\" TEXT," + // 7: xzq
                "\"SSDM\" TEXT," + // 8: ssdm
                "\"KHLX\" TEXT," + // 9: khlx
                "\"TYSHXYDM\" TEXT," + // 10: tyshxydm
                "\"LXR\" TEXT," + // 11: lxr
                "\"LXFS\" TEXT," + // 12: lxfs
                "\"YJDZ\" TEXT," + // 13: yjdz
                "\"BZ\" TEXT," + // 14: bz
                "\"QFJE\" TEXT," + // 15: qfje
                "\"YHH\" TEXT);"); // 16: yhh
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"OVERRATE_SELF_HANDLE_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, OverrateSelfHandleEntity entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
 
        String zhbh = entity.getZhbh();
        if (zhbh != null) {
            stmt.bindString(2, zhbh);
        }
 
        String khmc = entity.getKhmc();
        if (khmc != null) {
            stmt.bindString(3, khmc);
        }
 
        String gls = entity.getGls();
        if (gls != null) {
            stmt.bindString(4, gls);
        }
 
        String zd = entity.getZd();
        if (zd != null) {
            stmt.bindString(5, zd);
        }
 
        String dz = entity.getDz();
        if (dz != null) {
            stmt.bindString(6, dz);
        }
 
        String cbc = entity.getCbc();
        if (cbc != null) {
            stmt.bindString(7, cbc);
        }
 
        String xzq = entity.getXzq();
        if (xzq != null) {
            stmt.bindString(8, xzq);
        }
 
        String ssdm = entity.getSsdm();
        if (ssdm != null) {
            stmt.bindString(9, ssdm);
        }
 
        String khlx = entity.getKhlx();
        if (khlx != null) {
            stmt.bindString(10, khlx);
        }
 
        String tyshxydm = entity.getTyshxydm();
        if (tyshxydm != null) {
            stmt.bindString(11, tyshxydm);
        }
 
        String lxr = entity.getLxr();
        if (lxr != null) {
            stmt.bindString(12, lxr);
        }
 
        String lxfs = entity.getLxfs();
        if (lxfs != null) {
            stmt.bindString(13, lxfs);
        }
 
        String yjdz = entity.getYjdz();
        if (yjdz != null) {
            stmt.bindString(14, yjdz);
        }
 
        String bz = entity.getBz();
        if (bz != null) {
            stmt.bindString(15, bz);
        }
 
        String qfje = entity.getQfje();
        if (qfje != null) {
            stmt.bindString(16, qfje);
        }
 
        String yhh = entity.getYhh();
        if (yhh != null) {
            stmt.bindString(17, yhh);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, OverrateSelfHandleEntity entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
 
        String zhbh = entity.getZhbh();
        if (zhbh != null) {
            stmt.bindString(2, zhbh);
        }
 
        String khmc = entity.getKhmc();
        if (khmc != null) {
            stmt.bindString(3, khmc);
        }
 
        String gls = entity.getGls();
        if (gls != null) {
            stmt.bindString(4, gls);
        }
 
        String zd = entity.getZd();
        if (zd != null) {
            stmt.bindString(5, zd);
        }
 
        String dz = entity.getDz();
        if (dz != null) {
            stmt.bindString(6, dz);
        }
 
        String cbc = entity.getCbc();
        if (cbc != null) {
            stmt.bindString(7, cbc);
        }
 
        String xzq = entity.getXzq();
        if (xzq != null) {
            stmt.bindString(8, xzq);
        }
 
        String ssdm = entity.getSsdm();
        if (ssdm != null) {
            stmt.bindString(9, ssdm);
        }
 
        String khlx = entity.getKhlx();
        if (khlx != null) {
            stmt.bindString(10, khlx);
        }
 
        String tyshxydm = entity.getTyshxydm();
        if (tyshxydm != null) {
            stmt.bindString(11, tyshxydm);
        }
 
        String lxr = entity.getLxr();
        if (lxr != null) {
            stmt.bindString(12, lxr);
        }
 
        String lxfs = entity.getLxfs();
        if (lxfs != null) {
            stmt.bindString(13, lxfs);
        }
 
        String yjdz = entity.getYjdz();
        if (yjdz != null) {
            stmt.bindString(14, yjdz);
        }
 
        String bz = entity.getBz();
        if (bz != null) {
            stmt.bindString(15, bz);
        }
 
        String qfje = entity.getQfje();
        if (qfje != null) {
            stmt.bindString(16, qfje);
        }
 
        String yhh = entity.getYhh();
        if (yhh != null) {
            stmt.bindString(17, yhh);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public OverrateSelfHandleEntity readEntity(Cursor cursor, int offset) {
        OverrateSelfHandleEntity entity = new OverrateSelfHandleEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // ID
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // zhbh
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // khmc
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // gls
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // zd
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // dz
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // cbc
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // xzq
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // ssdm
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // khlx
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // tyshxydm
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // lxr
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // lxfs
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // yjdz
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // bz
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // qfje
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16) // yhh
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, OverrateSelfHandleEntity entity, int offset) {
        entity.setID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setZhbh(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setKhmc(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setGls(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setZd(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDz(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCbc(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setXzq(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setSsdm(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setKhlx(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setTyshxydm(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setLxr(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setLxfs(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setYjdz(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setBz(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setQfje(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setYhh(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(OverrateSelfHandleEntity entity, long rowId) {
        entity.setID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(OverrateSelfHandleEntity entity) {
        if(entity != null) {
            return entity.getID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(OverrateSelfHandleEntity entity) {
        return entity.getID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}