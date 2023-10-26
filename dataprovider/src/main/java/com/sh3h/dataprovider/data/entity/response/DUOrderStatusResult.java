package com.sh3h.dataprovider.data.entity.response;

/**
 * 工单状态查询返回结果
 * Created by dengzhimin on 2016/10/12.
 */
public class DUOrderStatusResult {

    private String taskId;//任务编号

    private int taskState;//任务状态

    private long replyTime;//回复时间

    private String extend;

    public DUOrderStatusResult(String taskId, int taskState, long replyTime, String extend) {
        this.taskId = taskId;
        this.taskState = taskState;
        this.replyTime = replyTime;
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

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
