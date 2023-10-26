package com.sh3h.hotline.ui.order.myorder.delayorback;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.request.DUReply;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.data.entity.response.DUOrder;
import com.sh3h.dataprovider.data.entity.response.DUProcess;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.dataprovider.util.NetworkUtil;
import com.sh3h.hotline.R;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.multimedia.MultimediaFragment;
import com.sh3h.hotline.ui.order.myorder.detail.OrderDetailsFragment;
import com.sh3h.hotline.ui.process.OrderProcessFragment;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 工单退单、延期详情
 * 根据工单状态{@link #mState}分别显示退单详情、延期详情
 * 包含3个Fragment：{@link OrderDetailsFragment}{@link MultimediaFragment}{@link OrderProcessFragment}
 * 根据界面跳转标志{@link #mOrigin}执行对应的操作
 * <p>
 * Created by zhangjing on 2016/9/12.
 */
public class DelayOrBackOrderActivity extends ParentActivity implements DelayOrBackOrderMvpView,
        RadioGroup.OnCheckedChangeListener {

    private final static String TAG = "DelayOrBackOrderActivity";
    private static final String STATE_FRAGMENT_SHOW = "CurrentFragment";

    @Inject
    EventPosterHelper mEventPosterHelper;

    private int mState;
    private int mOrigin;//0是我的工单，1是历史工单
    private boolean isDataUpload;//数据是否上传

    @Inject
    DelayOrBackOrderPresenter mDelayOrBackOrderPresenter;

    @BindView(R.id.delay_order_rg)
    RadioGroup mRadioGroup;

    @BindView(R.id.delay_or_back_ll)
    LinearLayout mLayout;

    private Toolbar mToolBar;

    @BindView(R.id.delay_or_back_bottom_ll)
    LinearLayout mBottomLayout;

    private Unbinder mUnbinder;

    //详细
    private OrderDetailsFragment mOrderDetailsFragment;

    //多媒体
    private MultimediaFragment mMultimediaFragment;

    //流程
    private OrderProcessFragment mOrderProcessFragment;

    //当前加载的fragment
    private Fragment mCurrentFragment;
    private FragmentManager mFragmentManager;

    //工单编号
    private String mTaskId;

    //任务回复
    private String mTaskReply;

    private Long mHistoryTaskID;

    //工单查询传值
    private DUOrder mDUOrder;
    private ArrayList<DUProcess> mDUProcesses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delay_order);
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);
        mDelayOrBackOrderPresenter.attachView(this);

        init();
        initFragment(savedInstanceState);
        initView();
    }

    /**
     * 初始化
     */
    private void init() {
        Intent intent = getIntent();
        if (intent == null) {
            throw new NullPointerException("intent is null");
        }
        mOrigin = intent.getIntExtra(Constant.ORIGIN, 0);
        switch (mOrigin) {//通过来源执行不同的操作
            case Constant.ORIGIN_MY_TASK:
                mTaskId = intent.getStringExtra(Constant.TASK_ID);
                mState = intent.getIntExtra(Constant.TASKSTATE, 0);
                if (mState == Constant.TASK_STATE_DELAY) {
                    mToolBar = initToolBar(R.string.label_handle_order, R.string.label_delay);
                } else if (mState == Constant.TASK_STATE_BACK) {
                    mToolBar = initToolBar(R.string.label_handle_order, R.string.label_charge_back);
                }
                break;
            case Constant.ORIGIN_MY_TASK_HISTORY:
                mTaskId = intent.getStringExtra(Constant.TASK_ID);
                mState = intent.getIntExtra(Constant.TASKSTATE, 0);
                mTaskReply = intent.getStringExtra(Constant.TASK_REPLY);
                mHistoryTaskID = intent.getLongExtra(DUHistoryTask.DUHistoryTask_ID, 0);
                isDataUpload = intent.getIntExtra(Constant.DATA_IS_UPLOAD, -1) == Constant.HAS_UPLOADED;
                mToolBar = initToolBar(R.string.label_handle_order, R.string.label_delay_or_back);
                break;
            case Constant.ORIGIN_TASK_QUERY_RESULT:
                mState = intent.getIntExtra(Constant.TASKSTATE, 0);
                mDUOrder = intent.getParcelableExtra(Constant.INTENT_PARAM_ORDER);
                mDUProcesses = intent.getParcelableArrayListExtra(Constant.INTENT_PARAM_ORDER_PROCESS_LIST);
                mToolBar = initToolBar(R.string.label_handle_order, R.string.label_delay_or_back);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mOrigin == Constant.ORIGIN_MY_TASK) {
            getMenuInflater().inflate(R.menu.menu_save, menu);
        } else if (mOrigin == Constant.ORIGIN_MY_TASK_HISTORY) {
            if (!isDataUpload) {
                getMenuInflater().inflate(R.menu.menu_history_orders, menu);
            }
        } else if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
            //...
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {//保存
            saveInfo();
            LogUtil.i(TAG, "action-save");
        }
        if (item.getItemId() == R.id.action_sycn) {
            syncInfo();
            LogUtil.i(TAG, "action-sync");
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 同步信息，把修改的信息更新数据库HistoryTask
     */
    private void syncInfo() {
        //回填内容数据未上传
        if (!isDataUpload) {
            DUHistoryTask duHistoryTask = new DUHistoryTask();
            long utc = System.currentTimeMillis();
            duHistoryTask.setID(mHistoryTaskID);
            duHistoryTask.setREPLY_TIME(utc);//处理时间
            duHistoryTask.setTASK_ID(mTaskId);
            duHistoryTask.setTASK_STATE(mState);
            Gson gson = new Gson();
            DUReply duReplyOrder = new DUReply();
            duReplyOrder.setReplyTime(utc);
            duReplyOrder.setTaskId(mTaskId);
            duReplyOrder.setTaskState(String.valueOf(mState));//状态
//            duReplyOrder.setOtherReason(mOrderDetailsFragment.getmEditReason().getText().toString());
            duReplyOrder.setOtherReason("原因");
            if (mState == Constant.TASK_STATE_DELAY) {
                String date = mOrderDetailsFragment.getDate();
                if (!TextUtils.isEmpty(date)) {
                    SimpleDateFormat sdf = new SimpleDateFormat(TextUtil.FORMAT_DATE);
                    try {
                        duReplyOrder.setDelayTime(sdf.parse(date).getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            JSONObject objectReply = null;
            try {
                objectReply = new JSONObject(gson.toJson(duReplyOrder));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            duHistoryTask.setTASK_REPLY(objectReply.toString());//处理内容
            duHistoryTask.setDuReply(duReplyOrder);
            mDelayOrBackOrderPresenter.updateHistoryTask(duHistoryTask);
        }
    }

    /**
     * 保存信息
     */
    private void saveInfo() {
        String reason = "原因";
        if (TextUtils.isEmpty(reason)) {
            ApplicationsUtil.showMessage(this, "请填写原因");
            return;
        }
        String date = mOrderDetailsFragment.getmTextDate();
        long utc = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        DUHistoryTask duHistoryTask = new DUHistoryTask();
        duHistoryTask.setREPLY_TIME(utc);//处理时间
        duHistoryTask.setUPLOAD_FLAG(Constant.NO_UPLOAD);//上传标志
        duHistoryTask.setTASK_ID(mTaskId);
        duHistoryTask.setUSER_ID(mDelayOrBackOrderPresenter.getUserId());
        duHistoryTask.setTASK_TYPE(Constant.TASK_TYPE_DOWNLOAD);
        DUReply duReplyOrder = new DUReply();
        duReplyOrder.setTaskId(duHistoryTask.getTASK_ID());
        duReplyOrder.setReplyTime(utc);
        duReplyOrder.setOtherReason(reason);//延期原因或者退单原因
        if (mState == Constant.TASK_STATE_DELAY) {//延期中
            if (TextUtils.isEmpty(date)) {
                ApplicationsUtil.showMessage(this, "请填写延期日期");
                return;
            }

            try {
                duHistoryTask.setTASK_STATE(Constant.TASK_STATE_DELAY);//延期中
                duReplyOrder.setDelayTime(sdf.parse(date).getTime() + 86399000L);
                duReplyOrder.setTaskState(String.valueOf(mState));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (mState == Constant.TASK_STATE_BACK) {//退单
            duHistoryTask.setTASK_STATE(Constant.TASK_STATE_BACK);//退单
            duReplyOrder.setTaskState(String.valueOf(Constant.TASK_STATE_BACK));
        }
        DUMyTask task = mOrderDetailsFragment.getmTask();
        Gson gson = new Gson();
        try {
            JSONObject objectTask = new JSONObject(gson.toJson(task));
            JSONObject objectReply = new JSONObject(gson.toJson(duReplyOrder));
            duHistoryTask.setTASK_CONTENT(objectTask.toString());//任务内容
            duHistoryTask.setTASK_REPLY(objectReply.toString());//处理内容
        } catch (JSONException e) {
            e.printStackTrace();
        }

        duHistoryTask.setDuReply(duReplyOrder);
        mDelayOrBackOrderPresenter.saveHistoryTask(duHistoryTask);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    /**
     * 初始化fragment
     *
     * @param savedInstanceState
     */
    private void initFragment(Bundle savedInstanceState) {
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fts = mFragmentManager.beginTransaction();
        if (savedInstanceState == null) {
            mOrderDetailsFragment = new OrderDetailsFragment();
            Bundle bundle = new Bundle();
            if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {//来源 工单查询
                bundle.putInt(Constant.ORIGIN, mOrigin);
                bundle.putParcelable(Constant.INTENT_PARAM_ORDER, mDUOrder);
                for (DUProcess duProcess : mDUProcesses) {
                    if (duProcess.getTaskState() == mState) {
                        bundle.putParcelable(Constant.INTENT_PARAM_ORDER_PROCESS, duProcess);
                    }
                }
            } else {
                bundle.putInt(Constant.TASKSTATE, mState);
                bundle.putInt(Constant.ORIGIN, mOrigin);
                bundle.putBoolean(Constant.DATA_IS_UPLOAD, isDataUpload);
                bundle.putString(Constant.TASK_ID, mTaskId);
                bundle.putString(Constant.TASK_REPLY, mTaskReply);
            }
            mOrderDetailsFragment.setArguments(bundle);
            //设置tag
            fts.add(R.id.delay_order_container, mOrderDetailsFragment,
                    mOrderDetailsFragment.getClass().getName()).commit();
            mCurrentFragment = mOrderDetailsFragment;
            return;
        }
        //获取“内存重启”时保存的fragment名字
        String saveName = savedInstanceState.getString(STATE_FRAGMENT_SHOW);
        if (TextUtil.isNullOrEmpty(saveName)) {
            return;
        }
        mOrderDetailsFragment = (OrderDetailsFragment) getSupportFragmentManager().findFragmentByTag(OrderDetailsFragment.class.getName());
        mMultimediaFragment = (MultimediaFragment) getSupportFragmentManager().findFragmentByTag(MultimediaFragment.class.getName());
        mOrderProcessFragment = (OrderProcessFragment) mFragmentManager.findFragmentByTag(OrderProcessFragment.class.getName());
        if (saveName.equals(OrderDetailsFragment.class.getName())) {
            if (mMultimediaFragment != null) {
                fts.hide(mMultimediaFragment);
            }
            if (mOrderProcessFragment != null) {
                fts.hide(mOrderProcessFragment);
            }
            fts.show(mOrderDetailsFragment).commit();
            mCurrentFragment = mOrderDetailsFragment;
        } else if (saveName.equals(MultimediaFragment.class.getName())) {
            if (mOrderDetailsFragment != null) {
                fts.hide(mOrderDetailsFragment);
            }
            if (mOrderProcessFragment != null) {
                fts.hide(mOrderProcessFragment);
            }
            fts.show(mMultimediaFragment).commit();
            mCurrentFragment = mMultimediaFragment;
        } else if (saveName.equals(OrderProcessFragment.class.getName())) {
            if (mOrderDetailsFragment != null) {
                fts.hide(mOrderDetailsFragment);
            }
            if (mMultimediaFragment != null) {
                fts.hide(mMultimediaFragment);
            }
            fts.show(mOrderProcessFragment).commit();
            mCurrentFragment = mOrderProcessFragment;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_FRAGMENT_SHOW, mCurrentFragment.getClass().getName());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mDelayOrBackOrderPresenter.detachView();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.delay_order_details_rb://详细
                if (mOrderDetailsFragment == null) {
                    mOrderDetailsFragment = new OrderDetailsFragment();
                    Bundle bundle = new Bundle();
                    if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
                        bundle.putInt(Constant.ORIGIN, mOrigin);
                        bundle.putParcelable(Constant.INTENT_PARAM_ORDER, mDUOrder);
                        for (DUProcess duProcess : mDUProcesses) {
                            if (duProcess.getTaskState() == mState) {
                                bundle.putParcelable(Constant.INTENT_PARAM_ORDER_PROCESS, duProcess);
                            }
                        }
                    } else {
                        bundle.putInt(Constant.TASKSTATE, mState);
                        bundle.putInt(Constant.ORIGIN, mOrigin);
                        bundle.putString(Constant.TASK_ID, mTaskId);
                        bundle.putBoolean(Constant.DATA_IS_UPLOAD, isDataUpload);
                    }
                    mOrderDetailsFragment.setArguments(bundle);
                }
                changeFragment(mOrderDetailsFragment);
                break;
            case R.id.delay_order_multimedia_rb://多媒体
                if (mMultimediaFragment == null) {
                    mMultimediaFragment = new MultimediaFragment();
                    Bundle bundle = new Bundle();
                    if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {//来源 工单查询
                        bundle.putInt(Constant.ORIGIN, mOrigin);
                        for (DUProcess duProcess : mDUProcesses) {
                            if (duProcess.getTaskState() == mState) {
                                bundle.putParcelableArrayList(Constant.INTENT_PARAM_ORDER_PROCES_FILE, (ArrayList<? extends Parcelable>) duProcess.getFiles());
                            }
                        }
                    } else {
                        bundle.putString(Constant.TASK_ID, mTaskId);
                        bundle.putInt(Constant.TASK_TYPE, Constant.TASK_TYPE_DOWNLOAD);
                        bundle.putInt(Constant.TASK_STATE, mState);
                        bundle.putInt(Constant.ORIGIN, mOrigin);
                        bundle.putBoolean(Constant.DATA_IS_UPLOAD, isDataUpload);
                    }
                    mMultimediaFragment.setArguments(bundle);
                }
                changeFragment(mMultimediaFragment);
                break;
            case R.id.delay_order_process_rb:
                if (mOrderProcessFragment == null) {
                    mOrderProcessFragment = new OrderProcessFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constant.ORIGIN, mOrigin);
                    if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
                        bundle.putParcelable(Constant.INTENT_PARAM_ORDER, mDUOrder);
                        bundle.putParcelableArrayList(Constant.INTENT_PARAM_ORDER_PROCESS_LIST, mDUProcesses);
                    } else {
                        bundle.putString(Constant.TASK_ID, mTaskId);
                    }
                    mOrderProcessFragment.setArguments(bundle);
                }
                changeFragment(mOrderProcessFragment);
                break;
        }
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

    /**
     * 设置布局显示状态
     *
     * @param isPictureDetail
     */
    public void setLayout(boolean isPictureDetail) {
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

    @Override
    public void onSaveHistoryTask(Boolean aBoolean, DUHistoryTask duHistoryTask) {
        if ((!aBoolean)
                || (duHistoryTask == null)
                || (duHistoryTask.getDuReply() == null)) {
            LogUtil.e(TAG, "param is error");
            return;
        }

        ApplicationsUtil.showMessage(this, R.string.toast_data_has_saved);
        if (NetworkUtil.isNetworkConnected(this)) {
//            uploadReply(duHistoryTask.getTASK_ID(), duHistoryTask.getTASK_TYPE(),
//                    duHistoryTask.getTASK_STATE(), duHistoryTask.getREPLY_TIME());
            uploadReplies(duHistoryTask.getTASK_ID(), Constant.TASK_TYPE_DOWNLOAD_ORDER);
        }
        finish();
        mEventPosterHelper.postEventSafely(new UIBusEvent.NotifyMyTasksUI());
    }

    @Override
    public void showMessage(String message) {
        ApplicationsUtil.showMessage(this, message);
    }

    @Override
    public void onUpdateHistoryTask(Boolean aBoolean, DUHistoryTask duHistoryTask) {
        if ((!aBoolean)
                || (duHistoryTask == null)
                || duHistoryTask.getDuReply() == null) {
            LogUtil.e(TAG, "param is error");
            return;
        }
        ApplicationsUtil.showMessage(this, R.string.toast_data_has_updated);

        if (NetworkUtil.isNetworkConnected(this)) {
            uploadReplies(duHistoryTask.getTASK_ID(), Constant.TASK_TYPE_DOWNLOAD_ORDER);
        }
        finish();
        mEventPosterHelper.postEventSafely(new UIBusEvent.NotifyHistoryTasksUI());
    }


}
