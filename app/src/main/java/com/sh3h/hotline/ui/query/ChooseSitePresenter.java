package com.sh3h.hotline.ui.query;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.response.DUWord;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.sh3h.mobileutil.util.LogUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 选择站点
 * Created by BJB147 on 2018/4/16.
 */
public class ChooseSitePresenter extends ParentPresenter<ChooseSiteMvpView> {

    private static final String TAG = "ChooseSitePresenter";

    @Inject
    public ChooseSitePresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void cancelEasyhttpRequest() {

    }

    /**
     * 获取所有的站点
     */
    public void getAllStations() {
        mSubscription.add(mDataManager.getAllStations()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUWord>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, e.getMessage());
                        getMvpView().onShowMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(List<DUWord> duWords) {
                        LogUtil.i(TAG, "onNext");
                        getMvpView().onGetSites(duWords);
                    }
                }));
    }
}
