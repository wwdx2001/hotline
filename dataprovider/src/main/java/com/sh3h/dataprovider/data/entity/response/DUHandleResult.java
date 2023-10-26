package com.sh3h.dataprovider.data.entity.response;

/**
 * 工单任务处理返回结果
 * Created by dengzhimin on 2016/10/11
 */

public class DUHandleResult {

    private boolean isSuccess;//是否成功

    private String message;//

    private String taskId;//任务编号

    private int taskState;//任务状态

    private String extend;

    public DUHandleResult(boolean isSuccess, String message, String taskId, int taskState, String extend) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.taskId = taskId;
        this.taskState = taskState;
        this.extend = extend;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
