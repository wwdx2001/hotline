package com.sh3h.hotline.util;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SizeUtils;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/3/6 08:45
 */
public class ToastUtils {
    private static Toast toast = null;

    public static void showToast( String toastContent) {
        View view = LayoutInflater.from(MainApplication.getInstance().getApplicationContext())
                .inflate(R.layout.toast_layout, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_toast);
        textView.setText(toastContent);
        if (toast != null) {
            toast.setView(view);
        } else {
            toast = new Toast(MainApplication.getInstance().getApplicationContext());
            toast.setView(view);
            toast.setGravity(Gravity.BOTTOM, 0,  SizeUtils.dp2px(86f));
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static void showLongToast(String toastContent) {
        View view = LayoutInflater.from(MainApplication.getInstance().getApplicationContext())
                .inflate(R.layout.toast_layout, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_toast);
        textView.setText(toastContent);
        if (toast != null) {
            toast.setView(view);
        } else {
            toast = new Toast(MainApplication.getInstance().getApplicationContext());
            toast.setView(view);
            toast.setGravity(Gravity.BOTTOM, 0, SizeUtils.dp2px(86f));
            toast.setDuration(Toast.LENGTH_LONG);
        }
        toast.show();
    }
}
