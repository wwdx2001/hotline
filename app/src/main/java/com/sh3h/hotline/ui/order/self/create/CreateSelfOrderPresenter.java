package com.sh3h.hotline.ui.order.self.create;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.hotline.ui.base.ParentPresenter;

import javax.inject.Inject;

/**
 * Created by dengzhimin on 2016/9/14.
 */
public class CreateSelfOrderPresenter extends ParentPresenter<CreateSelfOrderMvpView> {

    @Inject
    public CreateSelfOrderPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void cancelEasyhttpRequest() {

    }
}
