package com.sh3h.hotline.ui.bill;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.DUMyTaskDao;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.bill.detail.UserDetailInformationActivity;
import com.sh3h.hotline.ui.bill.result.QueryBillResultActivity;
import com.sh3h.hotline.ui.order.myorder.history.HistoryOrdersActivity;
import com.sh3h.hotline.util.CommonUtils;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by dengzhimin on 2016/9/14.
 */
public class QueryBillActivity extends ParentActivity implements QueryBillMvpView {

    public static final String TAG = "QueryBillActivity";

    @Inject
    Bus mEventBus;

    @Inject
    QueryBillPresenter mPresenter;

    @BindView(R.id.btn_query)
    Button mBtnQuery;

    @BindView(R.id.et_zhanghuBH)
    EditText etZhanghuBH;

    @BindView(R.id.et_nonghao)
    EditText etNonghao;

    @BindView(R.id.et_luming)
    EditText etLuming;

    @BindView(R.id.et_dizhi)
    EditText mDizhiEt;

    private String mSearchContent;
    private Unbinder mUnbinder;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_bill);
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);
        mEventBus.register(this);
        mPresenter.attachView(this);

        initToolBar(R.string.activity_query_bill);

        Intent intent = getIntent();
        if (savedInstanceState != null) {
            initParams(savedInstanceState);
        } else if (intent != null) {
            initParams(intent.getExtras());
        } else {
            initParams(null);
        }

        checkPermissions();
    }

    @OnClick(R.id.btn_query)
    public void onClick(View view) {
        if (!isConfigInitSuccess()) {
            ApplicationsUtil.showMessage(this, R.string.text_init_failure);
            return;
        }

        String cardId = etZhanghuBH.getText().toString().trim();
        String nonghao = etNonghao.getText().toString().trim();
        String luming = etLuming.getText().toString().trim();
        String address = mDizhiEt.getText().toString().trim();
        if (TextUtils.isEmpty(cardId) && TextUtils.isEmpty(nonghao)
                && TextUtils.isEmpty(luming) && TextUtils.isEmpty(address)) {
            Toast.makeText(this, R.string.toast_at_least_one_condition, Toast.LENGTH_SHORT).show();
            return;
        }
        switch (view.getId()) {
            case R.id.btn_query:
                if (!"".equals(cardId)) {
                    findKehuInfo(cardId);
                } else {
                    Intent intent = new Intent(QueryBillActivity.this, QueryBillResultActivity.class);
                    intent.putExtra(Constant.NONGHAO, nonghao);
                    intent.putExtra(Constant.LUMING, luming);
                    intent.putExtra(Constant.ADDRESS, address);
                    startActivity(intent);
                }
                break;
        }
    }

    private void findKehuInfo(String cardId) {
        mPresenter.getKehuInfo(cardId);
    }

    @Subscribe
    public void onInitResult(UIBusEvent.InitResult initResult) {
        LogUtil.i(TAG, "---onInitResult---" + initResult.isSuccess());
        if (initResult.isSuccess()) {

            String key = TimeUtils.getNowString(CommonUtils.getFormat("yyyy-MM-dd"));
            String account = SPUtils.getInstance().getString(Constant.USERID);
            String tips = SPUtils.getInstance(account + "_SaveOrdersTips").getString("Tips", "");
            boolean isTipsLimit = SPUtils.getInstance(account + "_SaveOrdersTips").getBoolean("IsTipsLimit", false);
            if (isTipsLimit) {
                if (!(ActivityUtils.getTopActivity() instanceof HistoryOrdersActivity)) {
                    showSaveOrdersTips();
                }
            } else {
                if (!tips.equals(key)) {
                    if (!(ActivityUtils.getTopActivity() instanceof HistoryOrdersActivity)) {
                        showSaveOrdersTips();
                    }
                }
            }

        } else {
            ApplicationsUtil.showMessage(this, R.string.text_init_failure);
        }
    }

    private void showSaveOrdersTips() {
        final AsyncSession asyncSession = GreenDaoUtils.getAsyncSession(MainApplication.getInstance());
        asyncSession.queryList(GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getDUMyTaskDao().queryBuilder()
                .where(DUMyTaskDao.Properties.TaskState.eq(Constant.ORIGIN_MY_TASK_HISTORY))
                .where(DUMyTaskDao.Properties.IsUploadSuccess.eq(Constant.NO_UPLOAD))
                .orderDesc(DUMyTaskDao.Properties.IsFlag)
//                .where(DUMyTaskDao.Properties.UserId.eq(mAccount))
                .build());
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation asyncOperation) {
                LogUtils.e("onResult", "start");
                if (asyncOperation.getType() == AsyncOperation.OperationType.QueryList) {
                    List<DUMyTask> duMyTasks = (List<DUMyTask>) asyncOperation.getResult();
                    LogUtils.e("onResult", "size=" + duMyTasks.size());
                    if (duMyTasks != null && duMyTasks.size() > 0) {
                        final String account = SPUtils.getInstance().getString(Constant.USERID);
                        String message = "" + Lists.newArrayList(Sets.newHashSet(duMyTasks)).size();
                        AlertDialog.Builder buildDialog = new AlertDialog.Builder(ActivityUtils.getTopActivity());
                        buildDialog.setTitle("提示");
                        buildDialog.setMessage("您有" + message + "条已处理工单待上传，您需要跳转到已处理工单页面去处理吗？");
                        buildDialog.setCancelable(false);
                        buildDialog.setNegativeButton("下次提示", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String key = TimeUtils.getNowString();
                                SPUtils.getInstance(account + "_SaveOrdersTips").put("Tips", key);
                                dialog.dismiss();
                            }
                        });
                        buildDialog.setPositiveButton("去上传", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String key = TimeUtils.getNowString(CommonUtils.getFormat("yyyy-MM-dd"));
                                SPUtils.getInstance(account + "_SaveOrdersTips").put("Tips", key);

                                Intent intent = new Intent(ActivityUtils.getTopActivity(), HistoryOrdersActivity.class);
                                startActivity(intent);
                                finish();
//                                ApplicationsUtil.showMessage(MyOrderListActivity.this, "确定");
                                dialog.dismiss();
                            }
                        });
                        buildDialog.create().show();
                    }
                }
            }
        });
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
    public void onFindKehuInfo(CustomerInfoFindResult entity) {
//        ToastUtils.showShort("客户详情");
        Intent intent = new Intent(this, UserDetailInformationActivity.class);
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
        mEventBus.unregister(this);
    }
}
