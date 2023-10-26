package com.sh3h.dataprovider.data.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 客户总欠费
 */
public class CustomerTotalArrearsResult implements Parcelable {

    /**
     * acctId : 0270707539
     * cisMrgGrp : 42
     * entityName : 刘月玲
     * address1 : 平利路41弄14号605.6.7室
     * QryTime : 2019-04-03 14:10:58
     * cmTotalAmt : 17.2
     * chargeAmt : 0.01
     * cmMsgId : 00
     * CmMsgDesc :
     */

    private String acctId;
    private String cisMrgGrp;
    private String entityName;
    private String address1;
    private String QryTime;
    private String cmTotalAmt;
    private String chargeAmt;
    private String cmMsgId;
    private String CmMsgDesc;

    protected CustomerTotalArrearsResult(Parcel in) {
        acctId = in.readString();
        cisMrgGrp = in.readString();
        entityName = in.readString();
        address1 = in.readString();
        QryTime = in.readString();
        cmTotalAmt = in.readString();
        chargeAmt = in.readString();
        cmMsgId = in.readString();
        CmMsgDesc = in.readString();
    }

    public static final Creator<CustomerTotalArrearsResult> CREATOR = new Creator<CustomerTotalArrearsResult>() {
        @Override
        public CustomerTotalArrearsResult createFromParcel(Parcel in) {
            return new CustomerTotalArrearsResult(in);
        }

        @Override
        public CustomerTotalArrearsResult[] newArray(int size) {
            return new CustomerTotalArrearsResult[size];
        }
    };

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

    public String getQryTime() {
        return QryTime;
    }

    public void setQryTime(String QryTime) {
        this.QryTime = QryTime;
    }

    public String getCmTotalAmt() {
        return cmTotalAmt;
    }

    public void setCmTotalAmt(String cmTotalAmt) {
        this.cmTotalAmt = cmTotalAmt;
    }

    public String getChargeAmt() {
        return chargeAmt;
    }

    public void setChargeAmt(String chargeAmt) {
        this.chargeAmt = chargeAmt;
    }

    public String getCmMsgId() {
        return cmMsgId;
    }

    public void setCmMsgId(String cmMsgId) {
        this.cmMsgId = cmMsgId;
    }

    public String getCmMsgDesc() {
        return CmMsgDesc;
    }

    public void setCmMsgDesc(String CmMsgDesc) {
        this.CmMsgDesc = CmMsgDesc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(acctId);
        dest.writeString(cisMrgGrp);
        dest.writeString(entityName);
        dest.writeString(address1);
        dest.writeString(QryTime);
        dest.writeString(cmTotalAmt);
        dest.writeString(chargeAmt);
        dest.writeString(cmMsgId);
        dest.writeString(CmMsgDesc);
    }
}
