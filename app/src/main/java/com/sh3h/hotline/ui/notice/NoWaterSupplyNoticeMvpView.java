package com.sh3h.hotline.ui.notice;

import com.sh3h.dataprovider.data.entity.response.WaterStopNotificationEntity;
import com.sh3h.hotline.ui.base.MvpView;

import java.util.List;

/**
 * Created by zhangjing on 2016/9/18.
 */
public interface NoWaterSupplyNoticeMvpView extends MvpView {

    void onGetRefreshData(List<WaterStopNotificationEntity> duNewsList, boolean isFromRefresh);

    void showMessage(String message);
}
