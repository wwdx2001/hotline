package com.sh3h.dataprovider;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/3/6 08:41
 */
public class URL {


//    public static final String BASE_URL = "http://116.228.174.93:8001/CustomerService/";
//    public static final String BASE_IN_URL = "http://10.200.201.22:8001/CustomerService/";

    // 正式地址
//    public static final String BASE_IN_URL = "https://cb.shanghaiwater.com:7901/CustomerService/";
//    public static final String BASE_URL = "https://cb.shanghaiwater.com:7901/CustomerService/";
//    public static final String BASE_URL = "http://210.13.106.203:8001/CustomerService/";
//    public static final String BASE_URL = "http://128.1.3.40:8080/CustomerService/";
//    public static final String BASE_URL = "http://180.167.190.84:8080/CustomerService/";

//    public static final String BASE_IN_URL = "http://180.167.6.229:14000/dingeAPPAPI/CustomerService/";
//    public static final String BASE_URL = "http://180.167.6.229:14000/dingeAPPAPI/CustomerService/";
//    public static final String BASE_URL_CB = "http://180.167.6.229:14000/dingeAPPAPI/CustomerService/";

    //市北宝山
//    public static final String BASE_IN_URL = "http://222.69.159.99:7676/dingeAPPAPI/CustomerService/";
//    public static final String BASE_URL = "http://180.167.6.229:14000/dingeAPPAPI/CustomerService/";
//    public static final String BASE_URL_CB = "http://222.69.159.99:7676/dingeAPPAPI/CustomerService/";
    //宝山内网
//    public static final String BASE_IN_URL = "http://10.122.8.37:7676/dingeAPPAPI/CustomerService/";
//    public static final String BASE_URL = "http://180.167.6.229:14000/dingeAPPAPI/CustomerService/";
//    public static final String BASE_URL_CB = "http://10.122.8.37:7676/dingeAPPAPI/CustomerService/";

//    public static final String BASE_IN_URL = "https://cbt.shanghaiwater.com:7901/CustomerService/";
//    public static final String BASE_URL = "https://cbt.shanghaiwater.com:7901/CustomerService/";
//    public static final String BASE_URL_CB = "https://cb.shanghaiwater.com:7900/ChaoBiao/";
    // 测试地址
//    public static final String BASE_IN_URL = "http://128.1.8.43:8000/CustomerService/";
//    public static final String BASE_URL_TEST = "https://cbt.shanghaiwater.com:7901/CustomerService/";
//    public static final String BASE_URL_TEST = "http://180.167.190.89:8000/CustomerService/";
//    public static final String BASE_URL_TEST = "http://128.1.8.43:8000/CustomerService/ZhangHuXXQuery";

    /**
     * 登录
     * http://180.167.6.229:14000/dingeAPPAPI/CustomerService/login
     */
    public static final String Login = "login";

    /**
     * 客户欠费查询
     * http://128.1.3.40:8080/ChaoBiao/KeHuQFQuery?acctId=9581590100
     * https://cb.shanghaiwater.com:7900/ChaoBiao/KeHuQFQuery?acctId=9581590100
     */
    public static final String KeHuQFQuery = "KeHuQFQuery";

    /**
     * 违章用水自开单
     * https://cbt.shanghaiwater.com:7901/CustomerService/WeiZhangYSSelfOpeningOrder
     * https://cb.shanghaiwater.com:7901/CustomerService/WeiZhangYSSelfOpeningOrder
     */
    public static final String WeiZhangYSSelfOpeningOrder = "WeiZhangYSSelfOpeningOrder";

    /**
     * 疑问水表自开单
     * https://cbt.shanghaiwater.com:7901/CustomerService/YiWenSBSelfOpeningOrder
     * https://cb.shanghaiwater.com:7901/CustomerService/YiWenSBSelfOpeningOrder
     */
    public static final String YiWenSBSelfOpeningOrder = "YiWenSBSelfOpeningOrder";

    /**
     * 普通催缴自开单
     * https://cbt.shanghaiwater.com:7901/CustomerService/CuiJiaoSelfOpeningOrder
     * https://cb.shanghaiwater.com:7901/CustomerService/CuiJiaoSelfOpeningOrder
     */
    public static final String CuiJiaoSelfOpeningOrder = "CuiJiaoSelfOpeningOrder";

    /**
     * 催缴任务列表
     * https://cbt.shanghaiwater.com:7901/CustomerService/CuiJIaoGDSelCount
     * https://cb.shanghaiwater.com:7901/CustomerService/CuiJIaoGDSelCount
     */
    public static final String CuiJIaoGDSelCount = "CuiJIaoGDSelCount";

    /**
     * 催缴工单列表
     * https://cbt.shanghaiwater.com:7901/CustomerService/CuJIaoGDSel
     * https://cb.shanghaiwater.com:7901/CustomerService/CuJIaoGDSel
     */
    public static final String CuJIaoGDSel = "CuJIaoGDSel";

    /**
     * 催缴工单详情
     * https://cbt.shanghaiwater.com:7901/CustomerService/CuJIaoGDSelByALBH
     * https://cb.shanghaiwater.com:7901/CustomerService/CuJIaoGDSelByALBH
     */
    public static final String CuJIaoGDSelByALBH = "CuJIaoGDSelByALBH";

    /**
     * 催缴工单回填
     * https://cbt.shanghaiwater.com:7901/CustomerService/PTCuiJiaoGDBack
     * https://cb.shanghaiwater.com:7901/CustomerService/PTCuiJiaoGDBack
     */
    public static final String PTCuiJiaoGDBack = "PTCuiJiaoGDBack";

    /**
     * 无数据复核工单列表
     * https://cbt.shanghaiwater.com:7901/CustomerService/NoDataFHYCGDSel
     * https://cb.shanghaiwater.com:7901/CustomerService/NoDataFHYCGDSel
     */
    public static final String NoDataFHYCGDSel = "NoDataFHYCGDSel";

    /**
     * 无数据复核工单回填
     * https://cbt.shanghaiwater.com:7901/CustomerService/NoDataFHYCGDBack
     * https://cb.shanghaiwater.com:7901/CustomerService/NoDataFHYCGDBack
     */
    public static final String NoDataFHYCGDBack = "NoDataFHYCGDBack";

    /**
     * 违章用水工单列表
     * https://cbt.shanghaiwater.com:7901/CustomerService/WeiZhangYSSel
     * https://cb.shanghaiwater.com:7901/CustomerService/WeiZhangYSSel
     */
    public static final String WeiZhangYSSel = "WeiZhangYSSel";

    /**
     * 违章用水工单回填
     * https://cbt.shanghaiwater.com:7901/CustomerService/WeiZhangYSBack
     * https://cb.shanghaiwater.com:7901/CustomerService/WeiZhangYSBack
     */
    public static final String WeiZhangYSBack = "WeiZhangYSBack";

    /**
     * 疑问水表工单列表
     * https://cbt.shanghaiwater.com:7901/CustomerService/YiWenSBSel
     * https://cb.shanghaiwater.com:7901/CustomerService/YiWenSBSel
     */
    public static final String YiWenSBSel = "YiWenSBSel";

    /**
     * 疑问水表工单回填
     * https://cbt.shanghaiwater.com:7901/CustomerService/YiWenSBBack
     * https://cb.shanghaiwater.com:7901/CustomerService/YiWenSBBack
     */
    public static final String YiWenSBBack = "YiWenSBBack";

    /**
     * 账户信息查询
     * https://cbt.shanghaiwater.com:7901/CustomerService/ZhangHuXXQuery
     * https://cb.shanghaiwater.com:7901/CustomerService/ZhangHuXXQuery
     */
    public static final String ZhangHuXXQuery = "ZhangHuXXQuery";

    /**
     * 非居超定额客户资料变更
     * https://cbt.shanghaiwater.com:7901/CustomerService/FeiJuLJZLBG
     * https://cb.shanghaiwater.com:7901/CustomerService/FeiJuLJZLBG
     */
    public static final String FeiJuLJZLBG = "FeiJuLJZLBG";

    /**
     * 发单任务获取
     * https://cbt.shanghaiwater.com:7901/CustomerService/FaDanRWSel
     * https://cb.shanghaiwater.com:7901/CustomerService/FaDanRWSel
     */
    public static final String FaDanRWSel = "FaDanRWSel";

    /**
     * 发单任务回填
     * https://cbt.shanghaiwater.com:7901/CustomerService/FaDanRWBack
     * https://cb.shanghaiwater.com:7901/CustomerService/FaDanRWBack
     */
    public static final String FaDanRWBack = "FaDanRWBack";

    /**
     * 发单任务统计
     * https://cbt.shanghaiwater.com:7901/CustomerService/FaDanRWCount
     * https://cb.shanghaiwater.com:7901/CustomerService/FaDanRWCount
     */
    public static final String FaDanRWCount = "FaDanRWCount";

    /**
     * 发单任务延期
     * https://cbt.shanghaiwater.com:7901/CustomerService/FaDanRWDelay
     * https://cb.shanghaiwater.com:7901/CustomerService/FaDanRWDelay
     */
    public static final String FaDanRWDelay = "FaDanRWDelay";

    /**
     * 发单任务退单
     * https://cbt.shanghaiwater.com:7901/CustomerService/FaDanRWCancel
     * https://cb.shanghaiwater.com:7901/CustomerService/FaDanRWCancel
     */
    public static final String FaDanRWCancel = "FaDanRWCancel";

    /**
     * 催缴任务获取
     * https://cbt.shanghaiwater.com:7901/CustomerService/CuJIaoGDSelFJ
     * https://cb.shanghaiwater.com:7901/CustomerService/CuJIaoGDSelFJ
     */
    public static final String CuJIaoGDSelFJ = "CuJIaoGDSelFJ";

    /**
     * 催缴任务回填
     * https://cbt.shanghaiwater.com:7901/CustomerService/CuiJiaoGDBack
     * https://cb.shanghaiwater.com:7901/CustomerService/CuiJiaoGDBack
     */
    public static final String CuiJiaoGDBack = "CuiJiaoGDBack";

    /**
     * 催缴任务统计
     * https://cbt.shanghaiwater.com:7901/CustomerService/CuJIaoGDSelFJCount
     * https://cb.shanghaiwater.com:7901/CustomerService/CuJIaoGDSelFJCount
     */
    public static final String CuJIaoGDSelFJCount = "CuJIaoGDSelFJCount";

    /**
     * 催缴任务延期
     * https://cbt.shanghaiwater.com:7901/CustomerService/CuJIaoGDDelay
     * https://cb.shanghaiwater.com:7901/CustomerService/CuJIaoGDDelay
     */
    public static final String CuJIaoGDDelay = "CuJIaoGDDelay";

    /**
     * 催缴任务退单
     * https://cbt.shanghaiwater.com:7901/CustomerService/CuJIaoGDCancel
     * https://cb.shanghaiwater.com:7901/CustomerService/CuJIaoGDCancel
     */
    public static final String CuJIaoGDCancel = "CuJIaoGDCancel";

    /**
     * 催缴账单详情
     * https://cbt.shanghaiwater.com:7901/CustomerService/FeiJuCDEQFXQ
     * https://cb.shanghaiwater.com:7901/CustomerService/FeiJuCDEQFXQ
     */
    public static final String FeiJuCDEQFXQ = "FeiJuCDEQFXQ";

    /**
     * 超定额照片上传
     * https://cbt.shanghaiwater.com:7901/CustomerService/AppReturnCDEOrderUploadFile
     * https://cb.shanghaiwater.com:7901/CustomerService/AppReturnCDEOrderUploadFile
     */
    public static final String AppReturnCDEOrderUploadFile = "AppReturnCDEOrderUploadFile";

    /**
     * 客户基本信息查询
     * http://128.1.3.40:8080/CustomerService/CustomerInfoQuery?acctId=1801241100
     * https://cb.shanghaiwater.com:7901/CustomerService/CustomerInfoQuery?acctId=1801241100
     */
    public static final String CustomerInfoQuery = "CustomerInfoQuery";

    /**
     * 模糊地址信息查询账户编号
     * http://128.1.3.40:8080/CustomerService/GetAcctIdByAddress
     * https://cb.shanghaiwater.com:7901/CustomerService/GetAcctIdByAddress
     */
    public static final String GetAcctIdByAddress = "GetAcctIdByAddress";

    /**
     * 最近换表工单查询
     * http://128.1.3.40:8080/CustomerService/ZuiJinHBGDQuery?acctId=1801241100
     * https://cb.shanghaiwater.com:7901/CustomerService/ZuiJinHBGDQuery?acctId=1801241100
     */
    public static final String ZuiJinHBGDQuery = "ZuiJinHBGDQuery";

    /**
     * 单户总欠费查询
     * http://128.1.3.40:8080/CustomerService/TotalArrearsQuery?acctId=1801241100
     * https://cb.shanghaiwater.com:7901/CustomerService/TotalArrearsQuery?acctId=1801241100
     */
    public static final String TotalArrearsQuery = "TotalArrearsQuery";

    /**
     * 单户账单明细查询
     * http://128.1.3.40:8080/CustomerService/BillDetailsQuery?acctId=1801241100
     * https://cb.shanghaiwater.com:7901/CustomerService//BillDetailsQuery?acctId=1801241100
     */
    public static final String BillDetailsQuery = "BillDetailsQuery";

    /**
     * 近期抄码查询
     * http://128.1.3.40:8080/CustomerService/RecentCodingQuery?acctId=1801241100
     * https://cb.shanghaiwater.com:7901/CustomerService/RecentCodingQuery?acctId=1801241100
     */
    public static final String RecentCodingQuery = "RecentCodingQuery";

    /**
     * 停水公告查询
     * http://128.1.3.40:8080/CustomerService/WaterShutdownAnnouncementQuery?cmQueryMsg=Y
     * https://cb.shanghaiwater.com:7901/CustomerService/WaterShutdownAnnouncementQuery?cmQueryMsg=Y
     */
    public static final String WaterShutdownAnnouncementQuery = "WaterShutdownAnnouncementQuery";

    /**
     * 客服代表自开单
     * http://128.1.3.40:8080/CustomerService/SelfOpeningOrder
     * https://cb.shanghaiwater.com:7901/CustomerService/SelfOpeningOrder
     */
    public static final String SelfOpeningOrder = "SelfOpeningOrder";

    /**
     * 表务类工单信息同步
     * http://128.1.3.40:8080/CustomerService/WorkInfoSynchronization
     */
    public static final String WorkInfoSynchronization = "WorkInfoSynchronization";

    /**
     * 下载词语信息
     * http://128.1.3.40:8080/CustomerService/GetWordList
     * https://cb.shanghaiwater.com:7901/CustomerService/GetWordList
     */
    public static final String GetWordList = "GetWordList";

    /**
     * 工单状态变更
     * https://cb.shanghaiwater.com:7901/CustomerService/WorkOrderStatusChange
     */
    public static final String WorkOrderStatusChange = "WorkOrderStatusChange";

    /**
     * 查询我的工单
     * https://cb.shanghaiwater.com:7901/CustomerService/GetWordOrderList
     */
    public static final String GetWordOrderList = "GetWordOrderList";

    /**
     * 上传多媒体数据
     * https://cb.shanghaiwater.com:7901/CustomerService/AppReturnOrderUploadFile
     */
    public static final String AppReturnOrderUploadFile = "AppReturnOrderUploadFile";


    /**
     * 工单数据上传
     * https://cb.shanghaiwater.com:7901/CustomerService/AppReturnOrder
     */
    public static final String AppReturnOrder = "AppReturnOrder";

    /**
     * 客服代表自开单查询
     * https://cb.shanghaiwater.com:7901/CustomerService/SelfOpeningOrderQuery
     */
    public static final String SelfOpeningOrderQuery = "SelfOpeningOrderQuery";

    /**
     * 将deviceToken推送到服务端
     * https://cb.shanghaiwater.com:7901/CustomerService/UpdataToken
     */
    public static final String UpdataToken = "UpdataToken";


}
