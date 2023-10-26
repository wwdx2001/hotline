package com.sh3h.hotline.service.handler;


import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.service.SyncMessage;
import com.sh3h.hotline.service.thread.WorkerThread;
import com.sh3h.mobileutil.util.LogUtil;
import com.squareup.otto.Bus;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import rx.Subscription;

public class BaseHandler implements IHandler {
    private static final String TAG = "BaseHandler";

    private static ExecutorService mExecutor = null;
    private BlockingQueue<SyncMessage> mBlockingQueue;
    private WorkerThread mWorkerThread;

    protected DataManager mDataManager;
    protected MainApplication mApplication;
    protected EventPosterHelper mEventPosterHelper;
    protected Bus mBus;
    protected Subscription mSubscription;

    public BaseHandler(MainApplication mainApplication,
                       DataManager dataManager,
                       ConfigHelper configHelper,
                       EventPosterHelper eventPosterHelper,
                       Bus bus) {
        mBlockingQueue = null;
        mWorkerThread = null;
        mApplication = mainApplication;
        mDataManager = dataManager;
        mEventPosterHelper = eventPosterHelper;
        mBus = bus;
    }

    @Override
    public void start() {
        LogUtil.i(TAG, "start");
        if (mExecutor == null) {
            mExecutor = Executors.newCachedThreadPool();
        }

        if (mBlockingQueue == null) {
            mBlockingQueue = new LinkedBlockingQueue<SyncMessage>();
            LogUtil.i(TAG, "create a blocking queue");
        }

        mWorkerThread = new WorkerThread(this, mBlockingQueue);
        mExecutor.execute(mWorkerThread);
        LogUtil.i(TAG, "create a thread");
    }

    @Override
    public final void put(SyncMessage syncMessage) {
        if (mBlockingQueue == null) {
            LogUtil.e(TAG, "blocking queue is null");
            return;
        }
        try {
            mBlockingQueue.put(syncMessage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean process(SyncMessage syncMessage) {
        if (syncMessage == null) {
            return false;
        }
        LogUtil.i(TAG, "process " + syncMessage.getSyncType());
        return true;
    }

    @Override
    public void stop() {
        if (mWorkerThread != null) {
            mWorkerThread.stop();
        }
        if(mBlockingQueue != null && mBlockingQueue.size() > 0){
            mBlockingQueue.clear();
        }
    }

    @Override
    public final void shutdown() {
        if (mExecutor != null && !mExecutor.isShutdown()) {
            mExecutor.shutdown();
        }
        mExecutor = null;
    }

}
