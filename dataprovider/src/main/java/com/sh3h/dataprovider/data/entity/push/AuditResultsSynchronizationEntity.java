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
 * @date 2019/4/4 15:13
 */
@Entity
public class AuditResultsSynchronizationEntity {

    /**
     * faId : AA
     * caseId : AA
     * applyType : aa
     * result : aa
     * comment : aa
     * delayDt : 2019-01-01T00:00:00
     */

    private String faId;
    private String caseId;
    private String applyType;
    private String result;
    private String comment;
    private String delayDt;

    @Generated(hash = 1555899023)
    public AuditResultsSynchronizationEntity(String faId, String caseId,
            String applyType, String result, String comment, String delayDt) {
        this.faId = faId;
        this.caseId = caseId;
        this.applyType = applyType;
        this.result = result;
        this.comment = comment;
        this.delayDt = delayDt;
    }

    @Generated(hash = 1796882863)
    public AuditResultsSynchronizationEntity() {
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

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDelayDt() {
        return delayDt;
    }

    public void setDelayDt(String delayDt) {
        this.delayDt = delayDt;
    }
}
