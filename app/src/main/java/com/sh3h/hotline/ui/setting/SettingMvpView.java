package com.sh3h.hotline.ui.setting;

import com.sh3h.hotline.ui.base.MvpView;
/**
 * Created by zhangjing on 2016/11/10.
 */

public interface SettingMvpView extends MvpView{
    void clearHistoryEnd();
    void onUploadLogFiles(Boolean aBoolean);
    void onError(int stringId);
}
