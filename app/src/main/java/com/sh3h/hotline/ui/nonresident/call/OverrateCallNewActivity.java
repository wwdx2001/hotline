package com.sh3h.hotline.ui.nonresident.call;

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
import com.sh3h.hotline.adapter.CallListAdapter;
import com.sh3h.hotline.entity.CallListEntity;
import com.sh3h.hotline.entity.CallTabStatisticsEntity;
import com.sh3h.hotline.entity.ReceiptListEntity;
import com.sh3h.hotline.entity.UploadDataResult;
import com.sh3h.hotline.event.NotifRefrashDataEvent;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.bill.detail.UserDetailInformationActivity;
import com.sh3h.hotline.ui.nonresident.chart.BarChartEntity;
import com.sh3h.hotline.ui.nonresident.chart.CallXYMarkerView;
import com.sh3h.hotline.ui.order.self.create.CreateSelfOrderActivity;
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
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * 超定额催缴
 */
public class OverrateCallNewActivity extends ParentActivity implements View.OnClickListener,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    @Inject
    DataManager mDataManager;

    @Inject
    Bus mBus;

    @BindView(R.id.tv_wwcgds)
    TextView tvWwcgds;

    @BindView(R.id.swiperefresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.ll_statistics_control)
    LinearLayout llStatisticsControl;

    @BindView(R.id.txt_statistics)
    TextView txtStatistics;

    @BindView(R.id.line)
    View line;

    @BindView(R.id.ll_tab)
    RelativeLayout llTab;

    @BindView(R.id.et_search)
    EditText mEtSearch;

    private Toolbar toolbar;

    private Integer[] titles = {R.string.radio_button_picih, R.string.radio_button_gongdans, R.string.radio_button_yiwancs,
            R.string.radio_button_weiwancs};

    private List<CallTabStatisticsEntity> tabItemBeans;

    private List<CallListEntity> itemBeans;
    private List<CallListEntity> adventList;
    private List<CallListEntity> nomalList;

    private CallListAdapter mAdapter;

    private Unbinder mUnbinder;

    private MenuItem menuItem;

    private PopupWindowFilter popWinFilter;

    private static String filter_sort = "asc";
    private static String filter_match = "xh";

    private Disposable mDisposable1;

    private DecimalFormat mFormat;

    private CallXYMarkerView mv;

  private EditText.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
      if (actionId == EditorInfo.IME_ACTION_SEARCH) {
        String searchText = mEtSearch.getText().toString().trim();
        if (itemBeans != null) {
          if (!"".equals(searchText)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
              List<CallListEntity> collect = itemBeans.stream()
                .filter(callListEntity -> callListEntity.getYhh().contains(searchText))
                .collect(Collectors.toList());
              mAdapter.setNewData(collect);
              tvWwcgds.setText(String.valueOf(collect.size()));
            } else {
              List<CallListEntity> datas = new ArrayList<>();
              for (CallListEntity data: itemBeans) {
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
        setContentView(R.layout.activity_overrate_call_new);
        getActivityComponent().inject(this);
        mBus.register(this);

        mUnbinder = ButterKnife.bind(this);

        toolbar = initToolBar(R.string.label_overrate_call);

        initView();

        initData();
    }

    private void initView() {
        llStatisticsControl.setOnClickListener(this);

        mFormat = new DecimalFormat("###,###,###,##");
        tabItemBeans = new ArrayList<>();

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
        mAdapter = new CallListAdapter(R.layout.item_call_list_new, itemBeans);
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
        //        CallListEntity callListEntity = new CallListEntity();
//        callListEntity.setXuHao("1/10");
//        callListEntity.setZhangHuBH("3213124242");
//        callListEntity.setXingZhengQ("普陀区");
//        callListEntity.setKeHuMC("上海三高计算机中心股份有限公司");
//        callListEntity.setYouJiDZ("大渡河路1550弄52支弄12号502-3");
//        callListEntity.setQianFeiJE("30000.00");
//        itemBeans.add(callListEntity);
//
//        CallListEntity callListEntity1 = new CallListEntity();
//        callListEntity1.setXuHao("2/10");
//        callListEntity1.setZhangHuBH("3213124242");
//        callListEntity1.setXingZhengQ("普陀区");
//        callListEntity1.setKeHuMC("上海三高计算机中心股份有限公司");
//        callListEntity1.setYouJiDZ("大渡河路1550弄52支弄12号502-3");
//        callListEntity1.setQianFeiJE("30000.00");
//        itemBeans.add(callListEntity1);
//
//        CallListEntity callListEntity2 = new CallListEntity();
//        callListEntity2.setXuHao("3/10");
//        callListEntity2.setZhangHuBH("3213124242");
//        callListEntity2.setXingZhengQ("普陀区");
//        callListEntity2.setKeHuMC("上海三高计算机中心股份有限公司");
//        callListEntity2.setYouJiDZ("大渡河路1550弄52支弄12号502-3");
//        callListEntity2.setQianFeiJE("30000.00");
//        itemBeans.add(callListEntity2);
//
//        CallListEntity callListEntity3 = new CallListEntity();
//        callListEntity3.setXuHao("4/10");
//        callListEntity3.setZhangHuBH("3213124242");
//        callListEntity3.setXingZhengQ("普陀区");
//        callListEntity3.setKeHuMC("上海三高计算机中心股份有限公司");
//        callListEntity3.setYouJiDZ("大渡河路1550弄52支弄12号502-3");
//        callListEntity3.setQianFeiJE("30000.00");
//        itemBeans.add(callListEntity3);
//
//        CallListEntity callListEntity4 = new CallListEntity();
//        callListEntity4.setXuHao("5/10");
//        callListEntity4.setZhangHuBH("3213124242");
//        callListEntity4.setXingZhengQ("普陀区");
//        callListEntity4.setKeHuMC("上海三高计算机中心股份有限公司");
//        callListEntity4.setYouJiDZ("大渡河路1550弄52支弄12号502-3");
//        callListEntity4.setQianFeiJE("30000.00");
//        itemBeans.add(callListEntity4);
//
//        CallListEntity callListEntity5 = new CallListEntity();
//        callListEntity5.setXuHao("6/10");
//        callListEntity5.setZhangHuBH("3213124242");
//        callListEntity5.setXingZhengQ("普陀区");
//        callListEntity5.setKeHuMC("上海三高计算机中心股份有限公司");
//        callListEntity5.setYouJiDZ("大渡河路1550弄52支弄12号502-3");
//        callListEntity5.setQianFeiJE("30000.00");
//        itemBeans.add(callListEntity5);
//
//        CallListEntity callListEntity6 = new CallListEntity();
//        callListEntity6.setXuHao("7/10");
//        callListEntity6.setZhangHuBH("3213124242");
//        callListEntity6.setXingZhengQ("普陀区");
//        callListEntity6.setKeHuMC("上海三高计算机中心股份有限公司");
//        callListEntity6.setYouJiDZ("大渡河路1550弄52支弄12号502-3");
//        callListEntity6.setQianFeiJE("30000.00");
//        itemBeans.add(callListEntity6);
//
//        CallListEntity callListEntity7 = new CallListEntity();
//        callListEntity7.setXuHao("8/10");
//        callListEntity7.setZhangHuBH("3213124242");
//        callListEntity7.setXingZhengQ("普陀区");
//        callListEntity7.setKeHuMC("上海三高计算机中心股份有限公司");
//        callListEntity7.setYouJiDZ("大渡河路1550弄52支弄12号502-3");
//        callListEntity7.setQianFeiJE("30000.00");
//        itemBeans.add(callListEntity7);
//
//        CallListEntity callListEntity8 = new CallListEntity();
//        callListEntity8.setXuHao("9/10");
//        callListEntity8.setZhangHuBH("3213124242");
//        callListEntity8.setXingZhengQ("普陀区");
//        callListEntity8.setKeHuMC("上海三高计算机中心股份有限公司");
//        callListEntity8.setYouJiDZ("大渡河路1550弄52支弄12号502-3");
//        callListEntity8.setQianFeiJE("30000.00");
//        itemBeans.add(callListEntity8);
//
//        CallListEntity callListEntity9 = new CallListEntity();
//        callListEntity9.setXuHao("10/10");
//        callListEntity9.setZhangHuBH("3213124242");
//        callListEntity9.setXingZhengQ("普陀区");
//        callListEntity9.setKeHuMC("上海三高计算机中心股份有限公司");
//        callListEntity9.setYouJiDZ("大渡河路1550弄52支弄12号502-3");
//        callListEntity9.setQianFeiJE("30000.00");
//        itemBeans.add(callListEntity9);
//        mAdapter.notifyDataSetChanged();

        itemBeans.clear();
        EasyHttp
                .post(URL.CuJIaoGDSelFJ)
                .params("cbyid", mDataManager.getAccount())
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new CallBackProxy<CustomApiResult<List<CallListEntity>>,
                        List<CallListEntity>>(new CustomCallBack<List<CallListEntity>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showTopProgressbar(mSwipeRefreshLayout);
                    }

                    @Override
                    public void onError(ApiException e) {
//                        hideProgress();
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
                    public void onSuccess(final List<CallListEntity> mNetWorkDatas) {
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

    private void riceAdventList(List<CallListEntity> mNetWorkDatas) {
        adventList = (List<CallListEntity>) CollectionUtils.select(mNetWorkDatas, new CollectionUtils.Predicate<CallListEntity>() {
            @Override
            public boolean evaluate(CallListEntity item) {
                return ((TimeUtils.string2Millis(item.getCjqx()) - TimeUtils.getNowMills()) / (24 * 60 * 60 * 1000)) <= 3;
            }
        });

        nomalList = (List<CallListEntity>) CollectionUtils.selectRejected(mNetWorkDatas, new CollectionUtils.Predicate<CallListEntity>() {
            @Override
            public boolean evaluate(CallListEntity item) {
                return ((TimeUtils.string2Millis(item.getCjqx()) - TimeUtils.getNowMills()) / (24 * 60 * 60 * 1000)) <= 3;
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
        showPopupWindow();
        return super.onOptionsItemSelected(item);
    }

    private void showPopupWindow() {
        if (popWinFilter == null) {
            //自定义的单击事件
            RadioGroup.OnCheckedChangeListener paramOnCheckedChangeListener = new OverrateCallNewActivity.OnCheckedChangeListener();
            popWinFilter = new PopupWindowFilter(OverrateCallNewActivity.this, paramOnCheckedChangeListener,
                    DisplayUtils.dipToPix(this, 160), DisplayUtils.dipToPix(this, 220));
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
        //设置默认获取焦点
        popWinFilter.setFocusable(true);
        //以某个控件的x和y的偏移量位置开始显示窗口
        popWinFilter.showAsDropDown(toolbar, toolbar.getWidth(), 0);
        //如果窗口存在，则更新
        popWinFilter.update();
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
        Intent intent = new Intent(OverrateCallNewActivity.this, OverrateCallHandleActivity.class);
        intent.putExtra("call", itemBeans.get(position));
        startActivity(intent);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
        switch (view.getId()) {
            case R.id.tv_yonghuhao:
                navigationUserInfo(position);
                break;
            case R.id.txt_delay:
                AlertDialog.Builder builder = new AlertDialog.Builder(OverrateCallNewActivity.this);
                View view1 = View.inflate(OverrateCallNewActivity.this, R.layout.dialog_receipt_delay, null);
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
                AlertDialog.Builder refundBuilder = new AlertDialog.Builder(OverrateCallNewActivity.this);
                refundBuilder.setTitle("退单原因");
                View refundView = View.inflate(OverrateCallNewActivity.this, R.layout.dialog_delay_refund, null);
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
//                if (!"0".equals(itemBeans.get(position).getSfwc())) {
//                    return;
//                }
                Intent intent = new Intent(OverrateCallNewActivity.this, OverrateCallHandleActivity.class);
                intent.putExtra("call", mAdapter.getData().get(position));
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
                        MProgressDialog.showProgress(OverrateCallNewActivity.this, "加载中...");
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                    }

                    @Override
                    public void onSuccess(CustomerInfoFindResult entity) {
                        if (entity != null) {
                            LogUtils.i(entity.toString());
                            Intent intent = new Intent(OverrateCallNewActivity.this, UserDetailInformationActivity.class);
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

    private void delay(CallListEntity entity, String delayDay) {
        EasyHttp
                .post(URL.CuJIaoGDDelay)
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

    private void chargeBack(CallListEntity entity, String backReason) {
        EasyHttp
                .post(URL.CuJIaoGDCancel)
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
                        } else {
                            ToastParams params = new ToastParams();
                            params.text = uploadSuccess.getMsg();
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                        }
                        notifyData();
                    }
                });
    }

    private void notifyData() {

        itemBeans.clear();
        EasyHttp
                .post(URL.CuJIaoGDSelFJ)
                .params("cbyid", mDataManager.getAccount())
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new CallBackProxy<CustomApiResult<List<CallListEntity>>,
                        List<CallListEntity>>(new CustomCallBack<List<CallListEntity>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showTopProgressbar(mSwipeRefreshLayout);
                    }

                    @Override
                    public void onError(ApiException e) {
//                        super.onError(e);
                        hideTopProgressbar(mSwipeRefreshLayout);
                        onCompleted();

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
                    public void onSuccess(final List<CallListEntity> mNetWorkDatas) {
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

    private List<CallListEntity> filterSort(List<CallListEntity> itemBeans) {
        Collections.sort(itemBeans, new Comparator<CallListEntity>() {
            @Override
            public int compare(CallListEntity o1, CallListEntity o2) {
                if ("asc".equals(filter_sort)) {
                    if ("yjdz".equals(filter_match)) {
                        Comparator<Object> cm = Collator.getInstance(Locale.CHINA);
                        return cm.compare(o1.getYjdz(), o2.getYjdz());
//                        return o1.getYjdz().compareTo(o2.getYjdz());
                    } else if ("hh".equals(filter_match)) {
                        return o1.getZhbh().compareTo(o2.getZhbh());
                    } else if ("qfje".equals(filter_match)) {
                        return Double.compare(Double.parseDouble(StringUtils.isEmpty(o1.getQfzje()) ? "0.00" : o1.getQfzje()),
                                Double.parseDouble(StringUtils.isEmpty(o2.getQfzje()) ? "0.00" : o2.getQfzje()));
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
                        return Double.compare(Double.parseDouble(StringUtils.isEmpty(o2.getQfzje()) ? "0.00" : o2.getQfzje()),
                                Double.parseDouble(StringUtils.isEmpty(o1.getQfzje()) ? "0.00" : o1.getQfzje()));
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
