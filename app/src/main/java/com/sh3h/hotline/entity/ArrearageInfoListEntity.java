package com.sh3h.hotline.entity;

import java.util.List;

public class ArrearageInfoListEntity {

    /**
     * state : 0
     * msg :
     * data : {"list":[{"acctId":"320001111","accessGrp":"214242","cmTotalAmt":231.12,"cmLeft":0.23}]}
     */

    private int state;
    private String msg;
    private DataBean data;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<ArrearageInfoEntity> list;

        public List<ArrearageInfoEntity> getList() {
            return list;
        }

        public void setList(List<ArrearageInfoEntity> list) {
            this.list = list;
        }
    }
}

