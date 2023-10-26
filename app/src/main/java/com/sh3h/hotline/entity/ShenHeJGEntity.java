package com.sh3h.hotline.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 审核结果
 *
 * @author xiaochao.dev@gamil.com
 * @date 2019/4/17 16:27
 */
public class ShenHeJGEntity {

    /**
     * faId : 234234
     * caseId : 234234
     * applyType : 00
     * result : 00
     * comment : sdfs
     * delayDt : 2019-01-19T00:00:00
     */

    private String faId;
    private String caseId;
    private String applyType;
    private String result;
    private String comment;
    private String delayDt;

    public static List<ShenHeJGEntity> arrayShenHeJGEntityFromData(String str) {

        Type listType = new TypeToken<ArrayList<ShenHeJGEntity>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<ShenHeJGEntity> arrayShenHeJGEntityFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<ShenHeJGEntity>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


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
