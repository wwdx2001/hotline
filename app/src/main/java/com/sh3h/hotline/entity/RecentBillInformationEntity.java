package com.sh3h.hotline.entity;

import java.util.List;

public class RecentBillInformationEntity {

    /**
     * state : 0
     * msg :
     * data : [{"postType":"公告类型0","publishDt":"2019-04-02 16:33:40","publisher":"发布人0","company":"发布单位0","cutDt":"2019-04-02 16:33:40","postArea":"0","postContent":"公告内容0"},{"postType":"公告类型1","publishDt":"2019-04-02 16:33:40","publisher":"发布人1","company":"发布单位1","cutDt":"2019-04-02 16:33:40","postArea":"1","postContent":"公告内容1"}]
     */

    private int state;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * postType : 公告类型0
         * publishDt : 2019-04-02 16:33:40
         * publisher : 发布人0
         * company : 发布单位0
         * cutDt : 2019-04-02 16:33:40
         * postArea : 0
         * postContent : 公告内容0
         */

        private String postType;
        private String publishDt;
        private String publisher;
        private String company;
        private String cutDt;
        private String postArea;
        private String postContent;

        public String getPostType()

        {
            return postType;
        }

        public void setPostType(String postType) {
            this.postType = postType;
        }

        public String getPublishDt() {
            return publishDt;
        }

        public void setPublishDt(String publishDt) {
            this.publishDt = publishDt;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getCutDt() {
            return cutDt;
        }

        public void setCutDt(String cutDt) {
            this.cutDt = cutDt;
        }

        public String getPostArea() {
            return postArea;
        }

        public void setPostArea(String postArea) {
            this.postArea = postArea;
        }

        public String getPostContent() {
            return postContent;
        }

        public void setPostContent(String postContent) {
            this.postContent = postContent;
        }
    }
}
