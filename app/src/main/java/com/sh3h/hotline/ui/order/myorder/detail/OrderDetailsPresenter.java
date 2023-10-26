package com.sh3h.hotline.ui.order.myorder.detail;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.base.DUEntitiesResult;
import com.sh3h.dataprovider.data.entity.response.DUBillBaseInfo;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.sh3h.localprovider.greendaoEntity.Task;
import com.sh3h.mobileutil.util.LogUtil;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhangjing on 2016/9/12.
 */
public class OrderDetailsPresenter extends ParentPresenter<OrderDetailsMvpView> {

    private final static String TAG = "OrderDetailsPresenter";

    @Inject
    public OrderDetailsPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void cancelEasyhttpRequest() {

    }

    /**
     * 根据taskId查找Task
     *
     * @param taskId
     */
    public void queryTaskByTaskId(String taskId) {
        mSubscription.add(mDataManager.queryTaskByTaskId(taskId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<DUMyTask>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, e.getMessage());
                        getMvpView().showMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(DUMyTask task) {
                        LogUtil.i(TAG, "onNext");
                        getMvpView().onGetTaskInfo(task);
                    }
                }));
    }

    /**
     * 账单查询
     *
     * @param xiaogenhao
     */
    public void searchBill(String xiaogenhao) {
        mSubscription.add(mDataManager.searchBill(xiaogenhao, null, null, null, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DUEntitiesResult<DUBillBaseInfo>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, "onError:" + e.getMessage());
                        getMvpView().showMessage(R.string.toast_load_account_error);
                    }

                    @Override
                    public void onNext(DUEntitiesResult<DUBillBaseInfo> result) {
                        LogUtil.i(TAG, "onNext");
                        if (result.getStatusCode() != Constant.STATUS_CODE_200) {
                            getMvpView().showMessage(result.getMessage());
                        } else {
                            if (result.getData().size() <= 0) {
                                getMvpView().showMessage(R.string.toast_cardid_error);
                            } else {
                                getMvpView().onIntent(result.getData().get(0));
                            }
                        }
                    }
                }));
    }
}
