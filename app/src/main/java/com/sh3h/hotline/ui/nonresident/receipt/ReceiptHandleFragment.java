package com.sh3h.hotline.ui.nonresident.receipt;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
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
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
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
import com.sh3h.hotline.entity.ReceiptListEntity;
import com.sh3h.hotline.event.CallTransferDataEvent;
import com.sh3h.hotline.ui.base.BaseActivity;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.sh3h.hotline.ui.nonresident.call.OverrateCallHandleActivity;
import com.sh3h.hotline.ui.nonresident.sign.SignatureNameNewActivity;
import com.sh3h.hotline.ui.order.TaskState;
import com.sh3h.hotline.util.ConvertUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bertsir.zbar.QrConfig;
import cn.bertsir.zbar.QrManager;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_PICTURE;
import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_PICTURE_SCAN;
import static com.sh3h.dataprovider.data.entity.DUMedia.UPLOAD_FLAG_LOCAL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReceiptHandleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReceiptHandleFragment extends ParentFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

  private static final String TAG = "MultimediaFileFragment";
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";
  private static final int CAPTURE_IMAGE = 1000;
  private static final int CAPTURE_ALBUM = 1001;

  @Inject
  DataManager mDataManager;

  @Inject
  Bus mBus;

  @BindView(R.id.et_paisongfs)
  EditText mPaiSongFS;

  @BindView(R.id.spinner_psfs)
  public Spinner mSpinnerPSFS;

  @BindView(R.id.et_guahaoxdh)
  public EditText mGuaHaoXDH;

  @BindView(R.id.et_shifousd)
  EditText mShiFouSD;

  @BindView(R.id.spinner_sfsd)
  public Spinner spinnerSfsd;

  @BindView(R.id.et_weiqiansyy)
  EditText mWeiQianSYY;

  @BindView(R.id.spinner_wqsyy)
  public Spinner spinnerWqsyy;

  @BindView(R.id.et_qianshour)
  EditText etQianShouR;

  @BindView(R.id.spinner_qsr)
  public Spinner spinnerQsr;

  @BindView(R.id.et_shoujianrsf)
  public EditText mShouJianRSF;

  @BindView(R.id.et_fadanqkbz)
  public EditText mFaDanQKBZ;

  @BindView(R.id.et_xinxibg)
  EditText mXinXiBG;

  @BindView(R.id.et_zip_code)
  EditText mZipCodeEt;

  @BindView(R.id.spinner_xxbg)
  public Spinner spinnerXxbg;

  @BindView(R.id.ll_ghxdh)
  LinearLayout llGhxdh;

  @BindView(R.id.ll_smps)
  LinearLayout llSmps;

  @BindView(R.id.ll_sfsd)
  LinearLayout llSfsd;

  @BindView(R.id.ll_wqsyy)
  LinearLayout llWqsyy;

  @BindView(R.id.ll_qsr)
  LinearLayout llQsr;

  @BindView(R.id.ll_sjrsf)
  LinearLayout llSjrsf;

  @BindView(R.id.ll_smcj)
  LinearLayout llSmcj;

  @BindView(R.id.constriant)
  LinearLayout constrain;


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

  @BindView(R.id.btn_scan)
  TextView btnScan;

  @BindView(R.id.btn_photo)
  TextView btnPhoto;

  @BindView(R.id.btn_sign)
  TextView btnSign;

  @BindView(R.id.btn_preview)
  TextView btnPreview;

  @BindView(R.id.btn_shangmencj)
  TextView btnShangMenCJ;

  @BindView(R.id.tv_qianfeije)
  TextView mQianFeiJE;

  private ReceiptListEntity mParam1;
  private String mParam2;

  private Unbinder mUnbinder;

  private ReceiptListEntity receiptListEntity;

  private String mImageName;

  protected CompositeSubscription mSubscription;

  private OverrateCallHandleEntity handleEntity;

  public OverrateReceiptHandleEntity receiptHandleEntity;

  private boolean isFirstPSFS = true;
  private boolean isFirstSfsd = true;
  private boolean isFirstWqsyy = true;
  private boolean isFirstQsr = true;

  private int qsrPosition = -1;

  public ReceiptHandleFragment() {
  }

  public static ReceiptHandleFragment newInstance(ReceiptListEntity param1, String param2) {
    ReceiptHandleFragment fragment = new ReceiptHandleFragment();
    Bundle args = new Bundle();
    args.putSerializable(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    mSubscription = new CompositeSubscription();
  }

  @Override
  public void onDetach() {
    super.onDetach();
    if (mSubscription != null) {
      mSubscription.unsubscribe();
      mSubscription = null;
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ((BaseActivity) getActivity()).getActivityComponent().inject(this);
    mBus.register(this);
    if (getArguments() != null) {
      mParam1 = (ReceiptListEntity) getArguments().getSerializable(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_receipt_handle_new, container, false);
    mUnbinder = ButterKnife.bind(this, view);
    initViews(view);
    initData(view);
    return view;
  }

  private void initViews(View view) {
    btnScan.setOnClickListener(this);
    btnPhoto.setOnClickListener(this);
    btnSign.setOnClickListener(this);
    btnPreview.setOnClickListener(this);
    btnShangMenCJ.setOnClickListener(this);

    if (ConvertUtil.convertToDouble(mParam1.getQfje(), 0) > 0) {
      llSmcj.setVisibility(View.VISIBLE);
    } else {
      llSmcj.setVisibility(View.GONE);
    }

    mSpinnerPSFS.setOnItemSelectedListener(this);
    spinnerSfsd.setOnItemSelectedListener(this);
    spinnerWqsyy.setOnItemSelectedListener(this);
    spinnerQsr.setOnItemSelectedListener(this);
    spinnerXxbg.setOnItemSelectedListener(this);
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

  public void initData(View view) {

    String[] spinnerArrPSFS = new String[]{"上门派送", "寄挂号信"};
    ArrayAdapter<String> fylbAdapter = new ArrayAdapter<String>(getContext(), R.layout.multiline_spinner_dropdown_item, spinnerArrPSFS);
    fylbAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mSpinnerPSFS.setAdapter(fylbAdapter);

    String[] spinnerArrSFSD = new String[]{"送达", "未送达"};
    ArrayAdapter<String> sfsdAdapter = new ArrayAdapter<String>(getContext(), R.layout.multiline_spinner_dropdown_item, spinnerArrSFSD);
    sfsdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinnerSfsd.setAdapter(sfsdAdapter);

    String[] spinnerArrWQSYY = new String[]{"上门无人", "动迁", "查无此地址", "查无此户名", "用户拒收", "其他"};
    ArrayAdapter<String> wqsyyAdapter = new ArrayAdapter<String>(getContext(), R.layout.multiline_spinner_dropdown_item, spinnerArrWQSYY);
    wqsyyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinnerWqsyy.setAdapter(wqsyyAdapter);

    String[] spinnerArrQSR = new String[]{"", "经理或主管", "财务", "门卫保安", "物业", "工程部", "前台接待", "后勤保障", "其他"};
    ArrayAdapter<String> qsrAdapter = new ArrayAdapter<String>(getContext(), R.layout.multiline_spinner_dropdown_item, spinnerArrQSR);
    qsrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinnerQsr.setAdapter(qsrAdapter);
    spinnerQsr.setSelection(qsrPosition);

    String[] spinnerArrXXBG = new String[]{"否", "是"};
    ArrayAdapter<String> xxbgAdapter = new ArrayAdapter<String>(getContext(), R.layout.multiline_spinner_dropdown_item, spinnerArrXXBG);
    xxbgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinnerXxbg.setAdapter(xxbgAdapter);

    List<OverrateReceiptHandleEntity> entityList = GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getOverrateReceiptHandleEntityDao()
      .queryBuilder()
      .where(OverrateReceiptHandleEntityDao.Properties.Albh.eq(mParam1.getAlbh()))
      .list();
    if (entityList != null && entityList.size() > 0) {
      receiptHandleEntity = entityList.get(0);

      mQianFeiJE.setText("￥" + mParam1.getQfje());

      List<String> psfsList = Arrays.asList(spinnerArrPSFS);
      mSpinnerPSFS.setSelection(psfsList.indexOf(receiptHandleEntity.getPsfs()));
      List<String> sfsdList = Arrays.asList(spinnerArrSFSD);
      spinnerSfsd.setSelection(sfsdList.indexOf(receiptHandleEntity.getSfsd()));
      List<String> wqsyyList = Arrays.asList(spinnerArrWQSYY);
      spinnerWqsyy.setSelection(wqsyyList.indexOf(receiptHandleEntity.getWqsyy()));
      List<String> qsrList = Arrays.asList(spinnerArrQSR);
      if (StringUtils.isEmpty(receiptHandleEntity.getQsr())) {
        qsrPosition = -1;
        spinnerQsr.setSelection(qsrPosition);
      } else {
        qsrPosition = qsrList.indexOf(receiptHandleEntity.getQsr());
        spinnerQsr.setSelection(qsrPosition);
      }
      LogUtils.e(receiptHandleEntity.getQsr());

      mZhangHuBH.setText(receiptHandleEntity.getYhh() + "/" + receiptHandleEntity.getZhbh());
      mXingZhengQ.setText(receiptHandleEntity.getXzq());
      mSheShuiDM.setText(receiptHandleEntity.getKhlx());
      mTongYiSHXYDM.setText(receiptHandleEntity.getTyshxydm());
      mKeHuMC.setText(receiptHandleEntity.getKhmc());
      mLianXiR.setText(receiptHandleEntity.getLxr());
      mLianXiFS.setText(receiptHandleEntity.getLxfs());
      mYouJiDZ.setText(receiptHandleEntity.getYjdz());
      mBeiZhu.setText(receiptHandleEntity.getBz());

      mGuaHaoXDH.setText(receiptHandleEntity.getGhxdh());
      mShouJianRSF.setText(receiptHandleEntity.getSjrsf());
      mFaDanQKBZ.setText(receiptHandleEntity.getFdqkbz());
      mZipCodeEt.setText(receiptHandleEntity.getYoubian());
      LogUtils.e(receiptHandleEntity.getWqsyy());

      List<OverrateCallHandleEntity> callEntityList = GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getOverrateCallHandleEntityDao()
        .queryBuilder()
        .where(OverrateCallHandleEntityDao.Properties.Albh.eq(mParam1.getAlbh()))
        .list();
      if (callEntityList != null && callEntityList.size() > 0) {
        OverrateCallHandleEntity callHandleEntity = callEntityList.get(0);
        if (callHandleEntity != null && ("要求变更“账单地址”".equals(callHandleEntity.getCjxl())
          || "户名不一致，要求更正后支付".equals(callHandleEntity.getCjxl())
          || "已过户".equals(callHandleEntity.getCjxl()))) {
          if ("是".equals(callHandleEntity.getIsCuiJiao())) {
            spinnerXxbg.setEnabled(false);
            spinnerXxbg.setSelection(1);
          } else {
            List<String> xxbgList = Arrays.asList(spinnerArrXXBG);
            spinnerXxbg.setSelection(xxbgList.indexOf(receiptHandleEntity.getXxbg()));
            spinnerXxbg.setEnabled(true);
          }
        } else {
          List<String> xxbgList = Arrays.asList(spinnerArrXXBG);
          spinnerXxbg.setSelection(xxbgList.indexOf(receiptHandleEntity.getXxbg()));
          spinnerXxbg.setEnabled(true);
        }
      } else {
        List<String> xxbgList = Arrays.asList(spinnerArrXXBG);
        spinnerXxbg.setSelection(xxbgList.indexOf(receiptHandleEntity.getXxbg()));
        spinnerXxbg.setEnabled(true);
      }
    } else {
      mQianFeiJE.setText("￥" + mParam1.getQfje());

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

  public String getSpinnerQsr() {
    LogUtils.e(qsrPosition);
    String qsr = "";
    if (qsrPosition == -1) {
      qsr = "";
    } else {
      if (spinnerQsr.getSelectedItem() == null) {
        qsr = "";
      } else {
        qsr = spinnerQsr.getSelectedItem().toString().trim();
      }
    }
    return qsr;
  }

  @Override
  protected void lazyLoad() {

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if ((CAPTURE_IMAGE == requestCode)
//                && (data != null)
      && (mImageName != null)) {
      File folder = getImagePath();
      File file = new File(folder, mImageName);
      if (!file.exists()) {
        LogUtil.e(TAG, String.format("%s isn't existing", mImageName));
        return;
      }
      //保存照片-压缩
      saveNewImage(mParam1.getAlbh(), Constant.TASK_TYPE_DOWNLOAD, TaskState.HANDLE, mImageName, file, false, null);
    } else if (requestCode == CAPTURE_ALBUM
      && data != null) {
      Uri originalUri = data.getData();
      LogUtil.i(TAG, "save albums");
      ContentResolver resolver = getActivity().getContentResolver();
      try {
        Bitmap bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
        File file = new File(getImagePath(), mImageName);
        //保存照片-压缩
        saveNewImage(mParam1.getAlbh(), Constant.TASK_TYPE_DOWNLOAD, TaskState.HANDLE, mImageName, file, false, null);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else if (requestCode == 2010 && resultCode == Activity.RESULT_OK) {
      LogUtils.e("CallHandle");
      if (data != null) {
        handleEntity = (OverrateCallHandleEntity) data.getSerializableExtra("CallHandle");
        if (handleEntity != null && ("要求变更“账单地址”".equals(handleEntity.getCjxl())
          || "户名不一致，要求更正后支付".equals(handleEntity.getCjxl())
          || "已过户".equals(handleEntity.getCjxl()))) {
          spinnerXxbg.setEnabled(false);
          spinnerXxbg.setSelection(1);

          if (!StringUtils.isEmpty(handleEntity.getZqzddz())) {
            mYouJiDZ.setText(handleEntity.getZqzddz());
          } else {
            mYouJiDZ.setText(mParam1.getYjdz());
          }

          if (!StringUtils.isEmpty(handleEntity.getZqhm())) {
            mKeHuMC.setText(handleEntity.getZqhm());
          } else {
            if (!StringUtils.isEmpty(handleEntity.getXhm())) {
              mKeHuMC.setText(handleEntity.getXhm());
            } else {
              mKeHuMC.setText(mParam1.getKhmc());
            }
          }
          mZipCodeEt.setText(mParam1.getYoubian());

        } else {
          spinnerXxbg.setEnabled(true);
        }
      }
    }
  }

  /**
   * 保存新照片-(压缩照片)
   *
   * @param taskId
   * @param taskType
   * @param taskState
   * @param fileName
   * @param filePath
   */
  public void saveNewImage(final String taskId, final int taskType, final int taskState,
                           final String fileName, File filePath, boolean isFromAlbum, Bitmap bitmap) {
    if (/*TextUtil.isNullOrEmpty(taskId)
                || */TextUtil.isNullOrEmpty(fileName)
      || (filePath == null)) {
      ToastUtils.showShort("param is null");
      LogUtil.e(TAG, "param is null");
      return;
    }

    if (isFromAlbum && bitmap != null) {//来自本地相册
      if (!saveJPG(bitmap, filePath.getPath())) {
        LogUtil.i(TAG, "save image failed!");
        ToastUtils.showShort("保存图片失败!");
        return;
      }
    }

    LogUtil.i(TAG, "saveNewImage");
    mSubscription.add(mDataManager.compressImageAndAddStamp(fileName, filePath)
      .concatMap(new Func1<Boolean, Observable<Boolean>>() {
        @Override
        public Observable<Boolean> call(Boolean aBoolean) {
          DUMedia duMedia = new DUMedia(
            -1,
            mDataManager.getUserId(),
            taskId,
            taskType,
            taskState,
            FILE_TYPE_PICTURE_SCAN,
            fileName,
            null,
            null,
            UPLOAD_FLAG_LOCAL,
            null,
            0);

          return mDataManager.saveMedia(duMedia);
        }
      })
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeOn(Schedulers.io())
      .subscribe(new Subscriber<Boolean>() {
        @Override
        public void onCompleted() {
          LogUtil.i(TAG, "saveNewImage onCompleted");
        }

        @Override
        public void onError(Throwable e) {
          LogUtil.e(TAG, "saveNewImage onError " + e.getMessage());
          if (!TextUtil.isNullOrEmpty(e.getMessage())) {
            ToastUtils.showShort(e.getMessage());
          }
        }

        @Override
        public void onNext(Boolean aBoolean) {
          LogUtil.i(TAG, "saveNewImage onNext " + aBoolean);
          if (aBoolean) {
            ToastUtils.showShort(R.string.text_save_success);
          } else {
            ToastUtils.showShort(R.string.text_save_fail);
          }
        }
      }));
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mUnbinder.unbind();
    mBus.unregister(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_scan:
        // startActivity(new Intent(ActivityUtils.getTopActivity(), ScanBarCodeActivity.class));
        scan();
        break;
      case R.id.btn_photo:
        takePhoto(false);
        break;
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
      case R.id.btn_shangmencj:
        Intent intent = new Intent(ActivityUtils.getTopActivity(), OverrateCallHandleActivity.class);
        CallListEntity callListEntity = convertEntity(mParam1);
        intent.putExtra("call", callListEntity);
        intent.putExtra("origin", "receipt");
        startActivityForResult(intent, 2010);
        break;
      default:
        break;
    }

  }

  /**
   * 拍照/选取相册
   */
  private void takePhoto(boolean isFromAlbums) {

    try {
      File folder = getImagePath();
      if (!folder.exists()) {
        folder.mkdirs();
      }

      mImageName = "PIC_" + mParam1.getAlbh() + "_" + System.currentTimeMillis() + ".jpg";
      if (!isFromAlbums) {
        File file = new File(folder, mImageName);
        Uri uri = null;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {//7.0以上
          uri = Uri.fromFile(file);
        } else {
          if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            uri = FileProvider.getUriForFile(getContext(), "com.sh3h.hotline.fileprovider", file);
          }
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CAPTURE_IMAGE);
      } else {
        //相册
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, CAPTURE_ALBUM);
      }
    } catch (Exception e) {
      e.printStackTrace();
      LogUtil.e(TAG, e.getMessage());
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
//                        if (duMediaList == null || duMediaList.size() != 1
//                            /*&& duMediaList.size() > 0*/) {
//                            return;
//                        }
//                        for (DUMedia duMedia : duMediaList) {
//                            String fileName = duMedia.getFileName();
//                            if (TextUtil.isNullOrEmpty(fileName)) {
//                                continue;
//                            }
//
//                            File file = new File(getImagePath(), fileName);
//                            if (file.exists()) {
//                                duMedia.setFilePath(file.getPath());
//                            }
//                        }

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

  public OverrateCallHandleEntity getHandleEntity() {
    return handleEntity;
  }

  public OverrateReceiptHandleEntity getReceiptHandleEntity() {
    return receiptHandleEntity;
  }

  private CallListEntity convertEntity(ReceiptListEntity mParam1) {
    CallListEntity entity = new CallListEntity();
    entity.setPch(mParam1.getPch());
    entity.setFfrq("");
    entity.setFfje("");
    entity.setDz(mParam1.getDz());
    entity.setDkbj("");
    entity.setCjr(mDataManager.getAccount());
    entity.setCbh("");
    entity.setBz(mParam1.getBz());
    entity.setFjzdxq("");
    entity.setFylb(mParam1.getFylb());
    entity.setFynr(mParam1.getFynr());
    entity.setKhmc(mParam1.getKhmc());
    entity.setKzcm("");
    entity.setKzrq("");
    entity.setKzsl("");
    entity.setLxdh(mParam1.getLxdh());
    entity.setLxfs(mParam1.getLxfs());
    entity.setLxr(mParam1.getLxr());
    entity.setPdsj(mParam1.getSlsj());
    entity.setQfkl("");
    entity.setQfzje(mParam1.getQfje());
    entity.setSffj("");
    entity.setSfwc("");
    entity.setSfyq("");
    entity.setTyshxydm(mParam1.getTyshxydm());
    entity.setAlbh(mParam1.getAlbh());
    entity.setTsbj("");
    entity.setXh(mParam1.getXh());
    entity.setXzq(mParam1.getXzq());
    entity.setYjdz(mParam1.getYjdz());
    entity.setZhbh(mParam1.getZhbh());
    entity.setSsdm(mParam1.getKhlx());
    entity.setYhh(mParam1.getYhh());
    entity.setXh(mParam1.getXh());
    entity.setAlbh(mParam1.getAlbh());
    entity.setKhlx(mParam1.getKhlx());
    entity.setCjqx(mParam1.getCqsx());

    return entity;
  }

  private void scan() {
    //扫码
    QrConfig qrConfig = new QrConfig.Builder()
      .setDesText("(识别二维码)")//扫描框下文字
      .setShowDes(false)//是否显示扫描框下面文字
      .setShowLight(true)//显示手电筒按钮
      .setShowTitle(false)//显示Title
      .setShowAlbum(true)//显示从相册选择按钮
      .setCornerColor(Color.WHITE)//设置扫描框颜色
      .setLineColor(Color.WHITE)//设置扫描线颜色
      .setLineSpeed(QrConfig.LINE_MEDIUM)//设置扫描线速度
      .setScanType(QrConfig.TYPE_ALL)//设置扫码类型（二维码，条形码，全部，自定义，默认为二维码）
      .setScanViewType(QrConfig.TYPE_BARCODE)//设置扫描框类型（二维码还是条形码，默认为二维码）
      .setCustombarcodeformat(QrConfig.BARCODE_I25)//此项只有在扫码类型为TYPE_CUSTOM时才有效
      .setPlaySound(true)//是否扫描成功后bi~的声音
      .setIsOnlyCenter(true)//是否只识别框中内容(默认为全屏识别)
      .setTitleText("扫描二维码")//设置Tilte文字
      .setTitleBackgroudColor(Color.BLUE)//设置状态栏颜色
      .setTitleTextColor(Color.BLACK)//设置Title文字颜色
      .create();
    QrManager.getInstance().init(qrConfig).startScan(ActivityUtils.getTopActivity(), new QrManager.OnScanResultCallback() {
      @Override
      public void onScanSuccess(String result) {
//                ToastUtils.showShort(result);
        mGuaHaoXDH.setText(result);
      }
    }, "");
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    String result = parent.getItemAtPosition(position).toString();
//        ToastUtils.showShort(result);
    LogUtils.e(result);
    List<OverrateCallHandleEntity> callEntityList = GreenDaoUtils.getDaoSession(MainApplication.getInstance()).getOverrateCallHandleEntityDao()
      .queryBuilder()
      .where(OverrateCallHandleEntityDao.Properties.Albh.eq(mParam1.getAlbh()))
      .list();
    switch (result) {
      case "上门派送":
        llGhxdh.setVisibility(View.GONE);
        llSmps.setVisibility(View.VISIBLE);

        mGuaHaoXDH.setText("");
        btnShangMenCJ.setEnabled(true);
        btnShangMenCJ.setBackgroundResource(R.drawable.btn_receipt_blue_shape);

        setXxbg(callEntityList);

        if (isFirstPSFS) {
          isFirstPSFS = false;
        } else {
          mFaDanQKBZ.setText("");
        }

        break;
      case "寄挂号信":
        llGhxdh.setVisibility(View.VISIBLE);
        llSmps.setVisibility(View.GONE);

        btnShangMenCJ.setEnabled(false);
        btnShangMenCJ.setBackgroundResource(R.drawable.btn_receipt_gray_shape);
        spinnerXxbg.setSelection(0);
        spinnerXxbg.setEnabled(false);

        if (isFirstPSFS) {
          isFirstPSFS = false;
        } else {
          mFaDanQKBZ.setText("");
        }

        break;
      case "送达":
        llWqsyy.setVisibility(View.GONE);
        llQsr.setVisibility(View.VISIBLE);
        llSjrsf.setVisibility(View.VISIBLE);
        if ("其他".equals(spinnerQsr.getSelectedItem().toString())) {
          llSjrsf.setVisibility(View.VISIBLE);
        } else {
          llSjrsf.setVisibility(View.GONE);
          mShouJianRSF.setText("");
        }
        if (isFirstSfsd) {
          isFirstSfsd = false;
        } else {
          mFaDanQKBZ.setText("");
        }

        setXxbg(callEntityList);
        break;
      case "未送达":
        llWqsyy.setVisibility(View.VISIBLE);
        llQsr.setVisibility(View.GONE);
        llSjrsf.setVisibility(View.GONE);
        mShouJianRSF.setText("");
        if (isFirstSfsd) {
          isFirstSfsd = false;
        } else {
          mFaDanQKBZ.setText("");
        }

        setXxbg(callEntityList);
        break;
      case "其他":
        if ("送达".equals(spinnerSfsd.getSelectedItem().toString())) {
          llSjrsf.setVisibility(View.VISIBLE);

          if ("其他".equals(spinnerWqsyy.getSelectedItem().toString())) {

          } else {
            qsrPosition = 7;
            if (isFirstQsr) {
              isFirstQsr = false;
            } else {
              mFaDanQKBZ.setText("");
            }
          }
        } else {
          llSjrsf.setVisibility(View.GONE);
          mShouJianRSF.setText("");
          if ("其他".equals(spinnerQsr.getSelectedItem().toString())) {

          } else {
            if (isFirstWqsyy) {
              isFirstWqsyy = false;
            } else {
              mFaDanQKBZ.setText("");
            }
          }
        }

        setXxbg(callEntityList);

        break;
      case "":
        qsrPosition = -1;
        llSjrsf.setVisibility(View.GONE);
        mShouJianRSF.setText("");
        if (isFirstQsr) {
          isFirstQsr = false;
        } else {
          mFaDanQKBZ.setText("");
        }

        setXxbg(callEntityList);
        break;
      case "经理或主管":
        qsrPosition = 0;
        llSjrsf.setVisibility(View.GONE);
        mShouJianRSF.setText("");
        if (isFirstQsr) {
          isFirstQsr = false;
        } else {
          mFaDanQKBZ.setText("");
        }

        setXxbg(callEntityList);
        break;
      case "财务":
        qsrPosition = 1;
        llSjrsf.setVisibility(View.GONE);
        mShouJianRSF.setText("");
        if (isFirstQsr) {
          isFirstQsr = false;
        } else {
          mFaDanQKBZ.setText("");
        }

        setXxbg(callEntityList);
        break;
      case "门卫保安":
        qsrPosition = 2;
        llSjrsf.setVisibility(View.GONE);
        mShouJianRSF.setText("");
        if (isFirstQsr) {
          isFirstQsr = false;
        } else {
          mFaDanQKBZ.setText("");
        }

        setXxbg(callEntityList);
        break;
      case "物业":
        qsrPosition = 3;
        llSjrsf.setVisibility(View.GONE);
        mShouJianRSF.setText("");
        if (isFirstQsr) {
          isFirstQsr = false;
        } else {
          mFaDanQKBZ.setText("");
        }

        setXxbg(callEntityList);
        break;
      case "工程部":
        qsrPosition = 4;
        llSjrsf.setVisibility(View.GONE);
        mShouJianRSF.setText("");
        if (isFirstQsr) {
          isFirstQsr = false;
        } else {
          mFaDanQKBZ.setText("");
        }

        setXxbg(callEntityList);
        break;
      case "前台接待":
        qsrPosition = 5;
        llSjrsf.setVisibility(View.GONE);
        mShouJianRSF.setText("");
        if (isFirstQsr) {
          isFirstQsr = false;
        } else {
          mFaDanQKBZ.setText("");
        }

        setXxbg(callEntityList);
        break;
      case "后勤保障":
        qsrPosition = 6;
        llSjrsf.setVisibility(View.GONE);
        mShouJianRSF.setText("");
        if (isFirstQsr) {
          isFirstQsr = false;
        } else {
          mFaDanQKBZ.setText("");
        }

        setXxbg(callEntityList);
        break;
      case "上门无人":
      case "动迁":
      case "查无此地址":
      case "查无此户名":
      case "用户拒收":
        llSjrsf.setVisibility(View.GONE);
        mShouJianRSF.setText("");
        if (isFirstWqsyy) {
          isFirstWqsyy = false;
        } else {
          mFaDanQKBZ.setText("");
        }

        setXxbg(callEntityList);
        break;
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

  private void setXxbg(List<OverrateCallHandleEntity> callEntityList) {
    if (callEntityList != null && callEntityList.size() > 0) {
      OverrateCallHandleEntity callHandleEntity = callEntityList.get(0);
      if (callHandleEntity != null && ("要求变更“账单地址”".equals(callHandleEntity.getCjxl())
        || "户名不一致，要求更正后支付".equals(callHandleEntity.getCjxl())
        || "已过户".equals(callHandleEntity.getCjxl()))) {
        if ("是".equals(callHandleEntity.getIsCuiJiao())) {
          spinnerXxbg.setEnabled(false);
          spinnerXxbg.setSelection(1);
        } else {
          spinnerXxbg.setEnabled(true);
        }
      } else {
        spinnerXxbg.setEnabled(true);
      }
    } else {
      spinnerXxbg.setEnabled(true);
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }

  public boolean saveJPG(Bitmap bitmap, String path) {
    if (bitmap == null || TextUtil.isNullOrEmpty(path)) {
      return false;
    }
    File file = new File(path);
    try {
      FileOutputStream out = new FileOutputStream(file);
      bitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
      out.flush();
      out.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return false;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Subscribe
  public void onCallTransferDataEvent(CallTransferDataEvent event) {
    LogUtils.e(event);
    if (event != null) {
      handleEntity = event.getData();
      if (handleEntity != null && ("要求变更“账单地址”".equals(handleEntity.getCjxl())
        || "户名不一致，要求更正后支付".equals(handleEntity.getCjxl())
        || "已过户".equals(handleEntity.getCjxl()))) {
        spinnerXxbg.setEnabled(false);
        spinnerXxbg.setSelection(1);

        if ("要求变更“账单地址”".equals(handleEntity.getCjxl())) {
          if (!StringUtils.isEmpty(handleEntity.getZqzddz())) {
            mYouJiDZ.setText(handleEntity.getZqzddz());
            mKeHuMC.setText(mParam1.getKhmc());
          } else {
            mYouJiDZ.setText(mParam1.getYjdz());
            mKeHuMC.setText(mParam1.getKhmc());
          }
        } else if ("户名不一致，要求更正后支付".equals(handleEntity.getCjxl())) {
          if (!StringUtils.isEmpty(handleEntity.getZqhm())) {
            mKeHuMC.setText(handleEntity.getZqhm());
            mYouJiDZ.setText(mParam1.getYjdz());
          } else {
            mKeHuMC.setText(mParam1.getKhmc());
            mYouJiDZ.setText(mParam1.getYjdz());
          }
        } else if ("已过户".equals(handleEntity.getCjxl())) {
          if (!StringUtils.isEmpty(handleEntity.getXhm())) {
            mKeHuMC.setText(handleEntity.getXhm());
            mYouJiDZ.setText(mParam1.getYjdz());
          } else {
            mKeHuMC.setText(mParam1.getKhmc());
            mYouJiDZ.setText(mParam1.getYjdz());
          }
        } else {
          mKeHuMC.setText(mParam1.getKhmc());
          mYouJiDZ.setText(mParam1.getYjdz());
        }

        mZipCodeEt.setText(mParam1.getYoubian());

//                if (!StringUtils.isEmpty(handleEntity.getZqhm())) {
//                    mKeHuMC.setText(handleEntity.getZqhm());
//                } else {
//                    if (!StringUtils.isEmpty(handleEntity.getXhm())) {
//                        mKeHuMC.setText(handleEntity.getXhm());
//                    } else {
//                        mKeHuMC.setText(mParam1.getKhmc());
//                    }
//                }
      } else {
        spinnerXxbg.setEnabled(true);
      }
    }
  }
}
