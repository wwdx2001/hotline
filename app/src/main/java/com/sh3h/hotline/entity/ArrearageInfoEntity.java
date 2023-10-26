package com.sh3h.hotline.entity;

public class ArrearageInfoEntity {


    /**
     * acctId : 320001111
     * accessGrp : 214242
     * cmTotalAmt : 231.12
     * cmLeft : 0.23
     */

    private String acctId;
    private String accessGrp;
    private String lastPaymentDate;
    private double cmTotalAmt;
    private double cmLeft;
    private double ashAmt;

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getAccessGrp() {
        return accessGrp;
    }

    public void setAccessGrp(String accessGrp) {
        this.accessGrp = accessGrp;
    }

    public double getCmTotalAmt() {
        return cmTotalAmt;
    }

    public void setCmTotalAmt(double cmTotalAmt) {
        this.cmTotalAmt = cmTotalAmt;
    }

    public double getCmLeft() {
        return cmLeft;
    }

    public void setCmLeft(double cmLeft) {
        this.cmLeft = cmLeft;
    }

    public String getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(String lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public double getAshAmt() {
        return ashAmt;
    }

    public void setAshAmt(double ashAmt) {
        this.ashAmt = ashAmt;
    }
}

