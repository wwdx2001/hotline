package com.sh3h.hotline.ui.order.myorder.handle;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.response.DUWord;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.sh3h.mobileutil.util.LogUtil;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhangjing on 2016/10/19.
 */

public class HandleOrderFragmentPresenter extends ParentPresenter<HandleOrderFragmentMvpView> {

    private final static String TAG = "HandleOrderFragmentPresenter";

    @Inject
    public HandleOrderFragmentPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void cancelEasyhttpRequest() {

    }

    /**
     * 查询处理类别
     */
    public void queryHandleType() {
        mSubscription.add(mDataManager.getFirstWords(Constant.HANDLE_TYPE)
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
                        getMvpView().onGetHandleTypeWords(duWords);
                    }
                })
        );
    }

    /**
     * 处理结果
     */
    public void queryHandleResult() {
        mSubscription.add(mDataManager.getFirstWords(Constant.HANDLE_RESULT)
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
                        getMvpView().onGetHandleResultWords(duWords);
                    }
                }));
    }


    /**
     * 查询处理内容(根据parentId)
     */
    public void getWords(String valueEx) {
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
                        LogUtil.i(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(Map<String, List<DUWord>> stringListMap) {
                        getMvpView().onGetWord(stringListMap);
                    }
                })
        );
    }

}
