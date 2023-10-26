package com.sh3h.hotline.ui.bill;

import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.hotline.ui.base.MvpView;

/**
 * Created by dengzhimin on 2016/9/14.
 */
public interface QueryBillMvpView extends MvpView {

    void showProgressDialog(String msg);

    void hindProgressDialog();

    void onFindKehuInfo(CustomerInfoFindResult entity);

    void onError(Exception e);

    void onCompleted(String info);
}
