package com.sh3h.hotline.ui.order.self.detail.receipt;

import com.sh3h.dataprovider.data.entity.request.DUCreateSelfOrder;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;

/**
 * Created by dengzhimin on 2016/9/18.
 */
public interface ReceiptDetailMvpView extends MvpView {

    void onInitData(DUCreateSelfOrder order);

    void onError(int resId);

    void onError(String message);
}
