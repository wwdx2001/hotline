package com.sh3h.hotline.ui.order.self.history;

import android.app.Dialog;
import android.app.ProgressDialog;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.newentity.ZikaidanJLEntity;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.sh3h.mobileutil.util.LogUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.subsciber.IProgressDialog;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dengzhimin on 2016/9/14.
 */
public class CreateSelfOrderHistoryPresenter extends ParentPresenter<CreateSelfOrderHistoryMvpView> {
    public static final String TAG = "CreateSelfOrderHistoryPresenter";
    private Disposable mDisposable1;

    private DataManager dataManager;

    @Inject
    public CreateSelfOrderHistoryPresenter(DataManager dataManager) {
        super(dataManager);
        this.dataManager = dataManager;
    }

    @Override
    public void cancelEasyhttpRequest() {
        EasyHttp.cancelSubscription(mDisposable1);
    }

    /**
     * load data
     */
    public void loadData(int offset, int count) {
        LogUtil.i(TAG, "loadData");
        IProgressDialog iProgressDialog = new IProgressDialog() {
            @Override
            public Dialog getDialog() {
                ProgressDialog dialog = new ProgressDialog(ActivityUtils.getTopActivity());
                dialog.setMessage("请稍候...");
                return dialog;
            }
        };
        mDisposable1 = EasyHttp.get(URL.SelfOpeningOrderQuery)
                .params("userId", SPUtils.getInstance().getString(Constant.USERID))
                .execute(new CallBackProxy<CustomApiResult<List<ZikaidanJLEntity>>, List<ZikaidanJLEntity>>(new CustomCallBack<List<ZikaidanJLEntity>>() {
                    @Override
                    public void onSuccess(List<ZikaidanJLEntity> zikaidanJLEntity) {
                        if (getMvpView() != null)
                            getMvpView().onLoadDataSuccess(zikaidanJLEntity);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (getMvpView() != null)
//                            getMvpView().onError(R.string.refresh_fail);
                            getMvpView().onError(e.getMessage());
                    }
                }) {
                });
//        mSubscription.add(mDataManager.getCreateSelfOrderHistory(offset, count)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<List<DUHistoryTask>>() {
//                    @Override
//                    public void onCompleted() {
//                        LogUtil.i(TAG, "loadData onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LogUtil.i(TAG, "loadData onError ".concat(e.getMessage()));
//                        getMvpView().onError(R.string.toast_create_self_order_load_error);
//                    }
//
//                    @Override
//                    public void onNext(List<DUHistoryTask> duHistoryTasks) {
//                        LogUtil.i(TAG, "loadData onNext");
//                        getMvpView().onLoadData(duHistoryTasks);
//                    }
//                }));

    }

    /**
     * load more data
     */
    public void loadMoreData(int offset, int count) {
        //do load more
        mSubscription.add(mDataManager.getCreateSelfOrderHistory(offset, count)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUHistoryTask>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, "onError".concat(e.getMessage()));
                    }

                    @Override
                    public void onNext(List<DUHistoryTask> duHistoryTasks) {
                        getMvpView().onLoadMoreData(duHistoryTasks);
                    }
                }));

    }

}
