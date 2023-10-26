package com.sh3h.hotline.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.response.DURecentBill;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.viewholder.EmptyViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dengzhimin on 2016/9/22.
 */
public class RecentBillInformationRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final int VIEW_TYPE_EMPTY = -1;

    private List<DURecentBill> mDatas;
    private Context mContext;
    private OnItemClickListener mListener;

    public RecentBillInformationRecycleViewAdapter(List<DURecentBill> datas, Context mContext) {
        this.mDatas = datas;
        this.mContext = mContext;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == VIEW_TYPE_EMPTY) {
            view = inflater.inflate(R.layout.item_empty, parent, false);
            return new EmptyViewHolder(view);
        }
        view = LayoutInflater.from(mContext).inflate(R.layout.item_recent_bill_information, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EmptyViewHolder) {
            ((EmptyViewHolder) holder).tvContent.setText(R.string.text_empty);
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        DURecentBill recentBill = mDatas.get(position);
        viewHolder.itemView.setTag(recentBill);
        viewHolder.tvYear.setText(String.valueOf(recentBill.getReadYear()));
        viewHolder.tvMonth.setText(String.valueOf(recentBill.getReadMonth()));
        viewHolder.tvReadCardState.setText(recentBill.getReadCardState());
        viewHolder.tvChaoMaRQ.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(recentBill.getReadTime())));
        viewHolder.tvChaoCi.setText(String.valueOf(recentBill.getReadTimes()));
        viewHolder.tvBenCiChaoMa.setText(String.valueOf(recentBill.getReading()));
        viewHolder.tvYongShuiLiang.setText(String.valueOf(recentBill.getReadWater()));
        viewHolder.tvYongShuiFei.setText(String.valueOf(recentBill.getPi1Money()));
        viewHolder.tvPaiShuiFei.setText(String.valueOf(recentBill.getPi2Money()));
        viewHolder.tvKaiZhangJinE.setText(String.valueOf(recentBill.getAccCheckMoney()));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClick(v, v.getTag());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_year)
        TextView tvYear;//年

        @BindView(R.id.tv_month)
        TextView tvMonth;//月

        @BindView(R.id.tv_read_card_state)
        TextView tvReadCardState;//抄表状态

        @BindView(R.id.tv_chaomariqi)
        TextView tvChaoMaRQ;//抄码日期

        @BindView(R.id.tv_chaoci)
        TextView tvChaoCi;//抄次

        @BindView(R.id.tv_bencichaoma)
        TextView tvBenCiChaoMa;//本次抄码

        @BindView(R.id.tv_yongshuiliang)
        TextView tvYongShuiLiang;//用水量

        @BindView(R.id.tv_yongshuifei)
        TextView tvYongShuiFei;//用水费

        @BindView(R.id.tv_paishuifei)
        TextView tvPaiShuiFei;//排水费

        @BindView(R.id.tv_kaizhangjine)
        TextView tvKaiZhangJinE;//开账金额

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, Object o);
    }

}
