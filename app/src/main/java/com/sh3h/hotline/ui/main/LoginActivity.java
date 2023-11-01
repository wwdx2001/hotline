package com.sh3h.hotline.ui.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.hjq.toast.ToastParams;
import com.hjq.toast.Toaster;
import com.hjq.toast.style.CustomToastStyle;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.request.DULoginInfo;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.LoginStatusEntity;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.subsciber.IProgressDialog;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class LoginActivity extends ParentActivity implements View.OnClickListener {

    @Inject
    DataManager mDataManager;

    protected CompositeSubscription mSubscription;

    private EditText mEtAccount, mEtPwd;
    private Button mBtnLogin;
    private String deviceId;
    private TextView mTvIMEI;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        getActivityComponent().inject(this);
        mSubscription = new CompositeSubscription();

        initView();
        initData();
    }

    protected int getLayoutId() {
        return R.layout.activity_login;
    }


    protected void initView() {
        mEtAccount = (EditText) findViewById(R.id.et_username);
        mEtPwd = (EditText) findViewById(R.id.et_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void initData() {
        checkPermissions();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (StringUtils.isEmpty(mEtAccount.getText().toString())) {
                    mEtAccount.requestFocus();
                    mEtAccount.setError("请输入账号");
                    return;
                }

                if (StringUtils.isEmpty(mEtPwd.getText().toString())) {
                    mEtPwd.requestFocus();
                    mEtPwd.setError("请输入密码");
                    return;
                }

//                startSBLogin();
                startCSLogin();
                break;
        }
    }

    private void startCSLogin() {
        IProgressDialog iProgressDialog = new IProgressDialog() {
            @Override
            public Dialog getDialog() {
                ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("正在登录...");
                return progressDialog;
            }
        };
        EasyHttp.post(URL.Login)
                .params("account", mEtAccount.getText().toString().trim())
                .params("passWord", mEtPwd.getText().toString().trim())
                .execute(new ProgressDialogCallBack<String>(iProgressDialog, true, false) {
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        ToastUtils.showLong("登录失败：" + e.getMessage());
                    }

                    @Override
                    public void onSuccess(String s) {
                        Gson gson = new Gson();
                        LoginStatusEntity loginStatusEntity = gson.fromJson(s, LoginStatusEntity.class);
                        if (loginStatusEntity.getState() != 0) {
                            ToastUtils.showShort(loginStatusEntity.getMsg());
                        } else {
                            if (loginStatusEntity.getData() != null) {
                                ToastUtils.showShort(loginStatusEntity.getMsg());
                                // 保存信息到登录历史表
                                LoginActivity.this.savePassword(loginStatusEntity.getData().getUserId().hashCode(), loginStatusEntity.getData().getAccount(),
                                        mEtPwd.getText().toString().trim(),
                                        "F6D62986-915F-4DD8-963A-B0632F-149E18");

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    }
                });
    }

    private void startSBLogin() {
//        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//        finish();
//        // 保存信息到登录历史表
//        LoginActivity.this.savePassword(0, mEtAccount.getText().toString().trim(),
//                mEtPwd.getText().toString().trim(),
//                "F6D62986-915F-4DD8-963A-B0632F-149E18");
        DULoginInfo duLoginInfo = new DULoginInfo(mEtAccount.getText().toString().trim(), mEtPwd.getText().toString().trim());
        mSubscription.add(mDataManager.login(duLoginInfo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e("---login onCompleted---");

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(String.format("---login onError: %s---", e.getMessage()));
                        ToastParams params = new ToastParams();
                        params.text = "登录失败";
                        params.style = new CustomToastStyle(R.layout.toast_error);
                        Toaster.show(params);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtils.e(String.format("---login onNext: %s---", aBoolean ? "true" : "false"));
                        if (aBoolean) {
                            ToastUtils.showShort("登录成功！");
//                            LoginActivity.this.savePassword(0, mEtAccount.getText().toString().trim(),
//                                    mEtPwd.getText().toString().trim(),
//                                    "F6D62986-915F-4DD8-963A-B0632F-149E18");
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            ToastParams params = new ToastParams();
                            params.text = "登录失败";
                            params.style = new CustomToastStyle(R.layout.toast_error);
                            Toaster.show(params);
                        }
                    }
                }));
    }

    /**
     * 存储用户编号 和 密码
     */
    private void savePassword(int userId, String account, String userName, String accessToken) {
        // 记录用户最后一次输入内容
        MainApplication.getInstance()
                .updateSession(userId, account, userName, accessToken);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscription.unsubscribe();
        mSubscription = null;
    }
}
