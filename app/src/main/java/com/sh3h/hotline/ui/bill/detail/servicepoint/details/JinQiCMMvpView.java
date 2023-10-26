package com.sh3h.hotline.ui.bill.detail.servicepoint.details;

import com.sh3h.hotline.entity.JinQiCMEntity;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;

public interface JinQiCMMvpView extends MvpView {

    void showAlert(String msg);

    void hideAlert();

    void giveDatas(JinQiCMEntity jinQiCMEntities);

    void onError(Exception e);

    void completed(String s);

}
