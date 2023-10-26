package com.sh3h.hotline.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.CallBillTabEntity;

import java.util.List;

public class CallBillTabAdapter extends BaseQuickAdapter<CallBillTabEntity, BaseViewHolder> {

    private List<CallBillTabEntity> data;

    public CallBillTabAdapter(int layoutResId, @Nullable List<CallBillTabEntity> data) {
        super(layoutResId, data);
        this.data = data;
    }

    public CallBillTabAdapter(@Nullable List<CallBillTabEntity> data) {
        super(data);
    }

    public CallBillTabAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final CallBillTabEntity callBillTabEntity) {
        baseViewHolder.setText(R.id.tv_nian, callBillTabEntity.getYear());
        baseViewHolder.setText(R.id.tv_jidu, callBillTabEntity.getJd());
        baseViewHolder.setText(R.id.tv_cdesl, callBillTabEntity.getCdesl());
        baseViewHolder.setText(R.id.tv_kzje, callBillTabEntity.getKzje());
        baseViewHolder.setText(R.id.tv_kzsl, callBillTabEntity.getKzrq());
    }
}
