package com.sh3h.hotline.ui.collection;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aliyun.common.utils.StringUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.hjq.toast.ToastParams;
import com.hjq.toast.Toaster;
import com.hjq.toast.style.CustomToastStyle;
import com.maning.mndialoglibrary.MProgressDialog;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.response.CLJBBean;
import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.data.entity.response.DUBillBaseInfo;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.data.entity.response.DUOrder;
import com.sh3h.dataprovider.data.entity.response.DUProcess;
import com.sh3h.dataprovider.data.entity.response.FYLYBean;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.CollectionInformationAdapter;
import com.sh3h.hotline.adapter.CollectionTaskAdapter;
import com.sh3h.hotline.entity.ArrearageInfoEntity;
import com.sh3h.hotline.entity.ArrearageInfoListEntity;
import com.sh3h.hotline.entity.CollectionInformationBean;
import com.sh3h.hotline.entity.CollectionTaskListBean;
import com.sh3h.hotline.entity.MainItemBean;
import com.sh3h.hotline.ui.base.BaseActivity;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.sh3h.hotline.ui.bill.detail.UserDetailInformationActivity;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

public class CollectionInfromationFragment extends ParentFragment implements
        MenuItem.OnMenuItemClickListener, View.OnClickListener {

    @BindView(R.id.tv_number)
    TextView mNumber;
    @BindView(R.id.tv_time)
    TextView mTime;
    @BindView(R.id.tv_acountNum)
    TextView mTvAcountNum;
    @BindView(R.id.tv_acountName)
    TextView mTvAcountName;
    @BindView(R.id.tv_waterAdd)
    TextView mTvWaterAdd;
    @BindView(R.id.tv_waterCode)
    TextView mTvWaterCode;
    @BindView(R.id.tv_waterAddres)
    TextView mTvWaterAddres;
    @BindView(R.id.tv_phoneNum)
    TextView mTvPhoneNum;
    @BindView(R.id.tv_addInformation)
    TextView mTvAddInformation;
    @BindView(R.id.tv_mpbh)
    TextView mTvMpbh;
    @BindView(R.id.tv_pztype)
    TextView mTvPztype;
    @BindView(R.id.tv_kj)
    TextView mTvKj;
    @BindView(R.id.tv_qfje)
    TextView mTvQfje;
    @BindView(R.id.tv_tsbj)
    TextView mTvTsbj;
    @BindView(R.id.tv_dkbj)
    TextView mTvDkbj;
    @BindView(R.id.tv_zdlx)
    TextView mTvZdlx;
    @BindView(R.id.tv_zhycffsj)
    TextView mTvZhycffsj;
    @BindView(R.id.tv_zhycffje)
    TextView mTvZhucffje;
    @BindView(R.id.tv_zhkzcm)
    TextView mTvZhkzcm;
    @BindView(R.id.tv_zhkzsl)
    TextView mTvZhkzsl;
    @BindView(R.id.tv_zhkzsj)
    TextView mTvZhkzsj;
    @BindView(R.id.tv_dqlxr)
    TextView mTvDqlxr;
    @BindView(R.id.tv_zhychbsj)
    TextView mTvZhychbsj;
    @BindView(R.id.tv_sfsmz)
    TextView mTvsfsmz;
    @BindView(R.id.tv_sccjbz)
    TextView mTvSccjbz;
    @BindView(R.id.tv_update)
    TextView mTvUpdate;

    @BindView(R.id.ib_phone)
    ImageButton mIbPhone;

    private Unbinder mUnbinder;

    private static final String TAG = "OrderDetailsFragment";
    private Disposable mDisposable1;
    private CollectionTaskListBean taskBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate" + this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection_information, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    private void initData() {
//        getData();
    }

    private void getData() {
        EasyHttp
                .get(URL.BASE_URL_CB + URL.KeHuQFQuery)
                .params("acctId", taskBean.getZhbh())
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<String>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        showProgress("更新欠费金额中...");
                    }

                    @Override
                    public void onError(ApiException e) {
                        hideProgress();
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
                        mTvQfje.setText("");
                    }

                    @Override
                    public void onSuccess(String s) {
                        hideProgress();
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
                                    mTvQfje.setText("0.00");
                                } else {
                                    mTvQfje.setText("" + amount);
                                }
                            }else {
                                mTvQfje.setText("0.00");
                            }
                        } else {
                            ToastParams params = new ToastParams();
                            params.text = listEntity.getMsg() == null ? "更新欠费金额失败,请手动更新，" : listEntity.getMsg();
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
//                            mTvQfje.setText("");
                        }
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        hideProgress();
                    }
                });
    }

    public String getQfje(){
        return mTvQfje.getText().toString().trim();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            throw new NullPointerException("bundle is null");
        }

        taskBean = bundle.getParcelable("TaskBean");

        mIbPhone.setOnClickListener(this);
        mTvAcountNum.setOnClickListener(this);
        mTvUpdate.setOnClickListener(this);
        if (taskBean != null) {
            mNumber.setText(taskBean.getXh());
//            mTvAcountNum.setText(taskBean.getYhh() + "/" + taskBean.getZhbh());
            mTvAcountNum.setText(taskBean.getZhbh());
            mTime.setText(taskBean.getPdsj());
            mTvAcountName.setText(taskBean.getKhmc());
            mTvWaterAdd.setText(taskBean.getFwdz());
            mTvWaterCode.setText(taskBean.getKhlx());
            mTvWaterAddres.setText(taskBean.getBw());
            mTvAddInformation.setText(taskBean.getDdxxxx());
            mTvMpbh.setText(taskBean.getMpbh());
            mTvPztype.setText(taskBean.getCjpzlb());
            mTvKj.setText(taskBean.getKj());
            mTvQfje.setText(taskBean.getQfzje());
            mTvTsbj.setText(taskBean.getTsbj());
            mTvDkbj.setText(taskBean.getDkbj());
            mTvZdlx.setText(taskBean.getZdlx());
            mTvZhycffsj.setText(taskBean.getFfrq());
            mTvZhucffje.setText(taskBean.getFfje());
            mTvZhkzcm.setText(taskBean.getKzcm());
            mTvZhkzsl.setText(taskBean.getKzsl());
            mTvZhkzsj.setText(taskBean.getKzrq());

            if (TextUtils.isEmpty(taskBean.getLxdh())) {
                mIbPhone.setVisibility(View.GONE);
            } else {
                if (TextUtils.isEmpty(taskBean.getSmzbj()) || "否".equals(taskBean.getSmzbj())) {
                    if (TextUtils.isEmpty(taskBean.getDqlxr())) {
                        mIbPhone.setVisibility(View.GONE);
                    } else {
                        mIbPhone.setVisibility(View.VISIBLE);
                    }
                } else {
                    mIbPhone.setVisibility(View.VISIBLE);
                }
            }

            mTvPhoneNum.setText(taskBean.getLxdh());
            mTvDqlxr.setText(taskBean.getDqlxr());
            mTvZhychbsj.setText(taskBean.getZjhbsj());
            mTvsfsmz.setText(taskBean.getSmzbj());
            mTvSccjbz.setText(taskBean.getSccjbz());

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
        MenuItem save = menu.findItem(R.id.action_save);
        save.setOnMenuItemClickListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        ApplicationsUtil.showMessage(getActivity(), "保存详细信息");
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_acountNum:
                navigationUserInfo();
                break;
            case R.id.ib_phone:
            case R.id.ib_tel:
                callPhone(v.getId());
                break;
            case R.id.tv_update:
                getData();
                break;
            default:
                break;
        }
    }

    /**
     * 拨打电话
     *
     * @param id
     */
    private void callPhone(int id) {
        String phone = "";
        switch (id) {
            case R.id.tv_phoneNum:
                phone = mTvPhoneNum.getText().toString().trim();
                break;
            case R.id.ib_tel:
//  phone = mTvContactValue.getText().toString().trim();
                break;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 跳转到客户详细信息
     */
    private void navigationUserInfo() {
        //查询客户基本信息
        mDisposable1 = EasyHttp.post(URL.CustomerInfoQuery)
                .params("acctId", taskBean.getZhbh() == null ? ""
                        : taskBean.getZhbh())
                .execute(new CallBackProxy<CustomApiResult<CustomerInfoFindResult>,
                        CustomerInfoFindResult>(new CustomCallBack<CustomerInfoFindResult>() {

                    @Override
                    public void onStart() {
                        MProgressDialog.showProgress(getActivity(), "加载中...");
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                    }

                    @Override
                    public void onSuccess(CustomerInfoFindResult entity) {
                        if (entity != null) {
                            LogUtils.i(entity.toString());
                            Intent intent = new Intent(getActivity(), UserDetailInformationActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(Constant.BILLBASEINFO, entity);
                            intent.putExtras(bundle);
                            startActivity(intent);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EasyHttp.cancelSubscription(mDisposable1);
    }
}
