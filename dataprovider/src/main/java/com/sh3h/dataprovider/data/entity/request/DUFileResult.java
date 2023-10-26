package com.sh3h.dataprovider.data.entity.request;

public class DUFileResult extends DUResponse {
    private int percent;
    private String name;
    private String path;
    private int version;

    public DUFileResult() {
        percent = 0;
        name = null;
        path = null;
    }

    public DUFileResult(int percent, String name, String path) {
        this.percent = percent;
        this.name = name;
        this.path = path;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}

