package com.sh3h.hotline.ui.knowledge;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.ui.base.MvpView;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.web.WebActivity;
import com.sh3h.mobileutil.util.ApplicationsUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by dengzhimin on 2016/9/14.
 */
public class KnowledgeBaseActivity extends ParentActivity implements KnowledgeBaseMvpView {

    private final static String TAG = "KnowledgeBaseActivity";

    @Inject
    KnowledgeBasePresenter mPresenter;

    @Inject
    Bus mEventBus;

    @Inject
    ConfigHelper mConfigHelper;

    private Unbinder mUnbinder;

    private boolean isInitSuccess;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledgebase);
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);
        mPresenter.attachView( this);
        mEventBus.register(this);
        initToolBar(R.string.activity_knowledgebase);

        Intent intent = getIntent();
        if (savedInstanceState != null) {
            initParams(savedInstanceState);
        } else if (intent != null) {
            initParams(intent.getExtras());
        } else {
            initParams(null);
        }
        checkPermissions();
    }

    @OnClick(R.id.fs_cv_network)
    public void guardOperate() {
        if (!isInitSuccess
                || TextUtil.isNullOrEmpty(mConfigHelper.getWikiServer())) {
            return;
        }
        Intent intent = new Intent(KnowledgeBaseActivity.this, WebActivity.class);
        intent.putExtra(Constant.WEB_VIEW_URL, mConfigHelper.getWikiServer());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mPresenter.detachView();
        mEventBus.unregister(this);
    }

    @Subscribe
    public void onInitResult(UIBusEvent.InitResult initResult) {
        LogUtil.i(TAG, "---onInitResult---" + initResult.isSuccess());
        if (initResult.isSuccess()) {
            isInitSuccess = true;
        } else {
            ApplicationsUtil.showMessage(this, R.string.text_init_failure);
        }
    }
}
