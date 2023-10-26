package com.sh3h.hotline.ui.query;

import com.sh3h.dataprovider.data.entity.response.DUOrder;
import com.sh3h.dataprovider.data.entity.response.DUProcess;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;

/**
 * Created by zhangjing on 2016/9/20.
 */
public interface QueryOrderResultMvpView extends MvpView{

    void onGetDUOrder(List<DUOrder> duOrderList);

    void showMessage(String message);

    void onLoadOrderProcessError(String message);

    /**
     * 获取工单流程信息成功
     * @param order 工单详细信息
     * @param duProcesses 工单流程信息
     */
    void onLoadOrderProcessSuccess(DUOrder order, List<DUProcess> duProcesses);
}
