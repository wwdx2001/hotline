package com.sh3h.localprovider.greendaoEntity;

import com.sh3h.localprovider.greendaoDao.DaoSession;
import com.sh3h.localprovider.greendaoDao.HistoryTaskDao;
import com.sh3h.localprovider.greendaoDao.MultiMediaDao;
import com.sh3h.localprovider.greendaoDao.TaskDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;


/**
 * Created by dengzhimin on 2016/9/27.
 * 工单任务（包括从远程获取的任务和本地自开单）
 */

@Entity(nameInDb = "HL_TASK")
public class Task {

    @Id(autoincrement = true)
    @Property(nameInDb = "ID")
    private Long ID;//主键 自增长

    @NotNull
    private int USER_ID;//用户编号

    private String CUSTOMER_ID;//客户编号

    @NotNull
    private String TASK_ID;//任务编号

    private String ISSUER;//反映人

    private String STATION;//受理站点

    private String ISSUE_NAME;//户名

    private String ISSUE_ORIGIN;//来源

    private long ISSUE_TIME;//发生时间

    private String ISSUE_ADDRESS;//发生地址

    private String TELEPHONE;//联系电话

    private String MOBILE;//手机

    @NotNull
    private String ISSUE_TYPE;//反映类型

    @NotNull
    private String ISSUE_CONTENT;//反映内容

    @NotNull
    private String REPLY_CLASS;//处理级别

    @NotNull
    private long REPLY_DEADLINE;//处理时限

    private String RECEIVE_COMMENT;//受理备注

    private String CARD_ID;//销根号

    @NotNull
    private int TASK_TYPE;//任务类型 0:下载的工单, 1:上报的自开单

    @NotNull
    private int TASK_STATE;//任务状态 -1:默认 0:派工，1：接收，2：延期，3：退单，4，处理（销单），5：销单不合格，6：销单完成

    private long DISPATCH_TIME;//派单时间

    @NotNull
    private int UPLOAD_FLAG;//上传标志 -1:默认 0:未上传, 1:已上传

    private String EXTEND;//replyImmediately, taskId, replyTime

    @ToMany(joinProperties = {@JoinProperty(name = "TASK_ID", referencedName = "TASK_ID")})
    private List<HistoryTask> historyTaskList;//建立关联，一条任务可对应多条历史记录

    @ToMany(joinProperties = {@JoinProperty(name = "TASK_ID", referencedName = "TASK_ID")})
    private List<MultiMedia> multiMedias;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1469429066)
    private transient TaskDao myDao;

    @Generated(hash = 288827274)
    public Task(Long ID, int USER_ID, String CUSTOMER_ID, @NotNull String TASK_ID,
            String ISSUER, String STATION, String ISSUE_NAME, String ISSUE_ORIGIN,
            long ISSUE_TIME, String ISSUE_ADDRESS, String TELEPHONE, String MOBILE,
            @NotNull String ISSUE_TYPE, @NotNull String ISSUE_CONTENT,
            @NotNull String REPLY_CLASS, long REPLY_DEADLINE, String RECEIVE_COMMENT,
            String CARD_ID, int TASK_TYPE, int TASK_STATE, long DISPATCH_TIME,
            int UPLOAD_FLAG, String EXTEND) {
        this.ID = ID;
        this.USER_ID = USER_ID;
        this.CUSTOMER_ID = CUSTOMER_ID;
        this.TASK_ID = TASK_ID;
        this.ISSUER = ISSUER;
        this.STATION = STATION;
        this.ISSUE_NAME = ISSUE_NAME;
        this.ISSUE_ORIGIN = ISSUE_ORIGIN;
        this.ISSUE_TIME = ISSUE_TIME;
        this.ISSUE_ADDRESS = ISSUE_ADDRESS;
        this.TELEPHONE = TELEPHONE;
        this.MOBILE = MOBILE;
        this.ISSUE_TYPE = ISSUE_TYPE;
        this.ISSUE_CONTENT = ISSUE_CONTENT;
        this.REPLY_CLASS = REPLY_CLASS;
        this.REPLY_DEADLINE = REPLY_DEADLINE;
        this.RECEIVE_COMMENT = RECEIVE_COMMENT;
        this.CARD_ID = CARD_ID;
        this.TASK_TYPE = TASK_TYPE;
        this.TASK_STATE = TASK_STATE;
        this.DISPATCH_TIME = DISPATCH_TIME;
        this.UPLOAD_FLAG = UPLOAD_FLAG;
        this.EXTEND = EXTEND;
    }

    @Generated(hash = 733837707)
    public Task() {
    }

    public Long getID() {
        return this.ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public int getUSER_ID() {
        return this.USER_ID;
    }

    public void setUSER_ID(int USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getCUSTOMER_ID() {
        return this.CUSTOMER_ID;
    }

    public void setCUSTOMER_ID(String CUSTOMER_ID) {
        this.CUSTOMER_ID = CUSTOMER_ID;
    }

    public String getTASK_ID() {
        return this.TASK_ID;
    }

    public void setTASK_ID(String TASK_ID) {
        this.TASK_ID = TASK_ID;
    }

    public String getISSUER() {
        return this.ISSUER;
    }

    public void setISSUER(String ISSUER) {
        this.ISSUER = ISSUER;
    }

    public String getSTATION() {
        return this.STATION;
    }

    public void setSTATION(String STATION) {
        this.STATION = STATION;
    }

    public String getISSUE_NAME() {
        return this.ISSUE_NAME;
    }

    public void setISSUE_NAME(String ISSUE_NAME) {
        this.ISSUE_NAME = ISSUE_NAME;
    }

    public String getISSUE_ORIGIN() {
        return this.ISSUE_ORIGIN;
    }

    public void setISSUE_ORIGIN(String ISSUE_ORIGIN) {
        this.ISSUE_ORIGIN = ISSUE_ORIGIN;
    }

    public long getISSUE_TIME() {
        return this.ISSUE_TIME;
    }

    public void setISSUE_TIME(long ISSUE_TIME) {
        this.ISSUE_TIME = ISSUE_TIME;
    }

    public String getISSUE_ADDRESS() {
        return this.ISSUE_ADDRESS;
    }

    public void setISSUE_ADDRESS(String ISSUE_ADDRESS) {
        this.ISSUE_ADDRESS = ISSUE_ADDRESS;
    }

    public String getTELEPHONE() {
        return this.TELEPHONE;
    }

    public void setTELEPHONE(String TELEPHONE) {
        this.TELEPHONE = TELEPHONE;
    }

    public String getMOBILE() {
        return this.MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public String getISSUE_TYPE() {
        return this.ISSUE_TYPE;
    }

    public void setISSUE_TYPE(String ISSUE_TYPE) {
        this.ISSUE_TYPE = ISSUE_TYPE;
    }

    public String getISSUE_CONTENT() {
        return this.ISSUE_CONTENT;
    }

    public void setISSUE_CONTENT(String ISSUE_CONTENT) {
        this.ISSUE_CONTENT = ISSUE_CONTENT;
    }

    public String getREPLY_CLASS() {
        return this.REPLY_CLASS;
    }

    public void setREPLY_CLASS(String REPLY_CLASS) {
        this.REPLY_CLASS = REPLY_CLASS;
    }

    public long getREPLY_DEADLINE() {
        return this.REPLY_DEADLINE;
    }

    public void setREPLY_DEADLINE(long REPLY_DEADLINE) {
        this.REPLY_DEADLINE = REPLY_DEADLINE;
    }

    public String getRECEIVE_COMMENT() {
        return this.RECEIVE_COMMENT;
    }

    public void setRECEIVE_COMMENT(String RECEIVE_COMMENT) {
        this.RECEIVE_COMMENT = RECEIVE_COMMENT;
    }

    public String getCARD_ID() {
        return this.CARD_ID;
    }

    public void setCARD_ID(String CARD_ID) {
        this.CARD_ID = CARD_ID;
    }

    public int getTASK_TYPE() {
        return this.TASK_TYPE;
    }

    public void setTASK_TYPE(int TASK_TYPE) {
        this.TASK_TYPE = TASK_TYPE;
    }

    public int getTASK_STATE() {
        return this.TASK_STATE;
    }

    public void setTASK_STATE(int TASK_STATE) {
        this.TASK_STATE = TASK_STATE;
    }

    public long getDISPATCH_TIME() {
        return this.DISPATCH_TIME;
    }

    public void setDISPATCH_TIME(long DISPATCH_TIME) {
        this.DISPATCH_TIME = DISPATCH_TIME;
    }

    public int getUPLOAD_FLAG() {
        return this.UPLOAD_FLAG;
    }

    public void setUPLOAD_FLAG(int UPLOAD_FLAG) {
        this.UPLOAD_FLAG = UPLOAD_FLAG;
    }

    public String getEXTEND() {
        return this.EXTEND;
    }

    public void setEXTEND(String EXTEND) {
        this.EXTEND = EXTEND;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1098428530)
    public List<HistoryTask> getHistoryTaskList() {
        if (historyTaskList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            HistoryTaskDao targetDao = daoSession.getHistoryTaskDao();
            List<HistoryTask> historyTaskListNew = targetDao._queryTask_HistoryTaskList(TASK_ID);
            synchronized (this) {
                if(historyTaskList == null) {
                    historyTaskList = historyTaskListNew;
                }
            }
        }
        return historyTaskList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1974510848)
    public synchronized void resetHistoryTaskList() {
        historyTaskList = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 908516094)
    public List<MultiMedia> getMultiMedias() {
        if (multiMedias == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MultiMediaDao targetDao = daoSession.getMultiMediaDao();
            List<MultiMedia> multiMediasNew = targetDao._queryTask_MultiMedias(TASK_ID);
            synchronized (this) {
                if(multiMedias == null) {
                    multiMedias = multiMediasNew;
                }
            }
        }
        return multiMedias;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1720946766)
    public synchronized void resetMultiMedias() {
        multiMedias = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1442741304)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTaskDao() : null;
    }

}