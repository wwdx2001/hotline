package com.sh3h.hotline.ui.main;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.local.preference.PreferencesHelper;
import com.sh3h.dataprovider.data.local.preference.UserSession;
import com.sh3h.hotline.ui.base.ParentPresenter;

import javax.inject.Inject;

/**
 * Created by dengzhimin on 2016/8/18.
 */
public class MainPresenter extends ParentPresenter<MainMvpView> {

    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public MainPresenter(DataManager dataManager, PreferencesHelper preferencesHelper) {
        super(dataManager);
        mPreferencesHelper = preferencesHelper;
    }

    public void initUser() {
        UserSession userSession = mPreferencesHelper.getUserSession();
        userSession.setUserId(3426);
    }

    @Override
    public void cancelEasyhttpRequest() {

    }
}
