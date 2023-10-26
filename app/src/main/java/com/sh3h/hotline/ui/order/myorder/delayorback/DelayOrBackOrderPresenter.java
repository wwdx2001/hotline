package com.sh3h.hotline.ui.order.myorder.delayorback;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.request.DUReply;
import com.sh3h.dataprovider.data.local.preference.PreferencesHelper;
import com.sh3h.dataprovider.data.local.preference.UserSession;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.hotline.R;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.sh3h.hotline.ui.order.TaskState;
import com.sh3h.mobileutil.util.LogUtil;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhangjing on 2016/9/12.
 */
public class DelayOrBackOrderPresenter extends ParentPresenter<DelayOrBackOrderMvpView> {
    @Inject
    EventPosterHelper mEventPosterHelper;

    private final static String TAG = "DelayOrBackOrderPresenter";
    private int mUserId;

    @Inject
    public DelayOrBackOrderPresenter(DataManager dataManager, PreferencesHelper preferencesHelper) {
        super(dataManager);
        mUserId = mDataManager.getUserId();
    }

    public int getUserId() {
        return mUserId;
    }

    /**
     * 保存历史工单记录
     *
     * @param duHistoryTask
     */
    public void saveHistoryTask(final DUHistoryTask duHistoryTask) {
        mSubscription.add(mDataManager.saveHistoryTask(duHistoryTask)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
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
                    public void onNext(Boolean aBoolean) {
                        getMvpView().onSaveHistoryTask(aBoolean, duHistoryTask);
                    }
                })
        );
    }

    /**
     * 更新历史工单
     *
     * @param duHistoryTask
     */
    public void updateHistoryTask(final DUHistoryTask duHistoryTask) {
        mSubscription.add(mDataManager.updateHistoryTaskByID(duHistoryTask)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
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
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "onNext");
                        getMvpView().onUpdateHistoryTask(aBoolean, duHistoryTask);
                    }
                }));
    }

    @Override
    public void cancelEasyhttpRequest() {

    }
}
