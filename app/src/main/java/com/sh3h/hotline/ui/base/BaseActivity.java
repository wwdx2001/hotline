package com.sh3h.hotline.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.injection.component.ActivityComponent;
import com.sh3h.hotline.injection.component.DaggerActivityComponent;
import com.sh3h.hotline.injection.module.ActivityModule;


public class BaseActivity extends AppCompatActivity {
    private ActivityComponent mActivityComponent;
    private boolean mStartAnimation;
    private boolean mEndAnimation;
    //private AlertDialog mDialog;
    private Toolbar mToolbar;
    private ProgressDialog mProgressDialog;
    private int downX;
    private int downY;
    private double spacing = 20;

    public BaseActivity() {
        mActivityComponent = null;
        mStartAnimation = true;
        mEndAnimation = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setForwardAnimation();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    @Override
//    public void startActivity(Intent intent) {
//        super.startActivity(intent);
//
//        if (mStartAnimation) {
//            // 设置切换动画，从右边进入，左边退出
//            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//        }
//    }

//    @Override
//    public void startActivityForResult(Intent intent, int requestCode) {
//        super.startActivityForResult(intent, requestCode);
//
//        if (mStartAnimation) {
//            // 设置切换动画，从右边进入，左边退出
//            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//        }
//    }

//    @Override
//    public void finish() {
//        super.finish();
//
//        if (mEndAnimation) {
//            // 设置切换动画，从左边进入，右边退出
//            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
//        }
//    }

    public Toolbar initToolBar(int resId) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.mipmap.arrow);
        mToolbar.setTitle(resId);
        setSupportActionBar(mToolbar);
        return mToolbar;
    }

    /**
     * 子标题
     *
     * @param title
     * @param subTitle
     */
    public Toolbar initToolBar(int title, int subTitle) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.mipmap.arrow);
        mToolbar.setTitle(title);
        mToolbar.setSubtitle(subTitle);
        setSupportActionBar(mToolbar);
        return mToolbar;
    }

    public Toolbar initMainToolBar(int title) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mToolbar.setNavigationIcon(R.mipmap.arrow);
        mToolbar.setTitle(title);
//        mToolbar.setSubtitle(subTitle);
        setSupportActionBar(mToolbar);
        return mToolbar;
    }


    public void setToolBarSubTitle(int subTitle) {
        if (mToolbar != null)
            mToolbar.setSubtitle(subTitle);
    }

    public void setToolbarTitle(String title) {
        if (mToolbar != null)
            mToolbar.setTitle(title);
    }

    public void setToolBarSubTitle(CharSequence subTitle) {
        if (mToolbar != null)
            mToolbar.setSubtitle(subTitle);
    }

    public ActivityComponent getActivityComponent() {
        if (mActivityComponent == null) {

            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(MainApplication.get(this).getComponent())
                    .build();

        }
        return mActivityComponent;
    }


    public void setStartAnimation(boolean animation) {
        mStartAnimation = animation;
    }

    public void setEndAnimation(boolean animation) {
        mEndAnimation = animation;
    }

    public void destroy() {
        finish();
    }

    public void setForwardAnimation() {
        setStartAnimation(true);
        setEndAnimation(false);
    }

    public void setBackwardAnimation() {
        setStartAnimation(false);
        setEndAnimation(true);
    }

    public void setBothAnimation() {
        setStartAnimation(true);
        setEndAnimation(true);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK
//                && event.getAction() == KeyEvent.ACTION_DOWN) {
//            finish();
//            return true;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }

    public void showProgress(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
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

    protected void hideKeyboard(IBinder binder, int flags) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binder, flags);
    }


    /**
     * 点击空白区域隐藏软键盘
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) ev.getX();
                int upY = (int) ev.getY();
                if (Math.abs(upX - downX) <= spacing && Math.abs(upY - downY) <= spacing) {
                    View v = getCurrentFocus();
                    if (isShouldHideKeyboard(v, ev)) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

}
