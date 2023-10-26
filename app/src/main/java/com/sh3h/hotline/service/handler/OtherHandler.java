package com.sh3h.hotline.service.handler;


import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.service.SyncMessage;
import com.squareup.otto.Bus;

public class OtherHandler extends BaseHandler {

    private static final String TAG = "OtherHandler";

    public OtherHandler(MainApplication mainApplication,
                        DataManager dataManager,
                        ConfigHelper configHelper,
                        EventPosterHelper eventPosterHelper,
                        Bus bus) {
        super(mainApplication, dataManager, configHelper, eventPosterHelper, bus);
    }

    @Override
    public boolean process(SyncMessage syncMessage) {
        return super.process(syncMessage);
    }
}
