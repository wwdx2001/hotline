package com.sh3h.hotline.adapter;

import android.support.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sh3h.dataprovider.data.entity.newentity.ZikaidanJLEntity;
import com.sh3h.hotline.R;

import java.util.List;
import java.util.Map;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/4/4 09:56
 */
public class ZikaidanJLAdapter extends BaseQuickAdapter<ZikaidanJLEntity, BaseViewHolder> {
    private Map<String, String> map;

    public ZikaidanJLAdapter(int layoutResId, @Nullable List<ZikaidanJLEntity> data, Map<String, String> map) {
        super(layoutResId, data);
        this.map = map;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ZikaidanJLEntity zikaidanJLEntity) {
        baseViewHolder.setText(R.id.tv_fuwudianbh, zikaidanJLEntity.getFaId());
        baseViewHolder.setText(R.id.tv_fanyinglx, zikaidanJLEntity.getFaTypeCd());
        if (map != null && !StringUtils.isEmpty(zikaidanJLEntity.getCljb())) {
            baseViewHolder.setText(R.id.tv_chulijb, map.get(zikaidanJLEntity.getCljb()));
        } else {
            baseViewHolder.setText(R.id.tv_chulijb, zikaidanJLEntity.getCljb());
        }
        baseViewHolder.setText(R.id.tv_kaidansj, zikaidanJLEntity.getClsx());
    }
}
