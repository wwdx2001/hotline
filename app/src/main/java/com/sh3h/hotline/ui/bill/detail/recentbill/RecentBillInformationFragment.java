package com.sh3h.hotline.ui.bill.detail.recentbill;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.data.entity.response.DUBillBaseInfo;
import com.sh3h.dataprovider.data.entity.response.DURecentBill;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.RecentBillAdapter;
import com.sh3h.hotline.adapter.RecentBillInformationRecycleViewAdapter;
import com.sh3h.hotline.adapter.RecentBillInformationRecycleViewAdapter.OnItemClickListener;
import com.sh3h.hotline.divider.DividerItemDecoration;
import com.sh3h.hotline.entity.RecentBillDetailInfoEntity;
import com.sh3h.hotline.ui.base.BaseLazyFragment;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.sh3h.hotline.ui.bill.detail.UserDetailInformationActivity;
import com.sh3h.hotline.ui.bill.detail.recentbill.detail.RecentBillInformationDetailActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dengzhimin on 2016/9/22.
 */

public class RecentBillInformationFragment extends BaseLazyFragment implements RecentBillInformationMvpView {

    @Inject
    RecentBillInformationPresenter mPresenter;

    @BindView(R.id.rv_recent_bill)
    RecyclerView mRecyclerView;

    private List<RecentBillDetailInfoEntity.BillListBean> datas;
    private LinearLayoutManager mLayoutManager;
    private RecentBillAdapter mRecycleViewAdapter;
    private Unbinder mUnbinder;
    private CustomerInfoFindResult customerInfoFindResult;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((UserDetailInformationActivity) getActivity()).getActivityComponent().inject(this);
        customerInfoFindResult = getArguments().getParcelable(Constant.BILLBASEINFO);
        mPresenter.attachView(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_recent_bill_information;
    }

    @Override
    protected void init(View view) {
        mUnbinder = ButterKnife.bind(this, view);
        initRecyclerView();
    }

    private void initRecyclerView() {
        datas = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecycleViewAdapter = new RecentBillInformationRecycleViewAdapter(datas, getActivity());
//        mRecyclerView.setAdapter(mRecycleViewAdapter);
        mRecycleViewAdapter = new RecentBillAdapter(R.layout.items_recent_bill_detail, datas);
        mRecyclerView.setAdapter(mRecycleViewAdapter);
    }

    @Override
    protected void lazyLoadData() {
        initData();
    }

    public void initData() {
        if (customerInfoFindResult != null) {
            mPresenter.getBillList(customerInfoFindResult.getAcctId());
        }
    }

    @Override
    public void showProgressDialog(String msg) {
        showProgress(msg);
    }

    @Override
    public void hideProgressDialog() {
        hideProgress();
    }

    @Override
    public void onBillList(List<RecentBillDetailInfoEntity.BillListBean> listBeans) {
        datas.clear();
        if (listBeans != null) {
            datas.addAll(listBeans);
            mRecycleViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onCompleted(String message) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            mUnbinder.unbind();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

}
