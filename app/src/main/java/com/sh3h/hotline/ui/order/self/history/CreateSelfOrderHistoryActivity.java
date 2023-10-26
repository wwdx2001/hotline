package com.sh3h.hotline.ui.order.self.history;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.newentity.ZikaidanJLEntity;
import com.sh3h.dataprovider.data.entity.response.CLJBBean;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.DUMyTaskDao;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.CreateSelfOrderHistoryRecycleViewAdapter.OnItemClickListener;
import com.sh3h.hotline.adapter.ZikaidanJLAdapter;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.order.myorder.history.HistoryOrdersActivity;
import com.sh3h.hotline.ui.order.self.detail.CreateSelfOrderDetailActivity;
import com.sh3h.hotline.ui.order.self.detail.ZikaidanJLDetail;
import com.sh3h.hotline.util.CommonUtils;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;

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

/**
 * Created by dengzhimin on 2016/9/14.
 */
public class CreateSelfOrderHistoryActivity extends ParentActivity
        implements CreateSelfOrderHistoryMvpView, OnItemClickListener, BaseQuickAdapter.OnItemClickListener {
    public final static String TAG = "CreateSelfOrderHistoryActivity";
    /**
     * 每次加载的工单数量
     **/
    public final static int LOAD_COUNT_DEFAULT = 10;
    /**
     * 默认偏移量
     **/
    public final static int OFFSET_DEFAULT = 0;

    @Inject
    CreateSelfOrderHistoryPresenter mPresenter;

    @Inject
    Bus mEventBus;

    @BindView(R.id.container)
    CoordinatorLayout mContainer;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mRefreshView;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.btn_floating)
    FloatingActionButton mBtnFloating;

    private Unbinder mUnbinder;
    //    private CreateSelfOrderHistoryRecycleViewAdapter mRecycleViewAdapter;
    private LinearLayoutManager mLayoutManager;

    private List<DUHistoryTask> mHistoryTasks;

    private ZikaidanJLAdapter mZikaidanJLAdapter;
    private List<ZikaidanJLEntity> mZikaidanJLEntityList;

    private int mOffset;
    private boolean isLoading;
    private boolean isLoadingMoreValid;
    private Toolbar mToolbar;

    public CreateSelfOrderHistoryActivity() {
        mOffset = 0;
        isLoading = false;
        isLoadingMoreValid = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createselforderhistory);
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);
        mPresenter.attachView(this);
        mEventBus.register(this);

        mToolbar = initToolBar(R.string.activity_createselforderhistory);
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

    private void initView() {
        mPresenter.loadData(0, 0);
        mRefreshView.setColorSchemeResources(android.R.color.holo_blue_light);
        mRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mPresenter.loadData(0, 0);
                //刷新当前页面加载的所有数据
//                mPresenter.loadData(OFFSET_DEFAULT, mAllLoadCount);
//                uploadCreateSelfOrderList();
//                if (PermissionUtils.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    GreenDaoUtils.getAsyncSession(CreateSelfOrderHistoryActivity.this)
//                            .runInTx(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mZikaidanJLEntityList = GreenDaoUtils.getDaoSession(CreateSelfOrderHistoryActivity.this)
//                                            .getZikaidanJLEntityDao().loadAll();
//                                    if (mZikaidanJLAdapter != null) {
//                                        mZikaidanJLAdapter.setNewData(mZikaidanJLEntityList);
//                                    }
//                                    mRefreshView.setRefreshing(false);
//                                }
//                            });
//                }
            }
        });

        mBtnFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //scroll to top
                mRecyclerView.smoothScrollToPosition(0);
            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        List<CLJBBean> cljbBeans =
                GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getCLJBBeanDao().loadAll();
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < cljbBeans.size(); i++) {
            map.put(cljbBeans.get(i).getCLJB_ID(), cljbBeans.get(i).getDESCR());
        }
//        mRecycleViewAdapter = new CreateSelfOrderHistoryRecycleViewAdapter(new ArrayList<DUHistoryTask>(), this);
        mZikaidanJLAdapter = new ZikaidanJLAdapter(R.layout.item_zikaidanjl, new ArrayList<ZikaidanJLEntity>(), map);
//        mRecycleViewAdapter.setOnItemClickListener(this);
        mZikaidanJLAdapter.setOnItemClickListener(this);
//        mRecyclerView.setAdapter(mRecycleViewAdapter);
        mRecyclerView.setAdapter(mZikaidanJLAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLayoutManager.findFirstVisibleItemPosition() > LOAD_COUNT_DEFAULT) {
                    mBtnFloating.setVisibility(View.VISIBLE);
                } else {
                    mBtnFloating.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                if ((mLayoutManager.findLastVisibleItemPosition() >= mHistoryTasks.size() - 1)
//                        && isLoadingMoreValid
//                        && (!isLoading)
//                        && dy > 0) {
////                    loadMore();
//                }
            }
        });
//        if (PermissionUtils.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            GreenDaoUtils.getAsyncSession(CreateSelfOrderHistoryActivity.this)
//                    .runInTx(new Runnable() {
//                        @Override
//                        public void run() {
//                            mZikaidanJLEntityList = GreenDaoUtils.getDaoSession(CreateSelfOrderHistoryActivity.this)
//                                    .getZikaidanJLEntityDao().loadAll();
//                            if (mZikaidanJLEntityList != null) {
//                                mZikaidanJLAdapter = new ZikaidanJLAdapter(R.layout.item_zikaidanjl, mZikaidanJLEntityList);
//                                mRecyclerView.setAdapter(mZikaidanJLAdapter);
//                                mZikaidanJLAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
//                                        Intent intent = new Intent(CreateSelfOrderHistoryActivity.this,
//                                                ZikaidanJLDetail.class);
//                                        intent.putExtra(Constant.ZIKAIDAN_JL, mZikaidanJLAdapter.getData()
//                                                .get(i));
//                                        startActivity(intent);
//                                    }
//                                });
//                            }
//                        }
//                    });
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mPresenter.detachView();
        mEventBus.unregister(this);
    }

    @Override
    public void onLoadData(List<DUHistoryTask> duHistoryTasks) {
        LogUtil.i(TAG, "onLoadData");
//        hideTopProgressbar(mRefreshView);
//
//        if (duHistoryTasks != null) {
//            int unReadRw = 0;
//            for (DUHistoryTask duHistoryTask : duHistoryTasks) {
//                if (duHistoryTask.getTASK_UPLOAD_FLAG() == Constant.NO_UPLOAD) {
//                    unReadRw++;
//                }
//            }
//
//            try {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put(MyModule.PACKAGE_NAME, getApplicationContext().getPackageName());
//                jsonObject.put(MyModule.ACTIVITY_NAME, CreateSelfOrderHistoryActivity.class.getName());
//                JSONArray subJSONArray = new JSONArray();
//                subJSONArray.put("count#" + unReadRw);
//                jsonObject.put(MyModule.DATA, subJSONArray);
//                MyModule myModule = new MyModule(jsonObject.toString());
//                MainApplication.get(getApplicationContext()).setMyModule(myModule);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        if ((duHistoryTasks == null) || (duHistoryTasks.size() <= 0)) {
//            LogUtil.i(TAG, "onLoadData: no data");
//            return;
//        }
//        mHistoryTasks = duHistoryTasks;
//        mRecycleViewAdapter.setData(duHistoryTasks);
//        mRecycleViewAdapter.notifyDataSetChanged();
        mToolbar.setSubtitle("记录数:" + mHistoryTasks.size() + "");
    }

    @Override
    public void onLoadDataSuccess(List<ZikaidanJLEntity> duHistoryTasks) {
        hideTopProgressbar(mRefreshView);
        mZikaidanJLAdapter.setNewData(duHistoryTasks);
    }

    @Override
    public void onLoadMoreData(List<DUHistoryTask> duHistoryTasks) {
        LogUtil.i(TAG, "onLoadMoreData");

//        isLoading = false;
//        if ((mHistoryTasks.size() > 0)
//                && (mHistoryTasks.get(mHistoryTasks.size() - 1) == null)) {
//            mHistoryTasks.remove(mHistoryTasks.size() - 1);
//            mRecycleViewAdapter.notifyItemRemoved(mHistoryTasks.size());
//        }
//
//        if ((duHistoryTasks == null) || (duHistoryTasks.size() <= 0)) {
//            LogUtil.i(TAG, "onLoadMoreData: duHistoryTasks has no item");
//            return;
//        }
//
//        isLoadingMoreValid = true;
//        mHistoryTasks.addAll(duHistoryTasks);
//        mRecycleViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String resId) {
        hideTopProgressbar(mRefreshView);
        ApplicationsUtil.showMessage(this, resId);
    }

    @Override
    public void onItemClick(View v, Object o) {
        if (o instanceof DUHistoryTask) {
            Intent intent = new Intent(CreateSelfOrderHistoryActivity.this, CreateSelfOrderDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constant.DU_HISTORY_TASK, (DUHistoryTask) o);
            bundle.putBoolean(Constant.REPLY_IMMEDIATELY, ((DUHistoryTask) o).isReplyImmediately());
            intent.putExtra(Constant.ISSUE_TYPE, ((DUHistoryTask) o).getISSUE_TYPE());//处理类别
            intent.putExtra(Constant.ISSUE_CONTENT, ((DUHistoryTask) o).getISSUE_CONTENT());//处理内容
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        Intent intent = new Intent(CreateSelfOrderHistoryActivity.this,
                ZikaidanJLDetail.class);
        intent.putExtra(Constant.ZIKAIDAN_JL, mZikaidanJLAdapter.getData()
                .get(i));
        startActivity(intent);
    }

//    private void loadMore() {
//        isLoading = true;
//        isLoadingMoreValid = false;
//        mHistoryTasks.add(null);//显示加载中的布局,对应MyAdapter的getItemViewType()方法
//        mRecycleViewAdapter.notifyItemInserted(mHistoryTasks.size() - 1);
//        mOffset += LOAD_COUNT_DEFAULT;
//        mPresenter.loadMoreData(mOffset, LOAD_COUNT_DEFAULT);
//    }

    @Subscribe
    public void onInitResult(UIBusEvent.InitResult initResult) {
        LogUtil.i(TAG, "---onInitResult---" + initResult.isSuccess());
        if (initResult.isSuccess()) {
            // 每次获取焦点时更新数据
            // 界面创建获取焦点时加载数量为 LOAD_COUNT_DEFAULT
            // 重新获取焦点时加载当前界面已有的数据 mAllLoadCount
//            showTopProgressbar(mRefreshView);
//            mPresenter.loadData(mOffset, LOAD_COUNT_DEFAULT);

            String key = TimeUtils.getNowString(CommonUtils.getFormat("yyyy-MM-dd"));
            String account = SPUtils.getInstance().getString(Constant.USERID);
            String tips = SPUtils.getInstance(account + "_SaveOrdersTips").getString("Tips","");
            boolean isTipsLimit = SPUtils.getInstance(account + "_SaveOrdersTips").getBoolean("IsTipsLimit",false);
            if(isTipsLimit){
                if(!(ActivityUtils.getTopActivity() instanceof HistoryOrdersActivity)){
                    showSaveOrdersTips();
                }
            }else{
                if (!tips.equals(key)) {
                    if(!(ActivityUtils.getTopActivity() instanceof HistoryOrdersActivity)){
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

//    @Subscribe
//    public void refreshUI(UIBusEvent.NotifyHistoryTasksUI notifyHistoryTasksUI) {
////        if (mRefreshView.isRefreshing()) {
////            mRefreshView.setRefreshing(false);
////        }
//        mPresenter.loadData(0, LOAD_COUNT_DEFAULT);
//    }
}
