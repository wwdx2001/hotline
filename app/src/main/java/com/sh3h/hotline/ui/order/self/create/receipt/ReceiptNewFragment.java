package com.sh3h.hotline.ui.order.self.create.receipt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.maning.mndialoglibrary.MProgressDialog;
import com.sh3h.dataprovider.BaseApplication;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.entity.response.CLJBBean;
import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.data.entity.response.DUBillBaseInfo;
import com.sh3h.dataprovider.data.entity.response.DUWord;
import com.sh3h.dataprovider.data.entity.response.FYLXBean;
import com.sh3h.dataprovider.data.entity.response.FYNRBean;
import com.sh3h.dataprovider.data.entity.response.XBBX;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.FYLXBeanDao;
import com.sh3h.dataprovider.greendaoDao.FYNRBeanDao;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.viewholder.SPAdapter;
import com.sh3h.hotline.entity.ZikaidanJLEntity;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.service.SyncType;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.sh3h.hotline.ui.bill.detail.UserDetailInformationActivity;
import com.sh3h.hotline.ui.multimedia.MultimediaFragment;
import com.sh3h.hotline.ui.order.self.create.CreateSelfOrderActivity;
import com.sh3h.hotline.ui.order.self.create.CreateSelfOrderNewActivity;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReceiptNewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReceiptNewFragment extends ParentFragment implements View.OnClickListener,
        ReceiptMvpView {

    @Inject
    ReceiptNewPresenter mPresenter;

    @Inject
    Bus mBus;

    @BindView(R.id.cb_xianchangchuli)
    CheckBox mCbXianchangchuli;

    @BindView(R.id.et_fuwudianbh)
    public EditText mEtFuwudianbh;

    @BindView(R.id.et_fanyinglx)
    EditText mEtFanyinglx;

    @BindView(R.id.et_fanyingnr)
    EditText mEtFanyingnr;

    @BindView(R.id.et_chulijb)
    EditText mEtChulijb;

    @BindView(R.id.et_fashengdizhi)
    EditText mEtFashengdizhi;

    @BindView(R.id.et_xbbx)
    EditText mEtXbbx;

    @BindView(R.id.et_gls)
    EditText mEtGls;

    @BindView(R.id.et_sszd)
    EditText mEtSszd;

    @BindView(R.id.et_zhbh)
    public EditText mEtZhbh;

    @BindView(R.id.et_lxdh)
    EditText mEtLxdh;

    @BindView(R.id.et_wzzt)
    EditText mEtWzzt;

    @BindView(R.id.et_wzdd)
    EditText mEtWzdd;

    @BindView(R.id.et_wznr)
    EditText mEtWznr;

    @BindView(R.id.et_mpbh)
    EditText mEtMpbh;

    @BindView(R.id.et_ywlx)
    EditText mEtYwlx;

    @BindView(R.id.et_xccm)
    EditText mEtXccm;

    @BindView(R.id.et_xcqkbz)
    EditText mEtXcqkbz;

    @BindView(R.id.ll_fylx)
    LinearLayout mLlfylx;

    @BindView(R.id.ll_fynr)
    LinearLayout mLlfynr;

    @BindView(R.id.ll_gls)
    LinearLayout mLlgls;

    @BindView(R.id.ll_sszd)
    LinearLayout mLlsszd;

    @BindView(R.id.ll_zhbh)
    LinearLayout mLlzhbh;

    @BindView(R.id.ll_fwdbh)
    LinearLayout mLlfwdbh;

    @BindView(R.id.ll_cljb)
    LinearLayout mLlcljb;

    @BindView(R.id.ll_xbbx)
    LinearLayout mLlxbbx;

    @BindView(R.id.ll_lxdh)
    LinearLayout mLllxdh;

    @BindView(R.id.ll_wzzt)
    LinearLayout mLlwzzt;

    @BindView(R.id.ll_wzdd)
    LinearLayout mLlwzdd;

    @BindView(R.id.ll_wznr)
    LinearLayout mLlwznr;

    @BindView(R.id.ll_mpbh)
    LinearLayout mLlmpbh;

    @BindView(R.id.ll_ywlx)
    LinearLayout mLlywlx;

    @BindView(R.id.ll_xccm)
    LinearLayout mLlxccm;

    @BindView(R.id.ll_fsdz)
    LinearLayout mLlfsdz;

    @BindView(R.id.ll_xcqkbz)
    LinearLayout mLlxcqkbz;

    @BindView(R.id.ll_sfxccl)
    LinearLayout mLlsfxccl;

    @BindView(R.id.ll_operate)
    LinearLayout mLloperate;

    @BindView(R.id.txt_reset)
    TextView mTxtReset;

    @BindView(R.id.txt_query)
    TextView mTxtQuery;

    private boolean isStartDate;//是否是开始日期

    private int mYear, mMonth, mDay, mHour, mMinute;

    private static final String TAG = "ReceiptFragment";
    private List<String> mChuLiJiBieData = null;
    private List<String> mFanYingLeiXingData = null;
    private List<String> mFanYingNeiRongData = null;
    private List<String> mFanYingQuMingData = null;
    private List<String> mFanYingLaiYuanData = null;

    private ArrayAdapter<String> mChuLiJiBieAdapter = null;
    private ArrayAdapter<String> mFanYingLeiXingAdapter = null;
    private ArrayAdapter<String> mFanYingNeiRongAdapter = null;
    private ArrayAdapter<String> mFanYingQuMingAdapter = null;
    private ArrayAdapter<String> mFanYingLaiYuanAdapter = null;
    private Map<String, String> xbbxMap = new HashMap<>();

    private Unbinder mUnbinder;

    private String mTaskId;
    private String[] mContents;
    private String fylxParams;
    private String cljbParams;
    private List<FYNRBean> mFynrBeanList;
    private List<String> mFynrList;
    private String xbbxParams;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ReceiptNewFragment() {
    }

    public static ReceiptNewFragment newInstance(String param1, String param2) {
        ReceiptNewFragment fragment = new ReceiptNewFragment();
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

        ((CreateSelfOrderNewActivity) getActivity()).getActivityComponent().inject(this);
        mPresenter.attachView(this);
        mBus.register(this);
        mTaskId = getArguments().getString(Constant.TASK_ID, "123456");
        List<XBBX> xbbxes = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getXBBXDao().loadAll();
        for (int i = 0; i < xbbxes.size(); i++) {
            xbbxMap.put(xbbxes.get(i).getMTR_TYPE_CD(), xbbxes.get(i).getDESCR());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receipt_new, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mEtChulijb.setOnClickListener(this);
        mEtFanyinglx.setOnClickListener(this);
        mEtFanyingnr.setOnClickListener(this);
        mEtFuwudianbh.setOnClickListener(this);
        mEtXbbx.setOnClickListener(this);

        mEtGls.setOnClickListener(this);
        mEtSszd.setOnClickListener(this);
        mEtYwlx.setOnClickListener(this);

        mTxtReset.setOnClickListener(this);
        mTxtQuery.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mUnbinder.unbind();
        mPresenter.detachView();
        mBus.unregister(this);
    }

    @Override
    protected void lazyLoad() {

    }

    @Subscribe
    public void onUploadCreateSelfOrder(UIBusEvent.UploadCreateSelfOrder selfOrder) {

        String fuwudianBH = mEtFuwudianbh.getText().toString().trim();//服务点编号
//        String fuwudianBH = "0951703387";//服务点编号
        String fanyingLX = mEtFanyinglx.getText().toString().trim();//反应类型
        String fanyingNR = mEtFanyingnr.getText().toString().trim();//反应内容
        String chuliJB = mEtChulijb.getText().toString().trim();//处理级别
        String fashengDZ = mEtFashengdizhi.getText().toString().trim();//发生地址
        String xbbx = mEtXbbx.getText().toString().trim();//新表表型


        String gls = mEtGls.getText().toString().trim();// 管理所
        String sszd = mEtSszd.getText().toString().trim();// 所属站点
        String zhbh = mEtZhbh.getText().toString().trim();// 账户编号
        String lxdh = mEtLxdh.getText().toString().trim();// 联系电话
        String wzzt = mEtWzzt.getText().toString().trim();// 违章主体
        String wzdd = mEtWzdd.getText().toString().trim();// 违章地点
        String wznr = mEtWznr.getText().toString().trim();// 违章内容
        String mpbh = mEtMpbh.getText().toString().trim();// 铭牌编号
        String ywlx = mEtYwlx.getText().toString().trim();// 疑问类型
        String xccm = mEtXccm.getText().toString().trim();// 现场抄码
        String xcqkbz = mEtXcqkbz.getText().toString().trim();// 现场情况备注

        if ("催缴工单".equals(fanyingLX)) {
            if (StringUtils.isEmpty(zhbh) || StringUtils.isEmpty(fashengDZ)) {
                ToastUtils.showShort("请先将信息填写完整");
                return;
            }
        } else if ("奖励工单".equals(fanyingLX) && "违章用水".equals(fanyingNR)) {
            if (StringUtils.isEmpty(zhbh) || StringUtils.isEmpty(fanyingLX) ||
                    StringUtils.isEmpty(fanyingNR) || StringUtils.isEmpty(gls) ||
                    StringUtils.isEmpty(lxdh) || StringUtils.isEmpty(wzzt) ||
                    StringUtils.isEmpty(wzdd) || StringUtils.isEmpty(wznr) ||
                    StringUtils.isEmpty(xcqkbz)) {
                ToastUtils.showShort("请先将信息填写完整");
                return;
            }
        } else if ("奖励工单".equals(fanyingLX) && "疑问水表".equals(fanyingNR)) {
            if (StringUtils.isEmpty(zhbh) || StringUtils.isEmpty(fanyingLX) ||
                    StringUtils.isEmpty(fanyingNR) || StringUtils.isEmpty(gls) ||
                    StringUtils.isEmpty(sszd) || StringUtils.isEmpty(mpbh) ||
                    StringUtils.isEmpty(ywlx) || StringUtils.isEmpty(xccm) ||
                    StringUtils.isEmpty(fashengDZ) || StringUtils.isEmpty(xcqkbz)) {
                ToastUtils.showShort("请先将信息填写完整");
                return;
            }
        } else {
            if (StringUtils.isEmpty(fuwudianBH) || StringUtils.isEmpty(fanyingLX) ||
                    StringUtils.isEmpty(fanyingNR) || StringUtils.isEmpty(chuliJB) ||
                    StringUtils.isEmpty(fashengDZ) || StringUtils.isEmpty(xbbx)) {
                ToastUtils.showShort("请先将信息填写完整");
                return;
            }
        }

        ZikaidanJLEntity zikaidanJLEntity = new ZikaidanJLEntity();
        zikaidanJLEntity.setFuwudianBH(fuwudianBH);
        zikaidanJLEntity.setChuliJB(cljbParams);
        zikaidanJLEntity.setFanyingLX(fylxParams);
        zikaidanJLEntity.setXbbx(xbbxParams);
        zikaidanJLEntity.setFanyingNR(fanyingNR);
        zikaidanJLEntity.setFashengDZ(fashengDZ);
        zikaidanJLEntity.setZuoyeren("userId");
        zikaidanJLEntity.setTijiaoren(mCbXianchangchuli.isChecked() ? "userId" : "");
        zikaidanJLEntity.setCommitDate(TimeUtils.date2String(new Date()));

        zikaidanJLEntity.setFylx(fanyingLX);
        zikaidanJLEntity.setGls(gls);
        zikaidanJLEntity.setSszd(sszd);
        zikaidanJLEntity.setZhbh(zhbh);
        zikaidanJLEntity.setLxdh(lxdh);
        zikaidanJLEntity.setWzzt(wzzt);
        zikaidanJLEntity.setWzdd(wzdd);
        zikaidanJLEntity.setWznr(wznr);
        zikaidanJLEntity.setMpbh(mpbh);
        zikaidanJLEntity.setYwlx(ywlx);
        zikaidanJLEntity.setXccm(xccm);
        zikaidanJLEntity.setXcqkbz(xcqkbz);
        mPresenter.saveAndCommitOrder((CreateSelfOrderNewActivity) getActivity(), mCbXianchangchuli.isChecked(), zikaidanJLEntity);
    }

    @Override
    public void onToDealOrder(boolean isDeal) {
        LogUtil.i(TAG, "onToDealOrder");

        getActivity().finish();
    }

    @Override
    public void onHint(int resId) {
        hideProgress();
        ApplicationsUtil.showMessage(getActivity(), resId);
    }

    @Override
    public void onHint(String error) {
        ApplicationsUtil.showMessage(getActivity(), error);
    }

    @Override
    public void onSaveSuccess(String savesuccess) {
        ToastUtils.showShort(savesuccess);
        getActivity().finish();
    }

    @Override
    public void onUploadOrder(String taskId, boolean isDeal) {
        startSyncService(SyncType.UPLOAD_All_CREATE_SELF_ORDER.ordinal(), taskId);
    }

    @Override
    public void onInitFanYingLeiXingSpinner(List<DUWord> words) {

    }

    @Override
    public void onInitChuLiJiBieSpinner(List<DUWord> words) {

    }

    @Override
    public void onInitFanYingNeiRongSpinner(List<DUWord> words) {
        mFanYingNeiRongData.clear();
        for (DUWord word : words) {
            mFanYingNeiRongData.add(word.getName());
        }
        mFanYingNeiRongAdapter.notifyDataSetChanged();
    }

    @Override
    public void onInitIssueAreaSpinner(List<DUWord> words) {

    }

    @Override
    public void onInitIssueOriginSpinner(List<DUWord> words) {

    }

    @Override
    public void onIntent(DUBillBaseInfo info) {
        hideProgress();
        Intent intent = new Intent(getActivity(), UserDetailInformationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.BILLBASEINFO, info);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_fuwudianbh:
                showFuwudianBHDialog();
                break;
            case R.id.et_fanyinglx:
            case R.id.et_fanyingnr:
            case R.id.et_chulijb:
            case R.id.et_xbbx://新表表型
            case R.id.et_gls:
            case R.id.et_sszd:
            case R.id.et_ywlx:
                if (GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getFYLXBeanDao().loadAll().size() == 0
                        || GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getCLJBBeanDao().loadAll().size() == 0
                        || GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getFYNRBeanDao().loadAll().size() == 0
                        || GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getXBBXDao().loadAll().size() == 0) {
                    ToastUtils.showShort("正在下载词条数据，请稍后再试");
                    BaseApplication.getInstance().getWordList();
                } else {
                    showWordInfoDialog(v.getId());
                }
                break;
            case R.id.txt_reset:
                mEtZhbh.setText("");
                mEtFashengdizhi.setText("");
                break;
            case R.id.txt_query:
                if (StringUtils.isEmpty(mEtZhbh.getText().toString().trim()) && StringUtils.isEmpty(mEtFashengdizhi.getText().toString().trim())) {
                    ToastUtils.showShort("请输入查询条件！");
                    return;
                }
                Intent intent = new Intent(getActivity(), ReceiptCallQueryResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("fylx", mEtFanyinglx.getText().toString().trim());
                bundle.putString("fynr", mEtFanyingnr.getText().toString().trim());
                bundle.putString("zhbh", mEtZhbh.getText().toString().trim());
                bundle.putString("fsdz", mEtFashengdizhi.getText().toString().trim());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void showFuwudianBHDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("请输入账户编号");
        View view = View.inflate(getActivity(), R.layout.dialog_fuwudianbh, null);
        final EditText editText = (EditText) view.findViewById(R.id.et_zhanghuBH);
        TextView textView = (TextView) view.findViewById(R.id.tv_search);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(editText.getText().toString())) {
                    ToastUtils.showShort("请输入账户编号");
                    return;
                }
                EasyHttp
                        .post(URL.CustomerInfoQuery)
                        .params("acctId", editText.getText().toString())
                        .execute(new CallBackProxy<CustomApiResult<CustomerInfoFindResult>,
                                CustomerInfoFindResult>(new CustomCallBack<CustomerInfoFindResult>() {

                            @Override
                            public void onStart() {
                                MProgressDialog.showProgress(getActivity(), "加载中..");
                            }

                            @Override
                            public void onSuccess(CustomerInfoFindResult entity) {
                                String address1 = entity.getAddress1() == null ? "" : entity.getAddress1();
                                String address2 = entity.getAddress2() == null ? "" : entity.getAddress2();
                                String address3 = entity.getAddress3() == null ? "" : entity.getAddress3();
                                mEtFashengdizhi.setText(address1 + address2 + address3);
                                List<CustomerInfoFindResult.SpListBean> spList = entity.getSpList();
                                final SPAdapter spAdapter = new SPAdapter(R.layout.item_text, spList);
                                recyclerView.setAdapter(spAdapter);
                                spAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                                        alertDialog.dismiss();
                                        if (!spAdapter.getData().get(i).getSpId().equals(mEtFuwudianbh.getText().toString())) {
                                            if (getActivity() instanceof CreateSelfOrderActivity) {
                                                CreateSelfOrderActivity activity = (CreateSelfOrderActivity) getActivity();
                                                activity.setFuwudianBH(spAdapter.getData().get(i).getSpId());
                                                activity.currentTime = String.valueOf(System.currentTimeMillis());
                                                MultimediaFragment fragment = activity.mMultimediaFragment;
                                                if (fragment != null) {
                                                    fragment.setTaskId(spAdapter.getData().get(i).getSpId());
                                                    fragment.mPictureAdapter.setData(new ArrayList<>());
                                                    fragment.mRecordAdapter.setData(new ArrayList<>());
                                                    fragment.mVideosAdapter.setData(new ArrayList<>());
                                                    fragment.mSignUpLayout.setVisibility(View.GONE);
                                                    fragment.mSignDUMedia = null;
                                                    if (mPresenter != null) {
                                                        mPresenter.upLoadFiles.clear();
                                                    }
                                                }
                                            }
                                        }
                                        mEtFuwudianbh.setText(spAdapter.getData().get(i).getSpId());
                                        List<XBBX> xbbxes = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                                                .getXBBXDao().loadAll();
                                        for (int j = 0; j < xbbxes.size(); j++) {
                                            xbbxMap.put(xbbxes.get(j).getDESCR(), xbbxes.get(j).getMTR_TYPE_CD());
                                        }
                                        xbbxParams = spAdapter.getData().get(i).getNewMtrType() == null ? "" :
                                                spAdapter.getData().get(i).getNewMtrType();
                                        mEtXbbx.setText(xbbxMap.get(xbbxParams));

                                    }
                                });
                            }

                            @Override
                            public void onCompleted() {
                                MProgressDialog.dismissProgress();
                            }
                        }) {
                        });
            }
        });
    }

    /**
     * 弹出词条对话框
     *
     * @param id
     */
    private void showWordInfoDialog(final int id) {
        final List<FYLXBean> fylxBeanList = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getFYLXBeanDao().queryBuilder()
                .where(FYLXBeanDao.Properties.FA_TYPE_CD.notEq("FHGD"))
                .where(FYLXBeanDao.Properties.FA_TYPE_CD.notEq("FHYC")).list();
        final List<CLJBBean> chuliBeanList = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getCLJBBeanDao().loadAll();
        final List<XBBX> xbbxBeanList = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getXBBXDao().loadAll();

//        FYLXBean bean = new FYLXBean();
//        bean.setFA_TYPE_DESCR("催缴工单");
//        bean.setFA_TYPE_CD("CJGD");
//        fylxBeanList.add(bean);

//        FYLXBean bean1 = new FYLXBean();
//        bean1.setFA_TYPE_DESCR("奖励工单");
//        bean1.setFA_TYPE_CD("JLGD");
//        fylxBeanList.add(bean1);

        //反应类型
        final List<String> fanyingleixingList = Lists.transform(fylxBeanList, new Function<FYLXBean, String>() {
            @Nullable
            @Override
            public String apply(@Nullable FYLXBean bwbdlBean) {
                return bwbdlBean.getFA_TYPE_DESCR();
            }
        });

        //处理级别
        final List<String> chulijibieList = Lists.transform(chuliBeanList, new Function<CLJBBean, String>() {
            @Nullable
            @Override
            public String apply(@Nullable CLJBBean bwbdlBean) {
                return bwbdlBean.getDESCR();
            }
        });

        //新表表型
        final List<String> xbbxList = Lists.transform(xbbxBeanList, new Function<XBBX, String>() {
            @Nullable
            @Override
            public String apply(@Nullable XBBX xbbx) {
                return xbbx.getDESCR();
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String title = "";
        if (id == R.id.et_fanyinglx) {
            mContents = fanyingleixingList.toArray(new String[fanyingleixingList.size()]);
            title = "反应类型";
        } else if (id == R.id.et_fanyingnr) {
            if (StringUtils.isEmpty(mEtFanyinglx.getText().toString())) {
                ToastUtils.showShort("请选择反应类型");
                return;
            }
            mContents = mFynrList.toArray(new String[mFynrList.size()]);
            title = "反应内容";
        } else if (id == R.id.et_chulijb) {
            mContents = chulijibieList.toArray(new String[chulijibieList.size()]);
            title = "处理级别";
        } else if (id == R.id.et_xbbx) {
            mContents = xbbxList.toArray(new String[xbbxList.size()]);
            title = "新表表型";
        }
//        else if (id == R.id.et_gls) {
////            mContents = xbbxList.toArray(new String[xbbxList.size()]);
//            title = "管理所";
//        } else if (id == R.id.et_sszd) {
////            mContents = xbbxList.toArray(new String[xbbxList.size()]);
//            title = "所属站点";
//        } else if (id == R.id.et_ywlx) {
////            mContents = xbbxList.toArray(new String[xbbxList.size()]);
//            title = "疑问类型";
//        }

        builder.setTitle("请选择" + title);
        builder.setItems(mContents, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (id) {
                    case R.id.et_fanyinglx:
                        fylxParams = fylxBeanList.get(which).getFA_TYPE_CD();
//                        fylxParams = "";
                        mEtFanyinglx.setText(mContents[which]);
                        mFynrBeanList = GreenDaoUtils.getDaoSession(BaseApplication.getInstance())
                                .getFYNRBeanDao().queryBuilder()
                                .where(FYNRBeanDao.Properties.FA_TYPE_CD.eq(fylxParams))
                                .list();

//                        if ("催缴工单".equals(mEtFanyinglx.getText().toString().trim())) {
//                            FYNRBean bean = new FYNRBean();
//                            bean.setDESCR("普通用户催缴");
//                            bean.setFA_TYPE_CD("CJGD");
//                            mFynrBeanList.add(bean);
//                        }
//                        else if ("奖励工单".equals(mEtFanyinglx.getText().toString().trim())) {
//                            FYNRBean bean = new FYNRBean();
//                            bean.setDESCR("违章用水");
//                            bean.setFA_TYPE_CD("JLGD");
//                            mFynrBeanList.add(bean);
//
//                            FYNRBean bean1 = new FYNRBean();
//                            bean1.setDESCR("疑问水表");
//                            bean1.setFA_TYPE_CD("JLGD");
//                            mFynrBeanList.add(bean1);
//                        }

                        //反应内容
                        mFynrList = Lists.transform(mFynrBeanList, new Function<FYNRBean, String>() {
                            @Nullable
                            @Override
                            public String apply(@Nullable FYNRBean bwbdlBean) {
                                return bwbdlBean.getDESCR();
                            }
                        });


                        if ("催缴工单".equals(mEtFanyinglx.getText().toString().trim())) {
                            mLlfylx.setVisibility(View.VISIBLE);
                            mLlfynr.setVisibility(View.VISIBLE);
                            mLlgls.setVisibility(View.GONE);
                            mLlsszd.setVisibility(View.GONE);
                            mLlzhbh.setVisibility(View.VISIBLE);
                            mLlfwdbh.setVisibility(View.GONE);
                            mLlcljb.setVisibility(View.GONE);
                            mLlxbbx.setVisibility(View.GONE);
                            mLllxdh.setVisibility(View.GONE);
                            mLlwzzt.setVisibility(View.GONE);
                            mLlwzdd.setVisibility(View.GONE);
                            mLlwznr.setVisibility(View.GONE);
                            mLlmpbh.setVisibility(View.GONE);
                            mLlywlx.setVisibility(View.GONE);
                            mLlxccm.setVisibility(View.GONE);
                            mLlfsdz.setVisibility(View.VISIBLE);
                            mLlxcqkbz.setVisibility(View.GONE);
                            mLlsfxccl.setVisibility(View.GONE);

                            mLloperate.setVisibility(View.VISIBLE);
                            ((CreateSelfOrderNewActivity) getActivity()).setMenuSubmitVisible(false);
                            ((CreateSelfOrderNewActivity) getActivity()).setGone(false);
                        }
//                        else if ("奖励工单".equals(mEtFanyinglx.getText().toString().trim())) {
//                            mLlfylx.setVisibility(View.VISIBLE);
//                            mLlfynr.setVisibility(View.VISIBLE);
//                            mLlgls.setVisibility(View.VISIBLE);
//                            mLlsszd.setVisibility(View.GONE);
//                            mLlzhbh.setVisibility(View.VISIBLE);
//                            mLlfwdbh.setVisibility(View.GONE);
//                            mLlcljb.setVisibility(View.GONE);
//                            mLlxbbx.setVisibility(View.GONE);
//                            mLllxdh.setVisibility(View.VISIBLE);
//                            mLlwzzt.setVisibility(View.VISIBLE);
//                            mLlwzdd.setVisibility(View.VISIBLE);
//                            mLlwznr.setVisibility(View.VISIBLE);
//                            mLlmpbh.setVisibility(View.GONE);
//                            mLlywlx.setVisibility(View.GONE);
//                            mLlxccm.setVisibility(View.GONE);
//                            mLlfsdz.setVisibility(View.GONE);
//                            mLlxcqkbz.setVisibility(View.VISIBLE);
//                            mLlsfxccl.setVisibility(View.GONE);
//
//                            mLloperate.setVisibility(View.GONE);
//                            ((CreateSelfOrderNewActivity) getActivity()).setMenuSubmitVisible(true);
//                            ((CreateSelfOrderNewActivity) getActivity()).setGone(true);
//                        }
                        else {
                            mLlfylx.setVisibility(View.VISIBLE);
                            mLlfynr.setVisibility(View.VISIBLE);
                            mLlgls.setVisibility(View.GONE);
                            mLlsszd.setVisibility(View.GONE);
                            mLlzhbh.setVisibility(View.GONE);
                            mLlfwdbh.setVisibility(View.VISIBLE);
                            mLlcljb.setVisibility(View.VISIBLE);
                            mLlxbbx.setVisibility(View.VISIBLE);
                            mLllxdh.setVisibility(View.GONE);
                            mLlwzzt.setVisibility(View.GONE);
                            mLlwzdd.setVisibility(View.GONE);
                            mLlwznr.setVisibility(View.GONE);
                            mLlmpbh.setVisibility(View.GONE);
                            mLlywlx.setVisibility(View.GONE);
                            mLlxccm.setVisibility(View.GONE);
                            mLlfsdz.setVisibility(View.VISIBLE);
                            mLlxcqkbz.setVisibility(View.GONE);
                            mLlsfxccl.setVisibility(View.VISIBLE);

                            mLloperate.setVisibility(View.GONE);
                            ((CreateSelfOrderNewActivity) getActivity()).setMenuSubmitVisible(true);
                            ((CreateSelfOrderNewActivity) getActivity()).setGone(true);
                        }

                        mEtFanyingnr.setText(mFynrList.size() > 0 ? mFynrList.get(0) : "");

                        mEtFuwudianbh.setText("");//服务点编号
                        mEtChulijb.setText("");//处理级别
                        mEtFashengdizhi.setText("");//发生地址
                        mEtXbbx.setText("");//新表表型
                        mEtGls.setText("");// 管理所
                        mEtSszd.setText("");// 所属站点
                        mEtZhbh.setText("");// 账户编号
                        mEtLxdh.setText("");// 联系电话
                        mEtWzzt.setText("");// 违章主体
                        mEtWzdd.setText("");// 违章地点
                        mEtWznr.setText("");// 违章内容
                        mEtMpbh.setText("");// 铭牌编号
                        mEtYwlx.setText("");// 疑问类型
                        mEtXccm.setText("");// 现场抄码
                        mEtXcqkbz.setText("");// 现场情况备注
                        break;
                    case R.id.et_fanyingnr:
                        mEtFanyingnr.setText(mContents[which]);

                        if ("催缴工单".equals(mEtFanyinglx.getText().toString().trim()) &&
                                "普通用户催缴".equals(mEtFanyingnr.getText().toString().trim())) {
                            mLlfylx.setVisibility(View.VISIBLE);
                            mLlfynr.setVisibility(View.VISIBLE);
                            mLlgls.setVisibility(View.GONE);
                            mLlsszd.setVisibility(View.GONE);
                            mLlzhbh.setVisibility(View.VISIBLE);
                            mLlfwdbh.setVisibility(View.GONE);
                            mLlcljb.setVisibility(View.GONE);
                            mLlxbbx.setVisibility(View.GONE);
                            mLllxdh.setVisibility(View.GONE);
                            mLlwzzt.setVisibility(View.GONE);
                            mLlwzdd.setVisibility(View.GONE);
                            mLlwznr.setVisibility(View.GONE);
                            mLlmpbh.setVisibility(View.GONE);
                            mLlywlx.setVisibility(View.GONE);
                            mLlxccm.setVisibility(View.GONE);
                            mLlfsdz.setVisibility(View.VISIBLE);
                            mLlxcqkbz.setVisibility(View.GONE);
                            mLlsfxccl.setVisibility(View.GONE);
                        }
//                        else if ("奖励工单".equals(mEtFanyinglx.getText().toString().trim()) &&
//                                "违章用水".equals(mEtFanyingnr.getText().toString().trim())) {
//                            mLlfylx.setVisibility(View.VISIBLE);
//                            mLlfynr.setVisibility(View.VISIBLE);
//                            mLlgls.setVisibility(View.VISIBLE);
//                            mLlsszd.setVisibility(View.GONE);
//                            mLlzhbh.setVisibility(View.VISIBLE);
//                            mLlfwdbh.setVisibility(View.GONE);
//                            mLlcljb.setVisibility(View.GONE);
//                            mLlxbbx.setVisibility(View.GONE);
//                            mLllxdh.setVisibility(View.VISIBLE);
//                            mLlwzzt.setVisibility(View.VISIBLE);
//                            mLlwzdd.setVisibility(View.VISIBLE);
//                            mLlwznr.setVisibility(View.VISIBLE);
//                            mLlmpbh.setVisibility(View.GONE);
//                            mLlywlx.setVisibility(View.GONE);
//                            mLlxccm.setVisibility(View.GONE);
//                            mLlfsdz.setVisibility(View.GONE);
//                            mLlxcqkbz.setVisibility(View.VISIBLE);
//                            mLlsfxccl.setVisibility(View.GONE);
//                        } else if ("奖励工单".equals(mEtFanyinglx.getText().toString().trim()) &&
//                                "疑问水表".equals(mEtFanyingnr.getText().toString().trim())) {
//                            mLlfylx.setVisibility(View.VISIBLE);
//                            mLlfynr.setVisibility(View.VISIBLE);
//                            mLlgls.setVisibility(View.VISIBLE);
//                            mLlsszd.setVisibility(View.VISIBLE);
//                            mLlzhbh.setVisibility(View.VISIBLE);
//                            mLlfwdbh.setVisibility(View.GONE);
//                            mLlcljb.setVisibility(View.GONE);
//                            mLlxbbx.setVisibility(View.GONE);
//                            mLllxdh.setVisibility(View.GONE);
//                            mLlwzzt.setVisibility(View.GONE);
//                            mLlwzdd.setVisibility(View.GONE);
//                            mLlwznr.setVisibility(View.GONE);
//                            mLlmpbh.setVisibility(View.VISIBLE);
//                            mLlywlx.setVisibility(View.VISIBLE);
//                            mLlxccm.setVisibility(View.VISIBLE);
//                            mLlfsdz.setVisibility(View.VISIBLE);
//                            mLlxcqkbz.setVisibility(View.VISIBLE);
//                            mLlsfxccl.setVisibility(View.GONE);
//                        }
                        else {
                            mLlfylx.setVisibility(View.VISIBLE);
                            mLlfynr.setVisibility(View.VISIBLE);
                            mLlgls.setVisibility(View.GONE);
                            mLlsszd.setVisibility(View.GONE);
                            mLlzhbh.setVisibility(View.GONE);
                            mLlfwdbh.setVisibility(View.VISIBLE);
                            mLlcljb.setVisibility(View.VISIBLE);
                            mLlxbbx.setVisibility(View.VISIBLE);
                            mLllxdh.setVisibility(View.GONE);
                            mLlwzzt.setVisibility(View.GONE);
                            mLlwzdd.setVisibility(View.GONE);
                            mLlwznr.setVisibility(View.GONE);
                            mLlmpbh.setVisibility(View.GONE);
                            mLlywlx.setVisibility(View.GONE);
                            mLlxccm.setVisibility(View.GONE);
                            mLlfsdz.setVisibility(View.VISIBLE);
                            mLlxcqkbz.setVisibility(View.GONE);
                            mLlsfxccl.setVisibility(View.VISIBLE);
                        }

                        mEtFuwudianbh.setText("");//服务点编号
                        mEtChulijb.setText("");//处理级别
                        mEtFashengdizhi.setText("");//发生地址
                        mEtXbbx.setText("");//新表表型
                        mEtGls.setText("");// 管理所
                        mEtSszd.setText("");// 所属站点
                        mEtZhbh.setText("");// 账户编号
                        mEtLxdh.setText("");// 联系电话
                        mEtWzzt.setText("");// 违章主体
                        mEtWzdd.setText("");// 违章地点
                        mEtWznr.setText("");// 违章内容
                        mEtMpbh.setText("");// 铭牌编号
                        mEtYwlx.setText("");// 疑问类型
                        mEtXccm.setText("");// 现场抄码
                        mEtXcqkbz.setText("");// 现场情况备注
                        break;
                    case R.id.et_chulijb:
                        cljbParams = chuliBeanList.get(which).getCLJB_ID();
                        mEtChulijb.setText(mContents[which]);
                        break;
                    case R.id.et_xbbx:
                        xbbxParams = xbbxBeanList.get(which).getMTR_TYPE_CD();
                        mEtXbbx.setText(mContents[which]);
                        break;
                    case R.id.et_gls:
//                        xbbxParams = xbbxBeanList.get(which).getMTR_TYPE_CD();
                        mEtGls.setText(mContents[which]);
                        break;
                    case R.id.et_sszd:
//                        xbbxParams = xbbxBeanList.get(which).getMTR_TYPE_CD();
                        mEtSszd.setText(mContents[which]);
                        break;
                    case R.id.et_ywlx:
//                        xbbxParams = xbbxBeanList.get(which).getMTR_TYPE_CD();
                        mEtYwlx.setText(mContents[which]);
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    public String getTaskId() {
        if (!"催缴工单".equals(mEtFanyinglx.getText().toString().trim()) && !"奖励工单".equals(mEtFanyinglx.getText().toString().trim())) {
            return mEtFuwudianbh.getText().toString().trim();
        } else {
            return mEtZhbh.getText().toString().trim();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}