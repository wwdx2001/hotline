package com.sh3h.hotline.ui.bill.detail.arrears;

import com.sh3h.dataprovider.data.entity.response.CustomerTotalArrearsResult;
import com.sh3h.dataprovider.data.entity.response.DURecentBill;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;

/**
 * Created by dengzhimin on 2016/9/22.
 */

public interface ArrearsInformationMvpView extends MvpView {

    void showProgressDialog(String msg);

    void hideProgressDialog();

    void onArrearsList(CustomerTotalArrearsResult datas);

    void onError(Exception e);

    void onCompleted(String message);
}
