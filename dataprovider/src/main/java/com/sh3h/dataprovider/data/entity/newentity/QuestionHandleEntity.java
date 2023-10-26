package com.sh3h.dataprovider.data.entity.newentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class QuestionHandleEntity {
    @Id(autoincrement = true)
    private Long ID;

    private String albh;
    private String pch;
    private String xh;
    private String yhh;
    private String zhbh;

    private String xcshqk;
    private String shbz;

    @Generated(hash = 1763447791)
    public QuestionHandleEntity(Long ID, String albh, String pch, String xh,
            String yhh, String zhbh, String xcshqk, String shbz) {
        this.ID = ID;
        this.albh = albh;
        this.pch = pch;
        this.xh = xh;
        this.yhh = yhh;
        this.zhbh = zhbh;
        this.xcshqk = xcshqk;
        this.shbz = shbz;
    }

    @Generated(hash = 1539049005)
    public QuestionHandleEntity() {
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getAlbh() {
        return albh;
    }

    public void setAlbh(String albh) {
        this.albh = albh;
    }

    public String getPch() {
        return pch;
    }

    public void setPch(String pch) {
        this.pch = pch;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getYhh() {
        return yhh;
    }

    public void setYhh(String yhh) {
        this.yhh = yhh;
    }

    public String getZhbh() {
        return zhbh;
    }

    public void setZhbh(String zhbh) {
        this.zhbh = zhbh;
    }

    public String getXcshqk() {
        return xcshqk;
    }

    public void setXcshqk(String xcshqk) {
        this.xcshqk = xcshqk;
    }

    public String getShbz() {
        return shbz;
    }

    public void setShbz(String shbz) {
        this.shbz = shbz;
    }
}
