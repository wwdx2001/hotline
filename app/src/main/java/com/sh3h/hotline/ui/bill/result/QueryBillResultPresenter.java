package com.sh3h.hotline.ui.bill.result;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.data.entity.response.FuzzyAdressQueryResult;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Created by dengzhimin on 2016/9/22.
 */

public class QueryBillResultPresenter extends ParentPresenter<QueryBillResultMvpView> {

    public static final String TAG = "QueryBillResultPresenter";
    private Disposable mDisposable1;
    private Disposable mDisposable2;

    private DataManager dataManager;

    @Inject
    public QueryBillResultPresenter(DataManager dataManager) {
        super(dataManager);
        this.dataManager = dataManager;
    }

    @Override
    public void cancelEasyhttpRequest() {
        EasyHttp.cancelSubscription(mDisposable1);
        EasyHttp.cancelSubscription(mDisposable2);
    }

    public void queryByAddress(String userId, String subAddress, String roadAddress, String alley) {
        HttpParams params = new HttpParams();
        if (!"".equals(userId)) {
            params.put("userId", userId);
        }
        if (!"".equals(subAddress)) {
            params.put("subAddress", subAddress);
        }
        if (!"".equals(roadAddress)) {
            params.put("roadAddress", roadAddress);
        }
        if (!"".equals(alley)) {
            params.put("alley", alley);
        }

        mDisposable1 = EasyHttp
                .post(URL.GetAcctIdByAddress)
                .params(params)
                .execute(new CallBackProxy<CustomApiResult<List<FuzzyAdressQueryResult>>,
                        List<FuzzyAdressQueryResult>>(new CustomCallBack<List<FuzzyAdressQueryResult>>() {

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
                    public void onSuccess(List<FuzzyAdressQueryResult> entity) {
                        if (getMvpView() != null) {
                            getMvpView().hindProgressDialog();
                            if (entity != null) {
                                getMvpView().onQueryByAddress(entity);
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

    void getKehuInfo(String kehuBH) {
        mDisposable2 = EasyHttp
                .post(URL.CustomerInfoQuery)
                .params("acctId", kehuBH)
                .execute(new CallBackProxy<CustomApiResult<CustomerInfoFindResult>,
                        CustomerInfoFindResult>(new SimpleCallBack<CustomerInfoFindResult>() {

                    @Override
                    public void onStart() {
                        if (getMvpView() != null) {
                            getMvpView().showProgressDialog("加载中");
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        if (getMvpView() != null) {
                            getMvpView().onError(e);
                            getMvpView().hindProgressDialog();
                            ToastUtils.showShort(e.getMessage());
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
