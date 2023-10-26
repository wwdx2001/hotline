package com.sh3h.hotline.ui.collection;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hjq.toast.ToastParams;
import com.hjq.toast.Toaster;
import com.hjq.toast.style.CustomToastStyle;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.DUMyTaskDao;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.CollectionTaskAdapter;
import com.sh3h.hotline.entity.CollectionTaskBean;
import com.sh3h.hotline.entity.ReceiptListEntity;
import com.sh3h.hotline.event.NotifRefrashDataEvent;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.order.myorder.history.HistoryOrdersActivity;
import com.sh3h.hotline.util.CommonUtils;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CollectionTaskActivity extends ParentActivity {
    private static final String TAG = "CreateSelfOrderNewActivity";

    @Inject
    DataManager mDataManager;

    @Inject
    Bus mEventBus;

    @BindView(R.id.collection_task_list_recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.synchronize_data_swiperefresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private CollectionTaskAdapter mAdapter;
    private Unbinder mUnbinder;
    private List<CollectionTaskBean> mCollectionTaskItenBean;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_task_list);
        getActivityComponent().inject(this);
        mEventBus.register(this);
        mUnbinder = ButterKnife.bind(this);

        initToolBar(R.string.collection_task);

        initView();
        //initData();

        Intent intent = getIntent();
        if (savedInstanceState != null) {
            initParams(savedInstanceState);
        } else if (intent != null) {
            initParams(intent.getExtras());
        } else {
            initParams(null);
        }
        checkPermissions();
    }

    private void initView() {
        //刷新控件
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                notifyData();
            }
        };

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);
        mSwipeRefreshLayout.setOnRefreshListener(listener);

        mCollectionTaskItenBean = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new CollectionTaskAdapter(CollectionTaskActivity.this, mRecyclerView);
        mAdapter.setDataSource(mCollectionTaskItenBean);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
//    for (int i = 0; i < 20; i++) {
//      CollectionTaskBean collectionTaskItenBean = new CollectionTaskBean();
//      collectionTaskItenBean.setCbh("CB111" + i);
//      collectionTaskItenBean.setYiwancheng(i);
//      collectionTaskItenBean.setLinqi(i);
//      collectionTaskItenBean.setWeiwancheng(i);
//      collectionTaskItenBean.setGongdanshu(20);
//      mCollectionTaskItenBean.add(collectionTaskItenBean);
//    }
//    mAdapter.notifyDataSetChanged();

        mCollectionTaskItenBean.clear();
        EasyHttp
                .post(URL.CuiJIaoGDSelCount)
                .params("cbyid", mDataManager.getAccount())
//                .params("cbyid", "111032")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new CallBackProxy<CustomApiResult<List<CollectionTaskBean>>,
                        List<CollectionTaskBean>>(new CustomCallBack<List<CollectionTaskBean>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showTopProgressbar(mSwipeRefreshLayout);
                    }

                    @Override
                    public void onError(ApiException e) {
                        hideTopProgressbar(mSwipeRefreshLayout);
//                        hideProgress();
                        onCompleted();
//                        llTab.setVisibility(View.GONE);
                        if (e.getCode() == 1010) {
//                            ToastUtils.showShort("没有数据");
                            ToastParams params = new ToastParams();
                            params.text = "没有数据";
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                        } else {
//                            ToastUtils.showShort(e.getMessage());
                            ToastParams params = new ToastParams();
                            params.text = e.getMessage();
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                        }

                    }

                    @Override
                    public void onSuccess(final List<CollectionTaskBean> mNetWorkDatas) {
//                        hideProgress();
                        hideTopProgressbar(mSwipeRefreshLayout);
                        LogUtils.e(mNetWorkDatas.toString());
                        if (mNetWorkDatas != null && mNetWorkDatas.size() > 0) {
                            mCollectionTaskItenBean.addAll(mNetWorkDatas);
                            mAdapter.notifyDataSetChanged();
                        } else {
//                            ToastUtils.showShort("没有数据");
                            ToastParams params = new ToastParams();
                            params.text = "没有数据";
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                        }

                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
//                        hideProgress();
                        hideTopProgressbar(mSwipeRefreshLayout);
                    }
                }) {
                });
    }

    private void notifyData() {
        mCollectionTaskItenBean.clear();
        EasyHttp
                .post(URL.CuiJIaoGDSelCount)
                .params("cbyid", mDataManager.getAccount())
//                .params("cbyid", "111032")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new CallBackProxy<CustomApiResult<List<CollectionTaskBean>>,
                        List<CollectionTaskBean>>(new CustomCallBack<List<CollectionTaskBean>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showTopProgressbar(mSwipeRefreshLayout);
                    }

                    @Override
                    public void onError(ApiException e) {
                        hideTopProgressbar(mSwipeRefreshLayout);
                        onCompleted();
//                        llTab.setVisibility(View.GONE);
                        if (e.getCode() == 1010) {
//                            ToastUtils.showShort("没有数据");
                            ToastParams params = new ToastParams();
                            params.text = "没有数据";
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                        } else {
//                            ToastUtils.showShort(e.getMessage());
                            ToastParams params = new ToastParams();
                            params.text = e.getMessage();
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                        }

                    }

                    @Override
                    public void onSuccess(final List<CollectionTaskBean> mNetWorkDatas) {
                        hideTopProgressbar(mSwipeRefreshLayout);
                        LogUtils.e(mNetWorkDatas.toString());
                        if (mNetWorkDatas != null && mNetWorkDatas.size() > 0) {
                            mCollectionTaskItenBean.addAll(mNetWorkDatas);
                            mAdapter.notifyDataSetChanged();
                        } else {
//                            ToastUtils.showShort("没有数据");
                            ToastParams params = new ToastParams();
                            params.text = "没有数据";
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        hideTopProgressbar(mSwipeRefreshLayout);
                    }
                }) {
                });
    }

    @Subscribe
    public void onInitResult(UIBusEvent.InitResult initResult) {
        LogUtil.i(TAG, "---onInitResult---" + initResult.isSuccess());
        if (initResult.isSuccess()) {
            initData();

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
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
        mUnbinder.unbind();
    }

    @Subscribe
    public void onNotifRefrashDataEvent(NotifRefrashDataEvent event) {
        notifyData();
    }
}
