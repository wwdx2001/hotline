package com.sh3h.hotline.ui.nonresident.sign;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.multimedia.signup.SignView;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.squareup.picasso.Picasso;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SignatureNameNewActivity extends ParentActivity implements SignatureNameMvpView , View.OnClickListener {

    @Inject
    SignatureNamePresenter mSignatureNamePresenter;

    @BindView(R.id.sign_up_signview)
    SignView signView;

    @BindView(R.id.txt_confirm)
    TextView txtConfirm;

    @BindView(R.id.txt_clear)
    TextView txtClear;

    @BindView(R.id.multimedia_signup_rl)
    public RelativeLayout mSignUpLayout;

    @BindView(R.id.ll_operate_btn)
    public LinearLayout llOperateBtn;

    @BindView(R.id.multimedia_sign_up_iv)
    public ImageView mSignupImage;

    @BindView(R.id.signup_uploaded_flag_image)
    public ImageView mSignupFlag;

    public DUMedia mSignDUMedia;

    private String type;
    private String taskId;
    private int taskType;
    private int taskState;
    private boolean mIsUploadFlag;

    private Unbinder mUnbinder;

    private MenuItem menuItem;

    private String signupName;

    private String currentTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_name_new);
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);
        mSignatureNamePresenter.attachView(this);

        initToolBar(R.string.label_signup_name);
        initView();
    }

    private void initView() {
        type = getIntent().getStringExtra("type");
        taskId = getIntent().getStringExtra(Constant.TASK_ID);
        taskType = getIntent().getIntExtra(Constant.TASK_TYPE, 0);
        taskState = getIntent().getIntExtra(Constant.TASK_STATE, 0);
        mIsUploadFlag = getIntent().getBooleanExtra(Constant.DATA_IS_UPLOAD, false);

        if (!StringUtils.isEmpty(type) && "preview".equals(type)) {
            mSignUpLayout.setVisibility(View.VISIBLE);
            signView.setVisibility(View.GONE);
            llOperateBtn.setVisibility(View.GONE);
        } else {
            mSignUpLayout.setVisibility(View.GONE);
            signView.setVisibility(View.VISIBLE);
            llOperateBtn.setVisibility(View.VISIBLE);
        }

        txtConfirm.setOnClickListener(this);
        txtClear.setOnClickListener(this);

        mSignatureNamePresenter.loadSignup(taskId, taskType, taskState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_sign, menu);
//        MenuItem actionSign = menu.findItem(R.id.action_sign);
//        MenuItem actionSubmit = menu.findItem(R.id.action_submit);
//        if (!StringUtils.isEmpty(type) && "preview".equals(type)) {
//            actionSign.setVisible(false);
//            actionSubmit.setVisible(false);
//        } else {
//            actionSign.setVisible(true);
//            actionSubmit.setVisible(true);
//        }
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign:
                signView.clearPath();
                break;
            case R.id.action_submit:
                signupName = "SIGN_" + taskId + "_" + System.currentTimeMillis() + ".png";
                mSignatureNamePresenter.saveSignupImage(signView.getImage(), signupName, taskId,
                        taskState, taskType);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mSignatureNamePresenter.detachView();
    }

    @Override
    public void onSaveSignup(Boolean aBoolean) {
        if (aBoolean) {
            ApplicationsUtil.showMessage(this, R.string.text_save_success);
            finish();
        } else {
            ApplicationsUtil.showMessage(this, R.string.text_save_fail);
        }
    }

    @Override
    public void onLoadSignup(DUMedia duMedia) {
//        if (duMedia == null || TextUtil.isNullOrEmpty(duMedia.getFilePath())) {
//            return;
//        }

        mSignDUMedia = duMedia;

        if (!StringUtils.isEmpty(type)) {
            if ("sign".equals(type)) {
                if (duMedia != null && !TextUtil.isNullOrEmpty(duMedia.getFilePath())) {
                    mSignatureNamePresenter.deleteSignup(duMedia);
                } else {
                    return;
                }

            } else {
                mSignUpLayout.setVisibility(View.VISIBLE);
                signView.setVisibility(View.GONE);
                llOperateBtn.setVisibility(View.GONE);

                if (duMedia == null || TextUtil.isNullOrEmpty(duMedia.getFilePath())) {
                    return;
                }

                if (duMedia.getUploadFlag() != Constant.HAS_UPLOADED) {
                    mSignupFlag.setImageResource(R.drawable.ic_cloud_upload_grey_24dp);
                } else {
                    mSignupFlag.setImageResource(R.drawable.ic_cloud_done_grep_24dp);
                }
                Picasso.with(this)
                        .load(new File(duMedia.getFilePath()))
                        .placeholder(R.mipmap.bg_place_holder)
                        .error(R.mipmap.bg_error)
                        .fit()
                        .tag(this)
                        .into(mSignupImage);
            }
        }

    }

    @Override
    public void onDeleteSignup(Boolean aBoolean) {
        mSignUpLayout.setVisibility(View.GONE);
        signView.setVisibility(View.VISIBLE);
        llOperateBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_clear:
                signView.clearPath();
                break;
            case R.id.txt_confirm:
                signupName = "SIGN_" + taskId + "_" + System.currentTimeMillis() + ".png";
                mSignatureNamePresenter.saveSignupImage(signView.getImage(), signupName, taskId,
                        taskState, taskType);
                break;
            default:
                break;
        }
    }
}