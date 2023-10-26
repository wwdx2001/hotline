package com.sh3h.hotline.event;

import com.sh3h.dataprovider.data.entity.newentity.OverrateCallHandleEntity;

public class CallTransferDataEvent {
    private OverrateCallHandleEntity data;

    public CallTransferDataEvent(OverrateCallHandleEntity data) {
        this.data = data;
    }

    public OverrateCallHandleEntity getData() {
        return data;
    }

    public void setData(OverrateCallHandleEntity data) {
        this.data = data;
    }
}
