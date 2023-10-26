package com.sh3h.dataprovider.greendao;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.sh3h.dataprovider.BaseApplication;
import com.sh3h.dataprovider.data.DataManager;

import java.io.File;
import java.io.IOException;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/4/3 11:22
 */
public class DatabaseContext extends ContextWrapper {
    DataManager dataManager;

    public DatabaseContext(Context base, DataManager dataManager) {
        super(base);
        this.dataManager = dataManager;

    }

    /**
     * 获得数据库路径，如果不存在，则自动创建
     */

    @Override

    public File getDatabasePath(String name) {

//判断是否存在sd卡

        boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());

        if (!sdExist) {//如果不存在,

            Log.e("SD卡管理：", "SD卡不存在，请加载SD卡");

            return null;

        } else {//如果存在

//获取sd卡路径

            File rootPath = android.os.Environment.getExternalStorageDirectory();


            String sh3h = "sh3h/hotline";//数据库所在目录
            if (dataManager != null) {
                sh3h = "sh3h/hotline/" + dataManager.getAccount();
            }
            LogUtils.e("testFilePath", BaseApplication.getInstance());

            //判断目录是否存在，不存在则创建该目录

            File dirFile = new File(rootPath, sh3h);

            if (!dirFile.exists())
                dirFile.mkdirs();

//数据库文件是否创建成功

            boolean isFileCreateSuccess = false;

//判断文件是否存在，不存在则创建该文件

            File dbFile = new File(dirFile, name);

            if (!dbFile.exists()) {
                try {

                    isFileCreateSuccess = dbFile.createNewFile();//创建文件

                } catch (IOException e) {

                    e.printStackTrace();

                }

            } else

                isFileCreateSuccess = true;

//返回数据库文件对象

            if (isFileCreateSuccess)

                return dbFile;

            else

                return null;

        }

    }

    @Override

    public SQLiteDatabase openOrCreateDatabase(String name, int mode,

                                               SQLiteDatabase.CursorFactory factory) {

        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);

        return result;

    }

    @Override

    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory,

                                               DatabaseErrorHandler errorHandler) {

        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);

        return result;

    }

}