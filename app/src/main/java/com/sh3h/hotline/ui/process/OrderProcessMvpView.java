package com.sh3h.hotline.ui.process;

import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;

/**
 * Created by dengzhimin on 2017/2/23.
 */

public interface OrderProcessMvpView extends MvpView {

    void onError(String message);

    void onSetHistoryTasks(List<DUHistoryTask> duHistoryTasks);
}
