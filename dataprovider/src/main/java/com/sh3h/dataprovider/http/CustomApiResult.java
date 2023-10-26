package com.sh3h.dataprovider.http;

import com.zhouyou.http.model.ApiResult;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/4/2 13:07
 */
public class CustomApiResult<T> extends ApiResult<T> {
    int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


    @Override
    public boolean isOk() {
        return state == 0;
    }
}
