package com.sh3h.hotline.ui.order.self.create.receipt;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.maning.mndialoglibrary.MProgressDialog;
import com.sh3h.dataprovider.BaseApplication;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.entity.response.CLJBBean;
import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.data.entity.response.DUBillBaseInfo;
import com.sh3h.dataprovider.data.entity.response.DUWord;
import com.sh3h.dataprovider.data.entity.response.FYLXBean;
import com.sh3h.dataprovider.data.entity.response.FYNRBean;
import com.sh3h.dataprovider.data.entity.response.XBBX;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.FYLXBeanDao;
import com.sh3h.dataprovider.greendaoDao.FYNRBeanDao;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.viewholder.SPAdapter;
import com.sh3h.hotline.entity.ZikaidanJLEntity;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.service.SyncType;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.sh3h.hotline.ui.bill.detail.UserDetailInformationActivity;
import com.sh3h.hotline.ui.multimedia.MultimediaFragment;
import com.sh3h.hotline.ui.order.self.create.CreateSelfOrderActivity;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dengzhimin on 2016/9/18.
 */
public class ReceiptFragment extends ParentFragment implements View.OnClickListener,
        ReceiptMvpView {

    @Inject
    ReceiptPresenter mPresenter;

    @Inject
    Bus mBus;
    @BindView(R.id.cb_xianchangchuli)
    CheckBox mCbXianchangchuli;
    @BindView(R.id.et_fuwudianbh)
    public EditText mEtFuwudianbh;
    @BindView(R.id.et_fanyinglx)
    EditText mEtFanyinglx;
    @BindView(R.id.et_fanyingnr)
    EditText mEtFanyingnr;
    @BindView(R.id.et_chulijb)
    EditText mEtChulijb;
    @BindView(R.id.et_fashengdizhi)
    EditText mEtFashengdizhi;
    @BindView(R.id.et_xbbx)
    EditText mEtXbbx;


//    @BindView(R.id.et_xiaogenhao)
//    EditText mEdXiaoGenHao;
//
//    @BindView(R.id.et_huming)
//    EditText mEdHuMing;
//
//    @BindView(R.id.et_fanyingren)
//    EditText mEdFanYingRen;
//
//    @BindView(R.id.et_fashengdizhi)
//    EditText mEdFaShengDiZhi;
//
//    @BindView(R.id.et_lianxidianhua)
//    EditText mEdLianXiDianHua;
//
//    @BindView(R.id.sp_fanyingleixing)
//    AppCompatSpinner mSpFanYingLeiXing;
//
//    @BindView(R.id.sp_fanyingneirong)
//    AppCompatSpinner mSpFanYingNeiRong;
//
//    @BindView(R.id.sp_fanyingquming)
//    AppCompatSpinner mSpFanYingQuMing;//反映区名
//
//    @BindView(R.id.sp_fanyinglaiyuan)
//    AppCompatSpinner mSpFanYingLaiYuan;//反映来源
//
//    @BindView(R.id.sp_chulijibie)
//    AppCompatSpinner mSpChuLiJiBie;
//
//    @BindView(R.id.et_shoulibeizhu)
//    EditText mEdShouLiBeiZhu;
//
//    @BindView(R.id.btn_xiaogenhao)
//    Button mBtnXiaoGenHao;
//
//    @BindString(R.string.text_error_no_null)
//    String mErrorHint;
//
//    @BindView(R.id.cb_xianchangchuli)
//    CheckBox mCheckBox;
//
//    @BindView(R.id.ll_layout_date_time)
//    LinearLayout mLayoutDateTime;
//
//    @BindView(R.id.tv_start_date_show)
//    TextView mStartDateShow;
//
//    @BindView(R.id.tv_end_date_show)
//    TextView mEndDateShow;
//
//    @BindString(R.string.text_please_select)
//    String mDefaultSelectedValue;

    private boolean isStartDate;//是否是开始日期

    private long mStartDateUtc;

    private long mEndDateUtc;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private static final String TAG = "ReceiptFragment";
    private List<String> mChuLiJiBieData = null;
    private List<String> mFanYingLeiXingData = null;
    private List<String> mFanYingNeiRongData = null;
    private List<String> mFanYingQuMingData = null;
    private List<String> mFanYingLaiYuanData = null;

    private ArrayAdapter<String> mChuLiJiBieAdapter = null;
    private ArrayAdapter<String> mFanYingLeiXingAdapter = null;
    private ArrayAdapter<String> mFanYingNeiRongAdapter = null;
    private ArrayAdapter<String> mFanYingQuMingAdapter = null;
    private ArrayAdapter<String> mFanYingLaiYuanAdapter = null;
    private Map<String, String> xbbxMap = new HashMap<>();

    private List<DUWord> mFanYingLeiXingWords;

    private Unbinder mUnbinder;

    private String mTaskId;
    private String[] mContents;
    private String fylxParams;
    private String cljbParams;
    private List<FYNRBean> mFynrBeanList;
    private List<String> mFynrList;
    private String xbbxParams;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CreateSelfOrderActivity) getActivity()).getActivityComponent().inject(this);
        mPresenter.attachView(this);
        mBus.register(this);
        mTaskId = getArguments().getString(Constant.TASK_ID, "123456");
        List<XBBX> xbbxes = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getXBBXDao().loadAll();
        for (int i = 0; i < xbbxes.size(); i++) {
            xbbxMap.put(xbbxes.get(i).getMTR_TYPE_CD(), xbbxes.get(i).getDESCR());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receipt, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mEtChulijb.setOnClickListener(this);
        mEtFanyinglx.setOnClickListener(this);
        mEtFanyingnr.setOnClickListener(this);
        mEtFuwudianbh.setOnClickListener(this);
        mEtXbbx.setOnClickListener(this);
//        mStartDateShow.setOnClickListener(this);
//        mEndDateShow.setOnClickListener(this);
//        initSpinner();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mUnbinder.unbind();
        mPresenter.detachView();
        mBus.unregister(this);
    }

    @Override
    protected void lazyLoad() {

    }

    private void initSpinner() {
        mChuLiJiBieData = new ArrayList<>();
        mFanYingLeiXingData = new ArrayList<>();
        mFanYingNeiRongData = new ArrayList<>();
        mFanYingQuMingData = new ArrayList<>();
        mFanYingLaiYuanData = new ArrayList<>();

        mChuLiJiBieAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, mChuLiJiBieData);
        mFanYingLeiXingAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, mFanYingLeiXingData);
        mFanYingNeiRongAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, mFanYingNeiRongData);
        mFanYingQuMingAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, mFanYingQuMingData);
        mFanYingLaiYuanAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, mFanYingLaiYuanData);

//        mSpChuLiJiBie.setAdapter(mChuLiJiBieAdapter);
//        mSpFanYingLeiXing.setAdapter(mFanYingLeiXingAdapter);
//        mSpFanYingNeiRong.setAdapter(mFanYingNeiRongAdapter);
//        mSpFanYingQuMing.setAdapter(mFanYingQuMingAdapter);
//        mSpFanYingLaiYuan.setAdapter(mFanYingLaiYuanAdapter);
//
//        mSpFanYingLeiXing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {//默认值
//                    mFanYingNeiRongData.add(mDefaultSelectedValue);
//                    mFanYingNeiRongAdapter.notifyDataSetChanged();
//                } else {
//                    mPresenter.initFanYingNeiRongSpinner(mFanYingLeiXingWords.get(position - 1).getValueEx());
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        mSpChuLiJiBie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                LogUtil.i(TAG, "mChuLiJiBieData:" + mChuLiJiBieData.get(position));
//                if (mChuLiJiBieData.get(position).equals("约期")) {
//                    mLayoutDateTime.setVisibility(View.VISIBLE);
//                } else {
//                    mLayoutDateTime.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        mPresenter.initChuLiJiBieSpinner();
        mPresenter.initFanYingLeiXingSpinner();
        mPresenter.initIssueAreaSpinner();
        mPresenter.initIssueOriginSpinner();
    }

    private void showDatePicker() {
        Calendar mycalendar = Calendar.getInstance();
        mYear = mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        mMonth = mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
        mDay = mycalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = (monthOfYear + 1);
                mDay = dayOfMonth;
                showTimePicker();
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;
                updateDateTime();
            }
        }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    /**
     * 更新日期时间
     */
    private void updateDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");//小写的mm表示的是分钟
        String dateStr = mYear + "-" + mMonth + "-" + mDay + " " + mHour + ":" + mMinute + "";
        Date date = null;
        try {
            date = sdf.parse(dateStr);
            if (isStartDate) {
//                mStartDateShow.setText(dateStr);
//                mStartDateUtc = date.getTime();
//                if ((mEndDateUtc != 0) && (mEndDateUtc < mStartDateUtc)) {
//                    ApplicationsUtil.showMessage(getActivity(), R.string.toast_time_error);
//                    mStartDateUtc = 0;
//                    mStartDateShow.setText("请选择时间");
//                }
            } else {
//                mEndDateShow.setText(dateStr);
//                mEndDateUtc = date.getTime();
//                if ((mStartDateUtc != 0) && (mEndDateUtc < mStartDateUtc)) {
//                    ApplicationsUtil.showMessage(getActivity(), R.string.toast_time_error);
//                    mEndDateUtc = 0;
//                    mEndDateShow.setText("请选择时间");
//                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

//    @OnClick(R.id.btn_xiaogenhao)
//    public void onClick() {
//        showProgress(getString(R.string.dialog_loading));
////        mPresenter.searchBill(mEdXiaoGenHao.getText().toString().trim());
//    }

//    @OnTextChanged(value = R.id.et_xiaogenhao, callback = OnTextChanged.Callback.TEXT_CHANGED)
//    public void onTextChanged(CharSequence sequence) {
//        if (TextUtil.isNullOrEmpty(sequence.toString())) {
//            mBtnXiaoGenHao.setEnabled(false);
//        } else {
//            mBtnXiaoGenHao.setEnabled(true);
//        }
//    }

    @Subscribe
    public void onUploadCreateSelfOrder(UIBusEvent.UploadCreateSelfOrder selfOrder) {
//        if (StringUtils.isEmpty(mEtFuwudianbh.getText().toString()) ||
//                StringUtils.isEmpty(mEtChulijb.getText().toString()) ||
//                StringUtils.isEmpty(mEtFanyingnr.getText().toString()) ||
//                StringUtils.isEmpty(mEtFanyinglx.getText().toString()) ||
//                StringUtils.isEmpty(mEtFashengdizhi.getText().toString())) {
//            ToastUtils.showShort("请填写完必要信息");
//            return;
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////        builder.setTitle(getString(R.string.text_prompt));
//        builder.setMessage(getString(R.string.text_save_update));
//        builder.setPositiveButton(getString(R.string.dialog_btn_save),
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //notice fragment do something
////                        if (mCbXianchangchuli.isChecked()) {
////                            ToastUtils.showShort("现场处理保存成功");
////                        } else {
////                            ToastUtils.showShort("非现场处理保存成功");
////                        }
//                        HttpParams params = new HttpParams();
//                        params.put("spId", mEtFuwudianbh.getText().toString());
//                        params.put("address", mEtFashengdizhi.getText().toString());
//                        params.put("faTypeCd", mEtFanyinglx.getText().toString());
//                        params.put("fynr", mEtFanyingnr.getText().toString());
//                        params.put("cljb", mEtChulijb.getText().toString());
//                        params.put("creDttm", TimeUtils.date2String(new Date()));
//                        params.put("UserId", "test");
//                        //作业人（提交人）
//                        params.put("repCd", mCbXianchangchuli.isChecked() ? "userId" : "");
//                        EasyHttp.post(URL.SelfOpeningOrder)
//                                .params(params)
//                                .execute(new CallBackProxy<CustomApiResult<ServiceResultEntity>,
//                                        ServiceResultEntity>(new CustomCallBack<ServiceResultEntity>() {
//                                    @Override
//                                    public void onStart() {
//                                        super.onStart();
//                                        MProgressDialog.showProgress(getActivity(), "正在保存上传");
//                                    }
//
//                                    @Override
//                                    public void onSuccess(ServiceResultEntity serviceResultEntity) {
//                                        ToastUtils.showShort(serviceResultEntity.getCmMsgDesc());
//                                        //保存到自开单记录
//                                        ZikaidanJLEntity zikaidanEntity = new ZikaidanJLEntity();
//                                        zikaidanEntity.setIsXianChangCL(mCbXianchangchuli.isChecked() ?
//                                                1 : 0);
//                                        zikaidanEntity.setFuwudianBH(mEtFuwudianbh.getText().toString());
//                                        zikaidanEntity.setFanyingLX(mEtFanyinglx.getText().toString());
//                                        zikaidanEntity.setFanyingNR(mEtFanyingnr.getText().toString());
//                                        zikaidanEntity.setChuliJB(mEtChulijb.getText().toString());
//                                        zikaidanEntity.setFashengDZ(mEtFashengdizhi.getText().toString());
//                                        zikaidanEntity.setCommitDate(TimeUtils.date2String(new Date()));
//                                        GreenDaoUtils.getDaoSession(getActivity()).getZikaidanJLEntityDao()
//                                                .insert(zikaidanEntity);
//
//                                        //清空， 方便继续开单
//                                        mCbXianchangchuli.setChecked(false);
//                                        mEtFuwudianbh.setText("");
//                                        mEtFanyinglx.setText("");
//                                        mEtFanyingnr.setText("");
//                                        mEtChulijb.setText("");
//                                        mEtFashengdizhi.setText("");
//                                    }
//
//                                    @Override
//                                    public void onCompleted() {
//                                        super.onCompleted();
//                                        MProgressDialog.dismissProgress();
//                                    }
//                                }) {
//                                });
//                    }
//                });
//        builder.show();
        String fuwudianBH = mEtFuwudianbh.getText().toString().trim();//服务点编号
//        String fuwudianBH = "0951703387";//服务点编号
        String fanyingLX = mEtFanyinglx.getText().toString().trim();//反应类型
        String fanyingNR = mEtFanyingnr.getText().toString().trim();//反应内容
        String chuliJB = mEtChulijb.getText().toString().trim();//处理级别
        String fashengDZ = mEtFashengdizhi.getText().toString().trim();//发生地址
        String xbbx = mEtXbbx.getText().toString().trim();//新表表型
        if (StringUtils.isEmpty(fuwudianBH) || StringUtils.isEmpty(fanyingLX) ||
                StringUtils.isEmpty(fanyingNR) || StringUtils.isEmpty(chuliJB) ||
                StringUtils.isEmpty(fashengDZ) || StringUtils.isEmpty(xbbx)) {
            ToastUtils.showShort("请先将信息填写完整");
            return;
        }

//        DUCreateSelfOrder order = new DUCreateSelfOrder();
//        order.setCardId(xiaogenhao);
//        order.setLocalTaskId(fuwudianBH);
//        order.setIssueName(huming);
//        order.setIssuer(fanyingren);
//        order.setIssueAddress(fashengDZ);
//        order.setTelephone(lainxidianhua);
//        order.setIssueType(fanyingLX);
//        order.setIssueContent(fanyingNR);
//        order.setIssueArea(fanyingquming);//区名
//        order.setIssueOrigin(fanyinglaiyuan);//来源
//        order.setReplyClass(chuliJB);
//        order.setReceviceComment(shoulibeizhu);
//        order.setIssueTime(System.currentTimeMillis());
//        order.setReplyImmediately(mCbXianchangchuli.isChecked());
//        order.setUserId(mPresenter.getUserId());
//        if (chulijibie.equals("约期")) {
//            order.setStartTime(mStartDateUtc);
//            order.setEndTime(mEndDateUtc);
//        } else {
//            mStartDateUtc = System.currentTimeMillis();
//            order.setStartTime(mStartDateUtc);
//        }
//        保存并上传自开单
//        mPresenter.saveCreateSelfOrderAndHistory(getContext(), order, mCbXianchangchuli.isChecked());
        ZikaidanJLEntity zikaidanJLEntity = new ZikaidanJLEntity();
        zikaidanJLEntity.setFuwudianBH(fuwudianBH);
        zikaidanJLEntity.setChuliJB(cljbParams);
        zikaidanJLEntity.setFanyingLX(fylxParams);
        zikaidanJLEntity.setXbbx(xbbxParams);
        zikaidanJLEntity.setFanyingNR(fanyingNR);
        zikaidanJLEntity.setFashengDZ(fashengDZ);
        zikaidanJLEntity.setZuoyeren("userId");
        zikaidanJLEntity.setTijiaoren(mCbXianchangchuli.isChecked() ? "userId" : "");
        zikaidanJLEntity.setCommitDate(TimeUtils.date2String(new Date()));
        mPresenter.saveAndCommitOrder((CreateSelfOrderActivity) getActivity(), mCbXianchangchuli.isChecked(), zikaidanJLEntity);
    }

    @Override
    public void onToDealOrder(boolean isDeal) {
        LogUtil.i(TAG, "onToDealOrder");
        Activity activity = getActivity();
        if (activity instanceof ParentActivity) {
            //((ParentActivity)activity).uploadCreateSelfOrder(mTaskId);
        }

//        if (isDeal) {
//            String fanyingleixing = null;
//            if (mSpFanYingLeiXing.getSelectedItem() != null) {
//                fanyingleixing = mSpFanYingLeiXing.getSelectedItem().toString();//反应类型(not null)
//            }
//
//            String fanyingneirong = null;
//            if (mSpFanYingNeiRong.getSelectedItem() != null) {
//                fanyingneirong = mSpFanYingNeiRong.getSelectedItem().toString();//反应内容(not null)
//            }
//            Intent intent = new Intent(getActivity(), HandleOrderActivity.class);
//            intent.putExtra(Constant.ORIGIN, Constant.ORIGIN_CREATE_SELF_ORDER);
//            intent.putExtra(Constant.TASK_ID, mTaskId);
//            intent.putExtra(Constant.ISSUE_TYPE, fanyingleixing);
//            intent.putExtra(Constant.ISSUE_CONTENT, fanyingneirong);
//            startActivity(intent);
//        }

        getActivity().finish();
    }

    @Override
    public void onHint(int resId) {
        hideProgress();
        ApplicationsUtil.showMessage(getActivity(), resId);
    }

    @Override
    public void onHint(String error) {
        ApplicationsUtil.showMessage(getActivity(), error);
    }

    @Override
    public void onSaveSuccess(String savesuccess) {
        ToastUtils.showShort(savesuccess);
        getActivity().finish();
    }

    @Override
    public void onUploadOrder(String taskId, boolean isDeal) {
        startSyncService(SyncType.UPLOAD_All_CREATE_SELF_ORDER.ordinal(), taskId);
    }

    @Override
    public void onInitFanYingLeiXingSpinner(List<DUWord> words) {
//        mFanYingLeiXingWords = words;
//        mFanYingLeiXingData.add(mDefaultSelectedValue);
//        for (DUWord word : words) {
//            mFanYingLeiXingData.add(word.getName());
//        }
//        mFanYingLeiXingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onInitChuLiJiBieSpinner(List<DUWord> words) {
//        mChuLiJiBieData.add(mDefaultSelectedValue);
//        for (DUWord word : words) {
//            mChuLiJiBieData.add(word.getName());
//        }
//        mChuLiJiBieAdapter.notifyDataSetChanged();
    }

    @Override
    public void onInitFanYingNeiRongSpinner(List<DUWord> words) {
        mFanYingNeiRongData.clear();
        for (DUWord word : words) {
            mFanYingNeiRongData.add(word.getName());
        }
        mFanYingNeiRongAdapter.notifyDataSetChanged();
    }

    @Override
    public void onInitIssueAreaSpinner(List<DUWord> words) {
//        mFanYingQuMingData.add(mDefaultSelectedValue);
//        for (DUWord word : words) {
//            mFanYingQuMingData.add(word.getName());
//        }
//        mFanYingQuMingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onInitIssueOriginSpinner(List<DUWord> words) {
//        mFanYingLaiYuanData.add(mDefaultSelectedValue);
//        for (DUWord word : words) {
//            mFanYingLaiYuanData.add(word.getName());
//        }
//        mFanYingLaiYuanAdapter.notifyDataSetChanged();
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

//    @Override
//    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
//        String text = getDate2String(millseconds);
//        if (isStartDate) {
//            mStartDateShow.setText(text);
//            if ((mEndDateUtc != 0)
//                    && (mEndDateUtc < mStartDateUtc)) {
//                mStartDateShow.setText("请选择日期");
//                mStartDateUtc = 0;
//                ApplicationsUtil.showMessage(getActivity(), "结束日期不得小于开始日期");
//            }
//        } else {
//            mEndDateShow.setText(text);
//            if ((mStartDateUtc != 0)
//                    && (mEndDateUtc < mStartDateUtc)) {
//                mEndDateShow.setText("请选择日期");
//                mEndDateUtc = 0;
//                ApplicationsUtil.showMessage(getActivity(), "结束日期不得小于开始日期");
//            }
//        }
//    }
//
//    private String getDate2String(long time) {
//        Date date = new Date(time);
//        long timeUtc = date.getTime() - 28800000;
//        if (isStartDate) {
//            mStartDateUtc = timeUtc;
//        } else {
//            mEndDateUtc = timeUtc;
//        }
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return sdf.format(date);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.tv_start_date_show:
//                isStartDate = true;
//                showDatePicker();
//                break;
//            case R.id.tv_end_date_show:
//                isStartDate = false;
//                showDatePicker();
//                break;
            case R.id.et_fuwudianbh:
                showFuwudianBHDialog();
                break;
            case R.id.et_fanyinglx:
            case R.id.et_fanyingnr:
            case R.id.et_chulijb:
            case R.id.et_xbbx://新表表型
                if (GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getFYLXBeanDao().loadAll().size() == 0
                        || GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getCLJBBeanDao().loadAll().size() == 0
                        || GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getFYNRBeanDao().loadAll().size() == 0
                        || GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getXBBXDao().loadAll().size() == 0) {
                    ToastUtils.showShort("正在下载词条数据，请稍后再试");
                    BaseApplication.getInstance().getWordList();
                } else {
                    showWordInfoDialog(v.getId());
                }
                break;

        }
    }

    private void showFuwudianBHDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("请输入账户编号");
        View view = View.inflate(getActivity(), R.layout.dialog_fuwudianbh, null);
        final EditText editText = (EditText) view.findViewById(R.id.et_zhanghuBH);
        TextView textView = (TextView) view.findViewById(R.id.tv_search);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(editText.getText().toString())) {
                    ToastUtils.showShort("请输入账户编号");
                    return;
                }
                EasyHttp
                        .post(URL.CustomerInfoQuery)
                        .params("acctId", editText.getText().toString())
                        .execute(new CallBackProxy<CustomApiResult<CustomerInfoFindResult>,
                                CustomerInfoFindResult>(new CustomCallBack<CustomerInfoFindResult>() {

                            @Override
                            public void onStart() {
                                MProgressDialog.showProgress(getActivity(), "加载中..");
                            }

                            @Override
                            public void onSuccess(CustomerInfoFindResult entity) {
                                String address1 = entity.getAddress1() == null ? "" : entity.getAddress1();
                                String address2 = entity.getAddress2() == null ? "" : entity.getAddress2();
                                String address3 = entity.getAddress3() == null ? "" : entity.getAddress3();
                                mEtFashengdizhi.setText(address1 + address2 + address3);
                                List<CustomerInfoFindResult.SpListBean> spList = entity.getSpList();
                                final SPAdapter spAdapter = new SPAdapter(R.layout.item_text, spList);
                                recyclerView.setAdapter(spAdapter);
                                spAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                                        alertDialog.dismiss();
                                        if (!spAdapter.getData().get(i).getSpId().equals(mEtFuwudianbh.getText().toString())) {
                                            if (getActivity() instanceof CreateSelfOrderActivity) {
                                                CreateSelfOrderActivity activity = (CreateSelfOrderActivity) getActivity();
                                                activity.setFuwudianBH(spAdapter.getData().get(i).getSpId());
                                                activity.currentTime = String.valueOf(System.currentTimeMillis());
                                                MultimediaFragment fragment = activity.mMultimediaFragment;
                                                if (fragment != null) {
                                                    fragment.setTaskId(spAdapter.getData().get(i).getSpId());
                                                    fragment.mPictureAdapter.setData(new ArrayList<>());
                                                    fragment.mRecordAdapter.setData(new ArrayList<>());
                                                    fragment.mVideosAdapter.setData(new ArrayList<>());
                                                    fragment.mSignUpLayout.setVisibility(View.GONE);
                                                    fragment.mSignDUMedia = null;
                                                    if (mPresenter != null) {
                                                        mPresenter.upLoadFiles.clear();
                                                    }
                                                }
                                            }
                                        }
                                        mEtFuwudianbh.setText(spAdapter.getData().get(i).getSpId());
                                        List<XBBX> xbbxes = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                                                .getXBBXDao().loadAll();
                                        for (int j = 0; j < xbbxes.size(); j++) {
                                            xbbxMap.put(xbbxes.get(j).getDESCR(), xbbxes.get(j).getMTR_TYPE_CD());
                                        }
                                        xbbxParams = spAdapter.getData().get(i).getNewMtrType() == null ? "" :
                                                spAdapter.getData().get(i).getNewMtrType();
                                        mEtXbbx.setText(xbbxMap.get(xbbxParams));

                                    }
                                });
                            }

                            @Override
                            public void onCompleted() {
                                MProgressDialog.dismissProgress();
                            }
                        }) {
                        });
            }
        });
    }

    /**
     * 弹出词条对话框
     *
     * @param id
     */
    private void showWordInfoDialog(final int id) {
//        final List<FYLXBean> fylxBeanList = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
//                .getFYLXBeanDao().loadAll();

        final List<FYLXBean> fylxBeanList = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getFYLXBeanDao().queryBuilder()
                .where(FYLXBeanDao.Properties.FA_TYPE_CD.notEq("FHGD"))
                .where(FYLXBeanDao.Properties.FA_TYPE_CD.notEq("FHYC")).list();
        final List<CLJBBean> chuliBeanList = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getCLJBBeanDao().loadAll();
        final List<XBBX> xbbxBeanList = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getXBBXDao().loadAll();
        //反应类型
        final List<String> fanyingleixingList = Lists.transform(fylxBeanList, new Function<FYLXBean, String>() {
            @Nullable
            @Override
            public String apply(@Nullable FYLXBean bwbdlBean) {
                return bwbdlBean.getFA_TYPE_DESCR();
            }
        });


        //处理级别
        final List<String> chulijibieList = Lists.transform(chuliBeanList, new Function<CLJBBean, String>() {
            @Nullable
            @Override
            public String apply(@Nullable CLJBBean bwbdlBean) {
                return bwbdlBean.getDESCR();
            }
        });

        //新表表型
        final List<String> xbbxList = Lists.transform(xbbxBeanList, new Function<XBBX, String>() {
            @Nullable
            @Override
            public String apply(@Nullable XBBX xbbx) {
                return xbbx.getDESCR();
            }
        });
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String title = "";
        if (id == R.id.et_fanyinglx) {
            mContents = fanyingleixingList.toArray(new String[fanyingleixingList.size()]);
            title = "反应类型";
        } else if (id == R.id.et_fanyingnr) {
            if (StringUtils.isEmpty(mEtFanyinglx.getText().toString())) {
                ToastUtils.showShort("请选择反应类型");
                return;
            }

            mContents = mFynrList.toArray(new String[mFynrList.size()]);
            title = "反应内容";
        } else if (id == R.id.et_chulijb) {
            mContents = chulijibieList.toArray(new String[chulijibieList.size()]);
            title = "处理级别";
        } else if (id == R.id.et_xbbx) {
            mContents = xbbxList.toArray(new String[xbbxList.size()]);
            title = "新表表型";
        }
        builder.setTitle("请选择" + title);
        builder.setItems(mContents, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (id) {
                    case R.id.et_fanyinglx:
                        fylxParams = fylxBeanList.get(which).getFA_TYPE_CD();
                        mEtFanyinglx.setText(mContents[which]);
                        mFynrBeanList = GreenDaoUtils.getDaoSession(BaseApplication.getInstance())
                                .getFYNRBeanDao().queryBuilder()
                                .where(FYNRBeanDao.Properties.FA_TYPE_CD.eq(fylxParams))
                                .list();
                        //反应内容
                        mFynrList = Lists.transform(mFynrBeanList, new Function<FYNRBean, String>() {
                            @Nullable
                            @Override
                            public String apply(@Nullable FYNRBean bwbdlBean) {
                                return bwbdlBean.getDESCR();
                            }
                        });
                        mEtFanyingnr.setText(mFynrList.size() > 0 ? mFynrList.get(0) : "");
                        break;
                    case R.id.et_fanyingnr:
                        mEtFanyingnr.setText(mContents[which]);
                        break;
                    case R.id.et_chulijb:
                        cljbParams = chuliBeanList.get(which).getCLJB_ID();
                        mEtChulijb.setText(mContents[which]);
                        break;
                    case R.id.et_xbbx:
                        xbbxParams = xbbxBeanList.get(which).getMTR_TYPE_CD();
                        mEtXbbx.setText(mContents[which]);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

//    @Subscribe
//    public void onFinish(UIBusEvent.SyncFinish sync) {
//        ApplicationsUtil.showMessage(getActivity(), sync.getMessage());
//        onToDealOrder(mCheckBox.isChecked());
//    }
}
