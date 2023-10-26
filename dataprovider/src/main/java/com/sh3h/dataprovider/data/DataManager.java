package com.sh3h.dataprovider.data;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.dataprovider.data.entity.base.DUEntitiesResult;
import com.sh3h.dataprovider.data.entity.base.DUEntityResult;
import com.sh3h.dataprovider.data.entity.request.DUCreateSelfOrder;
import com.sh3h.dataprovider.data.entity.request.DUFileResult;
import com.sh3h.dataprovider.data.entity.request.DULoginInfo;
import com.sh3h.dataprovider.data.entity.request.DUReply;
import com.sh3h.dataprovider.data.entity.request.DUUpdateInfo;
import com.sh3h.dataprovider.data.entity.request.DUUpdateResult;
import com.sh3h.dataprovider.data.entity.request.DUUserResult;
import com.sh3h.dataprovider.data.entity.response.DUArrearsDetail;
import com.sh3h.dataprovider.data.entity.response.DUBillBaseInfo;
import com.sh3h.dataprovider.data.entity.response.DUCreateSelfOrderResult;
import com.sh3h.dataprovider.data.entity.response.DUFile;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.data.entity.response.DUNews;
import com.sh3h.dataprovider.data.entity.response.DUOrder;
import com.sh3h.dataprovider.data.entity.response.DUProcess;
import com.sh3h.dataprovider.data.entity.response.DURecentBill;
import com.sh3h.dataprovider.data.entity.response.DURecentBillDetail;
import com.sh3h.dataprovider.data.entity.response.DUReplyOrderResult;
import com.sh3h.dataprovider.data.entity.response.DUWord;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.db.DbHelper;
import com.sh3h.dataprovider.data.local.file.FileHelper;
import com.sh3h.dataprovider.data.local.preference.PreferencesHelper;
import com.sh3h.dataprovider.data.local.preference.UserSession;
import com.sh3h.dataprovider.data.local.xml.XmlHelper;
import com.sh3h.dataprovider.data.remote.Downloader;
import com.sh3h.dataprovider.data.remote.HttpHelper;
import com.sh3h.dataprovider.data.remote.Parser;
import com.sh3h.dataprovider.exception.DUException;
import com.sh3h.dataprovider.injection.annotation.ApplicationContext;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.dataprovider.util.OtoO;
import com.sh3h.localprovider.greendaoEntity.HistoryTask;
import com.sh3h.localprovider.greendaoEntity.MultiMedia;
import com.sh3h.localprovider.greendaoEntity.Task;
import com.sh3h.localprovider.greendaoEntity.Word;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_PICTURE;
import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_SCREEN_SHOT;
import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_SIGNUP;
import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_VOICE;
import static com.sh3h.dataprovider.data.entity.DUMedia.PICTURE_SUFFIX;
import static com.sh3h.dataprovider.data.entity.DUMedia.SIGNUP_SUFFIX;
import static com.sh3h.dataprovider.data.entity.DUMedia.UPLOAD_FLAG_LOCAL;
import static com.sh3h.dataprovider.data.entity.DUMedia.VIDEO_SUFFIX;
import static com.sh3h.dataprovider.data.entity.DUMedia.VOICE_SUFFIX;

@Singleton
public class DataManager {
    private static final String TAG = "DataManager";

    private final Context mContext;
    private final HttpHelper mHttpHelper;
    private final DbHelper mDbHelper;
    private final PreferencesHelper mPreferencesHelper;
    private final ConfigHelper mConfigHelper;
    private final EventPosterHelper mEventPoster;
    private final XmlHelper mXmlHelper;
    private final FileHelper mFileHelper;
    private final Downloader mDownloader;
    private final Parser mParser;

    @Inject
    public DataManager(@ApplicationContext Context context,
                       HttpHelper httpHelper,
                       PreferencesHelper preferencesHelper,
                       DbHelper dbHelper,
                       ConfigHelper configHelper,
                       EventPosterHelper eventPosterHelper,
                       XmlHelper xmlHelper,
                       FileHelper fileHelper, Downloader mDownloader) {
        mContext = context;
        mHttpHelper = httpHelper;
        mPreferencesHelper = preferencesHelper;
        mDbHelper = dbHelper;
        mConfigHelper = configHelper;
        mEventPoster = eventPosterHelper;
        mXmlHelper = xmlHelper;
        mFileHelper = fileHelper;
        this.mDownloader = mDownloader;

        mParser = new Parser(dbHelper, configHelper, eventPosterHelper,xmlHelper);
    }

    public void destroy() {
        mDbHelper.destroy();
    }

    public void clearData() {
        mDbHelper.clearData();
    }

    public Observable<Void> init() {
        final int NONE = 0;
        final int CLEARING = 1; // clear history
        final int RECOVERY = 2; // restore factory

        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                boolean result = false;
                UserSession userSession = mPreferencesHelper.getUserSession();
                if (userSession.isRecovery()) {
                    result = mPreferencesHelper.clearUserSession();
                    subscriber.onNext(result ? RECOVERY : NONE);
                } else if (userSession.isClearing()) {
                    userSession.setClearing(false);
                    result = mPreferencesHelper.saveUserSession();
                    subscriber.onNext(result ? CLEARING : NONE);
                } else {
                    subscriber.onNext(NONE);
                }

                subscriber.onCompleted();
            }
        }).concatMap(new Func1<Integer, Observable<? extends Void>>() {
            @Override
            public Observable<? extends Void> call(Integer integer) {
                switch (integer) {
                    case CLEARING:
                        return mConfigHelper.clearAndInitConfigs(false);
                    case RECOVERY:
                        return mConfigHelper.clearAndInitConfigs(true);
                    case NONE:
                    default:
                        return mConfigHelper.initDefaultConfigs();
                }
            }
        });
    }

    public int getUserId() {
        UserSession userSession = mPreferencesHelper.getUserSession();
        return userSession != null ? userSession.getUserId() : 0;
    }

    public String getAccount() {
        UserSession userSession = mPreferencesHelper.getUserSession();
        return userSession != null ? userSession.getAccount() : "";
    }

    /**
     * login
     *
     * @param duLoginInfo
     * @return
     */
    public Observable<Boolean> login(final DULoginInfo duLoginInfo) {
        return mHttpHelper.login(duLoginInfo)
                .map(new Func1<DUUserResult, Boolean>() {
                    @Override
                    public Boolean call(DUUserResult duUserResult) {
                        UserSession userSession = mPreferencesHelper.getUserSession();
                        userSession.setAccount(duLoginInfo.getAccount());
                        userSession.set_password(duLoginInfo.getPassword());
                        userSession.setUserId(duUserResult.getUserId());
                        userSession.setUserName(duUserResult.getUserName());
                        userSession.set_accessToken(TextUtil.getString("F6D62986-915F-4DD8-963A-B0632F-149E18"));
                        return mPreferencesHelper.saveUserSession();
                    }
                });
    }

    /**
     * update app or data
     *
     * @param duUpdateInfo
     * @return
     */
    public Observable<DUFileResult> updateVersion(DUUpdateInfo duUpdateInfo) {
        return mHttpHelper.updateVersion(duUpdateInfo)
                .concatMap(new Func1<DUUpdateResult, Observable<DUFileResult>>() {
                    @Override
                    public Observable<DUFileResult> call(DUUpdateResult duUpdateResult) {
                        Observable<DUFileResult> observable =
                                mDownloader.downloadFile(duUpdateResult.getDuUpdateInfo(), duUpdateResult.getItemList());
                        if (observable == null) {
                            LogUtils.e("VersionService", "observable==null");
                            return Observable.create(new Observable.OnSubscribe<DUFileResult>() {
                                @Override
                                public void call(Subscriber<? super DUFileResult> subscriber) {
                                    if (subscriber.isUnsubscribed()) {
                                        return;
                                    }

                                    subscriber.onError(new Throwable("downloadFile is failure"));
                                }
                            });
                        }

                        return observable;
                    }
                })
                .doOnNext(new Action1<DUFileResult>() {
                    @Override
                    public void call(DUFileResult duFileResult) {
                        String path = duFileResult.getPath();
                        String name = duFileResult.getName();
                        int percent = duFileResult.getPercent();
                        if (TextUtil.isNullOrEmpty(path)
                                || TextUtil.isNullOrEmpty(name)) {
                            LogUtil.i(TAG, "---updateVersion doOnNext---null");
                            return;
                        }

                        LogUtil.i(TAG, String.format("---updateVersion doOnNext--name: %s, percent: %d",
                                name, percent));
                        if (percent >= 100) {
                            if (name.contains("data_")) {
                                mParser.parseData(duFileResult);
                            } else if (name.contains("app_")) {
                                mParser.unzipFile(duFileResult);
                            }
                        }
                    }
                });
    }

    /**
     * @param order
     * @param createTask
     * @return
     */
    public Observable<Boolean> saveCreateSelfOrderAndHistory(DUCreateSelfOrder order, DUHistoryTask createTask) {
        return mDbHelper.saveCreateSelfOrderAndHistory(OtoO.toTask(order), OtoO.dUHistoryTask2HistoryTask(createTask));
    }

    /**
     * 账单查询
     *
     * @param cardId  销根号(表号)
     * @param name    户名
     * @param address 地址
     * @return
     */
    public Observable<DUEntitiesResult<DUBillBaseInfo>> searchBill(String cardId, String tableNumber,
                                                                   String name, String address, boolean fuzzySearch) {
        return mHttpHelper.searchBill(cardId, tableNumber, name, address, fuzzySearch);
    }

    /**
     * 近期开账信息
     *
     * @param cardId     销根号(表号)
     * @param monthCount 月数
     * @return
     */
    public Observable<DUEntitiesResult<DURecentBill>> getRecentBill(String cardId, int monthCount) {
        return mHttpHelper.getRecentBill(cardId, monthCount);
    }

    /**
     * 近期开账详细信息
     *
     * @param cardId
     * @param feeId
     * @return
     */
    public Observable<DUEntityResult<DURecentBillDetail>> getRecentBillDetail(String cardId, int feeId) {
        return mHttpHelper.getRecentBillDetail(cardId, feeId);
    }

    /**
     * 欠费信息
     *
     * @param cardId     销根号(表号)
     * @param monthCount 月数
     * @return
     */
    public Observable<DUEntitiesResult<DURecentBill>> getArrearage(String cardId, int monthCount) {
        return mHttpHelper.getArrearage(cardId, monthCount);
    }

    /**
     * 欠费信息详细信息
     *
     * @param cardId
     * @param feeId
     * @return
     */
    public Observable<DUEntityResult<DUArrearsDetail>> getArrearageDetail(String cardId, int feeId) {
        return mHttpHelper.getArrearageDetail(cardId, feeId);
    }

    /**
     * 获取最新公告
     *
     * @return
     */
    public Observable<List<DUNews>> getNews() {
        return mHttpHelper.getNews()
                .filter(new Func1<DUEntitiesResult<DUNews>, Boolean>() {
                    @Override
                    public Boolean call(DUEntitiesResult<DUNews> duNewsDUEntitiesResult) {
                        return duNewsDUEntitiesResult.getCode() == DUEntitiesResult.SUCCESS_CODE &&
                                duNewsDUEntitiesResult.getStatusCode() == Constant.STATUS_CODE_200;
                    }
                })
                .concatMap(new Func1<DUEntitiesResult<DUNews>, Observable<? extends List<DUNews>>>() {
                    @Override
                    public Observable<? extends List<DUNews>> call(final DUEntitiesResult<DUNews> duNewsDUEntitiesResult) {
                        if (duNewsDUEntitiesResult.getData() == null) {
                            throw new IllegalArgumentException(DUException.PARAM_ERROR.getName());
                        }
                        return Observable.create(new Observable.OnSubscribe<List<DUNews>>() {
                            @Override
                            public void call(Subscriber<? super List<DUNews>> subscriber) {
                                subscriber.onNext(duNewsDUEntitiesResult.getData());
                            }
                        });
                    }
                });
    }

    /**
     * 公告实时查询
     *
     * @return
     */
    public Observable<List<DUNews>> searchNews(String title, String content, String type, long startTime, long endTime) {
        return mHttpHelper.searchNews(title, content, type, startTime, endTime)
                .filter(new Func1<DUEntitiesResult<DUNews>, Boolean>() {
                    @Override
                    public Boolean call(DUEntitiesResult<DUNews> duNewsDUEntitiesResult) {
                        return duNewsDUEntitiesResult.getCode() == DUEntitiesResult.SUCCESS_CODE &&
                                duNewsDUEntitiesResult.getStatusCode() == Constant.STATUS_CODE_200;
                    }
                })
                .concatMap(new Func1<DUEntitiesResult<DUNews>, Observable<? extends List<DUNews>>>() {
                    @Override
                    public Observable<? extends List<DUNews>> call(final DUEntitiesResult<DUNews> duNewsDUEntitiesResult) {
                        if (duNewsDUEntitiesResult.getData() == null) {
                            throw new IllegalArgumentException(DUException.PARAM_ERROR.getName());
                        }
                        return Observable.create(new Observable.OnSubscribe<List<DUNews>>() {
                            @Override
                            public void call(Subscriber<? super List<DUNews>> subscriber) {
                                subscriber.onNext(duNewsDUEntitiesResult.getData());
                            }
                        });
                    }
                });
    }

//    /**
//     * 分页获取工单任务
//     *
//     * @param userId
//     * @param startIndex
//     * @param count
//     * @return
//     */
//    public Observable<DUEntitiesResult<DUOrder>> getOrders(String userId, int startIndex, int count) {
//        return mHttpHelper.getOrders(userId, startIndex, count);
//    }

    /**
     * 工单查询
     *
     * @param taskId    任务编号
     * @param name      户名
     * @param address   地址
     * @param telephone 电话
     * @return
     */
    public Observable<DUEntitiesResult<DUOrder>> searchOrder(String taskId, String name, String address,
                                                             String telephone, String issueOrigin,
                                                             String issueType, String issueContent,
                                                             String loginStation,
                                                             String admissibleSite,
                                                             String issueArea,
                                                             long startDateUtc, long endDateUtc) {
        return mHttpHelper.searchOrder(taskId, name, address, telephone, issueOrigin,
                issueType, issueContent, loginStation, admissibleSite, issueArea,
                startDateUtc, endDateUtc);
    }

//    /**
//     * 工单处理
//     *
//     * @param requests 请求参数封装体
//     * @param userId   用户id
//     * @return
//     */
//    public Observable<DUEntityResult<DUHandleResult>> handleOrder(List<DUHandle> requests, String userId) {
//        return mHttpHelper.handleOrder(requests, userId);
//    }

//    /**
//     * 上传自开单
//     *
//     * @param requests
//     * @param userId
//     * @return
//     */
////    public Observable<DUEntitiesResult<DUCreateSelfOrderResult>> uploadCreateSelfOrder(List<DUCreateSelfOrder> requests, int userId) {
////        return mHttpHelper.uploadCreateSelfOrder(requests, userId);
////    }

//    public Observable<Boolean> updateCreateSelfOrderAndHistory(String loaclTaskId, String serverTaskId, int uploadFlag) {
//        return mDbHelper.updateCreateSelfOrderAndHistory(loaclTaskId, serverTaskId, uploadFlag);
//    }

    /**
     * initialize logger file
     */
    public void initLogger() {
        LogUtil.initLogger(mConfigHelper.getLogFilePath().getPath());
    }

    /**
     * close the logger file
     */
    public void closeLogger() {
        LogUtil.closeLogger();
    }

    /**
     * 在本地数据库分页查询我的工单
     *
     * @param offset
     * @param size
     * @return
     */
    public Observable<List<DUMyTask>> loadMyTasks(int offset, int size) {
        return mDbHelper.loadTask(getUserId(), Constant.INVALID_NUMBER, Constant.INVALID_NUMBER,
                Constant.TASK_TYPE_DOWNLOAD_ORDER, true)
                .map(new Func1<List<Task>, List<DUMyTask>>() {
                    @Override
                    public List<DUMyTask> call(List<Task> taskList) {
                        return OtoO.task2DUMyTask(taskList);
                    }
                });
    }

    public Observable<DUCreateSelfOrder> getCreateSelfOrderByTaskId(String taskId) {
        return mDbHelper.getCreateSelfOrderByTaskId(getUserId(), taskId)
                .map(new Func1<Task, DUCreateSelfOrder>() {
                    @Override
                    public DUCreateSelfOrder call(Task task) {
                        return OtoO.toCreateSelfOrder(task);
                    }
                });
    }

    /**
     * 数据库分页查询历史工单
     *
     * @param offset
     * @param size
     * @return
     */
    public Observable<List<DUHistoryTask>> loadHistoryTasks(int offset, int size) {
        return mDbHelper.loadHistoryTask(getUserId(), offset, size)
                .map(new Func1<List<HistoryTask>, List<DUHistoryTask>>() {
                    @Override
                    public List<DUHistoryTask> call(List<HistoryTask> historyTasks) {
                        return OtoO.historyTasks2DUHistoryTask(historyTasks);
                    }
                });
    }

    /**
     * 获取用户操作过的工单的所有历史记录
     *
     * @param userId
     * @param taskId
     * @return
     */
    public Observable<List<DUHistoryTask>> getHistoryTasks(int userId, String taskId) {
        return mDbHelper.getHistoryTasks(userId, taskId)
                .map(new Func1<List<HistoryTask>, List<DUHistoryTask>>() {
                    @Override
                    public List<DUHistoryTask> call(List<HistoryTask> historyTasks) {
                        return OtoO.historyTasks2DUHistoryTask(historyTasks);
                    }
                });
    }

    /**
     * 分页查询自开单记录
     *
     * @param offset
     * @param size
     * @return
     */
    public Observable<List<DUHistoryTask>> getCreateSelfOrderHistory(int offset, int size) {
        final int userId = getUserId();
        return mDbHelper.getCreateSelfOrderHistory(userId, offset, size)
                .map(new Func1<List<HistoryTask>, List<DUHistoryTask>>() {
                    @Override
                    public List<DUHistoryTask> call(List<HistoryTask> historyTasks) {
                        return OtoO.historyTasks2DUHistoryTask(historyTasks);
                    }
                });
    }

    /**
     * 保存历史工单
     *
     * @return
     */
    public Observable<Boolean> saveHistoryTask(final DUHistoryTask duhistoryTask) {
        return mDbHelper.saveHistoryTask(OtoO.dUHistoryTask2HistoryTask(duhistoryTask))
                .concatMap(new Func1<Boolean, Observable<? extends Boolean>>() {
                    @Override
                    public Observable<? extends Boolean> call(Boolean aBoolean) {
                        //增加条件的判断
                        return mDbHelper.updateMediaListFileTime(duhistoryTask.getTASK_ID(),
                                duhistoryTask.getREPLY_TIME());
                    }
                });
    }


    /**
     * 回复，处理
     *
     * @param taskId
     * @param taskType
     * @param taskState
     * @param replyTime
     * @return
     */
    public Observable<Boolean> uploadReply(String taskId, int taskType, int taskState, long replyTime) {
        final int userId = getUserId();
        return mDbHelper.getHistoryTask(userId, taskId, taskType, taskState, replyTime)
                .filter(new Func1<HistoryTask, Boolean>() {
                    @Override
                    public Boolean call(HistoryTask historyTask) {
                        return historyTask.getUPLOAD_FLAG() != Constant.HAS_UPLOADED;
                    }
                })
                .concatMap(new Func1<HistoryTask, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(final HistoryTask historyTask) {
                        return Observable.just(1)
                                .concatMap(new Func1<Integer, Observable<? extends Boolean>>() {
                                    @Override
                                    public Observable<? extends Boolean> call(Integer integer) {
                                        if (TextUtil.isNullOrEmpty(historyTask.getTASK_REPLY())) {
                                            return Observable.error(new Throwable("param is error"));
                                        }

                                        historyTask.setUPLOAD_FLAG(Constant.NO_UPLOAD);
                                        return mDbHelper.updateHistoryTask(historyTask);
                                    }
                                })
                                .concatMap(new Func1<Boolean, Observable<DUEntitiesResult<DUReplyOrderResult>>>() {
                                    @Override
                                    public Observable<DUEntitiesResult<DUReplyOrderResult>> call(Boolean aBoolean) {
                                        if (aBoolean) {
                                            return mHttpHelper.requestReply(userId, historyTask.getTASK_REPLY());
                                        } else {
                                            return Observable.error(new Throwable("update is error"));
                                        }
                                    }
                                })
                                .concatMap(new Func1<DUEntitiesResult<DUReplyOrderResult>, Observable<Boolean>>() {
                                    @Override
                                    public Observable<Boolean> call(DUEntitiesResult<DUReplyOrderResult> duReplyOrderResultDUEntitiesResult) {
                                        if ((duReplyOrderResultDUEntitiesResult.getCode() != DUEntitiesResult.SUCCESS_CODE)
                                                || (duReplyOrderResultDUEntitiesResult.getStatusCode() != Constant.STATUS_CODE_200)
                                                || (duReplyOrderResultDUEntitiesResult.getData() == null)
                                                || (duReplyOrderResultDUEntitiesResult.getData().size() != 1)) {
                                            return Observable.error(new Throwable("param is null"));
                                        }

                                        DUReplyOrderResult duReplyOrderResult =
                                                duReplyOrderResultDUEntitiesResult.getData().get(0);
                                        if (duReplyOrderResult.isIsSuccess()) {
                                            historyTask.setUPLOAD_FLAG(Constant.HAS_UPLOADED);
                                        } else {
                                            historyTask.setUPLOAD_FLAG(Constant.NO_UPLOAD);
                                        }
                                        return mDbHelper.updateHistoryTask(historyTask);
                                    }
                                })
                                .doOnError(new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        LogUtil.e(TAG, throwable.getMessage());
                                        historyTask.setUPLOAD_FLAG(Constant.NO_UPLOAD);
                                        mDbHelper.updateHistoryTask(historyTask);
                                    }
                                });
                    }
                });
    }

    /**
     * 下载工单任务(在service中进行)
     *
     * @return
     */
    public Observable<Boolean> downloadMyTasks() {
        final int userId = getUserId();
        return mHttpHelper.downloadMyTaskListFromServer(userId)
                .concatMap(new Func1<DUEntitiesResult<DUMyTask>, Observable<? extends Boolean>>() {
                    @Override
                    public Observable<? extends Boolean> call(DUEntitiesResult<DUMyTask> duMyTasksResultDUEntitiesResult) {
                        if ((duMyTasksResultDUEntitiesResult.getCode() != DUEntitiesResult.SUCCESS_CODE)
                                || (duMyTasksResultDUEntitiesResult.getStatusCode() != Constant.STATUS_CODE_200)
                                || (duMyTasksResultDUEntitiesResult.getData() == null)) {
                            return Observable.error(new Throwable("param is null"));
                        }

                        List<DUMyTask> duMyTaskList = duMyTasksResultDUEntitiesResult.getData();
                        return mDbHelper.saveAllMyTask(userId, OtoO.dUTasksResult2Tasks(duMyTaskList, userId));
                    }
                });
    }


    /**
     * 从DUReply字符串中获得DUReply集合
     *
     * @return
     */
    private List<DUReply> getDUReplyFromHistroyTask(List<String> strDUReplys) {
        Gson gson = new Gson();
        List<DUReply> duReplyList = new ArrayList<DUReply>();
        for (String str : strDUReplys) {
            DUReply duReplyTemp = gson.fromJson(str, DUReply.class);
            duReplyList.add(duReplyTemp);
        }
        return duReplyList;
    }

    public Observable<DUMyTask> queryTaskByTaskId(String taskId) {
        return mDbHelper.queryTaskByTaskId(taskId).map(new Func1<Task, DUMyTask>() {
            @Override
            public DUMyTask call(Task task) {
                return OtoO.task2DUTask(task);
            }
        });
    }

    /**
     * 下载词语信息
     *
     * @param group
     * @return
     */
    public Observable<Boolean> downloadWords(String group) {
        return mHttpHelper.downloadWords(group)
                .concatMap(new Func1<DUEntitiesResult<DUWord>, Observable<? extends Boolean>>() {
                    @Override
                    public Observable<? extends Boolean> call(DUEntitiesResult<DUWord> duWordDUEntitiesResult) {
                        if ((duWordDUEntitiesResult.getCode() != DUEntitiesResult.SUCCESS_CODE)
                                || (duWordDUEntitiesResult.getStatusCode() != Constant.STATUS_CODE_200)
                                || (duWordDUEntitiesResult.getData() == null)
                                || (duWordDUEntitiesResult.getData().size() <= 0)) {
                            return Observable.error(new Throwable("return value is error"));
                        }

                        List<DUWord> duWordList = duWordDUEntitiesResult.getData();
                        return mDbHelper.saveWords(OtoO.toWord(duWordList));
                    }
                });
    }

    /**
     * 查询第一标题的词语（处理类别）
     *
     * @return
     */
    public Observable<List<DUWord>> getFirstWords(String str) {
        return mDbHelper.getFirstWords(str)
                .map(new Func1<List<Word>, List<DUWord>>() {
                    @Override
                    public List<DUWord> call(List<Word> words) {
                        return OtoO.toDUWord(words);
                    }
                });
    }

    /**
     * 获得词语（根据parentId）
     *
     * @return
     */
    public Observable<Map<String, List<DUWord>>> getWords(String valueEx) {
        return mDbHelper.getWords(valueEx)
                .map(new Func1<Map<String, List<Word>>, Map<String, List<DUWord>>>() {
                    @Override
                    public Map<String, List<DUWord>> call(Map<String, List<Word>> stringListMap) {
                        Map<String, List<DUWord>> result = new HashMap<>();
                        for (Map.Entry<String, List<Word>> e : stringListMap.entrySet()) {
                            result.put(e.getKey(), OtoO.toDUWord(e.getValue()));
                        }
                        return result;
                    }
                });
    }

    /**
     * 压缩照片和添加水印
     *
     * @return
     */
    public Observable<Boolean> compressImageAndAddStamp(String fileName, File filePath) {
        return compressImageAndAddStamp(fileName, filePath, false);
    }

    /**
     * 压缩照片和添加水印
     *
     * @return
     */
    public Observable<Boolean> compressImageAndAddStamp(String fileName, File filePath, boolean isHighQulity) {
        return mFileHelper.compressImageAndAddStamp(fileName, filePath, isHighQulity);
    }

    /**
     * 压缩照片和添加水印
     *
     * @return
     */
    public Observable<Boolean> compressImageAndAddStamp(String fileName, File filePath, boolean isHighQulity, String address) {
        return mFileHelper.compressImageAndAddStamp(fileName, filePath, isHighQulity, address);
    }

    /**
     * 保存照片
     *
     * @param duMedia
     * @return
     */
    public Observable<Boolean> saveMedia(DUMedia duMedia) {
        return mDbHelper.saveMedia(duMedia);
    }

    /**
     * get all medias with some condition
     *
     * @param taskId
     * @param taskType
     * @param taskState
     * @param fileType
     * @return
     */
    public Observable<List<DUMedia>> getMediaList(String taskId,
                                                  int taskType,
                                                  int taskState,
                                                  int fileType) {
        return mDbHelper.getMediaList(getUserId(), taskId, taskType, taskState, fileType);
    }

    /**
     * upload all medias of one task or one history
     *
     * @param taskId
     * @param taskType
     * @param taskState
     * @return
     */
    public Observable<Boolean> deleteMediaList(final String taskId, int taskType, int taskState) {
        final int userId = getUserId();
        return mDbHelper.getMediaList(userId, taskId, taskType, taskState, DUMedia.FILE_TYPE_ALL)
                .filter(new Func1<List<DUMedia>, Boolean>() {
                    @Override
                    public Boolean call(List<DUMedia> duMediaList) {
                        LogUtils.e("------------------o" + duMediaList.size());
                        return duMediaList.size() > 0;
                    }
                })
                .concatMap(new Func1<List<DUMedia>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(final List<DUMedia> duMediaList) {
                        return Observable.just(1)
                                .concatMap(new Func1<Integer, Observable<? extends Boolean>>() {
                                    @Override
                                    public Observable<? extends Boolean> call(Integer integer) {
                                        LogUtils.e("------------------1" + duMediaList.size());
                                        return mDbHelper.deleteMediaList(duMediaList);
                                    }
                                })
                                .doOnError(new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        LogUtil.e(TAG, throwable.getMessage());
                                    }
                                });
                    }
                });
    }

    /**
     * delete a media except that the media is uploading or has been uploaded to the server
     *
     * @param id
     * @return
     */
    public Observable<Boolean> deleteMediaNew(long id) {
        return mDbHelper.getMedia(id)
                .filter(new Func1<DUMedia, Boolean>() {
                    @Override
                    public Boolean call(DUMedia duMedia) {
                        return duMedia.getUploadFlag() == UPLOAD_FLAG_LOCAL;
                    }
                })
                .concatMap(new Func1<DUMedia, Observable<? extends Boolean>>() {
                    @Override
                    public Observable<? extends Boolean> call(final DUMedia duMedia) {
                        return mDbHelper.deleteMedia(duMedia);
                    }
                });

    }

    /**
     * delete a media except that the media is uploading or has been uploaded to the server
     *
     * @param id
     * @return
     */
    public Observable<Boolean> deleteMedia(long id) {
        return mDbHelper.getMedia(id)
                .filter(new Func1<DUMedia, Boolean>() {
                    @Override
                    public Boolean call(DUMedia duMedia) {
                        return duMedia.getUploadFlag() == UPLOAD_FLAG_LOCAL;
                    }
                })
                .concatMap(new Func1<DUMedia, Observable<? extends Boolean>>() {
                    @Override
                    public Observable<? extends Boolean> call(final DUMedia duMedia) {
                        return mDbHelper.deleteMedia(duMedia)
                                .concatMap(new Func1<Boolean, Observable<? extends Boolean>>() {
                                    @Override
                                    public Observable<? extends Boolean> call(Boolean aBoolean) {
                                        if (aBoolean) {
                                            File filePath;
                                            switch (duMedia.getFileType()) {
                                                case FILE_TYPE_PICTURE:
                                                    filePath = new File(mConfigHelper.getImageFolderPath(),
                                                            duMedia.getFileName());
                                                    break;
                                                case FILE_TYPE_VOICE:
                                                    filePath = new File(mConfigHelper.getSoundFolderPath(),
                                                            duMedia.getFileName());
                                                    break;
                                                case FILE_TYPE_SCREEN_SHOT:
                                                    filePath = new File(mConfigHelper.getVideoFolderPath(),
                                                            duMedia.getFileName());
                                                    break;
                                                case FILE_TYPE_SIGNUP:
                                                    filePath = new File(mConfigHelper.getImageFolderPath(),
                                                            duMedia.getFileName());
                                                    break;
                                                default:
                                                    return Observable.error(new Throwable("failure to delete a media"));
                                            }
                                            duMedia.setFilePath(filePath.getPath());
                                            return mFileHelper.deleteFile(duMedia);
                                        } else {
                                            return Observable.error(new Throwable("failure to delete a media"));
                                        }
                                    }
                                });
                    }
                });

    }

    /**
     * upload all medias of one task or one history
     *
     * @param taskId
     * @param taskType
     * @param taskState
     * @return
     */
    public Observable<Boolean> updateMedias(final String taskId, int taskType, int taskState) {
        final int userId = getUserId();
        return mDbHelper.getMediaList(userId, taskId, taskType, taskState, DUMedia.FILE_TYPE_ALL)
                .filter(new Func1<List<DUMedia>, Boolean>() {
                    @Override
                    public Boolean call(List<DUMedia> duMediaList) {
                        return duMediaList.size() > 0;
                    }
                })
                .concatMap(new Func1<List<DUMedia>, Observable<? extends DUMedia>>() {
                    @Override
                    public Observable<? extends DUMedia> call(List<DUMedia> duMediaList) {
                        List<DUMedia> destDuMediaList = new ArrayList<DUMedia>();
                        for (DUMedia duMedia : duMediaList) {
                            if (duMedia.getUploadFlag() != Constant.HAS_UPLOADED) {
                                destDuMediaList.add(duMedia);
                            }
                        }

                        if (destDuMediaList.size() > 0) {
                            return Observable.from(destDuMediaList);
                        } else {
                            return Observable.empty();
                        }
                    }
                })
                .buffer(3)
                .concatMap(new Func1<List<DUMedia>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(final List<DUMedia> duMediaList) {
                        return Observable.just(1)
                                .concatMap(new Func1<Integer, Observable<? extends Boolean>>() {
                                    @Override
                                    public Observable<? extends Boolean> call(Integer integer) {
                                        for (DUMedia duMedia : duMediaList) {
                                            duMedia.setUploadFlag(Constant.HAS_UPLOADED);
                                        }
                                        return mDbHelper.updateMediaList(duMediaList);
                                    }
                                })
                                .doOnError(new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        LogUtil.e(TAG, throwable.getMessage());
                                        for (DUMedia duMedia : duMediaList) {
                                            duMedia.setUploadFlag(Constant.NO_UPLOAD);
                                        }
                                        mDbHelper.updateMediaList(duMediaList);
                                    }
                                });
                    }
                });
    }

    /**
     * 更新HistoryTask的
     *
     * @param duHistoryTask
     * @return
     */
    public Observable<Boolean> updateHistoryTask(DUHistoryTask duHistoryTask) {
        return mDbHelper.updateHistoryTaskReply(OtoO.dUHistoryTask2HistoryTask(duHistoryTask));
    }


    /**
     * 查询我的工单
     *
     * @param queryCondition
     * @return
     */
    public Observable<List<DUMyTask>> queryMyTask(String queryCondition) {
        return mDbHelper.queryMyTask(getUserId(), queryCondition)
                .map(new Func1<List<Task>, List<DUMyTask>>() {
                    @Override
                    public List<DUMyTask> call(List<Task> taskList) {
                        return OtoO.task2DUMyTask(taskList);
                    }
                });
    }

    /**
     * 通过历史工单的主键ID来更新
     *
     * @param duHistoryTask
     * @return
     */
    public Observable<Boolean> updateHistoryTaskByID(DUHistoryTask duHistoryTask) {
        return mDbHelper.updateHistoryTask(duHistoryTask.getID(), OtoO.dUHistoryTask2HistoryTask(duHistoryTask));
    }

    /**
     * @param taskId
     * @return
     */
    public Observable<Boolean> uploadCreateSelfOrderByTaskId(String taskId) {
        final int userId = getUserId();
        return mDbHelper.getCreateSelfOrderByTaskId(userId, taskId)
                .filter(new Func1<Task, Boolean>() {
                    @Override
                    public Boolean call(Task task) {
                        return task.getUPLOAD_FLAG() != Constant.HAS_UPLOADED;
                    }
                })
                .concatMap(new Func1<Task, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(final Task task) {
                        return Observable.just(1)
                                .concatMap(new Func1<Integer, Observable<Boolean>>() {
                                    @Override
                                    public Observable<Boolean> call(Integer integer) {
                                        task.setUPLOAD_FLAG(Constant.NO_UPLOAD);
                                        return mDbHelper.updateMyTask(userId, task, false);
                                    }
                                })
                                .concatMap(new Func1<Boolean, Observable<DUEntitiesResult<DUCreateSelfOrderResult>>>() {
                                    @Override
                                    public Observable<DUEntitiesResult<DUCreateSelfOrderResult>> call(Boolean aBoolean) {
                                        DUCreateSelfOrder duCreateSelfOrder = OtoO.toCreateSelfOrder(task);
                                        if (aBoolean && (duCreateSelfOrder != null)) {
                                            List<DUCreateSelfOrder> duCreateSelfOrderList = new ArrayList<>();
                                            duCreateSelfOrderList.add(duCreateSelfOrder);
                                            return mHttpHelper.uploadCreateSelfOrder(duCreateSelfOrderList, userId);
                                        } else {
                                            return Observable.error(new Throwable("update is error"));
                                        }
                                    }
                                })
                                .concatMap(new Func1<DUEntitiesResult<DUCreateSelfOrderResult>, Observable<Boolean>>() {
                                    @Override
                                    public Observable<Boolean> call(DUEntitiesResult<DUCreateSelfOrderResult>
                                                                            duCreateSelfOrderResultDUEntitiesResult) {
                                        if ((duCreateSelfOrderResultDUEntitiesResult.getCode() != DUEntitiesResult.SUCCESS_CODE)
                                                || (duCreateSelfOrderResultDUEntitiesResult.getStatusCode() != Constant.STATUS_CODE_200)
                                                || (duCreateSelfOrderResultDUEntitiesResult.getData() == null)
                                                || (duCreateSelfOrderResultDUEntitiesResult.getData().size() != 1)) {
                                            return Observable.error(new Throwable("param is null"));
                                        }

                                        DUCreateSelfOrderResult duCreateSelfOrderResult
                                                = duCreateSelfOrderResultDUEntitiesResult.getData().get(0);
                                        boolean isSuccess = duCreateSelfOrderResult.isSuccess();
                                        String localTaskId = duCreateSelfOrderResult.getLocalTaskId();
                                        String serverTaskId = duCreateSelfOrderResult.getServerTaskId();
                                        if (isSuccess
                                                && !TextUtil.isNullOrEmpty(localTaskId)
                                                && !TextUtil.isNullOrEmpty(serverTaskId)) {
                                            String extend =
                                                    OtoO.add2Extend(task.getEXTEND(), Constant.SERVER_TASK_ID, serverTaskId);
                                            if (!TextUtil.isNullOrEmpty(extend)) {
                                                task.setEXTEND(extend);
                                                task.setUPLOAD_FLAG(Constant.HAS_UPLOADED);
                                            } else {
                                                task.setUPLOAD_FLAG(Constant.NO_UPLOAD);
                                            }
                                        } else {
                                            task.setUPLOAD_FLAG(Constant.NO_UPLOAD);
                                        }

                                        return mDbHelper.updateMyTask(userId, task, true);
                                    }
                                })
                                .doOnError(new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        LogUtil.e(TAG, throwable.getMessage());
                                        task.setUPLOAD_FLAG(Constant.NO_UPLOAD);
                                        mDbHelper.updateMyTask(userId, task, false);
                                    }
                                });
                    }
                });
    }

    /**
     * @param offset
     * @param limit
     * @return
     */
    public Observable<Boolean> uploadAllCreateSelfOrderList(int offset, int limit) {
        final int userId = getUserId();
        List<Integer> taskTypeList = new ArrayList<>();
        taskTypeList.add(Constant.TASK_TYPE_CREATE_SELF_ORDER);
        return mDbHelper.getTaskList(userId, taskTypeList, offset, limit)
                .filter(new Func1<List<Task>, Boolean>() {
                    @Override
                    public Boolean call(List<Task> tasks) {
                        return tasks.size() > 0;
                    }
                })
                .map(new Func1<List<Task>, List<Task>>() {
                    @Override
                    public List<Task> call(List<Task> tasks) {
                        List<Task> destTaskList = new ArrayList<>();
                        for (Task task : tasks) {
                            if (task.getUPLOAD_FLAG() != Constant.HAS_UPLOADED) {
                                destTaskList.add(task);
                            }
                        }
                        return destTaskList;
                    }
                })
                .concatMap(new Func1<List<Task>, Observable<? extends Boolean>>() {
                    @Override
                    public Observable<? extends Boolean> call(final List<Task> tasks) {
                        if (tasks.size() <= 0) {
                            return Observable.create(new Observable.OnSubscribe<Boolean>() {
                                @Override
                                public void call(Subscriber<? super Boolean> subscriber) {
                                    if (subscriber.isUnsubscribed()) {
                                        return;
                                    }

                                    subscriber.onNext(true);
                                    subscriber.onCompleted();
                                }
                            });
                        }

                        return Observable.just(1)
                                .concatMap(new Func1<Integer, Observable<? extends Boolean>>() {
                                    @Override
                                    public Observable<? extends Boolean> call(Integer integer) {
                                        for (Task task : tasks) {
                                            task.setUPLOAD_FLAG(Constant.NO_UPLOAD);
                                        }
                                        return mDbHelper.updateMyTaskList(userId, tasks, false);
                                    }
                                })
                                .concatMap(new Func1<Boolean, Observable<? extends DUEntitiesResult<DUCreateSelfOrderResult>>>() {
                                    @Override
                                    public Observable<? extends DUEntitiesResult<DUCreateSelfOrderResult>> call(Boolean aBoolean) {
                                        if (!aBoolean) {
                                            return Observable.error(new Throwable("update is error"));
                                        }
//                                        if (aBoolean) {
                                        List<DUCreateSelfOrder> duCreateSelfOrderList = new ArrayList<>();
                                        for (Task task : tasks) {
                                            duCreateSelfOrderList.add(OtoO.toCreateSelfOrder(task));
                                        }
                                        return mHttpHelper.uploadCreateSelfOrder(duCreateSelfOrderList, userId);
//                                        } else {
//
//                                        }
                                    }
                                })
                                .concatMap(new Func1<DUEntitiesResult<DUCreateSelfOrderResult>, Observable<Boolean>>() {
                                    @Override
                                    public Observable<Boolean> call(DUEntitiesResult<DUCreateSelfOrderResult> duCreateSelfOrderResultDUEntitiesResult) {
                                        if ((duCreateSelfOrderResultDUEntitiesResult.getCode() != DUEntitiesResult.SUCCESS_CODE)
                                                || (duCreateSelfOrderResultDUEntitiesResult.getStatusCode() != Constant.STATUS_CODE_200)
                                                || (duCreateSelfOrderResultDUEntitiesResult.getData() == null)
                                                || (duCreateSelfOrderResultDUEntitiesResult.getData().size() != tasks.size())) {
                                            return Observable.error(new Throwable("param is null"));
                                        }

                                        List<DUCreateSelfOrderResult> duCreateSelfOrderResultList
                                                = duCreateSelfOrderResultDUEntitiesResult.getData();
                                        for (int i = 0; i < tasks.size(); i++) {
                                            DUCreateSelfOrderResult duCreateSelfOrderResult = duCreateSelfOrderResultList.get(i);
                                            Task task = tasks.get(i);
                                            boolean isSuccess = duCreateSelfOrderResult.isSuccess();
                                            String localTaskId = duCreateSelfOrderResult.getLocalTaskId();
                                            String serverTaskId = duCreateSelfOrderResult.getServerTaskId();

                                            if (isSuccess
                                                    && !TextUtil.isNullOrEmpty(localTaskId)
                                                    && !TextUtil.isNullOrEmpty(serverTaskId)) {
                                                String extend =
                                                        OtoO.add2Extend(task.getEXTEND(), Constant.SERVER_TASK_ID, serverTaskId);
                                                if (!TextUtil.isNullOrEmpty(extend)) {
                                                    task.setEXTEND(extend);
//                                                    task.setTASK_ID(serverTaskId);
                                                    task.setUPLOAD_FLAG(Constant.HAS_UPLOADED);
                                                } else {
                                                    task.setUPLOAD_FLAG(Constant.NO_UPLOAD);
                                                }
                                            } else {
                                                task.setUPLOAD_FLAG(Constant.NO_UPLOAD);
                                            }
                                        }

                                        return mDbHelper.updateMyTaskList(userId, tasks, true);
                                    }
                                })
                                .doOnError(new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        LogUtil.e(TAG, throwable.getMessage());
                                        for (Task task : tasks) {
                                            task.setUPLOAD_FLAG(Constant.NO_UPLOAD);
                                        }
                                        mDbHelper.updateMyTaskList(userId, tasks, false);
                                    }
                                });
                    }
                });
    }

    /**
     * 上传一条工单的多条历史数据
     *
     * @param taskId
     * @return
     */
    public Observable<Boolean> uploadManyReplies(String taskId) {
        final int userId = getUserId();
        return mDbHelper.getHistoryTasks(userId, taskId, Constant.HAS_UPLOADED)
                .filter(new Func1<List<HistoryTask>, Boolean>() {
                    @Override
                    public Boolean call(List<HistoryTask> historyTaskList) {
                        return historyTaskList.size() > 0;
                    }
                })
                .map(new Func1<List<HistoryTask>, List<HistoryTask>>() {
                    @Override
                    public List<HistoryTask> call(List<HistoryTask> historyTaskList) {
                        List<HistoryTask> destHistoryTaskList = new ArrayList<>();
                        for (HistoryTask historyTask : historyTaskList) {
                            if (TextUtil.isNullOrEmpty(historyTask.getTASK_REPLY())) {
                                continue;
                            }
                            destHistoryTaskList.add(historyTask);
                        }
                        return destHistoryTaskList;
                    }
                })
                .concatMap(new Func1<List<HistoryTask>, Observable<? extends Boolean>>() {
                               @Override
                               public Observable<? extends Boolean> call(final List<HistoryTask> historyTaskList) {
                                   return Observable.just(1)
                                           .concatMap(new Func1<Integer, Observable<? extends Boolean>>() {
                                                          @Override
                                                          public Observable<? extends Boolean> call(Integer integer) {
                                                              for (HistoryTask historyTask : historyTaskList) {
                                                                  historyTask.setUPLOAD_FLAG(Constant.NO_UPLOAD);//正在上传
                                                              }
                                                              return mDbHelper.updateHistoryTaskList(historyTaskList);
                                                          }
                                                      }
                                           )
                                           .concatMap(new Func1<Boolean, Observable<DUEntitiesResult<DUReplyOrderResult>>>() {
                                               @Override
                                               public Observable<DUEntitiesResult<DUReplyOrderResult>> call(Boolean aBoolean) {
                                                   if (!aBoolean) {
                                                       return Observable.error(new Throwable("update is error"));
                                                   }

                                                   List<String> duReplyList = new ArrayList<>();
                                                   for (HistoryTask historyTask : historyTaskList) {
                                                       duReplyList.add(TextUtil.getString(historyTask.getTASK_REPLY()));
                                                   }
                                                   return mHttpHelper.requestReplyList(userId, duReplyList);
                                               }
                                           })
                                           .concatMap(new Func1<DUEntitiesResult<DUReplyOrderResult>, Observable<? extends Boolean>>() {
                                               @Override
                                               public Observable<? extends Boolean> call(DUEntitiesResult<DUReplyOrderResult> duReplyOrderResultDUEntitiesResult) {
                                                   if ((duReplyOrderResultDUEntitiesResult.getCode() != DUEntitiesResult.SUCCESS_CODE)
                                                           || (duReplyOrderResultDUEntitiesResult.getStatusCode() != Constant.STATUS_CODE_200)
                                                           || (duReplyOrderResultDUEntitiesResult.getData() == null)
                                                           || (duReplyOrderResultDUEntitiesResult.getData().size() != historyTaskList.size())) {
                                                       return Observable.error(new Throwable("param is null"));
                                                   }

                                                   for (int i = 0; i < duReplyOrderResultDUEntitiesResult.getData().size(); i++) {
                                                       DUReplyOrderResult result = duReplyOrderResultDUEntitiesResult.getData().get(i);
                                                       HistoryTask historyTask = historyTaskList.get(i);
                                                       if (result.getTaskId().equals(historyTask.getTASK_ID())) { // && result.getTaskState().equals(String.valueOf(historyTask.getTASK_STATE()))
                                                           if (result.getIsSuccess()) {
                                                               historyTask.setUPLOAD_FLAG(Constant.HAS_UPLOADED);
                                                           } else {
                                                               historyTask.setUPLOAD_FLAG(Constant.NO_UPLOAD);
                                                           }
                                                       } else {
                                                           try {
                                                               JSONObject jsonObject = new JSONObject(historyTask.getTASK_REPLY());
                                                               String taskId = jsonObject.getString("taskId");
                                                               if (!TextUtil.isNullOrEmpty(taskId)
                                                                       && taskId.equals(result.getTaskId())) {
                                                                   if (result.getIsSuccess()) {
                                                                       historyTask.setUPLOAD_FLAG(Constant.HAS_UPLOADED);
                                                                   } else {
                                                                       historyTask.setUPLOAD_FLAG(Constant.NO_UPLOAD);
                                                                   }
                                                               }
                                                           } catch (JSONException e) {
                                                               e.printStackTrace();
                                                           }
                                                       }
                                                   }
                                                   return mDbHelper.updateHistoryTaskList(historyTaskList);
                                               }
                                           })
                                           .doOnError(new Action1<Throwable>() {
                                               @Override
                                               public void call(Throwable throwable) {
                                                   LogUtil.e(TAG, throwable.getMessage());
                                                   for (HistoryTask historyTask : historyTaskList) {
                                                       historyTask.setUPLOAD_FLAG(Constant.NO_UPLOAD);
                                                   }
                                                   mDbHelper.updateHistoryTaskList(historyTaskList);
                                               }
                                           });
                               }
                           }

                );
    }

    /**
     * upload all medias of one task or one history
     *
     * @param taskId
     * @param taskType
     * @param taskState
     * @return
     */
    public Observable<Boolean> uploadMedias(final String taskId, int taskType, int taskState) {
        final int userId = getUserId();
        return mDbHelper.getMediaList(userId, taskId, taskType, taskState, DUMedia.FILE_TYPE_ALL)
                .filter(new Func1<List<DUMedia>, Boolean>() {
                    @Override
                    public Boolean call(List<DUMedia> duMediaList) {
                        return duMediaList.size() > 0;
                    }
                })
                .concatMap(new Func1<List<DUMedia>, Observable<? extends DUMedia>>() {
                    @Override
                    public Observable<? extends DUMedia> call(List<DUMedia> duMediaList) {
                        List<DUMedia> destDuMediaList = new ArrayList<DUMedia>();
                        for (DUMedia duMedia : duMediaList) {
                            if (duMedia.getUploadFlag() != Constant.HAS_UPLOADED) {
                                destDuMediaList.add(duMedia);
                            }
                        }

                        if (destDuMediaList.size() > 0) {
                            return Observable.from(destDuMediaList);
                        } else {
                            return Observable.empty();
                        }
                    }
                })
                .buffer(3)
                .concatMap(new Func1<List<DUMedia>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(final List<DUMedia> duMediaList) {
                        return Observable.just(1)
                                .concatMap(new Func1<Integer, Observable<? extends Boolean>>() {
                                    @Override
                                    public Observable<? extends Boolean> call(Integer integer) {
                                        for (DUMedia duMedia : duMediaList) {
                                            duMedia.setUploadFlag(Constant.HAS_UPLOADED);
                                        }
                                        return mDbHelper.updateMediaList(duMediaList);
                                    }
                                })
                                .concatMap(new Func1<Boolean, Observable<? extends DUEntityResult>>() {
                                    @Override
                                    public Observable<? extends DUEntityResult> call(Boolean aBoolean) {
                                        if (aBoolean) {
                                            String serverTaskId = taskId;
                                            List<DUFile> duFileList = new ArrayList<DUFile>();
                                            for (DUMedia duMedia : duMediaList) {
                                                if (TextUtil.isNullOrEmpty(duMedia.getFileName())
                                                        || TextUtil.isNullOrEmpty(duMedia.getFileUrl())
                                                        || TextUtil.isNullOrEmpty(duMedia.getFileHash())) {
                                                    continue;
                                                }

                                                String fileType;
                                                switch (duMedia.getFileType()) {
                                                    case FILE_TYPE_PICTURE:
                                                        fileType = PICTURE_SUFFIX;
                                                        break;
                                                    case FILE_TYPE_VOICE:
                                                        fileType = VOICE_SUFFIX;
                                                        break;
                                                    case FILE_TYPE_SIGNUP:
                                                        fileType = SIGNUP_SUFFIX;
                                                        break;
                                                    case FILE_TYPE_SCREEN_SHOT:
                                                        fileType = VIDEO_SUFFIX;
                                                        break;
                                                    default:
                                                        fileType = null;
                                                        break;
                                                }

                                                if (fileType == null) {
                                                    continue;
                                                }

                                                DUFile duFile = new DUFile(
                                                        duMedia.getFileUrl(),
                                                        fileType,
                                                        0,
                                                        duMedia.getFileHash(),
                                                        null,
                                                        duMedia.getFileTime(),
                                                        duMedia.getTaskState());
                                                duMedia.setUploadFlag(Constant.HAS_UPLOADED); // sign the candidate items
                                                if (!TextUtil.isNullOrEmpty(duMedia.getExtend())) {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(duMedia.getExtend());
                                                        if (!TextUtil.isNullOrEmpty(jsonObject.getString("serverTaskId"))) {
                                                            serverTaskId = jsonObject.getString("serverTaskId");
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                duFileList.add(duFile);
                                            }

                                            return mHttpHelper.uploadMediaRelations(serverTaskId, duFileList);
                                        } else {
                                            return Observable.error(new Throwable("update is error"));
                                        }
                                    }
                                })
                                .concatMap(new Func1<DUEntityResult, Observable<? extends Boolean>>() {
                                    @Override
                                    public Observable<? extends Boolean> call(DUEntityResult duEntityResult) {
                                        return mDbHelper.updateMediaList(duMediaList);
                                    }
                                })
                                .doOnError(new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        LogUtil.e(TAG, throwable.getMessage());
                                        for (DUMedia duMedia : duMediaList) {
                                            duMedia.setUploadFlag(Constant.NO_UPLOAD);
                                        }
                                        mDbHelper.updateMediaList(duMediaList);
                                    }
                                });
                    }
                });
    }

    /**
     * upload all medias in db
     *
     * @return
     */
    public Observable<Boolean> uploadAllMedias(int taskType) {
        final int invalidValue = -1;
        return mDbHelper.loadTask(getUserId(), invalidValue, invalidValue, taskType, false)
                .concatMap(new Func1<List<Task>, Observable<Task>>() {
                    @Override
                    public Observable<Task> call(final List<Task> tasks) {
                        return Observable.create(new Observable.OnSubscribe<Task>() {
                            @Override
                            public void call(Subscriber<? super Task> subscriber) {
                                if (subscriber.isUnsubscribed()) {
                                    return;
                                }

                                for (Task task : tasks) {
                                    subscriber.onNext(task);
                                }
                                subscriber.onCompleted();
                            }
                        });
                    }
                })
                .concatMap(new Func1<Task, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Task task) {
                        return uploadMedias(task.getTASK_ID(), task.getTASK_TYPE(), Constant.TASK_STATE_ALL);
                    }
                });
    }


    /**
     * 上传本地所有历史记录（按照任务类型区分自开单还是下载的工单）
     *
     * @param userId
     * @param taskType 任务类型，0代表下载来的工单，1代表自开单
     * @return
     */
    public Observable<Boolean> uploadAllHistoryTask(final int userId, int taskType) {
        return mDbHelper.getAllHistoryTask(getUserId(), taskType)
                .filter(new Func1<List<HistoryTask>, Boolean>() {
                    @Override
                    public Boolean call(List<HistoryTask> historyTaskList) {
                        return historyTaskList.size() > 0;
                    }
                })
                .map(new Func1<List<HistoryTask>, List<HistoryTask>>() {
                    @Override
                    public List<HistoryTask> call(List<HistoryTask> historyTaskList) {
                        List<HistoryTask> destHistoryTaskList = new ArrayList<>();
                        for (HistoryTask historyTask : historyTaskList) {
                            if (TextUtil.isNullOrEmpty(historyTask.getTASK_REPLY())) {
                                continue;
                            }
                            destHistoryTaskList.add(historyTask);
                        }
                        return destHistoryTaskList;
                    }
                })
                .concatMap(new Func1<List<HistoryTask>, Observable<? extends Boolean>>() {
                               @Override
                               public Observable<? extends Boolean> call(final List<HistoryTask> historyTaskList) {
                                   return Observable.just(1)
                                           .concatMap(new Func1<Integer, Observable<? extends Boolean>>() {
                                                          @Override
                                                          public Observable<? extends Boolean> call(Integer integer) {
                                                              for (HistoryTask historyTask : historyTaskList) {
                                                                  historyTask.setUPLOAD_FLAG(Constant.NO_UPLOAD);//正在上传
                                                              }
                                                              return mDbHelper.updateHistoryTaskList(historyTaskList);
                                                          }
                                                      }
                                           )
                                           .concatMap(new Func1<Boolean, Observable<DUEntitiesResult<DUReplyOrderResult>>>() {
                                               @Override
                                               public Observable<DUEntitiesResult<DUReplyOrderResult>> call(Boolean aBoolean) {
                                                   if (!aBoolean) {
                                                       return Observable.error(new Throwable("update is error"));
                                                   }

                                                   List<String> duReplyList = new ArrayList<>();
                                                   for (HistoryTask historyTask : historyTaskList) {
                                                       duReplyList.add(TextUtil.getString(historyTask.getTASK_REPLY()));
                                                   }
                                                   return mHttpHelper.requestReplyList(getUserId(), duReplyList);
                                               }
                                           })
                                           .concatMap(new Func1<DUEntitiesResult<DUReplyOrderResult>, Observable<? extends Boolean>>() {
                                               @Override
                                               public Observable<? extends Boolean> call(DUEntitiesResult<DUReplyOrderResult> duReplyOrderResultDUEntitiesResult) {
                                                   if ((duReplyOrderResultDUEntitiesResult.getCode() != DUEntitiesResult.SUCCESS_CODE)
                                                           || (duReplyOrderResultDUEntitiesResult.getStatusCode() != Constant.STATUS_CODE_200)
                                                           || (duReplyOrderResultDUEntitiesResult.getData() == null)
                                                           || (duReplyOrderResultDUEntitiesResult.getData().size() != historyTaskList.size())) {
                                                       return Observable.error(new Throwable("param is null"));
                                                   }

                                                   for (int i = 0; i < duReplyOrderResultDUEntitiesResult.getData().size(); i++) {
                                                       DUReplyOrderResult result = duReplyOrderResultDUEntitiesResult.getData().get(i);
                                                       HistoryTask historyTask = historyTaskList.get(i);
                                                       if (result.getTaskId().equals(historyTask.getTASK_ID())) { // && result.getTaskState().equals(String.valueOf(historyTask.getTASK_STATE()))
                                                           if (result.getIsSuccess()) {
                                                               historyTask.setUPLOAD_FLAG(Constant.HAS_UPLOADED);
                                                           } else {
                                                               historyTask.setUPLOAD_FLAG(Constant.NO_UPLOAD);
                                                           }
                                                       } else {
                                                           try {
                                                               JSONObject jsonObject = new JSONObject(historyTask.getTASK_REPLY());
                                                               String taskId = jsonObject.getString("taskId");
                                                               if (!TextUtil.isNullOrEmpty(taskId)
                                                                       && taskId.equals(result.getTaskId())) {
                                                                   if (result.getIsSuccess()) {
                                                                       historyTask.setUPLOAD_FLAG(Constant.HAS_UPLOADED);
                                                                   } else {
                                                                       historyTask.setUPLOAD_FLAG(Constant.NO_UPLOAD);
                                                                   }
                                                               }
                                                           } catch (JSONException e) {
                                                               e.printStackTrace();
                                                           }
                                                       }
                                                   }
                                                   return mDbHelper.updateHistoryTaskList(historyTaskList);
                                               }
                                           })
                                           .doOnError(new Action1<Throwable>() {
                                               @Override
                                               public void call(Throwable throwable) {
                                                   LogUtil.e(TAG, throwable.getMessage());
                                                   for (HistoryTask historyTask : historyTaskList) {
                                                       historyTask.setUPLOAD_FLAG(Constant.NO_UPLOAD);
                                                   }
                                                   mDbHelper.updateHistoryTaskList(historyTaskList);
                                               }
                                           });
                               }
                           }
                );
    }

    public Observable<Boolean> updateTaskReplyFromCreateSelf(final String taskId) {
        return mDbHelper.queryTaskByTaskId(taskId).map(new Func1<Task, DUMyTask>() {
            @Override
            public DUMyTask call(Task task) {
                return OtoO.task2DUTask(task);
            }
        }).concatMap(new Func1<DUMyTask, Observable<? extends Boolean>>() {
            @Override
            public Observable<? extends Boolean> call(DUMyTask duMyTask) {
                String serverTaskId = "";
                try {
                    JSONObject jsonObject = new JSONObject(duMyTask.getExtend());
                    if (!TextUtil.isNullOrEmpty(jsonObject.getString("serverTaskId"))) {
                        serverTaskId = jsonObject.getString("serverTaskId");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return mDbHelper.updateHistoryTaskReplyCreateSelf(taskId, serverTaskId);
            }
        });
    }

    /**
     * 自开单跳转到处理工单
     *
     * @param taskId
     * @return
     */
    public Observable<List<DUHistoryTask>> loadTaskHandle(String taskId) {
        return mDbHelper.getTaskHandleCreateSelf(getUserId(), taskId)
                .map(new Func1<List<HistoryTask>, List<DUHistoryTask>>() {
                    @Override
                    public List<DUHistoryTask> call(List<HistoryTask> historyTaskList) {
                        return OtoO.historyTasks2DUHistoryTask(historyTaskList);
                    }
                });
    }

    /**
     * 查询工单流程信息
     *
     * @param taskId
     * @return
     */
    public Observable<List<DUProcess>> getOrderProcessInfo(String taskId) {
        return mHttpHelper.getOrderProcessInfo(taskId)
                .map(new Func1<DUEntitiesResult<DUProcess>, List<DUProcess>>() {
                    @Override
                    public List<DUProcess> call(DUEntitiesResult<DUProcess> duProcessDUEntitiesResult) {
                        if ((duProcessDUEntitiesResult.getCode() == DUEntitiesResult.SUCCESS_CODE)
                                && (duProcessDUEntitiesResult.getStatusCode() == Constant.STATUS_CODE_200)) {
                            return duProcessDUEntitiesResult.getData();
                        }
                        return null;
                    }
                });
    }

    /**
     * save pushed message
     *
     * @param isNewTask
     * @param jsonArray
     * @return
     * @throws Exception
     */
    public Observable<Boolean> saveMessage(boolean isNewTask, JSONArray jsonArray) throws Exception {
        Gson gson = new Gson();
        int userId = getUserId();
        if (isNewTask) {//是新任务
            List<DUMyTask> duMyTaskList = gson.fromJson(jsonArray.toString(),
                    new TypeToken<ArrayList<DUMyTask>>() {
                    }.getType());
            List<Task> tasks = OtoO.dUTasksResult2Tasks(duMyTaskList, userId);
            return mDbHelper.saveAllMyTask(userId, tasks);
        } else { //任务更新
            return Observable.error(new Throwable("error"));
        }
    }

    /**
     * 清除历史记录
     *
     * @return
     */
    public Observable<Boolean> clearHistory() {
        return Observable.merge(mConfigHelper.clearLogFiles(),
                mConfigHelper.clearImageFiles(),
                mConfigHelper.clearSoundFiles(),
                mDbHelper.clearData());
    }

    public Observable<Boolean> uploadLogFiles() {
        return mHttpHelper.uploadLogFiles();
    }

    /**
     * 删除过期的历史工单
     *
     * @param currentTime  当前时间
     * @param intervalTime 时间段（30天）
     * @return
     */
    public Observable<Boolean> deleteHistoryTasks(long currentTime, long intervalTime) {
        return mDbHelper.deleteHistoryTask(getUserId(), currentTime, intervalTime)
                .filter(new Func1<List<MultiMedia>, Boolean>() {
                    @Override
                    public Boolean call(List<MultiMedia> multiMediaList) {
                        return multiMediaList != null && multiMediaList.size() > 0;
                    }
                })
                .concatMap(new Func1<List<MultiMedia>, Observable<DUMedia>>() {
                    @Override
                    public Observable<DUMedia> call(final List<MultiMedia> multiMediaList) {
                        return Observable.create(new Observable.OnSubscribe<DUMedia>() {
                            @Override
                            public void call(Subscriber<? super DUMedia> subscriber) {
                                if (subscriber.isUnsubscribed()) {
                                    return;
                                }
                                for (DUMedia duMedia : OtoO.multiMedia2DUMedias(multiMediaList)) {
                                    subscriber.onNext(duMedia);
                                }
                                subscriber.onCompleted();
                            }
                        });
                    }
                })
                .concatMap(new Func1<DUMedia, Observable<? extends Boolean>>() {
                    @Override
                    public Observable<? extends Boolean> call(DUMedia duMedia) {
                        return mFileHelper.deleteFileRegular(duMedia);
                    }
                });
    }

    /**
     * 获取临期工单
     *
     * @return
     */
    public Observable<List<DUMyTask>> queryDeadlineTasks(long currentTime) {
        if (currentTime == 0) {
            currentTime = new Date().getTime();
        }
//        return GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
//                .getDUMyTaskDao().queryBuilder()
//                .where(DUMyTaskDao.Properties.ClsxLong.gt(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
//                .list();
        return mDbHelper.queryDeadLineTasks(currentTime, getUserId())
                .map(new Func1<List<Task>, List<DUMyTask>>() {
                    @Override
                    public List<DUMyTask> call(List<Task> taskList) {
                        return OtoO.task2DUMyTask(taskList);
                    }
                });
    }

    public Observable<Boolean> isAlertDeadlineTasks() {
        return mDbHelper.isExistDeadlineTask(getUserId());
    }

    /**
     * 获取所有站点
     *
     * @return
     */
    public Observable<List<DUWord>> getAllStations() {
        return mDbHelper.getAllStations()
                .map(new Func1<List<Word>, List<DUWord>>() {
                    @Override
                    public List<DUWord> call(List<Word> words) {
                        List<DUWord> all = OtoO.toDUWord(words);
                        List<DUWord> stationGroupList = new ArrayList<>();
                        List<DUWord> temp = new ArrayList<>();
                        for (DUWord duWord : all) {
                            if (duWord.getParentId().equals("1")) {
                                stationGroupList.add(duWord);
                            } else {
                                temp.add(duWord);
                            }
                        }

                        for (DUWord groupStation : stationGroupList) {
                            List<DUWord> childStations = new ArrayList<>();
                            for (DUWord tempStation : temp) {
                                if (groupStation.getValue().equals(tempStation.getParentId())) {
                                    childStations.add(tempStation);
                                }
                            }
                            groupStation.setDuWordList(childStations);
                        }
                        return stationGroupList;
                    }
                });
    }

    /**
     * 获得“供水热线”
     *
     * @return
     */
    public Observable<DUWord> getHotlineStation() {
        return mDbHelper.getHotlineStation()
                .map(new Func1<Word, DUWord>() {
                    @Override
                    public DUWord call(Word word) {
                        return OtoO.toDUWord(word);
                    }
                });
    }
}
