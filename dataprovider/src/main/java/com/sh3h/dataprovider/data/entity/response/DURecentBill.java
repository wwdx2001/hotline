package com.sh3h.dataprovider.data.entity.response;

/**
 * 近期开账实体
 * Created by dengzhimin on 2016/10/9.
 */

public class DURecentBill {

    private int readYear;//年

    private int readMonth;//月

    private String readCardState;//抄表状态

    private int readTimes;//抄次

    private long readTime;//抄码时间

    private int reading;//本次抄码

    private int readWater;//用水量

    private float pi1Money;//用水费

    private float pi2Money;//排水费

    private float accCheckMoney;//开账金额

    private int feeId;//费用id

    private String extend;

    public DURecentBill(int readYear, int readMonth, String readCardState, int readTimes,
                        long readTime, int reading, int readWater, float pi1Money, float pi2Money,
                        float accCheckMoney, int feeId, String extend) {
        this.readYear = readYear;
        this.readMonth = readMonth;
        this.readCardState = readCardState;
        this.readTimes = readTimes;
        this.readTime = readTime;
        this.reading = reading;
        this.readWater = readWater;
        this.pi1Money = pi1Money;
        this.pi2Money = pi2Money;
        this.accCheckMoney = accCheckMoney;
        this.feeId = feeId;
        this.extend = extend;
    }

    public int getReadYear() {
        return readYear;
    }

    public void setReadYear(int readYear) {
        this.readYear = readYear;
    }

    public int getReadMonth() {
        return readMonth;
    }

    public void setReadMonth(int readMonth) {
        this.readMonth = readMonth;
    }

    public int getReadTimes() {
        return readTimes;
    }

    public void setReadTimes(int readTimes) {
        this.readTimes = readTimes;
    }

    public int getReading() {
        return reading;
    }

    public void setReading(int reading) {
        this.reading = reading;
    }

    public int getReadWater() {
        return readWater;
    }

    public void setReadWater(int readWater) {
        this.readWater = readWater;
    }

    public float getPi1Money() {
        return pi1Money;
    }

    public void setPi1Money(float pi1Money) {
        this.pi1Money = pi1Money;
    }

    public float getPi2Money() {
        return pi2Money;
    }

    public void setPi2Money(float pi2Money) {
        this.pi2Money = pi2Money;
    }

    public float getAccCheckMoney() {
        return accCheckMoney;
    }

    public void setAccCheckMoney(float accCheckMoney) {
        this.accCheckMoney = accCheckMoney;
    }

    public int getFeeId() {
        return feeId;
    }

    public void setFeeId(int feeId) {
        this.feeId = feeId;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public long getReadTime() {
        return readTime;
    }

    public String getReadCardState() {
        return readCardState;
    }

    public void setReadCardState(String readCardState) {
        this.readCardState = readCardState;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }
}
