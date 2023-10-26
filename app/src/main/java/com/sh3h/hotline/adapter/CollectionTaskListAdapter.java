package com.sh3h.hotline.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.hjq.toast.ToastParams;
import com.hjq.toast.Toaster;
import com.hjq.toast.style.CustomToastStyle;
import com.maning.mndialoglibrary.MProgressDialog;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.ArrearageInfoEntity;
import com.sh3h.hotline.entity.ArrearageInfoListEntity;
import com.sh3h.hotline.entity.CollectionTaskListBean;
import com.sh3h.hotline.ui.bill.detail.UserDetailInformationActivity;
import com.sh3h.hotline.ui.collection.CollectionTaskOrderBackfillingActivity;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class CollectionTaskListAdapter extends RecyclerView.Adapter<CollectionTaskListAdapter.MyViewHolder> {
    private Context mContext;
    private List<CollectionTaskListBean> dataSource;
    private Disposable mDisposable1;

    public CollectionTaskListAdapter(Context mContext, RecyclerView mRecyclerView) {
        this.mContext = mContext;
        this.dataSource = new ArrayList<>();
    }

    public void setDataSource(List<CollectionTaskListBean> dataSource) {
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
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_collection_task_order, parent, false));
    }

    private static final String ACCOUNT = "account";
    //向子程序发送用户id
    private static final String USER_ID = "userId";
    //向子程序发送用户名
    private static final String USER_NAME = "userName";
    private static final String EXTENDED_INFO = "extendedInfo";

    /**
     * 通过 ViewHolder 来绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.mOrderNum.setText(dataSource.get(position).getXh() + "/");
        holder.mAccountNum.setText(String.valueOf(dataSource.get(position).getZhbh()));
        holder.mCustomerName.setText(String.valueOf(dataSource.get(position).getKhmc()));
        holder.mWaterCode.setText(String.valueOf(dataSource.get(position).getKhlx()));
        holder.mAmountOwed.setText(String.valueOf(dataSource.get(position).getQfzje()));
        holder.mWaterAdd.setText(String.valueOf(dataSource.get(position).getFwdz()));
        holder.mOrderType.setText(String.valueOf(dataSource.get(position).getCjly()));
        holder.mOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("onClick: ", String.valueOf(position));
                Intent intent = new Intent(mContext, CollectionTaskOrderBackfillingActivity.class);
//                intent.putExtra(ACCOUNT, "888888");
//                intent.putExtra(USER_ID, 3810);
//                intent.putExtra(USER_NAME, "test");
//                intent.putExtra(EXTENDED_INFO, "");
//                intent.putExtra("origin", 99);
//                intent.putExtra("taskId", dataSource.get(position).getAlbh());
                intent.putExtra("collection", dataSource.get(position));
                intent.putExtra("origin", 0);
                mContext.startActivity(intent);
            }
        });

        holder.mAccountNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationUserInfo(dataSource.get(position));
            }
        });

        holder.mTvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(dataSource.get(position));
            }
        });

    }

    private void getData(CollectionTaskListBean taskBean) {
        EasyHttp
                .get(URL.BASE_URL_CB + URL.KeHuQFQuery)
                .params("acctId", taskBean.getZhbh())
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<String>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        MProgressDialog.showProgress(ActivityUtils.getTopActivity(), "更新欠费金额中...");
                    }

                    @Override
                    public void onError(ApiException e) {
                        MProgressDialog.dismissProgress();
                        onCompleted();

                        if (e.getCode() == 1010) {
//                            ToastUtils.showShort("没有数据");
                            ToastParams params = new ToastParams();
                            params.text = "没有数据";
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                        } else {
//                            ToastUtils.showShort(e.getMessage());
                            ToastParams params = new ToastParams();
                            params.text = "更新欠费金额失败,请手动更新，" + e.getMessage();
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                        }
                    }

                    @Override
                    public void onSuccess(String s) {
                        MProgressDialog.dismissProgress();
                        Gson gson = new Gson();
                        ArrearageInfoListEntity listEntity = gson.fromJson(s, ArrearageInfoListEntity.class);
                        if (listEntity.getState() == 0) {
                            List<ArrearageInfoEntity> mQianFeiXX = listEntity.getData().getList();
                            double amount = 0;
                            if (mQianFeiXX != null && mQianFeiXX.size() > 0) {
                                for (ArrearageInfoEntity infoEntity : mQianFeiXX) {
                                    if ("余额库+灰库".equals(taskBean.getQfkl())) {
                                        amount += infoEntity.getCmTotalAmt() + infoEntity.getAshAmt();
                                    } else if ("灰库".equals(taskBean.getQfkl())) {
                                        amount += infoEntity.getAshAmt();
                                    } else {
                                        amount += infoEntity.getCmTotalAmt();
                                    }
                                }
                                if (amount == 0) {
                                    taskBean.setQfzje("0.00");
                                } else {
                                    taskBean.setQfzje("" + amount);
                                }
                                notifyDataSetChanged();
                            }
                        } else {
                            ToastParams params = new ToastParams();
                            params.text = listEntity.getMsg() == null ? "更新欠费金额失败," : listEntity.getMsg();
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        MProgressDialog.dismissProgress();
                    }
                });
    }

    /**
     * 跳转到客户详细信息
     */
    private void navigationUserInfo(CollectionTaskListBean taskBean) {
        //查询客户基本信息
        mDisposable1 = EasyHttp.post(URL.CustomerInfoQuery)
                .params("acctId", taskBean.getZhbh() == null ? ""
                        : taskBean.getZhbh())
                .execute(new CallBackProxy<CustomApiResult<CustomerInfoFindResult>,
                        CustomerInfoFindResult>(new CustomCallBack<CustomerInfoFindResult>() {

                    @Override
                    public void onStart() {
                        MProgressDialog.showProgress(ActivityUtils.getTopActivity(), "加载中...");
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                    }

                    @Override
                    public void onSuccess(CustomerInfoFindResult entity) {
                        if (entity != null) {
                            LogUtils.i(entity.toString());
                            Intent intent = new Intent(ActivityUtils.getTopActivity(), UserDetailInformationActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(Constant.BILLBASEINFO, entity);
                            intent.putExtras(bundle);
                            ActivityUtils.getTopActivity().startActivity(intent);
                        } else {
                            ToastUtils.showShort("没有数据");
                        }
                    }

                    @Override
                    public void onCompleted() {
                        MProgressDialog.dismissProgress();
                    }
                }) {
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

        private TextView mOrderNum, mOrderType, mAccountNum, mCustomerName, mWaterCode, mAmountOwed, mWaterAdd, mTvUpdate;
        private TextView mOperate;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mOrderNum = (TextView) itemView.findViewById(R.id.tv1);
            mAccountNum = (TextView) itemView.findViewById(R.id.tv_yonghubh);
            mCustomerName = (TextView) itemView.findViewById(R.id.tv_kehumc);
            mWaterCode = (TextView) itemView.findViewById(R.id.tv_sheshuidm);
            mWaterAdd = (TextView) itemView.findViewById(R.id.tv_yongshuidizhi);
            mAmountOwed = (TextView) itemView.findViewById(R.id.tv_qianfeije);
            mOrderType = (TextView) itemView.findViewById(R.id.tv_collection_type);
            mOperate = (TextView) itemView.findViewById(R.id.txt_operate);
            mTvUpdate = (TextView) itemView.findViewById(R.id.tv_update);
        }
    }
}
