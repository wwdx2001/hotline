package com.sh3h.dataprovider.data.entity.push;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.annotation.Entity;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/4/4 15:16
 */
@Entity
public class WorkInfoSynchronizationEntity {

    /**
     * faId : aa
     * caseId : aa
     * oldCaseId : aa
     * cmSta : aa
     * entityName : aa
     * disPatGrp : aa
     * repCd : aa
     * acctId : aa
     * ldsj : 2019-01-01T00:00:00
     * fsdz : aa
     * contactValue : aa
     * mobile : aa
     * fyly : aa
     * faTypeCd : aa
     * fynr : aa
     * cljb : aa
     * clsx : 2019-01-01T00:00:00
     * comment : aa
     * creDttm : 2019-01-01T00:00:00
     */

    private String faId;
    private String caseId;
    private String oldCaseId;
    private String cmSta;
    private String entityName;
    private String disPatGrp;
    private String repCd;
    private String acctId;
    private String ldsj;
    private String fsdz;
    private String contactValue;
    private String mobile;
    private String fyly;
    private String faTypeCd;
    private String fynr;
    private String cljb;
    private String clsx;
    private String comment;
    private String creDttm;


    @Generated(hash = 1047702216)
    public WorkInfoSynchronizationEntity(String faId, String caseId,
            String oldCaseId, String cmSta, String entityName, String disPatGrp,
            String repCd, String acctId, String ldsj, String fsdz,
            String contactValue, String mobile, String fyly, String faTypeCd,
            String fynr, String cljb, String clsx, String comment, String creDttm) {
        this.faId = faId;
        this.caseId = caseId;
        this.oldCaseId = oldCaseId;
        this.cmSta = cmSta;
        this.entityName = entityName;
        this.disPatGrp = disPatGrp;
        this.repCd = repCd;
        this.acctId = acctId;
        this.ldsj = ldsj;
        this.fsdz = fsdz;
        this.contactValue = contactValue;
        this.mobile = mobile;
        this.fyly = fyly;
        this.faTypeCd = faTypeCd;
        this.fynr = fynr;
        this.cljb = cljb;
        this.clsx = clsx;
        this.comment = comment;
        this.creDttm = creDttm;
    }

    @Generated(hash = 1483745808)
    public WorkInfoSynchronizationEntity() {
    }


    public String getFaId() {
        return faId;
    }

    public void setFaId(String faId) {
        this.faId = faId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getOldCaseId() {
        return oldCaseId;
    }

    public void setOldCaseId(String oldCaseId) {
        this.oldCaseId = oldCaseId;
    }

    public String getCmSta() {
        return cmSta;
    }

    public void setCmSta(String cmSta) {
        this.cmSta = cmSta;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getDisPatGrp() {
        return disPatGrp;
    }

    public void setDisPatGrp(String disPatGrp) {
        this.disPatGrp = disPatGrp;
    }

    public String getRepCd() {
        return repCd;
    }

    public void setRepCd(String repCd) {
        this.repCd = repCd;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getLdsj() {
        return ldsj;
    }

    public void setLdsj(String ldsj) {
        this.ldsj = ldsj;
    }

    public String getFsdz() {
        return fsdz;
    }

    public void setFsdz(String fsdz) {
        this.fsdz = fsdz;
    }

    public String getContactValue() {
        return contactValue;
    }

    public void setContactValue(String contactValue) {
        this.contactValue = contactValue;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFyly() {
        return fyly;
    }

    public void setFyly(String fyly) {
        this.fyly = fyly;
    }

    public String getFaTypeCd() {
        return faTypeCd;
    }

    public void setFaTypeCd(String faTypeCd) {
        this.faTypeCd = faTypeCd;
    }

    public String getFynr() {
        return fynr;
    }

    public void setFynr(String fynr) {
        this.fynr = fynr;
    }

    public String getCljb() {
        return cljb;
    }

    public void setCljb(String cljb) {
        this.cljb = cljb;
    }

    public String getClsx() {
        return clsx;
    }

    public void setClsx(String clsx) {
        this.clsx = clsx;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreDttm() {
        return creDttm;
    }

    public void setCreDttm(String creDttm) {
        this.creDttm = creDttm;
    }
}
