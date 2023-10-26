package com.sh3h.hotline.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.request.DUReply;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.preference.PreferencesHelper;
import com.sh3h.dataprovider.data.local.preference.UserSession;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.dataprovider.util.NetworkUtil;
import com.sh3h.hotline.R;
import com.sh3h.hotline.service.factory.AbstractFactory;
import com.sh3h.hotline.service.factory.HandlerFactory;
import com.sh3h.hotline.service.handler.BaseHandler;
import com.sh3h.hotline.service.handler.DownloadHandler;
import com.sh3h.hotline.service.handler.OtherHandler;
import com.sh3h.hotline.service.handler.QueryHandler;
import com.sh3h.hotline.service.handler.UploadHandler;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.util.AndroidComponentUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.squareup.otto.Bus;

import javax.inject.Inject;


public class SyncService extends Service {
    private static final String TAG = "SyncService";

    @Inject
    DataManager mDataManager;

    @Inject
    ConfigHelper mConfigHelper;

    @Inject
    PreferencesHelper mPreferencesHelper;

    @Inject
    EventPosterHelper mEventPosterHelper;

    @Inject
    Bus mBus;

    private DownloadHandler mDownloadHandler;
    private UploadHandler mUploadHandler;
    private QueryHandler mQueryHandler;
    private OtherHandler mOtherHandler;
    private int mUserId;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SyncService.class);
    }

    public static boolean isRunning(Context context) {
        return AndroidComponentUtil.isServiceRunning(context, SyncService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtil.i(TAG, "onCreate" + mDataManager);
        MainApplication.get(this).getComponent().inject(this);

        UserSession userSession = mPreferencesHelper.getUserSession();
        if (userSession != null) {
            mUserId = userSession.getUserId();
        }

        AbstractFactory factory = new HandlerFactory(MainApplication.get(this), mDataManager,
                mConfigHelper, mEventPosterHelper, mBus);
        mDownloadHandler = factory.createDownloadHandler();
        mUploadHandler = factory.createUploadHandler();
        mQueryHandler = factory.createQueryHandler();
        mOtherHandler = factory.createOtherHandler();
        mDownloadHandler.start();
        mUploadHandler.start();
        mQueryHandler.start();
        mOtherHandler.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        if (!NetworkUtil.isNetworkConnected(this)) {
            ApplicationsUtil.showMessage(this, R.string.toast_network_is_not_connect);
            stopSelf(startId);
            LogUtil.i(TAG, "---onStartCommand---network isn't connected");
            return START_NOT_STICKY;
        }

        try {
            Bundle bundle = intent.getExtras();
            int type = bundle.getInt(SyncConst.SYNC_TYPE, 0);
            String taskId = bundle.getString(SyncConst.SYNC_TASK_ID);
            int taskType = bundle.getInt(SyncConst.SYNC_TASK_TYPE);
            int taskState = bundle.getInt(SyncConst.SYNC_TASK_STATE);
            long replyTime = bundle.getLong(SyncConst.SYNC_REPLY_TIME);
            boolean isFromMyTask = bundle.getBoolean(SyncConst.SYNC_IS_FROM_MY_TASK, false);
            SyncType syncType = SyncType.values()[type];
            switch (syncType) {
                case NONE:
                    LogUtil.i(TAG, "onStartCommand none");
                    break;
                case DOWNLOAD_ALL_TASK:
                case DOWNLOAD_WORDS:
                    mDownloadHandler.put(new SyncMessage(mUserId, syncType));
                    break;
                case UPLOAD_ALL_TASK:
                case UPLOAD_ONE_TASK:
                case UPLOAD_ALL_MEDIA:
                case UPLOAD_ALL_MEDIA_OF_ONE_TASK:
                case UPLOAD_ONE_MEDIA_OF_ONE_TASK:
                    LogUtil.i(TAG, "onStartCommand upload");
                    mUploadHandler.put(new SyncMessage(mUserId, syncType, taskId, taskType, taskState, replyTime));
                    break;
                case UPLOAD_TASK_REPLY:
                case UPLOAD_ONE_CREATE_SELF_ORDER:
                    //mUploadHandler.put(new SyncMessage(syncType, bundle.getString(SyncConst.SYNC_TASK_ID)));
                case UPLOAD_All_CREATE_SELF_ORDER:
                    mUploadHandler.put(new SyncMessage(mUserId, syncType, taskId, taskType, taskState, replyTime));
                    break;
                case QUERY_TASK_WITH_CONDITION:
                case QUERY_DETAIL_OF_ONE_TASK:
                    LogUtil.i(TAG, "onStartCommand query");
                    mQueryHandler.put(new SyncMessage(mUserId, syncType));
                    break;
                case UPLOAD_HISTORY_TASKS_OF_ONE_TASK:
                    LogUtil.i(TAG, "UPLOAD_HISTORY_TASKS_OF_ONE_TASK");
                    mUploadHandler.put(new SyncMessage(mUserId, taskId, taskType, syncType));
                    break;
                case UPLOAD_ALL_HISTORY_TASKS_BY_TASK_TYPE:
                    LogUtil.i(TAG, "UPLOAD_ALL_HISTORY_TASKS_BY_TASK_TYPE");
                    mUploadHandler.put(new SyncMessage(mUserId, taskType, isFromMyTask, syncType));
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, e.getMessage());
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        try {
            mDownloadHandler.stop();
            mUploadHandler.stop();
            mQueryHandler.stop();
            mOtherHandler.stop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mDownloadHandler.shutdown();
        }

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
