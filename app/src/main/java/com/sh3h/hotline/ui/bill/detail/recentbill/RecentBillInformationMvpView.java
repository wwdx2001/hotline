package com.sh3h.hotline.ui.bill.detail.recentbill;

import com.sh3h.dataprovider.data.entity.response.DURecentBill;
import com.sh3h.hotline.entity.RecentBillDetailInfoEntity;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;

/**
 * Created by dengzhimin on 2016/9/22.
 */

public interface RecentBillInformationMvpView extends MvpView {

    void showProgressDialog(String msg);

    void hideProgressDialog();

    void onBillList(List<RecentBillDetailInfoEntity.BillListBean> datas);

    void onError(Exception e);

    void onCompleted(String message);
}
