package com.sh3h.hotline.ui.order.self.create.receipt;

import com.sh3h.dataprovider.data.entity.response.DUBillBaseInfo;
import com.sh3h.dataprovider.data.entity.response.DUWord;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;

/**
 * Created by dengzhimin on 2016/9/18.
 */
public interface ReceiptMvpView extends MvpView {

    void onToDealOrder(boolean isDeal);

    void onHint(int resId);

    void onHint(String error);

    void onSaveSuccess(String savesuccess);

    void onUploadOrder(String taskId, boolean isDeal);

    void onInitFanYingLeiXingSpinner(List<DUWord> words);

    void onInitChuLiJiBieSpinner(List<DUWord> words);

    void onInitFanYingNeiRongSpinner(List<DUWord> words);

    void onInitIssueAreaSpinner(List<DUWord> words);

    void onInitIssueOriginSpinner(List<DUWord> words);

    void onIntent(DUBillBaseInfo info);
}
