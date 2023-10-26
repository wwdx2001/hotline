package com.sh3h.hotline.ui.query;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.response.DUWord;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.sh3h.mobileutil.util.LogUtil;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.sh3h.dataprovider.util.Constant.ISSUE_AREA;
import static com.sh3h.dataprovider.util.Constant.ISSUE_ORIGIN;
import static com.sh3h.dataprovider.util.Constant.ISSUE_TYPE;

/**
 * Created by zhangjing on 2016/9/18.
 */
public class QueryOrderPresenter extends ParentPresenter<QueryOrderMvpView> {

    private static final String TAG = "QueryOrderPresenter";


    @Inject
    public QueryOrderPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void cancelEasyhttpRequest() {

    }

    /**
     * 反映来源
     */
    public void initIssueOriginSpinner() {
        mSubscription.add(mDataManager.getFirstWords(ISSUE_ORIGIN)
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
                    }

                    @Override
                    public void onNext(List<DUWord> duWords) {
                        getMvpView().onInitIssueOriginSpinner(duWords);
                    }
                })
        );
    }

    /**
     * 反映类型
     */
    public void initFanYingLeiXingSpinner() {
        mSubscription.add(mDataManager.getFirstWords(ISSUE_TYPE)
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
                    }

                    @Override
                    public void onNext(List<DUWord> duWords) {
                        getMvpView().onInitFanYingLeiXingSpinner(duWords);
                    }
                })
        );
    }

    /**
     * 反映内容
     *
     * @param valueEx
     */
    public void initFanYingNeiRongSpinner(String valueEx) {
        mSubscription.add(mDataManager.getWords(valueEx)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Map<String, List<DUWord>>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, "onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Map<String, List<DUWord>> stringListMap) {
                        getMvpView().onInitFanYingNeiRongSpinner(stringListMap.get(Constant.ISSUE_CONTENT));
                    }
                })
        );
    }

    /**
     * 反映区名
     */
    public void initFanYingArea() {
        mSubscription.add(mDataManager.getFirstWords(ISSUE_AREA)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUWord>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, "onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(List<DUWord> duWords) {
                        getMvpView().onInitFanYingQuMingSpinner(duWords);
                    }
                }));
    }

    public void getHotlineStation(final boolean isLogin) {
        mSubscription.add(mDataManager.getHotlineStation()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<DUWord>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, "onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(DUWord duWord) {
                        getMvpView().onInitStation(duWord,isLogin);
                    }
                }));
    }
}
