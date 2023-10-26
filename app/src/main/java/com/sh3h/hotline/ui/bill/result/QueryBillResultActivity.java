package com.sh3h.hotline.ui.bill.result;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.data.entity.response.FuzzyAdressQueryResult;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.QueryAcctidResultAdapter;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.bill.detail.UserDetailInformationActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by dengzhimin on 2016/9/22.
 */

public class QueryBillResultActivity extends ParentActivity implements QueryBillResultMvpView, BaseQuickAdapter.OnItemClickListener {

    @Inject
    QueryBillResultPresenter mPresenter;

    @BindView(R.id.rv_query_bill_result)
    RecyclerView mRecyclerView;

    @BindView(R.id.fab_query_bill_result)
    FloatingActionButton mBtnFloating;

    private QueryAcctidResultAdapter mRecycleViewAdapter;

    private Unbinder mUnbinder;
    private LinearLayoutManager mLayoutManager;
    private List<FuzzyAdressQueryResult> datas;
    private String nonghao;
    private String luming;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_bill_result);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);
        mPresenter.attachView(this);
        initToolBar(R.string.activity_query_bill_result);
        initRecyclerView();
    }

    private void initRecyclerView() {
        datas = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(this);
        mRecycleViewAdapter = new QueryAcctidResultAdapter(R.layout.item_query_bill_result, datas);
        mRecycleViewAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mRecycleViewAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void setListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLayoutManager.findFirstVisibleItemPosition() > 6) {
                    mBtnFloating.setVisibility(View.VISIBLE);
                } else {
                    mBtnFloating.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        nonghao = intent.getStringExtra(Constant.NONGHAO);
        luming = intent.getStringExtra(Constant.LUMING);
        address = intent.getStringExtra(Constant.ADDRESS);
        mPresenter.queryByAddress(SPUtils.getInstance().getString(Constant.USERID), address, luming, nonghao);
    }

    @OnClick(R.id.fab_query_bill_result)
    public void onClick(View view) {
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        mPresenter.getKehuInfo(datas.get(i).getAcctId());
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
    public void onQueryByAddress(List<FuzzyAdressQueryResult> listData) {
        datas.clear();
        datas.addAll(listData);
        mRecycleViewAdapter.setNewData(datas);
//        mRecycleViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFindKehuInfo(CustomerInfoFindResult entity) {
//        ToastUtils.showShort("客户详情");
        Intent intent = new Intent(QueryBillResultActivity.this, UserDetailInformationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.BILLBASEINFO, entity);
        intent.putExtras(bundle);
        startActivity(intent);
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
        mUnbinder.unbind();
        mPresenter.detachView();
    }
}
