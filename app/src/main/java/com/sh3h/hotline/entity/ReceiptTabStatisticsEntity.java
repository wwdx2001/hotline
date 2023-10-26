package com.sh3h.hotline.entity;

public class ReceiptTabStatisticsEntity {

        private String pdlx;
        private String pdsj;
        private String pch;
        private int gongdanshu;
        private int weiwancheng;
        private int yiwancheng;

        public void setPdlx(String pdlx) {
            this.pdlx = pdlx;
        }
        public String getPdlx() {
            return pdlx;
        }

        public void setPdsj(String pdsj) {
            this.pdsj = pdsj;
        }
        public String getPdsj() {
            return pdsj;
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

    public String getPch() {
        return pch;
    }

    public void setPch(String pch) {
        this.pch = pch;
    }

    @Override
    public String toString() {
        return "ReceiptTabStatisticsEntity{" +
                "pdlx='" + pdlx + '\'' +
                ", pdsj='" + pdsj + '\'' +
                ", pch='" + pch + '\'' +
                ", gongdanshu=" + gongdanshu +
                ", weiwancheng=" + weiwancheng +
                ", yiwancheng=" + yiwancheng +
                '}';
    }
}
