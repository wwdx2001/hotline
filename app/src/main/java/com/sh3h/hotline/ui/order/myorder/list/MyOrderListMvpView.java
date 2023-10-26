package com.sh3h.hotline.ui.order.myorder.list;

import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;

/**
 * Created by zhangjing on 2016/9/12.
 */
public interface MyOrderListMvpView extends MvpView {


    void onLoadExpiredAndFylxMyTasks(String title, List<DUMyTask> taskList);


    void onError(String message);

    void hideShowProgress();

    void onRequestStart();

    void updataStateSuccess(int type, String msg);

    void updataStateError(String error);


    void onLoadNetWorkData(List<DUMyTask> tasks, boolean isEnter,boolean isDeadline);

    void onLoadDeadLineData(List<DUMyTask> duMyTasks);

    void onLoadAllData(List<DUMyTask> duMyTasks);
}
