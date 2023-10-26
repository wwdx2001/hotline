package com.sh3h.hotline.ui.notice;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.data.entity.response.WaterStopNotificationEntity;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.DUMyTaskDao;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.order.myorder.history.HistoryOrdersActivity;
import com.sh3h.hotline.util.CommonUtils;
import com.sh3h.ipc.module.MyModule;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 停水公告
 * Created by zhangjing on 2016/9/18.
 */
public class NoWaterSupplyNoticeActivity extends ParentActivity implements NoWaterSupplyNoticeMvpView, View.OnClickListener {

    private final static String TAG = "NoWaterSupplyNoticeActivity";
    private Unbinder mUnbinder;

    @BindView(R.id.no_water_notice_fab)
    FloatingActionButton mFab;

    @BindView(R.id.synchronize_data_swiperefresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.no_water_notice_recyclerView)
    RecyclerView mRecyclerView;

    @Inject
    NoWaterSupplyNoticePresenter mNoWaterSupplyNoticePresenter;

    @Inject
    Bus mEventBus;

    private MyAdapter mAdapter;//适配器

    private List<WaterStopNotificationEntity> mDUNewsList;

    private long mStartDateUtc;//开始日期utc

    private long mEndDateUtc;//结束日期utc

    private TextView tvStartDate;

    private TextView tvEndDate;

    private int mYear;
    private int month;
    private int day;

    //    private String mTitleAndContentFilter;//title、content搜索条件
//    private String mCategotyFilter;//分类搜索条件
    private String mDateFilter;//日期搜索条件

    private boolean mIsLimitDate = true;//限制日期 ture：不限 false：限制 默认ture

    //    private AlertDialog mDatePickerDialog;
    private boolean isSearchFromSever = true;//是否从网络查询
    private String mSpinnerType;//类别
    private String mFilterTitle;//标题
    private String mFilterContent;//内容

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_water_supply_notice);
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);
        initToolBar(R.string.label_no_water_supply);
        mNoWaterSupplyNoticePresenter.attachView(this);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //xiaochao --
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_no_water_supply_notice, menu);


//        MenuItem menuItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                mTitleAndContentFilter = newText;
//                //过滤搜索结果
//                listFilter(mTitleAndContentFilter, "", mCategotyFilter, mDateFilter);
//                return false;
//            }
//        });
//        //设置提示信息
//        searchView.setQueryHint(getString(R.string.text_hint_title_or_content));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            showConditionDialog();
            return true;
        }
//        if (item.getItemId() == R.id.action_category || item.getItemId() == R.id.action_search) {
//            return true;
//        }
//        if (item.getItemId() == R.id.action_calendar) {
//            showDatePickerDialog();
//            return true;
//        }
//        item.setChecked(true);
//        mCategotyFilter = item.getTitle().toString();
//        if (item.getItemId() == R.id.action_category_all) {
//            mCategotyFilter = null;
//        }
//        listFilter(mTitleAndContentFilter, mCategotyFilter, mDateFilter);
        return super.onOptionsItemSelected(item);
    }

    /**
     * 展示条件选择弹框
     */
    private void showConditionDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_notice_condition, null);
        AppCompatSpinner typeSpinner = (AppCompatSpinner) view.findViewById(R.id.sp_notice_condition);
        final EditText editTitle = (EditText) view.findViewById(R.id.et_notice_title);
        final EditText editContent = (EditText) view.findViewById(R.id.et_notice_content);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox_is_from_server);//是否实时查询
        ImageView startDateCalendar = (ImageView) view.findViewById(R.id.iv_dialog_notice_start_date_calendar);
        ImageView endDateCalendar = (ImageView) view.findViewById(R.id.iv_dialog_notice_end_date_calendar);
        tvStartDate = (TextView) view.findViewById(R.id.tv_dialog_notice_start_date);
        tvEndDate = (TextView) view.findViewById(R.id.tv_dialog_notice_end_date);
        startDateCalendar.setOnClickListener(this);
        endDateCalendar.setOnClickListener(this);
        //数据
        initDialogSpinner(typeSpinner);
        initDialogData(editTitle, editContent, checkBox);
        builder.setView(view);
        builder.setNegativeButton(R.string.text_negative, null);
        builder.setPositiveButton(R.string.text_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mStartDateUtc == 0 || mEndDateUtc == 0) {
                    ApplicationsUtil.showMessage(NoWaterSupplyNoticeActivity.this, R.string.toast_choose_date);
                    return;
                }
                mFilterTitle = editTitle.getText().toString();
                mFilterContent = editContent.getText().toString();
                if (mSpinnerType.equals("不限")) {
                    mSpinnerType = "";
                }
                if (checkBox.isChecked()) {//实时查询
                    LogUtil.i(TAG, "search from server");
                    isSearchFromSever = true;
                    mNoWaterSupplyNoticePresenter.searchNews(false, mFilterTitle,
                            mFilterContent, mSpinnerType, mStartDateUtc, mEndDateUtc);
                } else {
                    LogUtil.i(TAG, "search from datalist");
                    isSearchFromSever = false;
                    listFilter(mFilterTitle, mFilterContent, mSpinnerType, mStartDateUtc, mEndDateUtc);
                }
            }
        });
        builder.show();
    }

    /**
     * 初始化弹框数据
     *
     * @param editTitle
     * @param editContent
     * @param checkBox
     */
    private void initDialogData(EditText editTitle, EditText editContent, CheckBox checkBox) {
        if (!TextUtil.isNullOrEmpty(mFilterTitle)) {
            editTitle.setText(mFilterTitle);//标题
        }
        if (!TextUtil.isNullOrEmpty(mFilterContent)) {
            editContent.setText(mFilterContent);//内容
        }
        SimpleDateFormat s2 = new SimpleDateFormat(TextUtil.FORMAT_DATE);
        String currentDate = s2.format(System.currentTimeMillis());
        Date date = null;
        try {
            date = s2.parse(currentDate);
            mStartDateUtc = mEndDateUtc = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (mStartDateUtc == mEndDateUtc && mStartDateUtc != 0) {
            mEndDateUtc = mStartDateUtc + 86399000L;//如果选择的日期相同，就在此基础上加23时59分59秒
        }

        if (mStartDateUtc != 0) {
            tvStartDate.setText(s2.format(new Date(mStartDateUtc)));
        }
        if (mEndDateUtc != 0) {
            tvEndDate.setText(s2.format(new Date(mEndDateUtc)));
        }
        checkBox.setChecked(isSearchFromSever);
    }

    /**
     * 初始化弹框的Spinner
     *
     * @param typeSpinner
     */
    private void initDialogSpinner(AppCompatSpinner typeSpinner) {
        final List<String> dataList = new ArrayList<String>();
        dataList.add("不限");
        dataList.add("计划停水");
        dataList.add("接水");
        dataList.add("维修");
        dataList.add("抢修");
        dataList.add("爆管");
        dataList.add("自检");
        dataList.add("其他");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataList);
        //设置样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        typeSpinner.setAdapter(adapter);
        if (!TextUtil.isNullOrEmpty(mSpinnerType)) {
            for (int i = 0; i < dataList.size(); i++) {
                if (mSpinnerType.equals(dataList.get(i))) {
                    typeSpinner.setSelection(i, true);
                    break;
                }
            }
        }
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSpinnerType = dataList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_dialog_notice_start_date_calendar:
                LogUtil.i(TAG, "start");
                chooseDate(true);
                break;
            case R.id.iv_dialog_notice_end_date_calendar:
                LogUtil.i(TAG, "end");
                chooseDate(false);
                break;
        }
    }

    /**
     * 选择日期
     *
     * @param isStart
     */
    private void chooseDate(final boolean isStart) {
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
            tvStartDate.setText(mYear + "-" + mCurrentMonth + "-" + mCurrentDay);
            try {
                String startDateStr = mYear + mCurrentMonth + mCurrentDay;
                mStartDateUtc = sdf.parse(startDateStr).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return;
        }
        tvEndDate.setText(mYear + "-" + mCurrentMonth + "-" + mCurrentDay);
        try {
            String endDateStr = mYear + mCurrentMonth + mCurrentDay;
            mEndDateUtc = sdf.parse(endDateStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (mStartDateUtc > mEndDateUtc) {
            tvEndDate.setText("");
            mEndDateUtc = 0;
            ApplicationsUtil.showMessage(this, "结束日期不能大于开始日期！");
        }
    }

    /**
     * 显示日期筛选dialog
     */
    private void showDatePickerDialog() {
//        if (mDatePickerDialog == null) {
//            View view = getLayoutInflater().inflate(R.layout.dialog_calendar, null);
//            CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_date_search);
//            final DatePicker datePicker = (DatePicker) view.findViewById(R.id.dp_date_search);
//            Calendar calendar = Calendar.getInstance();
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//            datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
//                @Override
//                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                    if (!mIsLimitDate) {
//                        mDateFilter = new StringBuffer().append(year).append("-")
//                                .append(monthOfYear + 1 >= 10 ? monthOfYear + 1 : "0" + (monthOfYear + 1)).append("-")
//                                .append(dayOfMonth >= 10 ? dayOfMonth : "0" + dayOfMonth).toString();
//                        listFilter("", "", "", mDateFilter);
//                    }
//                }
//            });
//            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    mIsLimitDate = isChecked;
//                    if (isChecked) {//不限日期
//                        mDateFilter = null;
//                    } else {
//                        mDateFilter = new StringBuffer().append(datePicker.getYear()).append("-")
//                                .append(datePicker.getMonth() + 1 >= 10 ? datePicker.getMonth() + 1 : "0" + (datePicker.getMonth() + 1)).append("-")
//                                .append(datePicker.getDayOfMonth() >= 10 ? datePicker.getDayOfMonth() : "0" + datePicker.getDayOfMonth()).toString();
//                    }
//                    listFilter("", "", "", mDateFilter);
//                }
//            });
//            mDatePickerDialog = new AlertDialog.Builder(this, R.style.DialogTheme)
//                    .setView(view)
//                    .create();
//            //dialog全屏显示
//            Window window = mDatePickerDialog.getWindow();
//            window.setGravity(Gravity.BOTTOM);//设置dialog在屏幕底部
//            WindowManager.LayoutParams lp = window.getAttributes();
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//            window.setAttributes(lp);//设置dialog宽度充满屏幕
//        }
//        mDatePickerDialog.show();
    }

    /*
     * 初始化控件
     */
    private void initView() {
        //刷新控件
        mDUNewsList = new ArrayList();
        mAdapter = new MyAdapter(mDUNewsList, NoWaterSupplyNoticeActivity.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        initRecyclerViewState();

        //已进入界面就刷新，访问网络数据
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);
//        showTopProgressbar(mSwipeRefreshLayout);
//        mNoWaterSupplyNoticePresenter.refreshData();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mNoWaterSupplyNoticePresenter.refreshData(true);
            }
        });

        //置顶按钮
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.smoothScrollToPosition(0);//滑到顶部
            }
        });
    }

    /**
     * 筛选
     *
     * @param categoryFilter
     */
    private void listFilter(String title, String content, String categoryFilter, long startDateUtc, long endDateUtc) {
        List<WaterStopNotificationEntity> duNewses = filterInput(filterCategory(filterDate(mDUNewsList, startDateUtc, endDateUtc), categoryFilter), title, content);
        mAdapter.setData(duNewses);
    }

    private List<WaterStopNotificationEntity> filterInput(List<WaterStopNotificationEntity> duNewses, String title, String contentStr) {
        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(contentStr)) {
            return duNewses;
        }
        if (duNewses.size() == 0) {
            return duNewses;
        }
        List<WaterStopNotificationEntity> duFilterNewses = new ArrayList<>();
        for (WaterStopNotificationEntity duNews : duNewses) {
//            if ((!TextUtil.isNullOrEmpty(title) && duNews.getContent().contains(title))
//                    || (!TextUtil.isNullOrEmpty(contentStr) && duNews.getContent().contains(contentStr))) {
//                duFilterNewses.add(duNews);
//            }
        }
        return duFilterNewses;
    }

    private List<WaterStopNotificationEntity> filterCategory(List<WaterStopNotificationEntity> duNewses, String categoryFilter) {
        if (TextUtils.isEmpty(categoryFilter)) {
            return duNewses;
        }
        if (duNewses.size() == 0) {
            return duNewses;
        }
        List<WaterStopNotificationEntity> duFilterNewses = new ArrayList<>();
        for (WaterStopNotificationEntity duNews : duNewses) {
//            if (categoryFilter.equals(duNews.getType())) {
//                duFilterNewses.add(duNews);
//            }
        }
        return duFilterNewses;
    }

    private List<WaterStopNotificationEntity> filterDate(List<WaterStopNotificationEntity> duNewses, long startDateUtc, long endDateUtc) {
        if (startDateUtc == 0 && endDateUtc == 0) {
            return duNewses;
        }
        if (duNewses.size() == 0) {
            return duNewses;
        }
        List<WaterStopNotificationEntity> duFilterNewses = new ArrayList<>();
//        for (WaterStopNotificationEntity duNews : duNewses) {
//            if (betweenDate(startDateUtc, endDateUtc, duNews.getPlanStart(), duNews.getPlanEnd())) {
//                duFilterNewses.add(duNews);
//            }
//        }
        return duFilterNewses;
    }

    /**
     * 判断是否在某个时间段之间
     *
     * @param dateStart
     * @param dateEnd
     * @return
     * @throws Exception
     */
    private boolean betweenDate(long startDateUtc, long endDateUtc, String dateStart, String dateEnd) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateS = format.parse(dateStart);
            Date dateE = format.parse(dateEnd);
            if (startDateUtc >= dateS.getTime() && endDateUtc <= dateE.getTime()) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 初始化RecyclerView状态
     */
    private void initRecyclerViewState() {
        final LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        //recyclerview添加滚动监听
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int firstItemPosition = manager.findFirstVisibleItemPosition();
                if (firstItemPosition > 10 && newState == 0) {
                    mFab.setVisibility(View.VISIBLE);
                } else {
                    mFab.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mNoWaterSupplyNoticePresenter.detachView();
        mEventBus.unregister(this);
    }

    @Override
    public void onGetRefreshData(final List<WaterStopNotificationEntity> duNewsList, boolean isFromRefresh) {
        LogUtil.i(TAG, "onGetRefreshData");
        hideTopProgressbar(mSwipeRefreshLayout);

        if (isFromRefresh) {
            mSpinnerType = mFilterTitle = mFilterContent = "";
            mStartDateUtc = mEndDateUtc = 0;
            isSearchFromSever = true;
        }

        if (duNewsList == null || duNewsList.size() == 0) {
            ApplicationsUtil.showMessage(this, R.string.toast_no_result);
            if (duNewsList != null && duNewsList.size() == 0) {
                mDUNewsList.clear();
                mDUNewsList.addAll(duNewsList);
                mAdapter.setData(duNewsList);
            }
            return;
        }
        mDUNewsList.clear();
        mDUNewsList.addAll(duNewsList);
        mAdapter.setData(duNewsList);

        if (duNewsList != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(MyModule.PACKAGE_NAME, getApplicationContext().getPackageName());
                jsonObject.put(MyModule.ACTIVITY_NAME, NoWaterSupplyNoticeActivity.class.getName());
                JSONArray subJSONArray = new JSONArray();
                subJSONArray.put("count#" + duNewsList.size());
                jsonObject.put(MyModule.DATA, subJSONArray);
                MyModule myModule = new MyModule(jsonObject.toString());
                MainApplication.get(getApplicationContext()).setMyModule(myModule);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showMessage(String message) {
        hideTopProgressbar(mSwipeRefreshLayout);
        ApplicationsUtil.showMessage(this, message);
    }


    /**
     * recyclerView 适配器
     */
    private class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<WaterStopNotificationEntity> mList;
        private Context mContext;

        public MyAdapter(List<WaterStopNotificationEntity> list, Context context) {
            mList = list;
            mContext = context;
        }

        public void setData(List<WaterStopNotificationEntity> list) {
            mList = list;
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.title.setText(mList.get(position).getPostType());
            viewHolder.date.setText(mList.get(position).getCutDt());
            viewHolder.content.setText(mList.get(position).getPostArea());

            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NoWaterSupplyNoticeActivity.this,
                            WaterNoticeDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constant.NOTICE_DETAILS, mList.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_no_water_notice_row_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        /**
         * 普通-显示数据item
         */
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public TextView date;
            public TextView content;
            public CardView cardView;

            public ViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.no_water_notice_item_tv_title);
                date = (TextView) view.findViewById(R.id.no_water_notice_item_tv_date);
                content = (TextView) view.findViewById(R.id.no_water_notice_item_tv_content);
                cardView = (CardView) view.findViewById(R.id.no_water_notice_item_cardview);
            }
        }
    }

    @Subscribe
    public void onInitResult(UIBusEvent.InitResult initResult) {
        LogUtil.i(TAG, "---onInitResult---" + initResult.isSuccess());
        if (initResult.isSuccess()) {
            showTopProgressbar(mSwipeRefreshLayout);
            mNoWaterSupplyNoticePresenter.refreshData(true);

            String key = TimeUtils.getNowString(CommonUtils.getFormat("yyyy-MM-dd"));
            String account = SPUtils.getInstance().getString(Constant.USERID);
            String tips = SPUtils.getInstance(account + "_SaveOrdersTips").getString("Tips","");
            boolean isTipsLimit = SPUtils.getInstance(account + "_SaveOrdersTips").getBoolean("IsTipsLimit",false);
            if(isTipsLimit){
                if(!(ActivityUtils.getTopActivity() instanceof HistoryOrdersActivity)){
                    showSaveOrdersTips();
                }
            }else{
                if (!tips.equals(key)) {
                    if(!(ActivityUtils.getTopActivity() instanceof HistoryOrdersActivity)){
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
}
