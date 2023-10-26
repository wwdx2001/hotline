package com.sh3h.hotline.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.response.DUProcess;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.order.TaskState;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dengzhimin on 2017/2/23.
 */

public class OrderProcessRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private List<?> datas;
    private Context context;
    private OnItemClickListener listener;

    public OrderProcessRecyclerViewAdapter(Context context, List<?> datas) {
        this.context = context;
        this.datas = datas;
    }

    public void setData(List<?> datas){
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_process, parent, false);
        view.setOnClickListener(this);
        return new OrderProcessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OrderProcessViewHolder) {
            OrderProcessViewHolder viewHolder = (OrderProcessViewHolder) holder;
            Object object = datas.get(position);
            viewHolder.itemView.setTag(object);
            int state = 0;
            long replyTime = 0L;
            if(object instanceof DUHistoryTask){
                DUHistoryTask historyTask = (DUHistoryTask) object;
                state = historyTask.getTASK_STATE();
                replyTime = historyTask.getREPLY_TIME();
            }else if(object instanceof DUProcess){
                DUProcess duProcess = (DUProcess) object;
                state = duProcess.getTaskState();
                replyTime = duProcess.getTaskTime();
            }
            switch (state) {
                case TaskState.DISPATCH:
                    viewHolder.tvOrderState.setText(R.string.text_dispatch);
                    break;
                case TaskState.RECEIVED://接收
                    viewHolder.tvOrderState.setText(R.string.text_receive);
                    break;
                case TaskState.DELAY://延期
                    viewHolder.tvOrderState.setText(R.string.label_delay);
                    break;
                case TaskState.BACK://退单
                    viewHolder.tvOrderState.setText(R.string.label_charge_back);
                    break;
                case TaskState.HANDLE://处理
                    viewHolder.tvOrderState.setText(R.string.label_handle);
                    break;
            }
            viewHolder.tvDealTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(replyTime));
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            Object object = v.getTag();
            if(object instanceof DUHistoryTask){
                if(((DUHistoryTask) object).getTASK_STATE() == TaskState.RECEIVED || ((DUHistoryTask) object).getTASK_STATE() == TaskState.DISPATCH){
                    return;
                }
            }else if(object instanceof DUProcess){
                if(((DUProcess) object).getTaskState() == TaskState.RECEIVED || ((DUProcess) object).getTaskState() == TaskState.DISPATCH){
                    return;
                }
            }
            listener.onItemClick(v, v.getTag());
        }
    }

    public class OrderProcessViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_order_state)
        TextView tvOrderState;

        @BindView(R.id.tv_deal_time)
        TextView tvDealTime;

        public OrderProcessViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, Object o);
    }
}