package com.sh3h.hotline.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sh3h.dataprovider.data.entity.response.CustomerTotalArrearsResult;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.OverrateCallHandleEntityDao;
import com.sh3h.dataprovider.greendaoDao.OverrateSelfHandleEntityDao;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.OverrateSelfBillingEntity;

import java.util.List;

public class QuerySelectAdapter extends BaseQuickAdapter<OverrateSelfBillingEntity, BaseViewHolder> {

    private List<OverrateSelfBillingEntity> data;
    private int checkPosition = -1;

    public QuerySelectAdapter(int layoutResId, @Nullable List<OverrateSelfBillingEntity> data) {
        super(layoutResId, data);
        this.data = data;
    }

    public QuerySelectAdapter(@Nullable List<OverrateSelfBillingEntity> data) {
        super(data);
    }

    public QuerySelectAdapter(int layoutResId) {
        super(layoutResId);
    }

    public void setDefaultSelect(int position) {
        this.checkPosition = position;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final OverrateSelfBillingEntity overrateSelfBillingEntity) {

        baseViewHolder.setText(R.id.tv_zhanghubh, overrateSelfBillingEntity.getYhh() + "/" + overrateSelfBillingEntity.getZhbh());
        baseViewHolder.setText(R.id.tv_kehumc, overrateSelfBillingEntity.getKhmc());
        baseViewHolder.setText(R.id.tv_xingzhengq, overrateSelfBillingEntity.getXzq());
        baseViewHolder.setText(R.id.tv_sheshuidm, overrateSelfBillingEntity.getKhlx());
        baseViewHolder.setText(R.id.tv_lianxir, overrateSelfBillingEntity.getLxr());
        baseViewHolder.setText(R.id.tv_lianxifs, overrateSelfBillingEntity.getLxfs());
        baseViewHolder.setText(R.id.tv_youjidz, overrateSelfBillingEntity.getYjdz());
        baseViewHolder.setText(R.id.tv_tongyishxydm, overrateSelfBillingEntity.getTyshxydm());

//        baseViewHolder.addOnClickListener(R.id.tv_sheshuidm,R.id.tv_youjidz);

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

        long count = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getOverrateSelfHandleEntityDao().queryBuilder()
                .where(OverrateSelfHandleEntityDao.Properties.Zhbh.eq(overrateSelfBillingEntity.getZhbh()))
                .count();
        baseViewHolder.setGone(R.id.txt_submited, count > 0);
    }
}
