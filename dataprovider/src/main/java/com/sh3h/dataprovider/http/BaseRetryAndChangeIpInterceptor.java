package com.sh3h.dataprovider.http;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.sh3h.dataprovider.BaseApplication;
import com.sh3h.dataprovider.URL;
import com.zhouyou.http.EasyHttp;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/6/24 14:27
 */

public class BaseRetryAndChangeIpInterceptor implements Interceptor {

    //这里的url定义不是很规范,可以的话请自己定义一个集合之类的直接通过集合来传比较合适
    private final int mRetryTimes;

    //retrytimes 重试次数
    public BaseRetryAndChangeIpInterceptor(int retryTimes) {
        mRetryTimes = retryTimes;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        //这里做了url的判断来保存sp里面的内外网标识,访问2次成功就更改一下,成功就不进行操作
        Request request = chain.request();
        ResponsBean responsBean = doRequest(chain, request);
        int tryCount = 0;
        String url = request.url().toString();
//        String outUrl = BaseApplication.getInstance().getConfigHelper().getBaseUri();
//        String inUrl = BaseApplication.getInstance().getConfigHelper().getBaseReservedUri();

        String outUrl = URL.BASE_IN_URL;
        String inUrl = URL.BASE_IN_URL;
        LogUtils.e("----------url0:", url);
        LogUtils.e("----------baseUrlIntranet:", inUrl);
        LogUtils.e("----------baseUrlExtranet:", outUrl);
//        if (!url.contains(baseUrl)) {
//            url = url.substring(url.indexOf("CustomerService") + "CustomerService/".length());
//            url = baseUrl + url;
//            request = request.newBuilder().url(url).build();
//            EasyHttp.getInstance().setBaseUrl(baseUrl);
//        }
        while (responsBean.getResponse() == null && tryCount < mRetryTimes) {
            if (url.contains(inUrl)) {
                url = url.replace(inUrl, outUrl);
                EasyHttp.getInstance().setBaseUrl(outUrl);
//                url = url.replace(inUrl, "https://cbt.shanghaiwater.com:7901/CustomerService/");
//                EasyHttp.getInstance().setBaseUrl("https://cbt.shanghaiwater.com:7901/CustomerService/");
                LogUtils.e("----------url1:", url);
            } else {
                url = url.replace(outUrl, inUrl);
                EasyHttp.getInstance().setBaseUrl(inUrl);
                LogUtils.e("----------url2:", url);
            }
            Request newRequest = request.newBuilder().url(url).build();
            tryCount++;
            //这里在为空的response的时候进行请求
            LogUtils.e("----------tryCount:", tryCount);
            if (tryCount >= mRetryTimes) {
                responsBean.setResponse(chain.proceed(newRequest));
            } else {
                responsBean = doRequest(chain, newRequest);
            }
        }
        if (responsBean.getResponse() == null) {
            if (responsBean.getIoException() == null) {
                responsBean.setIoException(new IOException());
            }
            throw responsBean.getIoException();
        }
        return responsBean.getResponse();
    }

    private ResponsBean doRequest(Chain chain, Request request) throws IOException {
        LogUtils.e("------headers", request.headers().toString());
        ResponsBean responsBean = new ResponsBean();
        Response response = null;
        try {
            response = chain.proceed(request);
            responsBean.setResponse(response);
        } catch (IOException e) {
            responsBean.setIoException(e);
            LogUtils.e("onErrorssssss", e.toString() + "------");
        }
        return responsBean;
    }
}
