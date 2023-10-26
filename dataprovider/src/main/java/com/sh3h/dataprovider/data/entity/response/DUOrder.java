package com.sh3h.dataprovider.data.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 工单实体
 * Created by dengzhimin on 2016/10/11.
 */

public class DUOrder implements Parcelable {

    //工单基本信息
    private String customerId;//客服编号

    private String taskId;//任务编号

    private String issuer;//反映人

    private String station;//受理站点

    private String issueName;//户名

    private String issueAddress;//发生地址

    private long issueTime;//发生时间

    private String telephone;//联系电话

    private String moblie;//手机

    private String issueType;//反映类型

    private String issueContent;//反映内容

    private String issueOrigin;//反映来源

    private String replyClass;//处理级别

    private long replyDeadline;//处理时限

    private String receviceComment;//受理备注

    private String cardId;//销根号

    private long dispatchTime;//派单时间

    private String extend;

    public DUOrder() {
    }

    public DUOrder(String customerId, String taskId, String issuer, String station, String issueName, String issueAddress, long issueTime, String telephone,
                   String moblie, String issueOrigin, String issueType, String issueContent, String replyClass, long replyDeadline, String receviceComment, String cardId,
                   long dispatchTime, String extend) {
        this.customerId = customerId;
        this.taskId = taskId;
        this.issuer = issuer;
        this.station = station;
        this.issueName = issueName;
        this.issueAddress = issueAddress;
        this.issueTime = issueTime;
        this.telephone = telephone;
        this.issueOrigin = issueOrigin;
        this.moblie = moblie;
        this.issueType = issueType;
        this.issueContent = issueContent;
        this.replyClass = replyClass;
        this.replyDeadline = replyDeadline;
        this.receviceComment = receviceComment;
        this.cardId = cardId;
        this.dispatchTime = dispatchTime;
        this.extend = extend;
    }

    protected DUOrder(Parcel in) {
        customerId = in.readString();
        taskId = in.readString();
        issuer = in.readString();
        station = in.readString();
        issueName = in.readString();
        issueAddress = in.readString();
        issueTime = in.readLong();
        telephone = in.readString();
        issueOrigin=in.readString();
        moblie = in.readString();
        issueType = in.readString();
        issueContent = in.readString();
        replyClass = in.readString();
        replyDeadline = in.readLong();
        receviceComment = in.readString();
        cardId = in.readString();
        dispatchTime = in.readLong();
        extend = in.readString();
    }

    public static final Creator<DUOrder> CREATOR = new Creator<DUOrder>() {
        @Override
        public DUOrder createFromParcel(Parcel in) {
            return new DUOrder(in);
        }

        @Override
        public DUOrder[] newArray(int size) {
            return new DUOrder[size];
        }
    };

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public String getIssueAddress() {
        return issueAddress;
    }

    public void setIssueAddress(String issueAddress) {
        this.issueAddress = issueAddress;
    }

    public long getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(long issueTime) {
        this.issueTime = issueTime;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMoblie() {
        return moblie;
    }

    public void setMoblie(String moblie) {
        this.moblie = moblie;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getIssueContent() {
        return issueContent;
    }

    public void setIssueContent(String issueContent) {
        this.issueContent = issueContent;
    }

    public String getReplyClass() {
        return replyClass;
    }

    public void setReplyClass(String replyClass) {
        this.replyClass = replyClass;
    }

    public long getReplyDeadline() {
        return replyDeadline;
    }

    public void setReplyDeadline(long replyDeadline) {
        this.replyDeadline = replyDeadline;
    }

    public String getReceviceComment() {
        return receviceComment;
    }

    public void setReceviceComment(String receviceComment) {
        this.receviceComment = receviceComment;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public long getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(long dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customerId);
        dest.writeString(taskId);
        dest.writeString(issuer);
        dest.writeString(station);
        dest.writeString(issueName);
        dest.writeString(issueAddress);
        dest.writeLong(issueTime);
        dest.writeString(telephone);
        dest.writeString(issueOrigin);
        dest.writeString(moblie);
        dest.writeString(issueType);
        dest.writeString(issueContent);
        dest.writeString(replyClass);
        dest.writeLong(replyDeadline);
        dest.writeString(receviceComment);
        dest.writeString(cardId);
        dest.writeLong(dispatchTime);
        dest.writeString(extend);
    }

    public String getIssueOrigin() {
        return issueOrigin;
    }

    public void setIssueOrigin(String issueOrigin) {
        this.issueOrigin = issueOrigin;
    }
}
