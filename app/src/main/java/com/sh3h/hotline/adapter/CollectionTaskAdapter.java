package com.sh3h.hotline.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.CollectionTaskBean;
import com.sh3h.hotline.ui.collection.CollectionTaskListActivity;

import java.util.ArrayList;
import java.util.List;

public class CollectionTaskAdapter extends RecyclerView.Adapter<CollectionTaskAdapter.MyViewHolder> {
  private Context mContext;
  private List<CollectionTaskBean> dataSource;


  public CollectionTaskAdapter(Context mContext, RecyclerView mRecyclerView) {
    this.mContext = mContext;
    this.dataSource = new ArrayList<>();
  }

  public void setDataSource(List<CollectionTaskBean> dataSource) {
    this.dataSource = dataSource;
    notifyDataSetChanged();
  }

  /**
   * 创建并且返回 ViewHolder
   *
   * @param parent
   * @param viewType
   * @return
   */
  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_collection_task, parent, false));
  }

  /**
   * 通过 ViewHolder 来绑定数据
   *
   * @param holder
   * @param position
   */
  @Override
  public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
    holder.mCeBenHao.setText(dataSource.get(position).getCbh());
    holder.mOrDerSum.setText(String.valueOf( dataSource.get(position).getGongdanshu()));
    holder.mDyingPeriod.setText(String.valueOf(dataSource.get(position).getLinqi()));
    holder.mCompleted.setText(String.valueOf(dataSource.get(position).getYiwancheng()));
    holder.mNotCompleted.setText(String.valueOf(dataSource.get(position).getWeiwancheng()));
    holder.mItemCollectionTask.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d( "onClick: ", String.valueOf(position));
        Intent intent =new Intent(holder.mItemCollectionTask.getContext(), CollectionTaskListActivity.class);
        intent.putExtra("cbh",dataSource.get(position).getCbh());
        mContext.startActivity(intent);
      }
    });



  }

  /**
   * 返回数据数量
   *
   * @return
   */
  @Override
  public int getItemCount() {
    return dataSource.size();
  }


  class MyViewHolder extends RecyclerView.ViewHolder {

    private TextView mCeBenHao,mOrDerSum,mDyingPeriod,mCompleted,mNotCompleted;
    private LinearLayout mItemCollectionTask;


    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
      mItemCollectionTask= (LinearLayout) itemView.findViewById(R.id.item_collection_task);
      mCeBenHao= (TextView) itemView.findViewById(R.id.colection_task_item_tv_book_number);
      mOrDerSum= (TextView) itemView.findViewById(R.id.colection_task_item_tv_order_sum);
      mDyingPeriod= (TextView) itemView.findViewById(R.id.colection_task_item_tv_dying_period);
      mCompleted= (TextView) itemView.findViewById(R.id.colection_task_item_tv_completed);
      mNotCompleted= (TextView) itemView.findViewById(R.id.collection_task_item_tv_not_completed);
    }
  }


}
