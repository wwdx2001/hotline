package com.sh3h.hotline.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by dengzhimin on 2016/9/21.
 */
public class CreateSelfOrderHistoryRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {

    private List<DUHistoryTask> mList;
    private Context mContext;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private OnItemClickListener mListener;

    public CreateSelfOrderHistoryRecycleViewAdapter(List<DUHistoryTask> tasks, Context context) {
        mList = tasks;
        mContext = context;
    }

    public void setData(List<DUHistoryTask> datas) {
        mList = datas;
    }

    public void addData(List<DUHistoryTask> datas) {
        mList.addAll(datas);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            DUHistoryTask task = mList.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.itemView.setTag(task);
            if (task.getTASK_UPLOAD_FLAG() == Constant.HAS_UPLOADED) {
//                viewHolder.ivUploadState.setImageResource(R.drawable.ic_cloud_done_grep_24dp);
                viewHolder.ivUploadState.setImageResource(R.mipmap.ic_cloud_uploaded_48px);
                viewHolder.tvUploadState.setText(R.string.label_has_uploaded);
            } else {
//                viewHolder.ivUploadState.setImageResource(R.drawable.ic_cloud_upload_grey_24dp);
                viewHolder.ivUploadState.setImageResource(R.mipmap.ic_cloud_upload_48px);
                viewHolder.tvUploadState.setText(R.string.label_no_upload);
            }
            int photoCount = 0;
            int voiceCount = 0;
            int photoNoUploadCount = 0;
            int voiceNoUploadCount = 0;

            for (DUMedia duMedia : task.getDuMedias()) {
                switch (duMedia.getFileType()) {
                    case DUMedia.FILE_TYPE_PICTURE:
                        ++photoCount;
                        if (duMedia.getUploadFlag() == DUMedia.UPLOAD_FLAG_LOCAL) {
                            ++photoNoUploadCount;
                        }
                        break;
                    case DUMedia.FILE_TYPE_VOICE:
                        ++voiceCount;
                        if (duMedia.getUploadFlag() == DUMedia.UPLOAD_FLAG_LOCAL) {
                            ++voiceNoUploadCount;
                        }
                        break;
                }
            }

            if (photoNoUploadCount > 0) {
//                viewHolder.ivPhotoUploadState.setImageResource(R.drawable.ic_photo_library_gray_24dp);
                viewHolder.ivPhotoUploadState.setImageResource(R.mipmap.ic_picture_upload_48px);
            } else {
//                viewHolder.ivPhotoUploadState.setImageResource(R.drawable.ic_image_gray_24dp);
                viewHolder.ivPhotoUploadState.setImageResource(R.mipmap.ic_picture_uploaded_48px);
            }

            viewHolder.tvPhotoUploadCount.setText(String.valueOf(photoCount));
            if (voiceNoUploadCount > 0) {
//                viewHolder.ivVoiceUploadState.setImageResource(R.drawable.ic_library_music_gray_24dp);
                viewHolder.ivVoiceUploadState.setImageResource(R.mipmap.ic_music_upload_48px);
            } else {
//                viewHolder.ivVoiceUploadState.setImageResource(R.drawable.ic_music_video_gray_24dp);
                viewHolder.ivVoiceUploadState.setImageResource(R.mipmap.ic_music_uploaded_48px);
            }

            viewHolder.tvVoiceUploadCount.setText(String.valueOf(voiceCount));
            viewHolder.tvAddress.setText(task.getADDRESS());
            viewHolder.tvOccurTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(task.getISSUE_TIME())));
            viewHolder.tvName.setText(task.getHU_MING());
            viewHolder.tvPhone.setText(task.getTELEPHONE());
            viewHolder.tvIssue.setText(task.getISSUER());
            viewHolder.tvIssueType.setText(task.getISSUE_TYPE());
            viewHolder.tvIssueContent.setText(task.getISSUE_CONTENT());
            viewHolder.tvDealLevel.setText(task.getREPLY_CLASS());
        } else {
            ((LoadingViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_create_self_order_history_layout, parent, false);
            view.setOnClickListener(this);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_order_list_row_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position) == null ? TYPE_FOOTER : TYPE_ITEM;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClick(v, v.getTag());
        }
    }

    /**
     * 显示ItemView
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAddress;//地址
        public TextView tvOccurTime;//发生时间
        public TextView tvName;//户名
        public TextView tvPhone;//联系电话
        public TextView tvIssue;//反映人
        public TextView tvIssueType;//反映类型
        public TextView tvIssueContent;//反映内容
        public TextView tvDealLevel;//处理级别

        public ImageView ivUploadState;//上传状态
        public TextView tvUploadState;
        public ImageView ivPhotoUploadState;//图片上传状态
        public TextView tvPhotoUploadCount;
        public ImageView ivVoiceUploadState;//录音上传状态
        public TextView tvVoiceUploadCount;

        public ItemViewHolder(View view) {
            super(view);
            tvAddress = (TextView) view.findViewById(R.id.tv_dizhi);
            tvOccurTime = (TextView) view.findViewById(R.id.tv_fashengshijian);
            tvName = (TextView) view.findViewById(R.id.tv_huming);
            tvIssue = (TextView) view.findViewById(R.id.tv_fanyingren);
            tvPhone = (TextView) view.findViewById(R.id.tv_lianxidianhua);
            tvDealLevel = (TextView) view.findViewById(R.id.tv_chulijibie);
            tvIssueType = (TextView) view.findViewById(R.id.tv_fanyingleixing);
            tvIssueContent = (TextView) view.findViewById(R.id.tv_fanyingneirong);
            ivUploadState = (ImageView) view.findViewById(R.id.iv_shifoushangchuan);
            tvUploadState = (TextView) view.findViewById(R.id.tv_shangchuanzhuangtai);
            ivPhotoUploadState = (ImageView) view.findViewById(R.id.iv_tupianshangchuan);
            tvPhotoUploadCount = (TextView) view.findViewById(R.id.tv_tupianshangchuan_shuliang);
            ivVoiceUploadState = (ImageView) view.findViewById(R.id.iv_yuyingshangchuan);
            tvVoiceUploadCount = (TextView) view.findViewById(R.id.tv_yuyingshagnchuan_shuliang);
        }
    }

    /**
     * 显示加载更多
     */
    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public TextView textView;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
            textView = (TextView) view.findViewById(R.id.textview);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, Object o);
    }
}