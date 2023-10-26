package com.sh3h.hotline.ui.nonresident.selfbilling;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.aliyun.demo.recorder.AliyunVideoRecorder;
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
import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.dataprovider.data.entity.newentity.OverrateSelfHandleEntity;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.OverrateSelfHandleEntityDao;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.OverrateSelfBillingEntity;
import com.sh3h.hotline.entity.UploadDataResult;
import com.sh3h.hotline.entity.UploadSuccess;
import com.sh3h.hotline.entity.VideosPathEntity;
import com.sh3h.hotline.event.NotifRefrashDataEvent;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.nonresident.media.MultimediaFileFragment;
import com.sh3h.hotline.ui.order.TaskState;
import com.sh3h.hotline.util.StringRegexUtils;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.body.ProgressResponseCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_VOICE;

public class EditSubmitActivity extends ParentActivity implements RadioGroup.OnCheckedChangeListener {

    private final static String TAG = "HandleOrderActivity";
    private static final String FRAGMENT_TAG = "FragmentTAG";
    private int mTaskState = Constant.TASK_STATE_CREATE;
    private int mTaskType = Constant.TASK_TYPE_CREATE_SELF_ORDER;

    @Inject
    DataManager mDataManager;

    @Inject
    EventPosterHelper mEventPosterHelper;

    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    @BindView(R.id.bottom_navigation)
    RadioGroup bottomNavigation;

    @BindView(R.id.rb_edit)
    RadioButton rbEdit;

    private Unbinder mUnbinder;

    private FragmentManager mFragmentManager;

    private Fragment mCurrentFragment;

    public MultimediaFileFragment mMultimediaFileFragment;

    public EditSubmitFragment mEditSubmitFragment;

    private MenuItem submitItem;

    private String fyly;
    private String fynr;
    private String zhbh;
    private String tyshxydm;
    private String khmc;

    private String lxr;
    private String lxfs;
    private String yjdz;
    private String bz;

    public OverrateSelfBillingEntity currentEntity;

    protected CompositeSubscription mSubscription;

    private List<File> uploadFiles;
    private List<DUMedia> signFiles;
    private List<DUMedia> imageFiles;
    private List<DUMedia> voiceFiles;
    private List<DUMedia> videoFiles;

    private Disposable mDisposable2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_submit);
        getActivityComponent().inject(this);
        mSubscription = new CompositeSubscription();
        mUnbinder = ButterKnife.bind(this);

        initToolBar(R.string.label_selfbilling_editsubmit);

        initView();
        initData();
        mCurrentFragment = initFragment(savedInstanceState);
    }

    private void initView() {
        bottomNavigation.setOnCheckedChangeListener(this);
    }

    private void initData() {
//        String timeDir = getTimeDir();
//        LogUtils.e("-------------------------time：" + timeDir);
        uploadFiles = new ArrayList<>();
        signFiles = new ArrayList<>();
        imageFiles = new ArrayList<>();
        voiceFiles = new ArrayList<>();
        videoFiles = new ArrayList<>();
        fyly = getIntent().getStringExtra("fyly");
        fynr = getIntent().getStringExtra("fynr");
        currentEntity = (OverrateSelfBillingEntity) getIntent().getSerializableExtra("SelfBilling");
    }

    private Fragment initFragment(Bundle savedInstanceState) {
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (savedInstanceState == null) {
            mEditSubmitFragment = EditSubmitFragment.newInstance(currentEntity, "");
            transaction.add(R.id.fragment_container, mEditSubmitFragment, EditSubmitFragment.class.getName()).commitAllowingStateLoss();
            return mEditSubmitFragment;
        }

        String fragmentTag = savedInstanceState.getString("FRAGMENT_NAME", QuerySelectFragment.class.getName());
        OverrateSelfBillingEntity currentEntity1 = (OverrateSelfBillingEntity) savedInstanceState.getSerializable(Constant.TASK_ID);
        if (fragmentTag.equals(EditSubmitFragment.class.getName())) {
            EditSubmitFragment fragment = EditSubmitFragment.newInstance(currentEntity1, "");
            transaction.add(R.id.fragment_container, fragment, fragmentTag).commitAllowingStateLoss();
            return fragment;
        } else if (fragmentTag.equals(MultimediaFileFragment.class.getName())) {
            MultimediaFileFragment fragment = MultimediaFileFragment.newInstance(currentEntity1.getZhbh(), Constant.TASK_TYPE_DOWNLOAD, TaskState.HANDLE);
            transaction.add(R.id.fragment_container, fragment, fragmentTag).commitAllowingStateLoss();
            return fragment;
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        submitItem = menu.findItem(R.id.action_save);
        submitItem.setTitle("提交");
        submitItem.setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            submitData();

//            List<OverrateSelfHandleEntity> entityList = GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getOverrateSelfHandleEntityDao()
//                    .queryBuilder()
//                    .where(OverrateSelfHandleEntityDao.Properties.Zhbh.eq(currentEntity.getZhbh()))
//                    .list();
//            if (entityList != null && entityList.size() > 0) {
//                OverrateSelfHandleEntity handlerEntity = entityList.get(0);
//                GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getOverrateSelfHandleEntityDao()
//                        .delete(handlerEntity);
//            }
//
//            finish();
        } else if (item.getItemId() == android.R.id.home) {
            saveData();
//            setResult(RESULT_OK);
            mEventPosterHelper.postEventSafely(new NotifRefrashDataEvent(false));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveData();
//        setResult(RESULT_OK);
        mEventPosterHelper.postEventSafely(new NotifRefrashDataEvent(false));
        super.onBackPressed();
    }

    private void saveData() {
        tyshxydm = mEditSubmitFragment.mTongYiSHXYDM.getText().toString().trim();
        khmc = mEditSubmitFragment.mKeHuMC.getText().toString().trim();

        lxr = mEditSubmitFragment.mLianXiR.getText().toString().trim();
        lxfs = mEditSubmitFragment.mLianXiFS.getText().toString().trim();
        yjdz = mEditSubmitFragment.mYouJiDZ.getText().toString().trim();
        bz = mEditSubmitFragment.mBeiZhu.getText().toString().trim();

        if (tyshxydm.equals(currentEntity.getTyshxydm() != null ? currentEntity.getTyshxydm().trim() : "")
                && khmc.equals(currentEntity.getKhmc() != null ? currentEntity.getKhmc().trim() : "")
                && lxr.equals(currentEntity.getLxr() != null ? currentEntity.getLxr().trim() : "")
                && lxfs.equals(currentEntity.getLxfs() != null ? currentEntity.getLxfs().trim() : "")
                && yjdz.equals(currentEntity.getYjdz() != null ? currentEntity.getYjdz().trim() : "")
                && bz.equals(currentEntity.getBz() != null ? currentEntity.getBz().trim() : "")) {

        } else {
            OverrateSelfHandleEntity handleEntity = new OverrateSelfHandleEntity();
            handleEntity.setID((long) (currentEntity.getZhbh() + currentEntity.getYhh()).hashCode());
            handleEntity.setZhbh(currentEntity.getZhbh());
            handleEntity.setKhlx(currentEntity.getKhlx());
            handleEntity.setYhh(currentEntity.getYhh());
            handleEntity.setSsdm(currentEntity.getSsdm());
            handleEntity.setXzq(currentEntity.getXzq());

            handleEntity.setTyshxydm(tyshxydm);
            handleEntity.setKhmc(khmc);

            handleEntity.setLxr(lxr);
            handleEntity.setLxfs(lxfs);
            handleEntity.setYjdz(yjdz);
            handleEntity.setBz(bz);

            GreenDaoUtils.getDaoSession(this).getOverrateSelfHandleEntityDao().insertOrReplace(handleEntity);
        }

    }

    private void submitData() {
//        ToastUtils.showShort("提交数据");
        saveData();

        String zhbh = mEditSubmitFragment.mZhangHuBH.getText().toString().trim();
        String xzq = mEditSubmitFragment.mXingZhengQ.getText().toString().trim();
        String ssdm = mEditSubmitFragment.mSheShuiDM.getText().toString().trim();
        tyshxydm = mEditSubmitFragment.mTongYiSHXYDM.getText().toString().trim();
        khmc = mEditSubmitFragment.mKeHuMC.getText().toString().trim();

        lxr = mEditSubmitFragment.mLianXiR.getText().toString().trim();
        lxfs = mEditSubmitFragment.mLianXiFS.getText().toString().trim();
        yjdz = mEditSubmitFragment.mYouJiDZ.getText().toString().trim();
        bz = mEditSubmitFragment.mBeiZhu.getText().toString().trim();

        if (!TextUtils.isEmpty(currentEntity.getTyshxydm()) && TextUtils.isEmpty(tyshxydm)) {
//                    ToastUtils.showShort("统一社会信用代码不能为空！");
            ToastParams params = new ToastParams();
            params.text = "统一社会信用代码不能为空！";
            params.style = new CustomToastStyle(R.layout.toast_error);
            Toaster.show(params);
            return;
        }
        if (!TextUtils.isEmpty(tyshxydm) && StringRegexUtils.isSpecialChar(tyshxydm)) {
//                    ToastUtils.showShort("统一社会信用代码不能为空！");
            ToastParams params = new ToastParams();
            params.text = "统一社会信用代码包含特殊字符，请重新填写！";
            params.style = new CustomToastStyle(R.layout.toast_error);
            Toaster.show(params);
            return;
        }
        if (!TextUtils.isEmpty(currentEntity.getKhmc()) && TextUtils.isEmpty(khmc)) {
//                    ToastUtils.showShort("客户名称不能为空！");
            ToastParams params = new ToastParams();
            params.text = "客户名称不能为空！";
            params.style = new CustomToastStyle(R.layout.toast_error);
            Toaster.show(params);
            return;
        }
        if (!TextUtils.isEmpty(khmc) && StringRegexUtils.isSpecialChar(khmc)) {
//                    ToastUtils.showShort("统一社会信用代码不能为空！");
            ToastParams params = new ToastParams();
            params.text = "客户名称包含特殊字符，请重新填写！";
            params.style = new CustomToastStyle(R.layout.toast_error);
            Toaster.show(params);
            return;
        }
        if (!TextUtils.isEmpty(currentEntity.getLxr()) && TextUtils.isEmpty(lxr)) {
//                    ToastUtils.showShort("联系人不能为空！");
            ToastParams params = new ToastParams();
            params.text = "联系人不能为空！";
            params.style = new CustomToastStyle(R.layout.toast_error);
            Toaster.show(params);
            return;
        }
        if (!TextUtils.isEmpty(lxr) && StringRegexUtils.isSpecialChar(lxr)) {
//                    ToastUtils.showShort("统一社会信用代码不能为空！");
            ToastParams params = new ToastParams();
            params.text = "联系人包含特殊字符，请重新填写！";
            params.style = new CustomToastStyle(R.layout.toast_error);
            Toaster.show(params);
            return;
        }
        if (!TextUtils.isEmpty(currentEntity.getLxfs()) && TextUtils.isEmpty(lxfs)) {
//                    ToastUtils.showShort("联系方式不能为空！");
            ToastParams params = new ToastParams();
            params.text = "联系方式不能为空！";
            params.style = new CustomToastStyle(R.layout.toast_error);
            Toaster.show(params);
            return;
        }
        if (!TextUtils.isEmpty(lxfs) && StringRegexUtils.isSpecialChar(lxfs)) {
//                    ToastUtils.showShort("统一社会信用代码不能为空！");
            ToastParams params = new ToastParams();
            params.text = "联系方式包含特殊字符，请重新填写！";
            params.style = new CustomToastStyle(R.layout.toast_error);
            Toaster.show(params);
            return;
        }
        if (!TextUtils.isEmpty(currentEntity.getYjdz()) && TextUtils.isEmpty(yjdz)) {
//                    ToastUtils.showShort("邮寄地址不能为空！");
            ToastParams params = new ToastParams();
            params.text = "邮寄地址不能为空！";
            params.style = new CustomToastStyle(R.layout.toast_error);
            Toaster.show(params);
            return;
        }
        if (!TextUtils.isEmpty(yjdz) && StringRegexUtils.isSpecialChar(yjdz)) {
//                    ToastUtils.showShort("统一社会信用代码不能为空！");
            ToastParams params = new ToastParams();
            params.text = "邮寄地址包含特殊字符，请重新填写！";
            params.style = new CustomToastStyle(R.layout.toast_error);
            Toaster.show(params);
            return;
        }

        if (tyshxydm.equals(currentEntity.getTyshxydm()) && khmc.equals(currentEntity.getKhmc())
                && lxr.equals(currentEntity.getLxr()) && lxfs.equals(currentEntity.getLxfs())
                && yjdz.equals(currentEntity.getYjdz())) {
//            ToastUtils.showShort("请修改后再进行提交！");
            ToastParams params = new ToastParams();
            params.text = "请修改后再进行提交！";
            params.style = new CustomToastStyle(R.layout.toast_error);
            Toaster.show(params);
            return;
        }

        if (mMultimediaFileFragment instanceof MultimediaFileFragment) {
            int fileSize = mMultimediaFileFragment.getFileSize();
            if (fileSize == 0) {
//                ToastUtils.showShort(getResources().getString(R.string.add_file));
                ToastParams params = new ToastParams();
                params.text = getResources().getString(R.string.add_file);
                params.style = new CustomToastStyle(R.layout.toast_error);
                Toaster.show(params);
                return;
            }

            int pictureSize = mMultimediaFileFragment.getPictureSize();
            if (pictureSize == 0) {
//                ToastUtils.showShort(getResources().getString(R.string.add_picture));
                ToastParams params = new ToastParams();
                params.text = getResources().getString(R.string.add_picture);
                params.style = new CustomToastStyle(R.layout.toast_error);
                Toaster.show(params);
                return;
            }
        }

        AlertDialog.Builder buildDialog = new AlertDialog.Builder(this);
        buildDialog.setTitle("提示");
        buildDialog.setMessage("您确定要上传数据吗？");
        buildDialog.setCancelable(false);
        buildDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        buildDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                ApplicationsUtil.showMessage(HandleOrderActivity.this, "确定");
                uploadFiles.clear();
                signFiles.clear();
                imageFiles.clear();
                voiceFiles.clear();
                videoFiles.clear();
                loadImages(currentEntity.getZhbh(), Constant.TASK_TYPE_DOWNLOAD, TaskState.HANDLE);
//                uploadData(fyly, fynr, tyshxydm, khmc, lxr, lxfs, yjdz, bz);
                dialog.dismiss();
            }
        });
        buildDialog.create().show();
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
                            LogUtils.e("imageName", fileName);
                            if (file.exists()) {
                                uploadFiles.add(file);
                                duMedia.setFilePath(file.getPath());
                                imageFiles.add(duMedia);
                            }
                        }
                    }
                }));
    }

    public File getImagePath() {
        return new File(Environment.getExternalStorageDirectory(), "sh3h/hotline/images");
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
                        LogUtil.i(TAG, "loadVoices onNext");

                        for (DUMedia duMedia : duMediaList) {
                            String fileName = duMedia.getFileName();
                            if (TextUtil.isNullOrEmpty(fileName)) {
                                continue;
                            }

                            File file = new File(getSoundPath(), fileName);
                            LogUtils.e("voiceName", fileName);
                            if (file.exists()) {
                                uploadFiles.add(file);
                                duMedia.setFilePath(file.getPath());
                                voiceFiles.add(duMedia);
                            }
                        }
                    }
                }));
    }

    public File getSoundPath() {
        return new File(Environment.getExternalStorageDirectory(), "sh3h/hotline/sounds");
    }

    /**
     * 加载本地录像
     *
     * @param taskId
     * @param taskState
     */
    public void loadVideos(final String taskId, final int taskType, final int taskState) {
        if (TextUtil.isNullOrEmpty(taskId)) {
            LogUtil.e(TAG, "param is null");
            return;
        }

        LogUtil.i(TAG, "loadVideos" + taskId);
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
                        if (imageFiles.size() > 0) {
//                            LogUtils.e("-----------------多媒体");
//                            deleteMedias(taskId, taskType, taskState);
//                            setResult(RESULT_OK);
//                            finish();

                            commitMultimediaInfo(uploadFiles, taskId, taskType, taskState);
//                            uploadData(fyly, fynr, tyshxydm, khmc, lxr, lxfs, yjdz, bz, taskId, taskType, taskState);
                        } else {
//                            ToastUtils.showShort(ActivityUtils.getTopActivity().getResources().getString(R.string.add_file));
                            ToastParams params = new ToastParams();
                            params.text = getResources().getString(R.string.add_file);
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
//                            commitInfo("");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "loadSignup onError " + e.getMessage());
                    }

                    @Override
                    public void onNext(List<DUMedia> duMediaList) {
                        LogUtil.i(TAG, "loadSignup onNext");
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
                            LogUtils.e("signName", fileName);
                            if (file.exists()) {
                                uploadFiles.add(file);
                                duMedia.setFilePath(file.getPath());
                                signFiles.add(duMedia);
                            }
                        }
                    }
                }));
    }

    /**
     * 提交多媒体信息
     *
     * @param uploadFiles
     */
    private void commitMultimediaInfo(List<File> uploadFiles, String taskId, int taskType, int taskState) {
        mDisposable2 = EasyHttp.post(URL.AppReturnCDEOrderUploadFile)
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
                        LogUtils.e(e);
//                        ToastUtils.showShort(e.getMessage());
                        ToastParams params = new ToastParams();
                        params.text = e.getMessage();
                        params.style = new CustomToastStyle(R.layout.toast_error);
                        Toaster.show(params);
                    }

                    @Override
                    public void onSuccess(String s) {
                        LogUtils.e(s);
                        Gson gson = new Gson();
                        UploadSuccess uploadSuccess = gson.fromJson(s, UploadSuccess.class);
                        if (uploadSuccess.getState() == 0) {
                            uploadData(fyly, fynr, tyshxydm, khmc, lxr, lxfs, yjdz, bz, taskId, taskType, taskState);
                        } else {
                            MProgressDialog.dismissProgress();
//                            ToastUtils.showShort(uploadSuccess.getMsg());
                            ToastParams params = new ToastParams();
                            params.text = uploadSuccess.getMsg();
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                        }
                    }
                });
    }

    private void uploadData(String fylb, String fynr, String tyshxydm, String khmc, String lxr,
                            String lxfs, String yjdz, String bz, String taskId, int taskType, int taskState) {
        String timeDir = getTimeDir();
        LogUtils.e("-------------------------time：" + timeDir);
        String fj1 = "";
        String fj2 = "";
        String fj3 = "";
        if (imageFiles.size() == 1) {
            if (imageFiles.get(0) != null) {
                fj1 = timeDir + "/" + imageFiles.get(0).getFileName();
                LogUtils.e("-------------------------fj1：" + fj1);
            }
        } else if (imageFiles.size() == 2) {
            if (imageFiles.get(0) != null) {
                fj1 = timeDir + "/" + imageFiles.get(0).getFileName();
                LogUtils.e("-------------------------fj1：" + fj1);
            }
            if (imageFiles.get(1) != null) {
                fj2 = timeDir + "/" + imageFiles.get(1).getFileName();
                LogUtils.e("-------------------------fj2：" + fj2);
            }
        } else {
            if (imageFiles.get(0) != null) {
                fj1 = timeDir + "/" + imageFiles.get(0).getFileName();
                LogUtils.e("-------------------------fj1：" + fj1);
            }
            if (imageFiles.get(1) != null) {
                fj2 = timeDir + "/" + imageFiles.get(1).getFileName();
                LogUtils.e("-------------------------fj2：" + fj2);
            }
            if (imageFiles.get(2) != null) {
                fj3 = timeDir + "/" + imageFiles.get(2).getFileName();
                LogUtils.e("-------------------------fj3：" + fj3);
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
            LogUtils.e("-------------------------ly：" + ly);
        }

        EasyHttp
                .get(URL.FeiJuLJZLBG)
                .params("fylb", fylb)
                .params("fynr", fynr)
                .params("slsj", TimeUtils.getNowString())
                .params("fqfs", "抄表员自开单")
                .params("lxdh", "")
//                .params("cbyid", "888888")
                .params("cbyid", mDataManager.getAccount())
                .params("dz", currentEntity.getDz())

                .params("zhbh", currentEntity.getZhbh())
                .params("yhh", currentEntity.getYhh())
                .params("khmc", currentEntity.getKhmc())
                .params("tyshxydm", currentEntity.getTyshxydm())
                .params("lxr", currentEntity.getLxr())
                .params("lxfs", currentEntity.getLxfs())
                .params("yjdz", currentEntity.getYjdz())
                .params("xzq", currentEntity.getXzq())

                .params("httyshxydm", tyshxydm)
                .params("htkhmc", khmc)
                .params("htlxr", lxr)
                .params("htlxfs", lxfs)
                .params("htzddz", yjdz)
                .params("bgbz", bz)

                .params("fj1", fj1)
                .params("fj2", fj2)
                .params("fj3", fj3)
                .params("sp", sp)
                .params("ly", ly)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showProgress("上传数据中");
                    }

                    @Override
                    public void onError(ApiException e) {
                        hideProgress();
                        LogUtils.e(e);
//                        ToastUtils.showShort(e.getMessage());
                        ToastParams params = new ToastParams();
                        params.text = e.getMessage();
                        params.style = new CustomToastStyle(R.layout.toast_error);
                        Toaster.show(params);
                    }

                    @Override
                    public void onSuccess(String s) {
                        hideProgress();
                        LogUtils.e(s);
                        Gson gson = new Gson();
                        UploadDataResult uploadSuccess = gson.fromJson(s, UploadDataResult.class);
                        if (uploadSuccess.getState() == 0) {
                            ToastUtils.showShort("提交成功");

//                        GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getOverrateSelfHandleEntityDao()
//                                .deleteByKey((long) (currentEntity.getZhbh() + currentEntity.getYhh()).hashCode());
                            List<OverrateSelfHandleEntity> entityList = GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getOverrateSelfHandleEntityDao()
                                    .queryBuilder()
                                    .where(OverrateSelfHandleEntityDao.Properties.Zhbh.eq(currentEntity.getZhbh()))
                                    .list();
                            if (entityList != null && entityList.size() > 0) {
                                OverrateSelfHandleEntity handlerEntity = entityList.get(0);
                                GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getOverrateSelfHandleEntityDao()
                                        .delete(handlerEntity);
                            }

                            deleteMedias(taskId, taskType, taskState);

//                            setResult(RESULT_OK);
                            mEventPosterHelper.postEventSafely(new NotifRefrashDataEvent(true));
                            finish();
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
                        hideProgress();
                    }
                });
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //record the name of current activing fragment
        if (mCurrentFragment != null) {
            outState.putString(FRAGMENT_TAG, mCurrentFragment.getClass().getName());
        }
        if (currentEntity != null) {
            outState.putSerializable(Constant.TASK_ID, currentEntity);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i(TAG, "onActivityResult");
        if (requestCode == MultimediaFileFragment.SNAP_VIDEO) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                int type = data.getIntExtra(AliyunVideoRecorder.RESULT_TYPE, 0);
                if (type == AliyunVideoRecorder.RESULT_TYPE_RECORD) {
                    if (TextUtil.isNullOrEmpty(data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH))) {
                        return;
                    }
                    mEventPosterHelper.postEventSafely(new UIBusEvent.RecordVideo(data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH)));
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragment(transaction);

        switch (checkedId) {
            case R.id.rb_edit:
                mEditSubmitFragment = (EditSubmitFragment) mFragmentManager.findFragmentByTag(EditSubmitFragment.class.getName());
                if (mEditSubmitFragment == null) {
                    mEditSubmitFragment = EditSubmitFragment.newInstance(currentEntity, "");
                    transaction.add(R.id.fragment_container, mEditSubmitFragment, mEditSubmitFragment.getClass().getName()).commit();
                } else {
                    transaction.show(mEditSubmitFragment).commit();
                }

                mCurrentFragment = mEditSubmitFragment;
                setToolbarTitle("编辑");
                break;
            case R.id.rb_media:
                mMultimediaFileFragment = (MultimediaFileFragment) mFragmentManager.findFragmentByTag(MultimediaFileFragment.class.getName());
                if (mMultimediaFileFragment == null) {
                    mMultimediaFileFragment = MultimediaFileFragment.newInstance(currentEntity.getZhbh(), Constant.TASK_TYPE_DOWNLOAD, TaskState.HANDLE);
                    transaction.add(R.id.fragment_container, mMultimediaFileFragment, mMultimediaFileFragment.getClass().getName()).commit();
                } else {
                    transaction.show(mMultimediaFileFragment).commit();
                }

                mCurrentFragment = mEditSubmitFragment;
                setToolbarTitle("多媒体");
                break;
            default:
                break;
        }
    }

    private void hideFragment(FragmentTransaction transaction) {
        MultimediaFileFragment mMultimediaFileFragment =
                (MultimediaFileFragment) mFragmentManager.findFragmentByTag(MultimediaFileFragment.class.getName());
        if (mMultimediaFileFragment != null) {
            transaction.hide(mMultimediaFileFragment);
        }

        EditSubmitFragment mEditSubmitFragment =
                (EditSubmitFragment) mFragmentManager.findFragmentByTag(EditSubmitFragment.class.getName());
        if (mEditSubmitFragment != null) {
            transaction.hide(mEditSubmitFragment);
        }

    }

}