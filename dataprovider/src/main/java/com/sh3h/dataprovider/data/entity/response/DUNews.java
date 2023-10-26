package com.sh3h.dataprovider.data.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 公告实体
 * Created by dengzhimin on 2016/10/11.
 */

public class DUNews implements Parcelable{

    private String pubPerson;//发布人

    private String pubDepartment;//发布单位

    private long pubTime;//发布时间

    private String planStart;//起始停水时间

    private String planEnd;//截止停水时间

    private String type;//公告类型

    private String scope;//公告范围

    private String title;//公告标题

    private String content;//公告内容

    public DUNews(String pubPerson, String pubDepartment, long pubTime, String planStart, String planEnd, String type, String scope,
                  String title, String content) {
        this.pubPerson = pubPerson;
        this.pubDepartment = pubDepartment;
        this.pubTime = pubTime;
        this.planStart = planStart;
        this.planEnd = planEnd;
        this.type = type;
        this.scope = scope;
        this.title = title;
        this.content = content;
    }

    protected DUNews(Parcel in) {
        pubPerson = in.readString();
        pubDepartment = in.readString();
        pubTime = in.readLong();
        planStart = in.readString();
        planEnd = in.readString();
        type = in.readString();
        scope = in.readString();
        title = in.readString();
        content = in.readString();
    }

    public static final Creator<DUNews> CREATOR = new Creator<DUNews>() {
        @Override
        public DUNews createFromParcel(Parcel in) {
            return new DUNews(in);
        }

        @Override
        public DUNews[] newArray(int size) {
            return new DUNews[size];
        }
    };

    public String getPubPerson() {
        return pubPerson;
    }

    public void setPubPerson(String pubPerson) {
        this.pubPerson = pubPerson;
    }

    public String getPubDepartment() {
        return pubDepartment;
    }

    public void setPubDepartment(String pubDepartment) {
        this.pubDepartment = pubDepartment;
    }

    public long getPubTime() {
        return pubTime;
    }

    public void setPubTime(long pubTime) {
        this.pubTime = pubTime;
    }

    public String getPlanStart() {
        return planStart;
    }

    public void setPlanStart(String planStart) {
        this.planStart = planStart;
    }

    public String getPlanEnd() {
        return planEnd;
    }

    public void setPlanEnd(String planEnd) {
        this.planEnd = planEnd;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pubPerson);
        dest.writeString(pubDepartment);
        dest.writeLong(pubTime);
        dest.writeString(planStart);
        dest.writeString(planEnd);
        dest.writeString(type);
        dest.writeString(scope);
        dest.writeString(title);
        dest.writeString(content);
    }
}
