package com.sh3h.hotline.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.maning.mndialoglibrary.MProgressDialog;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.dataprovider.data.entity.newentity.HandleOrderEntity;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.data.entity.response.ServiceResultEntity;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.DUMyTaskDao;
import com.sh3h.dataprovider.greendaoDao.HandleOrderEntityDao;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.entity.UploadSuccess;
import com.sh3h.hotline.entity.VideosPathEntity;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.order.TaskState;
import com.sh3h.hotline.util.AndroidComponentUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.body.ProgressResponseCallBack;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_VOICE;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/4/10 09:57
 */
public class UploadDataService extends Service {


    private static final String TAG = "ZikaidanUploadService";
    CompositeSubscription mSubscription;
    @Inject
    DataManager mDataManager;
    @Inject
    ConfigHelper mConfigHelper;
    @Inject
    EventPosterHelper mEventPosterHelper;
    //    private List<File> uploadFiles;
    private boolean isFromMyTask;

    @Override
    public void onCreate() {
        super.onCreate();
        MainApplication.get(this).getComponent().inject(this);
        mSubscription = new CompositeSubscription();
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, UploadDataService.class);
    }

    public static boolean isRunning(Context context) {
        return AndroidComponentUtil.isServiceRunning(context, UploadDataService.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
//            uploadFiles = new ArrayList<>();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                isFromMyTask = bundle.getBoolean(SyncConst.SYNC_IS_FROM_MY_TASK, false);
                commitAllData();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 提交数据
     */
    private void commitAllData() {
        LogUtil.e(TAG, "UploadDataService UploadData");
        final AsyncSession asyncSession = GreenDaoUtils.getAsyncSession(MainApplication.getInstance());
        asyncSession.queryList(GreenDaoUtils.getDaoSession(this)
                .getDUMyTaskDao().queryBuilder()
                .where(DUMyTaskDao.Properties.TaskState.eq(Constant.ORIGIN_MY_TASK_HISTORY))
                .where(DUMyTaskDao.Properties.IsUploadSuccess.eq(Constant.NO_UPLOAD))
                .build());
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation asyncOperation) {
                if (asyncOperation.getType() == AsyncOperation.OperationType.QueryList) {
                    List<DUMyTask> duMyTasks = (List<DUMyTask>) asyncOperation.getResult();
                    if (duMyTasks != null && duMyTasks.size() > 0) {
                        List<String> faIds = new ArrayList<>();
                        for (DUMyTask duMyTask : duMyTasks) {
                            faIds.add(duMyTask.getFaId());
                        }
                        List<HandleOrderEntity> list = GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                .getHandleOrderEntityDao().queryBuilder()
                                .where(HandleOrderEntityDao.Properties.FaId.in(faIds.toArray()))
                                .list();

                        loadMultimeDiaInfo(duMyTasks, list);
                    } else {
                        mEventPosterHelper.postEventSafely(new UIBusEvent.NotifyHistoryTasksUI());
                    }
                }
            }
        });
    }

    /**
     * 加载多媒体数据
     *
     * @param newList
     */
    private void loadMultimeDiaInfo(List<DUMyTask> newList, List<HandleOrderEntity> list) {
        for (int i = 0; i < newList.size(); i++) {
            List<File> fileList = new ArrayList<>();
            loadImages(fileList, newList.get(i).getFaId(), Constant.TASK_TYPE_DOWNLOAD_ORDER, TaskState.HANDLE, list.get(i), newList.get(i));
        }
    }

    /**
     * 加载本地图片
     *
     * @param fileList
     * @param taskId
     * @param taskType
     * @param taskState
     */
    public void loadImages(final List<File> fileList, final String taskId, final int taskType, final int taskState,
                           final HandleOrderEntity handleOrderEntity, final DUMyTask duMyTask) {
        if (TextUtil.isNullOrEmpty(taskId)) {
//            getMvpView().onError("param is null");
            LogUtil.e(TAG, "param is null");
            return;
        }

        LogUtil.i(TAG, "loadImages");
        mSubscription.add(mDataManager.getMediaList(taskId, taskType,
                taskState, DUMedia.FILE_TYPE_PICTURE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUMedia>>() {
                    @Override
                    public void onCompleted() {
                        loadVoices(fileList, taskId, taskType, taskState, handleOrderEntity, duMyTask);
                        LogUtil.i(TAG, "loadImages onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "loadImages onError " + e.getMessage());
//                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<DUMedia> duMediaList) {
                        LogUtil.i(TAG, "loadImages onNext" + duMediaList.size());

                        for (DUMedia duMedia : duMediaList) {
                            String fileName = duMedia.getFileName();
                            if (TextUtil.isNullOrEmpty(fileName)) {
                                continue;
                            }

                            File file = new File(getImagePath(), fileName);
                            LogUtils.e("fileName", fileName);
                            if (file.exists()) {
                                fileList.add(file);
                                duMedia.setFilePath(file.getPath());
                            }
                        }
//                        getMvpView().onLoadImages(duMediaList);
                    }
                }));
    }

    public File getSoundPath() {
        return mConfigHelper.getSoundFolderPath();
    }

    /**
     * 扫描本地录音
     *
     * @param fileList
     * @param taskId
     * @param taskType
     * @param taskState
     */
    public void loadVoices(final List<File> fileList, final String taskId, final int taskType, final int taskState,
                           final HandleOrderEntity handleOrderEntity, final DUMyTask duMyTask) {
        if (TextUtil.isNullOrEmpty(taskId)) {
//            getMvpView().onError("param is null");
            LogUtil.e(TAG, "param is null");
            return;
        }

        LogUtil.i(TAG, "loadVoices");
        mSubscription.add(mDataManager.getMediaList(taskId, taskType,
                taskState, FILE_TYPE_VOICE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUMedia>>() {
                    @Override
                    public void onCompleted() {
                        loadVideos(fileList, taskId, taskType, taskState, handleOrderEntity, duMyTask);
                        LogUtil.i(TAG, "loadVoices onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "loadVoices onError " + e.getMessage());
                    }

                    @Override
                    public void onNext(List<DUMedia> duMediaList) {
                        LogUtil.i(TAG, "loadImages onNext");

                        for (DUMedia duMedia : duMediaList) {
                            String fileName = duMedia.getFileName();
                            if (TextUtil.isNullOrEmpty(fileName)) {
                                continue;
                            }

                            File file = new File(getSoundPath(), fileName);
                            if (file.exists()) {
                                fileList.add(file);
                                duMedia.setFilePath(file.getPath());
                            }
                        }

//                        getMvpView().onLoadVoices(duMediaList);
                    }
                }));
    }

    /**
     * 加载本地录像
     *
     * @param fileList
     * @param taskId
     * @param taskState
     */
    public void loadVideos(final List<File> fileList, final String taskId, final int taskType, final int taskState,
                           final HandleOrderEntity handleOrderEntity, final DUMyTask duMyTask) {
        if (TextUtil.isNullOrEmpty(taskId)) {
//            getMvpView().onError("param is null");
            LogUtil.e(TAG, "param is null");
            return;
        }

        LogUtil.i(TAG, "loadImages" + taskId);
        mSubscription.add(mDataManager.getMediaList(taskId, taskType,
                taskState, DUMedia.FILE_TYPE_SCREEN_SHOT)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUMedia>>() {
                    @Override
                    public void onCompleted() {
                        loadSignup(fileList, taskId, taskType, taskState, handleOrderEntity, duMyTask);
                        LogUtil.i(TAG, "loadVideos onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "loadVideos onError " + e.getMessage());
//                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<DUMedia> duMediaList) {
                        Gson gson = new Gson();
                        LogUtil.i(TAG, "loadVideos onNext" + duMediaList.size());
                        if (duMediaList == null || duMediaList.isEmpty()) {
                            return;
                        }
                        for (DUMedia duMedia : duMediaList) {
                            String fileName = duMedia.getExtend();
                            VideosPathEntity pathEntity = gson.fromJson(fileName, VideosPathEntity.class);
                            fileName = pathEntity.getVideoPath();
                            if (TextUtil.isNullOrEmpty(fileName)) {
                                continue;
                            }

                            File file = new File(/*getVideoPath(),*/ fileName);
                            LogUtils.e("videoName", fileName);
                            if (file.exists()) {
                                fileList.add(file);
                                duMedia.setFilePath(file.getPath());
                            }
                        }
//                        getMvpView().onLoadVideoImages(duMediaList);
                    }
                }));
    }

    public void loadSignup(final List<File> fileList, String taskId, int taskType, int taskState,
                           final HandleOrderEntity handleOrderEntity, final DUMyTask duMyTask) {
        if (TextUtil.isNullOrEmpty(taskId)) {
//            getMvpView().onError("param is null");
            LogUtil.e(TAG, "param is null");
            return;
        }

        LogUtil.i(TAG, "loadSignup");
        mSubscription.add(mDataManager.getMediaList(taskId, taskType,
                taskState, DUMedia.FILE_TYPE_SIGNUP)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUMedia>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "loadSignup onCompleted");
                        if (fileList.size() > 0) {
                            commitMultimediaInfo(fileList, handleOrderEntity, duMyTask);
                        } else {
                            commitInfo("", handleOrderEntity, duMyTask);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "loadSignup onError " + e.getMessage());
//                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<DUMedia> duMediaList) {
                        LogUtil.i(TAG, "loadImages onNext");
                        if (duMediaList == null || duMediaList.size() != 1
                            /*&& duMediaList.size() > 0*/) {
                            return;
                        }
                        for (DUMedia duMedia : duMediaList) {
                            String fileName = duMedia.getFileName();
                            if (TextUtil.isNullOrEmpty(fileName)) {
                                continue;
                            }

                            File file = new File(getImagePath(), fileName);
                            if (file.exists()) {
                                fileList.add(file);
                                duMedia.setFilePath(file.getPath());
                            }
                        }
//                        getMvpView().onLoadSignup(duMediaList.get(0));
                    }
                }));
    }

    /**
     * 提交回填信息
     */
    private void commitInfo(final String msg, HandleOrderEntity handleOrderEntity, final DUMyTask duMyTask) {
        Gson gson = new Gson();
//        handleOrderEntity.setID(0L);
        String s = gson.toJson(handleOrderEntity);
//        s = "{\""+s.substring(9);//去掉ID
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[").append(s).append("]");
        LogUtils.e("testJson", stringBuilder.toString());
        EasyHttp.post(URL.AppReturnOrder)
//                .connectTimeout(2 * 60 * 1000)
//                .readTimeOut(3*60*1000)
//                .writeTimeOut(3*60*1000)
                .params("uploadData", stringBuilder.toString())
                .execute(new CallBackProxy<CustomApiResult<ServiceResultEntity>,
                        ServiceResultEntity>(new CustomCallBack<ServiceResultEntity>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
//                        getMvpView().onCommitError(e.getMessage());
//                        getMvpView().onHint(msg + e.getMessage());
                    }

                    @Override
                    public void onSuccess(ServiceResultEntity serviceResultEntity) {
                        duMyTask.setTaskState(Constant.ORIGIN_MY_TASK_HISTORY);
                        if ("00".equals(serviceResultEntity.getCmMsgId())) {
                            duMyTask.setState(Constant.TASK_STATE_FINISH);
                            duMyTask.setIsUploadSuccess(Constant.HAS_UPLOADED);
                        } else {
                            ToastUtils.showShort(serviceResultEntity.getCmMsgDesc());
                        }
                        GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                .getDUMyTaskDao().update(duMyTask);
//                        getMvpView().onSaveHistoryTaskSuccess(msg + serviceResultEntity.getCmMsgDesc());
                        //保存到自开单记录
//                        getMvpView().onSaveSuccess(msg + serviceResultEntity.getCmMsgDesc());
                        mEventPosterHelper.postEventSafely(new UIBusEvent.NotifyHistoryTasksUI());
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        MProgressDialog.dismissProgress();
                    }
                }) {
                });
    }

    /**
     * 提交多媒体信息
     *
     * @param uploadFiles
     */
    private void commitMultimediaInfo(List<File> uploadFiles,
                                      final HandleOrderEntity handleOrderEntity, final DUMyTask duMyTask) {
        EasyHttp.post(URL.AppReturnOrderUploadFile)
//                .connectTimeout(2 * 60 * 1000)
//                .readTimeOut(3*60*1000)
//                .writeTimeOut(3*60*1000)
                .addFileParams("file", uploadFiles, new ProgressResponseCallBack() {
                    @Override
                    public void onResponseProgress(long l, long l1, boolean b) {

                    }
                })
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        MProgressDialog.showProgress(ActivityUtils.getTopActivity());
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtils.showShort(e.getMessage());
                        MProgressDialog.dismissProgress();
//                        getMvpView().onCommitError(e.getMessage());
                    }

                    @Override
                    public void onSuccess(String s) {
                        Gson gson = new Gson();
                        UploadSuccess uploadSuccess = gson.fromJson(s, UploadSuccess.class);
                        if (uploadSuccess.getState() == 0) {
                            commitInfo(uploadSuccess.getMsg(), handleOrderEntity, duMyTask);
                        } else {
                            MProgressDialog.dismissProgress();
//                            getMvpView().onCommitError(uploadSuccess.getMsg());
                        }
                    }
                });
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public File getImagePath() {
        return mConfigHelper.getImageFolderPath();
    }
}
