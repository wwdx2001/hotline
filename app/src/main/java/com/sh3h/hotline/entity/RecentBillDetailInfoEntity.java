package com.sh3h.hotline.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecentBillDetailInfoEntity {

    /**
     * acctId : 0316111621
     * cisMrgGrp : 46
     * entityName : dfgds
     * address1 : ssdrftgyhg
     * billList : [{"ftType":"0","ftTypeDesc":"wwertggg","siBillid":"0","BillDate":"2019-04-27 00:00:00","saTypeDesc":"sdfgh","cmAmt":0.01,"payFlg":"0"},{"ftType":"0","ftTypeDesc":"wwertggg","siBillid":"0","BillDate":"2019-04-27 00:00:00","saTypeDesc":"sdfgh","cmAmt":0.01,"payFlg":"0"}]
     */

    private String acctId;
    private String cisMrgGrp;
    private String entityName;
    private String address1;
    private List<BillListBean> billList;

    public static List<RecentBillDetailInfoEntity> arrayRecentBillDetailInfoEntityFromData(String str) {

        Type listType = new TypeToken<ArrayList<RecentBillDetailInfoEntity>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<RecentBillDetailInfoEntity> arrayRecentBillDetailInfoEntityFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<RecentBillDetailInfoEntity>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getCisMrgGrp() {
        return cisMrgGrp;
    }

    public void setCisMrgGrp(String cisMrgGrp) {
        this.cisMrgGrp = cisMrgGrp;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public List<BillListBean> getBillList() {
        return billList;
    }

    public void setBillList(List<BillListBean> billList) {
        this.billList = billList;
    }

    public static class BillListBean {
        /**
         * ftType : 0
         * ftTypeDesc : wwertggg
         * siBillid : 0
         * BillDate : 2019-04-27 00:00:00
         * saTypeDesc : sdfgh
         * cmAmt : 0.01
         * payFlg : 0
         */

        private String ftType;
        private String ftTypeDesc;
        private String siBillid;
        private String BillDate;
        private String saTypeDesc;
        private double cmAmt;
        private String payFlg;

        private String billVol;

        public String getBillVol() {
            return billVol;
        }

        public void setBillVol(String billVol) {
            this.billVol = billVol;
        }

        public static List<BillListBean> arrayBillListBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<BillListBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static List<BillListBean> arrayBillListBeanFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);
                Type listType = new TypeToken<ArrayList<BillListBean>>() {
                }.getType();

                return new Gson().fromJson(jsonObject.getString(str), listType);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new ArrayList();


        }

        public String getFtType() {
            return ftType;
        }

        public void setFtType(String ftType) {
            this.ftType = ftType;
        }

        public String getFtTypeDesc() {
            return ftTypeDesc;
        }

        public void setFtTypeDesc(String ftTypeDesc) {
            this.ftTypeDesc = ftTypeDesc;
        }

        public String getSiBillid() {
            return siBillid;
        }

        public void setSiBillid(String siBillid) {
            this.siBillid = siBillid;
        }

        public String getBillDate() {
            return BillDate;
        }

        public void setBillDate(String BillDate) {
            this.BillDate = BillDate;
        }

        public String getSaTypeDesc() {
            return saTypeDesc;
        }

        public void setSaTypeDesc(String saTypeDesc) {
            this.saTypeDesc = saTypeDesc;
        }

        public double getCmAmt() {
            return cmAmt;
        }

        public void setCmAmt(double cmAmt) {
            this.cmAmt = cmAmt;
        }

        public String getPayFlg() {
            return payFlg;
        }

        public void setPayFlg(String payFlg) {
            this.payFlg = payFlg;
        }
    }
}
