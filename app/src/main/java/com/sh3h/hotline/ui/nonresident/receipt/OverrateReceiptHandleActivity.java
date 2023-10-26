package com.sh3h.hotline.ui.nonresident.receipt;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
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
import com.sh3h.dataprovider.data.entity.newentity.OverrateCallHandleEntity;
import com.sh3h.dataprovider.data.entity.newentity.OverrateReceiptHandleEntity;
import com.sh3h.dataprovider.data.entity.newentity.OverrateSelfHandleEntity;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.OverrateCallHandleEntityDao;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.ReceiptListEntity;
import com.sh3h.hotline.entity.UploadDataResult;
import com.sh3h.hotline.entity.UploadSuccess;
import com.sh3h.hotline.entity.VideosPathEntity;
import com.sh3h.hotline.event.NotifRefrashDataEvent;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.nonresident.media.MultimediaFileFragment;
import com.sh3h.hotline.ui.order.TaskState;
import com.sh3h.hotline.util.StringRegexUtils;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.body.ProgressResponseCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.request.PostRequest;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

public class OverrateReceiptHandleActivity extends ParentActivity implements RadioGroup.OnCheckedChangeListener {

    private static final String STATE_FRAGMENT_SHOW = "CurrentFragment";
    private final static String TAG = "OverrateReceiptHandleActivity";

    @Inject
    DataManager mDataManager;

    @Inject
    EventPosterHelper mEventPosterHelper;

    @BindView(R.id.delay_order_rg)
    public RadioGroup mRadioGroup;

    @BindView(R.id.delay_order_details_rb)
    RadioButton mDetailsRadioBtn;

    @BindView(R.id.delay_order_process_rb)
    RadioButton mProcessRadioBtn;

    @BindView(R.id.handle_order_ll)
    LinearLayout mLayout;

    @BindView(R.id.handle_order_bottom_ll)
    LinearLayout mBottomLayout;

    private Unbinder mUnbinder;

    //详细
    private ReceiptDetailFragment mOrderDetailsFragment;

    //处理
    private ReceiptHandleFragment mHandleOrderFragment;

    //多媒体
    private MultimediaFileFragment mMultimediaFragment;

    //当前加载的fragment
    private Fragment mCurrentFragment;

    private FragmentManager mFragmentManager;

    private MenuItem submitItem;

    private ReceiptListEntity receiptListEntity;

    private String psfs;
    private String ghxdh;
    private String sfsd;
    private String wqsyy;
    private String qsr;
    private String sjrsf;
    private String fdqkbz;
    private String xxbg;
    private String zhbh;
    private String xzq;
    private String ssdm;
    private String tyshxydm;
    private String khmc;
    private String lxr;
    private String lxfs;
    private String yjdz;
    private String bz;
    private String zipCode;

    protected CompositeSubscription mSubscription;

    private List<File> uploadFiles;
    private List<DUMedia> signFiles;
    private List<DUMedia> scanFiles;
    private List<DUMedia> imageFiles;
    private List<DUMedia> voiceFiles;
    private List<DUMedia> videoFiles;

    private Disposable mDisposable2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overrate_receip_handle);
        getActivityComponent().inject(this);
        mSubscription = new CompositeSubscription();
        mUnbinder = ButterKnife.bind(this);

        initToolBar(R.string.label_receipt_detail);

        initView(savedInstanceState);
        initFragment(savedInstanceState);
    }

    /**
     * 初始化控件
     */
    private void initView(Bundle savedInstanceState) {
        uploadFiles = new ArrayList<>();
        signFiles = new ArrayList<>();
        scanFiles = new ArrayList<>();
        imageFiles = new ArrayList<>();
        voiceFiles = new ArrayList<>();
        videoFiles = new ArrayList<>();
        receiptListEntity = (ReceiptListEntity) getIntent().getSerializableExtra("receipt");
        if (savedInstanceState == null)
            mRadioGroup.setOnCheckedChangeListener(this);
        mDetailsRadioBtn.setChecked(true);
    }

    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            initMutimediaFragment();
            initHandleOrderFragment();
            initOrderDetailFragment();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mCurrentFragment != null) {
            outState.putString(STATE_FRAGMENT_SHOW, mCurrentFragment.getClass().getName());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save_commit, menu);
        submitItem = menu.findItem(R.id.action_save);
        submitItem.setTitle("提交");
        submitItem.setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {//保存信息
            submitInfo();
        } else if (item.getItemId() == android.R.id.home) {
            saveData();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveData();
        super.onBackPressed();
    }

    private void saveData() {

        psfs = mHandleOrderFragment.mSpinnerPSFS.getSelectedItem().toString().trim();
        ghxdh = mHandleOrderFragment.mGuaHaoXDH.getText().toString().trim();
        sfsd = mHandleOrderFragment.spinnerSfsd.getSelectedItem().toString().trim();
        wqsyy = mHandleOrderFragment.spinnerWqsyy.getSelectedItem().toString().trim();
//        if (mHandleOrderFragment.spinnerQsr.getSelectedItem() == null) {
//            qsr = "";
//        } else {
//            qsr = mHandleOrderFragment.spinnerQsr.getSelectedItem().toString().trim();
//        }
        qsr = mHandleOrderFragment.getSpinnerQsr();
        sjrsf = mHandleOrderFragment.mShouJianRSF.getText().toString().trim();
        fdqkbz = mHandleOrderFragment.mFaDanQKBZ.getText().toString().trim();
        xxbg = mHandleOrderFragment.spinnerXxbg.getSelectedItem().toString().trim();

        zhbh = mHandleOrderFragment.mZhangHuBH.getText().toString().trim();
        xzq = mHandleOrderFragment.mXingZhengQ.getText().toString().trim();
        ssdm = mHandleOrderFragment.mSheShuiDM.getText().toString().trim();
        tyshxydm = mHandleOrderFragment.mTongYiSHXYDM.getText().toString().trim();
        khmc = mHandleOrderFragment.mKeHuMC.getText().toString().trim();

        lxr = mHandleOrderFragment.mLianXiR.getText().toString().trim();
        lxfs = mHandleOrderFragment.mLianXiFS.getText().toString().trim();
        yjdz = mHandleOrderFragment.mYouJiDZ.getText().toString().trim();
        bz = mHandleOrderFragment.mBeiZhu.getText().toString().trim();
        zipCode = mHandleOrderFragment.mZipCodeEt.getText().toString().trim();

        if ("上门派送".equals(psfs) && "送达".equals(sfsd) && "".equals(qsr)
                && "".equals(fdqkbz) && "否".equals(xxbg)) {
            return;
        }

//        if (tyshxydm.equals(receiptListEntity.getTyshxydm()) && khmc.equals(receiptListEntity.getKhmc())
//                && lxr.equals(receiptListEntity.getLxr()) && lxfs.equals(receiptListEntity.getLxfs())
//                && yjdz.equals(receiptListEntity.getYjdz()) && bz.equals(receiptListEntity.getBz())) {
//            return;
//        }

        OverrateReceiptHandleEntity handleEntity = new OverrateReceiptHandleEntity();
        handleEntity.setID((long) (receiptListEntity.getAlbh() + receiptListEntity.getPch()).hashCode());
        handleEntity.setPsfs(psfs);
        handleEntity.setGhxdh(ghxdh);
        handleEntity.setSfsd(sfsd);
        handleEntity.setWqsyy(wqsyy);
        handleEntity.setQsr(qsr);
        handleEntity.setSjrsf(sjrsf);
        handleEntity.setFdqkbz(fdqkbz);
        handleEntity.setXxbg(xxbg);

        handleEntity.setAlbh(receiptListEntity.getAlbh());
        handleEntity.setPch(receiptListEntity.getPch());
        handleEntity.setXh(receiptListEntity.getXh());
        handleEntity.setZhbh(receiptListEntity.getZhbh());
        handleEntity.setKhlx(receiptListEntity.getKhlx());
        handleEntity.setYhh(receiptListEntity.getYhh());
        handleEntity.setSsdm(receiptListEntity.getSsdm());
        handleEntity.setXzq(receiptListEntity.getXzq());

        handleEntity.setTyshxydm(tyshxydm);
        handleEntity.setKhmc(khmc);

        handleEntity.setLxr(lxr);
        handleEntity.setLxfs(lxfs);
        handleEntity.setYjdz(yjdz);
        handleEntity.setBz(bz);
        handleEntity.setYoubian(zipCode);

        GreenDaoUtils.getDaoSession(this).getOverrateReceiptHandleEntityDao().insertOrReplace(handleEntity);
        mEventPosterHelper.postEventSafely(new NotifRefrashDataEvent(true));
    }

    private void submitInfo() {
        if (mHandleOrderFragment == null) {
            ApplicationsUtil.showMessage(this, R.string.text_write_info);
            return;
        }
        saveData();

        psfs = mHandleOrderFragment.mSpinnerPSFS.getSelectedItem().toString().trim();
        ghxdh = mHandleOrderFragment.mGuaHaoXDH.getText().toString().trim();
        sfsd = mHandleOrderFragment.spinnerSfsd.getSelectedItem().toString().trim();
        wqsyy = mHandleOrderFragment.spinnerWqsyy.getSelectedItem().toString().trim();
//        if (mHandleOrderFragment.spinnerQsr.getSelectedItem() == null) {
//            qsr = "";
//        } else {
//            qsr = mHandleOrderFragment.spinnerQsr.getSelectedItem().toString().trim();
//        }
        qsr = mHandleOrderFragment.getSpinnerQsr();
        sjrsf = mHandleOrderFragment.mShouJianRSF.getText().toString().trim();
        fdqkbz = mHandleOrderFragment.mFaDanQKBZ.getText().toString().trim();
        xxbg = mHandleOrderFragment.spinnerXxbg.getSelectedItem().toString().trim();

        zhbh = mHandleOrderFragment.mZhangHuBH.getText().toString().trim();
        xzq = mHandleOrderFragment.mXingZhengQ.getText().toString().trim();
        ssdm = mHandleOrderFragment.mSheShuiDM.getText().toString().trim();
        tyshxydm = mHandleOrderFragment.mTongYiSHXYDM.getText().toString().trim();
        khmc = mHandleOrderFragment.mKeHuMC.getText().toString().trim();

        lxr = mHandleOrderFragment.mLianXiR.getText().toString().trim();
        lxfs = mHandleOrderFragment.mLianXiFS.getText().toString().trim();
        yjdz = mHandleOrderFragment.mYouJiDZ.getText().toString().trim();
        bz = mHandleOrderFragment.mBeiZhu.getText().toString().trim();
        zipCode = mHandleOrderFragment.mZipCodeEt.getText().toString().trim();

        if (TextUtils.isEmpty(psfs)) {
//            ToastUtils.showShort("派送方式不能为空！");
            ToastParams params = new ToastParams();
            params.text = "派送方式不能为空！";
            params.style = new CustomToastStyle(R.layout.toast_error);
            Toaster.show(params);
            return;
        }

        if ("寄挂号信".equals(psfs) && TextUtils.isEmpty(ghxdh)) {
            if (TextUtils.isEmpty(fdqkbz)) {
//                ToastUtils.showShort("发单情况备注不能为空！");
                ToastParams params = new ToastParams();
                params.text = "挂号信单号不能为空！";
                params.style = new CustomToastStyle(R.layout.toast_error);
                Toaster.show(params);
                return;
            }
        }

        if (TextUtils.isEmpty(sfsd)) {
//            ToastUtils.showShort("是否送达不能为空！");
            ToastParams params = new ToastParams();
            params.text = "是否送达不能为空！";
            params.style = new CustomToastStyle(R.layout.toast_error);
            Toaster.show(params);
            return;
        }
        if (!"寄挂号信".equals(psfs) && "送达".equals(sfsd) && TextUtils.isEmpty(qsr)) {
//            ToastUtils.showShort("签收人不能为空！");
            ToastParams params = new ToastParams();
            params.text = "签收人不能为空！";
            params.style = new CustomToastStyle(R.layout.toast_error);
            Toaster.show(params);
            return;
        }

        if (!"寄挂号信".equals(psfs) && "未送达".equals(sfsd) && ("用户拒收".equals(wqsyy) || "其他".equals(wqsyy))) {
            if (TextUtils.isEmpty(fdqkbz)) {
//                ToastUtils.showShort("发单情况备注不能为空！");
                ToastParams params = new ToastParams();
                params.text = "发单情况备注不能为空！";
                params.style = new CustomToastStyle(R.layout.toast_error);
                Toaster.show(params);
                return;
            }
        }

        if (!"寄挂号信".equals(psfs) && !"未送达".equals(sfsd) && "其他".equals(qsr)) {
            if (TextUtils.isEmpty(sjrsf)) {
//                ToastUtils.showShort("收件人身份不能为空！");
                ToastParams params = new ToastParams();
                params.text = "收件人身份不能为空！";
                params.style = new CustomToastStyle(R.layout.toast_error);
                Toaster.show(params);
                return;
            }
        }

        if ("是".equals(xxbg)) {
            if (!TextUtils.isEmpty(receiptListEntity.getTyshxydm()) && TextUtils.isEmpty(tyshxydm)) {
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
            if (!TextUtils.isEmpty(receiptListEntity.getKhmc()) && TextUtils.isEmpty(khmc)) {
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
            if (!TextUtils.isEmpty(receiptListEntity.getLxr()) && TextUtils.isEmpty(lxr)) {
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
            if (!TextUtils.isEmpty(receiptListEntity.getLxfs()) && TextUtils.isEmpty(lxfs)) {
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
            if (!TextUtils.isEmpty(receiptListEntity.getYjdz()) && TextUtils.isEmpty(yjdz)) {
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

            if (tyshxydm.equals(receiptListEntity.getTyshxydm()) && khmc.equals(receiptListEntity.getKhmc())
                    && lxr.equals(receiptListEntity.getLxr()) && lxfs.equals(receiptListEntity.getLxfs())
                    && yjdz.equals(receiptListEntity.getYjdz())) {
//                ToastUtils.showShort("请变更信息后再进行提交！");
                ToastParams params = new ToastParams();
                params.text = "请变更信息后再进行提交！";
                params.style = new CustomToastStyle(R.layout.toast_error);
                Toaster.show(params);
                return;
            }

//           if (!TextUtils.isEmpty(zipCode)) {
//              ToastParams params = new ToastParams();
//              params.text = "请添加邮政编码！";
//              params.style = new CustomToastStyle(R.layout.toast_error);
//              Toaster.show(params);
//              return;
//          }
        }

        if (mMultimediaFragment instanceof MultimediaFileFragment) {
            int fileSize = mMultimediaFragment.getFileSize();
            if (fileSize == 0) {
//                ToastUtils.showShort(getResources().getString(R.string.add_file));
                ToastParams params = new ToastParams();
                params.text = getResources().getString(R.string.add_file);
                params.style = new CustomToastStyle(R.layout.toast_error);
                Toaster.show(params);
                return;
            }

            int pictureSize = mMultimediaFragment.getPictureSize();
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
                scanFiles.clear();
                imageFiles.clear();
                voiceFiles.clear();
                videoFiles.clear();
                loadSignup(receiptListEntity.getAlbh(), Constant.TASK_TYPE_DOWNLOAD, TaskState.HANDLE);
//                uploadData(psfs, sfsd, wqsyy, wqsyy, ghxdh, qsr, sjrsf, xxbg, tyshxydm, khmc, lxr, lxfs, yjdz, bz);
                dialog.dismiss();
            }
        });
        buildDialog.create().show();
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
//                        if (uploadFiles.size() > 0) {
//                            commitMultimediaInfo(uploadFiles);
//                        } else {
//                            ToastUtils.showShort(ActivityUtils.getTopActivity().getResources().getString(R.string.add_file));
////                            commitInfo("");
//                        }
//                        if (!"寄挂号信".equals(psfs) && "送达".equals(sfsd)) {
//                            if (signFiles.size() > 0) {
//                                loadScanImages(taskId, taskType, taskState);
//                            } else {
////                                ToastUtils.showShort("还没有签名，请签名后再进行提交");
//                                ToastParams params = new ToastParams();
//                                params.text = "还没有签名，请签名后再进行提交";
//                                params.style = new CustomToastStyle(R.layout.toast_error);
//                                Toaster.show(params);
//                            }
//                        } else {
                        loadScanImages(taskId, taskType, taskState);
//                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "loadSignup onError " + e.getMessage());
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
     * 加载本地图片(扫描)
     *
     * @param taskId
     * @param taskType
     * @param taskState
     */
    public void loadScanImages(final String taskId, final int taskType, final int taskState) {
        if (TextUtil.isNullOrEmpty(taskId)) {
            LogUtil.e(TAG, "param is null");
            return;
        }

        LogUtil.i(TAG, "loadScanImages");
        mSubscription.add(mDataManager.getMediaList(taskId, taskType,
                taskState, DUMedia.FILE_TYPE_PICTURE_SCAN)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUMedia>>() {
                    @Override
                    public void onCompleted() {
                        if ("寄挂号信".equals(psfs)) {
                            if (scanFiles.size() > 0) {
                                loadImages(taskId, taskType, taskState);
                            } else {
//                                ToastUtils.showShort("还没有签名，请签名后再进行提交");
                                ToastParams params = new ToastParams();
                                params.text = "寄挂号信需要拍照，请拍照和再进行操作";
                                params.style = new CustomToastStyle(R.layout.toast_error);
                                Toaster.show(params);
                            }
                        } else {
                            loadImages(taskId, taskType, taskState);
                        }
                        LogUtil.i(TAG, "loadScanImages onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "loadScanImages onError " + e.getMessage());
                    }

                    @Override
                    public void onNext(List<DUMedia> duMediaList) {
                        LogUtil.i(TAG, "loadScanImages onNext" + duMediaList.size());

                        for (DUMedia duMedia : duMediaList) {
                            String fileName = duMedia.getFileName();
                            if (TextUtil.isNullOrEmpty(fileName)) {
                                continue;
                            }

                            File file = new File(getImagePath(), fileName);
                            LogUtils.e("scanImageName", fileName);
                            if (file.exists()) {
                                uploadFiles.add(file);
                                duMedia.setFilePath(file.getPath());
                                scanFiles.add(duMedia);
                            }
                        }
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
                        LogUtil.i(TAG, "loadImages onNext");

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
                        if (imageFiles.size() > 0) {
                            commitMultimediaInfo(uploadFiles, taskId, taskType, taskState);
                        } else {
//                            ToastUtils.showShort(ActivityUtils.getTopActivity().getResources().getString(R.string.add_file));
                            ToastParams params = new ToastParams();
                            params.text = ActivityUtils.getTopActivity().getResources().getString(R.string.add_file);
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
//                            commitInfo("");
                        }
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
                        Gson gson = new Gson();
                        UploadSuccess uploadSuccess = gson.fromJson(s, UploadSuccess.class);
                        if (uploadSuccess.getState() == 0) {
                            uploadData(psfs, sfsd, wqsyy, fdqkbz, ghxdh, qsr, sjrsf, xxbg, tyshxydm,
                                    khmc, lxr, lxfs, yjdz, bz, taskId, taskType, taskState);
                        } else {
                            MProgressDialog.dismissProgress();
                            ToastUtils.showShort(uploadSuccess.getMsg());
                        }
                    }
                });
    }

    private void uploadData(String psfs, String sfsd, String wsdyy, String fdqkbz, String ghxyjh, String qsr,
                            String qtqsr, String xxbg, String tyshxydm, String khmc, String lxr, String lxfs,
                            String yjdz, String bz, String taskId, int taskType, int taskState) {
        String timeDir = getTimeDir();

        if ("寄挂号信".equals(psfs)) {
            wsdyy = "";
            sfsd = "";
            qsr = "";
        }

        if ("送达".equals(sfsd)) {
            wsdyy = "";
        }

        if (!"其他".equals(qsr)) {
            qtqsr = "";
        }

        if ("未送达".equals(sfsd)) {
            qsr = "";
        }

        String jsyy = "";
        if ("用户拒收".equals(wqsyy)) {
            jsyy = fdqkbz;
        }

        String dzqm = "";
        if (!"寄挂号信".equals(psfs) && "送达".equals(sfsd)) {
            if (signFiles.size() > 0 && signFiles.get(0) != null) {
                dzqm = timeDir + "/" + signFiles.get(0).getFileName();
            }
        }

        String ghxzp = "";
        if ("寄挂号信".equals(psfs)) {
            if (scanFiles.size() > 0 && scanFiles.get(0) != null) {
                ghxzp = timeDir + "/" + scanFiles.get(0).getFileName();
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
                .post(URL.FaDanRWBack)
                .params("albh", receiptListEntity.getAlbh())
                .params("pch", receiptListEntity.getPch())
                .params("xh", receiptListEntity.getXh())
                .params("cjyid", mDataManager.getAccount())
                .params("psfs", psfs)
                .params("sfsd", sfsd)
//                .params("dzqm", "")
                .params("wsdyy", wsdyy)
                .params("jsyy", jsyy)
                .params("ghxyjh", ghxyjh)
//                .params("ghxzp", "")
                .params("qsr", qsr)
                .params("qtqsr", qtqsr)
                .params("bz", fdqkbz)

                .params("ghxzp", ghxzp)
                .params("dzqm", dzqm)
                .params("fj1", fj1)
                .params("fj2", fj2)
                .params("fj3", fj3)
                .params("sp", sp)
                .params("ly", ly)

                .params("xxbg", xxbg)
//                .params("zhbh", receiptListEntity.getZhbh())
//                .params("khmc", receiptListEntity.getKhmc())
//                .params("tyshxydm", receiptListEntity.getTyshxydm())
//                .params("lxr", receiptListEntity.getLxr())
//                .params("lxfs", receiptListEntity.getLxdh())
//                .params("yjdz", receiptListEntity.getYjdz())

                .params("httyshxydm", tyshxydm)
                .params("htkhmc", khmc)
                .params("htlxr", lxr)
                .params("htlxfs", lxfs)
                .params("htzddz", yjdz)
                .params("bgbz", bz)
                .params("gdzt", "完成")
                .params("gdztyy", "")
                .params("youbian", zipCode);

        if (!"寄挂号信".equals(psfs) && mHandleOrderFragment.getHandleEntity() != null) {
            String dzqm1 = "";
            if ("是".equals(mHandleOrderFragment.getHandleEntity().getCjqr())) {
                if (signFiles.size() > 0 && signFiles.get(0) != null) {
                    dzqm1 = timeDir + "/" + signFiles.get(0).getFileName();
                }
            }

            request.params("cjsj", TimeUtils.getNowString())
                    .params("cjjgdl", mHandleOrderFragment.getHandleEntity().getCjdl())
                    .params("cjjgxl", mHandleOrderFragment.getHandleEntity().getCjxl())
                    .params("qrcj", dzqm1)
                    .params("jfly", mHandleOrderFragment.getHandleEntity().getJfly())
                    .params("qkms", mHandleOrderFragment.getHandleEntity().getQkms())
                    .params("ncqcs", mHandleOrderFragment.getHandleEntity().getNcqcjcs())
                    .params("cjbz", mHandleOrderFragment.getHandleEntity().getCjbz())
                    .params("sfcj", "是");
        } else {
            request.params("sfcj", "否");
        }

        request.execute(new SimpleCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onError(ApiException e) {
//                ToastUtils.showShort(e.getMessage());
                ToastParams params = new ToastParams();
                params.text = e.getMessage();
                params.style = new CustomToastStyle(R.layout.toast_error);
                Toaster.show(params);
            }

            @Override
            public void onSuccess(String s) {
                LogUtils.e(s);
                Gson gson = new Gson();
                UploadDataResult uploadSuccess = gson.fromJson(s, UploadDataResult.class);
                if (uploadSuccess.getState() == 0) {
                    ToastUtils.showShort("提交成功");

                    OverrateReceiptHandleEntity handleEntity = mHandleOrderFragment.getReceiptHandleEntity();
                    if (handleEntity != null) {
                        GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getOverrateReceiptHandleEntityDao()
                                .delete(handleEntity);
                    }

                    List<OverrateCallHandleEntity> callEntityList = GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getOverrateCallHandleEntityDao()
                            .queryBuilder()
                            .where(OverrateCallHandleEntityDao.Properties.Albh.eq(receiptListEntity.getAlbh()))
                            .list();
                    if (callEntityList != null && callEntityList.size() > 0) {
                        OverrateCallHandleEntity callHandleEntity = callEntityList.get(0);
                        GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getOverrateCallHandleEntityDao()
                                .delete(callHandleEntity);
                    }

                    deleteMedias(taskId, taskType, taskState);

                    mEventPosterHelper.postEventSafely(new NotifRefrashDataEvent(true));
                    finish();
                } else {
                    ToastParams params = new ToastParams();
                    params.text = uploadSuccess.getMsg();
                    params.style = new CustomToastStyle(R.layout.toast_error);
                    Toaster.show(params);
                }
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
        switch (checkedId) {
            case R.id.delay_order_details_rb://详细
                setToolbarTitle("发单详情");
                initOrderDetailFragment();
                break;

            case R.id.delay_order_handle_rb://处理
                setToolbarTitle("发单处理");
                initHandleOrderFragment();
                break;

            case R.id.delay_order_multimedia_rb://多媒体
                setToolbarTitle("发单多媒体");
                initMutimediaFragment();
                mMultimediaFragment.notifyData(receiptListEntity.getAlbh(), Constant.TASK_TYPE_DOWNLOAD, TaskState.HANDLE);
                break;
            default:
                break;
        }
    }

    private void initOrderDetailFragment() {
        if (mOrderDetailsFragment == null) {
            mOrderDetailsFragment = ReceiptDetailFragment.newInstance(receiptListEntity, "");
        }
        changeFragment(mOrderDetailsFragment);
    }

    private void initHandleOrderFragment() {
        if (mHandleOrderFragment == null) {
            mHandleOrderFragment = ReceiptHandleFragment.newInstance(receiptListEntity, "");
        }
        changeFragment(mHandleOrderFragment);
    }

    private void initMutimediaFragment() {
        if (mMultimediaFragment == null) {
            mMultimediaFragment = MultimediaFileFragment.newInstance(receiptListEntity.getAlbh(), Constant.TASK_TYPE_DOWNLOAD, TaskState.HANDLE);
        }

        changeFragment(mMultimediaFragment);
    }

    /**
     * 切换Fragment
     *
     * @param fragment
     */
    private void changeFragment(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        if (mCurrentFragment != fragment) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (mCurrentFragment != null) {
                transaction.hide(mCurrentFragment);
            }
            if (!fragment.isAdded()) { // 先判断是否被add过
                // 隐藏当前的fragment，add下一个到Activity中
                transaction.add(R.id.delay_order_container, fragment, fragment.getClass().getName()).commit();
            } else {
                transaction.hide(mCurrentFragment).show(fragment).commit(); // 隐藏当前的fragment，显示下一个
            }
            mCurrentFragment = fragment;
        }
    }

    public int getPictureSize() {
        return mMultimediaFragment.getPictureSize();
    }
}
