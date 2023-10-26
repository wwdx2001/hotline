package com.sh3h.hotline.ui.bill.detail.recentbill.detail;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.response.DURecentBillDetail;
import com.sh3h.dataprovider.data.entity.base.DUEntityResult;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.sh3h.mobileutil.util.LogUtil;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dengzhimin on 2016/9/26.
 */

public class RecentBillInformationDetailPresenter extends ParentPresenter<RecentBillInformationDetailMvpView> {

    public static final String TAG = "RecentBillInformationDetailPresenter";

    @Inject
    public RecentBillInformationDetailPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void cancelEasyhttpRequest() {

    }

    public void initData(String cardId, int feeId){
        mSubscription.add(mDataManager.getRecentBillDetail(cardId, feeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DUEntityResult<DURecentBillDetail>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, "onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(DUEntityResult<DURecentBillDetail> duRecentBillDetailDUEntityResult) {
                        LogUtil.i(TAG, "onNext");
                        if(duRecentBillDetailDUEntityResult.getStatusCode() != Constant.STATUS_CODE_200){
                            getMvpView().showToast(duRecentBillDetailDUEntityResult.getMessage());
                        } else {
                            getMvpView().initData(duRecentBillDetailDUEntityResult.getData());
                        }
                    }
                }));

    }
}
