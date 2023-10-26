package com.sh3h.serverprovider.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 客户端信息
 */
public class ClientInfoEntity {

    private int _appVersion;
    private int _dataVersion;

    public int getAppVersion() {
        return _appVersion;
    }

    public void setAppVersion(int appVersion) {
        _appVersion = appVersion;
    }

    public int getDataVersion() {
        return _dataVersion;
    }

    public void setDataVersion(int dataVersion) {
        _dataVersion = dataVersion;
    }

    public JSONObject toJSON() {

        JSONObject result = new JSONObject();

        try {
            result.put("AppVersion", getAppVersion());
            result.put("DataVersion", getDataVersion());
        } catch (JSONException e) {
            return null;
        }

        return result;
    }
}

