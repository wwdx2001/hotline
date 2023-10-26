package com.sh3h.hotline.ui.multimedia;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.dataprovider.data.entity.response.DUFileRemote;
import com.sh3h.hotline.R;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.sh3h.dataprovider.data.entity.DUMedia.UPLOAD_FLAG_LOCAL;

/**
 * 录音适配器
 * Created by zhangjing on 2016/9/27.
 */

public class RecordAdapter extends BaseAdapter {
    private static final String TAG = "RecordAdapter";
    private Context context;
    private List<?> duMediaList;
    private float scale;

    public RecordAdapter(Context context, List<?> duMediaList) {
        this.duMediaList = duMediaList;
        this.context = context;
        this.scale = context.getResources().getDisplayMetrics().density;
    }

    public void setData(List<?> duMediaList){
        this.duMediaList = duMediaList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return duMediaList == null ? 0 : duMediaList.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return duMediaList.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.item_multimedia_record, null);
            viewHolder.iv_state = (ImageView) view.findViewById(R.id.item_multimedia_record_iv_upload_state);
            viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            //viewHolder.tv_wid = (TextView) view.findViewById(R.id.tv_record);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Object object = getItem(i);
        if (object instanceof DUMedia) {
            DUMedia duMedia = (DUMedia) object;

            // set time
            long time = 0;
            String data = duMedia.getExtend();
            if (!TextUtil.isNullOrEmpty(data)) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    DUMedia.Extend extend = DUMedia.Extend.fromJson(jsonObject);
                    time = extend.getTime();
                    time = (time + 500) / 1000;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            viewHolder.tv_time.setText(String.format(Locale.CHINESE, "%ds", time));

            // set upload image
            Drawable doneDrawable =
                    context.getResources().getDrawable(R.drawable.ic_cloud_done_grep_24dp);
            Drawable uploadDrawable =
                    context.getResources().getDrawable(R.drawable.ic_cloud_upload_grey_24dp);
            if (duMedia.getUploadFlag() == UPLOAD_FLAG_LOCAL) {
                viewHolder.iv_state.setImageDrawable(uploadDrawable);
            } else {
                viewHolder.iv_state.setImageDrawable(doneDrawable);
            }
        } else if(object instanceof DUFileRemote){
            DUFileRemote duFileRemote = (DUFileRemote) object;
            viewHolder.tv_time.setText("-s");
            viewHolder.iv_state.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cloud_done_grep_24dp));
        }

        return view;
    }

    class ViewHolder {
        TextView tv_wid;
        TextView tv_time;
        ImageView iv_state;
    }
}
