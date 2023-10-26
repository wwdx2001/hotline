package com.sh3h.hotline.ui.collection;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.StringUtils;
import com.hjq.toast.ToastParams;
import com.hjq.toast.Toaster;
import com.hjq.toast.style.CustomToastStyle;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.CollectionTaskListAdapter;
import com.sh3h.hotline.entity.CollectionTaskBean;
import com.sh3h.hotline.entity.CollectionTaskListBean;
import com.sh3h.hotline.event.NotifRefrashDataEvent;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.nonresident.call.OverrateCallNewActivity;
import com.sh3h.hotline.ui.nonresident.receipt.OverrateReceiptNewActivity;
import com.sh3h.hotline.util.DisplayUtils;
import com.sh3h.hotline.view.AddAndSubEditText;
import com.sh3h.hotline.view.PopupWindowFilter;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.exception.ApiException;

import java.lang.reflect.Method;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CollectionTaskListActivity extends ParentActivity implements SearchView.OnQueryTextListener, View.OnClickListener {

    @Inject
    DataManager mDataManager;

    @Inject
    Bus mEventBus;

    @BindView(R.id.collection_task_order_list_recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.synchronize_data_swiperefresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.collection_searchview)
    AddAndSubEditText mSearchView;

    @BindView(R.id.collection_search)
    TextView mSearch;

    @BindView(R.id.collection_reset)
    TextView mReset;

    private MenuItem mSortItem, mWaterAddItem, mMoneyItem, mOrderItem, mSortItem2;
    private CollectionTaskListAdapter mAdapter;
    private Unbinder mUnbinder;
    private List<CollectionTaskListBean> mCollectionTaskListBean;
    private PopupWindow mPopwindow;
    private PopupWindowFilter popWinFilter;
    private Toolbar toolbar;
    private static String filter_sort = "asc";
    private static String filter_match = "xh";
    public String cbh;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_task_order_list);
        getActivityComponent().inject(this);
        mEventBus.register(this);
        mUnbinder = ButterKnife.bind(this);
        toolbar = initToolBar(R.string.collection_task_order);

        initView();
        initData();
        checkPermissions();
    }

    private void initView() {
        mCollectionTaskListBean = new ArrayList<>();
        mSearchView.setHint("请输入账户编号、用水地址或客户名称");
        mSearch.setOnClickListener(this);
        mReset.setOnClickListener(this);
        mSearchView.setOnDrawableRightListener(new AddAndSubEditText.OnDrawableRightListener() {
            @Override
            public void onDrawableRightClick(View view) {
                mSearchView.setText("");
                getdataList();
            }
        });

        //刷新控件
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getdataList();
            }
        };

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);
        mSwipeRefreshLayout.setOnRefreshListener(listener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new CollectionTaskListAdapter(CollectionTaskListActivity.this, mRecyclerView);
        mAdapter.setDataSource(mCollectionTaskListBean);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        cbh = intent.getStringExtra("cbh");
        getdataList();

//    mSearchView.setIconifiedByDefault(false);
    }

    private void getdataList() {

//        List<CollectionTaskListBean> CollectionTaskListBean = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            CollectionTaskListBean collectionTaskListBean = new CollectionTaskListBean();
//            collectionTaskListBean.setXh(i + 1 + "");
//
//            int temp = 0;
//            temp = i * 20;
//            collectionTaskListBean.setQfzje(temp + ".00");
//            collectionTaskListBean.setKhlx("4200-公共管理");
//            if (i == 2 || i == 6) {
//                collectionTaskListBean.setCjlx("CIS发起催缴");
//                collectionTaskListBean.setFwdz("上海市逸仙路");
//            } else if (i == 7 || i == 4) {
//                collectionTaskListBean.setCjlx("抄表员催缴");
//                collectionTaskListBean.setFwdz("下海市逸仙路");
//            } else {
//                collectionTaskListBean.setCjlx("抄表机自开单");
//                collectionTaskListBean.setFwdz("中海市逸仙路");
//            }
//            collectionTaskListBean.setZhbh("123456789");
//            collectionTaskListBean.setKhlx("上海三高计算机中心");
//            CollectionTaskListBean.add(collectionTaskListBean);
//        }
//        return CollectionTaskListBean;

        mCollectionTaskListBean.clear();
        EasyHttp
                .post(URL.CuJIaoGDSel)
                .params("cbyid", mDataManager.getAccount())
//                .params("cbyid", "111032")
                .params("cbh", cbh)
//                .params("token", "F6D62986-915F-4DD8-963A-B0632F-149E18")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new CallBackProxy<CustomApiResult<List<CollectionTaskListBean>>,
                        List<CollectionTaskListBean>>(new CustomCallBack<List<CollectionTaskListBean>>() {
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
                    public void onSuccess(final List<CollectionTaskListBean> mNetWorkDatas) {
                        hideTopProgressbar(mSwipeRefreshLayout);
                        LogUtils.e(mNetWorkDatas.toString());
                        if (mNetWorkDatas != null && mNetWorkDatas.size() > 0) {
                            mCollectionTaskListBean.addAll(mNetWorkDatas);
//                            filter_match = "xh";
                            filterSort();
                            mAdapter.setDataSource(mCollectionTaskListBean);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
        mUnbinder.unbind();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_all_sort:
                showPopupWindow(true);
//                Toast.makeText(this, "点击了第" + 1 + "个", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_address_sort:
                showPopupWindow(false);
//                Toast.makeText(this, "点击了action_address_sort", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collection_task_order_list, menu);
        mSortItem = menu.findItem(R.id.action_all_sort);
        mSortItem2 = menu.findItem(R.id.action_address_sort);

//    addressSortItem.setIcon(R.mipmap.ic_default_sort);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collection_reset:
                mSearchView.setText("");
                getdataList();
                break;
            case R.id.collection_search:
                List<CollectionTaskListBean> mCollectionTaskListBean = selectList(mSearchView.getText().toString());
                mAdapter.setDataSource(mCollectionTaskListBean);
                mAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }


    private void showPopupWindow(boolean collection) {

        if (popWinFilter == null) {
            int paramInt1 = DisplayUtils.dipToPix(this, 160);
            int paramInt2 = DisplayUtils.dipToPix(this, 165);

            //自定义的单击事件
            RadioGroup.OnCheckedChangeListener paramOnCheckedChangeListener = new OnCheckedChangeListener();
            popWinFilter = new PopupWindowFilter(this, paramOnCheckedChangeListener,
                    paramInt1, paramInt2);
            //监听窗口的焦点事件，点击窗口外面则取消显示
            popWinFilter.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        popWinFilter.dismiss();
                    }
                }
            });
        }
        if (collection) {
            popWinFilter.setGone(R.id.radiogroup, true, false);
            popWinFilter.setGone(R.id.view_line, true, true);
            popWinFilter.setGone(R.id.radiogroup2, true, false);
            popWinFilter.setGone(R.id.radiogroup3, false, false);
            popWinFilter.setGone(R.id.radio_filter_yjdz, false);
            popWinFilter.setGone(R.id.radio_filter_hh, false);
            popWinFilter.setGone(R.id.radio_filter_ysdz, true);
            popWinFilter.setGone(R.id.radio_filter_qfje, false);
            popWinFilter.setGone(R.id.radio_filter_je, true);
        } else {
            popWinFilter.setGone(R.id.radiogroup, false, false);
            popWinFilter.setGone(R.id.view_line, false, true);
            popWinFilter.setGone(R.id.radiogroup2, false, false);
            popWinFilter.setGone(R.id.radiogroup3, true, false);
        }

        //设置默认获取焦点
        popWinFilter.setFocusable(true);
        //以某个控件的x和y的偏移量位置开始显示窗口
        popWinFilter.showAsDropDown(toolbar, toolbar.getWidth(), 0);

        //如果窗口存在，则更新
        popWinFilter.update();
    }

    class OnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.radio_filter_asc:
                    //升序
                    filter_sort = "asc";
                    filterSort();
                    mAdapter.setDataSource(mCollectionTaskListBean);
                    mAdapter.notifyDataSetChanged();
                    popWinFilter.dismiss();
                    break;
                case R.id.radio_filter_desc:
                    // 降序"
                    filter_sort = "desc";
                    filterSort();
                    mAdapter.setDataSource(mCollectionTaskListBean);
                    mAdapter.notifyDataSetChanged();
                    popWinFilter.dismiss();
                    break;
                case R.id.radio_filter_je:
                    //金额
                    filter_match = "je";
                    filterSort();
                    mAdapter.setDataSource(mCollectionTaskListBean);
                    mAdapter.notifyDataSetChanged();
                    popWinFilter.dismiss();
                    break;
                case R.id.radio_filter_ysdz:
                    //金额
                    filter_match = "ysdz";
                    filterSort();
                    mAdapter.setDataSource(mCollectionTaskListBean);
                    mAdapter.notifyDataSetChanged();
                    popWinFilter.dismiss();
                    break;
                case R.id.radio_filter_xh:
                    //序号
                    filter_match = "xh";
                    filterSort();
                    mAdapter.setDataSource(mCollectionTaskListBean);
                    mAdapter.notifyDataSetChanged();
                    popWinFilter.dismiss();
                    break;
                case R.id.radio_filter_qb:
                    //全部
                    List<CollectionTaskListBean> mCollectionTaskListBean = selectList("all");
                    mAdapter.setDataSource(mCollectionTaskListBean);
                    mAdapter.notifyDataSetChanged();
                    popWinFilter.dismiss();
                    break;
                case R.id.radio_filter_cbycj:
                    //抄表员催缴
                    List<CollectionTaskListBean> mCollectionTaskListBean1 = selectList("cbycj");
                    mAdapter.setDataSource(mCollectionTaskListBean1);
                    mAdapter.notifyDataSetChanged();
                    popWinFilter.dismiss();
                    break;
                case R.id.radio_filter_cisfqcj:
                    //CIS
                    List<CollectionTaskListBean> mCollectionTaskListBean2 = selectList("cisfqcj");
                    mAdapter.setDataSource(mCollectionTaskListBean2);
                    mAdapter.notifyDataSetChanged();
                    popWinFilter.dismiss();
                    break;
                case R.id.radio_filter_cbjzkd:
                    //金额
                    List<CollectionTaskListBean> mCollectionTaskListBean3 = selectList("cbjzkd");
                    mAdapter.setDataSource(mCollectionTaskListBean3);
                    mAdapter.notifyDataSetChanged();
                    popWinFilter.dismiss();
                    break;
                default:
                    break;
            }
        }
    }

    private List<CollectionTaskListBean> selectList(String type) {
        List<CollectionTaskListBean> CollectionTaskListBean = new ArrayList<>(mCollectionTaskListBean);
        Iterator<CollectionTaskListBean> iter = CollectionTaskListBean.iterator();
        if (type.equals("all")) {
        } else if (type.equals("cbycj")) {
            while (iter.hasNext()) {
                CollectionTaskListBean collectionTaskListBean = iter.next();
                if (collectionTaskListBean.getCjly() != null) {
                    if (!collectionTaskListBean.getCjly().contains("抄表员催缴")) {
                        iter.remove();
                    }
                } else {
                    iter.remove();
                }
            }
        } else if (type.equals("cisfqcj")) {
            while (iter.hasNext()) {
                CollectionTaskListBean collectionTaskListBean = iter.next();
                if (collectionTaskListBean.getCjly() != null) {
                    if (!collectionTaskListBean.getCjly().contains("CIS")) {
                        iter.remove();
                    }
                } else {
                    iter.remove();
                }
            }
        } else if (type.equals("cbjzkd")) {
            while (iter.hasNext()) {
                CollectionTaskListBean collectionTaskListBean = iter.next();
                if (collectionTaskListBean.getCjly() != null) {
                    if (!collectionTaskListBean.getCjly().contains("自开单")) {
                        iter.remove();
                    }
                } else {
                    iter.remove();
                }
            }
        } else {
            while (iter.hasNext()) {
                CollectionTaskListBean collectionTaskListBean = iter.next();
                if ((collectionTaskListBean.getCjly() != null && collectionTaskListBean.getCjly().contains(type))
                        || (collectionTaskListBean.getFwdz() != null && collectionTaskListBean.getFwdz().contains(type))
                        || (collectionTaskListBean.getXh() != null && String.valueOf(collectionTaskListBean.getXh()).contains(type))
                        || (collectionTaskListBean.getQfzje() != null && collectionTaskListBean.getQfzje().contains(type))
                        || (collectionTaskListBean.getKhlx() != null && collectionTaskListBean.getKhlx().contains(type))
                        || (collectionTaskListBean.getKhmc() != null && collectionTaskListBean.getKhmc().contains(type))
                        || (collectionTaskListBean.getZhbh() != null && collectionTaskListBean.getZhbh().contains(type))) {

                } else {
                    iter.remove();
                }
            }
        }
        return CollectionTaskListBean;
    }

    private void filterSort() {
        Collections.sort(mCollectionTaskListBean, new Comparator<CollectionTaskListBean>() {
            @Override
            public int compare(CollectionTaskListBean o1, CollectionTaskListBean o2) {
                if (filter_sort.equals("asc")) {
                    if (filter_match.equals("je")) {
                        return Double.compare(Double.parseDouble(StringUtils.isEmpty(o1.getQfzje()) ? "0.00" : o1.getQfzje()),
                                Double.parseDouble(StringUtils.isEmpty(o2.getQfzje()) ? "0.00" : o2.getQfzje()));
                    } else if (filter_match.equals("ysdz")) {
//                        return o1.getFwdz().compareTo(o2.getFwdz());
                        Comparator<Object> cm = Collator.getInstance(Locale.CHINA);
                        return cm.compare(o1.getFwdz(), o2.getFwdz());
                    } else {
                        return Integer.parseInt(StringUtils.isEmpty(o1.getXh()) ? "1" : o1.getXh())
                                - Integer.parseInt(StringUtils.isEmpty(o2.getXh()) ? "1" : o2.getXh());
                    }
                } else {
                    if (filter_match.equals("je")) {
                        return Double.compare(Double.parseDouble(StringUtils.isEmpty(o2.getQfzje()) ? "0.00" : o2.getQfzje()),
                                Double.parseDouble(StringUtils.isEmpty(o1.getQfzje()) ? "0.00" : o1.getQfzje()));
                    } else if (filter_match.equals("ysdz")) {
//                        return o2.getFwdz().compareTo(o1.getFwdz());
                        Comparator<Object> cm = Collator.getInstance(Locale.CHINA);
                        return cm.compare(o2.getFwdz(), o1.getFwdz());
                    } else {
                        return Integer.parseInt(StringUtils.isEmpty(o2.getXh()) ? "1" : o2.getXh())
                                - Integer.parseInt(StringUtils.isEmpty(o1.getXh()) ? "1" : o1.getXh());
                    }
                }

            }
        });

    }

    @Subscribe
    public void onNotifRefrashDataEvent(NotifRefrashDataEvent event) {
        getdataList();
    }
}
