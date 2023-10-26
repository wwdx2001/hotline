package com.sh3h.dataprovider.data.entity.response;

/**
 * 文件关联请求体
 * Created by dengzhimin on 2016/10/12.
 */

public class DUFile {

    private String url;//文件url

    private String fileType;//文件类型

    private long fileSize;//文件大小

    private String fileHash;//文件hash

    private long fileTime;//文件创建时间

    private int taskState;//文件状态

    private String originName; //

    public DUFile(String url, String fileType, long fileSize, String fileHash, String originName,
                  long fileTime, int taskState) {
        this.url = url;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.fileHash = fileHash;
        this.originName = originName;
        this.fileTime = fileTime;
        this.taskState = taskState;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public long getFileTime() {
        return fileTime;
    }

    public void setFileTime(long fileTime) {
        this.fileTime = fileTime;
    }

}
