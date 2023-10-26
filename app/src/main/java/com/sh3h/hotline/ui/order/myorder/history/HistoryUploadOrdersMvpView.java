package com.sh3h.hotline.ui.order.myorder.history;

import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;

/**
 * Created by zhangjing on 2016/9/18.
 */
public interface HistoryUploadOrdersMvpView extends MvpView {

    void onGetHistoryOrdersList(boolean isFirstTime, List<DUMyTask> result);

    void onFilterOrdersList(boolean isFirstTime, List<DUMyTask> result);

    void showMessage(String message);
}
