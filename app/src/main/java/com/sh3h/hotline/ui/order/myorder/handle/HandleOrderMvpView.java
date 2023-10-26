package com.sh3h.hotline.ui.order.myorder.handle;

import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.request.DUReply;
import com.sh3h.dataprovider.data.entity.response.DUWord;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;

/**
 * Created by zhangjing on 2016/9/14.
 */
public interface HandleOrderMvpView extends MvpView {

    void onSaveHistoryTask(Boolean aBoolean, DUHistoryTask duHistoryTask);

    void onSaveHistoryTaskSuccess(String msg);

    void onCommitTaskSuccess(String msg);

    void onCommitError(String error);

    void showMessage(String message);

    void onUpdateHistoryTask(Boolean aBoolean, DUHistoryTask duHistoryTask);

    void uploadTaskReplyCreateSelf(String taskId);

}
