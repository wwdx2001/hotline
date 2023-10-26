package com.sh3h.dataprovider.data.entity.base;

import java.util.List;

/**
 * Created by dengzhimin on 2016/6/1.
 */
public class DUEntitiesResult<T> {
    public static final int SUCCESS_CODE = 0;
    private int code;
    private int statusCode;
    private String message;
    private List<T> data;

    public DUEntitiesResult(int code, int statusCode, String message, List<T> data) {
        this.code = code;
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
