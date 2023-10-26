package com.sh3h.dataprovider.data.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 账单基本信息
 * Created by dengzhimin on 2016/10/9.
 */

public class DUBillBaseInfo implements Parcelable{

    private String waterCompany;//自来水公司
    private String officeId;//办事处码
    private String bookId;//册本
    private String accountType;//账单类型
    private String address;//地址
    private String account;//账号
    private String name;//用户名
    private String creditClass;//信用等级
    private String type;//用户类别
    private String reader;//抄表员
    private String bank;//开户账号
    private String waterPosition;//表位
    private float waterPrice;//用水价
    private float discountRate;//折扣率
    private float drainingPrice;//排水价
    private float drainingRate;//排水率
    private float drainingStepPrice;//排水费阶梯价
    private int isStep;//是否上阶梯
    private long installedTime;//装表日期
    private long switchedTime;//换表日期
    private long fixedTime;//修改日期
    private String scheduledWater;//计划用水
    private float firePrice;//消防费
    private String caliber;//口径
    private String cardId;//销根号
    private String cardBarCode;//表号
    private String priceCode;//简号
    private int range;//量程
    private String chargedWay;//收费方式
    private String waterClass;//表分类
    private String wholeCardId;//总表号
    private int adjustingPriceCode;//调价号
    private String partCode;//块号
    private String personProperty;//用户性质
    private String currentReading;//当前底码
    private String extend;

    public DUBillBaseInfo(String waterCompany, String officeId, String bookId, String accountType, String address, String account, String name, String creditClass,
                          String type, String reader, String bank, String waterPosition, float waterPrice, float discountRate, float drainingPrice,
                          float drainingRate, float drainingStepPrice, int isStep, long installedTime, long switchedTime, long fixedTime, String scheduledWater,
                          float firePrice, String caliber, String cardId,String cardBarCode,String priceCode, int range, String chargedWay, String waterClass, String wholeCardId,
                          int adjustingPriceCode, String partCode, String personProperty, String currentReading, String extend) {
        this.waterCompany = waterCompany;
        this.officeId = officeId;
        this.bookId = bookId;
        this.accountType = accountType;
        this.address = address;
        this.account = account;
        this.name = name;
        this.creditClass = creditClass;
        this.type = type;
        this.reader = reader;
        this.bank = bank;
        this.waterPosition = waterPosition;
        this.waterPrice = waterPrice;
        this.discountRate = discountRate;
        this.drainingPrice = drainingPrice;
        this.drainingRate = drainingRate;
        this.drainingStepPrice = drainingStepPrice;
        this.isStep = isStep;
        this.installedTime = installedTime;
        this.switchedTime = switchedTime;
        this.fixedTime = fixedTime;
        this.scheduledWater = scheduledWater;
        this.firePrice = firePrice;
        this.caliber = caliber;
        this.cardId = cardId;
        this.cardBarCode=cardBarCode;//水表条形码
        this.priceCode = priceCode;
        this.range = range;
        this.chargedWay = chargedWay;
        this.waterClass = waterClass;
        this.wholeCardId = wholeCardId;
        this.adjustingPriceCode = adjustingPriceCode;
        this.partCode = partCode;
        this.personProperty = personProperty;
        this.currentReading = currentReading;
        this.extend = extend;
    }

    protected DUBillBaseInfo(Parcel in) {
        waterCompany = in.readString();
        officeId = in.readString();
        bookId = in.readString();
        accountType = in.readString();
        address = in.readString();
        account = in.readString();
        name = in.readString();
        creditClass = in.readString();
        type = in.readString();
        reader = in.readString();
        bank = in.readString();
        waterPosition = in.readString();
        waterPrice = in.readFloat();
        discountRate = in.readFloat();
        drainingPrice = in.readFloat();
        drainingRate = in.readFloat();
        drainingStepPrice = in.readFloat();
        isStep = in.readInt();
        installedTime = in.readLong();
        switchedTime = in.readLong();
        fixedTime = in.readLong();
        scheduledWater = in.readString();
        firePrice = in.readFloat();
        caliber = in.readString();
        cardId = in.readString();
        cardBarCode=in.readString();
        priceCode = in.readString();
        range = in.readInt();
        chargedWay = in.readString();
        waterClass = in.readString();
        wholeCardId = in.readString();
        adjustingPriceCode = in.readInt();
        partCode = in.readString();
        personProperty = in.readString();
        currentReading = in.readString();
        extend = in.readString();
    }

    public static final Creator<DUBillBaseInfo> CREATOR = new Creator<DUBillBaseInfo>() {
        @Override
        public DUBillBaseInfo createFromParcel(Parcel in) {
            return new DUBillBaseInfo(in);
        }

        @Override
        public DUBillBaseInfo[] newArray(int size) {
            return new DUBillBaseInfo[size];
        }
    };

    public String getWaterCompany() {
        return waterCompany;
    }

    public void setWaterCompany(String waterCompany) {
        this.waterCompany = waterCompany;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreditClass() {
        return creditClass;
    }

    public void setCreditClass(String creditClass) {
        this.creditClass = creditClass;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReader() {
        return reader;
    }

    public void setReader(String reader) {
        this.reader = reader;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getWaterPosition() {
        return waterPosition;
    }

    public void setWaterPosition(String waterPosition) {
        this.waterPosition = waterPosition;
    }

    public float getWaterPrice() {
        return waterPrice;
    }

    public void setWaterPrice(float waterPrice) {
        this.waterPrice = waterPrice;
    }

    public float getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(float discountRate) {
        this.discountRate = discountRate;
    }

    public float getDrainingPrice() {
        return drainingPrice;
    }

    public void setDrainingPrice(float drainingPrice) {
        this.drainingPrice = drainingPrice;
    }

    public float getDrainingRate() {
        return drainingRate;
    }

    public void setDrainingRate(float drainingRate) {
        this.drainingRate = drainingRate;
    }

    public float getDrainingStepPrice() {
        return drainingStepPrice;
    }

    public void setDrainingStepPrice(float drainingStepPrice) {
        this.drainingStepPrice = drainingStepPrice;
    }

    public int getIsStep() {
        return isStep;
    }

    public void setIsStep(int isStep) {
        this.isStep = isStep;
    }

    public long getInstalledTime() {
        return installedTime;
    }

    public void setInstalledTime(long installedTime) {
        this.installedTime = installedTime;
    }

    public long getSwitchedTime() {
        return switchedTime;
    }

    public void setSwitchedTime(long switchedTime) {
        this.switchedTime = switchedTime;
    }

    public long getFixedTime() {
        return fixedTime;
    }

    public void setFixedTime(long fixedTime) {
        this.fixedTime = fixedTime;
    }

    public String getScheduledWater() {
        return scheduledWater;
    }

    public void setScheduledWater(String scheduledWater) {
        this.scheduledWater = scheduledWater;
    }

    public float getFirePrice() {
        return firePrice;
    }

    public void setFirePrice(float firePrice) {
        this.firePrice = firePrice;
    }

    public String getCaliber() {
        return caliber;
    }

    public void setCaliber(String caliber) {
        this.caliber = caliber;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getPriceCode() {
        return priceCode;
    }

    public void setPriceCode(String priceCode) {
        this.priceCode = priceCode;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public String getChargedWay() {
        return chargedWay;
    }

    public void setChargedWay(String chargedWay) {
        this.chargedWay = chargedWay;
    }

    public String getWaterClass() {
        return waterClass;
    }

    public void setWaterClass(String waterClass) {
        this.waterClass = waterClass;
    }

    public String getWholeCardId() {
        return wholeCardId;
    }

    public void setWholeCardId(String wholeCardId) {
        this.wholeCardId = wholeCardId;
    }

    public int getAdjustingPriceCode() {
        return adjustingPriceCode;
    }

    public void setAdjustingPriceCode(int adjustingPriceCode) {
        this.adjustingPriceCode = adjustingPriceCode;
    }

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public String getPersonProperty() {
        return personProperty;
    }

    public void setPersonProperty(String personProperty) {
        this.personProperty = personProperty;
    }

    public String getCurrentReading() {
        return currentReading;
    }

    public void setCurrentReading(String currentReading) {
        this.currentReading = currentReading;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getCardBarCode() {
        return cardBarCode;
    }

    public void setCardBarCode(String cardBarCode) {
        this.cardBarCode = cardBarCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(waterCompany);
        dest.writeString(officeId);
        dest.writeString(bookId);
        dest.writeString(accountType);
        dest.writeString(address);
        dest.writeString(account);
        dest.writeString(name);
        dest.writeString(creditClass);
        dest.writeString(type);
        dest.writeString(reader);
        dest.writeString(bank);
        dest.writeString(waterPosition);
        dest.writeFloat(waterPrice);
        dest.writeFloat(discountRate);
        dest.writeFloat(drainingPrice);
        dest.writeFloat(drainingRate);
        dest.writeFloat(drainingStepPrice);
        dest.writeInt(isStep);
        dest.writeLong(installedTime);
        dest.writeLong(switchedTime);
        dest.writeLong(fixedTime);
        dest.writeString(scheduledWater);
        dest.writeFloat(firePrice);
        dest.writeString(caliber);
        dest.writeString(cardId);
        dest.writeString(cardBarCode);
        dest.writeString(priceCode);
        dest.writeInt(range);
        dest.writeString(chargedWay);
        dest.writeString(waterClass);
        dest.writeString(wholeCardId);
        dest.writeInt(adjustingPriceCode);
        dest.writeString(partCode);
        dest.writeString(personProperty);
        dest.writeString(currentReading);
        dest.writeString(extend);
    }
}
