package com.sh3h.dataprovider.data.entity.newentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class RemoteHandleEntity {
    @Id(autoincrement = true)
    private Long ID;

    private String albh;
    private String pch;
    private String xh;
    private String yhh;
    private String zhbh;

    private String cbds;
    private String dssj;
    private String bz;

    @Generated(hash = 1800859971)
    public RemoteHandleEntity(Long ID, String albh, String pch, String xh,
            String yhh, String zhbh, String cbds, String dssj, String bz) {
        this.ID = ID;
        this.albh = albh;
        this.pch = pch;
        this.xh = xh;
        this.yhh = yhh;
        this.zhbh = zhbh;
        this.cbds = cbds;
        this.dssj = dssj;
        this.bz = bz;
    }

    @Generated(hash = 1587286097)
    public RemoteHandleEntity() {
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

    public String getCbds() {
        return cbds;
    }

    public void setCbds(String cbds) {
        this.cbds = cbds;
    }

    public String getDssj() {
        return dssj;
    }

    public void setDssj(String dssj) {
        this.dssj = dssj;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }
}
