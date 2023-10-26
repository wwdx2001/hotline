package com.sh3h.hotline.ui.bill.detail.zuijingd;

import com.sh3h.hotline.entity.ZuiJinHBGDEntity;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;

public interface ZuijinGDMvpView extends MvpView {

    void showProgressDialog(String msg);

    void hideProgressDialog();

    void onZuijinGD(List<ZuiJinHBGDEntity> entityList);

    void onError(Exception e);

    void onCompleted(String info);
}
