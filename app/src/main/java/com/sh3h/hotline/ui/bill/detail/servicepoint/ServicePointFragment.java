package com.sh3h.hotline.ui.bill.detail.servicepoint;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.ServicePointListAdapter;
import com.sh3h.hotline.ui.base.BaseLazyFragment;
import com.sh3h.hotline.ui.bill.detail.servicepoint.details.JinQiCMActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServicePointFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServicePointFragment extends BaseLazyFragment implements BaseQuickAdapter.OnItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    Unbinder unbinder;
    private Bundle mParam1;
    private String mParam2;
    private CustomerInfoFindResult customerInfoFindResult;
    private List<CustomerInfoFindResult.SpListBean> datas;
    private LinearLayoutManager mLayoutManager;
    private ServicePointListAdapter mAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static ServicePointFragment newInstance(Bundle param1, String param2) {
        ServicePointFragment fragment = new ServicePointFragment();
        Bundle args = new Bundle();
        args.putBundle(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getBundle(ARG_PARAM1);
            customerInfoFindResult = mParam1.getParcelable(Constant.BILLBASEINFO);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_service_point;
    }

    @Override
    protected void init(View view) {
        unbinder = ButterKnife.bind(this, view);
        initRecyclerView();
    }

    private void initRecyclerView() {
        datas = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ServicePointListAdapter(R.layout.item_service_point_result, datas);
        recyclerView.setAdapter(mAdapter);
//        mAdapter.setOnItemClickListener(this);

    }

    @Override
    protected void lazyLoadData() {
        super.lazyLoadData();
        if (customerInfoFindResult != null && customerInfoFindResult.getSpList() != null) {
            datas.clear();
            if (customerInfoFindResult.getSpList().size() > 0) {
                datas.add(customerInfoFindResult.getSpList().get(0));
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            try {
                unbinder.unbind();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        Intent intent = new Intent();
        intent.putExtra("spId", datas.get(i).getSpId());
        intent.setClass(getActivity(), JinQiCMActivity.class);
        startActivity(intent);
    }
}
