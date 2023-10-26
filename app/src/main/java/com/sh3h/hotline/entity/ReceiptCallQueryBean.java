package com.sh3h.hotline.entity;

public class ReceiptCallQueryBean {

    private String gls;// 管理所

    private String zd;// 站点

    private String zhbh;// 账户编号

    private String cbh;// 册本号

    private String qfje;// 欠费金额

    private String khmc;// 客户名称

    private String ysdz;// 用水地址

    private String ssdm;// 涉水代码

    private String khlx;// 涉水代码

    private boolean isChecked;

    public String getGls() {
        return gls;
    }

    public void setGls(String gls) {
        this.gls = gls;
    }

    public String getZd() {
        return zd;
    }

    public void setZd(String zd) {
        this.zd = zd;
    }

    public String getZhbh() {
        return zhbh;
    }

    public void setZhbh(String zhbh) {
        this.zhbh = zhbh;
    }

    public String getCbh() {
        return cbh;
    }

    public void setCbh(String cbh) {
        this.cbh = cbh;
    }

    public String getQfje() {
        return qfje;
    }

    public void setQfje(String qfje) {
        this.qfje = qfje;
    }

    public String getKhmc() {
        return khmc;
    }

    public void setKhmc(String khmc) {
        this.khmc = khmc;
    }

    public String getYsdz() {
        return ysdz;
    }

    public void setYsdz(String ysdz) {
        this.ysdz = ysdz;
    }

    public String getSsdm() {
        return ssdm;
    }

    public void setSsdm(String ssdm) {
        this.ssdm = ssdm;
    }

    public String getKhlx() {
        return khlx;
    }

    public void setKhlx(String khlx) {
        this.khlx = khlx;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
