package com.sh3h.hotline.ui.order.self.detail.receipt;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.request.DUCreateSelfOrder;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.sh3h.mobileutil.util.LogUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dengzhimin on 2016/9/18.
 */
public class ReceiptDetailPresenter extends ParentPresenter<ReceiptDetailMvpView> {
    public static final String TAG = "ReceiptDetailPresenter";

    @Inject
    public ReceiptDetailPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void cancelEasyhttpRequest() {

    }

    public void initData(String taskId) {
        mSubscription.add(mDataManager.getCreateSelfOrderByTaskId(taskId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DUCreateSelfOrder>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, "onError".concat(e.getMessage()));
                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(DUCreateSelfOrder duCreateSelfOrder) {
                        LogUtil.i(TAG, "onNext");
                        if (duCreateSelfOrder == null) {
                            getMvpView().onError(R.string.toast_taskid_is_error);
                        } else {
                            getMvpView().onInitData(duCreateSelfOrder);
                        }

                    }
                }));

    }
}
