package com.sh3h.dataprovider.data.entity.response;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/6/24 14:53
 */
@Entity
public class FSYY {


    /**
     * YY_ID : YY108
     * DESCR : 门闭估计
     * FA_TYPE_CD : BWWT
     * FA_TYPE_DESCR : 表务问题
     */

    private String YY_ID;
    private String DESCR;
    private String FA_TYPE_CD;
    private String FA_TYPE_DESCR;

    @Generated(hash = 52891048)
    public FSYY(String YY_ID, String DESCR, String FA_TYPE_CD, String FA_TYPE_DESCR) {
        this.YY_ID = YY_ID;
        this.DESCR = DESCR;
        this.FA_TYPE_CD = FA_TYPE_CD;
        this.FA_TYPE_DESCR = FA_TYPE_DESCR;
    }

    @Generated(hash = 126597588)
    public FSYY() {
    }

    public String getYY_ID() {
        return YY_ID;
    }

    public void setYY_ID(String YY_ID) {
        this.YY_ID = YY_ID;
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
