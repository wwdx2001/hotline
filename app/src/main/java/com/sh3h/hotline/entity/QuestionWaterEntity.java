package com.sh3h.hotline.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class QuestionWaterEntity implements Parcelable{

    private String fylb;
    private String fynr;
    private String gls;
    private String zd;
    private String sbsj;
    private String zhbh;
    private String lxdh;
    private String cbyid;
    private String albh;
    private String jlbbh;
    private String ywlx;
    private String xccb;
    private String dz;
    private String xcqkbz;
    private String xczp1;
    private String xczp2;
    private String xczp3;
    private String ly;
    private String sp;
    private String bpzp;
    private String qrr;
    private String sfyq;
    private String tdyy;
    private String gdzt;
    private String xchd;


    public void setFylb(String fylb) {
        this.fylb = fylb;
    }

    public String getFylb() {
        return fylb;
    }

    public void setFynr(String fynr) {
        this.fynr = fynr;
    }

    public String getFynr() {
        return fynr;
    }

    public void setGls(String gls) {
        this.gls = gls;
    }

    public String getGls() {
        return gls;
    }

    public void setZd(String zd) {
        this.zd = zd;
    }

    public String getZd() {
        return zd;
    }

    public void setSbsj(String sbsj) {
        this.sbsj = sbsj;
    }

    public String getSbsj() {
        return sbsj;
    }

    public void setZhbh(String zhbh) {
        this.zhbh = zhbh;
    }

    public String getZhbh() {
        return zhbh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setCbyid(String cbyid) {
        this.cbyid = cbyid;
    }

    public String getCbyid() {
        return cbyid;
    }

    public void setAlbh(String albh) {
        this.albh = albh;
    }

    public String getAlbh() {
        return albh;
    }

    public void setJlbbh(String jlbbh) {
        this.jlbbh = jlbbh;
    }

    public String getJlbbh() {
        return jlbbh;
    }

    public void setYwlx(String ywlx) {
        this.ywlx = ywlx;
    }

    public String getYwlx() {
        return ywlx;
    }

    public void setXccb(String xccb) {
        this.xccb = xccb;
    }

    public String getXccb() {
        return xccb;
    }

    public void setDz(String dz) {
        this.dz = dz;
    }

    public String getDz() {
        return dz;
    }

    public void setXcqkbz(String xcqkbz) {
        this.xcqkbz = xcqkbz;
    }

    public String getXcqkbz() {
        return xcqkbz;
    }

    public void setXczp1(String xczp1) {
        this.xczp1 = xczp1;
    }

    public String getXczp1() {
        return xczp1;
    }

    public void setXczp2(String xczp2) {
        this.xczp2 = xczp2;
    }

    public String getXczp2() {
        return xczp2;
    }

    public void setXczp3(String xczp3) {
        this.xczp3 = xczp3;
    }

    public String getXczp3() {
        return xczp3;
    }

    public void setLy(String ly) {
        this.ly = ly;
    }

    public String getLy() {
        return ly;
    }

    public void setSp(String sp) {
        this.sp = sp;
    }

    public String getSp() {
        return sp;
    }

    public void setBpzp(String bpzp) {
        this.bpzp = bpzp;
    }

    public String getBpzp() {
        return bpzp;
    }

    public void setQrr(String qrr) {
        this.qrr = qrr;
    }

    public String getQrr() {
        return qrr;
    }

    public void setSfyq(String sfyq) {
        this.sfyq = sfyq;
    }

    public String getSfyq() {
        return sfyq;
    }

    public void setTdyy(String tdyy) {
        this.tdyy = tdyy;
    }

    public String getTdyy() {
        return tdyy;
    }

    public void setGdzt(String gdzt) {
        this.gdzt = gdzt;
    }

    public String getGdzt() {
        return gdzt;
    }

    public void setXchd(String xchd) {
        this.xchd = xchd;
    }

    public String getXchd() {
        return xchd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.albh);
        dest.writeString(this.fylb);
        dest.writeString(this.fynr);
        dest.writeString(this.lxdh);
        dest.writeString(this.gls);
        dest.writeString(this.zd);
        dest.writeString(this.cbyid);
        dest.writeString(this.zhbh);
        dest.writeString(this.ywlx);
        dest.writeString(this.xccb);
        dest.writeString(this.dz);
        dest.writeString(this.xcqkbz);
        dest.writeString(this.xczp1);
        dest.writeString(this.xczp2);
        dest.writeString(this.xczp3);
        dest.writeString(this.ly);
        dest.writeString(this.sp);
        dest.writeString(this.qrr);
        dest.writeString(this.sfyq);
        dest.writeString(this.tdyy);
        dest.writeString(this.gdzt);
        dest.writeString(this.xchd);
    }

    protected QuestionWaterEntity(Parcel in) {
        this.albh = in.readString();
        this.fylb = in.readString();
        this.fynr = in.readString();
        this.lxdh = in.readString();
        this.zhbh = in.readString();
        this.gls = in.readString();
        this.zd = in.readString();
        this.cbyid = in.readString();
        this.ywlx = in.readString();
        this.xccb = in.readString();
        this.dz = in.readString();
        this.xcqkbz = in.readString();
        this.xczp1 = in.readString();
        this.xczp2 = in.readString();
        this.xczp3 = in.readString();
        this.ly = in.readString();
        this.sp = in.readString();
        this.qrr = in.readString();
        this.sfyq = in.readString();
        this.tdyy = in.readString();
        this.gdzt = in.readString();
        this.xchd = in.readString();
    }

    public static final Parcelable.Creator<QuestionWaterEntity> CREATOR = new Parcelable.Creator<QuestionWaterEntity>() {
        @Override
        public QuestionWaterEntity createFromParcel(Parcel source) {
            return new QuestionWaterEntity(source);
        }

        @Override
        public QuestionWaterEntity[] newArray(int size) {
            return new QuestionWaterEntity[size];
        }
    };
}
