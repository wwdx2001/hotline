package com.sh3h.localprovider;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.sh3h.localprovider.greendaoDao.DaoMaster;
import com.sh3h.localprovider.greendaoDao.DaoSession;
import com.sh3h.localprovider.greendaoDao.HistoryTaskDao;
import com.sh3h.localprovider.greendaoDao.MultiMediaDao;
import com.sh3h.localprovider.greendaoDao.TaskDao;
import com.sh3h.localprovider.greendaoDao.WordDao;
import com.sh3h.localprovider.greendaoEntity.HistoryTask;
import com.sh3h.localprovider.greendaoEntity.MultiMedia;
import com.sh3h.localprovider.greendaoEntity.Task;
import com.sh3h.localprovider.greendaoEntity.Word;
import com.sh3h.mobileutil.util.TextUtil;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liurui on 2015/8/3.
 */
public class DataProviderImpl implements IDataProvider {
    // task type
    private static final int TASK_TYPE_DOWNLOAD = 0; // 下载
    private static final int DELETE_COUNT_MAX = 800;//最大限度

    // task state
    private static final int TASK_STATE_RECEIVED = 0;//接收状态
    private static final int TASK_STATE_DELAY = 2;//延期
    private static final int TASK_STATE_REJECT = 3; // 退单
    private static final int TASK_STATE_HANDLER = 4; // 处理（销单）
    private static final int TASK_STATE_FINISHED = 6;//销单完成

    //upload flag
    private static final int NO_UPLOAD = 0;//未上传

    private static final long ONE_DAY = 86400000L;//一天时间

    //站点Station
    private static final String WORD_STATION = "Station";

    private Context context;
    private final static String MY_PWD = "sh3h";
    private Database mDatabase;
    private DaoMaster.DevOpenHelper openHelper;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;


    public DataProviderImpl() {
        mDatabase = null;
        context = null;
    }

    @Override
    public synchronized boolean init(String path, Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, path, null);
        mDatabase = openHelper.getWritableDb();
        if (mDatabase == null) {
            return false;
        }
        updateMultiMediaTable();
        updateTaskTable();
        updateTaskIssueOrigin();
//        mDatabase = openHelper.getEncryptedWritableDb(MY_PWD);
        mDaoMaster = new DaoMaster(mDatabase);
        mDaoSession = mDaoMaster.newSession();
        return true;
    }

    /**
     * 更新Task表的反映来源
     */
    private void updateTaskIssueOrigin() {
        if (!checkColumnExist(mDatabase, TaskDao.TABLENAME, "ISSUE__ORIGIN")) {
            try {
                mDatabase.beginTransaction();
                String updateTask = "ALTER TABLE " + TaskDao.TABLENAME + " ADD COLUMN ISSUE__ORIGIN TEXT;";
                mDatabase.execSQL(updateTask);
                mDatabase.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDatabase.endTransaction();
            }
        }
    }

    /**
     * 更新Task表结构
     */
    private void updateTaskTable() {
        if (!checkColumnExist(mDatabase, TaskDao.TABLENAME, "DISPATCH__TIME")) {
            try {
                mDatabase.beginTransaction();
                String updateTask = "ALTER TABLE " + TaskDao.TABLENAME + " ADD COLUMN DISPATCH__TIME INTEGER;";
                mDatabase.execSQL(updateTask);
                mDatabase.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDatabase.endTransaction();
            }
        }
    }

    /**
     * 更新多媒体表结构
     */
    private void updateMultiMediaTable() {
        if (!checkColumnExist(mDatabase, MultiMediaDao.TABLENAME, "FILE__TIME")) {
            try {
                mDatabase.beginTransaction();
                String updateMultiMedia = "ALTER TABLE " + MultiMediaDao.TABLENAME + " ADD COLUMN FILE__TIME INTEGER;";
                mDatabase.execSQL(updateMultiMedia);
                mDatabase.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDatabase.endTransaction();
            }
        }
    }

    /**
     * 方法1：检查某表列是否存在
     *
     * @param db
     * @param tableName  表名
     * @param columnName 列名
     * @return
     */
    private static boolean checkColumnExist(Database db, String tableName
            , String columnName) {
        boolean result = false;
        Cursor cursor = null;
        try {
            //查询一行
            cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0"
                    , null);
            result = cursor != null && cursor.getColumnIndex(columnName) != -1;
        } catch (Exception e) {
            Log.e("", "checkColumnExists..." + e.getMessage());
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    @Override
    public synchronized boolean saveMyTasks(int userId, List<Task> taskList) {
        if (taskList == null) {
            return false;
        }

        TaskDao taskDao = mDaoSession.getTaskDao();
//        //本地的任务（下载的且状态不为处理或者销单完成的删除）
//        List<Task> localTasks = taskDao.queryBuilder()
//                .where(TaskDao.Properties.TASK_TYPE.eq(0))//下载
//                .where(TaskDao.Properties.TASK_STATE.notEq(4))//处理
//                .where(TaskDao.Properties.TASK_STATE.notEq(6)).list();//销单完成
//        if (localTasks != null) {
//            for (Task task : localTasks) {
//                taskDao.delete(task);
//            }
//        }
        List<String> taskIdList = new ArrayList<String>();
        for (Task task : taskList) {
            if (!TextUtil.isNullOrEmpty(task.getTASK_ID())) {
                taskIdList.add(task.getTASK_ID());
            }
        }

        if (taskIdList.size() >= 0) {
            List<Task> mayDeleteTaskList =
                    taskDao.queryBuilder().where(TaskDao.Properties.USER_ID.eq(userId),
                            TaskDao.Properties.TASK_ID.notIn(taskIdList)).list();

            List<Task> needDeleteTaskList = new ArrayList<>();
            for (Task task : mayDeleteTaskList) {
                if ((task.getTASK_STATE() != TASK_STATE_REJECT)//退单
                        && (task.getTASK_STATE() != TASK_STATE_HANDLER)) {//处理
                    needDeleteTaskList.add(task);
                }
            }
            if (needDeleteTaskList.size() > 0) {
                taskDao.deleteInTx(needDeleteTaskList);
            }
        }

        if (taskIdList.size() > 0) {
            List<Task> taskListTemp =
                    taskDao.queryBuilder().where(TaskDao.Properties.USER_ID.eq(userId),
                            TaskDao.Properties.TASK_ID.in(taskIdList)).list();


            if ((taskListTemp != null) && (taskListTemp.size() > 0)) {
                List<Task> existingTaskList = new ArrayList<>();
                List<Task> localTaskList = new ArrayList<>();
                for (Task task1 : taskList) {
                    for (Task task2 : taskListTemp) {
                        if (!TextUtil.isNullOrEmpty(task1.getTASK_ID())
                                && !TextUtil.isNullOrEmpty(task2.getTASK_ID())
                                && task1.getTASK_ID().equals(task2.getTASK_ID())) {
                            existingTaskList.add(task1);
                            task2.setREPLY_DEADLINE(task1.getREPLY_DEADLINE());
                            task2.setTASK_STATE(task1.getTASK_STATE());
                            localTaskList.add(task2);
                            break;
                        }
                    }
                }

                if (localTaskList.size() > 0) {
                    taskDao.updateInTx(localTaskList);
                }

                for (Task task : existingTaskList) {
                    taskList.remove(task);
                }
            }
        }


        if (taskList.size() > 0) {
            taskDao.insertOrReplaceInTx(taskList);
        }
        return true;
    }

    @Override
    public synchronized boolean deleteMyTasks(int userId, List<Task> taskList) {
        boolean result = false;
        if (taskList == null) {
            result = false;
        } else if (taskList.size() <= 0) {
//            mDaoSession.getTaskDao()
//                    .queryBuilder()
//                    .where(TaskDao.Properties.USER_ID.eq(userId))
//                    .buildDelete()
//                    .executeDeleteWithoutDetachingEntities();
//            mDaoSession.getHistoryTaskDao()
//                    .queryBuilder()
//                    .where(HistoryTaskDao.Properties.USER_ID.eq(userId))
//                    .buildDelete()
//                    .executeDeleteWithoutDetachingEntities();
            result = true;
        } else {
//            List<String> taskIdList = new ArrayList<String>();
//            for (Task task : taskList) {
//                if (!TextUtil.isNullOrEmpty(task.getTASK_ID())) {
//                    taskIdList.add(task.getTASK_ID());
//                }
//            }
//
//            mDaoSession.getTaskDao()
//                    .queryBuilder()
//                    .where(TaskDao.Properties.USER_ID.eq(userId), TaskDao.Properties.TASK_ID.notIn(taskIdList))
//                    .buildDelete()
//                    .executeDeleteWithoutDetachingEntities();
//            mDaoSession.getHistoryTaskDao()
//                    .queryBuilder()
//                    .where(HistoryTaskDao.Properties.USER_ID.eq(userId), HistoryTaskDao.Properties.TASK_ID.notIn(taskIdList))
//                    .buildDelete()
//                    .executeDeleteWithoutDetachingEntities();
            result = true;
        }

        return result;
    }

    @Override
    public synchronized boolean deleteMyTask(int userId) {
        TaskDao taskDao = mDaoSession.getTaskDao();
        List<Task> mayDeleteTaskList =
                taskDao.queryBuilder().where(TaskDao.Properties.USER_ID.eq(userId)).list();

        List<Task> needDeleteTaskList = new ArrayList<>();
        for (Task task : mayDeleteTaskList) {
            if (task.getTASK_STATE() != TASK_STATE_REJECT//退单
                    || task.getTASK_STATE() != TASK_STATE_HANDLER) {//处理
                needDeleteTaskList.add(task);
            }
        }
        taskDao.deleteInTx(needDeleteTaskList);
        return true;
    }

    @Override
    public synchronized boolean updateMyTask(Task task) {
        if (task == null) {
            return false;
        }

        mDaoSession.getTaskDao().update(task);
        return true;
    }

    @Override
    public synchronized boolean updateMyTaskList(List<Task> taskList) {
        if (taskList == null) {
            return false;
        } else if (taskList.size() <= 0) {
            return true;
        }

        mDaoSession.getTaskDao().updateInTx(taskList);
        return true;
    }

    @Override
    public synchronized Task queryTaskByTaskId(String taskId) {
        return mDaoSession.getTaskDao().queryBuilder().where(TaskDao.Properties.TASK_ID.eq(taskId)).list().get(0);
    }

    @Override
    public synchronized boolean updateMyTask(String taskId, int taskState) {
        if (taskId == null) {
            return false;
        }

        List<Task> list = mDaoSession.getTaskDao().queryBuilder()
                .where(TaskDao.Properties.TASK_ID.eq(taskId)).list();
        if ((list == null) || (list.size() != 1)) {
            return false;
        }

        Task task = list.get(0);
        task.setTASK_STATE(taskState);
        mDaoSession.getTaskDao().update(task);
        return true;
    }

    @Override
    public synchronized boolean updateHistoryTask(HistoryTask historyTask) {
        if (historyTask == null) {
            return false;
        }


//        List<HistoryTask> list = mDaoSession.getHistoryTaskDao().queryBuilder()
//                .where(HistoryTaskDao.Properties.TASK_ID.eq(historyTask.getTASK_ID()))
//                .where(HistoryTaskDao.Properties.REPLY_TIME.eq(historyTask.getREPLY_TIME()))
//                .list();
//        HistoryTask updateHistoryTask = list.get(0);
//
//        if (updateHistoryTask.getUPLOAD_FLAG() != 2) {
//            //如果该histroytask的当前上传标志不是“已上传”
//            updateHistoryTask.setUPLOAD_FLAG(historyTask.getUPLOAD_FLAG());
//            mDaoSession.getHistoryTaskDao().update(updateHistoryTask);
//        }

        mDaoSession.getHistoryTaskDao().update(historyTask);
        return true;
    }

    @Override
    public synchronized boolean updateHistoryTaskList(List<HistoryTask> historyTaskList) {
        if (historyTaskList == null) {
            return false;
        } else if (historyTaskList.size() <= 0) {
            return true;
        }

        mDaoSession.getHistoryTaskDao().updateInTx(historyTaskList);
        return true;
    }

    @Override
    public synchronized List<Task> loadTasks(int userId, int offset, int size, int taskType, boolean isFromMyTask) {
        if ((offset < 0) && (size <= 0)) {
            if (isFromMyTask) {
                return mDaoSession.getTaskDao().queryBuilder()
                        .where(TaskDao.Properties.USER_ID.eq(userId),
                                TaskDao.Properties.TASK_TYPE.eq(taskType),//工单类型，自开单还是我的工单
                                TaskDao.Properties.TASK_STATE.notEq(TASK_STATE_REJECT), //"退单状态"
                                TaskDao.Properties.TASK_STATE.notEq(TASK_STATE_HANDLER))//任务状态不为“4”,处理
                        .orderDesc(TaskDao.Properties.ISSUE_TIME)//按照发生时间降序排列
                        .list();
            } else {
                return mDaoSession.getTaskDao().queryBuilder().where(TaskDao.Properties.USER_ID.eq(userId),
                        TaskDao.Properties.TASK_TYPE.eq(taskType)).list();
            }
        } else {
            return mDaoSession.getTaskDao().queryBuilder()
                    .where(TaskDao.Properties.USER_ID.eq(userId),
                            TaskDao.Properties.TASK_TYPE.eq(taskType),//工单类型，自开单还是我的工单
                            TaskDao.Properties.TASK_STATE.notEq(TASK_STATE_REJECT), //"退单状态"
                            TaskDao.Properties.TASK_STATE.notEq(TASK_STATE_HANDLER))//任务状态不为“4”,处理
                    .orderDesc(TaskDao.Properties.ISSUE_TIME)//按照发生时间降序排列
                    .offset(offset).limit(size).list();
        }
    }

    @Override
    public synchronized List<HistoryTask> loadHistoryTasks(int userId, int offset, int size) {
        List<HistoryTask> historyTaskList = mDaoSession.getHistoryTaskDao().queryBuilder()
                .where(HistoryTaskDao.Properties.USER_ID.eq(userId),
                        HistoryTaskDao.Properties.TASK_TYPE.eq(TASK_TYPE_DOWNLOAD))//类型为“下载”
                .whereOr(HistoryTaskDao.Properties.TASK_STATE.eq(TASK_STATE_DELAY),//延期
                        HistoryTaskDao.Properties.TASK_STATE.eq(TASK_STATE_REJECT),//退单
                        HistoryTaskDao.Properties.TASK_STATE.eq(TASK_STATE_HANDLER)//完成
                        , HistoryTaskDao.Properties.TASK_STATE.eq(TASK_STATE_FINISHED))//销单成功
                .orderDesc(HistoryTaskDao.Properties.REPLY_TIME)//按照回复时间降序
                .offset(offset).limit(size).list();
        return historyTaskList;
    }

    @Override
    public List<HistoryTask> getCreateSelfOrderHistory(int userId, int offset, int size) {
        return mDaoSession.getHistoryTaskDao().queryBuilder()
                .where(HistoryTaskDao.Properties.TASK_TYPE.eq(1),
                        HistoryTaskDao.Properties.USER_ID.eq(userId),
                        HistoryTaskDao.Properties.TASK_STATE.eq(9))
                .orderDesc(HistoryTaskDao.Properties.REPLY_TIME)
                .offset(offset).limit(size).list();
    }

    @Override
    public synchronized boolean saveHistoryTask(HistoryTask historyTask) {
        return updateMyTask(historyTask.getTASK_ID(), historyTask.getTASK_STATE())
                && mDaoSession.getHistoryTaskDao().insertOrReplace(historyTask) != -1;
    }

    public synchronized boolean saveCreateSelfOrder(Task task) {
        return mDaoSession.getTaskDao().insertOrReplace(task) != -1;
    }

    @Override
    public synchronized boolean saveCreateSelfOrderAndHistory(Task task, HistoryTask createTask) {
        mDaoSession.getDatabase().beginTransaction();

        try {
            //同时插入成功则设置事务成功
            //Task表中保存一条自开单记录，HistoryTask表中插入自开单创建记录和自开单上传记录
            if (saveCreateSelfOrder(task) && saveHistoryTask(createTask)) {
                mDaoSession.getDatabase().setTransactionSuccessful();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mDaoSession.getDatabase().endTransaction();
        }
        return false;
    }

    @Override
    public synchronized boolean saveWords(List<Word> words) {
        if (words == null || words.isEmpty()) {
            return false;
        }

        mDaoSession.getWordDao().insertOrReplaceInTx(words);
        return true;
    }

    @Override
    public synchronized void clearWords() {
        mDaoSession.getWordDao().deleteAll();
    }

    @Override
    public synchronized List<Task> getTaskList(int userId, List<Integer> taskTypeList, int offset, int limit) {
        if ((taskTypeList == null) || (taskTypeList.size() <= 0)) {
            if ((offset >= 0) && (offset < limit)) {
                return mDaoSession.getTaskDao()
                        .queryBuilder()
                        .where(TaskDao.Properties.USER_ID.eq(userId))
                        .orderDesc(TaskDao.Properties.ID)
                        .offset(offset)
                        .limit(limit)
                        .list();
            } else {
                return mDaoSession.getTaskDao()
                        .queryBuilder()
                        .where(TaskDao.Properties.USER_ID.eq(userId))
                        .orderDesc(TaskDao.Properties.ID)
                        .list();
            }
        } else {
            if ((offset >= 0) && (limit > 0)) {
                return mDaoSession.getTaskDao()
                        .queryBuilder()
                        .where(TaskDao.Properties.USER_ID.eq(userId), TaskDao.Properties.TASK_TYPE.in(taskTypeList))
                        .orderDesc(TaskDao.Properties.ID)
                        .offset(offset)
                        .limit(limit)
                        .list();
            } else {
                return mDaoSession.getTaskDao()
                        .queryBuilder()
                        .where(TaskDao.Properties.USER_ID.eq(userId), TaskDao.Properties.TASK_TYPE.in(taskTypeList))
                        .orderDesc(TaskDao.Properties.ID)
                        .list();
            }
        }
    }

    @Override
    public synchronized boolean saveMedia(MultiMedia multiMedia) {
        if (multiMedia == null) {
            return false;
        }

        if (multiMedia.getID() == -1) {
            long id = 0;
            List<MultiMedia> multiMediaList =
                    mDaoSession.getMultiMediaDao().queryBuilder().orderDesc(MultiMediaDao.Properties.ID).limit(1).list();
            if ((multiMediaList != null) && (multiMediaList.size() == 1)) {
                id = multiMediaList.get(0).getID();
            }

            multiMedia.setID(id + 1);
        }

        mDaoSession.getMultiMediaDao().insertOrReplaceInTx(multiMedia);
        return true;
    }

    @Override
    public synchronized List<MultiMedia> getMediaList(int userId,
                                                      String taskId,
                                                      int taskType,
                                                      int taskState,
                                                      int fileType) {
        if (taskId == null) {
            return null;
        }

        if (fileType == -1) { // all
            if (taskState == -1) { // all states
                return mDaoSession.getMultiMediaDao()
                        .queryBuilder()
                        .where(MultiMediaDao.Properties.USER_ID.eq(userId),
                                MultiMediaDao.Properties.TASK_ID.eq(taskId),
                                MultiMediaDao.Properties.TASK_TYPE.eq(taskType))
                        .list();
            } else {
                return mDaoSession.getMultiMediaDao()
                        .queryBuilder()
                        .where(MultiMediaDao.Properties.USER_ID.eq(userId),
                                MultiMediaDao.Properties.TASK_ID.eq(taskId),
                                MultiMediaDao.Properties.TASK_TYPE.eq(taskType),
                                MultiMediaDao.Properties.TASK_STATE.eq(taskState))
                        .list();
            }
        } else {
            if (taskState == -1) {
                return mDaoSession.getMultiMediaDao()
                        .queryBuilder()
                        .where(MultiMediaDao.Properties.USER_ID.eq(userId),
                                MultiMediaDao.Properties.TASK_ID.eq(taskId),
                                MultiMediaDao.Properties.TASK_TYPE.eq(taskType),
                                MultiMediaDao.Properties.FILE_TYPE.eq(fileType))
                        .list();
            } else {
                return mDaoSession.getMultiMediaDao()
                        .queryBuilder()
                        .where(MultiMediaDao.Properties.USER_ID.eq(userId),
                                MultiMediaDao.Properties.TASK_ID.eq(taskId),
                                MultiMediaDao.Properties.TASK_TYPE.eq(taskType),
                                MultiMediaDao.Properties.TASK_STATE.eq(taskState),
                                MultiMediaDao.Properties.FILE_TYPE.eq(fileType))
                        .list();
            }
        }
    }

    @Override
    public synchronized MultiMedia getMedia(long id) {
        List<MultiMedia> multiMediaList =
                mDaoSession.getMultiMediaDao().queryBuilder().where(MultiMediaDao.Properties.ID.eq(id)).list();
        return (multiMediaList != null) && (multiMediaList.size() == 1) ? multiMediaList.get(0) : null;

    }

    @Override
    public synchronized boolean deleteMedia(MultiMedia multiMedia) {
        if (multiMedia == null) {
            return false;
        }

        mDaoSession.getMultiMediaDao().delete(multiMedia);
        return true;
    }

    @Override
    public synchronized boolean deleteMediaList(List<MultiMedia> multiMedias) {
        if (multiMedias == null) {
            return false;
        } else if (multiMedias.size() <= 0) {
            return true;
        }

        mDaoSession.getMultiMediaDao().deleteInTx(multiMedias);
        return true;
    }

    public synchronized Task getCreateSelfOrderByTaskId(int userId, String taskId) {
        if (taskId == null) {
            return null;
        }

        List<Task> taskList = mDaoSession.getTaskDao().queryBuilder()
                .where(TaskDao.Properties.USER_ID.eq(userId), TaskDao.Properties.TASK_ID.eq(taskId)).list();
        if ((taskList == null) || (taskList.size() != 1)) {
            return null;
        }

        return taskList.get(0);
    }

    @Override
    public synchronized boolean updateCreateSelfOrder(String loaclTaskId, String serverTaskId, int uploadFlag) {
        if (loaclTaskId == null || serverTaskId == null) {
            return false;
        }
        Task task = mDaoSession.getTaskDao().queryBuilder().where(TaskDao.Properties.TASK_ID.eq(loaclTaskId)).unique();
        task.setTASK_ID(serverTaskId);
        task.setUPLOAD_FLAG(uploadFlag);
        mDaoSession.getTaskDao().update(task);
        return true;
    }

    @Override
    public boolean updateCreateSelfOrderAndHistory(String loaclTaskId, String serverTaskId, int uploadFlag) {
        //task状态
        //更新插入的保存记录
        //更新插入的上传记录
        mDaoSession.getDatabase().beginTransaction();
        try {
            //同时插入成功则设置事务成功
            if (updateCreateSelfOrder(loaclTaskId, serverTaskId, uploadFlag) && updateCreateSelfOrderHistory(loaclTaskId, serverTaskId, uploadFlag) &&
                    updateCreateSelfOrderHistory(loaclTaskId, serverTaskId) && updateMultiMedia(loaclTaskId, serverTaskId)) {
                mDaoSession.getDatabase().setTransactionSuccessful();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mDaoSession.getDatabase().endTransaction();
        }
        return false;
    }

    @Override
    public boolean updateCreateSelfOrderHistory(String loaclTaskId, String serverTaskId, int uploadFlag) {
        if (loaclTaskId == null || serverTaskId == null) {
            return false;
        }
        QueryBuilder<HistoryTask> queryBuilder = mDaoSession.getHistoryTaskDao().queryBuilder();
        //更新自开单上传记录
        HistoryTask uploadHistoryTask = queryBuilder.where(HistoryTaskDao.Properties.TASK_ID.eq(loaclTaskId),
                HistoryTaskDao.Properties.TASK_STATE.eq(10)).unique();
        if (uploadHistoryTask == null) {
            return false;
        }
        uploadHistoryTask.setTASK_ID(serverTaskId);
        uploadHistoryTask.setUPLOAD_FLAG(uploadFlag);
        mDaoSession.getHistoryTaskDao().update(uploadHistoryTask);
        return true;
    }

    @Override
    public boolean updateCreateSelfOrderHistory(String loaclTaskId, String serverTaskId) {
        if (loaclTaskId == null || serverTaskId == null) {
            return false;
        }
        //更新自开单创建记录
        QueryBuilder<HistoryTask> queryBuilder = mDaoSession.getHistoryTaskDao().queryBuilder();
        HistoryTask createHistoryTask = queryBuilder.where(HistoryTaskDao.Properties.TASK_ID.eq(loaclTaskId),
                HistoryTaskDao.Properties.TASK_STATE.eq(9)).unique();
        if (createHistoryTask == null) {
            return false;
        }
        createHistoryTask.setTASK_ID(serverTaskId);
        mDaoSession.getHistoryTaskDao().update(createHistoryTask);
        return true;
    }

    @Override
    public boolean updateMultiMedia(String loaclTaskId, String serverTaskId) {
        if (loaclTaskId == null || serverTaskId == null) {
            return false;
        }
        //更新多媒体文件taskId
        QueryBuilder<MultiMedia> queryBuilder = mDaoSession.getMultiMediaDao().queryBuilder();
        List<MultiMedia> medias = queryBuilder.where(MultiMediaDao.Properties.TASK_ID.eq(loaclTaskId)).list();
        if (medias == null || medias.size() == 0) {
            return true;
        }
        for (MultiMedia media : medias) {
            media.setTASK_ID(serverTaskId);
        }
        mDaoSession.getMultiMediaDao().updateInTx(medias);
        return true;
    }

    public boolean updateMultiMediaExtend(String loaclTaskId, String serverTaskId) {
        if (loaclTaskId == null || serverTaskId == null) {
            return false;
        }
        //更新多媒体文件taskId
        QueryBuilder<MultiMedia> queryBuilder = mDaoSession.getMultiMediaDao().queryBuilder();
        List<MultiMedia> medias = queryBuilder.where(MultiMediaDao.Properties.TASK_ID.eq(loaclTaskId)).list();
        if (medias == null || medias.size() == 0) {
            return true;
        }
        for (MultiMedia media : medias) {
            try {
                JSONObject jsonObject;
                if (TextUtil.isNullOrEmpty(media.getEXTEND())) {
                    jsonObject = new JSONObject();
                } else {
                    jsonObject = new JSONObject(media.getEXTEND());
                }
                jsonObject.put("serverTaskId", serverTaskId);
                media.setEXTEND(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mDaoSession.getMultiMediaDao().updateInTx(medias);
        return true;
    }

    @Override
    public boolean updateMultiMedia(String taskId, int taskState, int taskType, int uploadFlag) {
        return false;
    }

    @Override
    public synchronized boolean updateMultiMediaList(List<MultiMedia> multiMediaList) {
        if (multiMediaList == null) {
            return false;
        } else if (multiMediaList.size() <= 0) {
            return true;
        }

        mDaoSession.getMultiMediaDao().updateInTx(multiMediaList);
        return true;
    }

    @Override
    public synchronized void destroy() {
        if (mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }

        if (mDaoSession != null) {
            mDaoSession.clear();
            mDaoSession = null;
        }

        if (openHelper != null) {
            openHelper.close();
            openHelper = null;
        }
    }

    @Override
    public synchronized void clearAllTables() {
        //清空数据
        if (mDaoSession.getTaskDao() != null) {
            mDaoSession.getTaskDao().deleteAll();
        }
        if (mDaoSession.getHistoryTaskDao() != null) {
            mDaoSession.getHistoryTaskDao().deleteAll();
        }
        if (mDaoSession.getMultiMediaDao() != null) {
            mDaoSession.getMultiMediaDao().deleteAll();
        }
        if (mDaoSession.getWordDao() != null) {
            mDaoSession.getWordDao().deleteAll();
        }

    }

    /**
     * 获得其下左右的词语
     *
     * @param str 给定第一个spinner的标题，例如“处理类别”
     * @return
     */
    public synchronized List<Word> getFirstWords(String str) {
        //找出Group=0的Words
        List<Word> list = mDaoSession.getWordDao().queryBuilder()
                .where(WordDao.Properties.GROUP.eq("0")).list();
        String parentId = "";
        for (Word word : list) {
            if (word.getNAME().equals(str)) {
                parentId = word.getVALUE();
            }
        }
        List<Word> resultList = mDaoSession.getWordDao().queryBuilder()
                .where(WordDao.Properties.PARENT_ID.eq(parentId))
                .orderAsc(WordDao.Properties.VALUE).list();
        //找出树形的子词语
        List<Word> treeWordList = new ArrayList<Word>();
        //存放需要删除的一级菜单选项
        List<Word> deleteWord = new ArrayList<Word>();
        for (Word word : resultList) {
            String value = word.getVALUE();
            List<Word> wordtemp = mDaoSession.getWordDao().queryBuilder()
                    .where(WordDao.Properties.PARENT_ID.eq(value)).list();
            if (wordtemp != null && wordtemp.size() != 0) {
                deleteWord.add(word);
                treeWordList.addAll(wordtemp);
            }
        }
        resultList.removeAll(deleteWord);
        resultList.addAll(treeWordList);
        return resultList;
    }

    /**
     * //获得第二，三，四的词语
     *
     * @param str 词语的ValueEX
     * @return
     */
    public synchronized Map<String, List<Word>> getNextWords(String str) {
        Map<String, List<Word>> mapResult = new HashMap<String, List<Word>>();
        String[] strArray = str.split(",");
        if (strArray == null || strArray.length == 0) {
            return mapResult;
        }
        for (int i = 0; i < strArray.length; i++) {
            //value=截取前三位，获得typeName,如：处理内容
            String strThree = strArray[i].substring(0, 3);
            String typeName = mDaoSession.getWordDao().queryBuilder()
                    .where(WordDao.Properties.VALUE.eq(strThree)).list().get(0).getNAME();
            List<Word> wordListTemp = mDaoSession.getWordDao().queryBuilder()
                    .where(WordDao.Properties.PARENT_ID.eq(strArray[i])).list();
            if (wordListTemp == null || wordListTemp.size() == 0) {
                continue;
            }
            //找出树形的子词语
            List<Word> treeWordList = new ArrayList<Word>();
            //存放需要删除的一级菜单选项
            List<Word> deleteWord = new ArrayList<Word>();
            for (Word word : wordListTemp) {
                String value = word.getVALUE();
                List<Word> wordtemp = mDaoSession.getWordDao().queryBuilder()
                        .where(WordDao.Properties.PARENT_ID.eq(value)).list();
                if (wordtemp != null && wordtemp.size() != 0) {
                    deleteWord.add(word);
                    treeWordList.addAll(wordtemp);
                }
            }
            wordListTemp.removeAll(deleteWord);
            wordListTemp.addAll(treeWordList);
            mapResult.put(typeName, wordListTemp);
        }
        return mapResult;
    }

    @Override
    public synchronized List<String> getNoUploadHistoryTask(String taskId) {
        //找出历史记录中同一个taskId所有未上传的数据
        List<String> replyInfo = new ArrayList<String>();
        List<HistoryTask> historyTaskList = mDaoSession.getHistoryTaskDao().queryBuilder()
                .where(HistoryTaskDao.Properties.TASK_TYPE.eq(0))//下载类型为0
                .where(HistoryTaskDao.Properties.TASK_ID.eq(taskId))
                .where(HistoryTaskDao.Properties.UPLOAD_FLAG.eq(0))//上传标志为“未上传”
                .orderAsc(HistoryTaskDao.Properties.REPLY_TIME).list();
        if (historyTaskList == null && historyTaskList.size() == 0) {
            return replyInfo;
        }
        for (HistoryTask historyTask : historyTaskList) {
            replyInfo.add(historyTask.getTASK_REPLY());
        }
        return replyInfo;
    }

    @Override
    public synchronized boolean updateHistoryTaskReply(HistoryTask historyTask) {
        if (historyTask == null) {
            return false;
        }
        HistoryTask historyTaskTemp = mDaoSession.getHistoryTaskDao().queryBuilder()
                .where(HistoryTaskDao.Properties.TASK_ID.eq(historyTask.getTASK_ID()))
                .where(HistoryTaskDao.Properties.TASK_STATE.eq(historyTask.getTASK_STATE()))
                .where(HistoryTaskDao.Properties.UPLOAD_FLAG.eq(0)).list().get(0);
        if (historyTaskTemp == null) {
            return false;
        }
        historyTaskTemp.setREPLY_TIME(historyTask.getREPLY_TIME());
        historyTaskTemp.setTASK_REPLY(historyTask.getTASK_REPLY());
        mDaoSession.getHistoryTaskDao().update(historyTaskTemp);
        return true;
    }


    @Override
    public synchronized boolean updateSomeHistoryTaskFlag(List<HistoryTask> historyTaskList) {
        if (historyTaskList == null || historyTaskList.size() == 0) {
            return false;
        }
        for (HistoryTask temp : historyTaskList) {
            HistoryTask historyTask = mDaoSession.getHistoryTaskDao().queryBuilder()
                    .where(HistoryTaskDao.Properties.UPLOAD_FLAG.eq(0),
                            HistoryTaskDao.Properties.TASK_ID.eq(temp.getTASK_ID()),
                            HistoryTaskDao.Properties.TASK_STATE.eq(temp.getTASK_STATE()))
                    .list().get(0);
            historyTask.setUPLOAD_FLAG(temp.getUPLOAD_FLAG());
            mDaoSession.getHistoryTaskDao().update(historyTask);
        }
        return true;
    }

    @Override
    public synchronized HistoryTask getHistoryTask(int userId, String taskId, int taskType, int taskState, long replyTime) {
        if (TextUtil.isNullOrEmpty(taskId)) {
            return null;
        }

        List<HistoryTask> historyTaskList = mDaoSession.getHistoryTaskDao().queryBuilder()
                .where(HistoryTaskDao.Properties.USER_ID.eq(userId),
                        HistoryTaskDao.Properties.TASK_ID.eq(taskId),
                        HistoryTaskDao.Properties.TASK_TYPE.eq(taskType),
                        HistoryTaskDao.Properties.TASK_STATE.eq(taskState),
                        HistoryTaskDao.Properties.REPLY_TIME.eq(replyTime))
                .list();
        if ((historyTaskList != null) && (historyTaskList.size() == 1)) {
            return historyTaskList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public synchronized List<Task> queryMyTask(int userId, String query) {
        if (TextUtil.isNullOrEmpty(query)) {
            return null;
        }

        return mDaoSession.getTaskDao().queryBuilder()
                .where(TaskDao.Properties.USER_ID.eq(userId))
                .whereOr(TaskDao.Properties.TASK_ID.like("%" + query + "%"),//任务编号
                        TaskDao.Properties.ISSUE_NAME.like("%" + query + "%"),//户名
                        TaskDao.Properties.ISSUE_ADDRESS.like("%" + query + "%"),//地址
                        TaskDao.Properties.MOBILE.like("%" + query + "%"),//手机号码
                        TaskDao.Properties.TELEPHONE.like("%" + query + "%")).list();
    }

    /**
     * 更新历史工单
     *
     * @param historyTask
     * @return
     */
    @Override
    public synchronized boolean updateHistoryTask(Long id, HistoryTask historyTask) {
        if (historyTask == null) {
            return false;
        }
        HistoryTask temp = mDaoSession.getHistoryTaskDao().load(id);
        if (temp == null) {
            return false;
        }
        temp.setREPLY_TIME(historyTask.getREPLY_TIME());
        if (!TextUtil.isNullOrEmpty(historyTask.getTASK_REPLY())) {
            temp.setTASK_REPLY(historyTask.getTASK_REPLY());
        }
        mDaoSession.getHistoryTaskDao().update(temp);
        return true;
    }

    @Override
    public synchronized List<HistoryTask> getHistoryTasks(int userId, String taskId, int uploadFlag) {
        if (TextUtils.isEmpty(taskId)) {
            return null;
        }

        return mDaoSession.getHistoryTaskDao().queryBuilder()
                .where(HistoryTaskDao.Properties.USER_ID.eq(userId),
                        HistoryTaskDao.Properties.TASK_ID.eq(taskId),
                        HistoryTaskDao.Properties.UPLOAD_FLAG.notEq(uploadFlag))
                .orderAsc(HistoryTaskDao.Properties.TASK_STATE).list();//按照工单的任务状态升序
    }

    @Override
    public List<HistoryTask> getHistoryTasks(int userId, String taskId) {
        if (TextUtils.isEmpty(taskId)) {
            return null;
        }

        return mDaoSession.getHistoryTaskDao().queryBuilder()
                .where(HistoryTaskDao.Properties.USER_ID.eq(userId),
                        HistoryTaskDao.Properties.TASK_ID.eq(taskId))
                .orderDesc(HistoryTaskDao.Properties.REPLY_TIME).list();//按照REPLY_TIME降序
    }

    @Override
    public List<HistoryTask> getAllHistoryTaskList(int userId, int taskType) {

        return mDaoSession.getHistoryTaskDao().queryBuilder()
                .where(HistoryTaskDao.Properties.USER_ID.eq(userId),
                        HistoryTaskDao.Properties.TASK_TYPE.eq(taskType),
                        HistoryTaskDao.Properties.UPLOAD_FLAG.notEq(2))
                .orderAsc(HistoryTaskDao.Properties.TASK_STATE).list();//按照工单任务状态升序

    }

    @Override
    public synchronized boolean updateTaskReplyCreateSelf(String taskId, String serverTaskId) {
        if (TextUtil.isNullOrEmpty(taskId) || TextUtil.isNullOrEmpty(serverTaskId)) {
            return false;
        }

        mDaoSession.getDatabase().beginTransaction();

        try {
            //同时插入成功则设置事务成功
            if (updateTaskReplySelfTaskId(taskId, serverTaskId)
                    && updateMultiMediaExtend(taskId, serverTaskId)) {
                mDaoSession.getDatabase().setTransactionSuccessful();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mDaoSession.getDatabase().endTransaction();
        }
        return false;
    }

    /**
     * 更新自开单（处理工单）回复内容的taskId
     *
     * @param taskId
     * @param serverTaskId
     * @return
     */
    private synchronized boolean updateTaskReplySelfTaskId(String taskId, String serverTaskId) {
        if (TextUtil.isNullOrEmpty(taskId) || TextUtil.isNullOrEmpty(serverTaskId)) {
            return false;
        }
        HistoryTask historyTaskTemp = mDaoSession.getHistoryTaskDao().queryBuilder()
                .where(HistoryTaskDao.Properties.TASK_ID.eq(taskId))
                .where(HistoryTaskDao.Properties.TASK_STATE.eq(TASK_STATE_HANDLER)).list().get(0);
        if (historyTaskTemp == null) {
            return false;
        }

        try {
            JSONObject jsonObject = new JSONObject(historyTaskTemp.getTASK_REPLY());
            jsonObject.put("taskId", serverTaskId);
            historyTaskTemp.setTASK_REPLY(jsonObject.toString());
            JSONObject jsonObjectExtend = new JSONObject();
            jsonObjectExtend.put("serverTaskId", serverTaskId);
            historyTaskTemp.setEXTEND(jsonObjectExtend.toString());
            mDaoSession.getHistoryTaskDao().update(historyTaskTemp);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public synchronized List<HistoryTask> getTaskHandleCreateSelf(int userId, String taskId) {
        if (TextUtils.isEmpty(taskId)) {
            return null;
        }

        return mDaoSession.getHistoryTaskDao().queryBuilder()
                .where(HistoryTaskDao.Properties.USER_ID.eq(userId),
                        HistoryTaskDao.Properties.TASK_ID.eq(taskId),
                        HistoryTaskDao.Properties.TASK_STATE.eq(4))
                .list();//按照工单的任务状态升序
    }

    @Override
    public boolean updateMediaListFileTime(String taskId, long replyTime) {
        if (TextUtil.isNullOrEmpty(taskId)
                || replyTime == 0) {
            return false;
        }
        List<HistoryTask> historyTaskList = mDaoSession.getHistoryTaskDao().queryBuilder()
                .where(HistoryTaskDao.Properties.TASK_ID.eq(taskId),
                        HistoryTaskDao.Properties.REPLY_TIME.eq(replyTime))
                .list();

        if (historyTaskList == null
                || historyTaskList.size() != 1) {
            return false;
        }

        HistoryTask historyTaskResult = historyTaskList.get(0);
        historyTaskResult.resetMultiMedias();
        List<MultiMedia> multiMediaList = historyTaskResult.getMultiMedias();
        if (multiMediaList == null || multiMediaList.size() == 0) {
            return true;
        }
        for (MultiMedia multiMedia : multiMediaList) {
            multiMedia.setFILE_TIME(replyTime);
        }
        mDaoSession.getMultiMediaDao().updateInTx(multiMediaList);
        return true;
    }

    @Override
    public List<MultiMedia> deleteHistoryTask(int userId, long currentTime, long intervalTime) {
        List<HistoryTask> historyTaskList = mDaoSession.getHistoryTaskDao().queryBuilder()
                .where(HistoryTaskDao.Properties.USER_ID.eq(userId),
                        HistoryTaskDao.Properties.REPLY_TIME.le(currentTime - intervalTime))
                .list();//找出大于三十天的历史工单

        if (historyTaskList.size() <= 0) {
            return null;
        }

        List<String> taskIdList = new ArrayList<>();
        for (HistoryTask historyTask : historyTaskList) {
            taskIdList.add(historyTask.getTASK_ID());
        }

        if (taskIdList.size() <= 0) {
            return null;
        }

        if (taskIdList.size() >= DELETE_COUNT_MAX) {
            taskIdList = taskIdList.subList(0, DELETE_COUNT_MAX);
            historyTaskList = historyTaskList.subList(0, DELETE_COUNT_MAX);
        }
        //开启事务，删除三表
        mDaoSession.getDatabase().beginTransaction();
        try {
            List<Task> taskList = mDaoSession.getTaskDao().queryBuilder()
                    .where(TaskDao.Properties.TASK_ID.in(taskIdList))
                    .list();
            if (taskList != null && taskList.size() > 0) {
                mDaoSession.getTaskDao().deleteInTx(taskList);
            }

            List<MultiMedia> multiMediaList = mDaoSession.getMultiMediaDao().queryBuilder()
                    .where(MultiMediaDao.Properties.TASK_ID.in(taskIdList))
                    .list();
            if (multiMediaList != null && multiMediaList.size() > 0) {
                mDaoSession.getMultiMediaDao().deleteInTx(multiMediaList);
            }
            mDaoSession.getHistoryTaskDao().deleteInTx(historyTaskList);
            mDaoSession.getDatabase().setTransactionSuccessful();
            return multiMediaList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mDaoSession.getDatabase().endTransaction();
        }
        return null;
    }

    @Override
    public synchronized List<Task> queryDeadlineTasks(long currentTime, int userId) {
        long temp = currentTime + ONE_DAY;
        return mDaoSession.getTaskDao().queryBuilder()
                .where(TaskDao.Properties.USER_ID.eq(userId),
                        TaskDao.Properties.REPLY_DEADLINE.le(temp),
                        TaskDao.Properties.TASK_TYPE.eq(TASK_TYPE_DOWNLOAD),//工单类型我的工单
                        TaskDao.Properties.TASK_STATE.notEq(TASK_STATE_REJECT), //"退单状态"
                        TaskDao.Properties.TASK_STATE.notEq(TASK_STATE_HANDLER))//任务状态不为“4”,处理
                .orderAsc(TaskDao.Properties.ISSUE_TIME)//按照发生时间升序
                .list();
    }

    @Override
    public synchronized boolean isExistDeadlineTasks(int userId) {
        long temp = new Date().getTime() + ONE_DAY;
        List<Task> deadlineTasks = mDaoSession.getTaskDao().queryBuilder()
                .where(TaskDao.Properties.USER_ID.eq(userId),
                        TaskDao.Properties.REPLY_DEADLINE.le(temp),
                        TaskDao.Properties.TASK_TYPE.eq(TASK_TYPE_DOWNLOAD),
                        TaskDao.Properties.TASK_STATE.notEq(TASK_STATE_REJECT),//退单
                        TaskDao.Properties.TASK_STATE.notEq(TASK_STATE_HANDLER))//处理
                .list();
        if (deadlineTasks == null || deadlineTasks.size() == 0) {
            return false;
        }
        return true;
//        List<DUMyTask> duMyTasks = GreenDaoUtils.getDaoSession(ActivityUtils.getTopActivity())
//                .getDUMyTaskDao().queryBuilder()
//                .where(DUMyTaskDao.Properties.ClsxLong.lt(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
//                .list()
    }

    @Override
    public synchronized List<Word> getAllStation() {
        return mDaoSession.getWordDao().queryBuilder()
                .where(WordDao.Properties.GROUP.eq(WORD_STATION))
                .list();
    }

    @Override
    public synchronized Word getHotlineStation() {
        List<Word> result = mDaoSession.getWordDao().queryBuilder()
                .where(WordDao.Properties.GROUP.eq(WORD_STATION),
                        WordDao.Properties.PARENT_ID.eq(2))
                .list();
        if (result == null || result.size() != 1) {
            return null;
        }
        return result.get(0);
    }
}
