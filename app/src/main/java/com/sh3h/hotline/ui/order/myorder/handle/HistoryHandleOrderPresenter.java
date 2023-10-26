package com.sh3h.hotline.ui.order.myorder.handle;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.hotline.ui.base.ParentPresenter;
import com.sh3h.hotline.ui.order.myorder.history.HistoryOrdersMvpView;

import javax.inject.Inject;

/**
 * Created by zhangjing on 2016/9/19.
 */
public class HistoryHandleOrderPresenter extends ParentPresenter<HistoryOrdersMvpView>{

    @Inject
    public HistoryHandleOrderPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void cancelEasyhttpRequest() {

    }
}
