package com.sh3h.dataprovider.data.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 工单流程返回的服务器文件实体
 * Created by dengzhimin on 2017/2/23.
 */

public class DUFileRemote implements Parcelable{

    private String fileType;//文件类型

    private String fileName;//文件名称

    private String fileUrl;//文件url

    public DUFileRemote(){}

    public DUFileRemote(String fileType, String fileName, String fileUrl) {
        this.fileType = fileType;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    protected DUFileRemote(Parcel in) {
        fileType = in.readString();
        fileName = in.readString();
        fileUrl = in.readString();
    }

    public static final Creator<DUFileRemote> CREATOR = new Creator<DUFileRemote>() {
        @Override
        public DUFileRemote createFromParcel(Parcel in) {
            return new DUFileRemote(in);
        }

        @Override
        public DUFileRemote[] newArray(int size) {
            return new DUFileRemote[size];
        }
    };

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileType);
        dest.writeString(fileName);
        dest.writeString(fileUrl);
    }
}
