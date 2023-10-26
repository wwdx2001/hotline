package com.sh3h.hotline.ui.process;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.sh3h.mobileutil.util.LogUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dengzhimin on 2017/2/23.
 */

public class OrderProcessPresenter extends ParentPresenter<OrderProcessMvpView> {

    public static final String TAG = OrderProcessPresenter.class.getSimpleName();

    @Inject
    public OrderProcessPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void cancelEasyhttpRequest() {

    }

    public void getHistoryTasks(int userId, String taskId){
        mSubscription.add(mDataManager.getHistoryTasks(userId, taskId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUHistoryTask>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "loadMyHistoryTasks onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, e.getMessage());
                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<DUHistoryTask> duHistoryTasks) {
                        LogUtil.i(TAG, "loadMyHistoryTasks onNext");
                        getMvpView().onSetHistoryTasks(duHistoryTasks);
                    }
                }));
    }
}
