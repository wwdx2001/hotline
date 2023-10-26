package com.sh3h.hotline.ui.bill.detail.basic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.data.entity.response.DUBillBaseInfo;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.sh3h.hotline.ui.bill.detail.UserDetailInformationActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dengzhimin on 2016/9/22.
 */

public class BasicInformationFragment extends ParentFragment implements BasicInformationMvpView {

    @Inject
    BasicInformationPresenter mPresenter;

    @BindView(R.id.tv_acctId)
    TextView tv_acctId;

    @BindView(R.id.tv_cardId)
    TextView tv_cardId;

    @BindView(R.id.tv_perName)
    TextView tv_perName;

    @BindView(R.id.tv_custClCd)
    TextView tv_custClCd;

    @BindView(R.id.tv_custClass)
    TextView tv_custClass;

    @BindView(R.id.tv_creditClass)
    TextView tv_creditClass;

    @BindView(R.id.tv_riskClass)
    TextView tv_riskClass;

    @BindView(R.id.tv_creditValue)
    TextView tv_creditValue;

    @BindView(R.id.tv_phone25)
    TextView tv_phone25;

    @BindView(R.id.tv_address1)
    TextView tv_address1;

    @BindView(R.id.tv_address2)
    TextView tv_address2;

    @BindView(R.id.tv_address3)
    TextView tv_address3;

    @BindView(R.id.tv_address4)
    TextView tv_address4;

    @BindView(R.id.tv_acctFlg)
    TextView tv_acctFlg;

    @BindView(R.id.tv_drkInfo)
    TextView tv_drkInfo;

    @BindView(R.id.tv_sjfFlg)
    TextView tv_sjfFlg;

    @BindView(R.id.tv_totAmt)
    TextView tv_totAmt;

    @BindView(R.id.tv_waterPrice)
    TextView tv_waterPrice;

    @BindView(R.id.tv_wastePrice)
    TextView tv_wastePrice;

    @BindView(R.id.tv_acctMGrpDescr)
    TextView tv_acctMGrpDescr;

    @BindView(R.id.tv_bankFlg)
    TextView tv_bankFlg;

    private Unbinder mUnbinder;
    private CustomerInfoFindResult mBillBaseInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((UserDetailInformationActivity) getActivity()).getActivityComponent().inject(this);
        mBillBaseInfo = getArguments().getParcelable(Constant.BILLBASEINFO);
        mPresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_information, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        if (mBillBaseInfo != null) {
            tv_acctId.setText(mBillBaseInfo.getAcctId());
            tv_cardId.setText(mBillBaseInfo.getCardId());
            tv_perName.setText(mBillBaseInfo.getPerName());
            tv_custClCd.setText(mBillBaseInfo.getCustClCd());
            tv_custClass.setText(mBillBaseInfo.getCustClass());
            tv_creditClass.setText(mBillBaseInfo.getCreditClass());
            tv_riskClass.setText(mBillBaseInfo.getRiskClass());
            tv_creditValue.setText(mBillBaseInfo.getCreditValue());
            tv_phone25.setText(mBillBaseInfo.getPhone25());
            tv_address1.setText(mBillBaseInfo.getAddress1());
            tv_address2.setText(mBillBaseInfo.getAddress2());
            tv_address3.setText(mBillBaseInfo.getAddress3());
            tv_address4.setText(mBillBaseInfo.getAddress4());
            tv_acctFlg.setText(mBillBaseInfo.getAcctFlg());
            tv_drkInfo.setText(mBillBaseInfo.getDrkInfo() + "人");
            if ("0".equals(mBillBaseInfo.getSjfFlg())) {
                tv_sjfFlg.setText("未上阶梯");
            } else if ("1".equals(mBillBaseInfo.getSjfFlg())) {
                tv_sjfFlg.setText("已上阶梯");
            } else {
                tv_sjfFlg.setText(mBillBaseInfo.getSjfFlg());
            }

            tv_totAmt.setText(mBillBaseInfo.getTotAmt() + "");
            tv_waterPrice.setText(mBillBaseInfo.getWaterPrice() + "");
            tv_wastePrice.setText(mBillBaseInfo.getWastePrice() + "");
            tv_acctMGrpDescr.setText(mBillBaseInfo.getAcctMGrpDescr());
            if ("0".equals(mBillBaseInfo.getBankFlg())) {
                tv_bankFlg.setText("现金");
            } else if ("1".equals(mBillBaseInfo.getBankFlg())) {
                tv_bankFlg.setText("代扣");
            } else if ("2".equals(mBillBaseInfo.getBankFlg())) {
                tv_bankFlg.setText("托收");
            } else {
                tv_bankFlg.setText(mBillBaseInfo.getBankFlg());
            }

        }
        return view;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            mUnbinder.unbind();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

}
