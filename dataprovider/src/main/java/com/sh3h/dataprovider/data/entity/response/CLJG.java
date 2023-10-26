package com.sh3h.dataprovider.data.entity.response;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/7/26 09:37
 */
@Entity
public class CLJG {

    /**
     * CLJG_ID : R
     * DESCR : 实际解决
     */

    private String CLJG_ID;
    private String DESCR;

    @Generated(hash = 558479200)
    public CLJG(String CLJG_ID, String DESCR) {
        this.CLJG_ID = CLJG_ID;
        this.DESCR = DESCR;
    }

    @Generated(hash = 70375263)
    public CLJG() {
    }

    public String getCLJG_ID() {
        return CLJG_ID;
    }

    public void setCLJG_ID(String CLJG_ID) {
        this.CLJG_ID = CLJG_ID;
    }

    public String getDESCR() {
        return DESCR;
    }

    public void setDESCR(String DESCR) {
        this.DESCR = DESCR;
    }
}
