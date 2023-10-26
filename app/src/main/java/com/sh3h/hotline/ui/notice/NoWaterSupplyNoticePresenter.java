package com.sh3h.hotline.ui.notice;

import android.content.Context;

import com.blankj.utilcode.util.ActivityUtils;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.response.WaterStopNotificationEntity;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.NetworkUtil;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Created by zhangjing on 2016/9/18.
 */
public class NoWaterSupplyNoticePresenter extends ParentPresenter<NoWaterSupplyNoticeMvpView> {

    private final static String TAG = "NoWaterSupplyNoticePresenter";
    private Disposable mDisposable1;

    @Inject
    public NoWaterSupplyNoticePresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void cancelEasyhttpRequest() {
        EasyHttp.cancelSubscription(mDisposable1);
    }

    /**
     * 刷新数据
     */
    public void refreshData(final boolean isFromRefresh) {
//        if (!NetworkUtil.isNetworkConnected((Context) getMvpView())) {
//            getMvpView().showMessage(ActivityUtils.getTopActivity().getResources().getString(R.string.toast_network_is_not_connect));
//            return;
//        }

        mDisposable1 = EasyHttp.get(URL.WaterShutdownAnnouncementQuery)
                .params("cmQueryMsg", "Y")
                .execute(new CallBackProxy<CustomApiResult<List<WaterStopNotificationEntity>>,
                        List<WaterStopNotificationEntity>>(new CustomCallBack<List<WaterStopNotificationEntity>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (getMvpView() != null)
                            getMvpView().showMessage(e.getMessage());
                    }

                    @Override
                    public void onSuccess(List<WaterStopNotificationEntity> waterStopNotificationEntities) {
                        if (getMvpView() != null)
                            getMvpView().onGetRefreshData(waterStopNotificationEntities, isFromRefresh);
                    }
                }) {
                });

//                .

//        mSubscription.add(mDataManager.getNews()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<List<DUNews>>() {
//                    @Override
//                    public void onCompleted() {
//                        LogUtil.i(TAG, "onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LogUtil.i(TAG, e.getMessage());
//                        getMvpView().showMessage(Integer.valueOf(e.getMessage()));
//                    }
//
//                    @Override
//                    public void onNext(List<DUNews> duNewsList) {
//                        LogUtil.i(TAG, "onNext");
//                        getMvpView().onGetRefreshData(duNewsList, isFromRefresh);
//                    }
//                }));
    }

    /**
     * 停水公告实时查询
     */
    public void searchNews(final boolean isFromRefresh, String title, String content, String type, long startTime, long endTime) {
        if (!NetworkUtil.isNetworkConnected((Context) getMvpView())) {
            getMvpView().showMessage(ActivityUtils.getTopActivity().getResources().getString(R.string.toast_network_is_not_connect));
            return;
        }

//        mSubscription.add(mDataManager.searchNews(title, content, type, startTime, endTime)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<List<DUNews>>() {
//                    @Override
//                    public void onCompleted() {
//                        LogUtil.i(TAG, "onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LogUtil.i(TAG, e.getMessage());
//                        getMvpView().showMessage(Integer.valueOf(e.getMessage()));
//                    }
//
//                    @Override
//                    public void onNext(List<DUNews> duNewsList) {
//                        LogUtil.i(TAG, "onNext");
//                        getMvpView().onGetRefreshData(duNewsList, isFromRefresh);
//                    }
//                }));
    }
}
