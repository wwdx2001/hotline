package com.sh3h.dataprovider.data.entity.response;

/**
 * 自开单上传返回结果
 * Created by dengzhimin on 2016/10/11.
 */

public class DUCreateSelfOrderResult {

    private boolean isSuccess;//是否成功

    private String message;

    private String localTaskId;//本地任务编号

    private String serverTaskId;//服务器上任务编号

    private String extend;

    public DUCreateSelfOrderResult(boolean isSuccess, String message, String localTaskId, String serverTaskId, String extend) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.localTaskId = localTaskId;
        this.serverTaskId = serverTaskId;
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

    public String getLocalTaskId() {
        return localTaskId;
    }

    public void setLocalTaskId(String localTaskId) {
        this.localTaskId = localTaskId;
    }

    public String getServerTaskId() {
        return serverTaskId;
    }

    public void setServerTaskId(String serverTaskId) {
        this.serverTaskId = serverTaskId;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
