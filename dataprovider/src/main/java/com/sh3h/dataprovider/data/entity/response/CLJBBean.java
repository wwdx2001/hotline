package com.sh3h.dataprovider.data.entity.response;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/5/17 10:30
 */
@Entity
public class CLJBBean {

    /**
     * CLJB_ID : 0.25
     * DESCR : 15分钟
     */

    private String CLJB_ID;
    private String DESCR;

    @Generated(hash = 1748317695)
    public CLJBBean(String CLJB_ID, String DESCR) {
        this.CLJB_ID = CLJB_ID;
        this.DESCR = DESCR;
    }

    @Generated(hash = 166906238)
    public CLJBBean() {
    }

    public String getCLJB_ID() {
        return CLJB_ID;
    }

    public void setCLJB_ID(String CLJB_ID) {
        this.CLJB_ID = CLJB_ID;
    }

    public String getDESCR() {
        return DESCR;
    }

    public void setDESCR(String DESCR) {
        this.DESCR = DESCR;
    }
}
