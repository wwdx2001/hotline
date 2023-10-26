package com.sh3h.hotline.ui.notice;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.response.WaterStopNotificationEntity;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.ParentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 公告详情
 * Created by zhangjing on 2016/9/29.
 */

public class WaterNoticeDetailsActivity extends ParentActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv1)
    TextView mTv1;
    @BindView(R.id.tv2)
    TextView mTv2;
    @BindView(R.id.tv3)
    TextView mTv3;
    @BindView(R.id.tv4)
    TextView mTv4;
    @BindView(R.id.tv5)
    TextView mTv5;
    @BindView(R.id.tv6)
    TextView mTv6;
    @BindView(R.id.line)
    View mLine;
    @BindView(R.id.tv_leixing)
    TextView mTvLeixing;
    @BindView(R.id.tv_fabushijian)
    TextView mTvFabushijian;
    @BindView(R.id.tv_faburen)
    TextView mTvFaburen;
    @BindView(R.id.tv_fabudanwei)
    TextView mTvFabudanwei;
    @BindView(R.id.tv_tingshuishijian)
    TextView mTvTingshuishijian;
    @BindView(R.id.tv_tingshuifanwei)
    TextView mTvTingshuifanwei;
    @BindView(R.id.tv_neirong)
    TextView mTvNeirong;
    private Unbinder mUnbinder;

//    @BindView(R.id.tv_notice_type)
//    TextView mNoticeType;//公告类型
//
//    @BindView(R.id.tv_notice_date_time)
//    TextView mNoitceDateTime;//发布时间
//
//    @BindView(R.id.tv_notice_publish_person)
//    TextView mPublishPerson;//发布人
//
//    @BindView(R.id.tv_notice_publish_danwei)
//    TextView mPublishUnit;//发布单位
//
//    @BindView(R.id.tv_no_water_time)
//    TextView mNoWaterTime;//停水时间
//
//    @BindView(R.id.tv_notice_fanwei)
//    TextView mNoticeScope;//公告范围
//
//    @BindView(R.id.tv_notice_content)
//    TextView mNoticeContent;//公告内容


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_notice_details);
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);
        initToolBar(R.string.label_notice_details);
        init();
    }

    private void init() {
        if (getIntent() == null
                || getIntent().getExtras().getParcelable(Constant.NOTICE_DETAILS) == null) {
            return;
        }

        WaterStopNotificationEntity duNews = getIntent().getExtras().getParcelable(Constant.NOTICE_DETAILS);
        mTvLeixing.setText(duNews.getPostType());
        mTvFabushijian.setText(duNews.getPublishDt());
        mTvFaburen.setText(duNews.getPublisher());
        mTvFabudanwei.setText(duNews.getCompany());
        mTvTingshuishijian.setText(duNews.getCutDt());
        mTvTingshuifanwei.setText(duNews.getPostArea());
        mTvNeirong.setText(duNews.getPostContent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
