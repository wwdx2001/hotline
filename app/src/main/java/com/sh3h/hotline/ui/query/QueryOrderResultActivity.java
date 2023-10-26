package com.sh3h.hotline.ui.query;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.response.DUOrder;
import com.sh3h.dataprovider.data.entity.response.DUProcess;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.NetworkUtil;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.order.TaskState;
import com.sh3h.hotline.ui.order.myorder.delayorback.DelayOrBackOrderActivity;
import com.sh3h.hotline.ui.order.myorder.handle.HandleOrderActivity;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.TextUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 工单查询结果
 * Created by zhangjing on 2016/9/20.
 */
public class QueryOrderResultActivity extends ParentActivity implements QueryOrderResultMvpView,
        SearchView.OnQueryTextListener {

    private Unbinder mUnbinder;

    @BindView(R.id.query_order_result_recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.query_order_result_fab)
    FloatingActionButton mFab;

    @BindView(R.id.query_order_result_swiperefresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    private MyAdapter mAdatper;
    private List<DUOrder> mDUOrderList;
    private Intent intent;

    @Inject
    QueryOrderResultPresenter mQueryResultPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_order_result);
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);
        initToolBar(R.string.label_query_orders_result);
        mQueryResultPresenter.attachView(this);
        initView();
        if (!NetworkUtil.isNetworkConnected(this)) {
            ApplicationsUtil.showMessage(this, R.string.toast_network_is_not_connect);
            return;
        }

        if (getIntent() == null) {
            return;
        }
        intent = getIntent();
        mRefreshLayout.setRefreshing(true);
        mRefreshLayout.setProgressViewOffset(false, 0, 100);
        String taskId = intent.getStringExtra(Constant.TASK_ID);
        String name = intent.getStringExtra(Constant.NAME);
        String address = intent.getStringExtra(Constant.ADDRESS);
        String telephone = intent.getStringExtra(Constant.TELEPHONE);
        String issueOrigin = intent.getStringExtra(Constant.ISSUEORIGIN);
        String issueType = intent.getStringExtra(Constant.ISSUE_TYPE);//类别
        String issueContent = intent.getStringExtra(Constant.ISSUE_CONTENT);//内容
        String issueArea = intent.getStringExtra(Constant.ISSUE_AREA);//区名
        long startUtc = intent.getLongExtra(Constant.START_DATE, 0);
        long endUtc = intent.getLongExtra(Constant.END_DATE, 0);
        String loginSite = intent.getStringExtra(Constant.LOGIN_SITE);
        String acceptSite = intent.getStringExtra(Constant.ACCEPT_SITE);
        if (startUtc == endUtc && startUtc != 0) {
            endUtc = startUtc + 86399000L;//如果选择的日期相同，就在此基础上加23时59分59秒
        }
        mQueryResultPresenter.queryOrder(taskId, name, address, telephone, issueOrigin,
                issueType, issueContent, loginSite, acceptSite, issueArea, startUtc, endUtc);
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mDUOrderList = new ArrayList<DUOrder>();
        mAdatper = new MyAdapter(mDUOrderList, this);
        mRecyclerView.setAdapter(mAdatper);
        //置顶按钮
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.smoothScrollToPosition(0);//滑到顶部
            }
        });

        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);
        //一进界面就刷新
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                long startUtc = intent.getLongExtra(Constant.START_DATE, 0);
                long endUtc = intent.getLongExtra(Constant.END_DATE, 0);
                if (startUtc == endUtc && startUtc != 0) {
                    endUtc = startUtc + 86399000L;//如果选择的日期相同，就在此基础上加23时59分59秒
                }
                mQueryResultPresenter.queryOrder(
                        intent.getStringExtra(Constant.TASK_ID),
                        intent.getStringExtra(Constant.NAME),
                        intent.getStringExtra(Constant.ADDRESS),
                        intent.getStringExtra(Constant.TELEPHONE),
                        intent.getStringExtra(Constant.ISSUEORIGIN),
                        intent.getStringExtra(Constant.ISSUE_TYPE),
                        intent.getStringExtra(Constant.ISSUE_CONTENT),
                        intent.getStringExtra(Constant.LOGIN_SITE),
                        intent.getStringExtra(Constant.ACCEPT_SITE),
                        intent.getStringExtra(Constant.ISSUE_AREA),
                        startUtc,
                        endUtc);
            }
        });
        initRecyclerViewState();
    }

    /**
     * 初始化RecyclerView状态
     */
    private void initRecyclerViewState() {
        final LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
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
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_order_list, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        menu.findItem(R.id.action_all_tasks).setVisible(false);
        menu.findItem(R.id.action_deadline_tasks).setVisible(false);
        menu.findItem(R.id.action_address_sort).setVisible(false);
        menu.findItem(R.id.action_receive_time_asc).setVisible(false);
        menu.findItem(R.id.action_receive_time_desc).setVisible(false);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mQueryResultPresenter.detachView();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        List<DUOrder> results = new ArrayList();

        for (DUOrder duOrder : mDUOrderList) {
            if (duOrder.getTaskId().contains(query)
                    || duOrder.getCardId().contains(query)
                    || duOrder.getIssueName().contains(query)
                    || duOrder.getIssueAddress().contains(query)
                    || duOrder.getMoblie().contains(query)
                    || duOrder.getTelephone().contains(query)) {
                results.add(duOrder);
            }
        }

        mAdatper.setmList(results);
        mAdatper.notifyDataSetChanged();
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onGetDUOrder(List<DUOrder> result) {
        mDUOrderList.clear();
        mDUOrderList.addAll(result);
        mAdatper.setmList(mDUOrderList);
        mAdatper.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMessage(String message) {
        mRefreshLayout.setRefreshing(false);
        ApplicationsUtil.showMessage(this, message);
    }

    @Override
    public void onLoadOrderProcessError(String message) {
        ApplicationsUtil.showMessage(this, message);
    }

    @Override
    public void onLoadOrderProcessSuccess(DUOrder order, List<DUProcess> duProcesses) {
        if (duProcesses == null || duProcesses.size() == 0) {
            return;
        }
        //显示工单最后一次操作状态
        DUProcess duProcess = duProcesses.get(0);
        if (duProcess.getTaskState() == TaskState.RECEIVED) {
            ApplicationsUtil.showMessage(this, R.string.toast_current_received);
        }
        if (duProcess.getTaskState() == TaskState.DELAY || duProcess.getTaskState() == TaskState.BACK) {//延期、退单
            intent = new Intent(this, DelayOrBackOrderActivity.class);
            intent.putExtra(Constant.ORIGIN, Constant.ORIGIN_TASK_QUERY_RESULT);
            intent.putExtra(Constant.TASKSTATE, duProcess.getTaskState());
            intent.putExtra(Constant.INTENT_PARAM_ORDER, order);
            intent.putParcelableArrayListExtra(Constant.INTENT_PARAM_ORDER_PROCESS_LIST, (ArrayList<? extends Parcelable>) duProcesses);
            startActivity(intent);
        } else if (duProcess.getTaskState() == TaskState.HANDLE) {//处理
            intent = new Intent(this, HandleOrderActivity.class);
            intent.putExtra(Constant.ORIGIN, Constant.ORIGIN_TASK_QUERY_RESULT);
            intent.putExtra(Constant.INTENT_PARAM_ORDER, order);
            intent.putParcelableArrayListExtra(Constant.INTENT_PARAM_ORDER_PROCESS_LIST, (ArrayList<? extends Parcelable>) duProcesses);
            startActivity(intent);
        }
    }

    /**
     * recyclerView 适配器
     */
    private class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<DUOrder> mList;
        private Context mContext;

        public MyAdapter(List<DUOrder> mList, Context mContext) {
            this.mList = mList;
            this.mContext = mContext;
        }

        public void setmList(List<DUOrder> mList) {
            this.mList = mList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_search_history_result, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            DUOrder order = mList.get(position);
            viewHolder.taskId.setText(order.getTaskId());
            viewHolder.cardId.setText(order.getCardId());
            viewHolder.phone.setText(order.getTelephone());
            viewHolder.address.setText(order.getIssueAddress());
            viewHolder.issuer.setText(order.getIssuer());
            viewHolder.issueType.setText(order.getIssueType());
            if (!TextUtil.isNullOrEmpty(order.getIssueOrigin())) {
                viewHolder.issueOrigin.setText(order.getIssueOrigin());
            }
            viewHolder.issueContent.setText(order.getIssueContent());
            SimpleDateFormat format = new SimpleDateFormat(TextUtil.FORMAT_DATE_NO_SECOND);
            viewHolder.replyLimit.setText(format.format(order.getReplyDeadline()));
            viewHolder.dispatchTime.setText(format.format(order.getDispatchTime()));

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mQueryResultPresenter.getOrderProcessInfo(mList.get(position));

                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        /**
         * 普通-显示数据item
         */
        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView taskId;
            public TextView cardId;
            public TextView phone;
            public TextView address;
            public TextView issuer;
            public TextView issueType;//反映类型
            public TextView issueOrigin;//反映来源
            public TextView issueContent;//反映内容
            public TextView replyLimit;//处理时限
            public TextView dispatchTime;//派单时间

            public MyViewHolder(View view) {
                super(view);
                taskId = (TextView) view.findViewById(R.id.search_history_result_item_tv_order_number);
                cardId = (TextView) view.findViewById(R.id.search_history_result_item_tv_xiaogen_number);
                phone = (TextView) view.findViewById(R.id.search_history_result_item_tv_phone);
                address = (TextView) view.findViewById(R.id.search_history_result_item_tv_address);
                issuer = (TextView) view.findViewById(R.id.search_history_result_item_tv_reflect_person);
                issueType = (TextView) view.findViewById(R.id.search_history_result_item_tv_reflect_type);
                issueOrigin = (TextView) view.findViewById(R.id.search_history_result_item_tv_reflect_origin);
                issueContent = (TextView) view.findViewById(R.id.search_history_result_item_tv_reflect_content);
                replyLimit = (TextView) view.findViewById(R.id.search_history_result_item_handle_tv_time_limit);
                dispatchTime = (TextView) view.findViewById(R.id.search_history_result_item_tv_dispatch_time);
            }
        }

    }
}
