package com.sh3h.serverprovider.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 更新信息
 */
public class UpdateInfoEntity {

    /**
     * 更新项集合
     */
    private List<Item> Items;

    public List<Item> getItems() {
        return Items;
    }

    public void setItems(List<Item> itmes) {
        Items = itmes;
    }

    public UpdateInfoEntity() {
        this.Items = new ArrayList<Item>();
    }

    /**
     * 转换JSONObject对象为UpdateInfo
     *
     * @param jsonObject
     * @return
     */
    public static UpdateInfoEntity fromJSON(JSONObject jsonObject) {

        UpdateInfoEntity result = new UpdateInfoEntity();
        JSONArray items = jsonObject.optJSONArray("items");
        int len = items.length();

        for (int i = 0; i < len; i++) {
            result.getItems().add(new UpdateInfoEntity.Item(items.optJSONObject(i)));
        }

        return result;
    }

    /**
     * 具体更新项
     */
    public static class Item {

        /**
         * 更新类别
         */
        private ItemType _type;
        /**
         * 是否可更新
         */
        private boolean _enable;
        /**
         * 最新版本
         */
        private String _version;
        /**
         * 版本描述
         */
        private String _desc;
        /**
         * 下载地址
         */
        private String _url;

        public Item() { }

        public Item(JSONObject object) {
            this.setVersion(object.optString("version"));
            this.setUrl(object.optString("url"));
            this.setDesc(object.optString("desc"));
            this.setEnable(object.optBoolean("enable"));
            this.setType(ItemType.valueOf(object.optString("type")));
        }

        public ItemType getType() {
            return _type;
        }

        public void setType(ItemType type) {
            _type = type;
        }

        public boolean isEnable() {
            return _enable;
        }

        public void setEnable(boolean enable) {
            _enable = enable;
        }

        public String getVersion() {
            return _version;
        }

        public void setVersion(String version) {
            _version = version;
        }

        public String getDesc() {
            return _desc;
        }

        public void setDesc(String desc) {
            _desc = desc;
        }

        public String getUrl() {
            return _url;
        }

        public void setUrl(String url) {
            _url = url;
        }
    }

    /**
     * 更新项类型枚举
     */
    public enum ItemType {
        /**
         * 应用程序
         */
        App,
        /**
         * 数据包，配置
         */
        Data
    }
}

