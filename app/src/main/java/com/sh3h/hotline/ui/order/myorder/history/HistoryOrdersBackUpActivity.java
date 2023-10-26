package com.sh3h.hotline.ui.order.myorder.history;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.maning.mndialoglibrary.MProgressDialog;
import com.sh3h.dataprovider.BaseApplication;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.data.entity.response.FYLXBean;
import com.sh3h.dataprovider.data.entity.response.FYLYBean;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.DUMyTaskDao;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.MyOrderListAdapter;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.service.SyncConst;
import com.sh3h.hotline.service.UploadDataService;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.bill.detail.UserDetailInformationActivity;
import com.sh3h.hotline.ui.order.TaskState;
import com.sh3h.hotline.ui.order.myorder.delayorback.DelayOrBackOrderActivity;
import com.sh3h.hotline.ui.order.myorder.handle.HandleOrderActivity;
import com.sh3h.ipc.module.MyModule;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

import static com.sh3h.hotline.util.ConstDataUtil.PAGE_SIZE;

/**
 * 历史工单
 * Created by zhangjing on 2016/9/18.
 */
public class HistoryOrdersBackUpActivity extends ParentActivity implements HistoryOrdersMvpView, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener, SearchView.OnQueryTextListener {
    @Inject
    Bus mEventBus;

    @Inject
    HistoryOrdersPresenter mHistoryOrdersPresenter;

    @Inject
    ConfigHelper mConfigHelper;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.my_order_list_container)
    CoordinatorLayout mShackContainer;

    @BindView(R.id.my_order_list_fab)
    FloatingActionButton mFab;

    @BindView(R.id.synchronize_data_swiperefresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.my_order_list_recyclerView)
    RecyclerView mRecyclerView;

    private final static String TAG = "HistoryOrdersActivity";
    public final static int ORIGIN = Constant.ORIGIN_MY_TASK_HISTORY;
    private Unbinder mUnbinder;

    private MenuItem searchItem;
    private MyOrderListAdapter mAdapter;//适配器

    private boolean isLoading;
    private boolean isLoadingMoreValid;
    private int offset = 0;

    private List<DUMyTask> mHistoryTaskList;
    private Disposable mDisposable1;
    private String type;
    private boolean isDelineTask;
    private String currentType = "";
    private String[] mContents;
    private String fylxParams;
    private String fylxContent;

    public HistoryOrdersBackUpActivity() {
        offset = 0;
        isLoading = false;
        isLoadingMoreValid = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_list);
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);
        mHistoryOrdersPresenter.attachView(this);
        mEventBus.register(this);

        initToolBar();
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

    /**
     * 子标题(记录数：XXXX)
     */
    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.mipmap.arrow);
        mToolbar.setTitle(R.string.label_history_orders);
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history_orders, menu);
        searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_all_tasks://所有工单
                currentType = "全部工单";
                LogUtil.i(TAG, "allTasks");
                setToolbarTitle(getResources().getString(R.string.menu_all_tasks));
                type = getResources().getString(R.string.menu_all_tasks);
                isDelineTask = false;
                mRecyclerView.smoothScrollToPosition(0);//滑到顶部
                showTopProgressbar(mSwipeRefreshLayout);
                mHistoryOrdersPresenter.loadMyHistoryTasks(true, offset, PAGE_SIZE);
                break;
            case R.id.action_upload_tasks://未上传工单
                LogUtil.i(TAG, "uploadTasks");
                currentType = getResources().getString(R.string.menu_upload_tasks);
                setToolbarTitle(getResources().getString(R.string.menu_upload_tasks));
                type = getResources().getString(R.string.menu_upload_tasks);
                isDelineTask = false;
                mRecyclerView.smoothScrollToPosition(0);//滑到顶部
                mHistoryOrdersPresenter.getUploadTasks(System.currentTimeMillis(), false);
                break;
            case R.id.action_deadline_tasks://临期工单
                LogUtil.i(TAG, "deadlineTasks");
                currentType = getResources().getString(R.string.menu_deadline_tasks);
                setToolbarTitle(getResources().getString(R.string.menu_deadline_tasks));
                type = getResources().getString(R.string.menu_deadline_tasks);
                isDelineTask = true;
                mRecyclerView.smoothScrollToPosition(0);//滑到顶部
                mHistoryOrdersPresenter.getDeadlineTasks(System.currentTimeMillis(), false);
                break;
            case R.id.action_expired_task://超期工单
                currentType = getResources().getString(R.string.menu_expired_tasks);
                setToolbarTitle(getResources().getString(R.string.menu_expired_tasks));
                type = getResources().getString(R.string.menu_expired_tasks);
                isDelineTask = false;
                mRecyclerView.smoothScrollToPosition(0);//滑到顶部
                mHistoryOrdersPresenter.getExpiredTasks(System.currentTimeMillis());
                break;
            case R.id.action_noexpired_task://未超期工单
                currentType = getResources().getString(R.string.menu_noexpired_tasks);
                setToolbarTitle(getResources().getString(R.string.menu_noexpired_tasks));
                type = getResources().getString(R.string.menu_noexpired_tasks);
                isDelineTask = false;
                mRecyclerView.smoothScrollToPosition(0);//滑到顶部
                mHistoryOrdersPresenter.getNoExpiredTasks(System.currentTimeMillis());
                break;
            case R.id.action_fylx_tasks://反应类型
                if (GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getFYLXBeanDao().loadAll().size() == 0) {
                    ToastUtils.showShort("正在下载词条数据，请稍后再试");
                    BaseApplication.getInstance().getWordList();
                } else {
                    showWordInfoDialog(item.getItemId());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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
                        fylxParams = fylxBeanList.get(which).getFA_TYPE_CD();
                        fylxContent = mContents[which];
                        type = fylxContent;
                        isDelineTask = false;
                        LogUtil.i(TAG, "fylxfilterTasks");
                        mRecyclerView.smoothScrollToPosition(0);//滑到顶部
                        mHistoryOrdersPresenter.getFilterTask(fylxContent, fylxParams);
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    private Map<String, String> map = new HashMap<>();

    /*
     * 初始化控件
     */
    private void initView() {
        //刷新控件
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
//                if (!NetworkUtils.isAvailableByPing()) {
//                    ApplicationsUtil.showMessage(HistoryOrdersActivity.this,
//                            R.string.toast_network_is_not_connect);
//                    hideTopProgressbar(mSwipeRefreshLayout);
//                    return;
//                }
//                uploadAllHistoryTasks(Constant.TASK_TYPE_DOWNLOAD, false);
                mSwipeRefreshLayout.setRefreshing(false);
                Intent intent = UploadDataService.getStartIntent(HistoryOrdersBackUpActivity.this);
                intent.putExtra(SyncConst.SYNC_IS_FROM_MY_TASK, false);
                startService(intent);
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        initRecyclerViewState();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (isAllPermissionsGranted && mConfigHelper != null && mConfigHelper.initDefaultConfigFiles()) {
//            mHistoryOrdersPresenter.loadMyHistoryTasks(true, 0, PAGE_SIZE);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mHistoryOrdersPresenter.detachView();
        mEventBus.unregister(this);
        EasyHttp.cancelSubscription(mDisposable1);
    }

    @Override
    public void onGetHistoryOrdersList(boolean isFirstTime, List<DUMyTask> result) {
        LogUtil.i(TAG, "onGetHistoryOrdersList");
        hideTopProgressbar(mSwipeRefreshLayout);

        if (result != null) {
            int unReadRw = 0;
            for (DUMyTask duHistoryTask : result) {
                if (duHistoryTask.getIsUploadSuccess() == Constant.NO_UPLOAD) {
                    unReadRw++;
                }
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(MyModule.PACKAGE_NAME, getApplicationContext().getPackageName());
                jsonObject.put(MyModule.ACTIVITY_NAME, HistoryOrdersBackUpActivity.class.getName());
                JSONArray subJSONArray = new JSONArray();
                subJSONArray.put("count#" + unReadRw);
                jsonObject.put(MyModule.DATA, subJSONArray);
                MyModule myModule = new MyModule(jsonObject.toString());
                MainApplication.get(getApplicationContext()).setMyModule(myModule);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        isLoading = false;
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

        isLoadingMoreValid = true;
        if (isFirstTime) {
            mHistoryTaskList.clear();
        }
        mHistoryTaskList.addAll(result);
//        List<DUHistoryTask> filterResult = fiterLatestHistoryTask(mHistoryTaskList);
//        mHistoryTaskList.clear();
//        mHistoryTaskList.addAll(filterResult);
        mAdapter.notifyDataSetChanged();
        mToolbar.setSubtitle("记录数:" + mHistoryTaskList.size() + "");
    }

    @Override
    public void onFilterOrdersList(boolean isFirstTime, List<DUMyTask> result) {
        LogUtil.i(TAG, "onFilterOrdersList");
        hideTopProgressbar(mSwipeRefreshLayout);

        isLoading = false;
        if ((!isFirstTime) && (mHistoryTaskList.size() > 0)) {
            if (mHistoryTaskList.get(mHistoryTaskList.size() - 1) == null) {
                mHistoryTaskList.remove(mHistoryTaskList.size() - 1);
                mAdapter.notifyItemRemoved(mHistoryTaskList.size());// 这三行代码是去除最后一条loading的布局
            }
        }

//        if ((result == null) || (result.size() <= 0)) {
//            LogUtil.i(TAG, "onGetHistoryOrdersList: historyOrdersList has no item");
//            return;
//        }

        isLoadingMoreValid = true;
        if (isFirstTime) {
            mHistoryTaskList.clear();
        }
        mHistoryTaskList.addAll(result);
//        List<DUHistoryTask> filterResult = fiterLatestHistoryTask(mHistoryTaskList);
//        mHistoryTaskList.clear();
//        mHistoryTaskList.addAll(filterResult);
        mAdapter.notifyDataSetChanged();
        mToolbar.setSubtitle("记录数:" + mHistoryTaskList.size() + "");
    }

    /**
     * 过滤出最新状态的历史记录
     *
     * @param duHistoryTaskList
     * @return
     */
    private List<DUHistoryTask> fiterLatestHistoryTask(List<DUHistoryTask> duHistoryTaskList) {
        ArrayList<DUHistoryTask> duResult = new ArrayList<DUHistoryTask>();
        if (duHistoryTaskList == null || duHistoryTaskList.size() == 0) {
            return duResult;
        }
        Map<String, List<DUHistoryTask>> map = group(duHistoryTaskList, new GroupBy<String>() {
            @Override
            public String groupby(Object obj) {
                DUHistoryTask d = (DUHistoryTask) obj;
                return d.getTASK_ID();
            }
        });

        Iterator<Map.Entry<String, List<DUHistoryTask>>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, List<DUHistoryTask>> entry = entries.next();
            //排序,按照任务状态降序
            if (entry.getValue().size() > 1) {
                Collections.sort(entry.getValue(), new Comparator<DUHistoryTask>() {
                    @Override
                    public int compare(DUHistoryTask task1, DUHistoryTask task2) {
                        int i = task2.getTASK_STATE() - task1.getTASK_STATE();
                        if (i == 0) {
                            return task2.getTASK_STATE() - task1.getTASK_STATE();
                        }
                        return i;
                    }
                });
            }
            duResult.add(entry.getValue().get(0));
        }

        //按照回复时间排序
        Collections.sort(duResult, new Comparator<DUHistoryTask>() {
            @Override
            public int compare(DUHistoryTask task1, DUHistoryTask task2) {
                return String.valueOf(task2.getREPLY_TIME()).compareTo(String.valueOf(task1.getREPLY_TIME()));
//                return (int) ((task2.getREPLY_TIME()) - (task1.getREPLY_TIME()));
            }
        });
        return duResult;
    }

    @Override
    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
        switch (view.getId()) {
            case R.id.my_order_list_item_cardview:
                Intent intent;
                if (mAdapter.getData().get(position).getState() == TaskState.DELAY ||
                        mAdapter.getData().get(position).getState() == TaskState.BACK) {
                    intent = new Intent(HistoryOrdersBackUpActivity.this, DelayOrBackOrderActivity.class);
                    intent.putExtra(Constant.ORIGIN, ORIGIN);
                    intent.putExtra(Constant.DUMyTask, mAdapter.getData().get(position));
                    intent.putExtra(Constant.DATA_IS_UPLOAD, mAdapter.getData().get(position).getIsUploadSuccess());
                    intent.putExtra(Constant.TASKSTATE, mAdapter.getData().get(position).getState());
                    intent.putExtra(Constant.TASK_ID, mAdapter.getData().get(position).getFaId());
                    intent.putExtra(DUHistoryTask.DUHistoryTask_ID, mAdapter.getData().get(position).getID());
                    startActivity(intent);
                } else if (mAdapter.getData().get(position).getState() == TaskState.HANDLE ||
                        mAdapter.getData().get(position).getState() == TaskState.FINISH) {
                    intent = new Intent(HistoryOrdersBackUpActivity.this, HandleOrderActivity.class);
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
                        MProgressDialog.showProgress(HistoryOrdersBackUpActivity.this, "加载中...");
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                    }

                    @Override
                    public void onSuccess(CustomerInfoFindResult entity) {
                        if (entity != null) {
                            LogUtils.i(entity.toString());
                            Intent intent = new Intent(HistoryOrdersBackUpActivity.this, UserDetailInformationActivity.class);
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

    @Override
    public boolean onQueryTextSubmit(final String query) {
        if (TextUtils.isEmpty(query)) {
            return false;
        }
        GreenDaoUtils.getAsyncSession(this)
                .runInTx(new Runnable() {
                    @Override
                    public void run() {
                        List<DUMyTask> queryList = GreenDaoUtils.getDaoSession(HistoryOrdersBackUpActivity.this)
                                .getDUMyTaskDao().queryBuilder()
                                .where(DUMyTaskDao.Properties.TaskState.eq(Constant.ORIGIN_MY_TASK_HISTORY))
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
                        mToolbar.setSubtitle("记录数:" + mHistoryTaskList.size() + "");
                    }
                });
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public interface GroupBy<T> {
        T groupby(Object obj);
    }

    public static final <T extends Comparable<T>, D> Map<T, List<D>> group(Collection<D> colls, GroupBy<T> gb) {
        if (colls == null || colls.isEmpty()) {
            return null;
        }
        if (gb == null) {
            return null;
        }
        Iterator<D> iter = colls.iterator();
        Map<T, List<D>> map = new HashMap<T, List<D>>();
        while (iter.hasNext()) {
            D d = iter.next();
            T t = gb.groupby(d);
            if (map.containsKey(t)) {
                map.get(t).add(d);
            } else {
                List<D> list = new ArrayList<D>();
                list.add(d);
                map.put(t, list);
            }
        }
        return map;
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

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItemIndex = manager.findLastVisibleItemPosition();
//                if ((lastItemIndex >= mHistoryTaskList.size() - 1)
//                        && isLoadingMoreValid
//                        && (!isLoading)
//                        && dy > 0) {
//                    loadMore();
//                }
            }
        });
    }

    @Override
    public void onGetSynchronizeData() {
        hideTopProgressbar(mSwipeRefreshLayout);
        ApplicationsUtil.showMessage(HistoryOrdersBackUpActivity.this, "同步数据完成");
    }

    @Override
    public void showMessage(String message) {
        hideTopProgressbar(mSwipeRefreshLayout);
        ApplicationsUtil.showMessage(this, message);
    }

//    /**
//     * recyclerView 适配器
//     */
//    private class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//        private List<DUHistoryTask> mList;
//        private Context mContext;
//        private int loading = -1;
//
//        public MyAdapter(List<DUHistoryTask> list, Context context) {
//            mList = list;
//            mContext = context;
//        }
//
//        @Override
//        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//            final ItemOnClickListener listener = new ItemOnClickListener(position);
//            if (holder instanceof ViewHolder) {
//                final ViewHolder viewHolder = (ViewHolder) holder;
//                viewHolder.orderNo.setText(mList.get(position).getTASK_ID());
//                SimpleDateFormat s1 = new SimpleDateFormat(TextUtil.FORMAT_DATE_NO_SECOND);
//                SimpleDateFormat s2 = new SimpleDateFormat(TextUtil.FORMAT_DATE_NO_SECOND);
//                viewHolder.replyTime.setText(s1.format(new Date(mList.get(position).getREPLY_TIME())));
//                viewHolder.account.setText(mList.get(position).getHU_MING());
//                viewHolder.phone.setText(mList.get(position).getTELEPHONE());
//                viewHolder.address.setText(mList.get(position).getADDRESS());
//                viewHolder.issuer.setText(mList.get(position).getISSUER());
//                viewHolder.reflectType.setText(mList.get(position).getISSUE_TYPE());
//                if (!TextUtil.isNullOrEmpty(mList.get(position).getISSUE_ORIGIN())) {
//                    viewHolder.reflectOrigin.setText(mList.get(position).getISSUE_ORIGIN());
//                }
//                viewHolder.reflectContent.setText(mList.get(position).getISSUE_CONTENT());
//                viewHolder.handleLimit.setText(s2.format(new Date(mList.get(position).getREPLY_DEADLINE())));
//                viewHolder.cardView.setOnClickListener(listener);
//                switch (mList.get(position).getTASK_STATE()) {
//
//                    case TaskState.RECEIVED://接收
//                        viewHolder.result.setText(R.string.text_receive);
//                        break;
//                    case TaskState.DELAY://延期
//                        viewHolder.result.setText(R.string.label_delay);
//                        break;
//                    case TaskState.BACK://退单
//                        viewHolder.result.setText(R.string.label_charge_back);
//                        break;
//                    case TaskState.HANDLE://处理
//                        viewHolder.result.setText(R.string.label_handle);
//                        break;
//                    case TaskState.FINISH://已完成
//                        viewHolder.result.setText(R.string.label_finish);
//                        break;
//                }
//
//                if (mList.get(position).getUPLOAD_FLAG() == Constant.HAS_UPLOADED) {
//                    viewHolder.dataIsUpload.setImageResource(R.mipmap.ic_cloud_uploaded_48px);
//                    viewHolder.dataUploadText.setText(R.string.label_has_uploaded);
//                } else {
//                    viewHolder.dataIsUpload.setImageResource(R.mipmap.ic_cloud_upload_48px);
//                    viewHolder.dataUploadText.setText(R.string.label_no_upload);
//                }
//
//                int photoCount = 0;
//                int voiceCount = 0;
//                int photoNoUploadCount = 0;
//                int voiceNoUploadCount = 0;
//
//                for (DUMedia duMedia : mList.get(position).getDuMedias()) {
//                    switch (duMedia.getFileType()) {
//
//                        case DUMedia.FILE_TYPE_PICTURE:
//                            ++photoCount;
//                            if (duMedia.getUploadFlag() == DUMedia.UPLOAD_FLAG_LOCAL) {
//                                ++photoNoUploadCount;
//                            }
//                            break;
//
//                        case DUMedia.FILE_TYPE_VOICE:
//                            ++voiceCount;
//                            if (duMedia.getUploadFlag() == DUMedia.UPLOAD_FLAG_LOCAL) {
//                                ++voiceNoUploadCount;
//                            }
//                            break;
//                    }
//                }
//
//                viewHolder.photoCount.setText(String.valueOf(photoCount));//显示图片总数
//                if (photoNoUploadCount > 0) {
//                    viewHolder.photoIsUpload.setImageResource(R.mipmap.ic_picture_upload_48px);
//                } else {
//                    viewHolder.photoIsUpload.setImageResource(R.mipmap.ic_picture_uploaded_48px);
//                }
//
//                viewHolder.voiceCount.setText(String.valueOf(voiceCount));//显示音频总数
//                if (voiceNoUploadCount > 0) {
//                    viewHolder.voiceIsUpload.setImageResource(R.mipmap.ic_music_upload_48px);
//                } else {
//                    viewHolder.voiceIsUpload.setImageResource(R.mipmap.ic_music_uploaded_48px);
//                }
//            } else {
//                ((LoadingViewHolder) holder).progressBar.setIndeterminate(true);
//            }
//        }
//
//        /**
//         * item内控件事件
//         */
//        class ItemOnClickListener implements View.OnClickListener {
//
//            private int position;
//
//            public ItemOnClickListener(int position) {
//                this.position = position;
//            }
//
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.history_orders_item_cardview:
//                        Intent intent;
//                        if (mList.get(position).getTASK_STATE() == TaskState.DELAY ||
//                                mList.get(position).getTASK_STATE() == TaskState.BACK) {
//                            intent = new Intent(HistoryOrdersActivity.this, DelayOrBackOrderActivity.class);
//                            intent.putExtra(Constant.ORIGIN, ORIGIN);
//                            intent.putExtra(Constant.DUMyTask, mList.get(position));
//                            intent.putExtra(Constant.DUHistoryTask, mList.get(position));
//                            intent.putExtra(Constant.DATA_IS_UPLOAD, mList.get(position).getUPLOAD_FLAG());
//                            intent.putExtra(Constant.TASKSTATE, mList.get(position).getTASK_STATE());
//                            intent.putExtra(Constant.TASK_ID, mList.get(position).getTASK_ID());
//                            intent.putExtra(Constant.TASK_REPLY, mList.get(position).getTASK_REPLY());
//                            intent.putExtra(DUHistoryTask.DUHistoryTask_ID, mList.get(position).getID());
//                            startActivity(intent);
//                        } else if (mList.get(position).getTASK_STATE() == TaskState.HANDLE ||
//                                mList.get(position).getTASK_STATE() == TaskState.FINISH) {
//                            intent = new Intent(HistoryOrdersActivity.this, HandleOrderActivity.class);
//                            intent.putExtra(Constant.ORIGIN, ORIGIN);
//                            intent.putExtra(Constant.DUMyTask, mList.get(position).getDuMyTask());
//                            intent.putExtra(Constant.DUHistoryTask, mList.get(position));
//                            intent.putExtra(Constant.DATA_IS_UPLOAD, mList.get(position).getUPLOAD_FLAG());
//                            intent.putExtra(Constant.TASK_ID, mList.get(position).getTASK_ID());
//                            intent.putExtra(Constant.TASK_REPLY, mList.get(position).getTASK_REPLY());
//                            intent.putExtra(DUHistoryTask.DUHistoryTask_ID, mList.get(position).getID());
//                            startActivity(intent);
//                        }
//                        break;
//                }
//            }
//        }
//
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            if (viewType != loading) {
//                View view = LayoutInflater.from(mContext).inflate(R.layout.item_history_orders_row_layout, parent, false);
//                return new ViewHolder(view);
//            } else {
//                View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_order_list_row_loading, parent, false);
//                return new LoadingViewHolder(view);
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return mList.size();
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            if (mList.get(position) != null) {
//                return super.getItemViewType(position);
//            } else {
//                return loading;
//            }
//        }
//
//        /**
//         * 普通-显示数据item
//         */
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            public TextView orderNo;
//            public TextView replyTime;
//            public TextView account;
//            public TextView phone;
//            public TextView address;
//            public TextView issuer;
//            public TextView reflectType;
//            public TextView reflectOrigin;
//            public TextView reflectContent;
//            public TextView handleLimit;
//
//            public CardView cardView;
//            public TextView result;
//            public ImageView dataIsUpload;
//            public TextView dataUploadText;
//            public ImageView photoIsUpload;
//            public TextView photoCount;
//            public ImageView voiceIsUpload;
//            public TextView voiceCount;
//
//            public ViewHolder(View view) {
//                super(view);
//                orderNo = (TextView) view.findViewById(R.id.history_orders_item_tv_order_number);
//                replyTime = (TextView) view.findViewById(R.id.history_orders_item_tv_order_date);
//                account = (TextView) view.findViewById(R.id.history_orders_item_tv_account);
//                phone = (TextView) view.findViewById(R.id.history_orders_item_tv_phone);
//                address = (TextView) view.findViewById(R.id.history_orders_item_tv_address);
//                issuer = (TextView) view.findViewById(R.id.history_orders_item_tv_reflect_person);
//                reflectType = (TextView) view.findViewById(R.id.history_orders_item_tv_reflect_type);
//                reflectOrigin = (TextView) view.findViewById(R.id.history_orders_item_tv_reflect_origin);
//                reflectContent = (TextView) view.findViewById(R.id.history_orders_item_tv_reflect_content);
//                handleLimit = (TextView) view.findViewById(R.id.history_orders_item_tv_handle_limit);
//                cardView = (CardView) view.findViewById(R.id.history_orders_item_cardview);
//                result = (TextView) view.findViewById(R.id.history_orders_item_tv_result);
//                dataIsUpload = (ImageView) view.findViewById(R.id.history_orders_item_iv_data_isupload);
//                dataUploadText = (TextView) view.findViewById(R.id.history_orders_item_tv_data_isupload);
//                photoIsUpload = (ImageView) view.findViewById(R.id.history_orders_item_iv_photo_isupload);
//                photoCount = (TextView) view.findViewById(R.id.history_orders_item_photos_tv_count);
//                voiceCount = (TextView) view.findViewById(R.id.history_orders_item_voice_tv_count);
//                voiceIsUpload = (ImageView) view.findViewById(R.id.history_orders_item_iv_voice_isupload);
//            }
//        }
//
//        /**
//         * 显示loading-progressbar
//         */
//        public class LoadingViewHolder extends RecyclerView.ViewHolder {
//            public ProgressBar progressBar;
//            public TextView textView;
//
//            public LoadingViewHolder(View view) {
//                super(view);
//                progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
//                textView = (TextView) view.findViewById(R.id.textview);
//            }
//        }
//    }

    @Subscribe
    public void onInitResult(UIBusEvent.InitResult initResult) {
        LogUtil.i(TAG, "---onInitResult---" + initResult.isSuccess());
        List<FYLYBean> fylyBeans = GreenDaoUtils.getDaoSession(this).getFYLYBeanDao().loadAll();
        for (int i = 0; i < fylyBeans.size(); i++) {
            map.put(fylyBeans.get(i).getLY_ID(), fylyBeans.get(i).getDESCR());
        }
        if (mAdapter != null) {
            mAdapter.setMap(map);
        }
        if (initResult.isSuccess()) {
            showTopProgressbar(mSwipeRefreshLayout);
            mHistoryOrdersPresenter.loadMyHistoryTasks(true, offset, PAGE_SIZE);
        } else {
            ApplicationsUtil.showMessage(this, R.string.text_init_failure);
        }
    }

    @Subscribe
    public void refreshUI(UIBusEvent.NotifyHistoryTasksUI notifyHistoryTasksUI) {
//        if (mSwipeRefreshLayout.isRefreshing()) {
//            mSwipeRefreshLayout.setRefreshing(false);
//        }
        mHistoryOrdersPresenter.loadMyHistoryTasks(true, 0, PAGE_SIZE);
    }

}
