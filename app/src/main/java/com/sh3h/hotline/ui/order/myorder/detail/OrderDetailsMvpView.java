package com.sh3h.hotline.ui.order.myorder.detail;

import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.response.DUBillBaseInfo;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.hotline.ui.base.MvpView;
import com.sh3h.localprovider.greendaoEntity.Task;

/**
 * Created by zhangjing on 2016/9/12.
 */
public interface OrderDetailsMvpView extends MvpView{


    void onGetTaskInfo(DUMyTask task);

    void onGetHistoryTaskInfo(DUHistoryTask duHistoryTask);

    void showMessage(String message);

    void showMessage(int resId);

    void onIntent(DUBillBaseInfo info);

}
