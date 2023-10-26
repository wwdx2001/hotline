package com.sh3h.hotline.ui.order.myorder.handle;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.maning.mndialoglibrary.MProgressDialog;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.dataprovider.data.entity.newentity.HandleOrderEntity;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.data.entity.response.ServiceResultEntity;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.preference.PreferencesHelper;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.HandleOrderEntityDao;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.UploadSuccess;
import com.sh3h.hotline.entity.VideosPathEntity;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.sh3h.hotline.ui.order.TaskState;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.body.ProgressResponseCallBack;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_VOICE;

/**
 * Created by zhangjing on 2016/9/14.
 */
public class HandleOrderPresenter extends ParentPresenter<HandleOrderMvpView> {

    private final static String TAG = "HandleOrderPresenter";
    private List<File> uploadFiles;
    private final ConfigHelper mConfigHelper;
    private EventPosterHelper mEventPosterHelper;

    private int mUserId;
    private Disposable mDisposable1;
    private Disposable mDisposable2;
    private String mAccount = "";

    public File getImagePath() {
        return mConfigHelper.getImageFolderPath();
    }

    public File getSoundPath() {
        return mConfigHelper.getSoundFolderPath();
    }

    @Inject
    public HandleOrderPresenter(DataManager dataManager, ConfigHelper configHelper
            , PreferencesHelper preferencesHelper, EventPosterHelper mEventPosterHelper) {
        super(dataManager);
        mAccount = dataManager.getAccount();
        uploadFiles = new ArrayList<>();
        this.mConfigHelper = configHelper;
        mUserId = mDataManager.getUserId();
        this.mEventPosterHelper = mEventPosterHelper;
    }

    public int getUserId() {
        return mUserId;
    }

    HandleOrderEntity handleOrderEntity;
    DUMyTask duMyTask;

    public void commitMyTaskInfo(DUMyTask duMyTask, HandleOrderEntity handleOrderEntity) {
        this.duMyTask = duMyTask;
        this.handleOrderEntity = handleOrderEntity;
        loadImages(handleOrderEntity.getFaId(), Constant.TASK_TYPE_DOWNLOAD_ORDER, TaskState.HANDLE);
    }


    /**
     * 加载本地图片
     *
     * @param taskId
     * @param taskType
     * @param taskState
     */
    public void loadImages(final String taskId, final int taskType, final int taskState) {
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
                        loadVoices(taskId, taskType, taskState);
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
                                uploadFiles.add(file);
                                duMedia.setFilePath(file.getPath());
                            }
                        }
//                        getMvpView().onLoadImages(duMediaList);
                    }
                }));
    }

    /**
     * 扫描本地录音
     *
     * @param taskId
     * @param taskType
     * @param taskState
     */
    public void loadVoices(final String taskId, final int taskType, final int taskState) {
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
                        loadVideos(taskId, taskType, taskState);
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
                                uploadFiles.add(file);
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
     * @param taskId
     * @param taskState
     */
    public void loadVideos(final String taskId, final int taskType, final int taskState) {
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
                        loadSignup(taskId, taskType, taskState);
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
                                uploadFiles.add(file);
                                duMedia.setFilePath(file.getPath());
                            }
                        }
//                        getMvpView().onLoadVideoImages(duMediaList);
                    }
                }));
    }

    public void loadSignup(String taskId, int taskType, int taskState) {
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
                        if (uploadFiles.size() > 0) {
                            commitMultimediaInfo(uploadFiles);
                        } else {
                            ToastUtils.showShort(ActivityUtils.getTopActivity().getResources().getString(R.string.add_file));
//                            commitInfo("");
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
                                uploadFiles.add(file);
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
    private void commitInfo(final String msg) {
        Gson gson = new Gson();
        String s = gson.toJson(handleOrderEntity);
        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("{").append("\"data\":[").append(s).append("]}");
        stringBuilder.append("[").append(s).append("]");
        LogUtils.e("testJson", stringBuilder.toString());
        //                        getMvpView().onHint(msg + e.getMessage());
//保存到自开单记录
//                        getMvpView().onSaveSuccess(msg + serviceResultEntity.getCmMsgDesc());
        mDisposable1 = EasyHttp.post(URL.AppReturnOrder)
//                .connectTimeout(2 * 60 * 1000)
//                .readTimeOut(3*60*1000)
//                .writeTimeOut(3*60*1000)
                .params("uploadData", stringBuilder.toString())
                .execute(new CallBackProxy<CustomApiResult<ServiceResultEntity>,
                        ServiceResultEntity>(new CustomCallBack<ServiceResultEntity>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        MProgressDialog.showProgress(ActivityUtils.getTopActivity());
                    }

                    @Override
                    public void onError(ApiException e) {
//                        super.onError(e);
                        if (getMvpView() != null) {
                            MProgressDialog.dismissProgress();
                            getMvpView().onCommitError(e.getMessage());
                        }
//                        getMvpView().onHint(msg + e.getMessage());
                    }

                    @Override
                    public void onSuccess(ServiceResultEntity serviceResultEntity) {
                        MProgressDialog.dismissProgress();
                        if (duMyTask != null) {
                            mEventPosterHelper.postEventSafely(new UIBusEvent.InitResult(true));
                            if ("00".equals(serviceResultEntity.getCmMsgId())) {
                                duMyTask.setState(Constant.TASK_STATE_FINISH);
                                duMyTask.setIsUploadSuccess(Constant.HAS_UPLOADED);
                            }
                            duMyTask.setTaskState(Constant.ORIGIN_MY_TASK_HISTORY);
                            GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                    .getDUMyTaskDao().update(duMyTask);
                        }
                        if (getMvpView() != null)
                            getMvpView().onCommitTaskSuccess(msg + serviceResultEntity.getCmMsgDesc());
                        //保存到自开单记录
//                        getMvpView().onSaveSuccess(msg + serviceResultEntity.getCmMsgDesc());
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
    private void commitMultimediaInfo(List<File> uploadFiles) {
        mDisposable2 = EasyHttp.post(URL.AppReturnOrderUploadFile)
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
                        MProgressDialog.dismissProgress();
                        if (getMvpView() != null)
                            getMvpView().onCommitError(e.getMessage());
                    }

                    @Override
                    public void onSuccess(String s) {
                        Gson gson = new Gson();
                        UploadSuccess uploadSuccess = gson.fromJson(s, UploadSuccess.class);
                        if (uploadSuccess.getState() == 0) {
                            commitInfo(uploadSuccess.getMsg());
                        } else {
                            MProgressDialog.dismissProgress();
                            if (getMvpView() != null)
                                getMvpView().onCommitError(uploadSuccess.getMsg());
                        }
                    }
                });
    }


    public void saveToHistoryTask(final DUMyTask duMyTask, final HandleOrderEntity handleOrderEntity) {
        GreenDaoUtils.getAsyncSession(ActivityUtils.getTopActivity())
                .runInTx(new Runnable() {
                    @Override
                    public void run() {
                        List<HandleOrderEntity> list = GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                .getHandleOrderEntityDao().queryBuilder()
                                .where(HandleOrderEntityDao.Properties.FaId.eq(handleOrderEntity.getFaId()))
                                .list();
                        if (list != null && list.size() > 0) {
                            HandleOrderEntity handleOrderEntity1 = list.get(0);
                            handleOrderEntity1.setFinishDt(handleOrderEntity.getFinishDt());
                            handleOrderEntity1.setComment(handleOrderEntity.getComment());
                            handleOrderEntity1.setFaAct(handleOrderEntity.getFaAct());
                            handleOrderEntity1.setFaReason(handleOrderEntity.getFaReason());
                            handleOrderEntity1.setCljg(handleOrderEntity.getCljg());
                            handleOrderEntity1.setRegRead(handleOrderEntity.getRegRead());
                            handleOrderEntity1.setCbzt(handleOrderEntity.getCbzt());
                            handleOrderEntity1.setFaTypeCd(handleOrderEntity.getFaTypeCd());
                            handleOrderEntity1.setClnr(handleOrderEntity.getClnr());
                            handleOrderEntity1.setArriveDt(handleOrderEntity.getArriveDt());
                            GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                    .getHandleOrderEntityDao().update(handleOrderEntity1);
//                            duMyTask.setID((long) (duMyTask.getFaId().hashCode() + duMyTask.getCaseId().hashCode()));
                            duMyTask.setTaskState(Constant.ORIGIN_MY_TASK_HISTORY);
                            duMyTask.setState(Constant.TASK_STATE_CANCEL);
                            duMyTask.setIsFlag(System.currentTimeMillis() + "");
                            GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                    .getDUMyTaskDao().update(duMyTask);
                            mEventPosterHelper.postEventSafely(new UIBusEvent.InitResult(true));
                            if (getMvpView() != null)
                                getMvpView().onSaveHistoryTaskSuccess("保存成功");
                        } else {
                            long isSuccess = GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                    .getHandleOrderEntityDao().insert(handleOrderEntity);
                            if (isSuccess > 0) {
//                                duMyTask.setID((long) (duMyTask.getFaId().hashCode() + duMyTask.getCaseId().hashCode()));
                                duMyTask.setTaskState(Constant.ORIGIN_MY_TASK_HISTORY);
                                duMyTask.setState(Constant.TASK_STATE_CANCEL);
                                duMyTask.setIsFlag(System.currentTimeMillis() + "");
                                GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                        .getDUMyTaskDao().update(duMyTask);
                                mEventPosterHelper.postEventSafely(new UIBusEvent.InitResult(true));
                                if (getMvpView() != null)
                                    getMvpView().onSaveHistoryTaskSuccess("保存成功");
                            }
                        }
                    }
                });
    }

    /**
     * 保存历史工单记录
     *
     * @param duHistoryTask
     */
    public void saveHistoryTask(final DUHistoryTask duHistoryTask) {
        mSubscription.add(mDataManager.saveHistoryTask(duHistoryTask)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, e.getMessage());
                        if (getMvpView() != null)
                            getMvpView().showMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "onNext");
                        if (getMvpView() != null)
                            getMvpView().onSaveHistoryTask(aBoolean, duHistoryTask);
                    }
                })
        );
    }

    /**
     * 更新历史工单
     *
     * @param duHistoryTask
     */
    public void updateHistoryTask(final DUHistoryTask duHistoryTask) {
        mSubscription.add(mDataManager.updateHistoryTaskByID(duHistoryTask)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "updateHistoryTask onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, e.getMessage());
                        if (getMvpView() != null)
                            getMvpView().showMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "updateHistoryTask onNext");
                        if (getMvpView() != null)
                            getMvpView().onUpdateHistoryTask(aBoolean, duHistoryTask);
                    }
                }));
    }

    public void getServerTaskId(final String taskId) {
        mSubscription.add(mDataManager.updateTaskReplyFromCreateSelf(taskId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "getServerTaskId onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "updateHistoryTask onNext");
                        if (aBoolean) {
                            if (getMvpView() != null)
                                getMvpView().uploadTaskReplyCreateSelf(taskId);
                        }
                    }
                }));
    }


    @Override
    public void cancelEasyhttpRequest() {
        EasyHttp.cancelSubscription(mDisposable1);
        EasyHttp.cancelSubscription(mDisposable2);
    }
}
