package com.sh3h.hotline.service.handler;

import com.sh3h.hotline.service.SyncMessage;

/**
 * Created by dengzhimin on 2016/10/17.
 */

public interface IHandler {

    /**
     * 创建队列和工作线程
     */
    void start();

    /**
     * 暂停工作线程并清空消息队列
     */
    void stop();

    /**
     * 添加消息到消息队列
     * @param syncMessage
     * @return
     */
    void put(SyncMessage syncMessage);

    /**
     * 执行网络请求
     * @param syncMessage
     * @return
     */
    boolean process(SyncMessage syncMessage);

    /**
     * 关闭线程池
     */
    void shutdown();

}
