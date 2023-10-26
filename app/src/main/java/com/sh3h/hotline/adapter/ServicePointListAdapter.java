package com.sh3h.hotline.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.hotline.R;

import java.util.List;

public class ServicePointListAdapter extends BaseQuickAdapter<CustomerInfoFindResult.SpListBean, BaseViewHolder> {

    private List<CustomerInfoFindResult.SpListBean> data;

    public ServicePointListAdapter(int layoutResId, @Nullable List<CustomerInfoFindResult.SpListBean> data) {
        super(layoutResId, data);
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CustomerInfoFindResult.SpListBean spListBean) {
        baseViewHolder.setText(R.id.tv_spId,spListBean.getSpId());
        baseViewHolder.setText(R.id.tv_mtrUseType,spListBean.getMtrUseType());
        baseViewHolder.setText(R.id.tv_mtrId,spListBean.getMtrId());
        baseViewHolder.setText(R.id.tv_badgeNbr,spListBean.getBadgeNbr());
        baseViewHolder.setText(R.id.tv_mtrRfid,spListBean.getMtrRfid());
        baseViewHolder.setText(R.id.tv_mtrDN,spListBean.getMtrDN());
        baseViewHolder.setText(R.id.tv_mtrRange,spListBean.getMtrRange());
        baseViewHolder.setText(R.id.tv_mtrLocDesc,spListBean.getMtrLocDesc());
        baseViewHolder.setText(R.id.tv_mtrLocDetails,spListBean.getMtrLocDetails());
        baseViewHolder.setText(R.id.tv_mtrInstallDt,spListBean.getMtrInstallDt());
        baseViewHolder.setText(R.id.tv_zfbFlg,spListBean.getZfbFlg());
        baseViewHolder.setText(R.id.tv_mrRteCd,spListBean.getMrRteCd());
        baseViewHolder.setText(R.id.tv_cbyDesc,spListBean.getCbyDesc());

    }
}
