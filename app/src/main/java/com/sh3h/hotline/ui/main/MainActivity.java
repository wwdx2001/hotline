package com.sh3h.hotline.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.baseadapter.MainAdapter;
import com.sh3h.hotline.entity.MainItemBean;
import com.sh3h.hotline.ui.collection.CollectionTaskActivity;
import com.sh3h.hotline.ui.knowledge.KnowledgeBaseActivity;
import com.sh3h.hotline.ui.nonresident.NonResidentOverrateActivity;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.bill.QueryBillActivity;
import com.sh3h.hotline.ui.notice.NoWaterSupplyNoticeActivity;
import com.sh3h.hotline.ui.order.myorder.history.HistoryOrdersActivity;
import com.sh3h.hotline.ui.order.myorder.list.MyOrderListActivity;
import com.sh3h.hotline.ui.order.self.create.CreateSelfOrderNewActivity;
import com.sh3h.hotline.ui.order.self.history.CreateSelfOrderHistoryActivity;
import com.sh3h.hotline.ui.setting.SettingActivity;
import com.sh3h.mobileutil.util.TextUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends ParentActivity implements MainMvpView, BaseQuickAdapter.OnItemClickListener {

    @Inject
    MainPresenter mPresenter;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private MainAdapter mAdapter;
    private Unbinder mUnbinder;
    //    public static int[] icons = {R.mipmap.ic_home_mytask, R.mipmap.ic_home_historytask,R.mipmap.ic_feiju_chaodinge,
//            R.mipmap.ic_home_create_self_order, R.mipmap.ic_home_create_self_order_history
//            , R.mipmap.ic_home_query_bill, R.mipmap.ic_home_no_water_supply_notice, R.mipmap.ic_home_knowledge_base
//            , R.mipmap.ic_home_setting,R.mipmap.ic_home_collection_task};
    public static int[] icons = {R.mipmap.ic_feiju_chaodinge};
    private String[] titles;
    private List<MainItemBean> mMainItemBeans;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //进入首页时先判断用户是否登陆过，没有登陆就跳到登陆页面
        String account = SPUtils.getInstance("user").getString("account", "");
        String passWord = SPUtils.getInstance("user").getString("passWord", "");
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(passWord)) {
          Toast.makeText(this, "账号密码验证不完整，需重新登陆", Toast.LENGTH_SHORT);
          Intent intent = new Intent(this, LoginActivity.class);
          startActivity(intent);
          finish();
        }

        setContentView(R.layout.activity_main);
        getActivityComponent().inject(this);
        mPresenter.attachView(this);
        mUnbinder = ButterKnife.bind(this);
        initMainToolBar(R.string.app_name);

        mMainItemBeans = new ArrayList<>();
        titles = getResources().getStringArray(R.array.main_names);
        for (int i = 0; i < icons.length; i++) {
            MainItemBean itemBean = new MainItemBean();
            itemBean.setIcon(icons[i]);
            itemBean.setName(titles[i]);
            mMainItemBeans.add(itemBean);
        }
        mAdapter = new MainAdapter(R.layout.item_main, mMainItemBeans);
        mAdapter.setOnItemClickListener(this);
        mRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        mRecycler.setAdapter(mAdapter);
//        mPresenter.initUser();
        //startSyncService(SyncType.DOWNLOAD_ALL_TASK.ordinal());
        //startSyncService(SyncType.DOWNLOAD_WORDS.ordinal());

        //startMqttService();
        url2Path("http://128.1.3.40:8085/api/update/app/meterreading/MeterreadingManager_Apk_V48.zip");

    }


    private String url2Path(String url) {
        if (TextUtil.isNullOrEmpty(url)) {
            return null;
        }

        int start = url.lastIndexOf("/") + 1;
        if (start <= 0) {
            return null;
        }

        String name = url.substring(start);
        if (TextUtil.isNullOrEmpty(name)) {
            return null;
        }

        int end = name.indexOf("_");
        if (end <= 0) {
            return null;
        }

        name = name.substring(0, end);
        if (TextUtil.isNullOrEmpty(name)) {
            return null;
        }

        return "";

//        File file = new File(mConfigHelper.getApksFolderPath(), name + ".apk");
//        if (file.exists()) {
//            return file.getPath();
//        }
//        file = new File(mConfigHelper.getApksFolderPath(), name + ".patch");
//        return patchApk(file);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mPresenter.detachView();
    }

    @Override
    public boolean isCehua() {
        return false;
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_search:
                //Toast.makeText(MainActivity.this, R.string.menu_search, Toast.LENGTH_SHORT).show();
//                Snackbar.make(mShackContainer, R.string.menu_search, Snackbar.LENGTH_SHORT).show();
//                break;
//            case R.id.action_notification:
                //Toast.makeText(MainActivity.this, R.string.menu_notifications, Toast.LENGTH_SHORT).show();
//                break;
            case R.id.action_settings:
//                Toast.makeText(MainActivity.this, R.string.menu_settings, Toast.LENGTH_SHORT).show();
//                Snackbar.make(mShackContainer, R.string.menu_settings, Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
//            case R.id.action_about:
                //Toast.makeText(MainActivity.this, R.string.menu_about_us, Toast.LENGTH_SHORT).show();
//                Snackbar.make(mShackContainer, R.string.menu_about_us, Snackbar.LENGTH_SHORT).show();
          default:
            throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        Intent mIntent = null;
        switch (i) {
            case 0:
                mIntent = new Intent(this, NonResidentOverrateActivity.class);
//                mIntent = new Intent(this, MyOrderListActivity.class);
                break;
//            case 1:
//                mIntent = new Intent(this, HistoryOrdersActivity.class);
//                break;
//            case 2:
//                mIntent = new Intent(this, NonResidentOverrateActivity.class);
//                break;
//            case 3:
//                mIntent = new Intent(this, CreateSelfOrderNewActivity.class);
//                break;
//            case 4:
//                mIntent = new Intent(this, CreateSelfOrderHistoryActivity.class);
//                break;
//            case 5:
//                mIntent = new Intent(this, QueryBillActivity.class);
//                break;
//            case 6:
//                mIntent = new Intent(this, NoWaterSupplyNoticeActivity.class);
//                break;
//            case 7:
//                mIntent = new Intent(this, KnowledgeBaseActivity.class);
//                break;
//            case 8:
//                mIntent = new Intent(this, SettingActivity.class);
//                break;
//            case 9:
//                mIntent = new Intent(this, CollectionTaskActivity.class);
//                break;
            default:
                break;
        }
        if (mIntent != null) {
            startActivity(mIntent);
        }
    }

//    @OnClick(R.id.btn_billinquire)
//    public void billInquire(View view) {
//        Intent intent = new Intent(this, QueryBillActivity.class);
//        startActivity(intent);
//    }
//
//    @OnClick(R.id.btn_createselforder)
//    public void createSelfOrder(View view) {
//        Intent intent = new Intent(this, CreateSelfOrderActivity.class);
//        startActivity(intent);
//    }
//
//    @OnClick(R.id.btn_createselforderhistory)
//    public void createSelfOrderHistory(View view) {
//        Intent intent = new Intent(this, CreateSelfOrderHistoryActivity.class);
//        startActivity(intent);
//    }
//
//    @OnClick(R.id.btn_knowledgebase)
//    public void knowledgeBase(View view) {
//        Intent intent = new Intent(this, KnowledgeBaseActivity.class);
//        startActivity(intent);
//    }
//
//    @OnClick(R.id.btn_myorderList)
//    public void myOrderList(View view) {
//        Intent intent = new Intent(this, MyOrderListActivity.class);
//        startActivity(intent);
//    }
//
//    @OnClick(R.id.btn_historyorder)
//    public void historyOrder(View view) {
//        Intent intent = new Intent(this, HistoryOrdersActivity.class);
//        startActivity(intent);
//    }
//
//    @OnClick(R.id.btn_waternotice)
//    public void showNotice(View view) {
//        Intent intent = new Intent(this, NoWaterSupplyNoticeActivity.class);
//        startActivity(intent);
//    }
//
//    @OnClick(R.id.btn_queryorders)
//    public void queryOrders(View view) {
//        Intent intent = new Intent(this, QueryOrderActivity.class);
//        startActivity(intent);
//    }
//
//    @OnClick(R.id.btn_setting)
//    public void jumpSetting(View view) {
//        Intent intent = new Intent(this, SettingActivity.class);
//        startActivity(intent);
//    }

    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((secondTime - firstTime) < 2000) {
//                stopService(mServiceIntent);
                finish();
//                System.exit(0);
            } else {
                ToastUtils.showShort("再按一次退出");
                firstTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
