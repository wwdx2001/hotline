package com.sh3h.hotline.event;

import com.sh3h.hotline.service.SyncMessage;

/**
 * Created by zhangjing on 2016/9/9.
 */
public class UIBusEvent {

    public static class InitResult {
        private boolean isSuccess;

        public InitResult(boolean isSuccess) {
            this.isSuccess = isSuccess;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }
    }

    public static class DownloadResult {
        public enum Type {
            TASK,
            WORD
        }

        private Type type;
        private boolean isSuccess;
        private String error;

        public DownloadResult(Type type, boolean isSuccess, String error) {
            this.type = type;
            this.isSuccess = isSuccess;
            this.error = error;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }

    //同步服务忙碌
    public static class ServiceIsBusy {
    }

    //开始同步数据
    public static class SyncDataStart {
    }

    //上传自开单
    public static class UploadCreateSelfOrder {
    }

    //更新toolbar状态
    public static class NotifyToolBar {

        private boolean isShow;

        public NotifyToolBar(boolean isShow) {
            this.isShow = isShow;
        }

        public boolean isShow() {
            return isShow;
        }

        public void setShow(boolean show) {
            isShow = show;
        }
    }

    public static class NotifyMyTasksUI {

    }

    /**
     * 同步完成
     **/
//    public static class SyncFinish {
//        private String message;
//
//        public SyncFinish() {
//        }
//
//        public SyncFinish(String message) {
//            this.message = message;
//        }
//
//        public String getMessage() {
//            return message;
//        }
//
//        public void setMessage(String message) {
//            this.message = message;
//        }
//    }

    /**
     * 同步成功
     **/
    public static class SyncHint {
        private String taskId;
        private String message;

        public SyncHint() {
        }

        public SyncHint(String taskId, String message) {
            this.taskId = taskId;
            this.message = message;
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
    }

    public static class NotifyHistoryTasksUI {

    }

    public static class NotifyHistoryTaskSyncFinish {
        int successCount;
        int failCount;

        public NotifyHistoryTaskSyncFinish(int successCount, int failCount) {
            this.successCount = successCount;
            this.failCount = failCount;
        }

        public int getSuccessCount() {
            return successCount;
        }

        public void setSuccessCount(int successCount) {
            this.successCount = successCount;
        }

        public int getFailCount() {
            return failCount;
        }

        public void setFailCount(int failCount) {
            this.failCount = failCount;
        }
    }

    public static class NotifyDownloadTasks {
        SyncMessage syncMessage;

        public NotifyDownloadTasks(SyncMessage syncMessage) {
            this.syncMessage = syncMessage;
        }

        public SyncMessage getSyncMessage() {
            return syncMessage;
        }
    }

    public static class NotifyBillArrears {
        double totalBillArrears;

        public NotifyBillArrears(double totalBillArrears) {
            this.totalBillArrears = totalBillArrears;
        }

        public double getTotalBillArrears() {
            return totalBillArrears;
        }

        public void setTotalBillArrears(double totalBillArrears) {
            this.totalBillArrears = totalBillArrears;
        }
    }

    /**
     * 录视频
     */
    public static class RecordVideo {
        private String videoPath;

        public RecordVideo(String videoPath) {
            this.videoPath = videoPath;
        }

        public String getVideoPath() {
            return videoPath;
        }

        public void setVideoPath(String videoPath) {
            this.videoPath = videoPath;
        }
    }
}
