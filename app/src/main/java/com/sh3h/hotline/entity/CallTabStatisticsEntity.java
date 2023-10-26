package com.sh3h.hotline.entity;

import java.io.Serializable;

public class CallTabStatisticsEntity implements Serializable {

    private String pch;
    private int gongdanshu;
    private int weiwancheng;
    private int yiwancheng;

    public String getPch() {
        return pch;
    }

    public void setPch(String pch) {
        this.pch = pch;
    }

    public void setGongdanshu(int gongdanshu) {
        this.gongdanshu = gongdanshu;
    }

    public int getGongdanshu() {
        return gongdanshu;
    }

    public void setWeiwancheng(int weiwancheng) {
        this.weiwancheng = weiwancheng;
    }

    public int getWeiwancheng() {
        return weiwancheng;
    }

    public void setYiwancheng(int yiwancheng) {
        this.yiwancheng = yiwancheng;
    }

    public int getYiwancheng() {
        return yiwancheng;
    }
}
