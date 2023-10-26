package com.sh3h.hotline.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/4/10 14:30
 */
public class UploadSuccess {

    /**
     * state : 0
     * msg : 上传成功！
     */

    private int state;
    private String msg;

    public static List<UploadSuccess> arrayUploadSuccessFromData(String str) {

        Type listType = new TypeToken<ArrayList<UploadSuccess>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<UploadSuccess> arrayUploadSuccessFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<UploadSuccess>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
