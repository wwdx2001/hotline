package com.sh3h.hotline.ui.base;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.request.DUReply;
import com.sh3h.dataprovider.data.local.preference.PreferencesHelper;
import com.sh3h.hotline.util.ActivityManagerHelper;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.R;
import com.sh3h.hotline.service.SyncConst;
import com.sh3h.hotline.service.SyncService;
import com.sh3h.hotline.service.SyncType;

import javax.inject.Inject;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;


public class ParentActivity extends BaseActivity implements SwipeBackActivityBase {
    @Inject
    PreferencesHelper mPreferencesHelper;

    private static final String TAG = "ParentActivity";
    private static final String RESTORING_STATE = "restoringState";
    protected static final int PROGRESS_VIEW_OFFSET = 100;

    private SwipeBackActivityHelper mHelper;

    //protected boolean isConfigInitSuccess;

    private static final int PERMISSION_REQUEST_CODE = 1000; // 系统权限管理页面的参数
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    public static boolean isAllPermissionsGranted = false;

    public ParentActivity() {
        super();
        //isConfigInitSuccess = true;
      //将前台的activity添加到栈内
      ActivityManagerHelper.getInstance().pushActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        LogUtils.e("deviceToken", "Actiivty onCreate");
        setBothAnimation();
        if (isCehua()) {
            mHelper = new SwipeBackActivityHelper(this);
            mHelper.onActivityCreate();
        }
//        MainApplication.get(this).bindHostService();
//        MainApplication.get(this).initMonitor(isAllPermissionsGranted);
//        if (savedInstanceState != null) {
//            boolean restoringState = savedInstanceState.getBoolean(RESTORING_STATE, false);
//        }


    }

    public boolean isCehua() {
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void checkPermissions() {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && lackPermissions(PERMISSIONS)) {
            requestPermissions(PERMISSIONS, PERMISSION_REQUEST_CODE);
        } else {
            MainApplication.get(this).initConfig();
            isAllPermissionsGranted = true;

//            MainApplication.get(this).initUMPush();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            MainApplication.get(this).initConfig();
            isAllPermissionsGranted = true;
//            MainApplication.get(this).initUMPush();
        } else {
            popupPermissionDialog();
            isAllPermissionsGranted = false;
        }
    }

    private boolean lackPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lackPermissions(permission)) {
                return true;
            }
        }
        return false;
    }

    //判断是否缺少权限
    private boolean lackPermissions(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED;
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    private void popupPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.text_prompt)
                .setMessage(R.string.text_lack_permissions)
                .setCancelable(false)
                .setNegativeButton(R.string.text_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setPositiveButton(R.string.text_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mHelper != null)
            mHelper.onPostCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (MainApplication.get(this).isMonitorInit()) {
//            MobclickAgent.onPageStart(getClass().getName());
//            MobclickAgent.onResume(this);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (MainApplication.get(this).isMonitorInit()) {
//            MobclickAgent.onPageEnd(getClass().getName());
//            MobclickAgent.onPause(this);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if ((v == null) && (mHelper != null)) {
            return mHelper.findViewById(id);
        }
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        if (mHelper == null) {
            return new SwipeBackLayout(this);
        }
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    protected void vibrate(long duration) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {
                0, duration
        };
        vibrator.vibrate(pattern, -1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(RESTORING_STATE, true);
    }

//    public boolean isConfigInitSuccess() {
//        return isConfigInitSuccess;
//    }
//
//    protected void registerEventBus(boolean needCheckConfig) {
//        mEventBus.register(this);
//        if (needCheckConfig) {
//            isConfigInitSuccess = false;
//            if (MainApplication.get(this).isConfigInit()) {
//                isConfigInitSuccess = true;
//                initActivityData();
//            }
//        }
//    }

//    protected void registerEventBus(Activity activity) {
//        mEventBus.register(activity);
//    }
//
//    protected void unregisterEventBus() {
//        mEventBus.unregister(this);
//    }
//
//    protected void unregisterEventBus(Activity activity) {
//        mEventBus.unregister(activity);
//    }

//    @Subscribe
//    public void onInitResult(UIBusEvent.InitResult initResult) {
//        LogUtil.i(TAG, "---onInitResult---" + initResult.isSuccess());
//        if (initResult.isSuccess()) {
//            initActivityData();
//        } else {
//            // TODO
//        }
//    }

//    protected void initActivityData() {
//
//    }

    protected void initParams(Bundle bundle) {
        MainApplication.get(this).initParams(bundle);
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

    protected boolean isConfigInitSuccess() {
        return MainApplication.get(this).isConfigInit();
    }

    protected void startSyncService(int type) {
        Intent intent = SyncService.getStartIntent(this);
        Bundle bundle = new Bundle();
        bundle.putInt(SyncConst.SYNC_TYPE, type);
        intent.putExtras(bundle);
        startService(intent);
    }

    protected void startSyncService(int type, DUReply duReply) {
        Intent intent = SyncService.getStartIntent(this);
        Bundle bundle = new Bundle();
        bundle.putInt(SyncConst.SYNC_TYPE, type);
        //bundle.putParcelable(SyncConst.SYNC_TASK_REPLY, duReply);
        intent.putExtras(bundle);
        startService(intent);
    }

//    protected void startMqttService() {
//        Intent intent = new Intent(this, MQTTService.class);
////        startService(intent);
//    }

    protected void startSyncService(int type, DUHistoryTask duHistoryTask) {
        Intent intent = SyncService.getStartIntent(this);
        Bundle bundle = new Bundle();
        bundle.putInt(SyncConst.SYNC_TYPE, type);
        //bundle.putParcelable(SyncConst.SYNC_TASK_REPLY, duHistoryTask);
        intent.putExtras(bundle);
        startService(intent);
    }

    protected void uploadTasks() {

    }

    public void downloadTasks() {
        Intent intent = SyncService.getStartIntent(this);
        intent.putExtra(SyncConst.SYNC_TYPE, SyncType.DOWNLOAD_ALL_TASK.ordinal());
        startService(intent);
    }

    public void uploadReply(String taskId, int taskType, int taskState, long replyTime) {
        Intent intent = SyncService.getStartIntent(this);
        intent.putExtra(SyncConst.SYNC_TYPE, SyncType.UPLOAD_TASK_REPLY.ordinal());
        intent.putExtra(SyncConst.SYNC_TASK_ID, taskId);
        intent.putExtra(SyncConst.SYNC_TASK_TYPE, taskType);
        intent.putExtra(SyncConst.SYNC_TASK_STATE, taskState);
        intent.putExtra(SyncConst.SYNC_REPLY_TIME, replyTime);
        startService(intent);
    }

    public void uploadReplies(String taskId, int taskType) {
        Intent intent = SyncService.getStartIntent(this);
        intent.putExtra(SyncConst.SYNC_TYPE, SyncType.UPLOAD_HISTORY_TASKS_OF_ONE_TASK.ordinal());
        intent.putExtra(SyncConst.SYNC_TASK_TYPE, taskType);
        intent.putExtra(SyncConst.SYNC_TASK_ID, taskId);
        startService(intent);
    }

    public void uploadCreateSelfOrder(String taskId) {
        Intent intent = SyncService.getStartIntent(this);
        intent.putExtra(SyncConst.SYNC_TYPE, SyncType.UPLOAD_All_CREATE_SELF_ORDER.ordinal());
        intent.putExtra(SyncConst.SYNC_TASK_ID, taskId);
        startService(intent);
    }

    public void uploadCreateSelfOrderList() {
        Intent intent = SyncService.getStartIntent(this);
        intent.putExtra(SyncConst.SYNC_TYPE, SyncType.UPLOAD_All_CREATE_SELF_ORDER.ordinal());
        startService(intent);
    }


    public void uploadMedias(String taskId, int taskType, int taskState) {
        Intent intent = SyncService.getStartIntent(this);
        intent.putExtra(SyncConst.SYNC_TYPE, SyncType.UPLOAD_HISTORY_TASKS_OF_ONE_TASK.ordinal());
        intent.putExtra(SyncConst.SYNC_TASK_ID, taskId);
        intent.putExtra(SyncConst.SYNC_TASK_TYPE, taskType);
        intent.putExtra(SyncConst.SYNC_TASK_STATE, taskState);
        startService(intent);
    }

    /**
     * 上传历史记录所有内容
     *
     * @param taskType
     * @param isFromMyTask 是否来自于我的工单
     */
    public void uploadAllHistoryTasks(int taskType, boolean isFromMyTask) {
        Intent intent = SyncService.getStartIntent(this);
        intent.putExtra(SyncConst.SYNC_TYPE, SyncType.UPLOAD_ALL_HISTORY_TASKS_BY_TASK_TYPE.ordinal());
        intent.putExtra(SyncConst.SYNC_TASK_TYPE, taskType);
        intent.putExtra(SyncConst.SYNC_IS_FROM_MY_TASK, isFromMyTask);
        startService(intent);
    }
}
