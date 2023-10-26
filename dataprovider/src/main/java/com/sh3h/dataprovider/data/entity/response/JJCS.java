package com.sh3h.dataprovider.data.entity.response;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/6/24 14:52
 */
@Entity
public class JJCS {

    /**
     * CS_ID : JJ101
     * DESCR : 通知客户修理
     * FA_TYPE_CD : BWWT
     * FA_TYPE_DESCR : 表务问题
     */

    private String CS_ID;
    private String DESCR;
    private String FA_TYPE_CD;
    private String FA_TYPE_DESCR;

    @Generated(hash = 658284156)
    public JJCS(String CS_ID, String DESCR, String FA_TYPE_CD, String FA_TYPE_DESCR) {
        this.CS_ID = CS_ID;
        this.DESCR = DESCR;
        this.FA_TYPE_CD = FA_TYPE_CD;
        this.FA_TYPE_DESCR = FA_TYPE_DESCR;
    }

    @Generated(hash = 1897701798)
    public JJCS() {
    }

    public String getCS_ID() {
        return CS_ID;
    }

    public void setCS_ID(String CS_ID) {
        this.CS_ID = CS_ID;
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

    public String getFA_TYPE_DESCR() {
        return FA_TYPE_DESCR;
    }

    public void setFA_TYPE_DESCR(String FA_TYPE_DESCR) {
        this.FA_TYPE_DESCR = FA_TYPE_DESCR;
    }
}
