package com.sh3h.hotline.util;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * User:
 * Date: 2023/6/12
 * Time: 17:07
 * Desc: maxLine超过1行时，省略号失效问题解决方案
 *
 * 使用：
 * TextView attention = findviewById(R.id.attention);
 * TextViewUtil.setMaxEcplise(attention, 2, "内容");
 */
public class TextViewUtil {

    /**
     * 参数：maxLines 要限制的最大行数
     * 参数：content  指TextView中要显示的内容
     */
    public static void setMaxEcplise(final TextView mTextView, final int maxLines, final String content) {


        ViewTreeObserver observer = mTextView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTextView.setText(content);
                if (mTextView.getLineCount() > maxLines) {
                    int lineEndIndex = mTextView.getLayout().getLineEnd(maxLines - 1);
                    //下面这句代码中：我在项目中用数字3发现效果不好，改成1了

                    String text = content.subSequence(0, lineEndIndex - 3) + "...";
                    mTextView.setText(text);
                } else {
                    removeGlobalOnLayoutListener(mTextView.getViewTreeObserver(), this);
                }
            }
        });
    }


    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private static void removeGlobalOnLayoutListener(ViewTreeObserver obs, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (obs == null)
            return;
        if (Build.VERSION.SDK_INT < 16) {
            obs.removeGlobalOnLayoutListener(listener);
        } else {
            obs.removeOnGlobalLayoutListener(listener);
        }
    }
}
