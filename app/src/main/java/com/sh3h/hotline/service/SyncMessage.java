package com.sh3h.hotline.service;


import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.request.DUReply;

public class SyncMessage {
    private SyncType syncType;
    private int userId;
    private String taskId;
    private int taskType;
    private int taskState;//任务状态
    private long replyTime;
    private boolean isFromMyTask;//是否来自于“我的工单”

    public SyncMessage() {
        this.syncType = SyncType.NONE;
    }

    public SyncMessage(int taskType) {
        this.taskType = taskType;
    }

    public SyncMessage(int userId, SyncType syncType) {
        this.userId = userId;
        this.syncType = syncType;
    }

    public SyncMessage(int userId, SyncType syncType, String taskId,
                       int taskType, int taskState, long replyTime) {
        this.userId = userId;
        this.syncType = syncType;
        this.taskId = taskId;
        this.taskType = taskType;
        this.taskState = taskState;
        this.replyTime = replyTime;
    }

    public SyncMessage(int userId, String taskId,int taskType,SyncType syncType) {
        this.userId = userId;
        this.taskId = taskId;
        this.taskType=taskType;
        this.syncType = syncType;
    }

    public SyncMessage(int userId, int taskType, boolean isFromMyTask, SyncType syncType) {
        this.userId = userId;
        this.taskType = taskType;
        this.isFromMyTask = isFromMyTask;
        this.syncType = syncType;
    }

    public SyncType getSyncType() {
        return syncType;
    }

    public void setSyncType(SyncType syncType) {
        this.syncType = syncType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public long getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(long replyTime) {
        this.replyTime = replyTime;
    }

    public boolean isFromMyTask() {
        return isFromMyTask;
    }

    public void setFromMyTask(boolean fromMyTask) {
        isFromMyTask = fromMyTask;
    }
}
