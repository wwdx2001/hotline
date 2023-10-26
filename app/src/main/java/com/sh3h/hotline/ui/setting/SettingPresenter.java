package com.sh3h.hotline.ui.setting;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.sh3h.mobileutil.util.LogUtil;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhangjing on 2017/2/7.
 */

public class SettingPresenter extends ParentPresenter<SettingMvpView> {
    private static final String TAG = "SettingPresenter";

    @Inject
    public SettingPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void cancelEasyhttpRequest() {

    }

    public void clearHistory() {
        mSubscription.add(mDataManager.clearHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, " onCompleted");
                        getMvpView().clearHistoryEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, "clearHistory onError:" + e.getMessage());
                        getMvpView().onError(R.string.text_clear_history_failure);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "clearHistory onNext");
                    }
                }));
    }

    public void uploadLogFiles() {
        LogUtil.i(TAG, "uploadLogFiles");
        mDataManager.uploadLogFiles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "uploadLogFiles onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "uploadLogFiles onError " + e.getMessage());
                        getMvpView().onError(R.string.text_upload_log_failure);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "uploadLogFiles onNext " + aBoolean);
                        getMvpView().onUploadLogFiles(aBoolean);
                    }
                });
    }
}
