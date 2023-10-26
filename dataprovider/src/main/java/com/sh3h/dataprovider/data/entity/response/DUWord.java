package com.sh3h.dataprovider.data.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 词语信息
 * Created by dengzhimin on 2016/10/12.
 */

public class DUWord implements Parcelable{
    /**
     * group : 005
     * id : 231
     * name : 二次供水
     * parentId : 005003
     * remark :
     * value : 005003006
     * valueEx : 004002,016008,017008
     */

    private String group;
    private int id;
    private String name;
    private String parentId;
    private String remark;
    private String value;
    private String valueEx;

    private List<DUWord> duWordList;
    private boolean isChecked;

    public DUWord() {
    }

    public DUWord(String group, int id, String name, String parentId, String remark, String value, String valueEx) {
        this.group = group;
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.remark = remark;
        this.value = value;
        this.valueEx = valueEx;
    }

    protected DUWord(Parcel in) {
        group = in.readString();
        id = in.readInt();
        name = in.readString();
        parentId = in.readString();
        remark = in.readString();
        value = in.readString();
        valueEx = in.readString();
        duWordList = in.createTypedArrayList(DUWord.CREATOR);
        isChecked = in.readByte() != 0;
    }

    public static final Creator<DUWord> CREATOR = new Creator<DUWord>() {
        @Override
        public DUWord createFromParcel(Parcel in) {
            return new DUWord(in);
        }

        @Override
        public DUWord[] newArray(int size) {
            return new DUWord[size];
        }
    };

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueEx() {
        return valueEx;
    }

    public void setValueEx(String valueEx) {
        this.valueEx = valueEx;
    }

    public List<DUWord> getDuWordList() {
        return duWordList;
    }

    public void setDuWordList(List<DUWord> duWordList) {
        this.duWordList = duWordList;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(group);
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(parentId);
        parcel.writeString(remark);
        parcel.writeString(value);
        parcel.writeString(valueEx);
        parcel.writeTypedList(duWordList);
        parcel.writeByte((byte) (isChecked ? 1 : 0));
    }
}
