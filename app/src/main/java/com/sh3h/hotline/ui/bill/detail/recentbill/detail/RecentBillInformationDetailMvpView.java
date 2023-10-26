package com.sh3h.hotline.ui.bill.detail.recentbill.detail;

import com.sh3h.dataprovider.data.entity.response.DURecentBillDetail;
import com.sh3h.hotline.ui.base.MvpView;

/**
 * Created by dengzhimin on 2016/9/26.
 */

public interface RecentBillInformationDetailMvpView extends MvpView {

    void initData(DURecentBillDetail data);

    void showToast(String message);
}
