package com.sh3h.dataprovider.data.entity.response;

/**
 * 欠费信息详情实体
 * Created by dengzhimin on 2016/10/10.
 */

public class DUArrearsDetail extends DURecentBillDetail {

    public DUArrearsDetail(String waterCompany, String accountNo, String station, String bookId, String name, String accountType, String address, String account,
                           String type, String reader, String bank, String number, String waterPrice, String discountRate, String drainingPrice, String drainingRate,
                           String adjustingPriceCode, String partCode, String readTimes, String readCode, long readTime, String readState, String lastRead,
                           String lastWater, String lastBalance, String currentRead, String readWater, String suppliedCount, String pi1Money, String pi2Money,
                           String receivedMoney, String lateFee, String poundage, String currentBalance, String realMoney, String readFlag, String printCount,
                           String cancelAccountFlag, String cancelAccountNo, long cancelAccountTime, String cancelAccountPerson, String transferAccount,
                           String accumulativeCount, String internalReadWater, String firstStepReadWater, String firstStepWaterRange, String secondStepReadWater,
                           String secondStepWaterRange, String thirdStepReadWater, String thirdStepWaterRange, String externalPi2Money, String internalPi1Money,
                           String externalPi1Money, String drainageBase, String readBase, String fireCost, String chargeWay, String planWater, String priceCode,
                           String range, String caliber, String waterClass, String cardId,String cardBarCode,String extend, String downloadFlag, String uploadFlag) {
        super(waterCompany, accountNo, station, bookId, name, accountType, address, account, type, reader, bank, number, waterPrice, discountRate, drainingPrice,
                drainingRate, adjustingPriceCode, partCode, readTimes, readCode, readTime, readState, lastRead, lastWater, lastBalance, currentRead, readWater,
                suppliedCount, pi1Money, pi2Money, receivedMoney, lateFee, poundage, currentBalance, realMoney, readFlag, printCount, cancelAccountFlag,
                cancelAccountNo, cancelAccountTime, cancelAccountPerson, transferAccount, accumulativeCount, internalReadWater, firstStepReadWater,
                firstStepWaterRange, secondStepReadWater, secondStepWaterRange, thirdStepReadWater, thirdStepWaterRange, externalPi2Money, internalPi1Money,
                externalPi1Money, drainageBase, readBase, fireCost, chargeWay, planWater, priceCode, range, caliber, waterClass, cardId, cardBarCode,uploadFlag, downloadFlag,
                extend);
    }

}
