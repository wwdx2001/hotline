package com.sh3h.hotline.ui.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.NetworkUtil;
import com.sh3h.dataprovider.util.PackageUtil;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.main.LoginActivity;
import com.sh3h.hotline.util.ActivityManagerHelper;
import com.sh3h.hotline.util.CommonUtils;
import com.sh3h.mobileutil.util.ApplicationsUtil;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by zhangjing on 2017/2/7.
 */

public class SettingActivity extends ParentActivity implements SettingMvpView, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.cv_network)
    CardView mCvNetwork;

    @BindView(R.id.cv_version)
    CardView mCvVersion;

    @BindView(R.id.cv_clear_history)
    CardView mCvClearHistory;

    @BindView(R.id.tv_version)
    TextView mTvVersion;

    @BindView(R.id.radiogroup_tips)
    RadioGroup mRadioGroupTips;

    @BindView(R.id.cv_log_out)
    CardView mCvLogOut;

    @Inject
    ConfigHelper mConfigHelper;

    @Inject
    SettingPresenter mSettingPresenter;

    private Unbinder mUnbinder;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotline_setting);
//        ToastUtils.showLong("bugly 热更新 生效了");
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);
        mSettingPresenter.attachView(this);
        initToolBar(R.string.menu_settings);
        initViewData();
        checkPermissions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mSettingPresenter.detachView();
    }

    private void initViewData() {
        mTvVersion.setText(PackageUtil.getVersionName(this));
        String account = SPUtils.getInstance().getString(Constant.USERID);
        boolean isTipsLimit = SPUtils.getInstance(account + "_SaveOrdersTips").getBoolean("IsTipsLimit", false);
        if (isTipsLimit) {
            mRadioGroupTips.check(R.id.radio_tips_yes);
        } else {
            mRadioGroupTips.check(R.id.radio_tips_no);
        }
        mRadioGroupTips.setOnCheckedChangeListener(this);
    }

  /**
   * 提示是否退出登录
   * 如果退登情况sp，清空activity栈，跳转到登陆界面
   * @param view
   */
  @OnClick(R.id.cv_log_out)
  public void promptLogOut(View view) {
    AlertDialog.Builder dialog = new AlertDialog.Builder(this);

    dialog.setTitle("是否要退出登录");
    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });

    dialog.setNeutralButton("确定", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        SPUtils.getInstance("user").clear();

        ActivityManagerHelper.getInstance().popParentElesActivites();

        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
        startActivity(intent);
      }
    });

    dialog.show();
  }

    @OnClick(R.id.cv_network)
    public void setNetwork(View view) {
        if (!isConfigInitSuccess()) {
            ApplicationsUtil.showMessage(this, R.string.text_init_failure);
            return;
        }

        View customView = LayoutInflater.from(this).inflate(R.layout.dialog_content_edittext, null);
        final EditText et = (EditText) customView.findViewById(R.id.et_dialog_content);
        et.setText(mConfigHelper.getBaseUri());
        AlertDialog dialogNetWork = new AlertDialog.Builder(this)
                .setTitle(R.string.text_network_setting)
                .setMessage(R.string.dialog_content_network_setting)
                .setView(customView)
                .setNegativeButton(R.string.text_negative, null)
                .setPositiveButton(R.string.text_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!mConfigHelper.saveNetWorkSetting(et.getText().toString().trim())) {
                            ApplicationsUtil.showMessage(getApplicationContext(), R.string.text_save_fail);
                        }
                    }
                })
                .create();
        dialogNetWork.show();
    }

    @OnClick(R.id.cv_clear_history)
    public void setClearHistory(View view) {
        if (!isConfigInitSuccess()) {
            ApplicationsUtil.showMessage(this, R.string.text_init_failure);
            return;
        }

        AlertDialog dialogNetWork = new AlertDialog.Builder(this)
                .setTitle(R.string.text_clear_history_data)
                .setMessage(R.string.text_is_clear_history)
                .setNegativeButton(R.string.text_negative, null)
                .setPositiveButton(R.string.text_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSettingPresenter.clearHistory();
                    }
                })
                .create();
        dialogNetWork.show();
    }

    @Override
    public void clearHistoryEnd() {
        ApplicationsUtil.showMessage(this, R.string.text_clear_history_success);
    }


    @OnClick(R.id.cv_uploadLog)
    public void uploadLog(View view) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.text_request))
                .setMessage(getString(R.string.text_is_upload_log))
                .setNegativeButton(getString(R.string.text_negative), null)
                .setPositiveButton(getString(R.string.text_positive),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!NetworkUtil.isNetworkConnected(SettingActivity.this)) {
                                    ApplicationsUtil.showMessage(SettingActivity.this,
                                            R.string.toast_network_is_not_connect);
                                    return;
                                }
                                mSettingPresenter.uploadLogFiles();
                            }
                        })
                .show();
    }

    @Override
    public void onUploadLogFiles(Boolean aBoolean) {
        ApplicationsUtil.showMessage(this,
                aBoolean ? R.string.text_upload_log_successfully : R.string.text_upload_log_failure);
    }

    @Override
    public void onError(int stringId) {
        ApplicationsUtil.showMessage(this, stringId);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        String account = SPUtils.getInstance().getString(Constant.USERID);
        switch (checkedId) {
            case R.id.radio_tips_yes:
                SPUtils.getInstance(account + "_SaveOrdersTips").put("IsTipsLimit", true);
                break;
            case R.id.radio_tips_no:
                SPUtils.getInstance(account + "_SaveOrdersTips").put("IsTipsLimit", false);
                break;
            default:
                break;
        }
    }


}
