package com.sh3h.hotline.ui.nonresident.call;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hjq.toast.ToastParams;
import com.hjq.toast.Toaster;
import com.hjq.toast.style.CustomToastStyle;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.CallBillTabAdapter;
import com.sh3h.hotline.adapter.ReceiptTabStatisticsAdapter;
import com.sh3h.hotline.entity.CallBillTabEntity;
import com.sh3h.hotline.entity.CallTabStatisticsEntity;
import com.sh3h.hotline.entity.ReceiptTabStatisticsEntity;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.view.TabLayoutView.TabLayout;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CallBillDetailActivity extends ParentActivity {

    @BindView(R.id.m_tab)
    TabLayout mTabLayout;

    @BindView(R.id.tab_recyclerview)
    RecyclerView mTabRecyclerView;

    private Integer[] titles = {R.string.radio_button_nian, R.string.radio_button_jidu,
            R.string.radio_button_chaodingesl, R.string.radio_button_kaizhangje,
            R.string.radio_button_kaizhangsl};

    private List<CallBillTabEntity> tabItemBeans;

    private CallBillTabAdapter mTabAdapter;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_bill_detail);

        mUnbinder = ButterKnife.bind(this);

        initToolBar(R.string.label_call_bill_detail);

        initView();

        initData();
    }

    private void initView() {
        initTab(mTabLayout);
        initTabRecyclerView();
    }

    private void initTab(TabLayout view) {
        for (int i = 0; i < titles.length; i++) {
            TabLayout.Tab tab = view.newTab();
            tab.setText(titles[i]);
            // tab.setIcon(R.mipmap.ic_launcher);//icon会显示在文字上面
            view.addTab(tab);
        }
    }

    private void initTabRecyclerView() {
        tabItemBeans = new ArrayList<>();
        mTabAdapter = new CallBillTabAdapter(R.layout.item_call_bill_tab, tabItemBeans);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mTabRecyclerView.setHasFixedSize(true);
        mTabRecyclerView.setNestedScrollingEnabled(true);
        mTabRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTabRecyclerView.setAdapter(mTabAdapter);
    }

    private void initData() {
        String yhh = getIntent().getStringExtra("yhh");
//        CallBillTabEntity callBillTabEntity = new CallBillTabEntity();
//        callBillTabEntity.setNian("2022");
//        callBillTabEntity.setJiDu("4");
//        callBillTabEntity.setChaoDingESL("5000");
//        callBillTabEntity.setKaiZhangJE("30000.00");
//        callBillTabEntity.setKaiZhangRQ("2023-1-05");
//        tabItemBeans.add(callBillTabEntity);
//
//        CallBillTabEntity callBillTabEntity1 = new CallBillTabEntity();
//        callBillTabEntity1.setNian("2022");
//        callBillTabEntity1.setJiDu("3");
//        callBillTabEntity1.setChaoDingESL("0");
//        callBillTabEntity1.setKaiZhangJE("0.00");
//        callBillTabEntity1.setKaiZhangRQ("2022-10-08");
//        tabItemBeans.add(callBillTabEntity1);
//
//        CallBillTabEntity callBillTabEntity2 = new CallBillTabEntity();
//        callBillTabEntity2.setNian("2022");
//        callBillTabEntity2.setJiDu("2");
//        callBillTabEntity2.setChaoDingESL("0");
//        callBillTabEntity2.setKaiZhangJE("0.00");
//        callBillTabEntity2.setKaiZhangRQ("2022-7-02");
//        tabItemBeans.add(callBillTabEntity2);
        tabItemBeans.clear();
        EasyHttp
                .post(URL.FeiJuCDEQFXQ)
                .params("yhh", yhh)
                .execute(new CallBackProxy<CustomApiResult<List<CallBillTabEntity>>,
                        List<CallBillTabEntity>>(new CustomCallBack<List<CallBillTabEntity>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onError(ApiException e) {
//                        super.onError(e);
                        hideProgress();
                        onCompleted();

                        if (e.getCode() == 1010) {
                            ToastUtils.showShort("没有数据");
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
                    public void onSuccess(final List<CallBillTabEntity> mNetWorkDatas) {
                        LogUtils.e(mNetWorkDatas.toString());
                        if (mNetWorkDatas != null && mNetWorkDatas.size() > 0) {
                            tabItemBeans.addAll(mNetWorkDatas);
                            mTabAdapter.notifyDataSetChanged();
                        } else {
//                            ToastUtils.showShort("没有数据");
                            ToastParams params = new ToastParams();
                            params.text = "没有数据";
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                        }
                    }
                }) {
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}