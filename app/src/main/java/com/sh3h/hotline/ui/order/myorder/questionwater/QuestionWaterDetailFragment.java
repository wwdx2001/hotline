package com.sh3h.hotline.ui.order.myorder.questionwater;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hjq.toast.ToastParams;
import com.hjq.toast.Toaster;
import com.hjq.toast.style.CustomToastStyle;
import com.maning.mndialoglibrary.MProgressDialog;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.entity.response.CLJBBean;
import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.data.entity.response.FYLYBean;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.IllegalWaterEntity;
import com.sh3h.hotline.entity.QuestionWaterEntity;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.sh3h.hotline.ui.bill.detail.UserDetailInformationActivity;
import com.sh3h.hotline.ui.order.TaskState;
import com.sh3h.hotline.ui.order.myorder.handle.HandleOrderActivity;
import com.sh3h.hotline.ui.order.myorder.list.MyOrderListActivity;
import com.sh3h.hotline.ui.order.myorder.picture.PictureFileActivity;
import com.sh3h.mobileutil.util.TextUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionWaterDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionWaterDetailFragment extends ParentFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.tv_zhbh)
    TextView tvZhbh;

    @BindView(R.id.tv_xchdbh)
    TextView tvXchdbh;

    @BindView(R.id.tv_albh)
    TextView tvAlbh;

    @BindView(R.id.tv_yalbh)
    TextView tvYalbh;

    @BindView(R.id.tv_fyr)
    TextView tvFyr;

    @BindView(R.id.tv_slzd)
    TextView tvSlzd;

    @BindView(R.id.tv_fssj)
    TextView tvFssj;

    @BindView(R.id.tv_lxdh)
    TextView tvLxdh;

    @BindView(R.id.tv_khmc)
    TextView tvKhmc;

    @BindView(R.id.tv_fyly)
    TextView tvFyly;

    @BindView(R.id.tv_fylx)
    TextView tvFylx;

    @BindView(R.id.tv_fynr)
    TextView tvFynr;

    @BindView(R.id.tv_bh)
    TextView tvBh;

    @BindView(R.id.tv_ywlx)
    TextView tvYwlx;

    @BindView(R.id.tv_xccm)
    TextView tvXccm;

    @BindView(R.id.tv_fsdz)
    TextView tvFsdz;

    @BindView(R.id.tv_xcqkbz)
    TextView tvXcqkbz;

    @BindView(R.id.tv_cljb)
    TextView tvCljb;

    @BindView(R.id.tv_clsx)
    TextView tvClsx;

    @BindView(R.id.tv_pdsj)
    TextView tvPdsj;

    @BindView(R.id.tv_yysj)
    TextView tvYysj;

    @BindView(R.id.txt_inspect_xchjcp)
    TextView txtInspectXchjcp;

    @BindView(R.id.txt_inspect_bpzp)
    TextView txtInspectBpzp;

    @BindView(R.id.ib_phone)
    ImageButton mIbPhone;

    private Disposable mDisposable1;

    private String mParam1;
    private String mParam2;

    private Unbinder mUnbinder;
    private DUMyTask mDuMyTask;
    private Map<String, String> map;
    private QuestionWaterEntity entity;

    public QuestionWaterDetailFragment() {
    }

    public static QuestionWaterDetailFragment newInstance(String param1, String param2) {
        QuestionWaterDetailFragment fragment = new QuestionWaterDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        map = new HashMap<>();
        List<FYLYBean> fylyBeans = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getFYLYBeanDao().loadAll();
        List<CLJBBean> cljbBeans = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getCLJBBeanDao().loadAll();
        for (int i = 0; i < fylyBeans.size(); i++) {
            map.put(fylyBeans.get(i).getLY_ID(), fylyBeans.get(i).getDESCR());
        }
        for (int i = 0; i < cljbBeans.size(); i++) {
            map.put(cljbBeans.get(i).getCLJB_ID(), cljbBeans.get(i).getDESCR());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_water_detail, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initViews(view);
        initData(view);
        return view;
    }

    private void initViews(View view) {
        Bundle bundle = getArguments();
        if (bundle == null) {
            throw new NullPointerException("bundle is null");
        }
        mDuMyTask = bundle.getParcelable(Constant.DUMyTask);

        tvKhmc.setText("");
        tvBh.setText("");
        tvYwlx.setText("");
        tvXccm.setText("");
        tvXcqkbz.setText("");

        tvZhbh.setText(mDuMyTask.getAcctId());
        tvXchdbh.setText(mDuMyTask.getFaId());
        tvAlbh.setText(mDuMyTask.getCaseId());
        tvYalbh.setText(mDuMyTask.getOldCaseId());
        tvFyr.setText(mDuMyTask.getEntityName());
        tvSlzd.setText(mDuMyTask.getDisPatGrp());
        tvFssj.setText(mDuMyTask.getLdsj());
        tvFsdz.setText(mDuMyTask.getFsdz());
        tvLxdh.setText(mDuMyTask.getContactValue());
        if (map != null && !StringUtils.isEmpty(map.get(mDuMyTask.getFyly()))) {
            tvFyly.setText(map.get(mDuMyTask.getFyly()));
        } else {
            tvFyly.setText(mDuMyTask.getFyly());
        }
        tvFylx.setText(mDuMyTask.getFaTypeCd());
        tvFynr.setText(mDuMyTask.getFynr());

        if (map != null && !StringUtils.isEmpty(map.get(mDuMyTask.getCljb()))) {
            tvCljb.setText(map.get(mDuMyTask.getCljb()));
        } else {
            tvCljb.setText(mDuMyTask.getCljb());
        }
        tvClsx.setText(mDuMyTask.getClsx());
        tvPdsj.setText(mDuMyTask.getCreDttm());
        tvYysj.setText(mDuMyTask.getYysj());

        txtInspectXchjcp.setOnClickListener(this);
        txtInspectBpzp.setOnClickListener(this);
        tvZhbh.setOnClickListener(this);
        mIbPhone.setOnClickListener(this);
    }

    private void initData(View view) {
        mDisposable1 = EasyHttp
                .post(URL.YiWenSBSel)
                .params("cbyid", SPUtils.getInstance().getString(Constant.USERID))
                .params("xchd", mDuMyTask.getFaId())
                .execute(new CallBackProxy<CustomApiResult<List<QuestionWaterEntity>>,
                        List<QuestionWaterEntity>>(new CustomCallBack<List<QuestionWaterEntity>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        MProgressDialog.showProgress(getActivity(), "加载中...");
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        ToastParams params = new ToastParams();
                        params.text = e.getMessage();
                        params.style = new CustomToastStyle(R.layout.toast_error);
                        Toaster.show(params);
                    }

                    @Override
                    public void onSuccess(final List<QuestionWaterEntity> mNetWorkDatas) {
                        if (mNetWorkDatas == null || mNetWorkDatas.size() == 0) {
                            ToastUtils.showLong("无工单数据");
                        } else {
                            entity = mNetWorkDatas.get(0);
                            if (entity != null) {
                                tvKhmc.setText("");
                                tvBh.setText(entity.getJlbbh());
                                tvYwlx.setText(entity.getYwlx());
                                tvXccm.setText(entity.getXccb());
                                tvXcqkbz.setText(entity.getXcqkbz());
                            }
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
            case R.id.txt_inspect_xchjcp:
                navigationPicyureActivity("xchj",mDuMyTask.getFaId());
                break;
            case R.id.txt_inspect_bpzp:
                navigationPicyureActivity("bp",mDuMyTask.getFaId());
                break;
            case R.id.tv_zhbh:
                navigationUserInfo();
                break;
            case R.id.ib_phone:
                callPhone(v.getId());
                break;
            default:
                break;
        }
    }

    private void navigationPicyureActivity(String type, String taskeId) {
        if (entity != null) {
            Intent intent2 = new Intent(ActivityUtils.getTopActivity(), PictureFileActivity.class);
            intent2.putExtra(Constant.ORIGIN, 21);
            intent2.putExtra("questionTaskBean", entity);
            intent2.putExtra("type", type);
            intent2.putExtra(Constant.TASK_ID, taskeId);
            intent2.putExtra("taskType", Constant.TASK_TYPE_DOWNLOAD);
            intent2.putExtra("taskState", TaskState.HANDLE);
            startActivity(intent2);
        }
    }

    /**
     * 拨打电话
     *
     * @param id
     */
    private void callPhone(int id) {
        String phone = tvLxdh.getText().toString().trim();
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
                .params("acctId", tvZhbh.getText().toString().trim())
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