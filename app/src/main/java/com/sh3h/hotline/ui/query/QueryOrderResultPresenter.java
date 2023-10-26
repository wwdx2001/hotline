package com.sh3h.hotline.ui.query;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.base.DUEntitiesResult;
import com.sh3h.dataprovider.data.entity.response.DUOrder;
import com.sh3h.dataprovider.data.entity.response.DUProcess;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.sh3h.mobileutil.util.LogUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhangjing on 2016/9/20.
 */
public class QueryOrderResultPresenter extends ParentPresenter<QueryOrderResultMvpView> {

    private static final String TAG = "QueryOrderResultPresenter";

    @Inject
    public QueryOrderResultPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void cancelEasyhttpRequest() {

    }

    /**
     * 查询工单
     *
     * @param taskId
     * @param name
     * @param address
     * @param telephone
     */
    public void queryOrder(String taskId, String name, String address, String telephone,
                           String issueOrigin, String issueType, String issueContent,
                           String loginStation, String admissibleSite, String issueArea,
                           long startDateUtc, long endDateUtc) {
        mSubscription.add(mDataManager.searchOrder(taskId, name, address, telephone,
                issueOrigin, issueType, issueContent, loginStation, admissibleSite,
                issueArea, startDateUtc, endDateUtc)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DUEntitiesResult<DUOrder>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "queryOrder onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, "queryOrder onError:" + e.getMessage());
                        getMvpView().showMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(DUEntitiesResult<DUOrder> duOrderDUEntitiesResult) {
                        LogUtil.i(TAG, "queryOrder onNext");
                        if (duOrderDUEntitiesResult.getStatusCode() != Constant.STATUS_CODE_200
                                && duOrderDUEntitiesResult.getMessage() != null) {
                            getMvpView().showMessage(duOrderDUEntitiesResult.getMessage());
                            return;
                        }
                        if (duOrderDUEntitiesResult.getData() == null ||
                                duOrderDUEntitiesResult.getData().size() == 0) {
                            getMvpView().showMessage("无结果");
                            return;
                        }
                        getMvpView().onGetDUOrder(duOrderDUEntitiesResult.getData());
                    }
                }));
    }

    /**
     * 获取工单流程信息
     *
     * @param order
     */
    public void getOrderProcessInfo(final DUOrder order) {
        mSubscription.add(mDataManager.getOrderProcessInfo(order.getTaskId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<DUProcess>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "getOrderProcessInfo onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, "getOrderProcessInfo onError");
                        getMvpView().onLoadOrderProcessError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<DUProcess> duProcesses) {
                        LogUtil.i(TAG, "getOrderProcessInfo onNext");
                        getMvpView().onLoadOrderProcessSuccess(order, duProcesses);
                    }
                }));

    }
}
