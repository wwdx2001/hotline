package com.sh3h.hotline.ui.order.myorder.history;

import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;

/**
 * Created by zhangjing on 2016/9/18.
 */
public interface HistoryOrdersMvpView extends MvpView {

    void onGetHistoryOrdersList(boolean isFirstTime, List<DUMyTask> result);

    void onFilterOrdersList(boolean isFirstTime, List<DUMyTask> result);

    void onGetSynchronizeData();

    void showMessage(String message);
}
