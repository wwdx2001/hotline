package com.sh3h.hotline.ui.order.self.history;

import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.newentity.ZikaidanJLEntity;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;

/**
 * Created by dengzhimin on 2016/9/14.
 */
public interface CreateSelfOrderHistoryMvpView extends MvpView {

    void onLoadData(List<DUHistoryTask> duHistoryTasks);

    void onLoadDataSuccess(List<ZikaidanJLEntity> duHistoryTasks);

    void onLoadMoreData(List<DUHistoryTask> duHistoryTasks);

    void onError(String resId);

}
