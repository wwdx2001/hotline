package com.sh3h.hotline.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.OverrateCallHandleEntityDao;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.CallListEntity;

import java.util.List;

public class CallListAdapter extends BaseQuickAdapter<CallListEntity, BaseViewHolder> {

    private List<CallListEntity> data;

    public CallListAdapter(int layoutResId, @Nullable List<CallListEntity> data) {
        super(layoutResId, data);
        this.data = data;
    }

    public CallListAdapter(@Nullable List<CallListEntity> data) {
        super(data);
    }

    public CallListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final CallListEntity callListEntity) {
        baseViewHolder.setText(R.id.tv_xuhao, callListEntity.getXh());
        baseViewHolder.setText(R.id.tv_pch, callListEntity.getPch());

        SpannableString content = new SpannableString(callListEntity.getYhh());
        content.setSpan(new UnderlineSpan(), 0, callListEntity.getYhh().length(), 0);
        baseViewHolder.setText(R.id.tv_yonghuhao, content);

        baseViewHolder.setText(R.id.tv_xingzhengq, callListEntity.getXzq());
        baseViewHolder.setText(R.id.tv_kehumc, callListEntity.getKhmc());

        baseViewHolder.setText(R.id.tv_youjidz, callListEntity.getYjdz());
        baseViewHolder.setText(R.id.tv_qianfeije, callListEntity.getQfzje());
        String wcsx = callListEntity.getCjqx();
        try {
            if (!StringUtils.isEmpty(wcsx)) {
                wcsx = wcsx.split(" ")[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        baseViewHolder.setText(R.id.txt_wcsx, wcsx);

        TextView textView1 = baseViewHolder.getView(R.id.txt_delay);
        TextView textView2 = baseViewHolder.getView(R.id.txt_chargeback);
        TextView textView3 = baseViewHolder.getView(R.id.txt_operate);
        baseViewHolder.addOnClickListener(R.id.tv_yonghuhao, R.id.txt_delay, R.id.txt_chargeback, R.id.txt_operate);

        if (!"0".equals(callListEntity.getSfyq())) {
            baseViewHolder.setEnabled(R.id.txt_delay, false);
            textView1.setClickable(false);
            textView1.setTextColor(mContext.getResources().getColor(R.color.color_gray_767676));
            Drawable drawable1 = mContext.getResources().getDrawable(R.mipmap.ic_delay_order_gray_48px);
            drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
            textView1.setCompoundDrawables(drawable1, null, null, null);
            baseViewHolder.setGone(R.id.txt_delayed, true);
        } else {
            baseViewHolder.setEnabled(R.id.txt_delay, true);
            textView1.setClickable(true);
            textView1.setTextColor(mContext.getResources().getColor(R.color.color_blue));
            Drawable drawable1 = mContext.getResources().getDrawable(R.mipmap.ic_delay_order_blue_48px);
            drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
            textView1.setCompoundDrawables(drawable1, null, null, null);
            baseViewHolder.setGone(R.id.txt_delayed, false);
        }

        if (!TextUtils.isEmpty(callListEntity.getSfzf()) && !"0".equals(callListEntity.getSfzf())) {
            baseViewHolder.setEnabled(R.id.txt_chargeback, false);
            textView2.setClickable(false);
            textView2.setTextColor(mContext.getResources().getColor(R.color.color_gray_767676));
            Drawable drawable2 = mContext.getResources().getDrawable(R.mipmap.ic_back_order_gray_48px);
            drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
            textView2.setCompoundDrawables(drawable2, null, null, null);
        } else {
            baseViewHolder.setEnabled(R.id.txt_chargeback, true);
            textView2.setClickable(true);
            textView2.setTextColor(mContext.getResources().getColor(R.color.color_blue));
            Drawable drawable2 = mContext.getResources().getDrawable(R.mipmap.ic_back_order_blue_48px);
            drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
            textView2.setCompoundDrawables(drawable2, null, null, null);
        }

        baseViewHolder.setGone(R.id.txt_advent, ((TimeUtils.string2Millis(callListEntity.getCjqx()) - TimeUtils.getNowMills()) / (24 * 60 * 60 * 1000)) <= 3);

        long count = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getOverrateCallHandleEntityDao().queryBuilder()
                .where(OverrateCallHandleEntityDao.Properties.Albh.eq(callListEntity.getAlbh()))
                .count();
        baseViewHolder.setGone(R.id.txt_submited, count > 0);

//        if (((TimeUtils.string2Millis(callListEntity.getCqsx()) - TimeUtils.getNowMills()) / (24 * 60 * 60 * 1000)) <= 3) {
//            baseViewHolder.setBackgroundColor(R.id.constriant_call,mContext.getResources().getColor(R.color.color_red_f7cdd6));
//        } else {
//            baseViewHolder.setBackgroundColor(R.id.constriant_call,mContext.getResources().getColor(R.color.color_white_ffffff));
//        }
    }
}
