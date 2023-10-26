package com.sh3h.hotline.service.thread;

import com.sh3h.hotline.service.SyncMessage;
import com.sh3h.hotline.service.handler.BaseHandler;
import com.sh3h.mobileutil.util.LogUtil;

import java.util.concurrent.BlockingQueue;

/**
 * WorkerThread 工作线程
 * do something: download | upload | query | other
 * Created by dengzhimin on 2016/10/17.
 */

public class WorkerThread implements Runnable {

    private static final String TAG = "WorkerThread";

    private volatile boolean mIsRunning = true;

    private BaseHandler handler;
    private BlockingQueue<SyncMessage> queue;

    public WorkerThread(BaseHandler handler, BlockingQueue<SyncMessage> queue) {
        this.handler = handler;
        this.queue = queue;
    }

    @Override
    public void run() {
        LogUtil.i(TAG, "WorkerThread is running");
        try {
            while (mIsRunning) {
                SyncMessage syncMessage = queue.take();
                handler.process(syncMessage);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            LogUtil.e(TAG, "WorkerThread is interrupted");
        } finally {
            LogUtil.i(TAG, "WorkerThread is finished");
        }
    }

    public void stop() {
        mIsRunning = false;
    }
}
