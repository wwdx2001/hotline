package com.sh3h.dataprovider.data.entity;


import org.json.JSONObject;

public class DUMedia {
    public static class Extend {
        private static final String TIME_FIELD = "time";
        private long time;

        public Extend() {
            this.time = 0;
        }

        public Extend(long time) {
            this.time = time;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public JSONObject toJson() {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(TIME_FIELD, time);
                return jsonObject;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public static Extend fromJson(JSONObject jsonObject) {
            Extend extend = new Extend();
            if (jsonObject != null) {
                extend.setTime(jsonObject.optLong(TIME_FIELD));
            }
            return extend;
        }
    }

    public static final int FILE_TYPE_ALL = -1;
    public static final int FILE_TYPE_PICTURE = 0;
    public static final int FILE_TYPE_VOICE = 1;
    public static final int FILE_TYPE_SIGNUP = 4;
    public static final int FILE_TYPE_VIDEO = 2;
    public static final int FILE_TYPE_SCREEN_SHOT = 3;
    public static final int FILE_TYPE_PICTURE_SCAN = 5;
    public static final int FILE_TYPE_PICTURE_XCHJZP = 6;
    public static final int FILE_TYPE_PICTURE_BPZP = 7;

    public static final String PICTURE_SUFFIX = "jpg";
    public static final String VOICE_SUFFIX = "wav";
    public static final String SIGNUP_SUFFIX = "png";
    public static final String VIDEO_SUFFIX = "video/mp4";

    public static final int UPLOAD_FLAG_LOCAL = 0; // only in the local
    public static final int UPLOAD_FLAG_UPLOADING = 1; // uploading
    public static final int UPLOAD_FLAG_SERVER = 2; // both in the server and local

    public static final int TASK_STATE_ALL = -1;

    private long id;
    private int userId;
    private String taskId;
    private int taskType;
    private int taskState;
    private int fileType;
    private String fileName;
    private String fileHash;
    private String fileUrl;
    private int uploadFlag;
    private String extend;
    private String filePath; // only store in memory, not to db
    private long fileTime;

    public DUMedia() {
    }

    public DUMedia(long id,
                   int userId,
                   String taskId,
                   int taskType,
                   int taskState,
                   int fileType,
                   String fileName,
                   String fileHash,
                   String fileUrl,
                   int uploadFlag,
                   String extend,
                   long fileTime) {
        this.id = id;
        this.userId = userId;
        this.taskId = taskId;
        this.taskType = taskType;
        this.taskState = taskState;
        this.fileType = fileType;
        this.fileName = fileName;
        this.fileHash = fileHash;
        this.fileUrl = fileUrl;
        this.uploadFlag = uploadFlag;
        this.extend = extend;
        this.filePath = null;
        this.fileTime = fileTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getUploadFlag() {
        return uploadFlag;
    }

    public void setUploadFlag(int uploadFlag) {
        this.uploadFlag = uploadFlag;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileTime() {
        return fileTime;
    }

    public void setFileTime(long fileTime) {
        this.fileTime = fileTime;
    }
}
