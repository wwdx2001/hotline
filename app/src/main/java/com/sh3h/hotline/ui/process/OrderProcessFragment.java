package com.sh3h.hotline.ui.process;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.response.DUOrder;
import com.sh3h.dataprovider.data.entity.response.DUProcess;
import com.sh3h.dataprovider.data.local.preference.PreferencesHelper;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.OrderProcessRecyclerViewAdapter;
import com.sh3h.hotline.ui.base.BaseActivity;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.sh3h.hotline.ui.order.TaskState;
import com.sh3h.hotline.ui.order.myorder.delayorback.DelayOrBackOrderActivity;
import com.sh3h.hotline.ui.order.myorder.handle.HandleOrderActivity;
import com.sh3h.mobileutil.util.ApplicationsUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dengzhimin on 2017/2/23.
 */

public class OrderProcessFragment extends ParentFragment implements OrderProcessMvpView, OrderProcessRecyclerViewAdapter.OnItemClickListener {

    @Inject
    OrderProcessPresenter mPresenter;

    @Inject
    PreferencesHelper mPreferencesHelper;

    @BindView(R.id.rv_process)
    RecyclerView mRvProcess;

    private OrderProcessRecyclerViewAdapter mAdapter;

    private Unbinder mUnbinder;

    private String mTaskId;

    private int mUserId;

    private int mOrgin;

    //工单查询传值
    private DUOrder mDUOrder;
    private ArrayList<DUProcess> mDUProcesses;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        mPresenter.attachView(this);
        Bundle bundle = getArguments();
        if(bundle == null){
            throw new NullPointerException("bundle is null");
        }
        mOrgin = bundle.getInt(Constant.ORIGIN);
        if(mOrgin == Constant.ORIGIN_TASK_QUERY_RESULT){
            mDUOrder = bundle.getParcelable(Constant.INTENT_PARAM_ORDER);
            mDUProcesses = bundle.getParcelableArrayList(Constant.INTENT_PARAM_ORDER_PROCESS_LIST);
        } else {
            mTaskId = bundle.getString(Constant.TASK_ID);
        }
        if(mPreferencesHelper.getUserSession() != null){
            mUserId = mPreferencesHelper.getUserSession().getUserId();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_process, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRvProcess.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new OrderProcessRecyclerViewAdapter(getActivity(), new ArrayList<>());
        mAdapter.setOnItemClickListener(this);
        mRvProcess.setAdapter(mAdapter);
        if(mOrgin == Constant.ORIGIN_TASK_QUERY_RESULT){
            mAdapter.setData(mDUProcesses);
        }else{
            mPresenter.getHistoryTasks(mUserId, mTaskId);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mPresenter.detachView();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onError(String message) {
        ApplicationsUtil.showMessage(getActivity(), message);
    }

    @Override
    public void onSetHistoryTasks(List<DUHistoryTask> duHistoryTasks) {
        mAdapter.setData(duHistoryTasks);
    }

    @Override
    public void onItemClick(View v, Object o) {
        if(o instanceof DUHistoryTask){
            DUHistoryTask historyTask = (DUHistoryTask) o;
            if(getActivity() instanceof DelayOrBackOrderActivity && (historyTask.getTASK_STATE() == TaskState.BACK ||
                    historyTask.getTASK_STATE() == TaskState.DELAY)){
                ApplicationsUtil.showMessage(getActivity(), R.string.toast_task_state_info_showing);
                return;
            }
            if(getActivity() instanceof HandleOrderActivity && historyTask.getTASK_STATE() == TaskState.HANDLE){
                ApplicationsUtil.showMessage(getActivity(), R.string.toast_task_state_info_showing);
                return;
            }
            Intent intent = null;
            if(historyTask.getTASK_STATE()== TaskState.DELAY || historyTask.getTASK_STATE() == TaskState.BACK){//延期、退单
                intent = new Intent(getActivity(), DelayOrBackOrderActivity.class);
                intent.putExtra(Constant.ORIGIN, Constant.ORIGIN_MY_TASK_HISTORY);
                intent.putExtra(Constant.DATA_IS_UPLOAD, historyTask.getUPLOAD_FLAG());
                intent.putExtra(Constant.TASKSTATE, historyTask.getTASK_STATE());
                intent.putExtra(Constant.TASK_ID, historyTask.getTASK_ID());
                intent.putExtra(Constant.TASK_REPLY, historyTask.getTASK_REPLY());
                intent.putExtra(DUHistoryTask.DUHistoryTask_ID, historyTask.getID());
            }else if(historyTask.getTASK_STATE()== TaskState.HANDLE ){//处理
                intent = new Intent(getActivity(), HandleOrderActivity.class);
                intent.putExtra(Constant.ORIGIN, Constant.ORIGIN_MY_TASK_HISTORY);
                intent.putExtra(Constant.DATA_IS_UPLOAD, historyTask.getUPLOAD_FLAG());
                intent.putExtra(Constant.TASK_ID, historyTask.getTASK_ID());
                intent.putExtra(Constant.TASK_REPLY, historyTask.getTASK_REPLY());
                intent.putExtra(DUHistoryTask.DUHistoryTask_ID, historyTask.getID());
            }
            startActivity(intent);
        }
        if(o instanceof DUProcess){
            DUProcess duProcess = (DUProcess) o;
            if(getActivity() instanceof DelayOrBackOrderActivity && (duProcess.getTaskState() == TaskState.BACK ||
                    duProcess.getTaskState() == TaskState.DELAY)){
                ApplicationsUtil.showMessage(getActivity(), R.string.toast_task_state_info_showing);
                return;
            }
            if(getActivity() instanceof HandleOrderActivity && duProcess.getTaskState() == TaskState.HANDLE){
                ApplicationsUtil.showMessage(getActivity(), R.string.toast_task_state_info_showing);
                return;
            }
            Intent intent = null;
            if(duProcess.getTaskState()== TaskState.DELAY || duProcess.getTaskState() == TaskState.BACK){//延期、退单
                intent = new Intent(getActivity(), DelayOrBackOrderActivity.class);
                intent.putExtra(Constant.ORIGIN, mOrgin);
                intent.putExtra(Constant.TASKSTATE, duProcess.getTaskState());
                intent.putExtra(Constant.INTENT_PARAM_ORDER, mDUOrder);
                intent.putExtra(Constant.INTENT_PARAM_ORDER_PROCESS_LIST, mDUProcesses);
            }else if(duProcess.getTaskState()== TaskState.HANDLE ){//处理
                intent = new Intent(getActivity(), HandleOrderActivity.class);
                intent.putExtra(Constant.ORIGIN, mOrgin);
                intent.putExtra(Constant.INTENT_PARAM_ORDER, mDUOrder);
                intent.putExtra(Constant.INTENT_PARAM_ORDER_PROCESS_LIST, mDUProcesses);
            }
            startActivity(intent);
        }
    }
}
