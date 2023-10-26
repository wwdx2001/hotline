package com.sh3h.hotline.adapter;

import android.support.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sh3h.dataprovider.data.entity.response.CustomerTotalArrearsResult;
import com.sh3h.hotline.R;

import java.util.List;

public class ArrearsInfoResultAdapter extends BaseQuickAdapter<CustomerTotalArrearsResult, BaseViewHolder> {

    private List<CustomerTotalArrearsResult> data;

    public ArrearsInfoResultAdapter(int layoutResId, @Nullable List<CustomerTotalArrearsResult> data) {
        super(layoutResId, data);
        this.data = data;
    }

    public ArrearsInfoResultAdapter(@Nullable List<CustomerTotalArrearsResult> data) {
        super(data);
    }

    public ArrearsInfoResultAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CustomerTotalArrearsResult customerTotalArrearsResult) {
        baseViewHolder.setText(R.id.tv_entityName, customerTotalArrearsResult.getEntityName());
        baseViewHolder.setText(R.id.tv_cisMrgGrp, customerTotalArrearsResult.getCisMrgGrp());
        baseViewHolder.setText(R.id.tv_address1, customerTotalArrearsResult.getAddress1());
        baseViewHolder.setText(R.id.tv_QryTime, customerTotalArrearsResult.getQryTime());
        baseViewHolder.setText(R.id.tv_cmTotalAmt, StringUtils.isEmpty(customerTotalArrearsResult.getCmTotalAmt()) ? ""
                : customerTotalArrearsResult.getCmTotalAmt());
        baseViewHolder.setText(R.id.tv_chargeAmt, customerTotalArrearsResult.getChargeAmt() + "");
    }
}
