package com.sh3h.hotline.adapter;

import android.support.annotation.Nullable;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.ReceiptTabStatisticsEntity;

import java.util.List;

public class ReceiptTabStatisticsAdapter extends BaseQuickAdapter<ReceiptTabStatisticsEntity, BaseViewHolder> {

    private List<ReceiptTabStatisticsEntity> data;

    public ReceiptTabStatisticsAdapter(int layoutResId, @Nullable List<ReceiptTabStatisticsEntity> data) {
        super(layoutResId, data);
        this.data = data;
    }

    public ReceiptTabStatisticsAdapter(@Nullable List<ReceiptTabStatisticsEntity> data) {
        super(data);
    }

    public ReceiptTabStatisticsAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final ReceiptTabStatisticsEntity receiptTabStatisticsEntity) {
        baseViewHolder.setText(R.id.type, receiptTabStatisticsEntity.getPdlx());
        baseViewHolder.setText(R.id.time, receiptTabStatisticsEntity.getPch());
        baseViewHolder.setText(R.id.count, receiptTabStatisticsEntity.getGongdanshu()+"");
        baseViewHolder.setText(R.id.finished, receiptTabStatisticsEntity.getYiwancheng()+"");
        baseViewHolder.setText(R.id.undone, receiptTabStatisticsEntity.getWeiwancheng()+"");
    }
}
