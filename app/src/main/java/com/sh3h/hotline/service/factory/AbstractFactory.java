package com.sh3h.hotline.service.factory;

import com.sh3h.hotline.service.handler.DownloadHandler;
import com.sh3h.hotline.service.handler.OtherHandler;
import com.sh3h.hotline.service.handler.QueryHandler;
import com.sh3h.hotline.service.handler.UploadHandler;

/**
 * 工厂模式
 * Created by dengzhimin on 2016/10/17.
 */

public interface AbstractFactory {

    DownloadHandler createDownloadHandler();

    UploadHandler createUploadHandler();

    QueryHandler createQueryHandler();

    OtherHandler createOtherHandler();
}
