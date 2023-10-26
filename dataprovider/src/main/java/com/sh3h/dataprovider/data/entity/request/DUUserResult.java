package com.sh3h.dataprovider.data.entity.request;

public class DUUserResult extends DUResponse {
    // / <summary>
    // / 用户编号
    // / </summary>
    private int userId;

    // / <summary>
    // / 用户名
    // / </summary>
    private String userName;

    // / <summary>
    // / 简单账号
    // / </summary>
    private String account;

    // / <summary>
    // / 密码
    // / </summary>
    private String password;

    // / <summary>
    // / 手机
    // / </summary>
    private String cellPhone;

    // / <summary>
    // / 固定电话
    // / </summary>
    private String phone;

    // / <summary>
    // / 地址
    // / </summary>
    private String address;

    public DUUserResult() {
        this.userId = 0;
        this.userName = null;
        this.account = null;
        this.password = null;
        this.cellPhone = null;
        this.phone = null;
        this.address = null;
    }

    public DUUserResult(int userId,
                        String userName,
                        String account,
                        String password,
                        String cellPhone,
                        String phone,
                        String address) {
        this.userId = userId;
        this.userName = userName;
        this.account = account;
        this.password = password;
        this.cellPhone = cellPhone;
        this.phone = phone;
        this.address = address;
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

