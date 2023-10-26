package com.sh3h.dataprovider.greendaoDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.sh3h.dataprovider.data.entity.newentity.OverrateCallHandleEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "OVERRATE_CALL_HANDLE_ENTITY".
*/
public class OverrateCallHandleEntityDao extends AbstractDao<OverrateCallHandleEntity, Long> {

    public static final String TABLENAME = "OVERRATE_CALL_HANDLE_ENTITY";

    /**
     * Properties of entity OverrateCallHandleEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ID = new Property(0, Long.class, "ID", true, "_id");
        public final static Property Albh = new Property(1, String.class, "albh", false, "ALBH");
        public final static Property Pch = new Property(2, String.class, "pch", false, "PCH");
        public final static Property Xh = new Property(3, String.class, "xh", false, "XH");
        public final static Property Yhh = new Property(4, String.class, "yhh", false, "YHH");
        public final static Property Cjdl = new Property(5, String.class, "cjdl", false, "CJDL");
        public final static Property Cjxl = new Property(6, String.class, "cjxl", false, "CJXL");
        public final static Property Zqzddz = new Property(7, String.class, "zqzddz", false, "ZQZDDZ");
        public final static Property Zqhm = new Property(8, String.class, "zqhm", false, "ZQHM");
        public final static Property Xhm = new Property(9, String.class, "xhm", false, "XHM");
        public final static Property Jfly = new Property(10, String.class, "jfly", false, "JFLY");
        public final static Property Qkms = new Property(11, String.class, "qkms", false, "QKMS");
        public final static Property Ncqcjcs = new Property(12, String.class, "ncqcjcs", false, "NCQCJCS");
        public final static Property Cjqr = new Property(13, String.class, "cjqr", false, "CJQR");
        public final static Property Cjbz = new Property(14, String.class, "cjbz", false, "CJBZ");
        public final static Property Xxbg = new Property(15, String.class, "xxbg", false, "XXBG");
        public final static Property Zhbh = new Property(16, String.class, "zhbh", false, "ZHBH");
        public final static Property Xzq = new Property(17, String.class, "xzq", false, "XZQ");
        public final static Property Ssdm = new Property(18, String.class, "ssdm", false, "SSDM");
        public final static Property Khlx = new Property(19, String.class, "khlx", false, "KHLX");
        public final static Property Tyshxydm = new Property(20, String.class, "tyshxydm", false, "TYSHXYDM");
        public final static Property Khmc = new Property(21, String.class, "khmc", false, "KHMC");
        public final static Property Lxr = new Property(22, String.class, "lxr", false, "LXR");
        public final static Property Lxfs = new Property(23, String.class, "lxfs", false, "LXFS");
        public final static Property Yjdz = new Property(24, String.class, "yjdz", false, "YJDZ");
        public final static Property Bz = new Property(25, String.class, "bz", false, "BZ");
        public final static Property IsCuiJiao = new Property(26, String.class, "isCuiJiao", false, "IS_CUI_JIAO");
        public final static Property Youbian = new Property(27, String.class, "youbian", false, "YOUBIAN");
    }


    public OverrateCallHandleEntityDao(DaoConfig config) {
        super(config);
    }
    
    public OverrateCallHandleEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"OVERRATE_CALL_HANDLE_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: ID
                "\"ALBH\" TEXT," + // 1: albh
                "\"PCH\" TEXT," + // 2: pch
                "\"XH\" TEXT," + // 3: xh
                "\"YHH\" TEXT," + // 4: yhh
                "\"CJDL\" TEXT," + // 5: cjdl
                "\"CJXL\" TEXT," + // 6: cjxl
                "\"ZQZDDZ\" TEXT," + // 7: zqzddz
                "\"ZQHM\" TEXT," + // 8: zqhm
                "\"XHM\" TEXT," + // 9: xhm
                "\"JFLY\" TEXT," + // 10: jfly
                "\"QKMS\" TEXT," + // 11: qkms
                "\"NCQCJCS\" TEXT," + // 12: ncqcjcs
                "\"CJQR\" TEXT," + // 13: cjqr
                "\"CJBZ\" TEXT," + // 14: cjbz
                "\"XXBG\" TEXT," + // 15: xxbg
                "\"ZHBH\" TEXT," + // 16: zhbh
                "\"XZQ\" TEXT," + // 17: xzq
                "\"SSDM\" TEXT," + // 18: ssdm
                "\"KHLX\" TEXT," + // 19: khlx
                "\"TYSHXYDM\" TEXT," + // 20: tyshxydm
                "\"KHMC\" TEXT," + // 21: khmc
                "\"LXR\" TEXT," + // 22: lxr
                "\"LXFS\" TEXT," + // 23: lxfs
                "\"YJDZ\" TEXT," + // 24: yjdz
                "\"BZ\" TEXT," + // 25: bz
                "\"IS_CUI_JIAO\" TEXT," + // 26: isCuiJiao
                "\"YOUBIAN\" TEXT);"); // 27: youbian
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"OVERRATE_CALL_HANDLE_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, OverrateCallHandleEntity entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
 
        String albh = entity.getAlbh();
        if (albh != null) {
            stmt.bindString(2, albh);
        }
 
        String pch = entity.getPch();
        if (pch != null) {
            stmt.bindString(3, pch);
        }
 
        String xh = entity.getXh();
        if (xh != null) {
            stmt.bindString(4, xh);
        }
 
        String yhh = entity.getYhh();
        if (yhh != null) {
            stmt.bindString(5, yhh);
        }
 
        String cjdl = entity.getCjdl();
        if (cjdl != null) {
            stmt.bindString(6, cjdl);
        }
 
        String cjxl = entity.getCjxl();
        if (cjxl != null) {
            stmt.bindString(7, cjxl);
        }
 
        String zqzddz = entity.getZqzddz();
        if (zqzddz != null) {
            stmt.bindString(8, zqzddz);
        }
 
        String zqhm = entity.getZqhm();
        if (zqhm != null) {
            stmt.bindString(9, zqhm);
        }
 
        String xhm = entity.getXhm();
        if (xhm != null) {
            stmt.bindString(10, xhm);
        }
 
        String jfly = entity.getJfly();
        if (jfly != null) {
            stmt.bindString(11, jfly);
        }
 
        String qkms = entity.getQkms();
        if (qkms != null) {
            stmt.bindString(12, qkms);
        }
 
        String ncqcjcs = entity.getNcqcjcs();
        if (ncqcjcs != null) {
            stmt.bindString(13, ncqcjcs);
        }
 
        String cjqr = entity.getCjqr();
        if (cjqr != null) {
            stmt.bindString(14, cjqr);
        }
 
        String cjbz = entity.getCjbz();
        if (cjbz != null) {
            stmt.bindString(15, cjbz);
        }
 
        String xxbg = entity.getXxbg();
        if (xxbg != null) {
            stmt.bindString(16, xxbg);
        }
 
        String zhbh = entity.getZhbh();
        if (zhbh != null) {
            stmt.bindString(17, zhbh);
        }
 
        String xzq = entity.getXzq();
        if (xzq != null) {
            stmt.bindString(18, xzq);
        }
 
        String ssdm = entity.getSsdm();
        if (ssdm != null) {
            stmt.bindString(19, ssdm);
        }
 
        String khlx = entity.getKhlx();
        if (khlx != null) {
            stmt.bindString(20, khlx);
        }
 
        String tyshxydm = entity.getTyshxydm();
        if (tyshxydm != null) {
            stmt.bindString(21, tyshxydm);
        }
 
        String khmc = entity.getKhmc();
        if (khmc != null) {
            stmt.bindString(22, khmc);
        }
 
        String lxr = entity.getLxr();
        if (lxr != null) {
            stmt.bindString(23, lxr);
        }
 
        String lxfs = entity.getLxfs();
        if (lxfs != null) {
            stmt.bindString(24, lxfs);
        }
 
        String yjdz = entity.getYjdz();
        if (yjdz != null) {
            stmt.bindString(25, yjdz);
        }
 
        String bz = entity.getBz();
        if (bz != null) {
            stmt.bindString(26, bz);
        }
 
        String isCuiJiao = entity.getIsCuiJiao();
        if (isCuiJiao != null) {
            stmt.bindString(27, isCuiJiao);
        }
 
        String youbian = entity.getYoubian();
        if (youbian != null) {
            stmt.bindString(28, youbian);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, OverrateCallHandleEntity entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
 
        String albh = entity.getAlbh();
        if (albh != null) {
            stmt.bindString(2, albh);
        }
 
        String pch = entity.getPch();
        if (pch != null) {
            stmt.bindString(3, pch);
        }
 
        String xh = entity.getXh();
        if (xh != null) {
            stmt.bindString(4, xh);
        }
 
        String yhh = entity.getYhh();
        if (yhh != null) {
            stmt.bindString(5, yhh);
        }
 
        String cjdl = entity.getCjdl();
        if (cjdl != null) {
            stmt.bindString(6, cjdl);
        }
 
        String cjxl = entity.getCjxl();
        if (cjxl != null) {
            stmt.bindString(7, cjxl);
        }
 
        String zqzddz = entity.getZqzddz();
        if (zqzddz != null) {
            stmt.bindString(8, zqzddz);
        }
 
        String zqhm = entity.getZqhm();
        if (zqhm != null) {
            stmt.bindString(9, zqhm);
        }
 
        String xhm = entity.getXhm();
        if (xhm != null) {
            stmt.bindString(10, xhm);
        }
 
        String jfly = entity.getJfly();
        if (jfly != null) {
            stmt.bindString(11, jfly);
        }
 
        String qkms = entity.getQkms();
        if (qkms != null) {
            stmt.bindString(12, qkms);
        }
 
        String ncqcjcs = entity.getNcqcjcs();
        if (ncqcjcs != null) {
            stmt.bindString(13, ncqcjcs);
        }
 
        String cjqr = entity.getCjqr();
        if (cjqr != null) {
            stmt.bindString(14, cjqr);
        }
 
        String cjbz = entity.getCjbz();
        if (cjbz != null) {
            stmt.bindString(15, cjbz);
        }
 
        String xxbg = entity.getXxbg();
        if (xxbg != null) {
            stmt.bindString(16, xxbg);
        }
 
        String zhbh = entity.getZhbh();
        if (zhbh != null) {
            stmt.bindString(17, zhbh);
        }
 
        String xzq = entity.getXzq();
        if (xzq != null) {
            stmt.bindString(18, xzq);
        }
 
        String ssdm = entity.getSsdm();
        if (ssdm != null) {
            stmt.bindString(19, ssdm);
        }
 
        String khlx = entity.getKhlx();
        if (khlx != null) {
            stmt.bindString(20, khlx);
        }
 
        String tyshxydm = entity.getTyshxydm();
        if (tyshxydm != null) {
            stmt.bindString(21, tyshxydm);
        }
 
        String khmc = entity.getKhmc();
        if (khmc != null) {
            stmt.bindString(22, khmc);
        }
 
        String lxr = entity.getLxr();
        if (lxr != null) {
            stmt.bindString(23, lxr);
        }
 
        String lxfs = entity.getLxfs();
        if (lxfs != null) {
            stmt.bindString(24, lxfs);
        }
 
        String yjdz = entity.getYjdz();
        if (yjdz != null) {
            stmt.bindString(25, yjdz);
        }
 
        String bz = entity.getBz();
        if (bz != null) {
            stmt.bindString(26, bz);
        }
 
        String isCuiJiao = entity.getIsCuiJiao();
        if (isCuiJiao != null) {
            stmt.bindString(27, isCuiJiao);
        }
 
        String youbian = entity.getYoubian();
        if (youbian != null) {
            stmt.bindString(28, youbian);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public OverrateCallHandleEntity readEntity(Cursor cursor, int offset) {
        OverrateCallHandleEntity entity = new OverrateCallHandleEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // ID
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // albh
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // pch
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // xh
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // yhh
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // cjdl
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // cjxl
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // zqzddz
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // zqhm
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // xhm
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // jfly
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // qkms
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // ncqcjcs
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // cjqr
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // cjbz
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // xxbg
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // zhbh
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // xzq
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // ssdm
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // khlx
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // tyshxydm
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // khmc
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // lxr
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // lxfs
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // yjdz
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25), // bz
            cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26), // isCuiJiao
            cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27) // youbian
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, OverrateCallHandleEntity entity, int offset) {
        entity.setID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAlbh(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPch(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setXh(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setYhh(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCjdl(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCjxl(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setZqzddz(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setZqhm(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setXhm(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setJfly(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setQkms(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setNcqcjcs(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setCjqr(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setCjbz(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setXxbg(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setZhbh(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setXzq(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setSsdm(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setKhlx(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setTyshxydm(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setKhmc(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setLxr(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setLxfs(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setYjdz(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setBz(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
        entity.setIsCuiJiao(cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26));
        entity.setYoubian(cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(OverrateCallHandleEntity entity, long rowId) {
        entity.setID(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(OverrateCallHandleEntity entity) {
        if(entity != null) {
            return entity.getID();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(OverrateCallHandleEntity entity) {
        return entity.getID() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
