package com.sh3h.dataprovider.data.entity.response;

/**
 * 登录返回结果实体
 * Created by dengzhimin on 2016/10/12.
 */

public class DULogin {

    private int userId;
    private String access_token;

    public DULogin(int userId, String access_token){
        this.userId = userId;
        this.access_token = access_token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
