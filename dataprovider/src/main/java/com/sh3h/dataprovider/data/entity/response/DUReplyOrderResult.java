package com.sh3h.dataprovider.data.entity.response;

/**
 * 工单处理回复结果
 * Created by zhangjing on 2016/10/10.
 */
public class DUReplyOrderResult {


    /**
     * extend : 字符串内容
     * isSuccess : true
     * message : 字符串内容
     * taskId : 字符串内容
     * taskState : 字符串内容
     */

    private String extend;
    private boolean isSuccess;
    private String message;
    private String taskId;
    private String taskState;

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public boolean isIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
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

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public boolean getIsSuccess() {
        return isSuccess;
    }
}
