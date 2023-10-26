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
 * @date 2019/4/4 15:17
 */
@Entity
public class ReassignmentSynchronizationEntity {

    /**
     * faId : aa
     * caseId : aa
     * actFlg : aa
     */

    private String faId;
    private String caseId;
    private String actFlg;


    @Generated(hash = 7308637)
    public ReassignmentSynchronizationEntity(String faId, String caseId,
            String actFlg) {
        this.faId = faId;
        this.caseId = caseId;
        this.actFlg = actFlg;
    }

    @Generated(hash = 1003669095)
    public ReassignmentSynchronizationEntity() {
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

    public String getActFlg() {
        return actFlg;
    }

    public void setActFlg(String actFlg) {
        this.actFlg = actFlg;
    }
}
