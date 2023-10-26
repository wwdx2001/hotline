package com.sh3h.hotline.entity;

import java.util.List;

public class OverrateSelfListEntity {
    private int size;
    private List<OverrateSelfBillingEntity> list;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<OverrateSelfBillingEntity> getList() {
        return list;
    }

    public void setList(List<OverrateSelfBillingEntity> list) {
        this.list = list;
    }
}
