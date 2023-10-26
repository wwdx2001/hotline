package cn.bertsir.zbar;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.google.zxing.common.StringUtils;

import java.util.List;

import cn.bertsir.zbar.utils.PermissionConstants;
import cn.bertsir.zbar.utils.PermissionUtils;

/**
 * Created by Bert on 2017/9/22.
 */

public class QrManager {

    private static QrManager instance;
    private QrConfig options;

    public OnScanResultCallback resultCallback;

    public synchronized static QrManager getInstance() {
        if (instance == null)
            instance = new QrManager();
        return instance;
    }

    public OnScanResultCallback getResultCallback() {
        return resultCallback;
    }


    public QrManager init(QrConfig options) {
        this.options = options;
        return this;
    }

    public void startScan(final Activity activity, OnScanResultCallback resultCall, final String scanType) {
        if (options == null) {
            options = new QrConfig.Builder().create();
        }
        PermissionUtils.permission(activity, PermissionConstants.CAMERA, PermissionConstants.STORAGE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
                        shouldRequest.again(true);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        Intent intent = new Intent(activity, QRActivity.class);
                        intent.putExtra(QrConfig.EXTRA_THIS_CONFIG, options);
                        if (scanType != null && !scanType.equals("")) {
                            intent.putExtra(QrConfig.QR_SCAN_KEY, scanType);
                        }
                        activity.startActivity(intent);
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        Toast.makeText(activity, "摄像头权限被拒绝！", Toast.LENGTH_SHORT).show();

                    }
                }).request();


        // 绑定图片接口回调函数事件
        resultCallback = resultCall;
    }


    public interface OnScanResultCallback {
        /**
         * 处理成功
         * 多选
         *
         * @param result
         */
        void onScanSuccess(String result);

    }
}
