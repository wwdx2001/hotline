package com.sh3h.hotline.ui.bill.detail.jinqicm;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.data.entity.response.MRMEMO;
import com.sh3h.dataprovider.data.entity.response.MRSTATUS;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.JinQiCMAdapter;
import com.sh3h.hotline.entity.JinQiCMEntity;
import com.sh3h.hotline.ui.base.BaseLazyFragment;
import com.sh3h.hotline.ui.bill.detail.UserDetailInformationActivity;
import com.sh3h.hotline.ui.bill.detail.servicepoint.details.JinQiCMMvpView;
import com.sh3h.hotline.ui.bill.detail.servicepoint.details.JinQiCMPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * create an instance of this fragment.
 */
public class JinQiCMFragment extends BaseLazyFragment implements JinQiCMMvpView {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Bundle mParam1;
    private String mParam2;

    private Unbinder unbinder;
    private CustomerInfoFindResult customerInfoFindResult;
    private List<JinQiCMEntity.MrListBean> datas;
    private JinQiCMAdapter jinQiCMAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Map<String, String> map;

    @Inject
    JinQiCMPresenter jinQiCMPresenter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public JinQiCMFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static JinQiCMFragment newInstance(Bundle param1, String param2) {
        JinQiCMFragment fragment = new JinQiCMFragment();
        Bundle args = new Bundle();
        args.putBundle(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        jinQiCMPresenter.attachView(this);
        map = new HashMap<>();
        List<MRSTATUS> mrstatuses = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getMRSTATUSDao().loadAll();
        List<MRMEMO> mrmemos = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getMRMEMODao().loadAll();
        for (int i = 0; i < mrstatuses.size(); i++) {
            map.put(mrstatuses.get(i).getMr_Status_Id(), mrstatuses.get(i).getMr_Status_Desc());
        }
        for (int i = 0; i < mrmemos.size(); i++) {
            map.put(mrmemos.get(i).getMrmemo_Code(), mrmemos.get(i).getMrmemo_Desc());
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_jin_qi_cm;
    }

    @Override
    protected void init(View view) {
        unbinder = ButterKnife.bind(this, view);
        initRecyclerView();
    }

    private void initRecyclerView() {
        datas = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        jinQiCMAdapter = new JinQiCMAdapter(map, R.layout.items_jin_qi_cm, datas);
        recyclerView.setAdapter(jinQiCMAdapter);
    }

    private void initDatas() {
        if (customerInfoFindResult != null && customerInfoFindResult.getSpList() != null) {
            if (customerInfoFindResult.getSpList().size() > 0) {
                String spId = customerInfoFindResult.getSpList().get(0).getSpId();
                if (jinQiCMPresenter != null && spId != null) {
                    jinQiCMPresenter.getChaoMa(spId);
                }
            }
        }
    }

    @Override
    protected void lazyLoadData() {
//        super.lazyLoadData();
        initDatas();
    }

    @Override
    public void showAlert(String msg) {
        showProgress(msg);
    }

    @Override
    public void hideAlert() {
        hideProgress();
    }

    @Override
    public void giveDatas(JinQiCMEntity jinQiCMEntities) {
        datas.clear();
        if (jinQiCMEntities.getMrList() != null) {
            datas.addAll(jinQiCMEntities.getMrList());
            jinQiCMAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void completed(String s) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            unbinder.unbind();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        jinQiCMPresenter.detachView();
    }
}
