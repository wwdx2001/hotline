package com.sh3h.hotline.ui.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sh3h.hotline.service.SyncConst;
import com.sh3h.hotline.service.SyncService;

import java.text.NumberFormat;

import butterknife.ButterKnife;

public abstract class BaseLazyFragment extends Fragment {

    protected Activity mActivity;

    private ProgressDialog mProgressDialog;

    /**
     * 标记布局是否已经初始化完毕
     */
    protected boolean isViewCreated;

    /**
     * 标记是否已经加载过数据
     */
    protected boolean isLoadDataCompleted;

    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;

    /**
     * 声明rootView，当Fragment调用 onCreateView 时创建rootView，
     * 当Fragment切换为不可见并调用了 onDestroyView 销毁视图的时候，
     * 这个rootView并不会被销毁。当Fragment 重新切换回来并调用onCreateView重新创建视图
     * 的 时候，直接返回rootView.
     */
    protected View rootView;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayout(), container, false);
            ButterKnife.bind(this, rootView);
            init(rootView);
            //isViewCreated 置为 true
            isViewCreated = true;
        }
        return rootView;
    }

    /**
     * 在这里要调用一次lazyLoadData ，因为ViewPage展示第一页的时候setUserVisibleHint
     * 先于 onCreateView调用，这时候 isViewCreated 为false，不会加载数据。
     * onActivityCreated 在 onCreateView 之后调用，这时候视图已经初始化完
     * 毕 isViewCreated 为 true，所以我们在这里加载数据，保证第一页Fragment正常显示
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        }
    }

    /**
     * 可见
     */
    protected void onVisible() {
        if (isViewCreated && !isLoadDataCompleted()) {
            lazyLoadData();
        }
    }

    /**
     * 不可见
     */
    protected void onInvisible() {
    }

    /**
     * 获取布局文件
     *
     * @return
     */
    protected abstract int getLayout();

    /**
     * 初始化一些view和相关数据
     *
     * @param view
     */
    protected abstract void init(View view);

    /**
     * 延迟加载
     */
    protected void lazyLoadData() {
        isLoadDataCompleted = true;
    }

    public boolean isLoadDataCompleted() {
        return isLoadDataCompleted;
    }

    protected void startSyncService(int type) {
        Intent intent = SyncService.getStartIntent(getActivity());
        Bundle bundle = new Bundle();
        bundle.putInt(SyncConst.SYNC_TYPE, type);
        intent.putExtras(bundle);
        getActivity().startService(intent);
    }

    protected void startSyncService(int type, String taskId) {
        Intent intent = SyncService.getStartIntent(getActivity());
        Bundle bundle = new Bundle();
        bundle.putInt(SyncConst.SYNC_TYPE, type);
        bundle.putString(SyncConst.SYNC_TASK_ID, taskId);
        intent.putExtras(bundle);
        getActivity().startService(intent);
    }

    public void showProgress(String message) {
        if (mProgressDialog == null) {
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

}
