package com.sh3h.dataprovider.data.entity.request;

/**
 * gogndan
 * Created by dengzhimin on 2016/10/11.
 */

public class DUHandle {

    private String taskId;//任务编号

    private int taskState;//任务状态

    private long replyTime;//回复时间

    private String resolveType;//处理类别

    private String resolveContent;//处理内容

    private String issueReason;//发生原因

    private String resolveMethod;//解决措施

    private String resolveComment;//处理备注

    private long delayTime;//延期时间

    private String otherReason;//原因

    private String extend;

    public DUHandle(String taskId, int taskState, long replyTime, String resolveType, String resolveContent, String issueReason, String resolveMethod,
                    String resolveComment, long delayTime, String otherReason, String extend) {
        this.taskId = taskId;
        this.taskState = taskState;
        this.replyTime = replyTime;
        this.resolveType = resolveType;
        this.resolveContent = resolveContent;
        this.issueReason = issueReason;
        this.resolveMethod = resolveMethod;
        this.resolveComment = resolveComment;
        this.delayTime = delayTime;
        this.otherReason = otherReason;
        this.extend = extend;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public String getResolveType() {
        return resolveType;
    }

    public void setResolveType(String resolveType) {
        this.resolveType = resolveType;
    }

    public String getResolveContent() {
        return resolveContent;
    }

    public void setResolveContent(String resolveContent) {
        this.resolveContent = resolveContent;
    }

    public String getIssueReason() {
        return issueReason;
    }

    public void setIssueReason(String issueReason) {
        this.issueReason = issueReason;
    }

    public String getResolveMethod() {
        return resolveMethod;
    }

    public void setResolveMethod(String resolveMethod) {
        this.resolveMethod = resolveMethod;
    }

    public String getResolveComment() {
        return resolveComment;
    }

    public void setResolveComment(String resolveComment) {
        this.resolveComment = resolveComment;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public String getOtherReason() {
        return otherReason;
    }

    public void setOtherReason(String otherReason) {
        this.otherReason = otherReason;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
