package com.sh3h.hotline.ui.bill.detail;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.data.entity.response.CustomerTotalArrearsResult;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.baseadapter.BaseFragmentPagerAdapter;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.bill.detail.jinqicm.JinQiCMFragment;
import com.sh3h.hotline.ui.bill.detail.servicepoint.ServicePointFragment;
import com.sh3h.hotline.ui.bill.detail.zuijingd.ZuiJinGDFragment;
import com.sh3h.hotline.ui.bill.detail.arrears.ArrearsInformationFragment;
import com.sh3h.hotline.ui.bill.detail.basic.BasicInformationFragment;
import com.sh3h.hotline.ui.bill.detail.recentbill.RecentBillInformationFragment;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dengzhimin on 2016/9/22.
 */

public class UserDetailInformationActivity extends ParentActivity implements UserDetailInformationMvpView, TabLayout.OnTabSelectedListener {

    @Inject
    UserDetailInformationPresenter mPresenter;

    @Inject
    Bus mEventBus;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.top_navigation)
    TabLayout mTopNavigation;

    private Unbinder mUnbinder;
    private BaseFragmentPagerAdapter mAdapter;
    private List<Fragment> fragmentList;
    private Integer[] titles = {R.string.radio_button_basic_information, R.string.radio_button_service_point_information, R.string.activity_jinqi_cm,
            R.string.radio_button_recent_bill_information, R.string.radio_button_arrears_information, R.string.radio_button_huanbiao_information};
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_information);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);
        mPresenter.attachView(this);
        mEventBus.register(this);
        initToolBar(R.string.activity_user_detail_information);
        setToolBarSubTitle("欠费金额：0.0");
        initViewPager();
        initTabLayout();
    }

    private void initViewPager() {
        fragmentList = new ArrayList<>();
        mAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(mAdapter);
    }

    private void initTabLayout() {
        mTopNavigation.setupWithViewPager(viewPager);
        mTopNavigation.setTabsFromPagerAdapter(mAdapter);
        // 必须放在setupWithViewPager方法后，不然不显示标题
        mTopNavigation.addTab(mTopNavigation.newTab().setText(titles[0]));
        mTopNavigation.addTab(mTopNavigation.newTab().setText(titles[1]));
        mTopNavigation.addTab(mTopNavigation.newTab().setText(titles[2]));
        mTopNavigation.addTab(mTopNavigation.newTab().setText(titles[3]));
        mTopNavigation.addTab(mTopNavigation.newTab().setText(titles[4]));
        mTopNavigation.addTab(mTopNavigation.newTab().setText(titles[5]));
    }

    private void setListener() {
        mTopNavigation.addOnTabSelectedListener(this);
    }

    private void initData() {
        mBundle = getIntent().getExtras();
        CustomerInfoFindResult customerInfoFindResult = mBundle.getParcelable(Constant.BILLBASEINFO);
        mPresenter.getCustomerTotalArrears(customerInfoFindResult.getAcctId());
        initFragment(mBundle);
    }

    protected void initFragment(Bundle bundle) {
        BasicInformationFragment basicInformationFragment = new BasicInformationFragment();
        basicInformationFragment.setArguments(bundle);
        ServicePointFragment servicePointFragment = ServicePointFragment.newInstance(bundle, "");
        JinQiCMFragment jinQiCMFragment = JinQiCMFragment.newInstance(bundle, "");
        RecentBillInformationFragment recentBillInformationFragment = new RecentBillInformationFragment();
        recentBillInformationFragment.setArguments(bundle);
        ArrearsInformationFragment arrearsInformationFragment = new ArrearsInformationFragment();
        arrearsInformationFragment.setArguments(bundle);
        ZuiJinGDFragment zuiJinGDFragment = ZuiJinGDFragment.newInstance(bundle, "");
        fragmentList.add(basicInformationFragment);
        fragmentList.add(servicePointFragment);
        fragmentList.add(jinQiCMFragment);
        fragmentList.add(recentBillInformationFragment);
        fragmentList.add(arrearsInformationFragment);
        fragmentList.add(zuiJinGDFragment);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //选择的tab
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        //离开的那个tab
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        //再次选择tab
    }

    @Subscribe
    public void onGetTotalBillArrears(UIBusEvent.NotifyBillArrears notifyBillArrears) {
        double arrear = notifyBillArrears.getTotalBillArrears();
        BigDecimal b = new BigDecimal(arrear);
        float result = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        setToolBarSubTitle("欠费金额：" + result);
    }

    @Override
    public void showProgressDialog(String msg) {
        showProgress(msg);
    }

    @Override
    public void hindProgressDialog() {
        hideProgress();
    }

    @Override
    public void onCustomerArrears(CustomerTotalArrearsResult arrearsResult) {
        if (arrearsResult == null) {
            return;
        }
        double ChargeAmt = 0;
        double CmTotalAmt = 0;
        try {
            CmTotalAmt = Double.parseDouble(arrearsResult.getCmTotalAmt());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException nullPoint) {
            nullPoint.printStackTrace();
        }
        try {
            ChargeAmt = Double.parseDouble(arrearsResult.getChargeAmt());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException nullPoint) {
            nullPoint.printStackTrace();
        }
        double arrear = ChargeAmt + CmTotalAmt;
        BigDecimal b = new BigDecimal(arrear);
        float result = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        setToolBarSubTitle("欠费金额：" + result);
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onCompleted(String info) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        mUnbinder.unbind();
        mEventBus.unregister(this);
    }

}
