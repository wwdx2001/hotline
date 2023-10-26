package com.sh3h.hotline.ui.multimedia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.dataprovider.data.entity.response.DUFileRemote;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.mobileutil.util.TextUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * 加载照片适配器
 * Created by zhangjing on 2016/9/26.
 */

public class PictureAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<?> duMediaList;

    public PictureAdapter(Context context, List<?> duMediaList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.duMediaList = duMediaList;
    }

    public void setData(List<?> duMediaList){
        this.duMediaList = duMediaList;
        notifyDataSetChanged();
    }

    public List<?> getDuMediaList() {
        return duMediaList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_multimedia_picture, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_image);
            viewHolder.uploadedFlagImageView = (ImageView) convertView.findViewById(R.id.item_uploaded_flag_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Get the image URL for the current position.
        Object object = getItem(position);
        String path = null;
        if (object instanceof DUMedia) {
            DUMedia duMedia = (DUMedia) object;
            path = duMedia.getFilePath();
            if (duMedia.getUploadFlag()!= Constant.HAS_UPLOADED){
                viewHolder.uploadedFlagImageView.setImageResource(R.drawable.ic_cloud_upload_grey_24dp);
            }else{
                viewHolder.uploadedFlagImageView.setImageResource(R.drawable.ic_cloud_done_grep_24dp);
            }
            // Trigger the download of the URL asynchronously into the image view.
            Picasso.with(context) //
                    .load(new File(TextUtil.getString(path))) //
                    .placeholder(R.mipmap.bg_place_holder) //
                    .error(R.mipmap.bg_error) //
                    .fit()
                    .tag(context) //
                    .into(viewHolder.imageView);
        }else if(object instanceof DUFileRemote){
            DUFileRemote fileRemote = (DUFileRemote) object;
            if (fileRemote.getFileType().equals(DUMedia.VIDEO_SUFFIX)) {
                path = "file:///android_asset/image/bg_video.png";
            } else {
                path = fileRemote.getFileUrl();
            }
            // Trigger the download of the URL asynchronously into the image view.
            Picasso.with(context) //
                    .load(path) //
                    .placeholder(R.mipmap.bg_place_holder) //
                    .error(R.mipmap.bg_error) //
                    .fit()
                    .tag(context) //
                    .into(viewHolder.imageView);
        }



        return convertView;
    }

    @Override
    public int getCount() {
        return duMediaList.size();
    }

    @Override
    public Object getItem(int position) {
        return duMediaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        ImageView imageView;
        ImageView uploadedFlagImageView;
    }

}
