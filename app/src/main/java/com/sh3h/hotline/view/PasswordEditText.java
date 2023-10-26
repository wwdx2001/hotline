package com.sh3h.hotline.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.sh3h.hotline.R;


/**
 * 密码输入框（拓展，右边图片实现在xml布局中声明）
 */
@SuppressLint("AppCompatCustomView")
public class PasswordEditText extends EditText implements View.OnFocusChangeListener, TextWatcher {

    private Drawable mRightDrawable;//左右图片
    private boolean hasFocus = false;//是否获取焦点，默认不获取

    public PasswordEditText(Context context) {
        super(context);
        init();
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PasswordEditText(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        init();
    }

    private void init() {
        mRightDrawable = getCompoundDrawables()[2];//对应右边图片，其中[0,1,2,3]分别对应左上右下
        if (mRightDrawable == null) {
            mRightDrawable = getResources().getDrawable(R.drawable.ic_baseline_visibility_off);
        }
        mRightDrawable.setBounds(0, 0, mRightDrawable.getIntrinsicWidth(),
                mRightDrawable.getIntrinsicHeight());
        setTransformationMethod(PasswordTransformationMethod.getInstance());
        // 默认设置隐藏图标
        setClearIconVisible(false);
        setOnFocusChangeListener(this);//设置监听
        addTextChangedListener(this);
    }

    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mRightDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    boolean isShow = true;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (hasFocus) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                Rect rect = getCompoundDrawables()[2].getBounds();
                int height = rect.height();
                int distance = (getHeight() - height) / 2;
                boolean isInnerWidth = x > (getWidth() - getTotalPaddingRight()) && x < (getWidth() - getPaddingRight());
                boolean isInnerHeight = y > distance && y < (distance + height);
                if (isShow && isInnerWidth && isInnerHeight) {
                    mRightDrawable = getResources().getDrawable(R.drawable.ic_baseline_remove_red_eye);
                    mRightDrawable.setBounds(0, 0, mRightDrawable.getIntrinsicWidth(),
                            mRightDrawable.getIntrinsicHeight());
                    setCompoundDrawables(getCompoundDrawables()[0],
                            getCompoundDrawables()[1], mRightDrawable, getCompoundDrawables()[3]);
                    setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    setSelection(getText().toString().length());
                    isShow = !isShow;
                } else if (!isShow && isInnerWidth && isInnerHeight) {
                    mRightDrawable = getResources().getDrawable(R.drawable.ic_baseline_visibility_off);
                    mRightDrawable.setBounds(0, 0, mRightDrawable.getIntrinsicWidth(),
                            mRightDrawable.getIntrinsicHeight());
                    setCompoundDrawables(getCompoundDrawables()[0],
                            getCompoundDrawables()[1], mRightDrawable, getCompoundDrawables()[3]);
                    setTransformationMethod(PasswordTransformationMethod.getInstance());
                    setSelection(getText().toString().length());
                    isShow = !isShow;
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
