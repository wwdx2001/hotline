package com.sh3h.hotline.ui.nonresident.selfbilling;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.sh3h.dataprovider.BaseApplication;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.entity.response.CLJBBean;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.data.entity.response.FYLXBean;
import com.sh3h.dataprovider.data.entity.response.FYNRBean;
import com.sh3h.dataprovider.data.entity.response.XBBX;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.DUMyTaskDao;
import com.sh3h.dataprovider.greendaoDao.FYLXBeanDao;
import com.sh3h.dataprovider.greendaoDao.FYNRBeanDao;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.adapter.QuerySelectAdapter;
import com.sh3h.hotline.adapter.baseadapter.MainAdapter;
import com.sh3h.hotline.entity.MainItemBean;
import com.sh3h.hotline.entity.OverrateSelfBillingEntity;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.xujiaji.happybubble.BubbleDialog;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.exception.ApiException;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.xujiaji.happybubble.Auto.UP_AND_DOWN;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuerySelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuerySelectFragment extends ParentFragment implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener, AdapterView.OnItemSelectedListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.et_fanyinglx)
    EditText mFanYingLX;

    @BindView(R.id.et_fanyingnr)
    EditText mFanYingNR;

    @BindView(R.id.spinner_fylb)
    public Spinner mSpinnerFylb;

    @BindView(R.id.spinner_fynr)
    public Spinner mSpinnerFynr;

    @BindView(R.id.et_zhanghubh)
    public EditText mZhangHuBH;

    @BindView(R.id.et_kehumc)
    EditText mKeHuMC;

    @BindView(R.id.et_youjidz)
    EditText mYouJiDZ;

    @BindView(R.id.txt_reset)
    TextView mReset;

    @BindView(R.id.txt_query)
    TextView mQuery;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private String mParam1;
    private String mParam2;

    private Unbinder mUnbinder;
    private List<OverrateSelfBillingEntity> itemBeans;
    private QuerySelectAdapter mAdapter;
    private String[] mContents;
    private String fylxParams;
    private List<FYNRBean> mFynrBeanList;
    private List<String> mFynrList;

    private OverrateSelfBillingEntity currentEntity;

    public QuerySelectFragment() {
    }

    public static QuerySelectFragment newInstance(String param1, String param2) {
        QuerySelectFragment fragment = new QuerySelectFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_query_select, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initViews(view);
        initData(view);
        return view;
    }

    private void initViews(View view) {
        mFanYingLX.setOnClickListener(this);
        mFanYingNR.setOnClickListener(this);
        mReset.setOnClickListener(this);
        mQuery.setOnClickListener(this);

        mSpinnerFylb.setOnItemSelectedListener(this);
        mSpinnerFynr.setOnItemSelectedListener(this);

        initRecyclerView(view);
    }

    private void initRecyclerView(View view) {
        itemBeans = new ArrayList<>();
        mAdapter = new QuerySelectAdapter(R.layout.item_query_select, itemBeans);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void initData(View view) {
        String[] spinnerArrFYLB = new String[]{"奖励工单"};
        String[] spinnerArrFYNR = new String[]{"非居累进加价客户资料变更"};

        ArrayAdapter<String> fylbAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArrFYLB);
        fylbAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerFylb.setAdapter(fylbAdapter);
        ArrayAdapter<String> fynrAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArrFYNR);
        fynrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerFynr.setAdapter(fynrAdapter);
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
            case R.id.et_fanyinglx:
            case R.id.et_fanyingnr:
//                if (GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getFYLXBeanDao().loadAll().size() == 0
//                        || GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getFYNRBeanDao().loadAll().size() == 0) {
//                    ToastUtils.showShort("正在下载词条数据，请稍后再试");
//                    BaseApplication.getInstance().getWordList();
//                } else {
//                    showWordInfoDialog(v.getId());
//                }
                break;
            case R.id.txt_reset:
                mZhangHuBH.setText("");
                mKeHuMC.setText("");
                mYouJiDZ.setText("");

                itemBeans.clear();
                mAdapter.notifyDataSetChanged();

                ((OverrateSelfBillingActivity) getActivity()).setConfirnVisible(false);
                break;
            case R.id.txt_query:
                if ("".equals(mZhangHuBH.getText().toString().trim())
                        && "".equals(mKeHuMC.getText().toString().trim())
                        && "".equals(mYouJiDZ.getText().toString().trim())) {
                    ToastUtils.showShort("请至少输入一个查询条件！");
                } else {
                    queryData();
                }
                break;
            default:
                break;
        }
    }

    private void queryData() {
        itemBeans.clear();
//        for (int i = 0; i < 20; i++) {
//            OverrateSelfBillingEntity ofbEntity= new OverrateSelfBillingEntity();
//            ofbEntity.setZhbh("1234567890");
//            ofbEntity.setKhmc("张三");
//            ofbEntity.setXzq("浦东新区");
//            ofbEntity.setSsdm("4200-公共管理、社会保障和社会组织");
//            ofbEntity.setTyshxydm("325614527846987456");
//            ofbEntity.setLxr("李四");
//            ofbEntity.setLxfs("13154268759");
//            ofbEntity.setYjdz("上海市浦东新区控江路128弄25号nfdjkvgnorwjvnfvrnwegvrnlewvorlwvjnrewghreiwgvnfrkjvhnrswhfbvnsdfnvswbnfvwfebvnhkrbfvgrfbvwrhsvfbwrevfhrwfbvgrwhfvgjsbrfvhfjswvbsdfbvsdfbvdksfbvhbfdvbdfhsbvskdbvdfsbvfdhvgrwfvgbksdhfbvs");
//            itemBeans.add(ofbEntity);
//        }
//        mAdapter.notifyDataSetChanged();
        EasyHttp
                .post(URL.ZhangHuXXQuery)
                .params("zhbh", mZhangHuBH.getText().toString().trim())
                .params("yhlx", "非居用户")
                .params("khmc", mKeHuMC.getText().toString().trim())
                .params("yjdz", mYouJiDZ.getText().toString().trim())
                .execute(new CallBackProxy<CustomApiResult<List<OverrateSelfBillingEntity>>,
                        List<OverrateSelfBillingEntity>>(new CustomCallBack<List<OverrateSelfBillingEntity>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                    }

                    @Override
                    public void onSuccess(final List<OverrateSelfBillingEntity> mNetWorkDatas) {
                        LogUtils.e(mNetWorkDatas.toString());
                        itemBeans.addAll(mNetWorkDatas);
                        mAdapter.notifyDataSetChanged();
                        ((OverrateSelfBillingActivity) getActivity()).setConfirnVisible(true);
                    }
                }) {
                });
    }

    private void showWordInfoDialog(final int id) {
        final List<FYLXBean> fylxBeanList = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getFYLXBeanDao().queryBuilder()
                .where(FYLXBeanDao.Properties.FA_TYPE_CD.notEq("FHGD"))
                .where(FYLXBeanDao.Properties.FA_TYPE_CD.notEq("FHYC")).list();
        //反应类型
        final List<String> fanyingleixingList = Lists.transform(fylxBeanList, new Function<FYLXBean, String>() {
            @Nullable
            @Override
            public String apply(@Nullable FYLXBean bwbdlBean) {
                return bwbdlBean.getFA_TYPE_DESCR();
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String title = "";
        if (id == R.id.et_fanyinglx) {
            mContents = fanyingleixingList.toArray(new String[fanyingleixingList.size()]);
            title = "反应类型";
        } else if (id == R.id.et_fanyingnr) {
            if (StringUtils.isEmpty(mFanYingLX.getText().toString())) {
                ToastUtils.showShort("请选择反应类型");
                return;
            }

            mContents = mFynrList.toArray(new String[mFynrList.size()]);
            title = "反应内容";
        }
        builder.setTitle("请选择" + title);
        builder.setItems(mContents, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (id) {
                    case R.id.et_fanyinglx:
                        fylxParams = fylxBeanList.get(which).getFA_TYPE_CD();
                        mFanYingLX.setText(mContents[which]);
                        mFynrBeanList = GreenDaoUtils.getDaoSession(BaseApplication.getInstance())
                                .getFYNRBeanDao().queryBuilder()
                                .where(FYNRBeanDao.Properties.FA_TYPE_CD.eq(fylxParams))
                                .list();
                        //反应内容
                        mFynrList = Lists.transform(mFynrBeanList, new Function<FYNRBean, String>() {
                            @Nullable
                            @Override
                            public String apply(@Nullable FYNRBean bwbdlBean) {
                                return bwbdlBean.getDESCR();
                            }
                        });
                        mFanYingNR.setText(mFynrList.size() > 0 ? mFynrList.get(0) : "");
                        break;
                    case R.id.et_fanyingnr:
                        mFanYingNR.setText(mContents[which]);
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    public OverrateSelfBillingEntity getCurrentEntity() {
        return currentEntity;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        currentEntity = itemBeans.get(position);
        ((OverrateSelfBillingActivity) getActivity()).setCurrentEntity(currentEntity);
        if (!itemBeans.get(position).isChecked()) {
            for (OverrateSelfBillingEntity entity : itemBeans) {
                entity.setChecked(false);
            }
            currentEntity.setChecked(true);
        }
        mAdapter.setDefaultSelect(position);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.tv_sheshuidm:
                View bubbleView = LayoutInflater.from(ActivityUtils.getTopActivity()).inflate(R.layout.dialog_view_air_bubble, null);
                TextView txtShowContent = (TextView) bubbleView.findViewById(R.id.txt_show_content);
                txtShowContent.setText(itemBeans.get(position).getKhlx());
                new BubbleDialog(ActivityUtils.getTopActivity())
                        .addContentView(bubbleView)
                        .setClickedView(view)
                        .setPosition(BubbleDialog.Position.TOP, BubbleDialog.Position.LEFT)
                        .setOffsetY(8)
//                        .setOffsetX(10)
                        .autoPosition(UP_AND_DOWN)
                        .setTransParentBackground()
                        .calBar(true)
                        .show();
                break;
            case R.id.tv_youjidz:
                View bubbleView2 = LayoutInflater.from(ActivityUtils.getTopActivity()).inflate(R.layout.dialog_view_air_bubble, null);
                TextView txtShowContent2 = (TextView) bubbleView2.findViewById(R.id.txt_show_content);
                txtShowContent2.setText(itemBeans.get(position).getYjdz());
                new BubbleDialog(ActivityUtils.getTopActivity())
                        .addContentView(bubbleView2)
                        .setClickedView(view)
//                        .setPosition(BubbleDialog.Position.BOTTOM, BubbleDialog.Position.RIGHT)
                        .setOffsetY(8)
                        .autoPosition(UP_AND_DOWN)
                        .setTransParentBackground()
                        .calBar(true)
                        .show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String result = parent.getItemAtPosition(position).toString();
//        ToastUtils.showShort(result);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}