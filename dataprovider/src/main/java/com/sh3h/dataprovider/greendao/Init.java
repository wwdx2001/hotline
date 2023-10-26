package com.sh3h.dataprovider.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/4/3 11:03
 */
@Entity
public class Init {
    private String testGreendao;

    @Generated(hash = 2036018357)
    public Init(String testGreendao) {
        this.testGreendao = testGreendao;
    }

    @Generated(hash = 2125461712)
    public Init() {
    }

    public String getTestGreendao() {
        return this.testGreendao;
    }

    public void setTestGreendao(String testGreendao) {
        this.testGreendao = testGreendao;
    }
}
