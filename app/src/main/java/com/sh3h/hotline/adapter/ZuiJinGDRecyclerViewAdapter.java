package com.sh3h.hotline.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.viewholder.ZuiJinGDViewHolder;
import com.sh3h.hotline.entity.ZuiJinHBGDEntity;


import java.util.List;

public class ZuiJinGDRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ZuiJinHBGDEntity> mList;
    private Context mContext;

    public ZuiJinGDRecyclerViewAdapter(List<ZuiJinHBGDEntity> entityList, FragmentActivity activity) {
        mList = entityList;
        mContext = activity;
    }

    public void setData(List<ZuiJinHBGDEntity> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.items_zui_jin_gd, parent, false);
        return new ZuiJinGDViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ZuiJinGDViewHolder viewHolder = (ZuiJinGDViewHolder) holder;
        viewHolder.spId.setText(mList.get(position).getSpId());
        viewHolder.faId.setText(mList.get(position).getFaId());
        viewHolder.faTypeDesc.setText(mList.get(position).getFaTypeDesc());
        viewHolder.faCompDttm.setText(mList.get(position).getFaCompDttm());
        viewHolder.uninstallRead.setText(mList.get(position).getUninstallRead() + "");
        viewHolder.uninstallBadgeNum.setText(mList.get(position).getUninstallBadgeNum());
        viewHolder.newBadgeNum.setText(mList.get(position).getNewBadgeNum());
        viewHolder.installRead.setText(mList.get(position).getInstallRead() + "");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
