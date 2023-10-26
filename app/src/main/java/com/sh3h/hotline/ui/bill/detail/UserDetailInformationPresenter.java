package com.sh3h.hotline.ui.bill.detail;

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

public class UserDetailInformationPresenter extends ParentPresenter<UserDetailInformationMvpView> {

    public static final String TAG = "UserDetailInformationPresenter";
    private Disposable mDisposable1;

    @Inject
    public UserDetailInformationPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void cancelEasyhttpRequest() {
        EasyHttp.cancelSubscription(mDisposable1);
    }

    public void getCustomerTotalArrears(String cardId) {
        mDisposable1 = EasyHttp
                .post(URL.TotalArrearsQuery)
                .params("acctId", cardId)
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
                            getMvpView().hindProgressDialog();
                        }
                    }

                    @Override
                    public void onSuccess(CustomerTotalArrearsResult entity) {
                        if (getMvpView() != null) {
                            getMvpView().hindProgressDialog();
                            if (entity != null) {
                                LogUtils.i(entity.toString());
                                getMvpView().onCustomerArrears(entity);
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
