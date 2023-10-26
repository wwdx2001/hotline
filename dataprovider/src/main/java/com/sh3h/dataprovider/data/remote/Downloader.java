package com.sh3h.dataprovider.data.remote;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.blankj.utilcode.util.LogUtils;
import com.sh3h.dataprovider.data.entity.DUFile;
import com.sh3h.dataprovider.data.entity.request.DUFileResult;
import com.sh3h.dataprovider.data.entity.request.DUUpdateInfo;
import com.sh3h.dataprovider.data.entity.request.DUUpdateResult;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.exception.DUException;
import com.sh3h.dataprovider.injection.annotation.ApplicationContext;
import com.sh3h.dataprovider.util.PackageUtil;
import com.sh3h.mobileutil.util.TextUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

@Singleton
public class Downloader {
    private static final int TIMEOUT = 10 * 1000;// 超时
    private final Context mContext;
    private final ConfigHelper mConfigHelper;
    private int newApkVersion = 0;
    private int newDataVersion = 0;

    @Inject
    public Downloader(@ApplicationContext Context context,
                      ConfigHelper configHelper) {
        mContext = context;
        mConfigHelper = configHelper;
    }

    /**
     * download each file
     *
     * @param duUpdateInfo
     * @param items
     * @return
     */
    public Observable<DUFileResult> downloadFile(DUUpdateInfo duUpdateInfo, List<DUUpdateResult.Item> items) {
        if ((items == null) || (items.size() <= 0) || (duUpdateInfo == null)) {
            return null;
        }

        DUUpdateResult.Item item = items.remove(0);
        if (TextUtil.isNullOrEmpty(item.getUrl())) {
            return null;
        }

        String url = item.getUrl();
        String version = item.getVersion();
        int ver = TextUtil.getInt(version);
        int index = url.lastIndexOf("/");
        if (index <= 0) {
            return null;
        }

        String name = url.substring(index + 1);
        if (TextUtil.isNullOrEmpty(name)) {
            return null;
        }

        if (name.contains("app")) {
            int appVersion = duUpdateInfo.getAppVersion();
            LogUtils.e("VersionService", "-----------app=" + appVersion + "---" + ver);
            if (appVersion >= ver) {
                return downloadFile(duUpdateInfo, items);
            }

            String localApkPath = checkLocalApkFile(ver);
            if (localApkPath != null) {
                //installLocalApk(localApkPath);
                // exit system
                return downloadFile(url, name, true);
            } else {
                newApkVersion = ver;
                return downloadFile(url, name, false);
            }
        } else if (name.contains("data")) {
//            int dataVersion = duUpdateInfo.getAppVersion();
            int dataVersion = duUpdateInfo.getDataVersion();
            LogUtils.e("VersionService", "-----------data=" + dataVersion + "---" + ver);
            if (dataVersion >= ver) {
                return downloadFile(duUpdateInfo, items);
            } else {
                newDataVersion = ver;
                return downloadFile(url, name, false);
            }
        } else {
            return null;
        }
    }

    /**
     * 判断是否有apk文件
     */
    private String checkLocalApkFile(int remoteAppVersion) {
        File dir = mConfigHelper.getUpdateFolderPath();
        File[] files = dir.listFiles();
        if (files == null) {
            return null;
        }

        for (File file : files) {

            if (file.isDirectory() && file.listFiles() != null && file.listFiles().length > 0) {

                for (File apkFile : file.listFiles()) {
                    if (apkFile.getName().contains(".apk")) {
                        String path = isDownloader(apkFile, remoteAppVersion);
                        if (path != null) {
                            return path;
                        }
                    }
                }
            }
        }

        return null;
    }

    //判断服务器上的版本是否已经下载到本地
    private String isDownloader(File apkFile, int remoteAppVersion) {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo packageInfo = pm.getPackageArchiveInfo(apkFile.getPath(), PackageManager.GET_ACTIVITIES);
        if (packageInfo == null) {
            return null;
        }

        int localAppVersion = packageInfo.versionCode;
        // TODO 这里直接安装
        LogUtils.e("VersionService", "remoteAppVersion" + remoteAppVersion + "--localAppVersion" + localAppVersion);
//        if (remoteAppVersion > localAppVersion) {
//            AppUtils.installApp(apkFile);
//        }
        return localAppVersion == remoteAppVersion ? apkFile.getPath() : null;
    }

    /**
     * @param url
     * @param name
     * @param isFileExisting
     * @return
     */
    public Observable<DUFileResult> downloadFile(final String url,
                                                 final String name,
                                                 boolean isFileExisting) {
        if (isFileExisting) {
            return Observable.create(new Observable.OnSubscribe<DUFileResult>() {
                @Override
                public void call(Subscriber<? super DUFileResult> subscriber) {
                    if (subscriber.isUnsubscribed()) {
                        return;
                    }

                    File file = new File(mConfigHelper.getUpdateFolderPath(), name);
                    subscriber.onNext(getDUFileResult(100, name, file.getPath()));
                }
            });
        } else {
            return Observable.create(new Observable.OnSubscribe<DUFileResult>() {
                @Override
                public void call(Subscriber<? super DUFileResult> subscriber) {
                    if (subscriber.isUnsubscribed()) {
                        return;
                    }

                    try {
                        //downloadFile1(url, name, subscriber);
                        downloadFile2(url, name, subscriber);
                    } catch (Exception e) {
                        e.printStackTrace();
                        subscriber.onError(new Throwable(e.getMessage()));
                    }
                }
            });
        }
    }

    /**
     * @param strUrl
     * @param strName
     * @param subscriber
     * @throws Exception
     */
    private void downloadFile2(String strUrl, final String strName,
                               final Subscriber<? super DUFileResult> subscriber) throws Exception {
        LogUtils.e("VersionService", "开始下载文件");
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(strUrl).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                subscriber.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                FileOutputStream fos = null;
                File file = new File(mConfigHelper.getUpdateFolderPath(), strName);
                try {
                    is = response.body().byteStream();
                    long totalLength = response.body().contentLength();
                    String strPath = file.getPath();
                    fos = new FileOutputStream(file);
                    byte[] buf = new byte[2048];
                    int length = 0;
                    int curLength = 0;
                    int percent = 0;
                    while ((length = is.read(buf)) != -1) {
                        fos.write(buf, 0, length);
                        curLength += length;
                        percent = (int) (curLength * 100.0 / totalLength);
                        DUFileResult duFileResult = getDUFileResult(percent, strName, strPath);
                        if (strName.contains("apk")) {
                            duFileResult.setVersion(newApkVersion);
                        } else {
                            duFileResult.setVersion(newDataVersion);
                        }

                        subscriber.onNext(duFileResult);
                    }

//                    if (percent != 100) {
//                        subscriber.onNext(getDUFileResult(100, strName, strPath));
//                    }

                    fos.flush();
                } catch (IOException e) {
                    subscriber.onError(e);
                } finally {
                    try {
                        if (fos != null) {
                            fos.close();
                        }

                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    subscriber.onCompleted();
                }
            }
        });
    }

    private DUFileResult getDUFileResult(int percent, String name, String path) {
        return new DUFileResult(percent, name, path);
    }
}
