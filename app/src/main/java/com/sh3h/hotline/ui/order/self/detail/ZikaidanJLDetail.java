package com.sh3h.hotline.ui.order.self.detail;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.sh3h.dataprovider.data.entity.newentity.ZikaidanJLEntity;
import com.sh3h.dataprovider.data.entity.response.CLJBBean;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.ParentActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 自开单记录详情
 */
public class ZikaidanJLDetail extends ParentActivity {

    @BindView(R.id.cb_xianchangchuli)
    CheckBox mCbXianchangchuli;
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
    @BindView(R.id.et_fuwudianbh)
    TextView mEtFuwudianbh;
    @BindView(R.id.et_fanyinglx)
    TextView mEtFanyinglx;
    @BindView(R.id.et_fanyingnr)
    TextView mEtFanyingnr;
    @BindView(R.id.et_chulijb)
    TextView mEtChulijb;
    @BindView(R.id.et_fashengdizhi)
    TextView mEtFashengdizhi;
    @BindView(R.id.et_kaidanshijian)
    TextView mEtKaidanshijian;
    @BindView(R.id.et_xczt)
    TextView mEtXczt;
    @BindView(R.id.et_zyr)
    TextView mEtZyr;
    @BindView(R.id.et_slzd)
    TextView mEtSlzd;
    @BindView(R.id.et_zhbh)
    TextView mEtZhbh;
    @BindView(R.id.et_lxdh)
    TextView mEtLxdh;
    @BindView(R.id.et_khmc)
    TextView mEtKhmc;
    private Toolbar mToolbar;
    private ZikaidanJLEntity mZikaidanJLEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zikaidan_jldetail);
        ButterKnife.bind(this);
        mToolbar = mToolbar;
        mToolbar = initToolBar(R.string.activity_createselforderdetail);
        mZikaidanJLEntity = getIntent().getParcelableExtra(Constant.ZIKAIDAN_JL);
        initData();
    }

    private void initData() {
        List<CLJBBean> cljbBeans = GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getCLJBBeanDao().loadAll();
        Map<String, String> cljbMap = new HashMap<>();
        for (int i = 0; i < cljbBeans.size(); i++) {
            cljbMap.put(cljbBeans.get(i).getCLJB_ID(), cljbBeans.get(i).getDESCR());
        }
        if (mZikaidanJLEntity != null) {
//            mCbXianchangchuli.setChecked(mZikaidanJLEntity.getIsXianChangCL() == 1 ? true : false);
            mEtFuwudianbh.setText(mZikaidanJLEntity.getFaId());
            if (StringUtils.isEmpty(cljbMap.get(mZikaidanJLEntity.getCljb()))) {
                mEtChulijb.setText(mZikaidanJLEntity.getCljb());
            } else {
                mEtChulijb.setText(cljbMap.get(mZikaidanJLEntity.getCljb()));
            }
            mEtFanyinglx.setText(mZikaidanJLEntity.getFaTypeCd());
            mEtFanyingnr.setText(mZikaidanJLEntity.getFynr());
            mEtFashengdizhi.setText(mZikaidanJLEntity.getFsdz());
            mEtKaidanshijian.setText(mZikaidanJLEntity.getClsx());
            mEtXczt.setText(mZikaidanJLEntity.getCmSta());
            mEtZyr.setText(mZikaidanJLEntity.getRepCd());
            mEtSlzd.setText(mZikaidanJLEntity.getDisPatGrp());
            mEtZhbh.setText(mZikaidanJLEntity.getAcctId());
            mEtLxdh.setText(mZikaidanJLEntity.getContactValue());
            mEtKhmc.setText(mZikaidanJLEntity.getEntityName());
        }
        mCbXianchangchuli.setEnabled(false);
    }
}
