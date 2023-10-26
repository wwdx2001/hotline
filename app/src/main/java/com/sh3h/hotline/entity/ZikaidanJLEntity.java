package com.sh3h.hotline.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 自开单记录
 *
 * @author xiaochao.dev@gamil.com
 * @date 2019/4/4 09:35
 */
@Entity
public class ZikaidanJLEntity implements Parcelable {
    /**
     * 是否现场处理 1，是 0，不是
     */
    private int isXianChangCL;


    /**
     * 服务点编号
     */
    private String fuwudianBH;

    /**
     * 反应类型
     */
    private String fanyingLX;

    /**
     * 反应内容
     */
    private String fanyingNR;

    /**
     * 处理级别
     */
    private String chuliJB;

    /**
     * 开单时间
     */
    private String commitDate;

    private String zuoyeren;

    private String tijiaoren;

    private String xbbx;

    /**
     * 发生地址
     */
    private String fashengDZ;

    private String fylx;// 反映类型

    private String gls;// 管理所

    private String sszd;// 所属站点

    private String zhbh;// 账户编号

    private String lxdh;// 联系电话

    private String wzzt;// 违章主体

    private String wzdd;// 违章地点

    private String wznr;// 违章内容

    private String mpbh;// 铭牌编号

    private String ywlx;// 疑问类型

    private String xccm;// 现场抄码

    private String xcqkbz;// 现场情况备注

    public String getXbbx() {
        return xbbx;
    }

    public void setXbbx(String xbbx) {
        this.xbbx = xbbx;
    }

    public String getZuoyeren() {
        return zuoyeren;
    }

    public void setZuoyeren(String zuoyeren) {
        this.zuoyeren = zuoyeren;
    }

    public String getTijiaoren() {
        return tijiaoren;
    }

    public void setTijiaoren(String tijiaoren) {
        this.tijiaoren = tijiaoren;
    }

    public int getIsXianChangCL() {
        return this.isXianChangCL;
    }

    public void setIsXianChangCL(int isXianChangCL) {
        this.isXianChangCL = isXianChangCL;
    }

    public String getFuwudianBH() {
        return this.fuwudianBH;
    }

    public void setFuwudianBH(String fuwudianBH) {
        this.fuwudianBH = fuwudianBH;
    }

    public String getFanyingLX() {
        return this.fanyingLX;
    }

    public void setFanyingLX(String fanyingLX) {
        this.fanyingLX = fanyingLX;
    }

    public String getFanyingNR() {
        return this.fanyingNR;
    }

    public void setFanyingNR(String fanyingNR) {
        this.fanyingNR = fanyingNR;
    }

    public String getChuliJB() {
        return this.chuliJB;
    }

    public void setChuliJB(String chuliJB) {
        this.chuliJB = chuliJB;
    }

    public String getCommitDate() {
        return this.commitDate;
    }

    public void setCommitDate(String commitDate) {
        this.commitDate = commitDate;
    }

    public String getFashengDZ() {
        return this.fashengDZ;
    }

    public void setFashengDZ(String fashengDZ) {
        this.fashengDZ = fashengDZ;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.isXianChangCL);
        dest.writeString(this.fuwudianBH);
        dest.writeString(this.fanyingLX);
        dest.writeString(this.fanyingNR);
        dest.writeString(this.chuliJB);
        dest.writeString(this.commitDate);
        dest.writeString(this.zuoyeren);
        dest.writeString(this.tijiaoren);
        dest.writeString(this.xbbx);
        dest.writeString(this.fashengDZ);
    }

    public String getGls() {
        return this.gls;
    }

    public void setGls(String gls) {
        this.gls = gls;
    }

    public String getSszd() {
        return this.sszd;
    }

    public void setSszd(String sszd) {
        this.sszd = sszd;
    }

    public String getZhbh() {
        return this.zhbh;
    }

    public void setZhbh(String zhbh) {
        this.zhbh = zhbh;
    }

    public String getLxdh() {
        return this.lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getWzzt() {
        return this.wzzt;
    }

    public void setWzzt(String wzzt) {
        this.wzzt = wzzt;
    }

    public String getWzdd() {
        return this.wzdd;
    }

    public void setWzdd(String wzdd) {
        this.wzdd = wzdd;
    }

    public String getWznr() {
        return this.wznr;
    }

    public void setWznr(String wznr) {
        this.wznr = wznr;
    }

    public String getMpbh() {
        return this.mpbh;
    }

    public void setMpbh(String mpbh) {
        this.mpbh = mpbh;
    }

    public String getYwlx() {
        return this.ywlx;
    }

    public void setYwlx(String ywlx) {
        this.ywlx = ywlx;
    }

    public String getXccm() {
        return this.xccm;
    }

    public void setXccm(String xccm) {
        this.xccm = xccm;
    }

    public String getXcqkbz() {
        return this.xcqkbz;
    }

    public void setXcqkbz(String xcqkbz) {
        this.xcqkbz = xcqkbz;
    }

    public String getFylx() {
        return this.fylx;
    }

    public void setFylx(String fylx) {
        this.fylx = fylx;
    }

    protected ZikaidanJLEntity(Parcel in) {
        this.isXianChangCL = in.readInt();
        this.fuwudianBH = in.readString();
        this.fanyingLX = in.readString();
        this.fanyingNR = in.readString();
        this.chuliJB = in.readString();
        this.commitDate = in.readString();
        this.zuoyeren = in.readString();
        this.tijiaoren = in.readString();
        this.xbbx = in.readString();
        this.fashengDZ = in.readString();
    }

    @Generated(hash = 1955549158)
    public ZikaidanJLEntity(int isXianChangCL, String fuwudianBH, String fanyingLX,
            String fanyingNR, String chuliJB, String commitDate, String zuoyeren,
            String tijiaoren, String xbbx, String fashengDZ, String fylx, String gls,
            String sszd, String zhbh, String lxdh, String wzzt, String wzdd, String wznr,
            String mpbh, String ywlx, String xccm, String xcqkbz) {
        this.isXianChangCL = isXianChangCL;
        this.fuwudianBH = fuwudianBH;
        this.fanyingLX = fanyingLX;
        this.fanyingNR = fanyingNR;
        this.chuliJB = chuliJB;
        this.commitDate = commitDate;
        this.zuoyeren = zuoyeren;
        this.tijiaoren = tijiaoren;
        this.xbbx = xbbx;
        this.fashengDZ = fashengDZ;
        this.fylx = fylx;
        this.gls = gls;
        this.sszd = sszd;
        this.zhbh = zhbh;
        this.lxdh = lxdh;
        this.wzzt = wzzt;
        this.wzdd = wzdd;
        this.wznr = wznr;
        this.mpbh = mpbh;
        this.ywlx = ywlx;
        this.xccm = xccm;
        this.xcqkbz = xcqkbz;
    }

    @Generated(hash = 2138104926)
    public ZikaidanJLEntity() {
    }

    public static final Creator<ZikaidanJLEntity> CREATOR = new Creator<ZikaidanJLEntity>() {
        @Override
        public ZikaidanJLEntity createFromParcel(Parcel source) {
            return new ZikaidanJLEntity(source);
        }

        @Override
        public ZikaidanJLEntity[] newArray(int size) {
            return new ZikaidanJLEntity[size];
        }
    };
}
