package com.sh3h.hotline.ui.bill;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.exception.ApiException;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Created by dengzhimin on 2016/9/14.
 */
public class QueryBillPresenter extends ParentPresenter<QueryBillMvpView> {

    private Disposable mDisposable1;

    @Inject
    public QueryBillPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void cancelEasyhttpRequest() {
        EasyHttp.cancelSubscription(mDisposable1);
    }

    void getKehuInfo(String kehuBH) {
        mDisposable1 = EasyHttp
                .post(URL.CustomerInfoQuery)
                .params("acctId", kehuBH)
                .execute(new CallBackProxy<CustomApiResult<CustomerInfoFindResult>,
                        CustomerInfoFindResult>(new CustomCallBack<CustomerInfoFindResult>() {

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
                            getMvpView().hindProgressDialog();
                        }
                    }

                    @Override
                    public void onSuccess(CustomerInfoFindResult entity) {
                        if (getMvpView() != null) {
                            getMvpView().hindProgressDialog();
                            if (entity != null) {
                                LogUtils.i(entity.toString());
                                getMvpView().onFindKehuInfo(entity);
                            } else {
                                ToastUtils.showShort("请求失败");
                            }
                        }
                    }

                    @Override
                    public void onCompleted() {
                        if (getMvpView() != null) {
                            getMvpView().hindProgressDialog();
                        }
                    }
                }) {
                });

    }
}
