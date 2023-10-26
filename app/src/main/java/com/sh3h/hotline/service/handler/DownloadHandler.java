package com.sh3h.hotline.service.handler;


import android.widget.Toast;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.base.DUEntitiesResult;
import com.sh3h.dataprovider.data.entity.response.DUWord;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.service.SyncMessage;
import com.sh3h.hotline.service.SyncType;
import com.sh3h.hotline.ui.order.TaskState;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.hotline.MainApplication;
import com.sh3h.mobileutil.util.TextUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DownloadHandler extends BaseHandler {

    private static final String TAG = "DownloadHandler";

    public DownloadHandler(MainApplication mainApplication,
                           DataManager dataManager,
                           ConfigHelper configHelper,
                           EventPosterHelper eventPosterHelper,
                           Bus bus) {
        super(mainApplication, dataManager, configHelper, eventPosterHelper, bus);
        mBus.register(this);
    }

    @Override
    public boolean process(SyncMessage syncMessage) {
        if (!super.process(syncMessage)) {
            return false;
        }

        switch (syncMessage.getSyncType()) {
            case DOWNLOAD_ALL_TASK:
                downloadAllTasks(syncMessage);
                break;
            case DOWNLOAD_WORDS:
                downloadWords(syncMessage);
                break;
            default:
                LogUtil.e(TAG, "process is error");
                break;
        }

        return true;
    }

    @Override
    public final void stop() {
        super.stop();
        mBus.unregister(this);
    }

    /**
     * 下载任务
     *
     * @param syncMessage
     */
    private void downloadAllTasks(SyncMessage syncMessage) {
        LogUtil.i(TAG, "downloadAllTasks");
        if (syncMessage == null) {
            return;
        }

        mSubscription = mDataManager.downloadMyTasks()
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "downloadAllTasks onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, "downloadAllTasks onError " + e.getMessage());
                        postEvent(UIBusEvent.DownloadResult.Type.TASK, false, e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "onNext " + aBoolean);
                        postEvent(UIBusEvent.DownloadResult.Type.TASK, true, null);
                    }
                });
    }

    /**
     * 下载词语信息
     *
     * @param syncMessage
     */
    private void downloadWords(SyncMessage syncMessage) {
        LogUtil.i(TAG, "downloadWords");
        mSubscription = mDataManager.downloadWords("all")
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "downloadWords onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, "downloadWords onError" + e.getMessage());
                        postEvent(UIBusEvent.DownloadResult.Type.WORD, false, e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "downloadWords onNext");
                        postEvent(UIBusEvent.DownloadResult.Type.WORD, true, null);
                    }
                });
    }

    private void postEvent(UIBusEvent.DownloadResult.Type type,
                           boolean isSuccess,
                           String error) {
        mEventPosterHelper.postEventSafely(new UIBusEvent.DownloadResult(type, isSuccess, error));
    }

    @Subscribe
    public void downloadMyTasks(UIBusEvent.NotifyDownloadTasks notifyDownloadTasks) {
        if (notifyDownloadTasks == null
                || notifyDownloadTasks.getSyncMessage() == null) {
            return;
        }
        SyncMessage syncMessage = notifyDownloadTasks.getSyncMessage();
        this.put(new SyncMessage(syncMessage.getUserId(), SyncType.DOWNLOAD_ALL_TASK));
    }
}
