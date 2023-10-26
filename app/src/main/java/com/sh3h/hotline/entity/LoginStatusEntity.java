package com.sh3h.hotline.entity;

import com.sh3h.serverprovider.entity.LoginInfoEntity;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/3/13 17:40
 */
public class LoginStatusEntity {

    /**
     * state : 0
     * msg : 登录成功！
     * data : 60
     */

    private int state;
    private String msg;
    private LoginInfoEntity data;

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

    public LoginInfoEntity getData() {
        return data;
    }

    public void setData(LoginInfoEntity data) {
        this.data = data;
    }

    public class LoginInfoEntity{
        private String Code;
        private String Account;
        private String userId;
        private String Message;

        public String getCode() {
            return Code;
        }

        public void setCode(String code) {
            Code = code;
        }

        public String getAccount() {
            return Account;
        }

        public void setAccount(String account) {
            Account = account;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String message) {
            Message = message;
        }
    }
}
