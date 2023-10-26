package com.sh3h.hotline.entity;

public class UploadDataResult {
    private int state;
    private String msg;
    private Data data;
    public void setState(int state) {
        this.state = state;
    }
    public int getState() {
        return state;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public Data getData() {
        return data;
    }

    public class Data {
        private String cmCaseId;
        private String faId;
        private String albh;
        private String cmMsgId;
        private String code;
        private String cmMsgDesc;
        private String message;
        public void setCmCaseId(String cmCaseId) {
            this.cmCaseId = cmCaseId;
        }
        public String getCmCaseId() {
            return cmCaseId;
        }

        public void setFaId(String faId) {
            this.faId = faId;
        }
        public String getFaId() {
            return faId;
        }

        public void setCmMsgId(String cmMsgId) {
            this.cmMsgId = cmMsgId;
        }
        public String getCmMsgId() {
            return cmMsgId;
        }

        public void setCmMsgDesc(String cmMsgDesc) {
            this.cmMsgDesc = cmMsgDesc;
        }
        public String getCmMsgDesc() {
            return cmMsgDesc;
        }

        public String getAlbh() {
            return albh;
        }

        public void setAlbh(String albh) {
            this.albh = albh;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
