package com.sh3h.hotline.ui.query;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.response.DUWord;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 工单查询
 * Created by zhangjing on 2016/9/18.
 */
public class QueryOrderActivity extends ParentActivity implements QueryOrderMvpView,
        View.OnClickListener {

    public final static int REQUEST_CHOOSE_STATION = 1004;
    public final static int RESULT_CHOOSE_STATION = 1005;
    public final static int REQUEST_CHOOSE_ACCEPT_STATION = 1006;

    private Unbinder mUnbinder;

    @BindView(R.id.btn_query)
    Button mBtnQuery;

    @BindView(R.id.iv_choose_login_site)
    ImageView mIVChooseLoginSite;//选择登录站点

    @BindView(R.id.iv_back_login_site)
    ImageView mIVBackLoginSite;//回退“供水热线”

    @BindView(R.id.iv_clear_login_site)
    ImageView mIVClearLoginSite;//清除“登录站点”

    @BindView(R.id.iv_choose_accept_site)
    ImageView mIVChooseAcceptSite;//选择受理站点

    @BindView(R.id.iv_back_accept_site)
    ImageView mIVBackAcceptSite;

    @BindView(R.id.iv_clear_accept_site)
    ImageView mIVClearAcceptSite;

    @BindView(R.id.tv_login_station)
    TextView mTVLoginStation;

    @BindView(R.id.tv_accept_station)
    TextView mTVAcceptStation;

    @BindView(R.id.et_task_id)
    EditText mEditTaskID;

    @BindView(R.id.et_dizhi)
    EditText mEditAddress;

    @BindView(R.id.et_huming)
    EditText mEditHuMing;

    @BindView(R.id.et_lianxidianhua)
    EditText mEditTelephone;

    @Inject
    QueryOrderPresenter mQueryOrderPresenter;

    @BindView(R.id.iv_start_date_calendar)
    ImageView mChooseStartDate;

    @BindView(R.id.iv_end_date_calendar)
    ImageView mChooseEndDate;

    @BindView(R.id.tv_start_date_show)
    TextView mStartDateText;

    @BindView(R.id.sp_fanyinglaiyuan)
    AppCompatSpinner mSpFanYingLaiYuan;//反映来源

    @BindView(R.id.sp_fanyingleixing)
    AppCompatSpinner mSpFanYingLeiXing;//反映类型

    @BindView(R.id.sp_fanyingneirong)
    AppCompatSpinner mSpFanYingNeiRong;//反映内容

    @BindView(R.id.sp_relect_area)
    AppCompatSpinner mSpFanYingQuMing;//反映区名

    @BindString(R.string.text_please_select)
    String mDefaultSelectedValue;

    private ArrayAdapter<String> mFanYingLaiYuanAdapter = null;

    private ArrayAdapter<String> mFanYingLeiXingAdapter = null;

    private ArrayAdapter<String> mFanYingNeiRongAdapter = null;

    private ArrayAdapter<String> mFanYingQuMingAdapter = null;

    private long mStartDateUtc;//开始日期utc

    private long mEndDateUtc;//结束日期utc

    @BindView(R.id.tv_end_date_show)
    TextView mEndDateText;

    @Inject
    Bus mEventBus;

    private int mYear;
    private int month;
    private int day;

    private List<DUWord> mFanYingLeiXingWords;
    private List<String> mFanYingLaiYuanData = null;
    private List<String> mFanYingLeiXingData = null;
    private List<String> mFanYingNeiRongData = null;
    private List<String> mFanYingQuMingData = null;

    private String mLoginStation = "";//登录站点
    private String mAcceptStation = "";//受理站点

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_order);
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);
        initToolBar(R.string.label_query_orders);
        mQueryOrderPresenter.attachView(this);

        mEventBus.register(this);
        initView();

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

    private void initView() {
        mBtnQuery.setOnClickListener(this);
        mIVChooseLoginSite.setOnClickListener(this);
        mIVBackLoginSite.setOnClickListener(this);
        mIVClearLoginSite.setOnClickListener(this);
        mIVChooseAcceptSite.setOnClickListener(this);
        mIVBackAcceptSite.setOnClickListener(this);
        mIVClearAcceptSite.setOnClickListener(this);
        mChooseStartDate.setOnClickListener(this);
        mChooseEndDate.setOnClickListener(this);
        mFanYingLaiYuanData = new ArrayList<>();
        mFanYingLeiXingData = new ArrayList<>();
        mFanYingNeiRongData = new ArrayList<>();
        mFanYingQuMingData = new ArrayList<>();
        mFanYingLaiYuanAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mFanYingLaiYuanData);
        mFanYingLeiXingAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mFanYingLeiXingData);
        mFanYingNeiRongAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mFanYingNeiRongData);
        mFanYingQuMingAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mFanYingQuMingData);
        mSpFanYingLaiYuan.setAdapter(mFanYingLaiYuanAdapter);
        mSpFanYingLeiXing.setAdapter(mFanYingLeiXingAdapter);
        mSpFanYingNeiRong.setAdapter(mFanYingNeiRongAdapter);
        mSpFanYingQuMing.setAdapter(mFanYingQuMingAdapter);
//        initSpinner();
    }

    private void initSpinner() {
        mQueryOrderPresenter.initIssueOriginSpinner();
        mQueryOrderPresenter.initFanYingLeiXingSpinner();
        mQueryOrderPresenter.initFanYingArea();
        mQueryOrderPresenter.getHotlineStation(true);
        mQueryOrderPresenter.getHotlineStation(false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(System.currentTimeMillis());
        Date date = null;
        try {
            date = sdf.parse(currentDate);
            mStartDateUtc = mEndDateUtc = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mStartDateText.setText(currentDate);
        mEndDateText.setText(currentDate);

        mSpFanYingLeiXing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {//默认值
                    mFanYingNeiRongData.add(mDefaultSelectedValue);
                    mFanYingNeiRongAdapter.notifyDataSetChanged();
                } else {
                    mQueryOrderPresenter.initFanYingNeiRongSpinner(mFanYingLeiXingWords.get(position - 1).getValueEx());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mQueryOrderPresenter.detachView();
        mEventBus.unregister(this);
    }

    public void chooseDate(final boolean isStart) {
        Calendar mycalendar = Calendar.getInstance();
        mYear = mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        month = mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
        day = mycalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                month = monthOfYear;
                day = dayOfMonth;
                updateDate(isStart);
            }
        }, mYear, month, day);
        datePickerDialog.show();
    }

    /**
     * 更新日期
     */
    private void updateDate(boolean isStart) {
        String mCurrentMonth = (month + 1) + "";
        if (mCurrentMonth.length() == 1) {
            mCurrentMonth = "0" + mCurrentMonth;
        }
        String mCurrentDay = day + "";
        if (mCurrentDay.length() == 1) {
            mCurrentDay = "0" + mCurrentDay;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        if (isStart) {
            mStartDateText.setText(mYear + "-" + mCurrentMonth + "-" + mCurrentDay);
            try {
                String startDateStr = mYear + mCurrentMonth + mCurrentDay;
                mStartDateUtc = sdf.parse(startDateStr).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return;
        }
        mEndDateText.setText(mYear + "-" + mCurrentMonth + "-" + mCurrentDay);
        try {
            String endDateStr = mYear + mCurrentMonth + mCurrentDay;
            mEndDateUtc = sdf.parse(endDateStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (mStartDateUtc > mEndDateUtc) {
            mEndDateText.setText("");
            mEndDateUtc = 0;
            ApplicationsUtil.showMessage(this, "结束日期不能大于开始日期！");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_start_date_calendar://开始时间
                chooseDate(true);
                break;
            case R.id.iv_end_date_calendar://结束时间
                chooseDate(false);
                break;
            case R.id.iv_choose_login_site:
                jump2ChooseSite(true);
                break;
            case R.id.iv_choose_accept_site:
                jump2ChooseSite(false);
                break;
            case R.id.iv_back_login_site:
                mQueryOrderPresenter.getHotlineStation(true);
                break;
            case R.id.iv_back_accept_site:
                mQueryOrderPresenter.getHotlineStation(false);
                break;
            case R.id.iv_clear_login_site:
                clearSite(true);
                break;
            case R.id.iv_clear_accept_site:
                clearSite(false);
                break;
            case R.id.btn_query://查询
                if (!isConfigInitSuccess()) {
                    ApplicationsUtil.showMessage(this, R.string.text_init_failure);
                    return;
                }

                String taskId = mEditTaskID.getText().toString();
                String huMing = mEditHuMing.getText().toString();
                String address = mEditAddress.getText().toString();
                String phone = mEditTelephone.getText().toString();

                if ((mStartDateUtc == 0) && (mEndDateUtc != 0)) {
                    ApplicationsUtil.showMessage(this, R.string.toast_choose_start_date);
                    return;
                }

                if ((mStartDateUtc != 0) && (mEndDateUtc == 0)) {
                    ApplicationsUtil.showMessage(this, R.string.toast_choose_end_date);
                    return;
                }

                if (TextUtil.isNullOrEmpty(taskId)
                        && TextUtil.isNullOrEmpty(huMing)
                        && TextUtil.isNullOrEmpty(address)
                        && TextUtil.isNullOrEmpty(phone)
                        && (mStartDateUtc == 0 && mEndDateUtc == 0)
                        && mSpFanYingLaiYuan.getSelectedItem().toString().equals(mDefaultSelectedValue)
                        && mSpFanYingLeiXing.getSelectedItem().toString().equals(mDefaultSelectedValue)
                        && mSpFanYingNeiRong.getSelectedItem().toString().equals(mDefaultSelectedValue)
                        && mSpFanYingQuMing.getSelectedItem().toString().equals(mDefaultSelectedValue)
                        && TextUtil.isNullOrEmpty(mLoginStation)
                        && TextUtil.isNullOrEmpty(mAcceptStation)) {
                    ApplicationsUtil.showMessage(this, R.string.toast_at_least_one_condition);
                    return;
                }

                Intent intent = new Intent(QueryOrderActivity.this, QueryOrderResultActivity.class);
                intent.putExtra(Constant.TASK_ID, mEditTaskID.getText().toString());
                intent.putExtra(Constant.NAME, mEditHuMing.getText().toString());
                intent.putExtra(Constant.ADDRESS, mEditAddress.getText().toString());
                intent.putExtra(Constant.TELEPHONE, mEditTelephone.getText().toString());
                if (!mSpFanYingLaiYuan.getSelectedItem().toString().equals(mDefaultSelectedValue)) {
                    intent.putExtra(Constant.ISSUEORIGIN, mSpFanYingLaiYuan.getSelectedItem().toString());
                }
                if (!mSpFanYingLeiXing.getSelectedItem().toString().equals(mDefaultSelectedValue)) {
                    intent.putExtra(Constant.ISSUE_TYPE, mSpFanYingLeiXing.getSelectedItem().toString());
                }
                if (!mSpFanYingNeiRong.getSelectedItem().toString().equals(mDefaultSelectedValue)) {
                    intent.putExtra(Constant.ISSUE_CONTENT, mSpFanYingNeiRong.getSelectedItem().toString());
                }
                if (!mSpFanYingQuMing.getSelectedItem().toString().equals(mDefaultSelectedValue)) {
                    intent.putExtra(Constant.ISSUE_AREA, mSpFanYingQuMing.getSelectedItem().toString());
                }
                intent.putExtra(Constant.START_DATE, mStartDateUtc);
                intent.putExtra(Constant.END_DATE, mEndDateUtc);
                intent.putExtra(Constant.LOGIN_SITE, mLoginStation);
                intent.putExtra(Constant.ACCEPT_SITE, mAcceptStation);
                startActivity(intent);
                break;
        }
    }

    /**
     * 清除
     *
     * @param isLogin
     */
    private void clearSite(boolean isLogin) {
        if (isLogin) {
            mTVLoginStation.setText(R.string.text_choose_login_site);
            mLoginStation = "";
        } else {
            mTVAcceptStation.setText(R.string.text_choose_accept_site);
            mAcceptStation = "";
        }
    }

    /**
     * 跳转至选择站点
     */
    private void jump2ChooseSite(boolean isLogin) {
        Intent intent = new Intent(this, ChooseSiteActivity.class);
        if (isLogin) {
            startActivityForResult(intent, REQUEST_CHOOSE_STATION);
        } else {
            startActivityForResult(intent, REQUEST_CHOOSE_ACCEPT_STATION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_STATION) {
            if (resultCode == RESULT_CHOOSE_STATION && data != null) {
                DUWord duWord = data.getParcelableExtra(Constant.CHOOSE_STATION);
                mTVLoginStation.setText(duWord.getName());
                mLoginStation = duWord.getValue();
            }
        }
        if (requestCode == REQUEST_CHOOSE_ACCEPT_STATION) {
            if (resultCode == RESULT_CHOOSE_STATION && data != null) {
                DUWord duWord = data.getParcelableExtra(Constant.CHOOSE_STATION);
                mTVAcceptStation.setText(duWord.getName());
                mAcceptStation = duWord.getValue();
            }
        }
    }

    @Override
    public void onInitIssueOriginSpinner(List<DUWord> words) {
        mFanYingLaiYuanData.add(mDefaultSelectedValue);
        for (DUWord word : words) {
            mFanYingLaiYuanData.add(word.getName());
        }
        mFanYingLaiYuanAdapter.notifyDataSetChanged();
    }

    /**
     * 反映类型
     *
     * @param words
     */
    @Override
    public void onInitFanYingLeiXingSpinner(List<DUWord> words) {
        mFanYingLeiXingWords = words;
        mFanYingLeiXingData.add(mDefaultSelectedValue);
        for (DUWord word : words) {
            mFanYingLeiXingData.add(word.getName());
        }
        mFanYingLeiXingAdapter.notifyDataSetChanged();
    }

    /**
     * 反映内容
     *
     * @param words
     */
    @Override
    public void onInitFanYingNeiRongSpinner(List<DUWord> words) {
        mFanYingNeiRongData.clear();
        for (DUWord word : words) {
            mFanYingNeiRongData.add(word.getName());
        }
        mFanYingNeiRongAdapter.notifyDataSetChanged();
    }

    @Override
    public void onInitFanYingQuMingSpinner(List<DUWord> words) {
        mFanYingQuMingData.add(mDefaultSelectedValue);
        for (DUWord word : words) {
            mFanYingQuMingData.add(word.getName());
        }
        mFanYingQuMingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onInitStation(DUWord duWord, boolean isLoginStation) {
        if (isLoginStation) {
            mTVLoginStation.setText(duWord.getName());
            mLoginStation = duWord.getValue();
        } else {
            mTVAcceptStation.setText(duWord.getName());
            mAcceptStation = duWord.getValue();
        }
    }

    @Subscribe
    public void onInitResult(UIBusEvent.InitResult initResult) {
        if (initResult.isSuccess()) {
            initSpinner();
        } else {
            ApplicationsUtil.showMessage(this, R.string.text_init_failure);
        }
    }
}