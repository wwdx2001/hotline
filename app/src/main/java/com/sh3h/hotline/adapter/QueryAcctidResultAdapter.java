package com.sh3h.hotline.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sh3h.dataprovider.data.entity.response.FuzzyAdressQueryResult;
import com.sh3h.hotline.R;

import java.util.List;

public class QueryAcctidResultAdapter extends BaseQuickAdapter<FuzzyAdressQueryResult, BaseViewHolder> {

    private List<FuzzyAdressQueryResult> data;

    public QueryAcctidResultAdapter(int layoutResId, @Nullable List<FuzzyAdressQueryResult> data) {
        super(layoutResId, data);
        this.data = data;
    }

    public QueryAcctidResultAdapter(@Nullable List<FuzzyAdressQueryResult> data) {
        super(data);
    }

    public QueryAcctidResultAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, FuzzyAdressQueryResult fuzzyAdressQueryResult) {
        baseViewHolder.setText(R.id.tv_number, baseViewHolder.getAdapterPosition() + 1 + "/" + data.size() + "");
        baseViewHolder.setText(R.id.tv_zhanghubh, fuzzyAdressQueryResult.getAcctId());
        baseViewHolder.setText(R.id.tv_address, fuzzyAdressQueryResult.getAddress1());
    }
}
