package com.sh3h.dataprovider.data.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 客户基本信息
 */
public class CustomerInfoFindResult implements Parcelable {

    /**
     * acctId : 0270707539
     * cardId : 270707539
     * perName : 刘月玲
     * custClCd : 104H
     * custClass : 工房住宅按套室(按户)装表（合用户）
     * creditClass :
     * riskClass :
     * creditValue :
     * phone25 : 1234567890123
     * address1 : 平利路41弄14号605.6.7室
     * address2 : 平利路41弄14号605.6.7室
     * address3 : 平利路41弄14号605.6.7室
     * address4 : 平利路41弄14号605.6.7室
     * acctFlg : 居民
     * drkInfo : 5
     * sjfFlg : 0
     * totAmt : 0
     * waterPrice : 0.0
     * wastePrice : 0.0
     * acctMGrpDescr : 大场站
     * bankFlg : 0
     * SpList : [{"spId":"9270707539","mtrUseType":"7","mtrId":"270707539","badgeNbr":"18715049081000","mtrRfid":"1234567890123222","mtrDN":"15","mtrRange":"","mtrLocDesc":"龙头表","mtrLocDetails":"龙头","mtrInstallDt":"2015-10-14 00:00:00","zfbFlg":"00","mrRteCd":"27i313","cbyDesc":"李四"}]
     */

    private String acctId;
    private String cardId;
    private String perName;
    private String custClCd;
    private String custClass;
    private String creditClass;
    private String riskClass;
    private String creditValue;
    private String phone25;
    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private String acctFlg;
    private String drkInfo;
    private String sjfFlg;
    private double totAmt;
    private double waterPrice;
    private String wastePrice;
    private String acctMGrpDescr;
    private String bankFlg;
    private List<SpListBean> SpList;

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getPerName() {
        return perName;
    }

    public void setPerName(String perName) {
        this.perName = perName;
    }

    public String getCustClCd() {
        return custClCd;
    }

    public void setCustClCd(String custClCd) {
        this.custClCd = custClCd;
    }

    public String getCustClass() {
        return custClass;
    }

    public void setCustClass(String custClass) {
        this.custClass = custClass;
    }

    public String getCreditClass() {
        return creditClass;
    }

    public void setCreditClass(String creditClass) {
        this.creditClass = creditClass;
    }

    public String getRiskClass() {
        return riskClass;
    }

    public void setRiskClass(String riskClass) {
        this.riskClass = riskClass;
    }

    public String getCreditValue() {
        return creditValue;
    }

    public void setCreditValue(String creditValue) {
        this.creditValue = creditValue;
    }

    public String getPhone25() {
        return phone25;
    }

    public void setPhone25(String phone25) {
        this.phone25 = phone25;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress4() {
        return address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public String getAcctFlg() {
        return acctFlg;
    }

    public void setAcctFlg(String acctFlg) {
        this.acctFlg = acctFlg;
    }

    public String getDrkInfo() {
        return drkInfo;
    }

    public void setDrkInfo(String drkInfo) {
        this.drkInfo = drkInfo;
    }

    public String getSjfFlg() {
        return sjfFlg;
    }

    public void setSjfFlg(String sjfFlg) {
        this.sjfFlg = sjfFlg;
    }

    public double getTotAmt() {
        return totAmt;
    }

    public void setTotAmt(double totAmt) {
        this.totAmt = totAmt;
    }

    public double getWaterPrice() {
        return waterPrice;
    }

    public void setWaterPrice(double waterPrice) {
        this.waterPrice = waterPrice;
    }

    public String getWastePrice() {
        return wastePrice;
    }

    public void setWastePrice(String wastePrice) {
        this.wastePrice = wastePrice;
    }

    public String getAcctMGrpDescr() {
        return acctMGrpDescr;
    }

    public void setAcctMGrpDescr(String acctMGrpDescr) {
        this.acctMGrpDescr = acctMGrpDescr;
    }

    public String getBankFlg() {
        return bankFlg;
    }

    public void setBankFlg(String bankFlg) {
        this.bankFlg = bankFlg;
    }

    public List<SpListBean> getSpList() {
        return SpList;
    }

    public void setSpList(List<SpListBean> SpList) {
        this.SpList = SpList;
    }

    public static class SpListBean implements Parcelable {
        /**
         * spId : 9270707539
         * mtrUseType : 7
         * mtrId : 270707539
         * badgeNbr : 18715049081000
         * mtrRfid : 1234567890123222
         * mtrDN : 15
         * mtrRange :
         * mtrLocDesc : 龙头表
         * mtrLocDetails : 龙头
         * mtrInstallDt : 2015-10-14 00:00:00
         * zfbFlg : 00
         * mrRteCd : 27i313
         * cbyDesc : 李四
         */

        private String spId;
        private String mtrUseType;
        private String mtrId;
        private String badgeNbr;
        private String mtrRfid;
        private String mtrDN;
        private String mtrRange;
        private String mtrLocDesc;
        private String mtrLocDetails;
        private String mtrInstallDt;
        private String zfbFlg;
        private String mrRteCd;
        private String cbyDesc;
        private String newMtrType;

        public String getNewMtrType() {
            return newMtrType;
        }

        public void setNewMtrType(String newMtrType) {
            this.newMtrType = newMtrType;
        }

        protected SpListBean(Parcel in) {
            spId = in.readString();
            mtrUseType = in.readString();
            mtrId = in.readString();
            badgeNbr = in.readString();
            mtrRfid = in.readString();
            mtrDN = in.readString();
            mtrRange = in.readString();
            mtrLocDesc = in.readString();
            mtrLocDetails = in.readString();
            mtrInstallDt = in.readString();
            zfbFlg = in.readString();
            mrRteCd = in.readString();
            cbyDesc = in.readString();
        }

        public static final Creator<SpListBean> CREATOR = new Creator<SpListBean>() {
            @Override
            public SpListBean createFromParcel(Parcel in) {
                return new SpListBean(in);
            }

            @Override
            public SpListBean[] newArray(int size) {
                return new SpListBean[size];
            }
        };

        public String getSpId() {
            return spId;
        }

        public void setSpId(String spId) {
            this.spId = spId;
        }

        public String getMtrUseType() {
            return mtrUseType;
        }

        public void setMtrUseType(String mtrUseType) {
            this.mtrUseType = mtrUseType;
        }

        public String getMtrId() {
            return mtrId;
        }

        public void setMtrId(String mtrId) {
            this.mtrId = mtrId;
        }

        public String getBadgeNbr() {
            return badgeNbr;
        }

        public void setBadgeNbr(String badgeNbr) {
            this.badgeNbr = badgeNbr;
        }

        public String getMtrRfid() {
            return mtrRfid;
        }

        public void setMtrRfid(String mtrRfid) {
            this.mtrRfid = mtrRfid;
        }

        public String getMtrDN() {
            return mtrDN;
        }

        public void setMtrDN(String mtrDN) {
            this.mtrDN = mtrDN;
        }

        public String getMtrRange() {
            return mtrRange;
        }

        public void setMtrRange(String mtrRange) {
            this.mtrRange = mtrRange;
        }

        public String getMtrLocDesc() {
            return mtrLocDesc;
        }

        public void setMtrLocDesc(String mtrLocDesc) {
            this.mtrLocDesc = mtrLocDesc;
        }

        public String getMtrLocDetails() {
            return mtrLocDetails;
        }

        public void setMtrLocDetails(String mtrLocDetails) {
            this.mtrLocDetails = mtrLocDetails;
        }

        public String getMtrInstallDt() {
            return mtrInstallDt;
        }

        public void setMtrInstallDt(String mtrInstallDt) {
            this.mtrInstallDt = mtrInstallDt;
        }

        public String getZfbFlg() {
            return zfbFlg;
        }

        public void setZfbFlg(String zfbFlg) {
            this.zfbFlg = zfbFlg;
        }

        public String getMrRteCd() {
            return mrRteCd;
        }

        public void setMrRteCd(String mrRteCd) {
            this.mrRteCd = mrRteCd;
        }

        public String getCbyDesc() {
            return cbyDesc;
        }

        public void setCbyDesc(String cbyDesc) {
            this.cbyDesc = cbyDesc;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(spId);
            dest.writeString(mtrUseType);
            dest.writeString(mtrId);
            dest.writeString(badgeNbr);
            dest.writeString(mtrRfid);
            dest.writeString(mtrDN);
            dest.writeString(mtrRange);
            dest.writeString(mtrLocDesc);
            dest.writeString(mtrLocDetails);
            dest.writeString(mtrInstallDt);
            dest.writeString(zfbFlg);
            dest.writeString(mrRteCd);
            dest.writeString(cbyDesc);
        }
    }

    @Override
    public String toString() {
        return "CustomerInfoFindResult{" +
                "acctId='" + acctId + '\'' +
                ", cardId='" + cardId + '\'' +
                ", perName='" + perName + '\'' +
                ", custClCd='" + custClCd + '\'' +
                ", custClass='" + custClass + '\'' +
                ", creditClass='" + creditClass + '\'' +
                ", riskClass='" + riskClass + '\'' +
                ", creditValue='" + creditValue + '\'' +
                ", phone25='" + phone25 + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", address3='" + address3 + '\'' +
                ", address4='" + address4 + '\'' +
                ", acctFlg='" + acctFlg + '\'' +
                ", drkInfo=" + drkInfo +
                ", sjfFlg='" + sjfFlg + '\'' +
                ", totAmt=" + totAmt +
                ", waterPrice=" + waterPrice +
                ", wastePrice=" + wastePrice +
                ", acctMGrpDescr='" + acctMGrpDescr + '\'' +
                ", bankFlg='" + bankFlg + '\'' +
                ", SpList=" + SpList +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.acctId);
        dest.writeString(this.cardId);
        dest.writeString(this.perName);
        dest.writeString(this.custClCd);
        dest.writeString(this.custClass);
        dest.writeString(this.creditClass);
        dest.writeString(this.riskClass);
        dest.writeString(this.creditValue);
        dest.writeString(this.phone25);
        dest.writeString(this.address1);
        dest.writeString(this.address2);
        dest.writeString(this.address3);
        dest.writeString(this.address4);
        dest.writeString(this.acctFlg);
        dest.writeString(this.drkInfo);
        dest.writeString(this.sjfFlg);
        dest.writeDouble(this.totAmt);
        dest.writeDouble(this.waterPrice);
        dest.writeString(this.wastePrice);
        dest.writeString(this.acctMGrpDescr);
        dest.writeString(this.bankFlg);
        dest.writeTypedList(this.SpList);
    }

    public CustomerInfoFindResult() {
    }

    protected CustomerInfoFindResult(Parcel in) {
        this.acctId = in.readString();
        this.cardId = in.readString();
        this.perName = in.readString();
        this.custClCd = in.readString();
        this.custClass = in.readString();
        this.creditClass = in.readString();
        this.riskClass = in.readString();
        this.creditValue = in.readString();
        this.phone25 = in.readString();
        this.address1 = in.readString();
        this.address2 = in.readString();
        this.address3 = in.readString();
        this.address4 = in.readString();
        this.acctFlg = in.readString();
        this.drkInfo = in.readString();
        this.sjfFlg = in.readString();
        this.totAmt = in.readDouble();
        this.waterPrice = in.readDouble();
        this.wastePrice = in.readString();
        this.acctMGrpDescr = in.readString();
        this.bankFlg = in.readString();
        this.SpList = in.createTypedArrayList(SpListBean.CREATOR);
    }

    public static final Creator<CustomerInfoFindResult> CREATOR = new Creator<CustomerInfoFindResult>() {
        @Override
        public CustomerInfoFindResult createFromParcel(Parcel source) {
            return new CustomerInfoFindResult(source);
        }

        @Override
        public CustomerInfoFindResult[] newArray(int size) {
            return new CustomerInfoFindResult[size];
        }
    };
}
