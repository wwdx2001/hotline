package com.sh3h.dataprovider.util;

/**
 * Created by dengzhimin on 2016/10/9.
 */

public class Constant {

    public static final String USERID = "USER_ID";//userId
//    public static final String USERID = "QNXS";//userId

    //上传标志
    public static final int INVALID_UPLOAD = -1;//无效值
    public static final int NO_UPLOAD = 0;//未上传
    public static final int IS_UPLOADING = 1;//正在上传
    public static final int HAS_UPLOADED = 2;//已上传

    //接口返回状态码
    public static final int STATUS_CODE_200 = 200;
    public static final int STATUS_CODE_404 = 404;

    //自开单上传状态
    public static final int UPLOAD_FLAG_DEFAULT = 0;//未上传
    public static final int UPLOAD_FLAG_UPLOADED = 2;//上传完成

    //提示信息
    public static final String CREATE_SELF_ORDER_UPLOAD_FAILED = "自开单上传更新失败,保存至本地";
    public static final String CREATE_SELF_ORDER_UPLOAD_FINISH = "自开单上传更新完成";
    public static final String UPLOAD_SUCCESS = "上传更新成功";
    public static final String UPLOAD_FAILED = "上传更新失败";
    public static final String DOWNLOAD_MY_TASKS_SCCESS = "工单任务更新完成";
    public static final String DOWNLOAD_MY_TASKS_FAILED = "工单任务更新失败";

    //Intent Key
    public static final String ACCOUNT = "account";
    public static final String CARDID = "cardId";
    public static final String NAME = "name";
    public static final String NONGHAO = "nonghao";
    public static final String LUMING = "luming";
    public static final String ADDRESS = "address";
    public static final String TABLE_NUMBER = "tableNumber";
    public static final String FEEID = "feeId";
    public static final String BILLBASEINFO = "billBaseInfo";
    public static final String TASK_ID = "taskId";
    public static final String TASK_TYPE = "taskType";
    public static final String TASK_STATE = "taskState";
    public static final String TELEPHONE = "telephone";
    public static final String ISSUEORIGIN = "issueOrigin";
    public static final String DU_HISTORY_TASK = "DUHistoryTask";
    public static final String HANDLE_STATE = "HandleState";
    public static final String UPLOAD_FLAG = "uploadFlag";

    public static final String POSITION = "position";
    public static final String URLS = "urls";

    public static final String TASKSTATE = "state";
    public static final String ORIGIN = "origin";

    //界面跳转标志
    public static final int ORIGIN_MY_TASK = 0;//我的工单
    public static final int ORIGIN_MY_TASK_HISTORY = 1;//我的历史工单
    public static final int ORIGIN_CREATE_SELF_ORDER = 2;//自开单
    public static final int ORIGIN_CREATE_SELF_ORDER_HISTORY = 3;//自开单历史
    public static final int ORIGIN_TASK_QUERY_RESULT = 4;//工单查询结果
    public static final String VIDEO_FILE_PATH = "videoFilePath";//工单查询视频文件地址

    public static final int TASK_TYPE_DOWNLOAD = 0;
    public static final String DATA_IS_UPLOAD = "dataUpload";
    public static final String TYPE_IS_HISTORY = "TYPE_IS_HISTORY";
    public static final String TASK_REPLY = "taskReply";
    public static final String OTHER_REASON = "otherReason";
    public static final String DELAY_TIME = "delayTime";
    public static final String REPLY_TIME = "replyTime";
    public static final String INTENT_PARAM_ORDER = "order";//工单详情
    public static final String INTENT_PARAM_ORDER_PROCESS_LIST = "orderProcessList";//工单流程List
    public static final String INTENT_PARAM_ORDER_PROCESS = "orderProcess";//工单流程
    public static final String INTENT_PARAM_ORDER_PROCES_FILE = "orderProcessFile";//工单流程多媒体文件

    public static final String RESOLVE_TYPE = "resolveType";
    public static final String RESOLVE_CONTENT = "resolveContent";
    public static final String ISSUE_REASON = "issueReason";
    public static final String RESOLVE_METHOD = "resolveMethod";
    public static final String RESOLVE_RESULT = "resolveResult";
    public static final String RESOLVE_COMMENT = "resolveComment";
    public static final String IS_FINISH_TASK = "isFinishTask";
    public static final String RESOLVE_TIME = "resolveTime";
    public static final String ARRIVE_TIME = "arriveTime";

    public static final String ORDER_QUERY_DETAILS = "orderDetails";
    public static final String NOTICE_DETAILS = "noticeDetails";
    public static final String START_DATE = "startTime";
    public static final String END_DATE = "endTime";

    public static final String HANDLE_TYPE = "处理类别";
    public static final String HANDLE_CONTENT = "处理内容";
    public static final String HAPPEN_REASON = "发生原因";
    public static final String SOVLE_METHOD = "解决措施";
    public static final String REPLY_CLASS = "处理级别";
    public static final String ISSUE_CONTENT = "反映内容";
    public static final String ISSUE_LEIXING = "反映类型";
    public static final String ISSUE_TYPE = "反映类别";
    public static final String ISSUE_AREA = "反映区名";
    public static final String ISSUE_ORIGIN = "反映来源";
    public static final String HANDLE_RESULT = "处理结果";
    public static final String LOGIN_STATION = "登录站点";
    public static final String ADMISSIBLE_SITE = "受理站点";

    //任务类型
    public static final int TASK_TYPE_DOWNLOAD_ORDER = 0;//下载的工单
    public static final int TASK_TYPE_CREATE_SELF_ORDER = 1;//自开单

    //任务状态
    //    A：已接单 C：已完成 D：已调度 E：延期失败 F：已销单 P：按用户待定 T：申请退单
    //    U：销单失败 W：作业中 X：已取消 Y：申请延期
    public static final int TASK_STATE_ALL = -1;//所有状态
    public static final int TASK_STATE_SENDWORK = 0;//派工,D：已调度
    public static final int TASK_STATE_RECEIVED = 1;//接收,A：已接单
    public static final int TASK_STATE_DELAY = 2;//延期,Y：申请延期
    public static final int TASK_STATE_BACK = 3;//退单,T：申请退单
    public static final int TASK_STATE_CANCEL = 4;//销单,F：已销单
    public static final int TASK_STATE_CANCEL_FAILED = 5;//销单不合格,U：销单失败
    public static final int TASK_STATE_CANCEL_SUCCESS = 6;//销单成功,X：已取消
    public static final int TASK_STATE_DELAY_FAILED = 7;//延期不通过,E：延期失败
    public static final int TASK_STATE_DELAY_SUCCESS = 8;//延期成功
    public static final int TASK_STATE_CREATE = 9;//自开单创建
    public static final int TASK_STATE_CREATE_HANDLE = 10;//自开单处理
    public static final int TASK_STATE_FINISH = 11;//,C：已完成

    public static final String DUMyTask = "DUMyTask";
    public static final String DUHistoryTask = "DUHistoryTask";
    public static final String REPLY_IMMEDIATELY = "replyImmediately";
    public static final String SERVER_TASK_ID = "serverTaskId";
    public static final String VIDEO_PATH = "videoPath";
    public static final String REPLY_TIME_IN_HISTORY = "replyTimeInHistory";
    public static final String START_TIME_UTC = "startTimeUtc";
    public static final String END_TIME_UTC = "endTimeUtc";
    public static final String ISSUE_AREA_EX = "issueArea";
    public static final String ISSUE_ORIGIN_EX = "issueOrigin";

    public static boolean isAlertDeadline = false;
    public static boolean isAlertDeadline2 = true;
    public static final int INVALID_NUMBER = -1;
    /*WebView url*/
    public static final String WEB_VIEW_URL = "WebViewUrl";
    public static final String CHOOSE_STATION = "station";
    public static final String LOGIN_SITE = "loginSite";
    public static final String ACCEPT_SITE = "acceptSite";


    //xiaochao
    public static final String ZIKAIDAN_JL = "zikaidanJL";

    public static final String ORDER_ADAPTER_TYPE = "orderType";
    public static final int ADAPTER_TYPE_NORMAL = 1;
    public static final int ADAPTER_TYPE_HISTORY = 0;

    public static final String DEVICE_TOKEN = "deviceToken";
    public static final String PRE_DEVICE_TOKEN = "pre_deviceToken";
    public static final String IS_POST_DEVICE_TOKEN_SERVICE = "isPostDeviceTokenService";

    public static final String COLLECTION_NUM = "collectionNum";
}
