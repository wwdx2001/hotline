package com.sh3h.localprovider.greendaoEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Generated;

import java.util.List;
import org.greenrobot.greendao.DaoException;
import com.sh3h.localprovider.greendaoDao.DaoSession;
import com.sh3h.localprovider.greendaoDao.HistoryTaskDao;
import com.sh3h.localprovider.greendaoDao.MultiMediaDao;
import com.sh3h.localprovider.greendaoDao.TaskDao;


/**
 * Created by dengzhimin on 2016/9/27.
 * 历史工单
 */

@Entity(nameInDb = "HL_HISTORY")
public class HistoryTask {

    @Id(autoincrement = true)
    @Property(nameInDb = "ID")
    private Long ID;//主键 自增长

    @NotNull
    private int USER_ID;//用户编号

    @NotNull
    private String TASK_ID;//任务编号

    @NotNull
    private int TASK_TYPE;//任务类型

    @NotNull
    private int TASK_STATE;//任务状态

    private String TASK_CONTENT;//任务内容

    private String TASK_REPLY;//任务处理

    @NotNull
    private long REPLY_TIME;//处理时间

    @NotNull
    private int UPLOAD_FLAG;//上传标志

    private String EXTEND;//taskId

    @ToMany(joinProperties = {@JoinProperty(name = "TASK_ID", referencedName = "TASK_ID")})
    private List<Task> tasks;

    @ToMany(joinProperties = {@JoinProperty(name = "TASK_ID",referencedName = "TASK_ID"),
            @JoinProperty(name = "TASK_STATE",referencedName = "TASK_STATE"),
            @JoinProperty(name = "TASK_TYPE",referencedName = "TASK_TYPE")})
    private List<MultiMedia> multiMedias;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 679243484)
    private transient HistoryTaskDao myDao;

    @Generated(hash = 358812317)
    public HistoryTask(Long ID, int USER_ID, @NotNull String TASK_ID, int TASK_TYPE,
            int TASK_STATE, String TASK_CONTENT, String TASK_REPLY, long REPLY_TIME,
            int UPLOAD_FLAG, String EXTEND) {
        this.ID = ID;
        this.USER_ID = USER_ID;
        this.TASK_ID = TASK_ID;
        this.TASK_TYPE = TASK_TYPE;
        this.TASK_STATE = TASK_STATE;
        this.TASK_CONTENT = TASK_CONTENT;
        this.TASK_REPLY = TASK_REPLY;
        this.REPLY_TIME = REPLY_TIME;
        this.UPLOAD_FLAG = UPLOAD_FLAG;
        this.EXTEND = EXTEND;
    }

    @Generated(hash = 1509075121)
    public HistoryTask() {
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

    public String getTASK_ID() {
        return this.TASK_ID;
    }

    public void setTASK_ID(String TASK_ID) {
        this.TASK_ID = TASK_ID;
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

    public String getTASK_CONTENT() {
        return this.TASK_CONTENT;
    }

    public void setTASK_CONTENT(String TASK_CONTENT) {
        this.TASK_CONTENT = TASK_CONTENT;
    }

    public String getTASK_REPLY() {
        return this.TASK_REPLY;
    }

    public void setTASK_REPLY(String TASK_REPLY) {
        this.TASK_REPLY = TASK_REPLY;
    }

    public long getREPLY_TIME() {
        return this.REPLY_TIME;
    }

    public void setREPLY_TIME(long REPLY_TIME) {
        this.REPLY_TIME = REPLY_TIME;
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
    @Generated(hash = 1874305742)
    public List<Task> getTasks() {
        if (tasks == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TaskDao targetDao = daoSession.getTaskDao();
            List<Task> tasksNew = targetDao._queryHistoryTask_Tasks(TASK_ID);
            synchronized (this) {
                if(tasks == null) {
                    tasks = tasksNew;
                }
            }
        }
        return tasks;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 668181820)
    public synchronized void resetTasks() {
        tasks = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 931169897)
    public List<MultiMedia> getMultiMedias() {
        if (multiMedias == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MultiMediaDao targetDao = daoSession.getMultiMediaDao();
            List<MultiMedia> multiMediasNew = targetDao._queryHistoryTask_MultiMedias(TASK_ID, TASK_STATE, TASK_TYPE);
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
    @Generated(hash = 2108138162)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getHistoryTaskDao() : null;
    }


}
