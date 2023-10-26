package com.sh3h.dataprovider.data.entity.request;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 处理工单请求
 * Created by zhangjing on 2016/10/10.
 */

public class DUReply implements Parcelable {
    /**
     * delayTime : 字符串内容
     * extend : 字符串内容
     * issueReason : 字符串内容
     * otherReason : 字符串内容
     * replyTime : 9223372036854775807
     * resolveComment : 字符串内容
     * resolveContent : 字符串内容
     * resolveMethod : 字符串内容
     * resolveType : 字符串内容
     * taskId : 字符串内容
     * taskState : 字符串内容
     */

    private long delayTime;
    private String extend;
    private String issueReason;
    private String otherReason;
    private long replyTime;
    private String resolveComment;
    private String resolveContent;
    private String resolveMethod;
    private String resolveType;
    private String resolveResult;
    private long resolveTime;
    private String taskId;
    private String taskState;
    private boolean isFinishTask;

    public DUReply() {
    }

    public DUReply(long delayTime, String extend, String issueReason, String otherReason,
                   long replyTime, String resolveComment, String resolveContent,
                   String resolveMethod, String resolveType, String resolveResult, long resolveTime,
                   String taskId, String taskState, boolean isFinishTask) {
        this.delayTime = delayTime;
        this.extend = extend;
        this.issueReason = issueReason;
        this.otherReason = otherReason;
        this.replyTime = replyTime;
        this.resolveComment = resolveComment;
        this.resolveContent = resolveContent;
        this.resolveMethod = resolveMethod;
        this.resolveType = resolveType;
        this.resolveResult = resolveResult;
        this.resolveTime = resolveTime;
        this.taskId = taskId;
        this.taskState = taskState;
        this.isFinishTask = isFinishTask;
    }

    protected DUReply(Parcel in) {
        delayTime = in.readLong();
        extend = in.readString();
        issueReason = in.readString();
        otherReason = in.readString();
        replyTime = in.readLong();
        resolveComment = in.readString();
        resolveContent = in.readString();
        resolveMethod = in.readString();
        resolveType = in.readString();
        resolveResult = in.readString();
        resolveTime=in.readLong();
        taskId = in.readString();
        taskState = in.readString();
        isFinishTask = in.readByte() != 0;
    }

    public static final Creator<DUReply> CREATOR = new Creator<DUReply>() {
        @Override
        public DUReply createFromParcel(Parcel in) {
            return new DUReply(in);
        }

        @Override
        public DUReply[] newArray(int size) {
            return new DUReply[size];
        }
    };

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getIssueReason() {
        return issueReason;
    }

    public void setIssueReason(String issueReason) {
        this.issueReason = issueReason;
    }

    public String getOtherReason() {
        return otherReason;
    }

    public void setOtherReason(String otherReason) {
        this.otherReason = otherReason;
    }

    public long getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(long replyTime) {
        this.replyTime = replyTime;
    }

    public String getResolveComment() {
        return resolveComment;
    }

    public void setResolveComment(String resolveComment) {
        this.resolveComment = resolveComment;
    }

    public String getResolveContent() {
        return resolveContent;
    }

    public void setResolveContent(String resolveContent) {
        this.resolveContent = resolveContent;
    }

    public String getResolveMethod() {
        return resolveMethod;
    }

    public void setResolveMethod(String resolveMethod) {
        this.resolveMethod = resolveMethod;
    }

    public String getResolveType() {
        return resolveType;
    }

    public void setResolveType(String resolveType) {
        this.resolveType = resolveType;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(delayTime);
        dest.writeString(extend);
        dest.writeString(issueReason);
        dest.writeString(otherReason);
        dest.writeLong(replyTime);
        dest.writeString(resolveComment);
        dest.writeString(resolveContent);
        dest.writeString(resolveMethod);
        dest.writeString(resolveType);
        dest.writeString(resolveResult);
        dest.writeLong(resolveTime);
        dest.writeString(taskId);
        dest.writeString(taskState);
        dest.writeByte((byte) (isFinishTask ? 1 : 0));
    }

    public String getResolveResult() {
        return resolveResult;
    }

    public void setResolveResult(String resolveResult) {
        this.resolveResult = resolveResult;
    }

    public boolean isFinishTask() {
        return isFinishTask;
    }

    public void setFinishTask(boolean finishTask) {
        isFinishTask = finishTask;
    }

    public long getResolveTime() {
        return resolveTime;
    }

    public void setResolveTime(long resolveTime) {
        this.resolveTime = resolveTime;
    }
}
