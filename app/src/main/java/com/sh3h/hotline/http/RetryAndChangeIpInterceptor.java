package com.sh3h.hotline.http;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.sh3h.hotline.MainApplication;
import com.zhouyou.http.EasyHttp;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/6/24 14:27
 */

public class RetryAndChangeIpInterceptor implements Interceptor {

    //这里的url定义不是很规范,可以的话请自己定义一个集合之类的直接通过集合来传比较合适
    private final int mRetryTimes;

    //retrytimes 重试次数
    public RetryAndChangeIpInterceptor(int retryTimes) {
        mRetryTimes = retryTimes;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        //这里做了url的判断来保存sp里面的内外网标识,访问2次成功就更改一下,成功就不进行操作
        Request request = chain.request();
        Response response = doRequest(chain, request);
        int tryCount = 0;
        String url = request.url().toString();
        String outUrl = MainApplication.getInstance().getConfigHelper().getBaseUri();
        String inUrl = MainApplication.getInstance().getConfigHelper().getBaseReservedUri();
//        if (!url.contains(baseUrl)) {
//            url = url.substring(url.indexOf("CustomerService") + "CustomerService/".length());
//            url = baseUrl + url;
//            request = request.newBuilder().url(url).build();
//            EasyHttp.getInstance().setBaseUrl(baseUrl);
//        }
        while (response == null && tryCount < mRetryTimes) {
            if (url.contains(inUrl)) {
                url = url.replace(inUrl, outUrl);
                EasyHttp.getInstance().setBaseUrl(outUrl);
            } else {
                url = url.replace(outUrl, inUrl);
                EasyHttp.getInstance().setBaseUrl(inUrl);
            }
            Request newRequest = request.newBuilder().url(url).build();
            tryCount++;
            //这里在为空的response的时候进行请求
            response = doRequest(chain, newRequest);
        }
        if (response == null) {
            throw new IOException();
        }
        return response;
    }

    private Response doRequest(Chain chain, Request request) {
        Response response = null;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return response;
    }
}
