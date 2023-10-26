package com.sh3h.hotline.ui.order.myorder.handle;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.aliyun.demo.recorder.AliyunVideoRecorder;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.newentity.HandleOrderEntity;
import com.sh3h.dataprovider.data.entity.request.DUReply;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.data.entity.response.DUOrder;
import com.sh3h.dataprovider.data.entity.response.DUProcess;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.dataprovider.util.NetworkUtil;
import com.sh3h.hotline.R;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.multimedia.MultimediaFragment;
import com.sh3h.hotline.ui.order.TaskState;
import com.sh3h.hotline.ui.order.myorder.detail.OrderDetailsFragment;
import com.sh3h.hotline.ui.order.myorder.history.HistoryOrdersActivity;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.TextUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 工单处理界面
 * Created by zhangjing on 2016/9/14.
 */
public class HandleOrderActivity extends ParentActivity implements HandleOrderMvpView,
        RadioGroup.OnCheckedChangeListener {

    private final static String TAG = "HandleOrderActivity";

    private static final String STATE_FRAGMENT_SHOW = "CurrentFragment";
    private int mState = TaskState.HANDLE;

    @Inject
    EventPosterHelper mEventPosterHelper;

    @Inject
    HandleOrderPresenter mHandleOrderPresenter;

    @Inject
    ConfigHelper mConfigHelper;

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
    private OrderDetailsFragment mOrderDetailsFragment;

    //处理
    private HandleOrderFragment mHandleOrderFragment;

    private HistoryHandleOrderFragment mHistoryHandleOrderFragment;

    //多媒体
    private MultimediaFragment mMultimediaFragment;

//    private OrderProcessFragment mOrderProcessFragment;

    //当前加载的fragment
    private Fragment mCurrentFragment;
    private FragmentManager mFragmentManager;

    private int mOrigin;
    private boolean mIsDataUpload;
    private String mTaskId;//工单编号
    private String mTaskReply;//工单处理
    private Long mHistoryTaskID;//历史工单主键
    private String mServerTaskId;//自开单的服务器taskId
    private String mIssueType;
    private String mIssueContent;

    //工单查询传值
    private DUOrder mDUOrder;
    private ArrayList<DUProcess> mDUProcesses;
    private DUMyTask mDuMyTask;
    private boolean mIsFormHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.e(TAG, "onCreate------------------");
        setContentView(R.layout.activity_handle_order);
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);
        mHandleOrderPresenter.attachView(this);

        init();
        initView(savedInstanceState);
        initFragment(savedInstanceState);

    }

    private void init() {
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
                mTaskId = intent.getStringExtra(Constant.TASK_ID);
                mIssueType = getIntent().getStringExtra(Constant.ISSUE_TYPE);
                mIssueContent = getIntent().getStringExtra(Constant.ISSUE_CONTENT);
                break;
            case Constant.ORIGIN_CREATE_SELF_ORDER:
                mTaskId = intent.getStringExtra(Constant.TASK_ID);
                mIssueType = getIntent().getStringExtra(Constant.ISSUE_TYPE);
                mIssueContent = getIntent().getStringExtra(Constant.ISSUE_CONTENT);
                break;
            case Constant.ORIGIN_CREATE_SELF_ORDER_HISTORY://自开单历史
                mServerTaskId = getIntent().getStringExtra(Constant.SERVER_TASK_ID);
                mTaskId = intent.getStringExtra(Constant.TASK_ID);
                mIsDataUpload = intent.getIntExtra(Constant.DATA_IS_UPLOAD, 0) == Constant.HAS_UPLOADED;
                mTaskReply = getIntent().getStringExtra(Constant.TASK_REPLY);
                mHistoryTaskID = intent.getLongExtra(DUHistoryTask.DUHistoryTask_ID, 0);
                break;
            case Constant.ORIGIN_MY_TASK_HISTORY:
                mTaskId = intent.getStringExtra(Constant.TASK_ID);
                mHistoryTaskID = intent.getLongExtra(DUHistoryTask.DUHistoryTask_ID, 0);
                mIsDataUpload = intent.getIntExtra(Constant.DATA_IS_UPLOAD, 0) == Constant.HAS_UPLOADED;
                mTaskReply = getIntent().getStringExtra(Constant.TASK_REPLY);
                break;
            case Constant.ORIGIN_TASK_QUERY_RESULT:
                mDUOrder = intent.getParcelableExtra(Constant.INTENT_PARAM_ORDER);
                mDUProcesses = intent.getParcelableArrayListExtra(Constant.INTENT_PARAM_ORDER_PROCESS_LIST);
                break;
        }
        Log.e("反应内容", "反应内容：" + mIssueContent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mOrigin == Constant.ORIGIN_MY_TASK || mOrigin == Constant.ORIGIN_CREATE_SELF_ORDER) {
            getMenuInflater().inflate(R.menu.menu_save_commit, menu);
        } else if (mOrigin == Constant.ORIGIN_MY_TASK_HISTORY || mOrigin == Constant.ORIGIN_CREATE_SELF_ORDER_HISTORY) {
            if (!mIsDataUpload) {
                getMenuInflater().inflate(R.menu.menu_history_orders, menu);
            }
        } else if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
            //...
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {//保存信息
            saveInfo();
        }
        if (item.getItemId() == R.id.action_sycn) {
//            syncInfo();
            saveInfo();
        }
//        if (item.getItemId() == R.id.action_commit) {
//            commitInfo();
//        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 提交信息
     */
    private void commitInfo() {
        if (mHandleOrderFragment == null) {
            ApplicationsUtil.showMessage(this, R.string.text_write_info);
            return;
        }

        //判断处理类别
        String resolveType = mHandleOrderFragment.getmHandleType();
        String resolveContent = mHandleOrderFragment.getmHandleContent();
        String issueReason = mHandleOrderFragment.getmIssueReason();
        String resolveMethod = mHandleOrderFragment.getmSolveMeasure();
        String resolveResult = mHandleOrderFragment.getmSolveResult();//处理结果
        String fuheCM = mHandleOrderFragment.getFuheChaoMa();//复核抄码
        String chaobiaoZT = mHandleOrderFragment.getChaoBiaoZT();//抄表状态
        String resolveComment = mHandleOrderFragment.getmReceiveComment();
        long resolveTime = mHandleOrderFragment.getHandleTime();//处理时间
        long arriveTime = mHandleOrderFragment.getArriveTime();//到场时间
        if (TextUtils.isEmpty(resolveType)
                || TextUtils.isEmpty(resolveComment)
                || TextUtils.isEmpty(resolveContent)
                || TextUtils.isEmpty(issueReason)
                || TextUtils.isEmpty(resolveMethod)
                || TextUtils.isEmpty(resolveResult)
                || resolveTime == 0 || arriveTime == 0) {
            ApplicationsUtil.showMessage(this, R.string.text_write_info);
            return;
        }
        if (mHandleOrderFragment.mLlFuchaoChaoma != null && mHandleOrderFragment.mLlChaobiaoZt != null
                && mHandleOrderFragment.mLlFuchaoChaoma.getVisibility() == View.VISIBLE &&
                mHandleOrderFragment.mLlChaobiaoZt.getVisibility() == View.VISIBLE &&
                (TextUtils.isEmpty(fuheCM) || TextUtils.isEmpty(chaobiaoZT))) {
            ApplicationsUtil.showMessage(this, R.string.text_write_info);
            return;
        }
        HandleOrderEntity handleOrderEntity = new HandleOrderEntity();
        handleOrderEntity.setFaId(mTaskId);
        handleOrderEntity.setFaTypeCd(resolveType);
        handleOrderEntity.setClnr(resolveContent);
        handleOrderEntity.setFaReason(issueReason);
        handleOrderEntity.setFaAct(resolveMethod);
        handleOrderEntity.setCaseId(mDuMyTask.getCaseId());
        handleOrderEntity.setOldCaseId(mDuMyTask.getOldCaseId());
        handleOrderEntity.setArriveDt(TimeUtils.millis2String(arriveTime));
        handleOrderEntity.setRepCd(mDuMyTask.getRepCd().trim());
        handleOrderEntity.setComment(resolveComment);
        handleOrderEntity.setCmSta("C");
        //---------------
        handleOrderEntity.setCljg(resolveResult);
        handleOrderEntity.setRegRead(fuheCM);
        handleOrderEntity.setCbzt(chaobiaoZT);
        //--------------
        handleOrderEntity.setFinishDt(TimeUtils.millis2String(resolveTime));
        mHandleOrderPresenter.commitMyTaskInfo(mDuMyTask, handleOrderEntity);
    }

    /**
     * 同步信息
     */
    private void syncInfo() {
        if (!mIsDataUpload) {
            long utc = System.currentTimeMillis();
            DUHistoryTask duHistoryTask = new DUHistoryTask();
            duHistoryTask.setID(mHistoryTaskID);
            duHistoryTask.setUSER_ID(mHandleOrderPresenter.getUserId());//userid
            duHistoryTask.setTASK_ID(mTaskId);//任务编号
            duHistoryTask.setTASK_TYPE(Constant.TASK_TYPE_DOWNLOAD);//任务类型
            duHistoryTask.setTASK_STATE(TaskState.HANDLE);//任务状态
            duHistoryTask.setREPLY_TIME(utc);//处理时间
            if (mHandleOrderFragment != null) {
                String resolveType = mHandleOrderFragment.getmHandleType();
                String resolveContent = mHandleOrderFragment.getmHandleContent();
                String issueReason = mHandleOrderFragment.getmIssueReason();
                String resolveMethod = mHandleOrderFragment.getmSolveMeasure();
                String resolveResult = mHandleOrderFragment.getmSolveResult();//处理结果
                String resolveComment = mHandleOrderFragment.getmReceiveComment();
                long resolveTime = mHandleOrderFragment.getHandleTime();//处理时间
                if (TextUtils.isEmpty(resolveType)
                        || TextUtils.isEmpty(resolveComment)
                        || TextUtils.isEmpty(resolveResult)
                        || resolveTime == 0) {
                    ApplicationsUtil.showMessage(this, R.string.text_write_info);
                    return;
                }

                String handleTypeValue = mHandleOrderFragment.getmHandleTypeValue();
                String configHandleType;
                if (TextUtil.isNullOrEmpty(mConfigHelper.getHandleTypeValue())) {
                    configHandleType = "012004";//表务问题
                } else {
                    configHandleType = mConfigHelper.getHandleTypeValue();
                }
                //表务问题,弹框选择是否完成销单
                if (Arrays.asList(configHandleType.split(",")).contains(handleTypeValue)) {
                    LogUtils.i(TAG, "handle type contains");
                    showIsFinishFromSync(duHistoryTask, utc, resolveType, resolveContent, issueReason,
                            resolveMethod, resolveResult, resolveComment, resolveTime);
                    return;
                }

                DUReply duReplyOrder = new DUReply();
                duReplyOrder.setTaskId(duHistoryTask.getTASK_ID());
                if (!TextUtil.isNullOrEmpty(mServerTaskId)) {
                    duReplyOrder.setTaskId(mServerTaskId);
                }
                duReplyOrder.setTaskState(String.valueOf(TaskState.HANDLE));
                duReplyOrder.setReplyTime(utc);
                duReplyOrder.setResolveType(resolveType);//处理类别
                duReplyOrder.setResolveContent(resolveContent);//处理内容
                duReplyOrder.setIssueReason(issueReason);//发生原因
                duReplyOrder.setResolveResult(resolveResult);//处理结果
                duReplyOrder.setResolveMethod(resolveMethod);//解决措施
                duReplyOrder.setResolveComment(resolveComment);//处理备注
                duReplyOrder.setFinishTask(true);//是否完成销单
                try {
                    Gson gson = new Gson();
                    JSONObject objectReply = new JSONObject(gson.toJson(duReplyOrder));
                    duHistoryTask.setTASK_REPLY(objectReply.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                duHistoryTask.setDuReply(duReplyOrder);
            }
            mHandleOrderPresenter.updateHistoryTask(duHistoryTask);
        }
    }

    /**
     * 更新处理信息(未上传数据更新)，特殊类别，例：表务问题
     *
     * @param duHistoryTask
     * @param utc
     * @param resolveType
     * @param resolveContent
     * @param issueReason
     * @param resolveMethod
     * @param resolveResult
     * @param resolveComment
     * @param isFinishTask
     */
    private void syncHandleInfo(DUHistoryTask duHistoryTask, long utc, String resolveType,
                                String resolveContent, String issueReason, String resolveMethod,
                                String resolveResult, String resolveComment, boolean isFinishTask, long resolveTime) {
        DUReply duReplyOrder = new DUReply();
        duReplyOrder.setTaskId(duHistoryTask.getTASK_ID());
        if (!TextUtil.isNullOrEmpty(mServerTaskId)) {
            duReplyOrder.setTaskId(mServerTaskId);
        }
        duReplyOrder.setTaskState(String.valueOf(TaskState.HANDLE));
        duReplyOrder.setReplyTime(utc);
        duReplyOrder.setResolveType(resolveType);//处理类别
        duReplyOrder.setResolveContent(resolveContent);//处理内容
        duReplyOrder.setIssueReason(issueReason);//发生原因
        duReplyOrder.setResolveResult(resolveResult);//处理结果
        duReplyOrder.setResolveMethod(resolveMethod);//解决措施
        duReplyOrder.setResolveComment(resolveComment);//处理备注
        duReplyOrder.setResolveTime(resolveTime);//处理时间
        duReplyOrder.setFinishTask(isFinishTask);//是否完成销单
        try {
            Gson gson = new Gson();
            JSONObject objectReply = new JSONObject(gson.toJson(duReplyOrder));
            duHistoryTask.setTASK_REPLY(objectReply.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        duHistoryTask.setDuReply(duReplyOrder);
        mHandleOrderPresenter.updateHistoryTask(duHistoryTask);
    }

    /**
     * 保存信息
     */
    private void saveInfo() {
        if (mHandleOrderFragment == null) {
            ApplicationsUtil.showMessage(this, R.string.text_write_info);
            return;
        }

        //判断处理类别
        String resolveType = mHandleOrderFragment.getmHandleType();
        //处理内容
        String resolveContent = mHandleOrderFragment.getmHandleContent();
        //发生原因
        String issueReason = mHandleOrderFragment.getmIssueReason();
        //解决措施
        String resolveMethod = mHandleOrderFragment.getmSolveMeasure();
        //处理结果
        String resolveResult = mHandleOrderFragment.getmSolveResult();
        //复核抄码
        String fuheCM = mHandleOrderFragment.getFuheChaoMa();
        Log.e("fuhechaoma", "fuhechaoma is " + fuheCM);
        //抄表状态
        String chaobiaoZt = mHandleOrderFragment.getChaoBiaoZT();
        //处理备注
        String resolveComment = mHandleOrderFragment.getmReceiveComment();
        //处理时间
        long resolveTime = mHandleOrderFragment.getHandleTime();//处理时间
        long arriveTime = mHandleOrderFragment.getArriveTime();//到场时间
        if (TextUtils.isEmpty(resolveType)
                || TextUtils.isEmpty(resolveComment)
                || TextUtils.isEmpty(resolveContent)
                || TextUtils.isEmpty(issueReason)
                || TextUtils.isEmpty(resolveMethod)
                || TextUtils.isEmpty(resolveResult)
                || resolveTime == 0) {
            ApplicationsUtil.showMessage(this, R.string.text_write_info);
            return;
        }
        if (mHandleOrderFragment.mLlFuchaoChaoma != null && mHandleOrderFragment.mLlChaobiaoZt != null
                && mHandleOrderFragment.mLlFuchaoChaoma.getVisibility() == View.VISIBLE &&
                mHandleOrderFragment.mLlChaobiaoZt.getVisibility() == View.VISIBLE &&
                (TextUtils.isEmpty(fuheCM) || TextUtils.isEmpty(chaobiaoZt))) {
            ApplicationsUtil.showMessage(this, R.string.text_write_info);
            return;
        }
        final HandleOrderEntity handleOrderEntity = new HandleOrderEntity();
//        handleOrderEntity.setID((long) mDuMyTask.getFaId().hashCode());
        handleOrderEntity.setFaId(mTaskId);
        handleOrderEntity.setFaTypeCd(resolveType);
        handleOrderEntity.setClnr(resolveContent);
        handleOrderEntity.setFaReason(issueReason);
        handleOrderEntity.setFaAct(resolveMethod);
        handleOrderEntity.setCaseId(mDuMyTask.getCaseId());
        handleOrderEntity.setOldCaseId(mDuMyTask.getOldCaseId());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        handleOrderEntity.setArriveDt(TimeUtils.millis2String(arriveTime, format));
        handleOrderEntity.setRepCd(mDuMyTask.getRepCd());
        handleOrderEntity.setComment(resolveComment);
        handleOrderEntity.setCmSta("C");
        //---------------
        handleOrderEntity.setCljg(resolveResult);
        handleOrderEntity.setRegRead(fuheCM);
        handleOrderEntity.setCbzt(chaobiaoZt);
        //--------------
        handleOrderEntity.setFinishDt(TimeUtils.millis2String(resolveTime, format));
        if (mMultimediaFragment instanceof MultimediaFragment) {
            int fileSize = ((MultimediaFragment) mMultimediaFragment).getFileSize();
            if (fileSize == 0) {
                ToastUtils.showShort(getResources().getString(R.string.add_file));
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
                mHandleOrderPresenter.saveToHistoryTask(mDuMyTask, handleOrderEntity);
//                ApplicationsUtil.showMessage(HandleOrderActivity.this, "确定");
                dialog.dismiss();
            }
        });
        buildDialog.create().show();

//        String handleTypeValue = mHandleOrderFragment.getmHandleTypeValue();
//        String configHandleType;
//        if (TextUtil.isNullOrEmpty(mConfigHelper.getHandleTypeValue())) {
//            configHandleType = "012004";
//        } else {
//            configHandleType = mConfigHelper.getHandleTypeValue();
//        }
//        if (Arrays.asList(configHandleType.split(",")).contains(handleTypeValue)) {
//            LogUtils.i(TAG, "handle type contains");
//            showIsFinishFromSave(resolveType, resolveContent, issueReason, resolveMethod,
//                    resolveResult, resolveComment, resolveTime);
//            return;
//        }
//        long utc = System.currentTimeMillis();
//        DUHistoryTask duHistoryTask = new DUHistoryTask();
//        duHistoryTask.setUSER_ID(mHandleOrderPresenter.getUserId());//userid
//        duHistoryTask.setTASK_ID(mTaskId);//任务编号
//        if (mOrigin == Constant.ORIGIN_MY_TASK || mOrigin == Constant.ORIGIN_MY_TASK_HISTORY) {
//            duHistoryTask.setTASK_TYPE(Constant.TASK_TYPE_DOWNLOAD);//任务类型
//        } else {
//            duHistoryTask.setTASK_TYPE(Constant.TASK_TYPE_CREATE_SELF_ORDER);//任务类型
//        }
//        duHistoryTask.setTASK_STATE(TaskState.HANDLE);//任务状态
//        DUReply duReplyOrder = new DUReply();
//        duReplyOrder.setTaskId(duHistoryTask.getTASK_ID());
//        duReplyOrder.setTaskState(TaskState.HANDLE + "");
//        duReplyOrder.setReplyTime(utc);
//        duReplyOrder.setResolveType(resolveType);//处理类别
//        duReplyOrder.setResolveContent(resolveContent);//处理内容
//        duReplyOrder.setIssueReason(issueReason);//发生原因
//        duReplyOrder.setResolveMethod(resolveMethod);//解决措施
//        duReplyOrder.setResolveResult(resolveResult);//处理结果
//        duReplyOrder.setResolveComment(resolveComment);//处理备注
//        duReplyOrder.setResolveTime(resolveTime);
//        duReplyOrder.setFinishTask(true);
//        try {
//            Gson gson = new Gson();
//            if (mOrderDetailsFragment.getmTask() != null) {
//                JSONObject objectTask = new JSONObject(gson.toJson(mOrderDetailsFragment.getmTask()));
//                duHistoryTask.setTASK_CONTENT(objectTask.toString());
//            }
//            JSONObject objectReply = new JSONObject(gson.toJson(duReplyOrder));
//            duHistoryTask.setTASK_REPLY(objectReply.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        duHistoryTask.setDuReply(duReplyOrder);
//        duHistoryTask.setREPLY_TIME(utc);//处理时间
//        duHistoryTask.setUPLOAD_FLAG(Constant.NO_UPLOAD);//上传标志
//        if (!TextUtil.isNullOrEmpty(mServerTaskId)) {
//            duHistoryTask.setServerTaskId(mServerTaskId);
//        }

//        mHandleOrderPresenter.saveHistoryTask(duHistoryTask);
    }

    /**
     * 显示是否销单完成的弹框(保存)
     *
     * @param resolveType
     * @param resolveContent
     * @param issueReason
     * @param resolveMethod
     * @param resolveResult
     * @param resolveComment
     */
    private void showIsFinishFromSave(final String resolveType, final String resolveContent, final String issueReason,
                                      final String resolveMethod, final String resolveResult, final String resolveComment, final long resolveTime) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.text_request)
                .setMessage(R.string.text_is_sure_finish_task)
                .setNegativeButton(R.string.text_false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveHandleInfo(resolveType, resolveContent, issueReason, resolveMethod,
                                resolveResult, resolveComment, false, resolveTime);

                    }
                })
                .setPositiveButton(R.string.text_true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveHandleInfo(resolveType, resolveContent, issueReason, resolveMethod,
                                resolveResult, resolveComment, true, resolveTime);
                    }
                })
                .create();
        dialog.show();
    }

    /**
     * 显示是否销单完成的弹框(来自sync)
     *
     * @param resolveType
     * @param resolveContent
     * @param issueReason
     * @param resolveMethod
     * @param resolveResult
     * @param resolveComment
     */
    private void showIsFinishFromSync(final DUHistoryTask duHistory, final long utc, final String resolveType, final String resolveContent, final String issueReason,
                                      final String resolveMethod, final String resolveResult, final String resolveComment, final long resolveTime) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.text_request)
                .setMessage(R.string.text_is_sure_finish_task)
                .setNegativeButton(R.string.text_false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        syncHandleInfo(duHistory, utc, resolveType, resolveContent, issueReason,
                                resolveMethod, resolveResult, resolveComment, false, resolveTime);
                    }
                })
                .setPositiveButton(R.string.text_true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        syncHandleInfo(duHistory, utc, resolveType, resolveContent, issueReason,
                                resolveMethod, resolveResult, resolveComment, true, resolveTime);
                    }
                })
                .create();
        dialog.show();
    }


    /**
     * 保存处理信息，特殊类别，例：表务问题
     *
     * @param resolveType
     * @param resolveContent
     * @param issueReason
     * @param resolveMethod
     * @param resolveResult
     * @param resolveComment
     * @param isFinish
     */
    private void saveHandleInfo(String resolveType, String resolveContent, String issueReason,
                                String resolveMethod, String resolveResult, String resolveComment,
                                boolean isFinish, long resolveTime) {
        long utc = System.currentTimeMillis();
        DUHistoryTask duHistoryTask = new DUHistoryTask();
        duHistoryTask.setUSER_ID(mHandleOrderPresenter.getUserId());//userid
        duHistoryTask.setTASK_ID(mTaskId);//任务编号
        if (mOrigin == Constant.ORIGIN_MY_TASK || mOrigin == Constant.ORIGIN_MY_TASK_HISTORY) {
            duHistoryTask.setTASK_TYPE(Constant.TASK_TYPE_DOWNLOAD);//任务类型
        } else {
            duHistoryTask.setTASK_TYPE(Constant.TASK_TYPE_CREATE_SELF_ORDER);//任务类型
        }
        duHistoryTask.setTASK_STATE(TaskState.HANDLE);//任务状态
        DUReply duReplyOrder = new DUReply();
        duReplyOrder.setTaskId(duHistoryTask.getTASK_ID());
        duReplyOrder.setTaskState(TaskState.HANDLE + "");
        duReplyOrder.setReplyTime(utc);
        duReplyOrder.setResolveType(resolveType);//处理类别
        duReplyOrder.setResolveContent(resolveContent);//处理内容
        duReplyOrder.setIssueReason(issueReason);//发生原因
        duReplyOrder.setResolveMethod(resolveMethod);//解决措施
        duReplyOrder.setResolveResult(resolveResult);//处理结果
        duReplyOrder.setResolveComment(resolveComment);//处理备注
        duReplyOrder.setResolveTime(resolveTime);
        duReplyOrder.setFinishTask(isFinish);
        try {
            Gson gson = new Gson();
            if (mOrderDetailsFragment.getmTask() != null) {
                JSONObject objectTask = new JSONObject(gson.toJson(mOrderDetailsFragment.getmTask()));
                duHistoryTask.setTASK_CONTENT(objectTask.toString());
            }
            JSONObject objectReply = new JSONObject(gson.toJson(duReplyOrder));
            duHistoryTask.setTASK_REPLY(objectReply.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        duHistoryTask.setDuReply(duReplyOrder);
        duHistoryTask.setREPLY_TIME(utc);//处理时间
        duHistoryTask.setUPLOAD_FLAG(Constant.NO_UPLOAD);//上传标志
        if (!TextUtil.isNullOrEmpty(mServerTaskId)) {
            duHistoryTask.setServerTaskId(mServerTaskId);
        }
        mHandleOrderPresenter.saveHistoryTask(duHistoryTask);
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
     *
     * @param savedInstanceState
     */
    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            initMutimediaFragment();
            initHandleOrderFragment();
            initOrderDetailFragment();
        }
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
        mOrderDetailsFragment = (OrderDetailsFragment) getSupportFragmentManager().
                findFragmentByTag(OrderDetailsFragment.class.getName());
        mMultimediaFragment = (MultimediaFragment) getSupportFragmentManager().
                findFragmentByTag(MultimediaFragment.class.getName());
        mHandleOrderFragment = (HandleOrderFragment) getSupportFragmentManager().
                findFragmentByTag(HandleOrderFragment.class.getName());
        //历史-处理
        mHistoryHandleOrderFragment = (HistoryHandleOrderFragment) getSupportFragmentManager().
                findFragmentByTag(HistoryHandleOrderFragment.class.getName());
//        mOrderProcessFragment = (OrderProcessFragment) mFragmentManager.findFragmentByTag(OrderProcessFragment.class.getName());
        if (saveName.equals(OrderDetailsFragment.class.getName())) {
            if (mMultimediaFragment != null) {
                fts.hide(mMultimediaFragment);
            }
            if (mHandleOrderFragment != null) {
                fts.hide(mHandleOrderFragment);
            }
            if (mHistoryHandleOrderFragment != null) {
                fts.hide(mHistoryHandleOrderFragment);
            }
//            fts.show(mOrderDetailsFragment).commit();
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
        } else if (saveName.equals(HandleOrderFragment.class.getName())) {
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
        } else if (saveName.equals(HistoryHandleOrderFragment.class.getName())) {
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
//            fts.replace(R.id.delay_order_container, mCurrentFragment);
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
    protected void onDestroy() {
        LogUtils.e(TAG, "onDestroy--------------------");
        mUnbinder.unbind();
        mHandleOrderPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.delay_order_details_rb://详细
                initOrderDetailFragment();
                break;

            case R.id.delay_order_handle_rb://处理
                //来源 我的工单记录&&已上传 || 自开单记录&&已上传 intent to HistoryHandleOrderFragment
                initHandleOrderFragment();
                break;

            case R.id.delay_order_multimedia_rb://多媒体
                initMutimediaFragment();
                break;
            case R.id.delay_order_process_rb:
//                if (mOrderProcessFragment == null) {
//                    mOrderProcessFragment = new OrderProcessFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putInt(Constant.ORIGIN, mOrigin);
//                    if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
//                        bundle.putParcelable(Constant.INTENT_PARAM_ORDER, mDUOrder);
//                        bundle.putParcelableArrayList(Constant.INTENT_PARAM_ORDER_PROCESS_LIST, mDUProcesses);
//                    } else {
//                        bundle.putString(Constant.TASK_ID, mTaskId);
//                    }
//                    mOrderProcessFragment.setArguments(bundle);
//                }
//                changeFragment(mOrderProcessFragment);
                break;
        }
    }

    private void initOrderDetailFragment() {
//        try {
//            mOrderDetailsFragment = (OrderDetailsFragment) getSupportFragmentManager().
//                    findFragmentByTag(OrderDetailsFragment.class.getName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (mOrderDetailsFragment == null) {
            mOrderDetailsFragment = new OrderDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constant.DUMyTask, mDuMyTask);
            if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
                bundle.putInt(Constant.ORIGIN, mOrigin);
                bundle.putParcelable(Constant.INTENT_PARAM_ORDER, mDUOrder);
            } else {
                bundle.putInt(Constant.TASKSTATE, mState);
                bundle.putInt(Constant.ORIGIN, mOrigin);
                bundle.putString(Constant.TASK_ID, mTaskId);
                bundle.putBoolean(Constant.DATA_IS_UPLOAD, mIsDataUpload);
            }
            mOrderDetailsFragment.setArguments(bundle);
        }
        changeFragment(mOrderDetailsFragment);
    }

    private void initHandleOrderFragment() {
//        if ((mOrigin == Constant.ORIGIN_MY_TASK_HISTORY && mIsDataUpload) ||
//                (mOrigin == Constant.ORIGIN_CREATE_SELF_ORDER_HISTORY && mIsDataUpload)) {
//            if (mHistoryHandleOrderFragment == null) {
//                mHistoryHandleOrderFragment = new HistoryHandleOrderFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString(Constant.TASK_REPLY, mTaskReply);
//                bundle.putString(Constant.ISSUE_TYPE, TextUtil.getString(mIssueType));
//                bundle.putString(Constant.ISSUE_CONTENT, TextUtil.getString(mIssueContent));
//                mHistoryHandleOrderFragment.setArguments(bundle);
//            }
//            changeFragment(mHistoryHandleOrderFragment);
//        }
        //来源 工单查询 intent to HistoryHandleOrderFragment
//        if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {
//            if (mHistoryHandleOrderFragment == null) {
//                mHistoryHandleOrderFragment = new HistoryHandleOrderFragment();
//                Bundle bundle = new Bundle();
//                bundle.putInt(Constant.ORIGIN, mOrigin);
//                for (DUProcess duProcess : mDUProcesses) {
//                    if (duProcess.getTaskState() == mState) {
//                        bundle.putParcelable(Constant.INTENT_PARAM_ORDER_PROCESS, duProcess);
//                    }
//                }
//                mHistoryHandleOrderFragment.setArguments(bundle);
//            }
//            changeFragment(mHistoryHandleOrderFragment);
//        }
        //来源 我的工单记录&&未上传 || 自开单记录&&未上传，intent to HandleOrderFragment
        //来源 我的工单 || 自开单&&未上传，intent to HandleOrderFragment
        if (mOrigin == Constant.ORIGIN_MY_TASK || mOrigin == Constant.ORIGIN_CREATE_SELF_ORDER ||
                (mOrigin == Constant.ORIGIN_MY_TASK_HISTORY) ||
                (mOrigin == Constant.ORIGIN_CREATE_SELF_ORDER_HISTORY && !mIsDataUpload)) {//我的工单
//            try {
//                mHandleOrderFragment = (HandleOrderFragment) getSupportFragmentManager().
//                        findFragmentByTag(HandleOrderFragment.class.getName());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            if (mHandleOrderFragment == null) {
                mHandleOrderFragment = new HandleOrderFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.TASKSTATE, mState);
                bundle.putString(Constant.TASK_ID, mTaskId);
                bundle.putBoolean(Constant.DATA_IS_UPLOAD, mIsDataUpload);
                bundle.putString(Constant.TASK_REPLY, mTaskReply);
                bundle.putString(Constant.ISSUE_TYPE, TextUtil.getString(mIssueType));
                bundle.putString(Constant.ISSUE_CONTENT, mDuMyTask.getFynr());
                bundle.putString(Constant.ISSUE_LEIXING, mDuMyTask.getFaTypeCd());
                bundle.putBoolean(Constant.TYPE_IS_HISTORY, mIsFormHistory);
                mHandleOrderFragment.setArguments(bundle);
            }
            changeFragment(mHandleOrderFragment);
        }
    }

    private void initMutimediaFragment() {
//        try {
//            mMultimediaFragment = (MultimediaFragment) getSupportFragmentManager().
//                    findFragmentByTag(MultimediaFragment.class.getName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (mMultimediaFragment == null) {
            mMultimediaFragment = new MultimediaFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constant.DUMyTask, mDuMyTask);
            if (mOrigin == Constant.ORIGIN_TASK_QUERY_RESULT) {//来源 工单查询
                bundle.putInt(Constant.ORIGIN, mOrigin);
                for (DUProcess duProcess : mDUProcesses) {
                    if (duProcess.getTaskState() == mState) {
                        bundle.putParcelableArrayList(Constant.INTENT_PARAM_ORDER_PROCES_FILE, (ArrayList<? extends Parcelable>) duProcess.getFiles());
                    }
                }
            } else {
                bundle.putBoolean(Constant.DATA_IS_UPLOAD, mIsDataUpload);
                bundle.putString(Constant.TASK_ID, mTaskId);
                if (mOrigin == Constant.ORIGIN_MY_TASK || mOrigin == Constant.ORIGIN_MY_TASK_HISTORY) {
                    bundle.putInt(Constant.TASK_TYPE, Constant.TASK_TYPE_DOWNLOAD);
                } else {
                    bundle.putInt(Constant.TASK_TYPE, Constant.TASK_TYPE_CREATE_SELF_ORDER);
                }
                bundle.putInt(Constant.TASK_STATE, mState);
            }
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

    /**
     * 设置布局显示状态
     *
     * @param isPictureDetail 是否是幻灯片
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
            LogUtils.e(TAG, "param is error");
            return;
        }
        ApplicationsUtil.showMessage(this, R.string.toast_data_has_saved);
        if (duHistoryTask.getTASK_TYPE() == Constant.TASK_TYPE_DOWNLOAD_ORDER) {
            if (NetworkUtil.isNetworkConnected(this)) {
//                uploadReply(duHistoryTask.getTASK_ID(), duHistoryTask.getTASK_TYPE(),
//                        duHistoryTask.getTASK_STATE(), duHistoryTask.getREPLY_TIME());
                uploadReplies(duHistoryTask.getTASK_ID(), Constant.TASK_TYPE_DOWNLOAD_ORDER);
            }
        } else {
            mHandleOrderPresenter.getServerTaskId(duHistoryTask.getTASK_ID());
        }
        finish();
        mEventPosterHelper.postEventSafely(new UIBusEvent.NotifyMyTasksUI());
    }

    @Override
    public void onSaveHistoryTaskSuccess(String msg) {
//        if (NetworkUtils.isAvailableByPing()) {
        commitInfo();
//        } else {
//            ToastUtils.showShort(msg);
//            finish();
//        }
    }

    @Override
    public void onCommitTaskSuccess(String msg) {
        ToastUtils.showLong(msg);
        finish();
    }

    @Override
    public void onCommitError(String error) {
//        ToastUtils.showLong("工单上传失败：" + error + "\n当前工单已保存到已处理工单，请在已处理工单中查看再次上传");
//        finish();
        new AlertDialog.Builder(this)
                .setTitle("上传失败")
                .setMessage("工单上传失败：" + error + "。当前工单已保存到已处理工单，请在已处理工单中查看并再次上传")
                .setCancelable(false)
                .setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setPositiveButton("去查看", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(HandleOrderActivity.this, HistoryOrdersActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).create().show();
    }

    @Override
    public void showMessage(String message) {
        ApplicationsUtil.showMessage(this, message);
    }

    @Override
    public void onUpdateHistoryTask(Boolean aBoolean, DUHistoryTask duHistoryTask) {
        if ((!aBoolean)
                || (duHistoryTask == null)) {
            LogUtils.e(TAG, "param is error");
            return;
        }
        ApplicationsUtil.showMessage(this, R.string.toast_data_has_updated);
        if (NetworkUtil.isNetworkConnected(this)) {
            uploadReplies(duHistoryTask.getTASK_ID(), Constant.TASK_TYPE_DOWNLOAD_ORDER);
        }
        finish();
        mEventPosterHelper.postEventSafely(new UIBusEvent.NotifyHistoryTasksUI());
    }

    @Override
    public void uploadTaskReplyCreateSelf(String taskId) {
        if (NetworkUtil.isNetworkConnected(this)) {
            uploadReplies(taskId, Constant.TASK_TYPE_CREATE_SELF_ORDER);
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
}
