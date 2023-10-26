package com.sh3h.hotline.ui.order.myorder.remotewater;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.sh3h.dataprovider.data.entity.newentity.QuestionHandleEntity;
import com.sh3h.dataprovider.data.entity.newentity.RemoteHandleEntity;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.QuestionHandleEntityDao;
import com.sh3h.dataprovider.greendaoDao.RemoteHandleEntityDao;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.ParentFragment;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RemoteWaterHandleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemoteWaterHandleFragment extends ParentFragment implements View.OnClickListener {

    @BindView(R.id.et_cbds)
    public EditText mEtCbds;

    @BindView(R.id.et_dssj)
    public EditText mEtDssj;

    @BindView(R.id.et_bz)
    public EditText mEtBz;

    private TimePickerView pvCustomTime;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Unbinder mUnbinder;
    private DUMyTask mDuMyTask;

    public RemoteWaterHandleFragment() {
    }

    public static RemoteWaterHandleFragment newInstance(String param1, String param2) {
        RemoteWaterHandleFragment fragment = new RemoteWaterHandleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remote_water_handle, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mEtDssj.setOnClickListener(this);
        initCustomTimePicker();
        initData(view);
        return view;
    }

    private void initData(View view) {
        Bundle bundle = getArguments();
        if (bundle != null) {

            mDuMyTask = bundle.getParcelable(Constant.DUMyTask);

            if (mDuMyTask != null) {
                List<RemoteHandleEntity> entityList = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                        .getRemoteHandleEntityDao()
                        .queryBuilder()
                        .where(RemoteHandleEntityDao.Properties.Albh.eq(mDuMyTask.getCaseId()))
                        .list();
                if (entityList != null && entityList.size() > 0) {
                    RemoteHandleEntity remoteHandleEntity = entityList.get(0);

                    mEtCbds.setText(remoteHandleEntity.getCbds());
                    mEtDssj.setText(remoteHandleEntity.getDssj());
                    mEtBz.setText(remoteHandleEntity.getBz());
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        mUnbinder.unbind();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onClick(View v) {
        pvCustomTime.show();
    }

    /**
     * 自定义时间选择器
     */
    private void initCustomTimePicker() {

        /**
         * @description
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2099, 11, 31);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerBuilder(ActivityUtils.getTopActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
//                Toast.makeText(ActivityUtils.getTopActivity(), TimeUtils.date2String(date), Toast.LENGTH_SHORT).show();
                mEtDssj.setText(TimeUtils.date2String(date));
            }
        })
                /*.setType(new boolean[]{true, true, true, true, true, true})//default is all
                .setCancelText("取消")
                .setSubmitText("完成")
                .setContentTextSize(18)
                .setTitleSize(20)
                .setTitleText("读数时间")
                .setTitleColor(Color.BLACK)
               /*.setDividerColor(Color.WHITE)//设置分割线的颜色
                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
                .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)*/
                /*.animGravity(Gravity.RIGHT)// default is center*/
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setContentTextSize(18)
                .setType(new boolean[]{true, true, true, true, true, true})
                .setCancelText("取消")
                .setSubmitText("完成")
                .setContentTextSize(18)
                .setTitleSize(20)
                .setTitleText("读数时间")
                .setTitleColor(Color.BLACK)
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(0, 0, 0, 0, 0, 0)
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();

    }
}