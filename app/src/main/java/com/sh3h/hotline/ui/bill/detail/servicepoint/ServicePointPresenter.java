package com.sh3h.hotline.ui.bill.detail.servicepoint;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.hotline.ui.base.ParentPresenter;

public class ServicePointPresenter extends ParentPresenter<ServicePointMvpView> {

    public ServicePointPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void cancelEasyhttpRequest() {

    }
}
