package com.sh3h.dataprovider;

import android.app.Application;

import com.sh3h.dataprovider.data.entity.response.WordInfoEntity;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.greendao.GreenDaoUtils;
import com.sh3h.dataprovider.http.BaseRetryAndChangeIpInterceptor;
import com.sh3h.dataprovider.http.CustomApiResult;
import com.sh3h.dataprovider.http.CustomCallBack;
import com.sh3h.dataprovider.util.AppConstant;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.dataprovider.util.SystemInfoUtils;
import com.sh3h.dataprovider.util.TokenManager;
import com.squareup.otto.Bus;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpHeaders;
import com.zhouyou.http.model.HttpParams;

import javax.inject.Inject;


/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/4/3 14:22
 */
public class BaseApplication extends Application {

    @Inject
    EventPosterHelper mEventPosterHelper;

    @Inject
    Bus mEventBus;

    @Inject
    ConfigHelper mConfigHelper;

    private BaseApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initEasyHttp();
        mApplicationComponent = DaggerBaseApplicationComponent.builder()
                .baseApplicationModule(new BaseApplicationModule(this))
                .build();
        mApplicationComponent.inject(this);
        mEventBus.register(this);
//        getWordList();
    }

    private void initEasyHttp() {
        EasyHttp.init(this);

        //设置请求头
        HttpHeaders headers = new HttpHeaders();
//        headers.put("User-Agent", SystemInfoUtils.getUserAgent(this, AppConstant.APPID));
        headers.put("token", TokenManager.getInstance().getAuthModel().getAccessToken());

        //设置请求参数
//        HttpParams params = new HttpParams();
//        params.put("appId", AppConstant.APPID);

        EasyHttp.getInstance()
                .setBaseUrl(URL.BASE_IN_URL)
                .setConnectTimeout(60 * 1000 * 5)
                .setWriteTimeOut(60 * 1000 * 5)
                .setReadTimeOut(60 * 1000 * 5)
                .setRetryCount(1)//默认网络不好自动重试1次
                .setRetryDelay(10)//每次延时10ms重试
                .setRetryIncreaseDelay(10)//每次延时叠加10ms
                .setCertificates()//信任所有证书
                .addCommonHeaders(headers)//设置全局公共头
//                .addCommonParams(params)//设置全局公共参数
                .addInterceptor(new BaseRetryAndChangeIpInterceptor(1))
                .debug("EasyHttp", true);
    }

    private static BaseApplication instance = null;

    public static BaseApplication getInstance() {
        if (instance == null) {
            instance = new BaseApplication();
        }
        return instance;
    }

    public ConfigHelper getConfigHelper() {
        return mConfigHelper;
    }


    /**
     * 下载词语信息
     */
    public void getWordList() {
        EasyHttp.get(URL.GetWordList)
                .execute(new CallBackProxy<CustomApiResult<WordInfoEntity>, WordInfoEntity>(new CustomCallBack<WordInfoEntity>() {
                    @Override
                    public void onSuccess(final WordInfoEntity wordInfoEntity) {
                        mEventPosterHelper.postEventSafely(wordInfoEntity);
                        GreenDaoUtils.getAsyncSession(BaseApplication.this)
                                .runInTx(new Runnable() {
                                    @Override
                                    public void run() {
                                        GreenDaoUtils.getAsyncSession(BaseApplication.this)
                                                .runInTx(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        GreenDaoUtils.getDaoSession(BaseApplication.this)
                                                                .getXBBXDao().deleteAll();
                                                        GreenDaoUtils.getDaoSession(BaseApplication.this)
                                                                .getJJCSDao().deleteAll();
                                                        GreenDaoUtils.getDaoSession(BaseApplication.this)
                                                                .getFSYYDao().deleteAll();
                                                        GreenDaoUtils.getDaoSession(BaseApplication.this)
                                                                .getFYLXBeanDao().deleteAll();
                                                        GreenDaoUtils.getDaoSession(BaseApplication.this)
                                                                .getFYLYBeanDao().deleteAll();
                                                        GreenDaoUtils.getDaoSession(BaseApplication.this)
                                                                .getFYNRBeanDao().deleteAll();
                                                        GreenDaoUtils.getDaoSession(BaseApplication.this)
                                                                .getCLJBBeanDao().deleteAll();
                                                        GreenDaoUtils.getDaoSession(BaseApplication.this)
                                                                .getCLJGDao().deleteAll();

                                                        GreenDaoUtils.getDaoSession(BaseApplication.this)
                                                                .getFYLXBeanDao().insertOrReplaceInTx(wordInfoEntity.getFYLX());
                                                        GreenDaoUtils.getDaoSession(BaseApplication.this)
                                                                .getFYLYBeanDao().insertOrReplaceInTx(wordInfoEntity.getFYLY());
                                                        GreenDaoUtils.getDaoSession(BaseApplication.this)
                                                                .getFYNRBeanDao().insertOrReplaceInTx(wordInfoEntity.getFYNR());
                                                        GreenDaoUtils.getDaoSession(BaseApplication.this)
                                                                .getCLJBBeanDao().insertOrReplaceInTx(wordInfoEntity.getCLJB());
                                                        GreenDaoUtils.getDaoSession(BaseApplication.this)
                                                                .getMRSTATUSDao().insertOrReplaceInTx(wordInfoEntity.getMRSTATUS());
                                                        GreenDaoUtils.getDaoSession(BaseApplication.this)
                                                                .getMRMEMODao().insertOrReplaceInTx(wordInfoEntity.getMRMEMO());
                                                        GreenDaoUtils.getDaoSession(BaseApplication.this)
                                                                .getJJCSDao().insertOrReplaceInTx(wordInfoEntity.getJJCS());
                                                        GreenDaoUtils.getDaoSession(BaseApplication.this)
                                                                .getFSYYDao().insertOrReplaceInTx(wordInfoEntity.getFSYY());
                                                        GreenDaoUtils.getDaoSession(BaseApplication.this)
                                                                .getXBBXDao().insertOrReplaceInTx(wordInfoEntity.getXBBX());
                                                        GreenDaoUtils.getDaoSession(BaseApplication.this)
                                                                .getCLJGDao().insertOrReplaceInTx(wordInfoEntity.getCLJG());
                                                    }
                                                });
                                    }
                                });
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
//                        ToastUtils.showShort("词语下载失败");
                    }
                }) {
                });
    }

}
