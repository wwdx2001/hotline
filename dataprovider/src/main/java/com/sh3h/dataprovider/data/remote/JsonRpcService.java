package com.sh3h.dataprovider.data.remote;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.sh3h.dataprovider.data.entity.request.DULoginInfo;
import com.sh3h.dataprovider.data.entity.request.DULoginResult;
import com.sh3h.dataprovider.data.entity.request.DUUpdateInfo;
import com.sh3h.dataprovider.data.entity.request.DUUpdateResult;
import com.sh3h.dataprovider.data.entity.request.DUUserInfo;
import com.sh3h.dataprovider.data.entity.request.DUUserResult;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.mobileutil.util.TextUtil;
import com.sh3h.serverprovider.entity.ClientInfoEntity;
import com.sh3h.serverprovider.entity.LoginInfoEntity;
import com.sh3h.serverprovider.entity.LoginResultEntity;
import com.sh3h.serverprovider.entity.UpdateInfoEntity;
import com.sh3h.serverprovider.entity.UserInfoEntity;
import com.sh3h.serverprovider.rpc.service.BaseApiService;
import com.sh3h.serverprovider.rpc.service.UserApiService;
import com.sh3h.serverprovider.rpc.service.VersionApiService;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.DownloadProgressCallBack;
import com.zhouyou.http.exception.ApiException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class JsonRpcService {
    public JsonRpcService() {

    }

    public void init(String baseUrl) {
        BaseApiService.setBaseURL(baseUrl);
    }

    public void setDebug(boolean isDebug) {
        BaseApiService.setDebug(isDebug);
    }

    /**
     * login
     *
     * @param duLoginInfo
     * @return
     */
    public Observable<DULoginResult> login(final DULoginInfo duLoginInfo) {
        return Observable.create(new Observable.OnSubscribe<DULoginResult>() {
            @Override
            public void call(Subscriber<? super DULoginResult> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if ((duLoginInfo == null)
                            || TextUtil.isNullOrEmpty(duLoginInfo.getAccount())
                            || TextUtil.isNullOrEmpty(duLoginInfo.getPassword())) {
                        throw new NullPointerException("duLoginInfo contains null pointer");
                    }

                    LoginInfoEntity loginInfoEntity = new LoginInfoEntity();
                    LoginInfoEntity.LoginfoBean loginfoBean = new LoginInfoEntity.LoginfoBean();
                    loginfoBean.setAccount(duLoginInfo.getAccount());
                    loginfoBean.setPassword(duLoginInfo.getPassword());
                    loginInfoEntity.setLoginfo(loginfoBean);
                    UserApiService userApiService = new UserApiService();
                    LoginResultEntity loginResultEntity = userApiService.Login(loginInfoEntity);
                    if (loginResultEntity != null) {
                        if (loginResultEntity.isSuccessed()) {
                            DULoginResult duLoginResult = new DULoginResult(
                                    loginResultEntity.getError(),
                                    loginResultEntity.get_UserID());
                            subscriber.onNext(duLoginResult);
                        } else {
                            subscriber.onError(new Throwable(String.format("error: %d",
                                    loginResultEntity.getError())));
                        }
                    } else {
                        subscriber.onError(new Throwable("the returned value is null"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(new Throwable(e.getMessage()));
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * get user information
     *
     * @param duUserInfo
     * @return
     */
    public Observable<DUUserResult> getUserInfo(final DUUserInfo duUserInfo) {
        return Observable.create(new Observable.OnSubscribe<DUUserResult>() {
            @Override
            public void call(Subscriber<? super DUUserResult> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if (duUserInfo == null) {
                        throw new NullPointerException("duUserInfo contains null pointer");
                    }

                    UserApiService userApiService = new UserApiService();
                    UserInfoEntity userInfoEntity = userApiService.getUserInfo(duUserInfo.getUserId());
                    if (userInfoEntity != null) {
                        DUUserResult duLoginResult = new DUUserResult(
                                userInfoEntity.getUserId(),
                                userInfoEntity.getUserName(),
                                userInfoEntity.getAccount(),
                                userInfoEntity.getPWS(),
                                userInfoEntity.getCellPhone(),
                                userInfoEntity.getPhone(),
                                userInfoEntity.getAddress());
                        subscriber.onNext(duLoginResult);
                    } else {
                        subscriber.onError(new Throwable("the returned value is null"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(new Throwable(e.getMessage()));
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * @param duUpdateInfo
     * @return
     */
    public Observable<DUUpdateResult> updateVersion(final DUUpdateInfo duUpdateInfo) {
        return Observable.create(new Observable.OnSubscribe<DUUpdateResult>() {
            @Override
            public void call(Subscriber<? super DUUpdateResult> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if (duUpdateInfo == null) {
                        throw new NullPointerException("duUpdateInfo is null");
                    }

                    ClientInfoEntity clientInfoEntity = new ClientInfoEntity();
                    clientInfoEntity.setAppVersion(duUpdateInfo.getAppVersion());
                    clientInfoEntity.setDataVersion(duUpdateInfo.getDataVersion());
                    VersionApiService versionApiService = new VersionApiService();
                    UpdateInfoEntity updateInfoEntity = versionApiService.hasNewUpdate(clientInfoEntity);
                    if (updateInfoEntity != null) {
                        DUUpdateResult duUpdateResult = new DUUpdateResult();
                        List<UpdateInfoEntity.Item> srcItems = updateInfoEntity.getItems();
                        List<DUUpdateResult.Item> destItems = new ArrayList<>();
                        for (UpdateInfoEntity.Item srcItem : srcItems) {
                            if (srcItem.getType() == UpdateInfoEntity.ItemType.App) {
                                LogUtils.e("helloworld22", "version=" + srcItem.getVersion() + " url=" + srcItem.getUrl());
                                // TODO 下载安装apk
                                if (Integer.parseInt(srcItem.getVersion()) > AppUtils.getAppVersionCode()) {
                                    downloadInstallApk(srcItem.getVersion(), srcItem.getUrl());
                                }
                                continue;
                            }
                            DUUpdateResult.Item destItem = new DUUpdateResult.Item(srcItem.getType().ordinal(),
                                    srcItem.isEnable(), srcItem.getVersion(), srcItem.getDesc(),
                                    srcItem.getUrl());
                            destItems.add(destItem);
                        }
                        duUpdateResult.setItemList(destItems);
                        duUpdateResult.setDuUpdateInfo(duUpdateInfo);
                        subscriber.onNext(duUpdateResult);
                    } else {
                        subscriber.onError(new Throwable("userInfoEntity is null"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(new Throwable(e.getMessage()));
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 下载安装apk
     *
     * @param newVersion 最新版本
     * @param url        下载地址
     */
    private void downloadInstallApk(String newVersion, String url) {
        final String name = "app_v" + newVersion;
        final String absolutePath = new File(ConfigHelper.STORAGE_PATH, ConfigHelper.FOLDER_UPDATE).getAbsolutePath();
        EasyHttp.downLoad(url).cacheMode(CacheMode.NO_CACHE)
                .saveName(name + ".zip")
                .savePath(absolutePath)
                .execute(new DownloadProgressCallBack<String>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void update(long l, long l1, boolean b) {

                    }

                    @Override
                    public void onComplete(String s) {
                        LogUtils.e("helloworld22", "下载完成" + s);
                        try {
                            List<File> files = ZipUtils.unzipFile(s, absolutePath + "/app");
                            for (int i = 0; i < files.size(); i++) {
                                AppUtils.installApp(files.get(i));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}
