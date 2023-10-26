package com.sh3h.hotline.ui.bill.detail.arrears;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.data.entity.response.CustomerTotalArrearsResult;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.ArrearsInfoResultAdapter;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.BaseLazyFragment;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.sh3h.hotline.ui.bill.detail.UserDetailInformationActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dengzhimin on 2016/9/22.
 */

public class ArrearsInformationFragment extends BaseLazyFragment implements ArrearsInformationMvpView {

    @Inject
    ArrearsInformationPresenter mPresenter;

    @Inject
    EventPosterHelper mEventPosterHelper;

    @BindView(R.id.rv_recent_bill)
    RecyclerView mRecyclerView;

    private List<CustomerTotalArrearsResult> datas;
    private LinearLayoutManager mLayoutManager;
    private ArrearsInfoResultAdapter mRecycleViewAdapter;
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
        mRecycleViewAdapter = new ArrearsInfoResultAdapter(R.layout.item_arrears_info_result, datas);
        mRecyclerView.setAdapter(mRecycleViewAdapter);
    }

    @Override
    protected void lazyLoadData() {
//        super.lazyLoadData();
        initData();
    }

    public void initData() {
        if (customerInfoFindResult != null) {
            mPresenter.getArrearsList(customerInfoFindResult.getAcctId());
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
    public void onArrearsList(CustomerTotalArrearsResult listData) {
        datas.clear();
        datas.add(listData);
        mRecycleViewAdapter.notifyDataSetChanged();
        double totalBillArrears = 0;
        for (CustomerTotalArrearsResult arrearsResult : datas) {
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
            totalBillArrears = totalBillArrears + CmTotalAmt + ChargeAmt;
        }
        mEventPosterHelper.postEventSafely(new UIBusEvent.NotifyBillArrears(totalBillArrears));
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
        if (mUnbinder != null) {
            try {
                mUnbinder.unbind();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
