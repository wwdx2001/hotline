package com.sh3h.hotline.ui.bill.detail.basic;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.hotline.ui.base.ParentPresenter;

import javax.inject.Inject;

/**
 * Created by dengzhimin on 2016/9/22.
 */

public class BasicInformationPresenter extends ParentPresenter<BasicInformationMvpView> {

    @Inject
    public BasicInformationPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void cancelEasyhttpRequest() {

    }
}
