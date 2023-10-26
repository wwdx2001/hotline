package com.sh3h.dataprovider.data.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 工单流程
 * Created by dengzhimin on 2017/2/23.
 */
public class DUProcess implements Parcelable {

    private int taskState;//任务状态
    private long taskTime;//工单处理时间
    private String taskOperator;//操作人员
    private String resolveType;//处理类别
    private String resolveContent;//处理内容
    private String issueReason;//发生原因
    private String resolveMethod;//解决措施
    private String resolveResult;//处理结果
    private String resolveComment;//处理备注
    private boolean isFinishTask;//是否完成销单
    private String replyReason;//延期原因/退单原因
    private long approveTime;//审批时间
    private long endTime;//截止时间
    private String aprroveComment;//批复备注
    private boolean approveResult;//批复结果 true: 同意, false: 拒绝
    private List<DUFileRemote> files;//多媒体文件
    private String extend;//扩展信息

    public DUProcess() {
    }

    public DUProcess(int taskState, long taskTime, String taskOperator, String resolveType, String resolveContent, String issueReason, String resolveMethod,
                     String resolveResult, String resolveComment, boolean isFinishTask, String replyReason, long approveTime, long endTime, String aprroveComment, boolean approveResult,
                     List<DUFileRemote> files, String extend) {
        this.taskState = taskState;
        this.taskTime = taskTime;
        this.taskOperator = taskOperator;
        this.resolveType = resolveType;
        this.resolveContent = resolveContent;
        this.issueReason = issueReason;
        this.resolveMethod = resolveMethod;
        this.resolveResult = resolveResult;
        this.resolveComment = resolveComment;
        this.isFinishTask = isFinishTask;
        this.replyReason = replyReason;
        this.approveTime = approveTime;
        this.endTime = endTime;
        this.aprroveComment = aprroveComment;
        this.approveResult = approveResult;
        this.files = files;
        this.extend = extend;
    }

    protected DUProcess(Parcel in) {
        taskState = in.readInt();
        taskTime = in.readLong();
        taskOperator = in.readString();
        resolveType = in.readString();
        resolveContent = in.readString();
        issueReason = in.readString();
        resolveMethod = in.readString();
        resolveResult = in.readString();
        resolveComment = in.readString();
        isFinishTask = in.readByte() != 0;
        replyReason = in.readString();
        approveTime = in.readLong();
        endTime = in.readLong();
        aprroveComment = in.readString();
        approveResult = in.readByte() != 0;
        files = in.readArrayList(DUFileRemote.class.getClassLoader());
        extend = in.readString();
    }

    public static final Creator<DUProcess> CREATOR = new Creator<DUProcess>() {
        @Override
        public DUProcess createFromParcel(Parcel in) {
            return new DUProcess(in);
        }

        @Override
        public DUProcess[] newArray(int size) {
            return new DUProcess[size];
        }
    };

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public long getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(long taskTime) {
        this.taskTime = taskTime;
    }

    public String getTaskOperator() {
        return taskOperator;
    }

    public void setTaskOperator(String taskOperator) {
        this.taskOperator = taskOperator;
    }

    public String getResolveType() {
        return resolveType;
    }

    public void setResolveType(String resolveType) {
        this.resolveType = resolveType;
    }

    public String getResolveContent() {
        return resolveContent;
    }

    public void setResolveContent(String resolveContent) {
        this.resolveContent = resolveContent;
    }

    public String getIssueReason() {
        return issueReason;
    }

    public void setIssueReason(String issueReason) {
        this.issueReason = issueReason;
    }

    public String getResolveMethod() {
        return resolveMethod;
    }

    public void setResolveMethod(String resolveMethod) {
        this.resolveMethod = resolveMethod;
    }

    public String getResolveComment() {
        return resolveComment;
    }

    public void setResolveComment(String resolveComment) {
        this.resolveComment = resolveComment;
    }

    public String getReplyReason() {
        return replyReason;
    }

    public void setReplyReason(String replyReason) {
        this.replyReason = replyReason;
    }

    public long getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(long approveTime) {
        this.approveTime = approveTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getAprroveComment() {
        return aprroveComment;
    }

    public void setAprroveComment(String aprroveComment) {
        this.aprroveComment = aprroveComment;
    }

    public boolean isApproveResult() {
        return approveResult;
    }

    public void setApproveResult(boolean approveResult) {
        this.approveResult = approveResult;
    }

    public List<DUFileRemote> getFiles() {
        return files;
    }

    public void setFiles(List<DUFileRemote> files) {
        this.files = files;
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
        dest.writeInt(taskState);
        dest.writeLong(taskTime);
        dest.writeString(taskOperator);
        dest.writeString(resolveType);
        dest.writeString(resolveContent);
        dest.writeString(issueReason);
        dest.writeString(resolveMethod);
        dest.writeString(resolveResult);
        dest.writeString(resolveComment);
        dest.writeByte((byte) (isFinishTask ? 1 : 0));
        dest.writeString(replyReason);
        dest.writeLong(approveTime);
        dest.writeLong(endTime);
        dest.writeString(aprroveComment);
        dest.writeByte((byte) (approveResult ? 1 : 0));
        dest.writeList(files);
        dest.writeString(extend);
    }

    public String getResolveResult() {
        return resolveResult;
    }

    public void setResolveResult(String resolveResult) {
        this.resolveResult = resolveResult;
    }

    public boolean isFinishTask() {
        return isFinishTask;
    }

    public void setFinishTask(boolean finishTask) {
        isFinishTask = finishTask;
    }
}
