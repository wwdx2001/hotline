package com.sh3h.hotline.ui.collection;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.dataprovider.data.entity.newentity.CollectionHandleEntity;
import com.sh3h.dataprovider.data.entity.newentity.OverrateReceiptHandleEntity;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.CollectionHandleEntityDao;
import com.sh3h.dataprovider.greendaoDao.OverrateReceiptHandleEntityDao;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.CollectionTaskListBean;
import com.sh3h.hotline.ui.base.BaseActivity;
import com.sh3h.hotline.ui.base.ParentFragment;
import com.sh3h.hotline.ui.multimedia.MultimediaPresenter;
import com.sh3h.hotline.ui.multimedia.PictureDetailsActivity;
import com.sh3h.hotline.ui.multimedia.signup.SignView;
import com.sh3h.hotline.ui.nonresident.sign.SignatureNameNewActivity;
import com.sh3h.hotline.ui.order.TaskState;
import com.sh3h.hotline.ui.order.self.create.CreateSelfOrderActivity;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.squareup.picasso.Picasso;

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

public class CollectionBackfillingFragment extends ParentFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    @Inject
    DataManager mDataManager;

    @BindView(R.id.spinner_information_change)
    public Spinner mInformationChange;

    @BindView(R.id.et_collection_remark)
    public EditText mCollectionRemark;

    @BindView(R.id.et_lianxir)
    public EditText etLianxir;

    @BindView(R.id.et_lianxifs)
    public EditText etLianxifs;

    @BindView(R.id.spinner_collection_result)
    public Spinner mCollectionResult;

    @BindView(R.id.constriant)
    LinearLayout mConstriant;

    @BindView(R.id.btn_sign)
    TextView btnSign;

    @BindView(R.id.btn_preview)
    TextView btnPreview;

    private Unbinder mUnbinder;
    protected Activity mActivity;
    private CollectionTaskListBean taskBean;

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_collection_backfilling, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
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

    private void initView() {
        mCollectionResult.setOnItemSelectedListener(this);
        mInformationChange.setOnItemSelectedListener(this);
        btnSign.setOnClickListener(this);
        btnPreview.setOnClickListener(this);
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            taskBean = bundle.getParcelable("TaskBean");

            List<CollectionHandleEntity> entityList = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                    .getCollectionHandleEntityDao()
                    .queryBuilder()
                    .where(CollectionHandleEntityDao.Properties.Albh.eq(taskBean.getAlbh()))
                    .list();
            if (entityList != null && entityList.size() > 0) {
                CollectionHandleEntity collectionHandleEntity = entityList.get(0);

                String[] spinnerArrCjjg = new String[]{"已催无人", "已催客户拒收", "客户已缴", "已催有人", "其他"};
                List<String> cjjgList = Arrays.asList(spinnerArrCjjg);
                mInformationChange.setSelection(cjjgList.indexOf(collectionHandleEntity.getCjjg()));

                String[] spinnerArrXXBG = new String[]{"否", "是"};
                List<String> xxbgList = Arrays.asList(spinnerArrXXBG);
                mCollectionResult.setSelection(xxbgList.indexOf(collectionHandleEntity.getXxbg()));

                mCollectionRemark.setText(collectionHandleEntity.getCjbz());
                etLianxir.setText(collectionHandleEntity.getLxr());
                etLianxifs.setText(collectionHandleEntity.getLxfs());
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getItemAtPosition(i).toString().equals("是")) {
            mConstriant.setVisibility(View.VISIBLE);
        } else if (adapterView.getItemAtPosition(i).toString().equals("否")) {
            mConstriant.setVisibility(View.GONE);
        }

//        if (adapterView.getItemAtPosition(i).toString().equals("客户已缴") || adapterView.getItemAtPosition(i).toString().equals("已催有人") ||
//                adapterView.getItemAtPosition(i).toString().equals("其他")) {
        btnSign.setVisibility(View.VISIBLE);
        btnPreview.setVisibility(View.VISIBLE);
//        } else {
//            btnSign.setVisibility(View.GONE);
//            btnPreview.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign:
                getSignPicture(taskBean.getAlbh(), Constant.TASK_TYPE_DOWNLOAD, TaskState.HANDLE);
                break;
            case R.id.btn_preview:
                Intent previewIntent = new Intent(ActivityUtils.getTopActivity(), SignatureNameNewActivity.class);
                previewIntent.putExtra("type", "preview");
                previewIntent.putExtra(Constant.TASK_ID, taskBean.getAlbh());
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
                                    signIntent.putExtra(Constant.TASK_ID, taskBean.getAlbh());
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
                            signIntent.putExtra(Constant.TASK_ID, taskBean.getAlbh());
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

}
