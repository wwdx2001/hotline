package com.sh3h.hotline.ui.nonresident.selfbilling;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aliyun.demo.recorder.AliyunVideoRecorder;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.OverrateSelfBillingEntity;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.multimedia.MultimediaFragment;
import com.sh3h.hotline.ui.nonresident.media.MultimediaFileFragment;
import com.sh3h.hotline.ui.order.TaskState;
import com.sh3h.mobileutil.util.TextUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OverrateSelfBillingActivity extends ParentActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private final static String TAG = "HandleOrderActivity";
    private static final String FRAGMENT_TAG = "FragmentTAG";
    private int mTaskState = Constant.TASK_STATE_CREATE;
    private int mTaskType = Constant.TASK_TYPE_CREATE_SELF_ORDER;

    @Inject
    DataManager mDataManager;

    @Inject
    EventPosterHelper mEventPosterHelper;

    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    @BindView(R.id.txt_confirm)
    TextView txtConfirm;

    @BindView(R.id.bottom_navigation)
    RadioGroup bottomNavigation;

    @BindView(R.id.rb_edit)
    RadioButton rbEdit;

    @BindView(R.id.ll_arrow_tip)
    LinearLayout llArrowTip;

    @BindView(R.id.txt_query_select)
    TextView txtQuerySelect;

    @BindView(R.id.txt_edit_submit)
    TextView txtEditSubmit;

    private Unbinder mUnbinder;

    private FragmentManager mFragmentManager;

    private Fragment mCurrentFragment;

    public MultimediaFileFragment mMultimediaFileFragment;

    public QuerySelectFragment mQuerySelectFragment;

    public EditSubmitFragment mEditSubmitFragment;

    private MenuItem submitItem;

    public OverrateSelfBillingEntity currentEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overrate_self_billing);
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);

        initToolBar(R.string.label_overrate_selfbilling);

        initView();
        mCurrentFragment = initFragment(savedInstanceState);
    }

    private void initView() {
        txtConfirm.setOnClickListener(this);
        bottomNavigation.setOnCheckedChangeListener(this);
    }

    private Fragment initFragment(Bundle savedInstanceState) {
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (savedInstanceState == null) {
            mQuerySelectFragment = QuerySelectFragment.newInstance("", "");
            transaction.add(R.id.fragment_container, mQuerySelectFragment, QuerySelectFragment.class.getName()).commitAllowingStateLoss();
            return mQuerySelectFragment;
        }

        String fragmentTag = savedInstanceState.getString("FRAGMENT_NAME", QuerySelectFragment.class.getName());
        OverrateSelfBillingEntity currentEntity1 = (OverrateSelfBillingEntity) savedInstanceState.getSerializable(Constant.TASK_ID);
        if (fragmentTag.equals(QuerySelectFragment.class.getName())) {
            mQuerySelectFragment = QuerySelectFragment.newInstance("", "");
            currentEntity = mQuerySelectFragment.getCurrentEntity();
            transaction.add(R.id.fragment_container, mQuerySelectFragment, fragmentTag).commitAllowingStateLoss();
            return mQuerySelectFragment;
        } else if (fragmentTag.equals(EditSubmitFragment.class.getName())) {
            EditSubmitFragment fragment = EditSubmitFragment.newInstance(currentEntity1, "");
            transaction.add(R.id.fragment_container, fragment, fragmentTag).commitAllowingStateLoss();
            return fragment;
        } else if (fragmentTag.equals(MultimediaFileFragment.class.getName())) {
            MultimediaFileFragment fragment = MultimediaFileFragment.newInstance(currentEntity1.getZhbh(), Constant.TASK_TYPE_DOWNLOAD, TaskState.HANDLE);
            transaction.add(R.id.fragment_container, fragment, fragmentTag).commitAllowingStateLoss();
            return fragment;
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        submitItem = menu.findItem(R.id.action_save);
        submitItem.setTitle("提交");
        submitItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mCurrentFragment != null && (mCurrentFragment instanceof EditSubmitFragment
                    || mCurrentFragment instanceof MultimediaFileFragment)) {
                mEditSubmitFragment.clearData();
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                hideFragment(transaction);

                mQuerySelectFragment = (QuerySelectFragment) mFragmentManager.findFragmentByTag(QuerySelectFragment.class.getName());
                if (mQuerySelectFragment == null) {
                    mQuerySelectFragment = QuerySelectFragment.newInstance("", "");
                    transaction.add(R.id.fragment_container, mQuerySelectFragment, mQuerySelectFragment.getClass().getName()).commit();
                } else {
                    transaction.show(mQuerySelectFragment).commit();
                }

                mCurrentFragment = mQuerySelectFragment;

                setToolbarTitle("超定额自开单");
                submitItem.setVisible(false);
                txtConfirm.setVisibility(View.VISIBLE);
                bottomNavigation.setVisibility(View.GONE);
                llArrowTip.setBackgroundResource(R.mipmap.ic_overrate_selfbilling_leftarrow);
                txtQuerySelect.setTextColor(Color.parseColor("#ffffff"));
                txtEditSubmit.setTextColor(Color.parseColor("#666666"));
                return true;
            }
        } else if (item.getItemId() == R.id.action_save) {
            save();
        }

        return super.onOptionsItemSelected(item);
    }

    private void save() {
//        ToastUtils.showShort("提交数据");
        final String fylb = mQuerySelectFragment.mSpinnerFylb.getSelectedItem().toString().trim();
        final String fynr = mQuerySelectFragment.mSpinnerFynr.getSelectedItem().toString().trim();

        String zhbh = mEditSubmitFragment.mZhangHuBH.getText().toString().trim();
        String xzq = mEditSubmitFragment.mXingZhengQ.getText().toString().trim();
        String ssdm = mEditSubmitFragment.mSheShuiDM.getText().toString().trim();
        final String tyshxydm = mEditSubmitFragment.mTongYiSHXYDM.getText().toString().trim();
        final String khmc = mEditSubmitFragment.mKeHuMC.getText().toString().trim();

        final String lxr = mEditSubmitFragment.mLianXiR.getText().toString().trim();
        final String lxfs = mEditSubmitFragment.mLianXiFS.getText().toString().trim();
        final String yjdz = mEditSubmitFragment.mYouJiDZ.getText().toString().trim();
        final String bz = mEditSubmitFragment.mBeiZhu.getText().toString().trim();

        if (TextUtils.isEmpty(tyshxydm)) {
            ToastUtils.showShort("统一社会信用代码不能为空！");
            return;
        }
        if (TextUtils.isEmpty(khmc)) {
            ToastUtils.showShort("客户名称不能为空！");
            return;
        }
        if (TextUtils.isEmpty(lxr)) {
            ToastUtils.showShort("联系人不能为空！");
            return;
        }
        if (TextUtils.isEmpty(lxfs)) {
            ToastUtils.showShort("联系方式不能为空！");
            return;
        }
        if (TextUtils.isEmpty(yjdz)) {
            ToastUtils.showShort("邮寄地址不能为空！");
            return;
        }

        if (tyshxydm.equals(currentEntity.getTyshxydm()) && khmc.equals(currentEntity.getKhmc())
                && lxr.equals(currentEntity.getLxr()) && lxfs.equals(currentEntity.getLxfs())
                && yjdz.equals(currentEntity.getYjdz())) {
            ToastUtils.showShort("请修改后再进行提交！");
            return;
        }

        if (mMultimediaFileFragment instanceof MultimediaFileFragment) {
            int fileSize = mMultimediaFileFragment.getFileSize();
            if (fileSize == 0) {
                ToastUtils.showShort(getResources().getString(R.string.add_file));
                return;
            }

            int pictureSize = mMultimediaFileFragment.getPictureSize();
            if (pictureSize == 0) {
                ToastUtils.showShort(getResources().getString(R.string.add_picture));
                return;
            }
        }

        AlertDialog.Builder buildDialog = new AlertDialog.Builder(this);
        buildDialog.setTitle("提示");
        buildDialog.setMessage("您确定要上传数据吗？");
        buildDialog.setCancelable(false);
        buildDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        buildDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadData(fylb, fynr, tyshxydm, khmc, lxr, lxfs, yjdz, bz);
//                ApplicationsUtil.showMessage(HandleOrderActivity.this, "确定");
                dialog.dismiss();
            }
        });
        buildDialog.create().show();
    }

    private void uploadData(String fylb, String fynr, String tyshxydm, String khmc, String lxr, String lxfs, String yjdz, String bz) {
        EasyHttp
                .post(URL.FeiJuLJZLBG)
                .params("fylb", fylb)
                .params("fynr", fynr)
                .params("slsj", TimeUtils.getNowString())
                .params("fqfs", "抄表员自开单")
                .params("lxdh", "")
                .params("cbyid", mDataManager.getAccount())
                .params("dz", "")

                .params("zhbh", currentEntity.getZhbh())
                .params("hm", currentEntity.getKhmc())
                .params("tyshxydm", currentEntity.getTyshxydm())
                .params("lxr", currentEntity.getLxr())
                .params("lxfs", currentEntity.getLxfs())
                .params("yjdz", currentEntity.getYjdz())

                .params("gghtyshxydm", tyshxydm)
                .params("gghhm", khmc)
                .params("gghlxr", lxr)
                .params("gghlxfs", lxfs)
                .params("gghyjdz", yjdz)
                .params("bz", bz)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onError(ApiException e) {
                        if (e.getCode() == 1010) {
                            ToastUtils.showShort("没有数据");
                        } else {
                            ToastUtils.showShort(e.getMessage());
                        }
                    }

                    @Override
                    public void onSuccess(String s) {
                        ToastUtils.showShort("提交成功");
                        finish();
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //record the name of current activing fragment
        if (mCurrentFragment != null) {
            outState.putString(FRAGMENT_TAG, mCurrentFragment.getClass().getName());
        }
        if (currentEntity != null) {
            outState.putSerializable(Constant.TASK_ID, currentEntity);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i(TAG, "onActivityResult");
        if (requestCode == MultimediaFileFragment.SNAP_VIDEO) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                int type = data.getIntExtra(AliyunVideoRecorder.RESULT_TYPE, 0);
                if (type == AliyunVideoRecorder.RESULT_TYPE_RECORD) {
                    if (TextUtil.isNullOrEmpty(data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH))) {
                        return;
                    }
                    mEventPosterHelper.postEventSafely(new UIBusEvent.RecordVideo(data.getStringExtra(AliyunVideoRecorder.OUTPUT_PATH)));
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_confirm:
                if (mQuerySelectFragment != null) {
                    currentEntity = mQuerySelectFragment.getCurrentEntity();
                }
                if (currentEntity == null) {
                    ToastUtils.showLong("请选择用户信息！");
                    return;
                }

                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                hideFragment(transaction);

                mEditSubmitFragment = (EditSubmitFragment) mFragmentManager.findFragmentByTag(EditSubmitFragment.class.getName());
                if (mEditSubmitFragment == null) {
                    mEditSubmitFragment = EditSubmitFragment.newInstance(currentEntity, "");
                    transaction.add(R.id.fragment_container, mEditSubmitFragment, mEditSubmitFragment.getClass().getName()).commit();
                } else {
                    mEditSubmitFragment.notifyData(currentEntity);
                    transaction.show(mEditSubmitFragment).commit();
                }
                mCurrentFragment = mEditSubmitFragment;

                mMultimediaFileFragment = (MultimediaFileFragment) mFragmentManager.findFragmentByTag(MultimediaFileFragment.class.getName());
                if (mMultimediaFileFragment == null) {
                    mMultimediaFileFragment = MultimediaFileFragment.newInstance(currentEntity.getZhbh(), Constant.TASK_TYPE_DOWNLOAD, TaskState.HANDLE);
                } else {
                    mMultimediaFileFragment.notifyData(currentEntity.getZhbh(), Constant.TASK_TYPE_DOWNLOAD, TaskState.HANDLE);
                }

                setToolbarTitle("编辑");
                rbEdit.setChecked(true);
                submitItem.setVisible(true);
                txtConfirm.setVisibility(View.GONE);
                bottomNavigation.setVisibility(View.VISIBLE);
                llArrowTip.setBackgroundResource(R.mipmap.ic_overrate_selfbilling_rightarrow);
                txtQuerySelect.setTextColor(Color.parseColor("#666666"));
                txtEditSubmit.setTextColor(Color.parseColor("#ffffff"));
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragment(transaction);

        switch (checkedId) {
            case R.id.rb_edit:
                mEditSubmitFragment = (EditSubmitFragment) mFragmentManager.findFragmentByTag(EditSubmitFragment.class.getName());
                if (mEditSubmitFragment == null) {
                    mEditSubmitFragment = EditSubmitFragment.newInstance(currentEntity, "");
                    transaction.add(R.id.fragment_container, mEditSubmitFragment, mEditSubmitFragment.getClass().getName()).commit();
                } else {
                    transaction.show(mEditSubmitFragment).commit();
                }

                mCurrentFragment = mEditSubmitFragment;
                setToolbarTitle("编辑");
                break;
            case R.id.rb_media:
                mMultimediaFileFragment = (MultimediaFileFragment) mFragmentManager.findFragmentByTag(MultimediaFileFragment.class.getName());
                if (mMultimediaFileFragment == null) {
                    mMultimediaFileFragment = MultimediaFileFragment.newInstance(currentEntity.getZhbh(), Constant.TASK_TYPE_DOWNLOAD, TaskState.HANDLE);
                    transaction.add(R.id.fragment_container, mMultimediaFileFragment, mMultimediaFileFragment.getClass().getName()).commit();
                } else {
                    transaction.show(mMultimediaFileFragment).commit();
                }

                mCurrentFragment = mEditSubmitFragment;
                setToolbarTitle("多媒体");
                break;
            default:
                break;
        }
    }

    private void hideFragment(FragmentTransaction transaction) {
        MultimediaFileFragment mMultimediaFileFragment =
                (MultimediaFileFragment) mFragmentManager.findFragmentByTag(MultimediaFileFragment.class.getName());
        if (mMultimediaFileFragment != null) {
            transaction.hide(mMultimediaFileFragment);
        }

        EditSubmitFragment mEditSubmitFragment =
                (EditSubmitFragment) mFragmentManager.findFragmentByTag(EditSubmitFragment.class.getName());
        if (mEditSubmitFragment != null) {
            transaction.hide(mEditSubmitFragment);
        }

        QuerySelectFragment mQuerySelectFragment =
                (QuerySelectFragment) mFragmentManager.findFragmentByTag(QuerySelectFragment.class.getName());
        if (mQuerySelectFragment != null) {
            transaction.hide(mQuerySelectFragment);
        }
    }

    public void setConfirnVisible(boolean visible) {
        txtConfirm.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setCurrentEntity(OverrateSelfBillingEntity currentEntity) {
        this.currentEntity = currentEntity;
        LogUtils.e(currentEntity.getZhbh());
    }
}