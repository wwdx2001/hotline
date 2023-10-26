package com.sh3h.hotline.ui.order.myorder.history;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.maning.mndialoglibrary.MProgressDialog;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.DUMyTaskDao;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.MyOrderListAdapter;
import com.sh3h.hotline.service.SyncConst;
import com.sh3h.hotline.service.UploadDataService;
import com.sh3h.hotline.ui.base.BaseActivity;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.sh3h.hotline.ui.bill.detail.UserDetailInformationActivity;
import com.sh3h.hotline.ui.order.TaskState;
import com.sh3h.hotline.ui.order.myorder.delayorback.DelayOrBackOrderActivity;
import com.sh3h.hotline.ui.order.myorder.handle.HandleOrderActivity;
import com.sh3h.hotline.view.MyLinearLayoutManager;
import com.sh3h.ipc.module.MyModule;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.squareup.otto.Bus;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

import static com.sh3h.hotline.util.ConstDataUtil.PAGE_SIZE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryUploadOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryUploadOrdersFragment extends ParentFragment implements HistoryUploadOrdersMvpView, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener {

    private static final String TAG = "HistorySaveOrdersFragment";
    public final static int ORIGIN = Constant.ORIGIN_MY_TASK_HISTORY;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Unbinder mUnbinder;
    private List<DUMyTask> mHistoryTaskList;
    private Disposable mDisposable1;
    private MyOrderListAdapter mAdapter;//适配器
    private Map<String, String> map = new HashMap<>();

    @Inject
    Bus mEventBus;

    @Inject
    ConfigHelper mConfigHelper;

    @Inject
    HistoryUploadOrdersPresenter mUploadOrdersPresenter;

    @BindView(R.id.history_order_upload_container)
    CoordinatorLayout mShackContainer;

    @BindView(R.id.history_order_upload_fab)
    FloatingActionButton mFab;

    @BindView(R.id.history_upload_swiperefresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.history_order_upload_recyclerView)
    RecyclerView mRecyclerView;

    public HistoryUploadOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryUploadOrdersFragment.
     */
    public static HistoryUploadOrdersFragment newInstance(String param1, String param2) {
        HistoryUploadOrdersFragment fragment = new HistoryUploadOrdersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_upload_orders, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mEventBus.register(this);
        mUploadOrdersPresenter.attachView(this);
        initView();
        initData();
        return view;
    }

    private void initView() {
        initRecyclerView();
    }

    /*
     * 初始化控件
     */
    private void initRecyclerView() {
        //刷新控件
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                loadMyHistoryTasks(true, 0, PAGE_SIZE, true);
            }
        });

        //置顶按钮
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.smoothScrollToPosition(0);//滑到顶部
            }
        });
        mHistoryTaskList = new ArrayList<>();
        mAdapter = new MyOrderListAdapter(map, Constant.ADAPTER_TYPE_HISTORY,
                R.layout.item_my_order_list_row_layout, mHistoryTaskList);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setLayoutManager(new MyLinearLayoutManager(ActivityUtils.getTopActivity()));
        mRecyclerView.setAdapter(mAdapter);
        initRecyclerViewState();
    }

    private void initData() {
        loadMyHistoryTasks(true, 0, PAGE_SIZE, false);
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.e(TAG, "onDestroyView------------------");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.e(TAG, "onAttach------------------");
    }


    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.e(TAG, "onDetach------------------");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e(TAG, "onDestroy------------------");
        mUnbinder.unbind();
        mEventBus.unregister(this);
        mUploadOrdersPresenter.detachView();
        EasyHttp.cancelSubscription(mDisposable1);
    }

    @Override
    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
        switch (view.getId()) {
            case R.id.my_order_list_item_cardview:
                Intent intent;
                if (mAdapter.getData().get(position).getState() == TaskState.DELAY ||
                        mAdapter.getData().get(position).getState() == TaskState.BACK) {
                    intent = new Intent(ActivityUtils.getTopActivity(), DelayOrBackOrderActivity.class);
                    intent.putExtra(Constant.ORIGIN, ORIGIN);
                    intent.putExtra(Constant.DUMyTask, mAdapter.getData().get(position));
                    intent.putExtra(Constant.DATA_IS_UPLOAD, mAdapter.getData().get(position).getIsUploadSuccess());
                    intent.putExtra(Constant.TASKSTATE, mAdapter.getData().get(position).getState());
                    intent.putExtra(Constant.TASK_ID, mAdapter.getData().get(position).getFaId());
                    intent.putExtra(DUHistoryTask.DUHistoryTask_ID, mAdapter.getData().get(position).getID());
                    startActivity(intent);
                } else if (mAdapter.getData().get(position).getState() == TaskState.HANDLE ||
                        mAdapter.getData().get(position).getState() == TaskState.FINISH) {
                    intent = new Intent(ActivityUtils.getTopActivity(), HandleOrderActivity.class);
                    intent.putExtra(Constant.ORIGIN, ORIGIN);
                    intent.putExtra(Constant.DUMyTask, mAdapter.getData().get(position));
                    intent.putExtra(Constant.DATA_IS_UPLOAD, mAdapter.getData().get(position).getIsUploadSuccess());
                    intent.putExtra(Constant.TASK_ID, mAdapter.getData().get(position).getFaId());
                    intent.putExtra(DUHistoryTask.DUHistoryTask_ID, mAdapter.getData().get(position).getID());
                    intent.putExtra(Constant.TYPE_IS_HISTORY, true);
                    startActivity(intent);
                }
                break;
        }

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
        switch (view.getId()) {
            case R.id.ib_phone://跳转到拨号页面
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +
                        mAdapter.getData().get(position).getContactValue()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.tv0:
            case R.id.tv_acctId:
            case R.id.iv_icon:
            case R.id.view_navigation:
                navigationUserInfo(position);
                break;
        }
    }

    /**
     * 查询客户详细信息
     *
     * @param i
     */
    private void navigationUserInfo(int i) {
        mDisposable1 = EasyHttp.post(URL.CustomerInfoQuery)
                .params("acctId", mAdapter.getData().get(i).getAcctId())
                .execute(new CallBackProxy<CustomApiResult<CustomerInfoFindResult>,
                        CustomerInfoFindResult>(new CustomCallBack<CustomerInfoFindResult>() {

                    @Override
                    public void onStart() {
                        MProgressDialog.showProgress(ActivityUtils.getTopActivity(), "加载中...");
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                    }

                    @Override
                    public void onSuccess(CustomerInfoFindResult entity) {
                        if (entity != null) {
                            LogUtils.i(entity.toString());
                            Intent intent = new Intent(ActivityUtils.getTopActivity(), UserDetailInformationActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(Constant.BILLBASEINFO, entity);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            ToastUtils.showShort("没有数据");
                        }
                    }

                    @Override
                    public void onCompleted() {
                        MProgressDialog.dismissProgress();
                    }
                }) {
                });
    }

    public boolean onQueryTextSubmit(final String query) {
        if (TextUtils.isEmpty(query)) {
            return false;
        }
        GreenDaoUtils.getAsyncSession(ActivityUtils.getTopActivity())
                .runInTx(new Runnable() {
                    @Override
                    public void run() {
                        List<DUMyTask> queryList = GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                .getDUMyTaskDao().queryBuilder()
                                .where(DUMyTaskDao.Properties.TaskState.eq(Constant.ORIGIN_MY_TASK_HISTORY))
                                .where(DUMyTaskDao.Properties.IsUploadSuccess.eq(Constant.HAS_UPLOADED))
                                .whereOr(DUMyTaskDao.Properties.FaId.like("%" + query + "%"),
                                        DUMyTaskDao.Properties.ContactValue.like("%" + query + "%"),
                                        DUMyTaskDao.Properties.AcctId.like("%" + query + "%"),
                                        DUMyTaskDao.Properties.Fsdz.like("%" + query + "%"),
                                        DUMyTaskDao.Properties.EntityName.like("%" + query + "%"),
                                        DUMyTaskDao.Properties.Fyly.like("%" + query + "%"),
                                        DUMyTaskDao.Properties.FaTypeCd.like("%" + query + "%"),
                                        DUMyTaskDao.Properties.Fynr.like("%" + query + "%"),
                                        DUMyTaskDao.Properties.Clsx.like("%" + query + "%"),
                                        DUMyTaskDao.Properties.CreDttm.like("%" + query + "%"))
                                .list();
                        mHistoryTaskList.clear();
                        mHistoryTaskList.addAll(queryList);
                        mAdapter.notifyDataSetChanged();
                    }
                });
        return false;
    }

    /**
     * 初始化RecyclerView状态
     */
    private void initRecyclerViewState() {
        final MyLinearLayoutManager manager = (MyLinearLayoutManager) mRecyclerView.getLayoutManager();
        //recyclerview添加滚动监听
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int firstItemPosition = manager.findFirstVisibleItemPosition();
                if (firstItemPosition > 10 && newState == 0) {
                    mFab.setVisibility(View.VISIBLE);
                } else {
                    mFab.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
        if (mAdapter != null) {
            mAdapter.setMap(map);
        }
    }

    public void loadMyHistoryTasks(boolean isFirstTime, int offset, int size, boolean isProgress) {
        if (isProgress) {
            showTopProgressbar(mSwipeRefreshLayout);
        }
        if(mUploadOrdersPresenter != null) {
            mUploadOrdersPresenter.loadMyHistoryTasks(isFirstTime, offset, size);
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void optionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_all_tasks://所有工单
                if(mUploadOrdersPresenter != null) {
                    mRecyclerView.smoothScrollToPosition(0);//滑到顶部
                    showTopProgressbar(mSwipeRefreshLayout);
                    mUploadOrdersPresenter.loadMyHistoryTasks(true, 0, PAGE_SIZE);
                }
                break;
            case R.id.action_upload_tasks://未上传工单
                if(mUploadOrdersPresenter != null) {
                    mRecyclerView.smoothScrollToPosition(0);//滑到顶部
                    mUploadOrdersPresenter.getUploadTasks(System.currentTimeMillis(), false);
                }
                break;
            case R.id.action_deadline_tasks://临期工单
                if(mUploadOrdersPresenter != null) {
                    mRecyclerView.smoothScrollToPosition(0);//滑到顶部
                    mUploadOrdersPresenter.getDeadlineTasks(System.currentTimeMillis(), false);
                }
                break;
            case R.id.action_expired_task://超期工单
                if(mUploadOrdersPresenter != null) {
                    mRecyclerView.smoothScrollToPosition(0);//滑到顶部
                    mUploadOrdersPresenter.getExpiredTasks(System.currentTimeMillis());
                }
                break;
            case R.id.action_noexpired_task://未超期工单
                if(mUploadOrdersPresenter != null) {
                    mRecyclerView.smoothScrollToPosition(0);//滑到顶部
                    mUploadOrdersPresenter.getNoExpiredTasks(System.currentTimeMillis());
                }
                break;
        }
    }

    public void getFilterTask(String content, String parms) {
        mRecyclerView.smoothScrollToPosition(0);//滑到顶部
        mUploadOrdersPresenter.getFilterTask(content, parms);
    }

    @Override
    public void showMessage(String message) {
        hideTopProgressbar(mSwipeRefreshLayout);
        ApplicationsUtil.showMessage(ActivityUtils.getTopActivity(), message);
    }

    @Override
    public void onGetHistoryOrdersList(boolean isFirstTime, List<DUMyTask> result) {
        LogUtil.i(TAG, "onGetHistoryOrdersList");
        hideTopProgressbar(mSwipeRefreshLayout);

        if ((!isFirstTime) && (mHistoryTaskList.size() > 0)) {
            if (mHistoryTaskList.get(mHistoryTaskList.size() - 1) == null) {
                mHistoryTaskList.remove(mHistoryTaskList.size() - 1);
                mAdapter.notifyItemRemoved(mHistoryTaskList.size());// 这三行代码是去除最后一条loading的布局
            }
        }

        if ((result == null) || (result.size() <= 0)) {
            LogUtil.i(TAG, "onGetHistoryOrdersList: historyOrdersList has no item");
            return;
        }

        if (isFirstTime) {
            mHistoryTaskList.clear();
        }
        mHistoryTaskList.addAll(result);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFilterOrdersList(boolean isFirstTime, List<DUMyTask> result) {
        LogUtil.i(TAG, "onFilterOrdersList");
        hideTopProgressbar(mSwipeRefreshLayout);

        if ((!isFirstTime) && (mHistoryTaskList.size() > 0)) {
            if (mHistoryTaskList.get(mHistoryTaskList.size() - 1) == null) {
                mHistoryTaskList.remove(mHistoryTaskList.size() - 1);
                mAdapter.notifyItemRemoved(mHistoryTaskList.size());// 这三行代码是去除最后一条loading的布局
            }
        }
        if (isFirstTime) {
            mHistoryTaskList.clear();
        }
        mHistoryTaskList.addAll(result);
        mAdapter.notifyDataSetChanged();
    }
}