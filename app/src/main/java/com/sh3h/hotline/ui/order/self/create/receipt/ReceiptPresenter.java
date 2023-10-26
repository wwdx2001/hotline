package com.sh3h.hotline.ui.order.self.create.receipt;

import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.maning.mndialoglibrary.MProgressDialog;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.dataprovider.data.entity.base.DUEntitiesResult;
import com.sh3h.dataprovider.data.entity.request.DUCreateSelfOrder;
import com.sh3h.dataprovider.data.entity.response.DUBillBaseInfo;
import com.sh3h.dataprovider.data.entity.response.DUWord;
import com.sh3h.dataprovider.data.entity.response.ServiceResultEntity;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.file.FileHelper;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.NetworkUtil;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.UploadSuccess;
import com.sh3h.hotline.entity.VideosPathEntity;
import com.sh3h.hotline.entity.ZikaidanJLEntity;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.sh3h.hotline.ui.order.self.create.CreateSelfOrderActivity;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.body.ProgressResponseCallBack;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_VOICE;
import static com.sh3h.dataprovider.util.Constant.ISSUE_AREA;
import static com.sh3h.dataprovider.util.Constant.ISSUE_ORIGIN;
import static com.sh3h.dataprovider.util.Constant.ISSUE_TYPE;
import static com.sh3h.dataprovider.util.Constant.REPLY_CLASS;

/**
 * Created by dengzhimin on 2016/9/18.
 */
public class ReceiptPresenter extends ParentPresenter<ReceiptMvpView> {
    private static final String TAG = "ReceiptPresenter";
    private int mUserId;
    private String account;
    private final ConfigHelper mConfigHelper;
    private FileHelper mFileHelper;
    public List<File> upLoadFiles;
    private Disposable mDisposable1;
    private Disposable mDisposable2;

    @Inject
    public ReceiptPresenter(DataManager dataManager,
                            ConfigHelper configHelper,
                            FileHelper fileHelper) {
        super(dataManager);
        upLoadFiles = new ArrayList<>();
        mUserId = dataManager.getUserId();
        account = dataManager.getAccount();
        this.mConfigHelper = configHelper;
        this.mFileHelper = fileHelper;
    }

    public int getUserId() {
        return mUserId;
    }

    /**
     * 处理级别
     */
    public void initChuLiJiBieSpinner() {
        mSubscription.add(mDataManager.getFirstWords(REPLY_CLASS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUWord>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(List<DUWord> duWords) {
                        getMvpView().onInitChuLiJiBieSpinner(duWords);
                    }
                })
        );
    }

    /**
     * 反映区名
     */
    public void initIssueAreaSpinner() {
        mSubscription.add(mDataManager.getFirstWords(ISSUE_AREA)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUWord>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(List<DUWord> duWords) {
                        getMvpView().onInitIssueAreaSpinner(duWords);
                    }
                })
        );
    }

    /**
     * 反映来源
     */
    public void initIssueOriginSpinner() {
        mSubscription.add(mDataManager.getFirstWords(ISSUE_ORIGIN)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUWord>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(List<DUWord> duWords) {
                        getMvpView().onInitIssueOriginSpinner(duWords);
                    }
                })
        );
    }

    public void initFanYingLeiXingSpinner() {
        mSubscription.add(mDataManager.getFirstWords(ISSUE_TYPE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUWord>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(List<DUWord> duWords) {
                        getMvpView().onInitFanYingLeiXingSpinner(duWords);
                    }
                })
        );
    }


    public void initFanYingNeiRongSpinner(String valueEx) {
        mSubscription.add(mDataManager.getWords(valueEx)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Map<String, List<DUWord>>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, "onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Map<String, List<DUWord>> stringListMap) {
                        getMvpView().onInitFanYingNeiRongSpinner(stringListMap.get(Constant.ISSUE_CONTENT));
                    }
                })
        );
    }

    public void searchBill(String xiaogenhao) {
        mSubscription.add(mDataManager.searchBill(xiaogenhao, null, null, null, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DUEntitiesResult<DUBillBaseInfo>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, "onError:" + e.getMessage());
                        getMvpView().onHint(R.string.toast_load_account_error);
                    }

                    @Override
                    public void onNext(DUEntitiesResult<DUBillBaseInfo> result) {
                        LogUtil.i(TAG, "onNext");
                        if (result.getStatusCode() != Constant.STATUS_CODE_200) {
                            getMvpView().onHint(result.getMessage());
                        } else {
                            if (result.getData().size() <= 0) {
                                getMvpView().onHint(R.string.toast_cardid_error);
                            } else {
                                getMvpView().onIntent(result.getData().get(0));
                            }
                        }
                    }
                }));
    }

    private ZikaidanJLEntity zikaidanJLEntity;
    private boolean isCheckbox;

    /**
     * 保存并上传工单
     */
    public void saveAndCommitOrder(final CreateSelfOrderActivity activity, boolean isCheckbox, final ZikaidanJLEntity zikaidanJLEntity) {
        this.zikaidanJLEntity = zikaidanJLEntity;
        this.isCheckbox = isCheckbox;
        if (zikaidanJLEntity != null) {
            loadImages(zikaidanJLEntity.getFuwudianBH() + activity.currentTime, Constant.TASK_TYPE_CREATE_SELF_ORDER, Constant.TASK_STATE_CREATE);
//            long isSuccess = GreenDaoUtils.getDaoSession(context).getZikaidanJLEntityDao()
//                    .insertOrReplace(zikaidanJLEntity);
        }


//        if (isSuccess > 0) {
//            Intent startIntent = ZikaidanUploadService.getStartIntent(context);
//            startIntent.putExtra(Constant.ZIKAIDAN_JL, zikaidanJLEntity);
//            context.startService(startIntent);
//            getMvpView().onSaveSuccess(context.getResources().getString(R.string.text_save_success));
//        } else {
//            getMvpView().onHint(R.string.text_hint_save_failed);
//        }
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
                        loadVideos(taskId, taskType, taskState);
                        LogUtil.i(TAG, "loadImages onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "loadImages onError " + e.getMessage());
//                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<DUMedia> duMediaList) {
                        LogUtil.i(TAG, "loadImages onNext");

                        for (DUMedia duMedia : duMediaList) {
                            String fileName = duMedia.getFileName();
                            if (TextUtil.isNullOrEmpty(fileName)) {
                                continue;
                            }

                            File file = new File(getImagePath(), fileName);
                            if (file.exists()) {
                                upLoadFiles.add(file);
                                duMedia.setFilePath(file.getPath());
                            }

                        }
//                        getMvpView().onLoadImages(duMediaList);
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
                        loadVoices(taskId, taskType, taskState);
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
                            LogUtils.e("videoPath", fileName);
                            if (file.exists()) {
                                upLoadFiles.add(file);
                                duMedia.setFilePath(file.getPath());
                            }
                        }
//                        getMvpView().onLoadVideoImages(duMediaList);
                    }
                }));
    }

    /**
     * get the path of the video folder
     *
     * @return
     */
    public File getVideoPath() {
        return mConfigHelper.getVideoFolderPath();
    }

    public void loadSignup(String taskId, int taskType, int taskState) {
        LogUtils.e("taskId", taskId);
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
                        if (upLoadFiles.size() > 0) {
                            commitMultimediaData();
                        } else {
                            ToastUtils.showShort(ActivityUtils.getTopActivity().getResources().getString(R.string.add_file));
//                            commZiKaidanInfo("");
                        }
                        LogUtil.i(TAG, "loadSignup onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "loadSignup onError " + e.getMessage());
//                        getMvpView().onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<DUMedia> duMediaList) {
                        LogUtil.i(TAG, "loadImages onNext");
                        if (duMediaList == null || duMediaList.size() != 1) {
                            return;
                        }
                        for (DUMedia duMedia : duMediaList) {
                            String fileName = duMedia.getFileName();
                            if (TextUtil.isNullOrEmpty(fileName)) {
                                continue;
                            }

                            File file = new File(getImagePath(), fileName);
                            if (file.exists()) {
                                upLoadFiles.add(file);
                                duMedia.setFilePath(file.getPath());
                            }
                        }
//                        getMvpView().onLoadSignup(duMediaList.get(0));
                    }
                }));
    }

    /**
     * get the path of the image folder
     *
     * @return
     */
    public File getImagePath() {
        return mConfigHelper.getImageFolderPath();
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
                        loadSignup(taskId, taskType, taskState);
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
                                upLoadFiles.add(file);
                                duMedia.setFilePath(file.getPath());
                            }
                        }

//                        getMvpView().onLoadVoices(duMediaList);
                    }
                }));
    }

    /**
     * 上传多媒体数据
     */
    private void commitMultimediaData() {
        mDisposable1 = EasyHttp.post(URL.AppReturnOrderUploadFile)
//                .connectTimeout(2 * 60 * 1000)
//                .readTimeOut(3*60*1000)
//                .writeTimeOut(3*60*1000)
                .addFileParams("file", upLoadFiles, new ProgressResponseCallBack() {
                    @Override
                    public void onResponseProgress(long l, long l1, boolean b) {
                        LogUtils.e("FileSize", l1);
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
                        getMvpView().onHint(e.getMessage());
                    }

                    @Override
                    public void onSuccess(String s) {
                        Gson gson = new Gson();
                        UploadSuccess uploadSuccess = gson.fromJson(s, UploadSuccess.class);
                        if (uploadSuccess.getState() == 0) {
                            commZiKaidanInfo(uploadSuccess.getMsg());
                        } else {
                            MProgressDialog.dismissProgress();
                            getMvpView().onHint(uploadSuccess.getMsg());
                        }
                    }
                });
    }

    /**
     * 提交自开单信息
     *
     * @param msg
     */
    private void commZiKaidanInfo(final String msg) {
        if (zikaidanJLEntity == null) {
            MProgressDialog.dismissProgress();
            return;
        }
        HttpParams params = new HttpParams();
        params.put("spId", zikaidanJLEntity.getFuwudianBH());
        params.put("address", zikaidanJLEntity.getFashengDZ());
        params.put("newMtrType", zikaidanJLEntity.getXbbx());
        params.put("faTypeCd", zikaidanJLEntity.getFanyingLX());
        params.put("fynr", zikaidanJLEntity.getFanyingNR());
        params.put("cljb", zikaidanJLEntity.getChuliJB());
        params.put("creDttm", TimeUtils.date2String(new Date()));
        params.put("UserId", SPUtils.getInstance().getString(Constant.USERID));
        params.put("repCd", isCheckbox ? SPUtils.getInstance().getString(Constant.USERID) : "");
        mDisposable2 = EasyHttp.post(URL.SelfOpeningOrder)
                .params(params)
                .execute(new CallBackProxy<CustomApiResult<ServiceResultEntity>,
                        ServiceResultEntity>(new CustomCallBack<ServiceResultEntity>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        MProgressDialog.showProgress(ActivityUtils.getTopActivity());
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        getMvpView().onHint(msg + e.getMessage());
                    }

                    @Override
                    public void onSuccess(ServiceResultEntity serviceResultEntity) {
                        //保存到自开单记录
                        getMvpView().onSaveSuccess(msg + serviceResultEntity.getCmMsgDesc());
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
     * get the path of the sound folder
     *
     * @return
     */
    public File getSoundPath() {
        return mConfigHelper.getSoundFolderPath();
    }

    /**
     * 保存自开单
     * 自开单创建历史记录
     * 自开单上传历史记录
     *
     * @param order  自开单
     * @param isDeal 是否现场处理
     */
    public void saveCreateSelfOrderAndHistory(final Context context, final DUCreateSelfOrder order, final boolean isDeal) {
        LogUtil.i(TAG, "saveCreateSelfOrderAndHistory");
        if (order == null) {
            getMvpView().onHint(R.string.text_hint_save_failed);
            return;
        }

        DUHistoryTask createTask = new DUHistoryTask();
        createTask.setUSER_ID(mUserId);
        createTask.setTASK_ID(order.getLocalTaskId());
        createTask.setTASK_TYPE(Constant.TASK_TYPE_CREATE_SELF_ORDER);
        createTask.setTASK_STATE(Constant.TASK_STATE_CREATE);
        createTask.setREPLY_TIME(System.currentTimeMillis());
        createTask.setUPLOAD_FLAG(Constant.UPLOAD_FLAG_UPLOADED);
        order.setReplyTimeInHistory(createTask.getREPLY_TIME());

        mSubscription.add(mDataManager.saveCreateSelfOrderAndHistory(order, createTask)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "saveCreateSelfOrderAndHistory onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.i(TAG, "saveCreateSelfOrderAndHistory onError" + e.getMessage());
                        getMvpView().onHint(R.string.text_hint_save_failed);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "saveCreateSelfOrderAndHistory onNext " + aBoolean);
                        if (aBoolean) {//保存成功
                            getMvpView().onHint(R.string.text_save_success);
                            getMvpView().onToDealOrder(isDeal);
                            if (NetworkUtil.isNetworkConnected(context)) {//有网
                                //上传 do upload
                                getMvpView().onUploadOrder(order.getLocalTaskId(), isDeal);
                            }
                        } else {//保存失败
                            getMvpView().onHint(R.string.text_hint_save_failed);
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
