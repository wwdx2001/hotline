package com.sh3h.dataprovider.http;

import com.blankj.utilcode.util.ToastUtils;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.exception.ApiException;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/4/2 16:28
 */
public abstract class CustomCallBack<T> extends CallBack<T> {
    public CustomCallBack() {
    }

    public void onStart() {
    }

    public void onCompleted() {
    }

    @Override
    public void onError(ApiException e) {
        onCompleted();
        if (e.getCode() == 1010) {
            ToastUtils.showShort("没有数据");
        } else {
            ToastUtils.showShort(e.getMessage());
        }
    }

    public void onError(ApiException e, boolean isNull) {
        onCompleted();
        if (e.getCode() == 1010) {
            if (isNull) {
                ToastUtils.showShort("没有数据");
            }
        } else {
            ToastUtils.showShort(e.getMessage());
        }
    }
}
