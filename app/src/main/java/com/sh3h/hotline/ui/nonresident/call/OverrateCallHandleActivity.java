package com.sh3h.hotline.ui.nonresident.call;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
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
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.CallListEntity;
import com.sh3h.hotline.entity.UploadDataResult;
import com.sh3h.hotline.entity.UploadSuccess;
import com.sh3h.hotline.entity.VideosPathEntity;
import com.sh3h.hotline.event.CallTransferDataEvent;
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

public class OverrateCallHandleActivity extends ParentActivity implements RadioGroup.OnCheckedChangeListener {

    private static final String STATE_FRAGMENT_SHOW = "CurrentFragment";
    private final static String TAG = "OverrateCallHandleActivity";

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
    private CallDetailFragment mOrderDetailsFragment;

    //处理
    private CallHandleFragment mHandleOrderFragment;

    //多媒体
    private MultimediaFileFragment mMultimediaFragment;

    //当前加载的fragment
    private Fragment mCurrentFragment;

    private FragmentManager mFragmentManager;

    private MenuItem submitItem;

    private CallListEntity callListEntity;

    private String origin;

    private String cjdl;
    private String cjxl;
    private String zqzddz;
    private String zqhm;
    private String xhm;
    private String jfly;
    private String qkms;
    private String ncqcjcs;
    private String cjqr;
    private String cjbz;
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
    private List<DUMedia> imageFiles;
    private List<DUMedia> voiceFiles;
    private List<DUMedia> videoFiles;

    private Disposable mDisposable2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overrate_call_handle);
        getActivityComponent().inject(this);
        mSubscription = new CompositeSubscription();
        mUnbinder = ButterKnife.bind(this);

        initToolBar(R.string.label_call_detail);

        initView(savedInstanceState);
        initFragment(savedInstanceState);
    }

    /**
     * 初始化控件
     */
    private void initView(Bundle savedInstanceState) {
        String timeDir = getTimeDir();
        LogUtils.e("-------------------------time：" + timeDir);
        uploadFiles = new ArrayList<>();
        signFiles = new ArrayList<>();
        imageFiles = new ArrayList<>();
        voiceFiles = new ArrayList<>();
        videoFiles = new ArrayList<>();
        callListEntity = (CallListEntity) getIntent().getSerializableExtra("call");
        origin = (String) getIntent().getStringExtra("origin");
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

        String origin = (String) getIntent().getStringExtra("origin");
        if ("receipt".equals(origin)) {
            submitItem.setTitle("保存");
        } else {
            submitItem.setTitle("提交");
        }

        submitItem.setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {//保存信息
            submitInfo();
            return super.onOptionsItemSelected(item);
        } else if (item.getItemId() == android.R.id.home) {
            if ("receipt".equals(origin)) {
                boolean isback = submitInfo();
                if (isback) {
                    return super.onOptionsItemSelected(item);
                } else {
                    return false;
                }
            } else {
                saveData("否");
                return super.onOptionsItemSelected(item);
            }
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if ("receipt".equals(origin)) {
            boolean isback = submitInfo();
            if (isback) {
                super.onBackPressed();
            }
        } else {
            saveData("否");
            super.onBackPressed();
        }

    }

    private void saveData(String isCJ) {
        cjdl = mHandleOrderFragment.spinnerCjdl.getSelectedItem().toString().trim();
        cjxl = mHandleOrderFragment.spinnerCjxl.getSelectedItem().toString().trim();
        zqzddz = mHandleOrderFragment.mZzddz.getText().toString().trim();
        zqhm = mHandleOrderFragment.mZhenQqueHM.getText().toString().trim();
        xhm = mHandleOrderFragment.mXnHuM.getText().toString().trim();
        jfly = mHandleOrderFragment.mJuFuLY.getText().toString().trim();
        qkms = mHandleOrderFragment.mQingKuangMS.getText().toString().trim();
        ncqcjcs = mHandleOrderFragment.mNqcjcs.getText().toString().trim();
        cjqr = mHandleOrderFragment.spinnerCjqr.getSelectedItem().toString().trim();
        cjbz = mHandleOrderFragment.mCjbz.getText().toString().trim();
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

        if ("支付问题".equals(cjdl) && "客户申请付款流程中".equals(cjxl) && "".equals(ncqcjcs)
                && "".equals(cjbz) && "否".equals(cjqr) && "否".equals(xxbg)) {
            return;
        }

//        if (tyshxydm.equals(callListEntity.getTyshxydm()) && khmc.equals(callListEntity.getKhmc())
//                && lxr.equals(callListEntity.getLxr()) && lxfs.equals(callListEntity.getLxfs())
//                && yjdz.equals(callListEntity.getYjdz()) && bz.equals(callListEntity.getBz())) {
//            return;
//        }

        OverrateCallHandleEntity handleEntity = new OverrateCallHandleEntity();
        handleEntity.setID((long) (callListEntity.getAlbh() + callListEntity.getPch()).hashCode());
        handleEntity.setAlbh(callListEntity.getAlbh());
        handleEntity.setPch(callListEntity.getPch());
        handleEntity.setXh(callListEntity.getXh());
        handleEntity.setIsCuiJiao(isCJ);

        handleEntity.setCjdl(cjdl);
        handleEntity.setCjxl(cjxl);
        handleEntity.setZqzddz(zqzddz);
        handleEntity.setZqhm(zqhm);
        handleEntity.setXhm(xhm);
        handleEntity.setJfly(jfly);
        handleEntity.setQkms(qkms);
        handleEntity.setNcqcjcs(ncqcjcs);
        handleEntity.setCjqr(cjqr);
        handleEntity.setCjbz(cjbz);
        handleEntity.setXxbg(xxbg);

        handleEntity.setZhbh(zhbh);
        handleEntity.setXzq(xzq);
        handleEntity.setSsdm(ssdm);
        handleEntity.setKhlx(callListEntity.getKhlx());
        handleEntity.setYhh(callListEntity.getYhh());
        handleEntity.setTyshxydm(tyshxydm);
        handleEntity.setKhmc(khmc);

        handleEntity.setLxr(lxr);
        handleEntity.setLxfs(lxfs);
        handleEntity.setYjdz(yjdz);
        handleEntity.setBz(bz);
        handleEntity.setYoubian(zipCode);

        GreenDaoUtils.getDaoSession(this).getOverrateCallHandleEntityDao().insertOrReplace(handleEntity);

        mEventPosterHelper.postEventSafely(new NotifRefrashDataEvent(true));
    }

    private boolean submitInfo() {
        if (mHandleOrderFragment == null) {
            ApplicationsUtil.showMessage(this, R.string.text_write_info);
            return false;
        }

        saveData("是");

        cjdl = mHandleOrderFragment.spinnerCjdl.getSelectedItem().toString().trim();
        cjxl = mHandleOrderFragment.spinnerCjxl.getSelectedItem().toString().trim();
        zqzddz = mHandleOrderFragment.mZzddz.getText().toString().trim();
        zqhm = mHandleOrderFragment.mZhenQqueHM.getText().toString().trim();
        xhm = mHandleOrderFragment.mXnHuM.getText().toString().trim();
        jfly = mHandleOrderFragment.mJuFuLY.getText().toString().trim();
        qkms = mHandleOrderFragment.mQingKuangMS.getText().toString().trim();
        ncqcjcs = mHandleOrderFragment.mNqcjcs.getText().toString().trim();
        cjqr = mHandleOrderFragment.spinnerCjqr.getSelectedItem().toString().trim();
        cjbz = mHandleOrderFragment.mCjbz.getText().toString().trim();
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

        if (TextUtils.isEmpty(cjdl)) {
//            ToastUtils.showShort("催缴大类不能为空！");
            ToastParams params = new ToastParams();
            params.text = "催缴大类不能为空！";
            params.style = new CustomToastStyle(R.layout.toast_error);
            Toaster.show(params);
            return false;
        }
        if (TextUtils.isEmpty(ncqcjcs)) {
//            ToastUtils.showShort("拟采取催缴措施不能为空！");
            ToastParams params = new ToastParams();
            params.text = "拟采取催缴措施不能为空！";
            params.style = new CustomToastStyle(R.layout.toast_error);
            Toaster.show(params);
            return false;
        }
        if (TextUtils.isEmpty(cjqr)) {
//            ToastUtils.showShort("催缴确认不能为空！");
            ToastParams params = new ToastParams();
            params.text = "催缴确认不能为空！";
            params.style = new CustomToastStyle(R.layout.toast_error);
            Toaster.show(params);
            return false;
        }

        if ("账单问题".equals(cjdl) && "要求变更“账单地址”".equals(cjxl)) {
            if (TextUtils.isEmpty(zqzddz)) {
//                ToastUtils.showShort("正确账单地址不能为空！");
                ToastParams params = new ToastParams();
                params.text = "正确账单地址不能为空！";
                params.style = new CustomToastStyle(R.layout.toast_error);
                Toaster.show(params);
                return false;
            } else {
                if (zqzddz.equals(callListEntity.getYjdz())) {
//                ToastUtils.showShort("正确账单地址不能为空！");
                    ToastParams params = new ToastParams();
                    params.text = "请填写正确账单地址！";
                    params.style = new CustomToastStyle(R.layout.toast_error);
                    Toaster.show(params);
                    return false;
                }
            }
        }

        if ("账单问题".equals(cjdl) && "户名不一致，要求更正后支付".equals(cjxl)) {
            if (TextUtils.isEmpty(zqhm)) {
//                ToastUtils.showShort("正确户名不能为空！");
                ToastParams params = new ToastParams();
                params.text = "正确户名不能为空！";
                params.style = new CustomToastStyle(R.layout.toast_error);
                Toaster.show(params);
                return false;
            } else {
                if (zqhm.equals(callListEntity.getKhmc())) {
//                ToastUtils.showShort("正确账单地址不能为空！");
                    ToastParams params = new ToastParams();
                    params.text = "请填写正确户名！";
                    params.style = new CustomToastStyle(R.layout.toast_error);
                    Toaster.show(params);
                    return false;
                }
            }
        }

        if ("账单问题".equals(cjdl) && "已过户".equals(cjxl)) {
            if (TextUtils.isEmpty(xhm)) {
//                ToastUtils.showShort("新户名不能为空！");
                ToastParams params = new ToastParams();
                params.text = "新户名不能为空！";
                params.style = new CustomToastStyle(R.layout.toast_error);
                Toaster.show(params);
                return false;
            } else {
                if (xhm.equals(callListEntity.getKhmc())) {
//                ToastUtils.showShort("正确账单地址不能为空！");
                    ToastParams params = new ToastParams();
                    params.text = "请填写新户名！";
                    params.style = new CustomToastStyle(R.layout.toast_error);
                    Toaster.show(params);
                    return false;
                }
            }
        }

        if ("超定额问题".equals(cjdl)) {
            if (TextUtils.isEmpty(qkms)) {
//                ToastUtils.showShort("情况描述不能为空！");
                ToastParams params = new ToastParams();
                params.text = "情况描述不能为空！";
                params.style = new CustomToastStyle(R.layout.toast_error);
                Toaster.show(params);
                return false;
            }
        }

        if ("其他".equals(cjdl)) {
            if (TextUtils.isEmpty(qkms)) {
//                ToastUtils.showShort("情况描述不能为空！");
                ToastParams params = new ToastParams();
                params.text = "情况描述不能为空！";
                params.style = new CustomToastStyle(R.layout.toast_error);
                Toaster.show(params);
                return false;
            }
        }

        if ("用户拒付".equals(cjdl)) {
            if (TextUtils.isEmpty(jfly)) {
//                ToastUtils.showShort("拒付理由不能为空！");
                ToastParams params = new ToastParams();
                params.text = "拒付理由不能为空！";
                params.style = new CustomToastStyle(R.layout.toast_error);
                Toaster.show(params);
                return false;
            }
        }

        final boolean[] isBack = {false};
        if ("receipt".equals(origin)) {
            isBack[0] = loadSignup(callListEntity.getAlbh(), Constant.TASK_TYPE_DOWNLOAD, TaskState.HANDLE);
        } else {
            if ("是".equals(xxbg)) {
                if (!TextUtils.isEmpty(callListEntity.getTyshxydm()) && TextUtils.isEmpty(tyshxydm)) {
//                    ToastUtils.showShort("统一社会信用代码不能为空！");
                    ToastParams params = new ToastParams();
                    params.text = "统一社会信用代码不能为空！";
                    params.style = new CustomToastStyle(R.layout.toast_error);
                    Toaster.show(params);
                    return false;
                }
                if (!TextUtils.isEmpty(tyshxydm) && StringRegexUtils.isSpecialChar(tyshxydm)) {
//                    ToastUtils.showShort("统一社会信用代码不能为空！");
                    ToastParams params = new ToastParams();
                    params.text = "统一社会信用代码包含特殊字符，请重新填写！";
                    params.style = new CustomToastStyle(R.layout.toast_error);
                    Toaster.show(params);
                    return false;
                }
                if (!TextUtils.isEmpty(callListEntity.getKhmc()) && TextUtils.isEmpty(khmc)) {
//                    ToastUtils.showShort("客户名称不能为空！");
                    ToastParams params = new ToastParams();
                    params.text = "客户名称不能为空！";
                    params.style = new CustomToastStyle(R.layout.toast_error);
                    Toaster.show(params);
                    return false;
                }
                if (!TextUtils.isEmpty(khmc) && StringRegexUtils.isSpecialChar(khmc)) {
//                    ToastUtils.showShort("统一社会信用代码不能为空！");
                    ToastParams params = new ToastParams();
                    params.text = "客户名称包含特殊字符，请重新填写！";
                    params.style = new CustomToastStyle(R.layout.toast_error);
                    Toaster.show(params);
                    return false;
                }
                if (!TextUtils.isEmpty(callListEntity.getLxr()) && TextUtils.isEmpty(lxr)) {
//                    ToastUtils.showShort("联系人不能为空！");
                    ToastParams params = new ToastParams();
                    params.text = "联系人不能为空！";
                    params.style = new CustomToastStyle(R.layout.toast_error);
                    Toaster.show(params);
                    return false;
                }
                if (!TextUtils.isEmpty(lxr) && StringRegexUtils.isSpecialChar(lxr)) {
//                    ToastUtils.showShort("统一社会信用代码不能为空！");
                    ToastParams params = new ToastParams();
                    params.text = "联系人包含特殊字符，请重新填写！";
                    params.style = new CustomToastStyle(R.layout.toast_error);
                    Toaster.show(params);
                    return false;
                }
                if (!TextUtils.isEmpty(callListEntity.getLxfs()) && TextUtils.isEmpty(lxfs)) {
//                    ToastUtils.showShort("联系方式不能为空！");
                    ToastParams params = new ToastParams();
                    params.text = "联系方式不能为空！";
                    params.style = new CustomToastStyle(R.layout.toast_error);
                    Toaster.show(params);
                    return false;
                }
                if (!TextUtils.isEmpty(lxfs) && StringRegexUtils.isSpecialChar(lxfs)) {
//                    ToastUtils.showShort("统一社会信用代码不能为空！");
                    ToastParams params = new ToastParams();
                    params.text = "联系方式包含特殊字符，请重新填写！";
                    params.style = new CustomToastStyle(R.layout.toast_error);
                    Toaster.show(params);
                    return false;
                }
                if (!TextUtils.isEmpty(callListEntity.getYjdz()) && TextUtils.isEmpty(yjdz)) {
//                    ToastUtils.showShort("邮寄地址不能为空！");
                    ToastParams params = new ToastParams();
                    params.text = "邮寄地址不能为空！";
                    params.style = new CustomToastStyle(R.layout.toast_error);
                    Toaster.show(params);
                    return false;
                }
                if (!TextUtils.isEmpty(yjdz) && StringRegexUtils.isSpecialChar(yjdz)) {
//                    ToastUtils.showShort("统一社会信用代码不能为空！");
                    ToastParams params = new ToastParams();
                    params.text = "邮寄地址包含特殊字符，请重新填写！";
                    params.style = new CustomToastStyle(R.layout.toast_error);
                    Toaster.show(params);
                    return false;
                }

                if (tyshxydm.equals(callListEntity.getTyshxydm()) && khmc.equals(callListEntity.getKhmc())
                        && lxr.equals(callListEntity.getLxr()) && lxfs.equals(callListEntity.getLxdh())
                        && yjdz.equals(callListEntity.getYjdz())) {
//                    ToastUtils.showShort("请变更信息后再进行提交！！");
                    ToastParams params = new ToastParams();
                    params.text = "请变更信息后再进行提交！";
                    params.style = new CustomToastStyle(R.layout.toast_error);
                    Toaster.show(params);
                    return false;
                }
            }

            if (mMultimediaFragment instanceof MultimediaFileFragment) {
                int fileSize = mMultimediaFragment.getFileSize();
                if (fileSize == 0) {
//                    ToastUtils.showShort(getResources().getString(R.string.add_file));
                    ToastParams params = new ToastParams();
                    params.text = getResources().getString(R.string.add_file);
                    params.style = new CustomToastStyle(R.layout.toast_error);
                    Toaster.show(params);
                    return false;
                }

                int pictureSize = mMultimediaFragment.getPictureSize();
                if (pictureSize == 0) {
//                    ToastUtils.showShort(getResources().getString(R.string.add_picture));
                    ToastParams params = new ToastParams();
                    params.text = getResources().getString(R.string.add_picture);
                    params.style = new CustomToastStyle(R.layout.toast_error);
                    Toaster.show(params);
                    return false;
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
                    loadSignup(callListEntity.getAlbh(), Constant.TASK_TYPE_DOWNLOAD, TaskState.HANDLE);
//                    uploadData(cjdl, cjxl, cjqr, jfly, qkms, ncqcjcs, cjbz, xxbg, tyshxydm, khmc, lxr, lxfs, yjdz, bz);
                    dialog.dismiss();
                }
            });
            buildDialog.create().show();
        }

        return isBack[0];
    }

    public boolean loadSignup(String taskId, int taskType, int taskState) {
        if (TextUtil.isNullOrEmpty(taskId)) {
//            getMvpView().onError("param is null");
            LogUtil.e(TAG, "param is null");
            return false;
        }

        final boolean[] isback = {false};
        LogUtil.i(TAG, "loadSignup");
        mSubscription.add(mDataManager.getMediaList(taskId, taskType,
                taskState, DUMedia.FILE_TYPE_SIGNUP)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
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
                        isback[0] = true;
                        if ("是".equals(cjqr)) {
                            if (signFiles.size() > 0) {
                                if ("receipt".equals(origin)) {
                                    backReceiptHandle();
                                } else {
                                    loadImages(taskId, taskType, taskState);
                                }
                            } else {
//                                ToastUtils.showShort("还没有签名，请签名后再进行操作");
                                ToastParams params = new ToastParams();
                                params.text = "还没有签名，请签名后再进行操作";
                                params.style = new CustomToastStyle(R.layout.toast_error);
                                Toaster.show(params);
                                isback[0] = false;
                            }
                        } else {
                            if ("receipt".equals(origin)) {
                                backReceiptHandle();
                            } else {
                                loadImages(taskId, taskType, taskState);
                            }
                        }


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

        return isback[0];
    }

    private void backReceiptHandle() {
        OverrateCallHandleEntity handleEntity = new OverrateCallHandleEntity();
        handleEntity.setID((long) (callListEntity.getAlbh() + callListEntity.getZhbh() + callListEntity.getYhh()).hashCode());
        handleEntity.setAlbh(callListEntity.getAlbh());
        handleEntity.setPch(callListEntity.getPch());
        handleEntity.setXh(callListEntity.getXh());

        handleEntity.setCjdl(cjdl);
        handleEntity.setCjxl(cjxl);
        handleEntity.setZqzddz(zqzddz);
        handleEntity.setZqhm(zqhm);
        handleEntity.setXhm(xhm);
        handleEntity.setJfly(jfly);
        handleEntity.setQkms(qkms);
        handleEntity.setNcqcjcs(ncqcjcs);
        handleEntity.setCjqr(cjqr);
        handleEntity.setCjbz(cjbz);
        handleEntity.setXxbg(xxbg);

        handleEntity.setZhbh(zhbh);
        handleEntity.setXzq(xzq);
        handleEntity.setSsdm(ssdm);
        handleEntity.setKhlx(callListEntity.getKhlx());
        handleEntity.setYhh(callListEntity.getYhh());
        handleEntity.setTyshxydm(tyshxydm);
        handleEntity.setKhmc(khmc);

        handleEntity.setLxr(lxr);
        handleEntity.setLxfs(lxfs);
        handleEntity.setYjdz(yjdz);
        handleEntity.setBz(bz);

//        Intent intent = new Intent();
//        intent.putExtra("CallHandle", handleEntity);
//        setResult(RESULT_OK, intent);
        mEventPosterHelper.postEventSafely(new CallTransferDataEvent(handleEntity));
        finish();
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
//                        LogUtil.i(TAG, "loadVideos onCompleted");
                        if (imageFiles.size() > 0) {
                            commitMultimediaInfo(uploadFiles, taskId, taskType, taskState);
//                            uploadData(cjdl, cjxl, cjqr, jfly, qkms, ncqcjcs, cjbz, xxbg, tyshxydm,
//                                    khmc, lxr, lxfs, yjdz, bz, taskId, taskType, taskState);
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
                            uploadData(cjdl, cjxl, cjqr, jfly, qkms, ncqcjcs, cjbz, xxbg, tyshxydm,
                                    khmc, lxr, lxfs, yjdz, bz, taskId, taskType, taskState);
                        } else {
                            MProgressDialog.dismissProgress();
                            ToastUtils.showShort(uploadSuccess.getMsg());
                        }
                    }
                });
    }

    private void uploadData(String cjdl, String cjxl, String cjqr, String jfly, String qkms,
                            String ncqcs, String cjbz, String xxbg, String tyshxydm, String khmc,
                            String lxr, String lxfs, String yjdz, String bz, String taskId, int taskType, int taskState) {
        String timeDir = getTimeDir();

        if ("其他".equals(cjdl)) {
            cjxl = "";
        }

        if ("用户拒付".equals(cjdl)) {
            cjxl = "";
        }

        String dzqm = "";
        if ("是".equals(cjqr)) {
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
            LogUtils.e("-------------------------ly：" + ly);
        }

        EasyHttp
                .post(URL.CuiJiaoGDBack)
                .params("albh", callListEntity.getAlbh())
                .params("xh", callListEntity.getXh())
//                .params("cjyid","888888")
                .params("cjyid", mDataManager.getAccount())
                .params("cjsj", TimeUtils.getNowString())
                .params("cjjgdl", cjdl)
                .params("cjjgxl", cjxl)
//                .params("qrcj", cjqr)
                .params("jfly", jfly)
                .params("qkms", qkms)
                .params("ncqcs", ncqcs)
                .params("cjbz", cjbz)
//                .params("wqsyy", "")

                .params("qrcj", dzqm)
                .params("fj1", fj1)
                .params("fj2", fj2)
                .params("fj3", fj3)
                .params("sp", sp)
                .params("ly", ly)

                .params("xxbg", xxbg)
//                .params("zhbh", callListEntity.getZhbh())
//                .params("khmc", callListEntity.getKhmc())
//                .params("tyshxydm", callListEntity.getTyshxydm())
//                .params("lxr", callListEntity.getLxr())
//                .params("lxfs", callListEntity.getLxdh())
//                .params("yjdz", callListEntity.getYjdz())

                .params("httyshxydm", tyshxydm)
                .params("htkhmc", khmc)
                .params("htlxr", lxr)
                .params("htlxfs", lxfs)
                .params("htzddz", yjdz)
                .params("bgbz", bz)
                .params("gdzt", "完成")
                .params("gdztyy", "")
                .params("youbian", zipCode)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onError(ApiException e) {
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
                        UploadDataResult uploadSuccess = gson.fromJson(s, UploadDataResult.class);
                        if (uploadSuccess.getState() == 0) {
                            ToastUtils.showShort("提交成功");

                            OverrateCallHandleEntity handleEntity = mHandleOrderFragment.getCallHandleEntity();
                            if (handleEntity != null) {
                                GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getOverrateCallHandleEntityDao()
                                        .delete(handleEntity);
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
                setToolbarTitle("催缴详情");
                initOrderDetailFragment();
                break;

            case R.id.delay_order_handle_rb://处理
                setToolbarTitle("催缴处理");
                initHandleOrderFragment();
                break;

            case R.id.delay_order_multimedia_rb://多媒体
                setToolbarTitle("催缴多媒体");
                initMutimediaFragment();
                break;
            default:
                break;
        }
    }

    private void initOrderDetailFragment() {
        if (mOrderDetailsFragment == null) {
            mOrderDetailsFragment = CallDetailFragment.newInstance(callListEntity, "");
        }
        changeFragment(mOrderDetailsFragment);
    }

    private void initHandleOrderFragment() {
        if (mHandleOrderFragment == null) {
            mHandleOrderFragment = CallHandleFragment.newInstance(callListEntity, origin);
        }
        changeFragment(mHandleOrderFragment);
    }

    private void initMutimediaFragment() {
        if (mMultimediaFragment == null) {
            mMultimediaFragment = MultimediaFileFragment.newInstance(callListEntity.getAlbh(), Constant.TASK_TYPE_DOWNLOAD, TaskState.HANDLE);
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
}
