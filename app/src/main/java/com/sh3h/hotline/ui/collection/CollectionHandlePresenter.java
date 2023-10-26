package com.sh3h.hotline.ui.collection;

import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.hjq.toast.ToastParams;
import com.hjq.toast.Toaster;
import com.hjq.toast.style.CustomToastStyle;
import com.maning.mndialoglibrary.MProgressDialog;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.dataprovider.data.entity.newentity.CollectionHandleEntity;
import com.sh3h.dataprovider.data.entity.newentity.HandleOrderEntity;
import com.sh3h.dataprovider.data.entity.newentity.OverrateCallHandleEntity;
import com.sh3h.dataprovider.data.entity.newentity.OverrateReceiptHandleEntity;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.data.entity.response.ServiceResultEntity;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.file.FileHelper;
import com.sh3h.dataprovider.data.local.preference.PreferencesHelper;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.HandleOrderEntityDao;
import com.sh3h.dataprovider.greendaoDao.OverrateCallHandleEntityDao;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.dataprovider.util.OtoO;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.CollectionTaskListBean;
import com.sh3h.hotline.entity.UploadDataResult;
import com.sh3h.hotline.entity.UploadSuccess;
import com.sh3h.hotline.entity.VideosPathEntity;
import com.sh3h.hotline.event.NotifRefrashDataEvent;
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
import com.zhouyou.http.request.PostRequest;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_PICTURE;
import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_SCREEN_SHOT;
import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_SIGNUP;
import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_VOICE;
import static com.sh3h.dataprovider.data.entity.DUMedia.UPLOAD_FLAG_LOCAL;
import static com.sh3h.dataprovider.util.Constant.VIDEO_PATH;

public class CollectionHandlePresenter extends ParentPresenter<CollectionHandleMvpView> {

    private final static String TAG = "CollectionHandlePresenter";
    private List<File> uploadFiles;
    private List<DUMedia> signFiles;
    private List<DUMedia> scanFiles;
    private List<DUMedia> imageFiles;
    private List<DUMedia> voiceFiles;
    private List<DUMedia> videoFiles;
    private final ConfigHelper mConfigHelper;
    private EventPosterHelper mEventPosterHelper;

    private int mUserId;
    private Disposable mDisposable1;
    private Disposable mDisposable2;
    private String mAccount = "";

    @Inject
    public CollectionHandlePresenter(DataManager dataManager, ConfigHelper configHelper
            , PreferencesHelper preferencesHelper, EventPosterHelper mEventPosterHelper) {
        super(dataManager);
        mAccount = dataManager.getAccount();
        uploadFiles = new ArrayList<>();
        signFiles = new ArrayList<>();
        scanFiles = new ArrayList<>();
        imageFiles = new ArrayList<>();
        voiceFiles = new ArrayList<>();
        videoFiles = new ArrayList<>();
        this.mConfigHelper = configHelper;
        mUserId = mDataManager.getUserId();
        this.mEventPosterHelper = mEventPosterHelper;
    }

    public int getUserId() {
        return mUserId;
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    CollectionHandleEntity handleOrderEntity;
    CollectionTaskListBean duMyTask;

    public void commitMyTaskInfo(CollectionTaskListBean duMyTask, CollectionHandleEntity handleOrderEntity) {
        this.duMyTask = duMyTask;
        this.handleOrderEntity = handleOrderEntity;
        uploadFiles.clear();
        signFiles.clear();
        scanFiles.clear();
        imageFiles.clear();
        voiceFiles.clear();
        videoFiles.clear();
        loadSignup(handleOrderEntity.getAlbh(), Constant.TASK_TYPE_DOWNLOAD_ORDER, TaskState.HANDLE);
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
                        if (getMvpView() != null) {
                            getMvpView().onSignComplete(uploadFiles);
                        }
//                        loadImages(taskId, taskType, taskState);
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
                                signFiles.add(duMedia);
                            }
                        }
//                        getMvpView().onLoadSignup(duMediaList.get(0));
                    }
                }));
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
                                imageFiles.add(duMedia);
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
                                voiceFiles.add(duMedia);
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
//                        loadSignup(taskId, taskType, taskState);
                        LogUtil.i(TAG, "loadVideos onCompleted");
                        if (uploadFiles.size() > 0) {
                            commitMultimediaInfo(uploadFiles, taskId, taskType, taskState);
//                            commitInfo("", taskId, taskType, taskState);
                        } else {
                            ToastUtils.showShort(ActivityUtils.getTopActivity().getResources().getString(R.string.add_file));
//                            commitInfo("");
                        }
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
                                videoFiles.add(duMedia);
                            }
                        }
//                        getMvpView().onLoadVideoImages(duMediaList);
                    }
                }));
    }

    /**
     * 提交多媒体信息
     *
     * @param uploadFiles
     */
    private void commitMultimediaInfo(List<File> uploadFiles, final String taskId, final int taskType, final int taskState) {
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
                            commitInfo(uploadSuccess.getMsg(), taskId, taskType, taskState);
                        } else {
                            MProgressDialog.dismissProgress();
                            if (getMvpView() != null)
                                getMvpView().onCommitError(uploadSuccess.getMsg());
                        }
                    }
                });
    }

    /**
     * 提交回填信息
     */
    private void commitInfo(final String msg, final String taskId, final int taskType, final int taskState) {
        String timeDir = getTimeDir();

        String dzqm = "";
        if ("客户已缴".equals(handleOrderEntity.getCjjg()) || "已催有人".equals(handleOrderEntity.getCjjg()) ||
                "其他".equals(handleOrderEntity.getCjjg())) {
            if (signFiles.size() > 0 && signFiles.get(0) != null) {
                dzqm = timeDir + "/" + signFiles.get(0).getFileName();
            }
        }

        String fj1 = "";
        String fj2 = "";
        String fj3 = "";

        if (imageFiles.size() == 1) {
            if (imageFiles.get(0) != null) {
                fj1 = timeDir + "/" + imageFiles.get(0).getFileName();
            }
        } else if (imageFiles.size() == 2) {
            if (imageFiles.get(0) != null) {
                fj1 = timeDir + "/" + imageFiles.get(0).getFileName();
            }
            if (imageFiles.get(1) != null) {
                fj2 = timeDir + "/" + imageFiles.get(1).getFileName();
            }
        } else {
            if (imageFiles.get(0) != null) {
                fj1 = timeDir + "/" + imageFiles.get(0).getFileName();
            }
            if (imageFiles.get(1) != null) {
                fj2 = timeDir + "/" + imageFiles.get(1).getFileName();
            }
            if (imageFiles.get(2) != null) {
                fj3 = timeDir + "/" + imageFiles.get(2).getFileName();
            }
        }

        String sp = "";
        if (videoFiles.size() > 0 && videoFiles.get(0) != null) {
            Gson gson = new Gson();
            String fileName = videoFiles.get(0).getExtend();
            VideosPathEntity pathEntity = gson.fromJson(fileName, VideosPathEntity.class);
            fileName = pathEntity.getVideoPath();
            sp = timeDir + "/" + getFileName(fileName);
            LogUtils.e("-------------------------sp：" + sp);
        }

        String ly = "";
        if (voiceFiles.size() > 0 && voiceFiles.get(0) != null) {
            ly = timeDir + "/" + voiceFiles.get(0).getFileName();
        }

        PostRequest request = EasyHttp
                .post(URL.PTCuiJiaoGDBack)
                .params("albh", duMyTask.getAlbh())
//                .params("pch", duMyTask.getPch())
//                .params("xh", duMyTask.getXh())
                .params("cjyid", mDataManager.getAccount())
//                .params("cjyid", "111032")
//                .params("cjjg", handleOrderEntity.getCjjg())
                .params("cjjgdl", handleOrderEntity.getCjjg())
                .params("cjsj", TimeUtils.getNowString())
                .params("cjjgxl", "")
                .params("jfly", "")
                .params("qkms", "")
                .params("ncqcs", "")
                .params("cjbz", handleOrderEntity.getCjbz())
//                .params("dqqfje", duMyTask.getQfzje())

                .params("qrcj", dzqm)
                .params("fj1", fj1)
                .params("fj2", fj2)
                .params("fj3", fj3)
                .params("sp", sp)
                .params("ly", ly)

                .params("xxbg", handleOrderEntity.getXxbg())
                .params("htkhmc", "")
                .params("htzddz", "")
                .params("httyshxydm", "")
                .params("bgbz", "")
                .params("htlxr", handleOrderEntity.getLxr())
                .params("htlxfs", handleOrderEntity.getLxfs())
                .params("gdzt", "完成")
                .params("gdztyy", "");

        request.execute(new SimpleCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                MProgressDialog.showProgress(ActivityUtils.getTopActivity());
            }

            @Override
            public void onError(ApiException e) {
                if (getMvpView() != null) {
                    MProgressDialog.dismissProgress();
//                ToastUtils.showShort(e.getMessage());
                    ToastParams params = new ToastParams();
                    params.text = e.getMessage();
                    params.style = new CustomToastStyle(R.layout.toast_error);
                    Toaster.show(params);
                }
            }

            @Override
            public void onSuccess(String s) {
                MProgressDialog.dismissProgress();
                LogUtils.e(s);
                Gson gson = new Gson();
                UploadDataResult uploadSuccess = gson.fromJson(s, UploadDataResult.class);
                if (uploadSuccess.getState() == 0) {
                    ToastUtils.showShort("提交成功");

                    if (handleOrderEntity != null) {
                        GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getCollectionHandleEntityDao()
                                .delete(handleOrderEntity);
                    }

                    deleteMedias(taskId, taskType, taskState);

                    if (duMyTask != null) {
                        mEventPosterHelper.postEventSafely(new NotifRefrashDataEvent(true));
                    }

                    if (getMvpView() != null) {
                        getMvpView().onCommitTaskSuccess(msg + uploadSuccess.getData().getCmMsgDesc());
                    }
                } else {
                    ToastParams params = new ToastParams();
                    params.text = uploadSuccess.getMsg();
                    params.style = new CustomToastStyle(R.layout.toast_error);
                    Toaster.show(params);
                }
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                MProgressDialog.dismissProgress();
            }
        });
    }

    private void deleteMedias(String taskId, int taskType, int taskState) {
        mSubscription.add(mDataManager.deleteMediaList(taskId, taskType, taskState)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "deleteImage onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "deleteImage onError " + e.getMessage());
                        LogUtils.e(e);
//                        ToastUtils.showShort(e.getMessage());
//                        ToastParams params = new ToastParams();
//                        params.text = e.getMessage();
//                        params.style = new CustomToastStyle(R.layout.toast_error);
//                        Toaster.show(params);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "deleteImage onNext " + aBoolean);
                    }
                }));
    }

    public File getImagePath() {
        return new File(Environment.getExternalStorageDirectory(), "sh3h/hotline/images");
    }

    public File getSoundPath() {
        return new File(Environment.getExternalStorageDirectory(), "sh3h/hotline/sounds");
    }

    public String getFileName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
//        int end=pathandname.lastIndexOf(".");
//        if(start!=-1 && end!=-1){
        if (start != -1) {
            return pathandname.substring(start + 1);
        } else {
            return null;
        }
    }

    private static final ThreadLocal<SimpleDateFormat> SDF_THREAD_LOCAL = new ThreadLocal<>();

    private String getTimeDir() {
        SimpleDateFormat simpleDateFormat = SDF_THREAD_LOCAL.get();
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("yyyy/M/d", Locale.getDefault());
            SDF_THREAD_LOCAL.set(simpleDateFormat);
        }
        String timeDir = TimeUtils.getNowString(simpleDateFormat);
        return timeDir;
    }

    @Override
    public void cancelEasyhttpRequest() {
        EasyHttp.cancelSubscription(mDisposable1);
        EasyHttp.cancelSubscription(mDisposable2);
    }
}
