package com.sh3h.dataprovider.http;

import java.io.IOException;

import okhttp3.Response;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/9/26 16:18
 */
public class ResponsBean {
    private Response response;
    private IOException ioException;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public IOException getIoException() {
        return ioException;
    }

    public void setIoException(IOException ioException) {
        this.ioException = ioException;
    }
}
