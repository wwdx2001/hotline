package com.sh3h.hotline.ui.bill.result;

import com.sh3h.dataprovider.data.entity.response.CustomerInfoFindResult;
import com.sh3h.dataprovider.data.entity.response.DUBillBaseInfo;
import com.sh3h.dataprovider.data.entity.response.FuzzyAdressQueryResult;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;

/**
 * Created by dengzhimin on 2016/9/22.
 */

public interface QueryBillResultMvpView extends MvpView {

    void showProgressDialog(String msg);

    void hindProgressDialog();

    void onQueryByAddress(List<FuzzyAdressQueryResult> datas);

    void onFindKehuInfo(CustomerInfoFindResult entity);

    void onError(Exception e);

    void onCompleted(String info);
}
