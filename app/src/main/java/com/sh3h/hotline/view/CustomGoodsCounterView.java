package com.sh3h.hotline.view;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sh3h.hotline.R;

public class CustomGoodsCounterView extends FrameLayout implements View.OnClickListener {
    /**
     * 商品数量
     */
    int mGoodsNumber = 1;
    /**
     * 最小商品数量
     */
    int mMinCount = 1;
    /**
     * 库存商品数量
     */
    int mMaxCount;

    private EditText tvNumber;
    private TextView tvAdd;
    private TextView tvSub;

    private UpdateGoodsNumberListener mUpdateGoodsNumberListener;

    public CustomGoodsCounterView(Context context) {
        this(context, null);
    }

    public CustomGoodsCounterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomGoodsCounterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.layout_number_add_sub_layout, this, false);
        tvNumber = (EditText) rootView.findViewById(R.id.tv_number);
        tvAdd = (TextView) rootView.findViewById(R.id.tv_add);
        tvSub = (TextView) rootView.findViewById(R.id.tv_sub);
        tvAdd.setOnClickListener(this);
        tvSub.setOnClickListener(this);
//        tvNumber.setFilters(new InputFilter[]{new NumberValueFilter()});
        tvNumber.setEnabled(false);

        addView(rootView);

    }

    public void setEnable(boolean isEnable) {
        tvAdd.setEnabled(isEnable);
        tvSub.setEnabled(isEnable);
        tvNumber.setEnabled(isEnable);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_add) {
            addNumber();
        } else if (i == R.id.tv_sub) {
            subNumber();
        }
        updateGoodsNumber();
    }

    public EditText getTvNumber() {
        return tvNumber;
    }

    /**
     * 更新商品数量
     */
    public void updateGoodsNumber() {
        tvNumber.setText(String.valueOf(mGoodsNumber));
        tvNumber.setSelection(tvNumber.getText().toString().trim().length());
        addUpdateListener();
    }

    private void addUpdateListener() {
        if (mUpdateGoodsNumberListener != null) {
            mUpdateGoodsNumberListener.updateGoodsNumber(mGoodsNumber);
        }
    }

    public void addNumber() {
//        ++mGoodsNumber;
        if (mMaxCount == 0) {
            mGoodsNumber = mMinCount;
        } else {
            mGoodsNumber = (mGoodsNumber + 1 > mMaxCount) ? mMaxCount : mGoodsNumber + 1;
        }
    }

    public void subNumber() {
        mGoodsNumber = (mGoodsNumber - 1 < mMinCount) ? mMinCount : mGoodsNumber - 1;
    }

    /**
     * 获取库存商品数量
     *
     * @return
     */
    public int getmMaxCount() {
        return mMaxCount;
    }

    public void setmMaxCount(int mMaxCount) {
        this.mMaxCount = mMaxCount;
    }

    /**
     * 获取最小商品数量
     *
     * @return
     */
    public int getmMinCount() {
        return mMinCount;
    }

    public void setmMinCount(int mMinCount) {
        this.mMinCount = mMinCount;
    }

    /**
     * 获取商品数量
     *
     * @return
     */
    public int getGoodsNumber() {
        return mGoodsNumber;
    }

    public void setGoodsNumber(int goodsNumber) {
        mGoodsNumber = goodsNumber;
//        tvNumber.setText(String.valueOf(mGoodsNumber));
//        tvNumber.setSelection(tvNumber.getText().toString().trim().length());
//        updateGoodsNumber();
    }

    public void setUpdateGoodsNumberListener(UpdateGoodsNumberListener listener) {
        mUpdateGoodsNumberListener = listener;
    }

    /**
     * 更新商品数量监听器
     */
    public interface UpdateGoodsNumberListener {
        void updateGoodsNumber(int number);
    }
}
