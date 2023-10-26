package com.sh3h.hotline.ui.nonresident.call;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.dataprovider.data.entity.newentity.OverrateCallHandleEntity;
import com.sh3h.dataprovider.data.entity.newentity.OverrateReceiptHandleEntity;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.OverrateCallHandleEntityDao;
import com.sh3h.dataprovider.greendaoDao.OverrateReceiptHandleEntityDao;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.CallListEntity;
import com.sh3h.hotline.ui.base.BaseActivity;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.sh3h.hotline.ui.nonresident.sign.SignatureNameNewActivity;
import com.sh3h.hotline.ui.order.TaskState;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CallHandleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CallHandleFragment extends ParentFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @Inject
    DataManager mDataManager;

    @BindView(R.id.et_cuijiaodl)
    EditText mCuiJiaoDL;

    @BindView(R.id.spinner_cjdl)
    public Spinner spinnerCjdl;

    @BindView(R.id.et_cuijiaoxl)
    EditText mCuiJiaoXL;

    @BindView(R.id.spinner_cjxl)
    public Spinner spinnerCjxl;

    @BindView(R.id.et_zqzddz)
    public EditText mZzddz;

    @BindView(R.id.et_zhengquehm)
    public EditText mZhenQqueHM;

    @BindView(R.id.et_xinhum)
    public EditText mXnHuM;

    @BindView(R.id.et_jufuly)
    public EditText mJuFuLY;

    @BindView(R.id.et_qingkuangms)
    public EditText mQingKuangMS;

    @BindView(R.id.et_ncqcjcs)
    public EditText mNqcjcs;

    @BindView(R.id.et_cuijiaoqr)
    EditText mCuiJiaoQR;

    @BindView(R.id.et_cjbz)
    public EditText mCjbz;

    @BindView(R.id.spinner_cjqr)
    public Spinner spinnerCjqr;

    @BindView(R.id.et_xinxibg)
    EditText mXinXiBG;

    @BindView(R.id.spinner_xxbg)
    public Spinner spinnerXxbg;

    @BindView(R.id.ll_cjxl)
    LinearLayout llCjxl;

    @BindView(R.id.ll_zqzddz)
    LinearLayout llZqzddz;

    @BindView(R.id.ll_zqhm)
    LinearLayout llZqhm;

    @BindView(R.id.ll_xhm)
    LinearLayout llXhm;

    @BindView(R.id.ll_jfly)
    LinearLayout llJfly;

    @BindView(R.id.ll_qkms)
    LinearLayout llQkms;

    @BindView(R.id.ll_sign)
    LinearLayout llSign;

    @BindView(R.id.constriant)
    LinearLayout constrain;

    @BindView(R.id.ll_xxbg)
    LinearLayout llXxbg;


    @BindView(R.id.tv_zhanghubh)
    TextView mZhangHuBH;

    @BindView(R.id.tv_xinngzhengq)
    TextView mXingZhengQ;

    @BindView(R.id.tv_sheshuidm)
    TextView mSheShuiDM;

    @BindView(R.id.et_tongyishxydm)
    MaterialEditText mTongYiSHXYDM;

    @BindView(R.id.et_kehumc)
    MaterialEditText mKeHuMC;

    @BindView(R.id.et_lianxir)
    MaterialEditText mLianXiR;

    @BindView(R.id.et_lianxifs)
    MaterialEditText mLianXiFS;

    @BindView(R.id.et_youjidz)
    MaterialEditText mYouJiDZ;

    @BindView(R.id.et_beizhu)
    EditText mBeiZhu;

    @BindView(R.id.btn_sign)
    TextView btnSign;

    @BindView(R.id.btn_preview)
    TextView btnPreview;

    @BindView(R.id.et_zip_code)
    EditText mZipCodeEt;

    private CallListEntity mParam1;

    private String mParam2;

    private Unbinder mUnbinder;

    private boolean isFirstCjdl = true;

    private boolean isFirstCjxl = true;

    private OverrateCallHandleEntity callHandleEntity;

    public CallHandleFragment() {
    }

    public static CallHandleFragment newInstance(CallListEntity param1, String param2) {
        CallHandleFragment fragment = new CallHandleFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        if (getArguments() != null) {
            mParam1 = (CallListEntity) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_handle_new, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initViews(view);
        initData(view);
        return view;
    }

    private void initViews(View view) {

//        mZzddz.setText(mParam1.getYjdz());
//        mZhenQqueHM.setText(mParam1.getKhmc());
//        mXnHuM.setText(mParam1.getKhmc());

        if ("receipt".equals(mParam2)) {
            llXxbg.setVisibility(View.GONE);
        } else {
            llXxbg.setVisibility(View.VISIBLE);
        }

        btnSign.setOnClickListener(this);
        btnPreview.setOnClickListener(this);

        spinnerCjdl.setOnItemSelectedListener(this);
        spinnerCjxl.setOnItemSelectedListener(this);
        spinnerCjqr.setOnItemSelectedListener(this);
        spinnerXxbg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result = parent.getItemAtPosition(position).toString();
//        ToastUtils.showShort(result);
                switch (result) {
                    case "否":
                        constrain.setVisibility(View.GONE);
                        break;
                    case "是":
                        constrain.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setListener();
    }

    private boolean zddzFlag = true;

    private boolean zqhmFlag = true;

    private boolean xhmFlag = true;

    private void setListener() {
        mXnHuM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (!TextUtils.isEmpty(s.toString())) {
                    if (xhmFlag) {
                        xhmFlag = false;
                        mKeHuMC.setText(mXnHuM.getText().toString().trim());
                    } else {
                        xhmFlag = true;
                    }
//                }
            }
        });

        mZhenQqueHM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (!TextUtils.isEmpty(s.toString())) {
                    if (zqhmFlag) {
                        zqhmFlag = false;
                        mKeHuMC.setText(mZhenQqueHM.getText().toString().trim());
                    } else {
                        zqhmFlag = true;
                    }
//                }
            }
        });

        mZzddz.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (!TextUtils.isEmpty(s.toString())) {
                if (zddzFlag) {
                    zddzFlag = false;
                    mYouJiDZ.setText(mZzddz.getText().toString().trim());
                } else {
                    zddzFlag = true;
                }
//                }

            }
        });

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

//                if (!TextUtils.isEmpty(s.toString())) {
//                    "要求变更“账单地址”", "户名不一致，要求更正后支付", "已过户"
                    if ("户名不一致，要求更正后支付".equals(spinnerCjxl.getSelectedItem().toString().trim())) {
                        if (zqhmFlag) {
                            zqhmFlag = false;
                            mZhenQqueHM.setText(mKeHuMC.getText().toString().trim());
                        } else {
                            zqhmFlag = true;
                        }
                    } else if ("已过户".equals(spinnerCjxl.getSelectedItem().toString().trim())) {
                        if (xhmFlag) {
                            xhmFlag = false;
                            mXnHuM.setText(mKeHuMC.getText().toString().trim());
                        } else {
                            xhmFlag = true;
                        }
                    }
//                }
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
                LogUtils.e(s.toString());
                if (!mParam1.getYjdz().contentEquals(s)) {
                    mYouJiDZ.setHelperText(mParam1.getYjdz());
                    mYouJiDZ.setBottomTextSize(ConvertUtils.sp2px(17.0f));
                } else {
                    mYouJiDZ.setHelperText("");
                }

//                if (!TextUtils.isEmpty(s.toString())) {
                if (zddzFlag) {
                    zddzFlag = false;
                    if ("要求变更“账单地址”".equals(spinnerCjxl.getSelectedItem().toString().trim())) {
                        mZzddz.setText(mYouJiDZ.getText().toString().trim());
                    }
                } else {
                    zddzFlag = true;
                }
            }
//            }
        });
    }

    public void initData(View view) {
        String[] spinnerArrCJDL = new String[]{"支付问题", "账单问题", "超定额问题", "用户拒付", "其他"};
        ArrayAdapter<String> cjdlAdapter = new ArrayAdapter<String>(getContext(), R.layout.multiline_spinner_dropdown_item, spinnerArrCJDL);
        cjdlAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCjdl.setAdapter(cjdlAdapter);

        String[] spinnerArrCJXL = new String[]{"客户申请付款流程中", "托收失败", "已支付未销账", "申请“核销”中"};
        ArrayAdapter<String> cjxlAdapter = new ArrayAdapter<String>(getContext(), R.layout.multiline_spinner_dropdown_item, spinnerArrCJXL);
        cjxlAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCjxl.setAdapter(cjxlAdapter);

        String[] spinnerArrCJQR = new String[]{"否", "是"};
        ArrayAdapter<String> cjqrAdapter = new ArrayAdapter<String>(getContext(), R.layout.multiline_spinner_dropdown_item, spinnerArrCJQR);
        cjqrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCjqr.setAdapter(cjqrAdapter);

        String[] spinnerArrXXBG = new String[]{"否", "是"};
        ArrayAdapter<String> xxbgAdapter = new ArrayAdapter<String>(getContext(), R.layout.multiline_spinner_dropdown_item, spinnerArrXXBG);
        xxbgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerXxbg.setAdapter(xxbgAdapter);

        List<OverrateCallHandleEntity> entityList = GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getOverrateCallHandleEntityDao()
                .queryBuilder()
                .where(OverrateCallHandleEntityDao.Properties.Albh.eq(mParam1.getAlbh()))
                .list();
        if (entityList != null && entityList.size() > 0) {
            callHandleEntity = entityList.get(0);

            List<String> cjdlList = Arrays.asList(spinnerArrCJDL);
            spinnerCjdl.setSelection(cjdlList.indexOf(callHandleEntity.getCjdl()));
            List<String> cjxlList = Arrays.asList(spinnerArrCJXL);
            spinnerCjxl.setSelection(!cjxlList.contains(callHandleEntity.getCjxl()) ? 0 : cjxlList.indexOf(callHandleEntity.getCjxl()));
            List<String> cjqrList = Arrays.asList(spinnerArrCJQR);
            spinnerCjqr.setSelection(cjqrList.indexOf(callHandleEntity.getCjqr()));
            List<String> xxbgList = Arrays.asList(spinnerArrXXBG);
            spinnerXxbg.setSelection(xxbgList.indexOf(callHandleEntity.getXxbg()));

            mZzddz.setText(callHandleEntity.getZqzddz());
            mZhenQqueHM.setText(callHandleEntity.getZqhm());
            mXnHuM.setText(callHandleEntity.getXhm());
            mJuFuLY.setText(callHandleEntity.getJfly());
            mQingKuangMS.setText(callHandleEntity.getQkms());
            mNqcjcs.setText(callHandleEntity.getNcqcjcs());
            mCjbz.setText(callHandleEntity.getCjbz());

            mZhangHuBH.setText(callHandleEntity.getYhh() + "/" + callHandleEntity.getZhbh());
            mXingZhengQ.setText(callHandleEntity.getXzq());
            mSheShuiDM.setText(callHandleEntity.getKhlx());
            mTongYiSHXYDM.setText(callHandleEntity.getTyshxydm());
            mKeHuMC.setText(callHandleEntity.getKhmc());
            mLianXiR.setText(callHandleEntity.getLxr());
            mLianXiFS.setText(callHandleEntity.getLxfs());
            mYouJiDZ.setText(callHandleEntity.getYjdz());
            mBeiZhu.setText(callHandleEntity.getBz());
            mZipCodeEt.setText(callHandleEntity.getYoubian());
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
            mZipCodeEt.setText(mParam1.getYoubian());
        }
    }

    public OverrateCallHandleEntity getCallHandleEntity() {
        return callHandleEntity;
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
            case R.id.btn_sign:
                getSignPicture(mParam1.getAlbh(), Constant.TASK_TYPE_DOWNLOAD, TaskState.HANDLE);
                break;
            case R.id.btn_preview:
                Intent previewIntent = new Intent(ActivityUtils.getTopActivity(), SignatureNameNewActivity.class);
                previewIntent.putExtra("type", "preview");
                previewIntent.putExtra(Constant.TASK_ID, mParam1.getAlbh());
                previewIntent.putExtra(Constant.TASK_TYPE, Constant.TASK_TYPE_DOWNLOAD);
                previewIntent.putExtra(Constant.TASK_STATE, TaskState.HANDLE);
                startActivity(previewIntent);
                break;
            default:
                break;
        }
    }

    private void getSignPicture(String taskId, int taskType, int taskState) {
        mDataManager.getMediaList(taskId, taskType,
                taskState, DUMedia.FILE_TYPE_SIGNUP)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DUMedia>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<DUMedia> duMediaList) {
                        if (duMediaList != null && duMediaList.size() > 0) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityUtils.getTopActivity());
                            builder.setTitle("提示");
                            builder.setMessage("已经存在签名文件，是否需要删除重新签名？");
                            builder.setNegativeButton("否", null);
                            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent signIntent = new Intent(ActivityUtils.getTopActivity(), SignatureNameNewActivity.class);
                                    signIntent.putExtra("type", "sign");
                                    signIntent.putExtra(Constant.TASK_ID, mParam1.getAlbh());
                                    signIntent.putExtra(Constant.TASK_TYPE, Constant.TASK_TYPE_DOWNLOAD);
                                    signIntent.putExtra(Constant.TASK_STATE, TaskState.HANDLE);
                                    startActivity(signIntent);
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        } else {
                            Intent signIntent = new Intent(ActivityUtils.getTopActivity(), SignatureNameNewActivity.class);
                            signIntent.putExtra("type", "sign");
                            signIntent.putExtra(Constant.TASK_ID, mParam1.getAlbh());
                            signIntent.putExtra(Constant.TASK_TYPE, Constant.TASK_TYPE_DOWNLOAD);
                            signIntent.putExtra(Constant.TASK_STATE, TaskState.HANDLE);
                            startActivity(signIntent);
                        }
                    }
                });
    }

    public File getImagePath() {
        return new File(Environment.getExternalStorageDirectory(), "sh3h/hotline/images");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String result = parent.getItemAtPosition(position).toString();
//        ToastUtils.showShort(result);
        switch (result) {
            case "支付问题":
                String[] spinnerArrCJXL = new String[]{"客户申请付款流程中", "托收失败", "已支付未销账", "申请“核销”中"};
                ArrayAdapter<String> cjxlAdapter = new ArrayAdapter<String>(getContext(), R.layout.multiline_spinner_dropdown_item, spinnerArrCJXL);
                cjxlAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCjxl.setAdapter(cjxlAdapter);

                if (callHandleEntity != null) {
                    List<String> cjxlList = Arrays.asList(spinnerArrCJXL);
                    spinnerCjxl.setSelection(!cjxlList.contains(callHandleEntity.getCjxl()) ? 0 : cjxlList.indexOf(callHandleEntity.getCjxl()));
                }

                llCjxl.setVisibility(View.VISIBLE);
                llJfly.setVisibility(View.GONE);
                llQkms.setVisibility(View.GONE);

                llZqzddz.setVisibility(View.GONE);
                llZqhm.setVisibility(View.GONE);
                llXhm.setVisibility(View.GONE);

                if (isFirstCjdl) {
                    isFirstCjdl = false;
                } else {
                    mNqcjcs.setText("");
                    mCjbz.setText("");

                    mJuFuLY.setText("");
                    mQingKuangMS.setText("");
                    mZzddz.setText(mParam1.getYjdz());
                    mXnHuM.setText(mParam1.getKhmc());

                    mZhenQqueHM.setText(mParam1.getKhmc());

                    spinnerXxbg.setEnabled(true);
                    spinnerXxbg.setSelection(0);
                }
                break;
            case "账单问题":
                String[] spinnerArrCJXL1 = new String[]{"要求补寄账单后支付", "未收到账单", "要求变更“账单地址”", "户名不一致，要求更正后支付", "已过户"};
                ArrayAdapter<String> cjxlAdapter1 = new ArrayAdapter<String>(getContext(), R.layout.multiline_spinner_dropdown_item, spinnerArrCJXL1);
                cjxlAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCjxl.setAdapter(cjxlAdapter1);

                llCjxl.setVisibility(View.VISIBLE);
                llJfly.setVisibility(View.GONE);
                llQkms.setVisibility(View.GONE);

                if (isFirstCjdl) {
                    isFirstCjdl = false;
                    if (callHandleEntity != null) {
                        List<String> cjxlList1 = Arrays.asList(spinnerArrCJXL1);
                        spinnerCjxl.setSelection(!cjxlList1.contains(callHandleEntity.getCjxl()) ? 0 : cjxlList1.indexOf(callHandleEntity.getCjxl()));
                    }
                } else {
                    mNqcjcs.setText("");
                    mCjbz.setText("");

                    mJuFuLY.setText("");
                    mQingKuangMS.setText("");
                    mZzddz.setText(mParam1.getYjdz());
                    mXnHuM.setText(mParam1.getKhmc());

                    mZhenQqueHM.setText(mParam1.getKhmc());

                    spinnerXxbg.setEnabled(true);
                    spinnerXxbg.setSelection(0);
                }
                break;
            case "超定额问题":
                String[] spinnerArrCJXL2 = new String[]{"用户对核定定额不认可", "漏水引起超定额，用户不认可", "抄表质量引起超定额，用户不认可", "户表关系不正确，用户不认可", "其他"};
                ArrayAdapter<String> cjxlAdapter2 = new ArrayAdapter<String>(getContext(), R.layout.multiline_spinner_dropdown_item, spinnerArrCJXL2);
                cjxlAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCjxl.setAdapter(cjxlAdapter2);

                if (callHandleEntity != null) {
                    List<String> cjxlList2 = Arrays.asList(spinnerArrCJXL2);
                    spinnerCjxl.setSelection(!cjxlList2.contains(callHandleEntity.getCjxl()) ? 0 : cjxlList2.indexOf(callHandleEntity.getCjxl()));
                }

                llCjxl.setVisibility(View.VISIBLE);
                llJfly.setVisibility(View.GONE);
                llQkms.setVisibility(View.VISIBLE);

                if (isFirstCjdl) {
                    isFirstCjdl = false;
                } else {
                    mNqcjcs.setText("");
                    mCjbz.setText("");

                    mJuFuLY.setText("");
                    mZzddz.setText(mParam1.getYjdz());
                    mXnHuM.setText(mParam1.getKhmc());

                    mZhenQqueHM.setText(mParam1.getKhmc());

                    spinnerXxbg.setEnabled(true);
                    spinnerXxbg.setSelection(0);
                }
                break;
            case "用户拒付":
                llCjxl.setVisibility(View.GONE);
                llJfly.setVisibility(View.VISIBLE);
                llQkms.setVisibility(View.GONE);

                llZqzddz.setVisibility(View.GONE);
                llZqhm.setVisibility(View.GONE);
                llXhm.setVisibility(View.GONE);

                if (isFirstCjdl) {
                    isFirstCjdl = false;
                } else {
                    mNqcjcs.setText("");
                    mCjbz.setText("");

                    mQingKuangMS.setText("");
                    mZzddz.setText(mParam1.getYjdz());
                    mXnHuM.setText(mParam1.getKhmc());

                    mZhenQqueHM.setText(mParam1.getKhmc());

                    spinnerXxbg.setEnabled(true);
                    spinnerXxbg.setSelection(0);
                }
                break;
            case "其他":
                if (!"超定额问题".equals(spinnerCjdl.getSelectedItem().toString().trim())) {
                    llCjxl.setVisibility(View.GONE);
                    llJfly.setVisibility(View.GONE);
                    llQkms.setVisibility(View.VISIBLE);

                    if (isFirstCjxl) {
                        isFirstCjxl = false;
                    } else {
                        mNqcjcs.setText("");
                        mCjbz.setText("");

                        mQingKuangMS.setText("");
                        mJuFuLY.setText("");
                        mZzddz.setText(mParam1.getYjdz());
                        mXnHuM.setText(mParam1.getKhmc());

                        mZhenQqueHM.setText(mParam1.getKhmc());

                        spinnerXxbg.setEnabled(true);
                        spinnerXxbg.setSelection(0);
                    }
                } else {
                    llCjxl.setVisibility(View.VISIBLE);
                    llJfly.setVisibility(View.GONE);
                    llQkms.setVisibility(View.VISIBLE);

                    if (isFirstCjdl) {
                        isFirstCjdl = false;
                    } else {
                        mNqcjcs.setText("");
                        mCjbz.setText("");

                        mQingKuangMS.setText("");
                        mJuFuLY.setText("");
                        mZzddz.setText(mParam1.getYjdz());
                        mXnHuM.setText(mParam1.getKhmc());

                        mZhenQqueHM.setText(mParam1.getKhmc());

                        spinnerXxbg.setEnabled(true);
                        spinnerXxbg.setSelection(0);
                    }
                }
                llZqzddz.setVisibility(View.GONE);
                llZqhm.setVisibility(View.GONE);
                llXhm.setVisibility(View.GONE);

                break;
            case "客户申请付款流程中":
            case "托收失败":
            case "已支付未销账":
            case "申请“核销”中":
            case "要求补寄账单后支付":
            case "未收到账单":
                llCjxl.setVisibility(View.VISIBLE);
                llJfly.setVisibility(View.GONE);
                llQkms.setVisibility(View.GONE);
                llZqzddz.setVisibility(View.GONE);
                llZqhm.setVisibility(View.GONE);
                llXhm.setVisibility(View.GONE);

                if (isFirstCjxl) {
                    isFirstCjxl = false;
                } else {
                    mNqcjcs.setText("");
                    mCjbz.setText("");

                    mJuFuLY.setText("");
                    mQingKuangMS.setText("");
                    mZzddz.setText(mParam1.getYjdz());
                    mXnHuM.setText(mParam1.getKhmc());

                    mZhenQqueHM.setText(mParam1.getKhmc());

                    spinnerXxbg.setEnabled(true);
                    spinnerXxbg.setSelection(0);
                }
                break;
            case "用户对核定定额不认可":
            case "漏水引起超定额，用户不认可":
            case "抄表质量引起超定额，用户不认可":
            case "户表关系不正确，用户不认可":
                llCjxl.setVisibility(View.VISIBLE);
                llJfly.setVisibility(View.GONE);
                llQkms.setVisibility(View.VISIBLE);
                llZqzddz.setVisibility(View.GONE);
                llZqhm.setVisibility(View.GONE);
                llXhm.setVisibility(View.GONE);

                if (isFirstCjxl) {
                    isFirstCjxl = false;
                } else {
                    mQingKuangMS.setText("");
                    mNqcjcs.setText("");
                    mCjbz.setText("");

                    mJuFuLY.setText("");
                    mZzddz.setText(mParam1.getYjdz());
                    mXnHuM.setText(mParam1.getKhmc());

                    mZhenQqueHM.setText(mParam1.getKhmc());

                    spinnerXxbg.setEnabled(true);
                    spinnerXxbg.setSelection(0);
                }
                break;
            case "要求变更“账单地址”":
                llCjxl.setVisibility(View.VISIBLE);
                llJfly.setVisibility(View.GONE);
                llQkms.setVisibility(View.GONE);
                llZqzddz.setVisibility(View.VISIBLE);
                llZqhm.setVisibility(View.GONE);
                llXhm.setVisibility(View.GONE);

                if (isFirstCjxl) {
                    isFirstCjxl = false;
                    if (callHandleEntity != null) {
                        mZzddz.setText(callHandleEntity.getYjdz());
                    }
                } else {
                    mNqcjcs.setText("");
                    mCjbz.setText("");

                    mJuFuLY.setText("");
                    mQingKuangMS.setText("");
                    mZhenQqueHM.setText(mParam1.getKhmc());
                    mXnHuM.setText(mParam1.getKhmc());

                    mZzddz.setText(mParam1.getYjdz());
                }

                spinnerXxbg.setEnabled(false);
                spinnerXxbg.setSelection(1);
                break;
            case "户名不一致，要求更正后支付":
                llCjxl.setVisibility(View.VISIBLE);
                llJfly.setVisibility(View.GONE);
                llQkms.setVisibility(View.GONE);
                llZqzddz.setVisibility(View.GONE);
                llZqhm.setVisibility(View.VISIBLE);
                llXhm.setVisibility(View.GONE);

                if (isFirstCjxl) {
                    isFirstCjxl = false;
                    if (callHandleEntity != null) {
                        mZhenQqueHM.setText(callHandleEntity.getKhmc());
                    }
                } else {
                    mNqcjcs.setText("");
                    mCjbz.setText("");

                    mJuFuLY.setText("");
                    mQingKuangMS.setText("");
                    mZzddz.setText(mParam1.getYjdz());
                    mXnHuM.setText(mParam1.getKhmc());

                    mZhenQqueHM.setText(mParam1.getKhmc());
                }

                spinnerXxbg.setEnabled(false);
                spinnerXxbg.setSelection(1);
                break;
            case "已过户":
                llCjxl.setVisibility(View.VISIBLE);
                llJfly.setVisibility(View.GONE);
                llQkms.setVisibility(View.GONE);
                llZqzddz.setVisibility(View.GONE);
                llZqhm.setVisibility(View.GONE);
                llXhm.setVisibility(View.VISIBLE);

                if (isFirstCjxl) {
                    isFirstCjxl = false;
                    if (callHandleEntity != null) {
                        mXnHuM.setText(callHandleEntity.getKhmc());
                    }
                } else {
                    mNqcjcs.setText("");
                    mCjbz.setText("");

                    mJuFuLY.setText("");
                    mQingKuangMS.setText("");
                    mZzddz.setText(mParam1.getYjdz());
                    mZhenQqueHM.setText(mParam1.getKhmc());

                    mXnHuM.setText(mParam1.getKhmc());
                }

                spinnerXxbg.setEnabled(false);
                spinnerXxbg.setSelection(1);
                break;
            case "是":
                llSign.setVisibility(View.VISIBLE);
                break;
            case "否":
                llSign.setVisibility(View.GONE);
                break;
            default:
                spinnerXxbg.setEnabled(true);
                spinnerXxbg.setSelection(0);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
