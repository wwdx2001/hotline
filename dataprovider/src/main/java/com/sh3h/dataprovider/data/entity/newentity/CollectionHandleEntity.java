package com.sh3h.dataprovider.data.entity.newentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CollectionHandleEntity {
    @Id(autoincrement = true)
    private Long ID;

    private String albh;
    private String pch;
    private String xh;
    private String yhh;
    private String zhbh;

    private String cjjg;
    private String cjbz;
    private String xxbg;

    private String lxr;
    private String lxfs;

    @Generated(hash = 682922296)
    public CollectionHandleEntity(Long ID, String albh, String pch, String xh,
            String yhh, String zhbh, String cjjg, String cjbz, String xxbg,
            String lxr, String lxfs) {
        this.ID = ID;
        this.albh = albh;
        this.pch = pch;
        this.xh = xh;
        this.yhh = yhh;
        this.zhbh = zhbh;
        this.cjjg = cjjg;
        this.cjbz = cjbz;
        this.xxbg = xxbg;
        this.lxr = lxr;
        this.lxfs = lxfs;
    }

    @Generated(hash = 1143787788)
    public CollectionHandleEntity() {
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

    public String getCjjg() {
        return cjjg;
    }

    public void setCjjg(String cjjg) {
        this.cjjg = cjjg;
    }

    public String getCjbz() {
        return cjbz;
    }

    public void setCjbz(String cjbz) {
        this.cjbz = cjbz;
    }

    public String getXxbg() {
        return xxbg;
    }

    public void setXxbg(String xxbg) {
        this.xxbg = xxbg;
    }

    public String getLxr() {
        return lxr;
    }

    public void setLxr(String lxr) {
        this.lxr = lxr;
    }

    public String getLxfs() {
        return lxfs;
    }

    public void setLxfs(String lxfs) {
        this.lxfs = lxfs;
    }
}
