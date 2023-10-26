package com.sh3h.hotline.ui.bill.detail.servicepoint.details;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.JinQiCMAdapter;
import com.sh3h.hotline.entity.JinQiCMEntity;
import com.sh3h.hotline.ui.base.ParentActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class JinQiCMActivity extends ParentActivity implements JinQiCMMvpView{

    private Unbinder unbinder;
    private List<JinQiCMEntity.MrListBean> datas;
    private JinQiCMAdapter jinQiCMAdapter;

    @Inject
    JinQiCMPresenter jinQiCMPresenter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jin_qi_cm);

        initView();
        initDatas();
    }

    private void initView() {
        getActivityComponent().inject(this);
        unbinder=ButterKnife.bind(this);
        jinQiCMPresenter.attachView(this);
        initToolBar(R.string.activity_jinqi_cm);
        datas = new ArrayList<>();
        linearLayoutManager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        jinQiCMAdapter = new JinQiCMAdapter(R.layout.items_jin_qi_cm,datas);
        recyclerView.setAdapter(jinQiCMAdapter);

    }

    private void initDatas() {
        String s =getIntent().getStringExtra("spId");
        if(s != null){
            jinQiCMPresenter.getChaoMa(s);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        jinQiCMPresenter.detachView();
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
        datas.addAll(jinQiCMEntities.getMrList());
        jinQiCMAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void completed(String s) {

    }
}
