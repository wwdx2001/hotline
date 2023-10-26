package com.sh3h.hotline.service.factory;

import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.hotline.MainApplication;
import com.sh3h.hotline.service.handler.DownloadHandler;
import com.sh3h.hotline.service.handler.OtherHandler;
import com.sh3h.hotline.service.handler.QueryHandler;
import com.sh3h.hotline.service.handler.UploadHandler;
import com.squareup.otto.Bus;

/**
 * Created by dengzhimin on 2016/10/17.
 */

public class HandlerFactory implements AbstractFactory {

    private MainApplication mainApplication;
    private DataManager dataManager;
    private ConfigHelper configHelper;
    private EventPosterHelper eventPosterHelper;
    private Bus bus;

    public HandlerFactory(MainApplication mainApplication, DataManager dataManager,
                          ConfigHelper configHelper, EventPosterHelper eventPosterHelper, Bus bus){
        this.mainApplication = mainApplication;
        this.dataManager = dataManager;
        this.configHelper = configHelper;
        this.eventPosterHelper = eventPosterHelper;
        this.bus = bus;
    }

    @Override
    public DownloadHandler createDownloadHandler() {
        return new DownloadHandler(mainApplication, dataManager, configHelper, eventPosterHelper, bus);
    }

    @Override
    public UploadHandler createUploadHandler() {
        return new UploadHandler(mainApplication, dataManager, configHelper, eventPosterHelper, bus);
    }

    @Override
    public QueryHandler createQueryHandler() {
        return new QueryHandler(mainApplication, dataManager, configHelper, eventPosterHelper, bus);
    }

    @Override
    public OtherHandler createOtherHandler() {
        return new OtherHandler(mainApplication, dataManager, configHelper, eventPosterHelper, bus);
    }
}
