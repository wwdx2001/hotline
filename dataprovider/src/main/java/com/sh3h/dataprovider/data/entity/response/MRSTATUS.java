package com.sh3h.dataprovider.data.entity.response;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/5/28 14:10
 */
@Entity
public class MRSTATUS {

    /**
     * ID : 1
     * Mr_Class_Id : 0
     * Mr_Desc : 正常
     * Mr_Status_Id : 1
     * Mr_Status_Desc : 正常
     * Mr_Agr_Id : 1
     * Mr_Agr_Desc : 正常算法
     * Mr_Onsite : 1
     * Mr_Onsite_Desc : 抄见
     * Mr_Delay :
     * Delay_Reason : null
     * High_Low_Id : null
     */

    private Long ID;
    private String Mr_Class_Id;
    private String Mr_Desc;
    private String Mr_Status_Id;
    private String Mr_Status_Desc;
    private String Mr_Agr_Id;
    private String Mr_Agr_Desc;
    private String Mr_Onsite;
    private String Mr_Onsite_Desc;
    private String Mr_Delay;
    private String Delay_Reason;
    private String High_Low_Id;

    @Generated(hash = 2037322787)
    public MRSTATUS(Long ID, String Mr_Class_Id, String Mr_Desc,
            String Mr_Status_Id, String Mr_Status_Desc, String Mr_Agr_Id,
            String Mr_Agr_Desc, String Mr_Onsite, String Mr_Onsite_Desc,
            String Mr_Delay, String Delay_Reason, String High_Low_Id) {
        this.ID = ID;
        this.Mr_Class_Id = Mr_Class_Id;
        this.Mr_Desc = Mr_Desc;
        this.Mr_Status_Id = Mr_Status_Id;
        this.Mr_Status_Desc = Mr_Status_Desc;
        this.Mr_Agr_Id = Mr_Agr_Id;
        this.Mr_Agr_Desc = Mr_Agr_Desc;
        this.Mr_Onsite = Mr_Onsite;
        this.Mr_Onsite_Desc = Mr_Onsite_Desc;
        this.Mr_Delay = Mr_Delay;
        this.Delay_Reason = Delay_Reason;
        this.High_Low_Id = High_Low_Id;
    }

    @Generated(hash = 368912276)
    public MRSTATUS() {
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getMr_Class_Id() {
        return Mr_Class_Id;
    }

    public void setMr_Class_Id(String Mr_Class_Id) {
        this.Mr_Class_Id = Mr_Class_Id;
    }

    public String getMr_Desc() {
        return Mr_Desc;
    }

    public void setMr_Desc(String Mr_Desc) {
        this.Mr_Desc = Mr_Desc;
    }

    public String getMr_Status_Id() {
        return Mr_Status_Id;
    }

    public void setMr_Status_Id(String Mr_Status_Id) {
        this.Mr_Status_Id = Mr_Status_Id;
    }

    public String getMr_Status_Desc() {
        return Mr_Status_Desc;
    }

    public void setMr_Status_Desc(String Mr_Status_Desc) {
        this.Mr_Status_Desc = Mr_Status_Desc;
    }

    public String getMr_Agr_Id() {
        return Mr_Agr_Id;
    }

    public void setMr_Agr_Id(String Mr_Agr_Id) {
        this.Mr_Agr_Id = Mr_Agr_Id;
    }

    public String getMr_Agr_Desc() {
        return Mr_Agr_Desc;
    }

    public void setMr_Agr_Desc(String Mr_Agr_Desc) {
        this.Mr_Agr_Desc = Mr_Agr_Desc;
    }

    public String getMr_Onsite() {
        return Mr_Onsite;
    }

    public void setMr_Onsite(String Mr_Onsite) {
        this.Mr_Onsite = Mr_Onsite;
    }

    public String getMr_Onsite_Desc() {
        return Mr_Onsite_Desc;
    }

    public void setMr_Onsite_Desc(String Mr_Onsite_Desc) {
        this.Mr_Onsite_Desc = Mr_Onsite_Desc;
    }

    public String getMr_Delay() {
        return Mr_Delay;
    }

    public void setMr_Delay(String Mr_Delay) {
        this.Mr_Delay = Mr_Delay;
    }

    public String getDelay_Reason() {
        return Delay_Reason;
    }

    public void setDelay_Reason(String Delay_Reason) {
        this.Delay_Reason = Delay_Reason;
    }

    public String getHigh_Low_Id() {
        return High_Low_Id;
    }

    public void setHigh_Low_Id(String High_Low_Id) {
        this.High_Low_Id = High_Low_Id;
    }
}
