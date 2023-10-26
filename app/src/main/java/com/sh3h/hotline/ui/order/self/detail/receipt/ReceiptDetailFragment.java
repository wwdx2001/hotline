package com.sh3h.hotline.ui.order.self.detail.receipt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.sh3h.dataprovider.data.entity.request.DUCreateSelfOrder;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.hotline.R;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.sh3h.hotline.ui.order.self.detail.CreateSelfOrderDetailActivity;
import com.sh3h.localprovider.greendaoEntity.Task;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.Unbinder;

/**
 * Created by dengzhimin on 2016/9/18.
 */
public class ReceiptDetailFragment extends ParentFragment implements ReceiptDetailMvpView {

    @Inject
    ReceiptDetailPresenter mPresenter;

    @Inject
    EventPosterHelper mEventPosterHelper;

    @BindView(R.id.tv_xiaogenhao)
    TextView mTvXiaoGenHao;

    @BindView(R.id.tv_huming)
    TextView mTvHuMing;

    @BindView(R.id.tv_fanyingren)
    TextView mTvFanYingRen;

    @BindView(R.id.tv_fashengdizhi)
    TextView mTvFaShengDiZhi;

    @BindView(R.id.tv_lianxidianhua)
    TextView mTvLianXiDianHua;

    @BindView(R.id.tv_fanyingleixing)
    TextView mTvFanYingLeiXing;

    @BindView(R.id.tv_fanyingneirong)
    TextView mTvFanYingNeiRong;

    @BindView(R.id.tv_fanyingquming)
    TextView mTvFanYingQuMing;//区名

    @BindView(R.id.tv_fanyinglaiyuan)
    TextView mTvFanYingLaiYuan;//来源

    @BindView(R.id.tv_chulijibie)
    TextView mTvChuLiJiBie;

    @BindView(R.id.tv_shoulibeizhu)
    TextView mTvShouLiBeiZhu;

    @BindView(R.id.cb_xianchangchuli)
    CheckBox mCbXianChangChuLi;

    private static final String TAG = "ReceiptDetailFragment";

    private Unbinder mUnbinder;

    private String mTaskId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CreateSelfOrderDetailActivity) getActivity()).getActivityComponent().inject(this);
        mPresenter.attachView(this);

        try {
            mTaskId = getArguments().getString(Constant.TASK_ID);
            if (mTaskId == null) {
                Toast.makeText(getActivity(), R.string.toast_taskid_is_null, Toast.LENGTH_SHORT).show();
                throw new NullPointerException("TaskId is null");
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
            e.printStackTrace();
            getActivity().finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receipt_detail, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mPresenter.initData(mTaskId);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mPresenter.detachView();
    }

    @Override
    protected void lazyLoad() {

    }

//    @OnCheckedChanged(R.id.cb_xianchangchuli)
//    public void sceneDealWith(boolean checked) {
//        mEventPosterHelper.postEventSafely(new UIBusEvent.NotifyToolBar(checked));
//    }

    @Override
    public void onInitData(DUCreateSelfOrder order) {
        if (order == null) {
            Toast.makeText(getActivity(), R.string.toast_task_info_is_null, Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

        mTvXiaoGenHao.setText(TextUtil.getString(order.getCardId()));
        mTvHuMing.setText(TextUtil.getString(order.getIssueName()));
        mTvFanYingRen.setText(TextUtil.getString(order.getIssuer()));
        mTvFaShengDiZhi.setText(TextUtil.getString(order.getIssueAddress()));
        mTvLianXiDianHua.setText(TextUtil.getString(order.getTelephone()));
        mTvFanYingLeiXing.setText(TextUtil.getString(order.getIssueType()));
        mTvFanYingNeiRong.setText(TextUtil.getString(order.getIssueContent()));
        mTvFanYingQuMing.setText(TextUtil.getString(order.getIssueArea()));
        mTvFanYingLaiYuan.setText(TextUtil.getString(order.getIssueOrigin()));
        mTvChuLiJiBie.setText(TextUtil.getString(order.getReplyClass()));
        mTvShouLiBeiZhu.setText(TextUtil.getString(order.getReceviceComment()));
        mCbXianChangChuLi.setChecked(order.isReplyImmediately());
    }

    @Override
    public void onError(int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }
}
