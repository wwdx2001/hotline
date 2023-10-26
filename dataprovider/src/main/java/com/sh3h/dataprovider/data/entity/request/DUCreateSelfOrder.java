package com.sh3h.dataprovider.data.entity.request;

/**
 * 上传自开单请求体
 * Created by dengzhimin on 2016/10/11.
 */

public class DUCreateSelfOrder {
    private int userId;

    private String localTaskId;//本地任务编号

    private String cardId;//销根号

    private String issuer;//反映人

    private String issueName;//户名

    private long issueTime;//发生时间

    private String issueAddress;//发生地址

    private String telephone;//联系电话

    private String issueType;//反映类型

    private String issueContent;//反映内容

    private String issueArea;//反映区名

    private String issueOrigin;//反映来源

    private String replyClass;//处理级别

    private long startTime;//开始时间

    private long endTime;//结束时间

    private long replyDeadline;//处理时限

    private String receviceComment;//受理备注

    private boolean replyImmediately;//是否现场处理

    private String extend;

    private long replyTimeInHistory;

    public DUCreateSelfOrder() {
    }

    public DUCreateSelfOrder(int userId, String localTaskId, String cardId, String issuer, String issueName, long issueTime, String issueAddress, String telephone,
                             String issueType, String issueContent, String issueArea, String issueOrigin, String replyClass, long replyDeadline, String receviceComment, boolean replyImmediately,
                             String extend, long startTime, long endTime) {
        this.userId = userId;
        this.localTaskId = localTaskId;
        this.cardId = cardId;
        this.issuer = issuer;
        this.issueName = issueName;
        this.issueTime = issueTime;
        this.issueAddress = issueAddress;
        this.telephone = telephone;
        this.issueType = issueType;
        this.issueContent = issueContent;
        this.issueArea = issueArea;
        this.issueOrigin = issueOrigin;
        this.replyClass = replyClass;
        this.replyDeadline = replyDeadline;
        this.receviceComment = receviceComment;
        this.replyImmediately = replyImmediately;
        this.extend = extend;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLocalTaskId() {
        return localTaskId;
    }

    public void setLocalTaskId(String localTaskId) {
        this.localTaskId = localTaskId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public long getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(long issueTime) {
        this.issueTime = issueTime;
    }

    public String getIssueAddress() {
        return issueAddress;
    }

    public void setIssueAddress(String issueAddress) {
        this.issueAddress = issueAddress;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public boolean isReplyImmediately() {
        return replyImmediately;
    }

    public void setReplyImmediately(boolean replyImmediately) {
        this.replyImmediately = replyImmediately;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public long getReplyTimeInHistory() {
        return replyTimeInHistory;
    }

    public void setReplyTimeInHistory(long replyTimeInHistory) {
        this.replyTimeInHistory = replyTimeInHistory;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getIssueArea() {
        return issueArea;
    }

    public void setIssueArea(String issueArea) {
        this.issueArea = issueArea;
    }

    public String getIssueOrigin() {
        return issueOrigin;
    }

    public void setIssueOrigin(String issueOrigin) {
        this.issueOrigin = issueOrigin;
    }
}
