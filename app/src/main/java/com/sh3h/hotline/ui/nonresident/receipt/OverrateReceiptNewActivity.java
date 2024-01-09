package com.sh3h.hotline.ui.nonresident.receipt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.hjq.toast.ToastParams;
import com.hjq.toast.Toaster;
import com.hjq.toast.style.CustomToastStyle;
import com.maning.mndialoglibrary.MProgressDialog;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.ReceiptListAdapter;
import com.sh3h.hotline.adapter.ReceiptTabStatisticsAdapter;
import com.sh3h.hotline.entity.CallTabStatisticsEntity;
import com.sh3h.hotline.entity.ReceiptListEntity;
import com.sh3h.hotline.entity.ReceiptTabStatisticsEntity;
import com.sh3h.hotline.entity.UploadDataResult;
import com.sh3h.hotline.event.NotifRefrashDataEvent;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.bill.detail.UserDetailInformationActivity;
import com.sh3h.hotline.ui.nonresident.chart.BarChartEntity;
import com.sh3h.hotline.ui.nonresident.chart.CallXYMarkerView;
import com.sh3h.hotline.ui.nonresident.chart.ReceiptXYMarkerView;
import com.sh3h.hotline.util.CollectionUtils;
import com.sh3h.hotline.util.DisplayUtils;
import com.sh3h.hotline.view.CustomGoodsCounterView;
import com.sh3h.hotline.view.PopupWindowFilter;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.lang.reflect.Method;
import java.text.Collator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;


/**
 * 超定额发单
 */
public class OverrateReceiptNewActivity extends ParentActivity implements View.OnClickListener,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    @Inject
    DataManager mDataManager;

    @Inject
    Bus mBus;

    @BindView(R.id.tv_wwcgds)
    TextView tvWwcgds;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.swiperefresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.ll_statistics_control)
    LinearLayout llStatisticsControl;

    @BindView(R.id.txt_statistics)
    TextView txtStatistics;

    @BindView(R.id.line)
    View line;

    @BindView(R.id.ll_tab)
    LinearLayout llTab;

    @BindView(R.id.et_search)
    EditText mEtSearch;

    private Toolbar toolbar;

    private Integer[] titles = {R.string.radio_button_paidanlx, R.string.radio_button_picih,
            R.string.radio_button_gongdans, R.string.radio_button_yiwancs,
            R.string.radio_button_weiwancs};

    private Map<String, List<ReceiptTabStatisticsEntity>> maps;
    private List<ReceiptTabStatisticsEntity> tabItemBeans;

    private ReceiptTabStatisticsAdapter mTabAdapter;

    private List<ReceiptListEntity> itemBeans;
    private List<ReceiptListEntity> adventList;
    private List<ReceiptListEntity> nomalList;

    private ReceiptListAdapter mAdapter;

    private Unbinder mUnbinder;

    private MenuItem menuItem;

    private PopupWindowFilter popWinFilter;

    private static String filter_sort = "asc";
    private static String filter_match = "xh";

    private Disposable mDisposable1;

    private DecimalFormat mFormat;

    private ReceiptXYMarkerView mv;
    private ReceiptXYMarkerView mv2;
    private ReceiptXYMarkerView mv3;
    private ReceiptXYMarkerView mv4;

    private EditText.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
          String searchText = mEtSearch.getText().toString().trim();
          if (itemBeans != null) {
            if (!"".equals(searchText)) {
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                List<ReceiptListEntity> collect = itemBeans.stream()
                  .filter(receiptListEntity -> receiptListEntity.getYhh().contains(searchText))
                  .collect(Collectors.toList());
                mAdapter.setNewData(collect);
                tvWwcgds.setText(String.valueOf(collect.size()));
              } else {
                List<ReceiptListEntity> datas = new ArrayList<>();
                for (ReceiptListEntity data: itemBeans) {
                  if (data.getYhh().contains(searchText)) {
                    datas.add(data);
                  }
                }
                mAdapter.setNewData(datas);
                tvWwcgds.setText(String.valueOf(datas.size()));
              }
            } else {
              mAdapter.setNewData(itemBeans);
              tvWwcgds.setText(String.valueOf(itemBeans.size()));

            }
          }

        }
        return false;
      }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overrate_receipt_new);
        getActivityComponent().inject(this);
        mBus.register(this);

        mUnbinder = ButterKnife.bind(this);

        toolbar = initToolBar(R.string.label_overrate_receipt);

        initView();

        initData();
    }

    private void initView() {
        llStatisticsControl.setOnClickListener(this);

        mFormat = new DecimalFormat("###,###,###,##");
        tabItemBeans = new ArrayList<>();
        maps = new HashMap<>();

        initRecyclerView();

        mEtSearch.setOnEditorActionListener(editorActionListener);
    }

    private void initRecyclerView() {
        //刷新控件
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                notifyData();
            }
        };

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);
        mSwipeRefreshLayout.setOnRefreshListener(listener);

        itemBeans = new ArrayList<>();
        mAdapter = new ReceiptListAdapter(R.layout.item_receipt_list_new, itemBeans);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        getListData();
    }

    private void getListData() {
        //        List<ReceiptListEntity> itemList = new ArrayList<>();
//        ReceiptListEntity receiptListEntity = new ReceiptListEntity();
//        receiptListEntity.setPch("1");
//        receiptListEntity.setXh("4");
//        receiptListEntity.setFylb("非居累进加价");
//        receiptListEntity.setFynr("发单任务");
//        receiptListEntity.setSlsj("2023-03-28 16:00:00");
//        receiptListEntity.setLxdh("13333333333");
//        receiptListEntity.setXchd("13333333333");
//        receiptListEntity.setZhbh("1234567890");
//        receiptListEntity.setDz("纪念路216号");
//        receiptListEntity.setWcsx("2023-04-16 23:59:59");
//        receiptListEntity.setFdry("李四");
//        receiptListEntity.setPdlx("超定额季度账单");
//        receiptListEntity.setBz("备注1");
//        receiptListEntity.setXzq("普陀区");
//        receiptListEntity.setKhmc("上海三高计算机中心股份有限公司");
//        receiptListEntity.setYjdz("大渡河路1550弄52支弄12号502-3");
//        receiptListEntity.setLxr("张三");
//        receiptListEntity.setSfwc("0");
//        receiptListEntity.setTyshxydm("913100005UTYN8N703");
//        receiptListEntity.setSsdm("4200-公共管理、社会保障和社会组织");
//        receiptListEntity.setSfyq("0");
//        receiptListEntity.setSftd("0");
//        itemList.add(receiptListEntity);
//
//        ReceiptListEntity receiptListEntity1 = new ReceiptListEntity();
//        receiptListEntity1.setPch("2");
//        receiptListEntity1.setXh("5");
//        receiptListEntity1.setFylb("非居累进加价");
//        receiptListEntity1.setFynr("发单任务");
//        receiptListEntity1.setSlsj("2023-03-28 16:00:00");
//        receiptListEntity1.setLxdh("13333333333");
//        receiptListEntity1.setXchd("13333333333");
//        receiptListEntity1.setZhbh("1234567890");
//        receiptListEntity1.setDz("纪念路216号");
//        receiptListEntity1.setWcsx("2023-04-06 23:59:59");
//        receiptListEntity1.setFdry("李四");
//        receiptListEntity1.setPdlx("超定额季度账单");
//        receiptListEntity1.setBz("备注1");
//        receiptListEntity1.setXzq("普陀区");
//        receiptListEntity1.setKhmc("上海三高计算机中心股份有限公司");
//        receiptListEntity1.setYjdz("大渡河路1550弄52支弄12号502-3");
//        receiptListEntity1.setLxr("张三");
//        receiptListEntity1.setSfwc("0");
//        receiptListEntity1.setTyshxydm("913100005UTYN8N703");
//        receiptListEntity1.setSsdm("4200-公共管理、社会保障和社会组织");
//        receiptListEntity1.setSfyq("1");
//        receiptListEntity1.setSftd("0");
//        itemList.add(receiptListEntity1);
//
//        ReceiptListEntity receiptListEntity2 = new ReceiptListEntity();
//        receiptListEntity2.setPch("3");
//        receiptListEntity2.setXh("6");
//        receiptListEntity2.setFylb("非居累进加价");
//        receiptListEntity2.setFynr("发单任务");
//        receiptListEntity2.setSlsj("2023-03-28 16:00:00");
//        receiptListEntity2.setLxdh("13333333333");
//        receiptListEntity2.setXchd("13333333333");
//        receiptListEntity2.setZhbh("1234567890");
//        receiptListEntity2.setDz("纪念路216号");
//        receiptListEntity2.setWcsx("2023-04-02 23:59:59");
//        receiptListEntity2.setFdry("李四");
//        receiptListEntity2.setPdlx("超定额季度账单");
//        receiptListEntity2.setBz("备注1");
//        receiptListEntity2.setXzq("普陀区");
//        receiptListEntity2.setKhmc("上海三高计算机中心股份有限公司");
//        receiptListEntity2.setYjdz("大渡河路1550弄52支弄12号502-3");
//        receiptListEntity2.setLxr("张三");
//        receiptListEntity2.setSfwc("0");
//        receiptListEntity2.setTyshxydm("913100005UTYN8N703");
//        receiptListEntity2.setSsdm("4200-公共管理、社会保障和社会组织");
//        receiptListEntity2.setSfyq("0");
//        receiptListEntity2.setSftd("1");
//        itemList.add(receiptListEntity2);
//
//        riceAdventList(itemList);

        itemBeans.clear();
        EasyHttp
                .post(URL.FaDanRWSel)
                .params("cbyid", mDataManager.getAccount())
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new CallBackProxy<CustomApiResult<List<ReceiptListEntity>>,
                        List<ReceiptListEntity>>(new CustomCallBack<List<ReceiptListEntity>>() {
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
                    public void onSuccess(final List<ReceiptListEntity> mNetWorkDatas) {
//                        hideProgress();
                        hideTopProgressbar(mSwipeRefreshLayout);
                        LogUtils.e(mNetWorkDatas.toString());
                        if (mNetWorkDatas != null && mNetWorkDatas.size() > 0) {
                            llTab.setVisibility(View.VISIBLE);
                            tvWwcgds.setText(mNetWorkDatas.size() + "");
                            riceAdventList(mNetWorkDatas);
                        } else {
                            llTab.setVisibility(View.GONE);
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

    private void riceAdventList(List<ReceiptListEntity> mNetWorkDatas) {
        adventList = (List<ReceiptListEntity>) CollectionUtils.select(mNetWorkDatas, new CollectionUtils.Predicate<ReceiptListEntity>() {
            @Override
            public boolean evaluate(ReceiptListEntity item) {
                return ((TimeUtils.string2Millis(item.getCqsx()) - TimeUtils.getNowMills()) / (24 * 60 * 60 * 1000)) <= 3;
            }
        });

        nomalList = (List<ReceiptListEntity>) CollectionUtils.selectRejected(mNetWorkDatas, new CollectionUtils.Predicate<ReceiptListEntity>() {
            @Override
            public boolean evaluate(ReceiptListEntity item) {
                return ((TimeUtils.string2Millis(item.getCqsx()) - TimeUtils.getNowMills()) / (24 * 60 * 60 * 1000)) <= 3;
            }
        });

        adventList = filterSort(adventList);

        nomalList = filterSort(nomalList);

        itemBeans.addAll(adventList);
        itemBeans.addAll(nomalList);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        menuItem = menu.findItem(R.id.action_save);
        menuItem.setTitle("排序");
        menuItem.setIcon(R.mipmap.ic_default_sort);
        return super.onCreateOptionsMenu(menu);
    }

    // 让菜单同时显示图标和文字
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        showPopupMenu(OverrateReceiptNewActivity.this, findViewById(R.id.toolbar));
        showPopupWindow();
        return super.onOptionsItemSelected(item);
    }

    private void showPopupWindow() {
        if (popWinFilter == null) {
            //自定义的单击事件
            RadioGroup.OnCheckedChangeListener paramOnCheckedChangeListener = new OverrateReceiptNewActivity.OnCheckedChangeListener();
            popWinFilter = new PopupWindowFilter(OverrateReceiptNewActivity.this, paramOnCheckedChangeListener,
                    DisplayUtils.dipToPix(this, 160), DisplayUtils.dipToPix(this, 160));
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
        popWinFilter.setGone(R.id.radio_filter_qfje, false);
        //设置默认获取焦点
        popWinFilter.setFocusable(true);
        //以某个控件的x和y的偏移量位置开始显示窗口
        popWinFilter.showAsDropDown(toolbar, toolbar.getWidth(), 0);
        //如果窗口存在，则更新
        popWinFilter.update();
    }

    //当前选择的menuItem的id
    private int checkedItemId = R.id.menu_setting_wifi;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("RestrictedApi")
    private void showPopupMenu(final Context context, View ancher) {
        PopupMenu popupMenu = new PopupMenu(context, ancher);
        //引入菜单资源
        popupMenu.inflate(R.menu.menu_popup);

        //设置选中
        popupMenu.getMenu().findItem(checkedItemId).setChecked(true);
        //菜单项的监听
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_setting_wifi:
                        checkedItemId = R.id.menu_setting_wifi;
                        Toast.makeText(context, "WIFI", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.menu_setting_gps:
                        checkedItemId = R.id.menu_setting_gps;
                        Toast.makeText(context, "GPS", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.menu_setting_userIcon:
                        Toast.makeText(context, "USER_ICON", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        //使用反射。强制显示菜单图标
//        try {
//            Field field = popupMenu.getClass().getDeclaredField("mPopup");
//            field.setAccessible(true);
//            MenuPopupHelper mHelper = (MenuPopupHelper) field.get(popupMenu);
//            mHelper.setForceShowIcon(true);
//        } catch (IllegalAccessException | NoSuchFieldException e) {
//            e.printStackTrace();
//        }

        //显示PopupMenu
        popupMenu.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (popWinFilter != null) {
            popWinFilter.dismiss();
            popWinFilter = null;
        }
        mBus.unregister(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (!"0".equals(itemBeans.get(position).getSfwc())) {
            return;
        }
        Intent intent = new Intent(OverrateReceiptNewActivity.this, OverrateReceiptHandleActivity.class);
        intent.putExtra("receipt", itemBeans.get(position));
        startActivity(intent);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
        switch (view.getId()) {
            case R.id.tv_yonghuhao:
                navigationUserInfo(position);
                break;
            case R.id.txt_delay:
                AlertDialog.Builder builder = new AlertDialog.Builder(OverrateReceiptNewActivity.this);
                View view1 = View.inflate(OverrateReceiptNewActivity.this, R.layout.dialog_receipt_delay, null);
                final CustomGoodsCounterView customGoodsCounterView = (CustomGoodsCounterView) view1.findViewById(R.id.customGoodsCounterView);
                customGoodsCounterView.setmMaxCount(10);
                customGoodsCounterView.setmMinCount(1);
                builder.setView(view1);
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (StringUtils.isEmpty(customGoodsCounterView.getGoodsNumber() + "")) {
//                            ToastUtils.showShort("请输入延期天数");
                            ToastParams params = new ToastParams();
                            params.text = "请输入延期天数";
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                            return;
                        }

                        delay(itemBeans.get(position), customGoodsCounterView.getGoodsNumber() + "");
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            case R.id.txt_chargeback:
                AlertDialog.Builder refundBuilder = new AlertDialog.Builder(OverrateReceiptNewActivity.this);
                refundBuilder.setTitle("退单原因");
                View refundView = View.inflate(OverrateReceiptNewActivity.this, R.layout.dialog_delay_refund, null);
                refundView.findViewById(R.id.et_delay_day).setVisibility(View.GONE);
                final EditText etRemark2 = (EditText) refundView.findViewById(R.id.et_remark);
                etRemark2.setVisibility(View.GONE);
                final EditText etReason2 = (EditText) refundView.findViewById(R.id.et_reason);
                refundBuilder.setView(refundView);
                refundBuilder.setNegativeButton("取消", null);
                refundBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (StringUtils.isEmpty(etReason2.getText().toString())) {
//                            ToastUtils.showShort("请输入退单原因");
                            ToastParams params = new ToastParams();
                            params.text = "请输入退单原因";
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                            return;
                        }
                        chargeBack(itemBeans.get(position), etReason2.getText().toString().trim());
                    }
                });
                AlertDialog refudnDialog = refundBuilder.create();
                refudnDialog.show();
                break;
            case R.id.txt_operate:
                Intent intent = new Intent(OverrateReceiptNewActivity.this, OverrateReceiptHandleActivity.class);
                intent.putExtra("receipt", mAdapter.getData().get(position));
                startActivity(intent);
                break;
            default:
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
                .params("acctId", mAdapter.getData().get(i).getZhbh())
                .execute(new CallBackProxy<CustomApiResult<CustomerInfoFindResult>,
                        CustomerInfoFindResult>(new CustomCallBack<CustomerInfoFindResult>() {

                    @Override
                    public void onStart() {
                        MProgressDialog.showProgress(OverrateReceiptNewActivity.this, "加载中...");
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                    }

                    @Override
                    public void onSuccess(CustomerInfoFindResult entity) {
                        if (entity != null) {
                            LogUtils.i(entity.toString());
                            Intent intent = new Intent(OverrateReceiptNewActivity.this, UserDetailInformationActivity.class);
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

    private void delay(ReceiptListEntity entity, String delayDay) {
        EasyHttp
                .post(URL.FaDanRWDelay)
                .params("albh", entity.getAlbh())
                .params("cbyid", mDataManager.getAccount())
                .params("wcsx", delayDay)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onError(ApiException e) {
//                        ToastUtils.showShort("延期失败，" + e.getMessage());
                        ToastParams params = new ToastParams();
                        params.text = "延期失败，" + e.getMessage();
                        params.style = new CustomToastStyle(R.layout.toast_error);
                        Toaster.show(params);
                        return;
                    }

                    @Override
                    public void onSuccess(String s) {
                        Gson gson = new Gson();
                        UploadDataResult uploadSuccess = gson.fromJson(s, UploadDataResult.class);
                        if (uploadSuccess.getState() == 0) {
                            ToastUtils.showShort("延期成功");
                            notifyData();
                        } else {
                            ToastParams params = new ToastParams();
                            params.text = uploadSuccess.getMsg();
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                        }
                    }
                });
    }

    private void chargeBack(ReceiptListEntity entity, String backReason) {
        EasyHttp
                .post(URL.FaDanRWCancel)
                .params("albh", entity.getAlbh())
                .params("cbyid", mDataManager.getAccount())
                .params("tdyy", backReason)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onError(ApiException e) {
//                        ToastUtils.showShort("退单失败，" + e.getMessage());
                        ToastParams params = new ToastParams();
                        params.text = "退单失败，" + e.getMessage();
                        params.style = new CustomToastStyle(R.layout.toast_error);
                        Toaster.show(params);
                    }

                    @Override
                    public void onSuccess(String s) {
                        Gson gson = new Gson();
                        UploadDataResult uploadSuccess = gson.fromJson(s, UploadDataResult.class);
                        if (uploadSuccess.getState() == 0) {
                            ToastUtils.showShort("退单成功");
                            notifyData();
                        } else {
                            ToastParams params = new ToastParams();
                            params.text = uploadSuccess.getMsg();
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                        }
                    }
                });
    }

    private void notifyData() {
//        tabItemBeans.clear();
//        EasyHttp
//                .post(URL.FaDanRWCount)
//                .params("cbyid", mDataManager.getAccount())
//                .cacheMode(CacheMode.NO_CACHE)
//                .execute(new CallBackProxy<CustomApiResult<List<ReceiptTabStatisticsEntity>>,
//                        List<ReceiptTabStatisticsEntity>>(new CustomCallBack<List<ReceiptTabStatisticsEntity>>() {
//                    @Override
//                    public void onStart() {
//                        super.onStart();
//                    }
//
//                    @Override
//                    public void onError(ApiException e) {
//                        super.onError(e);
//                    }
//
//                    @Override
//                    public void onSuccess(final List<ReceiptTabStatisticsEntity> mNetWorkDatas) {
//                        LogUtils.e(mNetWorkDatas.toString());
//                        if (mNetWorkDatas != null && mNetWorkDatas.size() > 0) {
//                            llTab.setVisibility(View.VISIBLE);
//                            llStatisticsControl.setVisibility(View.VISIBLE);
//                            tabItemBeans.addAll(mNetWorkDatas);
//                            maps = getListToMads(tabItemBeans);
//
////                        setPieChartData(tabItemBeans);
////                        setBarChartData(tabItemBeans);
//
//                            setBarChartVisible();
//
//                            setBarChartDataNew(barChart, maps.get("年度计划告知书"));
//                            mv.setItemBeans(maps.get("年度计划告知书"));
//
//                            setBarChartDataNew(barChart2, maps.get("季度执行情况告知书"));
//                            mv2.setItemBeans(maps.get("季度执行情况告知书"));
//
//                            setBarChartDataNew(barChart3, maps.get("季度执行复核情况告知书"));
//                            mv3.setItemBeans(maps.get("季度执行复核情况告知书"));
//
//                            setBarChartDataNew(barChart4, maps.get("超定额季度账单"));
//                            mv4.setItemBeans(maps.get("超定额季度账单"));
//                        }
//
//                        getListData();
//                    }
//                }) {
//                });

        itemBeans.clear();
        EasyHttp
                .post(URL.FaDanRWSel)
                .params("cbyid", mDataManager.getAccount())
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new CallBackProxy<CustomApiResult<List<ReceiptListEntity>>,
                        List<ReceiptListEntity>>(new CustomCallBack<List<ReceiptListEntity>>() {
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
                    public void onSuccess(final List<ReceiptListEntity> mNetWorkDatas) {
                        hideTopProgressbar(mSwipeRefreshLayout);
                        LogUtils.e(mNetWorkDatas.toString());
                        if (mNetWorkDatas != null && mNetWorkDatas.size() > 0) {
                            llTab.setVisibility(View.VISIBLE);
                            tvWwcgds.setText(String.valueOf(mAdapter.getData().size()));
                            riceAdventList(mNetWorkDatas);
                        } else {
                            llTab.setVisibility(View.GONE);
//                            ToastUtils.showShort("没有数据");
                            ToastParams params = new ToastParams();
                            params.text = "没有数据";
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                        }
                        mEtSearch.setText("");
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
    public void onClick(View v) {
        if (llTab.getVisibility() == View.GONE) {
            line.setVisibility(View.VISIBLE);
            llTab.setVisibility(View.VISIBLE);

            Drawable drawable = this.getResources().getDrawable(R.drawable.ic_baseline_arrow_drop_up_24);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            txtStatistics.setCompoundDrawables(drawable, null, drawable, null);
        } else {
            line.setVisibility(View.GONE);
            llTab.setVisibility(View.GONE);
            Drawable drawable = this.getResources().getDrawable(R.drawable.ic_baseline_arrow_drop_down_24);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            txtStatistics.setCompoundDrawables(drawable, null, drawable, null);
        }

    }

    class OnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            itemBeans.clear();

            switch (checkedId) {
                case R.id.radio_filter_asc:
//                    ToastUtils.showLong("升序");
                    filter_sort = "asc";

                    adventList = filterSort(adventList);
                    nomalList = filterSort(nomalList);

                    itemBeans.addAll(adventList);
                    itemBeans.addAll(nomalList);

                    mAdapter.notifyDataSetChanged();
                    popWinFilter.dismiss();
                    break;
                case R.id.radio_filter_desc:
//                    ToastUtils.showLong("降序");
                    filter_sort = "desc";

                    adventList = filterSort(adventList);
                    nomalList = filterSort(nomalList);

                    itemBeans.addAll(adventList);
                    itemBeans.addAll(nomalList);

                    mAdapter.notifyDataSetChanged();
                    popWinFilter.dismiss();
                    break;
                case R.id.radio_filter_yjdz:
//                    ToastUtils.showLong("按邮寄地址");
                    filter_match = "yjdz";

                    adventList = filterSort(adventList);
                    nomalList = filterSort(nomalList);

                    itemBeans.addAll(adventList);
                    itemBeans.addAll(nomalList);

                    mAdapter.notifyDataSetChanged();
                    popWinFilter.dismiss();
                    break;
                case R.id.radio_filter_hh:
//                    ToastUtils.showLong("按户号");
                    filter_match = "hh";

                    adventList = filterSort(adventList);
                    nomalList = filterSort(nomalList);

                    itemBeans.addAll(adventList);
                    itemBeans.addAll(nomalList);

                    mAdapter.notifyDataSetChanged();
                    popWinFilter.dismiss();
                    break;
                case R.id.radio_filter_xh:
//                    ToastUtils.showLong("按序号");
                    filter_match = "xh";

                    adventList = filterSort(adventList);
                    nomalList = filterSort(nomalList);

                    itemBeans.addAll(adventList);
                    itemBeans.addAll(nomalList);

                    mAdapter.notifyDataSetChanged();
                    popWinFilter.dismiss();
                    break;
                case R.id.radio_filter_qfje:
//                    ToastUtils.showLong("按序号");
                    filter_match = "qfje";

                    adventList = filterSort(adventList);
                    nomalList = filterSort(nomalList);

                    itemBeans.addAll(adventList);
                    itemBeans.addAll(nomalList);

                    mAdapter.notifyDataSetChanged();
                    popWinFilter.dismiss();
                    break;
                default:
                    break;
            }
        }
    }

    private List<ReceiptListEntity> filterSort(List<ReceiptListEntity> itemBeans) {
        Collections.sort(itemBeans, new Comparator<ReceiptListEntity>() {
            @Override
            public int compare(ReceiptListEntity o1, ReceiptListEntity o2) {
                if ("asc".equals(filter_sort)) {
                    if ("yjdz".equals(filter_match)) {
                        Comparator<Object> cm = Collator.getInstance(Locale.CHINA);
                        return cm.compare(o1.getYjdz(), o2.getYjdz());
//                        return o1.getYjdz().compareTo(o2.getYjdz());
                    } else if ("hh".equals(filter_match)) {
                        return o1.getZhbh().compareTo(o2.getZhbh());
                    } else if ("qfje".equals(filter_match)) {
                        return Double.compare(Double.parseDouble(StringUtils.isEmpty(o1.getQfje()) ? "0.00" : o1.getQfje()),
                                Double.parseDouble(StringUtils.isEmpty(o2.getQfje()) ? "0.00" : o2.getQfje()));
                    } else {
                        return Integer.parseInt(StringUtils.isEmpty(o1.getXh()) ? "1" : o1.getXh())
                                - Integer.parseInt(StringUtils.isEmpty(o2.getXh()) ? "1" : o2.getXh());
                    }
                } else {
                    if ("yjdz".equals(filter_match)) {
                        Comparator<Object> cm = Collator.getInstance(Locale.CHINA);
                        return cm.compare(o2.getYjdz(), o1.getYjdz());
//                        return o2.getYjdz().compareTo(o1.getYjdz());
                    } else if ("hh".equals(filter_match)) {
                        return o2.getZhbh().compareTo(o1.getZhbh());
                    } else if ("qfje".equals(filter_match)) {
                        return Double.compare(Double.parseDouble(StringUtils.isEmpty(o2.getQfje()) ? "0.00" : o2.getQfje()),
                                Double.parseDouble(StringUtils.isEmpty(o1.getQfje()) ? "0.00" : o1.getQfje()));
                    } else {
                        return Integer.parseInt(StringUtils.isEmpty(o2.getXh()) ? "1" : o2.getXh())
                                - Integer.parseInt(StringUtils.isEmpty(o1.getXh()) ? "1" : o1.getXh());
                    }
                }

            }
        });

        return itemBeans;
    }

    @Subscribe
    public void onNotifRefrashDataEvent(NotifRefrashDataEvent event) {
        notifyData();
    }
}
