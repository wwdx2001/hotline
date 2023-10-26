package com.sh3h.hotline.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.CollectionPhotoBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CollectionPhotoAdapter extends RecyclerView.Adapter<CollectionPhotoAdapter.SelectedPicViewHolder>{
  private int maxImgCount;
  private Context mContext;
  private List<CollectionPhotoBean> mData;
  private LayoutInflater mInflater;
  private OnRecyclerViewItemClickListener listener;
  private OnRecyclerViewItemLongClickListener longListener;
  private boolean isAdded;   //是否额外添加了最后一个图片
  private int type;

  public interface OnRecyclerViewItemClickListener {
    void onItemClick(View view, int position, int type);
  }

  public interface OnRecyclerViewItemLongClickListener {
    void onItemLongClick(View view, int position, int type);
  }

  public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
    this.listener = listener;
  }

  public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener listener) {
    this.longListener = listener;
  }

  public void setImages(List<CollectionPhotoBean> data) {
    if (data == null) {
      return;
    }
    List<CollectionPhotoBean> newData = new ArrayList<>();
    for (int i = 0; i < data.size(); i++) {
      File file = new File(data.get(i).path);
      if (file.exists()) {
        newData.add(data.get(i));
      }
    }
//        mData = new ArrayList<>(data);
    mData = new ArrayList<>(newData);
    if (getItemCount() < maxImgCount) {
//            mData.add(new ImageItem());
      mData.add(0, new CollectionPhotoBean());
      isAdded = true;
    } else {
      isAdded = false;
    }
    notifyDataSetChanged();
  }


  public List<CollectionPhotoBean> getImages() {
    //由于图片未选满时，最后一张显示添加图片，因此这个方法返回真正的已选图片
//        if (isAdded) return new ArrayList<>(mData.subList(0, mData.size() - 1));
    if (isAdded) {
      return new ArrayList<>(mData.subList(1, mData.size()));
    } else {
      return mData;
    }
  }

  public CollectionPhotoAdapter(Context mContext, List<CollectionPhotoBean> data, int maxImgCount) {
    this.mContext = mContext;
    this.maxImgCount = maxImgCount;
    this.mInflater = LayoutInflater.from(mContext);
    setImages(data);
  }

  public CollectionPhotoAdapter(Context mContext, List<CollectionPhotoBean> data, int maxImgCount, int type) {
    this.mContext = mContext;
    this.maxImgCount = maxImgCount;
    this.mInflater = LayoutInflater.from(mContext);
    this.type = type;
    setImages(data);
  }

  @NonNull
  @Override
  public SelectedPicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new SelectedPicViewHolder(mInflater.inflate(R.layout.photo_recycle_item, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull SelectedPicViewHolder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public class SelectedPicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private ImageView iv_img;
    private int clickPosition;

    public SelectedPicViewHolder(View itemView) {
      super(itemView);
      iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
      iv_img.getLayoutParams().width = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(80)) / 3;
      iv_img.getLayoutParams().height = iv_img.getLayoutParams().width;
    }

    public void bind(int position) {
//      设置条目的点击事件
      itemView.setOnClickListener(this);
      itemView.setOnLongClickListener(this);
      //根据条目位置设置图片
      CollectionPhotoBean item = mData.get(position);
//            if (isAdded && position == getItemCount() - 1) {
      if (isAdded && position == 0) {
        iv_img.setImageResource(R.drawable.selector_camera);
//        clickPosition = Const.IMAGE_ITEM_ADD;
        clickPosition=1;
      } else if (isAdded && position != 0) {
//                ImagePicker.getInstance().getImageLoader().displayImage((Activity) mContext, item.path, iv_img, 0, 0);
//        GlideApp.with(mContext).load(item.path).centerCrop().into(iv_img);
        clickPosition = position - 1;
      } else {
//        GlideApp.with(mContext).load(item.path).centerCrop().into(iv_img);
        clickPosition = position;
      }
    }

    @Override
    public void onClick(View v) {
      if (listener != null) {
        listener.onItemClick(v, clickPosition, type);
      }
    }

    @Override
    public boolean onLongClick(View view) {
      if (longListener != null) {
        longListener.onItemLongClick(view, clickPosition, type);
      }
      return true;
    }
  }
}
