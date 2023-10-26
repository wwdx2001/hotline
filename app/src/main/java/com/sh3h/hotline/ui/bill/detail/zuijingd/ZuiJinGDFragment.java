package com.sh3h.hotline.ui.bill.detail.zuijingd;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.ZuiJinGDRecyclerViewAdapter;
import com.sh3h.hotline.entity.ZuiJinHBGDEntity;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class ZuiJinGDFragment extends BaseLazyFragment implements ZuijinGDMvpView {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //数据访问类
    @Inject
    ZuijinGDPresenter mZuijinGDPresenter;

    //关联Recyvlerview
    @BindView(R.id.rv_zjgd)
    RecyclerView mRecyclerView;

    private LinearLayoutManager mLayoutManager;
    //适配器
    private ZuiJinGDRecyclerViewAdapter zuiJinGDRecyclerViewAdapter;
    //butterKnife 插件关联
    private Unbinder mUnbinder;
    private CustomerInfoFindResult customerInfoFindResult;
    private List<ZuiJinHBGDEntity> entityList;
    private Bundle mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static ZuiJinGDFragment newInstance(Bundle param1, String param2) {
        ZuiJinGDFragment fragment = new ZuiJinGDFragment();
        Bundle args = new Bundle();
        args.putBundle(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((UserDetailInformationActivity) getActivity()).getActivityComponent().inject(this);
        if (getArguments() != null) {
            mParam1 = getArguments().getBundle(ARG_PARAM1);
            customerInfoFindResult = mParam1.getParcelable(Constant.BILLBASEINFO);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mZuijinGDPresenter.attachView(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_zui_jin_gd;
    }

    @Override
    protected void init(View view) {
        mUnbinder = ButterKnife.bind(this, view);
        initRecyclerView();
    }

    private void initRecyclerView() {
        entityList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        zuiJinGDRecyclerViewAdapter = new ZuiJinGDRecyclerViewAdapter(entityList, getActivity());
        mRecyclerView.setAdapter(zuiJinGDRecyclerViewAdapter);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
    }

    public void initData() {
        if (customerInfoFindResult != null) {
            // 获取数据
            mZuijinGDPresenter.getZuiJinGD(customerInfoFindResult.getAcctId());
        }
    }

    @Override
    protected void lazyLoadData() {
//        super.lazyLoadData();
        initData();
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
    public void onZuijinGD(List<ZuiJinHBGDEntity> entityList) {
        zuiJinGDRecyclerViewAdapter.setData(entityList);
        zuiJinGDRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onCompleted(String info) {

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
        mZuijinGDPresenter.detachView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
