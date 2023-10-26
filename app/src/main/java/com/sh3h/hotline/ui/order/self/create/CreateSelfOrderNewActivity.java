package com.sh3h.hotline.ui.order.self.create;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.aliyun.demo.recorder.AliyunVideoRecorder;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.DUMyTaskDao;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.multimedia.MultimediaNewFragment;
import com.sh3h.hotline.ui.order.myorder.history.HistoryOrdersActivity;
import com.sh3h.hotline.ui.order.self.create.receipt.ReceiptNewFragment;
import com.sh3h.hotline.util.CommonUtils;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CreateSelfOrderNewActivity extends ParentActivity
        implements CreateSelfOrderMvpView, RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "CreateSelfOrderNewActivity";
    private static final String FRAGMENT_TAG = "FragmentTAG";

    @Inject
    CreateSelfOrderPresenter mPresenter;

    @Inject
    EventPosterHelper mEventHelper;

    @Inject
    Bus mEventBus;

    @BindView(R.id.create_self_order_ll)
    LinearLayout mLayout;

    @BindView(R.id.layout_bottom_navigation)
    LinearLayout mBottomLayout;

    @BindView(R.id.bottom_navigation)
    RadioGroup mBottomNavigation;

    @BindView(R.id.rb_receipt)
    RadioButton mReceipt;

    @BindView(R.id.rb_media)
    RadioButton mMedia;

    @Inject
    EventPosterHelper mEventPosterHelper;

    private Toolbar mToolBar;

    private Unbinder mUnbinder;

    private FragmentManager mFragmentManager;

    private Fragment mCurrentFragment;

    public String mTaskId;
    public String mFuwudianBH;
    private int mTaskState = Constant.TASK_STATE_CREATE;
    private int mTaskType = Constant.TASK_TYPE_CREATE_SELF_ORDER;
    private Bundle mBundle;
    private ReceiptNewFragment mReceiptNewFragment;
    public String currentTime;
    public MultimediaNewFragment mMultimediaNewFragment;

    private MenuItem saveItem;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_self_order_new);
        currentTime = String.valueOf(System.currentTimeMillis());
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);
        mPresenter.attachView(this);
        mEventBus.register(this);

        mToolBar = initToolBar(R.string.activity_createselforder);
        mBundle = savedInstanceState;
        Intent intent = getIntent();
        if (savedInstanceState != null) {
            initParams(savedInstanceState);
        } else if (intent != null) {
            initParams(intent.getExtras());
        } else {
            initParams(null);
        }


        checkPermissions();
        LogUtil.i(TAG, "onCreate");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //record the name of current activing fragment
        if (mCurrentFragment != null) {
            outState.putString(FRAGMENT_TAG, mCurrentFragment.getClass().getName());
        }
        if (!TextUtil.isNullOrEmpty(mTaskId)) {
            outState.putString(Constant.TASK_ID, mTaskId);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mPresenter.detachView();
        mEventBus.unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        saveItem = menu.findItem(R.id.action_save);
        saveItem.setVisible(true);
        saveItem.setTitle("提交");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (isConfigInitSuccess()) {
                    save();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (!isConfigInitSuccess()) {
            LogUtil.e(TAG, "onCheckedChanged");
            return;
        }

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragment(transaction);

        switch (checkedId) {
            case R.id.rb_receipt:
                mReceiptNewFragment = (ReceiptNewFragment) mFragmentManager.findFragmentByTag(ReceiptNewFragment.class.getName());
                if (mReceiptNewFragment == null) {
                    mReceiptNewFragment = new ReceiptNewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.TASK_ID, mTaskId);
                    mReceiptNewFragment.setArguments(bundle);
                    transaction.add(R.id.fragment_container, mReceiptNewFragment, mReceiptNewFragment.getClass().getName()).commit();
                } else {
                    transaction.show(mReceiptNewFragment).commit();
                }
                mTaskId = mReceiptNewFragment.getTaskId();
                mFuwudianBH = mReceiptNewFragment.getTaskId();
                break;
            case R.id.rb_media:
                if (mReceiptNewFragment != null) {
                    mTaskId = mReceiptNewFragment.getTaskId();
                    mFuwudianBH = mReceiptNewFragment.getTaskId();
                }
                mMultimediaNewFragment = (MultimediaNewFragment) mFragmentManager.findFragmentByTag(MultimediaNewFragment.class.getName());
                if (mMultimediaNewFragment == null) {
                    mMultimediaNewFragment = new MultimediaNewFragment();
                    Bundle bundle = new Bundle();
                    if (StringUtils.isEmpty(mTaskId)) {
                        mTaskId = UUID.randomUUID().toString();
                    }
                    bundle.putString(Constant.TASK_ID, mTaskId + "");
                    bundle.putInt(Constant.TASK_STATE, mTaskState);
                    bundle.putInt(Constant.TASK_TYPE, mTaskType);
                    mMultimediaNewFragment.setArguments(bundle);
                    transaction.add(R.id.fragment_container, mMultimediaNewFragment, mMultimediaNewFragment.getClass().getName()).commit();
                } else {
                    transaction.show(mMultimediaNewFragment).commit();
                }
                break;
        }
    }

    private void initActivityData() {
        mTaskId = UUID.randomUUID().toString();
        mFragmentManager = getSupportFragmentManager();
        mCurrentFragment = initFragment(mBundle);
        mBottomNavigation.setOnCheckedChangeListener(this);
    }

    public void setFuwudianBH(String fuwudianBH) {
        if (StringUtils.isEmpty(fuwudianBH)) {
            mFuwudianBH = mReceiptNewFragment.getTaskId();
        } else {
            mFuwudianBH = fuwudianBH;
        }
    }

    public void setGone(boolean isVisible) {
        if (isVisible) {
            mMedia.setVisibility(View.VISIBLE);
        } else {
            mMedia.setVisibility(View.GONE);
        }
    }

    private void hideFragment(FragmentTransaction transaction) {
        MultimediaNewFragment MultimediaNewFragment =
                (MultimediaNewFragment) mFragmentManager.findFragmentByTag(MultimediaNewFragment.class.getName());
        if (MultimediaNewFragment != null) {
            transaction.hide(MultimediaNewFragment);
        }

        ReceiptNewFragment ReceiptNewFragment =
                (ReceiptNewFragment) mFragmentManager.findFragmentByTag(ReceiptNewFragment.class.getName());
        if (ReceiptNewFragment != null) {
            transaction.hide(ReceiptNewFragment);
        }
    }

    /**
     * init fragment
     *
     * @param savedInstanceState save the fragment state when app process is killed
     * @return current fragment
     */
    private Fragment initFragment(Bundle savedInstanceState) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        if (savedInstanceState == null) {
            mReceiptNewFragment = new ReceiptNewFragment();
            bundle.putString(Constant.TASK_ID, mTaskId);
            mReceiptNewFragment.setArguments(bundle);
            transaction.add(R.id.fragment_container, mReceiptNewFragment, ReceiptNewFragment.class.getName()).commitAllowingStateLoss();
            return mReceiptNewFragment;
        }

        String fragmentTag = savedInstanceState.getString("FRAGMENT_NAME", ReceiptNewFragment.class.getName());
        String taskId = savedInstanceState.getString(Constant.TASK_ID);
        if (fragmentTag.equals(ReceiptNewFragment.class.getName())) {
            mReceiptNewFragment = new ReceiptNewFragment();
            mTaskId = mReceiptNewFragment.getTaskId();
            mFuwudianBH = mReceiptNewFragment.getTaskId();
            bundle.putString(Constant.TASK_ID, taskId == null ? mTaskId : taskId);
            mReceiptNewFragment.setArguments(bundle);
            transaction.add(R.id.fragment_container, mReceiptNewFragment, fragmentTag).commitAllowingStateLoss();
            return mReceiptNewFragment;
        } else if (fragmentTag.equals(MultimediaNewFragment.class.getName())) {
            MultimediaNewFragment fragment = new MultimediaNewFragment();
            bundle.putString(Constant.TASK_ID, mTaskId);
            bundle.putInt(Constant.TASK_STATE, mTaskState);
            bundle.putInt(Constant.TASK_TYPE, mTaskType);
            fragment.setArguments(bundle);
            transaction.add(R.id.fragment_container, fragment, fragmentTag).commitAllowingStateLoss();
            return fragment;
        }
        return null;
    }

    /**
     * 设置布局显示状态
     *
     * @param isPictureDetail 是否是幻灯片
     */
    public void setLayout(boolean isPictureDetail) {
        if (!isConfigInitSuccess()) {
            LogUtil.e(TAG, "setLayout");
            return;
        }

        if (isPictureDetail) {
            mLayout.setBackgroundColor(Color.BLACK);
            mBottomLayout.setVisibility(View.GONE);
            if (mToolBar != null) {
                mToolBar.setVisibility(View.GONE);
            }
        } else {
            mLayout.setBackgroundColor(Color.parseColor("#0176da"));
            mBottomLayout.setVisibility(View.VISIBLE);
            if (mToolBar != null) {
                mToolBar.setVisibility(View.VISIBLE);
            }
        }
    }

    private void save() {
        mEventHelper.postEventSafely(new UIBusEvent.UploadCreateSelfOrder());
    }

    @Subscribe
    public void onInitResult(UIBusEvent.InitResult initResult) {
        LogUtil.i(TAG, "---onInitResult---" + initResult.isSuccess());
        if (initResult.isSuccess()) {
            initActivityData();

            String key = TimeUtils.getNowString(CommonUtils.getFormat("yyyy-MM-dd"));
            String account = SPUtils.getInstance().getString(Constant.USERID);
            String tips = SPUtils.getInstance(account + "_SaveOrdersTips").getString("Tips", "");
            boolean isTipsLimit = SPUtils.getInstance(account + "_SaveOrdersTips").getBoolean("IsTipsLimit", false);
            if (isTipsLimit) {
                if (!(ActivityUtils.getTopActivity() instanceof HistoryOrdersActivity)) {
                    showSaveOrdersTips();
                }
            } else {
                if (!tips.equals(key)) {
                    if (!(ActivityUtils.getTopActivity() instanceof HistoryOrdersActivity)) {
                        showSaveOrdersTips();
                    }
                }
            }

        } else {
            ApplicationsUtil.showMessage(this, R.string.text_init_failure);
        }
    }

    private void showSaveOrdersTips() {
        final AsyncSession asyncSession = GreenDaoUtils.getAsyncSession(MainApplication.getInstance());
        asyncSession.queryList(GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getDUMyTaskDao().queryBuilder()
                .where(DUMyTaskDao.Properties.TaskState.eq(Constant.ORIGIN_MY_TASK_HISTORY))
                .where(DUMyTaskDao.Properties.IsUploadSuccess.eq(Constant.NO_UPLOAD))
                .orderDesc(DUMyTaskDao.Properties.IsFlag)
//                .where(DUMyTaskDao.Properties.UserId.eq(mAccount))
                .build());
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation asyncOperation) {
                LogUtils.e("onResult", "start");
                if (asyncOperation.getType() == AsyncOperation.OperationType.QueryList) {
                    List<DUMyTask> duMyTasks = (List<DUMyTask>) asyncOperation.getResult();
                    LogUtils.e("onResult", "size=" + duMyTasks.size());
                    if (duMyTasks != null && duMyTasks.size() > 0) {
                        final String account = SPUtils.getInstance().getString(Constant.USERID);
                        String message = "" + Lists.newArrayList(Sets.newHashSet(duMyTasks)).size();
                        AlertDialog.Builder buildDialog = new AlertDialog.Builder(ActivityUtils.getTopActivity());
                        buildDialog.setTitle("提示");
                        buildDialog.setMessage("您有" + message + "条已处理工单待上传，您需要跳转到已处理工单页面去处理吗？");
                        buildDialog.setCancelable(false);
                        buildDialog.setNegativeButton("下次提示", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String key = TimeUtils.getNowString();
                                SPUtils.getInstance(account + "_SaveOrdersTips").put("Tips", key);
                                dialog.dismiss();
                            }
                        });
                        buildDialog.setPositiveButton("去上传", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String key = TimeUtils.getNowString(CommonUtils.getFormat("yyyy-MM-dd"));
                                SPUtils.getInstance(account + "_SaveOrdersTips").put("Tips", key);

                                Intent intent = new Intent(ActivityUtils.getTopActivity(), HistoryOrdersActivity.class);
                                startActivity(intent);
                                finish();
//                                ApplicationsUtil.showMessage(MyOrderListActivity.this, "确定");
                                dialog.dismiss();
                            }
                        });
                        buildDialog.create().show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.i(TAG, "onActivityResult");
        if (requestCode == MultimediaNewFragment.SNAP_VIDEO) {
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

    public void setMenuSubmitVisible(boolean b) {
        saveItem.setVisible(b);
    }
}
