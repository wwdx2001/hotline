package com.sh3h.localprovider.greendaoEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by dengzhimin on 2016/9/27.
 * 多媒体
 */

@Entity(nameInDb = "HL_MULTIMEDIA")
public class MultiMedia {

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

    @NotNull
    private int FILE_TYPE;//文件类型

    @NotNull
    private String FILE_NAME;//文件名称

    private String FILE_HASH;//文件上传后服务器返回文件Hash

    private String FILE_URL;//文件上传后服务器返回文件URL

    @NotNull
    private int UPLOAD_FLAG;//上传标志

    private String EXTEND;//扩展信息

    private long FILE_TIME;


    @Generated(hash = 1447662402)
    public MultiMedia(Long ID, int USER_ID, @NotNull String TASK_ID, int TASK_TYPE,
            int TASK_STATE, int FILE_TYPE, @NotNull String FILE_NAME,
            String FILE_HASH, String FILE_URL, int UPLOAD_FLAG, String EXTEND,
            long FILE_TIME) {
        this.ID = ID;
        this.USER_ID = USER_ID;
        this.TASK_ID = TASK_ID;
        this.TASK_TYPE = TASK_TYPE;
        this.TASK_STATE = TASK_STATE;
        this.FILE_TYPE = FILE_TYPE;
        this.FILE_NAME = FILE_NAME;
        this.FILE_HASH = FILE_HASH;
        this.FILE_URL = FILE_URL;
        this.UPLOAD_FLAG = UPLOAD_FLAG;
        this.EXTEND = EXTEND;
        this.FILE_TIME = FILE_TIME;
    }

    @Generated(hash = 704612124)
    public MultiMedia() {
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

    public int getFILE_TYPE() {
        return this.FILE_TYPE;
    }

    public void setFILE_TYPE(int FILE_TYPE) {
        this.FILE_TYPE = FILE_TYPE;
    }

    public String getFILE_NAME() {
        return this.FILE_NAME;
    }

    public void setFILE_NAME(String FILE_NAME) {
        this.FILE_NAME = FILE_NAME;
    }

    public String getFILE_HASH() {
        return this.FILE_HASH;
    }

    public void setFILE_HASH(String FILE_HASH) {
        this.FILE_HASH = FILE_HASH;
    }

    public String getFILE_URL() {
        return this.FILE_URL;
    }

    public void setFILE_URL(String FILE_URL) {
        this.FILE_URL = FILE_URL;
    }

    public int getUPLOAD_FLAG() {
        return this.UPLOAD_FLAG;
    }

    public void setUPLOAD_FLAG(int UPLOAD_FLAG) {
        this.UPLOAD_FLAG = UPLOAD_FLAG;
    }

    public String getEXTEND() {
        return EXTEND;
    }

    public void setEXTEND(String EXTEND) {
        this.EXTEND = EXTEND;
    }

    public long getFILE_TIME() {
        return this.FILE_TIME;
    }

    public void setFILE_TIME(long FILE_TIME) {
        this.FILE_TIME = FILE_TIME;
    }
}
