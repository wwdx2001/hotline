package com.sh3h.hotline.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sh3h.hotline.R;

public class PopupWindowFilter extends PopupWindow {

    private View mainView;
    private RadioGroup radiogroup, radiogroup2, radiogroup3;

    public PopupWindowFilter(Activity paramActivity, RadioGroup.OnCheckedChangeListener paramOnCheckedChangeListener,
                             int paramInt1, int paramInt2) {
        super(paramActivity);
        //窗口布局
        mainView = LayoutInflater.from(paramActivity).inflate(R.layout.popwin_filter, null);
        //分享布局
        radiogroup = (RadioGroup) mainView.findViewById(R.id.radiogroup);
        //复制布局
        radiogroup2 = (RadioGroup) mainView.findViewById(R.id.radiogroup2);

        radiogroup3 = (RadioGroup) mainView.findViewById(R.id.radiogroup3);
        //设置每个子布局的事件监听器
        if (paramOnCheckedChangeListener != null) {
            radiogroup.setOnCheckedChangeListener(paramOnCheckedChangeListener);
            radiogroup2.setOnCheckedChangeListener(paramOnCheckedChangeListener);
            radiogroup3.setOnCheckedChangeListener(paramOnCheckedChangeListener);
        }
        setContentView(mainView);
        //设置宽度
        setWidth(paramInt1);
        //设置高度
        setHeight(paramInt2);
        //设置显示隐藏动画
        setAnimationStyle(R.style.AnimTools);
        //设置背景透明
        setBackgroundDrawable(new ColorDrawable(0));
    }

    public void setGone(int id, boolean visible) {
        RadioButton radiobutton = (RadioButton) mainView.findViewById(id);
        radiobutton.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setGone(int id, boolean visible, boolean view) {
        if (view) {
            View line = (View) mainView.findViewById(id);
            line.setVisibility(visible ? View.VISIBLE : View.GONE);
        } else {
            RadioGroup radioGroup = (RadioGroup) mainView.findViewById(id);
            radioGroup.setVisibility(visible ? View.VISIBLE : View.GONE);
        }

    }


}
