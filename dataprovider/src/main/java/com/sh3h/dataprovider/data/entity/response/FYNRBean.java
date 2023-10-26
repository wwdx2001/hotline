package com.sh3h.dataprovider.data.entity.response;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/5/17 10:27
 */
@Entity
public class FYNRBean {

    /**
     * NR_ID : 0901
     * DESCR : 违章用水
     * FA_TYPE_CD : BWWT
     * BUS_TYPE : BX
     */

    private String NR_ID;
    private String DESCR;
    private String FA_TYPE_CD;
    private String BUS_TYPE;

    @Generated(hash = 1619042100)
    public FYNRBean(String NR_ID, String DESCR, String FA_TYPE_CD, String BUS_TYPE) {
        this.NR_ID = NR_ID;
        this.DESCR = DESCR;
        this.FA_TYPE_CD = FA_TYPE_CD;
        this.BUS_TYPE = BUS_TYPE;
    }

    @Generated(hash = 1924839820)
    public FYNRBean() {
    }

    public String getNR_ID() {
        return NR_ID;
    }

    public void setNR_ID(String NR_ID) {
        this.NR_ID = NR_ID;
    }

    public String getDESCR() {
        return DESCR;
    }

    public void setDESCR(String DESCR) {
        this.DESCR = DESCR;
    }

    public String getFA_TYPE_CD() {
        return FA_TYPE_CD;
    }

    public void setFA_TYPE_CD(String FA_TYPE_CD) {
        this.FA_TYPE_CD = FA_TYPE_CD;
    }

    public String getBUS_TYPE() {
        return BUS_TYPE;
    }

    public void setBUS_TYPE(String BUS_TYPE) {
        this.BUS_TYPE = BUS_TYPE;
    }
}
