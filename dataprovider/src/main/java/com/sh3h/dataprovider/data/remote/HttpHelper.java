package com.sh3h.dataprovider.data.remote;

import android.content.Context;

import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.dataprovider.data.entity.base.DUEntitiesResult;
import com.sh3h.dataprovider.data.entity.base.DUEntityResult;
import com.sh3h.dataprovider.data.entity.request.DUCreateSelfOrder;
import com.sh3h.dataprovider.data.entity.request.DUHandle;
import com.sh3h.dataprovider.data.entity.request.DULoginInfo;
import com.sh3h.dataprovider.data.entity.request.DULoginResult;
import com.sh3h.dataprovider.data.entity.request.DUUpdateInfo;
import com.sh3h.dataprovider.data.entity.request.DUUpdateResult;
import com.sh3h.dataprovider.data.entity.request.DUUserInfo;
import com.sh3h.dataprovider.data.entity.request.DUUserResult;
import com.sh3h.dataprovider.data.entity.response.DUArrearsDetail;
import com.sh3h.dataprovider.data.entity.response.DUBillBaseInfo;
import com.sh3h.dataprovider.data.entity.response.DUCreateSelfOrderResult;
import com.sh3h.dataprovider.data.entity.response.DUFile;
import com.sh3h.dataprovider.data.entity.response.DUHandleResult;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.data.entity.response.DUNews;
import com.sh3h.dataprovider.data.entity.response.DUOrder;
import com.sh3h.dataprovider.data.entity.response.DUOrderStatusResult;
import com.sh3h.dataprovider.data.entity.response.DUProcess;
import com.sh3h.dataprovider.data.entity.response.DURecentBill;
import com.sh3h.dataprovider.data.entity.response.DURecentBillDetail;
import com.sh3h.dataprovider.data.entity.response.DUReplyOrderResult;
import com.sh3h.dataprovider.data.entity.response.DUWord;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.config.SystemConfig;
import com.sh3h.dataprovider.injection.annotation.ApplicationContext;
import com.sh3h.dataprovider.util.ZipUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;
import com.squareup.otto.Bus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_PICTURE;
import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_SCREEN_SHOT;
import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_SIGNUP;
import static com.sh3h.dataprovider.data.entity.DUMedia.FILE_TYPE_VOICE;

@Singleton
public class HttpHelper {
    private static final String TAG = "HttpHelper";
    private final Context mContext;
    private final ConfigHelper mConfigHelper;
    private final Bus mBus;
    private boolean isConnected;
    private boolean isRestfulApi;
    private RestfulApiService restfulApiService;
    private JsonRpcService jsonRpcService;

    @Inject
    public HttpHelper(@ApplicationContext Context context, ConfigHelper configHelper, Bus bus) {
        mContext = context;
        mConfigHelper = configHelper;
        restfulApiService = null;
        mBus = bus;
        isRestfulApi = false;
        isConnected = false;
        restfulApiService = null;
        jsonRpcService = null;
    }

    public Observable<DUUserResult> login(DULoginInfo duLoginInfo) {
        connect();

        if (isRestfulApi) {
            return null;
        } else {
            return jsonRpcService.login(duLoginInfo)
                    .flatMap(new Func1<DULoginResult, Observable<DUUserResult>>() {
                        @Override
                        public Observable<DUUserResult> call(DULoginResult duLoginResult) {
                            DUUserInfo duUserInfo = new DUUserInfo(duLoginResult.getUserID());
                            return jsonRpcService.getUserInfo(duUserInfo);
                        }
                    });
        }
    }

    public Observable<DUUpdateResult> updateVersion(DUUpdateInfo duUpdateInfo) {
        connect();

        if (isRestfulApi) {
            return null;
        } else {
            return jsonRpcService.updateVersion(duUpdateInfo);
        }
    }

    /**
     * 账单查询
     *
     * @param cardId
     * @param name
     * @param address
     * @return
     */
    public Observable<DUEntitiesResult<DUBillBaseInfo>> searchBill(String cardId, String tableNumber,
                                                                   String name, String address, boolean fuzzySearch) {
        connect();

        return restfulApiService.searchBill(cardId, tableNumber, name, address, fuzzySearch);
    }

    /**
     * 近期开账信息
     *
     * @param cardId
     * @param monthCount
     * @return
     */
    public Observable<DUEntitiesResult<DURecentBill>> getRecentBill(String cardId, int monthCount) {
        connect();

        return restfulApiService.getRecentBill(cardId, monthCount);
    }

    /**
     * 近期开账详细信息
     *
     * @param cardId
     * @param feeId
     * @return
     */
    public Observable<DUEntityResult<DURecentBillDetail>> getRecentBillDetail(String cardId, int feeId) {
        connect();

        return restfulApiService.getRecentBillDetail(cardId, feeId);
    }

    /**
     * 欠费信息
     *
     * @param cardId
     * @param monthCount
     * @return
     */
    public Observable<DUEntitiesResult<DURecentBill>> getArrearage(String cardId, int monthCount) {
        connect();

        return restfulApiService.getArrearage(cardId, monthCount);
    }

    /**
     * 欠费信息详细信息
     *
     * @param cardId
     * @param feeId
     * @return
     */
    public Observable<DUEntityResult<DUArrearsDetail>> getArrearageDetail(String cardId, int feeId) {
        connect();

        return restfulApiService.getArrearageDetail(cardId, feeId);
    }

    /**
     * 获取最新公告
     *
     * @return
     */
    public Observable<DUEntitiesResult<DUNews>> getNews() {
        connect();

        return restfulApiService.getNews();
    }

    /**
     * 公告实时查询
     *
     * @param type
     * @return
     */
    public Observable<DUEntitiesResult<DUNews>> searchNews(String title, String content,
                                                           String type, long startTime, long endTime) {
        connect();

        return restfulApiService.searchNews(title, content, type, startTime, endTime);
    }

    /**
     * 工单查询
     *
     * @param taskId    任务编号
     * @param name      户名
     * @param address   地址
     * @param telephone 电话
     * @return
     */
    public Observable<DUEntitiesResult<DUOrder>> searchOrder(String taskId, String name,
                                                             String address, String telephone,
                                                             String issueOrigin, String issueType,
                                                             String issueContent, String loginStation,
                                                             String admissibleSite, String issueArea,
                                                             long startDateUtc, long endDateUtc) {
        connect();

        return restfulApiService.searchOrder(taskId, name, address, telephone, issueOrigin,
                issueType, issueContent, loginStation, admissibleSite, issueArea, startDateUtc,
                endDateUtc, true);
    }

    /**
     * 分页获取工单任务
     *
     * @param userId
     * @param startIndex
     * @param count
     * @return
     */
    public Observable<DUEntitiesResult<DUOrder>> getOrders(String userId, int startIndex, int count) {
        connect();

        return restfulApiService.getOrders(userId, startIndex, count);
    }

    /**
     * 工单处理
     *
     * @param requests 请求参数封装体
     * @param userId   用户id
     * @return
     */
    public Observable<DUEntityResult<DUHandleResult>> handleOrder(List<DUHandle> requests, String userId) {
        connect();

        return restfulApiService.handleOrder(requests, userId);
    }

    /**
     * 上传自开单
     *
     * @param requests
     * @param userId
     * @return
     */
    public Observable<DUEntitiesResult<DUCreateSelfOrderResult>> uploadCreateSelfOrder(List<DUCreateSelfOrder> requests, int userId) {
        connect();

        if ((requests == null) || (requests.size() <= 0)) {
            return Observable.error(new Throwable("param is error"));
        }


        return restfulApiService.uploadCreateSelfOrder(requests, userId);
    }

    /**
     * 获取工单任务状态
     *
     * @param taskIds
     * @param extend
     * @return
     */
    public Observable<DUEntitiesResult<DUOrderStatusResult>> getOrderStatus(List<String> taskIds, String extend) {
        connect();

        return null;//restfulApiService.getOrderStatus();
    }

    /**
     * 处理工单（包括接收等等等）
     *
     * @param userId
     * @return
     */
    public Observable<DUEntitiesResult<DUReplyOrderResult>> requestReply(int userId, String replay) {
        connect();

        if (TextUtil.isNullOrEmpty(replay)) {
            return Observable.error(new Throwable("reply is null"));
        }

        JSONArray array = new JSONArray();
        try {
            JSONObject object = new JSONObject(replay);
            array.put(object);
        } catch (JSONException e) {
            LogUtil.e(TAG, e.getMessage());
            e.printStackTrace();
            return Observable.error(e);
        }

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), array.toString());
        return restfulApiService.requestReplyTask(userId, requestBody);
    }


    /**
     * 下载工单列表
     *
     * @return
     */
    public Observable<DUEntitiesResult<DUMyTask>> downloadMyTaskListFromServer(int userId) {
        connect();
        return restfulApiService.downloadMyTasks(userId);
    }

    public Observable<DUEntitiesResult<DUWord>> downloadWords(String group) {
        connect();
        return restfulApiService.downloadWords(group);
    }


    /**
     * 处理工单集合
     *
     * @param userId
     * @return
     */
    public Observable<DUEntitiesResult<DUReplyOrderResult>> requestReplyList(int userId, List<String> replyList) {
        connect();

        JSONArray array = new JSONArray();
        try {
            for (String reply : replyList) {
                JSONObject object = new JSONObject(reply);
                array.put(object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), array.toString());
        return restfulApiService.requestReplyTask(userId, requestBody);
    }

    /**
     * @param duMediaList
     * @return
     */
    public Observable<Boolean> uploadMediaList(final List<DUMedia> duMediaList) {
        connect();

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    if (duMediaList == null) {
                        throw new NullPointerException("duMedia is null");
                    }

                    List<DUMedia> destDUMediaList = new ArrayList<DUMedia>();
                    Map<String, RequestBody> params = new HashMap<>();
                    for (DUMedia duMedia : duMediaList) {
                        if (TextUtil.isNullOrEmpty(duMedia.getFileName())
                                || (!TextUtil.isNullOrEmpty(duMedia.getFileHash())
                                && !TextUtil.isNullOrEmpty(duMedia.getFileUrl()))) {
                            continue;
                        }

                        File folder;
                        File file = null;

                        switch (duMedia.getFileType()) {
                            case FILE_TYPE_PICTURE:
                                folder = mConfigHelper.getImageFolderPath();
                                file = new File(folder, duMedia.getFileName());
                                break;
                            case FILE_TYPE_VOICE:
                                folder = mConfigHelper.getSoundFolderPath();
                                file = new File(folder, duMedia.getFileName());
                                break;
                            case FILE_TYPE_SIGNUP:
                                folder = mConfigHelper.getImageFolderPath();
                                file = new File(folder, duMedia.getFileName());
                                break;
                            case FILE_TYPE_SCREEN_SHOT:
                                folder = mConfigHelper.getVideoFolderPath();
                                String videoPath = null;
                                if (!TextUtil.isNullOrEmpty(duMedia.getExtend())) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(duMedia.getExtend());
                                        if (!TextUtil.isNullOrEmpty(jsonObject.getString("videoPath"))) {
                                            videoPath = jsonObject.getString("videoPath");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                file = new File(videoPath);
                                break;
                            default:
                                folder = null;
                                break;
                        }

                        if (folder == null) {
                            continue;
                        }

                        if (file.exists()) {
                            // create RequestBody instance from file
                            RequestBody requestBody =
                                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
                            params.put("file\"; filename=\"" + file.getName(), requestBody);
                            destDUMediaList.add(duMedia);
                        }
                    }

                    if ((params.size() > 0) && (params.size() == destDUMediaList.size())) {
                        Call<DUEntitiesResult<DUFile>> call = restfulApiService.uploadFiles(params);
                        Response<DUEntitiesResult<DUFile>> response = call.execute();
                        DUEntitiesResult<DUFile> duFileDUEntitiesResult = response.body();
                        List<DUFile> duFileList = duFileDUEntitiesResult.getData();
                        if ((duFileList != null)
                                && (duFileList.size() == destDUMediaList.size())) {
                            for (DUMedia duMedia : destDUMediaList) {
                                for (DUFile duFile : duFileList) {
                                    String srcFileName = null;
                                    if (duMedia.getFileType() == FILE_TYPE_SCREEN_SHOT) {
                                        String videoPath = null;
                                        if (!TextUtil.isNullOrEmpty(duMedia.getExtend())) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(duMedia.getExtend());
                                                if (!TextUtil.isNullOrEmpty(jsonObject.getString("videoPath"))) {
                                                    videoPath = jsonObject.getString("videoPath");
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (!TextUtil.isNullOrEmpty(videoPath)) {
                                            srcFileName = videoPath.substring(25);
                                        }
                                    } else {
                                        srcFileName = duMedia.getFileName();
                                    }
                                    String destFileName = duFile.getOriginName();
                                    if (TextUtil.isNullOrEmpty(srcFileName)
                                            || TextUtil.isNullOrEmpty(destFileName)
                                            || (!srcFileName.equals(destFileName))) {
                                        LogUtil.i(TAG, "not match");
                                        continue;
                                    }

                                    duMedia.setFileUrl(duFile.getUrl());
                                    duMedia.setFileHash(duFile.getFileHash());
                                }
                            }
                        }
                    }

                    subscriber.onNext(true);
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(new Throwable(e.getMessage()));
                } finally {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<DUEntityResult> uploadMediaRelations(String taskId, List<DUFile> duFileList) {
        connect();

        return restfulApiService.uploadFileRelations(taskId, duFileList);
    }

    public Observable<DUEntitiesResult<DUProcess>> getOrderProcessInfo(String taskId) {
        connect();

        return restfulApiService.getOrderProcessInfo(taskId);
    }

    public Observable<Boolean> uploadLogFiles() {
        connect();

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                File zipFile = null;
                try {
                    String account = TextUtil.getString(mConfigHelper.getAccount());
                    String path = mConfigHelper.getDBFilePath().getPath();
                    int index = path.lastIndexOf("/");
                    String name = path.substring(index + 1);
                    String dir = path.substring(0, index);
                    if (TextUtil.isNullOrEmpty(name)
                            || TextUtil.isNullOrEmpty(dir)) {
                        throw new NullPointerException("name or dir is null");
                    }

                    String zipFileName = String.format("%s_%d.zip", account, new Date().getTime());
                    String zipFilePath = String.format("%s/%s", dir, zipFileName);
                    ZipUtil.ZipFolder(path, zipFilePath);
                    zipFile = new File(zipFilePath);
                    if (!zipFile.exists()) {
                        subscriber.onError(new Throwable("file isn't existing"));
                        return;
                    }

                    //List<DUMedia> destDUMediaList = new ArrayList<DUMedia>();
                    Map<String, RequestBody> params = new HashMap<>();
                    // create RequestBody instance from file
                    RequestBody requestBody =
                            RequestBody.create(MediaType.parse("multipart/form-data"), zipFile);
                    params.put("file\"; filename=\"" + zipFile.getName(), requestBody);
                    Call<DUEntitiesResult<DUFile>> call = restfulApiService.uploadFiles(params);
                    Response<DUEntitiesResult<DUFile>> response = call.execute();
                    DUEntitiesResult<DUFile> duFileDUEntitiesResult = response.body();
                    List<DUFile> duFileList = duFileDUEntitiesResult.getData();
//                    if ((duFileList != null)
//                            && (duFileList.size() == destDUMediaList.size())) {
//                        for (DUMedia duMedia : destDUMediaList) {
//                            for (DUFile duFile : duFileList) {
//                                String srcFileName = duMedia.getFileName();
//                                String destFileName = duFile.getOriginName();
//                                if (TextUtil.isNullOrEmpty(srcFileName)
//                                        || TextUtil.isNullOrEmpty(destFileName)
//                                        || (!srcFileName.equals(destFileName))) {
//                                    LogUtil.i(TAG, "not match");
//                                    continue;
//                                }
//
//                                duMedia.setFileUrl(duFile.getUrl());
//                                duMedia.setFileHash(duFile.getFileHash());
//                            }
//                        }
//                    }
                    if (duFileList != null && duFileList.size() == 1) {
                        subscriber.onNext(true);
                    } else {
                        subscriber.onNext(false);
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, e.getMessage());
                    e.printStackTrace();
                    subscriber.onError(new Throwable(e.getMessage()));
                } finally {
                    if ((zipFile != null) && zipFile.exists()) {
                        zipFile.delete();
                    }
                    subscriber.onCompleted();
                }
            }
        });
    }

    private void connect() {
        if (isConnected) {
            return;
        }

        SystemConfig systemConfig = mConfigHelper.getSystemConfig();
//        String baseUrl = systemConfig.getString(SystemConfig.PARAM_SERVER_BASE_URI);
//        restfulApiService = RestfulApiService.Factory.newInstance(mBus, baseUrl);

        String baseUrlOther;
        if (systemConfig.getBoolean(SystemConfig.PARAM_SERVER_USING_RESERVED, false)) {
            baseUrlOther = systemConfig.getString(SystemConfig.PARAM_RESERVED_SERVER_BASE_URI_OTHER);
        } else {
            baseUrlOther = systemConfig.getString(SystemConfig.PARAM_SERVER_BASE_URI_OTHER);
        }

        isRestfulApi = systemConfig.getBoolean(SystemConfig.PARAM_SYS_RESTFUL_API, false);
        boolean isDebug = systemConfig.getBoolean(SystemConfig.PARAM_SYS_IS_DEBUG_MODE, true);
        if (isRestfulApi) { // restful api

        } else { // json rpc
            jsonRpcService = new JsonRpcService();
            jsonRpcService.init(baseUrlOther);
            jsonRpcService.setDebug(isDebug);
        }
        isConnected = true;
    }
}
