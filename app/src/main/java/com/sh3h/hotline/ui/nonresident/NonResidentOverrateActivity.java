package com.sh3h.hotline.ui.nonresident;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.baseadapter.MainAdapter;
import com.sh3h.hotline.entity.MainItemBean;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.nonresident.call.OverrateCallActivity;
import com.sh3h.hotline.ui.nonresident.call.OverrateCallNewActivity;
import com.sh3h.hotline.ui.nonresident.receipt.OverrateReceiptActivity;
import com.sh3h.hotline.ui.nonresident.receipt.OverrateReceiptNewActivity;
import com.sh3h.hotline.ui.nonresident.selfbilling.OverrateSelfBillingActivity;
import com.sh3h.hotline.ui.nonresident.selfbilling.OverrateSelfInvoicingActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NonResidentOverrateActivity extends ParentActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private MainAdapter mAdapter;
//    R.mipmap.ic_feiju_chaodinge_zikaid,
    public static int[] icons = { R.mipmap.ic_feiju_chaodinge_fadan,R.mipmap.ic_feiju_chaodinge_cuijiao};
    private String[] titles;
    private List<MainItemBean> mMainItemBeans;
    private Unbinder mUnbinder;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_resident_overrate);
        mUnbinder = ButterKnife.bind(this);

        initToolBar(R.string.label_nonresident_overrate);

        Intent intent = getIntent();
        if (savedInstanceState != null) {
            initParams(savedInstanceState);
        } else if (intent != null) {
            initParams(intent.getExtras());
        } else {
            initParams(null);
        }

        initView();

        checkPermissions();
    }

    private void initView() {
        mMainItemBeans = new ArrayList<>();
        titles = getResources().getStringArray(R.array.non_resident_overrate);
        for (int i = 0; i < icons.length; i++) {
            MainItemBean itemBean = new MainItemBean();
            itemBean.setIcon(icons[i]);
            itemBean.setName(titles[i]);
            mMainItemBeans.add(itemBean);
        }
        mAdapter = new MainAdapter(R.layout.item_main, mMainItemBeans);
        mAdapter.setOnItemClickListener(this);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent mIntent = null;
        switch (position) {
//            case 0:
//                mIntent = new Intent(this, OverrateSelfInvoicingActivity.class);
//                break;
            case 0:
                mIntent = new Intent(this, OverrateReceiptNewActivity.class);
                break;
            case 1:
                mIntent = new Intent(this, OverrateCallNewActivity.class);
                break;
            default:
                break;
        }
        if (mIntent != null) {
            startActivity(mIntent);
        }
    }
}