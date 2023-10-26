package com.sh3h.dataprovider.http;

import com.sh3h.dataprovider.util.AppConstant;
import com.sh3h.dataprovider.util.ComParamContact;
import com.sh3h.dataprovider.util.MD5;
import com.sh3h.dataprovider.util.TokenManager;
import com.zhouyou.http.interceptor.BaseDynamicInterceptor;
import com.zhouyou.http.utils.HttpLog;

import java.util.Map;
import java.util.TreeMap;

/**
 * <p>描述：对参数进行签名、添加token、时间戳处理的拦截器</p>
 * 主要功能说明：<br>
 * 因为参数签名没办法统一，签名的规则不一样，签名加密的方式也不同有MD5、BASE64等等，只提供自己能够扩展的能力。<br>
 * 作者： zhouyou<br>
 * 日期： 2017/5/4 15:21 <br>
 * 版本： v1.0<br>
 */
public class CustomSignInterceptor extends BaseDynamicInterceptor<CustomSignInterceptor> {
    @Override
    public TreeMap<String, String> dynamic(TreeMap<String, String> dynamicMap) {
        //dynamicMap:是原有的全局参数+局部参数
        //你不必关心当前是get/post/上传文件/混合上传等，库中会自动帮你处理。
        //根据需要自己处理，如果你只用到token则不必处理isTimeStamp()、isSign()
        if (isTimeStamp()) {//是否添加时间戳，因为你的字段key可能不是timestamp,这种动态的自己处理
            dynamicMap.put(ComParamContact.Common.TIMESTAMP, String.valueOf(System.currentTimeMillis()));
        }
        if (isSign()) {
            // 是否签名
            //1.因为你的字段key可能不是sign，这种需要动态的自己处理
            //2.因为你的签名的规则不一样，签名加密方式也不一样，只提供自己能够扩展的能力
            dynamicMap.put(ComParamContact.Common.SIGN, sign(dynamicMap));
        }
        if (isAccessToken()) {//是否添加token
            String acccess = TokenManager.getInstance().getAuthModel().getAccessToken();
            dynamicMap.put(ComParamContact.Common.ACCESSTOKEN, acccess);
        }
        //Logc.i("dynamicMap:" + dynamicMap.toString());
        return dynamicMap;//dynamicMap:是原有的全局参数+局部参数+新增的动态参数
    }

    //示例->签名规则：POST+url+参数的拼装+secret
    private String sign(TreeMap<String, String> dynamicMap) {
        String url = getHttpUrl().url().toString();
        url = url.replaceAll("%2F", "/");
        StringBuilder sb = new StringBuilder("POST");
        sb.append(url);
        for (Map.Entry<String, String> entry : dynamicMap.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        sb.append(AppConstant.APP_SECRET);
        HttpLog.i(sb.toString());
        return MD5.encode(sb.toString());
    }
}
