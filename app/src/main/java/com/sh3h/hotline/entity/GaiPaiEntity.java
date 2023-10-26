package com.sh3h.hotline.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户工单改派
 *
 * @author xiaochao.dev@gamil.com
 * @date 2019/4/12 10:00
 */
public class GaiPaiEntity {

    /**
     * faId : 3543289764332
     * caseId : 945734662
     * actFlg : 00
     */

    private String faId;
    private String caseId;
    private String actFlg;

    public static List<GaiPaiEntity> arrayGaiPaiEntityFromData(String str) {

        Type listType = new TypeToken<ArrayList<GaiPaiEntity>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<GaiPaiEntity> arrayGaiPaiEntityFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<GaiPaiEntity>>() {
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

    public String getActFlg() {
        return actFlg;
    }

    public void setActFlg(String actFlg) {
        this.actFlg = actFlg;
    }
}
