package com.sh3h.dataprovider.data.remote;


import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.google.common.collect.Tables;
import com.sh3h.dataprovider.data.entity.DUFile;
import com.sh3h.dataprovider.data.entity.DUUpdateXmlFile;
import com.sh3h.dataprovider.data.entity.request.DUFileResult;
import com.sh3h.dataprovider.data.local.config.ConfigHelper;
import com.sh3h.dataprovider.data.local.db.DbHelper;
import com.sh3h.dataprovider.data.local.xml.XmlHelper;
import com.sh3h.dataprovider.event.DataBusEvent;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.sh3h.dataprovider.util.FileUtil;
import com.sh3h.dataprovider.util.ZipUtil;
import com.sh3h.mobileutil.util.LogUtil;
import com.sh3h.mobileutil.util.TextUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Parser {
    private static final String TAG = "Parser";
    private static final String APK_NAME = "hotline";
    private static final String APK_SUFFIX = ".apk";

    private final DbHelper mDbHelper;
    private final ConfigHelper mConfigHelper;
    private final EventPosterHelper mEventPosterHelper;
    private final XmlHelper mXmlHelper;

    @Inject
    public Parser(DbHelper dbHelper, ConfigHelper configHelper,
                  EventPosterHelper eventPosterHelper, XmlHelper xmlHelper) {
        mDbHelper = dbHelper;
        mConfigHelper = configHelper;
        mEventPosterHelper = eventPosterHelper;
        mXmlHelper = xmlHelper;
    }

    public void parseData(DUFileResult duFileResult) {
        LogUtil.i(TAG, "---xml2db---");
        postEvent(DataBusEvent.ParserResult.OperationType.DATA_START, true, "start");

        if ((duFileResult == null)
                || (TextUtil.isNullOrEmpty(duFileResult.getName()))
                || (TextUtil.isNullOrEmpty(duFileResult.getPath()))
                || (mDbHelper == null)
                || (mConfigHelper == null)
                || (mEventPosterHelper == null)) {
            LogUtil.i(TAG, "---xml2db null---");
            postEvent(DataBusEvent.ParserResult.OperationType.DATA_END, false, "xml2db null");
            return;
        }

        String destFolder = getDestFolder(duFileResult.getName());
        if (TextUtil.isNullOrEmpty(destFolder)) {
            LogUtil.i(TAG, "---destination folder is error---");
            postEvent(DataBusEvent.ParserResult.OperationType.DATA_END, false,
                    "destination folder is error");
            return;
        }

        if (!unzipFile(duFileResult.getPath(), destFolder)) {
            LogUtil.i(TAG, "---failure to unzip data.zip---");
            postEvent(DataBusEvent.ParserResult.OperationType.DATA_END, false,
                    "failure to unzip data.zip");
            return;
        }

        removeConfigFiles(destFolder);

        File[] files = getXmlFiles(destFolder);
        if (files == null) {
            LogUtil.i(TAG, "---getXmlFiles is null---");
            postEvent(DataBusEvent.ParserResult.OperationType.DATA_END, false,
                    "getXmlFiles is null");
            return;
        } else if (files.length == 0) {
            LogUtil.i(TAG, "---getXmlFiles length is 0---");
            postEvent(DataBusEvent.ParserResult.OperationType.DATA_END, false,
                    "getXmlFiles length is 0");
            return;
        }

        deleteFiles(new File(duFileResult.getPath()), new File(destFolder));
        boolean b = mConfigHelper.saveDataVersion(duFileResult.getVersion());
        postEvent(DataBusEvent.ParserResult.OperationType.DATA_END, b, "end");
    }

    /**
     * parser the file
     *
     * @param duFile
     * @return
     */
    public boolean parseFile(DUFile duFile) {
        LogUtil.i(TAG, "---parseFile---");

        try {
            File file = new File(duFile.getPath());
            if (!file.exists()) {
                return false;
            }

            File apksFolderPath = mConfigHelper.getApksFolderPath();
            if (!apksFolderPath.exists()) {
                apksFolderPath.mkdirs();
            }

            String outPathString = apksFolderPath.getPath();
            if (duFile.getFileType() == DUFile.FileType.DATA) {
                File updateFolderPath = mConfigHelper.getUpdateFolderPathByUser();
                if (!updateFolderPath.exists()) {
                    updateFolderPath.mkdirs();
                }

                outPathString = updateFolderPath.getPath();
            }
            ZipUtil.UnZipFolder(file.getPath(), outPathString);
            file.delete();

            switch (duFile.getFileType()) {
                case APK:
                    return save2UpdateXml(duFile);
                case DATA:
                    return save2UpdateXml(duFile);
                default:
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i(TAG, "---parseFile---" + e.getMessage());
            return false;
        }
    }

    private void updateAttributeValue(Document doc, String account) {
        NodeList configs = doc.getElementsByTagName("CBJConfig");
        if (configs == null || configs.getLength() != 1) {
            LogUtil.i(TAG, "---updateAttributeValue configs is error---");
            return;
        }

        Element config = (Element) configs.item(0);
        NodeList pdas = config.getElementsByTagName("PDA");
        if (pdas == null || pdas.getLength() != 1) {
            LogUtil.i(TAG, "---updateAttributeValue pdas is error---");
            return;
        }

        Element pda = (Element) pdas.item(0);
        pda.setAttribute("ID", account);
    }

    public void unzipFile(DUFileResult duFileResult) {
        LogUtil.i(TAG, "---unzipFile---");
        postEvent(DataBusEvent.ParserResult.OperationType.APK_START, true, "start");

        if ((duFileResult == null)
                || (TextUtil.isNullOrEmpty(duFileResult.getName()))
                || (TextUtil.isNullOrEmpty(duFileResult.getPath()))
                || (mDbHelper == null)
                || (mConfigHelper == null)
                || (mEventPosterHelper == null)) {
            LogUtil.i(TAG, "---unzipFile null---");
            postEvent(DataBusEvent.ParserResult.OperationType.APK_END, false, "xml2db null");
            return;
        }

        String destFolder = getDestFolder(duFileResult.getName());
        if (TextUtil.isNullOrEmpty(destFolder)) {
            LogUtil.i(TAG, "---destination folder is error---");
            postEvent(DataBusEvent.ParserResult.OperationType.APK_END, false,
                    "destination folder is error");
            return;
        }

        if (!unzipFile(duFileResult.getPath(), destFolder)) {
            LogUtil.i(TAG, "---failure to unzip data.zip---");
            postEvent(DataBusEvent.ParserResult.OperationType.APK_END, false,
                    "failure to unzip data.zip");
            return;
        }

        String path = getApkPath(destFolder);
        LogUtils.e(TAG,"path="+path);
        LogUtils.e(TAG,"destFolder="+destFolder);
        if (TextUtil.isNullOrEmpty(path)) {
            postEvent(DataBusEvent.ParserResult.OperationType.APK_END, false, "");
        } else {
            postEvent(DataBusEvent.ParserResult.OperationType.APK_END, true, path);
        }
    }

    private boolean unzipFile(String zipFile, String destFolder) {
        try {
            ZipUtil.UnZipFolder(zipFile, destFolder);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void removeConfigFiles(String srcFolder) {
        File[] files = getConfigFiles(srcFolder);
        if ((files == null) || (files.length <= 0)) {
            return;
        }

        File destFolder = mConfigHelper.getConfigFolderPath();
        boolean b = false;
        for (File file : files) {
            b = file.renameTo(new File(destFolder, file.getName()));
        }

        mConfigHelper.getOtherConfig().setRead(false);
        mConfigHelper.getSystemConfig().setRead(false);
    }

    private File[] getConfigFiles(String folder) {
        File[] childFiles = new File(folder).listFiles();
        if (childFiles.length == 0) {
            return null;
        }
        File path = new File(childFiles[0].getPath(), "config");
        return path.listFiles();
    }

    private File[] getXmlFiles(String destFolder) {
        File[] childFiles = new File(destFolder).listFiles();
        if (childFiles.length == 0) {
            return null;
        }
        File path = new File(childFiles[0].getPath(), "data");
        return path.listFiles();
    }

    private String getApkPath(String destFolder) {
        Log.e("filename---", "destFolder=" + destFolder);
        File path = new File(destFolder);
        File[] files = path.listFiles();
        if ((files == null) || (files.length <= 0)) {
            return null;
        }

        for (File file : files) {
            String name = file.getName().toLowerCase();
            if ((name.indexOf(APK_NAME) == 0)
                    && (name.lastIndexOf(APK_SUFFIX) > 0)) {
                return file.getPath();
            }
        }

        return null;
    }

    // update/data_v10
    private String getDestFolder(String name) {
        if (TextUtil.isNullOrEmpty(name)) {
            return null;
        }

        File path = mConfigHelper.getUpdateFolderPath();
        if (!path.exists()) {
            path.mkdir();
        }

        name = name.substring(0, name.lastIndexOf(".zip"));
        if (TextUtil.isNullOrEmpty(name)) {
            return null;
        }

        path = new File(path, name);
        if (!path.exists()) {
            path.mkdir();
        }

        return path.getPath();
    }

    /**
     * @param duFile
     * @return
     */
    private boolean save2UpdateXml(DUFile duFile) {
        LogUtil.i(TAG, "---save2UpdateXml---");

        boolean found = false;
        DUUpdateXmlFile duUpdateXmlFileClone = mXmlHelper.getDuUpdateXmlFileClone();
        if ((duUpdateXmlFileClone != null) && (duUpdateXmlFileClone.getApplicationList() != null)) {
            List<DUUpdateXmlFile.Application> applicationList = duUpdateXmlFileClone.getApplicationList();
            for (DUUpdateXmlFile.Application application : applicationList) {
                if (application.getAppId().equals(duFile.getAppId())) {
                    switch (duFile.getFileType()) {
                        case APK:
                            if (application.getVersionCode() < duFile.getVersionCode()) {
                                application.setVersionCode(duFile.getVersionCode());
                                application.setVersionName(duFile.getVersionName());
                            }
                            break;
                        case DATA:
                            if (application.getData() != null) {
                                if (application.getData().getVersion() < duFile.getVersionCode()) {
                                    application.getData().setVersion(duFile.getVersionCode());
                                }
                            } else {
                                DUUpdateXmlFile.Data data = new DUUpdateXmlFile.Data(1,
                                        "Data", duFile.getVersionCode());
                                application.setData(data);
                            }
                            break;
                        default:
                            break;
                    }

                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            if (duFile.getFileType() == DUFile.FileType.APK) {
                if (duUpdateXmlFileClone == null) {
                    duUpdateXmlFileClone = new DUUpdateXmlFile();
                }

                int id = 1;
                List<DUUpdateXmlFile.Application> applicationList = duUpdateXmlFileClone.getApplicationList();
                if (applicationList == null) {
                    applicationList = new ArrayList<>();
                    duUpdateXmlFileClone.setApplicationList(applicationList);
                } else {
                    if (applicationList.size() > 0) {
                        id = applicationList.get(applicationList.size() - 1).getId() + 1;
                    }
                }

                applicationList.add(new DUUpdateXmlFile.Application(id, duFile.getAppName(),
                        duFile.getAppId(), duFile.getPackageName(), duFile.getVersionCode(),
                        duFile.getVersionName()));
            } else {
                return false;
            }
        }

        mXmlHelper.setDuUpdateXmlFile(duUpdateXmlFileClone);
        return mXmlHelper.saveDuUpdateXmlFile();
    }

    /**
     * @param zipFile
     * @param destFolder
     */
    private void deleteFiles(File zipFile, File destFolder) {
        try {
            FileUtil.deleteFile(zipFile);
            FileUtil.deleteFile(destFolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postEvent(DataBusEvent.ParserResult.OperationType operationType,
                           boolean isSuccess, String message) {
        if ((mEventPosterHelper != null)
                && (!TextUtil.isNullOrEmpty(message))) {
            mEventPosterHelper.postEventSafely(new DataBusEvent.ParserResult(
                    operationType, isSuccess, message));
        }
    }
}
