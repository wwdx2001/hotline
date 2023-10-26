package com.sh3h.dataprovider.data.local.db;

import android.content.Context;

import com.sh3h.dataprovider.BaseApplication;
import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.injection.annotation.ApplicationContext;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.OtoO;
import com.sh3h.localprovider.DBManager;
import com.sh3h.localprovider.greendaoEntity.HistoryTask;
import com.sh3h.localprovider.greendaoEntity.MultiMedia;
import com.sh3h.localprovider.greendaoEntity.Task;
import com.sh3h.localprovider.greendaoEntity.Word;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;

import static com.sh3h.dataprovider.util.Constant.SERVER_TASK_ID;

@Singleton
public class DbHelper {
    private static final String TAG = "DbHelper";

    private final Context mContext;
    private final ConfigHelper mConfigHelper;
    private boolean mIsInit;

    @Inject
    public DbHelper(@ApplicationContext Context context,
                    ConfigHelper configHelper) {
        mContext = context;
        mConfigHelper = configHelper;
        mIsInit = false;
    }

    /**
     * initialize
     */
    public synchronized boolean init() {
        if (!mIsInit) {
            mIsInit = true;
            return DBManager.getInstance().init(mConfigHelper.getDBFilePath().getPath(), mContext);
        }
        return true;
    }

    /**
     * destroy
     */
    public synchronized void destroy() {
        if (mIsInit) {
            mIsInit = false;
            DBManager.getInstance().destroy();
        }
    }

    /**
     * clear all tables
     */
    public Observable<Boolean> clearData() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    init();
                    GreenDaoUtils.getAsyncSession(BaseApplication.getInstance())
                            .runInTx(new Runnable() {
                                @Override
                                public void run() {
                                    GreenDaoUtils.getDaoSession(BaseApplication.getInstance())
                                            .getDUMyTaskDao().deleteAll();
                                    GreenDaoUtils.getDaoSession(BaseApplication.getInstance())
                                            .getHandleOrderEntityDao().deleteAll();
                                    GreenDaoUtils.getDaoSession(BaseApplication.getInstance())
                                            .getArriveDataEntityDao().deleteAll();
                                    GreenDaoUtils.getDaoSession(BaseApplication.getInstance())
                                            .getAuditResultsSynchronizationEntityDao().deleteAll();

                                    GreenDaoUtils.getDaoSession(BaseApplication.getInstance())
                                            .getFYLYBeanDao().deleteAll();
                                    GreenDaoUtils.getDaoSession(BaseApplication.getInstance())
                                            .getInitDao().deleteAll();
                                    GreenDaoUtils.getDaoSession(BaseApplication.getInstance())
                                            .getReassignmentSynchronizationEntityDao().deleteAll();
                                    GreenDaoUtils.getDaoSession(BaseApplication.getInstance())
                                            .getWorkInfoSynchronizationEntityDao().deleteAll();
                                    GreenDaoUtils.getDaoSession(BaseApplication.getInstance())
                                            .getCLJBBeanDao().deleteAll();
                                    GreenDaoUtils.getDaoSession(BaseApplication.getInstance())
                                            .getFYNRBeanDao().deleteAll();
                                    GreenDaoUtils.getDaoSession(BaseApplication.getInstance())
                                            .getFYLYBeanDao().deleteAll();
                                    GreenDaoUtils.getDaoSession(BaseApplication.getInstance())
                                            .getFYLXBeanDao().deleteAll();
                                }
                            });
                    DBManager.getInstance().clearAllTables();
                    subscriber.onNext(true);
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<Boolean> deleteMyTask(final int userId) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    init();
                    subscriber.onNext(DBManager.getInstance().deleteMyTask(userId));
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 保存"我的工单"列表
     *
     * @return
     */
    public Observable<Boolean> saveAllMyTask(final int userId, final List<Task> tasks) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if (tasks == null) {
                        throw new NullPointerException("param is null");
                    }

                    init();

                    if (DBManager.getInstance().deleteMyTasks(userId, tasks)) {
                        subscriber.onNext(DBManager.getInstance().saveMyTasks(userId, tasks));
                    } else {
                        subscriber.onNext(false);
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 分页查询“我的工单”
     *
     * @param userId userId
     * @param offset 起始位
     * @param size   数目
     * @return
     */
    public Observable<List<Task>> loadTask(final int userId, final int offset, final int size,
                                           final int taskType, final boolean isFromMyList) {
        return Observable.create(new Observable.OnSubscribe<List<Task>>() {
            @Override
            public void call(Subscriber<? super List<Task>> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    init();

                    List<Task> list = DBManager.getInstance().loadTasks(userId, offset, size,
                            taskType, isFromMyList);
                    if (list != null) {
                        subscriber.onNext(list);
                    } else {
                        subscriber.onError(new Throwable("the list of tasks is null"));
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * @param userId
     * @param task
     * @param needUpdateOtherTables
     * @return
     */
    public Observable<Boolean> updateMyTask(final int userId,
                                            final Task task,
                                            final boolean needUpdateOtherTables) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if (task == null) {
                        throw new NullPointerException("param is null");
                    }

                    init();
                    boolean result = DBManager.getInstance().updateMyTask(task);
                    if (needUpdateOtherTables
                            && result
                            && (!TextUtil.isNullOrEmpty(task.getEXTEND()))) {
                        long replyTimeInHistory = OtoO.getExtendLong(task.getEXTEND(), Constant.REPLY_TIME_IN_HISTORY);
                        String serverTaskId = OtoO.getExtendString(task.getEXTEND(), Constant.SERVER_TASK_ID);
                        List<HistoryTask> historyTaskList = task.getHistoryTaskList();
                        if (historyTaskList != null) {
                            for (HistoryTask historyTask : historyTaskList) {
                                historyTask.setEXTEND(OtoO.add2Extend(historyTask.getEXTEND(), SERVER_TASK_ID, serverTaskId));
                            }

                            DBManager.getInstance().updateHistoryTaskList(historyTaskList);
                        }

                        List<MultiMedia> multiMediaList = task.getMultiMedias();
                        if (multiMediaList != null) {
                            for (MultiMedia multiMedia : multiMediaList) {
                                multiMedia.setEXTEND(OtoO.add2Extend(multiMedia.getEXTEND(), SERVER_TASK_ID, serverTaskId));
                            }

                            DBManager.getInstance().updateMultiMediaList(multiMediaList);
                        }

                        subscriber.onNext(result);
                    } else {
                        subscriber.onNext(result);
                    }

                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<Boolean> updateMyTaskList(final int userId,
                                                final List<Task> taskList,
                                                final boolean needUpdateOtherTables) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if ((taskList == null) || (taskList.size() <= 0)) {
                        throw new NullPointerException("param is null");
                    }

                    init();
                    boolean result = DBManager.getInstance().updateMyTaskList(taskList);
                    if (needUpdateOtherTables
                            && result) {
                        for (Task task : taskList) {
                            if (TextUtil.isNullOrEmpty(task.getEXTEND())) {
                                continue;
                            }

                            long replyTimeInHistory = OtoO.getExtendLong(task.getEXTEND(), Constant.REPLY_TIME_IN_HISTORY);
                            String serverTaskId = OtoO.getExtendString(task.getEXTEND(), Constant.SERVER_TASK_ID);
                            List<HistoryTask> historyTaskList = task.getHistoryTaskList();
                            if (historyTaskList != null) {
                                for (HistoryTask historyTask : historyTaskList) {
                                    historyTask.setEXTEND(OtoO.add2Extend(historyTask.getEXTEND(), SERVER_TASK_ID, serverTaskId));
                                    //增加处理内容修改taskId为serverId
                                    if (historyTask.getTASK_REPLY() != null) {
                                        JSONObject jsonObject = new JSONObject(historyTask.getTASK_REPLY());
                                        jsonObject.put("taskId", serverTaskId);
                                        historyTask.setTASK_REPLY(jsonObject.toString());
                                    }
                                }
                                DBManager.getInstance().updateHistoryTaskList(historyTaskList);
                            }

                            List<MultiMedia> multiMediaList = task.getMultiMedias();
                            if (multiMediaList != null) {
                                for (MultiMedia multiMedia : multiMediaList) {
                                    multiMedia.setEXTEND(OtoO.add2Extend(multiMedia.getEXTEND(), SERVER_TASK_ID, serverTaskId));
                                }

                                DBManager.getInstance().updateMultiMediaList(multiMediaList);
                            }
                        }

                        subscriber.onNext(result);
                    } else {
                        subscriber.onNext(result);
                    }
//                    subscriber.onNext(result);

                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 分页查询"历史工单"
     *
     * @param offset 起始位
     * @param size   数目
     * @return
     */
    public Observable<List<HistoryTask>> loadHistoryTask(final int userId, final int offset, final int size) {
        return Observable.create(new Observable.OnSubscribe<List<HistoryTask>>() {
            @Override
            public void call(Subscriber<? super List<HistoryTask>> subscriber) {
                try {
                    init();
                    List<HistoryTask> list = DBManager.getInstance().loadHistoryTasks(userId, offset, size);
                    subscriber.onNext(list);
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<List<HistoryTask>> getCreateSelfOrderHistory(final int userId, final int offset, final int size) {
        return Observable.create(new Observable.OnSubscribe<List<HistoryTask>>() {
            @Override
            public void call(Subscriber<? super List<HistoryTask>> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    init();
                    List<HistoryTask> historyTaskList =
                            DBManager.getInstance().getCreateSelfOrderHistory(userId, offset, size);
                    if (historyTaskList != null) {
                        subscriber.onNext(historyTaskList);
                    } else {
                        subscriber.onError(new Throwable("historyTaskList is null"));
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 通过taskId来查找Task
     *
     * @param taskId
     * @return
     */
    public Observable<Task> queryTaskByTaskId(final String taskId) {
        return Observable.create(new Observable.OnSubscribe<Task>() {
            @Override
            public void call(Subscriber<? super Task> subscriber) {
                init();
                Task task = DBManager.getInstance().queryTaskByTaskId(taskId);
                subscriber.onNext(task);
            }
        });
    }

    /**
     * 更新
     *
     * @return
     */
    public Observable<Boolean> updateHistoryTask(final HistoryTask historyTask) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if (historyTask == null) {
                        return;
                    }

                    init();
                    subscriber.onNext(DBManager.getInstance().updateHistoryTask(historyTask));
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 保存自开单和历史记录
     *
     * @param task
     * @return
     */
    public Observable<Boolean> saveCreateSelfOrderAndHistory(final Task task,
                                                             final HistoryTask createTask) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {

            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if ((task == null) || (createTask == null)) {
                        throw new NullPointerException("param is null");
                    }

                    init();

                    subscriber.onNext(DBManager.getInstance().saveCreateSelfOrderAndHistory(task, createTask));
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<Task> getCreateSelfOrderByTaskId(final int userId, final String taskId) {
        return Observable.create(new Observable.OnSubscribe<Task>() {
            @Override
            public void call(Subscriber<? super Task> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if (TextUtil.isNullOrEmpty(taskId)) {
                        throw new NullPointerException("param is null");
                    }

                    init();

                    Task task = DBManager.getInstance().getCreateSelfOrderByTaskId(userId, taskId);
                    if (task != null) {
                        subscriber.onNext(task);
                    } else {
                        subscriber.onError(new Throwable("tasks is null"));
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * @param userId
     * @param taskTypeList
     * @param offset
     * @param limit
     * @return
     */
    public Observable<List<Task>> getTaskList(final int userId,
                                              final List<Integer> taskTypeList, // null is possible
                                              final int offset,
                                              final int limit) {
        return Observable.create(new Observable.OnSubscribe<List<Task>>() {
            @Override
            public void call(Subscriber<? super List<Task>> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    init();
                    List<Task> taskList = DBManager.getInstance().getTaskList(userId, taskTypeList, offset, limit);
                    if (taskList != null) {
                        subscriber.onNext(taskList);
                    } else {
                        subscriber.onError(new Throwable("taskList is null"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

//    public  Observable<Boolean> updateCreateSelfOrderAndHistory(final String loaclTaskId,
//                                                                            final String serverTaskId, final int uploadFlag) {
//        return Observable.create(new Observable.OnSubscribe<Boolean>() {
//            @Override
//            public void call(Subscriber<? super Boolean> subscriber) {
//                try {
//                    init();
//                    boolean isSuccess = DBManager.getInstance().updateCreateSelfOrderAndHistory(loaclTaskId, serverTaskId, uploadFlag);
//                    subscriber.onNext(isSuccess);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    subscriber.onError(e);
//                } finally {
//                    subscriber.onCompleted();
//                }
//            }
//        });
//    }

    /**
     * 保存“历史工单”
     */
    public Observable<Boolean> saveHistoryTask(final HistoryTask historyTask) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if (historyTask == null) {
                        throw new NullPointerException("param is null");
                    }

                    init();

                    subscriber.onNext(DBManager.getInstance().saveHistoryTask(historyTask));
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<Boolean> saveWords(final List<Word> words) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if ((words == null) || (words.size() <= 0)) {
                        throw new Exception("param is error");
                    }

                    init();
                    DBManager.getInstance().clearWords();
                    subscriber.onNext(DBManager.getInstance().saveWords(words));
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<List<Word>> getFirstWords(final String str) {
        return Observable.create(new Observable.OnSubscribe<List<Word>>() {
            @Override
            public void call(Subscriber<? super List<Word>> subscriber) {

                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if (TextUtil.isNullOrEmpty(str)) {
                        throw new Exception("param is error");
                    }
                    init();
                    List<Word> wordList = DBManager.getInstance().getFirstWords(str);
                    subscriber.onNext(wordList);
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<Map<String, List<Word>>> getWords(final String str) {
        return Observable.create(new Observable.OnSubscribe<Map<String, List<Word>>>() {
            @Override
            public void call(Subscriber<? super Map<String, List<Word>>> subscriber) {
                init();
                try {
                    Map<String, List<Word>> map = DBManager.getInstance().getNextWords(str);
                    subscriber.onNext(map);
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<Boolean> saveMedia(final DUMedia duMedia) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if (duMedia == null) {
                        throw new NullPointerException("param is null");
                    }

                    init();

                    boolean isSuccess = DBManager.getInstance().saveMedia(duMedia2MultiMedia(duMedia));
                    subscriber.onNext(isSuccess);
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<List<DUMedia>> getMediaList(final int userId,
                                                  final String taskId,
                                                  final int taskType,
                                                  final int taskState,
                                                  final int fileType) {
        return Observable.create(new Observable.OnSubscribe<List<DUMedia>>() {
            @Override
            public void call(Subscriber<? super List<DUMedia>> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if (TextUtil.isNullOrEmpty(taskId)) {
                        throw new NullPointerException("param is null");
                    }

                    init();

                    List<MultiMedia> multiMediaList = DBManager.getInstance().getMediaList(userId,
                            taskId, taskType, taskState, fileType);
                    if (multiMediaList != null) {
                        List<DUMedia> duMediaList = new ArrayList<>();
                        for (MultiMedia multiMedia : multiMediaList) {
                            DUMedia duMedia = multiMedia2DUMedia(multiMedia);
                            duMediaList.add(duMedia);
                        }

                        subscriber.onNext(duMediaList);
                    } else {
                        subscriber.onError(new Throwable("multiMediaList is null"));
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<DUMedia> getMedia(final long id) {
        return Observable.create(new Observable.OnSubscribe<DUMedia>() {
            @Override
            public void call(Subscriber<? super DUMedia> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    init();

                    MultiMedia multiMedia = DBManager.getInstance().getMedia(id);
                    if (multiMedia != null) {
                        subscriber.onNext(multiMedia2DUMedia(multiMedia));
                    } else {
                        subscriber.onError(new Throwable("multiMediaList is null"));
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<Boolean> deleteMedia(final DUMedia duMedia) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if ((duMedia == null)
                            || TextUtil.isNullOrEmpty(duMedia.getFileName())) {
                        throw new NullPointerException("param is null");
                    }

                    init();

                    subscriber.onNext(DBManager.getInstance().deleteMedia(duMedia2MultiMedia(duMedia)));
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<Boolean> deleteMediaList(final List<DUMedia> duMedias) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if ((duMedias == null)) {
                        throw new NullPointerException("param is null");
                    }

                    init();

                    subscriber.onNext(DBManager.getInstance().deleteMediaList(duMediaList2MultiMediaList(duMedias)));
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<Boolean> updateMediaList(final List<DUMedia> duMediaList) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if (duMediaList == null) {
                        throw new NullPointerException("param is null");
                    }

                    init();

                    subscriber.onNext(DBManager.getInstance().updateMultiMediaList(duMediaList2MultiMediaList(duMediaList)));
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    private List<MultiMedia> duMediaList2MultiMediaList(List<DUMedia> duMediaList) {
        List<MultiMedia> multiMediaList = new ArrayList<>();
        for (DUMedia duMedia : duMediaList) {
            multiMediaList.add(duMedia2MultiMedia(duMedia));
        }
        return multiMediaList;
    }

    private MultiMedia duMedia2MultiMedia(DUMedia duMedia) {
        return new MultiMedia(
                duMedia.getId(),
                duMedia.getUserId(),
                duMedia.getTaskId(),
                duMedia.getTaskType(),
                duMedia.getTaskState(),
                duMedia.getFileType(),
                duMedia.getFileName(),
                duMedia.getFileHash(),
                duMedia.getFileUrl(),
                duMedia.getUploadFlag(),
                duMedia.getExtend(),
                duMedia.getFileTime());
    }

    private DUMedia multiMedia2DUMedia(MultiMedia multiMedia) {
        return new DUMedia(
                multiMedia.getID(),
                multiMedia.getUSER_ID(),
                multiMedia.getTASK_ID(),
                multiMedia.getTASK_TYPE(),
                multiMedia.getTASK_STATE(),
                multiMedia.getFILE_TYPE(),
                multiMedia.getFILE_NAME(),
                multiMedia.getFILE_HASH(),
                multiMedia.getFILE_URL(),
                multiMedia.getUPLOAD_FLAG(),
                multiMedia.getEXTEND(),
                multiMedia.getFILE_TIME());
    }

    /**
     * 更新HistoryTask的回复信息
     *
     * @param historyTask
     * @return
     */
    public Observable<Boolean> updateHistoryTaskReply(final HistoryTask historyTask) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                init();
                try {
                    boolean isSuccess = DBManager.getInstance().updateHistoryTaskReply(historyTask);
                    subscriber.onNext(isSuccess);
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<List<String>> getStrTaskReplys(final HistoryTask historyTask) {
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                init();
                try {
                    List<String> replyInfos = DBManager.getInstance().getNoUploadHistoryTask(historyTask.getTASK_ID());
                    subscriber.onNext(replyInfos);
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 更新多条历史记录的上传标志
     *
     * @param historyTaskList
     * @return
     */
    public Observable<Boolean> updateSomeHistoryTaskFlag(final List<HistoryTask> historyTaskList) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                init();
                try {
                    boolean isSuccess = DBManager.getInstance().updateSomeHistoryTaskFlag(historyTaskList);
                    subscriber.onNext(isSuccess);
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * @param userId
     * @param taskId
     * @param taskType
     * @param taskState
     * @param replyTime
     * @return
     */
    public Observable<HistoryTask> getHistoryTask(final int userId, final String taskId,
                                                  final int taskType, final int taskState,
                                                  final long replyTime) {
        return Observable.create(new Observable.OnSubscribe<HistoryTask>() {
            @Override
            public void call(Subscriber<? super HistoryTask> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if (TextUtil.isNullOrEmpty(taskId)) {
                        throw new Exception("param is error");
                    }

                    init();

                    HistoryTask historyTask = DBManager.getInstance().getHistoryTask(userId,
                            taskId, taskType, taskState, replyTime);
                    if (historyTask != null) {
                        subscriber.onNext(historyTask);
                    } else {
                        subscriber.onError(new Throwable("historyTask is null"));
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<List<Task>> queryMyTask(final int userId, final String queryCondition) {
        return Observable.create(new Observable.OnSubscribe<List<Task>>() {
            @Override
            public void call(Subscriber<? super List<Task>> subscriber) {
                init();
                try {
                    List<Task> taskList = DBManager.getInstance().queryMyTask(userId, queryCondition);
                    subscriber.onNext(taskList);
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 更新历史工单
     *
     * @param historyTask
     * @return
     */
    public Observable<Boolean> updateHistoryTask(final Long id, final HistoryTask historyTask) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                init();
                try {
                    boolean isSuccess = DBManager.getInstance().updateHistoryTask(id, historyTask);
                    subscriber.onNext(isSuccess);
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 获得工单的历史记录
     *
     * @param userId
     * @param taskId
     * @param uploadFlag
     * @return
     */
    public Observable<List<HistoryTask>> getHistoryTasks(final int userId, final String taskId, final int uploadFlag) {
        return Observable.create(new Observable.OnSubscribe<List<HistoryTask>>() {
            @Override
            public void call(Subscriber<? super List<HistoryTask>> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                try {
                    if (TextUtil.isNullOrEmpty(taskId)) {
                        throw new Exception("param is error");
                    }

                    init();
                    List<HistoryTask> historyTaskList = DBManager.getInstance().getHistoryTasks(userId, taskId, uploadFlag);
                    if (historyTaskList == null) {
                        subscriber.onError(new Throwable("historyTaskList is null"));
                    } else {
                        subscriber.onNext(historyTaskList);
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 获得工单的所有历史记录
     *
     * @param userId
     * @param taskId
     * @return
     */
    public Observable<List<HistoryTask>> getHistoryTasks(final int userId, final String taskId) {
        return Observable.create(new Observable.OnSubscribe<List<HistoryTask>>() {
            @Override
            public void call(Subscriber<? super List<HistoryTask>> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                try {
                    if (TextUtil.isNullOrEmpty(taskId)) {
                        throw new Exception("param is error");
                    }

                    init();
                    List<HistoryTask> historyTaskList = DBManager.getInstance().getHistoryTasks(userId, taskId);
                    if (historyTaskList == null) {
                        subscriber.onError(new Throwable("historyTaskList is null"));
                    } else {
                        subscriber.onNext(historyTaskList);
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 更新历史工单集合
     */
    public Observable<Boolean> updateHistoryTaskList(final List<HistoryTask> historyTaskList) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if (historyTaskList == null || historyTaskList.size() == 0) {
                        subscriber.onError(new Throwable("param is error"));
                    }
                    init();
                    boolean isSuccess = DBManager.getInstance().updateHistoryTaskList(historyTaskList);
                    subscriber.onNext(isSuccess);

                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 根据任务类型来获得本地的历史工单
     *
     * @param userId
     * @param taskType 任务类型
     * @return
     */
    public Observable<List<HistoryTask>> getAllHistoryTask(final int userId, final int taskType) {
        return Observable.create(new Observable.OnSubscribe<List<HistoryTask>>() {
            @Override
            public void call(Subscriber<? super List<HistoryTask>> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    init();
                    List<HistoryTask> historyTaskList = DBManager.getInstance()
                            .getAllHistoryTaskList(userId, taskType);

                    if (historyTaskList == null) {
                        subscriber.onError(new Throwable("historyTaskList is null"));
                    } else {
                        subscriber.onNext(historyTaskList);
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 更新HistoryTask的回复信息
     *
     * @return
     */
    public Observable<Boolean> updateHistoryTaskReplyCreateSelf(final String taskId, final String serverTaskId) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                init();
                try {
                    boolean isSuccess = DBManager.getInstance().updateTaskReplyCreateSelf(taskId, serverTaskId);
                    subscriber.onNext(isSuccess);
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<List<HistoryTask>> getTaskHandleCreateSelf(final int userId, final String taskId) {
        return Observable.create(new Observable.OnSubscribe<List<HistoryTask>>() {
            @Override
            public void call(Subscriber<? super List<HistoryTask>> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                try {
                    if (TextUtil.isNullOrEmpty(taskId)) {
                        throw new Exception("param is error");
                    }

                    init();
                    List<HistoryTask> historyTaskList = DBManager.getInstance()
                            .getTaskHandleCreateSelf(userId, taskId);
                    if (historyTaskList == null) {
                        subscriber.onError(new Throwable("historyTaskList is null"));
                    } else {
                        subscriber.onNext(historyTaskList);
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 更新多媒体表的文件创建时间
     *
     * @return
     */
    public Observable<Boolean> updateMediaListFileTime(final String taskId, final long replyTime) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                init();
                try {
                    boolean isSuccess = DBManager.getInstance().updateMediaListFileTime(taskId, replyTime);
                    subscriber.onNext(isSuccess);
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 删除本地过期的历史工单
     *
     * @param userId
     * @param currentTime
     * @param intervalTime
     * @return
     */
    public Observable<List<MultiMedia>> deleteHistoryTask(final int userId, final long currentTime, final long intervalTime) {
        return Observable.create(new Observable.OnSubscribe<List<MultiMedia>>() {
            @Override
            public void call(Subscriber<? super List<MultiMedia>> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                try {
                    init();
                    subscriber.onNext(DBManager.getInstance().deleteHistoryTask(userId, currentTime, intervalTime));
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 查询临期工单
     *
     * @param userId
     * @return
     */
    public Observable<List<Task>> queryDeadLineTasks(final long currentTime, final int userId) {
        return Observable.create(new Observable.OnSubscribe<List<Task>>() {
            @Override
            public void call(Subscriber<? super List<Task>> subscriber) {
                init();
                try {
                    List<Task> taskList = DBManager.getInstance().queryDeadlineTasks(currentTime, userId);
                    subscriber.onNext(taskList);
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 是否存在临期工单
     */
    public Observable<Boolean> isExistDeadlineTask(final int userId) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                try {
                    init();
                    subscriber.onNext(DBManager.getInstance().isExistDeadlineTasks(userId));
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 获取所有站点
     *
     * @return
     */
    public Observable<List<Word>> getAllStations() {
        return Observable.create(new Observable.OnSubscribe<List<Word>>() {
            @Override
            public void call(Subscriber<? super List<Word>> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                try {
                    init();
                    subscriber.onNext(DBManager.getInstance().getAllStation());
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 获得“供水热线”站点
     *
     * @return
     */
    public Observable<Word> getHotlineStation() {
        return Observable.create(new Observable.OnSubscribe<Word>() {
            @Override
            public void call(Subscriber<? super Word> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                try {
                    init();
                    subscriber.onNext(DBManager.getInstance().getHotlineStation());
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }
}
