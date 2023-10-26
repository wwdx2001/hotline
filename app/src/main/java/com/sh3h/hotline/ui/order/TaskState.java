package com.sh3h.hotline.ui.order;

/**
 * Created by zhangjing on 2016/10/13.
 */

public class TaskState {


    public static final int DISPATCH = 0;//派工,D：已调度
    public static final int RECEIVED = 1;//接收,A：已接单
    public static final int DELAY = 2;//延期中,Y：申请延期
    public static final int BACK = 3;//退单,T：申请退单
    public static final int HANDLE = 4;//处理（销单）,F：已销单
    public static final int FINISH_FAIL = 5;//销根不合格,U：销单失败
    public static final int CANCEL_SUCCESS = 6;//销单成功,X：已取消
    public static final int DELAY_FAILED = 7;//延期不通过,E：延期失败
    public static final int DELAY_SUCCESS = 8;//延期成功
    public static final int CREATE = 9;//自开单创建
    public static final int CREATE_HANDLE = 10;//自开单处理
    public static final int FINISH = 11;//,C：已完成
}
