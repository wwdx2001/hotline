package com.sh3h.hotline.entity;

import java.util.List;

public class JinQiCMEntity {
    /**
     * spId : 9276064106
     * mrList : [{"badgeNbr":"9276064106","readDttm":"2019-04-01 00:00:00","mrSource":"人工抄表","regReading":120,"lastMrNote":"备注","lastReadStatus":"正常"},{"badgeNbr":"9276064106","readDttm":"2019-03-01 00:00:00","mrSource":"人工抄表","regReading":100,"lastMrNote":"备注","lastReadStatus":"正常"}]
     */

    private String spId;
    private List<MrListBean> mrList;

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public List<MrListBean> getMrList() {
        return mrList;
    }

    public void setMrList(List<MrListBean> mrList) {
        this.mrList = mrList;
    }

    public static class MrListBean {
        /**
         * badgeNbr : 9276064106
         * readDttm : 2019-04-01 00:00:00
         * mrSource : 人工抄表
         * regReading : 120
         * lastMrNote : 备注
         * lastReadStatus : 正常
         */

        private String badgeNbr;
        private String readDttm;
        private String mrSource;
        private String regReading;
        private String lastMrNote;
        private String lastReadStatus;

        public String getBadgeNbr() {
            return badgeNbr;
        }

        public void setBadgeNbr(String badgeNbr) {
            this.badgeNbr = badgeNbr;
        }

        public String getReadDttm() {
            return readDttm;
        }

        public void setReadDttm(String readDttm) {
            this.readDttm = readDttm;
        }

        public String getMrSource() {
            return mrSource;
        }

        public void setMrSource(String mrSource) {
            this.mrSource = mrSource;
        }

        public String getRegReading() {
            return regReading;
        }

        public void setRegReading(String regReading) {
            this.regReading = regReading;
        }

        public String getLastMrNote() {
            return lastMrNote;
        }

        public void setLastMrNote(String lastMrNote) {
            this.lastMrNote = lastMrNote;
        }

        public String getLastReadStatus() {
            return lastReadStatus;
        }

        public void setLastReadStatus(String lastReadStatus) {
            this.lastReadStatus = lastReadStatus;
        }
    }
}
