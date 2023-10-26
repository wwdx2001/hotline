package com.sh3h.hotline.adapter;

import android.support.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.JinQiCMEntity;

import java.util.List;
import java.util.Map;

public class JinQiCMAdapter extends BaseQuickAdapter<JinQiCMEntity.MrListBean, BaseViewHolder> {

    List<JinQiCMEntity.MrListBean> data;
    private Map<String, String> map;

    public JinQiCMAdapter(Map<String, String> map, int layoutResId, @Nullable List<JinQiCMEntity.MrListBean> data) {
        super(layoutResId, data);
        this.data = data;
        this.map = map;
    }

    public JinQiCMAdapter(int layoutResId, @Nullable List<JinQiCMEntity.MrListBean> data) {
        super(layoutResId, data);
        this.data = data;
        this.map = map;
    }

    public JinQiCMAdapter(@Nullable List<JinQiCMEntity.MrListBean> data) {
        super(data);
    }

    public JinQiCMAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, JinQiCMEntity.MrListBean mrListBean) {
        baseViewHolder.setText(R.id.tv_badgeNbr, mrListBean.getBadgeNbr());
        baseViewHolder.setText(R.id.tv_readDttm, mrListBean.getReadDttm());
        baseViewHolder.setText(R.id.tv_mrSource, mrListBean.getMrSource());
        baseViewHolder.setText(R.id.tv_regReading, mrListBean.getRegReading() + "");

        if (map != null && !StringUtils.isEmpty(mrListBean.getLastMrNote())) {
            baseViewHolder.setText(R.id.tv_lastMrNote, map.get(mrListBean.getLastMrNote()));
        } else {
            baseViewHolder.setText(R.id.tv_lastMrNote, mrListBean.getLastMrNote());
        }
        if (map != null && !StringUtils.isEmpty(map.get(mrListBean.getLastReadStatus()))) {
            baseViewHolder.setText(R.id.tv_lastReadStatus, map.get(mrListBean.getLastReadStatus()));
        } else {
            baseViewHolder.setText(R.id.tv_lastReadStatus, mrListBean.getLastReadStatus());
        }

    }
}
