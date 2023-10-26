package com.sh3h.hotline.ui.order.self.create.receipt;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.hjq.toast.ToastParams;
import com.hjq.toast.Toaster;
import com.hjq.toast.style.CustomToastStyle;
import com.maning.mndialoglibrary.MProgressDialog;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.QuerySelectAdapter;
import com.sh3h.hotline.adapter.ReceiptCallQueryAdapter;
import com.sh3h.hotline.entity.OverrateSelfBillingEntity;
import com.sh3h.hotline.entity.OverrateSelfListEntity;
import com.sh3h.hotline.entity.ReceiptCallQueryBean;
import com.sh3h.hotline.entity.UploadDataResult;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ReceiptCallQueryResultActivity extends ParentActivity implements BaseQuickAdapter.OnItemClickListener {

    @Inject
    DataManager mDataManager;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private Unbinder mUnbinder;

    private MenuItem saveItem;

    private List<OverrateSelfBillingEntity> itemBeans;
    private ReceiptCallQueryAdapter mAdapter;
    private OverrateSelfBillingEntity currentEntity;

    private String fyly;
    private String fynr;
    private String zhbh;
    private String fsdz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_call_query_result);
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);

        initToolBar(R.string.label_call_selfbilling);
        initViews();
        initData();
    }

    private void initViews() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        itemBeans = new ArrayList<>();
        mAdapter = new ReceiptCallQueryAdapter(R.layout.item_receipt_call_query, itemBeans);
        mAdapter.setOnItemClickListener(this);
//        mAdapter.setOnItemChildClickListener(this);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fyly = bundle.getString("fyly");
        fynr = bundle.getString("fynr");
        zhbh = bundle.getString("zhbh");
        fsdz = bundle.getString("fsdz");

        queryData();
    }

    private void queryData() {
        itemBeans.clear();

//        for (int i = 0; i < 10; i++) {
//            ReceiptCallQueryBean bean = new ReceiptCallQueryBean();
//            bean.setZhbh("1234567890");
//            bean.setCbh("25N403");
//            bean.setQfje("32.56");
//            bean.setGls("奉贤管理所");
//            bean.setZd("丰庄站");
//            bean.setKhmc("承诺的史可法黑瓦");
//            bean.setYsdz("上海市杨浦区纪念路8号");
//            bean.setSsdm("4200-公共管理、社会保障和社会组织");
//            itemBeans.add(bean);
//
//            ReceiptCallQueryBean bean1 = new ReceiptCallQueryBean();
//            bean1.setZhbh("89574258135");
//            bean1.setCbh("30S825");
//            bean1.setQfje("156.88");
//            bean1.setGls("闵行管理所");
//            bean1.setZd("曲阳站");
//            bean1.setKhmc("那家伙的防御Gina我");
//            bean1.setYsdz("上海市杨浦区纪念路8号");
//            bean1.setSsdm("4200-公共管理、社会保障和社会组织");
//            itemBeans.add(bean1);
//        }

//        mAdapter.notifyDataSetChanged();

        EasyHttp
                .post(URL.ZhangHuXXQuery)
                .params("zhbh", zhbh)
                .params("yhlx", "1")
                .params("cbh", "")
                .params("dz", fsdz)
                .params("khmc", "")
                .params("yjdz", "")
                .params("cbyid", mDataManager.getAccount())
//                .params("cbyid", "111032")
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
//                            ToastUtils.showShort("没有数据");
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
                        } else {
//                            ToastUtils.showShort("没有数据");
                            ToastParams params = new ToastParams();
                            params.text = "没有数据";
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                        }
                        mAdapter.setDefaultSelect(-1);
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        hideProgress();
                    }
                }) {
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        saveItem = menu.findItem(R.id.action_save);
        saveItem.setVisible(true);
        saveItem.setTitle("提交");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                break;
        }
        return super.onOptionsItemSelected(item);
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

    private void save() {
        if (currentEntity == null) {
            ToastUtils.showLong("请选择用户信息！");
            return;
        }
        AlertDialog.Builder buildDialog = new AlertDialog.Builder(this);
        buildDialog.setTitle("提示");
        buildDialog.setMessage("您确定要上传数据吗？");
        buildDialog.setCancelable(false);
        buildDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        buildDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submit();
            }
        });
        buildDialog.create().show();
    }

    private void submit() {
        EasyHttp
                .post(URL.CuiJiaoSelfOpeningOrder)
                .params("userId", mDataManager.getAccount())
//                .params("userId", "111032")
                .params("zhbh", currentEntity.getZhbh())
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        MProgressDialog.showProgress(ActivityUtils.getTopActivity());
                    }

                    @Override
                    public void onError(ApiException e) {
                        if (e.getCode() == 1010) {
                            ToastUtils.showShort("没有数据");
                        } else {
                            ToastUtils.showShort(e.getMessage());
                        }
                    }

                    @Override
                    public void onSuccess(String s) {
                        LogUtils.e(s);
                        Gson gson = new Gson();
                        UploadDataResult uploadSuccess = gson.fromJson(s, UploadDataResult.class);
                        if (uploadSuccess.getState() == 0) {
//                            ToastUtils.showShort("提交成功");
                            UploadDataResult.Data data = uploadSuccess.getData();
                            if (data != null) {
                                if (!"200".equals(data.getCode())) {
                                    ToastUtils.showShort(data.getMessage());
                                } else {
                                    ToastUtils.showShort("提交成功");
                                    finish();
                                }
                            } else {
                                ToastUtils.showShort("提交失败");
                            }
                        } else {
                            ToastUtils.showShort(uploadSuccess.getMsg());
                        }
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        MProgressDialog.dismissProgress();
                    }
                });
    }
}