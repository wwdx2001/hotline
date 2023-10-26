package com.sh3h.dataprovider.data.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/4/2 13:10
 */
public class WaterStopNotificationEntity implements Parcelable {

    /**
     * postType : 公告类型0
     * publishDt : 2019-04-02 11:10:53
     * publisher : 发布人0
     * company : 发布单位0
     * cutDt : 2019-04-02 11:10:53
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

    public static List<WaterStopNotificationEntity> arrayWaterStopNotificationEntityFromData(String str) {

        Type listType = new TypeToken<ArrayList<WaterStopNotificationEntity>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<WaterStopNotificationEntity> arrayWaterStopNotificationEntityFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<WaterStopNotificationEntity>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public String getPostType() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.postType);
        dest.writeString(this.publishDt);
        dest.writeString(this.publisher);
        dest.writeString(this.company);
        dest.writeString(this.cutDt);
        dest.writeString(this.postArea);
        dest.writeString(this.postContent);
    }

    public WaterStopNotificationEntity() {
    }

    protected WaterStopNotificationEntity(Parcel in) {
        this.postType = in.readString();
        this.publishDt = in.readString();
        this.publisher = in.readString();
        this.company = in.readString();
        this.cutDt = in.readString();
        this.postArea = in.readString();
        this.postContent = in.readString();
    }

    public static final Creator<WaterStopNotificationEntity> CREATOR = new Creator<WaterStopNotificationEntity>() {
        @Override
        public WaterStopNotificationEntity createFromParcel(Parcel source) {
            return new WaterStopNotificationEntity(source);
        }

        @Override
        public WaterStopNotificationEntity[] newArray(int size) {
            return new WaterStopNotificationEntity[size];
        }
    };
}
