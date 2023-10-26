package com.sh3h.hotline.ui.order.self.detail;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.R;
import com.sh3h.hotline.ui.base.ParentActivity;
import com.sh3h.hotline.ui.multimedia.MultimediaFragment;
import com.sh3h.hotline.ui.order.myorder.handle.HandleOrderActivity;
import com.sh3h.hotline.ui.order.self.detail.receipt.ReceiptDetailFragment;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.squareup.otto.Bus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dengzhimin on 2016/9/21.
 */

public class CreateSelfOrderDetailActivity extends ParentActivity
        implements CreateSelfOrderDetailMvpView, RadioGroup.OnCheckedChangeListener {
    @Inject
    CreateSelfOrderDetailPresenter mPresenter;

    @Inject
    Bus mEventBus;

    @BindView(R.id.bottom_navigation)
    RadioGroup mBottomNavigation;

    @BindView(R.id.container)
    CoordinatorLayout mContainerLayout;

    @BindView(R.id.create_self_order_ll)
    LinearLayout mLayout;

    @BindView(R.id.layout_bottom_navigation)
    LinearLayout mBottomLayout;

    private static final String TAG = "CreateSelfOrderDetailActivity";
    private static final String FRAGMENT_TAG = "FragmentTAG";
    private Toolbar mToolBar;

    private MenuItem mMenuItemDeal;
    private MenuItem mMenuItemSync;

    private Unbinder mUnbinder;

    private FragmentManager mFragmentManager;

    private Fragment mCurrentFragment;

    private DUHistoryTask mHistoryTask;

    private boolean isReplyImmediately;

    private String mIssueType;//处理类别

    private String mIssueContent;//处理内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createselforder);
        getActivityComponent().inject(this);
        mUnbinder = ButterKnife.bind(this);
        mPresenter.attachView(this);
        mEventBus.register(this);

        initViews(savedInstanceState);
        LogUtil.i(TAG, "onCreate");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(FRAGMENT_TAG, mCurrentFragment.getClass().getName());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mUnbinder.unbind();
        mPresenter.detachView();
        mEventBus.unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_self_order_detail, menu);
        mMenuItemDeal = menu.findItem(R.id.action_deal);
        mMenuItemSync = menu.findItem(R.id.action_sycn);
        if (mHistoryTask.getUPLOAD_FLAG() == Constant.HAS_UPLOADED) {//工单历史记录为已上传 隐藏同步按钮
            mMenuItemSync.setVisible(false);
        }
        if (isReplyImmediately) {
            mMenuItemDeal.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sycn:
                if (!TextUtil.isNullOrEmpty(mHistoryTask.getTASK_ID())) {
                    uploadCreateSelfOrder(mHistoryTask.getTASK_ID());
                }
                break;
            case R.id.action_deal:
                mPresenter.loadTaskHandle(mHistoryTask.getTASK_ID());
//                Intent intent = new Intent(CreateSelfOrderDetailActivity.this, HandleOrderActivity.class);
//                intent.putExtra(Constant.TASK_ID, mHistoryTask.getTASK_ID());
//                intent.putExtra(Constant.ORIGIN, Constant.ORIGIN_CREATE_SELF_ORDER_HISTORY);
////                intent.putExtra(Constant.DATA_IS_UPLOAD, mHistoryTask.getUPLOAD_FLAG());
//                intent.putExtra(Constant.TASK_REPLY, mHistoryTask.getTASK_REPLY());
//                intent.putExtra(DUHistoryTask.DUHistoryTask_ID, mHistoryTask.getID());
//                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragment(transaction);
        switch (checkedId) {
            case R.id.rb_receipt:
                ReceiptDetailFragment receiptDetailFragment =
                        (ReceiptDetailFragment) mFragmentManager.findFragmentByTag(ReceiptDetailFragment.class.getName());
                if (receiptDetailFragment == null) {
                    receiptDetailFragment = new ReceiptDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.TASK_ID, mHistoryTask.getTASK_ID());
                    receiptDetailFragment.setArguments(bundle);
                    transaction.add(R.id.fragment_container, receiptDetailFragment, receiptDetailFragment.getClass().getName()).commit();
                } else {
                    transaction.show(receiptDetailFragment).commit();
                }
                break;
            case R.id.rb_media:
                MultimediaFragment multimediaFragment =
                        (MultimediaFragment) mFragmentManager.findFragmentByTag(MultimediaFragment.class.getName());
                if (multimediaFragment == null) {
                    multimediaFragment = new MultimediaFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constant.ORIGIN,Constant.ORIGIN_CREATE_SELF_ORDER_HISTORY);
                    bundle.putString(Constant.TASK_ID, mHistoryTask.getTASK_ID());
                    bundle.putInt(Constant.TASK_TYPE, mHistoryTask.getTASK_TYPE());
                    bundle.putInt(Constant.TASK_STATE, mHistoryTask.getTASK_STATE());
                    bundle.putInt(Constant.UPLOAD_FLAG, mHistoryTask.getUPLOAD_FLAG());
                    multimediaFragment.setArguments(bundle);
                    transaction.add(R.id.fragment_container, multimediaFragment, multimediaFragment.getClass().getName()).commit();
                } else {
                    transaction.show(multimediaFragment).commit();
                }
                break;
        }
    }

//    @Subscribe
//    public void onNotify(UIBusEvent.NotifyToolBar notifyToolBar) {
//        if (mMenuItemDeal != null) {
//            mMenuItemDeal.setVisible(notifyToolBar.isShow());
//        }
//    }

    private void initViews(Bundle savedInstanceState) {
        mToolBar = initToolBar(R.string.activity_createselforderdetail);
        mFragmentManager = getSupportFragmentManager();
        try {
            mHistoryTask = getIntent().getExtras().getParcelable(Constant.DU_HISTORY_TASK);
            isReplyImmediately = getIntent().getExtras().getBoolean(Constant.REPLY_IMMEDIATELY);
            mIssueType = getIntent().getExtras().getString(Constant.ISSUE_TYPE);
            mIssueContent = getIntent().getExtras().getString(Constant.ISSUE_CONTENT);
            if (mHistoryTask == null) {
                throw new NullPointerException("DUHistoryTask is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
        mCurrentFragment = initFragment(savedInstanceState, mHistoryTask);
        mBottomNavigation.setOnCheckedChangeListener(this);
    }

    protected void hideFragment(FragmentTransaction transaction) {
        MultimediaFragment multimediaFragment =
                (MultimediaFragment) mFragmentManager.findFragmentByTag(MultimediaFragment.class.getName());
        if (multimediaFragment != null) {
            transaction.hide(multimediaFragment);
        }

        ReceiptDetailFragment receiptDetailFragment =
                (ReceiptDetailFragment) mFragmentManager.findFragmentByTag(ReceiptDetailFragment.class.getName());
        if (receiptDetailFragment != null) {
            transaction.hide(receiptDetailFragment);
        }
    }

    /**
     * init fragment
     *
     * @param savedInstanceState save the fragment state when app process is killed
     * @return current fragment
     */
    protected Fragment initFragment(Bundle savedInstanceState, DUHistoryTask task) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.TASK_ID, task.getTASK_ID());
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (savedInstanceState == null) {
            ReceiptDetailFragment fragment = new ReceiptDetailFragment();
            fragment.setArguments(bundle);
            transaction.add(R.id.fragment_container, fragment, ReceiptDetailFragment.class.getName()).commit();
            return fragment;
        }

        String fragmentTag = savedInstanceState.getString("FRAGMENT_NAME", ReceiptDetailFragment.class.getName());
        if (fragmentTag.equals(ReceiptDetailFragment.class.getName())) {
            ReceiptDetailFragment fragment = new ReceiptDetailFragment();
            fragment.setArguments(bundle);
            transaction.add(R.id.fragment_container, fragment, fragmentTag).commit();
            return fragment;
        } else if (fragmentTag.equals(MultimediaFragment.class.getName())) {
            MultimediaFragment fragment = new MultimediaFragment();
            bundle.putInt(Constant.TASK_TYPE, task.getTASK_TYPE());
            bundle.putInt(Constant.TASK_STATE, task.getTASK_STATE());
            fragment.setArguments(bundle);
            transaction.add(R.id.fragment_container, fragment, fragmentTag).commit();
            return fragment;
        }
        return null;
    }

    /**
     * 设置布局显示状态
     *
     * @param isPictureDetail 是否是幻灯片
     */
    public void setLayout(boolean isPictureDetail) {
        if (isPictureDetail) {
            mLayout.setBackgroundColor(Color.BLACK);
            mBottomLayout.setVisibility(View.GONE);
            if (mToolBar != null) {
                mToolBar.setVisibility(View.GONE);
            }
        } else {
            mLayout.setBackgroundColor(Color.parseColor("#0176da"));
            mBottomLayout.setVisibility(View.VISIBLE);
            if (mToolBar != null) {
                mToolBar.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onGetTaskHandle(List<DUHistoryTask> duHistoryTaskList) {
        Intent intent = new Intent(CreateSelfOrderDetailActivity.this, HandleOrderActivity.class);
        if (duHistoryTaskList != null && duHistoryTaskList.size() == 1) {
            DUHistoryTask duHistoryTask = duHistoryTaskList.get(0);
            intent.putExtra(Constant.TASK_ID, duHistoryTask.getTASK_ID());
            intent.putExtra(Constant.ORIGIN, Constant.ORIGIN_CREATE_SELF_ORDER_HISTORY);
            intent.putExtra(Constant.DATA_IS_UPLOAD, duHistoryTask.getUPLOAD_FLAG());
            intent.putExtra(Constant.TASK_REPLY, duHistoryTask.getTASK_REPLY());
            if (!TextUtil.isNullOrEmpty(duHistoryTask.getServerTaskId())) {
                intent.putExtra(Constant.SERVER_TASK_ID, duHistoryTask.getServerTaskId());
            }
            intent.putExtra(DUHistoryTask.DUHistoryTask_ID, duHistoryTask.getID());
        }
        if (duHistoryTaskList == null || duHistoryTaskList.size() == 0) {
            intent.putExtra(Constant.TASK_ID, mHistoryTask.getTASK_ID());
            intent.putExtra(Constant.ORIGIN, Constant.ORIGIN_CREATE_SELF_ORDER);
            intent.putExtra(Constant.ISSUE_TYPE, mIssueType);
            intent.putExtra(Constant.ISSUE_CONTENT, mIssueContent);
        }
        startActivity(intent);
    }
}
