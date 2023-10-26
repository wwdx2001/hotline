package com.sh3h.hotline.ui.nonresident.call;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.maning.mndialoglibrary.MProgressDialog;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.CallListEntity;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.sh3h.hotline.ui.bill.detail.UserDetailInformationActivity;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.exception.ApiException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CallDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CallDetailFragment extends ParentFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.tv_shoulisj)
    TextView mshouLiSJ;

    @BindView(R.id.tv_pch)
    TextView mPch;

    @BindView(R.id.tv_ssdm)
    TextView mSSDM;

    @BindView(R.id.tv_xingzhengq)
    TextView mXingZhengQ;

    @BindView(R.id.tv_yonghum)
    TextView mYongHuH;

    @BindView(R.id.tv_kehumc)
    TextView mKeHuMC;

    @BindView(R.id.tv_lianxir)
    TextView mLianXiR;

    @BindView(R.id.tv_lianxifs)
    TextView mLianXiFS;

    @BindView(R.id.tv_youjidz)
    TextView mYouJiDZ;

    @BindView(R.id.tv_qianfeije)
    TextView mQianFeiJE;

    @BindView(R.id.tv_tuoshoubj)
    TextView mTuoShouBJ;

    @BindView(R.id.tv_zhycffsj)
    TextView mZhycffsj;

    @BindView(R.id.tv_zhycffje)
    TextView mZhycffje;

    @BindView(R.id.txt_inspect)
    public TextView mTxtInspect;

    @BindView(R.id.ib_phone)
    ImageButton mIbPhone;

    private Disposable mDisposable1;

    private CallListEntity mParam1;
    private String mParam2;

    private Unbinder mUnbinder;

    public CallDetailFragment() {
    }

    public static CallDetailFragment newInstance(CallListEntity param1, String param2) {
        CallDetailFragment fragment = new CallDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (CallListEntity) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_detail_new, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mPch.setText(mParam1.getPch());
        mshouLiSJ.setText(mParam1.getPdsj());
        mXingZhengQ.setText(mParam1.getXzq());
        mSSDM.setText(mParam1.getKhlx());

        SpannableString content = new SpannableString(mParam1.getYhh() + "/" + mParam1.getZhbh());
        content.setSpan(new UnderlineSpan(), 0, (mParam1.getYhh() + "/" + mParam1.getZhbh()).length(), 0);
        mYongHuH.setText(content);

        mKeHuMC.setText(mParam1.getKhmc());
        mLianXiR.setText(mParam1.getLxr());
        mLianXiFS.setText(mParam1.getLxfs());
        mYouJiDZ.setText(mParam1.getYjdz());
        mQianFeiJE.setText(mParam1.getQfzje());
        if ("0".equals(mParam1.getTsbj())) {
            mTuoShouBJ.setText("否");
        } else {
            mTuoShouBJ.setText("是");
        }

        if (mParam1.getFfrq() != null && mParam1.getFfrq().contains("1900")) {
            mZhycffsj.setText("");
        } else {
            mZhycffsj.setText(mParam1.getFfrq());
        }

        mZhycffje.setText(mParam1.getFfje());

        mTxtInspect.setOnClickListener(this);
        mYongHuH.setOnClickListener(this);
        mIbPhone.setOnClickListener(this);
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_inspect:
                Intent intent = new Intent(ActivityUtils.getTopActivity(), CallBillDetailNewActivity.class);
                intent.putExtra("yhh", mParam1.getYhh());
                intent.putExtra("qfje", mParam1.getQfzje());
                startActivity(intent);
                break;
            case R.id.tv_yonghum:
                navigationUserInfo();
                break;
            case R.id.ib_phone:
                callPhone(v.getId());
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
        String phone = mLianXiFS.getText().toString().trim();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 跳转到客户详细信息
     */
    private void navigationUserInfo() {
//查询客户基本信息
        mDisposable1 = EasyHttp.post(URL.CustomerInfoQuery)
                .params("acctId", mParam1.getZhbh() == null ? ""
                        : mParam1.getZhbh())
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
}