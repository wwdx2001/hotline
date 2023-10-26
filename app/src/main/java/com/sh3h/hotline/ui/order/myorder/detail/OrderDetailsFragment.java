package com.sh3h.hotline.ui.order.myorder.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.maning.mndialoglibrary.MProgressDialog;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.response.CLJBBean;
import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.data.entity.response.DUBillBaseInfo;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.data.entity.response.DUOrder;
import com.sh3h.dataprovider.data.entity.response.DUProcess;
import com.sh3h.dataprovider.data.entity.response.FYLYBean;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.BaseActivity;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.sh3h.hotline.ui.bill.detail.UserDetailInformationActivity;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * 延期-详细信息
 * Created by zhangjing on 2016/9/12.
 */
public class OrderDetailsFragment extends ParentFragment implements
        MenuItem.OnMenuItemClickListener, View.OnClickListener, OrderDetailsMvpView {


    @Inject
    OrderDetailsPresenter mOrderDetailsPresenter;
    @BindView(R.id.tv1)
    TextView mTv1;
    @BindView(R.id.tv_acctId)
    TextView mTvAcctId;
    @BindView(R.id.view_navigation)
    View mView;
    @BindView(R.id.tv_faId)
    TextView mTvFaId;
    @BindView(R.id.tv_caseId)
    TextView mTvCaseId;
    @BindView(R.id.tv_oldCaseId)
    TextView mTvOldCaseId;
    @BindView(R.id.tv_entityName)
    TextView mTvEntityName;
    @BindView(R.id.tv_disPatGrp)
    TextView mTvDisPatGrp;
    @BindView(R.id.tv_ldsj)
    TextView mTvLdsj;
    @BindView(R.id.tv_fsdz)
    TextView mTvFsdz;
    @BindView(R.id.tv_contactValue)
    TextView mTvContactValue;
    @BindView(R.id.tv_mobile)
    TextView mTvMobile;
    @BindView(R.id.tv_fyly)
    TextView mTvFyly;
    @BindView(R.id.tv_faTypeCd)
    TextView mTvFaTypeCd;
    @BindView(R.id.tv_fynr)
    TextView mTvFynr;
    @BindView(R.id.tv_cljb)
    TextView mTvCljb;
    @BindView(R.id.tv_clsx)
    TextView mTvClsx;
    @BindView(R.id.tv_repCd)
    TextView mTvRepCd;
    @BindView(R.id.tv_comment)
    TextView mTvComment;
    @BindView(R.id.tv_creDttm)
    TextView mTvCreDttm;
    @BindView(R.id.ib_phone)
    ImageButton mIbPhone;
    @BindView(R.id.ib_tel)
    ImageButton mIbTel;

    @BindView(R.id.tv_yysj)
    TextView mTvYysj;

    private Unbinder mUnbinder;


    private DUMyTask mTask;
    private String strChooseDate;


    //工单查询传值
    private DUOrder mDUOrder;
    private DUProcess mDUProcess;
    private DUMyTask mDuMyTask;
    private static final String TAG = "OrderDetailsFragment";
    private Disposable mDisposable1;
    private Map<String, String> map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate" + this);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        map = new HashMap<>();
        List<FYLYBean> fylyBeans = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getFYLYBeanDao().loadAll();
        List<CLJBBean> cljbBeans = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getCLJBBeanDao().loadAll();
        for (int i = 0; i < fylyBeans.size(); i++) {
            map.put(fylyBeans.get(i).getLY_ID(), fylyBeans.get(i).getDESCR());
        }
        for (int i = 0; i < cljbBeans.size(); i++) {
            map.put(cljbBeans.get(i).getCLJB_ID(), cljbBeans.get(i).getDESCR());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mOrderDetailsPresenter.attachView(this);
        initView();
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            throw new NullPointerException("bundle is null");
        }
        mDuMyTask = bundle.getParcelable(Constant.DUMyTask);
//        mTvAcctId.setOnClickListener(this);
        mView.setOnClickListener(this);
        mIbPhone.setOnClickListener(this);
        mIbTel.setOnClickListener(this);
//        mOrigin = bundle.getInt(Constant.ORIGIN);
//        mState = bundle.getInt(Constant.TASKSTATE);
//        taskId = bundle.getString(Constant.TASK_ID);
//        mIsDataUpload = bundle.getBoolean(Constant.DATA_IS_UPLOAD);
        if (mDuMyTask != null) {
            mTvAcctId.setText(mDuMyTask.getAcctId());
            mTvFaId.setText(mDuMyTask.getFaId());
            mTvCaseId.setText(mDuMyTask.getCaseId());
            mTvOldCaseId.setText(mDuMyTask.getOldCaseId());
            mTvDisPatGrp.setText(mDuMyTask.getDisPatGrp());
            mTvLdsj.setText(mDuMyTask.getLdsj());
            mTvComment.setText(mDuMyTask.getComment());
            if (map != null && !StringUtils.isEmpty(map.get(mDuMyTask.getFyly()))) {
                mTvFyly.setText(map.get(mDuMyTask.getFyly()));
            } else {
                mTvFyly.setText(mDuMyTask.getFyly());
            }
            mTvFaTypeCd.setText(mDuMyTask.getFaTypeCd());
            mTvFynr.setText(mDuMyTask.getFynr());
            mTvFsdz.setText(mDuMyTask.getFsdz());
            mTvContactValue.setText(mDuMyTask.getContactValue());
            mTvMobile.setText(mDuMyTask.getMobile());
            if (map != null && !StringUtils.isEmpty(map.get(mDuMyTask.getCljb()))) {
                mTvCljb.setText(map.get(mDuMyTask.getCljb()));
            } else {
                mTvCljb.setText(mDuMyTask.getCljb());
            }
            mTvClsx.setText(mDuMyTask.getClsx());
            mTvRepCd.setText(mDuMyTask.getRepCd());
            mTvEntityName.setText(mDuMyTask.getEntityName());
            mTvCreDttm.setText(mDuMyTask.getCreDttm());
            mTvYysj.setText(mDuMyTask.getYysj());
        }
//        mLLAcctId.setOnClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
        MenuItem save = menu.findItem(R.id.action_save);
        save.setOnMenuItemClickListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }


    /**
     * 子标题
     *
     * @param title
     * @param subTitle
     */

    public void initToolBar(int title, int subTitle) {
//        setHasOptionsMenu(true);
//        mToolbar.setNavigationIcon(R.mipmap.arrow);
//        mToolbar.setTitle(title);
//        mToolbar.setSubtitle(subTitle);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mOrderDetailsPresenter.detachView();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        ApplicationsUtil.showMessage(getActivity(), "保存详细信息");
        return false;
    }

    @Override
    public void onClick(View v) {
//        Calendar mycalendar = Calendar.getInstance();
//        mYear = mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
//        month = mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
//        day = mycalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天
//        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                mYear = year;
//                month = monthOfYear;
//                day = dayOfMonth;
//                updateDate();
//            }
//        }, mYear, month, day);
//        datePickerDialog.show();
        switch (v.getId()) {
            case R.id.view_navigation:
                navigationUserInfo();
                break;
            case R.id.ib_phone:
            case R.id.ib_tel:
                callPhone(v.getId());
                break;
        }
    }

    /**
     * 拨打电话
     *
     * @param id
     */
    private void callPhone(int id) {
        String phone = "";
        switch (id) {
            case R.id.ib_phone:
                phone = mTvMobile.getText().toString().trim();
                break;
            case R.id.ib_tel:
                phone = mTvContactValue.getText().toString().trim();
                break;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 跳转到客户详细信息
     */
    private void navigationUserInfo() {
//查询客户基本信息
        mDisposable1 = EasyHttp.post(URL.CustomerInfoQuery)
                .params("acctId", mTvAcctId.getText().toString().trim() == null ? ""
                        : mTvAcctId.getText().toString().trim())
                .execute(new CallBackProxy<CustomApiResult<CustomerInfoFindResult>,
                        CustomerInfoFindResult>(new CustomCallBack<CustomerInfoFindResult>() {

                    @Override
                    public void onStart() {
                        MProgressDialog.showProgress(getActivity(), "加载中...");
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                    }

                    @Override
                    public void onSuccess(CustomerInfoFindResult entity) {
                        if (entity != null) {
                            LogUtils.i(entity.toString());
                            Intent intent = new Intent(getActivity(), UserDetailInformationActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(Constant.BILLBASEINFO, entity);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            ToastUtils.showShort("没有数据");
                        }
                    }

                    @Override
                    public void onCompleted() {
                        MProgressDialog.dismissProgress();
                    }
                }) {
                });
    }


    /**
     * 更新日期
     */
    private void updateDate() {
//        String mCurrentMonth = (month + 1) + "";
//        if (mCurrentMonth.length() == 1) {
//            mCurrentMonth = "0" + mCurrentMonth;
//        }
//        String mCurrentDay = day + "";
//        if (mCurrentDay.length() == 1) {
//            mCurrentDay = "0" + mCurrentDay;
//        }
//        mTextDate.setText(mYear + "-" + mCurrentMonth + "-" + mCurrentDay);
//        strChooseDate = mYear + mCurrentMonth + mCurrentDay;
    }

    @Override
    public void onGetTaskInfo(DUMyTask task) {
        mTask = task;
//        mGuestNumder.setText(task.getCustomerId());
//        if (task.getExtend() != null) {
//            try {
//                JSONObject jsonObject = new JSONObject(task.getExtend());
//                if (!TextUtil.isNullOrEmpty(jsonObject.getString("serverTaskId"))) {
//                    String serverTaskId = jsonObject.getString("serverTaskId");
//                    mTaskNumder.setText(serverTaskId);
//                } else {
//                    mTaskNumder.setText(task.getTaskId());
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else {
//            mTaskNumder.setText(task.getTaskId());
//        }
//        mFanYingRen.setText(task.getIssuer());
//        mStation.setText(task.getStation());
//        SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        mHappenTime.setText(s1.format(new Date(task.getIssueTime())));
//        mBeizhu.setText(task.getReceviceComment());
////        if (mState == TaskState.HANDLE) {
//        mXiaoGenHao.setText(task.getCardId());
//        if (!TextUtil.isNullOrEmpty(task.getCardId())) mBtnXiaogenhao.setEnabled(true);
//        mHuMing.setText(task.getIssueName());
//        mAddress.setText(task.getIssueAddress());
//        mTelephone.setText(task.getTelephone());
//        mMobile.setText(task.getMoblie());//手机
//        }
    }

    @Override
    public void onGetHistoryTaskInfo(DUHistoryTask duHistoryTask) {

    }

    @Override
    public void showMessage(String message) {
        hideProgress();
        ApplicationsUtil.showMessage(getActivity(), message);
    }

    @Override
    public void showMessage(int resId) {
        hideProgress();
        ApplicationsUtil.showMessage(getActivity(), resId);
    }

    @Override
    public void onIntent(DUBillBaseInfo info) {
        hideProgress();
        Intent intent = new Intent(getActivity(), UserDetailInformationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.BILLBASEINFO, info);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public DUMyTask getmTask() {
        return mTask;
    }

//    public EditText getmEditReason() {
//        return mEditReason;
//    }

    public String getmTextDate() {
        return strChooseDate;
    }

    public String getDate() {
        return mTvClsx.getText().toString();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EasyHttp.cancelSubscription(mDisposable1);
//        null.unbind();
    }
}
