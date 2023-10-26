package com.sh3h.hotline.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.order.TaskState;

import java.util.List;
import java.util.Map;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/4/16 10:33
 */
public class MyOrderListAdapter extends BaseQuickAdapter<DUMyTask, BaseViewHolder> {

    private int type;
    private Map<String, String> map;

    public MyOrderListAdapter(Map<String, String> map, int type, int layoutResId, @Nullable List<DUMyTask> data) {
        super(layoutResId, data);
        this.type = type;
        this.map = map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, DUMyTask duMyTask) {
        long clsxLong = TimeUtils.string2Millis(duMyTask.getClsx());
        if (clsxLong < System.currentTimeMillis()) {
            baseViewHolder.setBackgroundColor(R.id.view_bg, mContext.getResources().getColor(R.color.color_red_2CFF0000));
            baseViewHolder.setGone(R.id.tv_is_chaoqi, true);
        } else {
            baseViewHolder.setBackgroundColor(R.id.view_bg, mContext.getResources().getColor(R.color.color_transt));
            baseViewHolder.setGone(R.id.tv_is_chaoqi, false);
        }
        baseViewHolder.setText(R.id.tv_acctId, duMyTask.getAcctId());
        baseViewHolder.setText(R.id.my_order_list_item_tv_order_number, duMyTask.getFaId());
        baseViewHolder.setText(R.id.my_order_list_item_tv_phone, duMyTask.getContactValue());
        baseViewHolder.setText(R.id.my_order_list_item_tv_address, duMyTask.getFsdz());
        baseViewHolder.setText(R.id.my_order_list_item_tv_reflect_person, duMyTask.getEntityName());
        baseViewHolder.setText(R.id.tv_shenpi_beizhu, StringUtils.isEmpty(duMyTask.getShComment()) ? "" : duMyTask.getShComment());
        baseViewHolder.setGone(R.id.group_shenpi_beizhu, !StringUtils.isEmpty(duMyTask.getShComment()));
        baseViewHolder.setText(R.id.tv_yysj, duMyTask.getYysj());
        if (map != null) {
            baseViewHolder.setText(R.id.my_order_list_item_tv_reflect_origin, map.get(duMyTask.getFyly()));
        } else {
            baseViewHolder.setText(R.id.my_order_list_item_tv_reflect_origin, duMyTask.getFyly());
        }
        baseViewHolder.setText(R.id.my_order_list_item_tv_reflect_type, duMyTask.getFaTypeCd());
        baseViewHolder.setText(R.id.my_order_list_item_tv_reflect_content, duMyTask.getFynr());
        baseViewHolder.setText(R.id.my_order_list_item_handle_tv_time_limit, duMyTask.getClsx());

        switch (type) {
            case Constant.ADAPTER_TYPE_NORMAL:
                baseViewHolder.setGone(R.id.group, true);
                baseViewHolder.setGone(R.id.history_orders_item_tv_data_isupload, false);
                TextView textView1 = baseViewHolder.getView(R.id.my_order_list_item_tv_receive);
                TextView textView2 = baseViewHolder.getView(R.id.my_order_list_item_tv_delay);
                TextView textView3 = baseViewHolder.getView(R.id.my_order_list_item_tv_back);
                TextView textView4 = baseViewHolder.getView(R.id.my_order_list_item_tv_handle);
                baseViewHolder.addOnClickListener(R.id.my_order_list_item_tv_receive, R.id.my_order_list_item_tv_delay,
                        R.id.my_order_list_item_tv_back, R.id.my_order_list_item_tv_handle, R.id.tv0, R.id.tv_acctId,
                        R.id.iv_icon, R.id.ib_phone, R.id.view_navigation);

                switch (duMyTask.getCmSta()) {
                    case "未完成":
                        baseViewHolder.setText(R.id.my_order_list_state, mContext.getString(R.string.state_already_receive));
                        baseViewHolder.setTextColor(R.id.my_order_list_state, mContext.getResources().getColor(R.color.text_item));
                        initTextViewBtn(textView1, textView2, textView3, textView4,
                                false, false, true, true,
                                R.color.color_gray_767676, R.color.color_gray_767676, R.color.color_blue, R.color.color_blue,
                                R.mipmap.ic_has_received_48px, R.mipmap.ic_delay_order_gray_48px,
                                R.mipmap.ic_back_order_blue_48px, R.mipmap.ic_handle_order_blue_48px);
                        break;
                    case "A":
                        // A：已接单,已接收
                        // taskStatus = Constant.TASK_STATE_RECEIVED;
                        baseViewHolder.setText(R.id.my_order_list_state, mContext.getString(R.string.state_already_receive));
                        baseViewHolder.setTextColor(R.id.my_order_list_state, mContext.getResources().getColor(R.color.text_item));
                        initTextViewBtn(textView1, textView2, textView3, textView4,
                                false, true, true, true,
                                R.color.color_gray_767676, R.color.color_blue, R.color.color_blue, R.color.color_blue,
                                R.mipmap.ic_has_received_48px, R.mipmap.ic_delay_order_blue_48px,
                                R.mipmap.ic_back_order_blue_48px, R.mipmap.ic_handle_order_blue_48px);
                        break;
                    case "C":
                        // C：已完成
                        break;
                    case "D":
                        // D：已调度,派工,待接收
                        // taskStatus = Constant.TASK_STATE_SENDWORK;
                        baseViewHolder.setText(R.id.my_order_list_state, mContext.getResources().getString(R.string.state_wait_receive));
                        baseViewHolder.setTextColor(R.id.my_order_list_state, mContext.getResources().getColor(R.color.text_item));
                        initTextViewBtn(textView1, textView2, textView3, textView4,
                                true, false, false, false,
                                R.color.color_blue, R.color.color_gray_767676, R.color.color_gray_767676, R.color.color_gray_767676,
                                R.mipmap.ic_no_received_48px, R.mipmap.ic_delay_order_gray_48px,
                                R.mipmap.ic_back_order_gray_48px, R.mipmap.ic_handle_order_gray_48px);
                        break;
                    case "T":
                        // T：申请退单,退单中
                        // taskStatus = Constant.TASK_STATE_BACK;
                        baseViewHolder.setText(R.id.my_order_list_state, mContext.getString(R.string.state_refund));
                        baseViewHolder.setTextColor(R.id.my_order_list_state, mContext.getResources().getColor(R.color.text_item));
                        initTextViewBtn(textView1, textView2, textView3, textView4,
                                false, false, false, false,
                                R.color.color_gray_767676, R.color.color_gray_767676, R.color.color_gray_767676, R.color.color_gray_767676,
                                R.mipmap.ic_has_received_48px, R.mipmap.ic_delay_order_gray_48px,
                                R.mipmap.ic_back_order_gray_48px, R.mipmap.ic_handle_order_gray_48px);
//                        baseViewHolder.setText(R.id.my_order_list_state, mContext.getString(R.string.state_already_receive));
//                        initTextViewBtn(textView1, textView2, textView3, textView4,
//                                false, true, true, true,
//                                R.color.color_gray_767676, R.color.color_blue, R.color.color_blue, R.color.color_blue,
//                                R.mipmap.ic_has_received_48px, R.mipmap.ic_delay_order_blue_48px,
//                                R.mipmap.ic_back_order_blue_48px, R.mipmap.ic_handle_order_blue_48px);
                        break;
                    case "E":
                        // E：延期失败
                        // taskStatus = Constant.TASK_STATE_DELAY_FAILED;
                        // break;
                    case "U":
                        // U：销单失败,销单不合格
                        // taskStatus = Constant.TASK_STATE_CANCEL_FAILED;
                        baseViewHolder.setText(R.id.my_order_list_state, mContext.getString(R.string.state_xiaodan_fail));
                        baseViewHolder.setTextColor(R.id.my_order_list_state, mContext.getResources().getColor(R.color.color_red_ff0000));
                        initTextViewBtn(textView1, textView2, textView3, textView4,
                                false, true, true, true,
                                R.color.color_gray_767676, R.color.color_blue, R.color.color_blue, R.color.color_blue,
                                R.mipmap.ic_has_received_48px, R.mipmap.ic_delay_order_blue_48px,
                                R.mipmap.ic_back_order_blue_48px, R.mipmap.ic_handle_order_blue_48px);
//                        initTextViewBtn(textView1, textView2, textView3, textView4,
//                                true, false, false, false,
//                                R.color.color_blue, R.color.color_gray_767676, R.color.color_gray_767676, R.color.color_gray_767676,
//                                R.mipmap.ic_no_received_48px, R.mipmap.ic_delay_order_gray_48px,
//                                R.mipmap.ic_back_order_gray_48px, R.mipmap.ic_handle_order_gray_48px);
                        break;
                    case "Y":
                        // Y：申请延期,延期中
                        // taskStatus = Constant.TASK_STATE_DELAY;
                        baseViewHolder.setText(R.id.my_order_list_state, mContext.getString(R.string.state_delay));
                        baseViewHolder.setTextColor(R.id.my_order_list_state, mContext.getResources().getColor(R.color.text_item));
                        initTextViewBtn(textView1, textView2, textView3, textView4,
                                false, false, false, false,
                                R.color.color_gray_767676, R.color.color_gray_767676, R.color.color_gray_767676, R.color.color_gray_767676,
                                R.mipmap.ic_has_received_48px, R.mipmap.ic_delay_order_gray_48px,
                                R.mipmap.ic_back_order_gray_48px, R.mipmap.ic_handle_order_gray_48px);

//                        baseViewHolder.setText(R.id.my_order_list_state, mContext.getString(R.string.state_already_receive));
//                        initTextViewBtn(textView1, textView2, textView3, textView4,
//                                false, true, true, true,
//                                R.color.color_gray_767676, R.color.color_blue, R.color.color_blue, R.color.color_blue,
//                                R.mipmap.ic_has_received_48px, R.mipmap.ic_delay_order_blue_48px,
//                                R.mipmap.ic_back_order_blue_48px, R.mipmap.ic_handle_order_blue_48px);
                        break;
                    case "F":
                        // F：已销单
                        // taskStatus = Constant.TASK_STATE_CANCEL;
                        baseViewHolder.setText(R.id.my_order_list_state, mContext.getString(R.string.state_xiaodan_finish));
                        baseViewHolder.setTextColor(R.id.my_order_list_state, mContext.getResources().getColor(R.color.text_item));
                        initTextViewBtn(textView1, textView2, textView3, textView4,
                                false, false, false, false,
                                R.color.color_gray_767676, R.color.color_gray_767676, R.color.color_gray_767676, R.color.color_gray_767676,
                                R.mipmap.ic_has_received_48px, R.mipmap.ic_delay_order_gray_48px,
                                R.mipmap.ic_back_order_gray_48px, R.mipmap.ic_handle_order_gray_48px);
                        break;
                    case "P":
                        // P：按用户待定
                        baseViewHolder.setText(R.id.my_order_list_state, mContext.getString(R.string.state_anyonghu_daiding));
                        baseViewHolder.setTextColor(R.id.my_order_list_state, mContext.getResources().getColor(R.color.text_item));
                        initTextViewBtn(textView1, textView2, textView3, textView4,
                                false, false, false, false,
                                R.color.color_gray_767676, R.color.color_gray_767676, R.color.color_gray_767676, R.color.color_gray_767676,
                                R.mipmap.ic_has_received_48px, R.mipmap.ic_delay_order_gray_48px,
                                R.mipmap.ic_back_order_gray_48px, R.mipmap.ic_handle_order_gray_48px);
                        break;
                    case "W":
                        // W：作业中
                        baseViewHolder.setText(R.id.my_order_list_state, mContext.getString(R.string.state_zuoye_zhong));
                        baseViewHolder.setTextColor(R.id.my_order_list_state, mContext.getResources().getColor(R.color.text_item));
                        initTextViewBtn(textView1, textView2, textView3, textView4,
                                false, false, false, false,
                                R.color.color_gray_767676, R.color.color_gray_767676, R.color.color_gray_767676, R.color.color_gray_767676,
                                R.mipmap.ic_has_received_48px, R.mipmap.ic_delay_order_gray_48px,
                                R.mipmap.ic_back_order_gray_48px, R.mipmap.ic_handle_order_gray_48px);
                        break;
                    case "X":
                        // X：已取消
                        // taskStatus = Constant.TASK_STATE_CANCEL_SUCCESS;
                        baseViewHolder.setText(R.id.my_order_list_state, mContext.getString(R.string.state_already_cancel));
                        baseViewHolder.setTextColor(R.id.my_order_list_state, mContext.getResources().getColor(R.color.text_item));
                        initTextViewBtn(textView1, textView2, textView3, textView4,
                                false, false, false, false,
                                R.color.color_gray_767676, R.color.color_gray_767676, R.color.color_gray_767676, R.color.color_gray_767676,
                                R.mipmap.ic_has_received_48px, R.mipmap.ic_delay_order_gray_48px,
                                R.mipmap.ic_back_order_gray_48px, R.mipmap.ic_handle_order_gray_48px);
                        break;
                    default:
                        // 所有状态
                        // taskStatus = Constant.TASK_STATE_ALL;
                        baseViewHolder.setText(R.id.my_order_list_state, mContext.getString(R.string.state_other));
                        baseViewHolder.setTextColor(R.id.my_order_list_state, mContext.getResources().getColor(R.color.color_red_ff0000));
                        initTextViewBtn(textView1, textView2, textView3, textView4,
                                false, false, false, false,
                                R.color.color_gray_767676, R.color.color_gray_767676, R.color.color_gray_767676, R.color.color_gray_767676,
                                R.mipmap.ic_has_received_48px, R.mipmap.ic_delay_order_gray_48px,
                                R.mipmap.ic_back_order_gray_48px, R.mipmap.ic_handle_order_gray_48px);
                        break;
                }
                break;
            case Constant.ADAPTER_TYPE_HISTORY:
                baseViewHolder.addOnClickListener(R.id.my_order_list_item_tv_receive, R.id.my_order_list_item_tv_delay,
                        R.id.my_order_list_item_tv_back, R.id.my_order_list_item_tv_handle, R.id.tv0, R.id.tv_acctId,
                        R.id.iv_icon, R.id.ib_phone, R.id.view_navigation);

                TextView textView0 = baseViewHolder.getView(R.id.history_orders_item_tv_data_isupload);
                baseViewHolder.setGone(R.id.group, false);
                baseViewHolder.setGone(R.id.history_orders_item_tv_data_isupload, true);
                if (duMyTask.getIsUploadSuccess() == Constant.HAS_UPLOADED) {
                    Drawable drawable00 = mContext.getResources().getDrawable(R.mipmap.ic_cloud_uploaded_48px);
                    drawable00.setBounds(0, 0, drawable00.getMinimumWidth(), drawable00.getMinimumHeight());
                    textView0.setCompoundDrawables(drawable00, null, null, null);
                    textView0.setText(R.string.label_has_uploaded);
                } else {
                    Drawable drawable01 = mContext.getResources().getDrawable(R.mipmap.ic_cloud_upload_48px);
                    drawable01.setBounds(0, 0, drawable01.getMinimumWidth(), drawable01.getMinimumHeight());
                    textView0.setCompoundDrawables(drawable01, null, null, null);
                    textView0.setText(R.string.label_no_upload);
                }

                switch (duMyTask.getState()) {
                    case TaskState.RECEIVED://接收
                        baseViewHolder.setText(R.id.my_order_list_state, mContext.getString(R.string.text_receive));
                        break;
                    case TaskState.DELAY://延期
                        baseViewHolder.setText(R.id.my_order_list_state, mContext.getString(R.string.label_delay));
                        break;
                    case TaskState.BACK://退单
                        baseViewHolder.setText(R.id.my_order_list_state, mContext.getString(R.string.label_charge_back));
                        break;
                    case TaskState.HANDLE://处理
                        baseViewHolder.setText(R.id.my_order_list_state, mContext.getString(R.string.label_handle));
                        break;
                    case TaskState.FINISH://已完成
                        baseViewHolder.setText(R.id.my_order_list_state, mContext.getString(R.string.label_finish));
                        break;
                    default:
                        baseViewHolder.setText(R.id.my_order_list_state, mContext.getString(R.string.state_other));
                        break;
                }
                break;
            default:
                break;
        }
    }

    private void initTextViewBtn(TextView textView1, TextView textView2, TextView textView3, TextView textView4,
                                 boolean clickable1, boolean clickable2, boolean clickable3, boolean clickable4,
                                 int color1, int color2, int color3, int color4,
                                 int image1, int image2, int image3, int image4) {
        textView1.setClickable(clickable1);
        textView2.setClickable(clickable2);
        textView3.setClickable(clickable3);
        textView4.setClickable(clickable4);
        textView1.setTextColor(mContext.getResources().getColor(color1));
        textView2.setTextColor(mContext.getResources().getColor(color2));
        textView3.setTextColor(mContext.getResources().getColor(color3));
        textView4.setTextColor(mContext.getResources().getColor(color4));
        Drawable drawable1 = mContext.getResources().getDrawable(image1);
        Drawable drawable2 = mContext.getResources().getDrawable(image2);
        Drawable drawable3 = mContext.getResources().getDrawable(image3);
        Drawable drawable4 = mContext.getResources().getDrawable(image4);
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
        drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
        drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
        drawable4.setBounds(0, 0, drawable4.getMinimumWidth(), drawable4.getMinimumHeight());
        textView1.setCompoundDrawables(drawable1, null, null, null);
        textView2.setCompoundDrawables(drawable2, null, null, null);
        textView3.setCompoundDrawables(drawable3, null, null, null);
        textView4.setCompoundDrawables(drawable4, null, null, null);

    }
}
