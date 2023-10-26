package com.sh3h.dataprovider.data.entity.response;

/**
 * 用户信息
 * Created by dengzhimin on 2016/10/12.
 */

public class DUUser {

    private int userId;

    private String userName;

    private int sex;

    private String account;

    private String address;

    private String mobile;

    private String tel;

    public DUUser(int userId, String userName, int sex, String account, String address, String mobile, String tel) {
        this.userId = userId;
        this.userName = userName;
        this.sex = sex;
        this.account = account;
        this.address = address;
        this.mobile = mobile;
        this.tel = tel;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
