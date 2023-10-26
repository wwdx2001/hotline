package com.sh3h.hotline.ui.order.self.detail;

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
 * Created by dengzhimin on 2016/9/21.
 */

public class CreateSelfOrderDetailPresenter extends ParentPresenter<CreateSelfOrderDetailMvpView> {

    public static final String TAG = "CreateSelfOrderDetailPresenter";

    @Inject
    public CreateSelfOrderDetailPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void cancelEasyhttpRequest() {

    }

    public void loadTaskHandle(String taskId) {
        mSubscription.add(mDataManager.loadTaskHandle(taskId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUHistoryTask>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "loadTaskHandle onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, "loadTaskHandle onError ".concat(e.getMessage()));
                    }

                    @Override
                    public void onNext(List<DUHistoryTask> duHistoryTaskList) {
                        LogUtil.i(TAG, "loadTaskHandle onNext");
                        getMvpView().onGetTaskHandle(duHistoryTaskList);
                    }
                }));
    }
}
