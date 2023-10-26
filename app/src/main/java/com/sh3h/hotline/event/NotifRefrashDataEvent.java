package com.sh3h.hotline.event;

public class NotifRefrashDataEvent {
    private boolean isSuccess;

    public NotifRefrashDataEvent(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
