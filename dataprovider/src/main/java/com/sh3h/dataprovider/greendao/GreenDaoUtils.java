package com.sh3h.dataprovider.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.blankj.utilcode.util.LogUtils;
import com.sh3h.dataprovider.data.DataManager;
import com.sh3h.dataprovider.greendaoDao.DaoMaster;
import com.sh3h.dataprovider.greendaoDao.DaoSession;

import org.greenrobot.greendao.async.AsyncSession;


/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/3/6 11:43
 */
public class GreenDaoUtils {


    private static DaoSession sDaoSession;
    private static AsyncSession sAsyncSession;
    static DataManager dataManager;

    public static AsyncSession getAsyncSession(Context context) {
        if (sDaoSession == null) {
            sDaoSession = getDaoSession(context);
        }
        sAsyncSession = sDaoSession.startAsyncSession();
        return sAsyncSession;
    }

    public static DaoSession getDaoSession(Context context) {
        if (sDaoSession == null) {
            LogUtils.e("testFilePath", "sDaoSssion-----------");
//            DaoMaster.DevOpenHelper helper = new
//                    DaoMaster.DevOpenHelper(context.getApplicationContext(), "main.db");
//            SQLiteDatabase database = helper.getWritableDatabase();
//            DaoMaster daoMaster = new DaoMaster(database);
//            sDaoSession = daoMaster.newSession();
            DatabaseContext databaseContext = new DatabaseContext(context, dataManager);
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(databaseContext, "hotline.db", null);

            SQLiteDatabase db = helper.getWritableDatabase();

            DaoMaster daoMaster = new DaoMaster(db);

            sDaoSession = daoMaster.newSession();
        }
        return sDaoSession;
    }


    public static void initGreendao(Context context, DataManager dataManager) {
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context.getApplicationContext(), "main.db");
//        SQLiteDatabase database = helper.getWritableDatabase();
//        DaoMaster daoMaster = new DaoMaster(database);
        GreenDaoUtils.dataManager = dataManager;
        DatabaseContext databaseContext = new DatabaseContext(context, dataManager);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(databaseContext, "hotline.db", null);

        SQLiteDatabase db = helper.getWritableDatabase();

        DaoMaster daoMaster = new DaoMaster(db);

        sDaoSession = daoMaster.newSession();
    }
}
