package com.sh3h.hotline.ui.bill.detail.zuijingd;

import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.hotline.entity.ZuiJinHBGDEntity;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.exception.ApiException;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import rx.subscriptions.CompositeSubscription;

class ZuijinGDPresenter extends ParentPresenter<ZuijinGDMvpView> {
    protected final DataManager mDataManager;
    protected CompositeSubscription mSubscription;
    private Disposable mDisposable1;

    @Inject
    public ZuijinGDPresenter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }

    @Override
    public void cancelEasyhttpRequest() {
        EasyHttp.cancelSubscription(mDisposable1);
    }

    public void getZuiJinGD(String acctId) {
//        List<ZuiJinHBGDEntity> entityList = new ArrayList<>();
//        ZuiJinHBGDEntity entity = new ZuiJinHBGDEntity();
//        entity.setAcctId("12345");
//        entity.setFaCompDttm("1123456");
//        entity.setFaId("123456");
//        entity.setFaTypeDesc("12345");
//        entity.setInstallRead(1234);
//        entity.setNewBadgeNum("12345");
//        entity.setSpId("12345");
//        entity.setUninstallBadgeNum("1234");
//        entity.setUninstallRead(12345);
//        entityList.add(entity);
//        getMvpView().onZuijinGD(entityList);

        mDisposable1 = EasyHttp
                .post(URL.ZuiJinHBGDQuery)
                .params("acctId", acctId)
                .execute(new CallBackProxy<CustomApiResult<List<ZuiJinHBGDEntity>>, List<ZuiJinHBGDEntity>>(
                        new CustomCallBack<List<ZuiJinHBGDEntity>>() {
                            @Override
                            public void onStart() {
                                super.onStart();
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
                            public void onSuccess(List<ZuiJinHBGDEntity> zuiJinHBGDEntities) {
                                if (getMvpView() != null) {
                                    getMvpView().hideProgressDialog();
                                    if (zuiJinHBGDEntities != null) {
                                        getMvpView().onZuijinGD(zuiJinHBGDEntities);
                                    } else {
                                        getMvpView().onCompleted("请求失败");
                                    }
                                }
                            }

                            @Override
                            public void onCompleted() {
                                super.onCompleted();
                                if (getMvpView() != null) {
                                    getMvpView().hideProgressDialog();
                                    getMvpView().onCompleted("");
                                }
                            }
                        }) {
                });
    }
}
