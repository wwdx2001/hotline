package com.sh3h.hotline.ui.nonresident.selfbilling;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sh3h.dataprovider.data.entity.newentity.OverrateSelfHandleEntity;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.OverrateSelfHandleEntityDao;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.OverrateSelfBillingEntity;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.sh3h.hotline.util.DisplayUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditSubmitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditSubmitFragment extends ParentFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.tv_zhanghubh)
    public TextView mZhangHuBH;

    @BindView(R.id.tv_xinngzhengq)
    public TextView mXingZhengQ;

    @BindView(R.id.tv_sheshuidm)
    public TextView mSheShuiDM;

    @BindView(R.id.et_tongyishxydm)
    public MaterialEditText mTongYiSHXYDM;

    @BindView(R.id.et_kehumc)
    public MaterialEditText mKeHuMC;

    @BindView(R.id.et_lianxir)
    public MaterialEditText mLianXiR;

    @BindView(R.id.et_lianxifs)
    public MaterialEditText mLianXiFS;

    @BindView(R.id.et_youjidz)
    public MaterialEditText mYouJiDZ;

    @BindView(R.id.et_beizhu)
    public EditText mBeiZhu;

    private OverrateSelfBillingEntity mParam1;
    private String mParam2;

    private Unbinder mUnbinder;

    public OverrateSelfHandleEntity handlerEntity;

    public EditSubmitFragment() {
    }

    public static EditSubmitFragment newInstance(OverrateSelfBillingEntity param1, String param2) {
        EditSubmitFragment fragment = new EditSubmitFragment();
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
            mParam1 = (OverrateSelfBillingEntity) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_submit_new, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initViews(view);
        initData(view);
        return view;
    }

    private void initViews(View view) {

        setListener();
    }

    private void setListener() {
        mTongYiSHXYDM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mParam1.getTyshxydm().contentEquals(s)) {
                    mTongYiSHXYDM.setHelperText(mParam1.getTyshxydm());
                    mTongYiSHXYDM.setBottomTextSize(ConvertUtils.sp2px(17.0f));

                } else {
                    mTongYiSHXYDM.setHelperText("");
                }
            }
        });

        mKeHuMC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mParam1.getKhmc().contentEquals(s)) {
                    mKeHuMC.setHelperText(mParam1.getKhmc());
                    mKeHuMC.setBottomTextSize(ConvertUtils.sp2px(17.0f));
                } else {
                    mKeHuMC.setHelperText("");
                }
            }
        });

        mLianXiR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mParam1.getLxr().contentEquals(s)) {
                    mLianXiR.setHelperText(mParam1.getLxr());
                    mLianXiR.setBottomTextSize(ConvertUtils.sp2px(17.0f));
                } else {
                    mLianXiR.setHelperText("");
                }
            }
        });

        mLianXiFS.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mParam1.getLxfs().contentEquals(s)) {
                    mLianXiFS.setHelperText(mParam1.getLxfs());
                    mLianXiFS.setBottomTextSize(ConvertUtils.sp2px(17.0f));
                } else {
                    mLianXiFS.setHelperText("");
                }
            }
        });

        mYouJiDZ.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mParam1.getYjdz().contentEquals(s)) {
                    mYouJiDZ.setHelperText(mParam1.getYjdz());
                    mYouJiDZ.setBottomTextSize(ConvertUtils.sp2px(17.0f));
                } else {
                    mYouJiDZ.setHelperText("");
                }
            }
        });
    }

    private void initData(View view) {
        List<OverrateSelfHandleEntity> entityList = GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getOverrateSelfHandleEntityDao()
                .queryBuilder()
                .where(OverrateSelfHandleEntityDao.Properties.Zhbh.eq(mParam1.getZhbh()))
                .list();
        if (entityList != null && entityList.size() > 0) {
            handlerEntity = entityList.get(0);
            mZhangHuBH.setText(handlerEntity.getYhh() + "/" + handlerEntity.getZhbh());
            mXingZhengQ.setText(handlerEntity.getXzq());
            mSheShuiDM.setText(handlerEntity.getKhlx());
            mTongYiSHXYDM.setText(handlerEntity.getTyshxydm());
            mKeHuMC.setText(handlerEntity.getKhmc());
            mLianXiR.setText(handlerEntity.getLxr());
            mLianXiFS.setText(handlerEntity.getLxfs());
            mYouJiDZ.setText(handlerEntity.getYjdz());
            mBeiZhu.setText(handlerEntity.getBz());
        } else {
            mZhangHuBH.setText(mParam1.getYhh() + "/" + mParam1.getZhbh());
            mXingZhengQ.setText(mParam1.getXzq());
            mSheShuiDM.setText(mParam1.getKhlx());
            mTongYiSHXYDM.setText(mParam1.getTyshxydm());
            mKeHuMC.setText(mParam1.getKhmc());
            mLianXiR.setText(mParam1.getLxr());
            mLianXiFS.setText(mParam1.getLxfs());
            mYouJiDZ.setText(mParam1.getYjdz());
            mBeiZhu.setText(mParam1.getBz());
        }
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

    public void clearData() {
        mZhangHuBH.setText("");
        mXingZhengQ.setText("");
        mSheShuiDM.setText("");
        mTongYiSHXYDM.setText("");
        mKeHuMC.setText("");
        mLianXiR.setText("");
        mLianXiFS.setText("");
        mYouJiDZ.setText("");
        mBeiZhu.setText("");
        mParam1 = null;
    }

    public void notifyData(OverrateSelfBillingEntity selfBillingEntity) {
        this.mParam1 = selfBillingEntity;
        List<OverrateSelfHandleEntity> entityList = GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getOverrateSelfHandleEntityDao()
                .queryBuilder()
                .where(OverrateSelfHandleEntityDao.Properties.Zhbh.eq(mParam1.getZhbh()))
                .list();
        if (entityList != null && entityList.size() > 0) {
            handlerEntity = entityList.get(0);
            mZhangHuBH.setText(handlerEntity.getYhh() + "/" + handlerEntity.getZhbh());
            mXingZhengQ.setText(handlerEntity.getXzq());
            mSheShuiDM.setText(handlerEntity.getKhlx());
            mTongYiSHXYDM.setText(handlerEntity.getTyshxydm());
            mKeHuMC.setText(handlerEntity.getKhmc());
            mLianXiR.setText(handlerEntity.getLxr());
            mLianXiFS.setText(handlerEntity.getLxfs());
            mYouJiDZ.setText(handlerEntity.getYjdz());
            mBeiZhu.setText(handlerEntity.getBz());
        } else {
            mZhangHuBH.setText(mParam1.getYhh() + "/" + mParam1.getZhbh());
            mXingZhengQ.setText(mParam1.getXzq());
            mSheShuiDM.setText(mParam1.getKhlx());
            mTongYiSHXYDM.setText(mParam1.getTyshxydm());
            mKeHuMC.setText(mParam1.getKhmc());
            mLianXiR.setText(mParam1.getLxr());
            mLianXiFS.setText(mParam1.getLxfs());
            mYouJiDZ.setText(mParam1.getYjdz());
            mBeiZhu.setText(mParam1.getBz());
        }
    }

    public OverrateSelfHandleEntity getHandlerEntity() {
        return handlerEntity;
    }
}