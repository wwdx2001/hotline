package com.sh3h.hotline.ui.order.myorder.handle;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.sh3h.dataprovider.BaseApplication;
import com.sh3h.dataprovider.data.entity.newentity.ArriveDataEntity;
import com.sh3h.dataprovider.data.entity.newentity.HandleOrderEntity;
import com.sh3h.dataprovider.data.entity.response.CLJG;
import com.sh3h.dataprovider.data.entity.response.DUWord;
import com.sh3h.dataprovider.data.entity.response.FSYY;
import com.sh3h.dataprovider.data.entity.response.FYLXBean;
import com.sh3h.dataprovider.data.entity.response.FYNRBean;
import com.sh3h.dataprovider.data.entity.response.JJCS;
import com.sh3h.dataprovider.data.entity.response.MRSTATUS;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.ArriveDataEntityDao;
import com.sh3h.dataprovider.greendaoDao.FSYYDao;
import com.sh3h.dataprovider.greendaoDao.FYNRBeanDao;
import com.sh3h.dataprovider.greendaoDao.HandleOrderEntityDao;
import com.sh3h.dataprovider.greendaoDao.JJCSDao;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.BaseActivity;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.sh3h.mobileutil.util.TextUtil;

import org.json.JSONException;
import org.json.JSONObject;

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
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 处理工单-Fragment
 * Created by zhangjing on 2016/9/14.
 */
public class HandleOrderFragment extends ParentFragment implements HandleOrderFragmentMvpView,
        AdapterView.OnItemSelectedListener, View.OnClickListener {

    @BindView(R.id.sp_handle_result)
    EditText mEtHandleResult;
    @BindView(R.id.et_fuhe_chaoma)
    EditText mEtFuheChaoma;
    @BindView(R.id.et_chaobiao_zt)
    EditText mEtChaobiaoZt;
    //    @BindView(R.id.ll_handle_result)
//    LinearLayout mLlHandleResult;
    @BindView(R.id.ll_fuchao_chaoma)
    public LinearLayout mLlFuchaoChaoma;
    @BindView(R.id.ll_chaobiao_zt)
    public LinearLayout mLlChaobiaoZt;
    private Unbinder mUnbinder;

    @Inject
    HandleOrderFragmentPresenter mHandleOrderPresenter;

    @BindView(R.id.sp_handle_type)
    EditText mSPHandleType;//处理类别

    @BindView(R.id.sp_handle_content)
    EditText mSPHandleContent;//处理内容

    @BindView(R.id.sp_happened_reason)
    EditText mSPHappenReason;//发生原因

    @BindView(R.id.sp_handle_measure)
    EditText mSPHandleMeasure;//解决措施

//    @BindView(R.id.sp_handle_result)
//    AppCompatSpinner mSPHanleResult;//处理结果

    @BindView(R.id.et_shoulibeizhu)
    EditText mEditShouLiBeiZhu;//受理备注

    @BindView(R.id.tv_start_date_show)
    TextView mTextHandleTime;

    @BindView(R.id.tv_arrive_date_show)
    TextView tvArriveDateShow;

    private List<DUWord> handleTypeDUWords;
    private List<String> handleTypes;
    private List<String> handleContents;
    private List<String> issueReasons;
    private List<String> solveMeasures;
    private List<String> solveResults;
    private ArrayAdapter mHandleTypeAdapter;
    private ArrayAdapter mHandleContentAdatper;
    private ArrayAdapter mIssueReasonAdapter;
    private ArrayAdapter mSolveMeasureAdatper;
    private ArrayAdapter mSolveResultAdapter;

    //回填信息
    private JSONObject mTaskReplyJSONObject;
    private String mHandleType;
    private String mHandleContent;
    private String mIssueReason;
    private String mSolveMeasure;
    private String mSolveResult;
    private String mReceiveComment;//受理备注
    private String mHandleTypeValue;//处理类别value值


    private boolean isHandleTypeChanged;//处理类别是否改变
    private String mIssueType;
    private String mIssueContent;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private long handleTime;//处理时间
    private long arriveTime;//到场时间
    private String mTaskId;
    private String[] mContents;
    private boolean mIsDataUpload;
    private static final String TAG = "HandleOrderFragment";
    private String fylxParams = "";
    private List<FYNRBean> mFynrBeanList;
    private List<String> mFynrList;
    private Map<String, String> cllbmap1 = new HashMap<>();
    private Map<String, String> cllbmap2 = new HashMap<>();
    private Map<String, String> fsyymap1 = new HashMap<>();
    private Map<String, String> fsyymap2 = new HashMap<>();
    private Map<String, String> jjcsmap1 = new HashMap<>();
    private Map<String, String> jjcsmap2 = new HashMap<>();
    private Map<String, String> cljgmap1 = new HashMap<>();
    private Map<String, String> cljgmap2 = new HashMap<>();
    private Map<String, String> cbztmap1 = new HashMap<>();
    private Map<String, String> cbztmap2 = new HashMap<>();
    private List<FSYY> mFsyyBeanList;
    private List<String> mFsyyList;
    private List<JJCS> mJjcsBeanList;
    private List<String> mJjccList;
    private List<CLJG> mCLJGBeanList;
    private List<String> mCljgList;
    private List<MRSTATUS> mCBZTBeanList;
    private List<String> mCbztList;
    private String mIssueLeiXing;
    private boolean mIsFormHistory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.e(TAG, "onCreate------------------");
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        initMap();
    }

    private void initMap() {
        //处理类别
        List<FYLXBean> fylxBeans = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getFYLXBeanDao().loadAll();
        for (int i = 0; i < fylxBeans.size(); i++) {
            cllbmap1.put(fylxBeans.get(i).getFA_TYPE_CD(), fylxBeans.get(i).getFA_TYPE_DESCR());
            cllbmap2.put(fylxBeans.get(i).getFA_TYPE_DESCR(), fylxBeans.get(i).getFA_TYPE_CD());
        }
        //发生原因
        List<FSYY> fsyyList = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getFSYYDao().loadAll();
        for (int i = 0; i < fsyyList.size(); i++) {
            fsyymap1.put(fsyyList.get(i).getYY_ID(), fsyyList.get(i).getDESCR());
            //发生原因和解决措施有两套相同的词语，key相同
            fsyymap2.put(fsyyList.get(i).getFA_TYPE_DESCR() + fsyyList.get(i).getDESCR(), fsyyList.get(i).getYY_ID());
        }
        //解决措施
        List<JJCS> jjcsList = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getJJCSDao().loadAll();
        for (int i = 0; i < jjcsList.size(); i++) {
            jjcsmap1.put(jjcsList.get(i).getCS_ID(), jjcsList.get(i).getDESCR());
            jjcsmap2.put(jjcsList.get(i).getFA_TYPE_DESCR() + jjcsList.get(i).getDESCR(), jjcsList.get(i).getCS_ID());
        }
        //处理结果
        List<CLJG> cljgList = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getCLJGDao().loadAll();
        for (int i = 0; i < cljgList.size(); i++) {
            cljgmap1.put(cljgList.get(i).getCLJG_ID(), cljgList.get(i).getDESCR());
            cljgmap2.put(cljgList.get(i).getDESCR(), cljgList.get(i).getCLJG_ID());
        }
        //抄表状态
        List<MRSTATUS> cbztLit = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getMRSTATUSDao().loadAll();
        for (int i = 0; i < cbztLit.size(); i++) {
            cbztmap1.put(cbztLit.get(i).getMr_Status_Id(), cbztLit.get(i).getMr_Status_Desc());
            cbztmap2.put(cbztLit.get(i).getMr_Status_Desc(), cbztLit.get(i).getMr_Status_Id());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtils.e(TAG, "onCreateView------------------");
        View view = inflater.inflate(R.layout.fragment_handle_order, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mHandleOrderPresenter.attachView(this);
        initView();
        initData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.e(TAG, "onDestroyView------------------");
    }


    private void initData() {
        if (mIsFormHistory) {
            List<HandleOrderEntity> list = GreenDaoUtils.getDaoSession(getActivity())
                    .getHandleOrderEntityDao().queryBuilder()
                    .where(HandleOrderEntityDao.Properties.FaId.eq(mTaskId)).list();
            if (list != null && list.size() > 0) {
                mEditShouLiBeiZhu.setText(list.get(0).getComment());
                mSPHandleType.setText(cllbmap1.get(list.get(0).getFaTypeCd()));
//            mSPHandleContent.setText(list.get(0).getClnr());
                Log.e("fashegnyuanyin", "fsyy=" + list.get(0).getFaReason());
                mSPHappenReason.setText(fsyymap1.get(list.get(0).getFaReason()));
                mSPHandleMeasure.setText(jjcsmap1.get(list.get(0).getFaAct()));
                mTextHandleTime.setText(list.get(0).getFinishDt());
                tvArriveDateShow.setText(list.get(0).getArriveDt());
                mEtFuheChaoma.setText(list.get(0).getRegRead());
                mEtChaobiaoZt.setText(cbztmap1.get(list.get(0).getCbzt()));
                mEtHandleResult.setText(cljgmap1.get(list.get(0).getCljg()));
                if (("复核工单".equals(mSPHandleType.getText().toString())
                        || "复核延迟工单".equals(mSPHandleType.getText().toString().trim()))
                        && "现场复核".equals(mSPHandleMeasure.getText().toString().trim())) {
                    mLlFuchaoChaoma.setVisibility(View.VISIBLE);
                    mLlChaobiaoZt.setVisibility(View.VISIBLE);
                    Log.e("testVisibility", "--------------------------------显示" + mSPHandleMeasure.getText().toString().trim() + mSPHandleType.getText().toString().trim());
                } else {
                    mLlFuchaoChaoma.setVisibility(View.GONE);
                    mLlChaobiaoZt.setVisibility(View.GONE);
                    Log.e("testVisibility", "--------------------------------隐藏");
                }
            }
        }
    }

    private void initView() {
        handleTypes = new ArrayList<String>();
        handleContents = new ArrayList<String>();
        issueReasons = new ArrayList<String>();
        solveMeasures = new ArrayList<String>();
        solveResults = new ArrayList<String>();
        mHandleTypeAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, handleTypes);
        mHandleContentAdatper = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, handleContents);
        mIssueReasonAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, issueReasons);
        mSolveMeasureAdatper = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, solveMeasures);
        mSolveResultAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, solveResults);
        initSpinner();

        mIssueType = getArguments().getString(Constant.ISSUE_TYPE);
        mIssueLeiXing = getArguments().getString(Constant.ISSUE_LEIXING);
        mIssueContent = getArguments().getString(Constant.ISSUE_CONTENT);
        Log.e("反应内容", "反应内容：" + mIssueContent);
        mTaskId = getArguments().getString(Constant.TASK_ID);
        mIsDataUpload = getArguments().getBoolean(Constant.DATA_IS_UPLOAD, false);
        mIsFormHistory = getArguments().getBoolean(Constant.TYPE_IS_HISTORY);
        //显示默认时间
        handleTime = System.currentTimeMillis();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(handleTime);
        mTextHandleTime.setText(currentDate);

        List<ArriveDataEntity> arriveDataEntityList = GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                .getArriveDataEntityDao().queryBuilder()
                .where(ArriveDataEntityDao.Properties.FaId.eq(mTaskId))
                .orderDesc(ArriveDataEntityDao.Properties.ArriveDt)
                .list();
        if (arriveDataEntityList != null && arriveDataEntityList.size() > 0) {
            arriveTime = TimeUtils.string2Millis(arriveDataEntityList.get(0).getArriveDt());
            String currentArriveDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(arriveTime);
            tvArriveDateShow.setText(currentArriveDate);
        } else {

            arriveTime = System.currentTimeMillis();
            String currentArriveDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(arriveTime);
            ArriveDataEntity arriveDataEntity = new ArriveDataEntity();
            arriveDataEntity.setFaId(mTaskId);
            arriveDataEntity.setArriveDt(TimeUtils.millis2String(arriveTime));
            GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                    .getArriveDataEntityDao().insertOrReplace(arriveDataEntity);
            tvArriveDateShow.setText(currentArriveDate);
        }

        if (mIsDataUpload) {
            mSPHandleType.setEnabled(false);//处理类别
            mSPHandleContent.setEnabled(false);//处理内容
            mSPHappenReason.setEnabled(false);//发生原因
            mSPHandleMeasure.setEnabled(false);//解决措施
            mEtHandleResult.setEnabled(false);
            mEtFuheChaoma.setEnabled(false);
            mEtChaobiaoZt.setEnabled(false);
            mEditShouLiBeiZhu.setEnabled(false);//受理备注
            mTextHandleTime.setEnabled(false);
            tvArriveDateShow.setEnabled(false);
        } else {
            mSPHandleType.setEnabled(true);//处理类别
            mSPHandleContent.setEnabled(true);//处理内容
            mSPHappenReason.setEnabled(true);//发生原因
            mSPHandleMeasure.setEnabled(true);//解决措施
            mEtHandleResult.setEnabled(true);
            mEtFuheChaoma.setEnabled(true);
            mEtChaobiaoZt.setEnabled(true);
            mEditShouLiBeiZhu.setEnabled(true);//受理备注
            mTextHandleTime.setEnabled(true);
            tvArriveDateShow.setEnabled(true);
        }
        mSPHandleType.setText(mIssueLeiXing);
        mSPHandleType.setEnabled(false);
        mSPHandleContent.setText(mIssueContent);
        mSPHandleContent.setEnabled(false);
        selectHandleType(false, mIssueLeiXing, 0, GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getFYLXBeanDao().loadAll());
    }

    private void initSpinner() {
//        mSPHandleType.setSelection(0, true);
//        mSPHandleType.setOnItemSelectedListener(this);
//        mSPHandleType.setAdapter(mHandleTypeAdapter);
//        mSPHandleContent.setAdapter(mHandleContentAdatper);
//        mSPHappenReason.setAdapter(mIssueReasonAdapter);
//        mSPHandleMeasure.setAdapter(mSolveMeasureAdatper);
        mSPHandleType.setOnClickListener(this);
        mSPHandleContent.setOnClickListener(this);
        mSPHandleMeasure.setOnClickListener(this);
        mSPHappenReason.setOnClickListener(this);
        mEtHandleResult.setOnClickListener(this);
        mEtChaobiaoZt.setOnClickListener(this);
//        mSPHanleResult.setAdapter(mSolveResultAdapter);
        mHandleOrderPresenter.queryHandleType();
        mHandleOrderPresenter.queryHandleResult();
//        mSPHandleContent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                mHandleContent = handleContents.get(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        mSPHappenReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                mIssueReason = issueReasons.get(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        mSPHandleMeasure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                mSolveMeasure = solveMeasures.get(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        mSPHanleResult.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                mSolveResult = solveResults.get(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    @OnClick(R.id.tv_start_date_show)
    public void showDatePicker() {
        takePicker(mTextHandleTime, 0);
    }

    @OnClick(R.id.tv_arrive_date_show)
    public void getDatePicker() {
        takePicker(tvArriveDateShow, 1);
    }

    private void takePicker(final TextView textView, final int flag) {
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
                showTimePicker(textView, flag);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showTimePicker(final TextView textView, final int flag) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;
                updateDateTime(textView, flag);
            }
        }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    private void updateDateTime(TextView textView, int flag) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟
        String dateStr = mYear + "-" + mMonth + "-" + mDay + " " + mHour + ":" + mMinute + ":00";
        try {
            Date date = sdf.parse(dateStr);
            switch (flag) {
                case 0:
                    handleTime = date.getTime();
                    break;
                case 1:
                    arriveTime = date.getTime();
                    break;
                default:
                    break;
            }
            textView.setText(sdf.format(date));
        } catch (ParseException e) {
            textView.setText(dateStr);
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.e(TAG, "onAttach------------------");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.e(TAG, "onDetach------------------");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e(TAG, "onDestroy------------------");
        mUnbinder.unbind();
        mHandleOrderPresenter.detachView();
    }

    @Override
    protected void lazyLoad() {

    }

    /**
     * @param duWords
     */
    @Override
    public void onGetHandleTypeWords(List<DUWord> duWords) {
        handleTypeDUWords = duWords;
        for (DUWord duWord : duWords) {
            handleTypes.add(duWord.getName());
        }
        mHandleTypeAdapter.notifyDataSetChanged();

        if (!TextUtils.isEmpty(getArguments().getString(Constant.TASK_REPLY))) { // history task???
            String taskReply = getArguments().getString(Constant.TASK_REPLY);
            try {
                mTaskReplyJSONObject = new JSONObject(taskReply);
                if (mTaskReplyJSONObject == null) {
                    return;
                }
                mHandleType = mTaskReplyJSONObject.getString(Constant.RESOLVE_TYPE);
                for (int i = 0; i < handleTypes.size(); i++) {
                    if (mHandleType.equals(handleTypes.get(i))) {
//                        mSPHandleType.setSelection(i, true);
//                        mHandleType = handleTypes.get(i);
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (!TextUtil.isNullOrEmpty(mIssueType)) { // new task
            for (int i = 0; i < handleTypes.size(); i++) {
                if (mIssueType.equals(handleTypes.get(i))) {
//                    mSPHandleType.setSelection(i, true);
                    break;
                }
            }
        }
    }

    @Override
    public void onGetHandleResultWords(List<DUWord> duWords) {
        solveResults.clear();
        solveResults.add("");
        for (DUWord duWord : duWords) {
            solveResults.add(duWord.getName());
        }
        mSolveResultAdapter.notifyDataSetChanged();
        if (!TextUtils.isEmpty(getArguments().getString(Constant.TASK_REPLY))) {
            String taskReply = getArguments().getString(Constant.TASK_REPLY);
            try {
                mTaskReplyJSONObject = new JSONObject(taskReply);
                if (mTaskReplyJSONObject == null) {
                    return;
                }
                mSolveResult = mTaskReplyJSONObject.getString(Constant.RESOLVE_RESULT);
                for (int i = 0; i < solveResults.size(); i++) {
                    if (mSolveResult.equals(solveResults.get(i))) {
//                        mSPHanleResult.setSelection(i, true);
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onGetWord(Map<String, List<DUWord>> map) {

        handleContents.clear();
        issueReasons.clear();
        solveMeasures.clear();

        //处理内容
        if (map.get(Constant.HANDLE_CONTENT) != null) {
            List<DUWord> handleContent = map.get(Constant.HANDLE_CONTENT);
            for (DUWord duWord : handleContent) {
                handleContents.add(duWord.getName());
            }
            mHandleContent = handleContents.get(0);
        } else {
            mHandleContent = "";
        }
//        mSPHandleContent.setSelection(0, true);
        mHandleContentAdatper.notifyDataSetChanged();

        //发生原因
        if (map.get(Constant.HAPPEN_REASON) != null) {
            List<DUWord> issueReason = map.get(Constant.HAPPEN_REASON);
            issueReasons.add("");
            for (DUWord duWord : issueReason) {
                issueReasons.add(duWord.getName());
            }
            mIssueReason = issueReasons.get(0);
        } else {
            mIssueReason = "";
        }
//        mSPHappenReason.setSelection(0, true);
        mIssueReasonAdapter.notifyDataSetChanged();

        //解决措施
        if (map.get(Constant.SOVLE_METHOD) != null) {
            List<DUWord> solveMeasure = map.get(Constant.SOVLE_METHOD);
            solveMeasures.add("");
            for (DUWord duWord : solveMeasure) {
                solveMeasures.add(duWord.getName());
            }
            mSolveMeasure = solveMeasures.get(0);
        } else {
            mSolveMeasure = "";
        }
//        mSPHandleMeasure.setSelection(0, true);
        mSolveMeasureAdatper.notifyDataSetChanged();

        //未上传数据的状态，spinner显示任务回复的信息
        if (!TextUtils.isEmpty(getArguments().getString(Constant.TASK_REPLY))) { // history task
            try {
                mHandleContent = mTaskReplyJSONObject.getString(Constant.RESOLVE_CONTENT);
                mIssueReason = mTaskReplyJSONObject.getString(Constant.ISSUE_REASON);
                mSolveMeasure = mTaskReplyJSONObject.getString(Constant.RESOLVE_METHOD);
                mReceiveComment = mTaskReplyJSONObject.getString(Constant.RESOLVE_COMMENT);
                handleTime = mTaskReplyJSONObject.getLong(Constant.RESOLVE_TIME);//处理时间
                arriveTime = mTaskReplyJSONObject.getLong(Constant.ARRIVE_TIME);//处理时间
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < handleContents.size(); i++) {
                if (mHandleContent.equals(handleContents.get(i))) {
//                    mSPHandleContent.setSelection(i, true);
                    mHandleContent = handleContents.get(i);
                    break;
                }
            }

            for (int i = 0; i < issueReasons.size(); i++) {
                if (mIssueReason.equals(issueReasons.get(i))) {
//                    mSPHappenReason.setSelection(i, true);
                    mIssueReason = issueReasons.get(i);
                    break;
                }
            }

            for (int i = 0; i < solveMeasures.size(); i++) {
                if (mSolveMeasure.equals(solveMeasures.get(i))) {
//                    mSPHandleMeasure.setSelection(i, true);
                    mSolveMeasure = solveMeasures.get(i);
                    break;
                }
            }
            mEditShouLiBeiZhu.setText(mReceiveComment);
            mTextHandleTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(handleTime));
            tvArriveDateShow.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(arriveTime));
        } else if (!TextUtil.isNullOrEmpty(mIssueType)
                && !TextUtil.isNullOrEmpty(mHandleType)
                && !TextUtil.isNullOrEmpty(mIssueContent)
                && mIssueType.equals(mHandleType)) { // new task
            for (int i = 0; i < handleContents.size(); i++) {
                if (mIssueContent.equals(handleContents.get(i))) {
//                    mSPHandleContent.setSelection(i, true);
                    mHandleContent = handleContents.get(i);
                    break;
                }
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String valueEx = "";
        for (DUWord duword : handleTypeDUWords) {
            if (duword.getName().equals(handleTypes.get(position))) {
                valueEx = duword.getValueEx();
            }
        }
        mHandleOrderPresenter.getWords(valueEx);
        mHandleType = handleTypeDUWords.get(position).getName();
        mHandleTypeValue = handleTypeDUWords.get(position).getValue();
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String getmHandleType() {
        return cllbmap2.get(mSPHandleType.getText().toString().trim());
    }

    public String getmHandleContent() {
        return mSPHandleContent.getText().toString();
    }

    public String getmIssueReason() {
        return fsyymap2.get(mSPHandleType.getText().toString() + mSPHappenReason.getText().toString());
    }

    public String getmSolveMeasure() {
        return jjcsmap2.get(mSPHandleType.getText().toString() + mSPHandleMeasure.getText().toString());
    }

    public String getmReceiveComment() {
        mReceiveComment = mEditShouLiBeiZhu.getText().toString();
        return mReceiveComment;
    }

    public String getmSolveResult() {
//        return mSolveResult;
        return cljgmap2.get(mEtHandleResult.getText().toString());
    }

    public String getFuheChaoMa() {
        if (mLlFuchaoChaoma.getVisibility() == View.VISIBLE) {
            try {
                Long fuhechaoma = Long.parseLong(mEtFuheChaoma.getText().toString().trim());
                return fuhechaoma + "";
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return mEtFuheChaoma.getText().toString();
        } else {
            return "";
        }
    }

    public String getChaoBiaoZT() {
//        return mEtChaobiaoZt.getText().toString();
        if (mLlChaobiaoZt.getVisibility() == View.VISIBLE) {
            return cbztmap2.get(mEtChaobiaoZt.getText().toString());
        } else {
            return "";
        }
    }

    public void setmSolveResult(String mSolveResult) {
        this.mSolveResult = mSolveResult;
    }


    public String getmHandleTypeValue() {
        return mHandleTypeValue;
    }

    public void setmHandleTypeValue(String mHandleTypeValue) {
        this.mHandleTypeValue = mHandleTypeValue;
    }

    public long getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(long handleTime) {
        this.handleTime = handleTime;
    }

    public long getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(long arriveTime) {
        this.arriveTime = arriveTime;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sp_handle_type:
            case R.id.sp_handle_content:
            case R.id.sp_handle_measure:
            case R.id.sp_happened_reason:
            case R.id.sp_handle_result:
            case R.id.et_chaobiao_zt:
                try {
                    List<FYNRBean> fynrBeans = GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getFYNRBeanDao().loadAll();
                    List<FYLXBean> fylxBeans = GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getFYLXBeanDao().loadAll();
                    List<FSYY> fsyyList = GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getFSYYDao().loadAll();
                    List<JJCS> jjcsList = GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getJJCSDao().loadAll();
                    List<CLJG> cljgList = GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getCLJGDao().loadAll();
                    if (fynrBeans.size() == 0
                            || fylxBeans.size() == 0
                            || fsyyList.size() == 0
                            || jjcsList.size() == 0
                            || cljgList.size() == 0) {
                        ToastUtils.showShort("正在下载词条数据，请稍后再试");
                        BaseApplication.getInstance().getWordList();
                    } else {
                        showWordInfoDialog(v.getId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showShort("加载词语数据失败");
                    BaseApplication.getInstance().getWordList();
                }
                break;
        }
    }

    /**
     * 弹出词条对话框
     *
     * @param id
     */
    private void showWordInfoDialog(final int id) {
        //处理类别
        final List<FYLXBean> fylxBeanList = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getFYLXBeanDao().loadAll();
        final List<String> fanyingleixingList = Lists.transform(fylxBeanList, new Function<FYLXBean, String>() {
            @Nullable
            @Override
            public String apply(@Nullable FYLXBean bwbdlBean) {
                return bwbdlBean.getFA_TYPE_DESCR();
            }
        });
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String title = "";
        if (id == R.id.sp_handle_type) {//处理类别
            mContents = fanyingleixingList.toArray(new String[fanyingleixingList.size()]);
            title = "处理类别";
        } else if (id == R.id.sp_handle_content) {//处理内容
            if (StringUtils.isEmpty(mSPHandleType.getText().toString())) {
                ToastUtils.showShort("请选择处理类别");
                return;
            }
            if (mFynrList == null || mFynrList.size() == 0) {
                initMap();
                selectHandleType(false, mSPHandleType.getText().toString(), 0, GreenDaoUtils
                        .getDaoSession(MainApplication.getInstance()).getFYLXBeanDao().loadAll());
            }
            mContents = mFynrList.toArray(new String[mFynrList.size()]);
            title = "处理内容";
        } else if (id == R.id.sp_happened_reason) {//发生原因
            if (StringUtils.isEmpty(mSPHandleType.getText().toString())) {
                ToastUtils.showShort("请选择处理类别");
                return;
            }
            if (mFsyyList == null || mFsyyList.size() == 0) {
                initMap();
                selectHandleType(false, mSPHandleType.getText().toString(), 0, GreenDaoUtils
                        .getDaoSession(MainApplication.getInstance()).getFYLXBeanDao().loadAll());
            }
            mContents = mFsyyList.toArray(new String[mFsyyList.size()]);
            title = "发生原因";
        } else if (id == R.id.sp_handle_measure) {//解决措施
            if (StringUtils.isEmpty(mSPHandleType.getText().toString())) {
                ToastUtils.showShort("请选择处理类别");
                return;
            }
            if (mJjccList == null || mJjccList.size() == 0) {
                initMap();
                selectHandleType(false, mSPHandleType.getText().toString(), 0, GreenDaoUtils
                        .getDaoSession(MainApplication.getInstance()).getFYLXBeanDao().loadAll());
            }
            mContents = mJjccList.toArray(new String[mJjccList.size()]);
            title = "解决措施";
        } else if (id == R.id.sp_handle_result) {
            if (StringUtils.isEmpty(mEtHandleResult.getText().toString())) {
                ToastUtils.showShort("请选择处理结果");
                return;
            }
            if (mCljgList == null || mCljgList.size() == 0) {
                initMap();
                selectHandleType(false, mSPHandleType.getText().toString(), 0, GreenDaoUtils
                        .getDaoSession(MainApplication.getInstance()).getFYLXBeanDao().loadAll());
            }
            mContents = mCljgList.toArray(new String[mCljgList.size()]);
            title = "处理结果";
        } else if (id == R.id.et_chaobiao_zt) {
            if (StringUtils.isEmpty(mEtHandleResult.getText().toString())) {
                ToastUtils.showShort("请选择抄表状态结果");
                return;
            }
            if (mCbztList == null || mCbztList.size() == 0) {
                initMap();
                selectHandleType(false, mSPHandleType.getText().toString(), 0, GreenDaoUtils
                        .getDaoSession(MainApplication.getInstance()).getFYLXBeanDao().loadAll());
            }
            mContents = mCbztList.toArray(new String[mCbztList.size()]);
            title = "抄表状态";
        }
        builder.setTitle("请选择" + title);
        builder.setItems(mContents, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (id) {
                    case R.id.sp_handle_type:
                        selectHandleType(true, "", which, fylxBeanList);
                        break;
                    case R.id.sp_handle_content:
                        mSPHandleContent.setText(mContents[which]);
                        break;
                    case R.id.sp_handle_measure:
                        mSPHandleMeasure.setText(mContents[which]);
                        if (("复核工单".equals(mSPHandleType.getText().toString())
                                || "复核延迟工单".equals(mSPHandleType.getText().toString().trim()))
                                && "现场复核".equals(mSPHandleMeasure.getText().toString().trim())) {
                            mLlFuchaoChaoma.setVisibility(View.VISIBLE);
                            mLlChaobiaoZt.setVisibility(View.VISIBLE);
                        } else {
                            mLlFuchaoChaoma.setVisibility(View.GONE);
                            mLlChaobiaoZt.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.sp_happened_reason:
                        mSPHappenReason.setText(mContents[which]);
                        break;
                    case R.id.sp_handle_result:
                        mEtHandleResult.setText(mContents[which]);
                        break;
                    case R.id.et_chaobiao_zt:
                        mEtChaobiaoZt.setText(mContents[which]);
                        break;
//                    case R.id.et_chulijb:
//                        cljbParams = chuliBeanList.get(which).getCLJB();
//                        mEtChulijb.setText(mContents[which]);
//                        break;
                }
            }
        });
        builder.create().show();
    }

    private void selectHandleType(boolean isClick, String fylxString, int which, List<FYLXBean> fylxBeanList) {
        if (isClick) {
            fylxParams = fylxBeanList.get(which).getFA_TYPE_CD();
            mSPHandleType.setText(mContents[which]);
        } else {
            fylxParams = cllbmap2.get(fylxString);
        }
        if (fylxParams == null) {
            fylxParams = "";
        }
        Log.e("fylxParams", "fylxParams=" + fylxParams);
        //反应内容
        mFynrBeanList = GreenDaoUtils.getDaoSession(BaseApplication.getInstance())
                .getFYNRBeanDao().queryBuilder()
                .where(FYNRBeanDao.Properties.FA_TYPE_CD.eq(fylxParams))
                .list();
        if (mFynrBeanList == null) {
            mFynrBeanList = new ArrayList<>();
        }
        mFynrList = Lists.transform(mFynrBeanList, new Function<FYNRBean, String>() {
            @Nullable
            @Override
            public String apply(@Nullable FYNRBean bwbdlBean) {
                return bwbdlBean.getDESCR();
            }
        });
//        mSPHandleContent.setText(mFynrList.size() > 0 ? mFynrList.get(0) : "");
        mSPHandleContent.setText(mIssueContent);

        //发生原因
        mFsyyBeanList = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getFSYYDao().queryBuilder()
                .where(FSYYDao.Properties.FA_TYPE_CD.eq(fylxParams))
                .list();
        if (mFsyyBeanList == null) {
            mFsyyBeanList = new ArrayList<>();
        }
        mFsyyList = Lists.transform(mFsyyBeanList, new Function<FSYY, String>() {
            @Nullable
            @Override
            public String apply(@Nullable FSYY fsyy) {
                return fsyy.getDESCR();
            }
        });
        mSPHappenReason.setText(mFsyyList.size() > 0 ? mFsyyList.get(0) : "");

        //解决措施
        mJjcsBeanList = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getJJCSDao().queryBuilder()
                .where(JJCSDao.Properties.FA_TYPE_CD.eq(fylxParams))
                .list();
        if (mJjcsBeanList == null) {
            mJjcsBeanList = new ArrayList<>();
        }
        mJjccList = Lists.transform(mJjcsBeanList, new Function<JJCS, String>() {
            @Nullable
            @Override
            public String apply(@Nullable JJCS jjcs) {
                return jjcs.getDESCR();
            }
        });
        mSPHandleMeasure.setText(mJjccList.size() > 0 ? mJjccList.get(0) : "");

        //处理结果
        mCLJGBeanList = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getCLJGDao().loadAll();
        if (mCLJGBeanList == null) {
            mCLJGBeanList = new ArrayList<>();
        }
        mCljgList = Lists.transform(mCLJGBeanList, new Function<CLJG, String>() {
            @Nullable
            @Override
            public String apply(@Nullable CLJG cljg) {
                return cljg.getDESCR();
            }
        });
        mEtHandleResult.setText(mCljgList.size() > 0 ? mCljgList.get(0) : "");

        //抄表状态
        mCBZTBeanList = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getMRSTATUSDao().loadAll();
        if (mCBZTBeanList == null) {
            mCBZTBeanList = new ArrayList<>();
        }
        mCbztList = Lists.transform(mCBZTBeanList, new Function<MRSTATUS, String>() {
            @Nullable
            @Override
            public String apply(@Nullable MRSTATUS mrstatus) {
                return mrstatus.getMr_Status_Desc();
            }
        });
        mEtChaobiaoZt.setText(mCbztList.size() > 0 ? mCbztList.get(0) : "");
        Log.e("testVisibility", "--------------------------------");

        if (("复核工单".equals(mSPHandleType.getText().toString())
                || "复核延迟工单".equals(mSPHandleType.getText().toString().trim()))
                && "现场复核".equals(mSPHandleMeasure.getText().toString().trim())) {
            mLlFuchaoChaoma.setVisibility(View.VISIBLE);
            mLlChaobiaoZt.setVisibility(View.VISIBLE);
            Log.e("testVisibility", "--------------------------------显示" + mSPHandleMeasure.getText().toString().trim() + mSPHandleType.getText().toString().trim());
        } else {
            mLlFuchaoChaoma.setVisibility(View.GONE);
            mLlChaobiaoZt.setVisibility(View.GONE);
            Log.e("testVisibility", "--------------------------------隐藏");
        }
    }
}
