package com.sh3h.hotline.ui.order.myorder.list;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.maning.mndialoglibrary.MProgressDialog;
import com.sh3h.dataprovider.BaseApplication;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.data.entity.response.FYLXBean;
import com.sh3h.dataprovider.data.entity.response.FYLYBean;
import com.sh3h.dataprovider.data.entity.response.WordInfoEntity;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.DUMyTaskDao;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.MyOrderListAdapter;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.bill.detail.UserDetailInformationActivity;
import com.sh3h.hotline.ui.order.myorder.handle.HandleOrderActivity;
import com.sh3h.hotline.ui.order.myorder.history.HistoryOrdersActivity;
import com.sh3h.hotline.ui.order.myorder.illegalwater.IllegalWaterActivity;
import com.sh3h.hotline.ui.order.myorder.questionwater.QuestionWaterActivity;
import com.sh3h.hotline.ui.order.myorder.remotewater.RemoteWaterActivity;
import com.sh3h.hotline.util.CommonUtils;
import com.sh3h.hotline.view.MyLinearLayoutManager;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;
import org.greenrobot.greendao.query.QueryBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * 我的工单
 * Created by zhangjing on 2016/9/12.
 */
public class MyOrderListActivity extends ParentActivity
        implements MyOrderListMvpView, SearchView.OnQueryTextListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.my_order_list_container)
    CoordinatorLayout mShackContainer;

    @BindView(R.id.my_order_list_fab)
    FloatingActionButton mFab;

    @BindView(R.id.synchronize_data_swiperefresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.my_order_list_recyclerView)
    RecyclerView mRecyclerView;

    @Inject
    MyOrderListPresenter mMyOrderListPresenter;

    @Inject
    Bus mEventBus;

    private final static String TAG = "MyOrderListActivity";
    private final static int ORIGIN = Constant.ORIGIN_MY_TASK;
    protected final static int ORDER_BY_ADDRESS = 1001;
    protected final static int ORDER_BY_RECEIVE_TIME_ASC = 1002;
    protected final static int ORDER_BY_RECEIVE_TIME_DESC = 1003;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private Unbinder mUnbinder;
    private MyOrderListAdapter mAdapter;//适配器
    private MenuItem searchItem;
    private MenuItem addressSortItem;

    private List<DUMyTask> mMyTaskList;
    private Disposable mDisposable1;
    private List<FYLYBean> mFylyBeans;
    private Map<String, String> map;
    private String[] mContents;
    private String fylxParams;
    private String fylxContent;
    private String type;
    private boolean isDelineTask;
    private boolean isSortDescOrder = true;
    private String sortType = "default";
    private int currentPosition = 0;
    private String currentType = "";
    private String currentQuery = "";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_list);
        getActivityComponent().inject(this);
        map = new HashMap<>();
        mUnbinder = ButterKnife.bind(this);
        mMyOrderListPresenter.attachView(this);
        mEventBus.register(this);
        initToolBar(R.string.menu_deadline_tasks);
        currentType = getResources().getString(R.string.menu_deadline_tasks);
        type = getResources().getString(R.string.menu_deadline_tasks);
        isDelineTask = true;
        initView();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_order_list, menu);
        searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        addressSortItem = menu.findItem(R.id.action_address_sort);
        addressSortItem.setIcon(R.mipmap.ic_default_sort);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_all_tasks://所有工单
                sortType = "default";
                addressSortItem.setIcon(R.mipmap.ic_default_sort);
                getAllOrderList();
                break;
            case R.id.action_address_sort_asc://按地址正序排序
                currentType = "全部工单";
                LogUtil.i(TAG, "allTasks");
                setToolbarTitle(getResources().getString(R.string.menu_all_tasks));
                type = getResources().getString(R.string.menu_all_tasks);
                isDelineTask = false;
                mRecyclerView.smoothScrollToPosition(0);//滑到顶部
                mMyOrderListPresenter.loadAllTaskByAddressAsc();
                break;
            case R.id.action_address_sort_desc://按地址倒序排序
                currentType = "全部工单";
                LogUtil.i(TAG, "allTasks");
                setToolbarTitle(getResources().getString(R.string.menu_all_tasks));
                type = getResources().getString(R.string.menu_all_tasks);
                isDelineTask = false;
                mRecyclerView.smoothScrollToPosition(0);//滑到顶部
                mMyOrderListPresenter.loadAllTaskByAddressDesc();
                break;
            case R.id.action_deadline_tasks://临期工单
                sortType = "default";
                addressSortItem.setIcon(R.mipmap.ic_default_sort);
                getDeadlineList();
                break;
            case R.id.action_expired_task://超期工单
                sortType = "default";
                addressSortItem.setIcon(R.mipmap.ic_default_sort);
                getExptredList();
                break;
            case R.id.action_fylx_tasks://反应类型
                if (GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getFYLXBeanDao().loadAll().size() == 0) {
                    ToastUtils.showShort("正在下载词条数据，请稍后再试");
                    BaseApplication.getInstance().getWordList();
                } else {
                    showWordInfoDialog(item.getItemId());
                }
                break;
            case R.id.action_address_sort://按地址排序
                searchItem.collapseActionView();
                if (isSortDescOrder) {
                    addressSortItem.setIcon(R.mipmap.ic_address_sort_asc);
                    sortType = "DZ_ASC";
                    isSortDescOrder = false;
                } else {
                    addressSortItem.setIcon(R.mipmap.ic_address_sort_desc);
                    sortType = "DZ_DESC";
                    isSortDescOrder = true;
                }

                switch (currentType) {
                    case "临期工单":
                        getDeadlineList();
                        break;
                    case "全部工单":
                        getAllOrderList();
                        break;
                    case "超期工单":
                        getExptredList();
                        break;
                    case "查询":
                        getFindList();
                        break;
                    default://反应类型
                        getFYLXFilterList();
                        break;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getExptredList() {
        currentType = getResources().getString(R.string.menu_expired_tasks);
        setToolbarTitle(getResources().getString(R.string.menu_expired_tasks));
        type = getResources().getString(R.string.menu_expired_tasks);
        isDelineTask = false;
        mRecyclerView.smoothScrollToPosition(0);//滑到顶部
        mMyOrderListPresenter.getExpiredTasks(System.currentTimeMillis());
    }

    private void getDeadlineList() {
        LogUtil.i(TAG, "deadlineTasks");
        currentType = getResources().getString(R.string.menu_deadline_tasks);
        setToolbarTitle(getResources().getString(R.string.menu_deadline_tasks));
        type = getResources().getString(R.string.menu_deadline_tasks);
        isDelineTask = true;
        mRecyclerView.smoothScrollToPosition(0);//滑到顶部
        mMyOrderListPresenter.getDeadlineTasks(System.currentTimeMillis(), false);
    }

    private void getAllOrderList() {
        currentType = "全部工单";
        LogUtil.i(TAG, "allTasks");
        setToolbarTitle(getResources().getString(R.string.menu_all_tasks));
        type = getResources().getString(R.string.menu_all_tasks);
        isDelineTask = false;
        mRecyclerView.smoothScrollToPosition(0);//滑到顶部
        mMyOrderListPresenter.onLoadAllTaskData();
    }

    /**
     * 弹出词条对话框
     *
     * @param id
     */
    private void showWordInfoDialog(final int id) {
        final List<FYLXBean> fylxBeanList = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getFYLXBeanDao().loadAll();
        //反应类型
        final List<String> fanyingleixingList = Lists.transform(fylxBeanList, new Function<FYLXBean, String>() {
            @Nullable
            @Override
            public String apply(@Nullable FYLXBean bwbdlBean) {
                return bwbdlBean.getFA_TYPE_DESCR();
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String title = "";
        if (id == R.id.action_fylx_tasks) {
            mContents = fanyingleixingList.toArray(new String[fanyingleixingList.size()]);
            title = "反应类型";
        }
        builder.setTitle("请选择" + title);
        builder.setItems(mContents, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (id) {
                    case R.id.action_fylx_tasks:
                        sortType = "default";
                        addressSortItem.setIcon(R.mipmap.ic_default_sort);
                        fylxParams = fylxBeanList.get(which).getFA_TYPE_CD();
                        fylxContent = mContents[which];
                        getFYLXFilterList();
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void getFYLXFilterList() {
        type = fylxContent;
        isDelineTask = false;
        LogUtil.i(TAG, "fylxfilterTasks");
        mRecyclerView.smoothScrollToPosition(0);//滑到顶部
        mMyOrderListPresenter.getFilterTask(fylxContent, fylxParams);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EasyHttp.cancelSubscription(mDisposable1);
        mUnbinder.unbind();
        mMyOrderListPresenter.detachView();
        mEventBus.unregister(this);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = null;
    }

    @Override
    public void onLoadExpiredAndFylxMyTasks(String title, List<DUMyTask> taskList) {
        currentType = title;
        type = title;
        isDelineTask = false;
        setToolbarTitle(title);
        mAdapter.setNewData(taskList);
        filterOrder();
    }

    @Override
    public void onError(String message) {
        hideTopProgressbar(mSwipeRefreshLayout);
    }

    @Override
    public void hideShowProgress() {
        hideTopProgressbar(mSwipeRefreshLayout);
    }

    @Override
    public void onRequestStart() {
        showTopProgressbar(mSwipeRefreshLayout);
    }

    @Override
    public void updataStateSuccess(int state, String msg) {
        ToastUtils.showShort(msg);
        switch (type) {
            case "临期工单":
                mMyOrderListPresenter.getDeadlineTasks(System.currentTimeMillis(), false);
                break;
            case "全部工单":
                mMyOrderListPresenter.onLoadAllTaskData();
                break;
            case "超期工单":
                mMyOrderListPresenter.getExpiredTasks(System.currentTimeMillis());
                break;
            default:
                mMyOrderListPresenter.getFilterTask(type, "");
                break;
        }
    }

    @Override
    public void updataStateError(String error) {

    }

    @Override
    public void onLoadNetWorkData(final List<DUMyTask> tasks, final boolean isEnter, final boolean isDeadline) {
        final AsyncSession asyncSession = GreenDaoUtils.getAsyncSession(MainApplication.getInstance());
        //过滤掉本地已处理工单中未上传的工单
        asyncSession.deleteInTx(DUMyTask.class, GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getDUMyTaskDao()
                .queryBuilder()
                .where(DUMyTaskDao.Properties.TaskState.notEq(Constant.ORIGIN_MY_TASK_HISTORY))
                .list());
        asyncSession.setListener(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation asyncOperation) {
                switch (asyncOperation.getType()) {
                    case DeleteInTxIterable:
                        asyncSession.insertOrReplaceInTx(DUMyTask.class, tasks);
                        break;
                    case InsertOrReplaceInTxIterable:
                        if (isDeadline) {
                            mMyOrderListPresenter.getDeadlineTasks(System.currentTimeMillis(), isEnter);
                        } else {
                            mMyOrderListPresenter.onLoadAllTaskData();
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onLoadDeadLineData(List<DUMyTask> duMyTasks) {
        currentType = getResources().getString(R.string.menu_deadline_tasks);
        type = getResources().getString(R.string.menu_deadline_tasks);
        isDelineTask = true;
        setToolbarTitle(getResources().getString(R.string.menu_deadline_tasks));
        mAdapter.setNewData(duMyTasks);
        filterOrder();
    }

    /**
     * 过滤工单
     */
    private void filterOrder() {
        Log.e("EasyHttp", "-----------filterOrder" + currentType);
        if (StringUtils.isEmpty(currentType)) {
            return;
        }
        switch (currentType) {
            case "临期工单":
                GreenDaoUtils.getAsyncSession(MainApplication.getInstance())
                        .runInTx(new Runnable() {
                            @Override
                            public void run() {
                                QueryBuilder<DUMyTask> builder = GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                        .getDUMyTaskDao().queryBuilder()
                                        .where(DUMyTaskDao.Properties.TaskState.notEq(Constant.ORIGIN_MY_TASK_HISTORY))
                                        .where(DUMyTaskDao.Properties.ClsxLong.ge(System.currentTimeMillis()))
                                        .where(DUMyTaskDao.Properties.ClsxLong.le(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                                        .whereOr(DUMyTaskDao.Properties.CmSta.eq("A"), DUMyTaskDao.Properties.CmSta.eq("D"));

                                if ("DZ_ASC".equals(sortType)) {
                                    builder = builder.preferLocalizedStringOrder()
                                            .orderAsc(DUMyTaskDao.Properties.Fsdz);
                                } else if ("DZ_DESC".equals(sortType)) {
                                    builder = builder.preferLocalizedStringOrder()
                                            .orderDesc(DUMyTaskDao.Properties.Fsdz);
                                } else {
                                    builder = builder.orderDesc(DUMyTaskDao.Properties.CreDttm);
                                }

                                final List<DUMyTask> duMyTasks = builder.list();

                                type = getResources().getString(R.string.menu_deadline_tasks);
                                isDelineTask = true;
                                setToolbarTitle(getResources().getString(R.string.menu_deadline_tasks));
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAdapter.setNewData(duMyTasks);
                                    }
                                });
                            }
                        });

                break;
            case "全部工单":
                GreenDaoUtils.getAsyncSession(MainApplication.getInstance())
                        .runInTx(new Runnable() {
                            @Override
                            public void run() {
                                QueryBuilder<DUMyTask> builder = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                                        .getDUMyTaskDao().queryBuilder()
                                        .where(DUMyTaskDao.Properties.TaskState.notEq(Constant.ORIGIN_MY_TASK_HISTORY));

                                if ("DZ_ASC".equals(sortType)) {
                                    builder = builder.preferLocalizedStringOrder()
                                            .orderAsc(DUMyTaskDao.Properties.Fsdz);
                                } else if ("DZ_DESC".equals(sortType)) {
                                    builder = builder.preferLocalizedStringOrder()
                                            .orderDesc(DUMyTaskDao.Properties.Fsdz);
                                } else {
                                    builder = builder.orderDesc(DUMyTaskDao.Properties.CreDttm);
                                }

                                final List<DUMyTask> duMyTasks = builder.list();

                                type = getResources().getString(R.string.menu_all_tasks);
                                isDelineTask = false;
                                setToolbarTitle(getResources().getString(R.string.menu_all_tasks));
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAdapter.setNewData(duMyTasks);
                                    }
                                });
                            }
                        });

                break;
            case "超期工单":
                GreenDaoUtils.getAsyncSession(MainApplication.getInstance())
                        .runInTx(new Runnable() {
                            @Override
                            public void run() {
                                QueryBuilder<DUMyTask> builder = GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                        .getDUMyTaskDao().queryBuilder()
                                        .where(DUMyTaskDao.Properties.TaskState.notEq(Constant.ORIGIN_MY_TASK_HISTORY))
                                        .where(DUMyTaskDao.Properties.ClsxLong.lt(System.currentTimeMillis()))
                                        .whereOr(DUMyTaskDao.Properties.CmSta.eq("A"), DUMyTaskDao.Properties.CmSta.eq("D"));

                                if ("DZ_ASC".equals(sortType)) {
                                    builder = builder.preferLocalizedStringOrder()
                                            .orderAsc(DUMyTaskDao.Properties.Fsdz);
                                } else if ("DZ_DESC".equals(sortType)) {
                                    builder = builder.preferLocalizedStringOrder()
                                            .orderDesc(DUMyTaskDao.Properties.Fsdz);
                                } else {
                                    builder = builder.orderDesc(DUMyTaskDao.Properties.CreDttm);
                                }

                                final List<DUMyTask> duMyTasks = builder.list();

                                type = getResources().getString(R.string.menu_expired_tasks);
                                isDelineTask = false;
                                setToolbarTitle(getResources().getString(R.string.menu_expired_tasks));
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAdapter.setNewData(duMyTasks);
                                    }
                                });

                            }
                        });
                break;
            default://反应类型
                GreenDaoUtils.getAsyncSession(MainApplication.getInstance())
                        .runInTx(new Runnable() {
                            @Override
                            public void run() {
                                QueryBuilder<DUMyTask> builder = GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                        .getDUMyTaskDao().queryBuilder()
                                        .where(DUMyTaskDao.Properties.TaskState.notEq(Constant.ORIGIN_MY_TASK_HISTORY))
                                        .where(DUMyTaskDao.Properties.FaTypeCd.eq(fylxContent));

                                if ("DZ_ASC".equals(sortType)) {
                                    builder = builder.preferLocalizedStringOrder()
                                            .orderAsc(DUMyTaskDao.Properties.Fsdz);
                                } else if ("DZ_DESC".equals(sortType)) {
                                    builder = builder.preferLocalizedStringOrder()
                                            .orderDesc(DUMyTaskDao.Properties.Fsdz);
                                } else {
                                    builder = builder.orderDesc(DUMyTaskDao.Properties.CreDttm);
                                }

                                final List<DUMyTask> duMyTasks = builder.list();

                                type = fylxContent;
                                isDelineTask = false;
                                setToolbarTitle(fylxContent);
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAdapter.setNewData(duMyTasks);
                                    }
                                });
                            }
                        });
                break;
        }
        if (mAdapter.getData().size() > 0) {
            if (mAdapter.getData().size() > currentPosition) {
                mRecyclerView.scrollToPosition(currentPosition);
            } else {
                mRecyclerView.scrollToPosition((mAdapter.getData().size() - 1));
            }
        }
    }

    @Override
    public void onLoadAllData(List<DUMyTask> duMyTasks) {
        currentType = "全部工单";
        type = getResources().getString(R.string.menu_all_tasks);
        isDelineTask = false;
        setToolbarTitle(getResources().getString(R.string.menu_all_tasks));
        mAdapter.setNewData(duMyTasks);
        filterOrder();
    }

    /**
     * 查询
     *
     * @param query
     * @return
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        if (TextUtils.isEmpty(query)) {
            return false;
        }
        currentType = "查询";
        currentQuery = query;
        getFindList();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void getFindList() {
        if (TextUtils.isEmpty(currentQuery)) {
            return;
        }
        GreenDaoUtils.getAsyncSession(MainApplication.getInstance())
                .runInTx(new Runnable() {
                    @Override
                    public void run() {
                        QueryBuilder<DUMyTask> builder = GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                .getDUMyTaskDao().queryBuilder()
                                .where(DUMyTaskDao.Properties.TaskState.notEq(Constant.ORIGIN_MY_TASK_HISTORY))
                                .whereOr(DUMyTaskDao.Properties.FaId.like("%" + currentQuery + "%"),
                                        DUMyTaskDao.Properties.ContactValue.like("%" + currentQuery + "%"),
                                        DUMyTaskDao.Properties.AcctId.like("%" + currentQuery + "%"),
                                        DUMyTaskDao.Properties.Fsdz.like("%" + currentQuery + "%"),
                                        DUMyTaskDao.Properties.EntityName.like("%" + currentQuery + "%"),
                                        DUMyTaskDao.Properties.Fyly.like("%" + currentQuery + "%"),
                                        DUMyTaskDao.Properties.FaTypeCd.like("%" + currentQuery + "%"),
                                        DUMyTaskDao.Properties.Fynr.like("%" + currentQuery + "%"),
                                        DUMyTaskDao.Properties.Clsx.like("%" + currentQuery + "%"),
                                        DUMyTaskDao.Properties.CreDttm.like("%" + currentQuery + "%"));

                        if ("DZ_ASC".equals(sortType)) {
                            builder = builder.preferLocalizedStringOrder()
                                    .orderAsc(DUMyTaskDao.Properties.Fsdz);
                        } else if ("DZ_DESC".equals(sortType)) {
                            builder = builder.preferLocalizedStringOrder()
                                    .orderDesc(DUMyTaskDao.Properties.Fsdz);
                        } else {
                            builder = builder.orderDesc(DUMyTaskDao.Properties.CreDttm);
                        }

                        final List<DUMyTask> queryList = builder.list();

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.setNewData(queryList);
                            }
                        });
                    }
                });
        if (mAdapter.getData().size() > 0) {
            if (mAdapter.getData().size() > currentPosition) {
                mRecyclerView.scrollToPosition(currentPosition);
            } else {
                mRecyclerView.scrollToPosition((mAdapter.getData().size() - 1));
            }
        }
    }

    /*
     * 初始化控件
     */
    private void initView() {
        //刷新控件
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setToolbarTitle(getResources().getString(R.string.menu_all_tasks));
                currentType = "全部工单";
                sortType = "default";
                addressSortItem.setIcon(R.mipmap.ic_default_sort);
                mMyOrderListPresenter.loadNetworkData(false, false);
            }
        };

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);
        mSwipeRefreshLayout.setOnRefreshListener(listener);
        //置顶按钮
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.smoothScrollToPosition(0);//滑到顶部
            }
        });

        mMyTaskList = new ArrayList<DUMyTask>();
        mAdapter = new MyOrderListAdapter(map, Constant.ADAPTER_TYPE_NORMAL,
                R.layout.item_my_order_list_row_layout, mMyTaskList);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setLayoutManager(new MyLinearLayoutManager(MyOrderListActivity.this));
        mRecyclerView.setAdapter(mAdapter);
        initRecyclerViewState();
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
                if (mFab != null) {
                    if (firstItemPosition > 10 && newState == 0) {
                        mFab.setVisibility(View.VISIBLE);
                    } else {
                        mFab.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItemIndex = manager.findLastVisibleItemPosition();
//                if ((lastItemIndex >= mMyTaskList.size() - 1)
//                        && isLoadingMoreValid
//                        && (!isLoading)
//                        && (dy > 0)) {
//                    loadMore();
//                }
            }
        });
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, final int position) {
        currentPosition = position;
        String taskId = mAdapter.getData().get(position).getTaskId();
        String issueType = mAdapter.getData().get(position).getIssueType();
        String issueContent = mAdapter.getData().get(position).getIssueContent();
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
            case R.id.my_order_list_item_tv_receive://接收
                mMyOrderListPresenter.updataTaskState(Constant.TASK_STATE_RECEIVED,
                        mAdapter.getData().get(position), "", "", "");
                break;

            case R.id.my_order_list_item_tv_delay:
                AlertDialog.Builder builder = new AlertDialog.Builder(MyOrderListActivity.this);
                builder.setTitle("延期原因");
                View view1 = View.inflate(MyOrderListActivity.this, R.layout.dialog_delay_refund, null);
                final EditText etReason = (EditText) view1.findViewById(R.id.et_reason);
                final EditText etRemark = (EditText) view1.findViewById(R.id.et_remark);
                final EditText etDelayDay = (EditText) view1.findViewById(R.id.et_delay_day);
                builder.setView(view1);
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (StringUtils.isEmpty(etReason.getText().toString())) {
                            ToastUtils.showShort("请输入延期原因");
                            return;
                        }
                        if (StringUtils.isEmpty(etRemark.getText().toString())) {
                            ToastUtils.showShort("请输入延期备注");
                            return;
                        }
                        if (StringUtils.isEmpty(etDelayDay.getText().toString())) {
                            ToastUtils.showShort("请输入延期天数");
                            return;
                        }
                        mMyOrderListPresenter.updataTaskState(Constant.TASK_STATE_DELAY
                                , mAdapter.getData().get(position), etReason.getText().toString().trim(),
                                etRemark.getText().toString().trim(), etDelayDay.getText().toString().trim());
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;

            case R.id.my_order_list_item_tv_back:
                AlertDialog.Builder refundBuilder = new AlertDialog.Builder(MyOrderListActivity.this);
                refundBuilder.setTitle("退单原因");
                View refundView = View.inflate(MyOrderListActivity.this, R.layout.dialog_delay_refund, null);
                refundView.findViewById(R.id.et_delay_day).setVisibility(View.GONE);
                final EditText etRemark2 = (EditText) refundView.findViewById(R.id.et_remark);
                final EditText etReason2 = (EditText) refundView.findViewById(R.id.et_reason);
                refundBuilder.setView(refundView);
                refundBuilder.setNegativeButton("取消", null);
                refundBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (StringUtils.isEmpty(etReason2.getText().toString())) {
                            ToastUtils.showShort("请输入退单原因");
                            return;
                        }
                        if (StringUtils.isEmpty(etRemark2.getText().toString())) {
                            ToastUtils.showShort("请输入退单备注");
                            return;
                        }
                        mMyOrderListPresenter.updataTaskState(Constant.TASK_STATE_BACK
                                , mAdapter.getData().get(position), etReason2.getText().toString().trim(),
                                etRemark2.getText().toString().trim(), "");
                    }
                });
                AlertDialog refudnDialog = refundBuilder.create();
                refudnDialog.show();
                break;

            case R.id.my_order_list_item_tv_handle:
                switch (mAdapter.getData().get(position).getFynr()) {
                    case "疑问水表":
                        navigationQuestionHandleActivity(position, issueType, issueContent);
                        break;
                    case "违章用水":
                        navigationIllegalHandleActivity(position, issueType, issueContent);
                        break;
                    case "无数据复核延迟工单":
                        navigationRemoteHandleActivity(position, issueType, issueContent);
                        break;
                    default:
                        navigationHandleActivity(position, issueType, issueContent);
                        break;
                }
                break;
        }
    }

    private void navigationHandleActivity(int position, String issueType, String issueContent) {
        currentType = type;
        currentPosition = position;
        Intent intent2 = new Intent(MyOrderListActivity.this, HandleOrderActivity.class);
        intent2.putExtra(Constant.DUMyTask, mAdapter.getData().get(position));
        intent2.putExtra(Constant.ORIGIN, ORIGIN);
        intent2.putExtra(Constant.TASK_ID, mAdapter.getData().get(position).getFaId());
        intent2.putExtra(Constant.ISSUE_TYPE, TextUtil.getString(issueType));
        intent2.putExtra(Constant.ISSUE_CONTENT, TextUtil.getString(issueContent));
        intent2.putExtra(Constant.TYPE_IS_HISTORY, false);
        startActivity(intent2);
    }

    private static final String ACCOUNT = "account";
    //向子程序发送用户id
    private static final String USER_ID = "userId";
    //向子程序发送用户名
    private static final String USER_NAME = "userName";
    private static final String EXTENDED_INFO = "extendedInfo";

    private void navigationRemoteHandleActivity(int position, String issueType, String issueContent) {
        currentType = type;
        currentPosition = position;
        Intent intent2 = new Intent(MyOrderListActivity.this, RemoteWaterActivity.class);
//        intent2.putExtra(ACCOUNT, "888888");
//        intent2.putExtra(USER_ID, 3810);
//        intent2.putExtra(USER_NAME, "test");
//        intent2.putExtra(EXTENDED_INFO, "");
//        intent2.putExtra(Constant.ORIGIN, 99);
        intent2.putExtra(Constant.DUMyTask, mAdapter.getData().get(position));
        intent2.putExtra(Constant.ORIGIN, ORIGIN);
        intent2.putExtra(Constant.TASK_ID, mAdapter.getData().get(position).getCaseId());
        intent2.putExtra(Constant.ISSUE_TYPE, TextUtil.getString(issueType));
        intent2.putExtra(Constant.ISSUE_CONTENT, TextUtil.getString(issueContent));
        intent2.putExtra(Constant.TYPE_IS_HISTORY, false);
        startActivity(intent2);
    }

    private void navigationIllegalHandleActivity(int position, String issueType, String issueContent) {
        currentType = type;
        currentPosition = position;
        Intent intent2 = new Intent(MyOrderListActivity.this, IllegalWaterActivity.class);
        intent2.putExtra(Constant.DUMyTask, mAdapter.getData().get(position));
        intent2.putExtra(Constant.ORIGIN, ORIGIN);
        intent2.putExtra(Constant.TASK_ID, mAdapter.getData().get(position).getCaseId());
        intent2.putExtra(Constant.ISSUE_TYPE, TextUtil.getString(issueType));
        intent2.putExtra(Constant.ISSUE_CONTENT, TextUtil.getString(issueContent));
        intent2.putExtra(Constant.TYPE_IS_HISTORY, false);
        startActivity(intent2);
    }

    private void navigationQuestionHandleActivity(int position, String issueType, String issueContent) {
        currentType = type;
        currentPosition = position;
        Intent intent2 = new Intent(MyOrderListActivity.this, QuestionWaterActivity.class);
        intent2.putExtra(Constant.DUMyTask, mAdapter.getData().get(position));
        intent2.putExtra(Constant.ORIGIN, ORIGIN);
        intent2.putExtra(Constant.TASK_ID, mAdapter.getData().get(position).getCaseId());
        intent2.putExtra(Constant.ISSUE_TYPE, TextUtil.getString(issueType));
        intent2.putExtra(Constant.ISSUE_CONTENT, TextUtil.getString(issueContent));
        intent2.putExtra(Constant.TYPE_IS_HISTORY, false);
        startActivity(intent2);
    }

    @Subscribe
    public void onLoadFinishWord(WordInfoEntity wordInfoEntity) {
        mFylyBeans = wordInfoEntity.getFYLY();
        for (int i = 0; i < mFylyBeans.size(); i++) {
            map.put(mFylyBeans.get(i).getLY_ID(), mFylyBeans.get(i).getDESCR());
        }
        if (mAdapter != null) {
            mAdapter.setMap(map);
        }
    }

    /**
     * 首次加载数据
     *
     * @param initResult
     */
    @Subscribe
    public void onInitResult(UIBusEvent.InitResult initResult) {
//        List<DUMyTask> duMyTasks = GreenDaoUtils.getDaoSession(this).getDUMyTaskDao().loadAll();
        mFylyBeans = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getFYLYBeanDao().loadAll();
        for (int i = 0; i < mFylyBeans.size(); i++) {
            map.put(mFylyBeans.get(i).getLY_ID(), mFylyBeans.get(i).getDESCR());
        }
        if (mAdapter != null) {
            mAdapter.setMap(map);
        }

        LogUtil.i(TAG, "---onInitResult---" + initResult.isSuccess());
        if (initResult.isSuccess()) {
            mMyOrderListPresenter.getDeadlineTasks(System.currentTimeMillis(), true);
            mMyOrderListPresenter.loadNetworkData(true, isDelineTask);

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

            return;
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
    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        //查询客户基本信息
//        navigationUserInfo(i);
        if ("A".equals(mAdapter.getData().get(i).getCmSta()) || "U".equals(mAdapter.getData().get(i).getCmSta())) {
            navigationHandleActivity(i, mAdapter.getData().get(i).getIssueType(),
                    mAdapter.getData().get(i).getIssueContent());
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
                        MProgressDialog.showProgress(MyOrderListActivity.this, "加载中...");
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                    }

                    @Override
                    public void onSuccess(CustomerInfoFindResult entity) {
                        if (entity != null) {
                            LogUtils.i(entity.toString());
                            Intent intent = new Intent(MyOrderListActivity.this, UserDetailInformationActivity.class);
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
}