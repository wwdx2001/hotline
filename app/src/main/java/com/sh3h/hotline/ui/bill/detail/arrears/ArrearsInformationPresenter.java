package com.sh3h.hotline.ui.bill.detail.arrears;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.response.CustomerTotalArrearsResult;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.exception.ApiException;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Created by dengzhimin on 2016/9/22.
 */

public class ArrearsInformationPresenter extends ParentPresenter<ArrearsInformationMvpView> {

    public static final String TAG = "ArrearsInformationPresenter";
    private Disposable mDisposable1;

    @Inject
    public ArrearsInformationPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void cancelEasyhttpRequest() {
        EasyHttp.cancelSubscription(mDisposable1);
    }

    public void getArrearsList(String acctId) {
        mDisposable1 = EasyHttp
                .post(URL.TotalArrearsQuery)
                .params("acctId", acctId)
                .execute(new CallBackProxy<CustomApiResult<CustomerTotalArrearsResult>,
                        CustomerTotalArrearsResult>(new CustomCallBack<CustomerTotalArrearsResult>() {

                    @Override
                    public void onStart() {
                        if (getMvpView() != null) {
                            getMvpView().showProgressDialog("加载中");
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (getMvpView() != null) {
                            getMvpView().onError(e);
                            getMvpView().hideProgressDialog();
                        }
                    }

                    @Override
                    public void onSuccess(CustomerTotalArrearsResult entity) {
                        if (getMvpView() != null) {
                            getMvpView().hideProgressDialog();
                            if (entity != null) {
                                LogUtils.i(entity.toString());
                                getMvpView().onArrearsList(entity);
                            } else {
                                ToastUtils.showShort("请求失败");
                            }
                        }
                    }

                    @Override
                    public void onCompleted() {
                        if (getMvpView() != null) {
                            getMvpView().hideProgressDialog();
                        }
                    }
                }) {
                });

    }
}
