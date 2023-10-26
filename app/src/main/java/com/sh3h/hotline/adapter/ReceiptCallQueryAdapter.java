package com.sh3h.hotline.adapter;

import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.OverrateSelfBillingEntity;
import com.sh3h.mobileutil.util.LogUtil;

import java.util.List;

public class ReceiptCallQueryAdapter extends BaseQuickAdapter<OverrateSelfBillingEntity, BaseViewHolder> {

    private List<OverrateSelfBillingEntity> data;
    private int checkPosition = -1;

    public ReceiptCallQueryAdapter(int layoutResId, @Nullable List<OverrateSelfBillingEntity> data) {
        super(layoutResId, data);
        this.data = data;
    }

    public ReceiptCallQueryAdapter(@Nullable List<OverrateSelfBillingEntity> data) {
        super(data);
    }

    public ReceiptCallQueryAdapter(int layoutResId) {
        super(layoutResId);
    }

    public void setDefaultSelect(int position) {
        this.checkPosition = position;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final OverrateSelfBillingEntity overrateSelfBillingEntity) {

        baseViewHolder.setText(R.id.tv_zhbh, overrateSelfBillingEntity.getZhbh());
        baseViewHolder.setText(R.id.tv_cbh, overrateSelfBillingEntity.getCbc());
        baseViewHolder.setText(R.id.tv_qfje, overrateSelfBillingEntity.getQfje());
        baseViewHolder.setText(R.id.tv_gls, overrateSelfBillingEntity.getGls());
        baseViewHolder.setText(R.id.tv_zd, overrateSelfBillingEntity.getZd());
        baseViewHolder.setText(R.id.tv_khmc, overrateSelfBillingEntity.getKhmc());
        baseViewHolder.setText(R.id.tv_ysdz, overrateSelfBillingEntity.getDz());
        baseViewHolder.setText(R.id.tv_ssdm, overrateSelfBillingEntity.getKhlx());

//        CheckBox cb = baseViewHolder.getView(R.id.cb_query);
//        cb.setOnCheckedChangeListener(null);//重要，用于清空监听器
//        cb.setChecked(overrateSelfBillingEntity.isChecked());
//        baseViewHolder.setOnCheckedChangeListener(R.id.cb_query, new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                overrateSelfBillingEntity.setChecked(isChecked);
//                if (isChecked) {
//                    LogUtils.e("选中了" + baseViewHolder.getPosition());
//
//
//                } else {
//                    LogUtils.e("取消了" + baseViewHolder.getPosition());
//
//                }
//            }
//        });

        ImageView imageRadio = baseViewHolder.getView(R.id.image_radio);
        int position = baseViewHolder.getAdapterPosition();
        if (checkPosition != -1) {
            if (checkPosition == position) {
                imageRadio.setImageResource(R.drawable.ic_radio_button_checked);
            } else {
                imageRadio.setImageResource(R.drawable.ic_radio_button_unchecked);
            }
        } else {
            imageRadio.setImageResource(R.drawable.ic_radio_button_unchecked);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
