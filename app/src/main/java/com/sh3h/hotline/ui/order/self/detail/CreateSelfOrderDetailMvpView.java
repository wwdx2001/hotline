package com.sh3h.hotline.ui.order.self.detail;

import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;

/**
 * Created by dengzhimin on 2016/9/21.
 */

public interface CreateSelfOrderDetailMvpView extends MvpView {

    void onGetTaskHandle(List<DUHistoryTask> duHistoryTask);
}
