package com.sh3h.dataprovider.data.entity.response;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/7/2 13:14
 */
@Entity
public class XBBX {

    /**
     * MTR_TYPE_CD : 1
     * DESCR : 速度式机械水表
     */

    private String MTR_TYPE_CD;
    private String DESCR;

    @Generated(hash = 1539829652)
    public XBBX(String MTR_TYPE_CD, String DESCR) {
        this.MTR_TYPE_CD = MTR_TYPE_CD;
        this.DESCR = DESCR;
    }

    @Generated(hash = 1497177521)
    public XBBX() {
    }

    public String getMTR_TYPE_CD() {
        return MTR_TYPE_CD;
    }

    public void setMTR_TYPE_CD(String MTR_TYPE_CD) {
        this.MTR_TYPE_CD = MTR_TYPE_CD;
    }

    public String getDESCR() {
        return DESCR;
    }

    public void setDESCR(String DESCR) {
        this.DESCR = DESCR;
    }
}
