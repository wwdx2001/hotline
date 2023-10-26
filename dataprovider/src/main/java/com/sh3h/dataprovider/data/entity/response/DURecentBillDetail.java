package com.sh3h.dataprovider.data.entity.response;

/**
 * 近期开账详情实体
 * Created by dengzhimin on 2016/10/9.
 */

public class DURecentBillDetail {

    private String waterCompany;//自来水公司
    private String accountNo;//开账编号
    private String station;//站点
    private String bookId;//册本
    private String name;//用户名
    private String accountType;//账单类型
    private String address;//地址
    private String account;//账号
    private String type;//用户类别
    private String reader;//抄表员
    private String bank;//开户银行
    private String number;//户号
    private String waterPrice;//用水价
    private String discountRate;//折扣率
    private String drainingPrice;//排水价
    private String drainingRate;//排水率
    private String adjustingPriceCode;//调价号
    private String partCode;//块号
    private String readTimes;//抄次
    private String readCode;//抄次代码
    private long readTime;//抄表日期
    private String readState;//抄表状态
    private String lastRead;//上次抄码
    private String lastWater;//上次水量
    private String lastBalance;//上次余额
    private String currentRead;//本次抄码
    private String readWater;//用水量
    private String suppliedCount;//补点数
    private String pi1Money;//用水费
    private String pi2Money;//排水费
    private String receivedMoney;//应收金额
    private String lateFee;//滞纳金
    private String poundage;//手续费
    private String currentBalance;//本次余额
    private String realMoney;//实收金额
    private String readFlag;//抄表标志
    private String printCount;//打印数
    private String cancelAccountFlag;//销账标志
    private String cancelAccountNo;//销账编号
    private long cancelAccountTime;//销账日期
    private String cancelAccountPerson;//销账员
    private String transferAccount;//转账托收
    private String accumulativeCount;//累计计量
    private String internalReadWater;//用水量
    private String firstStepReadWater;//第一阶梯户年用水量
    private String firstStepWaterRange;//第一阶梯水量区间
    private String secondStepReadWater;//第二阶梯户年用水量
    private String secondStepWaterRange;//第二阶梯水量区间
    private String thirdStepReadWater;//第三阶梯户年用水量
    private String thirdStepWaterRange;//第三阶梯水量区间
    private String externalPi2Money;//基外排水费
    private String internalPi1Money;//基内用水费
    private String externalPi1Money;//基外用水费
    private String drainageBase;//排水基数
    private String readBase;//用水基数
    private String fireCost;//消防费
    private String chargeWay;//收费方式
    private String planWater;//计划用水
    private String priceCode;//简号
    private String range;//量程
    private String caliber;//口径
    private String waterClass;//表分类
    private String cardId;//表号
    private String cardBarCode;//条形码
    private String uploadFlag;//上报标志
    private String downloadFlag;//下送标志
    private String extend;

    public DURecentBillDetail(String waterCompany, String accountNo, String station, String bookId, String name, String accountType, String address, String account,
                              String type, String reader, String bank, String number, String waterPrice, String discountRate, String drainingPrice,
                              String drainingRate, String adjustingPriceCode, String partCode, String readTimes, String readCode, long readTime, String readState,
                              String lastRead, String lastWater, String lastBalance, String currentRead, String readWater, String suppliedCount, String pi1Money,
                              String pi2Money, String receivedMoney, String lateFee, String poundage, String currentBalance, String realMoney, String readFlag,
                              String printCount, String cancelAccountFlag, String cancelAccountNo, long cancelAccountTime, String cancelAccountPerson,
                              String transferAccount, String accumulativeCount, String internalReadWater, String firstStepReadWater, String firstStepWaterRange,
                              String secondStepReadWater, String secondStepWaterRange, String thirdStepReadWater, String thirdStepWaterRange, String externalPi2Money,
                              String internalPi1Money, String externalPi1Money, String drainageBase, String readBase, String fireCost, String chargeWay,
                              String planWater, String priceCode, String range, String caliber, String waterClass, String cardId,String cardBarCode,String uploadFlag,
                              String downloadFlag, String extend) {
        this.waterCompany = waterCompany;
        this.accountNo = accountNo;
        this.station = station;
        this.bookId = bookId;
        this.name = name;
        this.accountType = accountType;
        this.address = address;
        this.account = account;
        this.type = type;
        this.reader = reader;
        this.bank = bank;
        this.number = number;
        this.waterPrice = waterPrice;
        this.discountRate = discountRate;
        this.drainingPrice = drainingPrice;
        this.drainingRate = drainingRate;
        this.adjustingPriceCode = adjustingPriceCode;
        this.partCode = partCode;
        this.readTimes = readTimes;
        this.readCode = readCode;
        this.readTime = readTime;
        this.readState = readState;
        this.lastRead = lastRead;
        this.lastWater = lastWater;
        this.lastBalance = lastBalance;
        this.currentRead = currentRead;
        this.readWater = readWater;
        this.suppliedCount = suppliedCount;
        this.pi1Money = pi1Money;
        this.pi2Money = pi2Money;
        this.receivedMoney = receivedMoney;
        this.lateFee = lateFee;
        this.poundage = poundage;
        this.currentBalance = currentBalance;
        this.realMoney = realMoney;
        this.readFlag = readFlag;
        this.printCount = printCount;
        this.cancelAccountFlag = cancelAccountFlag;
        this.cancelAccountNo = cancelAccountNo;
        this.cancelAccountTime = cancelAccountTime;
        this.cancelAccountPerson = cancelAccountPerson;
        this.transferAccount = transferAccount;
        this.accumulativeCount = accumulativeCount;
        this.internalReadWater = internalReadWater;
        this.firstStepReadWater = firstStepReadWater;
        this.firstStepWaterRange = firstStepWaterRange;
        this.secondStepReadWater = secondStepReadWater;
        this.secondStepWaterRange = secondStepWaterRange;
        this.thirdStepReadWater = thirdStepReadWater;
        this.thirdStepWaterRange = thirdStepWaterRange;
        this.externalPi2Money = externalPi2Money;
        this.internalPi1Money = internalPi1Money;
        this.externalPi1Money = externalPi1Money;
        this.drainageBase = drainageBase;
        this.readBase = readBase;
        this.fireCost = fireCost;
        this.chargeWay = chargeWay;
        this.planWater = planWater;
        this.priceCode = priceCode;
        this.range = range;
        this.caliber = caliber;
        this.waterClass = waterClass;
        this.cardId = cardId;
        this.cardBarCode=cardBarCode;
        this.uploadFlag = uploadFlag;
        this.downloadFlag = downloadFlag;
        this.extend = extend;
    }

    public String getWaterCompany() {
        return waterCompany;
    }

    public void setWaterCompany(String waterCompany) {
        this.waterCompany = waterCompany;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getWaterPrice() {
        return waterPrice;
    }

    public void setWaterPrice(String waterPrice) {
        this.waterPrice = waterPrice;
    }

    public String getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(String discountRate) {
        this.discountRate = discountRate;
    }

    public String getDrainingPrice() {
        return drainingPrice;
    }

    public void setDrainingPrice(String drainingPrice) {
        this.drainingPrice = drainingPrice;
    }

    public String getDrainingRate() {
        return drainingRate;
    }

    public void setDrainingRate(String drainingRate) {
        this.drainingRate = drainingRate;
    }

    public String getAdjustingPriceCode() {
        return adjustingPriceCode;
    }

    public void setAdjustingPriceCode(String adjustingPriceCode) {
        this.adjustingPriceCode = adjustingPriceCode;
    }

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public String getReadTimes() {
        return readTimes;
    }

    public void setReadTimes(String readTimes) {
        this.readTimes = readTimes;
    }

    public String getReadCode() {
        return readCode;
    }

    public void setReadCode(String readCode) {
        this.readCode = readCode;
    }

    public long getReadTime() {
        return readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }

    public String getReadState() {
        return readState;
    }

    public void setReadState(String readState) {
        this.readState = readState;
    }

    public String getLastRead() {
        return lastRead;
    }

    public void setLastRead(String lastRead) {
        this.lastRead = lastRead;
    }

    public String getLastWater() {
        return lastWater;
    }

    public void setLastWater(String lastWater) {
        this.lastWater = lastWater;
    }

    public String getLastBalance() {
        return lastBalance;
    }

    public void setLastBalance(String lastBalance) {
        this.lastBalance = lastBalance;
    }

    public String getCurrentRead() {
        return currentRead;
    }

    public void setCurrentRead(String currentRead) {
        this.currentRead = currentRead;
    }

    public String getReadWater() {
        return readWater;
    }

    public void setReadWater(String readWater) {
        this.readWater = readWater;
    }

    public String getSuppliedCount() {
        return suppliedCount;
    }

    public void setSuppliedCount(String suppliedCount) {
        this.suppliedCount = suppliedCount;
    }

    public String getPi1Money() {
        return pi1Money;
    }

    public void setPi1Money(String pi1Money) {
        this.pi1Money = pi1Money;
    }

    public String getPi2Money() {
        return pi2Money;
    }

    public void setPi2Money(String pi2Money) {
        this.pi2Money = pi2Money;
    }

    public String getReceivedMoney() {
        return receivedMoney;
    }

    public void setReceivedMoney(String receivedMoney) {
        this.receivedMoney = receivedMoney;
    }

    public String getLateFee() {
        return lateFee;
    }

    public void setLateFee(String lateFee) {
        this.lateFee = lateFee;
    }

    public String getPoundage() {
        return poundage;
    }

    public void setPoundage(String poundage) {
        this.poundage = poundage;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(String realMoney) {
        this.realMoney = realMoney;
    }

    public String getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(String readFlag) {
        this.readFlag = readFlag;
    }

    public String getPrintCount() {
        return printCount;
    }

    public void setPrintCount(String printCount) {
        this.printCount = printCount;
    }

    public String getCancelAccountFlag() {
        return cancelAccountFlag;
    }

    public void setCancelAccountFlag(String cancelAccountFlag) {
        this.cancelAccountFlag = cancelAccountFlag;
    }

    public String getCancelAccountNo() {
        return cancelAccountNo;
    }

    public void setCancelAccountNo(String cancelAccountNo) {
        this.cancelAccountNo = cancelAccountNo;
    }

    public long getCancelAccountTime() {
        return cancelAccountTime;
    }

    public void setCancelAccountTime(long cancelAccountTime) {
        this.cancelAccountTime = cancelAccountTime;
    }

    public String getCancelAccountPerson() {
        return cancelAccountPerson;
    }

    public void setCancelAccountPerson(String cancelAccountPerson) {
        this.cancelAccountPerson = cancelAccountPerson;
    }

    public String getTransferAccount() {
        return transferAccount;
    }

    public void setTransferAccount(String transferAccount) {
        this.transferAccount = transferAccount;
    }

    public String getAccumulativeCount() {
        return accumulativeCount;
    }

    public void setAccumulativeCount(String accumulativeCount) {
        this.accumulativeCount = accumulativeCount;
    }

    public String getInternalReadWater() {
        return internalReadWater;
    }

    public void setInternalReadWater(String internalReadWater) {
        this.internalReadWater = internalReadWater;
    }

    public String getFirstStepReadWater() {
        return firstStepReadWater;
    }

    public void setFirstStepReadWater(String firstStepReadWater) {
        this.firstStepReadWater = firstStepReadWater;
    }

    public String getFirstStepWaterRange() {
        return firstStepWaterRange;
    }

    public void setFirstStepWaterRange(String firstStepWaterRange) {
        this.firstStepWaterRange = firstStepWaterRange;
    }

    public String getSecondStepReadWater() {
        return secondStepReadWater;
    }

    public void setSecondStepReadWater(String secondStepReadWater) {
        this.secondStepReadWater = secondStepReadWater;
    }

    public String getSecondStepWaterRange() {
        return secondStepWaterRange;
    }

    public void setSecondStepWaterRange(String secondStepWaterRange) {
        this.secondStepWaterRange = secondStepWaterRange;
    }

    public String getThirdStepReadWater() {
        return thirdStepReadWater;
    }

    public void setThirdStepReadWater(String thirdStepReadWater) {
        this.thirdStepReadWater = thirdStepReadWater;
    }

    public String getThirdStepWaterRange() {
        return thirdStepWaterRange;
    }

    public void setThirdStepWaterRange(String thirdStepWaterRange) {
        this.thirdStepWaterRange = thirdStepWaterRange;
    }

    public String getExternalPi2Money() {
        return externalPi2Money;
    }

    public void setExternalPi2Money(String externalPi2Money) {
        this.externalPi2Money = externalPi2Money;
    }

    public String getInternalPi1Money() {
        return internalPi1Money;
    }

    public void setInternalPi1Money(String internalPi1Money) {
        this.internalPi1Money = internalPi1Money;
    }

    public String getExternalPi1Money() {
        return externalPi1Money;
    }

    public void setExternalPi1Money(String externalPi1Money) {
        this.externalPi1Money = externalPi1Money;
    }

    public String getDrainageBase() {
        return drainageBase;
    }

    public void setDrainageBase(String drainageBase) {
        this.drainageBase = drainageBase;
    }

    public String getReadBase() {
        return readBase;
    }

    public void setReadBase(String readBase) {
        this.readBase = readBase;
    }

    public String getFireCost() {
        return fireCost;
    }

    public void setFireCost(String fireCost) {
        this.fireCost = fireCost;
    }

    public String getChargeWay() {
        return chargeWay;
    }

    public void setChargeWay(String chargeWay) {
        this.chargeWay = chargeWay;
    }

    public String getPlanWater() {
        return planWater;
    }

    public void setPlanWater(String planWater) {
        this.planWater = planWater;
    }

    public String getPriceCode() {
        return priceCode;
    }

    public void setPriceCode(String priceCode) {
        this.priceCode = priceCode;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getCaliber() {
        return caliber;
    }

    public void setCaliber(String caliber) {
        this.caliber = caliber;
    }

    public String getWaterClass() {
        return waterClass;
    }

    public void setWaterClass(String waterClass) {
        this.waterClass = waterClass;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getUploadFlag() {
        return uploadFlag;
    }

    public void setUploadFlag(String uploadFlag) {
        this.uploadFlag = uploadFlag;
    }

    public String getDownloadFlag() {
        return downloadFlag;
    }

    public void setDownloadFlag(String downloadFlag) {
        this.downloadFlag = downloadFlag;
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
}
