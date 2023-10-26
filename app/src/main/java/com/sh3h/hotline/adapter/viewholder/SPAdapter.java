package com.sh3h.hotline.adapter.viewholder;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.hotline.R;

import java.util.List;

/**
 * 服务点编号Adapter
 *
 * @author xiaochao.dev@gamil.com
 * @date 2019/4/3 22:17
 */
public class SPAdapter extends BaseQuickAdapter<CustomerInfoFindResult.SpListBean, BaseViewHolder> {
    public SPAdapter(int layoutResId, @Nullable List<CustomerInfoFindResult.SpListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CustomerInfoFindResult.SpListBean spListBean) {
        baseViewHolder.setText(R.id.tv_text, spListBean.getSpId());
    }
}
