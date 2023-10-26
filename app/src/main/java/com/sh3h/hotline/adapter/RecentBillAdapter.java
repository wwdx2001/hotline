package com.sh3h.hotline.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.RecentBillDetailInfoEntity;

import java.util.List;

public class RecentBillAdapter extends BaseQuickAdapter<RecentBillDetailInfoEntity.BillListBean, BaseViewHolder> {

    public RecentBillAdapter(int layoutResId, @Nullable List<RecentBillDetailInfoEntity.BillListBean> data) {
        super(layoutResId, data);
    }

    public RecentBillAdapter(@Nullable List<RecentBillDetailInfoEntity.BillListBean> data) {
        super(data);
    }

    public RecentBillAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, RecentBillDetailInfoEntity.BillListBean billListBean) {
        if ("BS".equals(billListBean.getFtType())) {
            baseViewHolder.setText(R.id.tv_ftType, "抄表账单");
        } else if ("AD".equals(billListBean.getFtType())) {
            baseViewHolder.setText(R.id.tv_ftType, "调整账单");
        } else {
            baseViewHolder.setText(R.id.tv_ftType, billListBean.getFtType());

        }
        baseViewHolder.setText(R.id.tv_ftTypeDesc, billListBean.getFtTypeDesc());
        baseViewHolder.setText(R.id.tv_siBillid, billListBean.getSiBillid());
        baseViewHolder.setText(R.id.tv_BillDate, billListBean.getBillDate());
        baseViewHolder.setText(R.id.tv_saTypeDesc, billListBean.getSaTypeDesc());
        baseViewHolder.setText(R.id.tv_cmAmt, billListBean.getCmAmt() + "");
        baseViewHolder.setText(R.id.tv_billVol, billListBean.getBillVol() == null ? "" : billListBean.getBillVol());

        if ("0".equals(billListBean.getPayFlg())) {
            baseViewHolder.setText(R.id.tv_payFlg, "未收");

        } else {
            baseViewHolder.setText(R.id.tv_payFlg, "已收");

        }


    }
}
