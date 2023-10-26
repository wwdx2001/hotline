package com.sh3h.dataprovider.data.remote;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sh3h.dataprovider.data.entity.response.DUFile;
import com.sh3h.dataprovider.data.entity.request.DULogin;
import com.sh3h.dataprovider.data.entity.response.DUArrearsDetail;
import com.sh3h.dataprovider.data.entity.response.DUBillBaseInfo;
import com.sh3h.dataprovider.data.entity.request.DUHandle;
import com.sh3h.dataprovider.data.entity.response.DUHandleResult;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.data.entity.response.DUNews;
import com.sh3h.dataprovider.data.entity.response.DUOrder;
import com.sh3h.dataprovider.data.entity.response.DUOrderStatusResult;
import com.sh3h.dataprovider.data.entity.response.DUProcess;
import com.sh3h.dataprovider.data.entity.response.DURecentBillDetail;
import com.sh3h.dataprovider.data.entity.request.DUCreateSelfOrder;
import com.sh3h.dataprovider.data.entity.response.DUCreateSelfOrderResult;
import com.sh3h.dataprovider.data.entity.base.DUEntitiesResult;
import com.sh3h.dataprovider.data.entity.base.DUEntityResult;
import com.sh3h.dataprovider.data.entity.response.DURecentBill;
import com.sh3h.dataprovider.data.entity.response.DUReplyOrderResult;
import com.sh3h.dataprovider.data.entity.response.DUUser;
import com.sh3h.dataprovider.data.entity.response.DUWord;
import com.squareup.otto.Bus;

import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface RestfulApiService {

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    @GET("v1/mobile/user/{USERID}")
    Observable<DUEntityResult<DUUser>> getUserInfo(@Path("USERID") int userId);

    /**
     * 登录
     *
     * @return
     */
    @POST("v1/mobile/auth")
    Observable<DUEntityResult<com.sh3h.dataprovider.data.entity.response.DULogin>> auth(@Body DULogin request);

    /**
     * 近期开账详细信息
     *
     * @param cardId 销根号
     * @param feeId  费用Id
     * @return
     */
    @GET("v1/mobile/account/detail")
    Observable<DUEntityResult<DURecentBillDetail>> getRecentBillDetail(@Query("cardId") String cardId, @Query("feeId") int feeId);

    /**
     * 下载工单任务
     *
     * @return
     */
    @GET("v1/mobile/tasks/list?")
    Observable<DUEntitiesResult<DUMyTask>> downloadMyTasks(@Query("userId") int userId);

    /**
     * 处理工单
     *
     * @param userId
     * @param requestReplay
     * @return
     */
    @Headers({"Content-type: application/json"})
    @POST("v1/mobile/tasks/reply")
    Observable<DUEntitiesResult<DUReplyOrderResult>> requestReplyTask(@Query("userId") int userId, @Body RequestBody requestReplay);

    /**
     * 近期开账
     *
     * @param cardId     销根号
     * @param monthCount 最近几个月
     * @return
     */
    @GET("v1/mobile/account/search")
    Observable<DUEntitiesResult<DURecentBill>> getRecentBill(@Query("cardId") String cardId, @Query("monthCount") int monthCount);

    /**
     * 欠费详细信息
     *
     * @param cardId 销根号
     * @param feeId  费用Id
     * @return
     */
    @GET("v1/mobile/arrearage/detail")
    Observable<DUEntityResult<DUArrearsDetail>> getArrearageDetail(@Query("cardId") String cardId, @Query("feeId") int feeId);

    /**
     * 欠费信息
     *
     * @param cardId     销根号
     * @param monthCount 最近几个月
     * @return
     */
    @GET("v1/mobile/arrearage/search")
    Observable<DUEntitiesResult<DURecentBill>> getArrearage(@Query("cardId") String cardId, @Query("monthCount") int monthCount);

    /**
     * 账务查询
     *
     * @param cardId  销根号
     * @param name    户名
     * @param address 地址
     * @return
     */
    @GET("v1/mobile/billing/search")
    Observable<DUEntitiesResult<DUBillBaseInfo>> searchBill(@Query("cardId") String cardId,
                                                            @Query("tableNumber") String tableNumber,
                                                            @Query("name") String name,
                                                            @Query("address") String address, @Query("fuzzySearch") boolean fuzzySearch);

    /**
     * 获取最新公告
     *
     * @return
     */
    @GET("v1/mobile/news/list")
    Observable<DUEntitiesResult<DUNews>> getNews();


    @GET("v1/mobile/news/search?")
    Observable<DUEntitiesResult<DUNews>> searchNews(@Query("title") String title,
                                                    @Query("content") String content,
                                                    @Query("type") String type,
                                                    @Query("startTime") long startTime,
                                                    @Query("endTime") long endTime);

    /**
     * 工单查询
     *
     * @param taskId    任务编号
     * @param name      户名
     * @param address   地址
     * @param telephone 电话
     * @return
     */
    @GET("v1/mobile/task/search?")
    Observable<DUEntitiesResult<DUOrder>> searchOrder(@Query("taskId") String taskId, @Query("name") String name,
                                                      @Query("address") String address,
                                                      @Query("telephone") String telephone,
                                                      @Query("issueOrigin") String issueOrigin,
                                                      @Query("issueType") String issueType,
                                                      @Query("issueContent") String issueContent,
                                                      @Query("loginStation") String loginStation,
                                                      @Query("admissibleSite") String admissibleSite,
                                                      @Query("issueArea") String issueArea,
                                                      @Query("startTime") long startTime, @Query("endTime") long endTime,
                                                      @Query("fuzzySearch") boolean fuzzySearch);

    /**
     * 分页获取工单任务
     *
     * @param userId 用户id
     * @param since  起始位置
     * @param count  查询数量
     * @return
     */
    @GET("v1/mobile/tasks/list")
    Observable<DUEntitiesResult<DUOrder>> getOrders(@Query("userId") String userId, @Query("since") int since, @Query("count") int count);

    /**
     * 工单处理
     *
     * @param requests 请求参数封装体
     * @param userId   用户id
     * @return
     */
    @POST("v1/mobile/tasks/reply")
    Observable<DUEntityResult<DUHandleResult>> handleOrder(@Body List<DUHandle> requests, @Query("userId") String userId);

    /**
     * 上传自开单
     *
     * @param requests
     * @param userId
     * @return
     */
    @Headers({"Content-type: application/json"})
    @POST("v1/mobile/tasks/report")
    Observable<DUEntitiesResult<DUCreateSelfOrderResult>> uploadCreateSelfOrder(@Body List<DUCreateSelfOrder> requests,
                                                                                @Query("userId") int userId);

    /**
     * 获取工单任务状态
     *
     * @param request
     * @return
     */
    @POST("v1/mobile/tasks/search")
    Observable<DUEntitiesResult<DUOrderStatusResult>> getOrderStatus(@Body RequestBody request);

    /**
     * 获取词语信息
     *
     * @param group
     * @return
     */
    @GET("v1/mobile/words/list")
    Observable<DUEntitiesResult<DUWord>> downloadWords(@Query("group") String group);

    /**
     * 获取多媒体信息
     *
     * @param hash
     * @return
     */
    @GET("v1/mobile/image/{HASH}")
    Observable<DUEntityResult<?>> getMediaInfo(@Path("HASH") String hash);

    /**
     * 文件关联
     *
     * @param taskId
     * @param duFileList
     * @return
     */
    @POST("v1/mobile/task/{taskId}/fileInfos/upload")
    Observable<DUEntityResult> uploadFileRelations(@Path("taskId") String taskId, @Body List<DUFile> duFileList);

    /**
     * 上传文件
     *
     * @return
     */
    @Multipart
    @POST("v1/mobile/files/upload")
    Call<DUEntitiesResult<DUFile>> uploadFiles(@PartMap Map<String, RequestBody> params);

    /**
     * 获取工单流程信息
     * 比如：接收、延期、退单、处理等操作内容和回复内容
     *
     * @return
     */
    @GET("v1/mobile/task/search/reply")
    Observable<DUEntitiesResult<DUProcess>> getOrderProcessInfo(@Query("taskId") String taskId);

    /********
     * Helper class that sets up a new services
     *******/
    class Factory {
        public static RestfulApiService newInstance(Bus bus, String baseUrl) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(true ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE); // BuildConfig.DEBUG
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(new UnauthorisedInterceptor(bus))
                    .build();

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(RestfulApiService.class);
        }
    }
}
