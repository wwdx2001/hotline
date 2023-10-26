package com.sh3h.hotline.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.CallTabStatisticsEntity;

import java.util.List;

public class CallTabStatisticsAdapter extends BaseQuickAdapter<CallTabStatisticsEntity, BaseViewHolder> {

    private List<CallTabStatisticsEntity> data;

    public CallTabStatisticsAdapter(int layoutResId, @Nullable List<CallTabStatisticsEntity> data) {
        super(layoutResId, data);
        this.data = data;
    }

    public CallTabStatisticsAdapter(@Nullable List<CallTabStatisticsEntity> data) {
        super(data);
    }

    public CallTabStatisticsAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final CallTabStatisticsEntity callTabStatisticsEntity) {
        baseViewHolder.setText(R.id.pch, callTabStatisticsEntity.getPch()+"");
        baseViewHolder.setText(R.id.count, callTabStatisticsEntity.getGongdanshu()+"");
        baseViewHolder.setText(R.id.finished, callTabStatisticsEntity.getYiwancheng()+"");
        baseViewHolder.setText(R.id.undone, callTabStatisticsEntity.getWeiwancheng()+"");
    }
}
