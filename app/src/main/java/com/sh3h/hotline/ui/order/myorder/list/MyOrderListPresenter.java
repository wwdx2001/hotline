package com.sh3h.hotline.ui.order.myorder.list;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.maning.mndialoglibrary.MProgressDialog;
import com.sh3h.dataprovider.URL;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.data.entity.response.ServiceResultEntity;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.DUMyTaskDao;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.sh3h.hotline.ui.order.myorder.history.HistoryOrdersActivity;
import com.sh3h.ipc.module.MyModule;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Created by zhangjing on 2016/9/12.
 */
public class MyOrderListPresenter extends ParentPresenter<MyOrderListMvpView> {
    private final static String TAG = "MyOrderListPresenter";
    private int mUserId;
    private String mAccount = "";
    private Disposable mDisposable1;
    private Disposable mDisposable2;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private String reason;
    private String comment;
    private Disposable mNetWorkDispose;

    @Inject
    public MyOrderListPresenter(DataManager dataManager) {
        super(dataManager);
        mUserId = mDataManager.getUserId();
        mAccount = mDataManager.getAccount();
        LogUtils.e("userId", "userId=" + mUserId + "account=" + mAccount);
    }

    @Override
    public void cancelEasyhttpRequest() {
        EasyHttp.cancelSubscription(mDisposable1);
        EasyHttp.cancelSubscription(mDisposable2);
    }

    public int getUserId() {
        return mUserId;
    }


    /**
     * 变更状态，如接收，延期，退单
     *
     * @param type
     */
    String acceptFlg = "";

    public void updataTaskState(final int type, final DUMyTask duMyTask, String reason, String remark, String delayTime) {
//        delayTime  延期天数改为Date类型
        int delayCount = 1;
        try {
            delayCount = Integer.parseInt(delayTime);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        Long mill = duMyTask.getClsxLong() + delayCount * 24 * 60 * 60 * 1000L;
        Log.e("aaaa", "clsx=" + duMyTask.getClsxLong() + "  add=" + delayCount * 24 * 60 * 60 * 1000 + "  mill=" + mill);
        String delayDate = "";
        if (type == Constant.TASK_STATE_RECEIVED) {
            acceptFlg = "A";//接单
        } else if (type == Constant.TASK_STATE_DELAY) {
            acceptFlg = "Y";//申请延期
            delayDate = TimeUtils.millis2String(mill);
            Log.e("aaaa", "delayDate=" + delayDate);
        } else if (type == Constant.TASK_STATE_BACK) {
            acceptFlg = "T";
        }
        duMyTask.setState(type);
        mDisposable1 = EasyHttp.post(URL.WorkOrderStatusChange)
//        mDisposable1 = EasyHttp.post("https://a985df5a-9769-4e1f-92a1-ce596f6437f6.mock.pstmn.io")
                .params("faId", duMyTask.getFaId())
                .params("caseId", duMyTask.getCaseId())
                .params("acceptFlg", acceptFlg)
                .params("reason", reason)
                .params("comment", remark)
                .params("delayTime", delayDate)
                .execute(new CallBackProxy<CustomApiResult<ServiceResultEntity>, ServiceResultEntity>(new CustomCallBack<ServiceResultEntity>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        MProgressDialog.showProgress(ActivityUtils.getTopActivity());
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        MProgressDialog.dismissProgress();
                    }

                    @Override
                    public void onSuccess(ServiceResultEntity serviceResultEntity) {
                        duMyTask.setCmSta(acceptFlg);
                        acceptFlg = "";
                        GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                                .getDUMyTaskDao().insertOrReplace(duMyTask);
                        if (getMvpView() != null) {
                            getMvpView().updataStateSuccess(type, serviceResultEntity.getCmMsgDesc());
                        }
                    }
                }) {
                });
    }

    /**
     * 加载网络数据（新增）
     */
    public void loadNetworkData(final boolean isEnter, final boolean isDeadline) {
        mNetWorkDispose = EasyHttp
                .post(URL.GetWordOrderList)
                .params("repCd", SPUtils.getInstance().getString(Constant.USERID))
                .execute(new CallBackProxy<CustomApiResult<List<DUMyTask>>,
                        List<DUMyTask>>(new CustomCallBack<List<DUMyTask>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        if (getMvpView() != null) {
                            getMvpView().onRequestStart();
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (getMvpView() != null) {
                            getMvpView().onError(e.getMessage());
                            if (isEnter && isDeadline) {
                                getDeadlineTasks(System.currentTimeMillis(), isEnter);
                            } else {
                                onLoadAllTaskData();
                            }
                        }
                        notifyMainAcountChange(-1);
                    }

                    @Override
                    public void onSuccess(final List<DUMyTask> mNetWorkDatas) {
                        if (getMvpView() != null) {
                            getMvpView().hideShowProgress();
                        }
                        if (mNetWorkDatas == null || mNetWorkDatas.size() == 0) {
                            ToastUtils.showLong("无工单数据");
                        }
                        List<DUMyTask> list = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                                .getDUMyTaskDao().queryBuilder()
                                .where(DUMyTaskDao.Properties.TaskState.eq(Constant.ORIGIN_MY_TASK_HISTORY))
                                .list();
                        List<DUMyTask> UduMyTasks = new ArrayList<>();
                        for (int i = 0; i < mNetWorkDatas.size(); i++) {
                            if(TextUtils.isEmpty(mNetWorkDatas.get(i).getClsx())){
                                mNetWorkDatas.get(i).setClsxLong(0L);
                            }else{
                                mNetWorkDatas.get(i).setClsxLong(TimeUtils.string2Millis(mNetWorkDatas.get(i).getClsx()));
                            }

                            mNetWorkDatas.get(i).setUserId(mAccount);
                            if ("U".equals(mNetWorkDatas.get(i).getCmSta())
                                    || !StringUtils.isEmpty(mNetWorkDatas.get(i).getApplyType())) {
                                DUMyTask duMyTask = mNetWorkDatas.get(i);
                                if (list.contains(duMyTask)) {
                                    duMyTask.setID((long) duMyTask.getFaId().hashCode());
                                    UduMyTasks.add(duMyTask);
                                }
                            }
                        }
                        mNetWorkDatas.removeAll(list);
                        mNetWorkDatas.addAll(UduMyTasks);
                        List<DUMyTask> newNetWorkList = mNetWorkDatas;
                        Log.e("taskSize", "网络 taskSize==" + newNetWorkList.size());
                        if (getMvpView() != null) {
                            getMvpView().onLoadNetWorkData(newNetWorkList, isEnter, isDeadline);
                        }
                        notifyMainAcountChange(newNetWorkList.size());
                    }
                }) {
                });
    }


    public void notifyMainAcountChange(int size) {
        int acount = size;
        int unReadRw = 0;
        try {
            //我的工单数量通知
            if (size == -1) {
                final List<DUMyTask> duMyTasks = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                        .getDUMyTaskDao().queryBuilder()
                        .where(DUMyTaskDao.Properties.TaskState.notEq(Constant.ORIGIN_MY_TASK_HISTORY))
                        .list();
                acount = duMyTasks.size();
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(MyModule.PACKAGE_NAME, AppUtils.getAppPackageName());
            jsonObject.put(MyModule.ACTIVITY_NAME, MyOrderListActivity.class.getName());
            JSONArray subJSONArray = new JSONArray();
            subJSONArray.put("count#" + acount);
            LogUtils.e("count =", "count = " + acount);
            jsonObject.put(MyModule.DATA, subJSONArray);
            MyModule myModule = new MyModule(jsonObject.toString());
            MainApplication.get(ActivityUtils.getTopActivity()).setMyModule(myModule);
            //已处理工单数量通知
            final List<DUMyTask> duMyTasks2 = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                    .getDUMyTaskDao().queryBuilder()
                    .where(DUMyTaskDao.Properties.TaskState.eq(Constant.ORIGIN_MY_TASK_HISTORY))
                    .list();
            for (DUMyTask duHistoryTask : duMyTasks2) {
                if (duHistoryTask.getIsUploadSuccess() == Constant.NO_UPLOAD) {
                    unReadRw++;
                }
            }
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put(MyModule.PACKAGE_NAME, AppUtils.getAppPackageName());
            jsonObject2.put(MyModule.ACTIVITY_NAME, HistoryOrdersActivity.class.getName());
            JSONArray subJSONArray2 = new JSONArray();
            subJSONArray2.put("count#" + unReadRw);
            jsonObject2.put(MyModule.DATA, subJSONArray2);
            MyModule myModule2 = new MyModule(jsonObject2.toString());
            MainApplication.get(ActivityUtils.getTopActivity()).setMyModule(myModule2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载超期工单（新增）
     */
    public void getExpiredTasks(long currentTime) {
        GreenDaoUtils.getAsyncSession(MainApplication.getInstance())
                .runInTx(new Runnable() {
                    @Override
                    public void run() {
//                        List<DUMyTask> duMyTasks2 = GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
//                                .getDUMyTaskDao().loadAll();
                        final List<DUMyTask> duMyTasks = GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                .getDUMyTaskDao().queryBuilder()
                                .where(DUMyTaskDao.Properties.TaskState.notEq(Constant.ORIGIN_MY_TASK_HISTORY))
                                .where(DUMyTaskDao.Properties.ClsxLong.lt(System.currentTimeMillis()))
//                                .where(DUMyTaskDao.Properties.ClsxLong.le(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                                .whereOr(DUMyTaskDao.Properties.CmSta.eq("A"), DUMyTaskDao.Properties.CmSta.eq("D"))
//                                .where(DUMyTaskDao.Properties.UserId.eq(mAccount))
                                .orderDesc(DUMyTaskDao.Properties.CreDttm)
                                .list();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (getMvpView() != null) {
                                    Log.e("taskSize", "超期工单 taskSize==" + duMyTasks.size());
                                    getMvpView().onLoadExpiredAndFylxMyTasks("超期工单", duMyTasks);
                                }
                            }
                        });

                    }
                });
    }

    /**
     * 获得临期工单（新增）
     */
    public void getDeadlineTasks(long currentTime, final boolean isEnter) {
        GreenDaoUtils.getAsyncSession(MainApplication.getInstance())
                .runInTx(new Runnable() {
                    @Override
                    public void run() {
                        final List<DUMyTask> duMyTasks = GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                .getDUMyTaskDao().queryBuilder()
                                .where(DUMyTaskDao.Properties.TaskState.notEq(Constant.ORIGIN_MY_TASK_HISTORY))
                                .where(DUMyTaskDao.Properties.ClsxLong.ge(System.currentTimeMillis()))
                                .where(DUMyTaskDao.Properties.ClsxLong.le(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                                .whereOr(DUMyTaskDao.Properties.CmSta.eq("A"), DUMyTaskDao.Properties.CmSta.eq("D"))
                                .orderDesc(DUMyTaskDao.Properties.CreDttm)
                                .list();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (getMvpView() != null) {
                                    Log.e("taskSize", "临期工单 taskSize==" + duMyTasks.size());
                                    if (isEnter) {//第一次进入
                                        if (duMyTasks != null && duMyTasks.size() > 0) {
                                            //临期工单
                                            if (getMvpView() != null) {
                                                getMvpView().onLoadDeadLineData(duMyTasks);
                                            }
                                        } else {
                                            //全部工单
                                            onLoadAllTaskData();
                                        }
                                    } else {
                                        if (getMvpView() != null) {
                                            getMvpView().onLoadDeadLineData(duMyTasks);
                                        }
                                    }
                                }
                            }
                        });

                    }
                });

    }

    /**
     * 加载全部工单（新增）
     */
    public void onLoadAllTaskData() {
        GreenDaoUtils.getAsyncSession(MainApplication.getInstance())
                .runInTx(new Runnable() {
                    @Override
                    public void run() {
                        final List<DUMyTask> duMyTasks = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                                .getDUMyTaskDao().queryBuilder()
                                .where(DUMyTaskDao.Properties.TaskState.notEq(Constant.ORIGIN_MY_TASK_HISTORY))
                                .orderDesc(DUMyTaskDao.Properties.CreDttm)
                                .list();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (getMvpView() != null) {
                                    Log.e("taskSize", "全部工单 taskSize==" + duMyTasks.size());
                                    getMvpView().onLoadAllData(duMyTasks);
                                }
                            }
                        });
                    }
                });
    }

    /**
     * 加载全部工单（新增，按地址正序排序）
     */
    public void loadAllTaskByAddressAsc() {
        GreenDaoUtils.getAsyncSession(MainApplication.getInstance())
                .runInTx(new Runnable() {
                    @Override
                    public void run() {
                        final List<DUMyTask> duMyTasks = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                                .getDUMyTaskDao().queryBuilder()
                                .where(DUMyTaskDao.Properties.TaskState.notEq(Constant.ORIGIN_MY_TASK_HISTORY))
                                .preferLocalizedStringOrder()
                                .orderAsc(DUMyTaskDao.Properties.Fsdz)
                                .list();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (getMvpView() != null) {
                                    Log.e("taskSize", "全部工单 taskSize==" + duMyTasks.size());
                                    getMvpView().onLoadAllData(duMyTasks);
                                }
                            }
                        });
                    }
                });
    }

    /**
     * 加载全部工单（新增，按地址倒序排序）
     */
    public void loadAllTaskByAddressDesc() {
        GreenDaoUtils.getAsyncSession(MainApplication.getInstance())
                .runInTx(new Runnable() {
                    @Override
                    public void run() {
                        final List<DUMyTask> duMyTasks = GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                                .getDUMyTaskDao().queryBuilder()
                                .where(DUMyTaskDao.Properties.TaskState.notEq(Constant.ORIGIN_MY_TASK_HISTORY))
                                .preferLocalizedStringOrder()
                                .orderDesc(DUMyTaskDao.Properties.Fsdz)
                                .list();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (getMvpView() != null) {
                                    Log.e("taskSize", "全部工单 taskSize==" + duMyTasks.size());
                                    getMvpView().onLoadAllData(duMyTasks);
                                }
                            }
                        });
                    }
                });
    }

    /**
     * 筛选反应类型工单（新增）
     *
     * @param content
     * @param parms
     */
    public void getFilterTask(final String content, final String parms) {
        GreenDaoUtils.getAsyncSession(MainApplication.getInstance())
                .runInTx(new Runnable() {
                    @Override
                    public void run() {
                        final List<DUMyTask> duMyTasks = GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
                                .getDUMyTaskDao().queryBuilder()
                                .where(DUMyTaskDao.Properties.TaskState.notEq(Constant.ORIGIN_MY_TASK_HISTORY))
                                .where(DUMyTaskDao.Properties.FaTypeCd.eq(content))
                                .orderDesc(DUMyTaskDao.Properties.CreDttm)
                                .list();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (getMvpView() != null) {
                                    Log.e("taskSize", "反应类型 taskSize==" + duMyTasks.size());
                                    getMvpView().onLoadExpiredAndFylxMyTasks(content, duMyTasks);
                                }
                            }
                        });

                    }
                });

    }
}