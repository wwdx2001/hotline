package com.sh3h.hotline;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.aliyun.common.httpfinal.QupaiHttpFinal;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hjq.toast.Toaster;
import com.sh3h.dataprovider.BaseApplication;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.preference.PreferencesHelper;
import com.sh3h.dataprovider.data.local.preference.UserSession;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.DUMyTaskDao;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.hotline.entity.GaiPaiEntity;
import com.sh3h.hotline.entity.ShenHeJGEntity;
import com.sh3h.hotline.entity.UploadSuccess;
import com.sh3h.hotline.event.UIBusEvent;
import com.sh3h.hotline.injection.component.ApplicationComponent;
import com.sh3h.hotline.injection.component.DaggerApplicationComponent;
import com.sh3h.hotline.injection.module.ApplicationModule;
import com.sh3h.hotline.service.AlertTasksService;
import com.sh3h.hotline.service.SyncConst;
import com.sh3h.hotline.service.SyncService;
import com.sh3h.hotline.service.SyncType;
import com.sh3h.hotline.ui.order.myorder.handle.HandleOrderActivity;
import com.sh3h.hotline.util.ConstDataUtil;
import com.sh3h.hotline.util.DeviceUtil;
import com.sh3h.hotline.util.SystemUtil;
import com.sh3h.ipc.IMainService;
import com.sh3h.ipc.IRemoteServiceCallback;
import com.sh3h.ipc.location.MyLocation;
import com.sh3h.ipc.module.MyModule;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainApplication extends BaseApplication {
    private static final String TAG = "ChildMainApplication";
    public static final String MAP_PACKAGE_NAME = "com.sh3h.citymap";
    public static final String MAP_AIDL_SERVICE_NAME = "com.sh3h.citymap.service.MapAidlService";
    private static final String PACKAGE_BASE_NAME = "com.sh3h";
    private static final String HOST_SERVICE_NAME = "com.sh3h.mainshell.service.HostService";
    private static final String BINDING_NAME = "bindingName";

    @Inject
    Bus mEventBus;

    @Inject
    DataManager mDataManager;

    @Inject
    ConfigHelper mConfigHelper;

    @Inject
    PreferencesHelper mPreferencesHelper;

    @Inject
    EventPosterHelper mEventPosterHelper;

    private ApplicationComponent mApplicationComponent;

    private long timeError;

    private boolean isMonitorInit;
    private boolean mIsConfigInit;

    private Subscription mSubscription;
    private IMainService mainService;
    private boolean mIsGpsLocated;
    private MyLocation mMyLocation;


    //时间
    private long currentTime = 0;
    private static long intervalTime = 2592000000L;//30天
    private String mDeviceToken;
    private PushAgent mMPushAgent;

    public MainApplication() {
        mApplicationComponent = null;
        timeError = 0;
        isMonitorInit = false;
        mSubscription = null;
        mainService = null;
        mIsGpsLocated = false;
        mMyLocation = null;
        LogUtil.i(TAG, "new");
    }

    public static MainApplication mInstance;

    public static MainApplication getInstance() {
        return mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.i(TAG, "onCreate");
        mInstance = this;
//        EasyHttp.getInstance().addInterceptor(new RetryAndChangeIpInterceptor(1));
        //腾讯bugly
        Bugly.init(this, "2b29995f04", false);
//        CrashReport.initCrashReport(getApplicationContext(), "2b29995f04", true);

        bindHostService();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        Utils.init(this);
        ToastUtils.setMsgTextSize(16);
        // 初始化 Toast 框架
        Toaster.init(this);
        initAliYun();
//        initUMPush();
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        mApplicationComponent.inject(this);
        mIsConfigInit = false;
        mEventBus.register(this);
    }

//    public void registUMPush() {
//        if (mMPushAgent != null) {
//            //注册推送服务，每次调用register方法都会回调该接口
//            mMPushAgent.register(new IUmengRegisterCallback() {
//                @Override
//                public void onSuccess(String deviceToken) {
//                    //注册成功会返回device token
//                    LogUtils.e("ummessage", "友盟注册返回的token=" + deviceToken);
//                    uploadDeviceToken(userId, deviceToken);
//                }
//
//                @Override
//                public void onFailure(String s, String s1) {
//                }
//            });
//        }
//
//    }


    public void initUMPush() {
//        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "831c6425a93c2b578ac385df0b3f55f8");
        UMConfigure.init(this, "5ca1d2c93fc195a1a60007e8",
                "Hotline", UMConfigure.DEVICE_TYPE_PHONE, "831c6425a93c2b578ac385df0b3f55f8");
        mMPushAgent = PushAgent.getInstance(this);
        mMPushAgent.setNotificaitonOnForeground(true);
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                super.getNotification(context, msg);//不能用super，否则会不显示通知
                try {
                    LogUtils.e("ummessage", "custom=" + msg.custom);
                    LogUtils.e("ummessage", "alias=" + msg.alias);
                    LogUtils.e("ummessage", "ticker=" + msg.ticker);
                    Map<String, String> map = msg.extra;
                    LogUtils.e("ummessage", map.get("type") + "   " + map.get("data"));
                    Gson gson = new Gson();
                    if (map.get("type").equals("gd")) {//推送工单
                        final DUMyTask duMyTask = gson.fromJson(map.get("data"), DUMyTask.class);
                        duMyTask.setClsxLong(TimeUtils.string2Millis(duMyTask.getClsx()));
//                        duMyTask.setID(Integer.valueOf(duMyTask.getFaId().hashCode()).longValue());
                        GreenDaoUtils.getAsyncSession(MainApplication.this)
                                .runInTx(new Runnable() {
                                    @Override
                                    public void run() {
                                        GreenDaoUtils.getDaoSession(MainApplication.this)
                                                .getDUMyTaskDao().insertOrReplaceInTx(duMyTask);
                                        mEventPosterHelper.postEventSafely(new UIBusEvent.InitResult(true));
                                    }
                                });
                        LogUtils.e("testUM", map.get("data"));
                    } else if (map.get("type").equals("gp")) {//工单改派
                        GaiPaiEntity gaiPaiEntity = gson.fromJson(map.get("data"), GaiPaiEntity.class);
                        final String faId = gaiPaiEntity.getFaId();
                        final String caseId = gaiPaiEntity.getCaseId();
                        String actFlg = gaiPaiEntity.getActFlg();
                        if (actFlg.equals("00")) {
                            GreenDaoUtils.getAsyncSession(ActivityUtils.getTopActivity())
                                    .runInTx(new Runnable() {
                                        @Override
                                        public void run() {
                                            List<DUMyTask> duMyTasks = GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                                    .getDUMyTaskDao().loadAll();
                                            for (int i = 0; i < duMyTasks.size(); i++) {
                                                if (faId.equals(duMyTasks.get(i).getFaId()) &&
                                                        caseId.equals(duMyTasks.get(i).getCaseId()) &&
                                                        duMyTasks.get(i).getCmSta().equals("D")) {
                                                    GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                                            .getDUMyTaskDao().delete(duMyTasks.get(i));
                                                    //发送通知删除
                                                    mEventPosterHelper.postEventSafely(new UIBusEvent.InitResult(true));
                                                }
                                            }
                                        }
                                    });

                        }
                    } else if (map.get("type").equals("sh")) {//工单审核结果
                        LogUtils.e("testUM_sh", map.get("data"));
                        final ShenHeJGEntity shenHeJGEntity = gson.fromJson(map.get("data"), ShenHeJGEntity.class);
                        final String faId = shenHeJGEntity.getFaId();
                        final String caseId = shenHeJGEntity.getCaseId();
                        if ("00".equals(shenHeJGEntity.getResult())) {//审核通过
                            GreenDaoUtils.getAsyncSession(ActivityUtils.getTopActivity())
                                    .runInTx(new Runnable() {
                                        @Override
                                        public void run() {
                                            List<DUMyTask> duMyTasks = GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                                    .getDUMyTaskDao().loadAll();
                                            if (shenHeJGEntity.getApplyType().equals("00")) {//退单审核成功
                                                for (int i = 0; i < duMyTasks.size(); i++) {
                                                    if (faId.equals(duMyTasks.get(i).getFaId()) &&
                                                            caseId.equals(duMyTasks.get(i).getCaseId())) {
                                                        GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                                                .getDUMyTaskDao().delete(duMyTasks.get(i));
                                                        //发送通知删除
                                                        mEventPosterHelper.postEventSafely(new UIBusEvent.InitResult(true));
                                                    }
                                                }
                                            } else if (shenHeJGEntity.getApplyType().equals("01")) {//延期审核成功
                                                for (int i = 0; i < duMyTasks.size(); i++) {
                                                    if (faId.equals(duMyTasks.get(i).getFaId()) &&
                                                            caseId.equals(duMyTasks.get(i).getCaseId())) {
                                                        Log.e("hello", "clsx=" + shenHeJGEntity.getDelayDt());
                                                        duMyTasks.get(i).setClsx(shenHeJGEntity.getDelayDt());
                                                        duMyTasks.get(i).setClsxLong(TimeUtils.string2Millis(shenHeJGEntity.getDelayDt()));
                                                        GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                                                .getDUMyTaskDao().update(duMyTasks.get(i));
                                                        //发送通知删除
                                                        mEventPosterHelper.postEventSafely(new UIBusEvent.InitResult(true));
                                                    }
                                                }
                                            } else if (shenHeJGEntity.getApplyType().equals("02")) {//销单成功

                                            }
                                        }
                                    });
                        } else if ("01".equals(shenHeJGEntity.getResult())) {//审核不通过
                            GreenDaoUtils.getAsyncSession(ActivityUtils.getTopActivity())
                                    .runInTx(new Runnable() {
                                        @Override
                                        public void run() {
                                            List<DUMyTask> duMyTasks = GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                                    .getDUMyTaskDao().loadAll();
                                            if (shenHeJGEntity.getApplyType().equals("00")) {//退单审核成功
                                                for (int i = 0; i < duMyTasks.size(); i++) {
                                                    if (faId.equals(duMyTasks.get(i).getFaId()) &&
                                                            caseId.equals(duMyTasks.get(i).getCaseId())) {
                                                        //                                                    GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                                        //                                                            .getDUMyTaskDao().delete(duMyTasks.get(i));
                                                        //发送通知删除

                                                        mEventPosterHelper.postEventSafely(new UIBusEvent.InitResult(true));
                                                    }
                                                }
                                            } else if (shenHeJGEntity.getApplyType().equals("01")) {//延期审核成功
                                                for (int i = 0; i < duMyTasks.size(); i++) {
                                                    if (faId.equals(duMyTasks.get(i).getFaId()) &&
                                                            caseId.equals(duMyTasks.get(i).getCaseId())) {
                                                        //                                                    duMyTasks.get(i).setClsx(shenHeJGEntity.getDelayDt());
                                                        //                                                    duMyTasks.get(i).setClsxLong(TimeUtils.string2Millis(shenHeJGEntity.getDelayDt()));
                                                        //                                                    GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                                        //                                                            .getDUMyTaskDao().update(duMyTasks.get(i));
                                                        //发送通知删除
                                                        mEventPosterHelper.postEventSafely(new UIBusEvent.InitResult(true));
                                                    }
                                                }
                                            } else if (shenHeJGEntity.getApplyType().equals("02")) {//销单失败
//                                                mEventPosterHelper.postEventSafely(new CommitSuccess());
                                                mEventPosterHelper.postEventSafely(new UIBusEvent.InitResult(true));
                                            }
                                        }
                                    });
                        }
                    }
                } catch (Exception e) {
                    LogUtils.e("umessage", "推送的消息格式不正确");
                    return super.getNotification(context, msg);
                }
                return super.getNotification(context, msg);
            }
        };
        PushAgent.getInstance(this).setMessageHandler(messageHandler);

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void openActivity(Context context, UMessage uMessage) {
                super.openActivity(context, uMessage);
            }

            @Override
            public void launchApp(Context context, UMessage uMessage) {
//                super.launchApp(context, uMessage);
                Gson gson = new Gson();
                Map<String, String> map = uMessage.extra;
                if ("gd".equals(map.get("type"))) {//打开推送工单
                    try {
                        final DUMyTask duMyTask = gson.fromJson(map.get("data"), DUMyTask.class);
                        Intent intent2 = new Intent(ActivityUtils.getTopActivity(), HandleOrderActivity.class);
                        intent2.putExtra(Constant.DUMyTask, duMyTask);
                        intent2.putExtra(Constant.ORIGIN, Constant.ORIGIN_MY_TASK);
                        intent2.putExtra(Constant.TASK_ID, duMyTask.getFaId());
                        intent2.putExtra(Constant.ISSUE_TYPE, TextUtil.getString(duMyTask.getIssueType()));
                        intent2.putExtra(Constant.ISSUE_CONTENT, TextUtil.getString(duMyTask.getIssueContent()));
                        startActivity(intent2);
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {

            }
        };
        mMPushAgent.setNotificationClickHandler(notificationClickHandler);
        //注册推送服务，每次调用register方法都会回调该接口
        mMPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                LogUtils.e("ummessage", "友盟注册返回的token=" + deviceToken);
                uploadDeviceToken(userId, deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
            }
        });
    }


    /**
     * 初始化阿里云
     */
    private void initAliYun() {
        System.loadLibrary("openh264");
        System.loadLibrary("encoder");
        System.loadLibrary("QuCore-ThirdParty");
        System.loadLibrary("QuCore");
        QupaiHttpFinal.getInstance().initOkHttpFinal();
    }

    private void isExistDeadlineTasks() {
//        Observable<Boolean> observable = mDataManager.isAlertDeadlineTasks();
//        if (observable == null) {
//            return;
//        }
        List<DUMyTask> duMyTasks1 = GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                .getDUMyTaskDao().loadAll();
        for (int i = 0; i < duMyTasks1.size(); i++) {
            LogUtils.e("临期工单1", "------" + duMyTasks1.get(i).getCmSta());
        }
        List<DUMyTask> duMyTasks = GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                .getDUMyTaskDao().queryBuilder()
                .whereOr(DUMyTaskDao.Properties.CmSta.eq("A"), DUMyTaskDao.Properties.CmSta.eq("D"))
                .where(DUMyTaskDao.Properties.State.notEq(Constant.TASK_STATE_FINISH))
                .where(DUMyTaskDao.Properties.ClsxLong.ge(System.currentTimeMillis()))
                .where(DUMyTaskDao.Properties.ClsxLong.le(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .list();
        LogUtils.e("临期工单", "size=" + duMyTasks.size());
        if (duMyTasks != null && duMyTasks.size() > 0) {
            LogUtils.e("临期工单", "size=" + duMyTasks.get(0).getCmSta());
            Intent intent = AlertTasksService.getStartIntent(MainApplication.this);
            startService(intent);
        }
//        observable.subscribeOn(Schedulers.io()).
//                subscribe(new Subscriber<Boolean>() {
//                    @Override
//                    public void onCompleted() {
//                        LogUtil.i(TAG, "isExistDeadlineTasks completed");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LogUtil.i(TAG, e.toString());
//                    }
//
//                    @Override
//                    public void onNext(Boolean aBoolean) {
//                        LogUtil.i(TAG, "isExistDeadlineTasks onNext:" + aBoolean);
//                        Constant.isAlertDeadline = aBoolean;
//                        if (aBoolean) {
//                            Intent intent = AlertTasksService.getStartIntent(MainApplication.this);
//                            startService(intent);
//                        }
//                    }
//                });
    }

    public void initConfig() {
        if (mIsConfigInit) {
            mEventPosterHelper.postEventSafely(new UIBusEvent.InitResult(true));
            return;
        }

        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
            Log.i(TAG, "---initConfig---2");
        }

        Log.i(TAG, "---initConfig---3");
        mSubscription = mConfigHelper.initDefaultConfigs(mDataManager)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "---initConfig onCompleted---");
                        mDataManager.initLogger();
                        mEventPosterHelper.postEventSafely(new UIBusEvent.InitResult(true));
                        mIsConfigInit = true;
//                        isExistDeadlineTasks();//判断是否存在临期工单
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "---initConfig onError---" + e.getMessage());
                        mEventPosterHelper.postEventSafely(new UIBusEvent.InitResult(false));
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        Log.i(TAG, "---initConfig onNext---");
                    }
                });
    }

    public boolean isConfigInit() {
        return mIsConfigInit;
    }

    Integer userId;

    public void initParams(Bundle bundle) {
        String account;
        String userName;
        String accessToken;
        UserSession userSession = mPreferencesHelper.getUserSession();
        if (bundle != null) {
            account = TextUtil.getString(bundle.getString(ConstDataUtil.ACCOUNT));
            SPUtils.getInstance().put(Constant.USERID, account);
            userId = bundle.getInt(ConstDataUtil.USER_ID);
            userName = TextUtil.getString(bundle.getString(ConstDataUtil.USER_NAME));
            accessToken = TextUtil.getString(bundle.getString(ConstDataUtil.ACCESS_TOKEN));

            userSession.setAccount(TextUtil.getString(account));
            userSession.setUserId(userId);
            userSession.setUserName(TextUtil.getString(userName));
            userSession.set_accessToken(TextUtil.getString(accessToken));
            mPreferencesHelper.saveUserSession();
        } else {

//            account = "3h_test1";
//            userId = 4292;
//            userName = "测试";
//            accessToken = "F6D62986-915F-4DD8-963A-B0632F-149E18";

//            account = "111090";
//            account = "111032";
//            userId = 3500;
//            userName = "李久云";

//            account = "09167";
//            userId = 4294;
//            userName = "马开荣";

//            account = "404318";
//            userId = 4292;
//            userName = "孟晶";

//            account = "111030";
//            userId = 4294;
//            userName = "金星";

//        account = "16168";
//        userId = 4295;
//        userName = "李久云";

//        account = "111057";
//        userId = 4293;
//        userName = "王建平";

//            account = "ljz1";
//            userId = 3497;
//            userName = "罗泾站1";
        }
        //将友盟deviceToken发送给服务端
//        if (StringUtils.isEmpty(SPUtils.getInstance().getString(Constant.DEVICE_TOKEN))) {
//            //友盟初始化失败，重新初始化
////            LogUtils.e("deviceToken", "重新初始化UM");
//            initUMPush();
//        } else {
//            //将deviceToken发送给服务端
//            postDeviceToken(userId, SPUtils.getInstance().getString(Constant.DEVICE_TOKEN));
//        }

//        account = "111090";
//        account = "111032";
//        userId = 3500;
//        userName = "李久云";
//        accessToken = "F6D62986-915F-4DD8-963A-B0632F-149E18";

//        account = "111030";
//        userId = 4294;
//        userName = "金星";

//        account = "404318";
//        userId = 4292;
//        userName = "孟晶";

//        account = "570016";
//        userId = 4293;
//        userName = "莫安顺";

//        account = "09167";
//        userId = 4294;
//        userName = "马开荣";

//        account = "08953 ";
//        userId = 4295;
//        userName = "朱振华";

//        userSession.setAccount(TextUtil.getString(account));
//        userSession.setUserId(userId);
//        userSession.setUserName(TextUtil.getString(userName));
//        userSession.set_accessToken(TextUtil.getString(accessToken));
//        mPreferencesHelper.saveUserSession();
    }

    public void updateSession(int userId, String account, String userName,String accessToken) {
        UserSession userSession = mPreferencesHelper.getUserSession();
        userSession.setAccount(TextUtil.getString(account));
        userSession.setUserId(userId);
        userSession.setUserName(TextUtil.getString(userName));
        userSession.set_accessToken(TextUtil.getString(accessToken));
        mPreferencesHelper.saveUserSession();
    }

//    private void postDeviceToken(int userId, String deviceToken) {
////        LogUtils.e("deviceToken", "postDeviceToken");
//        //没有推送给服务端
////        LogUtils.e("testDeviceToken","state==0");
////        LogUtils.e("testDeviceToken",mDeviceToken);
////        LogUtils.e("testDeviceToken1",deviceToken);
////        LogUtils.e("testDeviceToken2",SPUtils.getInstance().getBoolean(Constant.IS_POST_DEVICE_TOKEN_SERVICE, false));
//        LogUtils.e("deviceToken", "deviceToken--->友盟注册token---------" + deviceToken);
//        LogUtils.e("deviceToken", "mDeviceToken-->本地保存token---------" + mDeviceToken);
//        if (!SPUtils.getInstance().getBoolean(Constant.IS_POST_DEVICE_TOKEN_SERVICE, false) ||
//                !deviceToken.equals(mDeviceToken)) {
//            uploadDeviceToken(userId, deviceToken);
//        }
//    }

    private void uploadDeviceToken(Integer userId, final String deviceToken) {
        LogUtils.e("deviceToken", "----------account=" + SPUtils.getInstance().getString(Constant.USERID));
        EasyHttp.post(URL.UpdataToken)
                .params("repCd", SPUtils.getInstance().getString(Constant.USERID))
//                .params("repCd", "DANDAN")
                .params("deviceToken", deviceToken)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        LogUtils.e("deviceToken", "onStart");
                    }

                    @Override
                    public void onError(ApiException e) {
                        LogUtils.e("deviceToken", "error" + e.getMessage());
                    }

                    @Override
                    public void onSuccess(String s) {
                        LogUtils.e("deviceToken", s + "上传的token=" + deviceToken);
                        Gson gson = new Gson();
                        UploadSuccess uploadSuccess = gson.fromJson(s, UploadSuccess.class);
                        if (uploadSuccess.getState() == 0) {
                            SPUtils.getInstance().put(Constant.IS_POST_DEVICE_TOKEN_SERVICE, true);
                            mDeviceToken = SPUtils.getInstance().getString(Constant.DEVICE_TOKEN);
//                                LogUtils.e("testDeviceToken","state==0"+ SPUtils.getInstance().getBoolean(Constant.IS_POST_DEVICE_TOKEN_SERVICE));
                        } else {
                            SPUtils.getInstance().put(Constant.IS_POST_DEVICE_TOKEN_SERVICE, false);
                        }
                    }
                });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
        // 安装tinker
        Beta.installTinker();
    }

    public static MainApplication get(Context context) {
        return (MainApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    public ConfigHelper getConfigHelper() {
        return mConfigHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public void setTimeError(long timeError) {
        this.timeError = timeError;
    }

    public long getCurrentTime() {
        long time = new Date().getTime();
        return time + timeError;
    }

    public long getCurrentReadTime() {
        return currentTime;
    }

    public Date getCurrentDate() {
        return new Date(getCurrentTime());
    }

    public boolean isMonitorInit() {
        return isMonitorInit;
    }

    public void initMonitor(boolean isAllPermissionsGranted) {
        if (isMonitorInit || !isAllPermissionsGranted) {
            return;
        }

        //开启调试模式
        MobclickAgent.setDebugMode(mConfigHelper.isDebugMode());
        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
        //设置统计场景类型
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        //设置是否对日志信息进行加密, 默认false(不加密)
        MobclickAgent.enableEncrypt(false);
        MobclickAgent.onProfileSignIn(TextUtil.getString(DeviceUtil.getDeviceID(this)));
        isMonitorInit = true;
    }

    public void destroyMonitor() {
        if (isMonitorInit) {
            isMonitorInit = false;
            MobclickAgent.onProfileSignOff();
            MobclickAgent.onKillProcess(this);
        }
    }

    /***
     * Android L (lollipop, API 21) introduced a new problem when trying to invoke implicit intent,
     * "java.lang.IllegalArgumentException: Service Intent must be explicit"
     * <p/>
     * If you are using an implicit intent, and know only 1 target would answer this intent,
     * This method will help you turn the implicit intent into the explicit form.
     * <p/>
     * Inspired from SO answer: http://stackoverflow.com/a/26318757/1446466
     *
     * @param context
     * @param implicitIntent - The original implicit intent
     * @return Explicit Intent created from the implicit original intent
     */
    private static Intent createExplicitFromImplicitIntent(Context context, Intent
            implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }

    /**
     * download all words
     */
    private void downloadWords() {
        Intent intent = SyncService.getStartIntent(this);
        intent.putExtra(SyncConst.SYNC_TYPE, SyncType.DOWNLOAD_WORDS.ordinal());
        startService(intent);
    }

    @Subscribe
    public void onDownloadResult(UIBusEvent.DownloadResult downloadResult) {
        LogUtil.i(TAG, "onDownloadResult");
        int aa = R.string.text_download_word_success;
        if (downloadResult.getType() == UIBusEvent.DownloadResult.Type.WORD) {
//            ApplicationsUtil.showMessage(this,
//                    downloadResult.isSuccess()
//                            ? R.string.text_null
//                            : R.string.text_download_word_failure);
            if (!downloadResult.isSuccess()) {
//                ToastUtils.showShort(R.string.text_download_word_failure);
            }
        }
        try {
            if (mainService == null || mainService.getCurrentTime() == 0) {
                return;
            }
            currentTime = mainService.getCurrentTime();//系统时间
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Observable<Boolean> observable = mDataManager.deleteHistoryTasks(currentTime, intervalTime);
        if (observable == null) {
            return;
        }
        observable.subscribeOn(Schedulers.io()).
                subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "deleteHistoryTasks completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, e.toString());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.i(TAG, "deleteHistoryTasks onNext:" + aBoolean);
                    }
                });
    }

    @Subscribe
    public void onInitResult(UIBusEvent.InitResult initResult) {
        LogUtil.i(TAG, "---onInitResult---" + initResult.isSuccess());
//        initUMPush();
//        if (initResult.isSuccess()) {
//            downloadWords();
//        }
    }

    public void bindHostService() {
        LogUtils.i(TAG, "---bindHostService---1");
        if (mainService == null) {
            LogUtils.i(TAG, "---bindHostService---2");
            Intent intent = new Intent(HOST_SERVICE_NAME);
            intent = createExplicitFromImplicitIntent(this, intent);
            if (intent != null) {
                intent.putExtra(BINDING_NAME, IMainService.class.getName());
                bindService(intent, mainConnection, BIND_AUTO_CREATE);
            }
        }
    }

    public void unbindHostService() {
        LogUtil.i(TAG, "---unbindHostService---1");
        if (mainService != null) {
            Log.i(TAG, "---unbindHostService---2");
            unbindService(mainConnection);
            mainService = null;
        }
    }

    public void setMyModule(MyModule myModule) {
        try {
            if ((mainService != null) && (myModule != null)) {
                mainService.setMyModule(myModule);
            }
        } catch (RemoteException exception) {
            exception.printStackTrace();
            Log.e(TAG, "---setMyModule---" + exception.getMessage());
        }
    }

    public boolean isGpsLocated() {
        return mIsGpsLocated;
    }

    public MyLocation getMyLocation() {
        return mMyLocation;
    }

    private ServiceConnection mainConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mainService = IMainService.Stub.asInterface(service);
            LogUtil.i(TAG, "---mainConnection onServiceConnected---");
            try {
                mainService.registerCallback(mCallback);
                mainService.addPid(Process.myPid());
            } catch (RemoteException e) {
                e.printStackTrace();
                LogUtil.e(TAG, e.getMessage());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.i(TAG, "---mainConnection onServiceDisconnected---");
            try {
                mainService.unregisterCallback(mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
                LogUtil.e(TAG, e.getMessage());
            }
            mainService = null;
        }
    };

    private IRemoteServiceCallback mCallback = new IRemoteServiceCallback.Stub() {
        public void locationChanged(MyLocation myLocation) {
            LogUtil.i(TAG, "locationChanged");
            mHandler.sendMessage(mHandler.obtainMessage(MY_MSG_LOCATION, myLocation));
        }

        /**
         * This is called by the remote service regularly to tell us about
         * new values.  Note that IPC calls are dispatched through a thread
         * pool running in each process, so the code executing here will
         * NOT be running in our main thread like most other things -- so,
         * to update the UI, we need to use a Handler to hop over there.
         */
        public void moduleChanged(MyModule myModule) {
            LogUtil.i(TAG, "moduleChanged");
            mHandler.sendMessage(mHandler.obtainMessage(MY_MSG_MODULE, myModule));
        }

        public void exitSystem() {
            LogUtil.i(TAG, "exitSystem");
            mHandler.sendMessage(mHandler.obtainMessage(MY_MSG_EXIT));
        }

    };


    private static final int MY_MSG_LOCATION = 1;
    private static final int MY_MSG_MODULE = 2;
    private static final int MY_MSG_EXIT = 3;
    private Handler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private MainApplication mMainApplication;

        public MyHandler(MainApplication mainApplication) {
            mMainApplication = mainApplication;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MY_MSG_LOCATION:
                    if (msg.obj instanceof MyLocation) {
                        MyLocation myLocation = (MyLocation) msg.obj;
                        mMainApplication.mIsGpsLocated = myLocation.isGpsLocated();
                        mMainApplication.mMyLocation = new MyLocation(
                                myLocation.isGpsLocated(),
                                myLocation.getLongitude(),
                                myLocation.getLatitude(),
                                myLocation.getDirection(),
                                myLocation.getAccuracy());
                    }
                    break;
                case MY_MSG_MODULE:
                    analyzeData(msg);
                    break;
                case MY_MSG_EXIT:
                    mMainApplication.onDestroy();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }

        private void analyzeData(Message message) {
            Object obj = message.obj;
            if (!(obj instanceof MyModule)) {
                return;
            }

            MyModule myModule = (MyModule) message.obj;
            String info = myModule.getInfo();
            if (TextUtil.isNullOrEmpty(info)) {
                return;
            }

            JSONObject object;
            String packageName;
            JSONArray dataArray;
            try {
                object = new JSONObject(info);
                packageName = object.getString("packageName");
                dataArray = object.getJSONArray("data");

                PackageInfo packageInfo = SystemUtil.getPackageInfo(mMainApplication);
                if (packageInfo == null) {
                    return;
                }

                if (packageInfo.packageName.equals(packageName)) {
                    return;
                }

                for (int i = 0; i < dataArray.length(); i++) {
                    String str = dataArray.getString(i);
                    if (str.startsWith(MyModule.MQTT) && str.contains(MyModule.SEPARATOR)) {
                        int index = str.indexOf(MyModule.SEPARATOR);
                        parseMqtt(str.substring(index + 1));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void parseMqtt(String info) throws Exception {
            if (TextUtil.isNullOrEmpty(info)) {
                return;
            }

            final String messageType = "type";
            final String messageContent = "content";
            final int newTask = 201;
            final int updateTaskState = 202;

            JSONObject jsonObject = new JSONObject(info);
            if (jsonObject.isNull(messageType) || jsonObject.isNull(messageContent)) {
                return;
            }

            int type = jsonObject.optInt(messageType);
            JSONArray jsonArray = jsonObject.optJSONArray(messageContent);
            Observable<Boolean> observable =
                    mMainApplication.mDataManager.saveMessage(type == newTask, jsonArray);
            if (observable == null) {
                return;
            }

            observable.subscribeOn(Schedulers.io()).
                    subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {
                            LogUtil.i(TAG, "parseMqtt completed");
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e(TAG, e.toString());
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            LogUtil.i(TAG, "parseMqtt onNext:" + aBoolean);
                        }
                    });
        }
    }

    public void onDestroy() {
        LogUtil.i(TAG, "---meterreading onDestroy---");
        destroyMonitor();

        if (SyncService.isRunning(this)) {
            stopService(SyncService.getStartIntent(this));
        }
        if (AlertTasksService.isRunning(this)) {
            stopService(AlertTasksService.getStartIntent(this));
        }

        unbindHostService();
        System.exit(0);
    }
}
