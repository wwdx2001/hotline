package com.sh3h.hotline.ui.order.myorder.delayorback;

import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.request.DUReply;
import com.sh3h.hotline.ui.base.MvpView;

/**
 * Created by zhangjing on 2016/9/12.
 */
public interface DelayOrBackOrderMvpView extends MvpView {

    void onSaveHistoryTask(Boolean aBoolean, DUHistoryTask duHistoryTask);

    void showMessage(String message);

    void onUpdateHistoryTask(Boolean aBoolean, DUHistoryTask duHistoryTask);
}
