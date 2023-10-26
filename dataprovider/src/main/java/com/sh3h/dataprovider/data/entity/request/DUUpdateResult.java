package com.sh3h.dataprovider.data.entity.request;

import java.util.List;

public class DUUpdateResult extends DUResponse {
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
        Data;

        public static ItemType index2ItemType(int index) {
            switch (index) {
                case 0:
                    return App;
                case 1:
                default:
                    return Data;
            }
        }
    }

    public static class Item {
        /**
         * 更新类别
         */
        private ItemType itemType;
        /**
         * 是否可更新
         */
        private boolean enable;
        /**
         * 最新版本
         */
        private String version;
        /**
         * 版本描述
         */
        private String desc;
        /**
         * 下载地址
         */
        private String url;

        public Item() {
            itemType = ItemType.App;
            enable = false;
            version = null;
            desc = null;
            url = null;
        }

        public Item(int itemType,
                    boolean enable,
                    String version,
                    String desc,
                    String url) {
            this.itemType = ItemType.index2ItemType(itemType);
            this.enable = enable;
            this.version = version;
            this.desc = desc;
            this.url = url;
        }

        public ItemType getItemType() {
            return itemType;
        }

        public void setItemType(ItemType itemType) {
            this.itemType = itemType;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }


    }

    private List<Item> itemList;
    private DUUpdateInfo duUpdateInfo;

    public DUUpdateResult() {
        itemList = null;
        duUpdateInfo = null;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public DUUpdateInfo getDuUpdateInfo() {
        return duUpdateInfo;
    }

    public void setDuUpdateInfo(DUUpdateInfo duUpdateInfo) {
        this.duUpdateInfo = duUpdateInfo;
    }
}
