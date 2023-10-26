package com.sh3h.hotline.ui.order.myorder.remotewater;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.RequiresApi;

import com.aliyun.demo.recorder.AliyunVideoRecorder;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hjq.toast.ToastParams;
import com.hjq.toast.Toaster;
import com.hjq.toast.style.CustomToastStyle;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.newentity.CollectionHandleEntity;
import com.sh3h.dataprovider.data.entity.newentity.RemoteHandleEntity;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.DUMyTaskDao;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.event.NotifRefrashDataEvent;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.collection.CollectionHandlePresenter;
import com.sh3h.hotline.ui.multimedia.MultimediaFragment;
import com.sh3h.hotline.ui.nonresident.media.MultimediaFileFragment;
import com.sh3h.hotline.ui.order.TaskState;
import com.sh3h.hotline.util.CollectionUtils;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

public class RemoteWaterActivity extends ParentActivity implements RadioGroup.OnCheckedChangeListener, RemoteHandleMvpView {

    private final static String TAG = "HandleOrderActivity";

    private static final String STATE_FRAGMENT_SHOW = "CurrentFragment";
    private int mState = TaskState.HANDLE;

    @Inject
    EventPosterHelper mEventPosterHelper;

    @Inject
    Bus mEventBus;

    @Inject
    RemoteHandlePresenter mHandleOrderPresenter;

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

    private Toolbar mToolBar;

    private Unbinder mUnbinder;

    //详细
    private RemoteWaterDetailFragment mOrderDetailsFragment;

    //处理
    private RemoteWaterHandleFragment mHandleOrderFragment;

    //历史-处理
    private RemoteWaterHistoryFragment mHistoryHandleOrderFragment;

    //多媒体
    private MultimediaFragment mMultimediaFragment;

    //当前加载的fragment
    private Fragment mCurrentFragment;

    private FragmentManager mFragmentManager;

    private String mTaskId;//工单编号
    private int mOrigin;
    private String mTaskReply;//工单处理
    private Long mHistoryTaskID;//历史工单主键
    private boolean mIsDataUpload;
    private boolean mIsFormHistory;

    private DUMyTask mDuMyTask;
    private RemoteHandleEntity handleEntity;
    private String cbds;
    private String dssj;
    private String bz;
    private Disposable mNetWorkDispose;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_water);
        getActivityComponent().inject(this);
        mHandleOrderPresenter.attachView(this);
        mEventBus.register(this);
        mUnbinder = ButterKnife.bind(this);

        init(savedInstanceState);

        checkPermissions();

        initView(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent == null) {
            return;
//            throw new NullPointerException("intent is null");
        }

        mToolBar = initToolBar(R.string.label_handle_order);
        mOrigin = intent.getIntExtra(Constant.ORIGIN, 0);
        mDuMyTask = intent.getParcelableExtra(Constant.DUMyTask);
        mIsFormHistory = intent.getBooleanExtra(Constant.TYPE_IS_HISTORY, false);

        switch (mOrigin) {//通过来源执行不同的操作
            case Constant.ORIGIN_MY_TASK:
            case 99:
                mTaskId = intent.getStringExtra(Constant.TASK_ID);
                break;
            case Constant.ORIGIN_MY_TASK_HISTORY:
                mTaskId = intent.getStringExtra(Constant.TASK_ID);
                mHistoryTaskID = intent.getLongExtra(DUHistoryTask.DUHistoryTask_ID, 0);
                mIsDataUpload = intent.getIntExtra(Constant.DATA_IS_UPLOAD, 0) == Constant.HAS_UPLOADED;
                mTaskReply = getIntent().getStringExtra(Constant.TASK_REPLY);
                break;
        }

        if (mOrigin == 99) {
            if (savedInstanceState != null) {
                initParams(savedInstanceState);
            } else if (intent != null) {
                initParams(intent.getExtras());
            } else {
                initParams(null);
            }
        } else {
            initFragment();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save_commit, menu);
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
    protected void onDestroy() {
        super.onDestroy();
        mHandleOrderPresenter.detachView();
        mEventBus.unregister(this);
        mUnbinder.unbind();
        EasyHttp.cancelSubscription(mNetWorkDispose);
    }

    private void saveData() {
        if (mHandleOrderFragment == null) {
            return;
        }

        cbds = mHandleOrderFragment.mEtCbds.getText().toString().trim();
        dssj = mHandleOrderFragment.mEtDssj.getText().toString().trim();
        bz = mHandleOrderFragment.mEtBz.getText().toString().trim();

        handleEntity = new RemoteHandleEntity();
        handleEntity.setID((long) (mDuMyTask.getCaseId()).hashCode());
        handleEntity.setAlbh(mDuMyTask.getCaseId());
//        handleEntity.setPch(mDuMyTask.getPch());
//        handleEntity.setXh(mDuMyTask.getXh());
//        handleEntity.setYhh(mDuMyTask.getYhh());
        handleEntity.setZhbh(mDuMyTask.getAcctId());

        handleEntity.setCbds(cbds);
        handleEntity.setDssj(dssj);
        handleEntity.setBz(bz);

        GreenDaoUtils.getDaoSession(this).getRemoteHandleEntityDao().insertOrReplace(handleEntity);
//        mEventPosterHelper.postEventSafely(new NotifRefrashDataEvent(true));
    }

    /**
     * 保存信息
     */
    private void submitInfo() {
        if (mHandleOrderFragment == null) {
            ApplicationsUtil.showMessage(this, R.string.text_write_info);
            return;
        }
        saveData();

        if (TextUtils.isEmpty(cbds)) {
//            ToastUtils.showShort("派送方式不能为空！");
            ToastParams params = new ToastParams();
            params.text = "抄表读数不能为空！";
            params.style = new CustomToastStyle(R.layout.toast_error);
            Toaster.show(params);
            return;
        }

        if (TextUtils.isEmpty(dssj)) {
//            ToastUtils.showShort("派送方式不能为空！");
            ToastParams params = new ToastParams();
            params.text = "读数时间不能为空！";
            params.style = new CustomToastStyle(R.layout.toast_error);
            Toaster.show(params);
            return;
        }

        if (mMultimediaFragment instanceof MultimediaFragment) {
            int fileSize = mMultimediaFragment.getFileSize();
            if (fileSize == 0) {
//                ToastUtils.showShort(getResources().getString(R.string.add_file));
                ToastParams params = new ToastParams();
                params.text = getResources().getString(R.string.add_file);
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

                mHandleOrderPresenter.commitMyTaskInfo(mDuMyTask, handleEntity);
//                uploadData(psfs, sfsd, wqsyy, wqsyy, ghxdh, qsr, sjrsf, xxbg, tyshxydm, khmc, lxr, lxfs, yjdz, bz);
                dialog.dismiss();
            }
        });
        buildDialog.create().show();
    }

    /**
     * 初始化控件
     */
    private void initView(Bundle savedInstanceState) {
        if (savedInstanceState == null)
            mRadioGroup.setOnCheckedChangeListener(this);
        mDetailsRadioBtn.setChecked(true);
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        initMutimediaFragment();
        initHandleOrderFragment();
        initOrderDetailFragment();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtils.e(TAG, "onRestoreInstanceState--------------------");
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fts = mFragmentManager.beginTransaction();
        //获取“内存重启”时保存的fragment名字
        String saveName = savedInstanceState.getString(STATE_FRAGMENT_SHOW);
        if (TextUtil.isNullOrEmpty(saveName)) {
            return;
        }
        mOrderDetailsFragment = (RemoteWaterDetailFragment) getSupportFragmentManager().
                findFragmentByTag(RemoteWaterDetailFragment.class.getName());
        mMultimediaFragment = (MultimediaFragment) getSupportFragmentManager().
                findFragmentByTag(MultimediaFragment.class.getName());
        mHandleOrderFragment = (RemoteWaterHandleFragment) getSupportFragmentManager().
                findFragmentByTag(RemoteWaterHandleFragment.class.getName());
        //历史-处理
        mHistoryHandleOrderFragment = (RemoteWaterHistoryFragment) getSupportFragmentManager().
                findFragmentByTag(RemoteWaterHistoryFragment.class.getName());
        if (saveName.equals(RemoteWaterDetailFragment.class.getName())) {
            if (mMultimediaFragment != null) {
                fts.hide(mMultimediaFragment);
            }
            if (mHandleOrderFragment != null) {
                fts.hide(mHandleOrderFragment);
            }
            if (mHistoryHandleOrderFragment != null) {
                fts.hide(mHistoryHandleOrderFragment);
            }

            mCurrentFragment = mOrderDetailsFragment;
        } else if (saveName.equals(MultimediaFragment.class.getName())) {
            if (mHandleOrderFragment != null) {
                fts.hide(mHandleOrderFragment);
            }
            if (mHistoryHandleOrderFragment != null) {
                fts.hide(mHistoryHandleOrderFragment);
            }
            if (mOrderDetailsFragment != null) {
                fts.hide(mOrderDetailsFragment);
            }

            mCurrentFragment = mMultimediaFragment;
        } else if (saveName.equals(RemoteWaterHandleFragment.class.getName())) {
            if (mMultimediaFragment != null) {
                fts.hide(mMultimediaFragment);
            }
            if (mHistoryHandleOrderFragment != null) {
                fts.hide(mHistoryHandleOrderFragment);
            }
            if (mHandleOrderFragment != null) {
                fts.hide(mOrderDetailsFragment);
            }

            mCurrentFragment = mHandleOrderFragment;
        } else if (saveName.equals(RemoteWaterHistoryFragment.class.getName())) {
            if (mMultimediaFragment != null) {
                fts.hide(mMultimediaFragment);
            }
            if (mHandleOrderFragment != null) {
                fts.hide(mHandleOrderFragment);
            }
            if (mCurrentFragment != null) {
                fts.hide(mHistoryHandleOrderFragment);
            }
            if (mOrderDetailsFragment != null) {
                fts.hide(mOrderDetailsFragment);
            }

            mCurrentFragment = mHistoryHandleOrderFragment;
        }
        if (mCurrentFragment != null) {
            fts.show(mCurrentFragment).commit();
        }
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        LogUtils.e(TAG, "onSaveInstanceState--------------------");
        if (mCurrentFragment != null) {
            outState.putString(STATE_FRAGMENT_SHOW, mCurrentFragment.getClass().getName());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.delay_order_details_rb://详细
                initOrderDetailFragment();
                break;
            case R.id.delay_order_handle_rb://处理
                initHandleOrderFragment();
                break;
            case R.id.delay_order_multimedia_rb://多媒体
                initMutimediaFragment();
                break;
        }
    }

    private void initOrderDetailFragment() {
        if (mOrderDetailsFragment == null) {
            mOrderDetailsFragment = new RemoteWaterDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constant.DUMyTask, mDuMyTask);
            bundle.putInt(Constant.TASKSTATE, mState);
            bundle.putString(Constant.TASK_ID, mTaskId);
            mOrderDetailsFragment.setArguments(bundle);
        }
        changeFragment(mOrderDetailsFragment);
    }

    private void initHandleOrderFragment() {
        if (mHandleOrderFragment == null) {
            mHandleOrderFragment = new RemoteWaterHandleFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constant.DUMyTask, mDuMyTask);
            bundle.putInt(Constant.TASKSTATE, mState);
            bundle.putString(Constant.TASK_ID, mTaskId);
            bundle.putBoolean(Constant.DATA_IS_UPLOAD, mIsDataUpload);
            bundle.putString(Constant.TASK_REPLY, mTaskReply);
            bundle.putBoolean(Constant.TYPE_IS_HISTORY, mIsFormHistory);
            mHandleOrderFragment.setArguments(bundle);
        }
        changeFragment(mHandleOrderFragment);
    }

    private void initMutimediaFragment() {
        if (mMultimediaFragment == null) {
            mMultimediaFragment = new MultimediaFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constant.DUMyTask, mDuMyTask);
            bundle.putBoolean(Constant.DATA_IS_UPLOAD, mIsDataUpload);
            bundle.putString(Constant.TASK_ID, mTaskId);
            if (mOrigin == Constant.ORIGIN_MY_TASK || mOrigin == Constant.ORIGIN_MY_TASK_HISTORY) {
                bundle.putInt(Constant.TASK_TYPE, Constant.TASK_TYPE_DOWNLOAD);
            } else {
                bundle.putInt(Constant.TASK_TYPE, Constant.TASK_TYPE_CREATE_SELF_ORDER);
            }
            bundle.putInt(Constant.TASK_STATE, mState);

            bundle.putInt(Constant.ORIGIN, mOrigin);
            mMultimediaFragment.setArguments(bundle);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i(TAG, "onActivityResult");
        if (requestCode == MultimediaFragment.SNAP_VIDEO) {
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
    public void onCommitTaskSuccess(String msg) {
        finish();
    }

    @Override
    public void onCommitError(String error) {
//        ToastUtils.showShort(error);
        ToastParams params = new ToastParams();
        params.text = error;
        params.style = new CustomToastStyle(R.layout.toast_error);
        Toaster.show(params);
    }

    @Override
    public void showMessage(String message) {

    }

    @Subscribe
    public void onInitResult(UIBusEvent.InitResult initResult) {
        if (mOrigin == 99) {
            loadNetworkData();
        }
    }

    public void loadNetworkData() {
        mNetWorkDispose = EasyHttp
                .post(URL.GetWordOrderList)
                .params("repCd", SPUtils.getInstance().getString(Constant.USERID))
                .execute(new CallBackProxy<CustomApiResult<List<DUMyTask>>,
                        List<DUMyTask>>(new CustomCallBack<List<DUMyTask>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                    }

                    @Override
                    public void onSuccess(final List<DUMyTask> mNetWorkDatas) {
                        if (mNetWorkDatas == null || mNetWorkDatas.size() == 0) {
                            ToastUtils.showLong("无工单数据");
                        } else {
                            List<DUMyTask> newNetWorkList = new ArrayList<>(mNetWorkDatas);
                            CollectionUtils.filter(newNetWorkList, new CollectionUtils.Predicate<DUMyTask>() {
                                @Override
                                public boolean evaluate(DUMyTask item) {
                                    return item.getCaseId().equals(mTaskId);
                                }
                            });

                            if (newNetWorkList.size() > 0) {
                                mDuMyTask = newNetWorkList.get(0);
                                initFragment();
                            }
                        }

                    }
                }) {
                });
    }
}