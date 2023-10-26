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
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
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
import com.sh3h.hotline.adapter.baseadapter.BaseFragmentPagerAdapter;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.service.SyncConst;
import com.sh3h.hotline.service.UploadDataService;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.sh3h.hotline.ui.bill.detail.UserDetailInformationActivity;
import com.sh3h.hotline.ui.order.TaskState;
import com.sh3h.hotline.ui.order.myorder.delayorback.DelayOrBackOrderActivity;
import com.sh3h.hotline.ui.order.myorder.handle.HandleOrderActivity;
import com.sh3h.hotline.ui.order.myorder.list.MyOrderListActivity;
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
public class HistoryOrdersActivity extends ParentActivity implements HistoryOrdersMvpView, ViewPager.OnPageChangeListener, SearchView.OnQueryTextListener {
    @Inject
    Bus mEventBus;

    @Inject
    HistoryOrdersPresenter mHistoryOrdersPresenter;

    @Inject
    ConfigHelper mConfigHelper;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tablayout)
    TabLayout mTabLayout;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    private final static String TAG = "HistoryOrdersActivity";
    public final static int ORIGIN = Constant.ORIGIN_MY_TASK_HISTORY;
    private Unbinder mUnbinder;

    private MenuItem searchItem;
    private BaseFragmentPagerAdapter mPagerAdapter;
    private List<Fragment> mFragmentList;
    private HistorySaveOrdersFragment saveOrdersFragment;
    private HistoryUploadOrdersFragment uploadOrdersFragment;

    private int offset = 0;
    private String currentType = "";
    private int currentPossion = 0;
    private String type;
    private boolean isDelineTask;
    private String[] mContents;
    private String fylxParams;
    private String fylxContent;
    private Map<String, String> map = new HashMap<>();

    public HistoryOrdersActivity() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order_list);
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

    /*
     * 初始化控件
     */
    private void initView() {

    }

    private void initViewPager() {
        mFragmentList = new ArrayList<>();

        saveOrdersFragment = HistorySaveOrdersFragment.newInstance("", "");
        uploadOrdersFragment = HistoryUploadOrdersFragment.newInstance("", "");
        mFragmentList.add(saveOrdersFragment);
        mFragmentList.add(uploadOrdersFragment);

        mPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(2);
    }

    private void initTabLayout() {
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setText(R.string.title_task_save);
        mTabLayout.getTabAt(1).setText(R.string.title_task_upload);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history_orders_list, menu);
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
                if(currentPossion == 1){
                    if(uploadOrdersFragment != null) {
                        uploadOrdersFragment.optionsItemSelected(item);
                    }
                }else{
                    if(saveOrdersFragment != null) {
                        saveOrdersFragment.optionsItemSelected(item);
                    }
                }
                break;
            case R.id.action_upload_tasks://未上传工单
                LogUtil.i(TAG, "uploadTasks");
                currentType = getResources().getString(R.string.menu_upload_tasks);
                setToolbarTitle(getResources().getString(R.string.menu_upload_tasks));
                type = getResources().getString(R.string.menu_upload_tasks);
                isDelineTask = false;
                if(currentPossion == 1){
                    if(uploadOrdersFragment != null) {
                        uploadOrdersFragment.optionsItemSelected(item);
                    }
                }else{
                    if(saveOrdersFragment != null) {
                        saveOrdersFragment.optionsItemSelected(item);
                    }
                }
                break;
            case R.id.action_deadline_tasks://临期工单
                LogUtil.i(TAG, "deadlineTasks");
                currentType = getResources().getString(R.string.menu_deadline_tasks);
                setToolbarTitle(getResources().getString(R.string.menu_deadline_tasks));
                type = getResources().getString(R.string.menu_deadline_tasks);
                isDelineTask = true;
                if(currentPossion == 1){
                    if(uploadOrdersFragment != null) {
                        uploadOrdersFragment.optionsItemSelected(item);
                    }
                }else{
                    if(saveOrdersFragment != null) {
                        saveOrdersFragment.optionsItemSelected(item);
                    }
                }
                break;
            case R.id.action_expired_task://超期工单
                currentType = getResources().getString(R.string.menu_expired_tasks);
                setToolbarTitle(getResources().getString(R.string.menu_expired_tasks));
                type = getResources().getString(R.string.menu_expired_tasks);
                isDelineTask = false;
                if(currentPossion == 1){
                    if(uploadOrdersFragment != null) {
                        uploadOrdersFragment.optionsItemSelected(item);
                    }
                }else{
                    if(saveOrdersFragment != null) {
                        saveOrdersFragment.optionsItemSelected(item);
                    }
                }
                break;
            case R.id.action_noexpired_task://未超期工单
                currentType = getResources().getString(R.string.menu_noexpired_tasks);
                setToolbarTitle(getResources().getString(R.string.menu_noexpired_tasks));
                type = getResources().getString(R.string.menu_noexpired_tasks);
                isDelineTask = false;
                if(currentPossion == 1){
                    if(uploadOrdersFragment != null) {
                        uploadOrdersFragment.optionsItemSelected(item);
                    }
                }else{
                    if(saveOrdersFragment != null) {
                        saveOrdersFragment.optionsItemSelected(item);
                    }
                }
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
                        if(currentPossion == 1){
                            if(uploadOrdersFragment != null) {
                                uploadOrdersFragment.getFilterTask(fylxContent, fylxParams);
                            }
                        }else{
                            if(saveOrdersFragment != null) {
                                saveOrdersFragment.getFilterTask(fylxContent, fylxParams);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    public void setToolBar(String count){
        mToolbar.setSubtitle(count);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mHistoryOrdersPresenter.detachView();
        mEventBus.unregister(this);
    }

    @Override
    public void onGetHistoryOrdersList(boolean isFirstTime, List<DUMyTask> result) {

    }

    @Override
    public void onFilterOrdersList(boolean isFirstTime, List<DUMyTask> result) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(currentPossion == 1){
            if(uploadOrdersFragment != null) {
                uploadOrdersFragment.onQueryTextSubmit(query);
            }
        }else{
            if(saveOrdersFragment != null) {
                saveOrdersFragment.onQueryTextSubmit(query);
            }
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                currentPossion = 0;
                mToolbar.setTitle(R.string.title_task_save);
                break;
            case 1:
                currentPossion = 1;
                mToolbar.setTitle(R.string.title_task_upload);
                if(uploadOrdersFragment != null) {
                    uploadOrdersFragment.loadMyHistoryTasks(true, 0, PAGE_SIZE, false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onGetSynchronizeData() {
    }

    @Override
    public void showMessage(String message) {
    }

    @Subscribe
    public void onInitResult(UIBusEvent.InitResult initResult) {
        LogUtil.i(TAG, "---onInitResult---" + initResult.isSuccess());
        initViewPager();
        initTabLayout();

        List<FYLYBean> fylyBeans = GreenDaoUtils.getDaoSession(this).getFYLYBeanDao().loadAll();
        for (int i = 0; i < fylyBeans.size(); i++) {
            map.put(fylyBeans.get(i).getLY_ID(), fylyBeans.get(i).getDESCR());
        }

        if(currentPossion == 1){
            if (uploadOrdersFragment != null) {
                uploadOrdersFragment.setMap(map);
            }
        }else{
            if (saveOrdersFragment != null) {
                saveOrdersFragment.setMap(map);
            }
        }

        if (initResult.isSuccess()) {
            if(currentPossion == 1){
                if(uploadOrdersFragment != null) {
                    uploadOrdersFragment.loadMyHistoryTasks(true, offset, PAGE_SIZE, true);
                }
            }else{
                if(saveOrdersFragment != null) {
                    saveOrdersFragment.loadMyHistoryTasks(true, offset, PAGE_SIZE, true);
                }
            }
        } else {
            ApplicationsUtil.showMessage(this, R.string.text_init_failure);
        }
    }

    @Subscribe
    public void refreshUI(UIBusEvent.NotifyHistoryTasksUI notifyHistoryTasksUI) {
        if(currentPossion == 1){
            if(uploadOrdersFragment != null) {
                uploadOrdersFragment.loadMyHistoryTasks(true, 0, PAGE_SIZE, false);
            }
        }else{
            if(saveOrdersFragment != null) {
                saveOrdersFragment.loadMyHistoryTasks(true, 0, PAGE_SIZE, false);
            }
        }
    }

}
