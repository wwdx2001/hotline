package com.sh3h.dataprovider.util;

import com.sh3h.dataprovider.data.entity.DUHistoryTask;
import com.sh3h.dataprovider.data.entity.DUMedia;
import com.sh3h.dataprovider.data.entity.request.DUCreateSelfOrder;
import com.sh3h.dataprovider.data.entity.response.DUMyTask;
import com.sh3h.dataprovider.data.entity.response.DUWord;
import com.sh3h.localprovider.greendaoEntity.HistoryTask;
import com.sh3h.localprovider.greendaoEntity.MultiMedia;
import com.sh3h.localprovider.greendaoEntity.Task;
import com.sh3h.localprovider.greendaoEntity.Word;
import com.sh3h.mobileutil.util.TextUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.sh3h.dataprovider.util.Constant.END_TIME_UTC;
import static com.sh3h.dataprovider.util.Constant.ISSUE_AREA_EX;
import static com.sh3h.dataprovider.util.Constant.ISSUE_ORIGIN_EX;
import static com.sh3h.dataprovider.util.Constant.REPLY_IMMEDIATELY;
import static com.sh3h.dataprovider.util.Constant.REPLY_TIME_IN_HISTORY;
import static com.sh3h.dataprovider.util.Constant.SERVER_TASK_ID;
import static com.sh3h.dataprovider.util.Constant.START_TIME_UTC;

/**
 * 实体转换
 * Created by dengzhimin on 2016/10/18.
 */

public class OtoO {

    public static Word toWord(DUWord word) {
        return new Word(null, word.getParentId(), word.getGroup(), word.getName(),
                word.getValue(), word.getValueEx(), word.getRemark(), null);
    }

    public static List<Word> toWord(List<DUWord> words) {
        if (words == null) {
            return null;
        }

        List<Word> wordList = new ArrayList<>();
        for (DUWord word : words) {
            wordList.add(toWord(word));
        }

        return wordList;
    }

    public static DUWord toDUWord(Word word) {
        return new DUWord(word.getGROUP(), word.getID().intValue(), word.getNAME(), word.getPARENT_ID(),
                word.getREMARK(), word.getVALUE(), word.getVALUE_EX());
    }

    public static List<DUWord> toDUWord(List<Word> words) {
        List<DUWord> duWords = new ArrayList<>();
        if (words != null && words.size() > 0) {
            for (Word word : words) {
                duWords.add(toDUWord(word));
            }
        }
        return duWords;
    }

    public static Task toTask(DUCreateSelfOrder order) {
        if (order == null) {
            return null;
        }

        String extend = add2Extend(null, REPLY_IMMEDIATELY, order.isReplyImmediately());
        extend = add2Extend(extend, ISSUE_AREA_EX, order.getIssueArea());
        extend = add2Extend(extend, ISSUE_ORIGIN_EX, order.getIssueOrigin());
        extend = add2Extend(extend, REPLY_TIME_IN_HISTORY, order.getReplyTimeInHistory());
        extend = add2Extend(extend, START_TIME_UTC, order.getStartTime());
        extend = add2Extend(extend, END_TIME_UTC, order.getEndTime());
        return new Task(null,
                order.getUserId(),
                null,
                order.getLocalTaskId(),
                order.getIssuer(),
                null,
                order.getIssueName(),
                null,
                order.getIssueTime(),
                order.getIssueAddress(),
                order.getTelephone(),
                null,
                order.getIssueType(),
                order.getIssueContent(),
                order.getReplyClass(),
                order.getReplyDeadline(),
                order.getReceviceComment(),
                order.getCardId(),
                Constant.TASK_TYPE_CREATE_SELF_ORDER,
                Constant.TASK_STATE_CREATE,
                0,
                Constant.UPLOAD_FLAG_DEFAULT,
                extend);
    }

    public static DUCreateSelfOrder toCreateSelfOrder(Task task) {
        if (task == null) {
            return null;
        }

        boolean replyImmediately = getExtendBoolean(task.getEXTEND(), REPLY_IMMEDIATELY);
        long startDateUtc = getExtendLong(task.getEXTEND(), START_TIME_UTC);
        long endDateUtc = getExtendLong(task.getEXTEND(), END_TIME_UTC);
        String issueArea = getExtendString(task.getEXTEND(), ISSUE_AREA_EX);
        String issueOrigin = getExtendString(task.getEXTEND(), ISSUE_ORIGIN_EX);
        return new DUCreateSelfOrder(task.getUSER_ID(),
                task.getTASK_ID(),
                task.getCARD_ID(),
                task.getISSUER(),
                task.getISSUE_NAME(),
                task.getISSUE_TIME(),
                task.getISSUE_ADDRESS(),
                task.getTELEPHONE(),
                task.getISSUE_TYPE(),
                task.getISSUE_CONTENT(),
                issueArea,
                issueOrigin,
                task.getREPLY_CLASS(),
                task.getREPLY_DEADLINE(),
                task.getRECEIVE_COMMENT(),
                replyImmediately,
                null,
                startDateUtc,
                endDateUtc);
    }

//    public static List<DUCreateSelfOrder> toCreateSelfOrder(List<Task> tasks) {
//        List<DUCreateSelfOrder> orders = new ArrayList<>();
//        if (tasks != null && tasks.size() > 0) {
//            for (Task task : tasks) {
//                orders.add(toCreateSelfOrder(task));
//            }
//        }
//        return orders;
//    }

    /**
     * 数据库Task转换为DUTask
     */
    public static List<DUMyTask> task2DUMyTask(List<Task> list) {
        List<DUMyTask> duTasks = new ArrayList<DUMyTask>();
        if (list != null) {
            for (Task task : list) {
                DUMyTask duMyTask = new DUMyTask();
                duMyTask.setTaskId(task.getTASK_ID());
                duMyTask.setCardId(task.getCARD_ID());
                duMyTask.setCustomerId(task.getCUSTOMER_ID());
                duMyTask.setIssueAddress(task.getISSUE_ADDRESS());
                duMyTask.setIssueContent(task.getISSUE_CONTENT());
                duMyTask.setIssueName(task.getISSUE_NAME());
                duMyTask.setIssuer(task.getISSUER());
                duMyTask.setIssueTime(task.getISSUE_TIME());
                duMyTask.setIssueType(task.getISSUE_TYPE());
                duMyTask.setMoblie(task.getMOBILE());
                duMyTask.setReceviceComment(task.getRECEIVE_COMMENT());
                duMyTask.setReplyClass(task.getREPLY_CLASS());
                duMyTask.setReplyDeadline(task.getREPLY_DEADLINE());
                duMyTask.setTaskState(task.getTASK_STATE());
                duMyTask.setTelephone(task.getTELEPHONE());
                duMyTask.setDispatchTime(task.getDISPATCH_TIME());
                duMyTask.setIssueOrigin(task.getISSUE_ORIGIN());
                duTasks.add(duMyTask);
            }
        }

        return duTasks;
    }

    /**
     * historyTask转换
     *
     * @param list
     * @return
     */
    public static List<DUHistoryTask> historyTasks2DUHistoryTask(List<HistoryTask> list) {
        List<DUHistoryTask> duHistoryTaskList = new ArrayList<DUHistoryTask>();
        if (list != null) {
            for (HistoryTask historyTask : list) {
                DUHistoryTask duHistoryTask = new DUHistoryTask();
                List<Task> taskList = historyTask.getTasks();
                if ((taskList == null) || (taskList.size() != 1)) {
                    continue;
                }
                Task task = taskList.get(0);
                duHistoryTask.setID(historyTask.getID());
                duHistoryTask.setTASK_ID(historyTask.getTASK_ID());//任务编号
                duHistoryTask.setREPLY_TIME(historyTask.getREPLY_TIME());//回复时间
                duHistoryTask.setUPLOAD_FLAG(historyTask.getUPLOAD_FLAG());
                duHistoryTask.setTASK_STATE(historyTask.getTASK_STATE());
                duHistoryTask.setTASK_TYPE(historyTask.getTASK_TYPE());
                duHistoryTask.setTASK_REPLY(historyTask.getTASK_REPLY());
                duHistoryTask.setHU_MING(task.getISSUE_NAME());
                duHistoryTask.setTELEPHONE(task.getTELEPHONE());
                duHistoryTask.setADDRESS(task.getISSUE_ADDRESS());
                duHistoryTask.setISSUER(task.getISSUER());
                duHistoryTask.setISSUE_TIME(task.getISSUE_TIME());
                duHistoryTask.setISSUE_TYPE(task.getISSUE_TYPE());
                duHistoryTask.setISSUE_ORIGIN(task.getISSUE_ORIGIN());
                duHistoryTask.setISSUE_CONTENT(task.getISSUE_CONTENT());
                duHistoryTask.setREPLY_CLASS(task.getREPLY_CLASS());
                historyTask.resetMultiMedias();
                duHistoryTask.setDuMedias(multiMedia2DUMedias(historyTask.getMultiMedias()));
                duHistoryTask.setREPLY_DEADLINE(task.getREPLY_DEADLINE());
                duHistoryTask.setTASK_UPLOAD_FLAG(task.getUPLOAD_FLAG());
                duHistoryTask.setReplyImmediately(OtoO.getExtendBoolean(task.getEXTEND(), REPLY_IMMEDIATELY));
                duHistoryTask.setServerTaskId(OtoO.getExtendString(task.getEXTEND(), SERVER_TASK_ID));
                duHistoryTaskList.add(duHistoryTask);
            }
        }

        return duHistoryTaskList;
    }

    public static List<DUMedia> multiMedia2DUMedias(List<MultiMedia> multiMediaList) {
        List<DUMedia> duMediaList = new ArrayList<DUMedia>();
        DUMedia duMedia;
        for (MultiMedia multiMedia : multiMediaList) {
            duMedia = new DUMedia();
            duMedia.setTaskId(multiMedia.getTASK_ID());
            duMedia.setTaskState(multiMedia.getTASK_STATE());
            duMedia.setTaskType(multiMedia.getTASK_TYPE());
            duMedia.setFileType(multiMedia.getFILE_TYPE());
            duMedia.setFileName(multiMedia.getFILE_NAME());
            duMedia.setFilePath(multiMedia.getFILE_URL());
            duMedia.setUploadFlag(multiMedia.getUPLOAD_FLAG());
            duMedia.setExtend(multiMedia.getEXTEND());
            duMediaList.add(duMedia);
        }
        return duMediaList;
    }


    /**
     * historyTask转换
     *
     * @param list
     * @return
     */
    public static List<HistoryTask> duhistoryTasks2HistoryTask(List<DUHistoryTask> list) {
        List<HistoryTask> historyTaskList = new ArrayList<HistoryTask>();
        HistoryTask historyTask;
        for (DUHistoryTask duhistoryTask : list) {
            historyTask = new HistoryTask();
            historyTask.setTASK_ID(duhistoryTask.getTASK_ID());//任务编号
            historyTask.setUSER_ID(duhistoryTask.getUSER_ID());
            historyTask.setTASK_TYPE(duhistoryTask.getTASK_TYPE());
            historyTask.setTASK_STATE(duhistoryTask.getTASK_STATE());
            historyTask.setTASK_CONTENT(duhistoryTask.getTASK_CONTENT());
            historyTask.setTASK_REPLY(duhistoryTask.getTASK_REPLY());
            historyTask.setREPLY_TIME(duhistoryTask.getREPLY_TIME());//回复时间
            historyTask.setUPLOAD_FLAG(duhistoryTask.getUPLOAD_FLAG());
            historyTaskList.add(historyTask);
        }
        return historyTaskList;
    }

    /**
     * Task转换
     *
     * @param task
     * @return
     */
    public static DUMyTask task2DUTask(Task task) {
        DUMyTask duMyTask = new DUMyTask();
        duMyTask.setTaskId(task.getTASK_ID());
        duMyTask.setCustomerId(task.getCUSTOMER_ID());
        duMyTask.setIssuer(task.getISSUER());
        duMyTask.setStation(task.getSTATION());
        duMyTask.setIssueTime(task.getISSUE_TIME());
        duMyTask.setReceviceComment(task.getRECEIVE_COMMENT());
        duMyTask.setCardId(task.getCARD_ID());
        duMyTask.setIssueName(task.getISSUE_NAME());
        duMyTask.setIssueAddress(task.getISSUE_ADDRESS());
        duMyTask.setMoblie(task.getMOBILE());
        duMyTask.setTelephone(task.getTELEPHONE());
        duMyTask.setExtend(task.getEXTEND());
        duMyTask.setIssueOrigin(task.getISSUE_ORIGIN());
        return duMyTask;
    }

    /**
     * DUHistoryTask转换成数据库实体HistoryTask
     *
     * @param duHistoryTask
     * @return
     */
    public static HistoryTask dUHistoryTask2HistoryTask(DUHistoryTask duHistoryTask) {
        if (duHistoryTask == null) {
            return null;
        }

        String extend = "";

        if (!TextUtil.isNullOrEmpty(duHistoryTask.getServerTaskId())) {
            extend = duHistoryTask.getServerTaskId();
        }

        return new HistoryTask(null,
                duHistoryTask.getUSER_ID(),
                duHistoryTask.getTASK_ID(),
                duHistoryTask.getTASK_TYPE(),
                duHistoryTask.getTASK_STATE(),
                duHistoryTask.getTASK_CONTENT(),
                duHistoryTask.getTASK_REPLY(),
                duHistoryTask.getREPLY_TIME(),
                duHistoryTask.getUPLOAD_FLAG(),
                extend);
    }

    /**
     * 转换数据
     *
     * @param taskListResult
     * @return
     */
    public static List<Task> dUTasksResult2Tasks(List<DUMyTask> taskListResult, int userId) {
        List<Task> taskList = new ArrayList<Task>();
        Task task;
        for (DUMyTask result : taskListResult) {
            task = new Task();
            task.setUSER_ID(userId);
            task.setCUSTOMER_ID(result.getCustomerId());
            task.setTASK_ID(result.getTaskId());
            task.setISSUER(result.getIssuer());
            task.setSTATION(result.getStation());
            task.setISSUE_NAME(result.getIssueName());
            task.setISSUE_ORIGIN(result.getIssueOrigin());
            task.setISSUE_TIME(result.getIssueTime());
            task.setISSUE_ADDRESS(result.getIssueAddress());
            task.setTELEPHONE(result.getTelephone());
            task.setMOBILE(result.getMoblie());
            task.setISSUE_TYPE(result.getIssueType());
            task.setISSUE_CONTENT(result.getIssueContent());
            task.setREPLY_CLASS(result.getReplyClass());
            task.setREPLY_DEADLINE(result.getReplyDeadline());
            task.setRECEIVE_COMMENT(result.getReceviceComment());
            task.setCARD_ID(result.getCardId());
            task.setTASK_TYPE(0);
            task.setDISPATCH_TIME(result.getDispatchTime());
            task.setTASK_STATE(result.getTaskState());
            task.setUPLOAD_FLAG(Constant.INVALID_UPLOAD);
            taskList.add(task);
        }
        return taskList;
    }


    public static String add2Extend(String extend, String key, String value) {
        if (TextUtil.isNullOrEmpty(key) || TextUtil.isNullOrEmpty(value)) {
            return null;
        }

        try {
            JSONObject jsonObject;
            if (TextUtil.isNullOrEmpty(extend)) {
                jsonObject = new JSONObject();
            } else {
                jsonObject = new JSONObject(extend);
            }
            jsonObject.put(key, value);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String add2Extend(String extend, String key, boolean value) {
        if (TextUtil.isNullOrEmpty(key)) {
            return null;
        }

        try {
            JSONObject jsonObject;
            if (TextUtil.isNullOrEmpty(extend)) {
                jsonObject = new JSONObject();
            } else {
                jsonObject = new JSONObject(extend);
            }
            jsonObject.put(key, value);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String add2Extend(String extend, String key, long value) {
        if (TextUtil.isNullOrEmpty(key)) {
            return null;
        }

        try {
            JSONObject jsonObject;
            if (TextUtil.isNullOrEmpty(extend)) {
                jsonObject = new JSONObject();
            } else {
                jsonObject = new JSONObject(extend);
            }
            jsonObject.put(key, value);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean getExtendBoolean(String extend, String key) {
        if (TextUtil.isNullOrEmpty(extend) || TextUtil.isNullOrEmpty(key)) {
            return false;
        }

        try {
            JSONObject jsonObject = new JSONObject(extend);
            return jsonObject.optBoolean(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getExtendString(String extend, String key) {
        if (TextUtil.isNullOrEmpty(extend) || TextUtil.isNullOrEmpty(key)) {
            return null;
        }

        try {
            JSONObject jsonObject = new JSONObject(extend);
            return jsonObject.optString(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long getExtendLong(String extend, String key) {
        if (TextUtil.isNullOrEmpty(extend) || TextUtil.isNullOrEmpty(key)) {
            return -1;
        }

        try {
            JSONObject jsonObject = new JSONObject(extend);
            return jsonObject.optLong(key);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
