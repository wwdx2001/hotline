package com.sh3h.hotline.adapter;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.CollectionInformationBean;
import com.sh3h.hotline.entity.CollectionTaskBean;

import java.util.ArrayList;
import java.util.List;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

public class CollectionInformationAdapter extends RecyclerView.Adapter<CollectionInformationAdapter.MyViewHolder> {
  private Context mContext;
  private List<CollectionInformationBean> dataSource;


  public CollectionInformationAdapter(Context mContext, RecyclerView mRecyclerView) {
    this.mContext = mContext;
    this.dataSource = new ArrayList<>();
  }

  public void setDataSource(List<CollectionInformationBean> dataSource) {
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
    return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_keyandvalue, parent, false));
  }

  /**
   * 通过 ViewHolder 来绑定数据
   *
   * @param holder
   * @param position
   */
  @SuppressLint("ResourceAsColor")
  @Override
  public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
      holder.mKey.setText(dataSource.get(position).getKey());
      holder.mValue.setText(dataSource.get(position).getValue());
      if (position == 6){
        holder.mValue.setTextColor(R.color.color_blue);
      }
      holder.mValue.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Log.d( "onClick: ", String.valueOf(position));
          if (position == 6){
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+dataSource.get(position).getValue()));
            startActivity(intent);
          }
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

    private TextView mKey,mValue;
//    private LinearLayout mItemCollectionTask;


    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
      mKey= (TextView) itemView.findViewById(R.id.tv_key);
      mValue= (TextView) itemView.findViewById(R.id.tv_value);
    }
  }


}
