package com.sh3h.dataprovider.data.entity.response;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/5/17 10:28
 */
@Entity
public class FYLXBean {

    /**
     * LB_ID : 1
     * FA_TYPE_CD : BWWT
     * FA_TYPE_DESCR : 表务问题
     * BUS_TYPE : BX
     */

    private String LB_ID;
    private String FA_TYPE_CD;
    private String FA_TYPE_DESCR;
    private String BUS_TYPE;

    @Generated(hash = 1819111313)
    public FYLXBean(String LB_ID, String FA_TYPE_CD, String FA_TYPE_DESCR,
            String BUS_TYPE) {
        this.LB_ID = LB_ID;
        this.FA_TYPE_CD = FA_TYPE_CD;
        this.FA_TYPE_DESCR = FA_TYPE_DESCR;
        this.BUS_TYPE = BUS_TYPE;
    }

    @Generated(hash = 845994322)
    public FYLXBean() {
    }

    public String getLB_ID() {
        return LB_ID;
    }

    public void setLB_ID(String LB_ID) {
        this.LB_ID = LB_ID;
    }

    public String getFA_TYPE_CD() {
        return FA_TYPE_CD;
    }

    public void setFA_TYPE_CD(String FA_TYPE_CD) {
        this.FA_TYPE_CD = FA_TYPE_CD;
    }

    public String getFA_TYPE_DESCR() {
        return FA_TYPE_DESCR;
    }

    public void setFA_TYPE_DESCR(String FA_TYPE_DESCR) {
        this.FA_TYPE_DESCR = FA_TYPE_DESCR;
    }

    public String getBUS_TYPE() {
        return BUS_TYPE;
    }

    public void setBUS_TYPE(String BUS_TYPE) {
        this.BUS_TYPE = BUS_TYPE;
    }
}
