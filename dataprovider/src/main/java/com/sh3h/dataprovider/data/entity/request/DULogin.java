package com.sh3h.dataprovider.data.entity.request;

/**
 * 登录请求体
 * Created by dengzhimin on 2016/10/12.
 */

public class DULogin {

    private String account;//登录账号

    private String pwd;//密码

    private String AppIdentify;//app认证码

    public DULogin(String account, String pwd, String appIdentify) {
        this.account = account;
        this.pwd = pwd;
        AppIdentify = appIdentify;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getAppIdentify() {
        return AppIdentify;
    }

    public void setAppIdentify(String appIdentify) {
        AppIdentify = appIdentify;
    }
}
