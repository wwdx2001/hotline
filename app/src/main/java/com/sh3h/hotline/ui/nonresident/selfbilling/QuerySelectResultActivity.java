package com.sh3h.hotline.ui.nonresident.selfbilling;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.toast.ToastParams;
import com.hjq.toast.Toaster;
import com.hjq.toast.style.CustomToastStyle;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.response.FYNRBean;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.QuerySelectAdapter;
import com.sh3h.hotline.entity.OverrateSelfBillingEntity;
import com.sh3h.hotline.entity.OverrateSelfListEntity;
import com.sh3h.hotline.event.NotifRefrashDataEvent;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.nonresident.media.MultimediaFileFragment;
import com.sh3h.hotline.ui.order.TaskState;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.xujiaji.happybubble.BubbleDialog;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.xujiaji.happybubble.Auto.UP_AND_DOWN;

public class QuerySelectResultActivity extends ParentActivity implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener {

    @Inject
    DataManager mDataManager;

    @Inject
    Bus mBus;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.layout_bottom_navigation)
    LinearLayout layoutBottomNavigation;

    @BindView(R.id.txt_confirm)
    TextView txtConfirm;

    private Unbinder mUnbinder;
    private List<OverrateSelfBillingEntity> itemBeans;
    private QuerySelectAdapter mAdapter;

    private String fyly;
    private String fynr;
    private String zhbh;
    private String khmc;
    private String yjdz;
    private OverrateSelfBillingEntity currentEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_select_result);
        getActivityComponent().inject(this);
        mBus.register(this);
        mUnbinder = ButterKnife.bind(this);

        initToolBar(R.string.label_selfbilling_queryselect);

        initViews();
        initData();
    }

    private void initViews() {
        txtConfirm.setOnClickListener(this);

        initRecyclerView();
    }

    private void initRecyclerView() {
        itemBeans = new ArrayList<>();
        mAdapter = new QuerySelectAdapter(R.layout.item_query_select, itemBeans);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void initData() {
        fyly = getIntent().getStringExtra("fyly");
        fynr = getIntent().getStringExtra("fynr");
        zhbh = getIntent().getStringExtra("zhbh");
        khmc = getIntent().getStringExtra("khmc");
        yjdz = getIntent().getStringExtra("yjdz");

        queryData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mBus.unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_confirm:
                if (currentEntity == null) {
                    ToastUtils.showLong("请选择用户信息！");
                    return;
                }

                Intent intent = new Intent(this, EditSubmitActivity.class);
                intent.putExtra("fyly", fyly);
                intent.putExtra("fynr", fynr);
                intent.putExtra("SelfBilling", currentEntity);
                startActivityForResult(intent, 2001);
                break;
            default:
                break;
        }
    }

    private void queryData() {
        itemBeans.clear();
//        for (int i = 0; i < 20; i++) {
//            OverrateSelfBillingEntity ofbEntity= new OverrateSelfBillingEntity();
//            ofbEntity.setZhbh("1234567890");
//            ofbEntity.setKhmc("张三");
//            ofbEntity.setXzq("浦东新区");
//            ofbEntity.setSsdm("4200-公共管理、社会保障和社会组织");
//            ofbEntity.setTyshxydm("325614527846987456");
//            ofbEntity.setLxr("李四");
//            ofbEntity.setLxfs("13154268759");
//            ofbEntity.setYjdz("上海市浦东新区控江路128弄25号nfdjkvgnorwjvnfvrnwegvrnlewvorlwvjnrewghreiwgvnfrkjvhnrswhfbvnsdfnvswbnfvwfebvnhkrbfvgrfbvwrhsvfbwrevfhrwfbvgrwhfvgjsbrfvhfjswvbsdfbvsdfbvdksfbvhbfdvbdfhsbvskdbvdfsbvfdhvgrwfvgbksdhfbvs");
//            itemBeans.add(ofbEntity);
//        }
//        mAdapter.notifyDataSetChanged();

//        EasyHttp
//                .post(URL.ZhangHuXXQuery)
//                .params("zhbh", zhbh)
//                .params("yhlx", "2")
//                .params("cbh", "")
//                .params("dz", "")
//                .params("khmc", khmc)
//                .params("yjdz", yjdz)
//                .execute(new CallBackProxy<CustomApiResult<List<OverrateSelfBillingEntity>>,
//                        List<OverrateSelfBillingEntity>>(new CustomCallBack<List<OverrateSelfBillingEntity>>() {
//                    @Override
//                    public void onStart() {
//                        super.onStart();
//                        showProgress("数据加载中，请稍后");
//                    }
//
//                    @Override
//                    public void onError(ApiException e) {
//                        super.onError(e);
//                        hideProgress();
//                    }
//
//                    @Override
//                    public void onSuccess(final List<OverrateSelfBillingEntity> mNetWorkDatas) {
//                        hideProgress();
//                        LogUtils.e(mNetWorkDatas.toString());
//                        itemBeans.addAll(mNetWorkDatas);
//                        mAdapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                        super.onCompleted();
//                        hideProgress();
//                    }
//                }) {
//                });

        EasyHttp
                .post(URL.ZhangHuXXQuery)
                .params("zhbh", zhbh)
                .params("yhlx", "2")
                .params("cbh", "")
                .params("dz", "")
                .params("khmc", khmc)
                .params("yjdz", yjdz)
                .params("cbyid", mDataManager.getAccount())
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new CallBackProxy<CustomApiResult<OverrateSelfListEntity>,
                        OverrateSelfListEntity>(new CustomCallBack<OverrateSelfListEntity>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showProgress("数据加载中，请稍后");
                    }

                    @Override
                    public void onError(ApiException e) {
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
                    public void onSuccess(final OverrateSelfListEntity mNetWorkDatas) {
                        hideProgress();
                        LogUtils.e(mNetWorkDatas.toString());
                        if (mNetWorkDatas != null && mNetWorkDatas.getSize() > 0) {
                            itemBeans.addAll(mNetWorkDatas.getList());
                        }

                        if (itemBeans.size() > 0) {
                            layoutBottomNavigation.setVisibility(View.VISIBLE);
                        } else {
//                            ToastUtils.showShort("没有数据");
                            ToastParams params = new ToastParams();
                            params.text = "没有数据";
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                            layoutBottomNavigation.setVisibility(View.GONE);
                        }

                        mAdapter.setDefaultSelect(-1);
//                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        hideProgress();
                    }
                }) {
                });

//        EasyHttp
//                .post(URL.ZhangHuXXQuery)
//                .params("zhbh", zhbh)
//                .params("yhlx", "2")
//                .params("cbh", "")
//                .params("dz", "")
//                .params("khmc", khmc)
//                .params("yjdz", yjdz)
//                .execute(new CallBackProxy<CustomApiResult<OverrateSelfBillingEntity>,
//                        OverrateSelfBillingEntity>(new CustomCallBack<OverrateSelfBillingEntity>() {
//                    @Override
//                    public void onStart() {
//                        super.onStart();
//                        showProgress("数据加载中，请稍后");
//                    }
//
//                    @Override
//                    public void onError(ApiException e) {
//                        super.onError(e);
//                        hideProgress();
//                    }
//
//                    @Override
//                    public void onSuccess(final OverrateSelfBillingEntity mNetWorkDatas) {
//                        hideProgress();
//                        LogUtils.e(mNetWorkDatas.toString());
//                        itemBeans.add(mNetWorkDatas);
//                        mAdapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                        super.onCompleted();
//                        hideProgress();
//                    }
//                }) {
//                });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        currentEntity = itemBeans.get(position);
        if (!itemBeans.get(position).isChecked()) {
            for (OverrateSelfBillingEntity entity : itemBeans) {
                entity.setChecked(false);
            }
            currentEntity.setChecked(true);
        }
        mAdapter.setDefaultSelect(position);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.tv_sheshuidm:
                View bubbleView = LayoutInflater.from(ActivityUtils.getTopActivity()).inflate(R.layout.dialog_view_air_bubble, null);
                TextView txtShowContent = (TextView) bubbleView.findViewById(R.id.txt_show_content);
                txtShowContent.setText(itemBeans.get(position).getKhlx());
                new BubbleDialog(ActivityUtils.getTopActivity())
                        .addContentView(bubbleView)
                        .setClickedView(view)
                        .setPosition(BubbleDialog.Position.TOP, BubbleDialog.Position.LEFT)
                        .setOffsetY(8)
//                        .setOffsetX(10)
                        .autoPosition(UP_AND_DOWN)
                        .setTransParentBackground()
                        .calBar(true)
                        .show();
                break;
            case R.id.tv_youjidz:
                View bubbleView2 = LayoutInflater.from(ActivityUtils.getTopActivity()).inflate(R.layout.dialog_view_air_bubble, null);
                TextView txtShowContent2 = (TextView) bubbleView2.findViewById(R.id.txt_show_content);
                txtShowContent2.setText(itemBeans.get(position).getYjdz());
                new BubbleDialog(ActivityUtils.getTopActivity())
                        .addContentView(bubbleView2)
                        .setClickedView(view)
//                        .setPosition(BubbleDialog.Position.BOTTOM, BubbleDialog.Position.RIGHT)
                        .setOffsetY(8)
                        .autoPosition(UP_AND_DOWN)
                        .setTransParentBackground()
                        .calBar(true)
                        .show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2001 && resultCode == Activity.RESULT_OK) {
            queryData();
        }
    }

    @Subscribe
    public void onNotifRefrashDataEvent(NotifRefrashDataEvent event) {
        LogUtils.e(event.isSuccess());
        if (event.isSuccess()) {
            queryData();
        }
    }
}