package com.sh3h.dataprovider.data.entity.response;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/5/17 10:25
 */
@Entity
public class FYLYBean {

    /**
     * LY_ID : 001
     * DESCR : 客户-居民
     */

    private String LY_ID;
    private String DESCR;

    @Generated(hash = 141815909)
    public FYLYBean(String LY_ID, String DESCR) {
        this.LY_ID = LY_ID;
        this.DESCR = DESCR;
    }

    @Generated(hash = 1675360878)
    public FYLYBean() {
    }

    public String getLY_ID() {
        return LY_ID;
    }

    public void setLY_ID(String LY_ID) {
        this.LY_ID = LY_ID;
    }

    public String getDESCR() {
        return DESCR;
    }

    public void setDESCR(String DESCR) {
        this.DESCR = DESCR;
    }
}
