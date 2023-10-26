package com.sh3h.hotline.service.handler;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.base.DUEntitiesResult;
import com.sh3h.dataprovider.data.entity.request.DUCreateSelfOrder;
import com.sh3h.dataprovider.data.entity.request.DUReply;
import com.sh3h.dataprovider.data.entity.response.DUCreateSelfOrderResult;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.service.SyncMessage;
import com.sh3h.hotline.service.SyncType;
import com.sh3h.hotline.ui.order.TaskState;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.squareup.otto.Bus;

import java.util.List;

import rx.Subscriber;


public class UploadHandler extends BaseHandler {

    private static final String TAG = "UploadHandler";
    private static final int LIMIT_100 = 100;
    private static final int LIMIT_10 = 10;

    public UploadHandler(MainApplication mainApplication,
                         DataManager dataManager,
                         ConfigHelper configHelper,
                         EventPosterHelper eventPosterHelper,
                         Bus bus) {
        super(mainApplication, dataManager, configHelper, eventPosterHelper, bus);
    }

    @Override
    public boolean process(SyncMessage syncMessage) {
        if (!super.process(syncMessage)) {
            return false;
        }

        switch (syncMessage.getSyncType()) {
            case UPLOAD_ONE_CREATE_SELF_ORDER:
                uploadCreateSelfOrderByTaskId(syncMessage);
                break;
            case UPLOAD_All_CREATE_SELF_ORDER:
                uploadAllCreateSelfOrderList(syncMessage);
                break;
            case UPLOAD_TASK_REPLY:
                uploadTaskReply(syncMessage);
                break;
            case UPLOAD_HISTORY_TASKS_OF_ONE_TASK:
                uploadTaskReplyList(syncMessage);
                break;
            case UPLOAD_ALL_MEDIA_OF_ONE_TASK:
                uploadMedias(syncMessage);
                break;
            case UPLOAD_ALL_MEDIA:
                uploadAllMedias(Constant.TASK_TYPE_DOWNLOAD_ORDER);
                break;
            case UPLOAD_ALL_HISTORY_TASKS_BY_TASK_TYPE:
                uploadAllHistoryTasks(syncMessage);
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 上传单条任务处理信息
     *
     * @param syncMessage
     */
    private void uploadTaskReply(final SyncMessage syncMessage) {
        LogUtil.i(TAG, "uploadTaskReply");
        if ((syncMessage == null)
                || TextUtil.isNullOrEmpty(syncMessage.getTaskId())) {
            return;
        }

        int userId = syncMessage.getUserId();
        String taskId = syncMessage.getTaskId();
        int taskType = syncMessage.getTaskType();
        int taskState = syncMessage.getTaskState();
        long replyTime = syncMessage.getReplyTime();
        mSubscription = mDataManager.uploadReply(taskId, taskType, taskState, replyTime)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "uploadTaskReply onCompleted");
                        uploadMedias(syncMessage);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, "uploadTaskReply onError " + e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "uploadTaskReply onNext " + aBoolean);
                    }
                });
    }

    /**
     * 上传单条自开单信息
     *
     * @param syncMessage
     */
    private void uploadCreateSelfOrderByTaskId(final SyncMessage syncMessage) {
        LogUtil.i(TAG, "uploadCreateSelfOrder");
        if ((syncMessage == null)
                || TextUtil.isNullOrEmpty(syncMessage.getTaskId())) {
            return;
        }

        int userId = syncMessage.getUserId();
        String taskId = syncMessage.getTaskId();
        mSubscription = mDataManager.uploadCreateSelfOrderByTaskId(taskId)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "uploadCreateSelfOrder onCompleted");
                        syncMessage.setTaskType(Constant.TASK_TYPE_CREATE_SELF_ORDER);
                        syncMessage.setTaskState(Constant.TASK_STATE_ALL);
                        uploadMedias(syncMessage);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "uploadCreateSelfOrder onError " + e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "uploadCreateSelfOrder onNext");
                    }
                });
    }

    /**
     * 上传所有单条自开单信息
     *
     * @param syncMessage
     */
    private void uploadAllCreateSelfOrderList(final SyncMessage syncMessage) {
        LogUtil.i(TAG, "uploadCreateSelfOrder");
        if (syncMessage == null) {
            return;
        }

        uploadAllCreateSelfOrderListLoop(0, LIMIT_100);
    }

    /**
     * 上传所有单条自开单信息Loop
     *
     * @param offset
     * @param limit
     */
    private void uploadAllCreateSelfOrderListLoop(final int offset, final int limit) {
        LogUtil.i(TAG, "uploadAllCreateSelfOrderListLooper");
        mSubscription = mDataManager.uploadAllCreateSelfOrderList(offset, limit)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "uploadAllCreateSelfOrderListLooper onCompleted");
                        uploadAllHistoryTasks(new SyncMessage(Constant.TASK_TYPE_CREATE_SELF_ORDER));
                        uploadAllMedias(Constant.TASK_TYPE_CREATE_SELF_ORDER);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, "uploadAllCreateSelfOrderListLooper" + e.getMessage());
                        mEventPosterHelper.postEventSafely(new UIBusEvent.NotifyHistoryTasksUI());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "uploadAllCreateSelfOrderListLooper onNext " + aBoolean);
                        uploadAllCreateSelfOrderListLoop(offset + limit, limit);
                    }
                });
    }


    /**
     * 上传任务处理信息集合
     *
     * @param syncMessage
     */
    public void uploadTaskReplyList(final SyncMessage syncMessage) {
        LogUtil.i(TAG, "uploadTaskReplyList");
        if ((syncMessage == null)
                || TextUtil.isNullOrEmpty(syncMessage.getTaskId())) {
            return;
        }

        int userId = syncMessage.getUserId();
        String taskId = syncMessage.getTaskId();
        mSubscription = mDataManager.uploadManyReplies(taskId)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "uploadTaskReplyList onCompleted");
//                        syncMessage.setTaskType(Constant.TASK_TYPE_DOWNLOAD_ORDER);
                        syncMessage.setTaskState(Constant.TASK_STATE_ALL);
                        uploadMedias(syncMessage);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, "uploadTaskReplyList" + e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "uploadTaskReplyList onNext " + aBoolean);
                        if (aBoolean) {
                            mEventPosterHelper.postEventSafely(new UIBusEvent.NotifyHistoryTasksUI());
                        }
                    }
                });
    }

    /**
     * upload all medias of one task or one history
     * @param syncMessage
     */
    private void uploadMedias(SyncMessage syncMessage) {
        LogUtil.i(TAG, "uploadMedias");
        if ((syncMessage == null)
                || TextUtil.isNullOrEmpty(syncMessage.getTaskId())) {
            return;
        }

        String taskId = syncMessage.getTaskId();
        int taskType = syncMessage.getTaskType();
        int taskState = syncMessage.getTaskState(); // -1: all states
        mSubscription = mDataManager.uploadMedias(taskId, taskType, taskState)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "uploadMedias onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "uploadMedias onError " + e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "uploadMedias onNext " + aBoolean);
                    }
                });
    }

    /**
     * 上传所有的历史记录(区分自开单和下载的工单)
     *
     * @param syncMessage
     */
    public void uploadAllHistoryTasks(final SyncMessage syncMessage) {
        LogUtil.i(TAG, "uploadAllHistoryTasks");
        if ((syncMessage == null)) {
            return;
        }
        int userId = syncMessage.getUserId();
        final int taskType = syncMessage.getTaskType();
        final boolean isFromMyTask = syncMessage.isFromMyTask();
        mSubscription = mDataManager.uploadAllHistoryTask(userId, taskType)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "uploadAllHistoryTasks onCompleted");
                        if (isFromMyTask) {//
                            mEventPosterHelper.postEventSafely(new UIBusEvent.NotifyDownloadTasks(syncMessage));
                        } else {
                            mEventPosterHelper.postEventSafely(new UIBusEvent.NotifyHistoryTasksUI());
                        }
                        uploadAllMedias(taskType);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, "uploadAllHistoryTasks onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "uploadAllHistoryTasks onNext " + aBoolean);
                    }
                });
    }

    /**
     *
     */
    private void uploadAllMedias(int taskType) {
        mSubscription = mDataManager.uploadAllMedias(taskType)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "uploadAllMedias onCompleted");
                        mEventPosterHelper.postEventSafely(new UIBusEvent.NotifyHistoryTasksUI());
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, "uploadAllMedias onError " + e.getMessage());
                        mEventPosterHelper.postEventSafely(new UIBusEvent.NotifyHistoryTasksUI());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "uploadAllMedias onNext " + aBoolean);
                    }
                });
    }
}
