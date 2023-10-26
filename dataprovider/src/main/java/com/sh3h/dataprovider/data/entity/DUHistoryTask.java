package com.sh3h.dataprovider.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.sh3h.dataprovider.data.entity.request.DUReply;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;

import java.util.List;

/**
 * 历史工单封装类，DataManager和UI层交互
 * Created by zhangjing on 2016/10/17.
 */

public class DUHistoryTask implements Parcelable {
    public static String DUHistoryTask_ID = "historyID";

    /**
     * history information
     */
    private long ID; //主键

    private int USER_ID;//用户编号

    private String TASK_ID;//任务编号

    private int TASK_TYPE;//任务类型

    private int TASK_STATE;//任务状态

    private String TASK_CONTENT;//任务内容

    private String TASK_REPLY;//任务处理

    private long REPLY_TIME;//处理时间

    private int UPLOAD_FLAG;//上传标志

    /**
     * task information
     */
    //户名
    private String HU_MING;

    //电话
    private String TELEPHONE;

    //地址
    private String ADDRESS;

    //反映人
    private String ISSUER;

    //发生时间
    private long ISSUE_TIME;

    //反映类型
    private String ISSUE_TYPE;

    private String ISSUE_ORIGIN;

    //反映内容
    private String ISSUE_CONTENT;

    //处理级别
    private String REPLY_CLASS;

    //处理时限
    private long REPLY_DEADLINE;

    // 上传标志
    private int TASK_UPLOAD_FLAG;

    // 现场处理标志
    private boolean replyImmediately;

    // server task id
    private String serverTaskId;

    /**
     * the replay part of history
     */
    private DUReply duReply;

    /**
     * relative medias
     */
    private List<DUMedia> duMedias;

    private DUMyTask duMyTask;

    public DUHistoryTask() {
    }

    public DUHistoryTask(long ID, int USER_ID, String TASK_ID, int TASK_TYPE, int TASK_STATE, String TASK_CONTENT, String TASK_REPLY, long REPLY_TIME, int UPLOAD_FLAG, String HU_MING, String TELEPHONE, String ADDRESS, String ISSUER, long ISSUE_TIME, String ISSUE_TYPE, String ISSUE_ORIGIN, String ISSUE_CONTENT, String REPLY_CLASS, long REPLY_DEADLINE, int TASK_UPLOAD_FLAG, boolean replyImmediately, String serverTaskId, DUReply duReply, List<DUMedia> duMedias, DUMyTask duMyTask) {
        this.ID = ID;
        this.USER_ID = USER_ID;
        this.TASK_ID = TASK_ID;
        this.TASK_TYPE = TASK_TYPE;
        this.TASK_STATE = TASK_STATE;
        this.TASK_CONTENT = TASK_CONTENT;
        this.TASK_REPLY = TASK_REPLY;
        this.REPLY_TIME = REPLY_TIME;
        this.UPLOAD_FLAG = UPLOAD_FLAG;
        this.HU_MING = HU_MING;
        this.TELEPHONE = TELEPHONE;
        this.ADDRESS = ADDRESS;
        this.ISSUER = ISSUER;
        this.ISSUE_TIME = ISSUE_TIME;
        this.ISSUE_TYPE = ISSUE_TYPE;
        this.ISSUE_ORIGIN = ISSUE_ORIGIN;
        this.ISSUE_CONTENT = ISSUE_CONTENT;
        this.REPLY_CLASS = REPLY_CLASS;
        this.REPLY_DEADLINE = REPLY_DEADLINE;
        this.TASK_UPLOAD_FLAG = TASK_UPLOAD_FLAG;
        this.replyImmediately = replyImmediately;
        this.serverTaskId = serverTaskId;
        this.duReply = duReply;
        this.duMedias = duMedias;
        this.duMyTask = duMyTask;
    }

    protected DUHistoryTask(Parcel in) {
        ID = in.readLong();
        USER_ID = in.readInt();
        TASK_ID = in.readString();
        TASK_TYPE = in.readInt();
        TASK_STATE = in.readInt();
        TASK_CONTENT = in.readString();
        TASK_REPLY = in.readString();
        REPLY_TIME = in.readLong();
        UPLOAD_FLAG = in.readInt();
        HU_MING = in.readString();
        TELEPHONE = in.readString();
        ADDRESS = in.readString();
        ISSUER = in.readString();
        ISSUE_TIME = in.readLong();
        ISSUE_TYPE = in.readString();
        ISSUE_ORIGIN = in.readString();
        ISSUE_CONTENT = in.readString();
        REPLY_CLASS = in.readString();
        REPLY_DEADLINE = in.readLong();
        TASK_UPLOAD_FLAG = in.readInt();
        replyImmediately = in.readByte() != 0;
        serverTaskId = in.readString();
        duReply = in.readParcelable(DUReply.class.getClassLoader());
        duMyTask = in.readParcelable(DUMyTask.class.getClassLoader());
    }

    public static final Creator<DUHistoryTask> CREATOR = new Creator<DUHistoryTask>() {
        @Override
        public DUHistoryTask createFromParcel(Parcel in) {
            return new DUHistoryTask(in);
        }

        @Override
        public DUHistoryTask[] newArray(int size) {
            return new DUHistoryTask[size];
        }
    };

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public int getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(int USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getTASK_ID() {
        return TASK_ID;
    }

    public void setTASK_ID(String TASK_ID) {
        this.TASK_ID = TASK_ID;
    }

    public int getTASK_TYPE() {
        return TASK_TYPE;
    }

    public void setTASK_TYPE(int TASK_TYPE) {
        this.TASK_TYPE = TASK_TYPE;
    }

    public int getTASK_STATE() {
        return TASK_STATE;
    }

    public void setTASK_STATE(int TASK_STATE) {
        this.TASK_STATE = TASK_STATE;
    }

    public String getTASK_CONTENT() {
        return TASK_CONTENT;
    }

    public void setTASK_CONTENT(String TASK_CONTENT) {
        this.TASK_CONTENT = TASK_CONTENT;
    }

    public String getTASK_REPLY() {
        return TASK_REPLY;
    }

    public void setTASK_REPLY(String TASK_REPLY) {
        this.TASK_REPLY = TASK_REPLY;
    }

    public long getREPLY_TIME() {
        return REPLY_TIME;
    }

    public void setREPLY_TIME(long REPLY_TIME) {
        this.REPLY_TIME = REPLY_TIME;
    }

    public int getUPLOAD_FLAG() {
        return UPLOAD_FLAG;
    }

    public void setUPLOAD_FLAG(int UPLOAD_FLAG) {
        this.UPLOAD_FLAG = UPLOAD_FLAG;
    }

    public String getHU_MING() {
        return HU_MING;
    }

    public void setHU_MING(String HU_MING) {
        this.HU_MING = HU_MING;
    }

    public String getTELEPHONE() {
        return TELEPHONE;
    }

    public void setTELEPHONE(String TELEPHONE) {
        this.TELEPHONE = TELEPHONE;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getISSUER() {
        return ISSUER;
    }

    public void setISSUER(String ISSUER) {
        this.ISSUER = ISSUER;
    }

    public long getISSUE_TIME() {
        return ISSUE_TIME;
    }

    public void setISSUE_TIME(long ISSUE_TIME) {
        this.ISSUE_TIME = ISSUE_TIME;
    }

    public String getISSUE_TYPE() {
        return ISSUE_TYPE;
    }

    public void setISSUE_TYPE(String ISSUE_TYPE) {
        this.ISSUE_TYPE = ISSUE_TYPE;
    }

    public String getISSUE_ORIGIN() {
        return ISSUE_ORIGIN;
    }

    public void setISSUE_ORIGIN(String ISSUE_ORIGIN) {
        this.ISSUE_ORIGIN = ISSUE_ORIGIN;
    }

    public String getISSUE_CONTENT() {
        return ISSUE_CONTENT;
    }

    public void setISSUE_CONTENT(String ISSUE_CONTENT) {
        this.ISSUE_CONTENT = ISSUE_CONTENT;
    }

    public String getREPLY_CLASS() {
        return REPLY_CLASS;
    }

    public void setREPLY_CLASS(String REPLY_CLASS) {
        this.REPLY_CLASS = REPLY_CLASS;
    }

    public long getREPLY_DEADLINE() {
        return REPLY_DEADLINE;
    }

    public void setREPLY_DEADLINE(long REPLY_DEADLINE) {
        this.REPLY_DEADLINE = REPLY_DEADLINE;
    }

    public int getTASK_UPLOAD_FLAG() {
        return TASK_UPLOAD_FLAG;
    }

    public void setTASK_UPLOAD_FLAG(int TASK_UPLOAD_FLAG) {
        this.TASK_UPLOAD_FLAG = TASK_UPLOAD_FLAG;
    }

    public boolean isReplyImmediately() {
        return replyImmediately;
    }

    public void setReplyImmediately(boolean replyImmediately) {
        this.replyImmediately = replyImmediately;
    }

    public String getServerTaskId() {
        return serverTaskId;
    }

    public void setServerTaskId(String serverTaskId) {
        this.serverTaskId = serverTaskId;
    }

    public DUReply getDuReply() {
        return duReply;
    }

    public void setDuReply(DUReply duReply) {
        this.duReply = duReply;
    }

    public List<DUMedia> getDuMedias() {
        return duMedias;
    }

    public void setDuMedias(List<DUMedia> duMedias) {
        this.duMedias = duMedias;
    }

    public DUMyTask getDuMyTask() {
        return duMyTask;
    }

    public void setDuMyTask(DUMyTask duMyTask) {
        this.duMyTask = duMyTask;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(ID);
        dest.writeInt(USER_ID);
        dest.writeString(TASK_ID);
        dest.writeInt(TASK_TYPE);
        dest.writeInt(TASK_STATE);
        dest.writeString(TASK_CONTENT);
        dest.writeString(TASK_REPLY);
        dest.writeLong(REPLY_TIME);
        dest.writeInt(UPLOAD_FLAG);
        dest.writeString(HU_MING);
        dest.writeString(TELEPHONE);
        dest.writeString(ADDRESS);
        dest.writeString(ISSUER);
        dest.writeLong(ISSUE_TIME);
        dest.writeString(ISSUE_TYPE);
        dest.writeString(ISSUE_ORIGIN);
        dest.writeString(ISSUE_CONTENT);
        dest.writeString(REPLY_CLASS);
        dest.writeLong(REPLY_DEADLINE);
        dest.writeInt(TASK_UPLOAD_FLAG);
        dest.writeByte((byte) (replyImmediately ? 1 : 0));
        dest.writeString(serverTaskId);
        dest.writeParcelable(duReply, flags);
        dest.writeParcelable(duMyTask, flags);
    }
}
