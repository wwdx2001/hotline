package com.sh3h.hotline.ui.order.myorder.history;

import android.os.Handler;
import android.os.Looper;

import com.blankj.utilcode.util.LogUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.greendaoDao.DUMyTaskDao;
import com.sh3h.dataprovider.util.Constant;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.sh3h.localprovider.greendaoEntity.MultiMedia;
import com.sh3h.mobileutil.util.LogUtil;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by zhangjing on 2016/9/18.
 */
public class HistoryUploadOrdersPresenter extends ParentPresenter<HistoryUploadOrdersMvpView> {

    private final static String TAG = "HistoryUploadOrdersPresenter";
    private String mAccount = "";
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Inject
    public HistoryUploadOrdersPresenter(DataManager dataManager) {
        super(dataManager);
        mAccount = mDataManager.getAccount();
    }

    @Override
    public void cancelEasyhttpRequest() {

    }

    /**
     * 分页从数据库加载历史工单
     *
     * @param isFirstTime
     * @param offset
     * @param size
     */
    public void loadMyHistoryTasks(final boolean isFirstTime, int offset, int size) {
        LogUtil.i(TAG, "loadMyHistoryTasks offset");
        final AsyncSession asyncSession = GreenDaoUtils.getAsyncSession(MainApplication.getInstance());
        asyncSession.queryList(GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getDUMyTaskDao().queryBuilder()
                .where(DUMyTaskDao.Properties.TaskState.eq(Constant.ORIGIN_MY_TASK_HISTORY))
                .where(DUMyTaskDao.Properties.IsUploadSuccess.eq(Constant.HAS_UPLOADED))
                .orderDesc(DUMyTaskDao.Properties.IsFlag)
//                .where(DUMyTaskDao.Properties.UserId.eq(mAccount))
                .build());
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation asyncOperation) {
                LogUtils.e("onResult", "start");
                if (asyncOperation.getType() == AsyncOperation.OperationType.QueryList) {
                    List<DUMyTask> duMyTasks = (List<DUMyTask>) asyncOperation.getResult();
                    LogUtils.e("onResult", "size=" + duMyTasks.size());
                    if (duMyTasks != null) {
//                        getMvpView().onGetHistoryOrdersList(isFirstTime, duMyTasks);
                        //去除重复
                        getMvpView().onGetHistoryOrdersList(isFirstTime, Lists.newArrayList(Sets.newHashSet(duMyTasks)));
                    } else {
                        getMvpView().showMessage("");
                    }
                } else {
                    getMvpView().showMessage("");
                }
            }
        });
    }

    /**
     * 从数据库加载未上传工单
     *
     * @param currentTime
     * @param isEnter
     */
    public void getUploadTasks(long currentTime, final boolean isEnter) {
        final AsyncSession asyncSession = GreenDaoUtils.getAsyncSession(MainApplication.getInstance());
        asyncSession.queryList(GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getDUMyTaskDao().queryBuilder()
                .where(DUMyTaskDao.Properties.TaskState.eq(Constant.ORIGIN_MY_TASK_HISTORY))
                .where(DUMyTaskDao.Properties.IsUploadSuccess.eq(Constant.HAS_UPLOADED))
                .orderDesc(DUMyTaskDao.Properties.IsFlag)
                .build());
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation asyncOperation) {
                LogUtils.e("onResult", "start");
                if (asyncOperation.getType() == AsyncOperation.OperationType.QueryList) {
                    List<DUMyTask> duMyTasks = (List<DUMyTask>) asyncOperation.getResult();
                    LogUtils.e("onResult", "size=" + duMyTasks.size());
                    //去除重复
                    getMvpView().onFilterOrdersList(true, Lists.newArrayList(Sets.newHashSet(duMyTasks)));
                } else {
                    getMvpView().showMessage("");
                }
            }
        });
    }

    /**
     * 从数据库加载临期工单
     *
     * @param currentTime
     * @param isEnter
     */
    public void getDeadlineTasks(long currentTime, final boolean isEnter) {
        final AsyncSession asyncSession = GreenDaoUtils.getAsyncSession(MainApplication.getInstance());
        asyncSession.queryList(GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getDUMyTaskDao().queryBuilder()
                .where(DUMyTaskDao.Properties.TaskState.eq(Constant.ORIGIN_MY_TASK_HISTORY))
                .where(DUMyTaskDao.Properties.IsUploadSuccess.eq(Constant.HAS_UPLOADED))
                .where(DUMyTaskDao.Properties.ClsxLong.ge(System.currentTimeMillis()))
                .where(DUMyTaskDao.Properties.ClsxLong.le(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .orderDesc(DUMyTaskDao.Properties.IsFlag)
                .build());
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation asyncOperation) {
                LogUtils.e("onResult", "start");
                if (asyncOperation.getType() == AsyncOperation.OperationType.QueryList) {
                    List<DUMyTask> duMyTasks = (List<DUMyTask>) asyncOperation.getResult();
                    LogUtils.e("onResult", "size=" + duMyTasks.size());
                    //去除重复
                    getMvpView().onFilterOrdersList(true, Lists.newArrayList(Sets.newHashSet(duMyTasks)));
                } else {
                    getMvpView().showMessage("");
                }
            }
        });
    }

    /**
     * 从数据库加载超期工单
     *
     * @param currentTime
     */
    public void getExpiredTasks(long currentTime) {
        final AsyncSession asyncSession = GreenDaoUtils.getAsyncSession(MainApplication.getInstance());
        asyncSession.queryList(GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getDUMyTaskDao().queryBuilder()
                .where(DUMyTaskDao.Properties.TaskState.eq(Constant.ORIGIN_MY_TASK_HISTORY))
                .where(DUMyTaskDao.Properties.IsUploadSuccess.eq(Constant.HAS_UPLOADED))
                .where(DUMyTaskDao.Properties.ClsxLong.lt(System.currentTimeMillis()))
                .orderDesc(DUMyTaskDao.Properties.IsFlag)
                .build());
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation asyncOperation) {
                LogUtils.e("onResult", "start");
                if (asyncOperation.getType() == AsyncOperation.OperationType.QueryList) {
                    List<DUMyTask> duMyTasks = (List<DUMyTask>) asyncOperation.getResult();
                    LogUtils.e("onResult", "size=" + duMyTasks.size());
                    //去除重复
                    getMvpView().onFilterOrdersList(true, Lists.newArrayList(Sets.newHashSet(duMyTasks)));
                } else {
                    getMvpView().showMessage("");
                }
            }
        });
    }

    /**
     * 从数据库加载未超期工单
     *
     * @param currentTime
     */
    public void getNoExpiredTasks(long currentTime) {
        final AsyncSession asyncSession = GreenDaoUtils.getAsyncSession(MainApplication.getInstance());
        asyncSession.queryList(GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getDUMyTaskDao().queryBuilder()
                .where(DUMyTaskDao.Properties.TaskState.eq(Constant.ORIGIN_MY_TASK_HISTORY))
                .where(DUMyTaskDao.Properties.IsUploadSuccess.eq(Constant.HAS_UPLOADED))
                .where(DUMyTaskDao.Properties.ClsxLong.gt(System.currentTimeMillis()))
                .orderDesc(DUMyTaskDao.Properties.IsFlag)
                .build());
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation asyncOperation) {
                LogUtils.e("onResult", "start");
                if (asyncOperation.getType() == AsyncOperation.OperationType.QueryList) {
                    List<DUMyTask> duMyTasks = (List<DUMyTask>) asyncOperation.getResult();
                    LogUtils.e("onResult", "size=" + duMyTasks.size());
                    //去除重复
                    getMvpView().onFilterOrdersList(true, Lists.newArrayList(Sets.newHashSet(duMyTasks)));
                } else {
                    getMvpView().showMessage("");
                }
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
        final AsyncSession asyncSession = GreenDaoUtils.getAsyncSession(MainApplication.getInstance());
        asyncSession.queryList(GreenDaoUtils.getDaoSession(MainApplication.getInstance())
                .getDUMyTaskDao().queryBuilder()
                .where(DUMyTaskDao.Properties.TaskState.eq(Constant.ORIGIN_MY_TASK_HISTORY))
                .where(DUMyTaskDao.Properties.IsUploadSuccess.eq(Constant.HAS_UPLOADED))
                .where(DUMyTaskDao.Properties.FaTypeCd.eq(content))
                .orderDesc(DUMyTaskDao.Properties.IsFlag)
                .build());
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation asyncOperation) {
                LogUtils.e("onResult", "start");
                if (asyncOperation.getType() == AsyncOperation.OperationType.QueryList) {
                    List<DUMyTask> duMyTasks = (List<DUMyTask>) asyncOperation.getResult();
                    LogUtils.e("onResult", "size=" + duMyTasks.size());
                    //去除重复
                    getMvpView().onFilterOrdersList(true, Lists.newArrayList(Sets.newHashSet(duMyTasks)));
                } else {
                    getMvpView().showMessage("");
                }
            }
        });
    }

    private DUMedia multiMedia2DUMedia(MultiMedia multiMedia) {
        return new DUMedia(
                multiMedia.getID(),
                multiMedia.getUSER_ID(),
                multiMedia.getTASK_ID(),
                multiMedia.getTASK_TYPE(),
                multiMedia.getTASK_STATE(),
                multiMedia.getFILE_TYPE(),
                multiMedia.getFILE_NAME(),
                multiMedia.getFILE_HASH(),
                multiMedia.getFILE_URL(),
                multiMedia.getUPLOAD_FLAG(),
                multiMedia.getEXTEND(),
                multiMedia.getFILE_TIME());
    }

    /**
     * @param duHistoryTasks
     */
    private void fiterLatestTasks(List<DUHistoryTask> duHistoryTasks) {

    }
}
