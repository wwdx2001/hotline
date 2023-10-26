package com.sh3h.hotline.ui.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import com.sh3h.hotline.service.SyncConst;
import com.sh3h.hotline.service.SyncService;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class ParentFragment extends Fragment {
    protected Activity mActivity;

    private ProgressDialog mProgressDialog;

    protected static final int PROGRESS_VIEW_OFFSET = 200;

    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;

    public NumberFormat _f = NumberFormat.getInstance();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }


    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();

    protected void startSyncService(int type) {
        Intent intent = SyncService.getStartIntent(getActivity());
        Bundle bundle = new Bundle();
        bundle.putInt(SyncConst.SYNC_TYPE, type);
        intent.putExtras(bundle);
        getActivity().startService(intent);
    }

    protected void startSyncService(int type, String taskId){
        Intent intent = SyncService.getStartIntent(getActivity());
        Bundle bundle = new Bundle();
        bundle.putInt(SyncConst.SYNC_TYPE, type);
        bundle.putString(SyncConst.SYNC_TASK_ID, taskId);
        intent.putExtras(bundle);
        getActivity().startService(intent);
    }

    public void showProgress(String message) {
        if(mProgressDialog == null ){
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    protected void showTopProgressbar(SwipeRefreshLayout swipeRefreshLayout) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setProgressViewOffset(false, 0, PROGRESS_VIEW_OFFSET);
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    protected void hideTopProgressbar(SwipeRefreshLayout swipeRefreshLayout) {
        if ((swipeRefreshLayout != null) && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}
