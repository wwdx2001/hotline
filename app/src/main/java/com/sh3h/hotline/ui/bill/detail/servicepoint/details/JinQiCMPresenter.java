package com.sh3h.hotline.ui.bill.detail.servicepoint.details;

import com.blankj.utilcode.util.ToastUtils;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.hotline.entity.JinQiCMEntity;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class JinQiCMPresenter extends ParentPresenter<JinQiCMMvpView> {

    private Disposable mDisposable1;

    @Inject
    public JinQiCMPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void cancelEasyhttpRequest() {
        EasyHttp.cancelSubscription(mDisposable1);
    }

    public void getChaoMa(String spId) {
        // http://128.1.3.40:8080/CustomerService/RecentCodingQuery
        mDisposable1 = EasyHttp.post(URL.RecentCodingQuery)
                .params("spId", spId)
                .execute(new CallBackProxy<CustomApiResult<JinQiCMEntity>, JinQiCMEntity>(
                        new CustomCallBack<JinQiCMEntity>() {

                            @Override
                            public void onStart() {
                                super.onStart();
                                if (getMvpView() != null) {
                                    getMvpView().showAlert("加载中...");
                                }
                            }

                            @Override
                            public void onError(ApiException e) {
                                super.onError(e);
                                if (getMvpView() != null) {
                                    getMvpView().onError(e);
                                    getMvpView().hideAlert();
                                }
                            }

                            @Override
                            public void onSuccess(JinQiCMEntity jinQiCMEntities) {
                                if (getMvpView() != null) {
                                    getMvpView().hideAlert();
                                    if (jinQiCMEntities != null) {
                                        getMvpView().giveDatas(jinQiCMEntities);
                                    } else {
                                        ToastUtils.showShort("请求失败");
                                    }
                                }
                            }

                            @Override
                            public void onCompleted() {
                                super.onCompleted();
                                if (getMvpView() != null) {
                                    getMvpView().hideAlert();
                                }
                            }
                        }) {
                });

    }
}
