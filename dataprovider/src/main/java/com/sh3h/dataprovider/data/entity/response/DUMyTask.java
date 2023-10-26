package com.sh3h.dataprovider.data.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.Objects;

/**
 * 我的工单任务
 * Created by zhangjing on 2016/10/9.
 */
@Entity
public class DUMyTask implements Parcelable {
    /**
     * cardId : 13test
     * customerId : 1609673871
     * extend :
     * issueAddress : 111
     * issueContent : 用水服务质量
     * issueName :
     * issueTime : 1476328500000
     * issueType : 服务投诉
     * issuer : 111
     * moblie : 111
     * receviceComment : 12111e
     * replyClass : 2小时
     * replyDeadline : 1476335820000
     * station : 101
     * taskId : 1610001101
     * taskState : 1
     * telephone : 111
     */

    @Id(autoincrement = true)
    private Long ID;

    @Transient
    private String cardId;
    @Transient
    private String customerId;
    @Transient
    private String extend;
    @Transient
    private String issueAddress;
    @Transient
    private String issueContent;
    @Transient
    private String issueName;
    @Transient
    private String issueOrigin;
    @Transient
    private long issueTime;
    @Transient
    private String issueType;
    @Transient
    private String issuer;
    @Transient
    private String moblie;
    @Transient
    private String receviceComment;
    @Transient
    private String replyClass;
    @Transient
    private long replyDeadline;
    @Transient
    private String station;
    @Transient
    private String taskId;
    @Transient
    private String telephone;
    @Transient
    private long dispatchTime;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DUMyTask duMyTask = (DUMyTask) o;
        return Objects.equals(faId, duMyTask.faId) &&
//                Objects.equals(taskState,duMyTask.taskState) &&
                Objects.equals(caseId, duMyTask.caseId) /* &&
                Objects.equals(cmSta, duMyTask.cmSta) &&
                Objects.equals(entityName, duMyTask.entityName) &&
                Objects.equals(disPatGrp, duMyTask.disPatGrp) &&
                Objects.equals(repCd, duMyTask.repCd) &&
                Objects.equals(acctId, duMyTask.acctId) &&
                Objects.equals(ldsj, duMyTask.ldsj) &&
                Objects.equals(fsdz, duMyTask.fsdz) &&
                Objects.equals(contactValue, duMyTask.contactValue) &&
                Objects.equals(mobile, duMyTask.mobile) &&
                Objects.equals(fyly, duMyTask.fyly) &&
                Objects.equals(faTypeCd, duMyTask.faTypeCd) &&
                Objects.equals(fynr, duMyTask.fynr) &&
                Objects.equals(cljb, duMyTask.cljb) &&
                Objects.equals(clsx, duMyTask.clsx) &&
                Objects.equals(comment, duMyTask.comment) &&
                Objects.equals(creDttm, duMyTask.creDttm)*/;
    }

    @Override
    public int hashCode() {
        return Objects.hash(faId, caseId/*, oldCaseId, cmSta, entityName, disPatGrp, repCd, acctId, ldsj, fsdz, contactValue, mobile, fyly, faTypeCd, fynr, cljb, clsx, comment, creDttm*/);
    }

    private String faId;
    private String caseId;
    private String oldCaseId;
    private String cmSta;
    private String entityName;
    private String disPatGrp;
    private String repCd;
    private String acctId;
    private String ldsj;
    private String fsdz;
    private String contactValue;
    private String mobile;
    private String fyly;
    private String faTypeCd;
    private String fynr;
    private String cljb;
    private String clsx;
    private Long clsxLong;
    private String comment;
    private String creDttm;
    private String userId;
    private String shComment;
    /**
     * 上传成功时间(暂时替代使用,若后期使用该字段需要重新定义上传成功时间字段)
     */
    private String isFlag;
    private String applyType;
    private String yysj;

    /**
     * 状态：工单或历史工单
     */
    private int taskState;
    /**
     * 状态：处理、退单、接收、派工、完成
     */
    private int state;
    /**
     * 是否已上传
     */
    private int isUploadSuccess;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getIssueAddress() {
        return issueAddress;
    }

    public void setIssueAddress(String issueAddress) {
        this.issueAddress = issueAddress;
    }

    public String getIssueContent() {
        return issueContent;
    }

    public void setIssueContent(String issueContent) {
        this.issueContent = issueContent;
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

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getMoblie() {
        return moblie;
    }

    public void setMoblie(String moblie) {
        this.moblie = moblie;
    }

    public String getReceviceComment() {
        return receviceComment;
    }

    public void setReceviceComment(String receviceComment) {
        this.receviceComment = receviceComment;
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

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public long getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(long dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public String getIssueOrigin() {
        return issueOrigin;
    }

    public void setIssueOrigin(String issueOrigin) {
        this.issueOrigin = issueOrigin;
    }

    public String getFaId() {
        return this.faId;
    }

    public void setFaId(String faId) {
        this.faId = faId;
    }

    public String getCaseId() {
        return this.caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getOldCaseId() {
        return this.oldCaseId;
    }

    public void setOldCaseId(String oldCaseId) {
        this.oldCaseId = oldCaseId;
    }

    public String getCmSta() {
        return this.cmSta;
    }

    public void setCmSta(String cmSta) {
        this.cmSta = cmSta;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getDisPatGrp() {
        return this.disPatGrp;
    }

    public void setDisPatGrp(String disPatGrp) {
        this.disPatGrp = disPatGrp;
    }

    public String getRepCd() {
        return this.repCd;
    }

    public void setRepCd(String repCd) {
        this.repCd = repCd;
    }

    public String getAcctId() {
        return this.acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getLdsj() {
        return this.ldsj;
    }

    public void setLdsj(String ldsj) {
        this.ldsj = ldsj;
    }

    public String getFsdz() {
        return this.fsdz;
    }

    public void setFsdz(String fsdz) {
        this.fsdz = fsdz;
    }

    public String getContactValue() {
        return this.contactValue;
    }

    public void setContactValue(String contactValue) {
        this.contactValue = contactValue;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFyly() {
        return this.fyly;
    }

    public void setFyly(String fyly) {
        this.fyly = fyly;
    }

    public String getFaTypeCd() {
        return this.faTypeCd;
    }

    public void setFaTypeCd(String faTypeCd) {
        this.faTypeCd = faTypeCd;
    }

    public String getFynr() {
        return this.fynr;
    }

    public void setFynr(String fynr) {
        this.fynr = fynr;
    }

    public String getCljb() {
        return this.cljb;
    }

    public void setCljb(String cljb) {
        this.cljb = cljb;
    }

    public String getClsx() {
        return this.clsx;
    }

    public void setClsx(String clsx) {
        this.clsx = clsx;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreDttm() {
        return this.creDttm;
    }

    public void setCreDttm(String creDttm) {
        this.creDttm = creDttm;
    }

    public Long getID() {
        return this.ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getIsUploadSuccess() {
        return this.isUploadSuccess;
    }

    public void setIsUploadSuccess(int isUploadSuccess) {
        this.isUploadSuccess = isUploadSuccess;
    }

    public Long getClsxLong() {
        return this.clsxLong;
    }

    public void setClsxLong(Long clsxLong) {
        this.clsxLong = clsxLong;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShComment() {
        return this.shComment;
    }

    public void setShComment(String shComment) {
        this.shComment = shComment;
    }

    public String getIsFlag() {
        return this.isFlag;
    }

    public void setIsFlag(String isFlag) {
        this.isFlag = isFlag;
    }

    public String getApplyType() {
        return this.applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.ID);
        dest.writeString(this.cardId);
        dest.writeString(this.customerId);
        dest.writeString(this.extend);
        dest.writeString(this.issueAddress);
        dest.writeString(this.issueContent);
        dest.writeString(this.issueName);
        dest.writeString(this.issueOrigin);
        dest.writeLong(this.issueTime);
        dest.writeString(this.issueType);
        dest.writeString(this.issuer);
        dest.writeString(this.moblie);
        dest.writeString(this.receviceComment);
        dest.writeString(this.replyClass);
        dest.writeLong(this.replyDeadline);
        dest.writeString(this.station);
        dest.writeString(this.taskId);
        dest.writeString(this.telephone);
        dest.writeLong(this.dispatchTime);
        dest.writeString(this.faId);
        dest.writeString(this.caseId);
        dest.writeString(this.oldCaseId);
        dest.writeString(this.cmSta);
        dest.writeString(this.entityName);
        dest.writeString(this.disPatGrp);
        dest.writeString(this.repCd);
        dest.writeString(this.acctId);
        dest.writeString(this.ldsj);
        dest.writeString(this.fsdz);
        dest.writeString(this.contactValue);
        dest.writeString(this.mobile);
        dest.writeString(this.fyly);
        dest.writeString(this.faTypeCd);
        dest.writeString(this.fynr);
        dest.writeString(this.cljb);
        dest.writeString(this.clsx);
        dest.writeValue(this.clsxLong);
        dest.writeString(this.comment);
        dest.writeString(this.creDttm);
        dest.writeString(this.userId);
        dest.writeString(this.shComment);
        dest.writeString(this.isFlag);
        dest.writeString(this.applyType);
        dest.writeInt(this.taskState);
        dest.writeInt(this.state);
        dest.writeInt(this.isUploadSuccess);
        dest.writeString(this.yysj);
    }

    public String getYysj() {
        return this.yysj;
    }

    public void setYysj(String yysj) {
        this.yysj = yysj;
    }

    protected DUMyTask(Parcel in) {
        this.ID = (Long) in.readValue(Long.class.getClassLoader());
        this.cardId = in.readString();
        this.customerId = in.readString();
        this.extend = in.readString();
        this.issueAddress = in.readString();
        this.issueContent = in.readString();
        this.issueName = in.readString();
        this.issueOrigin = in.readString();
        this.issueTime = in.readLong();
        this.issueType = in.readString();
        this.issuer = in.readString();
        this.moblie = in.readString();
        this.receviceComment = in.readString();
        this.replyClass = in.readString();
        this.replyDeadline = in.readLong();
        this.station = in.readString();
        this.taskId = in.readString();
        this.telephone = in.readString();
        this.dispatchTime = in.readLong();
        this.faId = in.readString();
        this.caseId = in.readString();
        this.oldCaseId = in.readString();
        this.cmSta = in.readString();
        this.entityName = in.readString();
        this.disPatGrp = in.readString();
        this.repCd = in.readString();
        this.acctId = in.readString();
        this.ldsj = in.readString();
        this.fsdz = in.readString();
        this.contactValue = in.readString();
        this.mobile = in.readString();
        this.fyly = in.readString();
        this.faTypeCd = in.readString();
        this.fynr = in.readString();
        this.cljb = in.readString();
        this.clsx = in.readString();
        this.clsxLong = (Long) in.readValue(Long.class.getClassLoader());
        this.comment = in.readString();
        this.creDttm = in.readString();
        this.userId = in.readString();
        this.shComment = in.readString();
        this.isFlag = in.readString();
        this.applyType = in.readString();
        this.taskState = in.readInt();
        this.state = in.readInt();
        this.isUploadSuccess = in.readInt();
        this.yysj = in.readString();
    }

    @Generated(hash = 569510406)
    public DUMyTask(Long ID, String faId, String caseId, String oldCaseId, String cmSta, String entityName, String disPatGrp, String repCd, String acctId, String ldsj, String fsdz,
                    String contactValue, String mobile, String fyly, String faTypeCd, String fynr, String cljb, String clsx, Long clsxLong, String comment, String creDttm, String userId,
                    String shComment, String isFlag, String applyType, String yysj, int taskState, int state, int isUploadSuccess) {
        this.ID = ID;
        this.faId = faId;
        this.caseId = caseId;
        this.oldCaseId = oldCaseId;
        this.cmSta = cmSta;
        this.entityName = entityName;
        this.disPatGrp = disPatGrp;
        this.repCd = repCd;
        this.acctId = acctId;
        this.ldsj = ldsj;
        this.fsdz = fsdz;
        this.contactValue = contactValue;
        this.mobile = mobile;
        this.fyly = fyly;
        this.faTypeCd = faTypeCd;
        this.fynr = fynr;
        this.cljb = cljb;
        this.clsx = clsx;
        this.clsxLong = clsxLong;
        this.comment = comment;
        this.creDttm = creDttm;
        this.userId = userId;
        this.shComment = shComment;
        this.isFlag = isFlag;
        this.applyType = applyType;
        this.yysj = yysj;
        this.taskState = taskState;
        this.state = state;
        this.isUploadSuccess = isUploadSuccess;
    }

    @Generated(hash = 672873709)
    public DUMyTask() {
    }

    public static final Parcelable.Creator<DUMyTask> CREATOR = new Parcelable.Creator<DUMyTask>() {
        @Override
        public DUMyTask createFromParcel(Parcel source) {
            return new DUMyTask(source);
        }

        @Override
        public DUMyTask[] newArray(int size) {
            return new DUMyTask[size];
        }
    };
}
