package com.sh3h.dataprovider.data.entity.response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/4/4 09:17
 */
public class ServiceResultEntity {

    /**
     * cmMsgId : 00
     * cmMsgDesc : 成功
     */

    private String cmMsgId;
    private String cmMsgDesc;


    public String getCmMsgId() {
        return cmMsgId;
    }

    public void setCmMsgId(String cmMsgId) {
        this.cmMsgId = cmMsgId;
    }

    public String getCmMsgDesc() {
        return cmMsgDesc;
    }

    public void setCmMsgDesc(String cmMsgDesc) {
        this.cmMsgDesc = cmMsgDesc;
    }
}
