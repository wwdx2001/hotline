package com.sh3h.hotline.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.OverrateCallHandleEntityDao;
import com.sh3h.dataprovider.greendaoDao.OverrateReceiptHandleEntityDao;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.ReceiptListEntity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReceiptListAdapter extends BaseQuickAdapter<ReceiptListEntity, BaseViewHolder> {

    private List<ReceiptListEntity> data;

    public ReceiptListAdapter(int layoutResId, @Nullable List<ReceiptListEntity> data) {
        super(layoutResId, data);
        this.data = data;
    }

    public ReceiptListAdapter(@Nullable List<ReceiptListEntity> data) {
        super(data);
    }

    public ReceiptListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final ReceiptListEntity receiptListEntity) {
        baseViewHolder.setText(R.id.tv_xuhao, receiptListEntity.getXh());
        baseViewHolder.setText(R.id.tv_paidanlx, receiptListEntity.getPdlx());
        baseViewHolder.setText(R.id.tv_pch, receiptListEntity.getPch());

        SpannableString content = new SpannableString(receiptListEntity.getYhh());
        content.setSpan(new UnderlineSpan(), 0, receiptListEntity.getYhh().length(), 0);
        baseViewHolder.setText(R.id.tv_yonghuhao, content);

        baseViewHolder.setText(R.id.tv_xingzhengq, receiptListEntity.getXzq());
        baseViewHolder.setText(R.id.tv_kehumc, receiptListEntity.getKhmc());

        baseViewHolder.setText(R.id.tv_youjidz, receiptListEntity.getYjdz());
        baseViewHolder.setText(R.id.tv_qianfeije, receiptListEntity.getQfje());
        baseViewHolder.setText(R.id.tv_kehumc, receiptListEntity.getKhmc());
        String wcsx = receiptListEntity.getCqsx();
        try {
            if (!StringUtils.isEmpty(wcsx)) {
                wcsx = wcsx.split(" ")[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        baseViewHolder.setText(R.id.txt_wcsx, wcsx);

        if (!TextUtils.isEmpty(receiptListEntity.getPdlx()) && "超定额季度账单".equals(receiptListEntity.getPdlx())) {
            baseViewHolder.setText(R.id.tv7, "旧欠金额：");
        }

        TextView textView1 = baseViewHolder.getView(R.id.txt_delay);
        TextView textView2 = baseViewHolder.getView(R.id.txt_chargeback);
        TextView textView3 = baseViewHolder.getView(R.id.txt_operate);
        baseViewHolder.addOnClickListener(R.id.tv_yonghuhao, R.id.txt_delay, R.id.txt_chargeback, R.id.txt_operate);

        if (!"0".equals(receiptListEntity.getSfyq())) {
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

        if (!TextUtils.isEmpty(receiptListEntity.getSfzf()) && !"0".equals(receiptListEntity.getSfzf())) {
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

        baseViewHolder.setGone(R.id.txt_advent, ((TimeUtils.string2Millis(receiptListEntity.getCqsx()) - TimeUtils.getNowMills()) / (24 * 60 * 60 * 1000)) <= 3);

        long count = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getOverrateReceiptHandleEntityDao().queryBuilder()
                .where(OverrateReceiptHandleEntityDao.Properties.Albh.eq(receiptListEntity.getAlbh()))
                .count();
        baseViewHolder.setGone(R.id.txt_submited, count > 0);

//        if (((TimeUtils.string2Millis(receiptListEntity.getCqsx()) - TimeUtils.getNowMills()) / (24 * 60 * 60 * 1000)) <= 3) {
//            baseViewHolder.setBackgroundColor(R.id.constriant_receipt,mContext.getResources().getColor(R.color.color_red_f7cdd6));
//        } else {
//            baseViewHolder.setBackgroundColor(R.id.constriant_receipt,mContext.getResources().getColor(R.color.color_white_ffffff));
//        }

//        if (!"0".equals(receiptListEntity.getSftd())){
//            baseViewHolder.setEnabled(R.id.txt_chargeback,false);
//            textView2.setClickable(false);
//            textView2.setTextColor(mContext.getResources().getColor(R.color.color_gray_767676));
//            Drawable drawable1 = mContext.getResources().getDrawable(R.mipmap.ic_back_order_gray_48px;
//            drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
//            textView1.setCompoundDrawables(drawable1, null, null, null);
//        }else{
//            baseViewHolder.setEnabled(R.id.txt_delay,true);
//            textView1.setClickable(true);
//            textView1.setTextColor(mContext.getResources().getColor(R.color.color_blue));
//            Drawable drawable1 = mContext.getResources().getDrawable(R.mipmap.ic_back_order_blue_48px;
//            drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
//            textView1.setCompoundDrawables(drawable1, null, null, null);
//        }

    }

}
