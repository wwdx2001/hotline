package com.sh3h.serverprovider.rpc.service;

import org.json.JSONObject;

import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.serverprovider.entity.ClientInfoEntity;
import com.sh3h.serverprovider.entity.UpdateInfoEntity;

/**
 * 版本更新服务
 */
public class VersionApiService extends BaseApiService {

    private static final String URL = "Version.ashx";

    // private static final String HAS_NEW_CONFIG = "";
    // private static final String METHOD_HAS_NEW_VERSION = "";
    private static String METHOD_HASNEWUPDATE = "hasNewUpdate";

    @Override
    public String getHandlerURL() {
        return URL;
    }

    /**
     * 是否存在新的更新
     *
     * @param info
     * @return
     */
    public UpdateInfoEntity hasNewUpdate(ClientInfoEntity info) throws ApiException {

        JSONObject resp = null;
        try {
            resp = this.callJSONObject(VersionApiService.METHOD_HASNEWUPDATE,
                    new Object[]{ info.toJSON() });
        } catch (ApiException e) {
            LogUtil.e("API", "检测更新方法调用失败", e);
            throw e;
        }

        UpdateInfoEntity result = null;
        if (resp != null) {
            result = UpdateInfoEntity.fromJSON(resp);
        }

        return result;
    }

}

